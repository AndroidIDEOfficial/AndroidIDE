; Query for computing indents in tree-sitter language
; Capture names can be 'indent' or 'outdent'. Any other capture name will be ignored
; indent - increments indentation
; outdent - decrements indentation

(class_body
  "{" @indent
  "}" @outdent)

(enum_body
  "{" @indent
  "}" @outdent)

(interface_body
  "{" @indent
  "}" @outdent)

(constructor_body
  "{" @indent
  "}" @outdent)

(block
  "{" @indent
  "}" @outdent)

(switch_block
  "{" @indent
  "}" @outdent)

(array_initializer
  "{" @indent
  "}" @outdent)

(formal_parameters
  "(" @indent
  ")" @outdent)

(argument_list
  "(" @indent
  ")" @outdent)

; (expression_statement (method_invocation) @indent)