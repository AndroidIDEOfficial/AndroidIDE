# Build Actions
Gradle Build Actions.

## Build Tasks
To set `gradle options`, go to `Preferences > Build & Run > Additional Gradle commands`.
### Build
Runs `./gradlew build [options..]`<br/>
The `build` task is to designate assembling all outputs and running all checks.
### assembleDebug
To initiate a debug build, invoke the `assembleDebug` task:<br/>
Runs `./gradlew assembleDebug [options..]`
### assembleRelease
To initiate a release build, invoke the `assembleRelease` task:<br/>
Runs `./gradlew assembleRelease [options..]`
### Create AAB
Runs `./gradlew bundle [options..]`<br/>
Generates an app bundle (.aab file).
### Clean
Runs `./gradlew clean [options..]`<br/>
You can delete the contents of the build directory using the `clean` task.
### Clean & Build
Runs `./gradlew clean build [options..]`<br/>
At first runs `clean` task then `build` task.

## Lint Tasks
The lint tool checks for structural code problems that could affect the quality and performance of your Android application.
### Lint
Runs Lint for all build variants. [`./gradlew lint`]
### lintRelease
Runs Lint for release variant. [`./gradlew lintRelease`]
### lintDebug
Runs Lint for debug variant. [`./gradlew lintDebug`]
