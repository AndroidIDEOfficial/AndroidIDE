package com.itsaky.androidide.language.java.parser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.itsaky.androidide.javadoc.JavaDoc;
import com.itsaky.androidide.javadoc.JavaDocParser;
import com.itsaky.androidide.javadoc.JavaDocParserBuilder;
import com.itsaky.androidide.javadoc.OutputType;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IJavaDocCommentable;
import com.itsaky.androidide.language.java.parser.internal.JavaClassManager;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.itsaky.androidide.language.java.parser.model.ClassDescription;
import com.itsaky.androidide.language.java.parser.model.ConstructorDescription;
import com.itsaky.androidide.language.java.parser.model.FieldDescription;
import com.itsaky.androidide.language.java.parser.model.MethodDescription;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.parser.Tokens;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;
import io.github.rosemoe.editor.text.Content;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;

import static com.sun.tools.javac.tree.JCTree.*;

public class JavacParser {
	
	private Context context;
	private ParserFactory parserFactory;
	private JavaDocParser javadocParser;
	private DiagnosticCollector<JavaFileObject> diagnostics;
	private boolean canParse = true;
	
	private static final String DOT = ".";
    private static final String CONSTRUCTOR_NAME = "<init>";
	
	public JavacParser() {
		context = new Context();
		diagnostics = new DiagnosticCollector<JavaFileObject>();
		context.put(DiagnosticListener.class, diagnostics);
		Options.instance(context).put("allowStringFolding", "false");
        JavacFileManager fileManager = new JavacFileManager(context, true, Charsets.UTF_8);
        try {
            fileManager.setLocation(StandardLocation.PLATFORM_CLASS_PATH, ImmutableList.<File>of());
        } catch (IOException e) {
            canParse = false;
        }
		parserFactory = ParserFactory.instance(context);
	}
	
	@Nullable
	public JCCompilationUnit parse(Content src) {
		return parse(src.toString());
	}
	
	@Nullable
    public JCCompilationUnit parse(final String src) {
        if (!canParse) return null;
        SimpleJavaFileObject source = new SimpleJavaFileObject(URI.create("source"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return src;
            }
        };
        Log.instance(context).useSource(source);
		diagnostics.clearDiagnostics();
        Parser parser =
			parserFactory.newParser(src,
					/*keepDocComments=*/ true, // Necessary, because we show doc comments in AndroidIDE
					/*keepEndPos=*/ true,
					/*keepLineMap=*/ true);
        JCCompilationUnit unit;
        unit = parser.parseCompilationUnit();
        unit.sourcefile = source;
        return unit;
    }

