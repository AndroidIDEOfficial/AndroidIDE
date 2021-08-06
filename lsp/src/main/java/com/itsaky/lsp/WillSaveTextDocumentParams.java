package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class WillSaveTextDocumentParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("reason")
    public int reason;
}
