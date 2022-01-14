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
package com.itsaky.androidide.lsp.handlers;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPClientLauncher;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.lsp.StandardStreamsLauncher;
import com.itsaky.androidide.shell.IProcessExecutor;
import com.itsaky.androidide.shell.IProcessExitListener;
import com.itsaky.androidide.shell.ProcessExecutorFactory;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class JLSHandler implements LSPHandler {
    
    private static LSPClientLauncher mLauncher;
    
    private static final Logger LOG = Logger.instance("JLSHandler");
    private static final boolean QUIET = !StudioApp.DEBUG;
    
    @Override
    public void start(Runnable onStarted) {
//        try {
//            final IProcessExecutor executor = ProcessExecutorFactory.commonExecutor();
//            final ProcessStreamsHolder holder = new ProcessStreamsHolder();
//            final IProcessExitListener listener = new IProcessExitListener() {
//                @Override
//                public void onExit(int code) {
//                    LOG.info("Java Language server terminated with exit code " + code);
//                    shutdown();
//                }
//            };
//
//            executor.execAsync(holder, listener, false, Environment.JAVA.getAbsolutePath(), "-jar", Environment.JLS_JAR.getAbsolutePath(), QUIET ? "--quiet" : "");
//            startReader (holder.err); // holder.in is used by LSP4J
//
//            if (!IDELanguageClientImpl.isInitialized()) {
//                IDELanguageClientImpl.initialize(LSP.PROVIDER);
//            }
//
//            IDELanguageClientImpl client = IDELanguageClientImpl.getInstance();
//
//            mLauncher = new StandardStreamsLauncher(client, holder.in, holder.out, LSPProvider.LANGUAGE_JAVA);
//            mLauncher.setLaunchListener(server -> onStarted.run());
//            mLauncher.start();
//
//        } catch (Throwable th) {
//            LOG.error(StudioApp.getInstance().getString(com.itsaky.androidide.R.string.err_cannot_start_server, LSPProvider.LANGUAGE_JAVA), th);
//        }
    }
    
    @Override
    public Optional<InitializeResult> init(String rootPath) {

        final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        
        if (server == null) {
            LOG.error (StudioApp.getInstance().getString(com.itsaky.androidide.R.string.err_no_server_implementation));
            return Optional.empty();
        }
        
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
            LOG.error (StudioApp.getInstance().getString(com.itsaky.androidide.R.string.err_init_server), e);
            return Optional.empty();
        }
    }
    
    @Override
    public void initialized() {
        final IDELanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
        if (server == null) {
            return;
        }
        server.initialized(new InitializedParams());
    }
    
    @Override
    public void shutdown() {
        if(mLauncher != null) {
            mLauncher.shutdown();
        }
    }
    
    private void startReader(InputStream in) throws FileNotFoundException {
        final Thread readerThread = new Thread (new ErrReader(in), "JavaLanguageServerOutputReader");
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private static class ErrReader implements Runnable {

        private final InputStream err;

        ErrReader(InputStream err) {
            this.err = err;
        }

        @Override
        public void run() {
            try (final var reader = new BufferedReader(new InputStreamReader(this.err))) {
                var line = "";
                while ((line = reader.readLine()) != null) {
                    LOG.error(line);
                }
            } catch (IOException e) {
                LOG.error("Error reading error stream from Java language server.", e);
            }
        }
    }
}
