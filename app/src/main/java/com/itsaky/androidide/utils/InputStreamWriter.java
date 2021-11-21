package com.itsaky.androidide.utils;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Writes an InputStream to a file
 */
public class InputStreamWriter implements Runnable {
    
    private final InputStream in;
    private final File out;
    
    private final BufferedOutputStream os;

    public InputStreamWriter(InputStream in, File out) throws FileNotFoundException {
        this.in = in;
        this.out = out;
        
        this.os = new BufferedOutputStream (new FileOutputStream (out, true));
    }
    
    @Override
    public void run() {
        try {
            final BufferedReader reader = new BufferedReader (new InputStreamReader (in));
            while (true) {
                final int data = reader.read();
                if (data == -1) {
                    break;
                }
                
                os.write(data);
                os.flush();
            }
        } catch (Throwable th) {
            // ignored
        }
    }
    
}
