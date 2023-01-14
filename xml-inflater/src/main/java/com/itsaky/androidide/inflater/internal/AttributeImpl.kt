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

package com.itsaky.androidide.inflater.internal

import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.utils.newAttribute

open class AttributeImpl
@JvmOverloads
constructor(
  override val namespace: INamespace? = null,
  override val name: String,
  override var value: String
) : IAttribute {

  @JvmOverloads
  fun copyAttr(
    view: IView? = null,
    namespace: INamespace? = this.namespace,
    name: String = this.name,
    value: String = this.value
  ): IAttribute {
    return newAttribute(view = view, namespace = namespace, name = name, value = value)
  }

  fun immutable(): IAttribute {
    return ImmutableAttributeImpl(this)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is AttributeImpl) return false

    if (namespace != other.namespace) return false
    if (name != other.name) return false
    if (value != other.value) return false

    return true
  }

  override fun hashCode(): Int {
    var result = namespace?.hashCode() ?: 0
    result = 31 * result + name.hashCode()
    result = 31 * result + value.hashCode()
    return result
  }

  override fun toString(): String {
    return "AttributeImpl(namespace=$namespace, name='$name', value='$value')"
  }
}
