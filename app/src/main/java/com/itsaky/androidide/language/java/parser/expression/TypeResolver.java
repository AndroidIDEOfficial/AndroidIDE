package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.internal.IClass;
import com.itsaky.androidide.language.java.parser.internal.IField;
import com.itsaky.androidide.language.java.parser.internal.IMethod;
import com.itsaky.androidide.language.java.parser.internal.JavaDexClassLoader;
import com.itsaky.androidide.language.java.parser.internal.JavaUtil;
import com.sun.tools.javac.tree.JCTree;
import java.util.LinkedList;
import java.util.List;

import static com.itsaky.androidide.language.java.parser.internal.JavaUtil.*;
import static com.sun.tools.javac.tree.JCTree.JCArrayAccess;
import static com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import static com.sun.tools.javac.tree.JCTree.JCBlock;
import static com.sun.tools.javac.tree.JCTree.JCCatch;
import static com.sun.tools.javac.tree.JCTree.JCClassDecl;
import static com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import static com.sun.tools.javac.tree.JCTree.JCExpression;
import static com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCStatement;
import static com.sun.tools.javac.tree.JCTree.JCTry;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

public class TypeResolver {
    private static final String TAG = "TypeResolver";
    private JavaDexClassLoader mClassLoader;
    private JCCompilationUnit mUnit;
    private IClass mCurrentType;
	
	private boolean isStatic= false;
	
	public boolean isStatic() {
		return isStatic;
	}

    public TypeResolver(JavaDexClassLoader classLoader,
                        JCCompilationUnit unit,
                        IClass currentType) {
        mClassLoader = classLoader;
        mUnit = unit;
        mCurrentType = currentType;
    }

    public IClass resolveType(@NonNull JCExpression expression, int cursor) {
        return resolveTypeImpl(expression, cursor);
    }

    @Nullable
    private IClass resolveTypeImpl(@NonNull final JCExpression expression, int cursor) {
        List<JCTree> list = extractExpressionAtCursor(expression, cursor);
        if (list == null) {
            return null;
        }
        String exceptionMessage = "Can not resolve type of expression ";
        IClass currentType = mCurrentType;
        for (JCTree tree : list) {
            //only once time on this case
            if (tree instanceof JCIdent) {
                JCIdent jcIdent = (JCIdent) tree;

                //variable declaration, static import or inner class
                //case: variableDecl
                JCVariableDecl variableDecl = getVariableDeclaration(mUnit, jcIdent);
                if (variableDecl != null) {
                    currentType = JavaUtil.jcTypeToClass(mUnit, variableDecl.getType());
                } else {
                    //case: System.out -> find imported class, this expression can be static access
                    String className = findImportedClassName(mUnit, jcIdent.getName().toString());
                    currentType = mClassLoader.getClassReader().getParsedClass(className);
					isStatic = true;
                }
                // TODO: 13-Jun-18  case: static import
                // TODO: 13-Jun-18  case inner class
            } else if (tree instanceof JCMethodInvocation) {
                // TODO: 13-Jun-18 find method in current class or import static
                JCMethodInvocation jcMethod = (JCMethodInvocation) tree;
                JCExpression methodSelect = jcMethod.getMethodSelect();
                if (methodSelect instanceof JCFieldAccess) {
                    String methodName = ((JCFieldAccess) methodSelect).getIdentifier().toString();
                    List<JCExpression> arguments = jcMethod.getArguments();
                    IClass[] types = new IClass[arguments.size()];
                    IMethod method = currentType.getMethod(methodName, types);
                    if (method == null) {
                        throw new UnsupportedOperationException(exceptionMessage + tree);
                    }
                    currentType = method.getMethodReturnType();
                } else if (methodSelect instanceof JCIdent) { //method in current class
                    String methodName = ((JCIdent) methodSelect).getName().toString();
                    IMethod method = currentType.getMethod(methodName, null);
                    if (method == null) {
                        throw new UnsupportedOperationException(exceptionMessage + tree);
                    }
                    currentType = method.getMethodReturnType();
                } else {
                    throw new UnsupportedOperationException(exceptionMessage + tree);
                }
            } else if (tree instanceof JCFieldAccess) {
                if (currentType == null) {
                    throw new UnsupportedOperationException(exceptionMessage + tree);
                }

                String name = ((JCFieldAccess) tree).getIdentifier().toString();
                IField field = currentType.getField(name);
                if (field == null) {
                    throw new UnsupportedOperationException(exceptionMessage + tree);
                }
                currentType = field.getFieldType();
            } else if (tree instanceof JCArrayAccess) {
                JCIdent jcIdent = (JCIdent) ((JCArrayAccess) tree).getExpression();

                //variable declaration, static import or inner class
                //case: variableDecl
                JCVariableDecl variableDecl = getVariableDeclaration(mUnit, jcIdent);
                if (variableDecl != null) {
                    if (!(variableDecl.getType() instanceof JCArrayTypeTree)) {
                        throw new UnsupportedOperationException("can not resolve type of array access " + tree);
                    }
                    String className = ((JCArrayTypeTree) variableDecl.getType())
                            .getType().toString();
                    className = findImportedClassName(mUnit, className);
                    currentType = mClassLoader.getClassReader().getParsedClass(className);
                }
            }
        }
		
        return currentType;
    }


