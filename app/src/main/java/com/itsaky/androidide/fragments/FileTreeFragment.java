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
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutEditorFileTreeBinding;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.FileTreeCallable;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.views.editor.CodeEditorView;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

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
  private FileActionListener mFileActionListener;
  private TreeNode mRoot;
  private String mTreeState;

  public FileTreeFragment() {}

  @NonNull
  public static FileTreeFragment newInstance() {
    return new FileTreeFragment();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    mFileActionListener = (FileActionListener) context;
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    saveTreeState();
    outState.putString(KEY_STORED_TREE_STATE, mTreeState);
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
  public void onClick(TreeNode node, Object p2) {
    final File f = (File) p2;
    if (!f.exists()) {
      return;
    }

    if (f.isFile()) {
      if (f.getName().endsWith(".apk")) {
        final var intent = IntentUtils.getInstallAppIntent(f);
        if (intent != null) {
          requireContext().startActivity(intent);
        } else {
          StudioApp.getInstance()
              .toast(getString(R.string.msg_apk_install_intent_failed), Toaster.Type.ERROR);
        }
      } else if (mFileActionListener != null && (FileUtils.isUtf8(f) || f.length() == 0)) {
        mFileActionListener.openFile(f);
      }
    } else if (f.isDirectory() && f.exists()) {
      if (node.isExpanded()) {
        collapseNode(node);
      } else if (f.getAbsolutePath().equals(Environment.GRADLE_USER_HOME.getAbsolutePath())
          && !node.isExpanded()) {
        expandNode(node);
      } else {
        setLoading(node);
        listNode(
            node,
            () -> {
              updateChevron(node);
              expandNode(node);
            });
      }
    }
  }

  public void collapseNode(TreeNode node) {
    if (mFileTreeView == null) {
      return;
    }

    TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
    mFileTreeView.collapseNode(node);
  }

  public void expandNode(TreeNode node) {
    if (mFileTreeView == null) {
      return;
    }

    TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
    mFileTreeView.expandNode(node);
  }

  private void setLoading(@NonNull TreeNode node) {
    if (node.getViewHolder() instanceof FileTreeViewHolder) {
      ((FileTreeViewHolder) node.getViewHolder()).setLoading();
    }
  }

  private void listNode(@NonNull TreeNode node, Runnable whenDone) {

    if (whenDone == null) {
      whenDone = () -> {};
    }

    node.getChildren().clear();
    node.setExpanded(false);

    final var finalWhenDone = whenDone;
    TaskExecutor.execAsync(
        () -> {
          getNodeFromFiles(node.getValue().listFiles(), node);
          TreeNode temp = node;
          while (temp.size() == 1) {
            temp = temp.childAt(0);
            if (!temp.getValue().isDirectory()) {
              break;
            }
            getNodeFromFiles(temp.getValue().listFiles(), temp);
            temp.setExpanded(true);
          }
          return null;
        },
        __ -> finalWhenDone.run());
  }

  private void updateChevron(@NonNull TreeNode node) {
    if (node.getViewHolder() instanceof FileTreeViewHolder) {
      ((FileTreeViewHolder) node.getViewHolder()).updateChevron(!node.isExpanded());
    }
  }

  private void getNodeFromFiles(File[] files, TreeNode parent) {
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
    if (mFileActionListener != null) {
      mFileActionListener.showFileOptions((File) value, node);
    }
    return true;
  }

  public void listProjectFiles() {
    final var projectDirPath = ProjectManager.INSTANCE.getProjectDirPath();
    if (projectDirPath == null) {
      return;
    }

    final File gradleProps = Environment.GRADLE_PROPS;
    final File gradleHome = Environment.GRADLE_USER_HOME;
    File projectDir = new File(projectDirPath);
    mRoot = TreeNode.root(projectDir);
    if (gradleHome.exists() && gradleHome.isDirectory()) {
      if (!gradleProps.exists()) {
        FileIOUtils.writeFileFromString(
            gradleProps,
            "# Specify global Gradle properties in this file\n"
                + "# These properties will be applicable for every project you build"
                + " with Gradle.");
      }
      TreeNode home = new TreeNode(gradleHome);
      home.setViewHolder(new FileTreeViewHolder(getContext()));
      TreeNode prop = new TreeNode(gradleProps);
      prop.setViewHolder(new FileTreeViewHolder(getContext()));
      home.addChild(prop);
      mRoot.addChild(home);
    }
    mRoot.setViewHolder(new FileTreeViewHolder(getContext()));

    getScrollView().setVisibility(View.GONE);
    getLoadingProgress().setVisibility(View.VISIBLE);
    new TaskExecutor()
        .executeAsync(
            new FileTreeCallable(getContext(), mRoot, projectDir),
            (result) -> {
              getScrollView().setVisibility(View.VISIBLE);
              getLoadingProgress().setVisibility(View.GONE);
              AndroidTreeView tree = createTreeView(mRoot);
              if (tree != null) {
                tree.setUseAutoToggle(false);
                tree.setDefaultNodeClickListener(FileTreeFragment.this);
                tree.setDefaultNodeLongClickListener(FileTreeFragment.this);
                getScrollView().removeAllViews();

                final var view = tree.getView();
                getScrollView().addView(view);

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

  public HorizontalScrollView getScrollView() {
    return binding.filetreeHorizontalScrollView;
  }

  public ProgressBar getLoadingProgress() {
    return binding.fileTreeLoadingProgress;
  }

  private void tryRestoreState() {
    tryRestoreState(mTreeState);
  }

  private void tryRestoreState(String state) {
    if (!TextUtils.isEmpty(state) && mFileTreeView != null) {

      LOG.debug("Restoring tree view state:", "'" + state + "'");

      mFileTreeView.collapseAll();
      final var openNodesArray = state.split(NODES_PATH_SEPARATOR);
      final var openNodes = new HashSet<>(Arrays.asList(openNodesArray));
      restoreNodeState(mRoot, openNodes);
    } else {
      LOG.error("Unable to restore tree state", "treeState=" + state, "treeView=" + mFileTreeView);
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
              updateChevron(node);
              expandNode(node);
              restoreNodeState(node, openNodes);
            });
      }
    }
  }

  public interface FileActionListener {
    CodeEditorView openFile(File file);

    void showFileOptions(File file, TreeNode node);
  }
}
