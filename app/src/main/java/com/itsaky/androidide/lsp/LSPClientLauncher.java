package com.itsaky.androidide.lsp;

import com.blankj.utilcode.util.CloseUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
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

/**
 * Launches a client for a Language server and handles connection.
 * A callback is sent to the IDELanguageClient to tell the client to start the server if necessary.
 */
public class LSPClientLauncher extends Thread {

    private final int PORT;
    private Future<Void> listeningFuture;
    private final AbstractLanguageClient languageClient;

    private AsynchronousServerSocketChannel serverSocket;
    private IDELanguageServer server;

    public LSPClientLauncher(AbstractLanguageClient client, int port) {
        this.languageClient = client;
        this.PORT = port;

        setDaemon(true);
    }

    /**
     * Returns the connected LanguageServer, or {@code null}
     *
     * @return The LanguageServer or {@code null}
     */
    public IDELanguageServer getServer() {
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
            serverSocket  = AsynchronousServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(PORT));
            languageClient.connectionReport("Waiting for server to connect...");
            languageClient.startServer();

            final AsynchronousSocketChannel server     = serverSocket.accept().get();
            final OutputStream outWriter = new OutputStreamWrapper(Channels.newOutputStream(server));
            final InputStream inReader   = Channels.newInputStream(server);
            languageClient.connectionReport("Server connected. Launching client...");
            
            Launcher<IDELanguageServer> launcher = createClientLauncher(languageClient, inReader, outWriter);
            listeningFuture = launcher.startListening();
            
            this.server = launcher.getRemoteProxy();
            languageClient.onServerConnected(this.server);
            languageClient.connectionReport("Server is now listening.");

            try {
                LOG.info("Waiting for server connection result");
                listeningFuture.get();
                LOG.info("listeningFuture.get returned");
            } catch (Throwable th) {
                LOG.error("listeningFuture.get error", th);
            }
            
            // Make sure we close the server. Sockets are limited resources...
            CloseUtils.closeIOQuietly(serverSocket, server, outWriter, inReader);
            languageClient.connectionReport("Server disconnected.");
            languageClient.onServerDisconnected();
        } catch (Throwable th) {
            languageClient.connectionError(th);
        }
    }
    
    private Launcher<IDELanguageServer> createClientLauncher(AbstractLanguageClient client, InputStream in, OutputStream out) {
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
    public void shutdown() {
        try {
            if (this.server != null) {
                server.shutdown();
                server.exit();
            }
            serverSocket.close();
            if(listeningFuture != null && !listeningFuture.isDone()) {
                listeningFuture.cancel(true);
            }
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
                } catch (Throwable e) {
                    LOG.error("Error writing to LanguageServer[closed=" + closed + "]", e);
                }
            }
            
            LOG.info("Writer thread stopped");
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

    private static final Logger LOG = Logger.instance("LSPClientLauncher");
}
