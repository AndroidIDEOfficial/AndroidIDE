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

import com.android.SdkConstants
import com.android.SdkConstants.ANDROID_NS_NAME_PREFIX
import com.android.SdkConstants.ATTR_NAME
import com.android.SdkConstants.TAG_ACTIVITY
import com.android.SdkConstants.TAG_INTENT_FILTER
import com.android.SdkConstants.TAG_RECEIVER
import com.android.SdkConstants.TAG_SERVICE
import com.android.aaptcompiler.ResourceGroup
import com.android.aaptcompiler.ResourceTable
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ICompletionProvider
import com.itsaky.androidide.lsp.models.CompletionData
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionItemKind.FIELD
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.lsp.xml.edits.QualifiedValueEditHandler
import com.itsaky.androidide.lsp.xml.providers.completion.common.AttrValueCompletionProvider
import com.itsaky.androidide.lsp.xml.providers.completion.match
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.ModuleProject
import com.itsaky.androidide.xml.permissions.Permission
import com.itsaky.androidide.xml.resources.ResourceTableRegistry

/**
 * Completes attribute values in manifest file.
 *
 * @author Akash Yadav
 */
class ManifestAttrValueCompletionProvider(provider: ICompletionProvider) :
  AttrValueCompletionProvider(provider) {

  override fun completeInternal(
    attrName: String,
    prefix: String,
    groups: MutableSet<Triple<String, ResourceTable, ResourceGroup>>,
    result: MutableList<CompletionItem>
  ) {
    if (this.attrAtCursor.nodeName == /*android:name*/ "${ANDROID_NS_NAME_PREFIX}${ATTR_NAME}") {
      when (this.nodeAtCursor.nodeName) {
        "action" -> completeActionName(prefix, result)
        "category" -> completeCategory(prefix, result)
        "uses-permission" -> completePermission(prefix, result)
        "uses-feature" -> completeFeature(prefix, result)
      }
    }
    super.completeInternal(attrName, prefix, groups, result)
  }

  // TODO we could add an action using the actions registry to make it easier to add permissions
  //  with a GUI interface
  private fun completePermission(prefix: String, result: MutableList<CompletionItem>) {
    for (value in Permission.values()) {
      val match = match(value.name, value.constant, prefix)
      if (match == NO_MATCH) {
        continue
      }
  
      val item = createEnumOrFlagCompletionItem(ResourceTableRegistry.PCK_ANDROID, value.name, match)
      item.insertText = "."
      item.additionalEditHandler = QualifiedValueEditHandler()
      item.overrideTypeText = "Permission"
      
      // Show API information
      item.kind = FIELD
      item.data = CompletionData().apply { 
        this.className = SdkConstants.CLASS_MANIFEST_PERMISSION
        this.memberName = value.name
      }
      result.add(item)
    }
  }

  private fun completeActionName(prefix: String, result: MutableList<CompletionItem>) {
    val parent = this.nodeAtCursor.parentNode ?: return
    val parentOfParent = parent.parentNode ?: return

    if (parent.nodeName == TAG_INTENT_FILTER) {
      when (parentOfParent.nodeName) {
        TAG_ACTIVITY -> completeActivityActions(prefix, result)
        TAG_RECEIVER -> completeReceiverActions(prefix, result)
        TAG_SERVICE -> completeServiceActions(prefix, result)
      }
    }
  }

  private fun completeServiceActions(prefix: String, result: MutableList<CompletionItem>) {
    val module = getModule()
    if (module is AndroidModule) {
      addMatches(prefix, module.getServiceActions(), result)
    }
  }

  private fun completeActivityActions(prefix: String, result: MutableList<CompletionItem>) {
    val module = getModule()
    if (module is AndroidModule) {
      addMatches(prefix, module.getActivityActions(), result)
    }
  }

  private fun completeReceiverActions(prefix: String, result: MutableList<CompletionItem>) {
    val module = getModule()
    if (module is AndroidModule) {
      addMatches(prefix, module.getBroadcastActions(), result)
    }
  }

  private fun completeCategory(prefix: String, result: MutableList<CompletionItem>) {
    val mod = getModule()
    if (mod is AndroidModule) {
      addMatches(prefix, mod.getCategories(), result)
    }
  }

  private fun completeFeature(prefix: String, result: MutableList<CompletionItem>) {
    val mod = getModule()
    if (mod is AndroidModule) {
      addMatches(prefix, mod.getFeatures(), result)
    }
  }

  private fun addMatches(
    prefix: String,
    entries: List<String>,
    result: MutableList<CompletionItem>
  ) {
    for (entry in entries) {
      val match =
        if (entry.contains('.')) {
          match(entry.substringAfterLast('.'), entry, prefix)
        } else {
          matchLevel(entry, prefix)
        }

      if (match == NO_MATCH) {
        continue
      }

      val item = createEnumOrFlagCompletionItem(ResourceTableRegistry.PCK_ANDROID, entry, match)
      item.insertText = "."
      item.additionalEditHandler = QualifiedValueEditHandler()
      result.add(item)
    }
  }

  private fun getModule(): ModuleProject {
    return Lookup.DEFAULT.lookup(ModuleProject.COMPLETION_MODULE_KEY)
      ?: throw IllegalStateException("No module project provided")
  }

  override fun findResourceTables(nsUri: String): Set<ResourceTable> {
    return manifestResourceTable()
  }
}
