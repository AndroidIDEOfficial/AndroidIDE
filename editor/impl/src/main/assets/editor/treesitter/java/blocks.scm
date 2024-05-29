; Code block patterns for editor
; Capture names for scopes does not matter much in sora-editor implementation, you may use 'abc', 'test.xyz', etc.
; General, editor considers the captured node's region as code block region.
; However, capture name with '.marked' suffix is special. The last terminal node's start position in the capture will be the end position of the block
; 'terminal node' refers to a node without children

(class_declaration
  body: (_) @scope.marked)

(record_declaration
  body: (_) @scope.marked)

(enum_declaration
  body: (_) @scope.marked)

(block) @scope.marked

(array_initializer) @scope.marked