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

package com.itsaky.androidide.uidesigner.utils

import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.lsp.xml.utils.XMLBuilder

/**
 * Generates XML code for [IView].
 *
 * @author Akash Yadav
 */
object ViewToXml {

  @JvmStatic
  fun generateXml(view: ViewImpl): String {
    val builder = XMLBuilder("", System.lineSeparator())
    builder.apply {
      appendXmlProlog()
      linefeed()
      appendView(view)
    }
    return builder.toString()
  }

  private fun XMLBuilder.appendView(view: ViewImpl, indent: Int = 1) {
    startElement(view.tag, false)

    for (namespace in view.namespaceDecls) {
      linefeed()
      indent(indent)
      addSingleAttribute("xmlns:${namespace.prefix}", namespace.uri, true)
    }

    for (attr in view.attributes) {
      linefeed()
      indent(indent)
      addSingleAttribute("${attr.namespace.prefix}:${attr.name}", attr.value, true)
    }

    if (view is IViewGroup) {
      
      closeStartElement()
      
      for (i in 0 until view.childCount) {
        linefeed()
        linefeed()
        indent(indent)
        appendView(view[i] as ViewImpl, indent + 1)
      }

      linefeed()
      linefeed()

      endElement(view.tag)
    } else {
      selfCloseElement()
    }
  }

  private fun XMLBuilder.appendXmlProlog() {
    startPrologOrPI("xml")
    addSingleAttribute("version", "1.0", true)
    addSingleAttribute("encoding", "utf-8", true)
    endPrologOrPI()
  }
}
