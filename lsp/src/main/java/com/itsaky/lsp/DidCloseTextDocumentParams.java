package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DidCloseTextDocumentParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument = new TextDocumentIdentifier();
}
