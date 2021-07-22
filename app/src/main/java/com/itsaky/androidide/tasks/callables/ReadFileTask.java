package com.itsaky.androidide.tasks.callables;

import com.blankj.utilcode.util.FileIOUtils;
import java.io.File;
import java.util.concurrent.Callable;

public class ReadFileTask implements Callable<String> {
	
	private final File file;

	public ReadFileTask(File file) {
		this.file = file;
	}
	
	@Override
	public String call() throws Exception {
		return FileIOUtils.readFile2String(file);
	}
}
