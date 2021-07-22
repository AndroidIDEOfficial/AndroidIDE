package com.itsaky.androidide.language.java.parser.expression;

import com.itsaky.androidide.language.java.parser.util.EditorUtil;
import io.github.rosemoe.editor.widget.CodeEditor;

public class StatementParser {
    /**
     * " Search back from the cursor position till meeting '{' or ';'.
     * " '{' means statement start, ';' means end of a previous statement.
     *
     * @return statement before cursor
     * " Note: It's the base for parsing. And It's OK for most cases.
     */
    public static String resolveStatementFromCursor(CodeEditor editor) {
        String lineBeforeCursor = getCurrentLine(editor);
        if (lineBeforeCursor.matches("^\\s*(import|package)\\s+")) {
            return lineBeforeCursor;
        }
        int oldCursor = editor.getCursor().getLeft();
        int newCursor = oldCursor - 1;
        while (true) {
            if (newCursor == 0) break;
            char c = editor.getText().charAt(newCursor);
            if (c == '{' || c == '}' || c == ';') {
                newCursor++;
                break;
            }
            newCursor--;
        }
        String statement = editor.getText().subSequence(newCursor, oldCursor).toString();
        return mergeLine(statement);
    }


    public static String getCurrentLine(CodeEditor editText) {
        return EditorUtil.getLineBeforeCursor(editText, editText.getCursor().getLeft());
    }

    public static String mergeLine(String statement) {
        statement = cleanStatement(statement);
        return statement;
    }

    /**
     * set string literal empty, remove comments, trim begining or ending spaces
     * case: ' 	sb. /* block comment"/ append( "stringliteral" ) // comment '
     * return 'sb.append("")'
     */
    private static String cleanStatement(String code) {
        if (code.matches("\\s*")) {
            return "";
        }
        code = removeComment(code); //clear all comment
        //clear all string content
        code = code.replaceAll(Patterns.STRINGS.toString(), "\"\"");
        code = EditorUtil.trimLeft(code);
        code = code.replaceAll("[\n\t\r]", "");
        return code;
    }

    /**
     * remove all comment
     */
    private static String removeComment(String code) {
        return code.replaceAll(Patterns.JAVA_COMMENTS.toString(), "");
    }

}
