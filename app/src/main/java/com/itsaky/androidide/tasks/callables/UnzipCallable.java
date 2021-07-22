package com.itsaky.androidide.tasks.callables;

import com.blankj.utilcode.util.ZipUtils;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public final class UnzipCallable implements Callable<List<File>> {

	private File src;
	private File dest;

	public UnzipCallable(File src, File dest) {
		this.src = src;
		this.dest = dest;
	}

	@Override
	public List<File> call() throws Exception {
		return ZipUtils.unzipFile(src, dest);
	}
}
