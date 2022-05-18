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

import org.eclipse.lsp4j.jsonrpc.JsonRpcException;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.MessageIssueException;
import org.eclipse.lsp4j.jsonrpc.MessageIssueHandler;
import org.eclipse.lsp4j.jsonrpc.MessageProducer;
import org.eclipse.lsp4j.jsonrpc.messages.Message;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/** A message producer that reads from an input stream and parses messages from JSON. */
public class StreamMessageProducer implements MessageProducer, Closeable, MessageConstants {

  private static final ILogger LOG = ILogger.newInstance("StreamMessageProducer");

  private final MessageJsonHandler jsonHandler;
  private final MessageIssueHandler issueHandler;

  private InputStream input;

  private MessageConsumer callback;
  private boolean keepRunning;

  public StreamMessageProducer(InputStream input, MessageJsonHandler jsonHandler) {
    this(input, jsonHandler, null);
  }

  public StreamMessageProducer(
      InputStream input, MessageJsonHandler jsonHandler, MessageIssueHandler issueHandler) {
    this.input = input;
    this.jsonHandler = jsonHandler;
    this.issueHandler = issueHandler;
  }

  public InputStream getInput() {
    return input;
  }

  public void setInput(InputStream input) {
    this.input = input;
  }

  @Override
  public void listen(MessageConsumer callback) {
    if (keepRunning) {
      throw new IllegalStateException("This StreamMessageProducer is already running.");
    }
    this.keepRunning = true;
    this.callback = callback;
    try {
      StringBuilder headerBuilder = null;
      StringBuilder debugBuilder = null;
      boolean newLine = false;
      Headers headers = new Headers();
      while (keepRunning) {
        int c = input.read();
        if (c == -1) {
          // End of input stream has been reached
          keepRunning = false;
        } else {
          if (debugBuilder == null) debugBuilder = new StringBuilder();
          debugBuilder.append((char) c);
          if (c == '\n') {
            if (newLine) {
              // Two consecutive newlines have been read, which signals the start of the message
              // content
              if (headers.contentLength < 0) {
                fireError(
                    new IllegalStateException(
                        "Missing header "
                            + CONTENT_LENGTH_HEADER
                            + " in input \""
                            + debugBuilder
                            + "\""));
              } else {
                boolean result = handleMessage(input, headers);
                if (!result) keepRunning = false;
                newLine = false;
              }
              headers = new Headers();
              debugBuilder = null;
            } else if (headerBuilder != null) {
              // A single newline ends a header line
              parseHeader(headerBuilder.toString(), headers);
              headerBuilder = null;
            }
            newLine = true;
          } else if (c != '\r') {
            // Add the input to the current header line
            if (headerBuilder == null) headerBuilder = new StringBuilder();
            headerBuilder.append((char) c);
            newLine = false;
          }
        }
      } // while (keepRunning)
    } catch (IOException exception) {
      if (JsonRpcException.indicatesStreamClosed(exception)) {
        // Only log the error if we had intended to keep running
        if (keepRunning) fireStreamClosed(exception);
      } else throw new JsonRpcException(exception);
    } finally {
      this.callback = null;
      this.keepRunning = false;
    }
  }

  /** Log an error. */
  protected void fireError(Throwable error) {
    String message =
        error.getMessage() != null
            ? error.getMessage()
            : "An error occurred while processing an incoming message.";
    LOG.error(message, error);
  }

  /**
   * Read the JSON content part of a message, parse it, and notify the callback.
   *
   * @return {@code true} if we should continue reading from the input stream, {@code false} if we
   *     should stop
   */
  protected boolean handleMessage(InputStream input, Headers headers) throws IOException {
    if (callback == null) callback = message -> LOG.info("Received message: " + message);

    try {
      int contentLength = headers.contentLength;
      byte[] buffer = new byte[contentLength];
      int bytesRead = 0;

      while (bytesRead < contentLength) {
        int readResult = input.read(buffer, bytesRead, contentLength - bytesRead);
        if (readResult == -1) return false;
        bytesRead += readResult;
      }

      final var content = new String(buffer, headers.charset);

      try {
        Message message = jsonHandler.parseMessage(content);
        callback.consume(message);
      } catch (MessageIssueException exception) {
        // An issue was found while parsing or validating the message
        if (issueHandler != null)
          issueHandler.handle(exception.getRpcMessage(), exception.getIssues());
        else fireError(exception);
      }
    } catch (Exception exception) {
      // UnsupportedEncodingException can be thrown by String constructor
      // JsonParseException can be thrown by jsonHandler
      // We also catch arbitrary exceptions that are thrown by message consumers in order to keep
      // this thread alive
      fireError(exception);
    }
    return true;
  }

  /** Parse a header attribute and set the corresponding data in the {@link Headers} fields. */
  protected void parseHeader(String line, Headers headers) {
    int sepIndex = line.indexOf(':');
    if (sepIndex >= 0) {
      String key = line.substring(0, sepIndex).trim();
      switch (key) {
        case CONTENT_LENGTH_HEADER:
          try {
            headers.contentLength = Integer.parseInt(line.substring(sepIndex + 1).trim());
          } catch (NumberFormatException e) {
            fireError(e);
          }
          break;
        case CONTENT_TYPE_HEADER:
          {
            int charsetIndex = line.indexOf("charset=");
            if (charsetIndex >= 0) headers.charset = line.substring(charsetIndex + 8).trim();
            break;
          }
      }
    }
  }

  /** Report that the stream was closed through an exception. */
  protected void fireStreamClosed(Exception cause) {
    String message =
        cause.getMessage() != null ? cause.getMessage() : "The input stream was closed.";
    LOG.info(message, cause);
  }

  @Override
  public void close() {
    keepRunning = false;
  }

  protected static class Headers {
    public int contentLength = -1;
    public String charset = StandardCharsets.UTF_8.name();
  }
}
