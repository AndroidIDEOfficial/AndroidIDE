package com.itsaky.lsp;

import com.google.gson.JsonElement;

public class CodeLens {
    public Range range;
    public Command command;
    public JsonElement data;

    public CodeLens() {}

    public CodeLens(Range range, Command command, JsonElement data) {
        this.range = range;
        this.command = command;
        this.data = data;
    }
}
