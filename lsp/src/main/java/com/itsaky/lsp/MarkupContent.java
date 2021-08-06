package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class MarkupContent {
    @SerializedName("kind")
    public String kind;
    
    @SerializedName("value")
    public String value;

    public MarkupContent() {}

    public MarkupContent(String kind, String value) {
        this.kind = kind;
        this.value = value;
    }
}
