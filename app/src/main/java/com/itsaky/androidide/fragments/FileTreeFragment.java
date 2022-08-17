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
package com.itsaky.androidide.fragments;

import static com.unnamed.b.atv.view.AndroidTreeView.NODES_PATH_SEPARATOR;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.databinding.LayoutEditorFileTreeBinding;
import com.itsaky.androidide.eventbus.events.filetree.FileClickEvent;
import com.itsaky.androidide.eventbus.events.filetree.FileLongClickEvent;
import com.itsaky.androidide.events.ExpandTreeNodeRequestEvent;
import com.itsaky.androidide.events.ListProjectFilesRequestEvent;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.FileTreeCallable;
import com.itsaky.androidide.utils.ILogger;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileTreeFragment extends BottomSheetDialogFragment
    implements TreeNode.TreeNodeClickListener, TreeNode.TreeNodeLongClickListener {

  private static final String KEY_STORED_TREE_STATE = "fileTree_state";
  private static final ILogger LOG = ILogger.newInstance("FileTreeFragment");
  private LayoutEditorFileTreeBinding binding;
  private AndroidTreeView mFileTreeView;
  private TreeNode mRoot;
  private String mTreeState;

  public FileTreeFragment() {}

  @NonNull
  public static FileTreeFragment newInstance() {
    return new FileTreeFragment();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = LayoutEditorFileTreeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_STORED_TREE_STATE)) {
      mTreeState = savedInstanceState.getString(KEY_STORED_TREE_STATE, null);
    }

    listProjectFiles();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    saveTreeState();
    outState.putString(KEY_STORED_TREE_STATE, mTreeState);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
    mFileTreeView = null;
  }

  public void saveTreeState() {
    if (mFileTreeView != null) {
      mTreeState = mFileTreeView.getSaveState();
    } else {
      LOG.error("Unable to save tree state. TreeView is null.");
      mTreeState = null;
    }
  }

  @Override
  public void onClick(TreeNode node, Object p2) {
    final File file = (File) p2;
    if (!file.exists()) {
      return;
    }

    if (file.isDirectory()) {
      if (node.isExpanded()) {
        collapseNode(node);
      } else {
        setLoading(node);
        listNode(node, () -> expandNode(node));
      }
    }

    final var event = new FileClickEvent(file);
    event.put(Context.class, requireContext());
    EventBus.getDefault().post(event);
  }

  public void collapseNode(TreeNode node) {
    if (mFileTreeView == null) {
      return;
    }

    TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
    mFileTreeView.collapseNode(node);
    updateChevron(node);
  }

  private void updateChevron(@NonNull TreeNode node) {
    if (node.getViewHolder() instanceof FileTreeViewHolder) {
      ((FileTreeViewHolder) node.getViewHolder()).updateChevron(node.isExpanded());
    }
  }

  public void expandNode(TreeNode node) {
    if (mFileTreeView == null) {
      return;
    }

    TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
    mFileTreeView.expandNode(node);
    updateChevron(node);
  }

  private void setLoading(@NonNull TreeNode node) {
    if (node.getViewHolder() instanceof FileTreeViewHolder) {
      ((FileTreeViewHolder) node.getViewHolder()).setLoading(true);
    }
  }

  private void listNode(@NonNull TreeNode node, Runnable whenDone) {

    if (whenDone == null) {
      whenDone = () -> {};
    }

    node.getChildren().clear();
    node.setExpanded(false);

    final var finalWhenDone = whenDone;
    TaskExecutor.executeAsync(
        () -> {
          listFilesForNode(node.getValue().listFiles(), node);
          TreeNode temp = node;
          while (temp.size() == 1) {
            temp = temp.childAt(0);
            if (!temp.getValue().isDirectory()) {
              break;
            }
            listFilesForNode(temp.getValue().listFiles(), temp);
            temp.setExpanded(true);
          }
          return null;
        },
        __ -> finalWhenDone.run());
  }

  private void listFilesForNode(File[] files, TreeNode parent) {
    Arrays.sort(files, new FileTreeCallable.SortFileName());
    Arrays.sort(files, new FileTreeCallable.SortFolder());
    for (File file : files) {
      TreeNode node = new TreeNode(file);
      node.setViewHolder(new FileTreeViewHolder(getContext()));
      parent.addChild(node);
    }
  }

  @Override
  public boolean onLongClick(TreeNode node, Object value) {
    final var event = new FileLongClickEvent((File) value);
    event.put(Context.class, requireContext());
    event.put(TreeNode.class, node);
    EventBus.getDefault().post(event);
    return true;
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onGetListFilesRequested(ListProjectFilesRequestEvent event) {
    if (!isVisible() || getContext() == null) {
      return;
    }

    listProjectFiles();
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onGetExpandTreeNodeRequest(ExpandTreeNodeRequestEvent event) {
    if (!isVisible() || getContext() == null) {
      return;
    } else {
      event.getNode();
    }

    expandNode(event.getNode());
  }

  public void listProjectFiles() {
    if (binding == null) {
      // Fragment has been destroyed
      return;
    }
    final var projectDirPath = ProjectManager.INSTANCE.getProjectDirPath();
    final var projectDir = new File(projectDirPath);
    mRoot = TreeNode.root(projectDir);
    mRoot.setViewHolder(new FileTreeViewHolder(getContext()));

    binding.filetreeHorizontalScrollView.setVisibility(View.GONE);
    binding.fileTreeLoadingProgress.setVisibility(View.VISIBLE);
    TaskExecutor.executeAsync(
        new FileTreeCallable(getContext(), mRoot, projectDir),
        (result) -> {
          if (binding == null) {
            // Fragment has been destroyed
            return;
          }
          binding.filetreeHorizontalScrollView.setVisibility(View.VISIBLE);
          binding.fileTreeLoadingProgress.setVisibility(View.GONE);
          AndroidTreeView tree = createTreeView(mRoot);
          if (tree != null) {
            tree.setUseAutoToggle(false);
            tree.setDefaultNodeClickListener(FileTreeFragment.this);
            tree.setDefaultNodeLongClickListener(FileTreeFragment.this);
            binding.filetreeHorizontalScrollView.removeAllViews();

            final var view = tree.getView();
            binding.filetreeHorizontalScrollView.addView(view);

            view.post(this::tryRestoreState);
          }
        });
  }

  public AndroidTreeView createTreeView(TreeNode node) {
    Context ctx = null;
    if (getActivity() != null) {
      ctx = getActivity();
    } else if (getContext() != null) {
      ctx = getContext();
    }

    if (ctx == null) {
      return null;
    }

    return mFileTreeView = new AndroidTreeView(ctx, node, R.drawable.bg_ripple);
  }

  private void tryRestoreState() {
    tryRestoreState(mTreeState);
  }

  private void tryRestoreState(String state) {
    if (!TextUtils.isEmpty(state) && mFileTreeView != null) {
      mFileTreeView.collapseAll();
      final var openNodesArray = state.split(NODES_PATH_SEPARATOR);
      final var openNodes = new HashSet<>(Arrays.asList(openNodesArray));
      restoreNodeState(mRoot, openNodes);
    }
  }

  private void restoreNodeState(@NonNull TreeNode root, Set<String> openNodes) {
    final var children = root.getChildren();
    for (int i = 0, childrenSize = children.size(); i < childrenSize; i++) {
      final var node = children.get(i);
      if (openNodes.contains(node.getPath())) {
        listNode(
            node,
            () -> {
              expandNode(node);
              restoreNodeState(node, openNodes);
            });
      }
    }
  }
}
