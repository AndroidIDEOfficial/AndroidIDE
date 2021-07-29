package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.language.java.parser.model.FieldDescription;
import com.itsaky.androidide.language.java.parser.model.MethodDescription;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sun.tools.javac.tree.JCTree.JCClassDecl;
import static com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import static com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class CompleteThisKeyword extends JavaCompleteMatcherImpl {
    private static final Pattern THIS_DOT = Pattern.compile("(\\W)this\\s*\\.\\s*$");
    private static final Pattern THIS_DOT_EXPR = Pattern.compile("\\Wthis\\s*\\.\\s*(" + Patterns.IDENTIFIER.pattern() + ")$");
    private static final String TAG = "CompleteThisKeyword";
    private static JavaDexClassLoader mClassLoader;

    public CompleteThisKeyword(JavaDexClassLoader classLoader) {
        this.mClassLoader = classLoader;
    }

    protected static void addMethod(JCCompilationUnit unit, JCMethodDecl method, CodeEditor editor, String incomplete,
                                    List<SuggestItem> result) {
        if (method.getName().toString().toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
            List<JCTypeParameter> typeParameters
                    = method.getTypeParameters();
            ArrayList<String> paramsStr = new ArrayList<>();
            for (JCTypeParameter typeParameter : typeParameters) {
                paramsStr.add(typeParameter.toString());
            }
			IClass returnType = null;
			String rName = JavaUtil.findImportedClassName(unit, method.getReturnType().toString());
			if(rName != null && rName.trim().length() > 0) {
				returnType = mClassLoader.getClassReader().getParsedClass(rName);
			}
            MethodDescription desc = new MethodDescription(
                    method.getName().toString(),
					returnType,
                    (int) method.getModifiers().flags,
                    paramsStr);
            setInfo(desc, editor, incomplete);
            result.add(desc);
        }
    }

    protected static void addVariable(JCCompilationUnit unit, JCVariableDecl member, CodeEditor editor, String incomplete,
                                      List<SuggestItem> result) {
        if (member.getName().toString().toLowerCase(Locale.US).startsWith(incomplete.toLowerCase(Locale.US))) {
			IClass returnType = null;
			String rName = JavaUtil.findImportedClassName(unit, member.getType().toString());
			if(rName != null && rName.trim().length() > 0) {
				returnType = mClassLoader.getClassReader().getParsedClass(rName);
			}
            int flags = (int) member.getModifiers().flags;
            FieldDescription desc = new FieldDescription(
                    flags,
                    returnType,
                    member.getName().toString(),
                    null);
            setInfo(desc, editor, incomplete);
            result.add(desc);
        }
    }

    @Override
    public boolean process(JCCompilationUnit ast, CodeEditor editor, Expression expression, String statement, ArrayList<SuggestItem> result) {
        Matcher matcher = THIS_DOT.matcher(statement);
        if (matcher.find()) {
            getSuggestionInternal(editor, result, ast, "");
            return true;
        }
        matcher = THIS_DOT_EXPR.matcher(statement);
        if (matcher.find()) {
            String incomplete = matcher.group(1);
            getSuggestionInternal(editor, result, ast, incomplete);
            return true;
        }
        return false;
    }

    @Override
    public void getSuggestion(CodeEditor editor, String incomplete, List<SuggestItem> suggestItems) {

    }

    private void getSuggestionInternal(@NonNull CodeEditor editor, @NonNull ArrayList<SuggestItem> result,
                                       @NonNull JCCompilationUnit unit,
                                       @NonNull String incomplete) {
        if (unit == null) {
            return;
        }
        //current file declare
        List<JCTree> typeDecls = unit.getTypeDecls();
        if (typeDecls.isEmpty()) {
            return;
        }
        JCTree jcTree = typeDecls.get(0);
        if (jcTree instanceof JCClassDecl) {
            List<JCTree> members =
                    ((JCClassDecl) jcTree).getMembers();
            for (JCTree member : members) {
                if (member instanceof JCVariableDecl) {
                    addVariable(unit, (JCVariableDecl) member, editor, incomplete, result);
                } else if (member instanceof JCMethodDecl) {
                    JCMethodDecl method = (JCMethodDecl) member;
                    addMethod(unit, method, editor, incomplete, result);
                }
            }
        }
    }
}
