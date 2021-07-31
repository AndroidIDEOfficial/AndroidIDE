package com.itsaky.lsp;

import com.google.gson.JsonObject;

public class InitializeResult {
    public JsonObject capabilities;

    public InitializeResult() {}

    public InitializeResult(JsonObject capabilities) {
        this.capabilities = capabilities;
    }
}
