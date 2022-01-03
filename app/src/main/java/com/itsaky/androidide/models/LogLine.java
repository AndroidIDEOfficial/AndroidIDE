/************************************************************************************
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.R;
import java.util.Locale;
import io.github.rosemoe.editor.widget.EditorColorScheme;

public class LogLine {
	
	public String unformatted;
	
	public String date;
	public String time;
	public String pid;
	public String tid;
	public String priorityChar;
	public String tag;
	public String message;
	
	public int priority;
	
	public boolean formatted;
	
	// It makes things easier in LogLanguageImpl
	public static final int INFO = EditorColorScheme.LOG_INFO;
	public static final int DEBUG = EditorColorScheme.LOG_DEBUG;
	public static final int ERROR = EditorColorScheme.LOG_ERROR;
	public static final int WARNING = EditorColorScheme.LOG_WARNING;
	
	public LogLine(@NonNull LogLine src ) {
		this(src.date, src.time, src.pid, src.tid, src.priorityChar, src.tag, src.message);
	}
	
	public LogLine (String date, String time, String pid, String tid, String priorityChar, String tag, String message) {
		this (date, time, pid, tid, priorityChar, tag, message, true);
	}
	
	public LogLine (String unformatted) {
		this.unformatted = unformatted;
		this.formatted = false;
	}
	
	public LogLine(String date, String time, String pid, String tid, String priorityChar, @NonNull String tag, String message, boolean formatted) {
		this.date = date;
		this.time = time;
		this.pid = pid;
		this.tid = tid;
		this.priorityChar = priorityChar;
		this.tag = tag;
		this.message = message;
		this.priority = parsePriority(priorityChar);
		this.formatted = formatted;
        
        if(tag.length() > 25) {
            this.tag = "...".concat(tag.substring(tag.length() - 25));
        }
	}
	
	@NonNull
	public static LogLine forLogString (@NonNull final String log) {
		try {
			final var split = log.split ("\\s");
			return new LogLine(
					split[0], // date
					split[1], // time
					split[2], // process id
					split[3], // thread id
					split[4], // priority
					split[5], // tag
					split[6]  // message
			);
		} catch (Throwable th) {
			return new LogLine (log);
		}
	}
	
	private int parsePriority(String s) {
		if(s == null || s.trim().length() > 1) {
			return DEBUG;
		}
		char c = s.toLowerCase(Locale.US).charAt(0);
		if(c == 'w') {
			return WARNING;
		} else if(c == 'e') {
			return ERROR;
		} else if(c == 'i') {
			return INFO;
		} else {
			return DEBUG;
		}
	}
	
	public int getColor(Context ctx) {
		int id = R.color.log_normal;
		if(priority == WARNING) {
			id = R.color.log_warning;
		} else if(priority == ERROR) {
			id = R.color.log_error;
		} else if(priority == INFO) {
			id = R.color.log_info;
		}
		return ContextCompat.getColor(ctx, id);
	}

	@Override
	public String toString() {
		return this.formatted ? String.format("%-6s %-13s %-6s %-6s %-2s %-40s %s", date, time, pid, tid, priorityChar, tag, message) : this.unformatted;
	}
}
