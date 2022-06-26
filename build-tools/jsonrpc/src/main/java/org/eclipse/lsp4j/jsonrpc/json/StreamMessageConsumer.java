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

import org.eclipse.lsp4j.jsonrpc.JsonRpcException;
import org.eclipse.lsp4j.jsonrpc.MessageConsumer;
import org.eclipse.lsp4j.jsonrpc.messages.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** A message consumer that serializes messages to JSON and sends them to an output stream. */
public class StreamMessageConsumer implements MessageConsumer, MessageConstants {

  private final String encoding;
  private final MessageJsonHandler jsonHandler;

  private final Object outputLock = new Object();

  private OutputStream output;

  public StreamMessageConsumer(MessageJsonHandler jsonHandler) {
    this(null, StandardCharsets.UTF_8.name(), jsonHandler);
  }

  public StreamMessageConsumer(
      OutputStream output, String encoding, MessageJsonHandler jsonHandler) {
    this.output = output;
    this.encoding = encoding;
    this.jsonHandler = jsonHandler;
  }

  public StreamMessageConsumer(OutputStream output, MessageJsonHandler jsonHandler) {
    this(output, StandardCharsets.UTF_8.name(), jsonHandler);
  }

  public OutputStream getOutput() {
    return output;
  }

  public void setOutput(OutputStream output) {
    this.output = output;
  }

  @Override
  public void consume(Message message) {
    try {
      String content = jsonHandler.serialize(message);
      byte[] contentBytes = content.getBytes(encoding);
      int contentLength = contentBytes.length;

      String header = getHeader(contentLength);
      byte[] headerBytes = header.getBytes(StandardCharsets.US_ASCII);

      synchronized (outputLock) {
        output.write(headerBytes);
        output.write(contentBytes);
        output.flush();
      }
    } catch (IOException exception) {
      throw new JsonRpcException(exception);
    }
  }

  /**
   * Construct a header to be prepended to the actual content. This implementation writes {@code
   * Content-Length} and {@code Content-Type} attributes according to the LSP specification.
   */
  protected String getHeader(int contentLength) {
    StringBuilder headerBuilder = new StringBuilder();
    appendHeader(headerBuilder, CONTENT_LENGTH_HEADER, contentLength).append(CRLF);
    if (!StandardCharsets.UTF_8.name().equals(encoding)) {
      appendHeader(headerBuilder, CONTENT_TYPE_HEADER, JSON_MIME_TYPE);
      headerBuilder.append("; charset=").append(encoding).append(CRLF);
    }
    headerBuilder.append(CRLF);
    return headerBuilder.toString();
  }

  /** Append a header attribute to the given builder. */
  protected StringBuilder appendHeader(StringBuilder builder, String name, Object value) {
    return builder.append(name).append(": ").append(value);
  }
}
