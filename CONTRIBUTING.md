# Contributing

This document outlines how to propose a change to AndroidIDE. By contributing to this project, you
agree to abide the terms specified in the [CODE OF CONDUCT](./CODE_OF_CONDUCT.md).

## Requirements

- Android Studio.
- JDK 17 (prefer the one bundled with Android Studio).

> NOTE:
>
> At the time of this writing, the stable version of Android Studio (Electric Eel) is bundled with
> JDK 11. Only Android Studio Flamingo and newer are bundled with JDK 17.

## Build the project

Open the project in Android Studio. No extra steps are needed for this.

It is possible to build AndroidIDE in AndroidIDE itself. But, as it might be a too heavy task for an
Android device to be able to build the project, Android Studio is always recommended.

## Project structure

The `:app` module has two product flavors based on CPU ABI :
- `arm64-v8a`
- `armeabi-v7a`

These flavors are used to build separate APKs with ABI-specific assets. The overall logic of the application remains the same across both flavors, but the assets differ based on the targeted CPU architecture.

The project structure is as follows :

```
- app
  - src
    - arm64-v8a
      - assets
        - <arm64-v8a specific assets>
    - armeabi-v7a
      - assets
        - <armeabi-v7a specific assets>
    - main
      - assets
        - <shared assets>
```

When modifying assets, please make sure to update the assets in the appropriate flavor-specific directories (app/src/arm64-v8a/assets or app/src/armeabi-v7a/assets).

## Split APKs, Size Reduction, and Optimization

By utilizing product flavors and split APKs based on CPU ABI, the size of the APKs have been optimized. Each APK now includes only the shared libraries and assets specific to a particular ABI, resulting in reduced file sizes for each APK.

It's important to note that there are some issues that arise when using product flavors and split APKs. Specifically, the generation of invalid APKs for certain flavors occurs due to the inclusion of incompatible assets.

For instance, let's consider the 'armeabi-v7a' flavor. Currently, two APKs are generated for this flavor:

```
app-armeabi-v7a-armeabi-v7a-debug.apk
app-armeabi-v7a-arm64-v8a-debug.apk
```

However, the second APK listed above is invalid because it includes assets only for 'armeabi-v7a' and but contains 'arm64-v78a' shared libraries. As a result, installing this APK will render it non-functional.

Likewise, for the 'arm64-v8a' flavor, an APK named `app-arm64-v8a-armeabi-v7a-debug.apk` is generated. Although this APK may be functional due to the backwards compatibility of arm64-v8a with armeabi-v7a, proper functioning of the app cannot be guaranteed.

Considering these issues, it is recommended that only the following APKs be used:

```
app-arm64-v8a-arm64-v8a-debug.apk
app-armeabi-v7a-armeabi-v7a-debug.apk
```

## Source code format

- Indents : 2-space
- Java : `GoogleStyle`. Either use `google-java-format` or
  import [this](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)
  code style.
- Kotlin: Use [`ktfmt`](https://plugins.jetbrains.com/plugin/14912-ktfmt) IntelliJ Plugin and set
  the code style to `Google (internal)`
  . [`Learn more`](https://github.com/facebook/ktfmt#intellij-android-studio-and-other-jetbrains-ides)
  .
- XML : Default Android Studio formatter with 2-space indentations.

## Propose a change

Before proposing a change, it is always recommended to open an issue and discuss it with the
maintainers.

**The `main` branch is protected and requires all the commits to be signed with your GPG key and the
commit history to be linear.**
See [protected branches](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/defining-the-mergeability-of-pull-requests/about-protected-branches)
.

To contribute to this project,

- Fork the repo.
- Clone the forked repo to your local machine.
- Open the project.
- Make your changes.
- Create a pull request. Give a proper title and description to your PR.

## Issues & Pull requests

Use the issue/PR templates whenever possible. Provide a proper title and a clear description of the
issue/PR. For bug reports, provide a step-by-step procedure to reproduce the issue. Always put
things like crash
logs, and build outputs in `code blocks`.

## Contact us

If you want to discuss anything about the project, you can discuss it in
the [Telegram group](https://t.me/androidide_discussions).
