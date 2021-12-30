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

package com.itsaky.androidide.language.groovy;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lexers.groovy.GroovyLexer;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Stack;

import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;

public class GroovyAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {
    
    private final EditorLanguage language;
    
    public GroovyAnalyzer (EditorLanguage language) {
        this.language = language;
    }
    
	@Override
	public void analyze(IDELanguageServer server, File file, @NonNull Content content, TextAnalyzeResult colors, @NonNull TextAnalyzer.AnalyzeThread.Delegate delegate) throws IOException {
		final var stream = CharStreams.fromReader (new StringReader (content.toString ()));
		final var lexer = new GroovyLexer (stream);
		final var stack = new Stack<BlockLine> ();
        
        Token token;
        int line, column, lastLine = 1, currSwitch = 0, maxSwitch = 0, type, previous = 0;
        boolean isFirst = true;
        
        while (delegate.shouldAnalyze()) {
            token = lexer.nextToken ();
    
            if (token == null) {
                break;
            }
            
            line = token.getLine () - 1;
            column = token.getCharPositionInLine ();
            type = token.getType ();
            
            if (type == GroovyLexer.EOF) {
                lastLine = line;
                break;
            }
    
            switch(type) {
                case GroovyLexer.WS :
                    if(isFirst) {
                        colors.addNormalIfNull();
                    }
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
                    colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
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
                    colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
                    break;
                case GroovyLexer.STRING_LITERAL :
                case GroovyLexer.SINGLE_QUOTE_STRING :
                    checkAndAddHexString (token, EditorColorScheme.LITERAL, colors);
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
                    colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
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
                    colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
                    break;
                case GroovyLexer.COMMENT :
                case GroovyLexer.LINE_COMMENT :
                    colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
                    break;
                case GroovyLexer.AT :
                    colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
                    break;
                case GroovyLexer.IDENTIFIER :
                    if (previous == GroovyLexer.AT) {
                        colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
                        break;
                    }
            
                    colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
                case GroovyLexer.LBRACE :
                    colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
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
                    colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
                    if (!stack.isEmpty()) {
                        BlockLine block2 = stack.pop();
                        block2.endLine = line;
                        block2.endColumn = column;
                        if (block2.startLine != block2.endLine)
                            colors.addBlockLine(block2);
                    }
                    break;
                default:
                    colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
            }
            
            isFirst = false;
            if (type != GroovyLexer.WS) {
                previous = type;
            }
        }
        
        if (stack.isEmpty() && currSwitch > maxSwitch) {
            maxSwitch = currSwitch;
        }

        colors.determine(lastLine);
        colors.setSuppressSwitch(maxSwitch + 10);
	}

    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
    }
}
