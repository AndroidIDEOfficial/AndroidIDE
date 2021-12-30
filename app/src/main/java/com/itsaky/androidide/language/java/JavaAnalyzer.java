/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.language.java;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

import io.github.rosemoe.editor.langs.AbstractCodeAnalyzer;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;

// TODO request language server for semantic highlights instead of waiting for it to send

/**
 * Code analyzer for the Java Language
 *
 * @author Akash Yadav
 */
public class JavaAnalyzer extends AbstractCodeAnalyzer {
    
    private HighlightRangeHelper helper;
    private Map<Integer, Map<Integer, Diagnostic>> diagnostics = new HashMap<> ();
    
    private final Map<Integer, List<Range>> stringMap = new HashMap<> ();
    
    @Override
    public void analyze (IDELanguageServer languageServer, File file, @NonNull Content content, TextAnalyzeResult colors, @NonNull TextAnalyzer.AnalyzeThread.Delegate delegate) throws Exception {
        final var stream = CharStreams.fromReader (new StringReader (content.toString ()));
        final var lexer = new JavaLexer (stream);
        final var stack = new Stack<BlockLine> ();
        
        Token token;
        int line, column, lastLine = 1, type, currSwitch = 0, maxSwitch = 0;
        boolean isFirst = true;
        
        this.stringMap.clear ();
        
        while (delegate.shouldAnalyze ()) {
            token = lexer.nextToken ();
            
            if (token == null) {
                break;
            }
            
            type = token.getType ();
            line = token.getLine () - 1;
            column = token.getCharPositionInLine ();
            
            if (type == JavaLexer.EOF) {
                lastLine = line;
                break;
            }
            
            switch (type) {
                case JavaLexer.WS:
                    if (isFirst) {
                        colors.addNormalIfNull ();
                    }
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
                case JavaLexer.VAR:
                    colors.addIfNeeded (line, column, EditorColorScheme.KEYWORD);
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
                    colors.addIfNeeded (line, column, EditorColorScheme.LITERAL);
                    break;
                case JavaLexer.STRING_LITERAL:
                    checkAndAddHexString (token, EditorColorScheme.LITERAL, colors);
                    
                    Position start = new Position (line, column);
                    Position end = new Position (line, column + token.getText ().length ());
                    colors.addStringRange (line, new Range (start, end));
                
                {
                    List<Range> ranges = stringMap.get (line);
                    if (ranges == null) {
                        ranges = new ArrayList<> ();
                    }
                    ranges.add (new Range (start, end));
                    stringMap.put (line, ranges);
                }
                break;
                case JavaLexer.LPAREN:
                case JavaLexer.RPAREN:
                case JavaLexer.LBRACK:
                case JavaLexer.RBRACK:
                case JavaLexer.SEMI:
                case JavaLexer.COMMA:
                case JavaLexer.ASSIGN:
                case JavaLexer.GT:
                case JavaLexer.LT:
                case JavaLexer.BANG:
                case JavaLexer.TILDE:
                case JavaLexer.QUESTION:
                case JavaLexer.COLON:
                case JavaLexer.EQUAL:
                case JavaLexer.GE:
                case JavaLexer.LE:
                case JavaLexer.NOTEQUAL:
                case JavaLexer.AND:
                case JavaLexer.OR:
                case JavaLexer.INC:
                case JavaLexer.DEC:
                case JavaLexer.ADD:
                case JavaLexer.SUB:
                case JavaLexer.MUL:
                case JavaLexer.DIV:
                case JavaLexer.BITAND:
                case JavaLexer.BITOR:
                case JavaLexer.CARET:
                case JavaLexer.MOD:
                case JavaLexer.ADD_ASSIGN:
                case JavaLexer.SUB_ASSIGN:
                case JavaLexer.MUL_ASSIGN:
                case JavaLexer.DIV_ASSIGN:
                case JavaLexer.AND_ASSIGN:
                case JavaLexer.OR_ASSIGN:
                case JavaLexer.XOR_ASSIGN:
                case JavaLexer.MOD_ASSIGN:
                case JavaLexer.LSHIFT_ASSIGN:
                case JavaLexer.RSHIFT_ASSIGN:
                case JavaLexer.URSHIFT_ASSIGN:
                case JavaLexer.ARROW:
                case JavaLexer.COLONCOLON:
                case JavaLexer.ELLIPSIS:
                case JavaLexer.DOT:
                    colors.addIfNeeded (line, column, EditorColorScheme.OPERATOR);
                    break;
                case JavaLexer.BOOLEAN:
                case JavaLexer.BYTE:
                case JavaLexer.CHAR:
                case JavaLexer.DOUBLE:
                case JavaLexer.ENUM:
                case JavaLexer.FLOAT:
                case JavaLexer.INT:
                case JavaLexer.LONG:
                case JavaLexer.SHORT:
                    colors.addIfNeeded (line, column, EditorColorScheme.TYPE_NAME);
                    break;
                case JavaLexer.BLOCK_COMMENT:
                case JavaLexer.LINE_COMMENT:
                    colors.addIfNeeded (line, column, EditorColorScheme.COMMENT);
                    
                    final CharPosition a = content.getIndexer ().getCharPosition (token.getStopIndex ());
                    final Position s = new Position (line, column);
                    final Position e = new Position (a.line, a.column);
                    colors.addCommentRange (line, new Range (s, e));
                    
                    String lineComment = token.getText ();
                    if (lineComment != null) {
                        if (lineComment.startsWith ("//")) {
                            lineComment = lineComment.substring (2);
                        }
                        
                        // Trim leading whitespaces
                        int startsFrom = 0;
                        for (int i = 0; i < lineComment.length (); i++) {
                            if (Character.isWhitespace (lineComment.charAt (i))) {
                                startsFrom++;
                            } else {
                                break;
                            }
                        }
                        
                        lineComment = lineComment.substring (startsFrom);
                        
                        final String text = new String (lineComment.toCharArray ());
                        lineComment = lineComment.toLowerCase (Locale.getDefault ());
                        if (lineComment.startsWith ("todo ")) {
                            diagnostics.put (line, Collections.singletonMap (column, LSPUtils.newInfoDiagnostic (line,
                                    column,
                                    token.getText ().length (),
                                    text,
                                    token.getText ())));
                        } else if (lineComment.startsWith (ConstantsBridge.CUSTOM_COMMENT_WARNING_TOKEN)) {
                            diagnostics.put (line, Collections.singletonMap (column, LSPUtils.newWarningDiagnostic (line,
                                    column,
                                    token.getText ().length (),
                                    text,
                                    token.getText ())));
                        }
                    }
                    
                    break;
                case JavaLexer.AT:
                    colors.addIfNeeded (line, column, EditorColorScheme.ANNOTATION);
                    break;
                case JavaLexer.IDENTIFIER:
                    if (helper != null && helper.isPackageName (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.PACKAGE_NAME);
                        break;
                    }
                    
                    if (helper != null && helper.isEnumType (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.ENUM_TYPE);
                        break;
                    }
                    
                    if (helper != null && helper.isClassName (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.TYPE_NAME);
                        break;
                    }
                    
                    if (helper != null && helper.isAnnotationType (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.ANNOTATION);
                        break;
                    }
                    
                    if (helper != null && helper.isInterface (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.INTERFACE);
                        break;
                    }
                    
                    if (helper != null && helper.isEnum (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.ENUM);
                        break;
                    }
                    
                    if (helper != null && helper.isStaticField (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.STATIC_FIELD);
                        break;
                    }
                    
                    if (helper != null && helper.isField (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.FIELD);
                        break;
                    }
                    
                    if (helper != null && helper.isParameter (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.PARAMETER);
                        break;
                    }
                    
                    if (helper != null && helper.isLocal (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.LOCAL_VARIABLE);
                        break;
                    }
                    
                    if (helper != null && helper.isExceptionParam (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.EXCEPTION_PARAM);
                        break;
                    }
                    
                    if (helper != null && helper.isMethodDeclaration (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.METHOD_DECLARATION);
                        break;
                    }
                    
                    if (helper != null && helper.isMethodInvocation (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.METHOD_INVOCATION);
                        break;
                    }
                    
                    if (helper != null && helper.isConstructor (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.CONSTRUCTOR);
                        break;
                    }
                    
                    if (helper != null && helper.isStaticInit (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.STATIC_INIT);
                        break;
                    }
                    
                    if (helper != null && helper.isInstanceInit (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.INSTANCE_INIT);
                        break;
                    }
                    
                    if (helper != null && helper.isTypeParam (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.TYPE_PARAM);
                        break;
                    }
                    
                    if (helper != null && helper.isResourceVariable (line, column)) {
                        colors.addIfNeeded (line, column, EditorColorScheme.RESOURCE_VARIABLE);
                        break;
                    }
                    
                    colors.addIfNeeded (line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
                case JavaLexer.LBRACE:
                    colors.addIfNeeded (line, column, EditorColorScheme.OPERATOR);
                    if (stack.isEmpty ()) {
                        if (currSwitch > maxSwitch) {
                            maxSwitch = currSwitch;
                        }
                        currSwitch = 0;
                    }
                    currSwitch++;
                    BlockLine block = colors.obtainNewBlock ();
                    block.startLine = line;
                    block.startColumn = column;
                    stack.push (block);
                    break;
                case JavaLexer.RBRACE:
                    colors.addIfNeeded (line, column, EditorColorScheme.OPERATOR);
                    if (!stack.isEmpty ()) {
                        BlockLine block2 = stack.pop ();
                        block2.endLine = line;
                        block2.endColumn = column;
                        if (block2.startLine != block2.endLine) {
                            colors.addBlockLine (block2);
                        }
                    }
                    break;
                default:
                    colors.addIfNeeded (line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
            }
            
            isFirst = false;
        }
        
        if (stack.isEmpty () && currSwitch > maxSwitch) {
            maxSwitch = currSwitch;
        }
        colors.determine (lastLine);
        colors.setSuppressSwitch (maxSwitch + 10);
    }
    
    @Override
    public void setSemanticHighlights (SemanticHighlight highlights) {
        this.helper = new HighlightRangeHelper (highlights);
        this.helper.sort ();
    }
    
    @Override
    public void updateDiagnostics (Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
        if (diagnostics == null) {
            diagnostics = new HashMap<> ();
        }
        this.diagnostics = diagnostics;
    }
    
    @Override
    public Diagnostic findDiagnosticContaining (int line, int column) {
        final Map<Integer, Diagnostic> mappedByColumn = diagnostics.get (line);
        if (mappedByColumn != null) {
            Diagnostic diag = mappedByColumn.get (column);
            if (diag != null) {
                return diag;
            } else {
                for (Map.Entry<Integer, Diagnostic> entry : mappedByColumn.entrySet ()) {
                    diag = entry.getValue ();
                    final int start = diag.getRange ().getStart ().getCharacter ();
                    final int end = diag.getRange ().getEnd ().getCharacter ();
                    
                    if (column >= start && column <= end) {
                        return diag;
                    }
                }
            }
        }
        return super.findDiagnosticContaining (line, column);
    }
    
    @Override
    public List<Diagnostic> findDiagnosticsContainingLine (int line) {
        final Map<Integer, Diagnostic> diags = diagnostics.get (line);
        if (diags == null) {
            return super.findDiagnosticsContainingLine (line);
        }
        return diags.entrySet ().stream ()
                .filter (d -> d != null && d.getValue () != null)
                .map (Map.Entry::getValue)
                .collect (Collectors.toList ());
    }
    
    @Override
    public Map<Integer, Diagnostic> getDiagnosticsAtLine (int line) {
        return diagnostics.get (line);
    }
}
