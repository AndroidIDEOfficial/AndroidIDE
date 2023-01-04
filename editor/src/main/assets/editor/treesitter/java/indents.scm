; Query for computing indents in tree-sitter language
; Capture names can be 'indent' or 'outdent'. Any other capture name will be ignored
; indent - increments indentation
; outdent - decrements indentation

[
  "("
  "["
  "{"
  ] @indent

[
  (class_body)
  (enum_body)
  (interface_body)
  (constructor_declaration)
  (constructor_body)
  (block)
  (switch_block)
  (array_initializer)
  (argument_list)
  (formal_parameters)
  ] @indent

(expression_statement (method_invocation) @indent)

[
  ")"
  "]"
  "}"
] @outdent