package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.models.DiagnosticGroup;
import java.io.File;
import org.eclipse.lsp4j.Diagnostic;

public interface DiagnosticClickListener {
    void onGroupClick(DiagnosticGroup group);
    void onDiagnosticClick(File file, Diagnostic diagnostic);
}
