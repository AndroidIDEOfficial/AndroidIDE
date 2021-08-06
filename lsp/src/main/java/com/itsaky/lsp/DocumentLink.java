package com.itsaky.lsp;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class DocumentLink {
    
    @SerializedName("range")
    public Range range;
    
    @SerializedName("target")
    public String target;
    
    @SerializedName("data")
    public JsonArray data;
}
