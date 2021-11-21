package com.itsaky.androidide.shell;

import java.io.IOException;

/**
 * An executor to execute a command.
 * <p>
 * Subclasses of this interface can decide how to execute the command.
 * For example, you could use {@link ProcessBuilder} or you could 
 * use {@link com.itsaky.terminal.JNI JNI} to create subprocesses.
 * </p>
 */
public interface IProcessExecutor {
    
    /**
     * @see exec(ProcessStreamsHolder, boolean, String[])
     */
    int exec (ProcessStreamsHolder holder, String... args) throws IOException, InterruptedException;
    
    /**
     * Executes the given command with its attributes.
     * This method WILL block the current thread.
     *
     * @param holder A holder which will be provided the process's input, error and output stream
     * @param redirectErr Whether to redirect error stream to output stream
     * @param args Arguments for {@code cmd}
     * @return The exit code of the command
     */
    int exec (ProcessStreamsHolder holder, boolean redirectErr, String... args) throws IOException, InterruptedException;
    
    /**
     * Executes the given command with its attributes.
     * This method will not block the current thread.
     *
     * @param holder A holder which will be provided the process's input, error and output stream
     * @param redirectErr Whether to redirect error stream to output stream
     * @param onExit A listener that will be called once the process exits
     * @param args Arguments for {@code cmd}
     */
    void execAsync (ProcessStreamsHolder holder, IProcessExitListener onExit, boolean redirectErr, String... args) throws IOException;
}
