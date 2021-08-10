package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class ReferenceContext {
    
    @SerializedName("includeDeclaration")
    public boolean includeDeclaration;
    
    public ReferenceContext(){}

    public ReferenceContext(boolean includeDeclaration) {
        this.includeDeclaration = includeDeclaration;
    }
}
