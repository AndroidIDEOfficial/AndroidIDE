/*
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.lsp;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.transition.TransitionManager;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.views.CodeEditorView;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticResult;
import com.itsaky.lsp.models.Location;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.SignatureInformation;
import com.itsaky.lsp.models.TextEdit;
import com.itsaky.toaster.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;

/**
 * AndroidIDE specific implementation of the LanguageClient
 */
public class IDELanguageClientImpl implements ILanguageClient {
    
    protected static final Gson gson = new Gson();
    protected static final Logger LOG = Logger.instance("AbstractLanguageClient");
    
    private final Map<File, List<DiagnosticItem>> diagnostics = new HashMap<>();
    private static IDELanguageClientImpl mInstance;
    
    protected EditorActivityProvider activityProvider;
    
    private IDELanguageClientImpl (EditorActivityProvider provider) {
        setActivityProvider(provider);
    }
    
    public static IDELanguageClientImpl getInstance () {
        if (mInstance == null) {
            throw new IllegalStateException ("Client not initialized");
        }
        
        return mInstance;
    }
    
    public static IDELanguageClientImpl initialize (EditorActivityProvider provider) {
        if (mInstance != null) {
            throw new IllegalStateException ("Client is already initialized");
        }
        
        mInstance = new IDELanguageClientImpl (provider);
        
        return getInstance();
    }
    
    public static void shutdown () {
        mInstance = null;
    }
    
    public static boolean isInitialized () {
        return mInstance != null;
    }

    public void setActivityProvider(EditorActivityProvider provider) {
        this.activityProvider = provider;
    }
    
    protected EditorActivity activity() {
        if (activityProvider == null) return null;
        return activityProvider.provide();
    }
    
    private CodeEditorView findEditorByFile (File file) {
        return activity ().getEditorForFile (file);
    }
    
