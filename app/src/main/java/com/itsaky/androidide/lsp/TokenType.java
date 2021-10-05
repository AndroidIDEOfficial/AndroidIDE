package com.itsaky.androidide.lsp;

import org.eclipse.lsp4j.SemanticTokenTypes;
import io.github.rosemoe.editor.widget.EditorColorScheme;

public enum TokenType {
	/**
     * Order of the member declaration must be preseved!
     */
	NAMESPACE(SemanticTokenTypes.Namespace, EditorColorScheme.PACKAGE_NAME),
	CLASS(SemanticTokenTypes.Class, EditorColorScheme.TYPE_NAME),
	INTERFACE(SemanticTokenTypes.Interface, EditorColorScheme.INTERFACE),
	ENUM(SemanticTokenTypes.Enum, EditorColorScheme.ENUM),
	ENUM_MEMBER(SemanticTokenTypes.EnumMember, EditorColorScheme.STATIC_FIELD),
	TYPE(SemanticTokenTypes.Type, EditorColorScheme.TYPE_NAME),
	TYPE_PARAMETER(SemanticTokenTypes.TypeParameter, EditorColorScheme.TYPE_PARAM),
	METHOD(SemanticTokenTypes.Method, EditorColorScheme.METHOD_DECLARATION),
	PROPERTY(SemanticTokenTypes.Property, EditorColorScheme.FIELD),
	VARIABLE(SemanticTokenTypes.Variable, EditorColorScheme.LOCAL_VARIABLE),
	PARAMETER(SemanticTokenTypes.Parameter, EditorColorScheme.PARAMETER),
	MODIFIER(SemanticTokenTypes.Modifier, EditorColorScheme.KEYWORD),
	KEYWORD(SemanticTokenTypes.Keyword, EditorColorScheme.KEYWORD),
	
	ANNOTATION("annotation", EditorColorScheme.ANNOTATION),
	ANNOTATION_TYPE("annotationType", EditorColorScheme.TYPE_NAME),
	
	UNKNOWN("unknown", EditorColorScheme.TEXT_NORMAL);
	
	private String name;
	private int editorType;
    
	private TokenType (String name, int editorType) {
		this.name = name;
        this.editorType = editorType;
	}
    
    public int getEditorType() {
        return editorType;
    }
	
	@Override
	public String toString () {
		return name;
	}
}
