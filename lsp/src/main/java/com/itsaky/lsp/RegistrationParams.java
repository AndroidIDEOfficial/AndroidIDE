package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class RegistrationParams {
    
    @SerializedName("id")
    public String id;
    
    @SerializedName("method")
    public String method;
    
    @SerializedName("registerOptions")
    public JsonElement registerOptions;
}
