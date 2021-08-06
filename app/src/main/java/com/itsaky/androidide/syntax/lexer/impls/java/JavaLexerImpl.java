package com.itsaky.androidide.syntax.lexer.impls.java;

import com.itsaky.androidide.language.java.parser.JavaLexer;
import com.itsaky.androidide.syntax.lexer.Lexer;
import com.itsaky.androidide.syntax.lexer.impls.BaseJavaLexer;
import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;
import com.itsaky.lsp.JavaColors;
import com.itsaky.lsp.Range;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;

public class JavaLexerImpl extends BaseJavaLexer implements Lexer {
	
    private JavaColors javaColors;
    
	public JavaLexerImpl(String content, TextAnalyzeResult colors) {
		this.content = content;
		this.colors = colors;
		this.maxSwitch = 0;
		this.currSwitch = 0;
		this.previous = -1;
		this.wasClassName = false;
		this.isFirst = true;
		
		builtinTypes = new ArrayList<>();

		builtinTypes.add(JavaLexer.BOOLEAN);
		builtinTypes.add(JavaLexer.BYTE);
		builtinTypes.add(JavaLexer.CHAR);
		builtinTypes.add(JavaLexer.DOUBLE);
		builtinTypes.add(JavaLexer.ENUM);
		builtinTypes.add(JavaLexer.FLOAT);
		builtinTypes.add(JavaLexer.INT);
		builtinTypes.add(JavaLexer.LONG);
		builtinTypes.add(JavaLexer.SHORT);
	}
    
    public void setJavaColors(JavaColors colors) {
        this.javaColors = colors;
    }
    
	@Override
	public void init() throws IOException {
		lexer = new JavaLexer(CharStreams.fromReader(new StringReader(content)));
	}
	
	@Override
	public Token nextToken() {
		currentToken = lexer.nextToken();
		if(currentToken.getType() == JavaLexer.EOF) {
			this.lastLine = line();
			return null;
		}
		return token(type());
	}

	@Override
	public int line() {
		// currentToken's line index starts from 1
        // so we reduce it by 1
		int line = currentToken.getLine() - 1;
		this.lastLine = line;
		return line;
	}

	@Override
	public int column() {
		return currentToken.getCharPositionInLine();
	}

	@Override
	public String text() {
		return currentToken.getText();
	}