    public void showDiagnostic(DiagnosticItem diagnostic, final CodeEditor editor) {
        if(activity() == null || activity().getDiagnosticBinding() == null) {
            hideDiagnostics();
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

    /**
     * Shows the diagnostic at the bottom of the screen (just above the status text)
     * and requests code actions from language server
     *
     * @param diagnostic The diagnostic to show
     * @param editor The CodeEditor that requested
     */
    public void showDiagnosticAtBottom(final File file, final DiagnosticItem diagnostic, final CodeEditor editor) {
        if(activity() == null || file == null || diagnostic == null) {
            hideBottomDiagnosticView(file);
            return;
        }
        
        final var frag = findEditorByFile(file);
        if(frag == null) {
            hideBottomDiagnosticView(file);
            return;
        }
        
        final var binding = frag.getBinding();
        binding.diagnosticTextContainer.setVisibility(View.VISIBLE);
        binding.diagnosticText.setVisibility(View.VISIBLE);
        binding.diagnosticText.setClickable(false);
        binding.diagnosticText.setText(diagnostic.getMessage());
        
        final var future = editor.codeActions(Collections.singletonList(diagnostic));
        if(future == null) {
            hideBottomDiagnosticView(file);
            return;
        }
        
        future.whenComplete((result, error) -> {
            if(result == null) {
                hideBottomDiagnosticView(file);
                return;
            }
            
            if(result.getActions ().isEmpty()) {
                hideBottomDiagnosticView(file);
                return;
            }
            
            ThreadUtils.runOnUiThread(() -> {
                final SpannableStringBuilder sb = new SpannableStringBuilder();
                sb.append(activity().getString(R.string.msg_fix_diagnostic), new ForegroundColorSpan(ContextCompat.getColor(activity(), R.color.secondaryColor)), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
                sb.append(diagnostic.getMessage());
                binding.diagnosticText.setText(sb);
                binding.diagnosticText.setClickable(true);
                binding.diagnosticText.setOnClickListener(v -> showAvailableQuickfixes(editor, result.getActions ()));
            });
        });
    }
    
    public void hideDiagnostics() {
        if(activity() == null || activity().getDiagnosticBinding() == null) {
            return;
        }
        
        activity().getDiagnosticBinding().getRoot().setVisibility(View.GONE);
    }

    private void hideBottomDiagnosticView(final File file) {
        if(activity() == null || file == null) {
            return;
        }
        
        final var frag = findEditorByFile(file);
        if(frag == null || frag.getBinding() == null) {
            return;
        }
        
        frag.getBinding().diagnosticTextContainer.setVisibility(View.GONE);
        frag.getBinding().diagnosticText.setVisibility(View.GONE);
        frag.getBinding().diagnosticText.setClickable(false);
    }
    
    public void showSignatureHelp (com.itsaky.lsp.models.SignatureHelp signature, File file) {
        if(signature == null) {
            hideSignatureHelp();
            return;
        }
        
        var info = signatureWithMostParams(signature);
        if(info == null) return;
        activity().getBinding().symbolText.setText(formatSignature(info, signature.getActiveParameter()));
        final var frag = findEditorByFile(file);
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
    private com.itsaky.lsp.models.SignatureInformation signatureWithMostParams(com.itsaky.lsp.models.SignatureHelp signature) {
        com.itsaky.lsp.models.SignatureInformation signatureWithMostParams = null;
        int mostParamCount = 0;
        final var signatures = signature.getSignatures();
        for(int i=0;i<signatures.size();i++) {
            final var info = signatures.get(i);
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
    @NonNull
    private CharSequence formatSignature(@NonNull SignatureInformation signature, int paramIndex) {
        String name = signature.getLabel();
        name = name.substring(0, name.indexOf("("));

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(name, new ForegroundColorSpan(0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        var params = signature.getParameters();
        for(int i=0;i<params.size();i++) {
            int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
            final var info = params.get(i);
            if(i == params.size() - 1) {
                sb.append(info.getLabel() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(info.getLabel() + "", new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(",", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
            }
        }
        sb.append(")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public void publishDiagnostics(DiagnosticResult result) {
        boolean error = result == null;
        activity().handleDiagnosticsResultVisibility(error || result.getDiagnostics().isEmpty());
        
        if(error) {
            return;
        }
        
        File file = result.getFile ().toFile ();
        if(!file.exists() || !file.isFile()) return;
        
        diagnostics.put(file, result.getDiagnostics());
        activity().setDiagnosticsAdapter(newDiagnosticsAdapter());
    }
    
    /**
     * Called by {@link io.github.rosemoe.editor.widget.CodeEditor CodeEditor} to show locations in EditorActivity
     */
    public void showLocations(List<Location> locations) {
    
        // Cannot show anything if the activity() is null
        if (activity () == null) {
            return;
        }
    
        boolean error = locations == null || locations.isEmpty ();
        activity ().handleSearchResultVisibility (error);
    
    
        if (error) {
            activity ().setSearchResultAdapter (new SearchListAdapter (null, null, null));
            return;
        }
    
        final Map<File, List<SearchResult>> results = new HashMap<> ();
        for (int i = 0; i < locations.size (); i++) {
            try {
                final Location loc = locations.get (i);
                if (loc == null) {
                    continue;
                }
                
                final File file = loc.getFile ().toFile ();
                if (!file.exists () || !file.isFile ()) continue;
                var frag = findEditorByFile (file);
                Content content;
                if (frag != null && frag.getEditor () != null)
                    content = frag.getEditor ().getText ();
                else content = new Content (null, FileIOUtils.readFile2String (file));
                final List<SearchResult> matches = results.containsKey (file) ? results.get (file) : new ArrayList<> ();
                Objects.requireNonNull (matches).add (
                        new SearchResult (
                                loc.getRange (),
                                file,
                                content.getLineString (loc.getRange ().getStart ().getLine ()),
                                content.subContent (
                                        loc.getRange ().getStart ().getLine (),
                                        loc.getRange ().getStart ().getColumn (),
                                        loc.getRange ().getEnd ().getLine (),
                                        loc.getRange ().getEnd ().getColumn ()
                                ).toString ()
                        )
                );
                results.put (file, matches);
            } catch (Throwable th) {
                LOG.error ("Failed to show file location", th);
            }
        }
    
        activity ().handleSearchResults (results);
    }
    
    /**
     * Perform the given {@link CodeActionItem}
     *
     * @param editor The {@link CodeEditor} that invoked the code action request.
     *            This is required to reduce the time finding the code action from the edits.
     * @param action The action to perform
     */
    public void performCodeAction(CodeEditor editor, CodeActionItem action) {
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
            progress.dismiss();
            if(a == null || b != null || !a) {
                StudioApp.getInstance().toast(R.string.msg_cannot_perform_fix, Toaster.Type.ERROR);
            }
        });
    }

    private List<DiagnosticGroup> mapAsGroup(Map<File, List<DiagnosticItem>> diags) {
        List<DiagnosticGroup> groups = new ArrayList<>();
        if(diags == null || diags.size() <= 0)
            return groups;
        for(File file : diags.keySet()) {
            List<DiagnosticItem> fileDiags = diags.get(file);
            if(fileDiags == null || fileDiags.size() <= 0)
                continue;
            DiagnosticGroup group = new DiagnosticGroup(R.drawable.ic_language_java, file, fileDiags);
            groups.add(group);
        }
        return groups;
    }
    
    private void showAvailableQuickfixes (CodeEditor editor, List<CodeActionItem> actions) {
        final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder (activity ());
        builder.setTitle(R.string.msg_code_actions);
        builder.setItems(asArray(actions), (d, w) -> {
            d.dismiss();
            hideDiagnostics();
            hideBottomDiagnosticView(editor.getFile());
            performCodeAction(editor, actions.get(w));
        });
        builder.show();
    }

    private CharSequence[] asArray(List<CodeActionItem> actions) {
        final String[] arr = new String[actions.size()];
        for(int i=0;i<actions.size();i++) {
            arr[i] = actions.get(i).getTitle();
        }
        return arr;
    }
    
    private Boolean performCodeActionAsync(final CodeEditor editor, final CodeActionItem action) {
        final var changes = action.getChanges();
        if(changes.isEmpty()) {
            return Boolean.FALSE;
        }
        
        for(var change : changes) {
            final File file = change.getFile ().toFile ();
            if(!file.exists()) {
                continue;
            }

            for(TextEdit edit : change.getEdits ()) {
                final String editorFilepath = editor.getFile() == null ? "" : editor.getFile().getAbsolutePath();
                if(file.getAbsolutePath().equals(editorFilepath)) {
                    // Edit is in the same editor which requested the code action
                    editInEditor(editor, edit);
                } else {
                    var openedFrag = findEditorByFile(file);

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
        final int startCol = range.getStart().getColumn ();
        final int endLine = range.getEnd().getLine();
        final int endCol = range.getEnd().getColumn ();
        
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
    
    @Override
    public com.itsaky.lsp.models.ShowDocumentResult showDocument (com.itsaky.lsp.models.ShowDocumentParams params) {
        boolean success = false;
        final var result = new com.itsaky.lsp.models.ShowDocumentResult (false);
        if(activity() == null) {
            return result;
        }
    
        if(params != null) {
            File file = params.getFile ().toFile ();
            if (file.exists () && file.isFile () && FileUtils.isUtf8 (file)) {
                final var range = params.getSelection ();
                var frag = activity ().getEditorAtIndex (
                        activity ().getBinding ().tabs.getSelectedTabPosition ());
                if (frag != null
                        && frag.getFile () != null
                        && frag.getEditor () != null
                        && frag.getFile ().getAbsolutePath ().equals (file.getAbsolutePath ())) {
                    if (LSPUtils.isEqual (range.getStart (), range.getEnd ())) {
                        frag.getEditor ().setSelection (range.getStart ().getLine (), range.getStart ().getColumn ());
                    } else {
                        frag.getEditor ().setSelectionRegion (range.getStart ().getLine (),
                                range.getStart ().getColumn (),
                                range.getEnd ().getLine (),
                                range.getEnd ().getColumn ());
                    }
                } else {
                    activity ().openFileAndSelect (file, range);
                }
                success = true;
            }
        }
    
        result.setSuccess(success);
        return result;
    }
}
