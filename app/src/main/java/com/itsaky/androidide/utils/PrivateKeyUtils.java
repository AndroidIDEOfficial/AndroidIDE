package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileUtils;
import java.io.File;
import java.io.IOException;
import com.itsaky.androidide.utils.Environment;

public class PrivateKeyUtils {

	private PrivateKeyUtils() {
	}

	public static File getSshKeyFolder() {
		
                String sshKey = Environment.DEFAULT_HOME.getAbsolutePath() + "/.ssh";
		File getKey = new File(sshKey);
		return getKey;
	}

	public static void copySshKey() {

		String oldPath = "/storage/emulated/0/.ssh";
		File old_path = new File(oldPath);
		if (old_path.exists()) {
			FileUtils.copy(old_path, getSshKeyFolder());
		}
	}
}
