package com.itsaky.androidide.language.java.parser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCAssignOp;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCInstanceOf;
import com.sun.tools.javac.tree.JCTree.JCLabeledStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCSwitch;
import com.sun.tools.javac.tree.JCTree.JCSynchronized;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCLambda;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.util.List;
import java.util.Map;
import com.sun.tools.javac.tree.EndPosTable;

public class ExpressionResolver {
    private int mCursor;
    private EndPosTable mEndPositions;
    private JCTree.JCCompilationUnit mAst;

    public ExpressionResolver(@NonNull JCTree.JCCompilationUnit unit, @NonNull CodeEditor editor) {
        unit.getClass();
        editor.getClass();
        mAst = unit;
        mCursor = editor.getCursor().getLeft();
        mEndPositions = unit.endPositions;
    }

    @Nullable
    public Expression getExpressionAtCursor() {
        for (JCTree jcTree : mAst.getTypeDecls()) {
            Expression expression = getExpressionContainsCursor(jcTree);
            if (expression != null) {
                return expression;
            }
        }
        return null;
    }

    @Nullable
    private Expression getExpressionContainsCursor(JCTree tree) {
        if (!isCursorInsideTree(tree)) {
            return null;
        }
        if (tree instanceof JCClassDecl) {
            return getExpressionFromClass((JCClassDecl) tree);
        }
        return null;
    }

