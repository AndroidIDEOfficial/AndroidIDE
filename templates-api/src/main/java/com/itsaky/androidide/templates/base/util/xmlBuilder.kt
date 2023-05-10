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

package com.itsaky.androidide.templates.base.util

import com.android.xml.XmlBuilder

/**
 * Creates a new element in the [XmlBuilder] with the given [name].
 * The [configure] function is used to configure everything inside the element and then
 * the element is automatically closed.
 */
fun XmlBuilder.createElement(name: String, configure: XmlBuilder.() -> Unit) {
  startTag(name)
  apply(configure)
  endTag(name)
}

/**
 * Get the generated XML string from the [XmlBuilder] with XML declaration prepended.
 */
fun XmlBuilder.withXmlDecl() : String {
  return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n${toString()}"
}