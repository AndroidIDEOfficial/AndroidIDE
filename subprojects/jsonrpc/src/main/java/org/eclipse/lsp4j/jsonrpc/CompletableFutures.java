/******************************************************************************
 * Copyright (c) 2016 TypeFox and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public final class CompletableFutures {
  private CompletableFutures() {}

  /**
   * A utility method to create a {@link CompletableFuture} with cancellation support.
   *
   * @param code a function that accepts a {@link CancelChecker} and returns the to be computed
   *     value
   * @return a future
   */
  public static <R> CompletableFuture<R> computeAsync(Function<CancelChecker, R> code) {
    CompletableFuture<CancelChecker> start = new CompletableFuture<>();
    CompletableFuture<R> result = start.thenApplyAsync(code);
    start.complete(new FutureCancelChecker(result));
    return result;
  }

  /**
   * A utility method to create a {@link CompletableFuture} with cancellation support.
   *
   * @param code a function that accepts a {@link CancelChecker} and returns the to be computed
   *     value
   * @return a future
   */
  public static <R> CompletableFuture<R> computeAsync(
      Executor executor, Function<CancelChecker, R> code) {
    CompletableFuture<CancelChecker> start = new CompletableFuture<>();
    CompletableFuture<R> result = start.thenApplyAsync(code, executor);
    start.complete(new FutureCancelChecker(result));
    return result;
  }

  public static class FutureCancelChecker implements CancelChecker {

    private final CompletableFuture<?> future;

    public FutureCancelChecker(CompletableFuture<?> future) {
      this.future = future;
    }

    @Override
    public boolean isCanceled() {
      return future.isCancelled();
    }

    @Override
    public void checkCanceled() {
      if (future.isCancelled()) throw new CancellationException();
    }
  }
}
