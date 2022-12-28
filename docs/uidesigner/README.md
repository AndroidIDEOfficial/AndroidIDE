# The UI Designer

The UI Designer helps you visually design XML layouts by simply dragging and dropping widgets into the workspace.

This document briefly every element of the UI Designer workspace.

## The workspace

Your XML code is parsed and inflated by AndroidIDE's `LayoutInflater API` and then shown in the workspace. You can drag and drop the inflated views and widgets to move them, change their attributes, add new views or delete the existing ones.

<details>
  <summary>Workspace</summary>
  <img src="../images/uidesigner/workspace.png" width="250"/>
</details>

<details>
  <summary>Drag-n-Drop</summary>
  <img src="../images/uidesigner/drag-n-drop.png" width="250"/><br>
  A placeholder view is used to indicate the drop position of the widget when you start dragging.
</details> <br>

### Add new views

To add new views into the workspace:
- Open the left drawer which shows a list of supported views and layouts.
- Long click on the list items to start the drag. Drop them into the workspace to add to the layout.

<details>
  <summary>Widget drawer</summary>
  <img src="../images/uidesigner/widgets_drawer.png" width="250"/>
</details><br>

### Edit view attributes

Clicking on any inflated view opens the view info sheet which contains information about the view.

The sheet contains two buttons at the header :

- `Add` - Shows a list of attributes that you can add to the selected view.
- `Delete` - Deletes the selected view.

It also contains the list of attributes that have been applied to the selected view.

- Clicking on any attribute opens the value editor which you can use to edit the value of the selected attribute.
- Clicking on the 'Delete' button next to the attribute deletes that attribute from the view.

<details>
  <summary>View info sheet</summary>
  <img src="../images/uidesigner/view_info.png" width="250"/>
</details><br>
