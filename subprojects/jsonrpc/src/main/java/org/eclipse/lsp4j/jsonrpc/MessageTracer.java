/******************************************************************************
 * Copyright (c) 2016-2019 TypeFox and others.
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

import org.eclipse.lsp4j.jsonrpc.TracingMessageConsumer.RequestMetadata;

import java.io.PrintWriter;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Wraps a {@link MessageConsumer} with one that logs in a way that the LSP Inspector can parse. *
 * https://microsoft.github.io/language-server-protocol/inspector/
 */
public class MessageTracer implements Function<MessageConsumer, MessageConsumer> {
  private final PrintWriter printWriter;
  private final Map<String, RequestMetadata> sentRequests = new HashMap<>();
  private final Map<String, RequestMetadata> receivedRequests = new HashMap<>();

  MessageTracer(PrintWriter printWriter) {
    this.printWriter = Objects.requireNonNull(printWriter);
  }

  @Override
  public MessageConsumer apply(MessageConsumer messageConsumer) {
    return new TracingMessageConsumer(
        messageConsumer, sentRequests, receivedRequests, printWriter, Clock.systemDefaultZone());
  }
}
