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

import com.itsaky.androidide.classfile.ClassFileVersion
import com.itsaky.androidide.classfile.ClassFileVersion.JAVA_5
import com.itsaky.androidide.classfile.IClassFile
import java.io.DataInputStream

/**
 * An attribute which stores information about annotations.
 *
 * @author Akash Yadav
 */
abstract class AnnotationAttr(nameIndex: Int) : BaseAttribute(nameIndex) {
  override val version: ClassFileVersion = JAVA_5

  /** The annotations. */
  var annotations: Array<Annotation> = emptyArray()

  override fun read(attrLength: Int, file: IClassFile, input: DataInputStream) {
    val count = input.readUnsignedShort()
    this.annotations = Array(count) { Annotation.read(input) }
  }
}
