package com.itsaky.androidide.tasks.callables;

import android.content.Context;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.unnamed.b.atv.model.TreeNode;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;

public class FileTreeCallable implements Callable<Boolean> {
	private final Context ctx;
	private TreeNode parent;
	private File file;

	public FileTreeCallable(Context ctx, TreeNode parent, File file) {
		this.ctx = ctx;
		this.parent = parent;
		this.file = file;
	}

	@Override
	public Boolean call() throws Exception {
		getNodeFromArray(file.listFiles(/*new HiddenFilesFilter()*/), parent);
		return true;
	}

	private void getNodeFromArray(File[] files, TreeNode parent) {
		Arrays.sort(files, new SortFileName());
		Arrays.sort(files, new SortFolder());
		for (File file : files) {
			TreeNode node = new TreeNode(file);
			node.setViewHolder(new FileTreeViewHolder(ctx));
			parent.addChild(node);
		}
	}

	public static class HiddenFilesFilter implements FileFilter {

		@Override
		public boolean accept(File p1) {
			return !p1.getName().startsWith(".");
		}
	}

	public static class SortFileName implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			return f1.getName().compareTo(f2.getName());
		}
	}

	public static class SortFolder implements Comparator<File> {
		@Override
		public int compare(File f1, File f2) {
			if (f1.isDirectory() == f2.isDirectory())
				return 0;
			else if (f1.isDirectory() && !f2.isDirectory())
				return -1;
			else
				return 1;
		}
	}
}
