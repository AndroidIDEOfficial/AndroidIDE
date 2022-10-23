# Build Actions

The IDE provides access to various tasks which you can execute to build your project.
Below is the list of tasks that are accessible from the IDE with their brief description
and the actual Gradle tasks that the IDE executes.

## Build Tasks

Tasks to build your project.

To set `gradle options`, go to `Preferences > Build & Run > Additional Gradle commands`.

| Task             | Description                                                                                                        |
| ---------------- | ------------------------------------------------------------------------------------------------------------------ |
| Build            | Runs `./gradlew build [options..]` The `build` task is to designate assembling all outputs and running all checks. |
| Assemble Debug   | To initiate a debug build, invoke the `assembleDebug` task. Runs `./gradlew assembleDebug [options..]`             |
| Assemble Release | To initiate a release build, invoke the `assembleRelease` task. Runs `./gradlew assembleRelease [options..]`       |
| Create AAB       | Runs `./gradlew bundle [options..]`, Generates an app bundle (.aab file).                                          |
| Clean            | Runs `./gradlew clean [options..]`. You can delete the contents of the build directory using the `clean` task.     |
| Clean & Build    | Runs `./gradlew clean build [options..]`. At first runs `clean` task then `build` task.                            |

## Lint Tasks
The lint tool checks for structural code problems that could affect the quality and performance of your Android application.

| Task           | Description                                              |
| -------------- | -------------------------------------------------------- |
| `Lint`         | Runs Lint for all build variants. [`./gradlew lint`]     |
| `Lint release` | Runs Lint for release variant. [`./gradlew lintRelease`] |
| `Lint Debug`   | Runs Lint for debug variant. [`./gradlew lintDebug`]     |
