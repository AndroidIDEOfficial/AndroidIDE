package com.itsaky.androidide.tasks.callables;

import android.text.TextUtils;
import com.blankj.utilcode.util.FileUtils;
import java.io.File;
import java.io.FileFilter;

public class ListDirectoryCallable implements java.util.concurrent.Callable<String> {
	
	private File file;

	public ListDirectoryCallable(File file) {
		this.file = file;
	}
	
	@Override
	public String call() throws Exception {
		return TextUtils.join("\n", FileUtils.listFilesInDirWithFilter(file, ARCHIVE_FILTER, false));
	}
	
	private final FileFilter ARCHIVE_FILTER = new FileFilter(){

		@Override
		public boolean accept(File p1) {
			return p1.isFile() && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));
		}
	};
}
