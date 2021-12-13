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
import androidx.core.content.ContextCompat;
import com.itsaky.androidide.R;
import java.util.Locale;
import io.github.rosemoe.editor.widget.EditorColorScheme;

public class LogLine {
	
	public String date;
	public String time;
	public String pid;
	public String tid;
	public String priorityChar;
	public String tag;
	public String message;
	
	public int priority;
	
	// It makes things easier in LogLanguageImpl
	public static final int INFO = EditorColorScheme.LOG_INFO;
	public static final int DEBUG = EditorColorScheme.LOG_DEBUG;
	public static final int ERROR = EditorColorScheme.LOG_ERROR;
	public static final int WARNING = EditorColorScheme.LOG_WARNING;
	
	public LogLine(LogLine src ) {
		this(src.date, src.time, src.pid, src.tid, src.priorityChar, src.tag, src.message);
	}
	
	public LogLine(String date, String time, String pid, String tid, String priorityChar, String tag) {
		this(date, time, pid, tid, priorityChar, tag, "");
	}

	public LogLine(String date, String time, String pid, String tid, String priorityChar, String tag, String message) {
		this.date = date;
		this.time = time;
		this.pid = pid;
		this.tid = tid;
		this.priorityChar = priorityChar;
		this.tag = tag;
		this.message = message;
		this.priority = parsePriority(priorityChar);
        
        if(tag.length() > 25) {
            this.tag = "...".concat(tag.substring(tag.length() - 25));
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
		return String.format("%-6s %-13s %-6s %-6s %-2s %-40s %s", date, time, pid, tid, priorityChar, tag, message);
	}
}
