/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itsaky.androidide.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;

/**
 * {@link InputStream} implementation that reads a character stream from a {@link Reader} and
 * transforms it to a byte stream using a specified charset encoding. The stream is transformed
 * using a {@link CharsetEncoder} object, guaranteeing that all charset encodings supported by the
 * JRE are handled correctly. In particular for charsets such as UTF-16, the implementation ensures
 * that one and only one byte order marker is produced.
 *
 * <p>Since in general it is not possible to predict the number of characters to be read from the
 * {@link Reader} to satisfy a read request on the {@link ReaderInputStream}, all reads from the
 * {@link Reader} are buffered. There is therefore no well defined correlation between the current
 * position of the {@link Reader} and that of the {@link ReaderInputStream}. This also implies that
 * in general there is no need to wrap the underlying {@link Reader} in a {@link
 * java.io.BufferedReader}.
 *
 * <p>{@link ReaderInputStream} implements the inverse transformation of {@link
 * java.io.InputStreamReader}; in the following example, reading from {@code in2} would return the
 * same byte sequence as reading from {@code in} (provided that the initial byte sequence is legal
 * with respect to the charset encoding):
 *
 * <pre>
 * InputStream inputStream = ...
 * Charset cs = ...
 * InputStreamReader reader = new InputStreamReader(inputStream, cs);
 * ReaderInputStream in2 = new ReaderInputStream(reader, cs);
 * </pre>
 *
 * <p>{@link ReaderInputStream} implements the same transformation as {@link
 * java.io.OutputStreamWriter}, except that the control flow is reversed: both classes transform a
 * character stream into a byte stream, but {@link java.io.OutputStreamWriter} pushes data to the
 * underlying stream, while {@link ReaderInputStream} pulls it from the underlying stream.
 *
 * <p>Note that while there are use cases where there is no alternative to using this class, very
 * often the need to use this class is an indication of a flaw in the design of the code. This class
 * is typically used in situations where an existing API only accepts an {@link InputStream}, but
 * where the most natural way to produce the data is as a character stream, i.e. by providing a
 * {@link Reader} instance. An example of a situation where this problem may appear is when
 * implementing the {@code javax.activation.DataSource} interface from the Java Activation
 * Framework.
 *
 * <p>Given the fact that the {@link Reader} class doesn't provide any way to predict whether the
 * next read operation will block or not, it is not possible to provide a meaningful implementation
 * of the {@link InputStream#available()} method. A call to this method will always return 0. Also,
 * this class doesn't support {@link InputStream#mark(int)}.
 *
 * <p>Instances of {@link ReaderInputStream} are not thread safe.
 *
 * @since 2.0
 */
public class ReaderInputStream extends InputStream {
  private static final int DEFAULT_BUFFER_SIZE = 1024;

  static int checkMinBufferSize(final CharsetEncoder charsetEncoder, final int bufferSize) {
    final float minRequired = minBufferSize(charsetEncoder);
    if (bufferSize < minRequired) {
      throw new IllegalArgumentException(
          String.format(
              "Buffer size %,d must be at least %s for a CharsetEncoder %s.",
              bufferSize, minRequired, charsetEncoder.charset().displayName()));
    }
    return bufferSize;
  }

  static float minBufferSize(final CharsetEncoder charsetEncoder) {
    return charsetEncoder.maxBytesPerChar() * 2;
  }

  private final Reader reader;

  private final CharsetEncoder charsetEncoder;

  /**
   * CharBuffer used as input for the decoder. It should be reasonably large as we read data from
   * the underlying Reader into this buffer.
   */
  private final CharBuffer encoderIn;
  /**
   * ByteBuffer used as output for the decoder. This buffer can be small as it is only used to
   * transfer data from the decoder to the buffer provided by the caller.
   */
  private final ByteBuffer encoderOut;

  private CoderResult lastCoderResult;

  private boolean endOfInput;

  /**
   * Constructs a new {@link ReaderInputStream} that uses the default character encoding with a
   * default input buffer size of {@value #DEFAULT_BUFFER_SIZE} characters.
   *
   * @param reader the target {@link Reader}
   * @deprecated 2.5 use {@link #ReaderInputStream(Reader, Charset)} instead
   */
  @Deprecated
  public ReaderInputStream(final Reader reader) {
    this(reader, Charset.defaultCharset());
  }

