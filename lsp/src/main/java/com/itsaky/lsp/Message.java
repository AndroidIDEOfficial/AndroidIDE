package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("jsonrpc")
    public String jsonrpc;
    
    @SerializedName("id")
    public Integer id;
    
    @SerializedName("method")
    public String method;
    
    @SerializedName("params")
    public JsonElement params;
}
