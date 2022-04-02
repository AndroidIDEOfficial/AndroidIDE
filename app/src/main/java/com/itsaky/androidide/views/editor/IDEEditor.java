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

import static com.itsaky.androidide.views.editor.IDEEditor.TextAction.QUICKFIX;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
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
import com.itsaky.androidide.language.IAnalyzeManager;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.CodeActionParams;
import com.itsaky.lsp.models.CodeActionResult;
import com.itsaky.lsp.models.Command;
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
import com.itsaky.lsp.util.DiagnosticUtil;
import com.itsaky.toaster.Toaster;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;

public class IDEEditor extends CodeEditor {

    public static final String KEY_FILE = "editor_file";
    private static final Logger LOG = Logger.newInstance("IDEEditor");
    private int mFileVersion;
    private File file;
    private ILanguageServer mLanguageServer;
    private IDELanguageClientImpl mLanguageClient;
    private SignatureHelpWindow mSignatureHelpWindow;
    private DiagnosticWindow mDiagnosticWindow;
    private ITextActionPresenter mTextActionPresenter;

    public IDEEditor(Context context) {
        this(context, null);
    }

    public IDEEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IDEEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IDEEditor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setColorScheme(new SchemeAndroidIDE());
        setTextActionPresenter(chooseTextActionPresenter());
        subscribeEvent(SelectionChangeEvent.class, this::handleSelectionChange);
        subscribeEvent(ContentChangeEvent.class, this::handleContentChange);

