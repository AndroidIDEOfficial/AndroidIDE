package com.itsaky.androidide.shell;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellServer extends Thread {
    
    private final Callback callback;
    private BufferedReader output;
    private Process process;
    
    public ShellServer(Callback callback, String command, String dirPath, Map<String, String> env, boolean redirectErrors) {
        this.callback = callback;
        
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{command});
        processBuilder.directory(new File(dirPath));
        processBuilder.redirectErrorStream(redirectErrors);
        processBuilder.environment().putAll(env);
        
        try {
            this.process = processBuilder.start();
            this.output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (Throwable th) {
            if (callback != null) {
                String out = getFullStackTrace(th).concat("\n");
                callback.output(out);
            }
        }
    }
    
    public InputStream getProcessInputStream() {
        return process.getInputStream();
    }
    
    public OutputStream getProcessOutputStream() {
        return process.getOutputStream();
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
		} catch (Throwable e) {
            // Ignored
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
        if (this.callback != null) {
            this.callback.output(str);
        }
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
        closeIOQuietly(new Closeable[]{this.output, this.process.getInputStream(), this.process.getErrorStream(), this.process.getOutputStream()});
        this.process.destroy();
    }
    
    private String getFullStackTrace (Throwable th) {
        StringWriter sw = new StringWriter ();
        th.printStackTrace( new PrintWriter (sw));
        return sw.toString();
    }
    
    private void closeIOQuietly (Closeable ... toClose) {
        for (Closeable c : toClose) {
            try {
                c.close();
            } catch (IOException e) {
                // ignored
            }
        }
    }
	
	public interface Callback {
		public void output(CharSequence charSequence);
	}
}
