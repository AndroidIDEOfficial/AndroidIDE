package com.itsaky.androidide.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import com.blankj.utilcode.util.FileIOUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutEditorFileTreeBinding;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.FileTreeCallable;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import com.blankj.utilcode.util.FileUtils;

public class FileTreeFragment extends BottomSheetDialogFragment implements TreeNode.TreeNodeClickListener, TreeNode.TreeNodeLongClickListener {
	
	private LayoutEditorFileTreeBinding binding;
	private AndroidTreeView mFileTreeView;
	private FileActionListener mFileActionListener;
	private AndroidProject mProject;
	private TreeNode mRoot;
	
	public FileTreeFragment() {
	}

	public FileTreeFragment setFileActionListener(FileActionListener listener) {
		this.mFileActionListener = listener;
		return this;
	}

	public static FileTreeFragment newInstance(AndroidProject project) {
		Bundle bundle = new Bundle();
		bundle.putParcelable("project", project);
		FileTreeFragment frag = new FileTreeFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = LayoutEditorFileTreeBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() == null || getArguments() == null || !getArguments().containsKey("project")) return;
		mProject = getArguments().getParcelable("project");
        
		listProjectFiles();
	}
	
	@Override
	public void onClick(TreeNode node, Object p2) {
		final File f = (File) p2;
		if(f.isFile()) {
			if (mFileActionListener != null && FileUtils.isUtf8(f)) {
				mFileActionListener.openFile(f);
			}
		} else if(f.isDirectory() && f.exists()) {
			if(node.isExpanded()) {
				collapseNode(node);
			} else if(f.getAbsolutePath().equals(Environment.GRADLE_USER_HOME.getAbsolutePath()) && !node.isExpanded()) {
				expandNode(node);
			} else {
				setLoading(node);
				listNode(node, true);
			}
		}
	}
	
	private void listNode(TreeNode node, boolean wasClicked) {
		node.getChildren().clear();
		node.setExpanded(false);
		new TaskExecutor().executeAsync(() -> {
			getNodeFromFiles(node.getValue().listFiles(/* new FileTreeCallable.HiddenFilesFilter() */), node);
			TreeNode temp = node;
			while(temp.size() == 1) {
				temp = temp.childAt(0);
				if(!temp.getValue().isDirectory()) break;
				getNodeFromFiles(temp.getValue().listFiles(/* new FileTreeCallable.HiddenFilesFilter() */), temp);
				temp.setExpanded(true);
			}
			return null;
		}, __ -> {
			updateChevron(node);
			expandNode(node);
		});
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
	
	private void setLoading(TreeNode node) {
		if (node.getViewHolder() instanceof FileTreeViewHolder) {
			((FileTreeViewHolder) node.getViewHolder()).setLoading();
		}
	}
	
	private void updateChevron(TreeNode node) {
		if (node.getViewHolder() instanceof FileTreeViewHolder) {
			((FileTreeViewHolder) node.getViewHolder()).updateChevron(!node.isExpanded());
		}
	}

	@Override
	public boolean onLongClick(TreeNode node, Object value) {
		if(mFileActionListener != null)
			mFileActionListener.showFileOptions((File) value, node);
		return true;
	}
	
	public void expandNode(TreeNode node) {
		if(mFileTreeView == null) {
			return;
		}
		
		TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
		mFileTreeView.expandNode(node);
	}
	
	public void collapseNode(TreeNode node) {
		if(mFileTreeView == null) {
			return;
		}
		
		TransitionManager.beginDelayedTransition(binding.getRoot(), new ChangeBounds());
		mFileTreeView.collapseNode(node);
	}
	

	public void listProjectFiles() {
		if (mProject == null)
			return;
		
		final File gradleProps = Environment.GRADLE_PROPS;
		final File gradleHome = Environment.GRADLE_USER_HOME;
        final File root = StudioApp.getInstance().getRootDir().getParentFile();
        File projectDir = new File(mProject.getProjectPath());
//        projectDir = root;
		mRoot = TreeNode.root(projectDir);
		if(gradleHome.exists() && gradleHome.isDirectory()) {
			if(!gradleProps.exists())
				FileIOUtils.writeFileFromString(gradleProps, Environment.SAMPLE_GRADLE_PROP_CONTENTS);
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
		new TaskExecutor().executeAsync(new FileTreeCallable(getContext(), mRoot, projectDir), (result) -> {
			getScrollView().setVisibility(View.VISIBLE);
			getLoadingProgress().setVisibility(View.GONE);
			AndroidTreeView tree = createTreeView(mRoot);
			if(tree != null) {
				tree.setUseAutoToggle(false);
				tree.setDefaultNodeClickListener(FileTreeFragment.this);
				tree.setDefaultNodeLongClickListener(FileTreeFragment.this);
				getScrollView().removeAllViews();
				getScrollView().addView(tree.getView());
			}
		});
	}

	public AndroidTreeView createTreeView(TreeNode node) {
		Context ctx = null;
		if(getActivity() != null)
			ctx = getActivity();
		else if(getContext() != null) {
			ctx = getContext();
		}
		
		if(ctx == null)
			return null;
		
		return mFileTreeView = new AndroidTreeView(ctx, node, R.drawable.bg_ripple);
	}

	public HorizontalScrollView getScrollView() {
		return binding.filetreeHorizontalScrollView;
	}

	public ProgressBar getLoadingProgress() {
		return binding.fileTreeLoadingProgress;
	}

	public interface FileActionListener {
		public EditorFragment openFile(File file);
		public void showFileOptions(File file, TreeNode node);
	}
}
