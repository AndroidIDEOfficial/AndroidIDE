package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class RequestMessage {
    
    @SerializedName("id")
    public String id;
    
    @SerializedName("method")
    public String method;
    
    @SerializedName("params")
    public JsonElement params;
}
