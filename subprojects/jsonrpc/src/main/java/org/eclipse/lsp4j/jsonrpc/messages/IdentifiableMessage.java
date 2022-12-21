/******************************************************************************
 * Copyright (c) 2018 TypeFox and others.
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

import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/** A message with an {@code id} property. */
public abstract class IdentifiableMessage extends Message {

  /** The message identifier. */
  @NonNull private Either<String, Number> id;

  public String getId() {
    if (id == null) return null;
    if (id.isLeft()) return id.getLeft();
    if (id.isRight()) return id.getRight().toString();
    return null;
  }

  public void setId(String id) {
    this.id = Either.forLeft(id);
  }

  public void setId(int id) {
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
    int result = super.hashCode();
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    if (!super.equals(obj)) return false;
    IdentifiableMessage other = (IdentifiableMessage) obj;
    if (this.id == null) {
      if (other.id != null) return false;
    } else if (!this.id.equals(other.id)) return false;
    return true;
  }
}
