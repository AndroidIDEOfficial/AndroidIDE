package com.itsaky.androidide.shell;

import java.io.IOException;

/**
 * Provides static access to the process executor
 */
public class ProcessExecutorFactory {
    
    private static final CommonProcessExecutor executor = new CommonProcessExecutor ();
    
    public static IProcessExecutor commonExecutor () {
        return executor;
    }
    
    public static int exec (ProcessStreamsHolder holder, String ... args) throws InterruptedException, IOException {
        return executor.exec(holder, args);
    }
    
    public static int exec (ProcessStreamsHolder holder, boolean redirectErr, String ... args) throws InterruptedException, IOException {
        return executor.exec(holder, redirectErr, args);
    }
    
    public static void execAsync (ProcessStreamsHolder holder, IProcessExitListener exitListener, boolean redirectErr, String ... args) throws IOException {
        executor.execAsync(holder, exitListener, redirectErr, args);
    }
}
