package com.itsaky.androidide.lsp.handlers;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPClientLauncher;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.lsp.StandardStreamsLauncher;
import com.itsaky.androidide.lsp.client.java.JavaLanguageClient;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        
        final boolean quiet = false;
        mShell = StudioApp.getInstance().newShell((t -> LOG.verbose(t)), false);
        mShell.append(String.format(Locale.US, "java -jar %s%s", Environment.JLS_JAR.getAbsolutePath(), quiet ? /* IMPORTANT: Do not forget the leading space */ " --quiet" : ""), false);
        
        javaClient = new JavaLanguageClient(null, server -> storeServerInfoAndNotify(server, onStarted));
        javaClient.setActivityProvider(LSP.PROVIDER);
        mLauncher = new StandardStreamsLauncher(javaClient, mShell.getProcessInputStream(), mShell.getProcessOutputStream());
        mLauncher.start();
    }
    
    @Override
    public Optional<InitializeResult> init(String rootPath) {

        final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        LOG.info("SERVER JLS: " + server);
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
            LOG.info("init future: " + initResult);
            InitializeResult result = initResult.get();
            LOG.info("Initialize result", result);
            if(result.getCapabilities() != null) {
                LSPProvider.setServerCapabilitesForLanguage(LSPProvider.LANGUAGE_JAVA, result.getCapabilities());
            }
            return Optional.of(result);
        } catch (Throwable e) {
            LOG.error("Initialize error", e);
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
     * Store the instance of Java Language Server in LSPProvider
     */
    private void storeServerInfoAndNotify(IDELanguageServer server, Runnable startedListener) {
        LSPProvider.setLanguageServer(LSPProvider.LANGUAGE_JAVA, server);
        LSPProvider.setClientForLanguage(LSPProvider.LANGUAGE_JAVA, javaClient);

        if(startedListener != null)
            startedListener.run();
    }
}
