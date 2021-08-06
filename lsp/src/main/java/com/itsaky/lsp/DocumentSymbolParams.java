package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DocumentSymbolParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;

    public DocumentSymbolParams() {}

    public DocumentSymbolParams(TextDocumentIdentifier textDocument) {
        this.textDocument = textDocument;
    }
}
