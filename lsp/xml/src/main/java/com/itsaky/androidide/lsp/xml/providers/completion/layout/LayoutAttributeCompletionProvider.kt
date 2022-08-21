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

import com.android.aaptcompiler.AaptResourceType.STYLEABLE
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.Styleable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.providers.completion.AttributeCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Provides attribute completions in layout files.
 *
 * @author Akash Yadav
 */
class LayoutAttributeCompletionProvider : AttributeCompletionProvider() {

  private val log = ILogger.newInstance(AttributeCompletionProvider::class.java.simpleName)

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val node = document.findNodeAt(params.position.requireIndex())
    val styleables =
      Lookup.DEFAULT.lookup(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES_LOOKUP_KEY)
        ?.findPackage("android")
        ?.findGroup(STYLEABLE)
        ?: run {
          log.debug("Cannot find styles in framework resources")
          return CompletionResult.EMPTY
        }

    val nodeStyleables = findNodeStyleables(node.nodeName, styleables)
    if (nodeStyleables.isEmpty()) {
      return CompletionResult.EMPTY
    }

    val list = mutableListOf<CompletionItem>()
    val attr = document.findAttrAt(params.position.requireIndex())
    for (nodeStyleable in nodeStyleables) {
      for (ref in nodeStyleable.entries) {
        val matchLevel = matchLevel(ref.name.entry!!, attr.name)
        if (matchLevel == NO_MATCH) {
          continue
        }
        list.add(createAttrCompletionItem(ref, matchLevel))
      }
    }

    return CompletionResult(list)
  }

  private fun findNodeStyleables(nodeName: String, styleables: ResourceGroup): Set<Styleable> {
    val widgets = Lookup.DEFAULT.lookup(WidgetTable.COMPLETION_LOOKUP_KEY) ?: return emptySet()
    val result = mutableSetOf<Styleable>()

    // Find the widget
    val widget =
      if (nodeName.contains(".")) {
        widgets.getWidget(nodeName)
      } else {
        widgets.findWidgetWithSimpleName(nodeName)
      }
        ?: return emptySet()

    // Find the <declare-styleable> for the widget in the resource group
    val entry = styleables.findEntry(widget.simpleName)?.findValue(ConfigDescription())?.value
    if (entry != null && entry is Styleable) {
      result.add(entry)
    }

    // Find styleables for all the superclasses
    for (superclass in widget.superclasses) {
      val superr = widgets.getWidget(superclass) ?: continue
      val superEntry =
        styleables.findEntry(superr.simpleName)?.findValue(ConfigDescription())?.value
      if (superEntry != null && superEntry is Styleable) {
        result.add(superEntry)
      }
    }

    return result
  }
}
