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

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.LevelFileNameGenerator;
import com.blankj.utilcode.util.ThrowableUtils;

public class Logger {
    
    private static Logger instance;
    private static com.elvishew.xlog.Logger xLogger;
    private static String TAG = "AndroidIDE";
    
    public static Logger instance() {
        return instance == null ? createInstance(TAG) : instance;
    }
    
    public static Logger instance(String tag) {
        return createInstance(tag);
    }
    
    private Logger(String tag) {
        this.TAG = tag;
    }
    
    private static Logger createInstance(String tag) {
        LogConfiguration config = new LogConfiguration.Builder()
            .disableStackTrace()
            .disableThreadInfo()
            .logLevel(LogLevel.ALL)
            .tag("AndroidIDE") 
            .disableBorder()                                       
            .build();
        Printer filePrinter = new FilePrinter                      
            .Builder(FileUtil.getExternalStorageDir() + "/ide_xlog")                         
            .fileNameGenerator(new LevelFileNameGenerator())
            .cleanStrategy(f -> false)
            .build();
        XLog.init(config, filePrinter);
        xLogger = XLog.tag("AndroidIDE").build();
        
        instance = new Logger(tag);
        return instance;
    }
    
    public Logger warn(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            xLogger.w("null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        xLogger.w(sb.toString());
        return this;
    }
    
    public Logger debug(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            xLogger.d("null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        xLogger.d(sb.toString());
        return this;
    }
    
    public Logger error(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            xLogger.e("null");
            return this;
        }
        for(Object msg : messages) {
            if(msg != null && msg instanceof Throwable) {
                sb.append(ThrowableUtils.getFullStackTrace((Throwable) msg));
            } else sb.append(msg);
            sb.append("\n");
        }
        xLogger.e(sb.toString());
        return this;
    }
    
    public Logger verbose(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            xLogger.v("null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        xLogger.v(sb.toString());
        return this;
    }
    
    public Logger info(Object... messages) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(TAG);
        sb.append("]");
        sb.append("\n");
        if(messages == null) {
            xLogger.i("null");
            return this;
        }
        for(Object msg : messages) {
            sb.append(msg);
            sb.append("\n");
        }
        xLogger.i(sb.toString());
        return this;
    }
}
