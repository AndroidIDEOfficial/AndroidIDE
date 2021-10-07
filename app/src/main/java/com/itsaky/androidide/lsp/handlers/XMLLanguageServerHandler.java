package com.itsaky.androidide.lsp.handlers;

import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.lsp.LanguageServerWrapper;
import com.itsaky.androidide.lsp.client.xml.XMLLanguageClient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lemminx.XMLLanguageServer;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class XMLLanguageServerHandler implements LSPHandler {
    
    private LanguageServerWrapper server;
    private XMLLanguageClient client;
    
    @Override
    public void start(Runnable onStarted) {
        server = new LanguageServerWrapper(new XMLLanguageServer());
        client = new XMLLanguageClient(null, null);
        ((XMLLanguageServer) server.getServer()).setClient(client);
        
        LSPProvider.setLanguageServer(LSPProvider.LANGUAGE_XML, server);
        LSPProvider.setClientForLanguage(LSPProvider.LANGUAGE_XML, client);

        if(onStarted != null)
            onStarted.run();
    }
    
    @Override
    public Optional<InitializeResult> init(String rootPath) {
        
        if(this.server == null) {
            return Optional.empty();
        }
        
        final List<WorkspaceFolder> folders = new ArrayList<>();
        final File file = new File(rootPath);
        final WorkspaceFolder folder = new WorkspaceFolder();
        
        folder.setUri(file.toURI().toString());
        folder.setName(file.getName().toString());
        folders.add(folder);

        InitializeParams params = new InitializeParams();
        params.setClientInfo(LSP.getClientInfo());
        params.setWorkspaceFolders(folders);
        params.setCapabilities(LSP.getCapabilities());

        CompletableFuture<InitializeResult> initResult = server.initialize(params);
        try {
            InitializeResult result = initResult.get();
            if(result.getCapabilities() != null) {
                LSPProvider.setServerCapabilitesForLanguage(LSPProvider.LANGUAGE_XML, result.getCapabilities());
            }
            return Optional.of(result);
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void initialized() {
        
        if(server == null)
            return;
            
        server.initialized(new InitializedParams());
        
    }
    
    @Override
    public void shutdown() {
        server.shutdown();
    }
}
