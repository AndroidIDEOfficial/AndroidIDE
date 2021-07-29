package com.itsaky.androidide.language.java.provider;

import androidx.core.util.Pair;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.java.parser.Expression;
import com.itsaky.androidide.language.java.parser.ExpressionResolver;
import com.itsaky.androidide.language.java.parser.JavacParser;
import com.itsaky.androidide.language.java.parser.expression.CompleteExpression;
import com.itsaky.androidide.language.java.parser.expression.CompleteNewKeyword;
import com.itsaky.androidide.language.java.parser.expression.CompletePackage;
import com.itsaky.androidide.language.java.parser.expression.CompleteString;
import com.itsaky.androidide.language.java.parser.expression.CompleteThisKeyword;
import com.itsaky.androidide.language.java.parser.expression.CompleteTypeDeclared;
import com.itsaky.androidide.language.java.parser.expression.CompleteWord;
import com.itsaky.androidide.language.java.parser.expression.IJavaCompleteMatcher;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.PackageManager;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.utils.Environment;
import com.sun.tools.javac.tree.JCTree;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class JavaCompletionProvider {

	private final List<IJavaCompleteMatcher> mCompletors;
	private JavaDexClassLoader mClassLoader;
	private PackageManager mPackageManager;
	private JavacParser mJavaParser;
	
	private boolean initiated = false;

	public JavaCompletionProvider() {
		this.mCompletors = new ArrayList<>();
		this.mClassLoader = new JavaDexClassLoader(Environment.BOOTCLASSPATH);
		this.mPackageManager = new PackageManager();
		this.mJavaParser = new JavacParser();

		addMatchers();
	}

	public boolean loadProject(AndroidProject project) {
		mClassLoader.loadAllClasses(project);
		mPackageManager.init(project, mClassLoader.getClassReader());
		return initiated = true;
	}
	
	public boolean isInitiated() {
		return initiated;
	}

	private void addMatchers() {
        mCompletors.add(new CompleteExpression(mClassLoader));
        mCompletors.add(new CompleteNewKeyword(mClassLoader));
        mCompletors.add(new CompletePackage(mPackageManager));
        mCompletors.add(new CompleteString(mClassLoader));
        mCompletors.add(new CompleteTypeDeclared(mClassLoader));
        mCompletors.add(new CompleteThisKeyword(mClassLoader));
        mCompletors.add(new CompleteWord(mJavaParser, mClassLoader));
    }

	public Pair<ArrayList<SuggestItem>, List<Diagnostic<? extends JavaFileObject>>> getSuggestions(CodeEditor editor) {
        ArrayList<SuggestItem> result = new ArrayList<>();
        try {
            JCTree.JCCompilationUnit ast = mJavaParser.parse(editor.getText());
            List<IClass> classes = mJavaParser.parseClasses(ast);
			
            mClassLoader.updateClasses(classes);

            ExpressionResolver resolver = new ExpressionResolver(ast, editor);
            Expression expression = resolver.getExpressionAtCursor();
			
            JCTree.JCExpression jcExpression = expression.getExpression();
            int startPosition = jcExpression.getStartPosition();
			CharPosition s = editor.getText().getIndexer().getCharPosition(startPosition);
			CharPosition c = new CharPosition();
			c.line = editor.getCursor().getLeftLine();
			c.column = editor.getCursor().getLeftColumn();
            String statement = editor.getText().subContent(s.line, s.column, c.line, c.column).toString();
            for (IJavaCompleteMatcher autoComplete : mCompletors) {
                try {
                    boolean handled = autoComplete.process(ast, editor, expression, statement, result);
                    if (handled) {
                        break;
                    }
                } catch (Throwable e) {
                }
            }
        } catch (Throwable e) {
		}
        return Pair.create(result, mJavaParser.getDiagnostics());
    }
}
