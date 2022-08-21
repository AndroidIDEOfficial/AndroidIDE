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

package com.itsaky.androidide.lsp.xml.providers.completion.layout

import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Provides completions for attribute value in layout XML files.
 *
 * @author Akash Yadav
 */
class LayoutAttributeValueCompletionProvider : LayoutCompletionProvider() {

  private val log = ILogger.newInstance("AttributeValueCompletions")

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val attr = document.findAttrAt(params.position.requireIndex())
    val attrName =
      attr?.localName
        ?: run {
          log.warn("Cannot find attribute at index ${params.position.index}")
          return EMPTY
        }

    val attrs =
      Lookup.DEFAULT.lookup(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES_LOOKUP_KEY)
        ?.findPackage("android")
        ?.findGroup(ATTR)
        ?: run {
          log.warn("Cannot get attributes list from resource table")
          return EMPTY
        }

    val entry =
      attrs.findEntry(attrName)?.findValue(ConfigDescription())?.value
        ?: run {
          log.warn("Cannot find entry for attribute: $attrName")
          return EMPTY
        }

    if (entry !is AttributeResource) {
      return EMPTY
    }

    val list = mutableListOf<CompletionItem>()
    for (symbol in entry.symbols) {
      val matchLevel = matchLevel(symbol.symbol.name.entry!!, prefix)
      if (matchLevel == NO_MATCH && prefix.isNotEmpty()) {
        continue
      }

      list.add(createAttrValueCompletionItem(attrName, symbol.symbol.name.entry!!, matchLevel))
    }

    return CompletionResult(list)
  }
}
