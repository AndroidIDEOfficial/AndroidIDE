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

import com.android.aaptcompiler.Reference
import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.CompletionData
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItemKind.CLASS
import com.itsaky.androidide.lsp.models.CompletionItemKind.FIELD
import com.itsaky.androidide.lsp.models.CompletionItemKind.VALUE
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.utils.DocumentUtils
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Base class for all XML completion providers.
 *
 * @author Akash Yadav
 */
abstract class IXmlCompletionProvider {

  companion object {
    const val NAMESPACE_PREFIX = "http://schemas.android.com/apk/res"
  }
  
  /**
   * Whether this completion provide can provide completions for the given [pathData].
   *
   * @param pathData The data about the file that is being completed.
   * @param type The type of node at the cursor.
   */
  protected open fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return DocumentUtils.isXmlFile(pathData.file.toPath())
  }

  /**
   * Provide completions.
   *
   * @param params The completion params.
   * @param pathData The [ResourcePathData] for the file being completed.
   * @param document The DOM for the document.
   * @param prefix The incomplete partial identifier.
   * @return The completion result.
   */
  fun complete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    if (!canProvideCompletions(pathData, type)) {
      return CompletionResult.EMPTY
    }

    return doComplete(params, pathData, document, type, prefix)
  }

  /**
   * Provide completions.
   *
   * @param params The completion params.
   * @param pathData The [ResourcePathData] for the file being completed.
   * @param document The DOM for the document.
   * @param prefix The incomplete partial identifier.
   * @return The completion result.
   */
  protected abstract fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult

  /**
   * Create a completion item for XML tag.
   *
   * @param simpleName The simple name for the tag.
   * @param qualifiedName The fully qualified name for the tag.
   * @param matchLevel The match level.
   * @return The completion item.
   */
  protected open fun createTagCompletionItem(
    simpleName: String,
    qualifiedName: String,
    matchLevel: MatchLevel
  ): CompletionItem =
    CompletionItem().apply {
      this.label = simpleName
      this.detail = qualifiedName
      this.sortText = label.toString()
      this.matchLevel = matchLevel
      this.kind = CLASS
      this.data = CompletionData().apply { className = qualifiedName }
    }

  /**
   * Creates completion item for attribute completion items.
   *
   * @param attr The attribute reference from the resource table.
   * @param matchLevel The match level.
   */
  protected open fun createAttrCompletionItem(
    attr: Reference,
    matchLevel: MatchLevel
  ): CompletionItem =
    CompletionItem().apply {
      var pck = attr.name.pck
      if (pck == null || pck.isBlank()) {
        pck = "android"
      }

      this.label = attr.name.entry!!
      this.kind = FIELD
      this.detail = "From package '$pck'"
      this.insertText = "$pck:${attr.name.entry!!}=\"$0\""
      this.insertTextFormat = SNIPPET
      this.sortText = label.toString()
      this.matchLevel = matchLevel
      this.command = Command("Trigger completion request", Command.TRIGGER_COMPLETION)
    }

  /**
   * Create a completion item for an attribute's value.
   *
   * @param pck The package of the attribute.
   * @param type The type of the value. For example: color, anim, layout, etc.
   * @param name The attribute value.
   * @param matchLevel The match level.
   */
  protected open fun createAttrValueCompletionItem(
    pck: String = "",
    type: String,
    name: String,
    matchLevel: MatchLevel
  ): CompletionItem {
    val sb = StringBuilder()
    sb.append("@")
    if (pck.isNotBlank()) {
      sb.append(pck)
      sb.append(":")
    }
    sb.append(type)
    sb.append("/")
    sb.append(name)

    val text = sb.toString()
    return CompletionItem().apply {
      this.label = text
      this.detail = "From package '$pck'"
      this.kind = VALUE
      this.sortText = text
      this.insertText = text
      this.matchLevel = matchLevel
    }
  }
  
  /**
   * Create a completion item for an attribute's value.
   *
   * @param pck The package of the attribute.
   * @param name The name of the enum or flag value.
   * @param matchLevel The match level.
   */
  protected open fun createEnumOrFlagCompletionItem(
    pck: String = "",
    name: String,
    matchLevel: MatchLevel
  ): CompletionItem {
    return CompletionItem().apply {
      this.label = name
      this.detail = "From package '$pck'"
      this.kind = VALUE
      this.sortText = name
      this.insertText = name
      this.matchLevel = matchLevel
    }
  }
}
