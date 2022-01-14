package com.itsaky.lsp.java.completion;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

import java.io.IOException;
import java.nio.CharBuffer;

public class PruneMethodBodies extends TreeScanner<StringBuilder, Long> {
    private final JavacTask task;
    private final StringBuilder buf = new StringBuilder ();
    private CompilationUnitTree root;
    
    public PruneMethodBodies (JavacTask task) {
        this.task = task;
    }
    
    @Override
    public StringBuilder visitCompilationUnit (CompilationUnitTree t, Long find) {
        root = t;
        try {
            CharSequence contents = t.getSourceFile ().getCharContent (true);
            buf.setLength (0);
            buf.append (contents);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        super.visitCompilationUnit (t, find);
        return buf;
    }
    
    @Override
    public StringBuilder visitMethod (MethodTree t, Long find) {
        SourcePositions pos = Trees.instance (task).getSourcePositions ();
        if (t.getBody () == null) {
            return buf;
        }
        long start = pos.getStartPosition (root, t.getBody ());
        long end = pos.getEndPosition (root, t.getBody ());
        if (!(start <= find && find < end)) {
            for (int i = (int) start + 1; i < end - 1; i++) {
                if (!Character.isWhitespace (buf.charAt (i))) {
                    buf.setCharAt (i, ' ');
                }
            }
            return buf;
        }
        super.visitMethod (t, find);
        return buf;
    }
    
    @Override
    public StringBuilder reduce (StringBuilder a, StringBuilder b) {
        return buf;
    }
}
