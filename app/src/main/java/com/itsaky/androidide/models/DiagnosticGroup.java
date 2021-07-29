package com.itsaky.androidide.models;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import java.io.File;
import java.util.List;

public class DiagnosticGroup {
    public int icon;
    public String text;
    public File file;
    public List<CompilerDiagnostic> diagnostics;

    public DiagnosticGroup(int icon, File file, List<CompilerDiagnostic> diagnostics) {
        this.icon = icon;
        this.file = file;
        this.text = file.getName();
        this.diagnostics = diagnostics;
    }
}
