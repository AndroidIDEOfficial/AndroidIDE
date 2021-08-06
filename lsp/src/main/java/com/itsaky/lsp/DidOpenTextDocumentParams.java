package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DidOpenTextDocumentParams {
    
    @SerializedName("textDocument")
    public TextDocumentItem textDocument = new TextDocumentItem();

    public DidOpenTextDocumentParams() {}

    public DidOpenTextDocumentParams(TextDocumentItem textDocument) {
        this.textDocument = textDocument;
    }
}
