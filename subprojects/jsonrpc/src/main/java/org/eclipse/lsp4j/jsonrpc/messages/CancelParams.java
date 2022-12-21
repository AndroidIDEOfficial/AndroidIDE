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

import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/** To cancel a request a notification message with the following properties is sent. */
public class CancelParams {

  /** The request id to cancel. */
  @NonNull private Either<String, Number> id;

  @NonNull
  public String getId() {
    if (id == null) return null;
    if (id.isLeft()) return id.getLeft();
    if (id.isRight()) return id.getRight().toString();
    return null;
  }

  public void setId(@NonNull String id) {
    this.id = Either.forLeft(id);
  }

  public void setId(@NonNull int id) {
    this.id = Either.forRight(id);
  }

  @NonNull
  public Either<String, Number> getRawId() {
    return id;
  }

  public void setRawId(@NonNull Either<String, Number> id) {
    this.id = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    CancelParams other = (CancelParams) obj;
    if (this.id == null) {
      if (other.id != null) return false;
    } else if (!this.id.equals(other.id)) return false;
    return true;
  }

  @Override
  public String toString() {
    return MessageJsonHandler.toString(this);
  }
}
