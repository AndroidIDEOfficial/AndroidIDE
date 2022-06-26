/******************************************************************************
 * Copyright (c) 2018 Red Hat Inc
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

import org.eclipse.lsp4j.jsonrpc.json.ConcurrentMessageProcessor;
import org.eclipse.lsp4j.jsonrpc.json.StreamMessageProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class StandardLauncher<T> implements Launcher<T> {
  private final ExecutorService execService;
  private final T remoteProxy;
  private final RemoteEndpoint remoteEndpoint;
  private final ConcurrentMessageProcessor msgProcessor;

  public StandardLauncher(
      StreamMessageProducer reader,
      MessageConsumer messageConsumer,
      ExecutorService execService,
      T remoteProxy,
      RemoteEndpoint remoteEndpoint) {
    this(
        execService,
        remoteProxy,
        remoteEndpoint,
        new ConcurrentMessageProcessor(reader, messageConsumer));
  }

  public StandardLauncher(
      ExecutorService execService2,
      T remoteProxy2,
      RemoteEndpoint remoteEndpoint2,
      ConcurrentMessageProcessor msgProcessor) {
    this.execService = execService2;
    this.remoteProxy = remoteProxy2;
    this.remoteEndpoint = remoteEndpoint2;
    this.msgProcessor = msgProcessor;
  }

  @Override
  public Future<Void> startListening() {
    return msgProcessor.beginProcessing(execService);
  }

  @Override
  public T getRemoteProxy() {
    return remoteProxy;
  }

  @Override
  public RemoteEndpoint getRemoteEndpoint() {
    return remoteEndpoint;
  }
}
