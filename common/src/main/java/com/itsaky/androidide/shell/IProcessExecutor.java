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

import java.io.IOException;

/**
 * An executor to execute a command.
 *
 * <p>Subclasses of this interface can decide how to execute the command. For example, you could use
 * {@link ProcessBuilder} or you could use {@link com.itsaky.terminal.JNI JNI} to create
 * subprocesses.
 */
public interface IProcessExecutor {

  /**
   * @see exec(ProcessStreamsHolder, boolean, String[])
   */
  int exec(ProcessStreamsHolder holder, String... args) throws IOException, InterruptedException;

  /**
   * Executes the given command with its attributes. This method WILL block the current thread.
   *
   * @param holder A holder which will be provided the process's input, error and output stream
   * @param redirectErr Whether to redirect error stream to output stream
   * @param args Arguments for {@code cmd}
   * @return The exit code of the command
   */
  int exec(ProcessStreamsHolder holder, boolean redirectErr, String... args)
      throws IOException, InterruptedException;

  /**
   * Executes the given command with its attributes. This method WILL block the current thread.
   *
   * @param holder A holder which will be provided the process's input, error and output stream
   * @param redirectErr Whether to redirect error stream to output stream
   * @param cwd The working directory of the process
   * @param args Arguments for {@code cmd}
   * @return The exit code of the command
   */
  int exec(ProcessStreamsHolder holder, String cwd, boolean redirectErr, String... args)
      throws IOException, InterruptedException;

  /**
   * Executes the given command with its attributes. This method will not block the current thread.
   *
   * @param holder A holder which will be provided the process's input, error and output stream
   * @param redirectErr Whether to redirect error stream to output stream
   * @param onExit A listener that will be called once the process exits
   * @param args Arguments for {@code cmd}
   */
  void execAsync(
      ProcessStreamsHolder holder, IProcessExitListener onExit, boolean redirectErr, String... args)
      throws IOException;

  /**
   * Executes the given command with its attributes. This method will not block the current thread.
   *
   * @param holder A holder which will be provided the process's input, error and output stream
   * @param redirectErr Whether to redirect error stream to output stream
   * @param cwd The working directory of the process
   * @param onExit A listener that will be called once the process exits
   * @param args Arguments for {@code cmd}
   */
  void execAsync(
      ProcessStreamsHolder holder,
      IProcessExitListener onExit,
      String cwd,
      boolean redirectErr,
      String... args)
      throws IOException;
}
