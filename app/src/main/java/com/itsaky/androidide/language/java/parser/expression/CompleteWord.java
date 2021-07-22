package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.JavacParser;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.language.java.parser.model.KeywordDescription;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itsaky.androidide.language.java.parser.expression.CompleteThisKeyword.*;
import java.util.Locale;

/**
 * Final complete, no context, suggest class name, method name,
 * variable or any start with incomplete
 */
public class CompleteWord extends JavaCompleteMatcherImpl {
    private static final String TAG = "CompleteWord";
    private final static Set<String> KEYWORDS;

    static {
        String[] kws = {
                "abstract", "continue", "for", "new", "switch",
                "assert", "default", "if", "package", "synchronized",
                "boolean", "do", "goto", "private", "this",
                "break", "double", "implements", "protected", "throw",
                "byte", "else", "import", "public", "throws",
                "case", "enum", "instanceof", "return", "transient",
                "catch", "extends", "int", "short", "try",
                "char", "final", "interface", "static", "void",
                "class", "finally", "long", "strictfp", "volatile",
                "const", "float", "native", "super", "while",
                "null", "true", "false"
        };
        Set<String> s = new HashSet<>(Arrays.asList(kws));
        KEYWORDS = Collections.unmodifiableSet(s);
    }

    private JavacParser mJavaParser;
    private JavaDexClassLoader mClassLoader;

    public CompleteWord(JavacParser javaParser, JavaDexClassLoader classLoader) {
        this.mJavaParser = javaParser;
        mClassLoader = classLoader;
    }

    @Override
    public boolean process(JCTree.JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> result) {
        return getSuggestionInternal(editor, statement, result);
    }

    @Override
    public void getSuggestion(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {
        getSuggestionInternal(editor, incomplete, suggestItems);
    }

    private boolean getSuggestionInternal(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {
        boolean result = getPossibleInCurrentFile(editor, incomplete, suggestItems);
        result |= getKeyword(editor, incomplete, suggestItems);
        return result;
    }

    private boolean getKeyword(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {
        if (incomplete.trim().isEmpty()) {
            return false;
        }
        boolean found = false;
        for (String keyword : KEYWORDS) {
            if (keyword.toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US)) && !keyword.equalsIgnoreCase(incomplete)) {
                KeywordDescription e = new KeywordDescription(keyword);
                setInfo(e, editor, incomplete);
                suggestItems.add(e);
                found = true;
            }
        }
        return found;
    }

    private boolean getPossibleInCurrentFile(@NonNull CodeEditor editor, @NonNull String incomplete,
                                             @NonNull List<SuggestItem> suggestItems) {
        if (incomplete.trim().isEmpty()) {
            return false;
        }
		
        JCTree.JCCompilationUnit ast;
        try {
            ast = mJavaParser.parse(editor.getText());
        } catch (Exception e) {
            return false;
        }
		
        getPossibleResult(editor, suggestItems, ast, incomplete);
		
        List<IClass> classes = mClassLoader.findAllWithPrefix(incomplete);

        setInfo(classes, editor, incomplete);
        suggestItems.addAll(classes);

        return classes.size() > 0;
    }

    private void getPossibleResult(CodeEditor editor, List<SuggestItem> result,
                                   JCTree.JCCompilationUnit ast, String incomplete) {
        if (ast == null) {
            return;
        }
		
        List<JCTree> typeDecls = ast.getTypeDecls();
        if (typeDecls.isEmpty()) {
            return;
        }
        JCTree jcTree = typeDecls.get(0);
        if (jcTree instanceof JCTree.JCClassDecl) {
            List<JCTree> members =
                    ((JCTree.JCClassDecl) jcTree).getMembers();
            for (JCTree member : members) {
                if (member instanceof JCTree.JCVariableDecl) {
                    addVariable(ast, (JCTree.JCVariableDecl) member, editor, incomplete, result);
                } else if (member instanceof JCTree.JCMethodDecl) {
                    JCTree.JCMethodDecl method = (JCTree.JCMethodDecl) member;
                    addMethod(ast, method, editor, incomplete, result);
                    if (method.getStartPosition() <= editor.getCursor().getLeft()
                            && method.getBody().getEndPosition(ast.endPositions) >= editor.getCursor().getLeft()) {
                        collectFromMethod(ast, editor, incomplete, result, method);
                    }
                }
            }
        }
    }

    private void collectFromMethod(JCTree.JCCompilationUnit unit, CodeEditor editor, String incomplete, List<SuggestItem> result,
                                   JCTree.JCMethodDecl method) {
        List<JCTree.JCStatement> statements
                = method.getBody().getStatements();
        for (JCTree.JCStatement statement : statements) {
            if (statement instanceof JCTree.JCVariableDecl) {
                JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) statement;
                addVariable(unit, field, editor, incomplete, result);
            }
        }
		
        List<JCTree.JCVariableDecl> parameters = method.getParameters();
        for (JCTree.JCVariableDecl parameter : parameters) {
            addVariable(unit, parameter, editor, incomplete, result);
        }

    }
}
