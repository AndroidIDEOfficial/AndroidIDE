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

package com.itsaky.androidide.lsp.xml.providers.completion

import com.android.aaptcompiler.ResourceTable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import org.eclipse.lemminx.dom.DOMNode
import kotlin.math.min

fun match(simpleName: String, qualifiedName: String, prefix: String): MatchLevel {
  val simpleNameMatchLevel = matchLevel(simpleName, prefix)
  val nameMatchLevel = matchLevel(qualifiedName, prefix)
  if (simpleNameMatchLevel == NO_MATCH && nameMatchLevel == NO_MATCH) {
    return NO_MATCH
  }

  return MatchLevel.values()[min(simpleNameMatchLevel.ordinal, nameMatchLevel.ordinal)]
}

fun platformResourceTable(): ResourceTable? {
  return Lookup.DEFAULT.lookup(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES)
}

fun findAllNamespaces(node: DOMNode): MutableSet<Pair<String, String>> {
  val namespaces = mutableSetOf<Pair<String, String>>()
  var curr: DOMNode? = node
  
  @Suppress("SENSELESS_COMPARISON") // attributes might be null. ignore warning
  while (curr != null && curr.attributes != null) {
    for (i in 0 until curr.attributes.length) {
      val currAttr = curr.getAttributeAtIndex(i)
      if (currAttr.isXmlns) {
        namespaces.add(currAttr.localName to currAttr.value)
      }
    }
    curr = curr.parentNode
  }
  return namespaces
}