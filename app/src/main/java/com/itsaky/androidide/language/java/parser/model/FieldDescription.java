package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IField;
import com.itsaky.androidide.language.java.parser.internal.IJavaDocCommentable;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldDescription extends JavaSuggestItemImpl implements Member, IField {
    @NonNull
    private String mName;
    @NonNull
    private IClass mType;
    private int mModifiers;
    @Nullable
    private String mValue;

    public FieldDescription(int modifiers, IClass type, @NonNull String name, String initValue) {
        mName = name;
        mType = type;
        mModifiers = modifiers;
        mValue = initValue;
    }

    public FieldDescription(Field field) {
        mName = field.getName();
        mType = JavaClassManager.getInstance().getClassWrapper(field.getType());
        mModifiers = field.getModifiers();

        if (Modifier.isStatic(mModifiers)) {
            try {
                boolean primitive = field.getType().isPrimitive();
                Object o = field.get(null);
                if (primitive) {
                    mValue = o.toString();
                } else {
                    mValue = o.getClass().getSimpleName();
                }
            } catch (Exception ignored) {
            }
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
        insertImpl(editorView, mName);
    }


    @Override
    public char getTypeHeader() {
        return 'f';
    }

    @Override
    public String getName() {
        if (mValue == null) {
            return mName;
        } else {
            return mName + "(" + mValue + ")";
        }
    }

    @Override
    public String getDescription() {
        return mValue;
    }

    @Override
    public String getReturnType() {
        if (mType == null) {
            return "";
        }
        return mType.getSimpleName();
    }

    @Override
    public int getSuggestionPriority() {
        return JavaSuggestItemImpl.FIELD_DESC;
    }

    @Override
    public String toString() {
        return mName;
    }


    @Override
    public int getModifiers() {
        return mModifiers;
    }

    @Override
    public String getFieldName() {
        return mName;
    }

    @Override
    public IClass getFieldType() {
        return mType;
    }

    @Override
    public int getFieldModifiers() {
        return mModifiers;
    }

    @Override
    public Object getFieldValue() {
        return mValue;
    }
}
