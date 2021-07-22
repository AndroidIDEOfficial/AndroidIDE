package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;

public abstract class JavaSuggestItemImpl implements SuggestItem, Cloneable {
    public static final int FIELD_DESC = 0;
    public static final int METHOD_DESC = 1;
    public static final int CLASS_DESC = 2;
    public static final int OTHER_DESC = 3;

    private CodeEditor editor;
    private String incomplete;

    @Override
    public int getSuggestionPriority() {
        return OTHER_DESC;
    }

    public CodeEditor getEditor() {
        return editor;
    }

    public void setEditor(CodeEditor editor) {
        this.editor = editor;
    }

    public String getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(@NonNull String incomplete) {
        this.incomplete = incomplete;
    }
	
    protected void insertImpl(CodeEditor editorView, String text) {

        try {
            final int length = getIncomplete().length();
            final int cursor = getEditor().getCursor().getLeft();
            final int start = cursor - length;
            Content editable = editorView.getText();
			CharPosition s = editable.getIndexer().getCharPosition(start);
			CharPosition c = editable.getIndexer().getCharPosition(cursor);
            editable.replace(s.line, s.column, c.line, c.column, text);
			CharPosition select = editable.getIndexer().getCharPosition(start + text.length());
            editorView.setSelection(select.line, select.column);
        } catch (Exception e) {
        }
    }

	@Override
	public String toString() {
		return getName();
	}
}
