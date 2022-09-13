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

package com.itsaky.androidide.classfile.attributes

import java.io.DataInputStream

/**
 * Target information in a type annotation.
 *
 * @author Akash Yadav
 */
abstract class TargetInfo(val currentType: Int, val types: IntArray) {

  /** The read the target info. */
  internal abstract fun read(input: DataInputStream)

  companion object {

    internal fun forType(type: Int): TargetInfo {
      return when (type) {
        0x00,
        0x01 -> TypeParameterTarget(type)
        0x10 -> SupertypeTarget(type)
        0x11,
        0x12 -> TypeParameterBoundTarget(type)
        0x13,
        0x14,
        0x15 -> EmptyTarget(type)
        0x16 -> FormalParameterTarget(type)
        0x17 -> ThrowsTarget(type)
        0x40,
        0x41 -> LocalVarTarget(type)
        0x42 -> CatchTarget(type)
        0x43,
        0x44,
        0x45,
        0x46 -> OffsetTarget(type)
        0x47,
        0x48,
        0x49,
        0x4A,
        0x4B -> TypeArgumentTarget(type)
        else -> throw IllegalArgumentException("Unknown type parameter target")
      }
    }
  }
}

open class TypeParameterTarget
@JvmOverloads
constructor(currentType: Int, types: IntArray = intArrayOf(0x00, 0x01)) :
  TargetInfo(currentType, types) {

  /**
   * The value of the type_parameter_index item specifies which type parameter declaration is
   * annotated. A type_parameter_index value of 0 specifies the first type parameter declaration.
   */
  var typeParameterIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.typeParameterIndex = input.readUnsignedByte()
  }
}

class SupertypeTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x10)) {

  /**
   * A supertype_index value of 65535 specifies that the annotation appears on the superclass in an
   * extends clause of a class declaration.
   *
   * Any other supertype_index value is an index into the interfaces array of the enclosing
   * ClassFile structure, and specifies that the annotation appears on that superinterface in either
   * the implements clause of a class declaration or the extends clause of an interface declaration.
   */
  var supertypeIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.supertypeIndex = input.readUnsignedShort()
  }
}

class TypeParameterBoundTarget(currentType: Int) :
  TypeParameterTarget(currentType, intArrayOf(0x11, 0x12)) {

  /**
   * The value of the bound_index item specifies which bound of the type parameter declaration
   * indicated by type_parameter_index is annotated. A bound_index value of 0 specifies the first
   * bound of a type parameter declaration.
   */
  var boundIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    super.read(input)
    this.boundIndex = input.readUnsignedShort()
  }
}

class EmptyTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x13, 0x14, 0x15)) {

  override fun read(input: DataInputStream) {}
}

class FormalParameterTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x16)) {

  /**
   * The value of the formal_parameter_index item specifies which formal parameter declaration has
   * an annotated type. A formal_parameter_index value of i may, but is not required to, correspond
   * to the i'th parameter descriptor in the method descriptor.
   */
  var formalParameterIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.formalParameterIndex = input.readUnsignedByte()
  }
}

class ThrowsTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x17)) {

  /**
   * The value of the throws_type_index item is an index into the exception_index_table array of the
   * Exceptions attribute of the method_info structure enclosing the RuntimeVisibleTypeAnnotations
   * attribute.
   */
  var throwsTypeIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.throwsTypeIndex = input.readUnsignedShort()
  }
}

class LocalVarTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x40, 0x41)) {

  var table: Array<LocalVarTagetTableEntry> = emptyArray()

  class LocalVarTagetTableEntry {
    var startPc: Int = -1
      internal set
    var length: Int = -1
      internal set
    var index: Int = -1
      internal set
  }

  override fun read(input: DataInputStream) {
    val length = input.readUnsignedShort()
    this.table =
      Array(length) {
        val entry = LocalVarTagetTableEntry()
        entry.startPc = input.readUnsignedShort()
        entry.length = input.readUnsignedShort()
        entry.index = input.readUnsignedShort()
        entry
      }
  }
}

class CatchTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x42)) {

  var exceptionTableIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.exceptionTableIndex = input.readUnsignedShort()
  }
}

class OffsetTarget(currentType: Int) : TargetInfo(currentType, intArrayOf(0x43, 0x44, 0x45, 0x46)) {

  var offset: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.offset = input.readUnsignedShort()
  }
}

class TypeArgumentTarget(currentType: Int) :
  TargetInfo(currentType, intArrayOf(0x47, 0x48, 0x49, 0x4A, 0x4B)) {

  var offset: Int = -1
    internal set

  var typeArgumentIndex: Int = -1
    internal set

  override fun read(input: DataInputStream) {
    this.offset = input.readUnsignedShort()
    this.typeArgumentIndex = input.readUnsignedByte()
  }
}
