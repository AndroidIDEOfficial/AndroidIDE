package com.itsaky.lsp.java.completion;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

class FindInvocationAt extends TreePathScanner<TreePath, Long> {
    
    private final JavacTask task;
    private CompilationUnitTree root;
    
    FindInvocationAt (JavacTask task) {
        this.task = task;
    }
    
    @Override
    public TreePath visitCompilationUnit (CompilationUnitTree t, Long find) {
        root = t;
        return reduce (super.visitCompilationUnit (t, find), getCurrentPath ());
    }
    
    @Override
    public TreePath visitMethodInvocation (MethodInvocationTree t, Long find) {
        SourcePositions pos = Trees.instance (task).getSourcePositions ();
        long start = pos.getEndPosition (root, t.getMethodSelect ()) + 1;
        long end = pos.getEndPosition (root, t) - 1;
        if (start <= find && find <= end) {
            return reduce (super.visitMethodInvocation (t, find), getCurrentPath ());
        }
        return super.visitMethodInvocation (t, find);
    }
    
    @Override
    public TreePath visitNewClass (NewClassTree t, Long find) {
        SourcePositions pos = Trees.instance (task).getSourcePositions ();
        long start = pos.getEndPosition (root, t.getIdentifier ()) + 1;
        long end = pos.getEndPosition (root, t) - 1;
        if (start <= find && find <= end) {
            return reduce (super.visitNewClass (t, find), getCurrentPath ());
        }
        return super.visitNewClass (t, find);
    }
    
    @Override
    public TreePath reduce (TreePath a, TreePath b) {
        if (a != null) {
            return a;
        }
        return b;
    }
}
