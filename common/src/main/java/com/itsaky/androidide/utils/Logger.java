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
package com.itsaky.androidide.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ThrowableUtils;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Logger for the IDE.
 *
 * If a {@link Throwable} is passed to any of the logging methods, whole stack trace of the throwable
 * is printed. Any modifications to this class will affect every log message in the IDE.
 *
 * @author Akash Yadav
 */
public class Logger {
    
    private String TAG;
    
    private static Logger instance;
    private static final String MSG_SEPARATOR = " "; // Separate messages with a space.
    private static final List<LogListener> logListeners = new ArrayList<> ();
    
    public static final int DEBUG = 0;
    public static final int WARNING = 0;
    public static final int ERROR = 0;
    public static final int INFO = 0;
    public static final int VERBOSE = 0;
    
    public static Logger instance() {
        return instance == null ? instance = createInstance("AndroidIDE") : instance;
    }
    
    public static void addLogListener (LogListener listener) {
        logListeners.add (Objects.requireNonNull (listener));
    }
    
    public static void removeLogListener (LogListener listener) {
        logListeners.remove (Objects.requireNonNull (listener));
    }

    @NonNull
    @Contract("_ -> new")
    private static Logger createInstance(String tag) {
        return new Logger(tag);
    }

    @NonNull
    @Contract("_ -> new")
    public static Logger instance(String tag) {
        return createInstance(tag);
    }
    
    private Logger(String tag) {
        TAG = tag;
    }

    public Logger warn(Object... messages) {
        final var msg = generateMessage (messages);
        Log.w(TAG, msg);
        notifyListener (WARNING,msg);
        return this;
    }
    
    public Logger debug(Object... messages) {
        final var msg = generateMessage (messages);
        Log.d(TAG, msg);
        notifyListener (DEBUG,msg);
        return this;
    }
    
    public Logger error(Object... messages) {
        final var msg = generateMessage (messages);
        Log.e(TAG, msg);
        notifyListener (ERROR,msg);
        return this;
    }
    
    public Logger verbose(Object... messages) {
        final var msg = generateMessage (messages);
        Log.v(TAG, msg);
        notifyListener (VERBOSE,msg);
        return this;
    }
    
    public Logger info(Object... messages) {
        final var msg = generateMessage (messages);
        Log.i(TAG, msg);
        notifyListener (INFO,msg);
        return this;
    }

    @NonNull
    protected String generateMessage (Object... messages) {
        StringBuilder sb = new StringBuilder();
        if(messages == null) {
            return "null";
        }

        for(Object msg : messages) {
            sb.append(msg instanceof Throwable ?
                    ThrowableUtils.getFullStackTrace(((Throwable) msg)) :
                    msg);
            sb.append(MSG_SEPARATOR);
        }
        
        return sb.toString();
    }
    
    private void notifyListener (int priority, String msg) {
        for (final var listener : logListeners) {
            listener.log (priority, TAG, msg);
        }
    }
    
    /**
     * A listener which can be used to listen to log events.
     */
    public interface LogListener {
        void log (int priority, String tag, String message);
    }
}
