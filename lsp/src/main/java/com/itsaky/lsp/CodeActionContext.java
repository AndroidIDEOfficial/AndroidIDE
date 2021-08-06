package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class CodeActionContext {
    @SerializedName("diagnostics")
    public List<Diagnostic> diagnostics = new ArrayList<>();
    
    @SerializedName("only")
    public List<String> only;
}
