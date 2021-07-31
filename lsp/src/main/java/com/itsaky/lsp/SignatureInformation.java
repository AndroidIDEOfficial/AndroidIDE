package com.itsaky.lsp;

import java.util.List;

public class SignatureInformation {
    public String label;
    public MarkupContent documentation;
    public List<ParameterInformation> parameters;

    public SignatureInformation() {}

    public SignatureInformation(String label, MarkupContent documentation, List<ParameterInformation> parameters) {
        this.label = label;
        this.documentation = documentation;
        this.parameters = parameters;
    }
}
