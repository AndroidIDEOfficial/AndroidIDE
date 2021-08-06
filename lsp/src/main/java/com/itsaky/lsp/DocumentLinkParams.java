package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DocumentLinkParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;

    public DocumentLinkParams() {}

    public DocumentLinkParams(TextDocumentIdentifier textDocument) {
        this.textDocument = textDocument;
    }
}
