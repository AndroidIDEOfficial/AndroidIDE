package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class DidChangeConfigurationParams {
    @SerializedName("settings")
    public JsonElement settings;
}
