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

/*
 * Copyright (c) 1994, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.itsaky.androidide.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Reads all bytes from this input stream and writes the bytes to the
 * given output stream in the order that they are read. On return, this
 * input stream will be at end of stream. This method does not close either
 * stream.
 *
 * This method may block indefinitely reading from the input stream, or
 * writing to the output stream. The behavior for the case where the input
 * and/or output stream is _asynchronously closed_, or the thread
 * interrupted during the transfer, is highly input and output stream
 * specific, and therefore not specified.
 *
 * If an I/O error occurs reading from the input stream or writing to the
 * output stream, then it may do so after some bytes have been read or
 * written. Consequently the input stream may not be at end of stream and
 * one, or both, streams may be in an inconsistent state. It is strongly
 * recommended that both streams be promptly closed if an I/O error occurs.
 *
 * @param  output the output stream, non-null
 * @return the number of bytes transferred
 * @throws IOException if an I/O error occurs when reading or writing
 *
 */
@Throws(IOException::class, NullPointerException::class)
fun InputStream.transferToStream(output: OutputStream) : Long {
  var transferred: Long = 0
  val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
  var read: Int
  while (this.read(buffer, 0, DEFAULT_BUFFER_SIZE).also { read = it } >= 0) {
    output.write(buffer, 0, read)
    transferred += read.toLong()
  }
  return transferred
}