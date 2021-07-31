package com.itsaky.lsp;

import com.google.gson.JsonElement;

public interface LanguageClient {
    void javaProgressStart(JavaStartProgressParams params);
    void javaProgressReport(JavaReportProgressParams params);
    void javaProgressEnd();
    void publishDiagnostics(PublishDiagnosticsParams params);
    void showMessage(ShowMessageParams params);
    void registerCapability(String method, JsonElement options);
    void customNotification(String method, JsonElement params);
}
