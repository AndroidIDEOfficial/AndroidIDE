; Brackets pattern for editor
; Note that you should match both open node and close node in one pattern
; In sora-editor, capture named 'editor.brackets.open' is regarded as open symbol node, capture named 'editor.brackets.close' is regarded as close symbol node.
(block
  "{" @editor.brackets.open
  "}" @editor.brackets.close)

(expression
  "(" @editor.brackets.open
  ")" @editor.brackets.close)

(array_initializer
  "{" @editor.brackets.open
  "}" @editor.brackets.close)