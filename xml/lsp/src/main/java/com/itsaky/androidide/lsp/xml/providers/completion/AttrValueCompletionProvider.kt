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

import com.android.SdkConstants.ANDROID_URI
import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aapt.Resources.Attribute.FormatFlags.BOOLEAN
import com.android.aapt.Resources.Attribute.FormatFlags.COLOR
import com.android.aapt.Resources.Attribute.FormatFlags.DIMENSION
import com.android.aapt.Resources.Attribute.FormatFlags.ENUM
import com.android.aapt.Resources.Attribute.FormatFlags.FLAGS
import com.android.aapt.Resources.Attribute.FormatFlags.INTEGER
import com.android.aapt.Resources.Attribute.FormatFlags.REFERENCE
import com.android.aapt.Resources.Attribute.FormatFlags.STRING
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.BOOL
import com.android.aaptcompiler.AaptResourceType.DIMEN
import com.android.aaptcompiler.AaptResourceType.UNKNOWN
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.MAX_ITEMS
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.edits.QualifiedValueEditHandler
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.lsp.xml.utils.dimensionUnits
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.res.IResourceTablePackage
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.utils.attrValue_qualifiedRef
import com.itsaky.androidide.xml.utils.attrValue_qualifiedRefWithIncompletePckOrType
import com.itsaky.androidide.xml.utils.attrValue_qualifiedRefWithIncompleteType
import com.itsaky.androidide.xml.utils.attrValue_unqualifiedRef
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Provides completions for attribute value in layout XML files.
 *
 * @author Akash Yadav
 */
