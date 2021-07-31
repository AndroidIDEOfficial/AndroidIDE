package com.itsaky.lsp;

import java.util.List;
import java.util.Optional;

public class LanguageServer {
    public InitializeResult initialize(InitializeParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void initialized() {
        throw new RuntimeException("Unimplemented");
    }

    public void shutdown() {
        throw new RuntimeException("Unimplemented");
    }

    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didOpenTextDocument(DidOpenTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didChangeTextDocument(DidChangeTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void willSaveTextDocument(WillSaveTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<TextEdit> willSaveWaitUntilTextDocument(WillSaveTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didSaveTextDocument(DidSaveTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didCloseTextDocument(DidCloseTextDocumentParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<CompletionList> completion(TextDocumentPositionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public CompletionItem resolveCompletionItem(CompletionItem params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<Hover> hover(TextDocumentPositionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<SignatureHelp> signatureHelp(TextDocumentPositionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<List<Location>> gotoDefinition(TextDocumentPositionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<List<Location>> findReferences(ReferenceParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<SymbolInformation> documentSymbol(DocumentSymbolParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<SymbolInformation> workspaceSymbols(WorkspaceSymbolParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<CodeAction> codeAction(CodeActionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<CodeLens> codeLens(CodeLensParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public CodeLens resolveCodeLens(CodeLens params) {
        throw new RuntimeException("Unimplemented");
    }

    public Optional<RenameResponse> prepareRename(TextDocumentPositionParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public WorkspaceEdit rename(RenameParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<TextEdit> formatting(DocumentFormattingParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<FoldingRange> foldingRange(FoldingRangeParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public List<DocumentLink> documentLink(DocumentLinkParams params) {
        throw new RuntimeException("Unimplemented");
    }

    public void doAsyncWork() {}
}
