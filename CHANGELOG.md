# AndroidIDE Changelog

This is the changelog of all the releases of AndroidIDE.

## **v2.0.1-beta** (in development)

### Important notes
<!--Breaking changes and other important stuff here-->
- As 64-bit installation does not work on Android 12 (due to [restriction by the OS](https://source.android.com/devices/tech/debug/tagged-pointers)), 32-bit build tools need to be installed when installing AndroidIDE on devices running Android 12.

### Additions
<!--New features-->
- Code formatter for Java source code.
- Support for
  [`GestureOverlayView`](https://developer.android.com/reference/android/gesture/GestureOverlayView),
  [`ToggleButton`](https://developer.android.com/reference/android/widget/ToggleButton),
  [`Switch`](https://developer.android.com/reference/android/widget/Switch),
  [`GridLayout`](https://developer.android.com/reference/android/widget/GridLayout) in layout
  inflater.
- Preference to disable use of `TYPE_TEXT_VARIATION_VISIBLE_PASSWORD` flag in editor.
- Automatically trigger completion when attributes are inserted from completion window.
- Code actions
- `DocumentsProvider` for providing access to files in `HOME` directory.

### Removals
<!--Things that were removed-->
- Removed initial view attributes from view tags completion in XMLCompletionProvider.
- Vertical text action window has been removed.

### Fixes
<!--Bug fixes, etc-->
- Cash in attribute editors when adding an attribute.
- Crash when selecting diagnostic item from diagnostics list.
- Some diagnostic items are not shown in diagnostic window (#96).
- Diagnostics were updated late in the editor.
- Popup windows created by editor are not dismissed even when the file is closed.
- No build output when activity is recreated (#121).
- Crash in UI Designer when activity is recreated (#124).

### Improvements
<!--Things that were improved and worth mentioning-->
- Do not import classes from `java.lang` package when completion items are selected.
- XML completion provider now uses 'Match completions in lower case' preference.
- Dismiss all windows shown by editor when file tab is unselected
- Updated templates to work with Android 12 (#144).
- Updated Chinese translation. Thanks to @mikofe (#128).
- Updated French translation. Thanks to @Se-Lyan (#136).
- Updated German translation (#144).

## **v2.0.0-beta**
AndroidIDE v2.0.0-beta is here with new features and improvements.

### Important Notes
- This is a pre-release and is not so stable. There are some features that are partially
  implemented.
- The minimum SDK has been raised to `26` (`Android O`).

### Additions
- XML Layout Designer with ability to preview XML drawables.
- Added packages for terminal.
- libGDx project template. Thanks to @Smooth-E (#81).
- Kotlin project template. Thanks to @BanDroid (#60).
- Preference to switch between `bash` and `/system/bin/sh` in terminal.
- Preference for matching partial names in java completion.
- Preference to choose between `floating window text actions` and `text actions in ActionMode`.
- Preference to enable/disable font ligatures.
- Show process output when build tools installation fails.
- Ability to expand selection in supported languages.
- Ask user to save unsaved files before closing project.
- Hindi translation. Thanks to @Premjit-Chowdhury (#70, #127).
- German translation. Thanks to @MarvinStelter.
- Indonesian translation. Thanks to @BanDroid (#54, #57, #60, #72, #125).
- Chinese translation. Thanks to @Rosemoe (#56, #73, #104) and @mikofe (#118, #119, #123).
- French translation. Thanks to @Se-Lyan (#79).
- Russian translation. Thanks to @Smooth-E (#97).

### Removals
- Autosave of files is removed. However, a preference has been added for the same which, when
  enabled, saves the files automatically before close.

### Fixes
- File does not close and crashes if reopened and closed again.
- Flickering of whole auto completion window.
- Flickering of completion item due to API info.
- Build tools installation fails if `JAVA_HOME` does not exist.
- Invalid syntax highlight for method invocations in java.
- Package private classes not included in java completion.
- IDE crashes when `Paste` option is selected in terminal and no clip is available.
- Fixed a bug due to which the IDE was not able to open empty files.
- `LogSender` class not included in JLS workspace.

### Improvements
- Show crash report when the IDE crashes.
- The Java language server is now embedded in the IDE itself.
- The AndroidIDE Gradle plugin is now packaged with the IDE.
- Show name of opened project.
- Show signature help in window instead of using a `TextView`.
- The java completion provider now uses a custom JDK runtime image with Android framework classes
  and is no more dependent on JDK installation.
- Disable `Save` option when all files are saved.
- Allow installation of files from file tree.

### New Contributors
* @BanDroid
* @Rosemoe
* @Premjit-Chowdhury
* @Se-Lyan
* @Smooth-E
* @ansh2166
* @mikofe
