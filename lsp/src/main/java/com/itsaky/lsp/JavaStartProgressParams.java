package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class JavaStartProgressParams {
    
    @SerializedName("message")
    private String message;

    public JavaStartProgressParams() {}

    public JavaStartProgressParams(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