        setInputType(createInputFlags());
    }

    public static int createInputFlags() {
        var flags =
                EditorInfo.TYPE_CLASS_TEXT
                        | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                        | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        if (StudioApp.getInstance()
                .getPrefManager()
                .getBoolean(PreferenceManager.KEY_EDITOR_FLAG_PASSWORD, true)) {
            flags |= EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }

        return flags;
    }

    @NonNull
    @Contract(" -> new")
    public ITextActionPresenter chooseTextActionPresenter() {
        final var prefs = StudioApp.getInstance().getPrefManager();
        final var usePopup = prefs.getBoolean(PreferenceManager.KEY_EDITOR_USE_POPUP, false);
        if (usePopup) {
            return new EditorTextActionWindow(this);
        } else {
            return new EditorTextActionMode();
        }
    }

    public void analyze() {
        if (mLanguageServer != null
                && getFile() != null
                && getEditorLanguage() instanceof IDELanguage) {
            CompletableFuture.supplyAsync(() -> mLanguageServer.analyze(getFile().toPath()))
                    .whenComplete(
                            (diagnostics, throwable) -> {
                                final var lang = (IDELanguage) getEditorLanguage();
                                final var analyzer = lang.getAnalyzeManager();
                                if (analyzer instanceof IAnalyzeManager) {
                                    ((IAnalyzeManager) analyzer).updateDiagnostics(diagnostics);
                                }
                                analyzer.rerun();
                            });
        }
    }

    /**
     * Checks if the given range is valid for this editor's text.
     *
     * @param range The range to check.
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public boolean isValidRange(final Range range) {
        if (range == null) {
            return false;
        }

        final var start = range.getStart();
        final var end = range.getEnd();

        return isValidPosition(start)
                && isValidPosition(end)
                && start.compareTo(end) < 0; // make sure start position is before end position
    }

    /**
     * Checks if the given position is valid for this editor's text.
     *
     * @param position The position to check.
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public boolean isValidPosition(final Position position) {
        if (position == null) {
            return false;
        }

        return isValidLine(position.getLine())
                && isValidColumn(position.getLine(), position.getColumn());
    }

    /**
     * Checks if the given line is valid for this editor's text.
     *
     * @param line The line to check.
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public boolean isValidLine(int line) {
        return line >= 0 && line < getText().getLineCount();
    }

    /**
     * Checks if the given column is valid for this editor's text.
     *
     * @param line The line of the column to check.
     * @param column The column to check.
     * @return <code>true</code> if valid, <code>false</code> otherwise.
     */
    public boolean isValidColumn(int line, int column) {
        return column >= 0 && column < getText().getColumnCount(line);
    }

    /**
     * Set selection to the given range.
     *
     * @param range The range to select.
     */
    public void setSelection(@NonNull Range range) {
        if (isValidRange(range)) {
            setSelectionRegion(
                    range.getStart().getLine(),
                    range.getStart().getColumn(),
                    range.getEnd().getLine(),
                    range.getEnd().getColumn());
        }
    }

    /**
     * Set the selection of this editor to the given position.
     *
     * @param position The position to select.
     */
    public void setSelection(@NonNull Position position) {
        setSelection(position.getLine(), position.getColumn());
    }

    /**
     * Get the file that this editor is currently editing.
     *
     * @return The file instance.
     */
    public File getFile() {
        return file;
    }

    /**
     * Set the file that this editor is currently editing.
     *
     * @param file The file to set.
     */
    public void setFile(File file) {
        this.file = file;

        if (file != null && mLanguageServer != null) {
            final var documentHandler = mLanguageServer.getDocumentHandler();
            if (documentHandler.accepts(file.toPath())) {
                final var text = getText().toString();
                final var event = new DocumentOpenEvent(file.toPath(), text, mFileVersion = 0);
                documentHandler.onFileOpened(event);
            }

            // request diagnostics
            analyze();
        }

        if (file != null) {
            getExtraArguments().putString(KEY_FILE, file.getAbsolutePath());
        } else {
            getExtraArguments().remove(KEY_FILE);
        }
    }

    /**
     * Set the language server that this editor will connect with. If the language client is not
     * set, it'll be set to {@link IDELanguageClientImpl}.
     *
     * @param server The server to set. Provide <code>null</code> to disable all the language server
     *     features.
     */
    public void setLanguageServer(ILanguageServer server) {
        this.mLanguageServer = server;

        if (mLanguageClient == null && IDELanguageClientImpl.isInitialized()) {
            mLanguageClient = IDELanguageClientImpl.getInstance();
        }
    }

    /**
     * Append the given text at the end of the editor's content.
     *
     * @param text The text to append.
     * @return The line at which the text was appended.
     */
    public int append(CharSequence text) {
        final var content = getText();
        if (getLineCount() <= 0) {
            return 0;
        }
        final int line = getLineCount() - 1;
        int col = content.getColumnCount(line);
        if (col < 0) {
            col = 0;
        }
        content.insert(line, col, text);
        return line;
    }

    /** Set the selection of the editor's cursor to the last line of the it's content. */
    public void goToEnd() {
        final var line = getText().getLineCount() - 1;
        setSelection(line, 0);
    }

    /** Comment the line of the left cursor. */
    public void commentLine() {
        if (getFile() == null) {
            return;
        }

        final var text = getText();
        final var name = getFile().getName();
        int line = getCursor().getLeftLine();
        if (name.endsWith(".java") || name.endsWith(".gradle")) {
            while (line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                if (!text.getLineString(line).trim().startsWith("//")) {
                    text.insert(line, 0, "//");
                }
                line++;
            }
        } else if (name.endsWith(".xml")) {
            while (line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                final String lineString = text.getLineString(line);
                if (!lineString.trim().startsWith("<!--") && !lineString.trim().endsWith("-->")) {
                    text.replace(
                            line,
                            0,
                            line,
                            text.getColumnCount(line),
                            "<!--".concat(lineString).concat("-->"));
                }
                line++;
            }
        }
    }

    /** Uncomment the current line */
    public void uncommentLine() {
        if (getFile() == null) {
            return;
        }

        final var text = getText();
        final String name = getFile().getName();
        int line = getCursor().getLeftLine();
        if (name.endsWith(".java") || name.endsWith(".gradle")) {
            while (line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                String l = text.getLineString(line);
                if (l.trim().startsWith("//")) {
                    int i = l.indexOf("//");
                    text.delete(line, i, line, i + 2);
                }
                line++;
            }
        } else if (name.endsWith(".xml")) {
            final String commentStart = "<!--";
            final String commentEnd = "-->";
            while (line >= getCursor().getLeftLine() && line <= getCursor().getRightLine()) {
                String l = text.getLineString(line);
                if (l.trim().startsWith(commentStart)) {
                    int i = l.indexOf(commentStart);
                    text.delete(line, i, line, i + commentStart.length());
                }
                if (l.trim().endsWith(commentEnd)) {
                    int count = text.getColumnCount(line);
                    text.delete(line, count - commentEnd.length(), line, count);
                }
                line++;
            }
        }
    }

    /**
     * If any language server is set, asks the language server to find the definition of token at
     * the cursor position.
     *
     * <p>If the server returns a valid response, and the file specified in the response is same the
     * file in this editor, the range specified in the response will be selected.
     */
    @SuppressWarnings("deprecation")
    public void findDefinition() {
        if (getFile() == null) {
            return;
        }

        final ProgressDialog pd =
                ProgressDialog.show(
                        getContext(),
                        null,
                        getContext().getString(R.string.msg_finding_definition));

        try {
            final CompletableFuture<DefinitionResult> future =
                    CompletableFuture.supplyAsync(
                            () -> {
                                final var params =
                                        new com.itsaky.lsp.models.DefinitionParams(
                                                getFile().toPath(),
                                                new com.itsaky.lsp.models.Position(
                                                        getCursor().getLeftLine(),
                                                        getCursor().getLeftColumn()));

                                return mLanguageServer.findDefinition(params);
                            });

            future.whenComplete(
                    (result, error) -> {
                        if (result == null
                                || mLanguageClient == null
                                || future.isCancelled()
                                || future.isCompletedExceptionally()) {
                            LOG.error("An error occurred while finding definition", error);
                            showDefinitionNotFound(pd);
                            return;
                        }

                        final var locations = result.getLocations();
                        if (locations.size() <= 0) {
                            LOG.error("No definitions found", "Size:", locations.size());
                            showDefinitionNotFound(pd);
                            return;
                        }

                        ThreadUtils.runOnUiThread(
                                () -> {
                                    if (locations.size() == 1) {
                                        var location = locations.get(0);
                                        if (location.getFile().equals(getFile().toPath())) {
                                            setSelection(location.getRange());
                                            return;
                                        }
                                        mLanguageClient.showDocument(
                                                new ShowDocumentParams(
                                                        location.getFile(), location.getRange()));
                                    } else {
                                        mLanguageClient.showLocations(locations);
                                    }
                                });

                        dismissOnUiThread(pd);
                    });
        } catch (Throwable th) {
            LOG.error("An error occurred while finding definition", th);
            showDefinitionNotFound(pd);
        }
    }

    /**
     * Notify the user that no definitions can be found.
     *
     * @param pd The {@link ProgressDialog} that was shown when requesting the definitions.
     */
    @SuppressWarnings("deprecation")
    private void showDefinitionNotFound(final ProgressDialog pd) {
        ThreadUtils.runOnUiThread(
                () -> {
                    StudioApp.getInstance().toast(R.string.msg_no_definition, Toaster.Type.ERROR);
                    pd.dismiss();
                });
    }

    /**
     * If any language server instance is set, finds the references to of the token at the current
     * cursor position.
     *
     * <p>If the server returns a valid response, that response is forwarded to the {@link
     * IDELanguageClientImpl}.
     */
    public void findReferences() {
        if (getFile() == null) {
            return;
        }

        @SuppressWarnings("deprecation")
        final ProgressDialog pd =
                ProgressDialog.show(
                        getContext(),
                        null,
                        getContext().getString(R.string.msg_finding_references));

        try {
            final CompletableFuture<ReferenceResult> future =
                    CompletableFuture.supplyAsync(
                            () -> {
                                final var referenceParams =
                                        new ReferenceParams(
                                                getFile().toPath(),
                                                new com.itsaky.lsp.models.Position(
                                                        getCursor().getLeftLine(),
                                                        getCursor().getLeftColumn()),
                                                true);
                                return mLanguageServer.findReferences(referenceParams);
                            });

            future.whenComplete(
                    (result, error) -> {
                        if (result == null
                                || mLanguageClient == null
                                || future.isCancelled()
                                || future.isCompletedExceptionally()) {
                            LOG.error("An error occurred while finding references", error);
                            showReferencesNotFound(pd);
                            return;
                        }

                        if (result.getLocations().isEmpty()) {
                            showReferencesNotFound(pd);
                            return;
                        } else {
                            if (result.getLocations().size() == 1) {
                                final var loc = result.getLocations().get(0);
                                if (loc.getFile().equals(getFile().toPath())) {
                                    setSelection(loc.getRange());
                                    return;
                                }
                            }

                            ThreadUtils.runOnUiThread(
                                    () -> mLanguageClient.showLocations(result.getLocations()));
                        }

                        dismissOnUiThread(pd);
                    });
        } catch (Throwable th) {
            LOG.error("An error occurred while finding references", th);
            showReferencesNotFound(pd);
        }
    }

    /**
     * Notify the user that no references were found for the selected token.
     *
     * @param pd The {@link ProgressDialog} that was shown when requesting references.
     */
    @SuppressWarnings("deprecation")
    private void showReferencesNotFound(final ProgressDialog pd) {
        ThreadUtils.runOnUiThread(
                () -> {
                    StudioApp.getInstance().toast(R.string.msg_no_references, Toaster.Type.ERROR);
                    pd.dismiss();
                });
    }

    /**
     * If any language server is set, requests signature help at the cursor's position. On a valid
     * response, shows the signature help in a popup window.
     */
    @SuppressWarnings("unused") // accessed using reflection in CompletionItem class
    public void signatureHelp() {
        if (mLanguageServer != null && getFile() != null) {
            final CompletableFuture<SignatureHelp> future =
                    CompletableFuture.supplyAsync(
                            () ->
                                    mLanguageServer.signatureHelp(
                                            new SignatureHelpParams(
                                                    getFile().toPath(),
                                                    new Position(
                                                            getCursor().getLeftLine(),
                                                            getCursor().getLeftColumn()))));

            future.whenComplete(
                    (help, error) -> {
                        if (help == null
                                || mLanguageClient == null
                                || future.isCancelled()
                                || future.isCompletedExceptionally()) {
                            LOG.error("An error occurred while finding signature help", error);
                            return;
                        }

                        ThreadUtils.runOnUiThread(() -> showSignatureHelp(help));
                    });
        }
    }

    /**
     * Shows the given signature help in the editor.
     *
     * @param help The signature help data to show.
     */
    public void showSignatureHelp(SignatureHelp help) {
        getSignatureHelpWindow().setupAndDisplay(help);
    }

    /**
     * Dismisses the given dialog on the UI thread.
     *
     * @param dialog The dialog to dismiss.
     */
    private void dismissOnUiThread(@NonNull final Dialog dialog) {
        ThreadUtils.runOnUiThread(dialog::dismiss);
    }

    /** If any language server is set, notify the server that the file in this editor was saved. */
    public void didSave() {
        if (mLanguageServer != null && getFile() != null) {
            final var documentHandler = mLanguageServer.getDocumentHandler();
            final var file = getFile().toPath();
            if (documentHandler.accepts(file)) {
                documentHandler.onFileSaved(new DocumentSaveEvent(file));
            }
        }
    }

    /** Notify the language server that the file in this editor is about to be closed. */
    public void close() {
        if (mLanguageServer != null && getFile() != null) {
            final var documentHandler = mLanguageServer.getDocumentHandler();
            final var file = getFile().toPath();
            if (documentHandler.accepts(file)) {
                documentHandler.onFileClosed(new DocumentCloseEvent(file));
            }
            LOG.info("'textDocument/didClose' was sent to the language server.");
        } else {
            LOG.info("No language server is available for this file");
        }

        ensureWindowsDismissed();
    }

    public void onEditorSelected() {
        if (getFile() == null) {
            return;
        }

        final var path = getFile().toPath();
        if (mLanguageServer != null) {
            mLanguageServer.getDocumentHandler().onFileSelected(path);
        }
    }

    /**
     * Tells the language client to perform the given code action item.
     *
     * @param action The action to perform.
     */
    public void performCodeAction(CodeActionItem action) {
        if (mLanguageClient != null) {
            mLanguageClient.performCodeAction(this, action);
        }
    }

    @SuppressWarnings("unused")
    public void executeCommand(Command command) {
        if (command == null) {
            LOG.warn("Cannot execute command in editor. Command is null.");
            return;
        }

        LOG.info(String.format("Executing command '%s' for completion item.", command.getTitle()));
        switch (command.getCommand()) {
            case Command.TRIGGER_COMPLETION:
                final var completion = getComponent(EditorAutoCompletion.class);
                completion.requireCompletion();
                break;
            case Command.TRIGGER_PARAMETER_HINTS:
                signatureHelp();
                break;
            case Command.FORMAT_CODE:
                formatCodeAsync();
                break;
        }
    }

    /**
     * Request code actions from server at the given position containing given diagnostics
     *
     * @return A {@link CompletableFuture}. May return {@code null}.
     */
    public CompletableFuture<CodeActionResult> codeActions() {
        return codeActions(Collections.emptyList());
    }

    /**
     * Requests code actions for the given diagnostics to the language server.
     *
     * @param diagnostics The diagnostics to request code actions for.
     * @return The {@link CodeActionResult} from the server.
     */
    public CompletableFuture<CodeActionResult> codeActions(List<DiagnosticItem> diagnostics) {
        return codeActions(new CodeActionParams(getFile().toPath(), getCursorRange(), diagnostics));
    }

    public CompletableFuture<CodeActionResult> codeActions(CodeActionParams params) {
        if (mLanguageServer == null || mLanguageClient == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> mLanguageServer.codeActions(params));
    }

    /**
     * Requests the language server to provided a semantically larger selection than the current
     * selection. If a valid response is received, that range will be selected.
     */
    public void expandSelection() {
        if (mLanguageServer == null || getFile() == null) {
            LOG.error("Cannot expand selection. Language server or file is null");
            return;
        }

        //noinspection deprecation
        final var pd =
                ProgressDialog.show(
                        getContext(),
                        null,
                        getContext().getString(R.string.please_wait),
                        true,
                        false);
        final CompletableFuture<Range> future =
                CompletableFuture.supplyAsync(
                        () ->
                                mLanguageServer.expandSelection(
                                        new ExpandSelectionParams(
                                                getFile().toPath(), getCursorRange())));

        future.whenComplete(
                ((range, throwable) -> {
                    pd.dismiss();

                    if (throwable != null) {
                        LOG.error("Error computing expanded selection range", throwable);
                        return;
                    }

                    ThreadUtils.runOnUiThread(() -> setSelection(range));
                }));
    }

    /**
     * Get the cursor's position in the form of {@link Position}.
     *
     * @return The {@link Position} of the cursor.
     */
    @SuppressWarnings("unused")
    public Position getCursorAsLSPPosition() {
        return new Position(getCursor().getLeftLine(), getCursor().getLeftColumn());
    }

    /**
     * Get the cursor's selection range in the form of {@link Range}.
     *
     * @return The {@link Range} of the cursor.
     */
    public Range getCursorRange() {
        final var cursor = getCursor();
        final var start = new Position(cursor.getLeftLine(), cursor.getLeftColumn());
        final var end = new Position(cursor.getRightLine(), cursor.getRightColumn());
        return new Range(start, end);
    }

    /**
     * Validates the range if it is invalid and returns a valid range.
     *
     * @param range Th range to validate.
     * @return A new, validated range.
     */
    public Range validateRange(@NonNull final Range range) {
        final var start = range.getStart();
        final var end = range.getEnd();

        if (start.getLine() < 0) {
            start.setLine(0);
        } else if (start.getLine() >= getText().getLineCount()) {
            start.setLine(getText().getLineCount() - 1);
        }

        if (end.getLine() < 0) {
            end.setLine(0);
        } else if (end.getLine() >= getText().getLineCount()) {
            end.setLine(getText().getLineCount() - 1);
        }

        if (end.getLine() < start.getLine()) {
            var l = end.getLine();
            var l2 = start.getLine();
            start.setLine(l);
            end.setLine(l2);
        }

        if (start.getColumn() < 0) {
            start.setColumn(0);
        } else if (start.getColumn() >= getText().getColumnCount(start.getLine())) {
            start.setColumn(getText().getColumnCount(start.getLine()) - 1);
        }

        if (end.getColumn() < 0) {
            end.setColumn(0);
        } else if (end.getColumn() >= getText().getColumnCount(end.getLine())) {
            end.setColumn(getText().getColumnCount(end.getLine()) - 1);
        }

        if (end.getColumn() < start.getColumn()) {
            final var c = start.getColumn();
            final var c2 = end.getColumn();
            start.setColumn(c2);
            end.setColumn(c);
        }

        return new Range(start, end);
    }

    /**
     * Get the text action presenter attached with this editor.
     *
     * @return The attached text action presenter.
     */
    @SuppressWarnings("unused")
    public ITextActionPresenter getTextActionPresenter() {
        return mTextActionPresenter;
    }

    /**
     * Set the text action presenter of this editor.
     *
     * @param actionPresenter The presenter to set. Must not be <code>null</code>.
     */
    public void setTextActionPresenter(@NonNull ITextActionPresenter actionPresenter) {
        Objects.requireNonNull(actionPresenter, "Cannot set text action presenter to null");

        if (mTextActionPresenter != null) {
            mTextActionPresenter.destroy();
            mTextActionPresenter = null;
        }

        this.mTextActionPresenter = actionPresenter;

        actionPresenter.bindEditor(this);
        registerActionsTo(actionPresenter);
    }

    /**
     * Register the editor's actions to the given action presenter.
     *
     * @param actionPresenter The action presenter to register actions to.
     */
    public void registerActionsTo(@NonNull ITextActionPresenter actionPresenter) {
        Objects.requireNonNull(
                actionPresenter, "Cannot register actions to null text action presenter");

        var index = -1;

        TypedArray array =
                getContext()
                        .getTheme()
                        .obtainStyledAttributes(
                                new int[] {
                                    android.R.attr.actionModeSelectAllDrawable,
                                    android.R.attr.actionModeCutDrawable,
                                    android.R.attr.actionModeCopyDrawable,
                                    android.R.attr.actionModePasteDrawable,
                                });

        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_quickfix),
                        R.string.msg_code_actions,
                        TextAction.QUICKFIX,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_expand_selection),
                        R.string.action_expand_selection,
                        TextAction.EXPAND_SELECTION,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        tintDrawable(array.getDrawable(0)),
                        android.R.string.selectAll,
                        TextAction.SELECT_ALL,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        tintDrawable(array.getDrawable(1)),
                        android.R.string.cut,
                        TextAction.CUT,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        tintDrawable(array.getDrawable(2)),
                        android.R.string.copy,
                        TextAction.COPY,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        tintDrawable(array.getDrawable(3)),
                        android.R.string.paste,
                        TextAction.PASTE,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_goto_definition),
                        R.string.menu_navigate_definition,
                        TextAction.GOTO_DEFINITION,
                        index++));

        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_find_references),
                        R.string.menu_navigate_references,
                        TextAction.FIND_REFERENCES,
                        index++));
        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_comment_line),
                        R.string.menu_comment_line,
                        TextAction.COMMENT_LINE,
                        index++));

        //noinspection UnusedAssignment
        actionPresenter.registerAction(
                new TextAction(
                        createTextActionDrawable(R.drawable.ic_uncomment_line),
                        R.string.menu_uncomment_line,
                        TextAction.UNCOMMENT_LINE,
                        index++));

        array.recycle();
    }

    /**
     * Called by text action presenters to check if the action with the given ID should be shown or
     * not.
     *
     * @param actionId The action ID to check.
     * @return <code>true</code> if the action should be shown, <code>false</code> otherwise.
     */
    public boolean shouldShowTextAction(int actionId) {
        final var capabilities = mLanguageServer != null ? mLanguageServer.getCapabilities() : null;
        final var notNull = capabilities != null;
        final var expand = notNull && capabilities.getSmartSelectionsEnabled();
        final var definitions = notNull && capabilities.getDefinitionsAvailable();
        final var references = notNull && capabilities.getReferencesAvailable();
        final var commentUncomment =
                getFile() != null
                        && (getFile().getName().endsWith(".java")
                                || getFile().getName().endsWith(".gradle")
                                || getFile().getName().endsWith(".xml"));
        switch (actionId) {
            case TextAction.CUT:
            case TextAction.PASTE:
                return isEditable();
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

    /**
     * Performs the given text action in this editor.
     *
     * @param action The action to perform.
     */
    public void performTextAction(@NonNull TextAction action) {
        Objects.requireNonNull(action, "Cannot perform null text action");

        switch (action.id) {
            case TextAction.SELECT_ALL:
                selectAll();
                break;
            case TextAction.CUT:
                cutText();
                break;
            case TextAction.COPY:
                copyText();
                break;
            case TextAction.PASTE:
                pasteText();
                break;
            case TextAction.GOTO_DEFINITION:
                findDefinition();
                break;
            case TextAction.FIND_REFERENCES:
                findReferences();
                break;
            case TextAction.COMMENT_LINE:
                commentLine();
                break;
            case TextAction.UNCOMMENT_LINE:
                uncommentLine();
                break;
            case TextAction.EXPAND_SELECTION:
                expandSelection();
                break;
            case TextAction.QUICKFIX:
                showCodeActions(mTextActionPresenter.getActions());
                getTextActionPresenter().dismiss();
                break;
        }
    }

    /** Ensures that all the windows are dismissed. */
    public void ensureWindowsDismissed() {
        if (getDiagnosticWindow().isShowing()) {
            getDiagnosticWindow().dismiss();
        }

        if (getSignatureHelpWindow().isShowing()) {
            getSignatureHelpWindow().dismiss();
        }

        if (mTextActionPresenter != null) {
            mTextActionPresenter.dismiss();
        }
    }

    @SuppressWarnings("deprecation")
    private void showCodeActions() {
        if (!(getEditorLanguage() instanceof IDELanguage)) {
            return;
        }
        final var lang = (IDELanguage) getEditorLanguage();
        final var diagnostics = lang.getDiagnostics();
        if (diagnostics.isEmpty()) {
            LOG.error(
                    "No diagnostic items available. Code actions will be shown based on cursor position.");
        }

        final var diagnostic =
                DiagnosticUtil.binarySearchDiagnostic(diagnostics, getCursorAsLSPPosition());
        if (!diagnostics.isEmpty() && diagnostic == null) {
            LOG.info(
                    "No diagnostic found at cursor position. Code actions will be shown based on cursor position.");
        }

        final var future = codeActions(Collections.singletonList(diagnostic));
        final var pd =
                ProgressDialog.show(getContext(), null, "Computing code actions...", true, false);
        future.whenComplete(
                ((codeActionResult, throwable) -> {
                    dismissOnUiThread(pd);
                    if (codeActionResult == null
                            || codeActionResult.getActions().isEmpty()
                            || throwable != null) {
                        LOG.info("No code actions found.", throwable);
                        return;
                    }

                    ThreadUtils.runOnUiThread(() -> showCodeActions(codeActionResult.getActions()));
                }));
    }

    public void showCodeActions(List<CodeActionItem> actions) {
        final var titles = actions.stream().map(CodeActionItem::getTitle).toArray(String[]::new);
        final var builder = DialogUtils.newMaterialDialogBuilder(getContext());
        builder.setTitle("Select code action...");
        builder.setItems(
                titles,
                (dialog, which) -> {
                    final var action = actions.get(which);
                    if (action != null) {
                        performCodeAction(action);
                    } else {
                        LOG.error("Cannot perform code action. Action is null.");
                    }
                });
        builder.show();
    }

    private Drawable createTextActionDrawable(int icon) {
        return ContextCompat.getDrawable(getContext(), icon);
    }

    private Drawable tintDrawable(@NonNull Drawable drawable) {
        drawable.setTint(ContextCompat.getColor(getContext(), R.color.primaryIconColor));
        return drawable;
    }

    private void handleSelectionChange(
            @NonNull SelectionChangeEvent event, Unsubscribe unsubscribe) {
        if (event.isSelected() || !(getEditorLanguage() instanceof IDELanguage)) {
            // do not show diagnostics when text is selected
            // or if we cannot get diagnostics
            return;
        }

        final var diagnostics = ((IDELanguage) getEditorLanguage()).getDiagnostics();
        final var line = event.getLeft().line;
        final var column = event.getLeft().column;

        // diagnostics are expected to be sorted, so, we do a binary search
        final var diagnostic = binarySearchDiagnostic(diagnostics, line, column);
        getDiagnosticWindow().showDiagnostic(diagnostic);
    }

    @Nullable
    private DiagnosticItem binarySearchDiagnostic(
            @NonNull List<DiagnosticItem> diagnostics, int line, int column) {
        return DiagnosticUtil.binarySearchDiagnostic(diagnostics, line, column);
    }

    /**
     * Notify the language server that the content of this file has been changed.
     *
     * @param event The content change event.
     */
    private void handleContentChange(ContentChangeEvent event, Unsubscribe unsubscribe) {
        if (getFile() == null || mLanguageServer == null) {
            return;
        }

        final var documentHandler = mLanguageServer.getDocumentHandler();
        final var file = getFile().toPath();
        if (documentHandler.accepts(file)) {
            documentHandler.onContentChange(
                    new DocumentChangeEvent(file, getText(), mFileVersion + 1));
        }

        checkForSignatureHelp(event);
    }

    /**
     * Checks if the content change event should trigger signature help. Signature help trigger
     * characters are :
     *
     * <ul>
     *   <li><code>'('</code> (parentheses)
     *   <li><code>','</code> (comma)
     * </ul>
     *
     * @param event The content change event.
     */
    private void checkForSignatureHelp(@NonNull ContentChangeEvent event) {
        if (event.getAction() != ContentChangeEvent.ACTION_INSERT
                || event.getChangedText().length() != 1) {
            return;
        }

        final var ch = event.getChangedText().charAt(0);
        if (ch == '(' || ch == ',') {
            signatureHelp();
        }
    }

    private SignatureHelpWindow getSignatureHelpWindow() {
        if (mSignatureHelpWindow == null) {
            mSignatureHelpWindow = new SignatureHelpWindow(this);
        }

        return mSignatureHelpWindow;
    }

    private DiagnosticWindow getDiagnosticWindow() {
        if (mDiagnosticWindow == null) {
            mDiagnosticWindow = new DiagnosticWindow(this);
        }

        return mDiagnosticWindow;
    }

    /**
     * A text action presenter presents text actions (cut, copy, paste, etc). <br>
     *
     * <p><strong>The presenter handles its visibility itself.</strong>
     *
     * @author Akash Yadav
     */
    public interface ITextActionPresenter {
        /**
         * Bind the action presenter with the given editor instance.
         *
         * @param editor The editor to bind with.
         */
        void bindEditor(@NonNull IDEEditor editor);

        /**
         * Register the text action with this presenter.
         *
         * @param action The action to register.
         */
        void registerAction(@NonNull TextAction action);

        /**
         * Look for the action with the given id in the actions registry.
         *
         * @param id The id to look for.
         * @return The registered text action. Maybe <code>null</code>.
         */
        @Nullable
        TextAction findAction(int id);

        /** Invalidate the registered actions. */
        void invalidateActions();

        /** Dismiss the presenter. */
        void dismiss();

        /**
         * Destroy this action presenter. The presenter should unsubscribe from any subscribed
         * events and release any held resources.
         */
        void destroy();

        /**
         * Update the list of computed code actions.
         *
         * @param actions The new list of code actions.
         */
        void updateCodeActions(@NonNull List<CodeActionItem> actions);

        /**
         * Get the computed code actions.
         *
         * @return The list of code actions.
         */
        @NonNull
        List<CodeActionItem> getActions();

        default boolean canShowAction(
                @NonNull IDEEditor editor, @NonNull IDEEditor.TextAction action) {
            if (action.id == QUICKFIX) {
                try {
                    // If code actions are not returned within 150ms, hide the action.
                    final var future = computeCodeActions(editor);
                    final var result = future.get(150, TimeUnit.MILLISECONDS);
                    List<CodeActionItem> actions =
                            result == null ? Collections.emptyList() : result.getActions();
                    actions.removeIf(codeAction -> codeAction.getChanges().isEmpty());
                    updateCodeActions(actions);
                    return !actions.isEmpty();
                } catch (Throwable th) {
                    if (!(th instanceof TimeoutException)) {
                        LOG.error("Unable to calculate code actions", th);
                    }

                    return false;
                }
            }

            // all the actions are visible by default
            // so we need to get a confirmation from the editor
            if (action.visible) {
                return editor.shouldShowTextAction(action.id);
            }

            return false;
        }

        default CompletableFuture<CodeActionResult> computeCodeActions(IDEEditor editor) {
            return CompletableFuture.supplyAsync(
                    () -> {
                        if (editor.getFile() == null) {
                            throw new CompletionException(
                                    new NullPointerException(
                                            "Cannot compute code actions. "
                                                    + "No file is set to editor."));
                        }

                        final var file = editor.getFile().toPath();
                        final var range = editor.getCursorRange();
                        List<DiagnosticItem> diagnostics =
                                editor.getEditorLanguage() instanceof IDELanguage
                                        ? ((IDELanguage) editor.getEditorLanguage())
                                                .getDiagnostics()
                                        : Collections.emptyList();

                        final var diagnostic =
                                DiagnosticUtil.binarySearchDiagnostic(
                                        diagnostics, editor.getCursorAsLSPPosition());
                        diagnostics =
                                diagnostic == null
                                        ? Collections.emptyList()
                                        : Collections.singletonList(diagnostic);
                        final var params = new CodeActionParams(file, range, diagnostics);
                        LOG.debug("Requesting code actions for params:", params);
                        try {
                            return editor.codeActions(params).get();
                        } catch (Throwable e) {
                            throw new CompletionException(e);
                        }
                    });
        }
    }

    /**
     * A model class for text actions.
     *
     * @author Akash Yadav
     */
    public static class TextAction implements Comparable<TextAction> {

        public static final int QUICKFIX = 9;
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
        /** The ID of this text action; */
        public final int id;
        /** The index at which this action should be placed. */
        public final int index;
        /** The drawable resource id for this text action. */
        public Drawable icon;
        /** The string resource id for this text action. */
        @StringRes public int titleId;
        /** Whether this action should be visible to user. */
        public boolean visible = true;

        public TextAction(Drawable icon, int titleId, int id, int index) {
            this.icon = icon;
            this.titleId = titleId;
            this.id = id;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
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
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public int compareTo(TextAction o) {
            return Integer.compare(this.index, o.index);
        }
    }
}
