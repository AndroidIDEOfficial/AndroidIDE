# The code editor

AndroidIDE uses [sora-editor](https://github.com/Rosemoe/sora-editor) as it's code editor. Syntax highlighting in the editor is provided with the help of lexers generated using ANTLR4.

## Key Bindings
When working with a physical keyboard, you can use use key bindings for performing various text actions.
The editor provides support for some key bindings by default.
The currently supported key bindings are mostly similar to Android Studio/Intellij IDEA.

See the [supported key bindings](./key_bindings.md).

## Text actions

When you start selecting text in the editor, a compact text actions window is shown which allows you to perform some common actions like `Select all`, `Cut`, `Copy`, `Paste`, etc. This window is always anchored to the selection handles so that it is easily accessible.

<details>
    <summary>See screenshot</summary>
    <img src="../images/editor_text_actions.png" width="250"/>
</details>

## Code actions

Within the [text actions window](#text-actions), there is the `Code actions` item which provides some file-specific actions that you can perform. It helps you to do some frequent tasks quickly and provides diagnostics related actions if applicable.

As of now, only [Java code actions](./java_code_actions.md) are available.

<details>
    <summary>See location of code actions item in text actions window</summary>
    <img src="../images/editor_code_actions_item.png" width="250"/>
</details>