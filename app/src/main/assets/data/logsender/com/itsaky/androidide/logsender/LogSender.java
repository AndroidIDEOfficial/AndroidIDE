package com.itsaky.androidide.logsender;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.content.Context;

public class LogSender extends Thread {
	
	private static LogSender instance;
    private BufferedReader output;
    private Process process;
	private Context ctx;
	
	private static final String PACKAGE_NAME = "com.itsaky.androidide";
	private static final String APPEND_LOG = "com.itsaky.androidide.logs.APPEND_LOG";
	private static final String EXTRA_LINE = "log_line";
	
	private Callback CALLBACK = new Callback() {

		@Override
		public void output(CharSequence c) {
			if(c != null && c.toString().trim().length() > 0) {
				Intent i = new Intent();
				i.setAction(APPEND_LOG);
				i.putExtra(EXTRA_LINE, c.toString());
				i.setPackage(PACKAGE_NAME);
				ctx.sendBroadcast(i);
			}
		}
	};
	
    private LogSender(Context ctx) {
		this.ctx = ctx;
        final String command = "sh";
		final String dirPath = ctx.getFilesDir().getAbsolutePath();
        ProcessBuilder processBuilder = new ProcessBuilder(new String[]{command});
        processBuilder.directory(new File(dirPath));
        processBuilder.redirectErrorStream(true);
        try {
            this.process = processBuilder.start();
            this.output = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
			final String str = "logcat -v threadtime";
			this.process.getOutputStream().write(str.concat("\n").getBytes());
			this.process.getOutputStream().flush();
        } catch (Throwable th) {
        }
    }
	
	public static void startLogging(Context ctx) {
		instance = instance == null ? instance = new LogSender(ctx) : instance;
		instance.start();
	}
	
    @Override
    public void run() {
        while (true) {
            try {
                String readLine = this.output.readLine();
                if (readLine == null) break;
                CALLBACK.output(readLine.concat("\n"));
            } catch (Throwable th) {}
        }
        try {
            this.output.close();
            this.process.getInputStream().close();
            this.process.getErrorStream().close();
            this.process.getOutputStream().close();
            this.process.destroy();
        } catch (Throwable th) {}
    }
	
	public interface Callback {
		public void output(CharSequence charSequence);
	}
}