    @Nullable
    private List<JCTree> extractExpressionAtCursor(JCExpression expression, int cursor) {
        JCTree tree = expression;
        LinkedList<JCTree> list = new LinkedList<>();
        while (tree != null) {
            if (getEndPosition(tree) > cursor) {
                break;
            }
            if (tree instanceof JCMethodInvocation) {
                list.addFirst(tree);

                JCExpression methodSelect = ((JCMethodInvocation) tree).getMethodSelect();
                if (methodSelect instanceof JCIdent) {
                    break;
                } else if (methodSelect instanceof JCFieldAccess) {
                    tree = ((JCFieldAccess) methodSelect).getExpression();

                } else { //unsupported
                    return null;
                }
            } else if (tree instanceof JCFieldAccess) {  //var.field
                list.addFirst(tree);
                //select before
                tree = ((JCFieldAccess) tree).getExpression();

            } else if (tree instanceof JCIdent) { //var, it should be before any expression
                list.addFirst(tree);
                break;

            } else if (tree instanceof JCArrayAccess) { //variable.array[i].toString
                list.addFirst(tree);
                //select declare name
                tree = ((JCArrayAccess) tree).getExpression(); //select array name
                if (tree instanceof JCIdent) {
                    break;
                } else if (tree instanceof JCFieldAccess) {
                } else {
                    throw new UnsupportedOperationException("can not extract expression at array " + tree);
                }
            } else { //unsupported
                //should not happen
                return null;
            }
        }
        return list;
    }

    @Nullable
    private JCVariableDecl getVariableDeclaration(final JCCompilationUnit unit,
                                                  final JCIdent jcIdent) {
        List<JCTree> typeDecls = unit.getTypeDecls();
        for (JCTree typeDecl : typeDecls) {
            JCVariableDecl variableDecl = getVariableDeclaration(typeDecl, jcIdent);
            if (variableDecl != null) {
                return variableDecl;
            }
        }
        return null;
    }

