package com.itsaky.androidide.utils;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Reads an input stream line by line
 */
public class InputStreamLineReader implements Runnable {
    
    private final InputStream in;
    private final OnReadListener listener;

    public InputStreamLineReader(InputStream in, OnReadListener listener) {
        this.in = in;
        this.listener = listener;
    }
    
    @Override
    public void run() {
        try {
            final BufferedReader reader = new BufferedReader (new InputStreamReader (in));
            String line;
            while ((line = reader.readLine()) != null && listener != null) {
                listener.onRead(line);
            }
        } catch (Throwable th) {
            // ignored
        }
    }
    
    public static interface OnReadListener {
        void onRead (String line);
    }
}
