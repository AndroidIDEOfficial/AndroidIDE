package com.itsaky.lsp;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class InitializeResult {
    
    @SerializedName("capabilities")
    public JsonObject capabilities;

    public InitializeResult() {}

    public InitializeResult(JsonObject capabilities) {
        this.capabilities = capabilities;
    }
}
