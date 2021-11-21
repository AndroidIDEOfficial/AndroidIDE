package com.itsaky.androidide.lsp;

import androidx.annotation.RequiresApi;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import java.util.concurrent.Future;
import com.blankj.utilcode.util.CloseUtils;

@RequiresApi(26)
public class AsynchronousSocketLauncher extends LSPClientLauncher {
    
    private final int port;
    
    private Future<Void> listeningFuture;
    private AsynchronousServerSocketChannel serverSocket;
    
    public AsynchronousSocketLauncher (IDELanguageClientImpl client, int port) {
        super(client);
        this.port = port;
    }

    @Override
    protected void launch() throws Exception{
        serverSocket  = AsynchronousServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(this.port));
        
        final AsynchronousSocketChannel server     = serverSocket.accept().get();
        final OutputStream outWriter               = new OutputStreamWrapper(Channels.newOutputStream(server));
        final InputStream inReader                 = Channels.newInputStream(server);
        
        final Launcher<IDELanguageServer> launcher = createClientLauncher(languageClient, inReader, outWriter);
        this.listeningFuture = launcher.startListening();
        this.server = launcher.getRemoteProxy();
        
        try {
            listeningFuture.get();
        } catch (Throwable th) {}
        
        shutdown();
    }

    @Override
    public void shutdown() {
        if(this.server != null) {
            this.server.shutdown().whenComplete((a, b) -> {
                this.server.exit();
            });
        }
        
        if(listeningFuture != null && !listeningFuture.isDone()) {
            listeningFuture.cancel(true);
        }
        
        CloseUtils.closeIOQuietly(serverSocket);
    }
}
