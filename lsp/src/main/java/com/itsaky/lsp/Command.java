package com.itsaky.lsp;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class Command {
    
    @SerializedName("title")
    public String title;
    
    @SerializedName("command")
    public String command;
    
    @SerializedName("arguments")
    public JsonArray arguments;

    public Command() {}

    public Command(String title, String command, JsonArray arguments) {
        this.title = title;
        this.command = command;
        this.arguments = arguments;
    }
}
