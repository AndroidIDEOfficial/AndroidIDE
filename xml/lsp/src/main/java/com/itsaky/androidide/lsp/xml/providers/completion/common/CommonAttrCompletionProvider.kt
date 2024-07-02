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

import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.Styleable
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.AttrCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.ITagTransformer
import com.itsaky.androidide.xml.res.IResourceGroup
import org.eclipse.lemminx.dom.DOMNode

/**
 * Provides attribute completion for all other resource types.
 *
 * @property tagTransform A function which returns the styleable entry name for the given tag name
 * (first param) and its parent's tag name (second param).
 * @author Akash Yadav
 */
open class CommonAttrCompletionProvider(
  protected val tagTransform: ITagTransformer,
  provider: ICompletionProvider
) : AttrCompletionProvider(provider) {

  override fun findNodeStyleables(node: DOMNode, styleables: IResourceGroup): Set<Styleable> {
    val name = node.nodeName
    val styleable =
      styleables
        .findEntry(tagTransform.transform(name, nodeAtCursor.parentNode?.nodeName ?: ""))
        ?.findValue(ConfigDescription())
        ?.value
    if (styleable != null && styleable is Styleable) {
      return setOf(styleable)
    }

    return emptySet()
  }
}
