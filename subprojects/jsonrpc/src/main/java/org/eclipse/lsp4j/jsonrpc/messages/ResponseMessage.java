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
package org.eclipse.lsp4j.jsonrpc.messages;

/**
 * Response message sent as a result of a request. If a request doesn't provide a result value the
 * receiver of a request still needs to return a response message to conform to the JSON RPC
 * specification. The result property of the ResponseMessage should be set to null in this case to
 * signal a successful request. A response message is linked to a request via their {@code id}
 * properties.
 */
public class ResponseMessage extends IdentifiableMessage {

  /**
   * The result of a request. This can be omitted in the case of an error. The object type depends
   * on the method of the corresponding request.
   */
  private Object result;
  /** The error object in case a request fails. */
  private ResponseError error;

  public Object getResult() {
    return this.result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public ResponseError getError() {
    return this.error;
  }

  public void setError(ResponseError error) {
    this.error = error;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    if (!super.equals(obj)) return false;
    ResponseMessage other = (ResponseMessage) obj;
    if (this.result == null) {
      if (other.result != null) return false;
    } else if (!this.result.equals(other.result)) return false;
    if (this.error == null) {
      if (other.error != null) return false;
    } else if (!this.error.equals(other.error)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
    result = prime * result + ((this.error == null) ? 0 : this.error.hashCode());
    return result;
  }
}