    private Expression getExpressionFromStatement(JCTree.JCStatement statement) {
        if (!isCursorInsideTree(statement)) {
            return null;
        }
        Expression result;
        if (statement instanceof JCBlock) {
            JCBlock jcBlock = (JCBlock) statement;
            List<JCStatement> statements = jcBlock.getStatements();
            return addRootIfNeeded(jcBlock/*root*/,
								   getExpressionFromStatements(statements));

        } else if (statement instanceof JCClassDecl) {
            return getExpressionFromClass((JCClassDecl) statement);

        } else if (statement instanceof JCDoWhileLoop) {
            JCDoWhileLoop jcDoWhileLoop = (JCDoWhileLoop) statement;
            JCExpression cond = jcDoWhileLoop.cond;
            if (isCursorInsideTree(cond)) {
                return resolveExactlyExpression(statement, cond);
            }

            JCStatement body = jcDoWhileLoop.getStatement();
            result = getExpressionFromStatement(body);
            return addRootIfNeeded(statement, result);

        } else if (statement instanceof JCEnhancedForLoop) {
            JCEnhancedForLoop jcEnhancedForLoop = (JCEnhancedForLoop) statement;
            JCExpression expression = jcEnhancedForLoop.getExpression();
            if (isCursorInsideTree(expression)) {
                return resolveExactlyExpression(jcEnhancedForLoop, expression);
            }

            JCVariableDecl variable = jcEnhancedForLoop.getVariable();
            return addRootIfNeeded(
				jcEnhancedForLoop/*root*/,
				getExpressionFromStatement(variable));

        } else if (statement instanceof JCExpressionStatement) {
            JCExpression expression = ((JCExpressionStatement) statement).getExpression();
            if (isCursorInsideTree(expression)) {
                return resolveExactlyExpression(statement, expression);
            }

        } else if (statement instanceof JCForLoop) {
            JCForLoop jcForLoop = (JCForLoop) statement;
            List<JCStatement> initializer = jcForLoop.getInitializer();
            result = getExpressionFromStatements(initializer);
            if (result != null) {
                return addRootIfNeeded(jcForLoop, result);
            }

            JCExpression condition = jcForLoop.getCondition();
            if (isCursorInsideTree(condition)) {
                return resolveExactlyExpression(jcForLoop, condition);
            }

            List<JCExpressionStatement> update = jcForLoop.getUpdate();
            result = getExpressionFromStatements(update);
            if (result != null) {
                return addRootIfNeeded(jcForLoop, result);
            }

            result = getExpressionFromStatement(jcForLoop.getStatement());
            if (result != null) {
                return addRootIfNeeded(jcForLoop, result);
            }
        } else if (statement instanceof JCIf) {
            JCIf jcIf = (JCIf) statement;
            JCExpression condition = jcIf.getCondition();
            if (isCursorInsideTree(condition)) {
                return resolveExactlyExpression(jcIf, condition);
            }

            JCStatement thenStatement = jcIf.getThenStatement();
            result = getExpressionFromStatement(thenStatement);
            if (result != null) {
                return addRootIfNeeded(jcIf, result);
            }

            JCStatement elseStatement = jcIf.getElseStatement();
            if (elseStatement != null) {
                result = getExpressionFromStatement(thenStatement);
                if (result != null) {
                    return addRootIfNeeded(jcIf, result);
                }

            }
        } else if (statement instanceof JCLabeledStatement) {
            JCLabeledStatement jcLabeledStatement = (JCLabeledStatement) statement;
            return addRootIfNeeded(jcLabeledStatement,
								   getExpressionFromStatement(jcLabeledStatement.getStatement()));

        } else if (statement instanceof JCReturn) {
            JCReturn jcReturn = (JCReturn) statement;
            JCExpression expression = jcReturn.getExpression();
            if (isCursorInsideTree(expression)) {
                return resolveExactlyExpression(jcReturn, expression);
            }
        } else if (statement instanceof JCSwitch) {
            JCSwitch jcSwitch = (JCSwitch) statement;
            JCExpression jcExpression = jcSwitch.getExpression();
            if (isCursorInsideTree(jcExpression)) {
                return resolveExactlyExpression(jcSwitch, jcExpression);
            }

            List<JCCase> jcCases = jcSwitch.getCases();
            for (JCCase jcCase : jcCases) {
                JCExpression jcCaseExpression = jcCase.getExpression();
                if (isCursorInsideTree(jcCaseExpression)) {
                    return resolveExactlyExpression(jcCase, jcCaseExpression);
                }
                List<JCStatement> statements = jcCase.getStatements();
                result = getExpressionFromStatements(statements);
                if (result != null) {
                    return result;
                }
            }
        } else if (statement instanceof JCSynchronized) {
            JCSynchronized jcSynchronized = (JCSynchronized) statement;
            JCExpression jcExpression = jcSynchronized.getExpression();
            if (isCursorInsideTree(jcExpression)) {
                return resolveExactlyExpression(jcSynchronized, jcExpression);
            }

            JCBlock block = jcSynchronized.getBlock();
            return addRootIfNeeded(
				jcSynchronized/*root*/,
				getExpressionFromStatement(block));

        } else if (statement instanceof JCThrow) {
            JCThrow jcThrow = (JCThrow) statement;
            if (isCursorInsideTree(jcThrow.getExpression())) {
                return resolveExactlyExpression(jcThrow, jcThrow.getExpression());
            }

        } else if (statement instanceof JCTry) {
            JCTry jcTry = (JCTry) statement;
            JCBlock block = jcTry.getBlock();
            result = getExpressionFromStatement(block);
            if (result != null) {
                return addRootIfNeeded(jcTry, result);
            }

            List<JCCatch> catches = jcTry.getCatches();
            for (JCCatch aCatch : catches) {
                JCVariableDecl parameter = aCatch.getParameter();
                result = getExpressionFromStatement(parameter);
                if (result == null) {
                    block = aCatch.getBlock();
                    result = getExpressionFromStatement(block);
                }
                if (result != null) {
                    result.addRoot(aCatch);
                    result.addRoot(jcTry);
                    return result;
                }
            }

            block = jcTry.getFinallyBlock();
            result = getExpressionFromStatement(block);
            return addRootIfNeeded(jcTry, result);

        } else if (statement instanceof JCVariableDecl) {
            JCVariableDecl jcVariableDecl = (JCVariableDecl) statement;
            JCExpression initializer = jcVariableDecl.getInitializer();
            if (isCursorInsideTree(initializer)) {
                return resolveExactlyExpression(jcVariableDecl, initializer);
            }
            JCExpression vartype = jcVariableDecl.vartype;
            if (isCursorInsideTree(vartype)) {
                return resolveExactlyExpression(jcVariableDecl, vartype);
            }
        } else if (statement instanceof JCWhileLoop) {
            JCWhileLoop jcWhileLoop = (JCWhileLoop) statement;
            JCExpression condition = jcWhileLoop.getCondition();
            if (isCursorInsideTree(condition)) {
                return resolveExactlyExpression(jcWhileLoop, condition);
            }

            JCStatement body = jcWhileLoop.getStatement();
            return addRootIfNeeded(
				jcWhileLoop,
				getExpressionFromStatement(body));
        }

        return null;
    }

