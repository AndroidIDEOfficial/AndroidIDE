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

package com.itsaky.androidide.classfile

import com.itsaky.androidide.classfile.constants.IConstant

/**
 * Model class for holding information about class files.
 *
 * @author Akash Yadav
 */
interface IClassFile {

  /** The class file version. */
  val version: ClassFileVersion

  /** The constant pool. */
  val constantPool: Array<IConstant>

  /** Access flags for the class. */
  val accessFlags: Int

  /**
   * The index in the constant pool which corresponds to the internal representation of the name of
   * this class.
   */
  val thisClass: Int

  /**
   * The index in the constant pool which corresponds to the internal representation of the name of
   * the super class of this class.
   */
  val superClass: Int

  /** The interfaces that this class implements. */
  val interfaces: IntArray

  /** The fields of this class. */
  val fields: Array<IFieldInfo>

  /** Get the string representation of this class' name. */
  fun getName(): String

  /** Get the string representation of the name of this class' super class. */
  fun getSuperClassName(): String
}
