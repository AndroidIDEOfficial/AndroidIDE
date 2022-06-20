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
package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.actions.ActionsRegistry;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.EditorBottomSheetTabAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.ActivityEditorBinding;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding;
import com.itsaky.androidide.fragments.FileTreeFragment;
import com.itsaky.androidide.fragments.LogViewFragment;
import com.itsaky.androidide.fragments.NonEditableEditorFragment;
import com.itsaky.androidide.fragments.SearchResultFragment;
import com.itsaky.androidide.fragments.SimpleOutputFragment;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.handlers.EditorEventListener;
import com.itsaky.androidide.handlers.FileOptionsHandler;
import com.itsaky.androidide.handlers.IDEHandler;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.models.ApkMetadata;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.models.SaveResult;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.models.SheetOption;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.services.GradleBuildService;
import com.itsaky.androidide.services.LogReceiver;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData;
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData;
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult;
import com.itsaky.androidide.utils.CharSequenceInputStream;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.EditorActivityActions;
import com.itsaky.androidide.utils.EditorBottomSheetBehavior;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.RecursiveFileSearcher;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.viewmodel.EditorViewModel;
import com.itsaky.androidide.views.MaterialBanner;
import com.itsaky.androidide.views.SymbolInputView;
import com.itsaky.androidide.views.editor.CodeEditorView;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.inflater.values.ValuesTableFactory;
import com.itsaky.lsp.java.models.JavaServerSettings;
import com.itsaky.lsp.models.DiagnosticItem;
import com.itsaky.lsp.models.InitializeParams;
import com.itsaky.lsp.models.Range;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class EditorActivity extends StudioActivity
    implements FileTreeFragment.FileActionListener,
        TabLayout.OnTabSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        DiagnosticClickListener,
        IDEHandler.Provider,
        EditorActivityProvider,
        OptionsListFragment.OnOptionsClickListener {

  public static final String KEY_BOTTOM_SHEET_SHOWN = "editor_bottomSheetShown";
  private static final String TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment";
  private static final int ACTION_ID_CLOSE = 100;
  private static final int ACTION_ID_OTHERS = 101;
  private static final int ACTION_ID_ALL = 102;
  private static final ILogger LOG = ILogger.newInstance("EditorActivity");
  private final EditorEventListener mBuildEventListener = new EditorEventListener();
  private ActivityEditorBinding mBinding;
  private LayoutDiagnosticInfoBinding mDiagnosticInfoBinding;
  private EditorBottomSheetTabAdapter bottomSheetTabAdapter;
  private final LogReceiver mLogReceiver = new LogReceiver().setLogListener(this::appendApkLog);
  private FileTreeFragment mFileTreeFragment;
  private TreeNode mLastHeld;
  private SymbolInputView symbolInput;
  private FileOptionsHandler mFileOptionsHandler;
  private QuickAction mTabCloseAction;
  private TextSheetFragment mDaemonStatusFragment;
  private OptionsListFragment mFileOptionsFragment;
  private ProgressSheet mSearchingProgress;
  private AlertDialog mFindInProjectDialog;
  private ActivityResultLauncher<Intent> mUIDesignerLauncher;
  private EditorBottomSheetBehavior<? extends View> mEditorBottomSheet;
  private EditorViewModel mViewModel;

  private GradleBuildService mBuildService;
  private final ServiceConnection mGradleServiceConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
          mBuildService = ((GradleBuildService.GradleServiceBinder) service).getService();
          LOG.info("Gradle build service has been started...");

          mBuildService
              .setEventListener(mBuildEventListener)
              .startToolingServer(() -> initializeProject());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          mBuildService = null;
          LOG.info("Disconnected from Gradle build service...");
        }
      };

  @Override
  public void onBackPressed() {
    if (mBinding.getRoot().isDrawerOpen(GravityCompat.END)) {
      mBinding.getRoot().closeDrawer(GravityCompat.END);
    } else if (mBinding.getRoot().isDrawerOpen(GravityCompat.START)) {
      mBinding.getRoot().closeDrawer(GravityCompat.START);
    } else if (getDaemonStatusFragment().isShowing()) {
      getDaemonStatusFragment().dismiss();
    } else if (mFileOptionsFragment != null && mFileOptionsFragment.isShowing()) {
      mFileOptionsFragment.dismiss();
    } else if (mEditorBottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    } else {
      confirmProjectClose();
    }
  }

  @SuppressLint("RestrictedApi")
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (menu instanceof MenuBuilder) {
      MenuBuilder builder = (MenuBuilder) menu;
      builder.setOptionalIconsVisible(true);
    }

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    ensureToolbarMenu(menu);
    return true;
  }

  public void ensureToolbarMenu(@NonNull Menu menu) {
    menu.clear();

    final var data = new ActionData();
    final var currentEditor = getCurrentEditor();
    data.put(Context.class, EditorActivity.this);
    data.put(CodeEditorView.class, currentEditor);

    if (currentEditor != null) {
      data.put(IDEEditor.class, currentEditor.getEditor());
      data.put(File.class, currentEditor.getEditor().getFile());
    }

    ActionsRegistry.getInstance()
        .fillMenu(data, com.itsaky.androidide.actions.ActionItem.Location.EDITOR_TOOLBAR, menu);
  }

  @Nullable
  public CodeEditorView getCurrentEditor() {
    if (mViewModel.getCurrentFileIndex() != -1) {
      return getEditorAtIndex(mViewModel.getCurrentFileIndex());
    }

    return null;
  }

  @Nullable
  public CodeEditorView getEditorAtIndex(final int index) {
    return (CodeEditorView) mBinding.editorContainer.getChildAt(index);
  }

  @Override
  public EditorActivity provide() {
    return this;
  }

  @Override
  public EditorActivity provideEditorActivity() {
    return this;
  }

  public void handleSearchResults(Map<File, List<SearchResult>> results) {
    setSearchResultAdapter(
        new com.itsaky.androidide.adapters.SearchListAdapter(
            results,
            file -> {
              openFile(file);
              hideViewOptions();
            },
            match -> {
              openFileAndSelect(match.file, match);
              hideViewOptions();
            }));

    showSearchResults();

    if (mSearchingProgress != null && mSearchingProgress.isShowing()) {
      mSearchingProgress.dismiss();
    }
  }

  public void appendApkLog(LogLine line) {
    final var logFragment = bottomSheetTabAdapter.getLogFragment();
    if (logFragment != null) {
      logFragment.appendLog(line);
    }
  }

  public void showDaemonStatus() {
    ShellServer shell = getApp().newShell(t -> getDaemonStatusFragment().append(t));
    shell.bgAppend(String.format("echo '%s'", getString(R.string.msg_getting_daemom_status)));
    shell.bgAppend(
        String.format(
            "cd '%s' && sh gradlew --status",
            Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDirPath())));
    if (!getDaemonStatusFragment().isShowing()) {
      getDaemonStatusFragment().show(getSupportFragmentManager(), "daemon_status");
    }
  }

  public TextSheetFragment getDaemonStatusFragment() {
    return mDaemonStatusFragment == null
        ? mDaemonStatusFragment =
            new TextSheetFragment()
                .setTextSelectable(true)
                .setTitleText(R.string.gradle_daemon_status)
        : mDaemonStatusFragment;
  }

  public void showSearchResults() {
    if (mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    final int index = bottomSheetTabAdapter.findIndexOfFragmentByClass(SearchResultFragment.class);
    if (index >= 0 && index < mBinding.bottomSheet.tabs.getTabCount()) {
      final var tab = mBinding.bottomSheet.tabs.getTabAt(index);
      if (tab != null) {
        tab.select();
      }
    }
  }

  public void handleDiagnosticsResultVisibility(boolean errorVisible) {
    runOnUiThread(
        () -> {
          final var diagnosticsFragment = bottomSheetTabAdapter.getDiagnosticsFragment();
          if (diagnosticsFragment != null) {
            diagnosticsFragment.handleResultVisibility(errorVisible);
          }
        });
  }

  public void handleSearchResultVisibility(boolean errorVisible) {
    runOnUiThread(
        () -> {
          final var searchResultFragment = bottomSheetTabAdapter.getSearchResultFragment();
          if (searchResultFragment != null) {
            searchResultFragment.handleResultVisibility(errorVisible);
          }
        });
  }

  public void showFirstBuildNotice() {
    DialogUtils.newMaterialDialogBuilder(this)
        .setPositiveButton(android.R.string.ok, null)
        .setTitle(R.string.title_first_build)
        .setMessage(R.string.msg_first_build)
        .setCancelable(false)
        .create()
        .show();
  }

  @Override
  public void onGroupClick(DiagnosticGroup group) {
    if (group != null
        && group.file != null
        && group.file.exists()
        && FileUtils.isUtf8(group.file)) {
      openFile(group.file);
      hideViewOptions();
    }
  }

  @Override
  public void onDiagnosticClick(File file, @NonNull DiagnosticItem diagnostic) {
    openFileAndSelect(file, diagnostic.getRange());
    hideViewOptions();
  }

  public void openFileAndSelect(File file, Range selection) {
    openFile(file, selection);
    final var opened = getEditorForFile(file);
    if (opened != null && opened.getEditor() != null) {
      final var editor = opened.getEditor();
      final var range = editor.validateRange(selection);
      editor.post(
          () -> {
            if (LSPUtils.isEqual(range.getStart(), range.getEnd())) {
              editor.setSelection(range.getStart().getLine(), range.getEnd().getColumn());
            } else {
              editor.setSelection(range);
            }
          });
    }
  }

  @Nullable
  public CodeEditorView getEditorForFile(@NonNull final File file) {
    for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
      final CodeEditorView editor = (CodeEditorView) mBinding.editorContainer.getChildAt(i);
      if (file.equals(editor.getFile())) {
        return editor;
      }
    }

    return null;
  }

  @Override
  public CodeEditorView openFile(File file) {
    return openFile(file, null);
  }

  public void hideViewOptions() {
    if (mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
  }

  public CodeEditorView openFile(File file, com.itsaky.lsp.models.Range selection) {
    if (selection == null) {
      selection = com.itsaky.lsp.models.Range.NONE;
    }

    int index = openFileAndGetIndex(file, selection);
    final var tab = mBinding.tabs.getTabAt(index);
    if (tab != null && index >= 0 && !tab.isSelected()) {
      tab.select();
    }

    if (mBinding.editorDrawerLayout.isDrawerOpen(GravityCompat.END)) {
      mBinding.editorDrawerLayout.closeDrawer(GravityCompat.END);
    }

    mBinding.editorContainer.setDisplayedChild(index);

    try {
      return getEditorAtIndex(index);
    } catch (Throwable th) {
      LOG.error("Unable to get editor fragment at opened file index", index);
      LOG.error(th);
      return null;
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  public int openFileAndGetIndex(File file, com.itsaky.lsp.models.Range selection) {
    final var openedFileIndex = findIndexOfEditorByFile(file);

    if (openedFileIndex != -1) {
      LOG.error("File is already opened. File: " + file);
      return openedFileIndex;
    }

    final var position = mViewModel.getOpenedFileCount();

    LOG.info("Opening file at index:", position, "file: ", file);

    final var editor = new CodeEditorView(this, file, selection);
    editor.getEditor().subscribeEvent(ContentChangeEvent.class, this::onEditorContentChanged);
    editor.setLayoutParams(
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    mBinding.editorContainer.addView(editor);
    mBinding.editorContainer.setDisplayedChild(position);
    mBinding.tabs.addTab(mBinding.tabs.newTab().setText(file.getName()));

    mViewModel.addFile(file);
    mViewModel.setCurrentFile(position, file);

    return position;
  }

  public int findIndexOfEditorByFile(File file) {
    if (file == null) {
      LOG.error("Cannot find index of a null file.");
      return -1;
    }

    for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
      final var opened = mViewModel.getOpenedFile(i);
      if (opened.equals(file)) {
        return i;
      }
    }

    return -1;
  }

  private void onEditorContentChanged(ContentChangeEvent event, Unsubscribe unsubscribe) {
    if (event.getAction() != ContentChangeEvent.ACTION_SET_NEW_TEXT) {
      mViewModel.setFilesModified(true);
    }
  }

  @Override
  public void showFileOptions(File thisFile, TreeNode node) {
    mLastHeld = node;
    getFileOptionsFragment(thisFile).show(getSupportFragmentManager(), TAG_FILE_OPTIONS_FRAGMENT);
  }

  public OptionsListFragment getFileOptionsFragment(File file) {
    mFileOptionsFragment = new OptionsListFragment();
    mFileOptionsFragment.addOption(
        new SheetOption(0, R.drawable.ic_file_copy_path, R.string.copy_path, file));
    mFileOptionsFragment.addOption(
        new SheetOption(1, R.drawable.ic_file_rename, R.string.rename_file, file));
    mFileOptionsFragment.addOption(
        new SheetOption(2, R.drawable.ic_delete, R.string.delete_file, file));
    if (file.isDirectory()) {
      mFileOptionsFragment.addOption(
          new SheetOption(3, R.drawable.ic_new_file, R.string.new_file, file));
      mFileOptionsFragment.addOption(
          new SheetOption(4, R.drawable.ic_new_folder, R.string.new_folder, file));
    }

    return mFileOptionsFragment;
  }

  public ActivityEditorBinding getBinding() {
    return mBinding;
  }

  public LayoutDiagnosticInfoBinding getDiagnosticBinding() {
    return mDiagnosticInfoBinding;
  }

  @Override
  public void onTabSelected(@NonNull TabLayout.Tab tab) {
    final var position = tab.getPosition();
    mBinding.editorContainer.setDisplayedChild(position);

    final var editorView = getEditorAtIndex(position);
    Objects.requireNonNull(editorView);

    editorView.onEditorSelected();
    mViewModel.setCurrentFile(position, editorView.getFile());
    refreshSymbolInput(editorView);
  }

  @Override
  public void onTabUnselected(@NonNull TabLayout.Tab tab) {
    final var position = tab.getPosition();
    final var editorView = getEditorAtIndex(position);
    if (editorView != null) {
      final var editor = editorView.getEditor();
      if (editor != null) {
        editor.ensureWindowsDismissed();
      }
    }
  }

  @Override
  public void onTabReselected(@NonNull TabLayout.Tab tab) {
    mTabCloseAction.show(tab.view);
  }

  private void refreshSymbolInput(@NonNull CodeEditorView frag) {
    symbolInput.bindEditor(frag.getEditor());
    symbolInput.setSymbols(Symbols.forFile(frag.getFile()));
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem p1) {
    final int id = p1.getItemId();
    if (id == R.id.editornav_discuss) {
      getApp().openTelegramGroup();
    } else if (id == R.id.editornav_suggest) {
      getApp().openGitHub();
    } else if (id == R.id.editornav_needHelp) {
      showNeedHelpDialog();
    } else if (id == R.id.editornav_settings) {
      startActivity(new Intent(this, PreferencesActivity.class));
    } else if (id == R.id.editornav_share) {
      startActivity(IntentUtils.getShareTextIntent(getString(R.string.msg_share_app)));
    } else if (id == R.id.editornav_close_project) {
      confirmProjectClose();
    } else if (id == R.id.editornav_terminal) {
      openTerminal();
    }
    mBinding.getRoot().closeDrawer(GravityCompat.START);
    return false;
  }

  public void loadFragment(Fragment fragment) {
    super.loadFragment(fragment, mBinding.editorFrameLayout.getId());
  }

  public FileTreeFragment getFileTreeFragment() {
    return mFileTreeFragment;
  }

  public TreeNode getLastHoldTreeNode() {
    return mLastHeld;
  }

  public ProgressSheet getProgressSheet(int msg) {
    if (mSearchingProgress != null && mSearchingProgress.isShowing()) {
      mSearchingProgress.dismiss();
    }
    mSearchingProgress = new ProgressSheet();
    mSearchingProgress.setCancelable(false);
    mSearchingProgress.setWelcomeTextEnabled(false);
    mSearchingProgress.setMessage(getString(msg));
    mSearchingProgress.setSubMessageEnabled(false);
    return mSearchingProgress;
  }

  public void setDiagnosticsAdapter(@NonNull final DiagnosticsAdapter adapter) {
    runOnUiThread(
        () -> {
          final var diagnosticsFragment = bottomSheetTabAdapter.getDiagnosticsFragment();
          if (diagnosticsFragment != null) {
            diagnosticsFragment.setAdapter(adapter);
          }
        });
  }

  public void setSearchResultAdapter(@NonNull final SearchListAdapter adapter) {
    runOnUiThread(
        () -> {
          final var searchResultFragment = bottomSheetTabAdapter.getSearchResultFragment();
          if (searchResultFragment != null) {
            searchResultFragment.setAdapter(adapter);
          }
        });
  }

  @SuppressWarnings("UnusedReturnValue")
  public boolean saveAll() {
    return saveAll(true);
  }

  public void closeFile(int index) {
    if (index >= 0 && index < mViewModel.getOpenedFileCount()) {
      var opened = mViewModel.getOpenedFile(index);
      LOG.info("Closing file:", opened);
      final var editor = getEditorAtIndex(index);

      if (editor != null && editor.isModified()) {
        notifyFilesUnsaved(Collections.singletonList(editor), () -> closeFile(index));
        return;
      }

      if (editor != null && editor.getEditor() != null) {
        editor.getEditor().close();
        editor.getEditor().release();
      } else {
        LOG.error("Cannot save file before close. Editor instance is null");
      }

      mViewModel.removeFile(index);
      mBinding.tabs.removeTabAt(index);
      mBinding.editorContainer.removeViewAt(index);
    } else {
      LOG.error("Invalid file index. Cannot close.");
      return;
    }

    mBinding.tabs.requestLayout();
  }

  public void closeAll() {
    closeAll(null);
  }

  /**
   * Close all opened files. If all the files are saved successfully, then the provided {@link
   * Runnable} will be called.
   *
   * @param onSaved The {@link Runnable} to run when all the files are saved.
   */
  public void closeAll(Runnable onSaved) {
    final var count = mViewModel.getOpenedFileCount();
    final var unsavedFiles =
        mViewModel.getOpenedFiles().stream()
            .map(this::getEditorForFile)
            .filter(editor -> editor != null && editor.isModified())
            .collect(Collectors.toList());

    if (unsavedFiles.isEmpty()) {
      // Files were already saved, close all files one by one
      for (int i = 0; i < count; i++) {
        final var editor = getEditorAtIndex(i);
        if (editor != null && editor.getEditor() != null) {
          editor.getEditor().close();
        } else {
          LOG.error("Unable to close file at index:", i);
        }
      }

      mViewModel.removeAllFiles();
      mBinding.tabs.removeAllTabs();
      mBinding.tabs.requestLayout();
      mBinding.editorContainer.removeAllViews();

      if (onSaved != null) {
        onSaved.run();
      }
    } else {
      // There are unsaved files
      notifyFilesUnsaved(unsavedFiles, () -> closeAll(onSaved));
    }
  }

  public boolean areFilesModified() {
    return mViewModel.areFilesModified();
  }

  public EditorViewModel getViewModel() {
    return mViewModel;
  }

  public void closeOthers() {
    final var unsavedFiles =
        mViewModel.getOpenedFiles().stream()
            .map(this::getEditorForFile)
            .filter(editor -> editor != null && editor.isModified())
            .collect(Collectors.toList());

    if (unsavedFiles.isEmpty()) {
      final var file = mViewModel.getCurrentFile();
      for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
        final var editor = getEditorAtIndex(i);

        // Index of files changes as we keep close files
        // So we compare the files instead of index
        if (editor != null) {
          if (!file.equals(editor.getFile())) {
            closeFile(i);
          }
        } else {
          LOG.error("Unable to save file at index:", i);
        }
      }
    } else {
      notifyFilesUnsaved(unsavedFiles, this::closeOthers);
    }
  }

  public void previewLayout() {
    try {

      if (getCurrentEditor() == null || getCurrentEditor().getFile() == null) {
        LOG.error("No file is opened. Cannot preview layout.");
        return;
      }

      saveAll(false);

      getResourceDirPaths()
          .whenComplete(
              (dirs, error) ->
                  runOnUiThread(
                      () -> {
                        final Intent intent = new Intent(this, DesignerActivity.class);
                        intent.putExtra(
                            DesignerActivity.KEY_LAYOUT_PATH,
                            getCurrentEditor().getFile().getAbsolutePath());
                        intent.putStringArrayListExtra(DesignerActivity.KEY_RES_DIRS, dirs);
                        LOG.info("Launching UI Designer...");
                        mUIDesignerLauncher.launch(intent);
                      }));
    } catch (Throwable th) {
      LOG.error(getString(R.string.err_cannot_preview_layout), th);
      getApp().toast(R.string.msg_cannot_preview_layout, Toaster.Type.ERROR);
    }
  }

  public boolean saveAll(boolean notify) {
    return saveAll(notify, false);
  }

  @NonNull
  private CompletableFuture<ArrayList<String>> getResourceDirPaths() {
    return getResourceDirs()
        .thenApply(
            files ->
                files.stream()
                    .filter(Objects::nonNull)
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toCollection(ArrayList::new)));
  }

  public boolean saveAll(boolean notify, boolean canProcessResources) {
    SaveResult result = saveAllResult();

    if (notify) {
      getApp().toast(R.string.all_saved, Toaster.Type.SUCCESS);
    }

    if (result.gradleSaved) {
      notifySyncNeeded();
    }

    if (canProcessResources) {
      ProjectManager.INSTANCE.generateSources(mBuildService);
    }

    return result.gradleSaved;
  }

  private CompletableFuture<List<File>> getResourceDirs() {
    return ProjectManager.INSTANCE.getApplicationResDirectories();
  }

  public SaveResult saveAllResult() {
    SaveResult result = new SaveResult();
    for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
      saveResult(i, result);
    }

    return result;
  }

  public void notifySyncNeeded() {
    if (mBuildService != null && !mBuildService.isBuildInProgress()) {
      getSyncBanner()
          .setNegative(android.R.string.cancel, null)
          .setPositive(android.R.string.ok, v -> initializeProject())
          .show();
    }
  }

  public void saveResult(int index, SaveResult result) {
    if (index >= 0 && index < mViewModel.getOpenedFileCount()) {
      var frag = getEditorAtIndex(index);
      if (frag == null || frag.getFile() == null) {
        return;
      }

      // Must be called before frag.save()
      // Otherwise, it'll always return false
      final boolean modified = frag.isModified();

      frag.save();

      final boolean isGradle = frag.getFile().getName().endsWith(".gradle");
      final boolean isXml = frag.getFile().getName().endsWith(".xml");

      if (!result.gradleSaved) {
        result.gradleSaved = modified && isGradle;
      }

      if (!result.xmlSaved) {
        result.xmlSaved = modified && isXml;
      }
    }

    var modified = false;
    for (var file : mViewModel.getOpenedFiles()) {
      var editor = getEditorForFile(file);
      if (editor == null) {
        continue;
      }
      modified = modified || editor.isModified();
    }

    final boolean finalModified = modified;
    ThreadUtils.runOnUiThread(() -> mViewModel.setFilesModified(finalModified));
  }

  public MaterialBanner getSyncBanner() {
    return mBinding
        .syncBanner
        .setContentTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
        .setBannerBackgroundColor(ContextCompat.getColor(this, R.color.primaryLightColor))
        .setButtonTextColor(ContextCompat.getColor(this, R.color.secondaryColor))
        .setIcon(R.drawable.ic_sync)
        .setContentText(R.string.msg_sync_needed);
  }

  public void initializeProject() {
    final var projectPath = ProjectManager.INSTANCE.getProjectPath();
    if (projectPath == null) {
      LOG.error("Cannot initialize project. Project model is null.");
      return;
    }

    final var projectDir = new File(projectPath);
    if (!projectDir.exists()) {
      LOG.error("Project directory does not exist. Cannot initialize project");
      return;
    }

    ThreadUtils.runOnUiThread(
        () -> {
          setStatus(getString(R.string.msg_initializing_project));
          mBinding.buildProgressIndicator.setVisibility(View.VISIBLE);
        });

    final var future = mBuildService.initializeProject(projectDir.getAbsolutePath());
    future.whenComplete(
        (result, error) -> {
          if (result == null || error != null) {
            LOG.error("An error occurred initializing the project with Tooling API", error);
            setStatus(getString(R.string.msg_project_initialization_failed));
            return;
          }

          onProjectInitialized();
        });
  }

  public void setStatus(final CharSequence text) {
    setStatus(text, Gravity.CENTER);
  }

  protected void onProjectInitialized() {
    ProjectManager.INSTANCE.notifyProjectUpdate();
    ThreadUtils.runOnUiThread(
        () -> {
          initialSetup();
          setStatus(getString(R.string.msg_project_initialized));
          mBinding.buildProgressIndicator.setVisibility(View.GONE);

          if (mFindInProjectDialog != null && mFindInProjectDialog.isShowing()) {
            mFindInProjectDialog.dismiss();
          }

          mFindInProjectDialog = null; // Create the dialog again if needed
        });
  }

  public void setStatus(final CharSequence text, @GravityInt int gravity) {
    try {
      runOnUiThread(
          () -> {
            mBinding.bottomSheet.statusText.setGravity(gravity);
            mBinding.bottomSheet.statusText.setText(text);
          });
    } catch (Throwable th) {
      LOG.error("Failed to update status text", th);
    }
  }

  public void assembleDebug(boolean installApk) {
    execTasks(installApk ? installableTaskResultConsumer("debug") : null, "assembleDebug");
  }

  @SuppressWarnings("UnusedReturnValue")
  @NonNull
  public CompletableFuture<TaskExecutionResult> execTasks(
      Consumer<TaskExecutionResult> resultHandler, String... tasks) {
    saveAll(false);
    runOnUiThread(() -> appendBuildOut("Executing tasks: " + TextUtils.join(", ", tasks)));
    return mBuildService
        .executeTasks(tasks)
        .whenComplete(
            ((executionResult, throwable) -> {
              if (executionResult == null || throwable != null) {
                LOG.error("Tasks failed to execute", TextUtils.join(", ", tasks));
              }

              if (resultHandler != null) {
                resultHandler.accept(executionResult);
              }
            }));
  }

  public Consumer<TaskExecutionResult> installableTaskResultConsumer(@NonNull String variantName) {
    return result -> {
      if (result != null && result.isSuccessful()) {
        LOG.debug("Installing APK(s) for variant:", variantName);
        // TODO Handle multiple application modules
        final var projectManager = ProjectManager.INSTANCE;
        final var future = projectManager.getApplicationModule();
        future.whenCompleteAsync(
            (app, error) -> {
              if (app == null) {
                return;
              }

              Optional<SimpleVariantData> foundVariant =
                  app.getSimpleVariants().stream()
                      .filter(it -> variantName.equals(it.getName()))
                      .findFirst();
              if (foundVariant.isPresent()) {
                final var variant = foundVariant.get();
                final var main = variant.getMainArtifact();
                final var outputListingFile = main.getAssembleTaskOutputListingFile();
                if (outputListingFile == null) {
                  LOG.error("No output listing file provided with project model");
                  return;
                }

                final var apkFile = ApkMetadata.findApkFile(outputListingFile);
                if (apkFile == null) {
                  LOG.error("No apk file specified in output listing file:", outputListingFile);
                  return;
                }

                if (!apkFile.exists()) {
                  LOG.error("APK file specified in output listing file does not exist!", apkFile);
                  return;
                }

                install(apkFile);
              } else {
                LOG.error(
                    "No", variantName, "variant found in application module", app.projectPath);
              }
            });
      } else {
        LOG.debug("Cannot install APK. Task execution result:", result);
      }
    };
  }

  public void appendBuildOut(final String str) {
    final var frag = bottomSheetTabAdapter.getBuildOutputFragment();

    if (frag != null) {
      frag.appendOutput(str);
    }
  }

  public void install(@NonNull File apk) {
    runOnUiThread(
        () -> {
          LOG.debug("Installing APK:", apk);
          if (apk.exists()) {
            Intent i = IntentUtils.getInstallAppIntent(apk);
            if (i != null) {
              startActivity(i);
            } else {
              getApp().toast(R.string.msg_apk_install_intent_failed, Toaster.Type.ERROR);
            }
          } else {
            LOG.error("APK file does not exist!");
          }
        });
  }

  public void assembleRelease() {
    execTasks(null, "assembleRelease");
  }

  public void build() {
    execTasks(null, "build");
  }

  public void clean() {
    execTasks(null, "clean");
  }

  public void bundle() {
    execTasks(null, "bundle");
  }

  public void lint() {
    execTasks(null, "lint");
  }

  public void lintDebug() {
    execTasks(null, "lintDebug");
  }

  public void lintRelease() {
    execTasks(null, "lintRelease");
  }

  public void cleanAndRebuild() {
    execTasks(null, "clean", "build");
  }

  /////////////////////////////////////////////////
  ////////////// PRIVATE APIS /////////////////////
  /////////////////////////////////////////////////

  public AlertDialog getFindInProjectDialog() {
    return mFindInProjectDialog == null ? createFindInProjectDialog() : mFindInProjectDialog;
  }

  @Override
  public void onOptionsClick(SheetOption option) {
    if (mFileOptionsHandler != null) {
      mFileOptionsHandler.onOptionsClick(option);
    }
  }

  public GradleBuildService getBuildService() {
    return mBuildService;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(mBinding.editorToolbar);

    mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);

    mFileTreeFragment = FileTreeFragment.newInstance();
    mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true);

    setupDrawerToggle();
    loadFragment(mFileTreeFragment);

    symbolInput = new SymbolInputView(this);
    mBinding.bottomSheet.textContainer.addView(symbolInput, 0, new ViewGroup.LayoutParams(-1, -2));
    mBinding.tabs.addOnTabSelectedListener(this);

    setupEditorBottomSheet();
    createQuickActions();

    mBuildEventListener.setActivity(this);
    mFileOptionsHandler = new FileOptionsHandler(this);

    startServices();

    KeyboardUtils.registerSoftInputChangedListener(this, __ -> onSoftInputChanged());
    registerLogReceiver();
    setupContainers();
    setupDiagnosticInfo();

    mUIDesignerLauncher =
        registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::onGetUIDesignerResult);

    EditorActivityActions.register(this);
  }

  @Override
  protected View bindLayout() {
    mBinding = ActivityEditorBinding.inflate(getLayoutInflater());
    mDiagnosticInfoBinding = mBinding.diagnosticInfo;
    return mBinding.getRoot();
  }

  @Override
  protected void onPause() {
    super.onPause();
    dispatchOnPauseToEditors();
    EditorActivityActions.clear();

    if (mFileTreeFragment != null) {
      mFileTreeFragment.saveTreeState();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    EditorActivityActions.register(this);

    try {
      checkForCompilerModule();
      dispatchOnResumeToEditors();

      if (mFileTreeFragment != null) {
        mFileTreeFragment.listProjectFiles();
      }
    } catch (Throwable th) {
      LOG.error("Failed to update files list", th);
      getApp().toast(R.string.msg_failed_list_files, Toaster.Type.ERROR);
    }
  }

  @SuppressWarnings("deprecation")
  private void checkForCompilerModule() {
    if (!Environment.isCompilerModuleInstalled()) {
      final var pd =
          ProgressDialog.show(
              this,
              getString(R.string.title_compiler_module_install),
              getString(R.string.msg_compiler_module_install),
              true,
              false);

      final CompletableFuture<Boolean> future =
          CompletableFuture.supplyAsync(
              () -> {
                final var tmpModule = new File(Environment.TMP_DIR, "compiler-module.zip");
                if (!ResourceUtils.copyFileFromAssets(
                    ToolsManager.getCommonAsset("compiler-module.zip"),
                    tmpModule.getAbsolutePath())) {
                  throw new CompletionException(
                      new RuntimeException("Unable to copy compiler-module.zip"));
                }

                try {
                  ZipUtils.unzipFile(tmpModule, Environment.COMPILER_MODULE);
                } catch (Throwable e) {
                  throw new CompletionException(e);
                }

                if (!Environment.isCompilerModuleInstalled()) {
                  throw new CompletionException(new RuntimeException("Unknown error"));
                }

                try {
                  FileUtils.delete(tmpModule);
                } catch (Exception e) {
                  // ignored
                }

                return true;
              });

      future.whenComplete(
          (result, error) -> {
            pd.dismiss();

            if (error != null) {
              showCompilerModuleInstallError(error);
              return;
            }

            getApp().toast(getString(R.string.msg_compiler_module_installed), Toaster.Type.SUCCESS);
          });
    }
  }

  private void dispatchOnResumeToEditors() {
    CompletableFuture.runAsync(
        () -> {
          for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
            final var editor = getEditorAtIndex(i);
            if (editor != null) {
              editor.onResume();
            }
          }
        });
  }

  private void showCompilerModuleInstallError(Throwable error) {
    final var stacktrace = ThrowableUtils.getFullStackTrace(error);
    final var builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_installation_failed);
    builder.setMessage(getString(R.string.msg_compiler_module_install_failed, stacktrace));
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
    builder.setNegativeButton(
        R.string.copy,
        (dialog, which) -> {
          ClipboardUtils.copyText(stacktrace);
          dialog.dismiss();
        });
    builder.show();
  }

  private void dispatchOnPauseToEditors() {
    CompletableFuture.runAsync(
        () -> {
          for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
            final var editor = getEditorAtIndex(i);
            if (editor != null) {
              editor.onPause();
            }
          }
        });
  }

  @Override
  protected void onDestroy() {
    closeProject(false);
    try {
      unregisterReceiver(mLogReceiver);
    } catch (Throwable th) {
      LOG.error("Failed to unregister LogReceiver", th);
    }
    unbindService(mGradleServiceConnection);
    super.onDestroy();
    mBinding = null;
    mViewModel = null;
  }

  private void setupDrawerToggle() {
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            mBinding.editorDrawerLayout,
            mBinding.editorToolbar,
            R.string.app_name,
            R.string.app_name);
    mBinding.editorDrawerLayout.addDrawerListener(toggle);
    mBinding.startNav.setNavigationItemSelectedListener(this);
    toggle.syncState();
  }

  private void setupEditorBottomSheet() {
    bottomSheetTabAdapter = new EditorBottomSheetTabAdapter(this);
    mBinding.bottomSheet.pager.setAdapter(bottomSheetTabAdapter);

    final var mediator =
        new TabLayoutMediator(
            mBinding.bottomSheet.tabs,
            mBinding.bottomSheet.pager,
            true,
            true,
            (tab, position) -> tab.setText(bottomSheetTabAdapter.getTitle(position)));

    mediator.attach();
    mBinding.bottomSheet.pager.setUserInputEnabled(false);
    mBinding.bottomSheet.pager.setOffscreenPageLimit(
        bottomSheetTabAdapter.getItemCount() - 1); // DO not remove any views

    mEditorBottomSheet =
        (EditorBottomSheetBehavior<? extends View>)
            EditorBottomSheetBehavior.from(mBinding.bottomSheet.getRoot());
    mEditorBottomSheet.setBinding(mBinding.bottomSheet);
    mEditorBottomSheet.addBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {
            mBinding.bottomSheet.textContainer.setVisibility(
                newState == BottomSheetBehavior.STATE_EXPANDED ? View.INVISIBLE : View.VISIBLE);

            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
              final var editor = getCurrentEditor();
              if (editor != null && editor.getEditor() != null) {
                editor.getEditor().ensureWindowsDismissed();
              }
            }
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mBinding.bottomSheet.textContainer.setAlpha(1f - slideOffset);
          }
        });

    mBinding.bottomSheet.tabs.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {

          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            final var fragment = bottomSheetTabAdapter.getFragmentAtIndex(tab.getPosition());
            if (fragment instanceof NonEditableEditorFragment
                || fragment instanceof LogViewFragment) {
              mBinding.bottomSheet.clearFab.show();
              mBinding.bottomSheet.shareOutputFab.show();
            } else {
              mBinding.bottomSheet.clearFab.hide();
              mBinding.bottomSheet.shareOutputFab.hide();
            }
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {}

          @Override
          public void onTabReselected(TabLayout.Tab tab) {}
        });

    TooltipCompat.setTooltipText(
        mBinding.bottomSheet.clearFab, getString(R.string.title_clear_output));
    mBinding.bottomSheet.clearFab.setOnClickListener(
        v -> {
          final var fragment =
              bottomSheetTabAdapter.getFragmentAtIndex(
                  mBinding.bottomSheet.tabs.getSelectedTabPosition());
          if (fragment instanceof NonEditableEditorFragment) {
            final var editor = (NonEditableEditorFragment) fragment;
            if (editor.getEditor() != null) {
              editor.getEditor().setText("");
            }
          } else if (fragment instanceof LogViewFragment) {
            final LogViewFragment logFrag = (LogViewFragment) fragment;
            final var adapter = logFrag.getAdapter();
            if (adapter != null) {
              adapter.clear();
            } else {
              LOG.error(
                  "Cannot clear contents. Adapter in LogViewFragment("
                      + logFrag.getLogType()
                      + ") is null.");
            }
          }
        });

    mBinding.bottomSheet.shareOutputFab.setOnClickListener(
        v -> {
          final var fragment =
              bottomSheetTabAdapter.getFragmentAtIndex(
                  mBinding.bottomSheet.tabs.getSelectedTabPosition());

          if (fragment instanceof LogViewFragment) {
            final var logFrag = (LogViewFragment) fragment;
            final var type = logFrag.getLogType();
            final var adapter = logFrag.getAdapter();
            if (adapter != null) {

              //noinspection deprecation
              final var progress =
                  ProgressDialog.show(EditorActivity.this, null, getString(R.string.please_wait));
              CompletableFuture.runAsync(
                  () -> {
                    final var text = adapter.allAsString();
                    ThreadUtils.runOnUiThread(
                        () -> {
                          progress.dismiss();
                          shareText(text, type);
                        });
                  });
            } else {
              LOG.error("Adapter in LogViewFragment(" + type + ") is null");
            }
            return;
          }

          if (fragment instanceof SimpleOutputFragment) {
            final var editor = (SimpleOutputFragment) fragment;
            final var text = Objects.requireNonNull(editor.getEditor()).getText();
            final var type = "build_output";
            shareText(text, type);
            return;
          }

          LOG.error("Unknown fragment:", fragment);
        });

    if (!getApp().getPrefManager().getBoolean(KEY_BOTTOM_SHEET_SHOWN)
        && mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
      new Handler(Looper.getMainLooper())
          .postDelayed(
              () -> {
                mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                getApp().getPrefManager().putBoolean(KEY_BOTTOM_SHEET_SHOWN, true);
              },
              1500);
    }
  }

  @SuppressWarnings("deprecation")
  private void shareText(CharSequence text, String type) {
    final var pd = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
    CompletableFuture.supplyAsync(() -> writeTempFile(text, type))
        .whenComplete(
            (result, error) -> {
              ThreadUtils.runOnUiThread(pd::dismiss);
              if (result == null || error != null) {
                LOG.warn("Unable to share output", error);
                return;
              }

              ThreadUtils.runOnUiThread(() -> shareFile(result));
            });
  }

  private void shareFile(File file) {
    final var uri =
        FileProvider.getUriForFile(
            this, BuildConfig.APPLICATION_ID + ".utilcode.fileprovider", file);
    final var intent = new Intent(Intent.ACTION_SEND);
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_STREAM, uri);

    startActivity(Intent.createChooser(intent, null));
  }

  @NonNull
  private File writeTempFile(CharSequence text, String type) {
    final var in = new CharSequenceInputStream(text, StandardCharsets.UTF_8);
    final var file =
        new File(getFilesDir(), type + ".txt"); // use a common name to avoid multiple files
    if (!FileIOUtils.writeFileFromIS(file, in)) {
      LOG.error("Unable to write output of type", type, "to temporary file", file);
      throw new RuntimeException(new IOException("Cannot write output to temp file"));
    }

    return file;
  }

  private void notifyFilesUnsaved(List<CodeEditorView> unsavedEditors, Runnable invokeAfter) {
    //noinspection ConstantConditions
    final var mapped =
        unsavedEditors.stream()
            .map(CodeEditorView::getFile)
            .filter(Objects::nonNull)
            .map(File::getAbsolutePath)
            .collect(Collectors.toList());
    final var builder =
        DialogUtils.newYesNoDialog(
            this,
            getString(R.string.title_files_unsaved), // title
            getString(R.string.msg_files_unsaved, TextUtils.join("\n", mapped)), // message
            (dialog, which) -> { // 'yes' click
              dialog.dismiss();
              saveAll(true);
              invokeAfter.run();
            },
            (dialog, which) -> { // 'no' click
              dialog.dismiss();
              // Mark all the files as saved, then try to close them all
              for (var editor : unsavedEditors) {
                editor.markAsSaved();
              }

              invokeAfter.run();
            });
    builder.show();
  }

  @Contract(pure = true)
  private void onGetUIDesignerResult(@NonNull ActivityResult result) {
    final var index = mBinding.editorContainer.getDisplayedChild();
    final var editor = getEditorAtIndex(index);
    if (editor != null && result.getResultCode() == RESULT_OK) {
      final var data = result.getData();
      if (data != null && data.hasExtra(DesignerActivity.KEY_GENERATED_CODE)) {
        final var code = data.getStringExtra(DesignerActivity.KEY_GENERATED_CODE);
        editor.getEditor().setText(code);
        saveAll();
      } else {
        final var msg = getString(R.string.msg_invalid_designer_result);
        getApp().toast(msg, Toaster.Type.ERROR);
        LOG.error(msg, "Data returned by UI Designer is null or is invalid.");
      }
    } else {
      LOG.error(
          "UI Designer returned an invalid result code.", "Result code: " + result.getResultCode());
    }
  }

  private void openTerminal() {
    final Intent intent = new Intent(this, TerminalActivity.class);
    intent.putExtra(
        TerminalActivity.KEY_WORKING_DIRECTORY,
        Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDirPath()));
    startActivity(intent);
  }

  private void initialSetup() {
    getApp()
        .getPrefManager()
        .setOpenedProject(Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDirPath()));

    try {
      //noinspection ConstantConditions
      final var rootProject = ProjectManager.INSTANCE.getRootProject();

      var projectName = rootProject.getName().get();
      if (projectName.isEmpty()) {
        projectName = new File(ProjectManager.INSTANCE.getProjectDirPath()).getName();
        getSupportActionBar().setSubtitle(projectName);
      } else {
        getSupportActionBar().setSubtitle(projectName);
      }
    } catch (Throwable th) {
      // ignored
    }

    getResourceDirs()
        .thenAccept(
            dirs -> {
              dirs.removeIf(Objects::isNull);
              ValuesTableFactory.setupWithResDirectories(dirs.toArray(new File[0]));
            });
  }

  private void setupDiagnosticInfo() {
    GradientDrawable gd = new GradientDrawable();
    gd.setShape(GradientDrawable.RECTANGLE);
    gd.setColor(0xff212121);
    gd.setStroke(1, 0xffffffff);
    gd.setCornerRadius(8);
    mDiagnosticInfoBinding.getRoot().setBackground(gd);
    mDiagnosticInfoBinding.getRoot().setVisibility(View.GONE);
  }

  private void setupContainers() {
    handleDiagnosticsResultVisibility(true);
    handleSearchResultVisibility(true);
  }

  private void startServices() {

    if (bindService(
        new Intent(this, GradleBuildService.class),
        mGradleServiceConnection,
        Context.BIND_AUTO_CREATE)) {
      LOG.info("Bind request for Gradle build service was successful...");
    } else {
      LOG.error("Gradle build service doesn't exist or the IDE is not allowed to access it.");
    }

    if (!IDELanguageClientImpl.isInitialized()) {
      IDELanguageClientImpl.initialize(this);
    }

    initializeLanguageServers();

    // Actually, we don't need to start FileOptionsHandler
    // Because it would work anyway
    // But still we do...
    mFileOptionsHandler.start();
  }

  private void initializeLanguageServers() {
    final var client = IDELanguageClientImpl.getInstance();
    final var javaLanguageServer = getApp().getJavaLanguageServer();
    final var workspaceRoots = new HashSet<Path>();
    workspaceRoots.add(
        new File(Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDirPath())).toPath());
    workspaceRoots.add(Environment.HOME.toPath().resolve("logsender"));

    final var params = new InitializeParams(workspaceRoots);

    javaLanguageServer.connectClient(client);
    javaLanguageServer.applySettings(JavaServerSettings.getInstance());
    javaLanguageServer.initialize(params);
  }

  private void stopServices() {

    if (IDELanguageClientImpl.isInitialized()) {
      IDELanguageClientImpl.shutdown();
    }

    shutdownLanguageServers();
    if (mFileOptionsHandler != null) {
      mFileOptionsHandler.stop();
    }
  }

  private void shutdownLanguageServers() {
    final var javaServer = getApp().getJavaLanguageServer();
    javaServer.shutdown();
  }

  private void onSoftInputChanged() {
    invalidateOptionsMenu();
    if (KeyboardUtils.isSoftInputVisible(this)) {
      TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.TOP));
      symbolInput.setVisibility(View.VISIBLE);
      mBinding.bottomSheet.statusText.setVisibility(View.GONE);
      mBinding.bottomSheet.swipeHint.setVisibility(View.GONE);
    } else {
      TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.BOTTOM));
      symbolInput.setVisibility(View.GONE);
      mBinding.bottomSheet.statusText.setVisibility(View.VISIBLE);
      mBinding.bottomSheet.swipeHint.setVisibility(View.VISIBLE);
    }
  }

  private void closeProject(boolean manualFinish) {
    stopServices();

    // Make sure we close files
    // This fill further make sure that file contents are not erased.
    closeAll(
        () -> {
          getApp().getPrefManager().setOpenedProject(PreferenceManager.NO_OPENED_PROJECT);

          if (manualFinish) {
            finish();
          }
        });
  }

  private void confirmProjectClose() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_confirm_project_close);
    builder.setMessage(R.string.msg_confirm_project_close);
    builder.setNegativeButton(R.string.no, null);
    builder.setPositiveButton(
        R.string.yes,
        (d, w) -> {
          d.dismiss();
          closeProject(true);
        });
    builder.show();
  }

  @Nullable
  private AlertDialog createFindInProjectDialog() {
    final var rootProject = ProjectManager.INSTANCE.getRootProject();
    if (rootProject == null) {
      LOG.warn("No root project model found. Is the project initialized?");
      getApp().toast(getString(R.string.msg_project_not_initialized), Toaster.Type.ERROR);
      return null;
    }

    List<File> moduleDirs;
    try {
      moduleDirs =
          rootProject.listModules().get().stream()
              .map(SimpleModuleData::getProjectDir)
              .collect(Collectors.toList());
    } catch (Throwable e) {
      StudioApp.getInstance().toast(getString(R.string.msg_no_modules), Toaster.Type.ERROR);
      moduleDirs = Collections.emptyList();
    }

    return createFindInProjectDialog(moduleDirs);
  }

  private AlertDialog createFindInProjectDialog(List<File> moduleDirs) {
    final List<File> srcDirs = new ArrayList<>();
    final LayoutSearchProjectBinding binding =
        LayoutSearchProjectBinding.inflate(getLayoutInflater());
    binding.modulesContainer.removeAllViews();
    for (int i = 0; i < moduleDirs.size(); i++) {
      final File module = moduleDirs.get(i);
      final File src = new File(module, "src");
      if (!module.exists() || !module.isDirectory() || !src.exists() || !src.isDirectory()) {
        continue;
      }

      CheckBox check = new CheckBox(this);
      check.setText(module.getName());
      check.setChecked(true);

      LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(-2, -2);
      params.bottomMargin = SizeUtils.dp2px(4);
      binding.modulesContainer.addView(check, params);

      srcDirs.add(src);
    }

    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.menu_find_project);
    builder.setView(binding.getRoot());
    builder.setCancelable(false);
    builder.setPositiveButton(
        R.string.menu_find,
        (dialog, which) -> {
          final String text =
              Objects.requireNonNull(binding.input.getEditText()).getText().toString().trim();
          if (text.isEmpty()) {
            getApp().toast(R.string.msg_empty_search_query, Toaster.Type.ERROR);
            return;
          }

          final List<File> searchDirs = new ArrayList<>();
          for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
            CheckBox check = (CheckBox) binding.modulesContainer.getChildAt(i);
            if (check.isChecked()) {
              searchDirs.add(srcDirs.get(i));
            }
          }

          final String extensions =
              Objects.requireNonNull(binding.filter.getEditText()).getText().toString().trim();
          final List<String> extensionList = new ArrayList<>();
          if (!extensions.isEmpty()) {
            if (extensions.contains("|")) {
              for (String str : extensions.split(Pattern.quote("|"))) {
                if (str == null || str.trim().isEmpty()) {
                  continue;
                }

                extensionList.add(str);
              }
            } else {
              extensionList.add(extensions);
            }
          }

          if (searchDirs.isEmpty()) {
            getApp().toast(R.string.msg_select_search_modules, Toaster.Type.ERROR);
          } else {
            dialog.dismiss();
            getProgressSheet(R.string.msg_searching_project)
                .show(getSupportFragmentManager(), "search_in_project_progress");
            RecursiveFileSearcher.searchRecursiveAsync(
                text, extensionList, searchDirs, this::handleSearchResults);
          }
        });
    builder.setNegativeButton(android.R.string.cancel, (__, ___) -> __.dismiss());
    mFindInProjectDialog = builder.create();
    return mFindInProjectDialog;
  }

  private void registerLogReceiver() {
    IntentFilter filter = new IntentFilter();
    filter.addAction(LogReceiver.APPEND_LOG);
    registerReceiver(mLogReceiver, filter);
  }

  private void showNeedHelpDialog() {
    MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.need_help);
    builder.setMessage(R.string.msg_need_help);
    builder.setPositiveButton(android.R.string.ok, null);
    builder.create().show();
  }

  private void createQuickActions() {
    ActionItem closeThis =
        new ActionItem(
            ACTION_ID_CLOSE, getString(R.string.action_closeThis), R.drawable.ic_close_this);
    ActionItem closeOthers =
        new ActionItem(
            ACTION_ID_OTHERS, getString(R.string.action_closeOthers), R.drawable.ic_close_others);
    ActionItem closeAll =
        new ActionItem(ACTION_ID_ALL, getString(R.string.action_closeAll), R.drawable.ic_close_all);
    mTabCloseAction = new QuickAction(this, QuickAction.HORIZONTAL);
    mTabCloseAction.addActionItem(closeThis, closeOthers, closeAll);
    mTabCloseAction.setColorRes(R.color.tabAction_background);
    mTabCloseAction.setTextColorRes(R.color.tabAction_text);

    mTabCloseAction.setOnActionItemClickListener(
        (item) -> {
          final int id = item.getActionId();
          if (getApp().getPrefManager().getBoolean(PreferenceManager.KEY_EDITOR_AUTO_SAVE, false)) {
            saveAll();
          }

          if (id == ACTION_ID_CLOSE) {
            closeFile(mBinding.tabs.getSelectedTabPosition());
          }

          if (id == ACTION_ID_OTHERS) {
            closeOthers();
          }

          if (id == ACTION_ID_ALL) {
            closeAll();
          }
        });
  }
}
