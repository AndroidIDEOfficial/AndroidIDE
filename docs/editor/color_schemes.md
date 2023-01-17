# Editor Color Schemes

AndroidIDE [`v2.1.4-beta`](https://github.com/AndroidIDEOfficial/AndroidIDE/releases/tag/v2.1.4-beta) added limited support for custom color schemes in the editor. You can create your own color schemes and use it for AndroidIDE's editor. The color schemes are defined using the JSON syntax and are stored in the `$HOME/.androidide/ui/editor/schemes` directory. The default color scheme used is `AndroidIDE Default`.

Custom color schemes are currently used only for languages that use [`tree-sitter`](https://github.com/tree-sitter/tree-sitter) for syntax highlighting.

## File structure

The color schemes store in the schemes directory must have the following file structure : 

```
$HOME/.androidide/ui/editor/schemes
└── <scheme id> (directory)
    └── scheme.prop
```

Schemes are defined by in a directory whose name is same as the id of the scheme. For example, the `default` color scheme has the following directory structure :

```
$HOME/.androidide/ui/editor/schemes
└── default  <-- This is the scheme id
    ├── ...
    └── scheme.prop
```

## Scheme props

The `scheme.prop` file contains basic information about the color scheme such as the scheme name, version, supported languages, etc. This is the file that is first read by the IDE get information about the color scheme. The supported properties are :

```properties
# Parsed using the Java Properties parser
# <key>=<value>
# --------------------------------

# Name of the color scheme
scheme.name=<name>

# The version code of the color scheme
scheme.version=<integer>

# Whether the scheme is dark or light
scheme.isDark=<true|false>

# The JSON color scheme definition file
# This is the file which defines the color scheme
scheme.file=default.json
```

## Color scheme definition

The JSON file that is referenced by the `scheme.prop` file with the `scheme.file` property defines the color schemes for tokens. The structure of this file is as follows :

```json5
{
  "definitions": {
    "my_color": "#6f5a4a",
    "my_other_color": "#f9ddc9",
    ...
  },

  "editor": "@editor.json",
  
  "languages": [
    "@java.json",
    "@xml.json"
  ]
}
```

The root element of the JSON file must be a JSON object. The root JSON object contains two JSON objects ([`definitions`](#color-scheme-definition) and [`editor`](#editor-object)) and a JSON array ([`languages`](#languages-array)).

### Definitions object

You can define colors in the `definitions` object and then reuse these color definitions at multiple places. Every element in the `definitions` object must be a string whose value must be a HEX color code. For example :

```json5
{
  // Colors can be defined here
  // "key": "#hex color code"

  "definitions": {
    // we define the 'my_color' here
    "my_color": "#6f5a4a",
    ...
  },

  "editor": {
    // then reference 'my_color' here
    "bg": "@my_color",

    // as many times as we want!
    "line.bg": "@my_color"
  },
}
```

### Editor object

The `editor` element in the root JSON object can be a JSON object or it can be a string value which is a reference to another JSON file. If it is a reference to another JSON file, then the root element of that file must a JSON object. Either way, the JSON object defines the color scheme for the editor.

For example :

```json5
{
    "definitions": { ... },

    // this is valid
    "editor": {
        "bg": "#......"
    },

    // this is also valid
    "editor": "@editor.json"
}
```

In the second case, the `editor.json` file must have the following syntax: 

```json
{
    "bg": "#......",
    "...": "#......",

    // previously defined colors can be referred as well
    "...": "@my_color"
}
```

The keys for the editor colors can be found [here](https://github.com/AndroidIDEOfficial/AndroidIDE/blob/83b8ffb531e96bf306734332ddea2e38441d9d54/editor/src/main/java/com/itsaky/androidide/editor/schemes/internal/parser/SchemeParser.kt#L33).

### Languages array

The `languages` JSON array contains the color schemes for the supported languages. Similar to the [`editor`](#editor-object) object, the elements of the `languages` array can be a JSON object or a string value (reference to other JSON files). If the element in the array is a reference to a JSON file, then that JSON file must have a JSON object as its root element. Either way, the JSON object defines the tree-sitter metadata and styles for tree-sitter query capture names.

For example :

```json
{
    "definitions": { ... },
    "editor": { ... },

    "languages": [

        // You can define the language here
        {
            "types": [ "java" ],
            "styles": {  ... }
        },

        // or reference a file that defines the language
        "@java.json"
    ]
}
```