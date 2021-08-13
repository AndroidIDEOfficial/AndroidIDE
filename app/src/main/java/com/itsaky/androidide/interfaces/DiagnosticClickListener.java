package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.lsp.Diagnostic;
import java.io.File;

public interface DiagnosticClickListener {
    void onGroupClick(DiagnosticGroup group);
    void onDiagnosticClick(File file, Diagnostic diagnostic);
}
