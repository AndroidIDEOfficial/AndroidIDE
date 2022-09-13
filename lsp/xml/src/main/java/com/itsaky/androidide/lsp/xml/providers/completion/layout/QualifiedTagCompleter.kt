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
import com.itsaky.androidide.lsp.xml.providers.completion.match
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.utils.ClassTrie
import com.itsaky.androidide.xml.widgets.WidgetTable
import com.itsaky.androidide.xml.widgets.internal.DefaultWidgetTable
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Completes tags from
 *
 * @author Akash Yadav
 */
class QualifiedTagCompleter(provider: ICompletionProvider) : LayoutTagCompletionProvider(provider) {

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val result = mutableListOf<CompletionItem>()
    val (widgets, module) = doLookup()
    var fqn = prefix
    if (fqn.endsWith('.')) {
      fqn = fqn.substringBeforeLast('.')
    }

    widgets.getNode(fqn)?.children?.values?.forEach {
      val qualifiedName = "${fqn}.${it.name}"
      val match = match(it.name, qualifiedName, prefix)
      result.add(createTagCompletionItem(it.name, qualifiedName, match))
    }
    
    addFromTrie(module.compileClasspathClasses, fqn, prefix, result)
    addFromTrie(module.compileJavaSourceClasses, fqn, prefix, result)

    return CompletionResult(result)
  }
  
  private fun addFromTrie(trie: ClassTrie, fqn: String, prefix: String, result: MutableList<CompletionItem>) {
    val node = trie.findNode(fqn) ?: return
    node.children.values.forEach {
      val match = match(it.name, it.qualifiedName, prefix)
      result.add(createTagCompletionItem(it.name, it.qualifiedName, match))
    }
  }

  private fun doLookup(): Pair<DefaultWidgetTable, ModuleProject> {
    val widgets =
      Lookup.DEFAULT.lookup(WidgetTable.COMPLETION_LOOKUP_KEY)
        ?: throw IllegalStateException("No widget table provided")
    val module =
      Lookup.DEFAULT.lookup(ModuleProject.COMPLETION_MODULE_KEY)
        ?: throw IllegalStateException("No module project provided")
    return widgets as DefaultWidgetTable to module
  }
}