    @Nullable
    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        if (!canParse) return null;
        return diagnostics.getDiagnostics();
    }
	
    public List<IClass> parseClasses(JCCompilationUnit unit) {
        List<IClass> classes = new ArrayList<>();
        List<JCTree> typeDecls = unit.getTypeDecls();
        for (JCTree typeDecl : typeDecls) {
            if (typeDecl instanceof JCClassDecl) {
                classes.add(parseClass(unit, (JCClassDecl) typeDecl));
            }
        }
        return classes;
    }

    private IClass parseClass(JCCompilationUnit unit, JCClassDecl classDecl) {
        final String className = unit.getPackageName() + DOT + classDecl.getSimpleName();
        final int modifiers = JavaUtil.toJavaModifiers(classDecl.getModifiers().getFlags());
		
        ClassDescription clazz = new ClassDescription(
			className,
			modifiers,
			false,
			classDecl.getKind() == Tree.Kind.ANNOTATION_TYPE,
			classDecl.getKind() == Tree.Kind.ENUM,
			false);
		
        IClass extendsClass = JavaUtil.jcTypeToClass(unit, classDecl.getExtendsClause());
        if (extendsClass != null) {
            clazz.setSuperclass(extendsClass);
        } else {
            clazz.setSuperclass(JavaClassManager.getInstance().getParsedClass(Object.class.getName()));
        }
		
        List<JCTree> members = classDecl.getMembers();
        for (JCTree member : members) {
            if (member instanceof JCTree.JCMethodDecl) {
                addMethod(unit, clazz, (JCTree.JCMethodDecl) member);
            } else if (member instanceof JCTree.JCVariableDecl) {
                addVariable(unit, clazz, classDecl, (JCTree.JCVariableDecl) member);
            } else if(member instanceof JCTree.JCClassDecl) {
				addNestedClass(unit, classDecl, (JCTree.JCClassDecl) member);
			}
        }
		if(clazz != null && unit.docComments != null && unit.docComments.hasComment(classDecl)) {
			Tokens.Comment comment = unit.docComments.getComment(classDecl);
			if(comment.getStyle() == Tokens.Comment.CommentStyle.JAVADOC) {
				processJavaDoc(comment, clazz);
			}
		}
        //now add constructor
        //add methods
        return clazz;
    }
	
	private void addNestedClass(@NonNull JCCompilationUnit ast, JCClassDecl parent, JCClassDecl classDecl) {
		final String className = ast.getPackageName() + DOT + parent.getSimpleName() + "$" + classDecl.getSimpleName();
        final int modifiers = JavaUtil.toJavaModifiers(classDecl.getModifiers().getFlags());
		ClassDescription clazz = new ClassDescription(
			className,
			modifiers,
			false,
			classDecl.getKind() == Tree.Kind.ANNOTATION_TYPE,
			classDecl.getKind() == Tree.Kind.ENUM,
			false);
		IClass extendsClass = JavaUtil.jcTypeToClass(ast, classDecl.getExtendsClause());
        if (extendsClass != null) {
            clazz.setSuperclass(extendsClass);
        } else {
            clazz.setSuperclass(JavaClassManager.getInstance().getParsedClass(Object.class.getName()));
        }


        List<JCTree> members = classDecl.getMembers();
        for (JCTree member : members) {
            if (member instanceof JCTree.JCMethodDecl) {
                addMethod(ast, clazz, (JCTree.JCMethodDecl) member);
            } else if (member instanceof JCTree.JCVariableDecl) {
                addVariable(ast, clazz, classDecl, (JCTree.JCVariableDecl) member);
            } else if(member instanceof JCTree.JCClassDecl) {
				addNestedClass(ast, classDecl, (JCTree.JCClassDecl) member);
			}
        }
		
		if(clazz != null && ast.docComments != null && ast.docComments.hasComment(classDecl)) {
			Tokens.Comment comment = ast.docComments.getComment(classDecl);
			if(comment.getStyle() == Tokens.Comment.CommentStyle.JAVADOC) {
				processJavaDoc(comment, clazz);
			}
		}
	}

    private void addVariable(@NonNull JCCompilationUnit unit,
                             ClassDescription clazz, @NonNull JCClassDecl classDecl,
                             @NonNull JCTree.JCVariableDecl member) {
        String name = member.getName().toString();
        int modifiers = JavaUtil.toJavaModifiers(member.getModifiers().getFlags());
        IClass type = JavaUtil.jcTypeToClass(unit, member.getType());
        JCTree.JCExpression initializer = member.getInitializer();
        FieldDescription fieldDescription = new FieldDescription(
			modifiers,
			type,
			name,
			initializer != null ? initializer.toString() : null);
        clazz.addField(fieldDescription);
		if(fieldDescription != null && unit.docComments != null && unit.docComments.hasComment(member)) {
			Tokens.Comment comment = unit.docComments.getComment(member);
			if(comment.getStyle() == Tokens.Comment.CommentStyle.JAVADOC) {
				processJavaDoc(comment, fieldDescription);
			}
		}
    }

    private void addMethod(@NonNull JCCompilationUnit unit,
                           @NonNull ClassDescription clazz,
                           @NonNull JCTree.JCMethodDecl member) {


        final String methodName = member.getName().toString();
        final int modifiers = JavaUtil.toJavaModifiers(member.getModifiers().getFlags());
        final List<IClass> methodParameters = new ArrayList<>();
        List<JCTree.JCVariableDecl> parameters = member.getParameters();
        for (JCTree.JCVariableDecl parameter : parameters) {
            JCTree type = parameter.getType();
            IClass paramType = JavaUtil.jcTypeToClass(unit, type);
            methodParameters.add(paramType);
        }
		
        IClass returnType = JavaUtil.jcTypeToClass(unit, member.getReturnType());
		IJavaDocCommentable commetable = null;
        if (member.getName().toString().equals(CONSTRUCTOR_NAME)) {
            ConstructorDescription constructor = new ConstructorDescription(
				clazz.getFullClassName(),
				methodParameters);
            clazz.addConstructor(constructor);
			commetable = constructor;
        } else {
            MethodDescription methodDescription = new MethodDescription(
				methodName,
				modifiers,
				methodParameters,
				returnType);
            clazz.addMethod(methodDescription);
			commetable = methodDescription;
        }
		
		if(commetable != null && unit.docComments != null && unit.docComments.hasComment(member)) {
			Tokens.Comment comment = unit.docComments.getComment(member);
			if(comment.getStyle() == Tokens.Comment.CommentStyle.JAVADOC) {
				processJavaDoc(comment, commetable);
			}
		}
    }

	private void processJavaDoc(Tokens.Comment comment, IJavaDocCommentable commetable) {
		if(javadocParser == null) {
			javadocParser = JavaDocParserBuilder.withBasicTags()
				.withOutputType(OutputType.HTML)
				.build();
		}
		final JavaDoc doc = javadocParser.parse(comment.getText());
		final String html = doc.getSummary() + "\n" + doc.getDescription();
		commetable.setHtmlJavaDoc(html);
	}


    private void addConstructor(ClassDescription clazz, JCTree.JCMethodDecl member) {
        ConstructorDescription constructorDescription;
    }

    private JCClassDecl resolveClassSimpleName(List<JCTree> typeDecls) {
        for (JCTree typeDecl : typeDecls) {
            if (typeDecl instanceof JCClassDecl) {
                JCModifiers modifiers = ((JCClassDecl) typeDecl).getModifiers();
                Set<Modifier> flags = modifiers.getFlags();
                for (Modifier flag : flags) {
                    if (flag.equals(Modifier.PUBLIC)) {
                        return (JCClassDecl) typeDecl;
                    }
                }
            }
        }
        return null;
    }
}
