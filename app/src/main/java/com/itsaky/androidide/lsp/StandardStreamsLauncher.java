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

import com.blankj.utilcode.util.CloseUtils;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;
import org.eclipse.lsp4j.jsonrpc.Launcher;

public class StandardStreamsLauncher extends LSPClientLauncher {
    
    private final InputStream in;
    private final OutputStream out;
    private final String langCode;
    
    private Future<Void> listening;
    
    public StandardStreamsLauncher(IDELanguageClientImpl client, InputStream in, OutputStream out, String langCode) {
        super(client);
        this.in = new BufferedInputStream( in );
        this.out = new BufferedOutputStream( out );
        this.langCode = langCode;
    }
    
    @Override
    protected void launch() {
        final Launcher<IDELanguageServer> server = createClientLauncher(languageClient, in, out);
        
        this.server = server.getRemoteProxy();
        this.listening = server.startListening();
        
        LSPProvider.setLanguageServer(this.langCode, this.server);
        
        try {
            listening.get();
        } catch (Throwable th) {
            LOG.error (
                "---> StandardStreamsLauncher <---",
                "An error occured while waiting for language server process to stop",
                th
            );
        }
        
        shutdown();
    }

    @Override
    public void shutdown() {
        if(server != null) {
            server.shutdown().whenComplete((a, b) -> {
                server.exit();
            });
        }
        
        if (listening != null && !listening.isDone()) {
            listening.cancel(true);
        }
        
        CloseUtils.closeIO(in, out);
    }
}
