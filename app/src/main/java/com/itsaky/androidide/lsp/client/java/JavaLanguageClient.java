package com.itsaky.androidide.lsp.client.java;

import com.itsaky.androidide.lsp.IDELanguageClient;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.ProgressParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.RegistrationParams;
import org.eclipse.lsp4j.SetTraceParams;
import org.eclipse.lsp4j.ShowDocumentParams;
import org.eclipse.lsp4j.ShowDocumentResult;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.UnregistrationParams;
import org.eclipse.lsp4j.WorkDoneProgressCreateParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class JavaLanguageClient extends IDELanguageClient {
    
    public JavaLanguageClient(StarterListener starterListener, OnConnectedListener onConnectedListener) {
        super(starterListener, onConnectedListener);
    }
    
    @Override
    protected void connectionReport(String message) {
        LOG.info("Connection report: ", message);
    }

    @Override
    protected void connectionError(Throwable th) {
        LOG.info("Connection error: ", th);
    }
    
    @Override
    public CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams p1) {
        return null;
    }

    @Override
    public CompletableFuture<List<Object>> configuration(ConfigurationParams p1) {
        return null;
    }

    @Override
    public CompletableFuture<Void> createProgress(WorkDoneProgressCreateParams p1) {
        return null;
    }

    @Override
    public void logMessage(MessageParams p1) {
        LOG.info("logMessage: ", gson.toJson(p1));
    }

    @Override
    public void logTrace(LogTraceParams p1) {
        LOG.info("logTrace: ", gson.toJson(p1));
    }

    @Override
    public void notifyProgress(ProgressParams p1) {
        LOG.info("notifyProgress: ", gson.toJson(p1));
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams p1) {
    }

    @Override
    public CompletableFuture<Void> refreshCodeLenses() {
        return null;
    }

    @Override
    public CompletableFuture<Void> refreshSemanticTokens() {
        return null;
    }

    @Override
    public CompletableFuture<Void> registerCapability(RegistrationParams p1) {
        return null;
    }

    @Override
    public void setTrace(SetTraceParams p1) {
        LOG.info("setTrace: ", gson.toJson(p1));
    }

    @Override
    public CompletableFuture<ShowDocumentResult> showDocument(ShowDocumentParams p1) {
        return null;
    }

    @Override
    public void showMessage(MessageParams p1) {
        LOG.info("showMessage: ", gson.toJson(p1));
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams p1) {
        return null;
    }
    
    @Override
    public CompletableFuture<Void> unregisterCapability(UnregistrationParams p1) {
        return null;
    }

    @Override
    public CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {
        return null;
    }
}
