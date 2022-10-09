# Editor
Editor is one of the the main components of an IDE.
<!-- AndroidIDE uses Sora as it's code editor. -->

## [Key Bindings](./key-bindings.md)
Keyboard shortcuts you can use in the editor.

## Sidebar
#### 1st group
- `Terminal`: Open terminal with project root directory.
- `Close this project`: Close the project and editor.
#### 2nd group
Here is some links and help dialog.
#### 3rd group
- `IDE Preferences`: Open Preferences screen.
- `Share with Friends`: Share AndroidIDE.

## Options Menu
Top right options and three-dot(`⋮`) menu.
- `Quick run(▷)`: Runs `assembleDebug` and installs the debug apk (if permission is given).
- `Run(▶)`: Various Gradle actions. Check [here](./actions.md).
- `Find`:
    - `Find in file`: Find and replace in the file which is opened in the editor.
    - `Find in project`: Find in all the files in  the selected project modules. <br/>&emsp; You can also filter the files with their extensions seperated by '|' .
- `File tree`: Open the file tree of the project. <br/>&emsp; You     can also open it by swiping left from edge.
- `Gradle daemon status`: Know the Gradle daemon status.
- `Cancel build`: If any build action is running, cancel it.
- `Sync project`: Sync the project and it's dependencies.

## [BottomSheet](bottomsheet.md)
Swipe up to open the bottomsheet. You can find the
[Build Output](./bottomsheet.md#build-output),
[App Logs](./bottomsheet.md#app-logs),
[IDE Logs](./bottomsheet.md#ide-logs),
[Diagnostics](./bottomsheet.md#diagnostics),
[Search results](./bottomsheet.md#search-results) here.
