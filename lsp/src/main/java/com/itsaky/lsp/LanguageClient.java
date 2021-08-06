package com.itsaky.lsp;

import com.google.gson.JsonElement;
import java.io.File;

public interface LanguageClient {
    void javaProgressStart(JavaStartProgressParams params);
    void javaProgressReport(JavaReportProgressParams params);
    void javaProgressEnd();
    
    void publishDiagnostics(PublishDiagnosticsParams params);
    void signatureHelp(SignatureHelp signature, File file);
    void javaColors(JavaColors colors);
    
    void showMessage(ShowMessageParams params);
    void customNotification(String method, JsonElement params);
    
    void registerCapability(String method, JsonElement options);
    
    void onServerStarted(int currentId);
}
