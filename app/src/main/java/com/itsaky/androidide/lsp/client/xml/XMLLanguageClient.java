package com.itsaky.androidide.lsp.client.xml;

import com.itsaky.androidide.lsp.AbstractLanguageClient;
import com.itsaky.androidide.utils.JSONUtility;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lemminx.customservice.ActionableNotification;
import org.eclipse.lemminx.customservice.XMLLanguageClientAPI;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.ProgressParams;
import org.eclipse.lsp4j.RegistrationParams;
import org.eclipse.lsp4j.SetTraceParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.UnregistrationParams;
import org.eclipse.lsp4j.WorkDoneProgressCreateParams;
import org.eclipse.lsp4j.WorkspaceFolder;

public class XMLLanguageClient extends AbstractLanguageClient implements XMLLanguageClientAPI {
    
    public XMLLanguageClient(StarterListener starterListener, OnConnectedListener onConnectedListener) {
        super(starterListener, onConnectedListener);
    }
    
    @Override
    public void actionableNotification(ActionableNotification p1) {
        LOG.info("XMLLS: actionableNotification: " + JSONUtility.prettyPrinter.toJson(p1));
    }

    @Override
    public CompletableFuture<Object> executeClientCommand(ExecuteCommandParams p1) {
        LOG.info("XMLLS: executeClientCommand: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }
    
    @Override
    public CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams p1) {
        LOG.info("XMLLS: applyEdit: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }

    @Override
    public CompletableFuture<List<Object>> configuration(ConfigurationParams p1) {
        LOG.info("XMLLS: configuration: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }

    @Override
    public CompletableFuture<Void> createProgress(WorkDoneProgressCreateParams p1) {
        LOG.info("XMLLS: createProgress: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }
    
    @Override
    public void notifyProgress(ProgressParams p1) {
        LOG.info("XMLLS: notifyProgress: ", gson.toJson(p1));
    }

    @Override
    public CompletableFuture<Void> refreshCodeLenses() {
        LOG.info("XMLLS: refreshCodeLenses");
        return null;
    }

    @Override
    public CompletableFuture<Void> refreshSemanticTokens() {
        LOG.info("XMLLS: refreshSemanticTokens: ");
        return null;
    }

    @Override
    public CompletableFuture<Void> registerCapability(RegistrationParams p1) {
        LOG.info("XMLLS: registerCapability: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }

    @Override
    public void setTrace(SetTraceParams p1) {
        LOG.info("XMLLS: setTrafe: " + JSONUtility.prettyPrinter.toJson(p1));
    }

    @Override
    public void showMessage(MessageParams p1) {
        LOG.info("XMLLS: showMessage: " + JSONUtility.prettyPrinter.toJson(p1));
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams p1) {
        LOG.info("XMLLS: showMessageRequest: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }

    @Override
    public CompletableFuture<Void> unregisterCapability(UnregistrationParams p1) {
        LOG.info("XMLLS: unregisterCapability: " + JSONUtility.prettyPrinter.toJson(p1));
        return null;
    }

    @Override
    public CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {
        LOG.info("XMLLS: workspaceFolders");
        return null;
    }
}
