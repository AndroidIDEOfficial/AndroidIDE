/*
 * This file is part of AndroidIDE.
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
 */
package com.itsaky.androidide.views.editor;

import static com.itsaky.androidide.R.color.secondaryColor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CodeActionParams;
import com.itsaky.lsp.models.CodeActionResult;
import com.itsaky.lsp.models.DefinitionResult;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;
import com.itsaky.lsp.models.ExpandSelectionParams;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.ReferenceParams;
import com.itsaky.lsp.models.ReferenceResult;
import com.itsaky.lsp.models.ShowDocumentParams;
import com.itsaky.lsp.models.SignatureHelp;
import com.itsaky.lsp.models.SignatureHelpParams;
import com.itsaky.toaster.Toaster;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.widget.CodeEditor;

public class IDEEditor extends CodeEditor {
    
    private int mFileVersion;
    private File file;
    
    private ILanguageServer mLanguageServer;
    private IDELanguageClientImpl mLanguageClient;
    private SignatureHelpWindow mSignatureHelpWindow;
    private DiagnosticWindow mDiagnosticWindow;
    
    private ITextActionPresenter mTextActionPresenter;
    
    public static final String KEY_FILE = "editor_file";
    private static final Logger LOG = Logger.instance ("IDEEditor");
    
    public IDEEditor (Context context) {
        this (context, null);
    }
    
    public IDEEditor (Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }
    
    public IDEEditor (Context context, AttributeSet attrs, int defStyleAttr) {
        this (context, attrs, defStyleAttr, 0);
    }
    
