package com.itsaky.androidide.interfaces;

import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;

public interface DiagnosticClickListener {
    void onGroupClick(DiagnosticGroup group);
    void onDiagnosticClick(CompilerDiagnostic diagnostic);
}
