# Contributing
This document outlines how to propose a change to AndrodIDE. By contributing to this project, you agree to abide the terms specified in [CODE OF CONDUCT](./CODE_OF_CONDUCT.md).

## Propose a change
To contribute to this project,
- Fork the repo.
- Clone the forked repo to your local machine.
- Open the project in Android Studio.
- Make your changes.
- Create a pull request.

## Note
`Java 11` is required to build the project. 

Building AndroidIDE in AndroidIDE itself is also possible. But that might require some modifications.

If you face `AAPT2` issue while building AndroidIDE in the IDE itself, specify the `android.aapt2FromMavenOverride` property in `gradle.properties` with location of `aapt2` binary from Android SDK. However, this is an **experimental** property.

## Source code format
The source code must have `2-space` indentation (soft tab).
Java source code is formatted using `google-java-format` with `GOOGLE` style formatting.
You can execute `./gradlew formatJavaSources` in terminal to format all Java source files using Google Java Format.
**This will commit the changes as well**.

## Issues & Pull requests
Use the issue/PR templates when possible. Provide a proper title and a clear description for the issue/PR. For bug reports, provide a step-by-step procedure to reproduce the issue. Always put crash logs, build outputs, etc in `code blocks`.

If you want to discuss anything about the project, you can discuss it in the [Telegram group](https://t.me/androidide_discussions).