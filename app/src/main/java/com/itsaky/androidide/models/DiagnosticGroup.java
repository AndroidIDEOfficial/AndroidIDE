package com.itsaky.androidide.models;

import com.itsaky.lsp.Diagnostic;
import java.io.File;
import java.util.List;

public class DiagnosticGroup {
    public int icon;
    public String text;
    public File file;
    public List<Diagnostic> diagnostics;

    public DiagnosticGroup(int icon, File file, List<Diagnostic> diagnostics) {
        this.icon = icon;
        this.file = file;
        this.text = file.getName();
        this.diagnostics = diagnostics;
    }
}
