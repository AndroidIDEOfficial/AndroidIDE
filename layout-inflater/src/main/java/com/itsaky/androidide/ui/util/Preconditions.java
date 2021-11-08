package com.itsaky.androidide.ui.util;

import com.itsaky.androidide.ui.inflater.InflateException;

public class Preconditions {
    
    public static void assertNotnull (Object obj, String msg) throws InflateException {
        if (obj == null) {
            throw new InflateException(msg);
        }
    }
    
    public static void assertNotBlank (String str, String msg) throws InflateException {
        Preconditions.assertNotnull(str, msg);
        if(str.trim().length() <= 0) {
            throw new InflateException (msg);
        }
    }
}
