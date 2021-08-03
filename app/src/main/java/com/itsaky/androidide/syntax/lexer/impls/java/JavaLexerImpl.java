package com.itsaky.androidide.syntax.lexer.impls.java;

import com.itsaky.androidide.language.java.parser.JavaLexer;
import com.itsaky.androidide.syntax.lexer.Lexer;
import com.itsaky.androidide.syntax.lexer.impls.BaseJavaLexer;
import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;
import com.itsaky.lsp.Range;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import com.itsaky.lsp.JavaColors;

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
				colors.addIfNeeded(line, column, EditorColorScheme.IDENTIFIER_NAME);
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
				
				if (previous == JavaLexer.AT) {
					colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
					wasClassName = false;
					break;
				}

				if ((previous == JavaLexer.IDENTIFIER || builtinTypes.contains(previous)) && wasClassName) {
					colors.addIfNeeded(line, column, EditorColorScheme.IDENTIFIER_VAR);
					wasClassName = false;
					break;
				}
                
                if(isClassName(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.IDENTIFIER_NAME);
                    break;
                }
                
                if(isFieldOrStatic(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.FIELD);
                    break;
                }
                
                if(isPackageName(line, column)) {
                    colors.addIfNeeded(line, column, EditorColorScheme.PACKAGE_NAME);
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
    
    private boolean isPackageName(int line, int column) {
        if(javaColors == null || javaColors.packages == null) return false;
        final List<Range> packages = javaColors.packages;
        if(packages != null && packages.size() > 0) {
            for(int i=0;i<packages.size();i++) {
                final Range range = packages.get(i);
                if(range == null) continue;
                if(range.start.line == line && range.start.character == column)
                    return true;
            }
        }

        return false;
    }
    
    private boolean isClassName(int line, int column) {
        if(javaColors == null || javaColors.classNames == null) return false;
        final List<Range> names = javaColors.classNames;
        if(names != null && names.size() > 0) {
            for(int i=0;i<names.size();i++) {
                final Range range = names.get(i);
                if(range == null) continue;
                if(range.start.line == line && range.start.character == column)
                    return true;
            }
        }
        
        return false;
    }

    private boolean isFieldOrStatic(int line, int column) {
        return isField(line, column) || isStatic(line, column);
    }
    
    private boolean isField(int line, int column) {
        if(javaColors == null || javaColors.fields == null) return false;
        final List<Range> fields = javaColors.fields;
        if(fields != null && fields.size() > 0) {
            for(int i=0;i<fields.size();i++) {
                final Range range = fields.get(i);
                if(range == null) continue;
                if(range.start.line == line && range.start.character == column)
                    return true;
            }
        }
        return false;
    }
    
    private boolean isStatic(int line, int column) {
        if(javaColors == null || javaColors.statics == null) return false;
        final List<Range> statics = javaColors.statics;
        if(statics != null && statics.size() > 0) {
            for(int i=0;i<statics.size();i++) {
                final Range range = statics.get(i);
                if(range == null) continue;
                if(range.start.line == line && range.start.character == column)
                    return true;
            }
        } 
        return false;
    }
}
