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

import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.xml.res.IResourceTable
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

fun platformResourceTable(): IResourceTable? {
  return Lookup.getDefault().lookup(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES)
}

fun findAllNamespaces(node: DOMNode): MutableSet<Pair<String, String>> {
  val namespaces = mutableSetOf<Pair<String, String>>()
  var curr: DOMNode? = node

  while (curr != null && !curr.isOwnerDocument) {

    if (curr.attributes == null) {
      curr = curr.parentNode
      continue
    }

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

/**
 * Transforms entry name to tag name.
 *
 * For example: `AndroidManifestUsesPermission` -> `uses-permission`
 */
fun transformToTagName(entryName: String, prefix: String = ""): String {
  val name = StringBuilder()
  var index = prefix.length
  while (index < entryName.length) {
    var c = entryName[index]
    if (c.isUpperCase()) {
      if (index != prefix.length) {
        name.append('-')
      }
      c = c.lowercaseChar()
    }

    name.append(c)
    ++index
  }
  return name.toString()
}

/**
 * Transforms tag name to entry name.
 *
 * For example: `uses-permission` -> `AndroidManifestUsesPermission`
 */
fun transformToEntryName(tagName: String, prefix: String = ""): String {
  if (tagName == "manifest") {
    return MANIFEST_TAG_PREFIX
  }

  val name = StringBuilder(prefix)

  var index = 0
  var capitalize = false
  while (index < tagName.length) {
    var c = tagName[index]
    if (c == '-') {
      capitalize = true
      ++index
      continue
    }
    if (index == 0 || capitalize) {
      c = c.uppercaseChar()
      capitalize = false
    }
    name.append(c)
    ++index
  }

  return name.toString()
}
