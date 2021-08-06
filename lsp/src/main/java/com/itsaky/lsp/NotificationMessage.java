package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class NotificationMessage {
    
    @SerializedName("method")
    public String method;

    @SerializedName("params")
    public JsonElement params;
}
