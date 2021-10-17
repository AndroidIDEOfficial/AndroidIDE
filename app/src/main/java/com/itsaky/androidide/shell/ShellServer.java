package com.itsaky.androidide.shell;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellServer extends Thread {
    
    private final List<Callback> callbacks = Collections.synchronizedList(new ArrayList<Callback>());
    private BufferedReader output;
    private Process process;
    
    private static final Logger LOG = Logger.instance("ShellServer");
    
    public ShellServer(Callback callback, String command, String dirPath) {
        this(callback, command, dirPath, true);
    }
    
    public ShellServer(Callback callback, String command, String dirPath, boolean redirectErrors) {
        addCallback(callback);
        
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{command});
        processBuilder.directory(new File(dirPath));
        processBuilder.redirectErrorStream(redirectErrors);
        
        if(!redirectErrors)
            processBuilder.redirectError(new File("/sdcard/ide_xlog/process_error.txt"));
            
        processBuilder.environment().putAll(Environment.getEnvironment(false));
        try {
            this.process = processBuilder.start();
            this.output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (Throwable th) {
            LOG.error(ThrowableUtils.getFullStackTrace(th));
            if (callback != null) {
                String out = ThrowableUtils.getFullStackTrace(th).concat("\n");
                callback.output(out);
            }
        }
    }
    
    public int getPid() {
        int pid = -1;
        try {
            Field f = process.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            pid = f.getInt(process);
            f.setAccessible(false);
        } catch (Throwable ignored) {
            try {
                Matcher m = Pattern.compile("pid=(\\d+)").matcher(process.toString());
                pid = m.find() ? Integer.parseInt(m.group(1)) : -1;
            } catch (Throwable ignored2) {
                pid = -1;
            }
        }
        return pid;
    }
    
    public InputStream getProcessInputStream() {
        return process.getInputStream();
    }
    
    public OutputStream getProcessOutputStream() {
        return process.getOutputStream();
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
            LOG.error(ThrowableUtils.getFullStackTrace(e));
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
    
    class ErrorReader implements Runnable {
        
        private final InputStream error;

        public ErrorReader(InputStream error) {
            this.error = error;
        }
        
        @Override
        public void run() {
            try {
                final BufferedReader r = new BufferedReader(new InputStreamReader(error));
                String line = "";
                while((line = r.readLine()) != null) {
                    LOG.info("Error stream", line);
                }
            } catch (Throwable th) {
                LOG.error("Cannot read error stream");
            }
        }
    }
}
