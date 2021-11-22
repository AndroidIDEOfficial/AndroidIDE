/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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

import com.itsaky.lsp.services.IDELanguageServer;
import com.itsaky.lsp.services.IDETextDocumentService;
import com.itsaky.lsp.services.IDEWorkspaceService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.CallHierarchyIncomingCall;
import org.eclipse.lsp4j.CallHierarchyIncomingCallsParams;
import org.eclipse.lsp4j.CallHierarchyItem;
import org.eclipse.lsp4j.CallHierarchyOutgoingCall;
import org.eclipse.lsp4j.CallHierarchyOutgoingCallsParams;
import org.eclipse.lsp4j.CallHierarchyPrepareParams;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.ColorInformation;
import org.eclipse.lsp4j.ColorPresentation;
import org.eclipse.lsp4j.ColorPresentationParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.CreateFilesParams;
import org.eclipse.lsp4j.DeclarationParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DeleteFilesParams;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentColorParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentLink;
import org.eclipse.lsp4j.DocumentLinkParams;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.ImplementationParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.LinkedEditingRangeParams;
import org.eclipse.lsp4j.LinkedEditingRanges;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Moniker;
import org.eclipse.lsp4j.MonikerParams;
import org.eclipse.lsp4j.PrepareRenameParams;
import org.eclipse.lsp4j.PrepareRenameResult;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.ResolveTypeHierarchyItemParams;
import org.eclipse.lsp4j.SelectionRange;
import org.eclipse.lsp4j.SelectionRangeParams;
import org.eclipse.lsp4j.SemanticTokens;
import org.eclipse.lsp4j.SemanticTokensDelta;
import org.eclipse.lsp4j.SemanticTokensDeltaParams;
import org.eclipse.lsp4j.SemanticTokensParams;
import org.eclipse.lsp4j.SemanticTokensRangeParams;
import org.eclipse.lsp4j.SetTraceParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.TypeDefinitionParams;
import org.eclipse.lsp4j.TypeHierarchyItem;
import org.eclipse.lsp4j.TypeHierarchyParams;
import org.eclipse.lsp4j.WillSaveTextDocumentParams;
import org.eclipse.lsp4j.WorkDoneProgressCancelParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

/**
 * Wraps a {@link LanguageServer} to an {@link IDELanguageServer }
 */
public class LanguageServerWrapper implements IDELanguageServer {

    private final LanguageServer server;
    private final IDETextDocumentService textDocumentServiceWrapper;
    private final IDEWorkspaceService workspaceService;

    public LanguageServerWrapper(LanguageServer server) {
        this.server = server;
        this.textDocumentServiceWrapper = new TextDocumentServiceWrapper(server.getTextDocumentService());
        this.workspaceService = new WorkspaceServiceWrapper(server.getWorkspaceService());
    }
    
    public LanguageServer getServer() {
        return this.server;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        return server.initialize(params);
    }

    @Override
    public void initialized(InitializedParams params) {
        server.initialized(params);
    }

    @Override
    public void initialized() {
        server.initialized();
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return server.shutdown();
    }

    @Override
    public void exit() {
        server.exit();
    }

    @Override
    public IDETextDocumentService getTextDocumentService() {
        return textDocumentServiceWrapper;
    }

    @Override
    public IDEWorkspaceService getWorkspaceService() {
        return workspaceService;
    }

    @Override
    public void cancelProgress(WorkDoneProgressCancelParams params) {
        server.cancelProgress(params);
    }

    @Override
    public void setTrace(SetTraceParams params) {
    }
    
    class TextDocumentServiceWrapper implements IDETextDocumentService {
        
        private final TextDocumentService service;

        public TextDocumentServiceWrapper(TextDocumentService service) {
            this.service = service;
        }
        
        @Override
        public CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(CallHierarchyIncomingCallsParams p1) {
            return service.callHierarchyIncomingCalls(p1);
        }

        @Override
        public CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(CallHierarchyOutgoingCallsParams p1) {
            return service.callHierarchyOutgoingCalls(p1);
        }

        @Override
        public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams p1) {
            return service.codeAction(p1);
        }

