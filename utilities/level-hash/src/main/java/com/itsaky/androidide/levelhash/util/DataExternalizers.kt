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

import com.itsaky.androidide.levelhash.DataExternalizer
import java.io.DataInput
import java.io.DataOutput

/**
 * [DataExternalizer] implementations for various types.
 *
 * @author Akash Yadav
 */
object DataExternalizers {

  const val SIZE_BOOLEAN = 1L
  const val SIZE_BYTE = 1L
  const val SIZE_SHORT = 2L
  const val SIZE_CHAR = 2L
  const val SIZE_INT = 4L
  const val SIZE_LONG = 8L
  const val SIZE_FLOAT = 4L
  const val SIZE_DOUBLE = 8L

  /**
   * Data externalizer for unsigned [Long]s.
   */
  val BOOLEAN = object : DataExternalizer<Boolean> {
    override fun read(input: DataInput): Boolean {
      return input.readBoolean()
    }

    override fun write(out: DataOutput, data: Boolean) {
      out.writeBoolean(data)
    }
  }

  /**
   * Data externalizer for [Byte]s.
   */
  val BYTE = object : DataExternalizer<Byte> {
    override fun read(input: DataInput): Byte {
      return input.readByte()
    }

    override fun write(out: DataOutput, data: Byte) {
      out.write(data.toInt())
    }
  }

  /**
   * Data externalizer for unsigned [Byte]s.
   */
  val UBYTE = object : DataExternalizer<Int> {
    override fun read(input: DataInput): Int {
      return input.readUnsignedByte()
    }

    override fun write(out: DataOutput, data: Int) {
      out.writeByte(data)
    }
  }

  /**
   * Data externalizer for [Short]s.
   */
  val SHORT = object : DataExternalizer<Short> {
    override fun read(input: DataInput): Short {
      return input.readShort()
    }

    override fun write(out: DataOutput, data: Short) {
      out.writeShort(data.toInt())
    }
  }

  /**
   * Data externalizer for unsigned [Short]s.
   */
  val USHORT = object : DataExternalizer<Int> {
    override fun read(input: DataInput): Int {
      return input.readUnsignedShort()
    }

    override fun write(out: DataOutput, data: Int) {
      out.writeShort(data)
    }
  }

  /**
   * Data externalizer for [Char]s.
   */
  val CHAR = object : DataExternalizer<Char> {
    override fun read(input: DataInput): Char {
      return input.readChar()
    }

    override fun write(out: DataOutput, data: Char) {
      out.writeChar(data.code)
    }
  }

  /**
   * Data externalizer for [Int]s.
   */
  val INT = object : DataExternalizer<Int> {
    override fun read(input: DataInput): Int {
      return input.readInt()
    }

    override fun write(out: DataOutput, data: Int) {
      out.write(data)
    }
  }

  /**
   * Data externalizer for [Long]s.
   */
  val LONG = object : DataExternalizer<Long> {
    override fun read(input: DataInput): Long {
      return input.readLong()
    }

    override fun write(out: DataOutput, data: Long) {
      out.writeLong(data)
    }
  }

  /**
   * Data externalizer for [Float]s.
   */
  val FLOAT = object : DataExternalizer<Float> {
    override fun read(input: DataInput): Float {
      return input.readFloat()
    }

    override fun write(out: DataOutput, data: Float) {
      out.writeFloat(data)
    }
  }

  /**
   * Data externalizer for [Double]s.
   */
  val DOUBLE = object : DataExternalizer<Double> {
    override fun read(input: DataInput): Double {
      return input.readDouble()
    }

    override fun write(out: DataOutput, data: Double) {
      out.writeDouble(data)
    }
  }

  /**
   * Data externalizer for [String]s.
   */
  val STRING = object : DataExternalizer<String> {
    override fun read(input: DataInput): String {
      return input.readUTF()
    }

    override fun write(out: DataOutput, data: String) {
      out.writeUTF(data)
    }
  }
}