package com.itsaky.androidide.models;

import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.CompletionItem;
import com.itsaky.lsp.CompletionItemKind;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Cursor;
import io.github.rosemoe.editor.widget.CodeEditor;

public class CompletionItemWrapper implements SuggestItem {
    
    private CompletionItem item;
    private String prefix;

    public CompletionItemWrapper(CompletionItem item, String prefix) {
        this.item = item;
        this.prefix = prefix;
    }
    
    public String getInsertText() {
        return item.insertText == null ? item.label : item.insertText;
    }
    
    @Override
    public String getName() {
        return item.label;
    }

    @Override
    public String getDescription() {
        return item.detail;
    }

    @Override
    public String getReturnType() {
        return CompletionItemKind.asString(item.kind);
    }

    @Override
    public char getTypeHeader() {
        return getReturnType().charAt(0);
    }

    @Override
    public int getSuggestionPriority() {
        return item.kind;
    }

    @Override
    public void onSelectThis(CodeEditor editor) {
        try {
            final Cursor cursor = editor.getCursor();
            if(cursor.isSelected()) return;
            final int length = getInsertLength();
            String text = getInsertText();
            final boolean shiftLeft = text.contains("$0");
            final boolean atEnd = text.endsWith("$0");
            text = text.replace("$0", "");
            editor.getText().delete(cursor.getLeftLine(), cursor.getLeftColumn() - length, cursor.getLeftLine(), cursor.getLeftColumn());
            cursor.onCommitText(text);
            
            if(shiftLeft && !atEnd)
                editor.moveSelectionLeft();
        } catch (Throwable th) {
            Logger.instance("CompletionItemWrapper").e("onSelectThis, Error: ", ThrowableUtils.getFullStackTrace(th));
        }
    }
    
    private int getInsertLength() {
        if (prefix.endsWith(".")) return 0;
        else if (prefix.contains(".")) return prefix.substring(prefix.lastIndexOf(".") + 1).length();
        else return prefix.length();
    }
}
