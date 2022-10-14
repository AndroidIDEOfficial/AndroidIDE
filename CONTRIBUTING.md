# Contributing

This document outlines how to propose a change to AndroidIDE. By contributing to this project, you
agree to abide the terms specified in the [CODE OF CONDUCT](./CODE_OF_CONDUCT.md).

## Requirements

- Android Studio.
- JDK 11 (prefer the one bundled with Android Studio). Newer versions are not recommended.

## Build the project

Open the project in Android Studio. No extra steps are needed for this.

It is possible to build AndroidIDE in AndroidIDE itself. But, as it might be a too heavy task for an Android device to be able to build the project, Android Studio is always recommended.

## Source code format

The source code must have `2-space` indentation (soft tab). Java source code is formatted
using `google-java-format` with `GOOGLE` style formatting.

You can
execute `./gradlew formatJavaSources` in terminal to format all Java source files using Google Java
Format.
**This will commit the changes as well**.

## Propose a change

Before proposing a change, it is always recommended to open an issue and discuss it with the maintainers.

**The `main` branch is protected and requires all the commits to be signed with your GPG key and the commit history to be linear.** See [protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches).

To contribute to this project,

- Fork the repo.
- Clone the forked repo to your local machine.
- Open the project.
- Make your changes.
- Create a pull request. Give a proper title and description to your PR.

## Issues & Pull requests

Use the issue/PR templates whenever possible. Provide a proper title and a clear description of the
issue/PR. For bug reports, provide a step-by-step procedure to reproduce the issue. Always put things like crash
logs, and build outputs in `code blocks`.


## Contact us
If you want to discuss anything about the project, you can discuss it in
the [Telegram group](https://t.me/androidide_discussions).
