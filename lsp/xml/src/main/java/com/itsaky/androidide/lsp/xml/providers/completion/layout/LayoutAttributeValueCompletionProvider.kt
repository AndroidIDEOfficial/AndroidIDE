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

import com.android.aapt.Resources.Attribute.FormatFlags
import com.android.aapt.Resources.Attribute.FormatFlags.BOOLEAN
import com.android.aapt.Resources.Attribute.FormatFlags.COLOR
import com.android.aapt.Resources.Attribute.FormatFlags.DIMENSION
import com.android.aapt.Resources.Attribute.FormatFlags.ENUM
import com.android.aapt.Resources.Attribute.FormatFlags.FLAGS
import com.android.aapt.Resources.Attribute.FormatFlags.INTEGER
import com.android.aapt.Resources.Attribute.FormatFlags.REFERENCE_VALUE
import com.android.aapt.Resources.Attribute.FormatFlags.STRING
import com.android.aaptcompiler.AaptResourceType
import com.android.aaptcompiler.AaptResourceType.ATTR
import com.android.aaptcompiler.AaptResourceType.UNKNOWN
import com.android.aaptcompiler.AttributeResource
import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.aapt.findEntries
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.CompletionResult.Companion.EMPTY
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.resources.ResourceTableRegistry.Companion.COMPLETION_FRAMEWORK_RES_LOOKUP_KEY
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
      Lookup.DEFAULT.lookup(COMPLETION_FRAMEWORK_RES_LOOKUP_KEY)
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

    if (entry.typeMask == REFERENCE_VALUE) {
      for (value in AaptResourceType.values()) {
        if (value == UNKNOWN) {
          continue
        }

        addValues("android", value, prefix, list)
      }
    } else {
      // Check for specific attribute formats
      if (entry.hasType(STRING)) {
        addValues("android", type = AaptResourceType.STRING, prefix = prefix, result = list)
      }

      if (entry.hasType(INTEGER)) {
        addValues("android", type = AaptResourceType.INTEGER, prefix = prefix, result = list)
      }
  
      if (entry.hasType(COLOR)) {
        addValues("android", type = AaptResourceType.COLOR, prefix = prefix, result = list)
      }

      if (entry.hasType(BOOLEAN)) {
        addValues("android", type = AaptResourceType.BOOL, prefix = prefix, result = list)
      }

      if (entry.hasType(DIMENSION)) {
        addValues("android", type = AaptResourceType.DIMEN, prefix = prefix, result = list)
      }

      if (entry.hasType(INTEGER)) {
        addValues("android", type = AaptResourceType.INTEGER, prefix = prefix, result = list)
      }

      if (entry.hasType(ENUM) || entry.hasType(FLAGS)) {
        for (symbol in entry.symbols) {
          val matchLevel = matchLevel(symbol.symbol.name.entry!!, prefix)
          if (matchLevel == NO_MATCH && prefix.isNotEmpty()) {
            continue
          }

          list.add(
            createEnumOrFlagCompletionItem(
              pck = "android",
              name = symbol.symbol.name.entry!!,
              matchLevel
            )
          )
        }
      }
    }

    return CompletionResult(list)
  }

  private fun addValues(
    pck: String,
    type: AaptResourceType,
    prefix: String,
    result: MutableList<CompletionItem>
  ) {
    val resources = Lookup.DEFAULT.lookup(COMPLETION_FRAMEWORK_RES_LOOKUP_KEY) ?: return
    val colors =
      resources.findPackage(pck)?.findGroup(type)?.findEntries {
        matchLevel(it, prefix) != NO_MATCH
      }
        ?: return
    colors.forEach {
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
