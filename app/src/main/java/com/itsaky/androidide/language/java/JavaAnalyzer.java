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
import androidx.annotation.Nullable;

import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.HighlightRangeHelper;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.AnalyzeParams;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import io.github.rosemoe.editor.langs.AbstractCodeAnalyzer;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.syntax.EditorColorScheme;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;

/**
 * Code analyzer for the Java Language
 *
 * @author Akash Yadav
 */
public class JavaAnalyzer extends AbstractCodeAnalyzer {
    
    private final Map<Integer, List<Range>> stringMap = new HashMap<> ();
    private List<DiagnosticItem> diagnostics = new ArrayList<> ();
    
    private HighlightRangeHelper helper;
    
    @Override
    public void analyze (@Nullable ILanguageServer languageServer,
                         @Nullable File file,
                         @NonNull Content content,
                         @NonNull TextAnalyzeResult colors,
                         @NonNull TextAnalyzer.AnalyzeThread.Delegate delegate
    ) throws Exception {
        
        if (languageServer != null && file != null) {
            CompletableFuture.runAsync (() -> {
                final var analyzer = languageServer.getCodeAnalyzer ();
                final var result = analyzer.analyze (new AnalyzeParams (file.toPath ()));
                
                JavaAnalyzer.this.diagnostics = result.getDiagnostics ();
                JavaAnalyzer.this.helper = new HighlightRangeHelper (result.getSemanticHighlights ());
            });
        }
        
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
                            diagnostics.add (LSPUtils.newInfoDiagnostic (line,
                                    column,
                                    token.getText ().length (),
                                    text,
                                    token.getText ()));
                        } else if (lineComment.startsWith (ConstantsBridge.CUSTOM_COMMENT_WARNING_TOKEN)) {
                            diagnostics.add (LSPUtils.newWarningDiagnostic (line,
                                    column,
                                    token.getText ().length (),
                                    text,
                                    token.getText ()));
                        }
                    }
                
                    break;
                case JavaLexer.AT:
                    colors.addIfNeeded (line, column, EditorColorScheme.ANNOTATION);
                    break;
                case JavaLexer.IDENTIFIER:
    
                    if (helper != null) {
                        if (helper.isPackageName (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.PACKAGE_NAME);
                            break;
                        }
    
                        if (helper.isEnumType (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.ENUM_TYPE);
                            break;
                        }
    
                        if (helper.isClassName (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.TYPE_NAME);
                            break;
                        }
    
                        if (helper.isAnnotationType (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.ANNOTATION);
                            break;
                        }
    
                        if (helper.isInterface (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.INTERFACE);
                            break;
                        }
    
                        if (helper.isEnum (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.ENUM);
                            break;
                        }
    
                        if (helper.isStaticField (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.STATIC_FIELD);
                            break;
                        }
    
                        if (helper.isField (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.FIELD);
                            break;
                        }
    
                        if (helper.isParameter (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.PARAMETER);
                            break;
                        }
    
                        if (helper.isLocal (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.LOCAL_VARIABLE);
                            break;
                        }
    
                        if (helper.isExceptionParam (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.EXCEPTION_PARAM);
                            break;
                        }
    
                        if (helper.isMethodDeclaration (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.METHOD_DECLARATION);
                            break;
                        }
    
                        if (helper.isMethodInvocation (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.METHOD_INVOCATION);
                            break;
                        }
    
                        if (helper.isConstructor (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.CONSTRUCTOR);
                            break;
                        }
    
                        if (helper.isStaticInit (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.STATIC_INIT);
                            break;
                        }
    
                        if (helper.isInstanceInit (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.INSTANCE_INIT);
                            break;
                        }
    
                        if (helper.isTypeParam (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.TYPE_PARAM);
                            break;
                        }
    
                        if (helper.isResourceVariable (line, column)) {
                            colors.addIfNeeded (line, column, EditorColorScheme.RESOURCE_VARIABLE);
                            break;
                        }
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
    public DiagnosticItem findDiagnosticContaining (int line, int column) {
        final var position = new Position (line, column);
        
        // binary search the diagnostic item at the given position
        // compare by search position
        int left = 0; int right = diagnostics.size () - 1; int mid;
        while (left < right) {
            mid = (left + right) / 2;
            final var diagnostic = diagnostics.get (mid);
            final var range = diagnostic.getRange ();
            final var res = range.containsForBinarySearch (position);
            if (res < 0) {
                left = mid + 1;
            } else if (res > 0) {
                right = mid - 1;
            } else {
                return diagnostic;
            }
        }
        return super.findDiagnosticContaining (line, column);
    }
    
    @Override
    public List<DiagnosticItem> findDiagnosticsContainingLine (int line) {
        return diagnostics.stream ()
                .filter (diagnostic -> diagnostic.getRange ().containsLine (line))
                .collect (Collectors.toList ());
    }
    
    private static final Logger LOG = Logger.instance ("JavaAnalyzer");
}
