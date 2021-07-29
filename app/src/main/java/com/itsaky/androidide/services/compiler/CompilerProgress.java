package com.itsaky.androidide.services.compiler;

import org.eclipse.jdt.core.compiler.CompilationProgress;

public class CompilerProgress extends CompilationProgress {

    private boolean canceled = false;

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public void begin(int p1) {
    }

    @Override
    public void done() {
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setTaskName(String p1) {
    }

    @Override
    public void worked(int p1, int p2) {
    }
}
