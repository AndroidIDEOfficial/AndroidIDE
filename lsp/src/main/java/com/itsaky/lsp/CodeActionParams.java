package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class CodeActionParams {
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("context")
    public CodeActionContext context = new CodeActionContext();
}
