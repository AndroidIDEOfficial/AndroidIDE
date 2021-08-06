package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SignatureInformation {
    
    @SerializedName("label")
    public String label;
    
    @SerializedName("documentation")
    public MarkupContent documentation;
    
    @SerializedName("parameters")
    public List<ParameterInformation> parameters;

    public SignatureInformation() {}

    public SignatureInformation(String label, MarkupContent documentation, List<ParameterInformation> parameters) {
        this.label = label;
        this.documentation = documentation;
        this.parameters = parameters;
    }
}
