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

package com.itsaky.androidide.lsp.xml.providers.completion.common

import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aapt.Resources.Attribute.FormatFlags.BOOLEAN
import com.android.aapt.Resources.Attribute.FormatFlags.COLOR
import com.android.aapt.Resources.Attribute.FormatFlags.DIMENSION
import com.android.aapt.Resources.Attribute.FormatFlags.ENUM
import com.android.aapt.Resources.Attribute.FormatFlags.FLAGS
import com.android.aapt.Resources.Attribute.FormatFlags.INTEGER
import com.android.aapt.Resources.Attribute.FormatFlags.STRING
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AaptResourceType.UNKNOWN
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.ResourceTable
import com.itsaky.androidide.aapt.findEntries
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.providers.completion.IXmlCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.resources.ResourceTableRegistry.Companion.COMPLETION_FRAMEWORK_RES_LOOKUP_KEY
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Provides completions for attribute value in layout XML files.
 *
 * @author Akash Yadav
 */
class AttrValueCompletionProvider : IXmlCompletionProvider() {

  private val log = ILogger.newInstance("AttributeValueCompletions")

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && type == ATTRIBUTE_VALUE
  }

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

    val namespace =
      attr.namespaceURI
        ?: run {
          log.warn("Unknown namespace for attribute", attr)
          return EMPTY
        }

    val tables = findResourceTables(namespace)
    if (tables.isEmpty()) {
      return EMPTY
    }

    val list = mutableListOf<CompletionItem>()
    val pck = namespace.substringAfter(NAMESPACE_PREFIX)

    for (table in tables) {
      val attrs = table.findPackage(pck)?.findGroup(ATTR) ?: continue
      val entry = attrs.findEntry(attrName)?.findValue(ConfigDescription())?.value ?: continue
      if (entry !is AttributeResource) {
        continue
      }
      
      addFromTable(table, entry, pck, prefix, list)
    }

    return CompletionResult(list)
  }

  private fun addFromTable(
    table: ResourceTable,
    entry: AttributeResource,
    pck: String,
    prefix: String,
    list: MutableList<CompletionItem>
  ) {
    if (entry.typeMask == FormatFlags.REFERENCE_VALUE) {
      for (value in AaptResourceType.values()) {
        if (value == UNKNOWN) {
          continue
        }

        addValues(table, pck, value, prefix, list)
      }
    } else {
      // Check for specific attribute formats
      if (entry.hasType(STRING)) {
        addValues(table, pck, type = AaptResourceType.STRING, prefix = prefix, result = list)
      }

      if (entry.hasType(INTEGER)) {
        addValues(table, pck, type = AaptResourceType.INTEGER, prefix = prefix, result = list)
      }

      if (entry.hasType(COLOR)) {
        addValues(table, pck, type = AaptResourceType.COLOR, prefix = prefix, result = list)
      }

      if (entry.hasType(BOOLEAN)) {
        addValues(table, pck, type = BOOL, prefix = prefix, result = list)
      }

      if (entry.hasType(DIMENSION)) {
        addValues(table, pck, type = DIMEN, prefix = prefix, result = list)
      }

      if (entry.hasType(INTEGER)) {
        addValues(table, pck, type = AaptResourceType.INTEGER, prefix = prefix, result = list)
      }

      if (entry.hasType(ENUM) || entry.hasType(FLAGS)) {
        for (symbol in entry.symbols) {
          val matchLevel = matchLevel(symbol.symbol.name.entry!!, prefix)
          if (matchLevel == NO_MATCH && prefix.isNotEmpty()) {
            continue
          }

          list.add(
            createEnumOrFlagCompletionItem(pck = pck, name = symbol.symbol.name.entry!!, matchLevel)
          )
        }
      }
    }
  }

  private fun findResourceTables(nsUri: String): Set<ResourceTable> {
    if (nsUri == NAMESPACE_AUTO) {
      return findAllModuleResourceTables()
    }

    val pck = nsUri.substringAfter(NAMESPACE_PREFIX)
    if (pck.isBlank()) {
      log.warn("Invalid namespace: $nsUri")
      return emptySet()
    }

    if (pck == ResourceTableRegistry.PCK_ANDROID) {
      val platformResTable =
        Lookup.DEFAULT.lookup(COMPLETION_FRAMEWORK_RES_LOOKUP_KEY)
          ?: run {
            log.debug("No platform resource table is set")
            return emptySet()
          }

      return setOf(platformResTable)
    }

    val table =
      ResourceTableRegistry.getInstance().forPackage(pck)
        ?: run {
          log.error("Cannot find resource table for package: $pck")
          return emptySet()
        }

    return setOf(table)
  }

  private fun findAllModuleResourceTables(): Set<ResourceTable> {
    // TODO find all resource tables from completing file's module
    return emptySet()
  }

  private fun addValues(
    resources: ResourceTable,
    pck: String,
    type: AaptResourceType,
    prefix: String,
    result: MutableList<CompletionItem>
  ) {
    val entries =
      resources.findPackage(pck)?.findGroup(type)?.findEntries {
        matchLevel(it, prefix) != NO_MATCH
      }
        ?: return
    entries.forEach {
      result.add(
        createAttrValueCompletionItem(pck, type.tagName, it.name, matchLevel(it.name, prefix))
      )
    }
  }

  private fun AttributeResource.hasType(check: FormatFlags): Boolean {
    return hasType(check.number)
  }

  private fun AttributeResource.hasType(check: Int): Boolean {
    return this.typeMask and check != 0
  }
}
