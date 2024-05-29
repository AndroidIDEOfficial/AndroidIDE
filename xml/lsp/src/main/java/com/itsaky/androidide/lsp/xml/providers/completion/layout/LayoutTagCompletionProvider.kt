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
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.xml.providers.completion.IXmlCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.canCompleteLayout
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.TAG
import org.eclipse.lemminx.dom.DOMDocument

/**
 * [LayoutCompletionProvider] implementation for providing completing tags in an XML layout file.
 *
 * @author Akash Yadav
 */
open class LayoutTagCompletionProvider(val provider: ICompletionProvider) :
  IXmlCompletionProvider(provider) {

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && canCompleteLayout(pathData, type) && type == TAG
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

    return getCompleter(newPrefix).complete(params, pathData, document, type, newPrefix)
  }

  private fun getCompleter(prefix: String): IXmlCompletionProvider {
    if (prefix.contains('.')) {
      return QualifiedTagCompleter(provider)
    }

    return SimpleTagCompleter(provider)
  }
}
