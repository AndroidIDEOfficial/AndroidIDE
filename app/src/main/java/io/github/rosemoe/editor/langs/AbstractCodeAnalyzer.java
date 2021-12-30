package io.github.rosemoe.editor.langs;

import android.graphics.Color;

import androidx.annotation.NonNull;

import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.TextAnalyzeResult;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.eclipse.lsp4j.Diagnostic;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itsaky.lsp.SemanticHighlight;

public abstract class AbstractCodeAnalyzer implements CodeAnalyzer {

    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, Diagnostic>> diagnostics) {
    }
    
    @Override
    public Diagnostic findDiagnosticContaining(int line, int column) {
        return null;
    }

    @Override
    public List<Diagnostic> findDiagnosticsContainingLine(int line) {
        return new ArrayList<Diagnostic>();
    }

    @Override
    public Map<Integer, Diagnostic> getDiagnosticsAtLine(int line) {
        return new HashMap<Integer, Diagnostic>();
    }

    @Override
    public Map<Integer, Integer> getHexColorsInLine(int line) {
        return new HashMap<Integer, Integer>();
    }
    
    protected void checkAndAddHexString (@NonNull Token token, final int defaultColorId, TextAnalyzeResult result) {
        final var line = token.getLine () - 1;
        final var column = token.getCharPositionInLine ();
        final var m = HEX.matcher (token.getText ());
        if (m.find ()) {
            try {
                // If the text is: '"#f44336"'
                // Then then the inverted commas will have a separate span and the string value will have a separate span
                
                // Highlight opening '"'
                final var first = result.addIfNeeded (line, column, defaultColorId);
                first.underlineHeight = 0f;
                first.underlineColor = 0;
                
                // Highlight color value
                final var span = first.copy ();
                span.column += 1;
                span.underlineColor = Color.parseColor (token.getText ().substring (m.start (), m.end ()));
                span.underlineHeight = Span.HEX_COLOR_UNDERLINE_HEIGHT;
                result.add (span.line, span);
                
                // Highlight closing '"'
                final var second = first.copy ();
                second.column += token.getText ().length () - 1;
                second.underlineColor = 0;
                second.underlineHeight = 0f;
                result.add (second.line, second);
                
            } catch (Throwable th) {
                result.addIfNeeded (line, column, defaultColorId);
            }
        } else {
            result.addIfNeeded (line, column, defaultColorId);
        }
    }
    
    private final Pattern HEX = Pattern.compile ("#[a-fA-F0-9]{3,8}");
}
