/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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


package com.itsaky.androidide.lsp;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.eclipse.lsp4j.jsonrpc.Launcher;

/**
 * Launches a client for a Language server and handles connection.
 * A callback is sent to the IDELanguageClient to tell the client to start the server if necessary.
 */
public abstract class LSPClientLauncher extends Thread {
    
    protected final IDELanguageClientImpl languageClient;
    protected IDELanguageServer server;
    
    public LSPClientLauncher(IDELanguageClientImpl client) {
        Objects.requireNonNull(client);
        setDaemon(true);
        
        this.languageClient = client;
    }

    /**
     * Returns the connected LanguageServer, or {@code null}
     *
     * @return The LanguageServer or {@code null}
     */
    public IDELanguageServer getServer() {
        return this.server;
    }

    /**
     * Are we connected to the server?
     */
    public boolean isConnected() {
        return languageClient != null && languageClient.isConnected();
    }
    
    /**
     * Actual logic to start the server
     */
    protected abstract void launch() throws Exception;

    @Override
    public void run() {
        try {
            launch();
        } catch (Throwable th) {
            // TODO Log this
        }
    }
    
    protected Launcher<IDELanguageServer> createClientLauncher(IDELanguageClientImpl client, InputStream in, OutputStream out) {
        return new Launcher.Builder<IDELanguageServer> ()
            .setLocalService(client)
            .setRemoteInterface(IDELanguageServer.class)
            .setInput(in)
            .setOutput(out)
            .create();
    }

    /**
     * Shut down the language server
     */
    public abstract void shutdown();

    /**
     * Wraps an output stream to make sure that we don't write on UI Thread
     * Writing to a socket's output stream is considered as network operation
     * Trying to write to a socket on UI Thread will result in NetworkOnMainThreadExeption
     */
    protected class OutputStreamWrapper extends OutputStream {
        
        private final AsyncWriter writer;
        private final Thread writerThread;
        
        private final OutputStream actualStream;
        
        public OutputStreamWrapper(OutputStream actualStream) {
            this.writer = new AsyncWriter(actualStream);
            this.writerThread = new Thread(writer);
            this.actualStream = actualStream;
            
            this.writerThread.start();
        }
        
        @Override
        public void write(int p1) throws IOException {
            // Not needed
        }
        
        
        /**
         * LSP4J always calls this method to write to remote proxy.
         * See https://github.com/eclipse/lsp4j/blob/253aef9a702659f2524ecefaab7e829278c2ffd3/org.eclipse.lsp4j.jsonrpc/src/main/java/org/eclipse/lsp4j/jsonrpc/json/StreamMessageConsumer.java#L67
         */
        @Override
        public void write(final byte[] b) throws IOException {
            // Do not call super.write(b);
            // If done, it will further call write(int)
            this.writer.write(b);
        }

        @Override
        public void close() throws IOException {
            super.close();
            this.writer.close();
        }
    }
    
    /**
     * Actual implementation of the writer thread
     */
    private class AsyncWriter implements Runnable, Closeable {

        private final OutputStream out;
        private boolean closed;

        private final BlockingQueue<Writable> writeQueue = new ArrayBlockingQueue<>(10);

        public AsyncWriter(OutputStream out) {
            this.out = out;
            this.closed = false;
        }

        public void write(byte[] b) {
            this.writeQueue.add(new Writable(b));
        }

        @Override
        public void run() {
            while (!closed) {
                try {
                    Writable data = writeQueue.take();
                    this.out.write(data.data);
                    this.out.flush();
                } catch (Throwable e) { }
            }
        }

        @Override
        public void close() throws IOException {
            this.out.close();
            this.closed = true;
        }

        class Writable {
            byte[] data;

            public Writable(byte[] data) {
                this.data = data;
            }
        }
    }

    protected static final Logger LOG = Logger.instance("LSPClientLauncher");
}
