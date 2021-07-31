package com.itsaky.lsp;

import com.google.gson.JsonElement;
import java.net.URI;
import java.util.List;

public class InitializeParams {
    public int processId;
    public String rootPath;
    public URI rootUri;
    public JsonElement initializationOptions;
    public String trace;
    public List<WorkspaceFolder> workspaceFolders;
}
