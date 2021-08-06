package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class CodeLens {
    @SerializedName("range")
    public Range range;
    
    @SerializedName("command")
    public Command command;
    
    @SerializedName("data")
    public JsonElement data;

    public CodeLens() {}

    public CodeLens(Range range, Command command, JsonElement data) {
        this.range = range;
        this.command = command;
        this.data = data;
    }
}
