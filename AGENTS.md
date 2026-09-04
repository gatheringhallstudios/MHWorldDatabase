# Agent instructions — MHWorldDatabase

Applies to any coding agent working in this repo (Claude Code, Codex, Cursor, …).
`CLAUDE.md` is a symlink to this file.

Android app, Kotlin + Room, single module `:app`. Data ships as a prebuilt SQLite file at
`app/src/main/assets/databases/mhw.db` (read-only, versioned by `DATABASE_VERSION` in
`app/build.gradle`). User data — bookmarks and workshop sets — lives in a separate Room
database, `ApplicationDatabase`, with hand-written migrations in
[AppDatabase.kt](app/src/main/java/com/gatheringhallstudios/mhworlddatabase/data/AppDatabase.kt).

## Commit messages

**Keep them short. Under 300 characters total, body included.** A one-line subject is the
normal case; add a body only when the *why* is genuinely not obvious from the diff, and then
keep it to a sentence or two.

- Imperative subject, ~72 chars or less: `Fix tab contents going blank after recycling`.
- No bullet-point inventories of what changed — the diff already says that.
- No "Left as-is" / "Considered and rejected" sections. If a caveat needs recording, it goes
  in a code comment or an issue, not the commit message.
- Don't restate the code. `Observe with viewLifecycleOwner so re-registration takes` is
  useful; three paragraphs on how `FunctionAdapter.equals` works is not.
- Trailers (`Co-Authored-By:`, etc.) don't count toward the 300.

Some older commits in `master` are far longer than this. They're the exception, not the model.

Check before committing:

```bash
git log -1 --format='%B' | wc -c
```

## Release smoke test

Run this before every Play Store upload — after the version bump, on the artifact you are
actually going to ship. It is a crash-and-obvious-breakage pass, not a QA cycle; budget
15–20 minutes. For translation-specific coverage see
[localization-smoke-test.md](docs/localization-smoke-test.md).

### 0. Pre-flight (no device)

```bash
./gradlew lint test                                   # what CI runs
git diff --stat HEAD~1 -- app/build.gradle            # versionCode + versionName bumped?
```

Confirm all four are consistent for the new release:

| Thing | Where |
| --- | --- |
| `versionCode` / `versionName` | `app/build.gradle` |
| In-app changelog entry | `app/src/main/res/raw/changelog.xml` |
| Play Store release notes | `fastlane/metadata/android/en-US/changelogs/<versionCode>.txt` |
| `DATABASE_VERSION` | `app/build.gradle` — bump **only** if `assets/databases/mhw.db` changed |

Then build the real thing (signed, `minifyEnabled false`, but still the release variant):

```bash
bundle exec fastlane bundle_release      # app/build/outputs/bundle/release/app-release.aab
```

Install the bundle on device with `bundletool`, or `./gradlew assembleRelease` and install
that APK — do not smoke test a debug build. Debug sets `R.bool.DEBUG` true, which exposes
the "Debug Only" drawer section and changes behaviour.

### 1. Upgrade install (do this first, and don't skip it)

The most expensive bug this app can ship is wiping user data on update. Test the upgrade
path *before* wiping the device.

```bash
PKG=com.gatheringhallstudios.mhworlddatabase
# starting from the currently published version already installed, with bookmarks
# and at least one workshop set saved:
adb install -r app/build/outputs/apk/release/app-release.apk    # -r, NOT uninstall
```

Verify after launch:

- Bookmarks list still holds everything it held before.
- Workshop sets are intact, with their bound armor and decorations.
- If `DATABASE_VERSION` was bumped, the first launch is slower (the asset DB is re-copied)
  but still succeeds, and monster/item names render — not blank rows.

A migration that throws here shows up as a crash on first launch. Check
`adb logcat -b crash` before deciding it passed.

### 2. Fresh install

```bash
adb uninstall $PKG && adb install app/build/outputs/apk/release/app-release.apk
adb logcat -b crash -c
adb shell am start -n $PKG/.SplashActivity
```

Cold start must reach the monster list with the DB populated.

### 3. Navigation sweep

Open every drawer entry and confirm it loads a populated list — not empty, not spinning:

Monsters · Weapons · Armor · Workshop · Quests · Items · Item Crafting · Skills ·
Decorations · Charms · Tools · Kinsects · Locations · Bookmarks · Settings · Changelog · About

Then go one level deep on the screens with the most branching:

1. **Monster detail** — Summary tab, then swipe to Damage/Rewards, then swipe *back* to
   Summary. Content must still be there. (Tab recycling has regressed here before —
   see 396b58f8.)
2. **Weapon detail** — pick a Hunting Horn, a Bow, and a Bowgun. Each hits a different
   `ViewStub` branch that plain blades don't.
3. **Workshop** — create a set, open the armor selector, bind a piece, add a decoration,
   check the Summary tab totals, then delete the set.
4. **Bookmarks** — bookmark something, back out, confirm it appears; unbookmark it.
5. **Search** — query a monster name, tap a result.
6. **Changelog** — the new release is at the top with the right version name.

After each: `adb logcat -d -b crash | grep -c "FATAL EXCEPTION"` must be `0`.

### 4. Settings and locale

- Settings opens; both **App Language** and **Data Language** pickers open and list entries.
- Switch data language to one non-English locale, confirm monster names change, switch back.
- Rotate the device on a monster detail screen — state survives, no crash.

### 5. Release-only checks

Things a debug build won't catch:

- No "Debug Only" section in the drawer (`R.bool.DEBUG` is false in release).
- The app is signed with the release key: `apksigner verify --print-certs <apk>`.
- App size hasn't jumped unexpectedly — the DB asset is ~11 MB and dominates the bundle.

### Reporting

Anything with a `FATAL EXCEPTION` blocks the release; capture the full stack trace and the
exact screen. A blank or empty-list screen that is populated in the shipped version also
blocks it. Cosmetic translation gaps do not — the localization doc lists the known ones.
