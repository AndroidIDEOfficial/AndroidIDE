package com.itsaky.lsp;

import com.google.gson.JsonArray;

public class Command {
    public String title, command;
    public JsonArray arguments;

    public Command() {}

    public Command(String title, String command, JsonArray arguments) {
        this.title = title;
        this.command = command;
        this.arguments = arguments;
    }
}
