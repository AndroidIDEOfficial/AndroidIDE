package com.itsaky.androidide.language.java.parser.internal;

import io.github.rosemoe.editor.widget.CodeEditor;

public interface SuggestItem {

	public String getName();
	public String getDescription();
	public String getReturnType();
	public char getTypeHeader();
	public int getSuggestionPriority();
	public void onSelectThis(CodeEditor editor);
}
