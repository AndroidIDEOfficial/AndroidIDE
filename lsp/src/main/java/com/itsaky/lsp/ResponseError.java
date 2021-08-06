package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class ResponseError {
    
    @SerializedName("code")
    public int code;
    
    @SerializedName("message")
    public String message;
    
    @SerializedName("data")
    public Object data;

    public ResponseError() {}

    public ResponseError(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
