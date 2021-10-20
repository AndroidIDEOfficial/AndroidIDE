package io.github.rosemoe.editor.langs;

import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.lsp4j.Diagnostic;

public abstract class AbstractCodeAnalyzer implements CodeAnalyzer {
    
    @Override
    public Diagnostic findDiagnosticContaining(int line, int column) {
        return null;
    }

    @Override
    public List<Diagnostic> findDiagnosticsContainingLine(int line) {
        return new ArrayList<Diagnostic>();
    }
}
