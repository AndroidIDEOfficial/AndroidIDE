/*
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
 */
package com.itsaky.androidide.models;

import android.content.Context;

import java.util.Locale;

public class LogLine {

    // It makes things easier in LogLanguageImpl
    public static final int INFO;
    public static final int DEBUG;
    public static final int ERROR;
    public static final int WARNING;

    static {
        try {
            final var klass =
                    Class.forName("com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE");
            INFO = klass.getField("LOG_INFO").getInt(null);
            DEBUG = klass.getField("LOG_DEBUG").getInt(null);
            ERROR = klass.getField("LOG_ERROR").getInt(null);
            WARNING = klass.getField("LOG_WARNING").getInt(null);
        } catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }

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

    public LogLine(String priorityChar, String tag, String message) {
        this("", "", "", "", priorityChar, tag, message);
    }

    public LogLine(
            String date,
            String time,
            String pid,
            String tid,
            String priorityChar,
            String tag,
            String message) {
        this(date, time, pid, tid, priorityChar, tag, message, true);
    }

    public LogLine(
            String date,
            String time,
            String pid,
            String tid,
            String priorityChar,
            String tag,
            String message,
            boolean formatted) {
        this.date = date;
        this.time = time;
        this.pid = pid;
        this.tid = tid;
        this.priorityChar = priorityChar;
        this.tag = tag;
        this.message = message;
        this.priority = parsePriority(priorityChar);
        this.formatted = formatted;

        if (tag.length() > 25) {
            this.tag = "...".concat(tag.substring(tag.length() - 25));
        }
    }

    private int parsePriority(String s) {
        if (s == null || s.trim().length() > 1) {
            return DEBUG;
        }
        char c = s.toLowerCase(Locale.US).charAt(0);
        if (c == 'w') {
            return WARNING;
        } else if (c == 'e') {
            return ERROR;
        } else if (c == 'i') {
            return INFO;
        } else {
            return DEBUG;
        }
    }

    public LogLine(String unformatted) {
        this.unformatted = unformatted;
        this.formatted = false;
    }

    public static LogLine forLogString(final String log) {
        try {
            final var split = log.split("\\s", 7);
            return new LogLine(
                    split[0], // date
                    split[1], // time
                    split[2], // process id
                    split[3], // thread id
                    split[4], // priority
                    split[5], // tag
                    split[6] // message
                    );
        } catch (Throwable th) {
            return new LogLine(log);
        }
    }

    public int getColor(Context ctx) {
        int id = 0xffffffff;
        if (priority == WARNING) {
            id = 0xffff7043;
        } else if (priority == ERROR) {
            id = 0xffc50e29;
        } else if (priority == INFO) {
            id = 0xff4caf50;
        }
        return id;
    }

    @Override
    public String toString() {
        return this.formatted
                ? String.format(
                        "%-6s %-13s %-6s %-6s %-2s %-35s %s",
                        date, time, pid, tid, priorityChar, trimIfNeeded(tag, 35), message)
                : this.unformatted;
    }

    private String trimIfNeeded(String tag, int maxLength) {
        final var sb = new StringBuilder(tag);
        final var length = tag.length();
        if (length > maxLength) {
            final var start = length - maxLength;
            sb.delete(0, start);

            // When the tag is long enough, prefix the tag with '..'
            sb.setCharAt(0, '.');
            sb.setCharAt(1, '.');
        }

        return sb.toString();
    }

    public String toSimpleString() {
        return this.formatted
                ? String.format("%-25s %-2s %s", trimIfNeeded(tag, 25), priorityChar, message)
                : this.unformatted;
    }
}
