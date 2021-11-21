package com.itsaky.androidide.shell;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates subprocesses using a {@link ProcessBuilder}
 */
public class CommonProcessExecutor implements IProcessExecutor {

    final File HOME = getHome();

    @Override
    public int exec(ProcessStreamsHolder holder, String... args) throws IOException, InterruptedException {
        return exec(holder, true, args);
    }

    @Override
    public int exec(ProcessStreamsHolder holder, boolean redirectErr, String... args) throws IOException, InterruptedException {
        final Process proc = newProcess(args, redirectErr, holder);
        return proc.waitFor();
    }

    @Override
    public void execAsync(ProcessStreamsHolder holder, IProcessExitListener onExit, boolean redirectErr, String[] args) throws IOException {
        final Process proc = newProcess(args, redirectErr, holder);
        final Thread exitListener = new Thread(new ExitListenerRunnable (proc, onExit), "ProcessExitListener");
        exitListener.start();
    }

    private Process newProcess(String[] args, boolean redirectErr, ProcessStreamsHolder holder) throws IOException {
        final ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(HOME);
        builder.redirectErrorStream(redirectErr);
        builder.environment().putAll(getEnv());
        
        final Process proc = builder.start();
        holder.in = proc.getInputStream();
        holder.err = proc.getErrorStream();
        holder.out = proc.getOutputStream();
        
        return proc;
    }

    private static File getHome() {
        try {
            final Class<?> env = CommonProcessExecutor.class.getClassLoader().loadClass("com.itsaky.androidide.utils.Environment");
            final Field home = env.getDeclaredField("HOME");
            home.setAccessible(true);
            return (File) home.get(null);
        } catch (Throwable th) {
            // ignored
        }

        // Return this if we cannot get it from Environment class
        return new File("/data/data/com.itsaky.androidide/files/framework");
    }

    private static Map<String, String> getEnv() {
        try {
            final Class<?> env = CommonProcessExecutor.class.getClassLoader().loadClass("com.itsaky.androidide.utils.Environment");
            final Method method = env.getDeclaredMethod("getEnvironment", boolean.class);
            method.setAccessible(true);
            return (Map<String, String>) method.invoke(null, true);
        } catch (Throwable th) {
            // ignored
        }

        // Return this if we cannot get it from Environment class
        return new HashMap<String, String>();
    }

    private class ExitListenerRunnable implements Runnable {
        
        private final Process proc;
        private final IProcessExitListener listener;
        
        public ExitListenerRunnable(Process proc, IProcessExitListener listener) {
            this.proc = proc;
            this.listener = listener;
        }
        
        @Override
        public void run() {
            try {
                proc.waitFor();
            } catch (InterruptedException e) {
                // ignored
            }
            
            listener.onExit(proc.exitValue());
        }
    }
}
