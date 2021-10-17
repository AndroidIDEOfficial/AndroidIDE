package com.itsaky.androidide.lsp.handlers;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPClientLauncher;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.lsp.StandardStreamsLauncher;
import com.itsaky.androidide.lsp.client.java.JavaLanguageClient;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class JLSHandler implements LSPHandler {
    
    /**
     * Why not use {@link com.itsaky.androidide.shell.ShellServer ShellServer} instead?
     * Because it will try to read from the process's input stream
     * and at the same time, LSP4J will try to do the same. But,
     * as {@code ShellServer} will be started first, LSP4J will not he able to read at all.
     * <p>
     * That's why we use a ProcessBuilder without redirecting {@code System.err} of the process
     */
    private static ProcessBuilder mShellBuilder;
    private static Process mShell;
    
    private static JavaLanguageClient javaClient;
    private static LSPClientLauncher mLauncher;
    
    private static final Logger LOG = Logger.instance("JLSHandler");
    private static final boolean QUIET = !StudioApp.DEBUG;
    
    @Override
    public void start(Runnable onStarted) {
        if (mShell != null && mLauncher != null && mLauncher.isConnected()) {
            return;
        }
        
        try {
            // Why not directly use 'java' command?
            // I tried doing the same, but it ends up throwing unexpected errors.
            // Something related to FileSystem...
            mShellBuilder = new ProcessBuilder("/system/bin/sh");
            mShellBuilder.directory(Environment.HOME);
            mShellBuilder.redirectErrorStream(false);
            
            if(StudioApp.DEBUG) {
                mShellBuilder.redirectError(new File("/sdcard/ide_xlog/process_error.txt"));
            }
            
            mShellBuilder.environment().putAll(Environment.getEnvironment(false));
            mShell = mShellBuilder.start();
            
            mShell.getOutputStream().write(("java -jar " + Environment.JLS_JAR.getAbsolutePath() + (QUIET ? " --quiet" : "") + "\n").getBytes());
            mShell.getOutputStream().flush();
            
            javaClient = new JavaLanguageClient(null, server -> storeServerInfoAndNotify(server, onStarted));
            javaClient.setActivityProvider(LSP.PROVIDER);
            mLauncher = new StandardStreamsLauncher(javaClient, mShell.getInputStream(), mShell.getOutputStream());
            mLauncher.start();
        } catch (Throwable th) {}
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
        if(mShell != null) {
            mShell.destroy();
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
