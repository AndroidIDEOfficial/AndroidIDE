package com.itsaky.androidide.handlers.editor;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.gson.JsonElement;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.handlers.IDEHandler;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.JavaColors;
import com.itsaky.lsp.JavaReportProgressParams;
import com.itsaky.lsp.JavaStartProgressParams;
import com.itsaky.lsp.LanguageClient;
import com.itsaky.lsp.Location;
import com.itsaky.lsp.Message;
import com.itsaky.lsp.ParameterInformation;
import com.itsaky.lsp.PublishDiagnosticsParams;
import com.itsaky.lsp.Range;
import com.itsaky.lsp.ShowMessageParams;
import com.itsaky.lsp.SignatureHelp;
import com.itsaky.lsp.SignatureInformation;
import com.itsaky.toaster.Toaster;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageClientHandler extends IDEHandler implements LanguageClient {
    
    private EditorActivity activity;
    private final Map<File, List<Diagnostic>> diagnostics = new HashMap<>();
    
    @Override
    public void start(IDEHandler.Provider listener) {
        super.start(listener);
        this.activity = listener.provideEditorActivity();
        if(this.activity == null) throwNPE();
    }

    @Override
    public void stop() {
        // Unimplemented
    }
    
    @Override
    public void publishDiagnostics(PublishDiagnosticsParams params) {
        boolean error = params == null || params.diagnostics == null || params.diagnostics.size() <= 0;
        activity.handleDiagnosticsResultVisibility(error);

        if(error) return;

        File file = new File(params.uri);
        if(!(file.exists() && file.isFile())) return;

        diagnostics.put(file, params.diagnostics);
        activity.getDiagnosticsList().setAdapter(newDiagnosticsAdapter());

        EditorFragment editor = null;
        if(activity.getPagerAdapter() != null && (editor = activity.getPagerAdapter().findEditorByFile(new File(params.uri))) != null) {
            editor.setDiagnostics(params.diagnostics);
        }
    }

    @Override
    public void gotoDefinition(List<Location> locations) {
        if(activity.getProgressSheet() != null && activity.getProgressSheet().isShowing())
            activity.getProgressSheet().dismiss();
        if(locations == null || locations.size() <= 0) {
            activity.getApp().toast(R.string.msg_no_definition, Toaster.Type.ERROR);
            return;
        }
        Location loc = locations.get(0);
        final File file = new File(loc.uri);
        final Range range = loc.range;
        try {
            if(activity.getPagerAdapter() == null) return;
            if(activity.getPagerAdapter().getCount() <= 0) {
                activity.openFile(file, range);
                return;
            }
            EditorFragment frag = activity.getPagerAdapter().getFrag(activity.getBinding().tabs.getSelectedTabPosition());
            if(frag != null
               && frag.getFile() != null
               && frag.getEditor() != null
               && frag.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                if(range.start.equals(range.end)) {
                    frag.getEditor().setSelection(range.start.line, range.start.column);
                } else {
                    frag.getEditor().setSelectionRegion(range.start.line, range.start.column, range.end.line, range.end.column);
                }
            } else {
                activity.openFileAndSelect(file, range);
            }
        } catch (Throwable th) {
            LOG.error(ThrowableUtils.getFullStackTrace(th));
        }
    }

    @Override
    public void references(List<Location> references) {
        if(activity.getProgressSheet() != null && activity.getProgressSheet().isShowing())
            activity.getProgressSheet().dismiss();

        boolean error = references == null || references.size() <= 0;
        activity.handleSearchResultVisibility(error);


        if(error) {
            activity.getApp().toast(R.string.msg_no_references, Toaster.Type.INFO);
            activity.getSearchResultList().setAdapter(new SearchListAdapter(null, null, null));
            return;
        }

        final Map<File, List<SearchResult>> results = new HashMap<>();
        for(int i=0;i<references.size();i++) {
            try {
                final Location loc = references.get(i);
                if(loc == null || loc.uri == null || loc.range == null) continue;
                final File file = new File(loc.uri);
                if(!file.exists() || !file.isFile()) continue;
                EditorFragment frag = activity.getPagerAdapter().findEditorByFile(file);
                Content content;
                if(frag != null && frag.getEditor() != null)
                    content = frag.getEditor().getText();
                else content = new Content(null, FileIOUtils.readFile2String(file));
                final List<SearchResult> matches = results.containsKey(file) ? results.get(file) : new ArrayList<>();
                matches.add(
                    new SearchResult(
                        loc.range,
                        file,
                        content.getLineString(loc.range.start.line),
                        content.subContent(
                            loc.range.start.line,
                            loc.range.start.column,
                            loc.range.end.line,
                            loc.range.end.column
                        ).toString()
                    )
                );
                results.put(file, matches);
            } catch (Throwable th) {
                LOG.error(ThrowableUtils.getFullStackTrace(th));
            }
        }

        activity.handleSearchResults(results);
    }

    @Override
    public void signatureHelp(SignatureHelp signature, File file) {
        if(signature == null || signature.signatures == null) {
            activity.getBinding().symbolText.setVisibility(View.GONE);
            return;
        }
        SignatureInformation info = signature.signatures.get(signature.activeSignature);
        activity.getBinding().symbolText.setText(formatSignature(info, signature.activeParameter));
        final EditorFragment frag = activity.getPagerAdapter().findEditorByFile(file);
        if(frag != null) {
            final CodeEditor editor = frag.getEditor();
            final float[] cursor = editor.getCursorPosition();

            float x = editor.updateCursorAnchor() - (activity.getBinding().symbolText.getWidth() / 2);
            float y = activity.getBinding().editorAppBarLayout.getHeight() + (cursor[0] - editor.getRowHeight() - editor.getOffsetY() - activity.getBinding().symbolText.getHeight());
            activity.getBinding().symbolText.setVisibility(View.VISIBLE);
            activity.positionViewWithinScreen(activity.getBinding().symbolText, x, y);
        }
    }

    @Override
    public void javaColors(JavaColors colors) {
        final File file = new File(colors.uri);
        final EditorFragment editor = this.activity.getPagerAdapter().findEditorByFile(file);
        if(editor != null)
            editor.setJavaColors(colors);
    }
    
    @Override
    public void onServerStarted(int currentId) {
        new Thread(() -> {
            final JavaLanguageServer server = activity.getApp().getJavaLanguageServer();
            while(server != null && !activity.pendingMessages.isEmpty()) {
                Message msg = activity.pendingMessages.pop();
                server.send(msg);
            }
        }).start();
    }
    
    public DiagnosticsAdapter newDiagnosticsAdapter() {
        return new DiagnosticsAdapter(mapAsGroup(this.diagnostics), activity);
    }
    
    /**
     * Formats (highlights) a method signature
     *
     * @param signature Signature information
     * @param paramIndex Currently active parameter index
     */
    private CharSequence formatSignature(SignatureInformation signature, int paramIndex) {
        String name = signature.label;
        name = name.substring(0, name.indexOf("("));

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(name, new ForegroundColorSpan(0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        List<ParameterInformation> params = signature.parameters;
        for(int i=0;i<params.size();i++) {
            int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
            final ParameterInformation info = params.get(i);
            if(i == params.size() - 1) {
                sb.append(info.label, new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(info.label, new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(",", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
            }
        }
        sb.append(")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
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
    
    
    
    
    
    /**
     * NOT IMPLEMENTED
     */
     
    @Override
    public void javaProgressStart(JavaStartProgressParams params) {
    }

    @Override
    public void javaProgressReport(JavaReportProgressParams params) {
    }

    @Override
    public void javaProgressEnd() {
    }
    
    @Override
    public void showMessage(ShowMessageParams params) {
    }

    @Override
    public void customNotification(String method, JsonElement params) {
    }

    @Override
    public void registerCapability(String method, JsonElement options) {
    }
}
