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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;

/**
 * Implements an {@link InputStream} to read from String, StringBuffer, StringBuilder or CharBuffer.
 *
 * <p><strong>Note:</strong> Supports {@link #mark(int)} and {@link #reset()}.
 *
 * @since 2.2
 */
public class CharSequenceInputStream extends InputStream {

  private static final int BUFFER_SIZE = 2048;

  private static final int NO_MARK = -1;

  private final CharsetEncoder charsetEncoder;
  private final CharBuffer cBuf;
  private final ByteBuffer bBuf;

  private int cBufMark; // position in cBuf
  private int bBufMark; // position in bBuf

  /**
   * Constructs a new instance with a buffer size of 2048.
   *
   * @param cs the input character sequence.
   * @param charset the character set name to use.
   * @throws IllegalArgumentException if the buffer is not large enough to hold a complete
   *     character.
   */
  public CharSequenceInputStream(final CharSequence cs, final Charset charset) {
    this(cs, charset, BUFFER_SIZE);
  }

  /**
   * Constructs a new instance.
   *
   * @param cs the input character sequence.
   * @param charset the character set name to use, null maps to the default Charset.
   * @param bufferSize the buffer size to use.
   * @throws IllegalArgumentException if the buffer is not large enough to hold a complete
   *     character.
   */
  public CharSequenceInputStream(
      final CharSequence cs, final Charset charset, final int bufferSize) {
    // @formatter:off
    this.charsetEncoder =
        toCharset(charset)
            .newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
    // @formatter:on
    // Ensure that buffer is long enough to hold a complete character
    this.bBuf =
        ByteBuffer.allocate(ReaderInputStream.checkMinBufferSize(charsetEncoder, bufferSize));
    this.bBuf.flip();
    this.cBuf = CharBuffer.wrap(cs);
    this.cBufMark = NO_MARK;
    this.bBufMark = NO_MARK;
  }

  static Charset toCharset(final Charset charset) {
    return charset == null ? Charset.defaultCharset() : charset;
  }

  /**
   * Constructs a new instance with a buffer size of 2048.
   *
   * @param cs the input character sequence.
   * @param charset the character set name to use.
   * @throws IllegalArgumentException if the buffer is not large enough to hold a complete
   *     character.
   */
  public CharSequenceInputStream(final CharSequence cs, final String charset) {
    this(cs, charset, BUFFER_SIZE);
  }

  /**
   * Constructs a new instance.
   *
   * @param cs the input character sequence.
   * @param charset the character set name to use, null maps to the default Charset.
   * @param bufferSize the buffer size to use.
   * @throws IllegalArgumentException if the buffer is not large enough to hold a complete
   *     character.
   */
  public CharSequenceInputStream(
      final CharSequence cs, final String charset, final int bufferSize) {
    this(cs, toCharset(charset), bufferSize);
  }
  /**
   * Return an estimate of the number of bytes remaining in the byte stream.
   *
   * @return the count of bytes that can be read without blocking (or returning EOF).
   * @throws IOException if an error occurs (probably not possible).
   */
  @Override
  public int available() throws IOException {
    // The cached entries are in bbuf; since encoding always creates at least one byte
    // per character, we can add the two to get a better estimate (e.g. if bbuf is empty)
    // Note that the previous implementation (2.4) could return zero even though there were
    // encoded bytes still available.
    return this.bBuf.remaining() + this.cBuf.remaining();
  }

  static Charset toCharset(final String charsetName) throws UnsupportedCharsetException {
    return charsetName == null ? Charset.defaultCharset() : Charset.forName(charsetName);
  }

