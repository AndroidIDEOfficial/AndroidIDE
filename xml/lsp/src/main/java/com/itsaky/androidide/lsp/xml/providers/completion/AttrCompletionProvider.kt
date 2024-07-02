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

import com.android.aaptcompiler.AaptResourceType.STYLEABLE
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.Styleable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.androidide.xml.res.IResourceGroup
import com.itsaky.androidide.xml.res.IResourceTablePackage
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMNode

/**
 * Provides attribute completions in layout files.
 *
 * @author Akash Yadav
 */
open class AttrCompletionProvider(provider: ICompletionProvider) :
  IXmlCompletionProvider(provider) {

  private var attrHasNamespace = false

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && type == ATTRIBUTE
  }

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val list = mutableListOf<CompletionItem>()

    val newPrefix =
      if (attrAtCursor.name.contains(':')) {
        attrAtCursor.name.substringAfterLast(':')
      } else attrAtCursor.name

    attrHasNamespace = newPrefix != attrAtCursor.name

    val namespace =
      attrAtCursor.namespaceURI
        ?: run {
          return completeFromAllNamespaces(nodeAtCursor, list, newPrefix)
        }

    val nsPrefix = attrAtCursor.nodeName.substringBefore(':')
    completeForNamespace(namespace, nsPrefix, nodeAtCursor, newPrefix, list)

    return CompletionResult(list)
  }

  private fun completeFromAllNamespaces(
    node: DOMNode,
    list: MutableList<CompletionItem>,
    newPrefix: String
  ): CompletionResult {
    val namespaces = findAllNamespaces(node)
    namespaces.forEach { completeForNamespace(it.second, it.first, node, newPrefix, list) }

    return CompletionResult(list)
  }

  protected open fun completeForNamespace(
    namespace: String?,
    nsPrefix: String,
    node: DOMNode,
    newPrefix: String,
    list: MutableList<CompletionItem>
  ) {
    if (namespace == null) {
      log.warn("Namespace is null. Cannot compute completions for namespace prefix: {}.", nsPrefix)
      return
    }
    val tables = findResourceTables(namespace)
    if (tables.isEmpty()) {
      log.warn("No resource tables found for namespace: {}", namespace)
      return
    }

    val pck = namespace.substringAfter(NAMESPACE_PREFIX)
    val packages = mutableSetOf<IResourceTablePackage>()
    for (table in tables) {
      if (namespace == NAMESPACE_AUTO) {
        packages.addAll(table.packages.filter { it.name.isNotBlank() })
      } else {
        val tablePackage = table.findPackage(pck)
        tablePackage?.also { packages.add(it) }
      }
    }

    for (tablePackage in packages) {
      addFromPackage(tablePackage, node, tablePackage.name, nsPrefix, newPrefix, list)
    }
  }

  protected open fun addFromPackage(
    tablePackage: IResourceTablePackage?,
    node: DOMNode,
    pck: String,
    nsPrefix: String,
    newPrefix: String,
    list: MutableList<CompletionItem>
  ) {
    val styleables = tablePackage?.findGroup(STYLEABLE) ?: return
    val nodeStyleables = findNodeStyleables(node, styleables)
    if (nodeStyleables.isEmpty()) {
      return
    }

    addFromStyleables(
      styleables = nodeStyleables,
      pck = pck,
      pckPrefix = nsPrefix,
      prefix = newPrefix,
      list = list
    )
  }

  protected open fun addFromStyleables(
    styleables: Set<Styleable>,
    pck: String,
    pckPrefix: String,
    prefix: String,
    list: MutableList<CompletionItem>
  ) {
    for (nodeStyleable in styleables) {
      for (ref in nodeStyleable.entries) {
        val matchLevel = matchLevel(ref.name.entry!!, prefix)
        if (matchLevel == NO_MATCH || hasAttr(pckPrefix, ref)) {
          continue
        }
        list.add(
          createAttrCompletionItem(
            attr = ref,
            resPkg = pck,
            nsPrefix = pckPrefix,
            hasNamespace = attrHasNamespace,
            matchLevel = matchLevel,
            partial = prefix
          )
        )
      }
    }
  }

  protected open fun hasAttr(prefix: String, ref: com.android.aaptcompiler.Reference): Boolean {
    return this.nodeAtCursor.hasAttribute("${prefix}:${ref.name.entry}")
  }

  protected open fun findNodeStyleables(node: DOMNode, styleables: IResourceGroup): Set<Styleable> {
    val nodeName = node.nodeName
    val widgets = Lookup.getDefault().lookup(WidgetTable.COMPLETION_LOOKUP_KEY) ?: return emptySet()

    // Find the widget
    val widget =
      if (nodeName.contains(".")) {
        widgets.getWidget(nodeName)
      } else {
        widgets.findWidgetWithSimpleName(nodeName)
      }

    if (widget != null) {
      // This is a widget from the Android SDK
      // we can get its superclasses and other stuff
      return findStyleablesForWidget(styleables, widgets, widget, node)
    } else if (nodeName.contains('.')) {
      // Probably a custom view or a view from libraries
      // If the developer follows the naming convention then only the completions will be provided
      // This must be called if and only if the tag name is qualified
      return findStyleablesForName(styleables, node, true)
    }

    log.info("Cannot find styleable entries for tag: null")
    return emptySet()
  }

  protected open fun findStyleablesForName(
    styleables: IResourceGroup,
    node: DOMNode,
    addFromParent: Boolean = false,
    suffix: String = ""
  ): Set<Styleable> {
    val result = mutableSetOf<Styleable>()

    // Styles must be defined by the View class' simple name
    var name = node.nodeName
    if (name.contains('.')) {
      name = name.substringAfterLast('.')
    }

    // Common attributes for all views
    addWidgetStyleable(styleables = styleables, widget = "View", result = result)

    // Find the declared styleable
    val entry = findStyleableEntry(styleables, "$name$suffix")
    if (entry != null) {
      result.add(entry)
    }

    // If the layout params from the parent must be added, check for parent and then add them
    // Layout param attributes must be added only from the direct parent
    if (addFromParent) {
      node.parentNode?.also { result.addAll(findLayoutParams(styleables, node.parentNode)) }
    }

    return result
  }

  protected open fun findLayoutParams(
    styleables: IResourceGroup,
    parentNode: DOMNode
  ): Set<Styleable> {
    val result = mutableSetOf<Styleable>()

    // Add layout params common for all view groups and the ones supporting child margins
    addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_Layout")
    addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_MarginLayout")

    var name = parentNode.nodeName
    if (name.contains('.')) {
      name = name.substringAfterLast('.')
    }

    addWidgetStyleable(styleables, name, result, "_Layout")

    return result
  }

  protected open fun findStyleablesForWidget(
    styleables: IResourceGroup,
    widgets: WidgetTable,
    widget: Widget,
    node: DOMNode,
    adddFromParent: Boolean = true,
    suffix: String = ""
  ): Set<Styleable> {
    val result = mutableSetOf<Styleable>()

    // Find the <declare-styleable> for the widget in the resource group
    addWidgetStyleable(styleables, widget, result, suffix = suffix)

    // Find styleables for all the superclasses
    addSuperclassStyleables(styleables, widgets, widget, result, suffix = suffix)

    // Add attributes provided by the layout params
    if (adddFromParent && node.parentNode != null) {
      val parentName = node.parentNode.nodeName
      val parentWidget =
        if (parentName.contains(".")) {
          widgets.getWidget(parentName)
        } else {
          widgets.findWidgetWithSimpleName(parentName)
        }

      if (parentWidget != null) {
        result.addAll(
          findStyleablesForWidget(
            styleables,
            widgets,
            parentWidget,
            node.parentNode,
            false,
            "_Layout"
          )
        )
      } else {
        result.addAll(findLayoutParams(styleables, node.parentNode))
      }
    }

    return result
  }

  protected open fun addWidgetStyleable(
    styleables: IResourceGroup,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
  ) {
    addWidgetStyleable(styleables, widget.simpleName, result, suffix)
  }

  protected open fun addWidgetStyleable(
    styleables: IResourceGroup,
    widget: String,
    result: MutableSet<Styleable>,
    suffix: String = ""
  ) {
    val entry = findStyleableEntry(styleables, "${widget}${suffix}")
    if (entry != null) {
      result.add(entry)
    }
  }

  protected open fun addSuperclassStyleables(
    styleables: IResourceGroup,
    widgets: WidgetTable,
    widget: Widget,
    result: MutableSet<Styleable>,
    suffix: String = ""
  ) {
    for (superclass in widget.superclasses) {
      // When a ViewGroup is encountered in the superclasses, add the margin layout params
      if ("android.view.ViewGroup" == superclass) {
        addWidgetStyleable(styleables, "ViewGroup", result, suffix = "_MarginLayout")
      }

      val superr = widgets.getWidget(superclass) ?: continue
      addWidgetStyleable(styleables, superr.simpleName, result, suffix = suffix)
    }
  }

  protected open fun findStyleableEntry(styleables: IResourceGroup, name: String): Styleable? {
    val value = styleables.findEntry(name)?.findValue(ConfigDescription())?.value
    if (value !is Styleable) {
      log.warn("Cannot find styleable for {}", name)
      return null
    }
    return value
  }
}
