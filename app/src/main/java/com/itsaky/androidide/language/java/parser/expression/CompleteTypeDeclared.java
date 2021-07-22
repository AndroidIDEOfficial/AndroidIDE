package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.interfaces.Filter;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Complete class declaration
 * <p>
 * public? static? final? class Name (extends otherClass)? (implements otherInterfaces)?
 * public? enum Name
 * public? final? interface Name (extends otherInterfaces)?
 */
public class CompleteTypeDeclared extends JavaCompleteMatcherImpl {
    //case: public class A
    //case: public class Name extends OtherClass
    //case: class Name extends C implements D
    public static final Pattern CLASS_DECLARE = Pattern.compile(
            //more modifiers, public static final ....
            "((public|protected|private|abstract|static|final|strictfp)\\s+)*" +
                    //type
                    "((class|inteface|enum)\\s+)" +
                    //name
                    "([a-zA-Z_][a-zA-Z0-9_]*)" +
                    //inherit
                    "(\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*))?" +
                    "(\\s+implements\\s+([a-zA-Z_][a-zA-Z0-9_]*))?");
    private static final Pattern END_EXTENDS
            = Pattern.compile("\\s+extends\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");
    private static final Pattern END_IMPLEMENTS
            = Pattern.compile("\\s+implements\\s+([a-zA-Z_][a-zA-Z0-9_]*)$");

    private static final String TAG = "CompleteClassDeclared";
    private JavaDexClassLoader mClassLoader;

    public CompleteTypeDeclared(JavaDexClassLoader classLoader) {

        mClassLoader = classLoader;
    }

    @Override
    public boolean process(JCTree.JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> result) {
        Matcher matcher = CLASS_DECLARE.matcher(statement);
        if (matcher.find()) {
            matcher = END_IMPLEMENTS.matcher(statement);
            if (matcher.find()) {
                String incompleteInterface = matcher.group(1);
                return getSuggestionInternal(editor, incompleteInterface, result,
                        "interface");

            }

            matcher = END_EXTENDS.matcher(statement);
            if (matcher.find()) {
                String incompleteInterface = matcher.group(1);
                return getSuggestionInternal(editor, incompleteInterface, result,
                        "class");

            }

        }
        return false;
    }

    /**
     * @param declareType - enum, class, annotation, interface
     */
    private boolean getSuggestionInternal(@NonNull CodeEditor editor,
                                          @NonNull String incomplete,
                                          @NonNull List<SuggestItem> result,
                                          @Nullable String declareType) {

        //filter interfaces or classes
        Filter<IClass> filter = null;
        switch (declareType) {
            case "interface":
                filter = new Filter<IClass>() {
                    @Override
                    public boolean accept(IClass clazz) {
                        if (Modifier.isFinal(clazz.getModifiers())) {
                            return false;
                        }
                        if (!Modifier.isPublic(clazz.getModifiers())) {
                            return false;
                        }
                        return clazz.isInterface();
                    }
                };
                break;
            case "class":
                filter = new Filter<IClass>() {
                    @Override
                    public boolean accept(IClass clazz) {
                        if (Modifier.isFinal(clazz.getModifiers())) {
                            return false;
                        }
                        if (!Modifier.isPublic(clazz.getModifiers())) {
                            return false;
                        }
                        return !clazz.isInterface();
                    }
                };
                break;
        }

        List<IClass> classes = mClassLoader.findAllWithPrefix(incomplete, filter);
        if (classes.size() > 0) {
            setInfo(classes, editor, incomplete);
            result.addAll(classes);
            return true;
        }
        return false;
    }

    @Override
    public void getSuggestion(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {
        getSuggestionInternal(editor, incomplete, suggestItems, null);
    }
}
