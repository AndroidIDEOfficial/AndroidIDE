package com.itsaky.androidide.lsp.handlers;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPClientLauncher;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.lsp.client.java.JavaLanguageClient;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java9.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class JLSHandler implements LSPHandler {
    
    private static ShellServer mShell;
    private static JavaLanguageClient javaClient;
    private static LSPClientLauncher mLauncher;
    
    private static final Logger LOG = Logger.instance("LSP");
    
    @Override
    public void start(Runnable onStarted) {
        if (mShell != null && mLauncher != null && mLauncher.isConnected()) {
            return;
        }
        javaClient = new JavaLanguageClient(() -> startJLS(false), server -> storeServerInfoAndNotify(server, onStarted));
        javaClient.setActivityProvider(LSP.PROVIDER);
        mLauncher = new LSPClientLauncher(javaClient, LSP.Ports.JAVA);
        mLauncher.start();
    }
    
    @Override
    public Optional<InitializeResult> init(String rootPath) {

        final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        if (server == null)
            return Optional.empty();

        final List<WorkspaceFolder> folders = new ArrayList<>();
        final File file = new File(rootPath);
        final WorkspaceFolder folder = new WorkspaceFolder();
        final WorkspaceFolder logsender = new WorkspaceFolder();

        folder.setUri(file.toURI().toString());
        folder.setName(file.getName().toString());
        folders.add(folder);

        logsender.setUri(StudioApp.getInstance().getLogSenderDir().toURI().toString());
        logsender.setName(StudioApp.getInstance().getLogSenderDir().getName());
        folders.add(logsender);

        InitializeParams params = new InitializeParams();
        params.setClientInfo(LSP.getClientInfo());
        params.setWorkspaceFolders(folders);
        params.setCapabilities(LSP.getCapabilities());

        CompletableFuture<InitializeResult> initResult = server.initialize(params);
        try {
            InitializeResult result = initResult.get();
            if(result.getCapabilities() != null) {
                LSPProvider.setServerCapabilitesForLanguage(LSPProvider.LANGUAGE_JAVA, result.getCapabilities());
            }
            return Optional.of(result);
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
    
    @Override
    public void initialized() {
        
        final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        if (server == null)
            return;
            
        // Initialized params do not have any parameters
        server.initialized(new InitializedParams());

    }
    
    @Override
    public void shutdown() {
        if(mLauncher != null) {
            mLauncher.shutdown();
        }
    }

    /**
     * Start JDT LS from the command line
     */
    private void startJLS(boolean quiet) {
        mShell = StudioApp.getInstance().newShell(t -> LOG.verbose(t));
        mShell.append(String.format("java -Djls.client.port=%d -jar %s%s", LSP.Ports.JAVA, Environment.JLS_JAR.getAbsolutePath(), quiet ? /* IMPORTANT: Do not forget the leading space */ " --quiet" : ""));
    }

    /**
     * Store the instance of Java Language Server in LSPProvider
     */
    private void storeServerInfoAndNotify(IDELanguageServer server, Runnable startedListener) {
        LSPProvider.setLanguageServer(LSPProvider.LANGUAGE_JAVA, server);
        LSPProvider.setClientForLanguage(LSPProvider.LANGUAGE_JAVA, javaClient);

        if(startedListener != null)
            startedListener.run();
    }
}
