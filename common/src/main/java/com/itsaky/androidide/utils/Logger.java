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

/**
 * Logger for the IDE.
 *
 * If a {@link Throwable} is passed to any of the logging methods, whole stack trace of the throwable
 * is printed. Any modifications to this class will affect every log message in the IDE.
 *
 * @author Akash Yadav
 */
public class Logger {
    
    private static Logger instance;
    private static String TAG = "AndroidIDE";
    private static final String MSG_SEPARATOR = " "; // Separate messages with a space.
    
    public static Logger instance() {
        return instance == null ? instance = createInstance(TAG) : instance;
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
        Log.w(TAG, generateMessage(messages));
        return this;
    }
    
    public Logger debug(Object... messages) {
        Log.d(TAG, generateMessage(messages));
        return this;
    }
    
    public Logger error(Object... messages) {
        Log.e(TAG, generateMessage(messages));
        return this;
    }
    
    public Logger verbose(Object... messages) {
        Log.v(TAG, generateMessage(messages));
        return this;
    }
    
    public Logger info(Object... messages) {
        Log.i(TAG, generateMessage(messages));
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
}
