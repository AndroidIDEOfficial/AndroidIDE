package com.itsaky.androidide.lsp;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.SemanticHighlight;
import com.itsaky.lsp.services.IDELanguageClient;
import com.itsaky.lsp.services.IDELanguageServer;
import com.itsaky.toaster.Toaster;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java9.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.LogTraceParams;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.ParameterInformation;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ShowDocumentParams;
import org.eclipse.lsp4j.ShowDocumentResult;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureInformation;
import org.eclipse.lsp4j.TextEdit;

/**
 * AndroidIDE specific implementation of the LanguageClient
 */
public abstract class IDELanguageClientImpl implements IDELanguageClient {

    protected static final Gson gson = new Gson();
    protected static final Logger LOG = Logger.instance("AbstractLanguageClient");

    protected EditorActivityProvider activityProvider;
    
    private final Map<File, List<Diagnostic>> diagnostics = new HashMap<>();
    private final StarterListener starterListener;
    private final OnConnectedListener onConnectedListener;

    private boolean isConnected;
    
    public static final int DIAGNOSTIC_TRANSITION_DURATION = 80;

    public IDELanguageClientImpl(StarterListener starterListener, OnConnectedListener onConnectedListener) {
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
    protected void onServerConnected(IDELanguageServer server) {
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
    protected void startServerViaCommand() {
        if (this.starterListener != null) {
            starterListener.startServer();
        }
    }

    protected EditorActivity activity() {
        if (activityProvider == null) return null;
        return activityProvider.provide();
    }

    @Override
    public void semanticHighlights(SemanticHighlight highlights) {
        final File file = new File(URI.create(highlights.uri));
        final EditorFragment editor = activity().getPagerAdapter().findEditorByFile(file);
        
        if(editor != null) {
            editor.getEditor().setSemanticHighlights(highlights);
        }
    }
    
    public void showDiagnostic(Diagnostic diagnostic, final CodeEditor editor) {
        if(activity() == null || activity().getDiagnosticBinding() == null) {
            return;
        }
        
        if(diagnostic == null) {
            hideDiagnostics();
            return;
        }
        
        final LayoutDiagnosticInfoBinding binding = activity().getDiagnosticBinding();
        binding.getRoot().setText(diagnostic.getMessage());
        
        binding.getRoot().setVisibility(View.VISIBLE);
        
        final float[] cursor = editor.getCursorPosition();

        float x = editor.updateCursorAnchor() - (binding.getRoot().getWidth() / 2);
        float y = activity().getBinding().editorAppBarLayout.getHeight() + (cursor[0] - editor.getRowHeight() - editor.getOffsetY() - binding.getRoot().getHeight());
        binding.getRoot().setX(x);
        binding.getRoot().setY(y);
        activity().positionViewWithinScreen(binding.getRoot(), x, y);
        
    }
    
    public void hideDiagnostics() {
        if(activity() == null || activity().getDiagnosticBinding() == null) {
            return;
        }
        
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds());
        set.addTransition(new Fade());
        set.setDuration(DIAGNOSTIC_TRANSITION_DURATION);
        
        activity().getDiagnosticBinding().getRoot().setVisibility(View.GONE);
    }
    
    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show signature help in EditorActivity
     */
    public void showSignatureHelp(SignatureHelp signature, File file) {
        if(signature == null || signature.getSignatures() == null) {
            hideSignatureHelp();
            return;
        }
        SignatureInformation info = signatureWithMostParams(signature);
        if(info == null) return;
        activity().getBinding().symbolText.setText(formatSignature(info, signature.getActiveParameter()));
        final EditorFragment frag = activity().getPagerAdapter().findEditorByFile(file);
        if(frag != null) {
            final CodeEditor editor = frag.getEditor();
            final float[] cursor = editor.getCursorPosition();

            float x = editor.updateCursorAnchor() - (activity().getBinding().symbolText.getWidth() / 2);
            float y = activity().getBinding().editorAppBarLayout.getHeight() + (cursor[0] - editor.getRowHeight() - editor.getOffsetY() - activity().getBinding().symbolText.getHeight());
            TransitionManager.beginDelayedTransition(activity().getBinding().getRoot());
            activity().getBinding().symbolText.setVisibility(View.VISIBLE);
            activity().positionViewWithinScreen(activity().getBinding().symbolText, x, y);
        }
    }
    
    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to hide signature help in EditorActivity
     */
    public void hideSignatureHelp() {
        if(activity() == null) return;
        TransitionManager.beginDelayedTransition(activity().getBinding().getRoot());
        activity().getBinding().symbolText.setVisibility(View.GONE);
    }
     
    /**
     * Find the signature with most parameters
     *
     * @param signature The SignatureHelp provided by @{link IDELanguageServer}
     */
    private SignatureInformation signatureWithMostParams(SignatureHelp signature) {
        SignatureInformation signatureWithMostParams = null;
        int mostParamCount = 0;
        final List<SignatureInformation> signatures = signature.getSignatures();
        for(int i=0;i<signatures.size();i++) {
            final SignatureInformation info = signatures.get(i);
            int count = info.getParameters().size();
            if(mostParamCount < count) {
                mostParamCount = count;
                signatureWithMostParams = info;
            }
        }
        return signatureWithMostParams;
    }

    /**
     * Formats (highlights) a method signature
     *
     * @param signature Signature information
     * @param paramIndex Currently active parameter index
     */
    private CharSequence formatSignature(SignatureInformation signature, int paramIndex) {
        String name = signature.getLabel();
        name = name.substring(0, name.indexOf("("));

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(name, new ForegroundColorSpan(0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        List<ParameterInformation> params = signature.getParameters();
        for(int i=0;i<params.size();i++) {
            int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
            final ParameterInformation info = params.get(i);
            if(i == params.size() - 1) {
                sb.append(info.getLabel().getLeft() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(info.getLabel().getLeft() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(",", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
            }
        }
        sb.append(")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams params) {
        boolean error = params == null || params.getDiagnostics() == null || params.getDiagnostics().isEmpty();
        activity().handleDiagnosticsResultVisibility(error);
        
        if(error) return;
        
        File file = new File(URI.create(params.getUri()));
        if(!file.exists() || !file.isFile()) return;
        
        diagnostics.put(file, params.getDiagnostics());
        activity().getDiagnosticsList().setAdapter(newDiagnosticsAdapter());
        
        EditorFragment editor = null;
        if(activity().getPagerAdapter() != null && (editor = activity().getPagerAdapter().findEditorByFile(new File(URI.create(params.getUri())))) != null) {
            editor.setDiagnostics(params.getDiagnostics());
        }
    }
    
    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show locations in EditorActivity
     */
    public void showLocations(List<? extends Location> locations) {
        
        // Cannot show anything if the activity() is null
        if(activity() == null) {
            return;
        }
        
        boolean error = locations == null || locations.isEmpty();
        activity().handleSearchResultVisibility(error);


        if(error) {
            activity().getSearchResultList().setAdapter(new SearchListAdapter(null, null, null));
            return;
        }

        final Map<File, List<SearchResult>> results = new HashMap<>();
        for(int i=0;i<locations.size();i++) {
            try {
                final Location loc = locations.get(i);
                if(loc == null || loc.getUri() == null || loc.getRange() == null) continue;
                final File file = new File(URI.create(loc.getUri()));
                if(!file.exists() || !file.isFile()) continue;
                EditorFragment frag = activity().getPagerAdapter().findEditorByFile(file);
                Content content;
                if(frag != null && frag.getEditor() != null)
                    content = frag.getEditor().getText();
                else content = new Content(null, FileIOUtils.readFile2String(file));
                final List<SearchResult> matches = results.containsKey(file) ? results.get(file) : new ArrayList<>();
                matches.add(
                    new SearchResult(
                        loc.getRange(),
                        file,
                        content.getLineString(loc.getRange().getStart().getLine()),
                        content.subContent(
                            loc.getRange().getStart().getLine(),
                            loc.getRange().getStart().getCharacter(),
                            loc.getRange().getEnd().getLine(),
                            loc.getRange().getEnd().getCharacter()
                        ).toString()
                    )
                );
                results.put(file, matches);
            } catch (Throwable th) {
                LOG.error(ThrowableUtils.getFullStackTrace(th));
            }
        }

        activity().handleSearchResults(results);
    }

    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show location links in EditorActivity.
     * These location links are mapped as {@link org.eclipse.lsp4j.Location Location} and then {@link #showLocations(List) } is called.
     */
    public void showLocationLinks(List<? extends LocationLink> locations) {
        
        if(locations == null || locations.size() <= 0) {
            return;
        }
        
        showLocations(locations
            .stream()
                .filter(l -> l != null)
                .map(l -> asLocation(l))
                .filter(l -> l != null)
                .collect(Collectors.toList())
            );
    }
    
    /**
     * Perform the given {@link CodeAction}
     *
     * @param editor The {@link CodeEditor} that invoked the code action request.
     *            This is required to reduce the time finding the code action from the edits.
     * @param action The action to perform
     */
    public void performCodeAction(CodeEditor editor, CodeAction action) {
        if(activity() == null || editor == null || action == null) {
            StudioApp.getInstance().toast(R.string.msg_cannot_perform_fix, Toaster.Type.ERROR);
            return;
        }
        
        final ProgressSheet progress = new ProgressSheet();
        progress.setSubMessageEnabled(false);
        progress.setWelcomeTextEnabled(false);
        progress.setCancelable(false);
        progress.setMessage(activity().getString(R.string.msg_performing_fixes));
        progress.show(activity().getSupportFragmentManager(), "quick_fix_progress");
        
        new TaskExecutor().executeAsyncProvideError(() -> performCodeActionAsync(editor, action), (a, b) -> {
            final Boolean complete = a;
            final Throwable error = b;
            
            progress.dismiss();
            
            if(complete == null || error != null || !complete.booleanValue()) {
                StudioApp.getInstance().toast(R.string.msg_cannot_perform_fix, Toaster.Type.ERROR);
                LOG.error("Quickfix error", error);
                return;
            }
        });
    }

    /**
     * Usually called {@link CodeEditor} to show a specific document in EditorActivity and select the specified range
     */
    @Override
    public CompletableFuture<ShowDocumentResult> showDocument(ShowDocumentParams params) {
        ShowDocumentResult result = new ShowDocumentResult();
        boolean success = false;
        
        if(activity() == null) {
            result.setSuccess(success);
            return CompletableFuture.completedFuture(result);
        }
        
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
    public void telemetryEvent(Object params) {
        FirebaseCrashlytics.getInstance().log("LanguageServer[" + this.getClass().getName() + "][TelemetryEvent]\n" + params.toString());
    }

    @Override
    public void logMessage(MessageParams params) {
        if(params.getType() == MessageType.Error) {
            LOG.error(params.getMessage());
        } else if(params.getType() == MessageType.Info) {
            LOG.info(params.getMessage());
        } else if(params.getType() == MessageType.Warning) {
            LOG.warn(params.getMessage());
        } else if(params.getType() == MessageType.Log) {
            LOG.debug(params.getMessage());
        }
    }

    @Override
    public void logTrace(LogTraceParams params) {
        
    }
    
    private Location asLocation(LocationLink link) {
        if(link == null || link.getTargetRange() == null || link.getTargetUri() == null) return null;
        final Location location = new Location();
        location.setUri(link.getTargetUri());
        location.setRange(link.getTargetRange());
        return location;
    }

    private List<DiagnosticGroup> mapAsGroup(Map<File, List<Diagnostic>> diags) {
        List<DiagnosticGroup> groups = new ArrayList<>();
        if(diags == null || diags.size() <= 0)
            return groups;
        for(File file : diags.keySet()) {
            List<Diagnostic> fileDiags = diags.get(file);
            if(fileDiags == null || fileDiags.size() <= 0)
                continue;
            DiagnosticGroup group = new DiagnosticGroup(R.drawable.ic_language_java, file, fileDiags);
            groups.add(group);
        }
        return groups;
    }
    
    private Boolean performCodeActionAsync(final CodeEditor editor, final CodeAction action) {
        final Map <String, List<TextEdit>> edits = action.getEdit().getChanges();
        if(edits == null || edits.isEmpty()) {
            return Boolean.FALSE;
        }
        
        for(Map.Entry<String, List<TextEdit>> entry : edits.entrySet()) {
            final File file = new File(URI.create(entry.getKey()));
            if(!file.exists()) continue;

            for(TextEdit edit : entry.getValue()) {
                final String editorFilepath = editor.getFile() == null ? "" : editor.getFile().getAbsolutePath();
                if(file.getAbsolutePath().equals(editorFilepath)) {
                    // Edit is in the same editor which requested the code action
                    editInEditor(editor, edit);
                } else {
                    EditorFragment openedFrag = activity().getPagerAdapter().findEditorByFile(file);

                    if(openedFrag != null && openedFrag.getEditor() != null) {
                        // Edit is in another 'opened' file
                        editInEditor(openedFrag.getEditor(), edit);
                    } else {
                        // Edit is in some other file which is not opened
                        // We should open that file and perform the edit
                        openedFrag = activity().openFile(file);
                        if(openedFrag != null && openedFrag.getEditor() != null) {
                            editInEditor(openedFrag.getEditor(), edit);
                        }
                    }
                }
            }
        }

        return Boolean.TRUE;
    }
    
    private void editInEditor (final CodeEditor editor, final TextEdit edit) {
        final Range range = edit.getRange();
        final int startLine = range.getStart().getLine();
        final int startCol = range.getStart().getCharacter();
        final int endLine = range.getEnd().getLine();
        final int endCol = range.getEnd().getCharacter();
        
        activity().runOnUiThread(() -> {
            if(startLine == endLine && startCol == endCol) {
                editor.getText().insert(startLine, startCol, edit.getNewText());
            } else {
                editor.getText().replace(startLine, startCol, endLine, endCol, edit.getNewText());
            }
        });
    }
    
    public DiagnosticsAdapter newDiagnosticsAdapter() {
        return new DiagnosticsAdapter(mapAsGroup(this.diagnostics), activity());
    }
    
    /**
     * Reports connection progress
     */
    protected void connectionReport(String message) {
        LOG.info("LSP Connection report", message);
    }

    /**
     * Called when there was an error connecting to server.
     */
    protected void connectionError(Throwable th) {
        LOG.error("LSP connection error", th);
    }

    public static interface StarterListener {
        void startServer();
    }

    public static interface OnConnectedListener {
        void onConnected(IDELanguageServer server);
    }
}
