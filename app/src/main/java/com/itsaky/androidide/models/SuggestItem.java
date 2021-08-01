package com.itsaky.androidide.models;

import io.github.rosemoe.editor.widget.CodeEditor;

public interface SuggestItem {
    String getName();
    String getDescription();
    String getReturnType();
    String getSortText();
    
    char getTypeHeader();
    
    void onSelectThis(CodeEditor editor);
}
