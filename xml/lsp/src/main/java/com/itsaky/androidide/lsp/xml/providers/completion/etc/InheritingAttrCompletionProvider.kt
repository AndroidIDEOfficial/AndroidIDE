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

package com.itsaky.androidide.lsp.xml.providers.completion.etc

import com.android.aaptcompiler.Styleable
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.AttrCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.ITagTransformer
import com.itsaky.androidide.xml.res.IResourceGroup
import org.eclipse.lemminx.dom.DOMNode

/**
 * Provides attribute completion for entries which inherit attributes from other entries.
 *
 * @author Akash Yadav
 */
class InheritingAttrCompletionProvider(
  private val parentProvider: (String) -> List<String> = { emptyList() },
  private val tagTransform: ITagTransformer,
  provider: ICompletionProvider
) : AttrCompletionProvider(provider) {

  override fun findNodeStyleables(node: DOMNode, styleables: IResourceGroup): Set<Styleable> {
    val nodeStyleables = mutableSetOf<Styleable>()
    val name = node.nodeName
    val entryName = tagTransform.transform(name, nodeAtCursor.parentNode?.nodeName ?: "")
    val styleable = findStyleableEntry(styleables, entryName)
    styleable?.let { nodeStyleables.add(it) }

    val parents = parentProvider(entryName)
    if (parents.isNotEmpty()) {
      parents.forEach {
        findStyleableEntry(styleables, it)?.let { parentStyleable ->
          nodeStyleables.add(parentStyleable)
        }
      }
    }
    return nodeStyleables
  }
}
