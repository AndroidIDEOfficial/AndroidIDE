package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class ReferenceParams extends TextDocumentPositionParams {
    
    @SerializedName("context")
    public ReferenceContext context;
}
