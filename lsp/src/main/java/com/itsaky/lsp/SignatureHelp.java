package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SignatureHelp {
    @SerializedName("signatures")
    public List<SignatureInformation> signatures;
    
    @SerializedName("activeSignature")
    public Integer activeSignature;
    
    @SerializedName("activeParameter")
    public Integer activeParameter;

    public SignatureHelp() {}

    public SignatureHelp(List<SignatureInformation> signatures, Integer activeSignature, Integer activeParameter) {
        this.signatures = signatures;
        this.activeSignature = activeSignature;
        this.activeParameter = activeParameter;
    }
}
