package com.itsaky.androidide.shell;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.utils.Environment;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShellServer extends Thread {
    private final List<Callback> callbacks = Collections.synchronizedList(new ArrayList());
    private BufferedReader output;
    private Process process;

    public ShellServer(Callback callback, String command, String dirPath) {
        addCallback(callback);
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{command});
        processBuilder.directory(new File(dirPath));
		processBuilder.redirectErrorStream(true);
        try {
            this.process = processBuilder.start();
            this.output = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("export HOME=" + Environment.path(Environment.HOME) + " && cd");
            stringBuilder.append("\nexport TMPDIR=" + Environment.path(Environment.mkdirIfNotExits(Environment.TMP_DIR)));
            stringBuilder.append("\nexport JAVA_HOME=" + Environment.path(Environment.JAVA_HOME));
            stringBuilder.append("\nexport ANDROID_HOME=" + Environment.path(Environment.ANDROID_HOME));
            stringBuilder.append("\nexport GRADLE_USER_HOME=$HOME/.gradle");
            stringBuilder.append("\nexport PATH=$PATH:$HOME/bin");
            stringBuilder.append("\nexport PATH=$PATH:" + Environment.path(Environment.JAVA_HOME) + "/bin");
            stringBuilder.append("\nexport PATH=$PATH:" + Environment.path(Environment.GRADLE_DIR) + "/bin");
            stringBuilder.append("\nexport PATH=$PATH:" + Environment.path(Environment.ANDROID_HOME) + "/cmdline-tools/latest/bin");
            stringBuilder.append("\nexport PATH=$PATH:" + Environment.path(Environment.ANDROID_HOME) + "/cmake/bin");
            stringBuilder.append("\nexport PATH=$PATH:" + Environment.path(Environment.BINDIR));
            stringBuilder.append("\nexport JLS_HOME=" + Environment.path(Environment.JLS_HOME));
            
            // Provide paths to .so files needed to execute JDK
            final String lib = Environment.LIBDIR.getAbsolutePath();
            stringBuilder.append("\nexport LD_LIBRARY_PATH=" + lib);
            stringBuilder.append(System.getenv().containsKey("LD_LIBRARY_PATH") ? ":" + System.getenv().get("LD_LIBRARY_PATH") : "");
            
            if(android.os.Build.VERSION.SDK_INT >= 30) {
                // Hook calls to malloc() in native binaries
                // Required only in Android 11 and up
                stringBuilder.append("\nexport LD_PRELOAD=" + Environment.LIBHOOKSO.getAbsolutePath());
            }
            
            // Monotonic clock fix, needed for JDK
            stringBuilder.append("\nln -sf /apex/com.android.runtime/lib64/bionic/libc.so " + lib + "/librt.so && " +
                   "ln -sf /apex/com.android.runtime/lib64/bionic/libc.so " + lib + "/libpthread.so");
             
            // Execute all commands...
            append(stringBuilder.toString(), false);
            
            // Make sure there are proper permissions set to the $HOME directory
            append("chmod -R 777 $HOME", false);
        } catch (Throwable th) {
            if (callback != null) {
                String out = ThrowableUtils.getFullStackTrace(th).concat("\n");
                callback.output(out);
            }
        }
    }

    public ShellServer addCallback(Callback callback) {
        if (callback != null) {
            this.callbacks.add(callback);
        }
        return this;
    }

    public void append(String str) {
        append(str, true);
    }

	public ShellServer bgAppend(String str) {
		append(str, false);
		return this;
	}

	public ShellServer bgAppend(String... arr) {
		for (String s : arr)
			bgAppend(s);
		return this;
	}

    public void append(String str, boolean z) {
        if (z) {
            try {
                if (str.endsWith("\n")) {
                    output(str);
                } else {
                    output(str.concat("\n"));
                }
            } catch (Throwable th) {
                output(th.toString().concat("\n"));
                return;
            }
        }
        try {
			this.process.getOutputStream().write(str.concat("\n").getBytes());
			this.process.getOutputStream().flush();
		} catch (IOException e) {

		}
    }

    public void append(String... strArr) {
        for (String append : strArr) {
            append(append);
        }
    }

    public void exit() {
        append("exit");
    }

    public void output(String str) {
        int i = 0;
        if (this.callbacks != null && this.callbacks.size() != 0) {
            Callback[] callbackArr = this.callbacks.toArray(new Callback[0]);
            while (i < callbackArr.length) {
                callbackArr[i].output(str);
                i++;
            }
        }
    }

    public ShellServer removeCallback(Callback callback) {
        if (callback != null) {
            this.callbacks.remove(callback);
        }
        return this;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String readLine = this.output.readLine();
                if (readLine == null) {
                    break;
                }
                output(readLine.concat("\n"));
            } catch (Throwable th) {
                output(th.toString().concat("\n"));
            }
        }
        CloseUtils.closeIOQuietly(new Closeable[]{this.output, this.process.getInputStream(), this.process.getErrorStream(), this.process.getOutputStream()});
        this.process.destroy();
    }
	
	public interface Callback {
		public void output(CharSequence charSequence);
	}
}
