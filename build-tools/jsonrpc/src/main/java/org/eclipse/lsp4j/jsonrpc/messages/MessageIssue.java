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

/** Describes an issue found while parsing or validating a message. */
public class MessageIssue {

  @NonNull private String text;

  private int code;

  private Exception cause;

  public MessageIssue(@NonNull String text) {
    this(text, 0, null);
  }

  public MessageIssue(@NonNull String text, int code, Exception cause) {
    this.text = text;
    this.code = code;
    this.cause = cause;
  }

  public MessageIssue(@NonNull String text, int code) {
    this(text, code, null);
  }

  public int getIssueCode() {
    return code;
  }

  public Exception getCause() {
    return cause;
  }

  @Override
  public String toString() {
    return getText();
  }

  @NonNull
  public String getText() {
    return text;
  }
}
