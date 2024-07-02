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

import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.api.describeSnippet
import com.itsaky.androidide.lsp.models.ClassCompletionData
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItemKind.CLASS
import com.itsaky.androidide.lsp.models.CompletionItemKind.FIELD
import com.itsaky.androidide.lsp.models.CompletionItemKind.VALUE
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.InsertTextFormat.PLAIN_TEXT
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.xml.edits.AttrValueEditHandler
import com.itsaky.androidide.lsp.xml.edits.QualifiedValueEditHandler
import com.itsaky.androidide.lsp.xml.edits.TagEditHandler
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import org.eclipse.lemminx.dom.DOMAttr
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for all XML completion providers.
 *
 * @author Akash Yadav
 */
abstract class IXmlCompletionProvider(private val provider: ICompletionProvider) {

  protected lateinit var nodeAtCursor: DOMNode
  protected lateinit var attrAtCursor: DOMAttr
  protected lateinit var allNamespaces: Set<Pair<String, String>>

  companion object {
    @JvmStatic
    protected val log: Logger = LoggerFactory.getLogger(IXmlCompletionProvider::class.java)

    const val NAMESPACE_PREFIX = "http://schemas.android.com/apk/res/"
    const val NAMESPACE_AUTO = "http://schemas.android.com/apk/res-auto"
  }

  protected open fun matchLevel(candidate: CharSequence, partial: CharSequence): MatchLevel {
    return provider.matchLevel(candidate, partial)
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

    this.nodeAtCursor =
      document.findNodeAt(params.position.requireIndex()) ?: return CompletionResult.EMPTY

    if (type == ATTRIBUTE_VALUE || type == ATTRIBUTE) {
      this.attrAtCursor =
        document.findAttrAt(params.position.requireIndex()) ?: return CompletionResult.EMPTY
    }

    this.allNamespaces = findAllNamespaces(this.nodeAtCursor)
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
    matchLevel: MatchLevel,
    isPlatformWidget: Boolean = false
  ): CompletionItem =
    CompletionItem().apply {
      this.ideLabel = simpleName
      this.detail = qualifiedName
      this.ideSortText = ideLabel
      this.insertText = if (isPlatformWidget) simpleName else qualifiedName
      this.insertTextFormat = PLAIN_TEXT
      this.editHandler = TagEditHandler()
      this.matchLevel = matchLevel
      this.completionKind = CLASS
      this.data = ClassCompletionData(className = qualifiedName)
    }

  /**
   * Creates completion item for attribute completion items.
   *
   * @param attr The attribute reference from the resource table.
   * @param matchLevel The match level.
   */
  protected open fun createAttrCompletionItem(
    attr: com.android.aaptcompiler.Reference,
    partial: String,
    resPkg: String,
    nsPrefix: String,
    hasNamespace: Boolean,
    matchLevel: MatchLevel
  ): CompletionItem =
    CompletionItem().apply {
      var prefix = nsPrefix
      if (nsPrefix.isBlank()) {
        prefix = ""
      } else if (!nsPrefix.endsWith(':')) {
        prefix += ":"
      }

      val title = "$prefix${attr.name.entry!!}"
      val insertText = if (hasNamespace) attr.name.entry!! else title
      this.ideLabel = title
      this.completionKind = FIELD
      this.detail = "From package '$resPkg'"
      this.insertText = "$insertText=\"$0\""
      this.insertTextFormat = SNIPPET
      this.snippetDescription = describeSnippet(partial)
      this.ideSortText = ideLabel
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
    if (pck.isNotBlank() && pck == ResourceTableRegistry.PCK_ANDROID) {
      sb.append(pck)
      sb.append(":")
    }
    sb.append(type)
    sb.append("/")
    sb.append(name)

    val text = sb.toString()
    return CompletionItem().apply {
      this.ideLabel = text
      this.detail = "From package '$pck'"
      this.completionKind = VALUE
      this.overrideTypeText = type.uppercase()
      this.ideSortText = if (pck == ResourceTableRegistry.PCK_ANDROID) "zzz$text" else text
      this.insertText = text
      this.insertTextFormat = PLAIN_TEXT
      this.editHandler = AttrValueEditHandler()
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
      this.ideLabel = name
      this.detail = if (pck.isBlank()) "" else "From package '$pck'"
      this.completionKind = VALUE
      this.ideSortText = "000$name"
      this.insertText = name
      this.editHandler = QualifiedValueEditHandler()
      this.matchLevel = matchLevel
    }
  }

  protected open fun findResourceTables(nsUri: String?): Set<IResourceTable> {
    if (nsUri.isNullOrBlank()) {
      return emptySet()
    }
    if (nsUri == NAMESPACE_AUTO) {
      return findAllModuleResourceTables()
    }

    val pck = nsUri.substringAfter(NAMESPACE_PREFIX)
    if (pck.isBlank()) {
      log.warn("Invalid namespace: {}", nsUri)
      return emptySet()
    }

    if (pck == ResourceTableRegistry.PCK_ANDROID) {
      val platformResTable =
        Lookup.getDefault().lookup(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES)
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

  protected open fun findAllModuleResourceTables(): Set<IResourceTable> {
    val lookup = Lookup.getDefault()
    val sourceResTables = lookup.lookup(ResourceTableRegistry.COMPLETION_MODULE_RES) ?: emptySet()
    val depResTables = lookup.lookup(ResourceTableRegistry.COMPLETION_DEP_RES) ?: emptySet()
    return mutableSetOf<IResourceTable>().also {
      it.addAll(sourceResTables)
      it.addAll(depResTables)
    }
  }
}
