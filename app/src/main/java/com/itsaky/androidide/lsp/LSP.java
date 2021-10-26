package com.itsaky.androidide.lsp;

import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.lsp.handlers.JLSHandler;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.services.IDELanguageServer;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.ClientInfo;
import org.eclipse.lsp4j.ColorProviderCapabilities;
import org.eclipse.lsp4j.CompletionCapabilities;
import org.eclipse.lsp4j.CreateFilesParams;
import org.eclipse.lsp4j.DeleteFilesParams;
import org.eclipse.lsp4j.DidChangeConfigurationCapabilities;
import org.eclipse.lsp4j.DidChangeWatchedFilesCapabilities;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.ExecuteCommandCapabilities;
import org.eclipse.lsp4j.FileCreate;
import org.eclipse.lsp4j.FileDelete;
import org.eclipse.lsp4j.FileOperationsWorkspaceCapabilities;
import org.eclipse.lsp4j.FileRename;
import org.eclipse.lsp4j.GeneralClientCapabilities;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.SynchronizationCapabilities;
import org.eclipse.lsp4j.TextDocumentClientCapabilities;
import org.eclipse.lsp4j.WindowClientCapabilities;
import org.eclipse.lsp4j.WorkspaceClientCapabilities;

/**
 * A manager class to manage Language Server Protocol implementations
 * An initiaize request must be sent to the server only through specific nested classes.
 * This will make sure that we map the capabilites of each language server we support.
 */
public class LSP {
    
    public static EditorActivityProvider PROVIDER;
    private static final Logger LOG = Logger.instance("LSP");
    
    public static JLSHandler Java = new JLSHandler();
    
    /**
     * Stores information about ports at which the specified language server must be started
     */
    public static class Ports {
        public static final int JAVA = 4116;
    }
    
    public static void setActivityProvider(EditorActivityProvider provider) {
        PROVIDER = provider;
    }
    
    public static void notifyFileCreated(File file) {
        if(!shouldSendFileActionNotification(file)) return;
        
        final CreateFilesParams params =
            new CreateFilesParams(
                Collections.singletonList(
                    new FileCreate(
                        file.toURI().toString()
                     )
                 )
             );
        
        for(Map.Entry<String, IDELanguageServer> entry : LSPProvider.getAvailableServers().entrySet()) {
            final IDELanguageServer server = entry.getValue();
            if(server == null) {
                server.getWorkspaceService().didCreateFiles(params);
            }
        }
    }
    
    public static void notifyFileDeleted(File file) {
        if(!shouldSendFileActionNotification(file)) return;
        
        final DeleteFilesParams params =
            new DeleteFilesParams(
            Collections.singletonList(
                new FileDelete(
                    file.toURI().toString()
                )
            )
        );

        for(Map.Entry<String, IDELanguageServer> entry : LSPProvider.getAvailableServers().entrySet()) {
            final IDELanguageServer server = entry.getValue();
            if(server == null) {
                server.getWorkspaceService().didDeleteFiles(params);
            }
        }
    }
    
    public static void notifyFileRenamed(File file, String newName) {
        if(!shouldSendFileActionNotification(file))  return;
        
        final File newFile = new File(file.getParentFile(), newName);
        final RenameFilesParams params =
            new RenameFilesParams(
            Collections.singletonList(
                new FileRename(
                    file.toURI().toString(), // Old uri
                    newFile.toURI().toString() // New uri
                )
            )
        );

        for(Map.Entry<String, IDELanguageServer> entry : LSPProvider.getAvailableServers().entrySet()) {
            final IDELanguageServer server = entry.getValue();
            if(server == null) {
                server.getWorkspaceService().didRenameFiles(params);
            }
        }
    }
    
    public static void notifyWatchedFilesChanged(final DidChangeWatchedFilesParams params) {
        for(Map.Entry<String, IDELanguageServer> entry : LSPProvider.getAvailableServers().entrySet()) {
            final IDELanguageServer server = entry.getValue();
            if(server == null) {
                server.getWorkspaceService().didChangeWatchedFiles(params);
            }
        }
    }
    
    public static boolean shouldSendFileActionNotification(File file) {
        // TODO Check if we should send notification to any language server
        // which handles this type of files
        return true;
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
