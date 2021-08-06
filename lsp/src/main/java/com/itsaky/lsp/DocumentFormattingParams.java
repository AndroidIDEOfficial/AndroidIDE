package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DocumentFormattingParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
    
    @SerializedName("options")
    public FormattingOptions options;
}
