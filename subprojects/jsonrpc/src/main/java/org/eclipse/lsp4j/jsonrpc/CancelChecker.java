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

/** Used for processing requests with cancellation support. */
public interface CancelChecker {

  /** Check for cancellation without throwing an exception. */
  default boolean isCanceled() {
    try {
      checkCanceled();
    } catch (CancellationException ce) {
      return true;
    }
    return false;
  }

  /** Throw a {@link CancellationException} if the currently processed request has been canceled. */
  void checkCanceled();
}
