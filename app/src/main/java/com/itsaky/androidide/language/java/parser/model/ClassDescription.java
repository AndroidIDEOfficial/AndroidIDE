package com.itsaky.androidide.language.java.parser.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.expression.PackageImporter;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IField;
import com.itsaky.androidide.language.java.parser.internal.IJavaDocCommentable;
import com.itsaky.androidide.language.java.parser.internal.IMethod;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ClassDescription extends JavaSuggestItemImpl implements IClass {
    private static final String TAG = "ClassDescription";
    @NonNull
    private final String mClassName;
    private final ArrayList<ConstructorDescription> mConstructors = new ArrayList<>();
    private final ArrayList<IField> mFields = new ArrayList<>();
    private final ArrayList<IMethod> mMethods = new ArrayList<>();
    private final ArrayList<IClass> mImplements = new ArrayList<>();
    private final int mModifiers;
    private final boolean mPrimitive, mAnnotation, mEum, isMemberClass;
	
    //init later
    @Nullable
    private IClass mSuperClass;

    public ClassDescription(Class c) {
        mClassName = c.getName();
        mModifiers = c.getModifiers();
        mPrimitive = c.isPrimitive();
        mAnnotation = c.isAnnotation();
		isMemberClass = c.isMemberClass();
        mEum = c.isEnum();
    }

    public ClassDescription(String className, int modifiers,
                            boolean isPrimitive,
                            boolean isAnnotation,
                            boolean isEnum,
							boolean isMember) {
        this.mClassName = className;
        this.mModifiers = modifiers;
        this.mPrimitive = isPrimitive;
        this.mAnnotation = isAnnotation;
		this.isMemberClass = isMember;
        this.mEum = isEnum;
    }
    
    public static ClassDescription ofObject() {
        ClassDescription desc = new ClassDescription(Object.class);
        desc.initMembers(Object.class);
        return desc;
    }
	
	@Override
	public boolean isMemberClass() {
		return isMemberClass;
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
    public String getName() {
        if (getPackageName().isEmpty()) {
            return getSimpleName();
        }
        return getSimpleName() + " (" + getPackageName() + ")";
    }

    @Override
    public final String getDescription() {
        return getPackageName().replace("$", ".");
    }

    @Override
    public String getReturnType() {
        return "";
    }

    @Override
    public char getTypeHeader() {
        return 'c';
    }

    @Override
    public int getSuggestionPriority() {
        return JavaSuggestItemImpl.CLASS_DESC;
    }

    public void onSelectThis(CodeEditor editorView) {
        try {
            final int length = getIncomplete().length();
            final int start = editorView.getCursor().getLeft() - length;
			final int end = editorView.getCursor().getLeft();
			final CharPosition s = editorView.getText().getIndexer().getCharPosition(start);
            Content editable = editorView.getText();
            editable.delete(start, end);
            editable.insert(s.line, s.column, getSimpleName());
            PackageImporter.importClass(editable, getFullClassName());
        } catch (Exception e) {
        }
    }

    @Override
    public String getSimpleName() {
        return JavaUtil.getSimpleName(mClassName).replace("$", ".");
    }

    @Override
    public String getFullClassName() {
        return mClassName;
    }

    @Override
    @Nullable
    public IClass getSuperclass() {
        return mSuperClass;
    }

    public String getPackageName() {
        return JavaUtil.getPackageName(mClassName);
    }

    public ArrayList<ConstructorDescription> getConstructors() {
        return mConstructors;
    }

    public ArrayList<IField> getFields() {
        return mFields;
    }

    public void addConstructor(ConstructorDescription constructorDescription) {
        this.mConstructors.add(constructorDescription);
    }

    public void addField(IField fieldDescription) {
        mFields.add(fieldDescription);
    }

    public void addMethod(IMethod methodDescription) {
        mMethods.add(methodDescription);
    }

    public List<IMethod> getMethods() {
        return mMethods;
    }

    @Override
    public String toString() {
        return mClassName;
    }

    @SuppressWarnings("ConstantConditions")
    public ArrayList<SuggestItem> getMember(String prefix) {
        ArrayList<SuggestItem> result = new ArrayList<>();
        for (ConstructorDescription constructor : mConstructors) {
            if (!prefix.isEmpty()) {
                if (constructor.getName().startsWith(prefix)) {
                    result.add(constructor);
                }
            }
        }
        for (IField field : mFields) {
            if (prefix.isEmpty() || field.getFieldName().startsWith(prefix)) {
                result.add(field);
            }
        }
        getMethods(result, prefix);
        return result;
    }

    public void getMethods(ArrayList<SuggestItem> result, String prefix) {
        for (IMethod method : mMethods) {
            if (prefix.isEmpty() || method.getMethodName().startsWith(prefix)) {
                result.add(method);
            }
        }
    }


    @Override
    public int getModifiers() {
        return mModifiers;
    }

    @Override
    public boolean isInterface() {
        return false;
    }


    public boolean isEnum() {
        return mEum;
    }

    @Override
    public boolean isPrimitive() {
        return mPrimitive;
    }

    @Override
    public boolean isAnnotation() {
        return mAnnotation;
    }

    @Override
    public IMethod getMethod(String methodName, IClass[] argsType) {
        for (IMethod method : mMethods) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }

        if (getSuperclass() != null) {
            return getSuperclass().getMethod(methodName, argsType);
        }
        return null;
    }

    @Override
    public IField getField(String name) {
        for (IField field : mFields) {
            if (field.getFieldName().equals(name)) {
                return field;
            }
        }
        if (getSuperclass() != null) {
            return getSuperclass().getField(name);
        }
        return null;
    }
	
    public void initMembers(Class c) {
        mImplements.clear();
        Class[] interfaces = c.getInterfaces();
        for (Class anInterface : interfaces) {
            IClass implement = JavaClassManager.getInstance().getClassWrapper(anInterface);
			mImplements.add(implement);
        }

        if (c.getSuperclass() != null) {
            mSuperClass = JavaClassManager.getInstance().getClassWrapper(c.getSuperclass());
        } else {
            if (!getFullClassName().equals(Object.class.getName())
                    && mSuperClass == null) {
                mSuperClass = JavaClassManager.getInstance().getParsedClass(Object.class.getName());
            }
        }

        for (Constructor constructor : c.getConstructors()) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                addConstructor(new ConstructorDescription(constructor));
            }
        }

        for (Field field : c.getFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                if (!field.getName().equals(field.getDeclaringClass().getName())) {
                    addField(new FieldDescription(field));
                }
            }
        }

        for (Method method : c.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                addMethod(new MethodDescription(method));
            }
        }
    }

    public void setSuperclass(IClass superclass) {
        this.mSuperClass = superclass;
    }
}