    public IDEEditor (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
        
        setColorScheme (new SchemeAndroidIDE ());
        setTextActionPresenter (chooseTextActionPresenter ());
        subscribeEvent (SelectionChangeEvent.class, (event, unsubscribe) -> handleSelectionChange (event));
        subscribeEvent (ContentChangeEvent.class, (event, unsubscribe) -> handleContentChange (event));
        
        // default editor input type + no suggestions flag
        setInputType (EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }
    
    @NonNull
    @Contract(" -> new")
    public ITextActionPresenter chooseTextActionPresenter () {
        final var prefs = StudioApp.getInstance ().getPrefManager ();
        final var usePopup = prefs.getBoolean (PreferenceManager.KEY_EDITOR_USE_POPUP, false);
        if (usePopup) {
            return new EditorTextActionWindow (this);
        } else {
            return new EditorTextActionMode ();
        }
    }
    
    /**
     * Set selection to the given range.
     *
     * @param range The range to select.
     */
    public void setSelection (@NonNull Range range) {
        setSelectionRegion (range.getStart ().getLine (),
                range.getStart ().getColumn (),
                range.getEnd ().getLine (),
                range.getEnd ().getColumn ());
    }
    
    /**
     * Set the selection of this editor to the given position.
     *
     * @param position The position to select.
     */
    public void setSelection (@NonNull Position position) {
        setSelection (position.getLine (), position.getColumn ());
    }
    
    /**
     * Set the file that this editor is currently editing.
     *
     * @param file The file to set.
     */
    public void setFile (File file) {
        this.file = file;
        
        if (file != null && mLanguageServer != null) {
            final var text = getText ().toString ();
            final var event = new DocumentOpenEvent (file.toPath (), text, mFileVersion = 0);
            mLanguageServer.getDocumentHandler ().onFileOpened (event);
        }
        
        if (file != null) {
            getExtraArguments ().putString (KEY_FILE, file.getAbsolutePath ());
        } else {
            getExtraArguments ().remove (KEY_FILE);
        }
    }
    
    /**
     * Get the file that this editor is currently editing.
     *
     * @return The file instance.
     */
    public File getFile () {
        return file;
    }
    
    /**
     * Set the language server that this editor will connect with.
     * If the language client is not set, it'll be set to {@link IDELanguageClientImpl}.
     *
     * @param server The server to set. Provide <code>null</code> to
     *               disable all the language server features.
     */
    public void setLanguageServer (ILanguageServer server) {
        this.mLanguageServer = server;
        
        if (mLanguageClient == null && IDELanguageClientImpl.isInitialized ()) {
            mLanguageClient = IDELanguageClientImpl.getInstance ();
        }
    }
    
    /**
     * Append the given text at the end of the editor's content.
     *
     * @param text The text to append.
     * @return The line at which the text was appended.
     */
    public int append (CharSequence text) {
        final var content = getText ();
        if (getLineCount () <= 0) {
            return 0;
        }
        final int line = getLineCount () - 1;
        int col = content.getColumnCount (line);
        if (col < 0) {
            col = 0;
        }
        content.insert (line, col, text);
        return line;
    }
    
    /**
     * Set the selection of the editor's cursor
     * to the last line of the it's content.
     */
    public void goToEnd () {
        final var line = getText ().getLineCount () - 1;
        setSelection (line, 0);
    }
    
    /**
     * Comment the line of the left cursor.
     */
    public void commentLine () {
        if (getFile () == null) {
            return;
        }
        
        final var text = getText ();
        final var name = getFile ().getName ();
        int line = getCursor ().getLeftLine ();
        if (name.endsWith (".java") || name.endsWith (".gradle")) {
            while (line >= getCursor ().getLeftLine () && line <= getCursor ().getRightLine ()) {
                if (!text.getLineString (line).trim ().startsWith ("//")) {
                    text.insert (line, 0, "//");
                }
                line++;
            }
        } else if (name.endsWith (".xml")) {
            while (line >= getCursor ().getLeftLine () && line <= getCursor ().getRightLine ()) {
                final String lineString = text.getLineString (line);
                if (!lineString.trim ().startsWith ("<!--")
                        && !lineString.trim ().endsWith ("-->")) {
                    text.replace (line, 0, line, text.getColumnCount (line), "<!--".concat (lineString).concat ("-->"));
                }
                line++;
            }
        }
    }
    
    /**
     * Uncomment the current line
     */
    public void uncommentLine () {
        if (getFile () == null) {
            return;
        }
        
        final var text = getText ();
        final String name = getFile ().getName ();
        int line = getCursor ().getLeftLine ();
        if (name.endsWith (".java") || name.endsWith (".gradle")) {
            while (line >= getCursor ().getLeftLine () && line <= getCursor ().getRightLine ()) {
                String l = text.getLineString (line);
                if (l.trim ().startsWith ("//")) {
                    int i = l.indexOf ("//");
                    text.delete (line, i, line, i + 2);
                }
                line++;
            }
        } else if (name.endsWith (".xml")) {
            final String commentStart = "<!--";
            final String commentEnd = "-->";
            while (line >= getCursor ().getLeftLine () && line <= getCursor ().getRightLine ()) {
                String l = text.getLineString (line);
                if (l.trim ().startsWith (commentStart)) {
                    int i = l.indexOf (commentStart);
                    text.delete (line, i, line, i + commentStart.length ());
                }
                if (l.trim ().endsWith (commentEnd)) {
                    int count = text.getColumnCount (line);
                    text.delete (line, count - commentEnd.length (), line, count);
                }
                line++;
            }
        }
    }
    
    /**
     * If any language server is set, asks the language server
     * to find the definition of token at the cursor position.
     * <p>
     * If the server returns a valid response, and the file specified
     * in the response is same the file in this editor,
     * the range specified in the response will be selected.
     * </p>
     */
    @SuppressWarnings("deprecation")
    public void findDefinition () {
        if (getFile () == null) {
            return;
        }
        
        final ProgressDialog pd = ProgressDialog.show (getContext (), null, getContext ().getString (R.string.msg_finding_definition));
        
        try {
            final CompletableFuture<DefinitionResult> future = CompletableFuture.supplyAsync (() -> {
                final var provider = mLanguageServer.getDefinitionProvider ();
                final var params = new com.itsaky.lsp.models.DefinitionParams (
                        getFile ().toPath (),
                        new com.itsaky.lsp.models.Position (
                                getCursor ().getLeftLine (),
                                getCursor ().getLeftColumn ()
                        )
                );
                
                return provider.findDefinitions (params);
            });
            
            future.whenComplete ((result, error) -> {
                
                if (result == null
                        || mLanguageClient == null
                        || future.isCancelled ()
                        || future.isCompletedExceptionally ()
                ) {
                    LOG.error ("An error occurred while finding definition", error);
                    showDefinitionNotFound (pd);
                    return;
                }
                
                final var locations = result.getLocations ();
                if (locations.size () <= 0) {
                    LOG.error ("No definitions found", "Size:", locations.size ());
                    showDefinitionNotFound (pd);
                    return;
                }
                
                ThreadUtils.runOnUiThread (() -> {
                    if (locations.size () == 1) {
                        var location = locations.get (0);
                        if (location.getFile ().equals (getFile ().toPath ())) {
                            setSelection (location.getRange ());
                            return;
                        }
                        mLanguageClient.showDocument (new ShowDocumentParams (location.getFile (), location.getRange ()));
                    } else {
                        mLanguageClient.showLocations (locations);
                    }
                });
                
                dismissOnUiThread (pd);
            });
        } catch (Throwable th) {
            LOG.error ("An error occurred while finding definition", th);
            showDefinitionNotFound (pd);
        }
    }
    
    /**
     * Notify the user that no definitions can be found.
     *
     * @param pd The {@link ProgressDialog} that was shown when requesting the definitions.
     */
    @SuppressWarnings("deprecation")
    private void showDefinitionNotFound (final ProgressDialog pd) {
        ThreadUtils.runOnUiThread (() -> {
            StudioApp.getInstance ().toast (R.string.msg_no_definition, Toaster.Type.ERROR);
            pd.dismiss ();
        });
    }
    
    /**
     * If any language server instance is set,
     * finds the references to of the token at the current cursor position.
     * <p>
     * If the server returns a valid response, that response is forwarded
     * to the {@link IDELanguageClientImpl}.
     */
    public void findReferences () {
        if (getFile () == null) {
            return;
        }
        
        @SuppressWarnings("deprecation") final ProgressDialog pd = ProgressDialog.show (getContext (), null, getContext ().getString (R.string.msg_finding_references));
        
        try {
            final CompletableFuture<ReferenceResult> future = CompletableFuture.supplyAsync (() -> {
                final var provider = mLanguageServer.getReferenceProvider ();
                final var referenceParams = new ReferenceParams (
                        getFile ().toPath (),
                        new com.itsaky.lsp.models.Position (
                                getCursor ().getLeftLine (),
                                getCursor ().getLeftColumn ()),
                        true);
                return provider.findReferences (referenceParams);
            });
            
            future.whenComplete ((result, error) -> {
                
                if (result == null
                        || mLanguageClient == null
                        || future.isCancelled ()
                        || future.isCompletedExceptionally ()
                ) {
                    LOG.error ("An error occurred while finding references", error);
                    showReferencesNotFound (pd);
                    return;
                }
                
                if (result.getLocations ().isEmpty ()) {
                    showReferencesNotFound (pd);
                    return;
                } else {
                    if (result.getLocations ().size () == 1) {
                        final var loc = result.getLocations ().get (0);
                        if (loc.getFile ().equals (getFile ().toPath ())) {
                            setSelection (loc.getRange ());
                            return;
                        }
                    }
                    
                    ThreadUtils.runOnUiThread (() -> mLanguageClient.showLocations (result.getLocations ()));
                }
                
                dismissOnUiThread (pd);
            });
        } catch (Throwable th) {
            LOG.error ("An error occurred while finding references", th);
            showReferencesNotFound (pd);
        }
    }
    
    /**
     * Notify the user that no references were found for the selected token.
     *
     * @param pd The {@link ProgressDialog} that was shown when requesting references.
     */
    @SuppressWarnings("deprecation")
    private void showReferencesNotFound (final ProgressDialog pd) {
        ThreadUtils.runOnUiThread (() -> {
            StudioApp.getInstance ().toast (R.string.msg_no_references, Toaster.Type.ERROR);
            pd.dismiss ();
        });
    }
    
    /**
     * If any language server is set, requests signature help at the cursor's position.
     * On a valid response, shows the signature help in a popup window.
     */
    @SuppressWarnings("unused")  // accessed using reflection in CompletionItem class
    public void signatureHelp () {
        if (mLanguageServer != null && getFile () != null) {
            final CompletableFuture<SignatureHelp> future = CompletableFuture.supplyAsync (() -> {
                final var provider = mLanguageServer.getSignatureHelpProvider ();
                return provider.provideSignatures (new SignatureHelpParams (getFile ().toPath (), new com.itsaky.lsp.models.Position (
                        getCursor ().getLeftLine (),
                        getCursor ().getLeftColumn ()
                )));
            });
            
            future.whenComplete ((help, error) -> {
                if (help == null
                        || mLanguageClient == null
                        || future.isCancelled ()
                        || future.isCompletedExceptionally ()
                ) {
                    LOG.error ("An error occurred while finding signature help", error);
                    return;
                }
                
                ThreadUtils.runOnUiThread (() -> showSignatureHelp (help));
            });
        }
    }
    
    public void showSignatureHelp (SignatureHelp help) {
        getSignatureHelpWindow ().setupAndDisplay (help);
    }
    
    /**
     * Dismisses the given dialog on the UI thread.
     *
     * @param dialog The dialog to dismiss.
     */
    private void dismissOnUiThread (@NonNull final Dialog dialog) {
        ThreadUtils.runOnUiThread (dialog::dismiss);
    }
    
    /**
     * If any language server is set, notify the server that the file in this editor was saved.
     */
    public void didSave () {
        if (mLanguageServer != null && getFile () != null) {
            mLanguageServer.getDocumentHandler ().onFileSaved (
                    new DocumentSaveEvent (getFile ().toPath ())
            );
        }
    }
    
    /**
     * Notify the language server that the file in this editor is about to be closed.
     */
    public void close () {
        if (mLanguageServer != null && getFile () != null) {
            mLanguageServer.getDocumentHandler ().onFileClosed (
                    new DocumentCloseEvent (getFile ().toPath ())
            );
            LOG.info ("'textDocument/didClose' was sent to the language server.");
        } else {
            LOG.info ("No language server is available for this file");
        }
    }
    
    /**
     * Request code actions from server at the given position containing given diagnostics
     *
     * @return A {@link CompletableFuture}. May return {@code null}.
     */
    public CompletableFuture<CodeActionResult> codeActions () {
        // TODO Implement this properly
        return CompletableFuture.completedFuture (new CodeActionResult ());
    }
    
    /**
     * Requests code actions for the given diagnostics to the language server.
     *
     * @param diagnostics The diagnostics to request code actions for.
     * @return The {@link CodeActionResult} from the server.
     */
    public CompletableFuture<CodeActionResult> codeActions (List<DiagnosticItem> diagnostics) {
        if (mLanguageServer == null || mLanguageClient == null) {
            return null;
        }
        
        return CompletableFuture.supplyAsync (() -> mLanguageServer.getCodeActionProvider ()
                .codeActions (new CodeActionParams (
                        getFile ().toPath (),
                        getCursorRange (),
                        diagnostics
                )));
    }
    
    /**
     * Requests the language server to provided a semantically larger selection than the current selection.
     * If a valid response is received, that range will be selected.
     */
    public void expandSelection () {
        if (mLanguageServer == null || getFile () == null) {
            LOG.error ("Cannot expand selection. Language server or file is null");
            return;
        }
        
        //noinspection deprecation
        final var pd = ProgressDialog.show (getContext (), null, getContext ().getString (R.string.please_wait), true, false);
        final CompletableFuture<Range> future = CompletableFuture.supplyAsync (() ->
                mLanguageServer.getSelectionProvider ()
                        .expandSelection (
                                new ExpandSelectionParams (
                                        getFile ().toPath (),
                                        getCursorRange ()
                                )
                        )
        );
        
        future.whenComplete (((range, throwable) -> {
            
            pd.dismiss ();
            
            if (throwable != null) {
                LOG.error ("Error computing expanded selection range", throwable);
                return;
            }
            
            ThreadUtils.runOnUiThread (() -> setSelection (range));
        }));
    }
    
    /**
     * Get the cursor's position in the form of {@link Position}.
     *
     * @return The {@link Position} of the cursor.
     */
    @SuppressWarnings("unused")
    public Position getCursorAsLSPPosition () {
        return new Position (getCursor ().getLeftLine (), getCursor ().getLeftColumn ());
    }
    
    /**
     * Get the cursor's selection range in the form of {@link Range}.
     *
     * @return The {@link Range} of the cursor.
     */
    public Range getCursorRange () {
        final var cursor = getCursor ();
        final var start = new Position (cursor.getLeftLine (), cursor.getLeftColumn ());
        final var end = new Position (cursor.getRightLine (), cursor.getRightColumn ());
        return new Range (start, end);
    }
    
    /**
     * Set the text action presenter of this editor.
     *
     * @param actionPresenter The presenter to set. Must not be <code>null</code>.
     */
    public void setTextActionPresenter (@NonNull ITextActionPresenter actionPresenter) {
        Objects.requireNonNull (actionPresenter, "Cannot set text action presenter to null");
        
        if (mTextActionPresenter != null) {
            mTextActionPresenter.destroy ();
            mTextActionPresenter = null;
        }
        
        this.mTextActionPresenter = actionPresenter;
        
        actionPresenter.bindEditor (this);
        registerActionsTo (actionPresenter);
    }
    
    /**
     * Get the text action presenter attached with this editor.
     *
     * @return The attached text action presenter.
     */
    @SuppressWarnings("unused")
    public ITextActionPresenter getTextActionPresenter () {
        return mTextActionPresenter;
    }
    
    /**
     * Register the editor's actions to the given action presenter.
     *
     * @param actionPresenter The action presenter to register actions to.
     */
    public void registerActionsTo (@NonNull ITextActionPresenter actionPresenter) {
        Objects.requireNonNull (actionPresenter, "Cannot register actions to null text action presenter");
        
        var index = -1;
        
        TypedArray array = getContext ().getTheme ().obtainStyledAttributes (new int[]{
                android.R.attr.actionModeSelectAllDrawable,
                android.R.attr.actionModeCutDrawable,
                android.R.attr.actionModeCopyDrawable,
                android.R.attr.actionModePasteDrawable,
        });
        
        actionPresenter.registerAction (
                new TextAction (
                        createTextActionDrawable (R.drawable.ic_expand_selection),
                        R.string.action_expand_selection,
                        TextAction.EXPAND_SELECTION,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        array.getDrawable (0),
                        android.R.string.selectAll,
                        TextAction.SELECT_ALL,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        array.getDrawable (1),
                        android.R.string.cut,
                        TextAction.CUT,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        array.getDrawable (2),
                        android.R.string.copy,
                        TextAction.COPY,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        array.getDrawable (3),
                        android.R.string.paste,
                        TextAction.PASTE,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        createTextActionDrawable (R.drawable.ic_goto_definition),
                        R.string.menu_navigate_definition,
                        TextAction.GOTO_DEFINITION,
                        index++
                )
        );
        
        actionPresenter.registerAction (
                new TextAction (
                        createTextActionDrawable (R.drawable.ic_find_references),
                        R.string.menu_navigate_references,
                        TextAction.FIND_REFERENCES,
                        index++
                )
        );
        actionPresenter.registerAction (
                new TextAction (
                        createTextActionDrawable (R.drawable.ic_comment_line),
                        R.string.menu_comment_line,
                        TextAction.COMMENT_LINE,
                        index++
                )
        );
        
        //noinspection UnusedAssignment
        actionPresenter.registerAction (
                new TextAction (
                        createTextActionDrawable (R.drawable.ic_uncomment_line),
                        R.string.menu_uncomment_line,
                        TextAction.UNCOMMENT_LINE,
                        index++
                )
        );
        
        
        array.recycle ();
    }
    
    /**
     * Called by text action presenters to check if the action with the given ID should be shown or not.
     *
     * @param actionId The action ID to check.
     * @return <code>true</code> if the action should be shown, <code>false</code> otherwise.
     */
    public boolean shouldShowTextAction (int actionId) {
        final var capabilities = mLanguageServer != null ? mLanguageServer.getCapabilities () : null;
        final var notNull = capabilities != null;
        final var expand = notNull && capabilities.getSmartSelectionsEnabled ();
        final var definitions = notNull && capabilities.getDefinitionsAvailable ();
        final var references = notNull && capabilities.getReferencesAvailable ();
        final var commentUncomment = getFile () != null
                && (getFile ().getName ().endsWith (".java")
                || getFile ().getName ().endsWith (".gradle")
                || getFile ().getName ().endsWith (".xml"));
        switch (actionId) {
            case TextAction.CUT:
            case TextAction.PASTE:
                return isEditable ();
            case TextAction.GOTO_DEFINITION:
                return definitions;
            case TextAction.EXPAND_SELECTION:
                return expand;
            case TextAction.FIND_REFERENCES:
                return references;
            case TextAction.COMMENT_LINE:
            case TextAction.UNCOMMENT_LINE:
                return commentUncomment;
        }
        
        return true;
    }
    
    public void performTextAction (@NonNull TextAction action) {
        Objects.requireNonNull (action, "Cannot perform null text action");
        
        switch (action.id) {
            case TextAction.SELECT_ALL:
                selectAll ();
                break;
            case TextAction.CUT:
                cutText ();
                break;
            case TextAction.COPY:
                copyText ();
                break;
            case TextAction.PASTE:
                pasteText ();
                break;
            case TextAction.GOTO_DEFINITION:
                findDefinition ();
                break;
            case TextAction.FIND_REFERENCES:
                findReferences ();
                break;
            case TextAction.COMMENT_LINE:
                commentLine ();
                break;
            case TextAction.UNCOMMENT_LINE:
                uncommentLine ();
                break;
            case TextAction.EXPAND_SELECTION:
                expandSelection ();
                break;
        }
    }
    
    private Drawable createTextActionDrawable (int icon) {
        final Drawable d = ContextCompat.getDrawable (getContext (), icon);
        if (d != null) {
            d.setColorFilter (ContextCompat.getColor (getContext (), secondaryColor), PorterDuff.Mode.SRC_ATOP);
        }
        return d;
    }
    
    private void handleSelectionChange (SelectionChangeEvent event) {
        if (event.isSelected () || !(getEditorLanguage () instanceof IDELanguage)) {
            // do not show diagnostics when text is selected
            // or if we cannot get diagnostics
            return;
        }
        
        final var diagnostics = ((IDELanguage) getEditorLanguage ()).getDiagnostics ();
        final var line = event.getLeft ().line;
        final var column = event.getLeft ().column;
        
        // diagnostics are expected to be sorted, so, we do a binary search
        getDiagnosticWindow ().showDiagnostic (binarySearchDiagnostic (diagnostics, line, column));
    }
    
    @Nullable
    private DiagnosticItem binarySearchDiagnostic (@NonNull List<DiagnosticItem> diagnostics, int line, int column) {
        
        if (diagnostics.isEmpty ()) {
            return null;
        }
        
        final var pos = new Position (line, column);
        int left = 0;
        int right = diagnostics.size () - 1;
        int mid;
        while (left < right) {
            mid = (left + right) / 2;
            var d = diagnostics.get (mid);
            var r = d.getRange ();
            var c = r.containsForBinarySearch (pos);
            if (c < 0) {
                right = mid - 1;
            } else if (c > 0) {
                left = mid + 1;
            } else {
                return d;
            }
        }
        
        return null;
    }
    
    /**
     * Notify the language server that the content of this file
     * has been changed.
     *
     * @param event The content change event.
     */
    private void handleContentChange (ContentChangeEvent event) {
        
        if (getFile () == null) {
            return;
        }
        
        StudioApp.getInstance ()
                .getJavaLanguageServer ()
                .getDocumentHandler ()
                .onContentChange (
                        new DocumentChangeEvent (
                                getFile ().toPath (),
                                getText (),
                                mFileVersion + 1
                        )
                );
        
        checkForSignatureHelp (event);
    }
    
    /**
     * Checks if the content change event should trigger signature help.
     * Signature help trigger characters are :
     * <ul>
     *     <li><code>'('</code> (parentheses) </li>
     *     <li><code>','</code> (comma) </li>
     * </ul>
     *
     * @param event The content change event.
     */
    private void checkForSignatureHelp (@NonNull ContentChangeEvent event) {
        if (event.getAction () != ContentChangeEvent.ACTION_INSERT
                || event.getChangedText ().length () != 1) {
            return;
        }
        
        final var ch = event.getChangedText ().charAt (0);
        if (ch == '(' || ch == ',') {
            signatureHelp ();
        }
    }
    
    private SignatureHelpWindow getSignatureHelpWindow () {
        if (mSignatureHelpWindow == null) {
            mSignatureHelpWindow = new SignatureHelpWindow (this);
        }
        
        return mSignatureHelpWindow;
    }
    
    private DiagnosticWindow getDiagnosticWindow () {
        if (mDiagnosticWindow == null) {
            mDiagnosticWindow = new DiagnosticWindow (this);
        }
        
        return mDiagnosticWindow;
    }
    
    /**
     * A text action presenter presents text actions (cut, copy, paste, etc). <br>
     * <p>
     * <strong>The presenter handles its visibility itself.</strong>
     * </p>
     *
     * @author Akash Yadav
     */
    public interface ITextActionPresenter {
        
        /**
         * Bind the action presenter with the given editor instance.
         *
         * @param editor The editor to bind with.
         */
        void bindEditor (@NonNull IDEEditor editor);
        
        /**
         * Register the text action with this presenter.
         *
         * @param action The action to register.
         */
        void registerAction (@NonNull TextAction action);
    
        /**
         * Destroy this action presenter. The presenter should unsubscribe
         * from any subscribed events and release any held resources.
         */
        void destroy ();
    }
    
    /**
     * A model class for text actions.
     *
     * @author Akash Yadav
     */
    public static class TextAction implements Comparable<TextAction> {
        
        public static final int EXPAND_SELECTION = 4;
        public static final int GOTO_DEFINITION = 5;
        public static final int FIND_REFERENCES = 6;
        public static final int COMMENT_LINE = 7;
        public static final int UNCOMMENT_LINE = 8;
        
        // common action IDs
        public static final int PASTE = 3;
        public static final int COPY = 2;
        public static final int CUT = 1;
        public static final int SELECT_ALL = 0;
        
        /**
         * The drawable resource id for this text action.
         */
        public Drawable icon;
        
        /**
         * The string resource id for this text action.
         */
        @StringRes
        public int titleId;
        
        /**
         * The ID of this text action;
         */
        public final int id;
        
        /**
         * The index at which this action should be placed.
         */
        public final int index;
        
        public TextAction (Drawable icon, int titleId, int id, int index) {
            this.icon = icon;
            this.titleId = titleId;
            this.id = id;
            this.index = index;
        }
        
        @Override
        public boolean equals (Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof TextAction)) {
                return false;
            }
            TextAction that = (TextAction) o;
            return id == that.id;
        }
        
        @Override
        public int hashCode () {
            return Objects.hash (id);
        }
        
        @Override
        public int compareTo (TextAction o) {
            return Integer.compare (this.index, o.index);
        }
    }
}