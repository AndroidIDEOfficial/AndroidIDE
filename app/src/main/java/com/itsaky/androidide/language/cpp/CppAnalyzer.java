package com.itsaky.androidide.language.cpp;

import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.*;
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

import com.itsaky.androidide.language.IAnalyzeManager;
import com.itsaky.androidide.lexers.cpp.CPP14Lexer;
import com.itsaky.androidide.lexers.cpp.CPP14Parser;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticSeverity;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;

import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.lang.styling.Styles;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CppAnalyzer extends SimpleAnalyzeManager<Void> implements IAnalyzeManager {

    private static final ILogger LOG = ILogger.newInstance("CppAnalyzer");
    private final List<DiagnosticItem> ideDiagnostics = new ArrayList<>();
    private List<DiagnosticItem> diagnostics = new ArrayList<>();

    @Override
    public void updateDiagnostics(@NonNull List<DiagnosticItem> diagnostics) {
        this.diagnostics = diagnostics;
    }

    @NonNull
    @Override
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
        final var styles = new Styles();
        final var colors = new MappedSpans.Builder();
        final CodePointCharStream stream;
        try {
            stream = CharStreams.fromReader(new CharSequenceReader(text));
        } catch (IOException e) {
            LOG.error("Unable to read text to analyze", e);
            return styles;
        }

        final var lexer = new CPP14Lexer(stream);
        // final var parser = new CPP14Parser(new CommonTokenStream(lexer));
        final var stack = new Stack<CodeBlock>();
        Token token;
        int line, column, lastLine = 1, currSwitch = 0, maxSwitch = 0, type, previous = 0;
        boolean isFirst = true;

        ideDiagnostics.clear();

        while (!delegate.isCancelled()) {
            token = lexer.nextToken();
            if (token == null) {
                break;
            }

            line = token.getLine() - 1;
            column = token.getCharPositionInLine();
            type = token.getType();

            if (type == CPP14Lexer.EOF) {
                lastLine = line;
                break;
            }

            final var tokenLength = token.getText().length();

            switch (type) {
                case Whitespace:
                    if (isFirst) {
                        colors.addNormalIfNull();
                    }
                    break;
                case Alignas:
                case Alignof:
                case Asm:
                case Auto:
                case Break:
                case Class:
                case Const:
                case Catch:
                case Case:
                case Constexpr:
                case Const_cast:
                case Continue:
                case Decltype:
                case Default:
                case Delete:
                case Do:
                case Dynamic_cast:
                case Else:
                case Enum:
                case Explicit:
                case Export:
                case Extern:
                case Final:
                case For:
                case Friend:
                case Goto:
                case If:
                case Inline:
                case Mutable:
                case Namespace:
                case New:
                case Noexcept:
                case Operator:
                case Override:
                case Private:
                case Protected:
                case Public:
                case Register:
                case Reinterpret_cast:
                case Return:
                case Sizeof:
                case Signed:
                case Static:
                case Static_cast:
                case Static_assert:
                case Struct:
                case Switch:
                case Template:
                case This:
                case Throw:
                case Thread_local:
                case Try:
                case Typedef:
                case Typeid_:
                case Typename_:
                case Unsigned:
                case Union:
                case Using:
                case Virtual:
                case Volatile:
                case While:
                case Void:
                    colors.addIfNeeded(line, column, forKeyword());
                    break;
                case Bool:
                case Char:
                case Char16:
                case Char32:
                case Double:
                case Float:
                case Int:
                case Long:
                case Short:
                case Wchar:
                    colors.addIfNeeded(line, column, get(TYPE_NAME));
                    break;
                case LeftParen:
                case LeftBracket:
                case LeftBrace:
                case Dot:
                case RightParen:
                case RightBracket:
                case RightBrace:
                case Semi:
                case Comma:
                    colors.addIfNeeded(line, column, get(OPERATOR));
                    break;
                case Plus:
                case Minus:
                case Star:
                case Div:
                case Mod:
                case Caret:
                case And:
                case Or:
                case Tilde:
                case Not:
                case Assign:
                case Less:
                case Greater:
                case PlusAssign:
                case MinusAssign:
                case StarAssign:
                case DivAssign:
                case ModAssign:
                case XorAssign:
                case AndAssign:
                case OrAssign:
                case RightShiftAssign:
                case LeftShiftAssign:
                case Equal:
                case NotEqual:
                case LessEqual:
                case GreaterEqual:
                case AndAnd:
                case OrOr:
                case PlusPlus:
                case MinusMinus:
                case Arrow:
                case ArrowStar:
                case Question:
                case Colon:
                case Doublecolon:
                case DotStar:
                case Ellipsis:
                    colors.addIfNeeded(line, column, get(OPERATOR));
                    break;
                case IntegerLiteral:
                case DecimalLiteral:
                case OctalLiteral:
                case HexadecimalLiteral:
                case BinaryLiteral:
                case Integersuffix:
                case CharacterLiteral:
                case FloatingLiteral:
                case StringLiteral:
                case BooleanLiteral:
                case Nullptr:
                case True_:
                case False_:
                    colors.addIfNeeded(line, column, get(LITERAL));
                    break;
                case BlockComment:
                    colors.addIfNeeded(line, column, forComment());
                    break;
                case LineComment:
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
                case MultiLineMacro:
                case Directive:
                    colors.addIfNeeded(line, column, forString());
                    break;

                default:
                    colors.addIfNeeded(line, column, get(TEXT_NORMAL));
                    break;
            }

            isFirst = false;
            if (type != Whitespace) {
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
            if (d == null) {
                continue;
            }

            var start = d.getRange().getStart();
            var end = d.getRange().getEnd();

            try {
                colors.markProblemRegion(
                        convertToSpanFlag(d.getSeverity()),
                        start.getLine(),
                        start.getColumn(),
                        end.getLine(),
                        end.getColumn());
            } catch (Throwable e) {
                // Might happen frequently if user types faster
            }
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
