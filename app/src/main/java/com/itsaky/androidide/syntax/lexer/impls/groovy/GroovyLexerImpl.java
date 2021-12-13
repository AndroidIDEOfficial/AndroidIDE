/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/

package com.itsaky.androidide.syntax.lexer.impls.groovy;

import com.itsaky.androidide.lexers.groovy.GroovyLexer;
import com.itsaky.androidide.syntax.lexer.Lexer;
import com.itsaky.androidide.syntax.lexer.impls.BaseJavaLexer;
import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;
import com.itsaky.androidide.utils.LSPUtils;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextUtils;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.lsp4j.Range;

public class GroovyLexerImpl extends BaseJavaLexer implements Lexer {
    
    private final EditorLanguage language;
    private final Map<Integer, List<Range>> stringMap = new HashMap<>();
    public final MultilineStringHandler stringHandler = new MultilineStringHandler();
    
	public GroovyLexerImpl(EditorLanguage language, Content content, TextAnalyzeResult colors) {
        this.language = language;
		this.content = content;
		this.colors = colors;
		this.maxSwitch = 0;
		this.currSwitch = 0;
		this.previous = -1;
		this.wasClassName = false;
		this.isFirst = true;

		builtinTypes = new ArrayList<>();

		builtinTypes.add(GroovyLexer.BOOLEAN);
		builtinTypes.add(GroovyLexer.BYTE);
		builtinTypes.add(GroovyLexer.CHAR);
		builtinTypes.add(GroovyLexer.DOUBLE);
		builtinTypes.add(GroovyLexer.ENUM);
		builtinTypes.add(GroovyLexer.FLOAT);
		builtinTypes.add(GroovyLexer.INT);
		builtinTypes.add(GroovyLexer.LONG);
		builtinTypes.add(GroovyLexer.SHORT);
	}

	@Override
	public void init() throws IOException {
		lexer = new GroovyLexer(content.toString());
	}

	@Override
	public Token nextToken() {
		currentToken = lexer.nextToken();
		if(currentToken.getType() == GroovyLexer.EOF) {
			this.lastLine = line();
			return null;
		}
		return token(type());
	}