    private Expression resolveExactlyExpression(JCTree parent, JCExpression expression) {
        if (expression instanceof JCAssign) {
            JCAssign jcAssign = (JCAssign) expression;
            JCExpression lhs = jcAssign.getVariable();
            if (isCursorInsideTree(lhs)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcAssign, lhs));
            }
            JCExpression rhs = jcAssign.getExpression();
            if (isCursorInsideTree(rhs)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcAssign, rhs));
            }
        } else if (expression instanceof JCAssignOp) {
            JCAssignOp jcAssign = (JCAssignOp) expression;
            JCExpression lhs = jcAssign.getVariable();
            if (isCursorInsideTree(lhs)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcAssign, lhs));
            }
            JCExpression rhs = jcAssign.getExpression();
            if (isCursorInsideTree(rhs)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcAssign, rhs));
            }
        } else if (expression instanceof JCBinary) {
            JCBinary jcBinary = (JCBinary) expression;
            JCExpression leftOperand = jcBinary.getLeftOperand();
            if (isCursorInsideTree(leftOperand)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcBinary, leftOperand));
            }
            JCExpression rightOperand = jcBinary.getRightOperand();
            if (isCursorInsideTree(rightOperand)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcBinary, rightOperand));
            }
        } else if (expression instanceof JCConditional) {
            JCConditional jcConditional = (JCConditional) expression;
            JCExpression condition = jcConditional.getCondition();
            if (isCursorInsideTree(condition)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcConditional, condition));
            }

            JCExpression trueExpression = jcConditional.getTrueExpression();
            if (isCursorInsideTree(trueExpression)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcConditional, trueExpression));
            }

            JCExpression falseExpression = jcConditional.getFalseExpression();
            if (isCursorInsideTree(falseExpression)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcConditional, falseExpression));
            }

        } else if (expression instanceof JCInstanceOf) {
            JCInstanceOf jcInstanceOf = (JCInstanceOf) expression;
            JCExpression lhs = jcInstanceOf.getExpression();
            if (isCursorInsideTree(lhs)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcInstanceOf, lhs));
            }
        } else if (expression instanceof JCUnary) {
            JCUnary jcUnary = (JCUnary) expression;
            JCExpression jcExpression = jcUnary.getExpression();
            if (isCursorInsideTree(jcExpression)) {
                return addRootIfNeeded(
					parent, resolveExactlyExpression(jcUnary, jcExpression));
            }
        }

        return new Expression(parent, expression);
    }

    @Nullable
    private Expression getExpressionFromClass(JCClassDecl tree) {

        if (!isCursorInsideTree(tree)) {
            return null;
        }
        Expression expression = null;
        List<JCTree> members = tree.getMembers();
        for (JCTree member : members) {
            if (member instanceof JCMethodDecl) {
                expression = getExpressionFromMethod((JCMethodDecl) member);

            } else if (member instanceof JCVariableDecl) {
                expression = getExpressionFromStatement((JCVariableDecl) member);

            } else if (member instanceof JCClassDecl) {

                expression = getExpressionFromClass((JCClassDecl) member);
            }

            if (expression != null) {
                return addRootIfNeeded(tree, expression);
            }
        }
        return null;
    }

    @Nullable
    private Expression getExpressionFromMethod(JCMethodDecl method) {
		
        Expression expression;
        List<JCVariableDecl> parameters = method.getParameters();

        for (JCVariableDecl parameter : parameters) {
            expression = getExpressionFromStatement(parameter);
            if (expression != null) {
                expression.addRoot(method);
                return expression;
            }
        }
		
        expression = getExpressionFromStatement(method.getBody());
		
        return addRootIfNeeded(method, expression);
    }

    @Nullable
    private Expression getExpressionFromStatements(
		@NonNull List<? extends JCStatement> statements) {
        for (JCStatement statement : statements) {
            Expression expression = getExpressionFromStatement(statement);
            if (expression != null) {
                return expression;
            }
        }
        return null;
    }
	
    private Expression addRootIfNeeded(JCTree root, Expression expression) {
        if (expression != null) {
            expression.addRoot(root);
            return expression;
        }
        return null;
    }
	
    private boolean isCursorInsideTree(JCTree tree) {
        if (tree == null) {
            return false;
        }
        int startPosition = tree.getStartPosition();
        int endPosition = tree.getEndPosition(mEndPositions);
        return startPosition <= mCursor && mCursor <= endPosition;
    }
}
