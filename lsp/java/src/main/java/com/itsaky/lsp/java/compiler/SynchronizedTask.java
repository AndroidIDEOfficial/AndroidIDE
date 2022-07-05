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

package com.itsaky.lsp.java.compiler;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.CompilationCancellationException;

import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import kotlin.jvm.functions.Function1;

public class SynchronizedTask {

  private static final ILogger LOG = ILogger.newInstance("SynchronizedTask");
  private volatile boolean isCompiling = false;
  private final Semaphore semaphore = new Semaphore(1);
  private CompileTask task;

  public void run(@NonNull Consumer<CompileTask> taskConsumer) {
    semaphore.acquireUninterruptibly();
    try {
      taskConsumer.accept(this.task);
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
