package com.itsaky.androidide.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.itsaky.androidide.models.LogLine;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReceiver extends BroadcastReceiver {
	
	private LogListener listener;
	
	public static final String APPEND_LOG = "com.itsaky.androidide.logs.APPEND_LOG";
	public static final String EXTRA_LINE = "log_line";
    
	public LogReceiver setLogListener(LogListener listener) {
		this.listener = listener;
		return this;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(APPEND_LOG) && intent.hasExtra(EXTRA_LINE)) {
			String line = intent.getStringExtra(EXTRA_LINE);
			if(line == null) return;
			try {
				sendLogLine(line);
			} catch (Throwable th) {}
		}
	}

	private void sendLogLine(String line) {
		final String[] split = line.split("\\s", 7);
        final LogLine log = new LogLine(
            split[0], // date
            split[1], // time
            split[2], // process id
            split[3], // thread id
            split[4], // priority
            split[5], // tag
            split[6]  // message
        );
        
        if(listener != null) {
            listener.appendLogLine(log);
        }
	}
	
	public static interface LogListener {
		public void appendLogLine(LogLine line);
	}
}
