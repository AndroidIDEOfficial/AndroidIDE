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
import com.itsaky.androidide.classfile.attributes.IAttribute.Type
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.SIGNATURE
import java.io.DataInputStream

/**
 * Signature of a class, field or method.
 *
 * @author Akash Yadav
 */
class SignatureAttr(nameIndex: Int) : BaseAttribute(nameIndex) {
  override val version: ClassFileVersion = JAVA_5
  override val type: Type = SIGNATURE
  
  var signatureIndex: Int = -1
    internal set
  
  override fun read(attrLength: Int, file: IClassFile, input: DataInputStream) {
    this.signatureIndex = input.readUnsignedShort()
  }
}