package com.itsaky.androidide.language.java.parser.expression;

import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IMethod;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;
import java.lang.reflect.Modifier;

public class CompleteString extends JavaCompleteMatcherImpl {
    public static final Pattern STRING_DOT =
            Pattern.compile("\"\\s*\\.\\s*$", Pattern.MULTILINE);
    public static final Pattern STRING_DOT_EXPR
            = Pattern.compile("\"\\s*\\.\\s*(" + METHOD_NAME.pattern() + ")$");
    private static final String TAG = "CompleteString";
    private final JavaDexClassLoader mClassLoader;

    public CompleteString(JavaDexClassLoader classLoader) {
        mClassLoader = classLoader;
    }

    @Override
    public boolean process(JCTree.JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> suggestItems) {
        Matcher matcher = STRING_DOT.matcher(statement);
        if (matcher.find()) {
            getSuggestion(editor, "", suggestItems);
            return true;
        }
        matcher = STRING_DOT_EXPR.matcher(statement);
        if (matcher.find()) {
            String incompleteMethod = matcher.group(1);
            getSuggestion(editor, incompleteMethod, suggestItems);
            return true;
        }
        return false;
    }

    /**
     * Complete string only filter method, not filter field or constructor
     */
    @Override
    public void getSuggestion(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {
        JavaClassManager reader = mClassLoader.getClassReader();
        IClass stringClass = reader.getParsedClass(String.class.getName());
        assert stringClass != null;
        ArrayList<SuggestItem> methods = new ArrayList<>();
        for (IMethod method : stringClass.getMethods()) {
			if(!Modifier.isPublic(method.getModifiers()))
				continue;
            if (method.getMethodName().toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
                suggestItems.add(method);
            }
        }
        setInfo(methods, editor, incomplete);
        suggestItems.addAll(methods);
    }
}
