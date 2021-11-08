package com.itsaky.androidide.ui.inflater;

/**
 * Thrown when there was an error inflating the layout
 */
public class InflateException extends RuntimeException {
    
    public InflateException (String message) {
        super(message);
    }
    
    public InflateException (Throwable th) {
        super (th);
    }
    
    public InflateException (String msg, Throwable th) {
        super (msg, th);
    }
}
