package com.itsaky.lsp.java;

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
    private final Runnable close;

    public CompilationUnitTree root() {
        if (roots.size() != 1) {
            throw new RuntimeException(Integer.toString(roots.size()));
        }
        return roots.get(0);
    }

    public CompilationUnitTree root(Path file) {
        for (CompilationUnitTree root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                return root;
            }
        }
        throw new RuntimeException("not found");
    }

    public CompilationUnitTree root(JavaFileObject file) {
        for (CompilationUnitTree root : roots) {
            if (root.getSourceFile().toUri().equals(file.toUri())) {
                return root;
            }
        }
        throw new RuntimeException("not found");
    }

    public CompileTask(
            JavacTask task,
            List<CompilationUnitTree> roots,
            List<Diagnostic<? extends JavaFileObject>> diagnostics,
            Runnable close) {
        this.task = task;
        this.roots = roots;
        this.diagnostics = diagnostics;
        this.close = close;
    }

    @Override
    public void close() {
        close.run();
    }
}
