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
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionParams
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.projects.ModuleProject
import com.itsaky.androidide.utils.ClassTrie
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Completes platform widget names.
 *
 * @author Akash Yadav
 */
class SimpleTagCompleter(provider: ICompletionProvider) : LayoutTagCompletionProvider(provider) {

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val widgets =
      Lookup.getDefault().lookup(WidgetTable.COMPLETION_LOOKUP_KEY)?.getAllWidgets()
        ?: return CompletionResult.EMPTY
    val result = mutableListOf<CompletionItem>()

    // Complete all tags which do not require fully qualified name
    for (widget in widgets) {
      val match = matchLevel(widget.simpleName, prefix)
      result.add(createTagCompletionItem(widget.simpleName, widget.qualifiedName, match, true))
    }

    // Complete the root package names if possible
    val module =
      Lookup.getDefault().lookup(ModuleProject.COMPLETION_MODULE_KEY) ?: return CompletionResult(result)

    // Add root packages from the compile classpath and source paths
    addFromTrie(module.compileClasspathClasses, prefix, result)
    addFromTrie(module.compileJavaSourceClasses, prefix, result)

    return CompletionResult(result)
  }

  private fun addFromTrie(trie: ClassTrie, prefix: String, result: MutableList<CompletionItem>) {
    trie.root.children.values.forEach {
      val match = matchLevel(it.name, prefix)
      result.add(createTagCompletionItem(it.name, it.qualifiedName, match))
    }
  }
}