open class AttrValueCompletionProvider(provider: ICompletionProvider) :
  IXmlCompletionProvider(provider) {

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
    val attrName =
      attrAtCursor.localName
        ?: run {
          log.warn("Cannot find attribute at index {}", params.position.index)
          return EMPTY
        }

    // TODO Currently we do not support completing values for attributes without a namespace
    //  For example, completions will be provided for: 'android:textColor="@@cursor@@"' but
    //  not for 'textColor="@@cursor"'

    val namespace =
      attrAtCursor.namespaceURI
        ?: run {
          log.warn("Unknown namespace for attribute: {}", attrAtCursor)
          return EMPTY
        }

    return completeValue(namespace = namespace, prefix = prefix, attrName = attrName)
  }

  fun setNamespaces(namespaces: Set<Pair<String, String>>) {
    this.allNamespaces = namespaces
  }

  fun completeValue(
    namespace: String?,
    prefix: String,
    attrName: String,
    attrValue: String? = null
  ): CompletionResult {

    if (namespace.isNullOrBlank()) {
      return EMPTY
    }

    val tables = findResourceTables(namespace)
    if (tables.isEmpty()) {
      return EMPTY
    }

    val pck = namespace.substringAfter(NAMESPACE_PREFIX)
    val list = mutableListOf<CompletionItem>()

    val attr =
      findAttr(tables, namespace, pck, attrName)
        ?: run {
          log.warn(
            "No attribute found with name '{}' in package '{}'", attrName,
            if (namespace == NAMESPACE_AUTO) "<auto>" else pck
          )
          return EMPTY
        }

    val value = attrValue ?: this.attrAtCursor.value

    // If user is directly typing the entry name. For example 'app_name'
    if (!value.startsWith('@')) {
      addValuesForAttr(attr, pck, prefix, list)
      return CompletionResult(list)
    }

    // If user is typign entry with package name and resource type. For example
    // '@com.itsaky.test.app:string/app_name' or '@android:string/ok'
    var matcher = attrValue_qualifiedRef.matcher(value)
    if (matcher.matches()) {
      val valPck = matcher.group(1)
      val typeStr = matcher.group(3)
      val valType =
        com.android.aaptcompiler.AaptResourceType.values().firstOrNull { it.tagName == typeStr }
          ?: return EMPTY
      val newPrefix = matcher.group(4) ?: ""
      addValues(valType, newPrefix, list) { it == valPck }
      return CompletionResult(list)
    }

    // If user is typing qualified reference but with incomplete type
    // For example: '@android:str' or '@com.itsaky.test.app:str'
    matcher = attrValue_qualifiedRefWithIncompleteType.matcher(value)
    if (matcher.matches()) {
      val valPck = matcher.group(1)!!
      val incompleteType = matcher.group(3) ?: ""
      addResourceTypes(valPck, incompleteType, list)
      return CompletionResult(list)
    }

    // If user is typing qualified reference but with incomplete type or package name
    // For example: '@android:str' or '@str'
    matcher = attrValue_qualifiedRefWithIncompletePckOrType.matcher(value)
    if (matcher.matches()) {
      val valPck = matcher.group(1)!!

      if (!valPck.contains('.')) {
        addResourceTypes("", valPck, list)
      }

      addPackages(valPck, list)

      return CompletionResult(list)
    }

    // If user is typing entry name with resource type. For example '@string/app_name'
    matcher = attrValue_unqualifiedRef.matcher(value)
    if (matcher.matches()) {
      val typeStr = matcher.group(1)
      val newPrefix = matcher.group(2) ?: ""
      val valType =
        com.android.aaptcompiler.AaptResourceType.values().firstOrNull { it.tagName == typeStr }
          ?: return EMPTY
      addValues(valType, newPrefix, list)
      return CompletionResult(list)
    }

    return EMPTY
  }

  private fun addPackages(incompletePck: String, list: MutableList<CompletionItem>) {
    val packages =
      findResourceTables(ANDROID_URI).flatMap {
        it.packages.filter { pck -> matchLevel(pck.name, incompletePck) != NO_MATCH }
      }
    packages.forEach {
      val match = matchLevel(it.name, incompletePck)
      val item = createEnumOrFlagCompletionItem(it.name, it.name, match)
      item.editHandler = QualifiedValueEditHandler()
      list.add(item)
    }
  }

  private fun addResourceTypes(
    pck: String,
    incompleteType: String,
    list: MutableList<CompletionItem>
  ) {
    listResTypes().forEach {
      val match = matchLevel(it, incompleteType)
      if (match == NO_MATCH && incompleteType.isNotBlank()) {
        return@forEach
      }

      val item = createEnumOrFlagCompletionItem(pck, it, match)
      item.overrideTypeText = "Resource type"
      list.add(item)
    }
  }

  private fun listResTypes(): List<String> =
    com.android.aaptcompiler.AaptResourceType.values().map { it.tagName }

  protected open fun resTableForFindAttr() = platformResourceTable()

  private fun findAttr(
    tables: Set<IResourceTable>,
    namespace: String,
    pck: String,
    attr: String
  ): AttributeResource? {
    if (namespace != NAMESPACE_AUTO && pck == ResourceTableRegistry.PCK_ANDROID) {
      // AndroidX dependencies include attribute declarations with the 'android' package
      // Those must not be included when completing values
      val attrEntry =
        resTableForFindAttr()!!
          .findPackage(ResourceTableRegistry.PCK_ANDROID)
          ?.findGroup(ATTR)
          ?.findEntry(attr)
          ?.findValue(ConfigDescription())
          ?.value
      return if (attrEntry is AttributeResource) attrEntry else null
    }

    return if (namespace == NAMESPACE_AUTO) {
      findAttr(tables.flatMap { it.packages }, attr)
    } else {
      findAttr(tables.mapNotNull { it.findPackage(pck) }, attr)
    }
  }

  private fun findAttr(
    packages: Collection<IResourceTablePackage>,
    attr: String
  ): AttributeResource? {
    for (pck in packages) {
      val entry =
        pck.findGroup(ATTR)?.findEntry(attr)?.findValue(ConfigDescription())?.value ?: continue
      if (entry is AttributeResource) {
        return entry
      }
    }
    return null
  }

  private fun addValuesForAttr(
    attr: AttributeResource,
    pck: String,
    prefix: String,
    list: MutableList<CompletionItem>
  ) {
    if (attr.typeMask == FormatFlags.REFERENCE_VALUE) {
      completeReferences(prefix, list)
    } else {
      // Check for specific attribute formats
      if (attr.hasType(STRING)) {
        addValues(
          type = com.android.aaptcompiler.AaptResourceType.STRING,
          prefix = prefix,
          result = list
        )
      }

      if (attr.hasType(INTEGER)) {
        addValues(
          type = com.android.aaptcompiler.AaptResourceType.INTEGER,
          prefix = prefix,
          result = list
        )
      }

      if (attr.hasType(COLOR)) {
        addValues(
          type = com.android.aaptcompiler.AaptResourceType.COLOR,
          prefix = prefix,
          result = list
        )
      }

      if (attr.hasType(BOOLEAN)) {
        addValues(type = BOOL, prefix = prefix, result = list)
      }

      if (attr.hasType(DIMENSION)) {
        if (prefix.isNotBlank() && prefix[0].isDigit()) {
          addConstantDimensionValues(prefix, list)
        } else addValues(type = DIMEN, prefix = prefix, result = list)
      }

      if (attr.hasType(INTEGER)) {
        addValues(
          type = com.android.aaptcompiler.AaptResourceType.INTEGER,
          prefix = prefix,
          result = list
        )
      }

      if (attr.hasType(ENUM) || attr.hasType(FLAGS)) {
        for (symbol in attr.symbols) {
          val matchLevel = matchLevel(symbol.symbol.name.entry!!, prefix)
          if (matchLevel == NO_MATCH && prefix.isNotEmpty()) {
            continue
          }

          list.add(
            createEnumOrFlagCompletionItem(pck = pck, name = symbol.symbol.name.entry!!, matchLevel)
          )
        }
      }

      if (attr.hasType(REFERENCE)) {
        completeReferences(prefix, list)
      }
    }
  }

  private fun addConstantDimensionValues(prefix: String, list: MutableList<CompletionItem>) {
    var i = 0
    while (i < prefix.length && prefix[i].isDigit()) {
      ++i
    }
    val dimen = prefix.substring(0, i)
    for (unit in dimensionUnits) {
      val value = "${dimen}${unit}"
      val matchLevel = matchLevel(value, prefix)
      if (matchLevel == NO_MATCH) {
        continue
      }
      list.add(createEnumOrFlagCompletionItem(name = value, matchLevel = matchLevel))
    }
  }

  private fun completeReferences(prefix: String, list: MutableList<CompletionItem>) {
    for (value in com.android.aaptcompiler.AaptResourceType.values()) {
      if (value == UNKNOWN) {
        continue
      }

      addValues(value, prefix, list)
    }
  }

  private fun addValues(
    type: com.android.aaptcompiler.AaptResourceType,
    prefix: String,
    result: MutableList<CompletionItem>,
    checkPck: (String) -> Boolean = { true }
  ) {
    if (result.size >= MAX_ITEMS + 1) {
      return
    }

    val entries =
      allNamespaces
        .flatMap { findResourceTables(it.second) }
        .flatMap { table ->
          table.packages.mapNotNull { pck ->
            if (!checkPck(pck.name)) {
              return@mapNotNull null
            }
            pck.name to
              pck.findGroup(type)?.findEntries { entryName ->
                matchLevel(entryName, prefix) != NO_MATCH
              }
          }
        }
        .toHashSet()

    entries.forEach { pair ->
      pair.second?.forEach { entry ->
        result.add(
          createAttrValueCompletionItem(
            pair.first,
            type.tagName,
            entry.name,
            matchLevel(entry.name, prefix)
          )
        )
      }
    }
  }

  override fun findResourceTables(nsUri: String?): Set<IResourceTable> {
    // When completing values, all namespaces must be included
    val tables = HashSet(findAllModuleResourceTables())

    if (nsUri.isNullOrBlank()) {
      return tables
    }

    tables.addAll(super.findResourceTables(nsUri))
    log.info("Found {} resource tables for namespace: {}", tables.size, nsUri)
    return tables
  }

  private fun AttributeResource.hasType(check: FormatFlags): Boolean {
    return hasType(check.number)
  }

  private fun AttributeResource.hasType(check: Int): Boolean {
    return this.typeMask and check != 0
  }
}