	@Override
	public int line() {
		// currentToken's line index starts from 1
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
        
        Span span;
		switch(tokenType) {
			case GroovyLexer.WS :
				type = TokenType.WS;
				if(isFirst)
					colors.addNormalIfNull();
				break;
			case GroovyLexer.ABSTRACT:
			case GroovyLexer.ASSERT:
			case GroovyLexer.BREAK:
			case GroovyLexer.CASE:
			case GroovyLexer.CATCH:
			case GroovyLexer.CLASS:
			case GroovyLexer.CONST:
			case GroovyLexer.CONTINUE:
			case GroovyLexer.DEFAULT:
			case GroovyLexer.DO:
			case GroovyLexer.ELSE:
			case GroovyLexer.EXTENDS:
			case GroovyLexer.FINAL:
			case GroovyLexer.FINALLY:
			case GroovyLexer.FOR:
			case GroovyLexer.IF:
			case GroovyLexer.GOTO:
			case GroovyLexer.IMPLEMENTS:
			case GroovyLexer.IMPORT:
			case GroovyLexer.INSTANCEOF:
			case GroovyLexer.INTERFACE:
			case GroovyLexer.NATIVE:
			case GroovyLexer.NEW:
			case GroovyLexer.PACKAGE:
			case GroovyLexer.PRIVATE:
			case GroovyLexer.PROTECTED:
			case GroovyLexer.PUBLIC:
			case GroovyLexer.RETURN:
			case GroovyLexer.STATIC:
			case GroovyLexer.STRICTFP:
			case GroovyLexer.SUPER:
			case GroovyLexer.SWITCH:
			case GroovyLexer.SYNCHRONIZED:
			case GroovyLexer.THIS:
			case GroovyLexer.THROW:
			case GroovyLexer.THROWS:
			case GroovyLexer.TRANSIENT:
			case GroovyLexer.TRY:
			case GroovyLexer.VOID:
			case GroovyLexer.VOLATILE:
			case GroovyLexer.WHILE:
				type = TokenType.KEYWORD;
				span = colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
				wasClassName = false;
				break;
			case GroovyLexer.DECIMAL_LITERAL:
			case GroovyLexer.HEX_LITERAL:
			case GroovyLexer.OCT_LITERAL:
			case GroovyLexer.BINARY_LITERAL:
			case GroovyLexer.FLOAT_LITERAL:
			case GroovyLexer.HEX_FLOAT_LITERAL:
			case GroovyLexer.BOOL_LITERAL:
			case GroovyLexer.CHAR_LITERAL:
			case GroovyLexer.NULL_LITERAL:
				type = TokenType.NUMBER_LITERAL;
				span = colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
				wasClassName = false;
				break;
			case GroovyLexer.STRING_LITERAL :
			case GroovyLexer.SINGLE_QUOTE_STRING :
				type = TokenType.STRING_LITERAL;
				span = colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
				wasClassName = false;
				addHexColorIfPresent(currentToken, span);
				break;
			case GroovyLexer.LPAREN :
			case GroovyLexer.RPAREN :
			case GroovyLexer.LBRACK :
			case GroovyLexer.RBRACK :
			case GroovyLexer.SEMI :
			case GroovyLexer.COMMA :
			case GroovyLexer.ASSIGN :
			case GroovyLexer.GT :
			case GroovyLexer.LT :
			case GroovyLexer.BANG :
			case GroovyLexer.TILDE :
			case GroovyLexer.QUESTION :
			case GroovyLexer.COLON :
			case GroovyLexer.EQUAL :
			case GroovyLexer.GE :
			case GroovyLexer.LE :
			case GroovyLexer.NOTEQUAL :
			case GroovyLexer.AND :
			case GroovyLexer.OR :
			case GroovyLexer.INC :
			case GroovyLexer.DEC :
			case GroovyLexer.ADD :
			case GroovyLexer.SUB :
			case GroovyLexer.MUL :
			case GroovyLexer.DIV :
			case GroovyLexer.BITAND :
			case GroovyLexer.BITOR :
			case GroovyLexer.CARET :
			case GroovyLexer.MOD :
			case GroovyLexer.ADD_ASSIGN :
			case GroovyLexer.SUB_ASSIGN :
			case GroovyLexer.MUL_ASSIGN :
			case GroovyLexer.DIV_ASSIGN :
			case GroovyLexer.AND_ASSIGN :
			case GroovyLexer.OR_ASSIGN :
			case GroovyLexer.XOR_ASSIGN :
			case GroovyLexer.MOD_ASSIGN :
			case GroovyLexer.LSHIFT_ASSIGN :
			case GroovyLexer.RSHIFT_ASSIGN :
			case GroovyLexer.URSHIFT_ASSIGN :
			case GroovyLexer.ARROW :
			case GroovyLexer.COLONCOLON :
			case GroovyLexer.ELLIPSIS :
			case GroovyLexer.DOT :
				type = TokenType.OPERATOR;
				span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				wasClassName = false;
				break;
			case GroovyLexer.BOOLEAN:
			case GroovyLexer.BYTE :
			case GroovyLexer.CHAR:
			case GroovyLexer.DOUBLE:
			case GroovyLexer.ENUM :
			case GroovyLexer.FLOAT:
			case GroovyLexer.INT :
			case GroovyLexer.LONG:
			case GroovyLexer.SHORT:
				type = TokenType.TYPE;
				span = colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
				wasClassName = true;
				break;

			case GroovyLexer.COMMENT :
			case GroovyLexer.LINE_COMMENT :
				type = TokenType.COMMENT;
				span = colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
				wasClassName = false;
				break;
			case GroovyLexer.AT :
				type = TokenType.ANNOTATION;
				span = colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
				wasClassName = false;
				break;
			case GroovyLexer.IDENTIFIER :
				type = TokenType.IDENTIFIER;

				if (previous == GroovyLexer.AT) {
					span = colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
					wasClassName = false;
					break;
				}

				if ((previous == GroovyLexer.IDENTIFIER || builtinTypes.contains(previous)) && wasClassName) {
					span = colors.addIfNeeded(line, column, EditorColorScheme.LOCAL_VARIABLE);
					wasClassName = false;
					break;
				}

				span = colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
				break;
			case GroovyLexer.LBRACE :
				type = TokenType.OPERATOR;
				span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
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
			case GroovyLexer.RBRACE :
				type = TokenType.OPERATOR;
				span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
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
				if (tokenType == GroovyLexer.LBRACK || (tokenType == GroovyLexer.RBRACK && previous == GroovyLexer.LBRACK)) {
					span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
					break;
				}
				span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
				break;
		}

		isFirst = false;
		if(tokenType != GroovyLexer.WS) {
			previous = tokenType;
		}

		return type;
	}
    
    private class MultilineStringHandler implements NewlineHandler {

        @Override
        public boolean matchesRequirement(String beforeText, String afterText, CharPosition cursor) {
            if(language == null || stringMap == null || stringMap.isEmpty()) {
                return false;
            }

            final List<Range> ranges = stringMap.get(cursor.line);
            if(ranges == null || ranges.isEmpty()) {
                return false;
            }

            return isInRanges(ranges, cursor.line, cursor.column);
        }

        @Override
        public NewlineHandler.HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
            int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
            int advance = language.getIndentAdvance(beforeText) + 1;
            final StringBuilder sb = new StringBuilder("\\n\" + \n") // Appends: \n" + <new-line>
                .append(TextUtils.createIndent(count + advance, tabSize, language.useTab()))
                .append("\"");
            return new HandleResult(sb, 0);
        }

        private boolean isInRanges (List<Range> ranges, int line, int column) {

            if(ranges == null || ranges.isEmpty()) {
                return false;
            }

            for (Range range : ranges) {
                if (LSPUtils.isInRange(range, line, column)) {
                    return true;
                }
            }

            return false;
        }
    }
}
