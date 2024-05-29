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

package com.itsaky.androidide.actions.filetree

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.eventbus.events.Event
import com.itsaky.androidide.events.ExpandTreeNodeRequestEvent
import com.itsaky.androidide.events.ListProjectFilesRequestEvent
import com.unnamed.b.atv.model.TreeNode
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Base class for actions related to the file tree.
 *
 * @author Akash Yadav
 */
abstract class BaseFileTreeAction(
  context: Context,
  @StringRes labelRes: Int? = null,
  @DrawableRes iconRes: Int? = null
) : EditorActivityAction() {

  override var requiresUIThread: Boolean = true
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_FILE_TREE

  init {
    labelRes?.let { label = context.getString(it) }
    iconRes?.let { icon = ContextCompat.getDrawable(context, it) }
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (!data.hasFileTreeData()) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  protected open fun ActionData.hasFileTreeData(): Boolean {
    return hasRequiredData(Context::class.java, File::class.java, TreeNode::class.java)
  }

  protected fun ActionData.getTreeNode() : TreeNode? {
    return this[TreeNode::class.java]
  }

  protected fun ActionData.requireTreeNode() : TreeNode {
    return getTreeNode()!!
  }

  protected fun Event.putData(context: Context): Event {
    put(Context::class.java, context)
    return this
  }

  protected fun requestFileListing() {
    EventBus.getDefault().post(ListProjectFilesRequestEvent())
  }

  protected fun requestExpandNode(node: TreeNode) {
    EventBus.getDefault().post(ExpandTreeNodeRequestEvent(node))
  }
}
