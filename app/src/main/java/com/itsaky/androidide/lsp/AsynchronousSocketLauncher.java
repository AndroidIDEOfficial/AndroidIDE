/************************************************************************************
 * This file is part of AndroidIDE.
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

    @Override
    public String getLanguageCode() {
        return "";
    }
}
