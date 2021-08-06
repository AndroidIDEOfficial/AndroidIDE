package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkspaceEdit {
    @SerializedName("changes")
    public Map<URI, List<TextEdit>> changes = new HashMap<>();
}