  @Override
  public void close() throws IOException {
    // noop
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
   * Fills the byte output buffer from the input char buffer.
   *
   * @throws CharacterCodingException an error encoding data.
   */
  private void fillBuffer() throws CharacterCodingException {
    this.bBuf.compact();
    final CoderResult result = this.charsetEncoder.encode(this.cBuf, this.bBuf, true);
    if (result.isError()) {
      result.throwException();
    }
    this.bBuf.flip();
  }

  /**
   * {@inheritDoc}
   *
   * @param readlimit max read limit (ignored).
   */
  @Override
  public synchronized void mark(final int readlimit) {
    this.cBufMark = this.cBuf.position();
    this.bBufMark = this.bBuf.position();
    this.cBuf.mark();
    this.bBuf.mark();
    // It would be nice to be able to use mark & reset on the cbuf and bbuf;
    // however the bbuf is re-used so that won't work
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  @Override
  public int read() throws IOException {
    for (; ; ) {
      if (this.bBuf.hasRemaining()) {
        return this.bBuf.get() & 0xFF;
      }
      fillBuffer();
      if (!this.bBuf.hasRemaining() && !this.cBuf.hasRemaining()) {
        return -1;
      }
    }
  }

  @Override
  public int read(final byte[] b) throws IOException {
    return read(b, 0, b.length);
  }

  @Override
  public int read(final byte[] array, int off, int len) throws IOException {
    Objects.requireNonNull(array, "array");
    if (len < 0 || (off + len) > array.length) {
      throw new IndexOutOfBoundsException(
          "Array Size=" + array.length + ", offset=" + off + ", length=" + len);
    }
    if (len == 0) {
      return 0; // must return 0 for zero length read
    }
    if (!this.bBuf.hasRemaining() && !this.cBuf.hasRemaining()) {
      return -1;
    }
    int bytesRead = 0;
    while (len > 0) {
      if (this.bBuf.hasRemaining()) {
        final int chunk = Math.min(this.bBuf.remaining(), len);
        this.bBuf.get(array, off, chunk);
        off += chunk;
        len -= chunk;
        bytesRead += chunk;
      } else {
        fillBuffer();
        if (!this.bBuf.hasRemaining() && !this.cBuf.hasRemaining()) {
          break;
        }
      }
    }
    return bytesRead == 0 && !this.cBuf.hasRemaining() ? -1 : bytesRead;
  }

  @Override
  public synchronized void reset() throws IOException {
    //
    // This is not the most efficient implementation, as it re-encodes from the beginning.
    //
    // Since the bbuf is re-used, in general it's necessary to re-encode the data.
    //
    // It should be possible to apply some optimisations however:
    // + use mark/reset on the cbuf and bbuf. This would only work if the buffer had not been
    // (re)filled since
    // the mark. The code would have to catch InvalidMarkException - does not seem possible to
    // check
    // if mark is
    // valid otherwise. + Try saving the state of the cbuf before each fillBuffer; it might be
    // possible to
    // restart from there.
    //
    if (this.cBufMark != NO_MARK) {
      // if cbuf is at 0, we have not started reading anything, so skip re-encoding
      if (this.cBuf.position() != 0) {
        this.charsetEncoder.reset();
        this.cBuf.rewind();
        this.bBuf.rewind();
        this.bBuf.limit(0); // rewind does not clear the buffer
        while (this.cBuf.position() < this.cBufMark) {
          this.bBuf.rewind(); // empty the buffer (we only refill when empty during normal
          // processing)
          this.bBuf.limit(0);
          fillBuffer();
        }
      }
      if (this.cBuf.position() != this.cBufMark) {
        throw new IllegalStateException(
            "Unexpected CharBuffer position: actual="
                + cBuf.position()
                + " "
                + "expected="
                + this.cBufMark);
      }
      this.bBuf.position(this.bBufMark);
      this.cBufMark = NO_MARK;
      this.bBufMark = NO_MARK;
    }
  }

  @Override
  public long skip(long n) throws IOException {
    //
    // This could be made more efficient by using position to skip within the current buffer.
    //
    long skipped = 0;
    while (n > 0 && available() > 0) {
      //noinspection ResultOfMethodCallIgnored
      this.read();
      n--;
      skipped++;
    }
    return skipped;
  }
}
