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

package com.itsaky.androidide.levelhash.util

import com.itsaky.androidide.levelhash.RandomAccessIO
import com.itsaky.androidide.levelhash.seekByte
import com.itsaky.androidide.levelhash.seekChar
import com.itsaky.androidide.levelhash.seekDouble
import com.itsaky.androidide.levelhash.seekFloat
import com.itsaky.androidide.levelhash.seekInt
import com.itsaky.androidide.levelhash.seekLong
import com.itsaky.androidide.levelhash.seekShort
import org.slf4j.LoggerFactory
import java.io.DataInput
import java.io.DataInputStream
import java.io.DataOutput
import java.io.EOFException
import java.nio.MappedByteBuffer
import kotlin.math.min

/**
 * Implementation of the [DataInput] and [DataOutput]
 * backed by a [MappedByteBuffer].
 *
 * @author Akash Yadav
 */
internal class MappedRandomAccessIO : DataInput, DataOutput, RandomAccessIO, AutoCloseable {

  private var positionOffset = 0L
  private var position = 0L
  private var size = 0L

  private lateinit var buffer: MappedByteBuffer

  companion object {
    private val log = LoggerFactory.getLogger(MappedRandomAccessIO::class.java)
  }

  internal fun buf(): MappedByteBuffer = buf(this.position)

  internal fun buf(position: Long): MappedByteBuffer {
    return this.buffer.apply { position(position.toInt()) }
  }

  fun reset(buffer: MappedByteBuffer, position: Long, length: Long, positionOffset: Long = 0L) {
    this.buffer = buffer
    this.position = position
    this.size = length
    this.positionOffset = positionOffset
  }

  override fun close() {
    if (!::buffer.isInitialized) {
      return
    }

    try {
      buffer.unmap()
    } catch (err: Throwable) {
      log.error("Failed to close memory-mapped buffer", err)
    }
  }

  override fun position(position: Long) {
    require(position >= 0L) { "position must be >= 0, position=$position" }
    require(position < buffer.limit()) {
      "position must be < buffer.limit(), position=$position buffer.limit=${buffer.limit()}"
    }
    tryPosition(position)
  }

  override fun tryPosition(position: Long): Boolean {
    if (position < 0L || position >= buffer.limit()) {
      return false
    }
    val currentPos = position()
    if (currentPos == position) {
      return true
    }
    this.position = this.positionOffset + position
    return true
  }

  override fun position(): Long {
    return this.position
  }

  fun size() = this.size

  override fun readFully(bytes: ByteArray) {
    if (bytes.isEmpty()) return
    readFully(bytes, 0, bytes.size)
  }

  override fun readFully(bytes: ByteArray, offset: Int, length: Int) {
    if (this.position() + length > size()) {
      throw EOFException("Cannot read past end of segment")
    }

    for (i in 0 until length) {
      bytes[offset + i] = readByte()
    }
  }

  override fun skipBytes(count: Int): Int {
    val position = position()
    val avltoRead = this.size() - position
    val skip = min(avltoRead.toInt(), count)
    position(position + skip)
    return skip
  }

  override fun readBoolean(): Boolean {
    return readUnsignedByte() == 1
  }

  override fun readByte(): Byte {
    return buf(position).get().also { seekByte() }
  }

  override fun readUnsignedByte(): Int {
    val b = readByte()
    return b.toInt() and 0xFF
  }

  override fun readShort(): Short {
    return buf().short.also { seekShort() }
  }

  override fun readUnsignedShort(): Int {
    val r = readShort()
    return r.toInt() and 0xFFFF
  }

  override fun readChar(): Char {
    return buf().char.also { seekChar() }
  }

  override fun readInt(): Int {
    return buf().int.also { seekInt() }
  }

  override fun readLong(): Long {
    return buf().long.also { seekLong() }
  }

  override fun readFloat(): Float {
    return buf().float.also { seekFloat() }
  }

  override fun readDouble(): Double {
    return buf().double.also { seekDouble() }
  }

  override fun readLine(): String? {
    throw UnsupportedOperationException()
  }

  override fun readUTF(): String {
    return DataInputStream.readUTF(this)
  }

  override fun write(b: Int) {
    buf().put(b.toByte())
    seekByte()
  }

  override fun write(bytes: ByteArray) {
    write(bytes, 0, bytes.size)
  }

  override fun write(bytes: ByteArray, offset: Int, length: Int) {
    buf().put(bytes, offset, length).also { seekRelative(length) }
  }

  override fun writeBoolean(boolean: Boolean) {
    write(if (boolean) 1 else 0)
  }

  override fun writeByte(byte: Int) {
    write(byte)
  }

  override fun writeShort(short: Int) {
    buf().putShort(short.toShort()).also { seekShort() }
  }

  override fun writeChar(char: Int) {
    buf().putChar(char.toChar()).also { seekChar() }
  }

  override fun writeInt(int: Int) {
    buf().putInt(int).also { seekInt() }
  }

  override fun writeLong(long: Long) {
    buf().putLong(long).also { seekLong() }
  }

  override fun writeFloat(float: Float) {
    buf().putFloat(float).also { seekFloat() }
  }

  override fun writeDouble(double: Double) {
    buf().putDouble(double).also { seekDouble() }
  }

  override fun writeBytes(str: String) {
    throw UnsupportedOperationException()
  }

  override fun writeChars(str: String) {
    throw UnsupportedOperationException()
  }

  override fun writeUTF(str: String) {
    UtfIO.writeUTF(str, this)
  }
}