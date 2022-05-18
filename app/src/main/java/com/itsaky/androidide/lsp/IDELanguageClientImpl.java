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

import android.view.View;

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
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.views.editor.CodeEditorView;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.lsp.api.ILanguageClient;
import com.itsaky.lsp.models.CodeActionItem;
import com.itsaky.lsp.models.Command;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.DiagnosticResult;
import com.itsaky.lsp.models.Location;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;
import com.itsaky.toaster.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.rosemoe.sora.text.Content;

/** AndroidIDE specific implementation of the LanguageClient */
public class IDELanguageClientImpl implements ILanguageClient {

  protected static final Gson gson = new Gson();
  protected static final ILogger LOG = ILogger.newInstance("AbstractLanguageClient");
  private static IDELanguageClientImpl mInstance;
  private final Map<File, List<DiagnosticItem>> diagnostics = new HashMap<>();
  protected EditorActivityProvider activityProvider;

  private IDELanguageClientImpl(EditorActivityProvider provider) {
    setActivityProvider(provider);
  }

  public static IDELanguageClientImpl getInstance() {
    if (mInstance == null) {
      throw new IllegalStateException("Client not initialized");
    }

    return mInstance;
  }

  public static IDELanguageClientImpl initialize(EditorActivityProvider provider) {
    if (mInstance != null) {
      throw new IllegalStateException("Client is already initialized");
    }

    mInstance = new IDELanguageClientImpl(provider);

    return getInstance();
  }

  public static void shutdown() {
    mInstance = null;
  }

  public static boolean isInitialized() {
    return mInstance != null;
  }

  public void setActivityProvider(EditorActivityProvider provider) {
    this.activityProvider = provider;
  }

  protected EditorActivity activity() {
    if (activityProvider == null) return null;
    return activityProvider.provide();
  }

  private CodeEditorView findEditorByFile(File file) {
    return activity().getEditorForFile(file);
  }

  public void hideDiagnostics() {
    if (activity() == null || activity().getDiagnosticBinding() == null) {
      return;
    }

    activity().getDiagnosticBinding().getRoot().setVisibility(View.GONE);
  }

  private void hideBottomDiagnosticView(final File file) {
    if (activity() == null || file == null) {
      return;
    }

    final var frag = findEditorByFile(file);
    if (frag == null || frag.getBinding() == null) {
      return;
    }

    frag.getBinding().diagnosticTextContainer.setVisibility(View.GONE);
    frag.getBinding().diagnosticText.setVisibility(View.GONE);
    frag.getBinding().diagnosticText.setClickable(false);
  }

  @Override
  public void publishDiagnostics(DiagnosticResult result) {
    boolean error = result == null;
    activity().handleDiagnosticsResultVisibility(error || result.getDiagnostics().isEmpty());

    if (error) {
      return;
    }

    File file = result.getFile().toFile();
    if (!file.exists() || !file.isFile()) return;

    final var editorView = activity().getEditorForFile(file);
    if (editorView != null) {
      final var editor = editorView.getEditor();
      if (editor != null) {
        final var editorLanguage = editor.getEditorLanguage();
        if (editorLanguage instanceof IDELanguage) {
          ((IDELanguage) editorLanguage).setDiagnostics(result.getDiagnostics());
        }
      }
    }

    diagnostics.put(file, result.getDiagnostics());
    activity().setDiagnosticsAdapter(newDiagnosticsAdapter());
  }

  /** Called by {@link IDEEditor IDEEditor} to show locations in EditorActivity */
  public void showLocations(List<Location> locations) {

    // Cannot show anything if the activity() is null
    if (activity() == null) {
      return;
    }

    boolean error = locations == null || locations.isEmpty();
    activity().handleSearchResultVisibility(error);

    if (error) {
      activity().setSearchResultAdapter(new SearchListAdapter(null, null, null));
      return;
    }

    final Map<File, List<SearchResult>> results = new HashMap<>();
    for (int i = 0; i < locations.size(); i++) {
      try {
        final Location loc = locations.get(i);
        if (loc == null) {
          continue;
        }

        final File file = loc.getFile().toFile();
        if (!file.exists() || !file.isFile()) continue;
        var frag = findEditorByFile(file);
        Content content;
        if (frag != null && frag.getEditor() != null) content = frag.getEditor().getText();
        else content = new Content(FileIOUtils.readFile2String(file));
        final List<SearchResult> matches =
            results.containsKey(file) ? results.get(file) : new ArrayList<>();
        Objects.requireNonNull(matches)
            .add(
                new SearchResult(
                    loc.getRange(),
                    file,
                    content.getLineString(loc.getRange().getStart().getLine()),
                    content
                        .subContent(
                            loc.getRange().getStart().getLine(),
                            loc.getRange().getStart().getColumn(),
                            loc.getRange().getEnd().getLine(),
                            loc.getRange().getEnd().getColumn())
                        .toString()));
        results.put(file, matches);
      } catch (Throwable th) {
        LOG.error("Failed to show file location", th);
      }
    }

    activity().handleSearchResults(results);
  }

  @Override
  public void performCodeAction(File file, CodeActionItem actionItem) {
    final var editor = activity().getEditorForFile(file);
    if (editor != null) {
      performCodeAction(editor.getEditor(), actionItem);
    }
  }

