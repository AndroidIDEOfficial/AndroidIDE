package com.itsaky.androidide.shell;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Holder input, error and output streams for a {@link java.lang.Process}
 */
public class ProcessStreamsHolder {
    public InputStream in;
    public InputStream err;
    public OutputStream out;
}
