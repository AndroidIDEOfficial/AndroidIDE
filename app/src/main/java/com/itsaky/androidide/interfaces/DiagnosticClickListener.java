package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.lsp.Diagnostic;

public interface DiagnosticClickListener {
    void onGroupClick(DiagnosticGroup group);
    void onDiagnosticClick(Diagnostic diagnostic);
}
