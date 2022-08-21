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
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMNode

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

    val nodeStyleables = findNodeStyleables(node, styleables)
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

  private fun findNodeStyleables(node: DOMNode, styleables: ResourceGroup): Set<Styleable> {
    val nodeName = node.nodeName
    val widgets = Lookup.DEFAULT.lookup(WidgetTable.COMPLETION_LOOKUP_KEY) ?: return emptySet()
    val result = mutableSetOf<Styleable>()

    // Find the widget
    val widget =
      if (nodeName.contains(".")) {
        widgets.getWidget(nodeName)
      } else {
        widgets.findWidgetWithSimpleName(nodeName)
      }
        ?: widgets.getWidget("android.view.View")!!

    // Find the <declare-styleable> for the widget in the resource group
    addWidgetStyleable(styleables, widget, result)

    // Find styleables for all the superclasses
    addSuperclassStyleables(styleables, widgets, widget, result)

    // Add attributes provided by the layout params
    if (node.parentNode != null) {
      val parentName = node.parentNode.nodeName
      val parentWidget =
        if (parentName.contains(".")) {
          widgets.getWidget(parentName)
        } else {
          widgets.findWidgetWithSimpleName(parentName)
        }

      if (parentWidget != null) {
        addWidgetStyleable(styleables, parentWidget, result, "_Layout")
        addSuperclassStyleables(styleables, widgets, parentWidget, result, "_Layout")
      }
    }

    return result
  }

  private fun addWidgetStyleable(
    styleables: ResourceGroup,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
  ) {
    val entry = findStyleableEntry(styleables, "${widget.simpleName}$suffix")
    if (entry != null) {
      result.add(entry)
    }
  }

  private fun addSuperclassStyleables(
    styleables: ResourceGroup,
    widgets: WidgetTable,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
  ) {
    for (superclass in widget.superclasses) {

      // When a ViewGroup is encountered in the superclasses, add the margin layout params
      if ("android.view.ViewGroup" == superclass) {
        val marginEntry = findStyleableEntry(styleables, "ViewGroup_MarginLayout")
        if (marginEntry != null) {
          result.add(marginEntry)
        }
      }

      val superr = widgets.getWidget(superclass) ?: continue
      val superEntry = findStyleableEntry(styleables, "${superr.simpleName}$suffix")
      if (superEntry != null) {
        result.add(superEntry)
      }
    }
  }

  private fun findStyleableEntry(styleables: ResourceGroup, simpleName: String): Styleable? {
    val value = styleables.findEntry(simpleName)?.findValue(ConfigDescription())?.value
    if (value !is Styleable) {
      return null
    }

    return value
  }
}
