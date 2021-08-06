package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class TextDocumentContentChangeEvent {
    @SerializedName("range")
    public Range range;
    
    @SerializedName("rangeLength")
    public Integer rangeLength;
    
    @SerializedName("text")
    public String text;
}
