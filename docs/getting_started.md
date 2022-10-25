# Getting Started

This guide shows how to get started with AndroidIDE and build an Android application. Before proceeding, please make sure that you have installed the build tools. If not, see [the installation guide](./installation.md#readme).

## Creating a new project

Creating a new project is simple. Simply open AndroidIDE and click on the `Create project`  button.

There are project templates available which setup some basic things for you. You can choose a template of your choice.

After you select the project template, you'll be asked to enter the details about your application. Enter the application name and it's package name. You could also choose the directory where the project will be created along with the application's minimum and target SDK.

Once you're done with above things, click the 'Create project' button.

## Working with an existing project

To open an existing project in AndroidIDE, click the `Open existing project` in the main activity. This opens the SAF file picker where you can choose the project directory you want to open.

Gradle based Android projects are supported. However, it is currently not possible to build projects that use older versions of the Android Gradle Plugin. To be able to build a project in AndroidIDE, your project must use Android Gradle Plugin `v7.2.0` or newer.

For people trying to build projects developed with other older IDEs, you might need to update your build scripts before opening the project in AndroidIDE.

## More info

Once you create a new project or open an existing project, the IDE will open the [editor](./editor) and will start to sync the project. If this is the first time you're building a project on AndroidIDE, it might take a while to set things up. **_It totally depends on your internet connection_**. To see the build progress, [swipe up the build status bar](./editor/bottomsheet.md) at the bottom of your screen.

Once the sync is successful, you can start working on your project or simply press the [`Run`](./editor/actions.md) button in the [toolbar](./editor#options-menu) to build and install your application.
