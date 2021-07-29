package com.itsaky.androidide.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.utils.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReceiver extends BroadcastReceiver {
	
	private LogListener listener;
	
	public static final String APPEND_LOG = "com.itsaky.androidide.logs.APPEND_LOG";
	public static final String EXTRA_LINE = "log_line";
	
	private LogLine logTag = null;
	
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
		Matcher m = LOG_HEAD_PATTERN.matcher(line);
		LogLine log = null;
		final boolean find = line != null && line.trim().length() > 2 /*if length() == 2 then only [] is available*/ && m.find();
		Logger.instance().i("line -> " + line + "\nlogTag find() -> " + find);
		if(find) {
			String date = "";
			String time = "";
			String pid = "";
			String tid = "";
			String priority = "";
			String tag = "";
			m = DATE_MATCHER.matcher(line);
			if(m.find()) 
				date = line.substring(m.start(), m.end());
			
			m = TIME_MATCHER.matcher(line);
			if(m.find()) 
				time = line.substring(m.start(), m.end());
				
			m = PIDTID_MATCHER.matcher(line);
			if(m.find()) {
				String[] s = line.substring(m.start(), m.end()).split(":");
				if(s.length > 0)
					pid = s[0];
				if(s.length > 1)
					tid = s[1];
			}
			
			m = PRIORITYTAG_MATCHER.matcher(line);
			if(m.find()) {
				String[] s = line.substring(m.start(), m.end()).split("/", 2);
				if(s.length > 0)
					priority = s[0];
				if(s.length > 1)
					tag = s[1];
				if(tag.trim().endsWith("]"))
					tag = tag.substring(0, tag.lastIndexOf("]")).trim();
			}
			
			logTag = new LogLine(date, time, pid, tid, priority, tag);
			return;
		}
		
		if(logTag != null) {
			log = new LogLine(logTag);
			log.message = line;
		} else {
			log = new LogLine("", "", "", "", "", "", line);
		}
		
		Logger.instance().i("log = " + log + "\nlogListener = " + listener);
		if(log != null && listener != null) {
			Logger.instance().i("listener.appendLogLine(...) log ->\n" + log);
			listener.appendLogLine(log);
			logTag = null;
		}
	}
	
	public static interface LogListener {
		public void appendLogLine(LogLine line);
	}
	
	public static final Pattern DATE_MATCHER = Pattern.compile("\\d{2}-\\d{2}");
	public static final Pattern TIME_MATCHER = Pattern.compile("\\d{2}:\\d{2}:\\d{2}\\.\\d{2,3}");
	public static final Pattern PIDTID_MATCHER = Pattern.compile("\\d{4,5}\\s*:\\s*\\d{4,5}");
	public static final Pattern PRIORITYTAG_MATCHER = Pattern.compile("[A-Z]\\/.*");
	public static final Pattern LOG_HEAD_PATTERN = Pattern.compile("\\[\\s*\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d{2,3}\\s+\\d{4,5}\\s*:\\s*\\d{4,5}\\s+\\w/.*\\]");
}
