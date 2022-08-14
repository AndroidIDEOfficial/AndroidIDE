/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.java.compiler;

import androidx.annotation.NonNull;

import com.itsaky.androidide.lsp.java.CompilationCancellationException;
import com.itsaky.androidide.lsp.java.utils.CancelChecker;
import com.itsaky.androidide.utils.ILogger;

import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import kotlin.jvm.functions.Function1;

public class SynchronizedTask {

  private static final ILogger LOG = ILogger.newInstance("SynchronizedTask");
  private volatile boolean isCompiling = false;
  private final Semaphore semaphore = new Semaphore(1);
  private CompileTask task;

  public void run(@NonNull Consumer<CompileTask> taskConsumer) {
    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      throw new CompilationCancellationException(e);
    }
    try {
      taskConsumer.accept(this.task);
    } catch (Throwable err) {
      if (!CancelChecker.isCancelled(err)) {
        LOG.error("An error occurred while working with compilation task", err);
      }
      throw err;
    } finally {
      semaphore.release();
    }
  }

  public <T> T get(@NonNull Function1<CompileTask, T> function) {
    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      throw new CompilationCancellationException(e);
    }

    try {
      return function.invoke(this.task);
    } catch (Throwable err) {
      if (!CancelChecker.isCancelled(err)) {
        LOG.error("An error occurred while working with compilation task", err);
      }
      throw err;
    } finally {
      semaphore.release();
    }
  }

  void post(@NonNull Runnable run) {
    try {
      semaphore.acquire();
    } catch (InterruptedException e) {
      throw new CompilationCancellationException(e);
    }

    isCompiling = true;

    try {
      if (this.task != null) {
        this.task.close();
      }
      run.run();
    } catch (Throwable err) {
      if (!CancelChecker.isCancelled(err)) {
        LOG.error("An error occurred", err);
      }
      throw err;
    } finally {
      semaphore.release();
      isCompiling = false;
    }
  }

  void setTask(CompileTask task) {
    this.task = task;
  }

  public synchronized boolean isCompiling() {
    return isCompiling || semaphore.hasQueuedThreads();
  }
}