	@Override
	public TokenType type() {
		final int line = line();
		final int column = column();
		final int tokenType = currentToken.getType();
		TokenType type = TokenType.TEXT;
		switch(tokenType) {
			case JavaLexer.WS :
				type = TokenType.WS;
				if(isFirst)
					colors.addNormalIfNull();
				break;
			case JavaLexer.ABSTRACT:
			case JavaLexer.ASSERT:
			case JavaLexer.BREAK:
			case JavaLexer.CASE:
			case JavaLexer.CATCH:
			case JavaLexer.CLASS:
			case JavaLexer.CONST:
			case JavaLexer.CONTINUE:
			case JavaLexer.DEFAULT:
			case JavaLexer.DO:
			case JavaLexer.ELSE:
			case JavaLexer.EXTENDS:
			case JavaLexer.FINAL:
			case JavaLexer.FINALLY:
			case JavaLexer.FOR:
			case JavaLexer.IF:
			case JavaLexer.GOTO:
			case JavaLexer.IMPLEMENTS:
			case JavaLexer.IMPORT:
			case JavaLexer.INSTANCEOF:
			case JavaLexer.INTERFACE:
			case JavaLexer.NATIVE:
			case JavaLexer.NEW:
			case JavaLexer.PACKAGE:
			case JavaLexer.PRIVATE:
			case JavaLexer.PROTECTED:
			case JavaLexer.PUBLIC:
			case JavaLexer.RETURN:
			case JavaLexer.STATIC:
			case JavaLexer.STRICTFP:
			case JavaLexer.SUPER:
			case JavaLexer.SWITCH:
			case JavaLexer.SYNCHRONIZED:
			case JavaLexer.THIS:
			case JavaLexer.THROW:
			case JavaLexer.THROWS:
			case JavaLexer.TRANSIENT:
			case JavaLexer.TRY:
			case JavaLexer.VOID:
			case JavaLexer.VOLATILE:
			case JavaLexer.WHILE:
				type = TokenType.KEYWORD;
				colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
				wasClassName = false;
				break;
			case JavaLexer.DECIMAL_LITERAL:
			case JavaLexer.HEX_LITERAL:
			case JavaLexer.OCT_LITERAL:
			case JavaLexer.BINARY_LITERAL:
			case JavaLexer.FLOAT_LITERAL:
			case JavaLexer.HEX_FLOAT_LITERAL:
			case JavaLexer.BOOL_LITERAL:
			case JavaLexer.CHAR_LITERAL:
			case JavaLexer.NULL_LITERAL:
				type = TokenType.NUMBER_LITERAL;
				colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
				wasClassName = false;
				break;
			case JavaLexer.STRING_LITERAL :
				type = TokenType.STRING_LITERAL;
				colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
				wasClassName = false;
				addHexColorIfPresent();
				break;
			case JavaLexer.LPAREN :
			case JavaLexer.RPAREN :
			case JavaLexer.LBRACK :
			case JavaLexer.RBRACK :
			case JavaLexer.SEMI :
			case JavaLexer.COMMA :
			case JavaLexer.ASSIGN :
			case JavaLexer.GT :
			case JavaLexer.LT :
			case JavaLexer.BANG :
			case JavaLexer.TILDE :
			case JavaLexer.QUESTION :
			case JavaLexer.COLON :
			case JavaLexer.EQUAL :
			case JavaLexer.GE :
			case JavaLexer.LE :
			case JavaLexer.NOTEQUAL :
			case JavaLexer.AND :
			case JavaLexer.OR :
			case JavaLexer.INC :
			case JavaLexer.DEC :
			case JavaLexer.ADD :
			case JavaLexer.SUB :
			case JavaLexer.MUL :
			case JavaLexer.DIV :
			case JavaLexer.BITAND :
			case JavaLexer.BITOR :
			case JavaLexer.CARET :
			case JavaLexer.MOD :
			case JavaLexer.ADD_ASSIGN :
			case JavaLexer.SUB_ASSIGN :
			case JavaLexer.MUL_ASSIGN :
			case JavaLexer.DIV_ASSIGN :
			case JavaLexer.AND_ASSIGN :
			case JavaLexer.OR_ASSIGN :
			case JavaLexer.XOR_ASSIGN :
			case JavaLexer.MOD_ASSIGN :
			case JavaLexer.LSHIFT_ASSIGN :
			case JavaLexer.RSHIFT_ASSIGN :
			case JavaLexer.URSHIFT_ASSIGN :
			case JavaLexer.ARROW :
			case JavaLexer.COLONCOLON :
			case JavaLexer.ELLIPSIS :
			case JavaLexer.DOT :
				type = TokenType.OPERATOR;
				colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				wasClassName = false;
				break;
			case JavaLexer.BOOLEAN:
			case JavaLexer.BYTE :
			case JavaLexer.CHAR:
			case JavaLexer.DOUBLE:
			case JavaLexer.ENUM :
			case JavaLexer.FLOAT:
			case JavaLexer.INT :
			case JavaLexer.LONG:
			case JavaLexer.SHORT:
				type = TokenType.TYPE;
				colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
				wasClassName = true;
				break;

			case JavaLexer.COMMENT :
			case JavaLexer.LINE_COMMENT :
				type = TokenType.COMMENT;
				colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
				wasClassName = false;
				break;
			case JavaLexer.AT :
				type = TokenType.ANNOTATION;
				colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
				wasClassName = false;
				break;
			case JavaLexer.IDENTIFIER :
				type = TokenType.IDENTIFIER;
				
                if(isPackageName(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.PACKAGE_NAME);
                    break;
                }
                
                if(isEnumType(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.ENUM_TYPE);
                    break;
                }
                
                if(isClassName(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
                    break;
                }
                
                if(isAnnotationType(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
                    break;
                }
                
                if(isInterface(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.INTERFACE);
                    break;
                }
                
                if(isEnum(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.ENUM);
                    break;
                }
                
                if(isStaticField(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.STATIC_FIELD);
                    break;
                }
                
                if(isField(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.FIELD);
                    break;
                }
                
                if(isParameter(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.PARAMETER);
                    break;
                }
                
                if(isLocal(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.LOCAL_VARIABLE);
                    break;
                }
                
                if(isExceptionParam(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.EXCEPTION_PARAM);
                    break;
                }
                
                if(isMethod(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.METHOD);
                    break;
                }
                
                if(isConstructor(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.CONSTRUCTOR);
                    break;
                }
                
                if(isStaticInit(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.STATIC_INIT);
                    break;
                }
                
                if(isInstanceInit(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.INSTANCE_INIT);
                    break;
                }
                
                if(isTypeParam(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.TYPE_PARAM);
                    break;
                }
                
                if(isResourceVariable(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.RESOURCE_VARIABLE);
                    break;
                }
                
				colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
				break;
			case JavaLexer.LBRACE :
				type = TokenType.OPERATOR;
				colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				wasClassName = false;
				if (stack.isEmpty()) {
					if (currSwitch > maxSwitch)
						maxSwitch = currSwitch;
					currSwitch = 0;
				}
				currSwitch++;
				BlockLine block = colors.obtainNewBlock();
				block.startLine = line;
				block.startColumn = column;
				stack.push(block);
				break;
			case JavaLexer.RBRACE :
				type = TokenType.OPERATOR;
				colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				wasClassName = false;
				if (!stack.isEmpty()) {
					BlockLine block2 = stack.pop();
					block2.endLine = line;
					block2.endColumn = column;
					if (block2.startLine != block2.endLine)
						colors.addBlockLine(block2);
				}
				break;
			default:
				type = TokenType.TEXT;
				wasClassName = false;
				if (tokenType == JavaLexer.LBRACK || (tokenType == JavaLexer.RBRACK && previous == JavaLexer.LBRACK)) {
					colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
					break;
				}
				colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				break;
		}
		
		isFirst = false;
		if(tokenType != JavaLexer.WS) {
			previous = tokenType;
		}
		 
		return type;
	}

    private boolean isEnumType(int line, int column) {
        if(javaColors == null || javaColors.enumTypes == null) return false;
        return isInRange(javaColors.enumTypes, line, column);
    }

    private boolean isAnnotationType(int line, int column) {
        if(javaColors == null || javaColors.annotationTypes == null) return false;
        return isInRange(javaColors.annotationTypes, line, column);
    }

    private boolean isInterface(int line, int column) {
        if(javaColors == null || javaColors.interfaces == null) return false;
        return isInRange(javaColors.interfaces, line, column);
    }

    private boolean isEnum(int line, int column) {
        if(javaColors == null || javaColors.enums == null) return false;
        return isInRange(javaColors.enums, line, column);
    }

    private boolean isParameter(int line, int column) {
        if(javaColors == null || javaColors.parameters == null) return false;
        return isInRange(javaColors.parameters, line, column);
    }

    private boolean isExceptionParam(int line, int column) {
        if(javaColors == null || javaColors.exceptionParams == null) return false;
        return isInRange(javaColors.exceptionParams, line, column);
    }

    private boolean isConstructor(int line, int column) {
        if(javaColors == null || javaColors.constructors == null) return false;
        return isInRange(javaColors.constructors, line, column);
    }

    private boolean isStaticInit(int line, int column) {
        if(javaColors == null || javaColors.staticInits == null) return false;
        return isInRange(javaColors.staticInits, line, column);
    }

    private boolean isInstanceInit(int line, int column) {
        if(javaColors == null || javaColors.instanceInits == null) return false;
        return isInRange(javaColors.instanceInits, line, column);
    }

    private boolean isTypeParam(int line, int column) {
        if(javaColors == null || javaColors.typeParams == null) return false;
        return isInRange(javaColors.typeParams, line, column);
    }

    private boolean isResourceVariable(int line, int column) {
        if(javaColors == null || javaColors.resourceVariables == null) return false;
        return isInRange(javaColors.resourceVariables, line, column);
    }
    
    private boolean isPackageName(int line, int column) {
        if(javaColors == null || javaColors.packages == null) return false;
        return isInRange(javaColors.packages, line, column);
    }
    
    private boolean isClassName(int line, int column) {
        if(javaColors == null || javaColors.classNames == null) return false;
        return isInRange(javaColors.classNames, line, column);
    }
    
    private boolean isField(int line, int column) {
        if(javaColors == null || javaColors.fields == null) return false;
        return isInRange(javaColors.fields, line, column);
    }
    
    private boolean isStaticField(int line, int column) {
        if(javaColors == null || javaColors.statics == null) return false;
        return isInRange(javaColors.statics, line, column);
    }
    
    private boolean isMethod(int line, int column) {
        if(javaColors == null || javaColors.methods == null) return false;
        return isInRange(javaColors.methods, line, column);
    }
    
    private boolean isLocal(int line, int column) {
        if(javaColors == null || javaColors.locals == null) return false;
        return isInRange(javaColors.locals, line, column);
    }
    
    private boolean isInRange(List<Range> ranges, int line, int column) {
        if(ranges != null && ranges.size() > 0) {
            for(int i=0;i<ranges.size();i++) {
                final Range range = ranges.get(i);
                if(range == null) continue;
                if(range.start.line == line && range.start.character == column)
                    return true;
            }
        } 
        return false;
    }
}
