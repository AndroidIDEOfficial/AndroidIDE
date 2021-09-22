package com.itsaky.androidide.handlers.editor;

import android.text.TextUtils;
import android.view.View;
import com.blankj.utilcode.util.FileIOUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.handlers.IDEHandler;
import com.itsaky.androidide.interfaces.JLSRequestor;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.lsp.CodeAction;
import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.DidChangeWatchedFilesParams;
import com.itsaky.lsp.DidCloseTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.DidSaveTextDocumentParams;
import com.itsaky.lsp.FileChangeType;
import com.itsaky.lsp.FileEvent;
import com.itsaky.lsp.LanguageClient;
import com.itsaky.lsp.Range;
import com.itsaky.lsp.ReferenceParams;
import com.itsaky.lsp.TextDocumentPositionParams;
import com.itsaky.lsp.TextEdit;
import io.github.rosemoe.editor.text.Content;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JLSHandler extends IDEHandler implements JLSRequestor {
    
    private EditorActivity activity;
    private JavaLanguageServer languageServer;
    
    private final LanguageClient client;
    private static final Gson gson = new Gson();

    public JLSHandler(LanguageClient client) {
        this.client = client;
    }
    
    public JavaLanguageServer getJLS() {
        return languageServer;
    }
    
    @Override
    public void start(IDEHandler.Provider provider) {
        super.start(provider);
        this.activity = provider.provideEditorActivity();
        if(activity == null) {
            throw new NullPointerException("EditorActivity should not be null");
        }
        
        AndroidProject project = provider.provideAndroidProject();
        if(project == null) {
            throw new NullPointerException("Project is null");
        }
        
        this.languageServer = new JavaLanguageServer(project, this.client);
        this.languageServer.startServer();
        
        StudioApp.getInstance().setJavaLanguageServer(languageServer);
    }
    
    @Override
    public void stop() {
        languageServer.exit();
    }
    
    @Override
    public void hideSignature() {
        activity.getBinding().symbolText.setVisibility(View.GONE);
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams p) {
        if(languageServer != null)
            languageServer.didOpen(p);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_OPEN, gson.toJson(p)));
    }

    @Override
    public void didClose(DidCloseTextDocumentParams p) {
        if(languageServer != null)
            languageServer.didClose(p);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_CLOSE, gson.toJson(p)));
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        if(languageServer != null)
            languageServer.didChange(params);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_CHANGE, gson.toJson(params)));
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        if(languageServer != null)
            languageServer.didSave(params);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        if(languageServer != null)
            languageServer.didChangeWatchedFiles(params);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }

    @Override
    public void signatureHelp(TextDocumentPositionParams params, File file) {
        if(languageServer != null)
            languageServer.signatureHelp(params, file);
        else activity.addPendingMessage(activity.createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }

    @Override
    public void findDefinition(TextDocumentPositionParams params) {
        if(languageServer == null || !languageServer.isStarted()) return;
        languageServer.findDefinition(params);
        activity.getProgressSheet(R.string.msg_finding_definition).show(activity.getSupportFragmentManager(), "definition_progress");
    }

    @Override
    public void findReferences(ReferenceParams params) {
        if(languageServer == null || !languageServer.isStarted()) return;
        languageServer.findReferences(params);
        activity.getProgressSheet(R.string.msg_finding_references).show(activity.getSupportFragmentManager(), "references_progress");
    }

    @Override
    public void performCodeActions(final File file, List<CodeAction> actions) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.AppTheme_MaterialAlertDialog);
        builder.setTitle(R.string.msg_code_actions);
        builder.setItems(mapActionsAsArray(removeDuplicates(actions)), (d, w) -> {
            d.dismiss();
            int index = w;
            confirmActionIfNecessary(file, actions.get(index));
        });
        builder.setCancelable(true);
        builder.show();
    }
    
    /**
     * Shows a dialog to user to get confirmation from user to modify files other than this one
     * If the code action is only in the current file, directly performs the actions
     */
    private void confirmActionIfNecessary(File file, CodeAction action) {
        if(isInSameFile(file, action.edit.changes)) {
            performCodeAction(file, action);
        } else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.AppTheme_MaterialAlertDialog);
            builder.setTitle(R.string.title_confirm_changes);
            builder.setMessage(activity.getString(R.string.msg_confirm_changes, getFileNamesOfEdits(action.edit.changes.keySet())));
            builder.setPositiveButton(android.R.string.yes, (d, w) -> performCodeAction(file, action));
            builder.setNegativeButton(android.R.string.no, null);
            builder.show();
        }
    }

    private String getFileNamesOfEdits(Set<URI> names) {
        return TextUtils.join("\n", names.stream().filter(n -> n != null).map(File::new).collect(Collectors.toList()));
    }

    /**
     * Some code actions may contain duplicate items <br>
     * Removes duplicate CodeActions from the given list
     *
     * @param actions CodeActions to filter
     */
    private List<CodeAction> removeDuplicates(List<CodeAction> actions) {
        List<CodeAction> filtered = new ArrayList<>();
        for(CodeAction action : actions) {
            if(filtered.contains(action)) continue;

            filtered.add(action);
        }
        return filtered;
    }

    private CharSequence[] mapActionsAsArray(List<CodeAction> actions) {
        String[] result = new String[actions.size()];
        for(int i=0;i<actions.size();i++) {
            CodeAction action = actions.get(i);
            if(action == null) continue;
            result[i] = actions.get(i).title;
        }
        return result;
    }

    /**
     * Performs given code action
     * 
     * @param file Currently opened file. This is provided by current CodeEditor when invoking JLSRequestor#performCodeActions
     */
    private void performCodeAction(File file, CodeAction action) {
        if(action != null && action.edit != null && action.edit.changes != null) {
            Map<URI, List<TextEdit>> changes = action.edit.changes;

            /**
             * TODO: Ask for confirmation before writing file other than currently opened file
             */
            for(Map.Entry<URI, List<TextEdit>> entry : changes.entrySet()) {
                if(entry == null && entry.getKey() == null || entry.getValue() == null) continue;
                performEdits(new File(entry.getKey()), entry.getValue());
            }
        }
    }

    /**
     * Performs edits in specified file. If the file is already opened,
     * it will find the EditorFragment containing this file and perform the edits in its Content
     * <br><br>
     * If the file is not opened, it will read that file, edit it accordingly and will save it.
     *
     * @param file The file to edit
     * @param edits The edits to perform
     */
    public void performEdits(File file, List<TextEdit> edits) {
        if(edits == null || edits.size() <= 0) return;

        boolean wroteInFragment = false;

        if(activity.getPagerAdapter() != null) {
            EditorFragment frag = activity.getPagerAdapter().findEditorByFile(file);
            if(frag != null && frag.getEditor() != null && frag.getEditor().getText() != null) {
                performEdits(frag.getEditor().getText(), edits);
                wroteInFragment = true;
            }
        }

        if(!wroteInFragment) {
            readAndPerformEdits(file, edits);
        }
    }

    /**
     * Performs given text edits in provided content
     *
     * @param text Content to edit
     * @param edits The edits to perform
     */
    private void performEdits(Content text, List<TextEdit> edits) {
        for(TextEdit edit : edits) {
            if(edit == null) continue;
            if(edit.newText == null || edit.range == null) continue;
            final Range range = edit.range;
            text.replace(
                range.start.line,
                range.start.column,
                range.end.line,
                range.end.column,
                edit.newText
            );
        }
    }

    /**
     * Reads the provided file and performs the given edits
     *
     * @param file The file to read
     * @param edits Edits to perform
     */
    private void readAndPerformEdits(File file, List<TextEdit> edits) {
        String text = FileIOUtils.readFile2String(file);
        if(text == null || text.length() <= 0) return;
        Content content = new Content(null, text);
        performEdits(content, edits);

        FileIOUtils.writeFileFromString(file, content.toString());

        notifyFileChanged(file);
    }

    /**
     * Checks if all the changes are in the same (currently opened) file
     *
     * @param current File to check
     * @param changes The changes to check
     * @return Whether all changes are in the currently opened file
     */
    private boolean isInSameFile(File current, Map<URI, List<TextEdit>> changes) {
        for(URI uri : changes.keySet()) {
            File file = new File(uri);
            if(file.exists()
               && !file.getAbsolutePath().equals(current.getAbsolutePath())) {
                return false;
            }
        }
        return true;
    }
    
    public void notifyFileCreated(File file) {
        notifyExternalFileChange(file, FileChangeType.Created);

        activity.openFile(file);
    }

    public void notifyFileDeleted(File file) {
        notifyExternalFileChange(file, FileChangeType.Deleted);
    }

    public void notifyFileChanged(File file) {
        notifyExternalFileChange(file, FileChangeType.Changed);
    }

    private void notifyExternalFileChange(File file, int changeType) {
        if(!file.getName().endsWith(".java")) return;
        DidChangeWatchedFilesParams p = new DidChangeWatchedFilesParams();
        p.changes.add(new FileEvent(file.toURI(), changeType));
        didChangeWatchedFiles(p);
    }
}
