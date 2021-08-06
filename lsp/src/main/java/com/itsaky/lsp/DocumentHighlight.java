package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DocumentHighlight {
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("kind")
    public int kind;
}
