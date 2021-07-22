package com.itsaky.androidide.language.java.parser.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import com.itsaky.androidide.language.java.parser.expression.PackageImporter;
import com.itsaky.androidide.language.java.parser.expression.PatternFactory;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.regex.Matcher;
import io.github.rosemoe.editor.text.CharPosition;

/**
 * Created by Duy on 20-Jul-17.
 */

public class EditorUtil {

    private static final String TAG = "EditorUtil";

    public EditorUtil() {
    }

    @Nullable
    public static String getCurrentPackage(String editText) {
        Matcher matcher = PatternFactory.PACKAGE.matcher(editText);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String getCurrentClassName(String editor) {
        return "";
    }

    @NonNull
    public static String getCurrentClassSimpleName(String editor) {
        String className = getCurrentClassName(editor);
        int i = className.indexOf(".");
        if (i == -1) return className;
        else {
            return className.substring(className.lastIndexOf(".") + 1);
        }
    }


    public static ArrayList<String> getPossibleClassName(String source, String simpleName, String prefix) {
        Log.d(TAG, "getPossibleClassName() called with:  simpleName = [" + simpleName + "], prefix = [" + prefix + "]");

        ArrayList<String> classList = new ArrayList<>();
        String importedClassName = PackageImporter.getImportedClassName(source, simpleName);
        Log.d(TAG, "getPossibleClassName importedClassName = " + importedClassName);
        if (importedClassName != null) {
            classList.add(importedClassName);
        } else {
            if (!prefix.contains(".")) {
                classList.add(getCurrentClassName(source)); //current member
                if (simpleName != null && !simpleName.isEmpty()) {
                    classList.add("java.lang." + simpleName); //default java.lang package
                } else if (!prefix.isEmpty()) {
                    classList.add("java.lang." + prefix);
                }
            } else {
                classList.add(prefix);
            }
        }
        Log.d(TAG, "getPossibleClassName() returned: " + classList);
        return classList;
    }

    public static String getLine(EditText editText, int pos) {
        if (pos < 0 || pos > editText.length()) return "";
        int line = LineUtils.getLineFromIndex(pos, editText.getLayout().getLineCount(), editText.getLayout());

        int lineStart = editText.getLayout().getLineStart(line);
        int lineEnd = editText.getLayout().getLineEnd(line);
        return editText.getText().subSequence(lineStart, lineEnd).toString();
    }

    @Nullable
    public static String getWord(EditText editText, int pos, boolean removeParentheses) {
        String line = getLine(editText, pos).trim();
        return getLastWord(line, removeParentheses);
    }

    @NonNull
    public static String getWord(EditText editText, int pos) {
        String line = getLine(editText, pos).trim();
        return getLastWord(line, false);
    }

    @NonNull
    public static String getLastWord(String line, boolean removeParentheses) {
        String result = PatternFactory.lastMatchStr(line, PatternFactory.WORD);
        if (result != null) {
            return removeParentheses ? result.replaceAll(".*\\(", "") : result;
        } else {
            return "";
        }
    }

    @Nullable
    public static String getPreWord(EditText editor, int pos) {
        String line = getLine(editor, pos);
        String[] split = line.split(PatternFactory.SPLIT_NON_WORD_STR);
        return split.length >= 2 ? split[split.length - 2] : null;
    }

    @NonNull
    public static String getLineBeforeCursor(@NonNull CodeEditor editor, final int cursor) {
        int start = cursor - 1;
        String text = editor.getText().toString();
        while (start > 0 && text.charAt(start) != '\n') {
            start = start - 1;
            if (start >= 0 && text.charAt(start) == '\n'){
                start++;
                break;
            }
        }
		CharPosition s = editor.getText().getIndexer().getCharPosition(start);
		CharPosition c = editor.getText().getIndexer().getCharPosition(cursor - 1);
        return editor.getText().subContent(s.line, s.column, c.line, c.column).toString();
    }

    public static String trimLeft(String code) {
        if (code == null) {
            return null;
        }
        if (code.length() == 0) {
            return "";
        }
        int index = 0;
        while (index < code.length()) {
            if (Character.isWhitespace(code.charAt(index))) {
                index++;
            } else {
                break;
            }
        }
        if (index == code.length()) {
            return "";
        }
        return code.substring(index);
    }
}
