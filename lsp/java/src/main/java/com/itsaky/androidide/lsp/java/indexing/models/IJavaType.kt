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

package com.itsaky.androidide.lsp.java.indexing.models

import io.realm.RealmList
import io.realm.annotations.Required

/**
 * A Java user type. May be a class, interface, enum, or annotation. Qualified name of a type or a
 * package name is always represented in the (partially) internal form. For example, the fully qualified
 * of `java.lang.Object` is `java/lang/Object` and the package name is `java/lang`.
 *
 * @property fqn The fully qualified name of the type.
 * @property name The simple name of the type.
 * @property packageName The package name of the type.
 * @property superInterfacesFqn The fully qualified names of the interfaces implemented by the type.
 * @property fields The fields of the type.
 * @property methods The methods of the type.
 * @property isClass Whether the type is a class.
 * @property isInterface Whether the type is an interface.
 * @property isAnnotation Whether the type is an annotation.
 * @property isEnum Whether the type is an enum.
 * @author Akash Yadav
 */
interface IJavaType<FieldT : IJavaSymbol, MethodT : IJavaSymbol> : IJavaSymbol {
  var fqn: String?
  var name: String?
  var packageName: String?

  /**
   * The fully qualified name of the superclass of the type.
   *
   * Info about fields and methods are availabe in the class file itself. Hhowever, a class may
   * extend a class (or implement an interface) which may be defined in other jar files. As a result,
   * we only store the fully qualifed names of the classes and interfaces then add an index based on
   * these fully qualifed names in the database.
   *
   * This property is not [Required] because we also index `java/lang/Object`, which does not have
   * a superclass.
   */
  var superClassFqn: String?
  var superInterfacesFqn: RealmList<String>?
  var fields: RealmList<FieldT>?
  var methods: RealmList<MethodT>?

  val isClass: Boolean
    get() = false

  val isInterface: Boolean
    get() = false

  val isAnnotation: Boolean
    get() = false

  val isEnum: Boolean
    get() = false
}