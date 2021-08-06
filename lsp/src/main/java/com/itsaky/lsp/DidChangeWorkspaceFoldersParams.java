package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class DidChangeWorkspaceFoldersParams {
    @SerializedName("event")
    public WorkspaceFoldersChangeEvent event;
}
