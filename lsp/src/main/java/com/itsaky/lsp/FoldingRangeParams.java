package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class FoldingRangeParams {
    
    @SerializedName("textDocument")
    public TextDocumentIdentifier textDocument;
}
