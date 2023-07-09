; bracket pair highlights query for sora-editor

; for xml declaration, mark the opening '<?' as brackets open and closing '?>' as brackets close
(xml_decl
  "<?" @editor.brackets.open
  "?>" @editor.brackets.close)

; for empty element tag, mark opening '<' and closing '/>' as brackets open and close
(empty_element
  "<" @editor.brackets.open
  ">" @editor.brackets.close)

; in case of an element which has an end tag
; mark the tag name in tag_start as bracket open
; and the tag name in tag_end as bracket close
(tag_start
  tag_name: (name) @editor.brackets.open)
(tag_end
  tag_name: (name) @editor.brackets.close)

; bracket pairs for CDATA sections
(cdata_start) @editor.brackets.open
(cdata_end) @editor.brackets.close