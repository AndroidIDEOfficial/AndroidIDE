package com.itsaky.androidide.lsp;

import com.blankj.utilcode.util.CloseUtils;
import com.itsaky.androidide.utils.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;

/**
 * Launches a client for a Language server and handles connection.
 * A callback is sent to the IDELanguageClient to tell the client to start the server if necessary.
 */
public class LSPClientLauncher extends Thread {

    private final int PORT;
    private Future<Void> listeningFuture;
    private final IDELanguageClient languageClient;

    private AsynchronousServerSocketChannel serverSocket;
    private LanguageServer server;

    public LSPClientLauncher(IDELanguageClient client, int port) {
        this.languageClient = client;
        this.PORT = port;

        setDaemon(true);
    }

    /**
     * Returns the connected LanguageServer, or {@code null}
     *
     * @return The LanguageServer or {@code null}
     */
    public LanguageServer getServer() {
        return server;
    }

    /**
     * Are we connected to the server?
     */
    public boolean isConnected() {
        return languageClient != null && languageClient.isConnected();
    }

    @Override
    public void run() {
        try { 
            languageClient.connectionReport("Starting server socket");
            serverSocket = AsynchronousServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(PORT));
            languageClient.connectionReport("Waiting for server to connect...");
            languageClient.startServer();

            final AsynchronousSocketChannel server = serverSocket.accept().get();
            final OutputStream outWriter = new OutputStreamWrapper(Channels.newOutputStream(server));
            final InputStream inReader   = Channels.newInputStream(server);
            languageClient.connectionReport("Server connected. Launching client...");
            
            Launcher<LanguageServer> launcher = LSPLauncher.createClientLauncher(languageClient, inReader, outWriter);
            listeningFuture = launcher.startListening();

            this.server = launcher.getRemoteProxy();
            languageClient.onServerConnected(this.server);
            languageClient.connectionReport("Server is now listening.");

            while (!(listeningFuture.isCancelled() || listeningFuture.isDone())) {
                // Take some rest
                sleep(50);
            }
            
            // Make sure we close the server. Sockets are limited resources...
            CloseUtils.closeIOQuietly(inReader, outWriter, server);
            languageClient.connectionReport("Server disconnected.");
            languageClient.onServerDisconnected();
        } catch (Throwable th) {
            languageClient.connectionError(th);
        }
    }

    /**
     * Shut down the language server
     */
    public void shutdown() {
        try {
            if (this.server != null) {
                server.shutdown();
                server.exit();
            }
            serverSocket.close();
        } catch (Throwable th) {
            // Ignored
        }
    }

    /**
     * Wraps an output stream to make sure that we don't write on UI Thread
     * Writing to a socket's output stream is considered as network operation
     * Trying to write to a socket on UI Thread will result in NetworkOnMainThreadExeption
     */

    private class OutputStreamWrapper extends OutputStream {
        
        private final AsyncWriter writer;
        private final Thread writerThread;
        
        public OutputStreamWrapper(OutputStream actualStream) {
            this.writer = new AsyncWriter(actualStream);
            this.writerThread = new Thread(writer);
            
            this.writerThread.start();
        }
        
        @Override
        public void write(int p1) throws IOException {
            // Not needed
        }

        @Override
        public void write(byte[] b) throws IOException {
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
                } catch (Throwable e) {
                    LOG.error("Error writing to LanguageServer");
                }
            }
            
            LOG.info("Writer thread stopped");
        }

        @Override
        public void close() throws IOException {
            this.closed = true;
            this.out.close();
        }

        class Writable {
            byte[] data;

            public Writable(byte[] data) {
                this.data = data;
            }
        }
    }

    private static final Logger LOG = Logger.instance("LSPClientLauncher");
}
