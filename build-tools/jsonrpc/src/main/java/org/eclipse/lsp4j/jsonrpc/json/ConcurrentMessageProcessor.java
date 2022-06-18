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
package org.eclipse.lsp4j.jsonrpc.json;

import com.itsaky.androidide.utils.ILogger;

import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.MessageProducer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class connects a message producer with a message consumer by listening for new messages in a
 * dedicated thread.
 */
public class ConcurrentMessageProcessor implements Runnable {

  private static final ILogger LOG = ILogger.newInstance("ConcurrentMessageProducer");
  private final MessageProducer messageProducer;
  private final MessageConsumer messageConsumer;
  private boolean isRunning;

  public ConcurrentMessageProcessor(
      MessageProducer messageProducer, MessageConsumer messageConsumer) {
    this.messageProducer = messageProducer;
    this.messageConsumer = messageConsumer;
  }

  /**
   * Start a thread that listens for messages in the message producer and forwards them to the
   * message consumer.
   *
   * @param messageProducer - produces messages, e.g. by reading from an input channel
   * @param messageConsumer - processes messages and potentially forwards them to other consumers
   * @param executorService - the thread is started using this service
   * @return a future that is resolved when the started thread is terminated, e.g. by closing a
   *     stream
   * @deprecated Please use the non-static ConcurrentMessageProcessor.beginProcessing() instead.
   */
  @Deprecated
  public static Future<Void> startProcessing(
      MessageProducer messageProducer,
      MessageConsumer messageConsumer,
      ExecutorService executorService) {
    ConcurrentMessageProcessor reader =
        new ConcurrentMessageProcessor(messageProducer, messageConsumer);
    final Future<?> result = executorService.submit(reader);
    return wrapFuture(result, messageProducer);
  }

  public static Future<Void> wrapFuture(Future<?> result, MessageProducer messageProducer) {
    return new Future<Void>() {

      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        if (mayInterruptIfRunning && messageProducer instanceof Closeable) {
          try {
            ((Closeable) messageProducer).close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        return result.cancel(mayInterruptIfRunning);
      }

      @Override
      public boolean isCancelled() {
        return result.isCancelled();
      }

      @Override
      public boolean isDone() {
        return result.isDone();
      }

      @Override
      public Void get() throws InterruptedException, ExecutionException {
        return (Void) result.get();
      }

      @Override
      public Void get(long timeout, TimeUnit unit)
          throws InterruptedException, ExecutionException, TimeoutException {
        return (Void) result.get(timeout, unit);
      }
    };
  }

  /**
   * Start a thread that listens for messages in the message producer and forwards them to the
   * message consumer.
   *
   * @param executorService - the thread is started using this service
   * @return a future that is resolved when the started thread is terminated, e.g. by closing a
   *     stream
   */
  public Future<Void> beginProcessing(ExecutorService executorService) {
    final Future<?> result = executorService.submit(this);
    return wrapFuture(result, messageProducer);
  }

  public void run() {
    processingStarted();
    try {
      messageProducer.listen(messageConsumer);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    } finally {
      processingEnded();
    }
  }

  protected void processingStarted() {
    if (isRunning) {
      throw new IllegalStateException("The message processor is already running.");
    }
    isRunning = true;
  }

  protected void processingEnded() {
    isRunning = false;
  }
}
