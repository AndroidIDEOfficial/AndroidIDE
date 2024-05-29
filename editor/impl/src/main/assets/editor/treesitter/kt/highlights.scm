;;; Identifiers

(simple_identifier) @identifier

; `this` this keyword inside classes
(this_expression) @variable.builtin

; `super` keyword inside classes
(super_expression) @variable.builtin

(statements
	  (property_declaration
  	  	(variable_declaration
  		  	(simple_identifier) @property.local)))

(source_file
	(property_declaration
		(variable_declaration
			(simple_identifier) @property.top_level)))

(class_body
	(property_declaration
		(variable_declaration
			(simple_identifier) @property.class)))

(class_parameter
	(simple_identifier) @property.class)

; id_1.id_2.id_3: `id_2` and `id_3` are assumed as object properties
(_
	(navigation_suffix
		(simple_identifier) @property.class))

; SCREAMING CASE identifiers are assumed to be constants
((simple_identifier) @constant
(#match? @constant "^[A-Z][A-Z0-9_]*$"))

(_
	(navigation_suffix
		(simple_identifier) @constant
		(#match? @constant "^[A-Z][A-Z0-9_]*$")))

(enum_entry
	(simple_identifier) @constant)

(type_identifier) @type

; '?' operator, replacement for Java @Nullable
(nullable_type) @punctuation.special

((type_identifier) @type.builtin
	(#any-of? @type.builtin
		"Byte"
		"Short"
		"Int"
		"Long"
		"UByte"
		"UShort"
		"UInt"
		"ULong"
		"Float"
		"Double"
		"Boolean"
		"Char"
		"String"
		"Array"
		"ByteArray"
		"ShortArray"
		"IntArray"
		"LongArray"
		"UByteArray"
		"UShortArray"
		"UIntArray"
		"ULongArray"
		"FloatArray"
		"DoubleArray"
		"BooleanArray"
		"CharArray"
		"Map"
		"Set"
		"List"
		"EmptyMap"
		"EmptySet"
		"EmptyList"
		"MutableMap"
		"MutableSet"
		"MutableList"
))

(package_header
  "package" @keyword)

(import_header
	"import" @keyword)

;;; Function definitions

(function_declaration
	(simple_identifier) @function.declaration)

(getter
	("get") @function.builtin)
(setter
	("set") @function.builtin)

(primary_constructor
  "constructor" @keyword
  (class_parameter
    (simple_identifier) @property.class
    (_
      (type_identifier) @type )))

(secondary_constructor
	("constructor") @keyword)

(constructor_delegation_call
	[ "this" "super" ] @keyword)

(constructor_invocation
	(user_type
		(type_identifier) @constructor))

(anonymous_initializer
	("init") @function.invocation)

(parameter
	(simple_identifier) @parameter)

(parameter_with_optional_type
	(simple_identifier) @parameter)

; lambda parameters
(lambda_literal
	(lambda_parameters
		(variable_declaration
			(simple_identifier) @parameter)))

;;; Function calls

; function()
(call_expression
	. (simple_identifier) @function.invocation)

; ::function
(callable_reference
	. (simple_identifier) @function.invocation)

; object.function() or object.property.function()
(call_expression
	(navigation_expression
		(navigation_suffix
			(simple_identifier) @function.invocation) . ))

(call_expression
	. (simple_identifier) @function.builtin
    (#any-of? @function.builtin
		"arrayOf"
		"arrayOfNulls"
		"byteArrayOf"
		"shortArrayOf"
		"intArrayOf"
		"longArrayOf"
		"ubyteArrayOf"
		"ushortArrayOf"
		"uintArrayOf"
		"ulongArrayOf"
		"floatArrayOf"
		"doubleArrayOf"
		"booleanArrayOf"
		"charArrayOf"
		"emptyArray"
		"mapOf"
		"setOf"
		"listOf"
		"emptyMap"
		"emptySet"
		"emptyList"
		"mutableMapOf"
		"mutableSetOf"
		"mutableListOf"
		"print"
		"println"
		"error"
		"TODO"
		"run"
		"runCatching"
		"repeat"
		"lazy"
		"lazyOf"
		"enumValues"
		"enumValueOf"
		"assert"
		"check"
		"checkNotNull"
		"require"
		"requireNotNull"
		"with"
		"suspend"
		"synchronized"
))

;;; Literals

[(line_comment) (multiline_comment)] @comment

(shebang_line) @preproc

(real_literal) @number
[
	(integer_literal)
	(long_literal)
	(hex_literal)
	(bin_literal)
	(unsigned_literal)
] @number

[
	"null" ; should be highlighted the same as booleans
	(boolean_literal)
] @constant.builtin

(character_literal) @string

(string_literal) @string

; There are 3 ways to define a regex
;    - "[abc]?".toRegex()
(call_expression
	(navigation_expression
		((string_literal) @string.regex)
		(navigation_suffix
			((simple_identifier) @_function
			(#eq? @_function "toRegex")))))

;    - Regex("[abc]?")
(call_expression
	((simple_identifier) @_function
	(#eq? @_function "Regex"))
	(call_suffix
		(value_arguments
			(value_argument
				(string_literal) @string.regex))))

;    - Regex.fromLiteral("[abc]?")
(call_expression
	(navigation_expression
		((simple_identifier) @_class
		(#eq? @_class "Regex"))
		(navigation_suffix
			((simple_identifier) @_function
			(#eq? @_function "fromLiteral"))))
	(call_suffix
		(value_arguments
			(value_argument
				(string_literal) @string.regex))))

;;; Keywords

(type_alias "typealias" @keyword)

(companion_object "companion" @keyword)

[
	(class_modifier)
	(member_modifier)
	(function_modifier)
	(property_modifier)
	(platform_modifier)
	(variance_modifier)
	(parameter_modifier)
	(visibility_modifier)
	(reification_modifier)
	(inheritance_modifier)
] @type.qualifier

[
	"val"
	"var"
	"enum"
	"class"
	"object"
	"interface"
	"fun"
;	"typeof" ; NOTE: It is reserved for future use
  "fun"
	"for"
	"do"
	"while"
	"try"
  "catch"
  "throw"
  "finally"
  "if"
  "else"
  "when"
  "return"
  "continue"
  "break"
] @keyword

(label) @label

(annotation
	"@" @attribute (use_site_target)? @attribute)
(annotation
	(user_type
		(type_identifier) @attribute))
(annotation
	(constructor_invocation
		(user_type
			(type_identifier) @attribute)))

(file_annotation
	"@" @attribute "file" @attribute ":" @attribute)
(file_annotation
	(user_type
		(type_identifier) @attribute))
(file_annotation
	(constructor_invocation
		(user_type
			(type_identifier) @attribute)))

;;; Operators & Punctuation

[
	"!"
	"!="
	"!=="
	"="
	"=="
	"==="
	">"
	">="
	"<"
	"<="
	"||"
	"&&"
	"+"
	"++"
	"+="
	"-"
	"--"
	"-="
	"*"
	"*="
	"/"
	"/="
	"%"
	"%="
	"?."
	"?:"
	"!!"
	"is"
	"!is"
	"in"
	"!in"
	"as"
	"as?"
	".."
	"->"
	"."
	","
	";"
	":"
	"::"
] @operator

[
  "(" ")"
  "[" "]"
  "{" "}"
] @bracket