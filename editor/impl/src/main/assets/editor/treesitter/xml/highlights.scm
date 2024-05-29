(xml_decl
  decl: "xml" @xml_decl)

(xml_version
  version_attr: "version" @attr.name)

(xml_encoding
  encoding_attr: "encoding" @attr.name)

(xml_version_value) @attr.value
(xml_encoding_value) @attr.value

(empty_element
  tag_name: (name) @element.tag)

(tag_start
  tag_name: (name) @element.tag)

(tag_end
  tag_name: (name) @element.tag)

"xmlns" @ns_declarator
(ns_decl
  xmlns_prefix: (name) @xmlns.prefix)

(xml_attr
  ns_prefix: (name) @attr.prefix
  attr_name: (name) @attr.name)

(attr_value) @attr.value

(comment) @comment

(entity_ref) @xml.ref
(char_ref) @xml.ref

(comment) @comment

(cdata_start) @cdata.start
(cdata_end) @cdata.end
(cdata) @cdata.data

(char_data) @text

(eq) @operator

["<" "/" ">" "<?" "?>"] @operator