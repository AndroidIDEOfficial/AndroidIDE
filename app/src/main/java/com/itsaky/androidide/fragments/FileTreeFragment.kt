/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.updatePadding
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder
import com.itsaky.androidide.databinding.LayoutEditorFileTreeBinding
import com.itsaky.androidide.eventbus.events.filetree.FileClickEvent
import com.itsaky.androidide.eventbus.events.filetree.FileLongClickEvent
import com.itsaky.androidide.events.ExpandTreeNodeRequestEvent
import com.itsaky.androidide.events.ListProjectFilesRequestEvent
import com.itsaky.androidide.projects.ProjectManager.getProjectDirPath
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.tasks.TaskExecutor.executeAsync
import com.itsaky.androidide.tasks.callables.FileTreeCallable
import com.itsaky.androidide.tasks.callables.FileTreeCallable.SortFileName
import com.itsaky.androidide.tasks.callables.FileTreeCallable.SortFolder
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.doOnApplyWindowInsets
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.model.TreeNode.TreeNodeClickListener
import com.unnamed.b.atv.model.TreeNode.TreeNodeLongClickListener
import com.unnamed.b.atv.view.AndroidTreeView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.io.File
import java.util.Arrays

class FileTreeFragment :
  BottomSheetDialogFragment(), TreeNodeClickListener, TreeNodeLongClickListener {
  private var binding: LayoutEditorFileTreeBinding? = null
  private var mFileTreeView: AndroidTreeView? = null
  private var mRoot: TreeNode? = null
  private var mTreeState: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = LayoutEditorFileTreeBinding.inflate(inflater, container, false)
    binding?.root?.doOnApplyWindowInsets { view, insets, _, _ ->
      insets.getInsets(statusBars()).apply { view.updatePadding(top = top + SizeUtils.dp2px(8f)) }
    }
    return binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_STORED_TREE_STATE)) {
      mTreeState = savedInstanceState.getString(KEY_STORED_TREE_STATE, null)
    }
    listProjectFiles()
  }

  override fun onStart() {
    super.onStart()
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    saveTreeState()
    outState.putString(KEY_STORED_TREE_STATE, mTreeState)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
    mFileTreeView = null
  }

  fun saveTreeState() {
    mTreeState =
      if (mFileTreeView != null) {
        mFileTreeView!!.saveState
      } else {
        LOG.error("Unable to save tree state. TreeView is null.")
        null
      }
  }

  override fun onClick(node: TreeNode, p2: Any) {
    val file = p2 as File
    if (!file.exists()) {
      return
    }
    if (file.isDirectory) {
      if (node.isExpanded) {
        collapseNode(node)
      } else {
        setLoading(node)
        listNode(node) { expandNode(node) }
      }
    }
    val event = FileClickEvent(file)
    event.put(Context::class.java, requireContext())
    EventBus.getDefault().post(event)
  }

  private fun collapseNode(node: TreeNode) {
    if (mFileTreeView == null) {
      return
    }
    TransitionManager.beginDelayedTransition(binding!!.root, ChangeBounds())
    mFileTreeView!!.collapseNode(node)
    updateChevron(node)
  }

  private fun updateChevron(node: TreeNode) {
    if (node.viewHolder is FileTreeViewHolder) {
      (node.viewHolder as FileTreeViewHolder).updateChevron(node.isExpanded)
    }
  }

  private fun expandNode(node: TreeNode) {
    if (mFileTreeView == null) {
      return
    }
    TransitionManager.beginDelayedTransition(binding!!.root, ChangeBounds())
    mFileTreeView!!.expandNode(node)
    updateChevron(node)
  }

  private fun setLoading(node: TreeNode) {
    if (node.viewHolder is FileTreeViewHolder) {
      (node.viewHolder as FileTreeViewHolder).setLoading(true)
    }
  }

  private fun listNode(node: TreeNode, whenDone: Runnable) {
    node.children.clear()
    node.isExpanded = false
    executeAsync({
      listFilesForNode(node.value.listFiles() ?: return@executeAsync null, node)
      var temp = node
      while (temp.size() == 1) {
        temp = temp.childAt(0)
        if (!temp.value.isDirectory) {
          break
        }
        listFilesForNode(temp.value.listFiles() ?: continue, temp)
        temp.isExpanded = true
      }
      null
    }) {
      whenDone.run()
    }
  }

  private fun listFilesForNode(files: Array<File>, parent: TreeNode) {
    Arrays.sort(files, SortFileName())
    Arrays.sort(files, SortFolder())
    for (file in files) {
      val node = TreeNode(file)
      node.viewHolder = FileTreeViewHolder(context)
      parent.addChild(node)
    }
  }

  override fun onLongClick(node: TreeNode, value: Any): Boolean {
    val event = FileLongClickEvent((value as File))
    event.put(Context::class.java, requireContext())
    event.put(TreeNode::class.java, node)
    EventBus.getDefault().post(event)
    return true
  }

  @Suppress("unused", "UNUSED_PARAMETER")
  @Subscribe(threadMode = MAIN)
  fun onGetListFilesRequested(event: ListProjectFilesRequestEvent?) {
    if (!isVisible || context == null) {
      return
    }
    listProjectFiles()
  }

  @Suppress("unused")
  @Subscribe(threadMode = MAIN)
  fun onGetExpandTreeNodeRequest(event: ExpandTreeNodeRequestEvent) {
    if (!isVisible || context == null) {
      return
    } else {
      event.node
    }
    expandNode(event.node)
  }

  fun listProjectFiles() {
    if (binding == null) {
      // Fragment has been destroyed
      return
    }
    val projectDirPath = getProjectDirPath()
    val projectDir = File(projectDirPath)
    mRoot = TreeNode(File(""))
    mRoot!!.viewHolder = FileTreeViewHolder(requireContext())

    val projectRoot = TreeNode.root(projectDir)
    projectRoot.viewHolder = FileTreeViewHolder(context)
    mRoot!!.addChild(projectRoot)

    binding!!.horizontalCroll.visibility = View.GONE
    binding!!.horizontalCroll.visibility = View.VISIBLE
    executeAsync(FileTreeCallable(context, projectRoot, projectDir)) {
      if (binding == null) {
        // Fragment has been destroyed
        return@executeAsync
      }
      binding!!.horizontalCroll.visibility = View.VISIBLE
      binding!!.loading.visibility = View.GONE
      val tree = createTreeView(mRoot)
      if (tree != null) {
        tree.setUseAutoToggle(false)
        tree.setDefaultNodeClickListener(this@FileTreeFragment)
        tree.setDefaultNodeLongClickListener(this@FileTreeFragment)
        binding!!.horizontalCroll.removeAllViews()
        val view = tree.view
        binding!!.horizontalCroll.addView(view)
        view.post { tryRestoreState() }
      }
    }
  }

  private fun createTreeView(node: TreeNode?): AndroidTreeView? {
    return if (context == null) {
      null
    } else AndroidTreeView(context, node, drawable.bg_ripple).also { mFileTreeView = it }
  }

  private fun tryRestoreState(state: String? = mTreeState) {
    if (!TextUtils.isEmpty(state) && mFileTreeView != null) {
      mFileTreeView!!.collapseAll()
      val openNodes =
        state!!.split(AndroidTreeView.NODES_PATH_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
      restoreNodeState(mRoot!!, HashSet(openNodes))
    }

    mRoot?.let { rootNode ->
      if (rootNode.children.isNotEmpty()) {
        rootNode.childAt(0)?.let { projectRoot -> expandNode(projectRoot) }
      }
    }
  }

  private fun restoreNodeState(root: TreeNode, openNodes: Set<String>) {
    val children = root.children
    var i = 0
    val childrenSize = children.size
    while (i < childrenSize) {
      val node = children[i]
      if (openNodes.contains(node.path)) {
        listNode(node) {
          expandNode(node)
          restoreNodeState(node, openNodes)
        }
      }
      i++
    }
  }

  companion object {

    // Should be same as defined in layout/activity_editor.xml
    const val TAG = "editor.fileTree"
    private const val KEY_STORED_TREE_STATE = "fileTree_state"
    private val LOG = ILogger.newInstance("FileTreeFragment")
    @JvmStatic
    fun newInstance(): FileTreeFragment {
      return FileTreeFragment()
    }
  }
}
