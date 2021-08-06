package com.itsaky.lsp;

import com.google.gson.annotations.SerializedName;

public class JavaReportProgressParams {
    @SerializedName("message")
    private String message;
    
    @SerializedName("increment")
    private int increment = -1;

    public JavaReportProgressParams() {}

    public JavaReportProgressParams(String message) {
        this.message = message;
    }

    public JavaReportProgressParams(String message, int increment) {
        this.message = message;
        this.increment = increment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}
