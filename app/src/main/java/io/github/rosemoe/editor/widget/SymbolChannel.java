package io.github.rosemoe.editor.widget;

import com.itsaky.androidide.utils.Symbols;
import io.github.rosemoe.editor.text.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * A channel to insert symbols in {@link CodeEditor}
 * @author Rosemoe
 */
public class SymbolChannel {

    private CodeEditor mEditor;
    
    private final List<Character> pairs;

    protected SymbolChannel(CodeEditor editor) {
        mEditor = editor;
        pairs = new ArrayList<>();
        pairs.add('}');
        pairs.add(')');
        pairs.add(']');
        pairs.add('"');
        pairs.add('\'');
        pairs.add('>');
    }
    
    /**
     * Inserts the given text in the editor.
     * <p>
     * This method allows you to insert texts externally to the content of editor.
     * The content of {@param symbolText} is not checked to be exactly characters of symbols.
     *
     * @param symbolText Text to insert, usually a text of symbols
     * @param selectionOffset New selection position relative to the start of text to insert.
     *                        Ranging from 0 to symbolText.length()
     */
    public void insertSymbol(String text, int selectionOffset) {
        if (selectionOffset < 0 || selectionOffset > text.length()) {
            return;
        }
        Cursor cur = mEditor.getText().getCursor();
        if (cur.isSelected()) {
            cur.onDeleteKeyPressed();
            mEditor.notifyExternalCursorChange();
        }
        
        if(cur.getLeftColumn() < mEditor.getText().getColumnCount(cur.getLeftLine())
           && text.length() == 1
           && text.charAt(0) == mEditor.getText().charAt(cur.getLeftLine(), cur.getLeftColumn())
           && pairs.contains(text.charAt(0))) {
               mEditor.moveSelectionRight();
        } else {
            cur.onCommitText(text);
            if (selectionOffset != text.length()) {
                mEditor.setSelection(cur.getRightLine(), cur.getRightColumn() - (text.length() - selectionOffset));
            }
        }
    }

}
