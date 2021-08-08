package com.itsaky.androidide.models;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.lsp.CompletionItem;
import com.itsaky.lsp.CompletionItemKind;
import com.itsaky.lsp.Position;
import com.itsaky.lsp.TextDocumentIdentifier;
import com.itsaky.lsp.TextDocumentPositionParams;
import com.itsaky.lsp.TextEdit;
import io.github.rosemoe.editor.text.Cursor;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.List;

public class CompletionItemWrapper implements SuggestItem, Comparable {
    
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
    public String getSortText() {
        return item.kind + getName();
    }

    @Override
    public void onSelectThis(CodeEditor editor) {
        try {
            final Cursor cursor = editor.getCursor();
            if(cursor.isSelected()) return;
            final int line = cursor.getLeftLine();
            int col = cursor.getLeftColumn();
            while(true) {
                col--;
                if(col == 0)
                    break;
                if(!JavaCharacter.isJavaIdentifierPart(editor.getText().charAt(line, col)))
                    break;
            }
            col++;
            String text = getInsertText();
            final boolean shiftLeft = text.contains("$0");
            final boolean atEnd = text.endsWith("$0");
            text = text.replace("$0", "");
            editor.getText().delete(line, col, line, cursor.getLeftColumn());
            cursor.onCommitText(text);
            
            if(shiftLeft && !atEnd) {
                editor.moveSelectionLeft();
                requestSignature(editor);
            }
             
            final List<TextEdit> edits = item.additionalTextEdits;
            if(edits != null && edits.size() > 0) {
                for(int i=0;i<edits.size();i++) {
                    final TextEdit edit = edits.get(i);
                    if(edit == null || edit.range == null || edit.newText == null) continue;
                    final Position start = edit.range.start;
                    final Position end =  edit.range.end;
                    if(start == null || end == null) continue;
                    if(start.equals(end)) {
                        editor.getText().insert(start.line, start.character, edit.newText);
                    } else {
                        editor.getText().replace(start.line, start.character, end.line, end.character, edit.newText);
                    }
                }
            }
        } catch (Throwable th) {}
    }
    
    private void requestSignature(CodeEditor editor) {
        final JavaLanguageServer server = StudioApp.getInstance().getJavaLanguageServer();
        if (server != null) {
            TextDocumentPositionParams p = new TextDocumentPositionParams();
            p.textDocument = new TextDocumentIdentifier(editor.getFile().toURI());
            p.position = new Position(editor.getCursor().getLeftLine(), editor.getCursor().getLeftColumn());
            server.signatureHelp(p, editor.getFile());
        }
    }

    @Override
    public int compareTo(Object p1) {
        if(p1 instanceof CompletionItemWrapper) {
            CompletionItemWrapper that = (CompletionItemWrapper) p1;
            return this.getSortText().compareTo(that.getSortText());
        }
        return 1;
    }
    
    private int getInsertLength() {
        if (prefix.endsWith(".")) return 0;
        else if (prefix.contains(".")) return prefix.substring(prefix.lastIndexOf(".") + 1).length();
        else return prefix.length();
    }
}
