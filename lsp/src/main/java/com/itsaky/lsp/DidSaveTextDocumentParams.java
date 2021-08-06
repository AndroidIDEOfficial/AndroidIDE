package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DidSaveTextDocumentParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("text")
    public String text;
}
