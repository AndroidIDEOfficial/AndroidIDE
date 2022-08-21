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

import com.android.aaptcompiler.ResourcePathData
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItem.Companion.matchLevel
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.providers.completion.TagCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument
import kotlin.math.max

/**
 * [TagCompletionProvider] implementation for providing completing tags in an XML layout file.
 *
 * @author Akash Yadav
 */
class LayoutTagCompletionProvider : LayoutCompletionProvider() {

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && type == TAG
  }

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val newPrefix =
      if (prefix.startsWith("<")) {
        prefix.substring(1)
      } else {
        prefix
      }

    val widgets =
      Lookup.DEFAULT.lookup(WidgetTable.COMPLETION_LOOKUP_KEY)?.getAllWidgets()
        ?: return CompletionResult.EMPTY
    val result = mutableListOf<CompletionItem>()

    for (widget in widgets) {
      val simpleNameMatchLevel = matchLevel(widget.simpleName, newPrefix)
      val nameMatchLevel = matchLevel(widget.qualifiedName, newPrefix)
      if (simpleNameMatchLevel == NO_MATCH && nameMatchLevel == NO_MATCH) {
        continue
      }

      val matchLevel =
        MatchLevel.values()[max(simpleNameMatchLevel.ordinal, nameMatchLevel.ordinal)]

      result.add(createTagCompletionItem(widget.simpleName, widget.qualifiedName, matchLevel))
    }

    return CompletionResult(result)
  }
}
