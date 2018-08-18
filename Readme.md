[![CircleCI](https://circleci.com/gh/gatheringhallstudios/MHWorldDatabase/tree/master.svg?style=svg)](https://circleci.com/gh/gatheringhallstudios/MHWorldDatabase/tree/master)

# MHWorldDatabase

The main repository for the open source [MHWorld Database](https://play.google.com/store/apps/details?id=com.gatheringhallstudios.mhworlddatabase) Android app. This project displays data from the [MHWorldData](https://github.com/gatheringhallstudios/MHWorldData) project.

The project's Trello board, containing todo's and bugs, is available [here](https://trello.com/b/xJhj9ChK/mhworld-database)

If you wish to chat with us, we also have a [discord server](https://discord.gg/k5rmEWh)

## Build instructions

This project depends on the [MHWorldData](https://github.com/CarlosFdez/MHWorldData) project. Clone it and run the build command to get the latest version of the database. The file will appear in the root directory of that project as `mhw.db`.

- Install Android Studio, and use it to open this project directory
- Copy the mhw.db file to `app\src\main\assets\databases`
- Install the version 28 Android SDK via the SDK manager
- Compile and run the project. You may need to create an emulator using the AVD

## Contributing

If you want to contribute, there are a few ways. Feel free to create an issue, a PR, or to [join us on our discord](https://discord.gg/k5rmEWh) and talk about it.

This project internally receives data from the [MHWorldData](https://github.com/gatheringhallstudios/MHWorldData) project, which is currently incomplete. We could use translations and additional data.

Additionally, you can look over our [Trello](https://trello.com/b/xJhj9ChK/mhworld-database) and see what needs doing.

## Architecture

This project is built using the Android Architecture Components in Kotlin. We use ViewModels to maintain state during configuration changes, and Android Room with LiveData to push data. The data is a stored as a SQL file under assets, and copied to the Android data folder before querying.
