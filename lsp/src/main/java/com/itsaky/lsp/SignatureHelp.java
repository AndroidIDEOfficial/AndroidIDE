package com.itsaky.lsp;

import java.util.List;

public class SignatureHelp {
    public List<SignatureInformation> signatures;
    public Integer activeSignature, activeParameter;

    public SignatureHelp() {}

    public SignatureHelp(List<SignatureInformation> signatures, Integer activeSignature, Integer activeParameter) {
        this.signatures = signatures;
        this.activeSignature = activeSignature;
        this.activeParameter = activeParameter;
    }
}
