/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * {@link ILogger} implementation for the JVM.
 *
 * @author Akash Yadav
 */
public class JvmLogger extends ILogger {

    private static final Logger LOG = Logger.getLogger("main");

    static {
        setRootFormat();
    }

    protected JvmLogger(String tag) {
        super(tag);
    }

    private static void setRootFormat() {
        var root = Logger.getLogger("");

        for (var h : root.getHandlers()) {
            h.setFormatter(new LogFormat());
        }
    }

    @Override
    protected void doLog(int priority, String message) {
        switch (priority) {
            case ERROR:
                LOG.log(Level.SEVERE, message);
                break;
            case WARNING:
                LOG.log(Level.WARNING, message);
                break;
            case VERBOSE:
                LOG.log(Level.FINEST, message);
                break;
            case INFO:
                LOG.log(Level.INFO, message);
                break;
            case DEBUG:
            default:
                LOG.log(Level.FINE, message);
                break;
        }
    }

    private static class LogFormat extends Formatter {

        private final String format = "%1$tT.%1$tL\t%4$s\t%2$s\t%5$s%6$s%n";
        private final Date date = new Date();

        @Override
        public synchronized String format(LogRecord record) {
            date.setTime(record.getMillis());
            String source;
            if (record.getSourceClassName() != null) {
                source = last(record.getSourceClassName());
                if (record.getSourceMethodName() != null) {
                    source += " " + record.getSourceMethodName();
                }
            } else {
                source = record.getLoggerName();
            }
            var message = formatMessage(record);
            var throwable = "";
            if (record.getThrown() != null) {
                var sw = new StringWriter();
                var pw = new PrintWriter(sw);
                pw.println();
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            }
            return String.format(
                    Locale.ROOT,
                    format,
                    date,
                    source,
                    record.getLoggerName(),
                    record.getLevel().getLocalizedName(),
                    message,
                    throwable);
        }

        private String last(String className) {
            var dot = className.lastIndexOf('.');
            if (dot == -1) {
                return className;
            }
            return className.substring(dot + 1);
        }
    }
}
