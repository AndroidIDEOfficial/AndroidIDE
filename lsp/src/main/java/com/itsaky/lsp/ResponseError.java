package com.itsaky.lsp;

public class ResponseError {
    public int code;
    public String message;
    public Object data;

    public ResponseError() {}

    public ResponseError(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
