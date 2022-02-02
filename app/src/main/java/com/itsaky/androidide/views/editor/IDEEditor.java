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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
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

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.layout.Layout;

public class IDEEditor extends CodeEditor {
    
    private int mFileVersion;
    private File file;
    
    private ILanguageServer mLanguageServer;
    private IDELanguageClientImpl mLanguageClient;
    private SignatureHelpWindow mSignatureHelpWindow;
    
    @SuppressWarnings("FieldCanBeLocal,unused")
    private final EditorTextActionMode mActionMode;
    
    public static final String KEY_FILE = "editor_file";
    private static final Logger LOG = Logger.instance ("IDEEditor");
    
    public IDEEditor (Context context) {
        this(context, null);
    }
    
    public IDEEditor (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public IDEEditor (Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    
    public IDEEditor (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        
        this.mActionMode = new EditorTextActionMode (this);
        
        setColorScheme (new SchemeAndroidIDE ());
        subscribeEvent (ContentChangeEvent.class, (event, unsubscribe) -> notifyContentChanged ());
        
        // default editor input type + no suggestions flag
        setInputType (EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }
    
    /**
     * Set selection to the given range.
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
     * @param position The position to select.
     */
    public void setSelection (@NonNull Position position) {
        setSelection (position.getLine (), position.getColumn ());
    }
    
    /**
     * Set the file that this editor is currently editing.
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
     * @see CodeEditor#updateCursorAnchor()
     */
    public float updateCursorAnchor () {
        return super.updateCursorAnchor ();
    }
    
    /**
     * The the coordinates of the left cursor.
     * @return A <code>float[]</code> containing the cursor position.
     */
    public float[] getCursorPosition () {
        return getLayout().getCharLayoutOffset (getCursor ().getLeftLine (), getCursor ().getLeftColumn ());
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
    @SuppressWarnings ("deprecation")
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
     * @param pd The {@link ProgressDialog} that was shown when requesting the definitions.
     */
    @SuppressWarnings ("deprecation")
    private void showDefinitionNotFound (final ProgressDialog pd) {
        ThreadUtils.runOnUiThread (() -> {
            StudioApp.getInstance ().toast (R.string.msg_no_definition, Toaster.Type.ERROR);
            pd.dismiss ();
        });
    }
    
    /**
     * If any language server instance is set,
     * finds the references to of the token at the current cursor position.
     *
     * If the server returns a valid response, that response is forwarded
     * to the {@link IDELanguageClientImpl}.
     */
    public void findReferences () {
        if (getFile () == null) {
            return;
        }
        
        @SuppressWarnings ("deprecation")
        final ProgressDialog pd = ProgressDialog.show (getContext (), null, getContext ().getString (R.string.msg_finding_references));
        
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
        getSignatureHelpWindow ().setSignatureHelp (help);
        getSignatureHelpWindow ().show ();
    }
    
    /**
     * Dismisses the given dialog on the UI thread.
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
            
            setSelection (range);
        }));
    }
    
    /**
     * Get the cursor's position in the form of {@link Position}.
     * @return The {@link Position} of the cursor.
     */
    public Position getCursorAsLSPPosition () {
        return new Position (getCursor ().getLeftLine (), getCursor ().getLeftColumn ());
    }
    
    /**
     * Get the cursor's selection range in the form of {@link Range}.
     * @return The {@link Range} of the cursor.
     */
    public Range getCursorRange () {
        final var cursor = getCursor ();
        final var start = new Position (cursor.getLeftLine (), cursor.getLeftColumn ());
        final var end = new Position (cursor.getRightLine (), cursor.getRightColumn ());
        return new Range (start, end);
    }
    
    // FIXME This is temporary. Find another way to show diagnostic messages in editor.
    private Layout getLayout () {
        final var c = CodeEditor.class;
        final Field f;
        try {
            f = c.getDeclaredField ("mLayout");
            f.setAccessible (true);
            return (Layout) f.get (this);
        } catch (Throwable e) {
            throw new RuntimeException (e);
        }
    }
    
    /**
     * Notify the language server that the content of this file
     * has been changed.
     */
    private void notifyContentChanged () {
        
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
    }
    
    private SignatureHelpWindow getSignatureHelpWindow () {
        if (mSignatureHelpWindow == null) {
            mSignatureHelpWindow = new SignatureHelpWindow (this);
        }
        
        return mSignatureHelpWindow;
    }
}