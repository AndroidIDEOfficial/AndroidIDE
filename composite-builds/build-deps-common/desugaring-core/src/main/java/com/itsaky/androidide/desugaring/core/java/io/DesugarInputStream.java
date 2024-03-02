/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.desugaring.core.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Akash Yadav
 */
@SuppressWarnings("unused")
public class DesugarInputStream {

  /**
   * The maximum size of array to allocate. Some VMs reserve some header words in an array. Attempts
   * to allocate larger arrays may result in OutOfMemoryError: Requested array size exceeds VM
   * limit
   */
  private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;

  // MAX_SKIP_BUFFER_SIZE is used to determine the maximum buffer size to
  // use when skipping.
  private static final int MAX_SKIP_BUFFER_SIZE = 2048;

  private static final int DEFAULT_BUFFER_SIZE = 16384;

  private DesugarInputStream() {
    throw new UnsupportedOperationException();
  }

  public static byte[] readAllBytes(InputStream in) throws IOException {
    return readNBytes(in, Integer.MAX_VALUE);
  }

  public static byte[] readNBytes(InputStream in, int len) throws IOException {
    if (len < 0) {
      throw new IllegalArgumentException("len < 0");
    }

    List<byte[]> bufs = null;
    byte[] result = null;
    int total = 0;
    int remaining = len;
    int n;
    do {
      byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
      int nread = 0;

      // read to EOF which may read more or less than buffer size
      while ((n = in.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
        nread += n;
        remaining -= n;
      }

      if (nread > 0) {
        if (MAX_BUFFER_SIZE - total < nread) {
          throw new OutOfMemoryError("Required array size too large");
        }
        if (nread < buf.length) {
          buf = Arrays.copyOfRange(buf, 0, nread);
        }
        total += nread;
        if (result == null) {
          result = buf;
        } else {
          if (bufs == null) {
            bufs = new ArrayList<>();
            bufs.add(result);
          }
          bufs.add(buf);
        }
      }
      // if the last call to read returned -1 or the number of bytes
      // requested have been read then break
    } while (n >= 0 && remaining > 0);

    if (bufs == null) {
      if (result == null) {
        return new byte[0];
      }
      return result.length == total ? result : Arrays.copyOf(result, total);
    }

    result = new byte[total];
    int offset = 0;
    remaining = total;
    for (byte[] b : bufs) {
      int count = Math.min(b.length, remaining);
      System.arraycopy(b, 0, result, offset, count);
      offset += count;
      remaining -= count;
    }

    return result;
  }

  public static int readNBytes(InputStream input, byte[] b, int off, int len) throws IOException {
    Objects.checkFromIndexSize(off, len, b.length);

    int n = 0;
    while (n < len) {
      int count = input.read(b, off + n, len - n);
      if (count < 0) {
        break;
      }
      n += count;
    }
    return n;
  }

  public static long transferTo(InputStream in, OutputStream out) throws IOException {
    Objects.requireNonNull(out, "out");
    long transferred = 0;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    int read;
    while ((read = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) >= 0) {
      out.write(buffer, 0, read);
      transferred += read;
    }
    return transferred;
  }
}
