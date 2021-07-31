package com.itsaky.lsp;

public class TextDocumentPositionParams {
    public TextDocumentIdentifier textDocument;
    public Position position;

    public TextDocumentPositionParams() {}

    public TextDocumentPositionParams(TextDocumentIdentifier textDocument, Position position) {
        this.textDocument = textDocument;
        this.position = position;
    }
}