        @Override
        public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams p1) {
            return service.codeLens(p1);
        }

        @Override
        public CompletableFuture<List<ColorPresentation>> colorPresentation(ColorPresentationParams p1) {
            return service.colorPresentation(p1);
        }

        @Override
        public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams p1) {
            return service.completion(p1);
        }

        @Override
        public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> declaration(DeclarationParams p1) {
            return service.declaration(p1);
        }

        @Override
        public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams p1) {
            return service.definition(p1);
        }

        @Override
        public void didChange(DidChangeTextDocumentParams p1) {
            service.didChange(p1);
        }

        @Override
        public void didClose(DidCloseTextDocumentParams p1) {
            service.didClose(p1);
        }

        @Override
        public void didOpen(DidOpenTextDocumentParams p1) {
            service.didOpen(p1);
        }

        @Override
        public void didSave(DidSaveTextDocumentParams p1) {
            service.didSave(p1);
        }

        @Override
        public CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams p1) {
            return service.documentColor(p1);
        }

        @Override
        public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams p1) {
            return service.documentHighlight(p1);
        }

        @Override
        public CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams p1) {
            return service.documentLink(p1);
        }

        @Override
        public CompletableFuture<DocumentLink> documentLinkResolve(DocumentLink p1) {
            return service.documentLinkResolve(p1);
        }

        @Override
        public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams p1) {
            return service.documentSymbol(p1);
        }

        @Override
        public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams p1) {
            return service.foldingRange(p1);
        }

        @Override
        public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams p1) {
            return service.formatting(p1);
        }

        @Override
        public CompletableFuture<Hover> hover(HoverParams p1) {
            return service.hover(p1);
        }

        @Override
        public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> implementation(ImplementationParams p1) {
            return service.implementation(p1);
        }

        @Override
        public CompletableFuture<LinkedEditingRanges> linkedEditingRange(LinkedEditingRangeParams p1) {
            return service.linkedEditingRange(p1);
        }

        @Override
        public CompletableFuture<List<Moniker>> moniker(MonikerParams p1) {
            return service.moniker(p1);
        }

        @Override
        public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams p1) {
            return service.onTypeFormatting(p1);
        }

        @Override
        public CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(CallHierarchyPrepareParams p1) {
            return service.prepareCallHierarchy(p1);
        }

        @Override
        public CompletableFuture<Either<Range, PrepareRenameResult>> prepareRename(PrepareRenameParams p1) {
            return service.prepareRename(p1);
        }

        @Override
        public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams p1) {
            return service.rangeFormatting(p1);
        }

        @Override
        public CompletableFuture<List<? extends Location>> references(ReferenceParams p1) {
            return service.references(p1);
        }

        @Override
        public CompletableFuture<WorkspaceEdit> rename(RenameParams p1) {
            return service.rename(p1);
        }

        @Override
        public CompletableFuture<CodeAction> resolveCodeAction(CodeAction p1) {
            return service.resolveCodeAction(p1);
        }

        @Override
        public CompletableFuture<CodeLens> resolveCodeLens(CodeLens p1) {
            return service.resolveCodeLens(p1);
        }

        @Override
        public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem p1) {
            return service.resolveCompletionItem(p1);
        }

        @Override
        public CompletableFuture<TypeHierarchyItem> resolveTypeHierarchy(ResolveTypeHierarchyItemParams p1) {
            return service.resolveTypeHierarchy(p1);
        }

        @Override
        public CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams p1) {
            return service.selectionRange(p1);
        }

        @Override
        public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams p1) {
            return service.semanticTokensFull(p1);
        }

        @Override
        public CompletableFuture<Either<SemanticTokens, SemanticTokensDelta>> semanticTokensFullDelta(SemanticTokensDeltaParams p1) {
            return service.semanticTokensFullDelta(p1);
        }

        @Override
        public CompletableFuture<SemanticTokens> semanticTokensRange(SemanticTokensRangeParams p1) {
            return service.semanticTokensRange(p1);
        }

        @Override
        public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams p1) {
            return service.signatureHelp(p1);
        }

        @Override
        public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(TypeDefinitionParams p1) {
            return service.typeDefinition(p1);
        }

        @Override
        public CompletableFuture<TypeHierarchyItem> typeHierarchy(TypeHierarchyParams p1) {
            return service.typeHierarchy(p1);
        }

        @Override
        public void willSave(WillSaveTextDocumentParams p1) {
            service.willSave(p1);
        }

        @Override
        public CompletableFuture<List<TextEdit>> willSaveWaitUntil(WillSaveTextDocumentParams p1) {
            return service.willSaveWaitUntil(p1);
        }
    }
    
    class WorkspaceServiceWrapper implements IDEWorkspaceService {
        
        private final WorkspaceService service;

        public WorkspaceServiceWrapper(WorkspaceService service) {
            this.service = service;
        }
        
        @Override
        public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
            return service.executeCommand(params);
        }

        @Override
        public CompletableFuture<List<? extends SymbolInformation>> symbol(WorkspaceSymbolParams params) {
            return service.symbol(params);
        }

        @Override
        public void didChangeConfiguration(DidChangeConfigurationParams params) {
            service.didChangeConfiguration(params);
        }

        @Override
        public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
            service.didChangeWatchedFiles(params);
        }

        @Override
        public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
            service.didChangeWorkspaceFolders(params);
        }

        @Override
        public CompletableFuture<WorkspaceEdit> willCreateFiles(CreateFilesParams params) {
            return service.willCreateFiles(params);
        }

        @Override
        public void didCreateFiles(CreateFilesParams params) {
            service.didCreateFiles(params);
        }

        @Override
        public CompletableFuture<WorkspaceEdit> willRenameFiles(RenameFilesParams params) {
            return service.willRenameFiles(params);
        }

        @Override
        public void didRenameFiles(RenameFilesParams params) {
            service.didRenameFiles(params);
        }

        @Override
        public CompletableFuture<WorkspaceEdit> willDeleteFiles(DeleteFilesParams params) {
            return service.willDeleteFiles(params);
        }

        @Override
        public void didDeleteFiles(DeleteFilesParams params) {
            service.didDeleteFiles(params);
        }
    }
}
