/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.editor.ui;

import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getTabSize;
import static com.itsaky.androidide.preferences.internal.EditorPreferencesKt.getVisiblePasswordFlag;
import static com.itsaky.androidide.resources.R.string;
import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.editor.R;
import com.itsaky.androidide.editor.adapters.CompletionListAdapter;
import com.itsaky.androidide.editor.api.IEditor;
import com.itsaky.androidide.editor.api.ILspEditor;
import com.itsaky.androidide.editor.language.IDELanguage;
import com.itsaky.androidide.editor.language.cpp.CppLanguage;
import com.itsaky.androidide.editor.language.groovy.GroovyLanguage;
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage;
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguageProvider;
import com.itsaky.androidide.editor.schemes.IDEColorScheme;
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider;
import com.itsaky.androidide.editor.snippets.AbstractSnippetVariableResolver;
import com.itsaky.androidide.editor.snippets.FileVariableResolver;
import com.itsaky.androidide.editor.snippets.WorkspaceVariableResolver;
import com.itsaky.androidide.eventbus.events.editor.ChangeType;
import com.itsaky.androidide.eventbus.events.editor.ColorSchemeInvalidatedEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentSaveEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentSelectedEvent;
import com.itsaky.androidide.flashbar.Flashbar;
import com.itsaky.androidide.lsp.api.ILanguageClient;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.models.Command;
import com.itsaky.androidide.lsp.models.DefinitionParams;
import com.itsaky.androidide.lsp.models.DefinitionResult;
import com.itsaky.androidide.lsp.models.ExpandSelectionParams;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.lsp.models.ShowDocumentParams;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.progress.ICancelChecker;
import com.itsaky.androidide.projects.FileManager;
import com.itsaky.androidide.syntax.colorschemes.DynamicColorScheme;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.DocumentUtils;
import com.itsaky.androidide.utils.FlashbarActivityUtilsKt;
import com.itsaky.androidide.utils.FlashbarUtilsKt;
import com.itsaky.androidide.utils.ILogger;
import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import io.github.rosemoe.sora.lang.EmptyLanguage;
import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.IDEEditorSearcher;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow;
import io.github.rosemoe.sora.widget.snippet.variable.ISnippetVariableResolver;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import kotlin.io.FilesKt;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IDEEditor extends CodeEditor implements IEditor, ILspEditor {

  private static final long SELECTION_CHANGE_DELAY = 500;

  private static final ILogger LOG = ILogger.newInstance("IDEEditor");
  private int fileVersion;
  private File file;
  private boolean isModified;
  private boolean ensurePosAnimEnabled = true;
  @Nullable
  private EditorActionsMenu actionsMenu;
  private IDEEditorSearcher searcher;
  private ILanguageServer languageServer;
  private SignatureHelpWindow signatureHelpWindow;
  private DiagnosticWindow diagnosticWindow;
  @Nullable
  ILanguageClient languageClient;

  private final Handler selectionChangeHandler = new Handler(Looper.getMainLooper());

  @Nullable
  private Runnable selectionChangeRunner = () -> {
    final var cursor = getCursor();

    // we do not use getSignatureHelpWindow() to avoid initializing the window unnecessarily
    if (languageClient == null || cursor == null || cursor.isSelected() ||
        (signatureHelpWindow != null && signatureHelpWindow.isShowing())) {
      return;
    }

    final var line = cursor.getLeftLine();
    final var column = cursor.getLeftColumn();

    // diagnostics are expected to be sorted, so, we do a binary search
    getDiagnosticWindow().showDiagnostic(languageClient.getDiagnosticAt(getFile(), line, column));
  };

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

    this.isModified = false;
    this.actionsMenu = new EditorActionsMenu(this);
    this.actionsMenu.init();

    final var window = new EditorCompletionWindow(this);
    window.setAdapter(new CompletionListAdapter());
    replaceComponent(EditorAutoCompletion.class, window);

    setColorScheme(SchemeAndroidIDE.newInstance(context));
    setSearcher(new IDEEditorSearcher(this));
    getComponent(EditorTextActionWindow.class).setEnabled(false);
    subscribeEvent(SelectionChangeEvent.class, this::handleSelectionChange);
    subscribeEvent(ContentChangeEvent.class, this::handleContentChange);

    setInputType(createInputFlags());

    EventBus.getDefault().register(this);
  }

  private void handleSelectionChange(@NonNull SelectionChangeEvent event, Unsubscribe unsubscribe) {
    if (getDiagnosticWindow().isShowing()) {
      getDiagnosticWindow().dismiss();
    }

    if (selectionChangeRunner != null) {
      selectionChangeHandler.removeCallbacks(selectionChangeRunner);
      selectionChangeHandler.postDelayed(selectionChangeRunner, SELECTION_CHANGE_DELAY);
    }
  }

  private DiagnosticWindow getDiagnosticWindow() {
    if (diagnosticWindow == null) {
      diagnosticWindow = new DiagnosticWindow(this);
    }

    return diagnosticWindow;
  }

  public void setEnsurePosAnimEnabled(boolean ensurePosAnimEnabled) {
    this.ensurePosAnimEnabled = ensurePosAnimEnabled;
  }

  public boolean isEnsurePosAnimEnabled() {
    return ensurePosAnimEnabled;
  }

  /**
   * Get the file that this editor is currently editing.
   *
   * @return The file instance.
   */
  @Override
  public File getFile() {
    return file;
  }

  /**
   * Set the file that this editor is currently editing.
   *
   * @param file The file to set.
   */
  public void setFile(File file) {
    updateFile(file);

    if (file != null) {
      dispatchDocumentOpenEvent();
    }
  }

  public void updateFile(File file) {
    this.file = file;
  }

  @NonNull
  @Override
  public Bundle getExtraArguments() {
    var args = super.getExtraArguments();
    final var path = file == null ? null : file.getAbsolutePath();
    args.putString(KEY_FILE, path);
    return args;
  }

  protected void dispatchDocumentOpenEvent() {
    if (getFile() == null) {
      return;
    }

    final var openEvent = new DocumentOpenEvent(getFile().toPath(), getText().toString(),
        fileVersion = 0);

    // Notify FileManager first
    FileManager.INSTANCE.onDocumentOpen(openEvent);

    EventBus.getDefault().post(openEvent);
  }

  @Override
  public boolean isModified() {
    return isModified;
  }

  /**
   * If any language server is set, requests signature help at the cursor's position. On a valid
   * response, shows the signature help in a popup window.
   */
  @Override
  public void signatureHelp() {
    if (languageServer != null && getFile() != null) {
      final CompletableFuture<SignatureHelp> future = CompletableFuture.supplyAsync(
          () -> languageServer.signatureHelp(
              new SignatureHelpParams(getFile().toPath(), getCursorLSPPosition())));

      future.whenComplete((help, error) -> {
        if (help == null || languageClient == null || future.isCancelled() ||
            future.isCompletedExceptionally()) {
          LOG.error("An error occurred while finding signature help", error);
          return;
        }

        //noinspection ConstantConditions
        ThreadUtils.runOnUiThread(() -> showSignatureHelp(help));
      });
    }
  }

  @Override
  public int getTabWidth() {
    return getTabSize();
  }

  /**
   * Shows the given signature help in the editor.
   *
   * @param help The signature help data to show.
   */
  @Override
  public void showSignatureHelp(SignatureHelp help) {
    getSignatureHelpWindow().setupAndDisplay(help);
  }

  private SignatureHelpWindow getSignatureHelpWindow() {
    if (signatureHelpWindow == null) {
      signatureHelpWindow = new SignatureHelpWindow(this);
    }

    return signatureHelpWindow;
  }

  /**
   * Set the selection of this editor to the given position.
   *
   * @param position The position to select.
   */
  @Override
  public void setSelection(@NonNull Position position) {
    setSelection(position.getLine(), position.getColumn());
  }

  /**
   * Set selection to the given range.
   *
   * @param range The range to select.
   */
  @Override
  public void setSelection(@NonNull Range range) {
    if (isValidPosition(range.getStart(), true) && isValidPosition(range.getEnd(), true)) {
      setSelectionRegion(range.getStart().getLine(), range.getStart().getColumn(),
          range.getEnd().getLine(), range.getEnd().getColumn());
    } else {
      LOG.warn("Selection range is invalid", range);
    }
  }

  @Override
  public void ensurePositionVisible(int line, int column, boolean noAnimation) {
    super.ensurePositionVisible(line, column, !isEnsurePosAnimEnabled() || noAnimation);
  }

  /**
   * Get the cursor's selection range in the form of {@link Range}.
   *
   * @return The {@link Range} of the cursor.
   */
  @Override
  public Range getCursorLSPRange() {
    final var cursor = getCursor();
    final var start = new Position(cursor.getLeftLine(), cursor.getLeftColumn());
    final var end = new Position(cursor.getRightLine(), cursor.getRightColumn());
    return new Range(start, end);
  }

  /**
   * Get the cursor's position in the form of {@link Position}.
   *
   * @return The {@link Position} of the cursor.
   */
  @Override
  @SuppressWarnings("unused")
  public Position getCursorLSPPosition() {
    return new Position(getCursor().getLeftLine(), getCursor().getLeftColumn());
  }

  /**
   * Validates the range if it is invalid and returns a valid range.
   *
   * @param range Th range to validate.
   */
  @Override
  public void validateRange(@NonNull final Range range) {
    final var start = range.getStart();
    final var end = range.getEnd();
    final var text = getText();
    final var lineCount = text.getLineCount();

    start.setLine(min(max(0, start.getLine()), lineCount - 1));
    start.setColumn(min(max(0, start.getColumn()), text.getColumnCount(start.getLine())));

    end.setLine(min(max(0, end.getLine()), lineCount - 1));
    end.setColumn(min(max(0, end.getColumn()), text.getColumnCount(end.getLine())));
  }

  /**
   * Checks if the given range is valid for this editor's text.
   *
   * @param range The range to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  @Override
  public boolean isValidRange(final Range range, boolean allowColumnEqual) {
    if (range == null) {
      return false;
    }

    final var start = range.getStart();
    final var end = range.getEnd();

    return isValidPosition(start, allowColumnEqual) && isValidPosition(end, allowColumnEqual) &&
        start.compareTo(end) < 0; // make sure start position is before end position
  }

  /**
   * Checks if the given position is valid for this editor's text.
   *
   * @param position The position to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  @Override
  public boolean isValidPosition(final Position position, boolean allowColumnEqual) {
    if (position == null) {
      return false;
    }

    return isValidLine(position.getLine()) &&
        isValidColumn(position.getLine(), position.getColumn(), allowColumnEqual);
  }

  /**
   * Checks if the given line is valid for this editor's text.
   *
   * @param line The line to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  @Override
  public boolean isValidLine(int line) {
    return line >= 0 && line < getText().getLineCount();
  }

  /**
   * Checks if the given column is valid for this editor's text.
   *
   * @param line   The line of the column to check.
   * @param column The column to check.
   * @return <code>true</code> if valid, <code>false</code> otherwise.
   */
  @Override
  public boolean isValidColumn(int line, int column, boolean allowEqual) {
    final var columnCount = getText().getColumnCount(line);
    return column >= 0 && (column < columnCount || (allowEqual && column == columnCount));
  }

  @Override
  @UiThread
  public void replaceContent(final CharSequence newContent) {
    final var lastLine = getText().getLineCount() - 1;
    final var lastColumn = getText().getColumnCount(lastLine);
    getText().replace(0, 0, lastLine, lastColumn, newContent == null ? "" : newContent);
  }

  /**
   * Set the language server that this editor will connect with. If the language client is not set,
   * it'll be set to {@link ILanguageClient} set in the language server.
   *
   * @param server The server to set. Provide <code>null</code> to disable all the language server
   *               features.
   */
  @Override
  public void setLanguageServer(ILanguageServer server) {
    this.languageServer = server;

    if (server != null) {
      this.languageClient = server.getClient();
      getSnippetController().setFileVariableResolver(new FileVariableResolver(this));
      getSnippetController().setWorkspaceVariableResolver(new WorkspaceVariableResolver());
    }
  }

  @Override
  public void setLanguageClient(@Nullable final ILanguageClient languageClient) {
    this.languageClient = languageClient;
  }

  /**
   * Append the given text at the end of the editor's content.
   *
   * @param text The text to append.
   * @return The line at which the text was appended.
   */
  @Override
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

  /**
   * Set the selection of the editor's cursor to the last line of the it's content.
   */
  @Override
  public void goToEnd() {
    final var line = getText().getLineCount() - 1;
    setSelection(line, 0);
  }

  /**
   * If any language server is set, asks the language server to find the definition of token at the
   * cursor position.
   *
   * <p>If the server returns a valid response, and the file specified in the response is same the
   * file in this editor, the range specified in the response will be selected.
   */
  @Override
  public void findDefinition() {
    if (getFile() == null) {
      return;
    }

    final var cancelChecker = new ICancelChecker.Default();

    final var action = (Consumer<Flashbar>) flashbar -> {
      final var params = new DefinitionParams(getFile().toPath(),
          getCursorLSPPosition());

      final CompletableFuture<DefinitionResult> future = CompletableFuture.supplyAsync(
          () -> languageServer.findDefinition(params, cancelChecker));

      future.whenComplete(
          (result, error) -> onFindDefinitionResult(flashbar, future, result, error));
    };

    FlashbarActivityUtilsKt.flashProgress(((Activity) getContext()),
        builder -> configureFlashbar(builder, string.msg_finding_definition, cancelChecker),
        action);
  }

  private void onFindDefinitionResult(Flashbar flashbar, CompletableFuture<DefinitionResult> future,
      DefinitionResult result, Throwable error) {
    LOG.debug("onFindDefinitionsResult");
    dismissOnUiThread(flashbar);

    if (result == null || languageClient == null || future.isCancelled() ||
        future.isCompletedExceptionally()) {
      LOG.error("An error occurred while finding definition", error);
      notify(string.msg_no_definition);
      return;
    }

    final var locations = result.getLocations();
    if (locations.size() == 0) {
      LOG.error("No definitions found", "Size:", locations.size());
      notify(string.msg_no_definition);
      return;
    }

    ThreadUtils.runOnUiThread(() -> {
      if (locations.size() == 1) {
        var location = locations.get(0);
        if (DocumentUtils.isSameFile(location.getFile(), getFile().toPath())) {
          setSelection(location.getRange());
          return;
        }
        languageClient.showDocument(
            new ShowDocumentParams(location.getFile(), location.getRange()));
      } else {
        languageClient.showLocations(locations);
      }
    });
  }

  /**
   * Dismisses the given flashbar on the UI thread.
   *
   * @param flashbar The flashbar to dismiss.
   */
  private void dismissOnUiThread(@NonNull final Flashbar flashbar) {
    post(flashbar::dismiss);
  }

  /**
   * If any language server instance is set, finds the references to of the token at the current
   * cursor position.
   *
   * <p>If the server returns a valid response, that response is forwarded to the {@link
   * ILanguageClient}.
   */
  @Override
  public void findReferences() {
    if (getFile() == null) {
      return;
    }

    final var cancelChecker = new ICancelChecker.Default();

    final var action = (Consumer<Flashbar>) flashbar -> {
      final var referenceParams = new ReferenceParams(getFile().toPath(),
          getCursorLSPPosition(), true);

      final var future = CompletableFuture.supplyAsync(
          () -> languageServer.findReferences(referenceParams, cancelChecker));

      future.whenComplete(
          (result, error) -> onFindReferencesResult(flashbar, future, result, error));
    };

    FlashbarActivityUtilsKt.flashProgress(((Activity) getContext()),
        builder -> configureFlashbar(builder, R.string.msg_finding_references, cancelChecker),
        action);
  }

  private void onFindReferencesResult(final Flashbar flashbar,
      final CompletableFuture<ReferenceResult> future,
      final ReferenceResult result, final Throwable error
  ) {
    LOG.debug("onFindRefsResult");
    dismissOnUiThread(flashbar);

    if (result == null || languageClient == null || future.isCancelled() ||
        future.isCompletedExceptionally()) {
      LOG.error("An error occurred while finding references", error);
      notify(string.msg_no_references);
      return;
    }

    if (result.getLocations().isEmpty()) {
      notify(string.msg_no_references);
      return;
    }

    if (result.getLocations().size() == 1) {
      final var loc = result.getLocations().get(0);
      if (DocumentUtils.isSameFile(loc.getFile(), getFile().toPath())) {
        setSelection(loc.getRange());
        return;
      }
    }

    ThreadUtils.runOnUiThread(() -> languageClient.showLocations(result.getLocations()));
  }

  /**
   * Notify the user that no results were found for a task.
   *
   */
  private void notify(@StringRes final int message) {
    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(() -> FlashbarUtilsKt.flashError(message));
  }

  private void configureFlashbar(Flashbar.Builder builder, @StringRes int message,
      ICancelChecker cancelChecker) {
    builder.message(message)
        .negativeActionText(android.R.string.cancel)
        .negativeActionTapListener(bar -> {
          cancelChecker.cancel();
          bar.dismiss();
        });
  }

  /**
   * Requests the language server to provided a semantically larger selection than the current
   * selection. If a valid response is received, that range will be selected.
   */
  @Override
  public void expandSelection() {
    if (languageServer == null || getFile() == null) {
      LOG.error("Cannot Expand selection. Language server or file is null");
      return;
    }

    //noinspection deprecation
    final var pd = ProgressDialog.show(getContext(), null,
        getContext().getString(string.please_wait), true, false);
    final CompletableFuture<Range> future = CompletableFuture.supplyAsync(
        () -> languageServer.expandSelection(
            new ExpandSelectionParams(getFile().toPath(), getCursorLSPRange())));

    future.whenComplete(((range, throwable) -> {
      pd.dismiss();

      if (throwable != null) {
        LOG.error("Error computing expanded selection range", throwable);
        return;
      }

      //noinspection ConstantConditions
      ThreadUtils.runOnUiThread(() -> setSelection(range));
    }));
  }

  @Override
  protected void onFocusChanged(final boolean gainFocus, final int direction,
      @Nullable final Rect previouslyFocusedRect
  ) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    if (!gainFocus) {
      ensureWindowsDismissed();
    }
  }

  /**
   * Ensures that all the windows are dismissed.
   */
  @Override
  public void ensureWindowsDismissed() {
    if (getDiagnosticWindow().isShowing()) {
      getDiagnosticWindow().dismiss();
    }

    if (getSignatureHelpWindow().isShowing()) {
      getSignatureHelpWindow().dismiss();
    }

    if (actionsMenu != null && actionsMenu.isShowing()) {
      actionsMenu.dismiss();
    }
  }

  /**
   * Notify the language server that the content of this file has been changed.
   *
   * @param event The content change event.
   */
  private void handleContentChange(ContentChangeEvent event, Unsubscribe unsubscribe) {
    isModified = true;
    if (getFile() == null) {
      return;
    }

    CompletableFuture.runAsync(() -> {
      dispatchDocumentChangeEvent(event);
      checkForSignatureHelp(event);
    });
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
    if (languageServer == null) {
      return;
    }
    final var changeLength = event.getChangedText().length();
    if (event.getAction() != ContentChangeEvent.ACTION_INSERT || changeLength > 0 &&
        changeLength <= 2) { // changeLength will be 2 as '(' and ')' are inserted at the same time
      return;
    }

    final var ch = event.getChangedText().charAt(0);
    if (ch == '(' || ch == ',') {
      signatureHelp();
    }
  }

  protected void dispatchDocumentChangeEvent(final ContentChangeEvent event) {
    if (getFile() == null) {
      return;
    }

    final var file = getFile().toPath();
    var type = ChangeType.INSERT;
    if (event.getAction() == ContentChangeEvent.ACTION_DELETE) {
      type = ChangeType.DELETE;
    } else if (event.getAction() == ContentChangeEvent.ACTION_SET_NEW_TEXT) {
      type = ChangeType.NEW_TEXT;
    }

    var changeDelta = type == ChangeType.NEW_TEXT ? 0 : event.getChangedText().length();
    if (type == ChangeType.DELETE) {
      changeDelta = -changeDelta;
    }

    final var start = event.getChangeStart();
    final var end = event.getChangeEnd();
    final var changeRange = new Range(new Position(start.line, start.column, start.index),
        new Position(end.line, end.column, end.index));

    final var changedText = event.getChangedText().toString();
    final var changeEvent = new DocumentChangeEvent(file, changedText, getText().toString(),
        ++fileVersion, type, changeDelta, changeRange);

    // Notify FileManager first
    FileManager.INSTANCE.onDocumentContentChange(changeEvent);
    EventBus.getDefault().post(changeEvent);
  }

  public static int createInputFlags() {
    var flags = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE |
        EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    if (getVisiblePasswordFlag()) {
      flags |= EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    }

    return flags;
  }

  public void markUnmodified() {
    this.isModified = false;
  }

  public void markModified() {
    this.isModified = true;
  }

  public void analyze() {
    if (languageServer != null && getFile() != null && getEditorLanguage() instanceof IDELanguage) {
      CompletableFuture.supplyAsync(() -> languageServer.analyze(getFile().toPath()))
          .whenComplete((diagnostics, throwable) -> {
            if (languageClient != null) {
              languageClient.publishDiagnostics(diagnostics);
            }
          });
    }
  }

  /**
   * Notify the language server that the file in this editor is about to be closed.
   */
  public void notifyClose() {
    if (getFile() == null) {
      LOG.info("No language server is available for this file");
      return;
    }

    dispatchDocumentCloseEvent();

    if (actionsMenu != null) {
      actionsMenu.unsubscribeEvents();
    }

    if (selectionChangeRunner != null) {
      this.selectionChangeHandler.removeCallbacks(selectionChangeRunner);
      this.selectionChangeRunner = null;
    }

    ensureWindowsDismissed();
  }

  @Override
  public void release() {
    ensureWindowsDismissed();
    super.release();
    ISnippetVariableResolver resolver = getSnippetController().getFileVariableResolver();
    if (resolver instanceof AbstractSnippetVariableResolver) {
      ((AbstractSnippetVariableResolver) resolver).close();
    }

    resolver = getSnippetController().getWorkspaceVariableResolver();
    if (resolver instanceof AbstractSnippetVariableResolver) {
      ((AbstractSnippetVariableResolver) resolver).close();
    }

    getSnippetController().setFileVariableResolver(null);
    getSnippetController().setWorkspaceVariableResolver(null);

    if (this.actionsMenu != null) {
      this.actionsMenu.destroy();
    }
    this.actionsMenu = null;
    this.file = null;
    this.languageServer = null;
    this.languageClient = null;
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

  protected void dispatchDocumentCloseEvent() {
    if (getFile() == null) {
      return;
    }

    final var closeEvent = new DocumentCloseEvent(getFile().toPath(), getCursorLSPRange());

    // Notify FileManager first
    FileManager.INSTANCE.onDocumentClose(closeEvent);

    EventBus.getDefault().post(closeEvent);
  }

  public void onEditorSelected() {
    if (getFile() == null) {
      return;
    }

    dispatchDocumentSelectedEvent();
  }

  protected void dispatchDocumentSelectedEvent() {
    if (getFile() == null) {
      return;
    }

    final var selectedEvent = new DocumentSelectedEvent(getFile().toPath());
    EventBus.getDefault().post(selectedEvent);
  }

  @Override
  @SuppressWarnings("unused")
  public void executeCommand(@Nullable Command command) {
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

  @Override
  public IDEEditorSearcher getSearcher() {
    return searcher;
  }

  protected void setSearcher(@NonNull IDEEditorSearcher searcher) {
    this.searcher = searcher;
  }

  @Override
  public void beginSearchMode() {
    throw new UnsupportedOperationException(
        "Search ActionMode is not supported. Use CodeEditorView.beginSearch() instead.");
  }

  public void dispatchDocumentSaveEvent() {
    if (getFile() == null) {
      return;
    }

    isModified = false;
    final var saveEvent = new DocumentSaveEvent(getFile().toPath());
    EventBus.getDefault().post(saveEvent);
  }

  @SuppressWarnings("unused")
  @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
  public void onColorSchemeInvalidated(ColorSchemeInvalidatedEvent event) {
    final var file = getFile();
    if (file == null) {
      return;
    }

    setupLanguage(file);
  }

  public void setupLanguage(File file) {
    if (file == null) {
      return;
    }

    final var language = createLanguage(file);
    final var extension = FilesKt.getExtension(file);
    if (language instanceof TreeSitterLanguage) {
      IDEColorSchemeProvider.INSTANCE.readScheme(getContext(), scheme -> {
        applyTreeSitterLang(language, extension, scheme);
      });
    } else {
      setEditorLanguage(language);
    }
  }

  public void applyTreeSitterLang(final TreeSitterLanguage language, final String type,
      @Nullable SchemeAndroidIDE scheme
  ) {
    applyTreeSitterLang(((Language) language), type, scheme);
  }

  private void applyTreeSitterLang(final Language language, final String type,
      @Nullable SchemeAndroidIDE scheme
  ) {
    if (scheme == null) {
      LOG.error("Failed to read current color scheme");
      scheme = SchemeAndroidIDE.newInstance(getContext());
    }

    if (scheme instanceof IDEColorScheme &&
        ((IDEColorScheme) scheme).getLanguageScheme(type) == null) {
      LOG.warn("Color scheme does not support file type '" + type + "'");
      scheme = SchemeAndroidIDE.newInstance(getContext());
    }

    if (scheme instanceof DynamicColorScheme) {
      ((DynamicColorScheme) scheme).apply(getContext());
    }

    setColorScheme(scheme);
    setEditorLanguage(language);
  }

  private Language createLanguage(File file) {
    if (!file.isFile()) {
      return new EmptyLanguage();
    }

    final var tsLang = TreeSitterLanguageProvider.INSTANCE.forFile(file, getContext());
    if (tsLang != null) {
      return tsLang;
    }

    String ext = FileUtils.getFileExtension(file);
    switch (ext) {
      case "gradle":
        return new GroovyLanguage();
      case "c":
      case "h":
      case "cc":
      case "cpp":
      case "cxx":
        return new CppLanguage();
      default:
        return new EmptyLanguage();
    }
  }
}
