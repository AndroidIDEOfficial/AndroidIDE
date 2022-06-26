/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.androidide.shell;

import com.itsaky.androidide.utils.Environment;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/** Creates subprocesses using a {@link ProcessBuilder} */
public class CommonProcessExecutor implements IProcessExecutor {

  final File HOME = getHome();

  private static File getHome() {
    return Environment.HOME;
  }

  @Override
  public int exec(ProcessStreamsHolder holder, String... args)
      throws IOException, InterruptedException {
    return exec(holder, true, args);
  }

  @Override
  public int exec(ProcessStreamsHolder holder, boolean redirectErr, String... args)
      throws IOException, InterruptedException {
    return exec(holder, HOME.getAbsolutePath(), redirectErr, args);
  }

  @Override
  public int exec(ProcessStreamsHolder holder, String cwd, boolean redirectErr, String[] args)
      throws IOException, InterruptedException {
    final Process proc = newProcess(args, cwd, redirectErr, holder);
    return proc.waitFor();
  }

  @Override
  public void execAsync(
      ProcessStreamsHolder holder, IProcessExitListener onExit, boolean redirectErr, String... args)
      throws IOException {
    execAsync(holder, onExit, HOME.getAbsolutePath(), redirectErr, args);
  }

  @Override
  public void execAsync(
      ProcessStreamsHolder holder,
      IProcessExitListener onExit,
      String cwd,
      boolean redirectErr,
      String... args)
      throws IOException {
    final Process proc = newProcess(args, cwd, redirectErr, holder);
    final Thread exitListener =
        new Thread(new ExitListenerRunnable(proc, onExit), "ProcessExitListener");
    exitListener.start();
  }

  private Process newProcess(
      String[] args, String cwd, boolean redirectErr, ProcessStreamsHolder holder)
      throws IOException {
    return newProcess(args, new File(cwd), redirectErr, holder);
  }

  private Process newProcess(
      String[] args, File cwd, boolean redirectErr, ProcessStreamsHolder holder)
      throws IOException {
    final ProcessBuilder builder = new ProcessBuilder(args);
    builder.directory(cwd);
    builder.redirectErrorStream(redirectErr);
    builder.environment().putAll(getEnv());

    final Process proc = builder.start();
    holder.in = proc.getInputStream();
    holder.err = proc.getErrorStream();
    holder.out = proc.getOutputStream();

    return proc;
  }

  private static Map<String, String> getEnv() {
    return Environment.getEnvironment();
  }

  private static class ExitListenerRunnable implements Runnable {

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
        // Ignored
      }

      listener.onExit(proc.exitValue());
    }
  }
}
