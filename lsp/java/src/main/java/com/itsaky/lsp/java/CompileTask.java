package com.itsaky.lsp.java;

import androidx.annotation.NonNull;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import java.nio.file.Path;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompileTask implements AutoCloseable {
    
    public final JavacTask task;
    public final List<CompilationUnitTree> roots;
    public final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    
    private final CompileBatch compileBatch;
    
    public CompileTask(@NonNull CompileBatch compileBatch, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        this.compileBatch = compileBatch;
        this.task = compileBatch.task;
        this.roots = compileBatch.roots;
        this.diagnostics = diagnostics;
    }

    public CompilationUnitTree root() {
        if (roots.size() != 1) {
            throw new RuntimeException("No compilation units found. Roots: " + roots.size ());
        }
        return roots.get(0);
    }

    public CompilationUnitTree root(Path file) {
        for (CompilationUnitTree root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                return root;
            }
        }
        throw new RuntimeException("Compilation unit not found");
    }

    public CompilationUnitTree root(JavaFileObject file) {
        for (CompilationUnitTree root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                return root;
            }
        }
        throw new RuntimeException("Compilation unit not found");
    }

    @Override
    public void close() {
        compileBatch.close ();
    }
}
