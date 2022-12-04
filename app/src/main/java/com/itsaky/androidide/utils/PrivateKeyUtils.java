package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileUtils;
import java.io.File;
import java.io.IOException;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.preferences.internal.sshKeyPath;

public class PrivateKeyUtils {

	private PrivateKeyUtils() {
	}

	public static File getSshKeyFolder() {
		
                String sshKey = Environment.DEFAULT_HOME + "/.ssh";
		File getKey = new File(sshKey);
		return getKey;
	}

	public static void copySshKey() {

		String oldPath = SSH_KEY_PATH;
		File old_path = new File(oldPath);
		if (old_path.exists()) {
			FileUtils.copy(old_path, getSshKeyFolder());
		}
	}
}
