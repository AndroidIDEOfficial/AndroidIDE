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

package com.itsaky.androidide.inflater

/**
 * Model for an XML attribute.
 *
 * @author Akash Yadav
 */
interface IAttribute {

  /** The namespace of the attribute. */
  val namespace: INamespace?
  
  /** The name of the attribute. */
  val name: String

  /** The value of the attribute. */
  var value: String
  
  /**
   * The name of this attribute the form `<namespace>:<name>`.
   */
  val qualifiedName : String
    get() {
      val ns = namespace?.prefix?.let { "${it}:" } ?: ""
      return "${ns}${name}"
    }
}
