package com.itsaky.androidide.syntax.lexer.highlighters;

import android.text.SpannableStringBuilder;

/**
 * A highlighter is used to highlight a small piece of code
 * 
 * Implementations of these class shouldn't be used in CodeEditor
 * For CodeEditor usage, implement an EditorLanguage instead
 */
public interface Highlighter {
    SpannableStringBuilder highlight(String code, String match) throws Exception;
}
