package com.itsaky.androidide.utils;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.LevelFileNameGenerator;

public class Logger {
    
    private static Logger instance;
    private static com.elvishew.xlog.Logger xLogger;
    private static String TAG = "AndroidIDE";
    
    public static Logger instance() {
        return instance == null ? createInstance(TAG) : instance;
    }
    
    public static Logger instance(String tag) {
        return instance == null ? createInstance(tag) : instance;
    }
    
    private Logger(String tag) {
        this.TAG = tag;
    }
    
    private static Logger createInstance(String tag) {
        LogConfiguration config = new LogConfiguration.Builder()
            .logLevel(com.itsaky.androidide.BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE)
            .tag("AndroidIDE")                                         
            .enableThreadInfo()                                    
            .enableStackTrace(10)                                  
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
    
    public Logger d(Object... messages) {
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
    
    public Logger e(Object... messages) {
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
            sb.append(msg);
            sb.append("\n");
        }
        xLogger.e(sb.toString());
        return this;
    }
    
    public Logger v(Object... messages) {
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
    
    public Logger i(Object... messages) {
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
