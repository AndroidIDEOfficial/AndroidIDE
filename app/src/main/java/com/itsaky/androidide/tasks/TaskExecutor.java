/*
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
 */

package com.itsaky.androidide.tasks;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.utils.ILogger;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class TaskExecutor {

  private final ILogger LOG = ILogger.newInstance("TaskExecutor");

  public static <R> void execAsync(Callable<R> callable, Callback<R> callback) {
    new TaskExecutor().executeAsync(callable, callback);
  }

  public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return callable.call();
              } catch (Throwable th) {
                LOG.error(
                    "An error occurred while executing Callable in background" + " thread.", th);
                return null;
              }
            })
        .whenComplete(
            (result, throwable) -> ThreadUtils.runOnUiThread(() -> callback.complete(result)));
  }

  public <R> void executeAsyncProvideError(Callable<R> callable, CallbackWithError<R> callback) {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return callable.call();
              } catch (Throwable th) {
                LOG.error(
                    "An error occurred while executing Callable in background" + " thread.", th);
                throw new CompletionException(th);
              }
            })
        .whenComplete(
            (result, throwable) ->
                ThreadUtils.runOnUiThread(() -> callback.complete(result, throwable)));
  }

  public interface Callback<R> {
    void complete(R result);
  }

  public interface CallbackWithError<R> {
    void complete(R result, Throwable error);
  }
}
