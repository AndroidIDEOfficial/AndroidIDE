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
 * Wherever a type is used in a declaration or expression, the type_path structure identifies which
 * part of the type is annotated. An annotation may appear on the type itself, but if the type is a
 * reference type, then there are additional locations where an annotation may appear.
 *
 * @author Akash Yadav
 */
class TypePath {

  var paths: Array<PathEntry> = emptyArray()
    internal set

  class PathEntry {
    var typePathKind: Int = -1
      internal set
    var typeArgumentIndex: Int = -1
      internal set

    internal fun read(input: DataInputStream) {
      this.typePathKind = input.readUnsignedByte()
      this.typeArgumentIndex = input.readUnsignedByte()
    }
  }

  internal fun read(input: DataInputStream) {
    this.paths =
      Array(input.readUnsignedByte()) {
        val entry = PathEntry()
        entry.read(input)
        entry
      }
  }
}
