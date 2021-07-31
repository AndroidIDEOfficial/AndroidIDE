package com.itsaky.lsp;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkspaceEdit {
    public Map<URI, List<TextEdit>> changes = new HashMap<>();
}
