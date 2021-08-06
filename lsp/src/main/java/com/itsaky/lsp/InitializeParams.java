package com.itsaky.lsp;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.List;
import java.util.Set;

public class InitializeParams {
    @SerializedName("processId")
    public int processId;
    
    @SerializedName("rootPath")
    public String rootPath;
    
    @SerializedName("rootUri")
    public URI rootUri;
    
    @SerializedName("roots")
    public Set<URI> roots;
    
    @SerializedName("initializationOptions")
    public JsonElement initializationOptions;
    
    @SerializedName("trace")
    public String trace;
    
    @SerializedName("workspaceFolders")
    public List<WorkspaceFolder> workspaceFolders;
}
