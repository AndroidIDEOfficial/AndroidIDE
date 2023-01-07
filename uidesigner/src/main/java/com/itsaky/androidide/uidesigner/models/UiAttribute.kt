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

package com.itsaky.androidide.uidesigner.models

import android.os.Parcelable
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.inflater.internal.NamespaceImpl
import com.itsaky.androidide.inflater.viewAdapter
import kotlinx.parcelize.Parcelize

/**
 * UI Designer specific implementation of [IAttribute].
 *
 * @author Akash Yadav
 */
@Parcelize
open class UiAttribute
@JvmOverloads
constructor(
  override val namespace: NamespaceImpl = INamespace.ANDROID as NamespaceImpl,
  override val name: String,
  override var value: String,
  internal var isRequired: Boolean = false
) : AttributeImpl(namespace, name, value), Parcelable {

  constructor(
    src: IAttribute
  ) : this(namespace = src.namespace as NamespaceImpl, name = src.name, value = src.value)

  companion object {
    @JvmStatic
    fun isRequired(view: IView, attribute: IAttribute): Boolean {
      val adapter = view.viewAdapter ?: return false
      return adapter.isRequiredAttribute(attribute)
    }
  }
}