  /**
   * Perform the given {@link CodeActionItem}
   *
   * @param editor The {@link IDEEditor} that invoked the code action request. This is required to
   *     reduce the time finding the code action from the edits.
   * @param action The action to perform
   */
  public void performCodeAction(IDEEditor editor, CodeActionItem action) {
    if (activity() == null || editor == null || action == null) {
      LOG.error(
          "Unable to perform code action",
          "activity=" + activity(),
          "editor=" + editor,
          "action=" + action);
      StudioApp.getInstance().toast(R.string.msg_cannot_perform_fix, Toaster.Type.ERROR);
      return;
    }

    final ProgressSheet progress = new ProgressSheet();
    progress.setSubMessageEnabled(false);
    progress.setWelcomeTextEnabled(false);
    progress.setCancelable(false);
    progress.setMessage(activity().getString(R.string.msg_performing_actions));
    progress.show(activity().getSupportFragmentManager(), "quick_fix_progress");

    new TaskExecutor()
        .executeAsyncProvideError(
            () -> performCodeActionAsync(editor, action),
            (result, throwable) -> {
              ThreadUtils.runOnUiThread(progress::dismiss);
              if (result == null || throwable != null || !result) {
                LOG.error(
                    "Unable to perform code action", "result=" + result, "throwable=" + throwable);
                StudioApp.getInstance().toast(R.string.msg_cannot_perform_fix, Toaster.Type.ERROR);
              } else {
                editor.executeCommand(action.getCommand());
              }
            });
  }

  private List<DiagnosticGroup> mapAsGroup(Map<File, List<DiagnosticItem>> diags) {
    List<DiagnosticGroup> groups = new ArrayList<>();
    if (diags == null || diags.size() <= 0) return groups;
    for (File file : diags.keySet()) {
      var fileDiags = diags.get(file);
      if (fileDiags == null || fileDiags.size() <= 0) continue;
      DiagnosticGroup group = new DiagnosticGroup(R.drawable.ic_language_java, file, fileDiags);
      groups.add(group);
    }
    return groups;
  }

  private void showAvailableQuickfixes(IDEEditor editor, List<CodeActionItem> actions) {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(activity());
    builder.setTitle(R.string.msg_code_actions);
    builder.setItems(
        asArray(actions),
        (d, w) -> {
          d.dismiss();
          hideDiagnostics();
          hideBottomDiagnosticView(editor.getFile());
          performCodeAction(editor, actions.get(w));
        });
    builder.show();
  }

  private CharSequence[] asArray(List<CodeActionItem> actions) {
    final String[] arr = new String[actions.size()];
    for (int i = 0; i < actions.size(); i++) {
      arr[i] = actions.get(i).getTitle();
    }
    return arr;
  }

  private Boolean performCodeActionAsync(final IDEEditor editor, final CodeActionItem action) {
    LOG.debug("Performing code action:", action);
    final var changes = action.getChanges();
    if (changes.isEmpty()) {
      return Boolean.FALSE;
    }

    for (var change : changes) {
      final var path = change.getFile();
      if (path == null) {
        continue;
      }

      final File file = path.toFile();
      if (!file.exists()) {
        continue;
      }

      for (TextEdit edit : change.getEdits()) {
        final String editorFilepath =
            editor.getFile() == null ? "" : editor.getFile().getAbsolutePath();
        if (file.getAbsolutePath().equals(editorFilepath)) {
          // Edit is in the same editor which requested the code action
          editInEditor(editor, edit);
        } else {
          var openedFrag = findEditorByFile(file);

          if (openedFrag != null && openedFrag.getEditor() != null) {
            // Edit is in another 'opened' file
            editInEditor(openedFrag.getEditor(), edit);
          } else {
            // Edit is in some other file which is not opened
            // We should open that file and perform the edit
            openedFrag = activity().openFile(file);
            if (openedFrag != null && openedFrag.getEditor() != null) {
              editInEditor(openedFrag.getEditor(), edit);
            }
          }
        }
      }
    }

    return Boolean.TRUE;
  }

  private void execCommand(IDEEditor editor, Command command) {
    editor.executeCommand(command);
  }

  private void editInEditor(final IDEEditor editor, final TextEdit edit) {
    final Range range = edit.getRange();
    final int startLine = range.getStart().getLine();
    final int startCol = range.getStart().getColumn();
    final int endLine = range.getEnd().getLine();
    final int endCol = range.getEnd().getColumn();

    activity()
        .runOnUiThread(
            () -> {
              if (startLine == endLine && startCol == endCol) {
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
  public com.itsaky.lsp.models.ShowDocumentResult showDocument(
      com.itsaky.lsp.models.ShowDocumentParams params) {
    boolean success = false;
    final var result = new com.itsaky.lsp.models.ShowDocumentResult(false);
    if (activity() == null) {
      return result;
    }

    if (params != null) {
      File file = params.getFile().toFile();
      if (file.exists() && file.isFile() && FileUtils.isUtf8(file)) {
        final var range = params.getSelection();
        var frag =
            activity().getEditorAtIndex(activity().getBinding().tabs.getSelectedTabPosition());
        if (frag != null
            && frag.getFile() != null
            && frag.getEditor() != null
            && frag.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
          if (LSPUtils.isEqual(range.getStart(), range.getEnd())) {
            frag.getEditor().setSelection(range.getStart().getLine(), range.getStart().getColumn());
          } else {
            frag.getEditor().setSelection(range);
          }
        } else {
          activity().openFileAndSelect(file, range);
        }
        success = true;
      }
    }

    result.setSuccess(success);
    return result;
  }
}
