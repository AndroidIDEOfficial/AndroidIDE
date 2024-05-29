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

package com.itsaky.androidide.handlers

import android.content.Context
import androidx.core.view.GravityCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem.Location.EDITOR_FILE_TREE
import com.itsaky.androidide.actions.ActionMenu
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.internal.DefaultActionsRegistry
import com.itsaky.androidide.activities.editor.EditorHandlerActivity
import com.itsaky.androidide.eventbus.events.filetree.FileClickEvent
import com.itsaky.androidide.eventbus.events.filetree.FileLongClickEvent
import com.itsaky.androidide.events.ExpandTreeNodeRequestEvent
import com.itsaky.androidide.events.FileContextMenuItemClickEvent
import com.itsaky.androidide.events.ListProjectFilesRequestEvent
import com.itsaky.androidide.fragments.sheets.OptionsListFragment
import com.itsaky.androidide.models.SheetOption
import com.itsaky.androidide.utils.ApkInstaller
import com.itsaky.androidide.utils.InstallationResultHandler
import com.itsaky.androidide.utils.flashError
import com.unnamed.b.atv.model.TreeNode
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.io.File

/**
 * Handles events related to files in filetree.
 *
 * @author Akash Yadav
 */
@Suppress("unused")
class FileTreeActionHandler : BaseEventHandler() {

  private var lastHeld: TreeNode? = null

  companion object {

    const val TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment"
    const val MB_10: Long = 10 * 1024 * 1024
  }

  @Subscribe(threadMode = MAIN)
  fun onFileClicked(event: FileClickEvent) {
    if (!checkIsEditorActivity(event)) {
      logCannotHandle(event)
      return
    }

    if (event.file.isDirectory) {
      return
    }

    val context = event[Context::class.java]!! as EditorHandlerActivity
    context.binding.root.closeDrawer(GravityCompat.START)
    if (event.file.name.endsWith(".apk")) {
      ApkInstaller.installApk(
        context,
        InstallationResultHandler.createEditorActivitySender(context),
        event.file,
        context.installationSessionCallback()
      )
      return
    }

    if (MB_10 < event.file.length()) {
      flashError("File is too big!")
      log.warn(
        "Cannot open {} as it is too big. File size: {} bytes", event.file, event.file.length())
      return
    }

    context.openFile(event.file)
  }

  @Subscribe(threadMode = MAIN)
  fun onFileLongClicked(event: FileLongClickEvent) {
    if (!checkIsEditorActivity(event)) {
      logCannotHandle(event)
      return
    }

    this.lastHeld = event[TreeNode::class.java]
    val context = event[Context::class.java]!! as EditorHandlerActivity
    createFileOptionsFragment(context, event.file)
      .show(context.supportFragmentManager, TAG_FILE_OPTIONS_FRAGMENT)
  }

  private fun createFileOptionsFragment(
    context: EditorHandlerActivity,
    file: File
  ): OptionsListFragment {
    val fragment = OptionsListFragment()
    val registry = ActionsRegistry.getInstance()
    val actions = registry.getActions(EDITOR_FILE_TREE)
    val data = ActionData()
    data.apply {
      put(Context::class.java, context)
      put(File::class.java, file)
      put(TreeNode::class.java, lastHeld)
    }

    for (action in actions.values) {

      check(action !is ActionMenu) { "File tree actions do not support action menus" }

      action.prepare(data)
      if (!action.enabled || !action.visible) {
        continue
      }

      fragment.addOption(
        SheetOption(action.id, action.icon, action.label, file).apply { this.extra = data }
      )
    }

    return fragment
  }

  @Subscribe(threadMode = MAIN)
  internal fun onFileOptionClicked(event: FileContextMenuItemClickEvent) {
    val option = event.option
    if (option.extra !is ActionData) {
      return
    }

    val data = option.extra!! as ActionData
    val registry = ActionsRegistry.getInstance() as DefaultActionsRegistry
    val action = registry.findAction(EDITOR_FILE_TREE, option.id)

    checkNotNull(action) {
      "Invalid FileContextMenuItemClickEvent received. No action item registered with id '${option.id}'"
    }

    registry.executeAction(action, data)
  }

  private fun requestFileListing() {
    EventBus.getDefault().post(ListProjectFilesRequestEvent())
  }

  private fun requestExpandHeldNode() {
    requestExpandNode(lastHeld!!)
  }

  private fun requestExpandNode(node: TreeNode) {
    EventBus.getDefault().post(ExpandTreeNodeRequestEvent(node))
  }
}
