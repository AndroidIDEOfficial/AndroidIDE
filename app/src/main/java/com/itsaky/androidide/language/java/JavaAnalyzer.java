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

import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.ANNOTATION;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TYPE_NAME;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.get;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.withoutCompletion;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticResult;
import com.itsaky.lsp.models.DiagnosticSeverity;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.lang.styling.Styles;

/**
 * Code analyzer for the Java Language
 *
 * @author Akash Yadav
 */
public class JavaAnalyzer extends SimpleAnalyzeManager<Void> {

    private static final Logger LOG = Logger.instance("JavaAnalyzer");
    private final List<DiagnosticItem> ideDiagnostics = new ArrayList<>();
    private final ILanguageServer languageServer;
    private List<DiagnosticItem> diagnostics = new ArrayList<>();

    public JavaAnalyzer(ILanguageServer languageServer) {
        this.languageServer = languageServer;
    }

    @NonNull
    public List<DiagnosticItem> getDiagnostics() {
        final var result = new ArrayList<>(ideDiagnostics);
        if (diagnostics != null && !diagnostics.isEmpty()) {
            result.addAll(diagnostics);
        }
        result.sort(DiagnosticItem.START_COMPARATOR);
        return result;
    }

    @Override
    protected Styles analyze(StringBuilder text, Delegate<Void> delegate) {
        final var args = getExtraArguments();
        if (args.containsKey(IDEEditor.KEY_FILE)) {
            final var file = new File(args.getString(IDEEditor.KEY_FILE));
            if (languageServer != null && file.exists()) {
                CompletableFuture.runAsync(
                        () -> {
                            diagnostics = languageServer.analyze(file.toPath());
                            if (languageServer.getClient() != null) {
                                languageServer
                                        .getClient()
                                        .publishDiagnostics(
                                                new DiagnosticResult(file.toPath(), diagnostics));
                            }
                        });
            }
        }

        final CodePointCharStream stream;
        final var styles = new Styles();
        final var colors = new MappedSpans.Builder();
        try {
            stream = CharStreams.fromReader(new CharSequenceReader(text));
        } catch (IOException e) {
            LOG.error("Unable to create char stream for text", e);
            return styles;
        }
        final var lexer = new JavaLexer(stream);
        final var stack = new Stack<CodeBlock>();

        Token token;
        int line, column, lastLine = 1, type, currSwitch = 0, maxSwitch = 0, previous = -1;
        boolean isFirst = true;

        ideDiagnostics.clear();

        while (!delegate.isCancelled()) {
            token = lexer.nextToken();

            if (token == null) {
                break;
            }

            type = token.getType();
            line = token.getLine() - 1;
            column = token.getCharPositionInLine();

            if (type == JavaLexer.EOF) {
                lastLine = line;
                break;
            }

            final var tokenLength = token.getText().length();
            switch (type) {
                case JavaLexer.WS:
                    if (isFirst) {
                        colors.addNormalIfNull();
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
                    colors.addIfNeeded(line, column, forKeyword());
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
                    colors.addIfNeeded(line, column, get(LITERAL));
                    break;
                case JavaLexer.STRING_LITERAL:
                    colors.addIfNeeded(line, column, forString());
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
                    colors.addIfNeeded(line, column, get(OPERATOR));
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
                    colors.addIfNeeded(line, column, get(TYPE_NAME));
                    break;
                case JavaLexer.BLOCK_COMMENT:
                    colors.addIfNeeded(line, column, forComment());
                    break;
                case JavaLexer.LINE_COMMENT:
                    var commentType = SchemeAndroidIDE.COMMENT;

                    // highlight special line comments
                    var commentText = token.getText();
                    if (commentText.length() > 2) {
                        commentText = commentText.substring(2);
                        commentText = commentText.trim();

                        var mark = true;
                        if ("todo".equalsIgnoreCase(commentText.substring(0, 4))) {
                            commentType = SchemeAndroidIDE.TODO_COMMENT;
                        } else if ("fixme".equalsIgnoreCase(commentText.substring(0, 5))) {
                            commentType = SchemeAndroidIDE.FIXME_COMMENT;
                        } else {
                            mark = false;
                        }

                        if (mark) {
                            if (diagnostics == null) {
                                diagnostics = new ArrayList<>();
                            }
                            final var diagnostic = new DiagnosticItem();
                            diagnostic.setSeverity(DiagnosticSeverity.WARNING);
                            diagnostic.setMessage(commentText);
                            diagnostic.setCode("special.comment");
                            diagnostic.setRange(
                                    new Range(
                                            new Position(line, column),
                                            new Position(line, column + tokenLength)));
                            diagnostic.setSource(commentText);
                            ideDiagnostics.add(diagnostic);
                        }
                    }

                    colors.addIfNeeded(line, column, withoutCompletion(commentType));

                    break;
                case JavaLexer.AT:
                    colors.addIfNeeded(line, column, get(ANNOTATION));
                    break;
                case JavaLexer.IDENTIFIER:
                    int colorId = SchemeAndroidIDE.TEXT_NORMAL;
                    if (previous == JavaLexer.AT) {
                        colorId = SchemeAndroidIDE.ANNOTATION;
                    }
                    colors.addIfNeeded(line, column, get(colorId));
                    break;
                case JavaLexer.LBRACE:
                    colors.addIfNeeded(line, column, get(OPERATOR));
                    if (stack.isEmpty()) {
                        if (currSwitch > maxSwitch) {
                            maxSwitch = currSwitch;
                        }
                        currSwitch = 0;
                    }
                    currSwitch++;
                    var block = styles.obtainNewBlock();
                    block.startLine = line;
                    block.startColumn = column;
                    stack.push(block);
                    break;
                case JavaLexer.RBRACE:
                    colors.addIfNeeded(line, column, get(OPERATOR));
                    if (!stack.isEmpty()) {
                        var block2 = stack.pop();
                        block2.endLine = line;
                        block2.endColumn = column;
                        if (block2.startLine != block2.endLine) {
                            styles.addCodeBlock(block2);
                        }
                    }
                    break;
                default:
                    colors.addIfNeeded(line, column, get(TEXT_NORMAL));
                    break;
            }

            isFirst = false;

            if (type != JavaLexer.WS) {
                previous = type;
            }
        }

        if (stack.isEmpty() && currSwitch > maxSwitch) {
            maxSwitch = currSwitch;
        }

        colors.determine(lastLine);
        styles.setSuppressSwitch(maxSwitch + 10);

        if (diagnostics != null && !diagnostics.isEmpty()) {
            markDiagnostics(colors, diagnostics);
        }

        if (!ideDiagnostics.isEmpty()) {
            markDiagnostics(colors, ideDiagnostics);
        }

        styles.spans = colors.build();

        return styles;
    }

    private void markDiagnostics(MappedSpans.Builder colors, List<DiagnosticItem> diagnostics) {
        diagnostics.sort(DiagnosticItem.START_COMPARATOR);
        for (var d : diagnostics) {
            var start = d.getRange().getStart();
            var end = d.getRange().getEnd();

            colors.markProblemRegion(
                    convertToSpanFlag(d.getSeverity()),
                    start.getLine(),
                    start.getColumn(),
                    end.getLine(),
                    end.getColumn());
        }
    }

    @Contract(pure = true)
    private int convertToSpanFlag(@NonNull DiagnosticSeverity severity) {
        switch (severity) {
            case WARNING:
                return Span.FLAG_WARNING;
            case ERROR:
                return Span.FLAG_ERROR;
            case HINT:
            case INFO:
            default:
                return Span.FLAG_TYPO;
        }
    }
}