  /**
   * Constructs a new {@link ReaderInputStream} with a default input buffer size of {@value
   * #DEFAULT_BUFFER_SIZE} characters.
   *
   * @param reader the target {@link Reader}
   * @param charset the charset encoding
   */
  public ReaderInputStream(final Reader reader, final Charset charset) {
    this(reader, charset, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructs a new {@link ReaderInputStream}.
   *
   * @param reader the target {@link Reader}.
   * @param charset the charset encoding.
   * @param bufferSize the size of the input buffer in number of characters.
   */
  public ReaderInputStream(final Reader reader, final Charset charset, final int bufferSize) {
    // @formatter:off
    this(
        reader,
        toCharset(charset)
            .newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE),
        bufferSize);
    // @formatter:on
  }

  /**
   * Constructs a new {@link ReaderInputStream}.
   *
   * @param reader the target {@link Reader}
   * @param charsetEncoder the charset encoder
   * @since 2.1
   */
  public ReaderInputStream(final Reader reader, final CharsetEncoder charsetEncoder) {
    this(reader, charsetEncoder, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructs a new {@link ReaderInputStream}.
   *
   * @param reader the target {@link Reader}
   * @param charsetEncoder the charset encoder, null defauls to the default Charset encoder.
   * @param bufferSize the size of the input buffer in number of characters
   * @since 2.1
   */
  public ReaderInputStream(
      final Reader reader, final CharsetEncoder charsetEncoder, final int bufferSize) {
    this.reader = reader;
    this.charsetEncoder = toCharsetEncoder(charsetEncoder);
    this.encoderIn = CharBuffer.allocate(checkMinBufferSize(this.charsetEncoder, bufferSize));
    this.encoderIn.flip();
    this.encoderOut = ByteBuffer.allocate(128);
    this.encoderOut.flip();
  }

  /**
   * Constructs a new {@link ReaderInputStream} with a default input buffer size of {@value
   * #DEFAULT_BUFFER_SIZE} characters.
   *
   * @param reader the target {@link Reader}
   * @param charsetName the name of the charset encoding
   */
  public ReaderInputStream(final Reader reader, final String charsetName) {
    this(reader, charsetName, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructs a new {@link ReaderInputStream}.
   *
   * @param reader the target {@link Reader}
   * @param charsetName the name of the charset encoding, null maps to the default Charset.
   * @param bufferSize the size of the input buffer in number of characters
   */
  public ReaderInputStream(final Reader reader, final String charsetName, final int bufferSize) {
    this(reader, toCharset(charsetName), bufferSize);
  }

  /**
   * Close the stream. This method will cause the underlying {@link Reader} to be closed.
   *
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public void close() throws IOException {
    reader.close();
  }

  /**
   * Fills the internal char buffer from the reader.
   *
   * @throws IOException If an I/O error occurs
   */
  private void fillBuffer() throws IOException {
    if (!endOfInput && (lastCoderResult == null || lastCoderResult.isUnderflow())) {
      encoderIn.compact();
      final int position = encoderIn.position();
      // We don't use Reader#read(CharBuffer) here because it is more efficient
      // to write directly to the underlying char array (the default implementation
      // copies data to a temporary char array).
      final int c = reader.read(encoderIn.array(), position, encoderIn.remaining());
      if (c == -1) {
        endOfInput = true;
      } else {
        encoderIn.position(position + c);
      }
      encoderIn.flip();
    }
    encoderOut.compact();
    lastCoderResult = charsetEncoder.encode(encoderIn, encoderOut, endOfInput);
    if (endOfInput) {
      lastCoderResult = charsetEncoder.flush(encoderOut);
    }
    if (lastCoderResult.isError()) {
      lastCoderResult.throwException();
    }
    encoderOut.flip();
  }

  /**
   * Gets the CharsetEncoder.
   *
   * @return the CharsetEncoder.
   */
  CharsetEncoder getCharsetEncoder() {
    return charsetEncoder;
  }

  /**
   * Read a single byte.
   *
   * @return either the byte read or {@code -1} if the end of the stream has been reached
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public int read() throws IOException {
    for (; ; ) {
      if (encoderOut.hasRemaining()) {
        return encoderOut.get() & 0xFF;
      }
      fillBuffer();
      if (endOfInput && !encoderOut.hasRemaining()) {
        return -1;
      }
    }
  }

  /**
   * Read the specified number of bytes into an array.
   *
   * @param b the byte array to read into
   * @return the number of bytes read or {@code -1} if the end of the stream has been reached
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public int read(final byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  /**
   * Read the specified number of bytes into an array.
   *
   * @param array the byte array to read into
   * @param off the offset to start reading bytes into
   * @param len the number of bytes to read
   * @return the number of bytes read or {@code -1} if the end of the stream has been reached
   * @throws IOException if an I/O error occurs.
   */
  @Override
  public int read(final byte[] array, int off, int len) throws IOException {
    Objects.requireNonNull(array, "array");
    if (len < 0 || off < 0 || (off + len) > array.length) {
      throw new IndexOutOfBoundsException(
          "Array size=" + array.length + ", offset=" + off + ", length=" + len);
    }
    int read = 0;
    if (len == 0) {
      return 0; // Always return 0 if len == 0
    }
    while (len > 0) {
      if (encoderOut.hasRemaining()) { // Data from the last read not fully copied
        final int c = Math.min(encoderOut.remaining(), len);
        encoderOut.get(array, off, c);
        off += c;
        len -= c;
        read += c;
      } else if (endOfInput) { // Already reach EOF in the last read
        break;
      } else { // Read again
        fillBuffer();
      }
    }
    return read == 0 && endOfInput ? -1 : read;
  }

  static Charset toCharset(final Charset charset) {
    return charset == null ? Charset.defaultCharset() : charset;
  }

  static Charset toCharset(final String charsetName) throws UnsupportedCharsetException {
    return charsetName == null ? Charset.defaultCharset() : Charset.forName(charsetName);
  }

  static CharsetEncoder toCharsetEncoder(CharsetEncoder charsetEncoder) {
    return charsetEncoder != null ? charsetEncoder : Charset.defaultCharset().newEncoder();
  }
}
