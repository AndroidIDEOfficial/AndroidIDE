package com.itsaky.lemminx.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtils {
    
    public static Logger newLogger(String name) {
		Logger logger = Logger.getLogger(name);
		try {
            final FileHandler handler = new FileHandler("/sdcard/ide_xlog/java_logger.txt");
            handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return logger;
	}
    
}
