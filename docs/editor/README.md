# Editor UI

Whenever you create a new project or open an existing project, you're presented with the editor screen. This page guides you through the various elements of the Editor UI.

## Options Menu

Top right options and three-dot(`⋮`) menu.

- `Quick run(▷)`: Runs `assembleDebug` and installs the debug apk (if permission is given).
- `Run tasks`: Allows you to select and run various Gradle tasks. Check [here](./build_actions.md#readme).
- `Find`:
    - `Find in file`: Find and replace in the file which is opened in the editor.
    - `Find in project`: Find in all the files in  the selected project modules. You can also filter the files with their extensions seperated by '|' .
- `File tree`: Open the file tree of the project. You can also open it by swiping left from edge. [Learn more](#the-file-tree).
- `Gradle daemon status`: Know the Gradle daemon status.
- `Cancel build`: If any build action is running, cancels it.
- `Sync project`: Sync the project. The IDE asks you to sync the project when there are any changes in the project configuration files. However, you could use this option to sync the project manually.

## The file tree

The file tree shows your project files in a tree format. You can access the file tree sliding the right navigation drawer to the left or by clicking the 'File tree` option in the options menu. You can simply click any file to open it in the editor. Long clicking on any file item shows you some actions that you can perform on that given file/folder.

## Bottom sheet

The bottom sheet provides information about the Gradle builds along with app logs, IDE logs and much more. [Learn more](./bottom_sheet.md).

## Code editor

The code is where you work with your project files. It provides various actions based on the type of files. [Learn more](./code_editor.md).
