package com.itsaky.androidide.services.compiler.model;

import java.io.File;
import javax.tools.Diagnostic;

public interface CompilerDiagnostic {
    File file();
    long line();
    long column();
    long start();
    long end();
    String message();
    Diagnostic.Kind kind();
    
    int color();
}
