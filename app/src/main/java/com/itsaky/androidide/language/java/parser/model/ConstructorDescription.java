package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.language.java.parser.expression.PackageImporter;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IJavaDocCommentable;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ConstructorDescription extends JavaSuggestItemImpl implements IJavaDocCommentable {
    private ArrayList<IClass> mParameterTypes = new ArrayList<>();
    private String mConstructorName;

    public ConstructorDescription(Constructor constructor) {
        mConstructorName = constructor.getName();
        Class[] parameterTypes = constructor.getParameterTypes();
        for (Class parameterType : parameterTypes) {
            IClass type = JavaClassManager.getInstance().getClassWrapper(parameterType);
            mParameterTypes.add(type);
        }
    }

    public ConstructorDescription(String name, List<IClass> paramTypes) {
        mConstructorName = name;
        mParameterTypes.addAll(paramTypes);
    }

	private String javadoc;
	@Override
	public IJavaDocCommentable setHtmlJavaDoc(String javadoc) {
		this.javadoc = javadoc;
		return this;
	}

	@Override
	public String getJavaDoc() {
		return javadoc;
	}

    @Override
    public void onSelectThis(@NonNull CodeEditor editorView) {
        try {
            final int length = getIncomplete().length();
            final int cursor = editorView.getCursor().getLeft();
            final int start = cursor - length;
            Content editable = editorView.getText();
			final CharPosition sPos = editable.getIndexer().getCharPosition(start);
			final CharPosition cPos = editable.getIndexer().getCharPosition(cursor);

            if (getParameterTypes().size() > 0) {
                editable.replace(sPos.line, sPos.column, cPos.line, cPos.column, getSimpleName() + "()");
				CharPosition select = editable.getIndexer().getCharPosition(start + getSimpleName().length() + 1);
                editorView.setSelection(select.line, select.column);
            } else {
                String text = getSimpleName() + "();";
                editable.replace(sPos.line, sPos.column, cPos.line, cPos.column, text);
				CharPosition select = editable.getIndexer().getCharPosition(start + text.length());
                editorView.setSelection(select.line, select.column);
            }
			
            PackageImporter.importClass(editorView.getText(), mConstructorName);
        } catch (Exception e) {
        }
    }
	
    @Override
    public String getName() {
        return getSimpleName() + "(" + paramsToString(getParameterTypes()) + ")";
    }
	
    @Override
    public char getTypeHeader() {
        return 'c';
    }
	
    @Override
    public String getDescription() {
        return "Class constructor";
    }
	
    @Override
    public String getReturnType() {
        return "";
    }
	
    @Override
    public String toString() {
        return getSimpleName() + "()";
    }
	
    private String paramsToString(@NonNull ArrayList<IClass> parameterTypes) {
        StringBuilder result = new StringBuilder();
        boolean firstTime = true;
        for (IClass parameterType : parameterTypes) {
            if (firstTime) {
                firstTime = false;
            } else {
                result.append(",");
            }
            result.append(parameterType.getSimpleName());
        }
        return result.toString();
    }

    public ArrayList<IClass> getParameterTypes() {
        return mParameterTypes;
    }

    private String getSimpleName() {
        return JavaUtil.getSimpleName(mConstructorName);
    }
}
