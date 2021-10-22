package com.itsaky.androidide.syntax.lexer.impls.javadoc;

import com.itsaky.androidide.antlr4.javadoc.JavadocLexer;
import com.itsaky.androidide.antlr4.javadoc.JavadocParserBaseVisitor;
import com.itsaky.androidide.utils.Logger;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.EditorColorScheme;
import java.io.StringReader;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import static com.itsaky.androidide.antlr4.javadoc.JavadocParser.*;

public class JavaDocLexer extends JavadocParserBaseVisitor<Void> {

    private final String doc;
    private final int startLine;
    private final int startColumn;

    private final TextAnalyzeResult colors;

    private static final Logger LOG = Logger.instance("JavadocLexer");
    
    public JavaDocLexer(String doc, int startLine, int startColumn, TextAnalyzeResult colors) {
        this.doc = doc;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.colors = colors;
        
        LOG.info("new JavadocLexer (doc=" + doc + ", startLine=" + startLine + ", startColumn=" + startColumn + ", colors=" + colors);
    }

    public void lex() throws Exception {
        final JavadocLexer lexer = new JavadocLexer(CharStreams.fromReader(new StringReader(this.doc)));
        final List<? extends Token> tokens = lexer.getAllTokens();
        
        boolean inlineOpen = false;
        int bracesAfterInline = 0;
        for (int i=0;i<tokens.size();i++) {
            final Token token = tokens.get(i);
            final int line = line(token.getLine());
            final int column = column(line, token.getCharPositionInLine());
            final int type = token.getType();
            final String text = token.getText();
            LOG.info("token.get(i=" + i + ", size=" + tokens.size() + ") [line=" + line + ", column=" + column + ", type=" + type + ", text=" + text + "]");
            if(type == AT) {
                // Skip this
                // '@' must be highlighted only if it is directly followed by a name
            } else if(type == NAME && i > 0) {
                Token prev = tokens.get(i - 1);
                Token prevOfPrev = i > 1 ? tokens.get(i - 2) : null;
                if(prev.getType() == AT) {
                    if(prevOfPrev != null && prevOfPrev.getType() == BRACE_OPEN) {
                        // Inline tag '{@'
                        // In this case, position of '{' must be considered
                        final int cLine = line(prevOfPrev.getLine());
                        final int cCol = column(cLine, prevOfPrev.getCharPositionInLine());
                        colors.addIfNeeded(cLine, cCol, EditorColorScheme.JAVADOC_INLINE_TAG);
                    } else {
                        // A simple javadoc tag i.e. '@<tagName>'
                        // Position of '@' will be considered
                        final int cLine = line(prev.getLine());
                        final int cCol = column(cLine, prev.getCharPositionInLine());
                        colors.addIfNeeded(cLine, cCol, EditorColorScheme.JAVADOC_TAG);
                    }
                }
            } else if(type == BRACE_OPEN) {
                if(inlineOpen) {
                    bracesAfterInline ++;
                }
            } else if (type == BRACE_CLOSE) {
                if(inlineOpen && bracesAfterInline == 0) {
                    colors.addIfNeeded(line, column, EditorColorScheme.JAVADOC_INLINE_TAG);
                    inlineOpen = false;
                } else {
                    bracesAfterInline --;
                }
            } else {
                colors.addIfNeeded(line, column, EditorColorScheme.COMMENT);
            }
        }
    }

    private int line(int current) {
        return startLine + current;
    }

    private int column(int line, int column) {
        if (line == 0) {
            return startColumn + column;
        }
        return column;
    }
}
