## ANTLR4 Lexers for AndroidIDE

The grammar files are located in the `antlr` directory. The `LexerGeneratorPlugin` (defined in
the `:build-logic:ide` module) is applied to this module (`:lexers`) which generates the Java lexers
and parsers using these grammars.

This module is used to store only the generated lexers. As all of the Java files in this module are
auto-generated, the `java` has been added to gitignore.