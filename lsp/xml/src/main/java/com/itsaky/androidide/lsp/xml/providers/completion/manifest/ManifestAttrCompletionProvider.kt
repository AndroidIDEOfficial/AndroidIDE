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

package com.itsaky.androidide.lsp.xml.providers.completion.manifest

import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.ResourceTable
import com.android.aaptcompiler.Styleable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.AttrCompletionProvider
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import org.eclipse.lemminx.dom.DOMNode

/**
 * Provides attribution completion for AndroidManifest.
 *
 * @author Akash Yadav
 */
class ManifestAttrCompletionProvider(provider: ICompletionProvider) :
  AttrCompletionProvider(provider) {

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && canCompleteManifest(pathData, type)
  }

  override fun findResourceTables(nsUri: String): Set<ResourceTable> {
    return setOf(
      Lookup.DEFAULT.lookup(ResourceTableRegistry.COMPLETION_MANIFEST_ATTR_RES) ?: return emptySet()
    )
  }

  override fun findNodeStyleables(node: DOMNode, styleables: ResourceGroup): Set<Styleable> {
    val name = node.nodeName
    val styleable =
      styleables.findEntry(transformToEntryName(name))?.findValue(ConfigDescription())?.value
    if (styleable != null && styleable is Styleable) {
      return setOf(styleable)
    }

    return emptySet()
  }
}
