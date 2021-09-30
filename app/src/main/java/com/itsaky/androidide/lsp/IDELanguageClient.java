package com.itsaky.androidide.lsp;

import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ShowDocumentParams;
import org.eclipse.lsp4j.ShowDocumentResult;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageServer;

/**
 * AndroidIDE specific implementation of the LanguageClient
 */
public abstract class IDELanguageClient implements LanguageClient {

    protected static final Gson gson = new Gson();
    protected static final Logger LOG = Logger.instance("IDELanguageClient");

    protected EditorActivityProvider activityProvider;

    private final StarterListener starterListener;
    private final OnConnectedListener onConnectedListener;

    private boolean isConnected;

    public IDELanguageClient(StarterListener starterListener, OnConnectedListener onConnectedListener) {
        this.starterListener = starterListener;
        this.onConnectedListener = onConnectedListener;
    }

    public void setActivityProvider(EditorActivityProvider provider) {
        this.activityProvider = provider;
    }

    /**
     * Are we connected to the server?
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Called when the LanguageServer is connected successfully
     */
    protected void onServerConnected(LanguageServer server) {
        this.isConnected = true;
        if (onConnectedListener != null)
            onConnectedListener.onConnected(server);
    }

    /**
     * Called when the connection to the LanguageServer was cancelled.
     */
    protected void onServerDisconnected() {
        this.isConnected = false;
    }

    /**
     * Called when the ServerSocket is started. At this point, the {@link LSPClient} waits for the server to connect. <br>
     * This is (probably) the proper time to start your language server as ServerSocket is waiting for a client to connect.
     */
    protected void startServer() {
        if (this.starterListener != null) {
            starterListener.startServer();
        }
    }

    protected EditorActivity activity() {
        if (activityProvider == null) return null;
        return activityProvider.provide();
    }

    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show locations in EditorActivity
     */
    public void showLocations(List<? extends Location> locations) {

    }

    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show locations in EditorActivity
     */
    public void showLocationLinks(List<? extends LocationLink> locations) {

    }

    /**
     * Usually called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show a specific document in EditorActivity and select the specified range
     */
    @Override
    public CompletableFuture<ShowDocumentResult> showDocument(ShowDocumentParams params) {
        ShowDocumentResult result = new ShowDocumentResult();
        boolean success = false;
        
        if(params != null && params.getUri() != null && params.getSelection() != null) {
            File file = new File(URI.create(params.getUri()));
            if(file.exists() && file.isFile() && FileUtils.isUtf8(file)) {
                final Range range = params.getSelection();
                EditorFragment frag = activity().getPagerAdapter().getFrag(activity().getBinding().tabs.getSelectedTabPosition());
                if(frag != null
                   && frag.getFile() != null
                   && frag.getEditor() != null
                   && frag.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                    if(LSPUtils.isEqual(range.getStart(), range.getEnd())) {
                        frag.getEditor().setSelection(range.getStart().getLine(), range.getStart().getCharacter());
                    } else {
                        frag.getEditor().setSelectionRegion(range.getStart().getLine(), range.getStart().getCharacter(), range.getEnd().getLine(), range.getEnd().getCharacter());
                    }
                } else {
                    activity().openFileAndSelect(file, range);
                }
                success = true;
            }
        }
        
        result.setSuccess(success);
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public void telemetryEvent(Object p1) {
        LOG.info("telemetryEvent: ", gson.toJson(p1));
    }

    /**
     * Reports connection progress
     */
    protected abstract void connectionReport(String message);

    /**
     * Called when there was an error connecting to server.
     */
    protected abstract void connectionError(Throwable th);

    public static interface StarterListener {
        void startServer();
    }

    public static interface OnConnectedListener {
        void onConnected(LanguageServer server);
    }
}
