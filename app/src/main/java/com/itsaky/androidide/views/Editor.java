package com.itsaky.androidide.views;

import android.content.Context;
import android.util.AttributeSet;
import io.github.rosemoe.editor.widget.CodeEditor;

public class Editor extends CodeEditor {
    
    public Editor(Context context) {
        this(context, null);
    }
    
    public Editor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public Editor(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    
    public Editor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