    /**
     * Visit all scope and find variable declaration when given jcIdent
     *
     * @param parent - parent block to find
     */
    @Nullable
    private JCVariableDecl getVariableDeclaration(final JCTree parent,
                                                  final JCIdent jcIdent) {
        // TODO: 14-Jun-18 need add parent if jcIdent to resolve exactly scope
        if (parent instanceof JCBlock) {
            List<JCStatement> statements = ((JCBlock) parent).getStatements();
            return getVariableDeclarationFromStatements(parent, statements, jcIdent);

        } else if (parent instanceof JCTree.JCCase) {
            JCTree.JCCase jcCase = (JCTree.JCCase) parent;
            return getVariableDeclarationFromStatements(parent, jcCase.getStatements(), jcIdent);

        } else if (parent instanceof JCClassDecl) {
            List<JCTree> members = ((JCClassDecl) parent).getMembers();
            //all member equals scope
            for (JCTree member : members) {
                if (member instanceof JCVariableDecl) { //variable
                    JCVariableDecl variableDecl = (JCVariableDecl) member;
                    if (canBeSampleVariable(parent, variableDecl, jcIdent)) {
                        return variableDecl;
                    }
                } else if (member instanceof JCMethodDecl) { //method
                    JCMethodDecl jcMethodDecl = (JCMethodDecl) member;
                    JCBlock jcMethodDeclBody = jcMethodDecl.getBody();
                    JCVariableDecl variableDecl = getVariableDeclaration(jcMethodDeclBody, jcIdent);
                    if (variableDecl != null) {
                        return variableDecl;
                    }

                    variableDecl = getVariableDeclarationFromStatements(member,
                            ((JCMethodDecl) member).getParameters(), jcIdent);
                    if (variableDecl != null) {
                        return variableDecl;
                    }
                } else {
                    JCVariableDecl variableDecl = getVariableDeclaration(member, jcIdent);
                    if (variableDecl != null) {
                        return variableDecl;
                    }
                }
            }
        } else if (parent instanceof JCTree.JCDoWhileLoop) {
            return getVariableDeclaration(((JCTree.JCDoWhileLoop) parent).getStatement(), jcIdent);

        } else if (parent instanceof JCTree.JCEnhancedForLoop) {
            JCVariableDecl variable = ((JCTree.JCEnhancedForLoop) parent).getVariable();
            if (canBeSampleVariable(parent, variable, jcIdent)) {
                return variable;
            }
            return getVariableDeclaration(((JCTree.JCEnhancedForLoop) parent).getStatement(), jcIdent);

        } else if (parent instanceof JCTree.JCForLoop) {
            List<JCStatement> initializer = ((JCTree.JCForLoop) parent).getInitializer();
            JCVariableDecl variableDecl = getVariableDeclarationFromStatements(parent, initializer, jcIdent);
            if (variableDecl != null) {
                return variableDecl;
            }
            return getVariableDeclaration(((JCTree.JCForLoop) parent).getStatement(), jcIdent);

        } else if (parent instanceof JCTree.JCIf) {
            JCTree.JCIf jcIf = (JCTree.JCIf) parent;
            JCStatement thenStatement = jcIf.getThenStatement();
            JCVariableDecl variableDecl = getVariableDeclaration(thenStatement, jcIdent);
            if (variableDecl != null) {
                return variableDecl;
            }
            JCStatement elseStatement = jcIf.getElseStatement();
            if (elseStatement != null) {
                return getVariableDeclaration(elseStatement, jcIdent);
            }

        } else if (parent instanceof JCTree.JCLabeledStatement) {
            return getVariableDeclaration(((JCTree.JCLabeledStatement) parent).getStatement(), jcIdent);

        } else if (parent instanceof JCTree.JCSynchronized) {
            return getVariableDeclaration(((JCTree.JCSynchronized) parent).getBlock(), jcIdent);

        } else if (parent instanceof JCTry) {
            JCTry jcTry = (JCTry) parent;
            JCBlock jcTryBlock = jcTry.getBlock();
            JCVariableDecl variableDecl = getVariableDeclaration(jcTryBlock, jcIdent);
            if (variableDecl != null) {
                return variableDecl;
            }
            for (JCCatch jcCatch : jcTry.getCatches()) {
                JCVariableDecl parameter = jcCatch.getParameter();
                if (canBeSampleVariable(jcCatch, parameter, jcIdent)) {
                    return parameter;
                }
                JCBlock jcCatchBlock = jcCatch.getBlock();
                variableDecl = getVariableDeclaration(jcCatchBlock, jcIdent);
                if (variableDecl != null) {
                    return variableDecl;
                }
            }

        } else if (parent instanceof JCTree.JCWhileLoop) {
            return getVariableDeclaration(((JCTree.JCWhileLoop) parent).getStatement(), jcIdent);

        }
        return null;
    }

    @Nullable
    private JCVariableDecl getVariableDeclarationFromStatements(JCTree parent,
                                                                List<? extends JCStatement> statements,
                                                                JCIdent jcIdent) {
        for (JCStatement statement : statements) {
            if (statement instanceof JCVariableDecl) {
                JCVariableDecl variableDecl = (JCVariableDecl) statement;
                if (canBeSampleVariable(parent, variableDecl, jcIdent)) {
                    return variableDecl;
                }
            } else {
                JCVariableDecl variableDecl = getVariableDeclaration(statement, jcIdent);
                if (variableDecl != null) {
                    return variableDecl;
                }
            }
        }
        return null;
    }


    /**
     * @param parent - scope of variable
     */
    private boolean canBeSampleVariable(@NonNull JCTree parent,
                                        @NonNull JCVariableDecl variable,
            /*should all parent if identifier*/
                                        @NonNull JCIdent ident) {
        if (!variable.getName().equals(ident.getName())) {
            return false;
        }

        //identifier inside or equal scope if variable o
        //-------------------------------------------
        // private ArrayList list = new ArrayList();
        // void method(){
        //      list.toString()
        //}
        //-------------------------------------------
        // void method(ArrayList list){
        //      list.toString()
        //}
        //-------------------------------------------
        //void method(){
        //      ArrayList list;
        //      list.toString()
        //}
        if (isChildOfParent(parent, ident)) {
            int startPosition = variable.getStartPosition();
            int startPosition1 = ident.getStartPosition();

            return true;
        }
        return false;
    }

    private boolean isChildOfParent(JCTree parent, JCTree child) {
        //scope of child inside parent
        return parent.getStartPosition() <= child.getStartPosition()
                && getEndPosition(parent) >= getEndPosition(child);
    }

    private int getEndPosition(JCTree tree) {
        return tree.getEndPosition(mUnit.endPositions);
    }

    static class c {
        static void cd() {

        }
    }

}
