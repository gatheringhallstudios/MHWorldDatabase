# MHWorldDatabase

The main repository for the open source [MHWorld Database](https://play.google.com/store/apps/details?id=com.gatheringhallstudios.mhworlddatabase) Android app. This project displays data from the [MHWorldData](https://github.com/gatheringhallstudios/MHWorldData) project.

The project's Trello board, containing todo's and bugs, is available [here](https://trello.com/b/xJhj9ChK/mhworld-database)

If you wish to chat with us, we also have a [discord server](https://discord.gg/k5rmEWh)

[<img src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
     alt="Get it on F-Droid"
     height="80">](https://f-droid.org/packages/com.gatheringhallstudios.mhworlddatabase/)
[<img src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png"
     alt="Get it on Google Play"
     height="80">](https://play.google.com/store/apps/details?id=com.gatheringhallstudios.mhworlddatabase)

## Build instructions

- Install Android Studio, and use it to open this project directory
- Install the version 36 Android SDK via the SDK manager
- Use JDK 17 for Gradle (Android Studio: Settings > Build Tools > Gradle > Gradle JDK). Newer JDKs fail to compile the build script.
- Compile and run the project via Android Studio. You may need to create an emulator using the AVD

Debug builds install as a separate `.dev` app, so they don't replace a copy installed from the Play Store.

## CI

CI runs on [Bitrise](https://bitrise.io) and is defined in [bitrise.yml](bitrise.yml). Pull requests run lint, unit tests and a debug build; pushing a `v*` tag builds the signed release, which is published to Google Play through Bitrise Release Management. See [AGENTS.md](AGENTS.md) for the full workflow list and release steps.

## Contributing

If you want to contribute, there are a few ways. Feel free to create an issue, a PR, or to [join us on our discord](https://discord.gg/k5rmEWh) and talk about it.

This project internally receives data from the [MHWorldData](https://github.com/gatheringhallstudios/MHWorldData) project, which is currently incomplete. We could use translations and additional data.

Additionally, you can look over our [Trello](https://trello.com/b/xJhj9ChK/mhworld-database) and see what needs doing.

## Architecture

This project is built using the Android Architecture Components in Kotlin. We use ViewModels to maintain state during configuration changes, and Android Room with LiveData to push data. The data is a stored as a SQL file under assets, and copied to the Android data folder before querying.
