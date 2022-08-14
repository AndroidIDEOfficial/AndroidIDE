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

import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getVisiblePasswordFlag;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.CompletionListAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.eventbus.events.editor.ChangeType;
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentSaveEvent;
import com.itsaky.androidide.eventbus.events.editor.DocumentSelectedEvent;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.lsp.api.ILanguageServer;
import com.itsaky.androidide.lsp.models.Command;
import com.itsaky.androidide.lsp.models.DefinitionResult;
import com.itsaky.androidide.lsp.models.ExpandSelectionParams;
import com.itsaky.androidide.lsp.models.ReferenceParams;
import com.itsaky.androidide.lsp.models.ReferenceResult;
import com.itsaky.androidide.lsp.models.ShowDocumentParams;
import com.itsaky.androidide.lsp.models.SignatureHelp;
import com.itsaky.androidide.lsp.models.SignatureHelpParams;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.DocumentUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.toaster.Toaster;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.SelectionChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import io.github.rosemoe.sora.widget.CodeEditor;
import io.github.rosemoe.sora.widget.IDEEditorSearcher;
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion;
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow;

public class IDEEditor extends CodeEditor {

  public static final String KEY_FILE = "editor_file";
  private static final ILogger LOG = ILogger.newInstance("IDEEditor");
  private final EditorActionsMenu actionsMenu;
  private IDEEditorSearcher searcher;
  private int fileVersion;
  private File file;
  private ILanguageServer languageServer;
  private SignatureHelpWindow signatureHelpWindow;
  private DiagnosticWindow diagnosticWindow;
  IDELanguageClientImpl languageClient;

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

    actionsMenu = new EditorActionsMenu(this);
    actionsMenu.init();

    final var window = new EditorCompletionWindow(this);
    window.setAdapter(new CompletionListAdapter());
    replaceComponent(EditorAutoCompletion.class, window);

    setColorScheme(new SchemeAndroidIDE());
    setSearcher(new IDEEditorSearcher(this));
    getComponent(EditorTextActionWindow.class).setEnabled(false);
    subscribeEvent(SelectionChangeEvent.class, this::handleSelectionChange);
    subscribeEvent(ContentChangeEvent.class, this::handleContentChange);

