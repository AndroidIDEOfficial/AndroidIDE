package io.github.rosemoe.editor.langs;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.SemanticHighlight;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.TextAnalyzeResult;

public abstract class AbstractCodeAnalyzer implements CodeAnalyzer {

    @Override
    public void setSemanticHighlights(SemanticHighlight highlights) {
    }

    @Override
    public void updateDiagnostics(Map<Integer, Map<Integer, DiagnosticItem>> diagnostics) {
    }
    
    @Override
    public DiagnosticItem findDiagnosticContaining(int line, int column) {
        return null;
    }

    @Override
    public List<DiagnosticItem> findDiagnosticsContainingLine(int line) {
        return new ArrayList<>();
    }

    @Override
    public Map<Integer, DiagnosticItem> getDiagnosticsAtLine(int line) {
        return new HashMap<>();
    }

    @Override
    public Map<Integer, Integer> getHexColorsInLine(int line) {
        return new HashMap<> ();
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
