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

import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;

/** An exception thrown in order to send a response with an attached {@code error} object. */
public class ResponseErrorException extends RuntimeException {

  private static final long serialVersionUID = -5970739895395246885L;
  private ResponseError responseError;

  public ResponseErrorException(ResponseError responseError) {
    this.responseError = responseError;
  }

  @Override
  public String getMessage() {
    return responseError.getMessage();
  }

  public ResponseError getResponseError() {
    return responseError;
  }
}
