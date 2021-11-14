package com.itsaky.androidide.ui.util;

import com.itsaky.androidide.ui.inflater.InflateException;

public class Preconditions {
    
    public static void assertNotnull (Object obj, String msg) throws InflateException {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
    }
    
    public static void assertAllNotNull (String msg, Object ... objs) {
        int i = 0;
        for (Object obj : objs) {
            if (obj == null) {
                throw new NullPointerException ("[" + i + "] " + msg);
            }
            ++i;
        }
    }
    
    public static void assertNotBlank (String str, String msg) throws InflateException {
        Preconditions.assertNotnull(str, msg);
        if(str.trim().length() <= 0) {
            throw new InflateException (msg);
        }
    }
}
