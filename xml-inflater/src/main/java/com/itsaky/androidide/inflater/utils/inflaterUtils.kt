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

package com.itsaky.androidide.inflater.utils

import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IComponentFactory
import com.itsaky.androidide.inflater.ILayoutInflater
import com.itsaky.androidide.inflater.ILayoutInflater.Companion
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.lookup.Lookup

/** Get the [ILayoutInflater] registered with [Lookup]. */
fun lookupLayoutInflater(): ILayoutInflater? {
  return Lookup.DEFAULT.lookup(Companion.LOOKUP_KEY)
}

/** Get the [IComponentFactory] registered with [Lookup]. */
fun lookupComponentFactory(): IComponentFactory? {
  return Lookup.DEFAULT.lookup(IComponentFactory.LAYOUT_INFLATER_COMPONENT_FACTORY_KEY)
}

@JvmOverloads
fun newAttribute(
  view: IView? = null,
  attribute: IAttribute,
  namespace: INamespace? = null,
  name: String? = null,
  value: String? = null
): IAttribute {
  return newAttribute(
    view = view,
    namespace = namespace ?: attribute.namespace,
    name = name ?: attribute.name,
    value = value ?: attribute.value
  )
}

@JvmOverloads
fun newAttribute(
  view: IView? = null,
  namespace: INamespace = INamespace.ANDROID,
  name: String,
  value: String
): IAttribute {
  val componentFactory = lookupComponentFactory()
  if (componentFactory != null && view != null) {
    return componentFactory.createAttr(view, namespace, name, value)
  }
  return AttributeImpl(namespace, name, value)
}
