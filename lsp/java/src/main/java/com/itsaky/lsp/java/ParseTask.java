package com.itsaky.lsp.java;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;

public class ParseTask {
    public final JavacTask task;
    public final CompilationUnitTree root;

    public ParseTask(JavacTask task, CompilationUnitTree root) {
        this.task = task;
        this.root = root;
    }
}
