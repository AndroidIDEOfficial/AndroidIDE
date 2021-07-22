package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;

/**
 * Created by Duy on 20-Jul-17.
 */

public class PrimitiveArrayConstructorDescription extends JavaSuggestItemImpl {
    private String name;

    public PrimitiveArrayConstructorDescription(String name) {
        this.name = name;
    }

    @Override
    public void onSelectThis(@NonNull CodeEditor editorView) {
        try {
            final int length = getIncomplete().length();
            final int cursor = getEditor().getCursor().getLeft();
            final int start = cursor - length;
            Content editable = editorView.getText();
			CharPosition s = editable.getIndexer().getCharPosition(start);
			CharPosition c = editable.getIndexer().getCharPosition(cursor);
            editable.replace(s.line, s.column, c.line, c.column, name + "[]");
			CharPosition select = editable.getIndexer().getCharPosition(start + name.length() + 1);
            editorView.setSelection(select.line, select.column);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name + "[]";
    }

    @Override
    public char getTypeHeader() {
        return 'a';
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getReturnType() {
        return "";
    }


    @Override
    public String toString() {
        return name + "[]";
    }

}
