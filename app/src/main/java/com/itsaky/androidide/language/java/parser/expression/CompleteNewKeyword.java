package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.language.java.parser.model.ConstructorDescription;
import com.itsaky.androidide.language.java.parser.model.PrimitiveArrayConstructorDescription;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

/**
 * Suggest constructor
 */
public class CompleteNewKeyword extends JavaCompleteMatcherImpl {
    private static final Pattern NEW_CLASS = Pattern.compile(
            "(\\s*new\\s+)(" + CONSTRUCTOR.pattern() + ")$", Pattern.MULTILINE);
    private static final String[] PRIMITIVE_ARRAY_TYPE =
            {"boolean", "byte", "double", "char", "float", "int", "long", "short"};

    private final JavaDexClassLoader mClassLoader;

    public CompleteNewKeyword(JavaDexClassLoader classLoader) {
        mClassLoader = classLoader;
    }

    @Override
    public boolean process(JCTree.JCCompilationUnit ast,
                           CodeEditor editor, Expression expression,
                           String statement, ArrayList<SuggestItem> result) {
        Matcher matcher = NEW_CLASS.matcher(statement);
        if (matcher.find()) {
            String incompleteCts = matcher.group(2);
            return getSuggestionInternal(editor, incompleteCts, result);
        }
        return false;
    }

    @Override
    public void getSuggestion(@NonNull CodeEditor editor,
                              @NonNull String incomplete,
                              @NonNull List<SuggestItem> suggestItems) {
        getSuggestionInternal(editor, incomplete, suggestItems);
    }

    private boolean getSuggestionInternal(@NonNull CodeEditor editor, @NonNull String incomplete,
                                          @NonNull List<SuggestItem> suggestItems) {
        if (incomplete.isEmpty()) {
            return false;
        }

        boolean handled = false;
        //try to find constructor
        List<IClass> classes = mClassLoader.findAllWithPrefix(incomplete);
        for (IClass clazz : classes) {
            List<ConstructorDescription> constructors = clazz.getConstructors();
            setInfo(constructors, editor, incomplete);
            suggestItems.addAll(constructors);
            handled = true;
        }
        for (String type : PRIMITIVE_ARRAY_TYPE) {
            if (type.toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
                PrimitiveArrayConstructorDescription c
                        = new PrimitiveArrayConstructorDescription(type);
                setInfo(c, editor, incomplete);
                suggestItems.add(c);
                handled = true;
            }
        }

        return handled;
    }

	
}