    setInputType(createInputFlags());
  }

  private void handleSelectionChange(@NonNull SelectionChangeEvent event, Unsubscribe unsubscribe) {
    if (event.isSelected() || languageClient == null) {
      // do not show diagnostics when text is selected
      // or if we cannot get diagnostics
      return;
    }

    final var line = event.getLeft().line;
    final var column = event.getLeft().column;

    // diagnostics are expected to be sorted, so, we do a binary search
    getDiagnosticWindow().showDiagnostic(languageClient.getDiagnosticAt(getFile(), line, column));
  }

  private DiagnosticWindow getDiagnosticWindow() {
    if (diagnosticWindow == null) {
      diagnosticWindow = new DiagnosticWindow(this);
    }

    return diagnosticWindow;
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

    if (file != null) {
      dispatchDocumentOpenEvent();
    }

    if (file != null) {
      getExtraArguments().putString(KEY_FILE, file.getAbsolutePath());
    } else {
      getExtraArguments().remove(KEY_FILE);
    }
  }

  protected void dispatchDocumentOpenEvent() {
    if (getFile() == null) {
      return;
    }

    final var openEvent =
        new DocumentOpenEvent(getFile().toPath(), getText().toString(), fileVersion = 0);
    EventBus.getDefault().post(openEvent);
  }

  /**
   * Notify the language server that the content of this file has been changed.
   *
   * @param event The content change event.
   */
  private void handleContentChange(ContentChangeEvent event, Unsubscribe unsubscribe) {
    if (getFile() == null || languageServer == null) {
      return;
    }

    CompletableFuture.runAsync(
        () -> {
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
    if (event.getAction() != ContentChangeEvent.ACTION_INSERT
        || event.getChangedText().length() != 1) {
      return;
    }

    final var ch = event.getChangedText().charAt(0);
    if (ch == '(' || ch == ',') {
      signatureHelp();
    }
  }

  /**
   * If any language server is set, requests signature help at the cursor's position. On a valid
   * response, shows the signature help in a popup window.
   */
  public void signatureHelp() {
    if (languageServer != null && getFile() != null) {
      final CompletableFuture<SignatureHelp> future =
          CompletableFuture.supplyAsync(
              () ->
                  languageServer.signatureHelp(
                      new SignatureHelpParams(
                          getFile().toPath(),
                          new Position(getCursor().getLeftLine(), getCursor().getLeftColumn()))));

      future.whenComplete(
          (help, error) -> {
            if (help == null
                || languageClient == null
                || future.isCancelled()
                || future.isCompletedExceptionally()) {
              LOG.error("An error occurred while finding signature help", error);
              return;
            }

            //noinspection ConstantConditions
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

  private SignatureHelpWindow getSignatureHelpWindow() {
    if (signatureHelpWindow == null) {
      signatureHelpWindow = new SignatureHelpWindow(this);
    }

    return signatureHelpWindow;
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
    final var changeRange =
        new Range(
            new Position(start.line, start.column, start.index),
            new Position(end.line, end.column, end.index));

    final var changeEvent =
        new DocumentChangeEvent(
            file, getText().toString(), fileVersion + 1, type, changeDelta, changeRange);
    EventBus.getDefault().post(changeEvent);
  }

  public static int createInputFlags() {
    var flags =
        EditorInfo.TYPE_CLASS_TEXT
            | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
            | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    if (getVisiblePasswordFlag()) {
      flags |= EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
    }

    return flags;
  }

  public void analyze() {
    if (languageServer != null && getFile() != null && getEditorLanguage() instanceof IDELanguage) {
      CompletableFuture.supplyAsync(() -> languageServer.analyze(getFile().toPath()))
          .whenComplete(
              (diagnostics, throwable) -> {
                if (languageClient != null) {
                  languageClient.publishDiagnostics(diagnostics);
                }
              });
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
   * Set the language server that this editor will connect with. If the language client is not set,
   * it'll be set to {@link IDELanguageClientImpl}.
   *
   * @param server The server to set. Provide <code>null</code> to disable all the language server
   *     features.
   */
  public void setLanguageServer(ILanguageServer server) {
    this.languageServer = server;

    if (languageClient == null && IDELanguageClientImpl.isInitialized()) {
      languageClient = IDELanguageClientImpl.getInstance();
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

  /**
   * If any language server is set, asks the language server to find the definition of token at the
   * cursor position.
   *
   * <p>If the server returns a valid response, and the file specified in the response is same the
   * file in this editor, the range specified in the response will be selected.
   */
  @SuppressWarnings({"deprecation", "unused"})
  public void findDefinition() {
    if (getFile() == null) {
      return;
    }

    final ProgressDialog pd =
        ProgressDialog.show(
            getContext(), null, getContext().getString(R.string.msg_finding_definition));

    try {
      final CompletableFuture<DefinitionResult> future =
          CompletableFuture.supplyAsync(
              () -> {
                final var params =
                    new com.itsaky.androidide.lsp.models.DefinitionParams(
                        getFile().toPath(),
                        new com.itsaky.androidide.models.Position(
                            getCursor().getLeftLine(), getCursor().getLeftColumn()));

                return languageServer.findDefinition(params);
              });

      future.whenComplete(
          (result, error) -> {
            if (result == null
                || languageClient == null
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

            //noinspection ConstantConditions
            ThreadUtils.runOnUiThread(
                () -> {
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
    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(
        () -> {
          StudioApp.getInstance().toast(R.string.msg_no_definition, Toaster.Type.ERROR);
          pd.dismiss();
        });
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
   * Dismisses the given dialog on the UI thread.
   *
   * @param dialog The dialog to dismiss.
   */
  private void dismissOnUiThread(@NonNull final Dialog dialog) {
    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(dialog::dismiss);
  }

  /**
   * If any language server instance is set, finds the references to of the token at the current
   * cursor position.
   *
   * <p>If the server returns a valid response, that response is forwarded to the {@link
   * IDELanguageClientImpl}.
   */
  @SuppressWarnings("unused")
  public void findReferences() {
    if (getFile() == null) {
      return;
    }

    @SuppressWarnings("deprecation")
    final ProgressDialog pd =
        ProgressDialog.show(
            getContext(), null, getContext().getString(R.string.msg_finding_references));

    try {
      final var future =
          CompletableFuture.supplyAsync(
              () -> {
                final var referenceParams =
                    new ReferenceParams(
                        getFile().toPath(),
                        new com.itsaky.androidide.models.Position(
                            getCursor().getLeftLine(), getCursor().getLeftColumn()),
                        true);
                return languageServer.findReferences(referenceParams);
              });

      future.whenComplete((result, error) -> onFindReferencesResult(pd, future, result, error));
    } catch (Throwable th) {
      LOG.error("An error occurred while finding references", th);
      showReferencesNotFound(pd);
    }
  }

  @SuppressWarnings("deprecation")
  private void onFindReferencesResult(
      final ProgressDialog pd,
      final CompletableFuture<ReferenceResult> future,
      final ReferenceResult result,
      final Throwable error) {
    if (result == null
        || languageClient == null
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
        if (DocumentUtils.isSameFile(loc.getFile(), getFile().toPath())) {
          setSelection(loc.getRange());
          return;
        }
      }

      //noinspection ConstantConditions
      ThreadUtils.runOnUiThread(() -> languageClient.showLocations(result.getLocations()));
    }

    dismissOnUiThread(pd);
  }

  /**
   * Notify the user that no references were found for the selected token.
   *
   * @param pd The {@link ProgressDialog} that was shown when requesting references.
   */
  @SuppressWarnings("deprecation")
  private void showReferencesNotFound(final ProgressDialog pd) {
    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(
        () -> {
          StudioApp.getInstance().toast(R.string.msg_no_references, Toaster.Type.ERROR);
          pd.dismiss();
        });
  }

  /** Notify the language server that the file in this editor is about to be closed. */
  public void close() {
    if (getFile() == null) {
      LOG.info("No language server is available for this file");
      return;
    }

    dispatchDocumentCloseEvent();

    actionsMenu.unsubscribeEvents();
    ensureWindowsDismissed();
  }

  /** Ensures that all the windows are dismissed. */
  public void ensureWindowsDismissed() {
    if (getDiagnosticWindow().isShowing()) {
      getDiagnosticWindow().dismiss();
    }

    if (getSignatureHelpWindow().isShowing()) {
      getSignatureHelpWindow().dismiss();
    }

    if (actionsMenu != null) {
      if (actionsMenu.isShowing()) {
        actionsMenu.dismiss();
      }
    }
  }

  protected void dispatchDocumentCloseEvent() {
    if (getFile() == null) {
      return;
    }

    final var closeEvent = new DocumentCloseEvent(getFile().toPath());
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
   * Requests the language server to provided a semantically larger selection than the current
   * selection. If a valid response is received, that range will be selected.
   */
  public void expandSelection() {
    if (languageServer == null || getFile() == null) {
      LOG.error("Cannot expand selection. Language server or file is null");
      return;
    }

    //noinspection deprecation
    final var pd =
        ProgressDialog.show(
            getContext(), null, getContext().getString(R.string.please_wait), true, false);
    final CompletableFuture<Range> future =
        CompletableFuture.supplyAsync(
            () ->
                languageServer.expandSelection(
                    new ExpandSelectionParams(getFile().toPath(), getCursorRange())));

    future.whenComplete(
        ((range, throwable) -> {
          pd.dismiss();

          if (throwable != null) {
            LOG.error("Error computing expanded selection range", throwable);
            return;
          }

          //noinspection ConstantConditions
          ThreadUtils.runOnUiThread(() -> setSelection(range));
        }));
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
   * Get the cursor's position in the form of {@link Position}.
   *
   * @return The {@link Position} of the cursor.
   */
  @SuppressWarnings("unused")
  public Position getCursorAsLSPPosition() {
    return new Position(getCursor().getLeftLine(), getCursor().getLeftColumn());
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

  @Override
  public IDEEditorSearcher getSearcher() {
    return searcher;
  }

  protected void setSearcher(@NonNull IDEEditorSearcher searcher) {
    this.searcher = searcher;
  }

  @Override
  public void beginSearchMode() {
    final var callback = new SearchActionMode(this);
    if (getContext() instanceof AppCompatActivity) {
      startActionMode(callback);
    } else {
      LOG.error("Unable start search action mode. Activity must inherit AppCompatActivity.");
    }
  }

  protected void dispatchDocumentSaveEvent() {
    if (getFile() == null) {
      return;
    }

    final var saveEvent = new DocumentSaveEvent(getFile().toPath());
    EventBus.getDefault().post(saveEvent);
  }
}
