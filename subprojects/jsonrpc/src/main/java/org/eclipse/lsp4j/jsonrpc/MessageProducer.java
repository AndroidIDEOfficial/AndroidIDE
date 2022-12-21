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

public interface MessageProducer {

  /**
   * Listen to a message source and forward all messages to the given consumer. Typically this
   * method blocks until the message source is unable to deliver more messages.
   *
   * @throws JsonRpcException when accessing the JSON-RPC communication channel fails
   */
  void listen(MessageConsumer messageConsumer) throws JsonRpcException;
}
