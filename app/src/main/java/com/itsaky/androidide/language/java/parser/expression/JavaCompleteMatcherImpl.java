package com.itsaky.androidide.language.java.parser.expression;

import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.language.java.parser.model.JavaSuggestItemImpl;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.List;
import java.util.regex.Pattern;

public abstract class JavaCompleteMatcherImpl implements IJavaCompleteMatcher {
    //statement end with character, number or dot
    public static final Pattern END_WITH_CHARACTER_OR_DOT
            = Pattern.compile("[.0-9A-Za-z_]\\s*$", Pattern.MULTILINE);
    //statement end with dot "str."
    public static final Pattern END_WITH_DOT = Pattern.compile("\\.\\s*$", Pattern.MULTILINE);

    /**
     * Check statement is valid when end with dot
     */
    public static final Pattern VALID_WHEN_END_WITH_DOT
            = Pattern.compile(
            "[" +
                    "\"" + //string: "Hello".|
                    ")" + //expression in parentheses: (new String("")).|
                    "0-9A-Za-z_" + //name of variable or identifier: variable.|
                    "\\]" + //array access: array[].|
                    "]\\s*\\.\\s*$", Pattern.MULTILINE);
    public static final Pattern KEYWORD_DOT
            = Pattern.compile("(" + Patterns.RE_KEYWORDS.pattern() + ")\\s*\\.\\s*$");


    public static final Pattern METHOD_NAME = Patterns.IDENTIFIER;
    public static final Pattern VARIABLE_NAME = Patterns.IDENTIFIER;
    //String or java.lang.String
    public static final Pattern CLASS_NAME = Pattern.compile(
            Patterns.IDENTIFIER.pattern() + "(\\s*\\.\\s*" + Patterns.IDENTIFIER.pattern() + ")*");
    //java.util.*
    public static final Pattern PACKAGE_NAME = CLASS_NAME;
    //java.io.FileInputStream
    public static final Pattern CONSTRUCTOR = CLASS_NAME;

    private static final String TAG = "JavaCompleteMatcherImpl";

    protected static void setInfo(List<? extends SuggestItem> members, CodeEditor editor, String incomplete) {
        for (SuggestItem member : members) {
            if (member instanceof JavaSuggestItemImpl) {
                setInfo((JavaSuggestItemImpl) member, editor, incomplete);
            } else {
            }
        }
    }

    protected static void setInfo(Object member, CodeEditor editor, String incomplete) {
        if (member instanceof JavaSuggestItemImpl) {
            ((JavaSuggestItemImpl) member).setEditor(editor);
            ((JavaSuggestItemImpl) member).setIncomplete(incomplete);
        }
    }
}
