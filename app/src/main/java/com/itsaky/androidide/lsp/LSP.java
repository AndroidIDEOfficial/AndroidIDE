package com.itsaky.androidide.lsp;

import com.google.gson.Gson;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.client.java.JavaLanguageClient;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.ClientInfo;
import org.eclipse.lsp4j.GeneralClientCapabilities;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.TextDocumentClientCapabilities;
import org.eclipse.lsp4j.WindowClientCapabilities;
import org.eclipse.lsp4j.WorkspaceClientCapabilities;
import org.eclipse.lsp4j.WorkspaceFolder;
import org.eclipse.lsp4j.services.LanguageServer;
import java.util.concurrent.ExecutionException;
import java.util.Optional;
import org.eclipse.lsp4j.CallHierarchyCapabilities;
import org.eclipse.lsp4j.CodeActionCapabilities;
import org.eclipse.lsp4j.DidChangeConfigurationCapabilities;
import org.eclipse.lsp4j.DidChangeWatchedFilesCapabilities;
import org.eclipse.lsp4j.ExecuteCommandCapabilities;
import org.eclipse.lsp4j.FileOperationsWorkspaceCapabilities;
import org.eclipse.lsp4j.SemanticTokensCapabilities;
import org.eclipse.lsp4j.SynchronizationCapabilities;
import org.eclipse.lsp4j.ColorProviderCapabilities;
import org.eclipse.lsp4j.CompletionCapabilities;
import org.eclipse.lsp4j.CompletionItemCapabilities;
import com.itsaky.androidide.interfaces.EditorActivityProvider;

/**
 * A manager class to manage Language Server Protocol implementations
 * An initiaize request must be sent to the server only through specific nested classes.
 * This will make sure that we map the capabilites of each language server we support.
 */
public class LSP {
    
    private static EditorActivityProvider PROVIDER;
    private static final Logger LOG = Logger.instance("LSP");
    
    /**
     * Stores information about ports at which the specified language server must be started
     */
    public static class Ports {
        public static final int JAVA = 4116;
    }
    
    /**
     * Manages Java Language Server
     */
    public static class Java {
        private static ShellServer mShell;
        private static JavaLanguageClient javaClient;
        private static LSPClientLauncher mLauncher;

        /**
         * Starts the Java Language server and the client
         */
        public static void start(Runnable onStarted) {
            if (mShell != null && mLauncher != null && mLauncher.isConnected()) {
                return;
            }
            javaClient = new JavaLanguageClient(() -> LSP.Java.startJLS(false), server -> storeServerInfoAndNotify(server, onStarted));
            javaClient.setActivityProvider(LSP.PROVIDER);
            mLauncher = new LSPClientLauncher(javaClient, Ports.JAVA);
            mLauncher.start();
        }

        /**
         * Sends initialize request to the Java Language Server
         */
        public static Optional<InitializeResult> init(String rootPath) {

            final LanguageServer server = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
            if (server == null)
                return Optional.empty();

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
                    LSPProvider.setServerCapabilitesForLanguage(LSPProvider.LANGUAGE_JAVA, result.getCapabilities());
                }
                return Optional.of(result);
            } catch (Throwable e) {
                return Optional.empty();
            }
        }
        
        /**
         * Sends a shutdown request to the java language server
         */
         
        public static void shutdown() {
            if(mLauncher != null) {
                mLauncher.shutdown();
            }
        }
        
        /**
         * Start JDT LS from the command line
         */
        private static void startJLS(boolean quiet) {
            mShell = StudioApp.getInstance().newShell(t -> LOG.verbose(t));
            mShell.append(String.format("java -Djls.client.port=%d -jar %s%s", Ports.JAVA, Environment.JLS_JAR.getAbsolutePath(), quiet ? /* IMPORTANT: Do not forget the leading space */ " --quiet" : ""));
        }

        /**
         * Store the instance of Java Language Server in LSPProvider
         */
        private static void storeServerInfoAndNotify(LanguageServer server, Runnable startedListener) {
            LSPProvider.setLanguageServer(LSPProvider.LANGUAGE_JAVA, server);
            LSPProvider.setClientForLanguage(LSPProvider.LANGUAGE_JAVA, javaClient);
            
            if(startedListener != null)
                startedListener.run();
        }
    }
    
    public static void setActivityProvider(EditorActivityProvider provider) {
        PROVIDER = provider;
    }
    
    /**
     * Shuts down all known language server
     */
    public static void shutdownAll() {
        // Shutdown language servers one by one
        LSP.Java.shutdown();
    }

    /**
     * Global LSP client information that should be passed to LanguageServer with InitializeParams
     */
    public static ClientInfo getClientInfo() {
        ClientInfo info = new ClientInfo();
        info.setName("AndroidIDE");
        info.setVersion(com.itsaky.androidide.BuildConfig.VERSION_NAME);
        return info;
    }

    public static ClientCapabilities getCapabilities() {
        ClientCapabilities capabilities = new ClientCapabilities();
        capabilities.setGeneral(LSP.getGeneralCapabilities());
        capabilities.setTextDocument(LSP.getTextDocumentCapabilities());
        capabilities.setWindow(LSP.getWindowCapabilities());
        capabilities.setWorkspace(LSP.getWorkspaceCapabilities());
        return capabilities;
    }

    private static GeneralClientCapabilities getGeneralCapabilities() {
        GeneralClientCapabilities c = new GeneralClientCapabilities();
        return c;
    }

    private static TextDocumentClientCapabilities getTextDocumentCapabilities() {
        TextDocumentClientCapabilities c = new TextDocumentClientCapabilities();
        c.setSynchronization(synchronizationCapabilities());
        c.setColorProvider(new ColorProviderCapabilities());
        c.setCompletion(completionCapabilities());
        return c;
    }

    private static CompletionCapabilities completionCapabilities() {
        CompletionCapabilities c = new CompletionCapabilities();
        return c;
    }

    private static SynchronizationCapabilities synchronizationCapabilities() {
        SynchronizationCapabilities c = new SynchronizationCapabilities();
        c.setDidSave(true);
        c.setWillSave(false);
        c.setWillSaveWaitUntil(false);
        return c;
    }

    private static WindowClientCapabilities getWindowCapabilities() {
        WindowClientCapabilities c = new WindowClientCapabilities();
        return c;
    }

    private static WorkspaceClientCapabilities getWorkspaceCapabilities() {
        WorkspaceClientCapabilities c = new WorkspaceClientCapabilities();
        c.setApplyEdit(true);
        c.setConfiguration(true);
        c.setDidChangeConfiguration(new DidChangeConfigurationCapabilities(false));
        c.setDidChangeWatchedFiles(new DidChangeWatchedFilesCapabilities(false));
        c.setExecuteCommand(new ExecuteCommandCapabilities(true));
        c.setFileOperations(fileOperationsWorkspaceCapabilities());
        return c;
    }
    
    private static FileOperationsWorkspaceCapabilities fileOperationsWorkspaceCapabilities() {
        FileOperationsWorkspaceCapabilities c = new FileOperationsWorkspaceCapabilities();
        c.setDidCreate(true);
        c.setDidDelete(true);
        c.setDidRename(true);
        return c;
    }
}
