package com.itsaky.lsp;

public class DocumentSymbolParams {
    public TextDocumentIdentifier textDocument;

    public DocumentSymbolParams() {}

    public DocumentSymbolParams(TextDocumentIdentifier textDocument) {
        this.textDocument = textDocument;
    }
}
