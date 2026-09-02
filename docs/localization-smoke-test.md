# Localization smoke test — agent instructions

Light manual-equivalent smoke pass over the app's translations, meant to be driven by an
agent with `adb` against a running emulator. Not a substitute for unit or UI tests; the goal
is to catch locale-specific crashes and obviously-broken screens quickly.

Budget roughly 2 minutes per locale. Run the static pre-check first — it is seconds long and
catches the highest-severity class of bug on its own.

## What "a locale" means here

Two independent axes. Test them separately; bugs live in both.

| Axis | Controls | Source | Set via |
| --- | --- | --- | --- |
| **App locale** | UI strings (buttons, titles, settings) | `res/values-*/strings.xml` | System per-app picker, or in-app Settings → App Language |
| **Data locale** | Monster/item/weapon names and descriptions | `mhw.db`, `*_text.lang_id` columns | In-app Settings → Data Language |

The app ships 12 UI locales (`en ar de es fr it ja ko pl pt ru zh-CN`) and the database
carries 12 languages. They are *not* the same set: UI `zh-rCN` is Simplified Chinese, while
the database's `zh` is 繁體中文 (Traditional). Treat that as known, not a bug to re-report.

When no data locale is chosen, it follows the app locale (`AppSettings.defaultDataLocale`),
falling back to `en`. So changing the app locale alone usually changes both.

## Static pre-check (no device)

A format-specifier mismatch between a translation and the base string is a **crash**, not a
cosmetic issue: `String.format` throws on the malformed one. Run this before touching a device.

```bash
cd app/src/main/res && python3 - <<'PY'
import re, glob
def parse(f):
    spec = {}
    for m in re.finditer(r'name="([^"]+)"[^>]*>(.*?)</string>', open(f, encoding='utf-8').read(), re.S):
        # record every string, even with no specifiers -- a *dropped* one is the dangerous case
        spec[m.group(1)] = sorted(re.findall(
            r'%(?:\d+\$)?[-+ #0]*\d*(?:\.\d+)?([a-zA-Z%])', m.group(2)))
    return spec
base = parse('values/strings.xml')
for f in sorted(glob.glob('values-*/strings.xml')):
    loc = parse(f)
    # only compare strings this locale actually overrides; absent ones fall back safely
    bad = [f"{n}: base={base[n]} loc={loc[n]}"
           for n in loc if n in base and loc[n] != base[n]]
    print(f"{f.split('/')[0]:<14} {'OK' if not bad else '; '.join(bad)}")
PY
```

Expected: every locale `OK`. Anything else is a crash waiting on whichever screen uses that
string — fix the translation, do not work around it in code.

## Device setup

```bash
PKG=com.gatheringhallstudios.mhworlddatabase
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell cmd locale set-app-locales $PKG --locales de-DE   # any BCP-47 tag from the list
adb shell cmd locale get-app-locales $PKG                   # confirm it took
```

Reset when finished: `adb shell cmd locale set-app-locales $PKG --locales ""`.

Screenshots need an explicit display id on multi-display emulators:

```bash
D=$(adb shell dumpsys SurfaceFlinger --display-id | head -1 | grep -o '[0-9]\{15,\}')
adb shell screencap -p -d $D /sdcard/s.png && adb pull /sdcard/s.png ./shot.png
```

Taps also need `-d 0`: `adb shell input -d 0 tap X Y`. Prefer locating targets from
`adb shell uiautomator dump` bounds over hardcoded coordinates — text positions shift per
locale, which is the whole point of this test.

## Per-locale pass

For each locale under test, clear the crash buffer first (`adb logcat -b crash -c`), then walk:

1. **Cold start** → monster list populates.
2. **Monster detail** → Summary tab, then Damage tab.
3. **Weapons** → one tree list → one weapon detail. Prefer Hunting Horn, Bow, or a Bowgun:
   they exercise the weapon-specific `ViewStub` branches that plain blades do not.
4. **Workshop** → create a set → open the armor selector → bind one piece → Summary tab.
5. **Settings** → confirm both language pickers open and list entries.

After each step: `adb logcat -d -b crash | grep -c "FATAL EXCEPTION"` must stay `0`.

Verify you are actually where you think you are before judging a screen — dump the hierarchy
and assert on a string you expect. A tap that silently missed will otherwise read as a pass.

## What counts as a failure

Report these:

- Any `FATAL EXCEPTION`. Capture the full stack trace.
- A screen that renders blank or loses a section that is populated in English.
- Text overlapping, clipped mid-word, or overflowing its row. German and Russian are the
  usual offenders — they run long.
- **Arabic only:** layout not mirrored (back arrow, drawer, list icons should flip). The
  codebase still has `RtlHardcoded` and `RtlSymmetry` lint warnings, so mirroring is not
  guaranteed and is worth looking at closely.
- Data still in English after setting a data language — that is a query or settings bug, not
  a translation gap.

Do **not** report these — they are known and tracked:

- English text appearing inside a translated screen. `MissingTranslation` is deliberately
  disabled in `app/build.gradle`, and translations are incomplete: `values-de`, for instance,
  has ~71 of 349 strings still identical to English. Only report it if a *whole screen* is
  English in a locale that is otherwise well covered.
- The `App Language` preference itself showing in English. Its strings
  (`preference_app_language*`) exist only in `values/` and have not been translated yet.
- Simplified/Traditional Chinese mismatch between UI and data, described above.

## Coverage guidance

A full 12-locale sweep is rarely worth it. Unless asked for exhaustive coverage, run:

- **`de`** — long strings, most likely to overflow layouts.
- **`ar`** — the only RTL locale; catches mirroring bugs nothing else will.
- **`ja`** — CJK glyphs and no word spacing; catches font and wrapping issues.
- **`en`** — control, to confirm a failure is locale-specific and not a general regression.

Add `ru` if you have budget; it is the other long-string locale.
