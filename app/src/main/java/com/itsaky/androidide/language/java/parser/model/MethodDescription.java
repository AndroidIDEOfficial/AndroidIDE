package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IJavaDocCommentable;
import com.itsaky.androidide.language.java.parser.internal.IMethod;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodDescription extends JavaSuggestItemImpl implements Member, SuggestItem, IMethod {
    private String mMethodName;
    private int mModifiers;
    private ArrayList<String> mParameterTypes = new ArrayList<>();
    private ArrayList<IClass> mParameters = new ArrayList<>();

    /**
     * Return type of method, null if it is constructor
     */
    @Nullable
    private IClass mReturnType;

    public MethodDescription(String name, IClass returnType,
                             long modifiers, ArrayList<String> parameterTypes) {
        mMethodName = name;
        mReturnType = returnType;
        mModifiers = (int) modifiers;
        mParameterTypes = parameterTypes;
    }

    public MethodDescription(@NonNull String methodName,
                             int modifiers,
                             @NonNull List<IClass> parameters,
                             @Nullable IClass returnType) {
        mMethodName = methodName;
        mModifiers = modifiers;
        mParameters.addAll(parameters);
        mReturnType = returnType;
    }

    public MethodDescription(@NonNull Method method) {
        mMethodName = method.getName();
        mModifiers = method.getModifiers();
        mReturnType = JavaClassManager.getInstance().getClassWrapper(method.getReturnType());
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            mParameterTypes.add(parameterType.getName());
        }
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
            final int start = editorView.getCursor().getLeft() - length;
            Content editable = editorView.getText();
			CharPosition s = editable.getIndexer().getCharPosition(start);
            editable.delete(start, editorView.getCursor().getLeft());
            String simpleName = JavaUtil.getSimpleName(mMethodName);
            String text = simpleName + "()" + (shouldAddSemicolon(getEditor()) ? ";" : "");
            if (getParameterTypes().size() > 0) {
                editable.insert(s.line, s.column, text);
				CharPosition select = editable.getIndexer().getCharPosition(start + simpleName.length() + 1);
                editorView.setSelection(select.line, select.column);
            } else {
                editable.insert(s.line, s.column, text);
				CharPosition select = editable.getIndexer().getCharPosition(start + text.length());
                editorView.setSelection(select.line, select.column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldAddSemicolon(CodeEditor editor) {
        int cursor = editor.getCursor().getLeft();
        String text = editor.getText().toString();
        while (cursor < text.length()) {
            char c = text.charAt(cursor);
            if (c == ' ' || c == '\n') {
                cursor++;
                continue;
            }
            if (c == ';') {
                return false;
            } else {
                break;
            }
        }

        if (getMethodReturnType() == null) {
            return true;
        }
        if (void.class.getName().equals(getMethodReturnType().getFullClassName())) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getParameterTypes() {
        return mParameterTypes;
    }

    @Override
    public char getTypeHeader() {
        return 'm';
    }

    @Override
    public String getName() {
        return mMethodName + "(" + paramsToString() + ")";
    }

    private String paramsToString() {
        StringBuilder result = new StringBuilder();
        boolean firstTime = true;
        ArrayList<String> parameterTypes = getParameterTypes();
        for (String parameterType : parameterTypes) {
            if (firstTime) {
                firstTime = false;
            } else {
                result.append(",");

            }
            result.append(JavaUtil.getSimpleName(parameterType));
        }
        return result.toString();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getReturnType() {
        if (mReturnType == null) {
            return "";
        }
        return mReturnType.getSimpleName();
    }

    @Override
    public int getSuggestionPriority() {
        return METHOD_DESC;
    }

    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < mParameterTypes.size(); i++) {
            String parameterType = mParameterTypes.get(i);
            if (i == mParameterTypes.size() - 1) {
                params.append(JavaUtil.getSimpleName(parameterType));
                break;
            }
            params.append(JavaUtil.getSimpleName(parameterType)).append(",");
        }
        return mMethodName + "(" + params.toString() + ")";
    }

    @Override
    public int getModifiers() {
        return mModifiers;
    }

    @Override
    public String getMethodName() {
        return mMethodName;
    }

    @Override
    public IClass[] getMethodParameterTypes() {
        return null;
    }

    @Override
    public IClass getMethodReturnType() {
        return mReturnType;
    }
}
