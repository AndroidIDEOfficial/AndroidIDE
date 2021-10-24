package com.itsaky.androidide.syntax.lexer.impls.java;

import androidx.core.util.Pair;
import com.itsaky.androidide.antlr4.java.JavaLexer;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.syntax.lexer.Lexer;
import com.itsaky.androidide.syntax.lexer.impls.BaseJavaLexer;
import com.itsaky.androidide.syntax.lexer.tokens.Token;
import com.itsaky.androidide.syntax.lexer.tokens.TokenType;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageServer;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.Indexer;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.CharStreams;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

public class JavaLexerImpl extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {

    private HighlightRangeHelper helper;
    private Map<Integer, Map<Integer, Diagnostic>> diagnostics = new HashMap<>();
    private final Map<Integer, Map<Integer, Diagnostic>> customDiagnostics = new HashMap<>();
    
    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
        this.helper = new HighlightRangeHelper(highlights);
        this.helper.sort();
    }
    
    @Override
    public void analyze(IDELanguageServer languageServer, File file, CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) throws Exception {
        final JavaLexerAnalyzer lexer = new JavaLexerAnalyzer((Content) content, colors, this.helper);
        lexer.init();
        while (delegate.shouldAnalyze()) {
            // null = EOF
            if(lexer.nextToken() == null)
                break;
        }
        if (lexer.stack.isEmpty() && lexer.currSwitch > lexer.maxSwitch)
            lexer.maxSwitch = lexer.currSwitch;

        colors.determine(lexer.lastLine);
        colors.setSuppressSwitch(lexer.maxSwitch + 10);
        colors.setHexColors(lexer.lineColors);
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
        if(diagnostics == null) {
            diagnostics = new HashMap<>();
        }
        this.diagnostics = diagnostics;
    }

    @Override
    public Diagnostic findDiagnosticContaining(int line, int column) {
        final Map<Integer, Diagnostic> mappedByColumn = diagnostics.get(line);
        if(mappedByColumn != null) {
            Diagnostic diag = mappedByColumn.get(column);
            if(diag != null) {
                return diag;
            } else {
                for(Map.Entry<Integer, Diagnostic> entry : mappedByColumn.entrySet()) {
                    diag = entry.getValue();
                    final int start = diag.getRange().getStart().getCharacter();
                    final int end = diag.getRange().getEnd().getCharacter();
                    
                    if(column >= start && column <= end) {
                        return diag;
                    }
                }
            }
        }
        return super.findDiagnosticContaining(line, column);
    }

    @Override
    public List<Diagnostic> findDiagnosticsContainingLine(int line) {
        final Map<Integer, Diagnostic> diags = diagnostics.get(line);
        if(diags == null) {
            return super.findDiagnosticsContainingLine(line);
        }
        return diags.entrySet().stream()
            .filter(d -> d != null && d.getValue() != null)
            .map(d -> d.getValue())
            .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Diagnostic> getDiagnosticsAtLine(int line) {
        return diagnostics.get(line);
    }
    
    public class JavaLexerAnalyzer extends BaseJavaLexer implements Lexer {
        
        private final HighlightRangeHelper helper;
        
        public JavaLexerAnalyzer(Content content, TextAnalyzeResult colors, HighlightRangeHelper helper) {
            this.content = content;
            this.colors = colors;
            this.helper = helper;
            this.maxSwitch = 0;
            this.currSwitch = 0;
            this.previous = -1;
            this.wasClassName = false;
            this.isFirst = true;
        }
        
        @Override
        public void init() throws IOException {
            lexer = new JavaLexer(CharStreams.fromReader(new StringReader(content.toString())));
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
            
            Span span = null;
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
                case JavaLexer.VAR :
                    type = TokenType.KEYWORD;
                    span = colors.addIfNeeded(line, column, EditorColorScheme.KEYWORD);
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
                    span = colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
                    wasClassName = false;
                    break;
                case JavaLexer.STRING_LITERAL :
                    type = TokenType.STRING_LITERAL;
                    span = colors.addIfNeeded(line, column, EditorColorScheme.LITERAL);
                    wasClassName = false;

                    Position start = new Position(line, column);
                    Position end = new Position(line, column + currentToken.getText().length());
                    colors.addStringRange(line, new Range(start, end));

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
                    span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
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
                    span = colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
                    wasClassName = true;
                    break;
                case JavaLexer.BLOCK_COMMENT :
                    
                    /**
                     * Comment span must be initially added to
                     * highlight '/**' and starting comment text as a comment.
                     */
                    type = TokenType.COMMENT;
                    span = colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
                    wasClassName = false;
                    
                    // Walk through the comment
                    final Indexer indexer = content.getIndexer();
                    final CharPosition endPos = indexer.getCharPosition(currentToken.getStopIndex());
                    
                    int l = line;
                    int c = column;
                    
                    int el = -1;
                    int ec = -1;
                    
                    try {
                        scan_comment: while (helper != null) {
                            if(l == endPos.line && c == endPos.column) {
                                // Reached end of comment
                                // Nothing to search for
                                break scan_comment;
                            }

                            final Pair<Integer, Range> result = helper.findJavadocColorSchemeId(l, c);
                            final int colorId = result.first;
                            final Range range = result.second;
                            if(colorId != HighlightRangeHelper.NOT_FOUND) {

                                // We found a valid start position of a JavaDoc element
                                // Add it to the scheme
                                colors.addIfNeeded(l, c, colorId);

                                // Store end line and column
                                el = range.getEnd().getLine();
                                ec = range.getEnd().getCharacter();
                                
                                if(el >= content.getLineCount()) {
                                    break scan_comment;
                                }
                                
                                // Skip to the end line and column
                                l = el;
                                c = ec;
                                
                                // Again add the normal comment highlight
                                // After the javadoc element
                                if(ec >= content.getColumnCount(el)) {
                                    el ++;
                                    ec = 0;
                                }
                                
                                colors.addIfNeeded(el, ec, EditorColorScheme.COMMENT);
                            }

                            c ++;
                            if(c >= content.getColumnCount(l)) {
                                l ++;
                                c = 0;
                            }
                        }
                    } catch (Throwable th) {
                        LOG.error("Error while highlighting comment", th);
                    }
                    
                    break;
                case JavaLexer.LINE_COMMENT :
                    type = TokenType.COMMENT;
                    span = colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
                    wasClassName = false;
                    
                    String lineComment = currentToken.getText();
                    if(lineComment != null) {
                        lineComment = lineComment.trim();
                        if(lineComment.startsWith("//")) {
                            lineComment = lineComment.substring(2).trim();
                        }
                        final String text = new String (lineComment.toCharArray());
                        lineComment = lineComment.toLowerCase(Locale.getDefault());
                        if(lineComment.startsWith("todo ")) {
                            diagnostics.put(line, Collections.singletonMap(column, LSPUtils.newInfoDiagnostic(line, column, currentToken.getText().length(), text, currentToken.getText())));
                        } else if (lineComment.startsWith(ConstantsBridge.CUSTOM_COMMENT_WARNING_TOKEN)) {
                            diagnostics.put(line, Collections.singletonMap(column, LSPUtils.newWarningDiagnostic(line, column, currentToken.getText().length(), text, currentToken.getText())));
                        }
                    }
                    
                    break;
                case JavaLexer.AT :
                    type = TokenType.ANNOTATION;
                    span = colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
                    wasClassName = false;
                    break;
                case JavaLexer.IDENTIFIER :
                    type = TokenType.IDENTIFIER;

                    if(helper != null && helper.isPackageName(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.PACKAGE_NAME);
                        break;
                    }

                    if(helper != null && helper.isEnumType(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.ENUM_TYPE);
                        break;
                    }

                    if(helper != null && helper.isClassName(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.TYPE_NAME);
                        break;
                    }

                    if(helper != null && helper.isAnnotationType(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.ANNOTATION);
                        break;
                    }

                    if(helper != null && helper.isInterface(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.INTERFACE);
                        break;
                    }

                    if(helper != null && helper.isEnum(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.ENUM);
                        break;
                    }

                    if(helper != null && helper.isStaticField(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.STATIC_FIELD);
                        break;
                    }

                    if(helper != null && helper.isField(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.FIELD);
                        break;
                    }

                    if(helper != null && helper.isParameter(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.PARAMETER);
                        break;
                    }

                    if(helper != null && helper.isLocal(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.LOCAL_VARIABLE);
                        break;
                    }

                    if(helper != null && helper.isExceptionParam(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.EXCEPTION_PARAM);
                        break;
                    }

                    if(helper != null && helper.isMethodDeclaration(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.METHOD_DECLARATION);
                        break;
                    }
                    
                    if(helper != null && helper.isMethodInvocation(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.METHOD_INVOCATION);
                        break;
                    }

                    if(helper != null && helper.isConstructor(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.CONSTRUCTOR);
                        break;
                    }

                    if(helper != null && helper.isStaticInit(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.STATIC_INIT);
                        break;
                    }

                    if(helper != null && helper.isInstanceInit(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.INSTANCE_INIT);
                        break;
                    }

                    if(helper != null && helper.isTypeParam(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.TYPE_PARAM);
                        break;
                    }

                    if(helper != null && helper.isResourceVariable(line, column)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.RESOURCE_VARIABLE);
                        break;
                    }

                    span = colors.addIfNeeded(line, column, EditorColorScheme.TEXT_NORMAL);
                    break;
                case JavaLexer.LBRACE :
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
                case JavaLexer.RBRACE :
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
                    if (tokenType == JavaLexer.LBRACK || (tokenType == JavaLexer.RBRACK && previous == JavaLexer.LBRACK)) {
                        span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
                        break;
                    }
                    span = colors.addIfNeeded(line, column, EditorColorScheme.OPERATOR);
                    break;
            }

            isFirst = false;
            if(tokenType != JavaLexer.WS) {
                previous = tokenType;
            }
            
            return type;
        }
    }
    
    private static final Logger LOG = Logger.instance("JavaLexerImpl");
}
