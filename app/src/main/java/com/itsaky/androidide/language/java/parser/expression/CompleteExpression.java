package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IField;
import com.itsaky.androidide.language.java.parser.internal.IMethod;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sun.tools.javac.tree.JCTree.JCErroneous;
import static com.sun.tools.javac.tree.JCTree.JCExpression;

/**
 * This class very complex, implement after
 */
public class CompleteExpression extends JavaCompleteMatcherImpl {
    private static final String TAG = "CompleteExpression";
    private final JavaDexClassLoader mClassLoader;
    private EndPosTable mEndPositions;

    private JCCompilationUnit mAst;
    private IClass mCurrentType;

    private int mCursor;
    private CodeEditor mEditor;

    public CompleteExpression(@NonNull JavaDexClassLoader classLoader) {
        mClassLoader = classLoader;
    }

    public void prepare(CodeEditor editor, JCCompilationUnit ast) {
        mEditor = editor;
        mCursor = editor.getCursor().getLeft();
        mAst = ast;
        mEndPositions = ast.endPositions;

        List<JCTree> typeDecls = mAst.getTypeDecls();
        for (JCTree typeDecl : typeDecls) {
            if (typeDecl instanceof JCTree.JCClassDecl) {
                int startPosition = typeDecl.getStartPosition();
                int endPosition = typeDecl.getEndPosition(mEndPositions);
                if (startPosition <= mCursor && mCursor <= endPosition) {
                    String simpleName = ((JCTree.JCClassDecl) typeDecl).getSimpleName().toString();
                    JCExpression packageName = ast.getPackageName();
                    mCurrentType = JavaClassManager.getInstance()
                            .getParsedClass(packageName + "." + simpleName);
                }
            }
        }
    }

    @Override
    public void getSuggestion(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {

    }

    @Override
    public boolean process(JCCompilationUnit ast, CodeEditor editor, Expression expression,
                           String unused, ArrayList<SuggestItem> result) {
        try {
            prepare(editor, ast);
            return expression != null && performComplete(expression.getExpression(), result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean performComplete(JCExpression jcExpression, ArrayList<SuggestItem> result) {
        if (jcExpression instanceof JCErroneous) {
            List<? extends JCTree> errorTrees = ((JCErroneous) jcExpression).getErrorTrees();
            JCTree errExpr = errorTrees.get(0);
            if (errExpr instanceof JCExpression) {
                return performComplete((JCExpression) errExpr, result);
            }

        } else if (jcExpression instanceof JCFieldAccess) {
            return performCompleteFieldAccess((JCFieldAccess) jcExpression, result);

        } else if (jcExpression instanceof JCTree.JCIdent) {
            return performCompleteIdent((JCTree.JCIdent) jcExpression, result);

        }
        return false;
    }

    /**
     * Suggestion method, variable
     */
    private boolean performCompleteIdent(JCTree.JCIdent jcIdent, ArrayList<SuggestItem> result) {
        return false;
    }

    private boolean performCompleteFieldAccess(@NonNull JCFieldAccess jcFieldAccess,
                                               @NonNull ArrayList<SuggestItem> result) {
        int startPosition = jcFieldAccess.getStartPosition();
        int cursorOffset = mCursor - startPosition;
        int incompleteLength = jcFieldAccess.getIdentifier().length();
        String incomplete = jcFieldAccess.getIdentifier().toString();
        //simple hack, improve later
        if (incomplete.equalsIgnoreCase("<error>")) {
            incomplete = "";
        }
		
        JCExpression expression = jcFieldAccess.getExpression();
		TypeResolver resolver = new TypeResolver(mClassLoader, mAst, mCurrentType);
        IClass type = resolver.resolveType(expression, mCursor);
        if (type != null) {
			
            List<IMethod> methods = type.getMethods();
            for (IMethod method : methods) {
                if(Modifier.isPublic(method.getModifiers())) {
					if(resolver.isStatic() && !Modifier.isStatic(method.getModifiers()))
						continue;
					if (method.getMethodName().toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
						setInfo(method, mEditor, incomplete);
						result.add(method);
					}
				}
            }
			
            ArrayList<IField> fields = type.getFields();
            for (IField field : fields) {
				if(Modifier.isPublic(field.getFieldModifiers())) {
					if(resolver.isStatic() && !Modifier.isStatic(field.getFieldModifiers()))
						continue;
					if (field.getFieldName().toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
						setInfo(field, mEditor, incomplete);
						result.add(field);
					}
				}
            }

            return true;
        }
        return false;
    }


}
