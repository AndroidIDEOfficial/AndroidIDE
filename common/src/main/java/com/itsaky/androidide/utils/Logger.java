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
import com.blankj.utilcode.util.ThrowableUtils;

public class Logger {
    
    private static Logger instance;
    private static String TAG = "AndroidIDE";
    
    public static Logger instance() {
        return instance == null ? instance = createInstance(TAG) : instance;
    }

    private static Logger createInstance(String tag) {
        return new Logger(tag);
    }

    public static Logger instance(String tag) {
        return createInstance(tag);
    }
    
    private Logger(String tag) {
        TAG = tag;
    }

    public Logger warn(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            Log.w(TAG, "null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        Log.w(TAG, sb.toString());
        return this;
    }
    
    public Logger debug(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            Log.d(TAG, "null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        Log.d(TAG, sb.toString());
        return this;
    }
    
    public Logger error(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            Log.e(TAG, "null");
            return this;
        }
        for(Object msg : messages) {
            if(msg instanceof Throwable) {
                sb.append(ThrowableUtils.getFullStackTrace((Throwable) msg));
            } else sb.append(msg);
            sb.append("\n");
        }
        Log.e(TAG, sb.toString());
        return this;
    }
    
    public Logger verbose(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            Log.v(TAG, "null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        Log.v(TAG, sb.toString());
        return this;
    }
    
    public Logger info(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            Log.i(TAG, "null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        Log.i(TAG, sb.toString());
        return this;
    }
}
