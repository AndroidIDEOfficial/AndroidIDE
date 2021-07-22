package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;

public interface IJavaCompleteMatcher {
    boolean process(JCTree.JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> result)
            throws Exception;

    void getSuggestion(@NonNull CodeEditor editor,
                       @NonNull String incomplete,
                       @NonNull List<SuggestItem> suggestItems);
}
