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

import static com.blankj.utilcode.util.IntentUtils.getShareTextIntent;
import static com.itsaky.androidide.models.prefs.GeneralPreferencesKt.NO_OPENED_PROJECT;
import static com.itsaky.androidide.models.prefs.GeneralPreferencesKt.setLastOpenedProject;

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
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThreadUtils;
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
import com.itsaky.androidide.fragments.SearchResultFragment;
import com.itsaky.androidide.fragments.ShareableOutputFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.handlers.EditorActivityLifecyclerObserver;
import com.itsaky.androidide.handlers.EditorEventListener;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.lookup.Lookup;
import com.itsaky.androidide.lsp.IDELanguageClientImpl;
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry;
import com.itsaky.androidide.lsp.java.JavaLanguageServer;
import com.itsaky.androidide.lsp.models.DiagnosticItem;
import com.itsaky.androidide.lsp.xml.XMLLanguageServer;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.models.SaveResult;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.models.prefs.EditorPreferencesKt;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.Project;
import com.itsaky.androidide.projects.builder.BuildService;
import com.itsaky.androidide.services.GradleBuildService;
import com.itsaky.androidide.services.LogReceiver;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.EditorActivityActions;
import com.itsaky.androidide.utils.EditorBottomSheetBehavior;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.IntentUtils;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.RecursiveFileSearcher;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.viewmodel.EditorViewModel;
import com.itsaky.androidide.views.MaterialBanner;
import com.itsaky.androidide.views.SymbolInputView;
import com.itsaky.androidide.views.editor.CodeEditorView;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.inflater.values.ValuesTableFactory;
import com.itsaky.toaster.Toaster;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import kotlin.Unit;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class EditorActivity extends StudioActivity
    implements TabLayout.OnTabSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        DiagnosticClickListener,
        EditorActivityProvider {

  public static final String KEY_BOTTOM_SHEET_SHOWN = "editor_bottomSheetShown";
  private static final String KEY_PROJECT_PATH = "saved_projectPath";
  private static final int ACTION_ID_CLOSE = 100;
  private static final int ACTION_ID_OTHERS = 101;
  private static final int ACTION_ID_ALL = 102;
  private static final ILogger LOG = ILogger.newInstance("EditorActivity");

  private final EditorEventListener mBuildEventListener = new EditorEventListener();
  private final EditorActivityLifecyclerObserver mLifecycleObserver =
      new EditorActivityLifecyclerObserver();
  private ActivityEditorBinding mBinding;
  private LayoutDiagnosticInfoBinding mDiagnosticInfoBinding;
  private EditorBottomSheetTabAdapter bottomSheetTabAdapter;
  private final LogReceiver mLogReceiver = new LogReceiver().setLogListener(this::appendApkLog);
  private FileTreeFragment mFileTreeFragment;
  private SymbolInputView symbolInput;
  private QuickAction mTabCloseAction;
  private TextSheetFragment mDaemonStatusFragment;
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
          Lookup.DEFAULT.register(BuildService.class, mBuildService);
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
  private boolean isFinishing = false;

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

  public void handleSearchResults(Map<File, List<SearchResult>> results) {
    if (results == null) {
      results = Collections.emptyMap();
    }

    setSearchResultAdapter(
        new SearchListAdapter(
            results,
            file -> {
              openFile(file);
              hideViewOptions();
              return Unit.INSTANCE;
            },
            match -> {
              openFileAndSelect(match.file, match);
              hideViewOptions();
              return Unit.INSTANCE;
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
      editor.post(
          () -> {
            final var range = editor.validateRange(selection);
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

  public CodeEditorView openFile(File file) {
    return openFile(file, null);
  }

  @Nullable
  public CodeEditorView openFile(File file, com.itsaky.androidide.models.Range selection) {
    if (selection == null) {
      selection = com.itsaky.androidide.models.Range.NONE;
    }

    if (ImageUtils.isImage(file)) {
      IntentUtils.openImage(this, file);
      return null;
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
  public int openFileAndGetIndex(File file, com.itsaky.androidide.models.Range selection) {
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

  public void hideViewOptions() {
    if (mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
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
      startActivity(getShareTextIntent(getString(R.string.msg_share_app)));
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

      final Intent intent = new Intent(this, DesignerActivity.class);
      intent.putExtra(
          DesignerActivity.KEY_LAYOUT_PATH, getCurrentEditor().getFile().getAbsolutePath());
      intent.putStringArrayListExtra(DesignerActivity.KEY_RES_DIRS, getResourceDirPaths());
      LOG.info("Launching UI Designer...");
      mUIDesignerLauncher.launch(intent);
    } catch (Throwable th) {
      LOG.error(getString(R.string.err_cannot_preview_layout), th);
      getApp().toast(R.string.msg_cannot_preview_layout, Toaster.Type.ERROR);
    }
  }

  public boolean saveAll(boolean notify) {
    return saveAll(notify, false);
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

  public SaveResult saveAllResult() {
    SaveResult result = new SaveResult();
    for (int i = 0; i < mViewModel.getOpenedFileCount(); i++) {
      saveResult(i, result);
    }

    return result;
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

  public void notifySyncNeeded() {
    if (mBuildService != null && !mBuildService.isBuildInProgress()) {
      getSyncBanner()
          .setNegative(android.R.string.cancel, null)
          .setPositive(android.R.string.ok, v -> initializeProject())
          .show();
    }
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
    final var projectDir = new File(projectPath);
    if (!projectDir.exists()) {
      LOG.error("Project directory does not exist. Cannot initialize project");
      return;
    }

    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(this::preProjectInit);

    final var future = mBuildService.initializeProject(projectDir.getAbsolutePath());
    future.whenCompleteAsync(
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

  public void setStatus(final CharSequence text, @GravityInt int gravity) {
    try {
      runOnUiThread(
          () -> {
            if (mBinding == null) {
              return;
            }

            mBinding.bottomSheet.statusText.setGravity(gravity);
            mBinding.bottomSheet.statusText.setText(text);
          });
    } catch (Throwable th) {
      LOG.error("Failed to update status text", th);
    }
  }

  public void appendBuildOut(final String str) {
    final var frag = bottomSheetTabAdapter.getBuildOutputFragment();

    if (frag != null) {
      frag.appendOutput(str);
    }
  }

  public AlertDialog getFindInProjectDialog() {
    return mFindInProjectDialog == null ? createFindInProjectDialog() : mFindInProjectDialog;
  }

  public GradleBuildService getBuildService() {
    return mBuildService;
  }

  protected void onProjectInitialized() {
    ProjectManager.INSTANCE.setupProject(mBuildService.projectProxy);
    ProjectManager.INSTANCE.notifyProjectUpdate();

    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread(this::postProjectInit);
  }

  protected void postProjectInit() {
    if (mBinding == null || mViewModel == null) {
      // Activity has been destroyed
      return;
    }

    initialSetup();
    setStatus(getString(R.string.msg_project_initialized));
    mViewModel.isInitializing.setValue(false);

    if (mFindInProjectDialog != null && mFindInProjectDialog.isShowing()) {
      mFindInProjectDialog.dismiss();
    }

    mFindInProjectDialog = null; // Create the dialog again if needed
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ILanguageServerRegistry.getDefault().register(new JavaLanguageServer());
    ILanguageServerRegistry.getDefault().register(new XMLLanguageServer());
    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_PROJECT_PATH)) {
      ProjectManager.INSTANCE.setProjectPath(savedInstanceState.getString(KEY_PROJECT_PATH));
    }

    getLifecycle().addObserver(mLifecycleObserver);

    setSupportActionBar(mBinding.editorToolbar);

    mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);

    mFileTreeFragment = FileTreeFragment.newInstance();
    mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true);

    setupDrawerToggle();
    loadFragment(mFileTreeFragment);

    symbolInput = new SymbolInputView(this);
    mBinding.bottomSheet.textContainer.addView(symbolInput, 0, new ViewGroup.LayoutParams(-1, -2));
    mBinding.tabs.addOnTabSelectedListener(this);

    setupViews();
    createQuickActions();

    mBuildEventListener.setActivity(this);

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

    if (mFileTreeFragment != null) {
      mFileTreeFragment.saveTreeState();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    EditorActivityActions.register(this);

    try {
      if (mFileTreeFragment != null) {
        mFileTreeFragment.listProjectFiles();
      }
    } catch (Throwable th) {
      LOG.error("Failed to update files list", th);
      getApp().toast(R.string.msg_failed_list_files, Toaster.Type.ERROR);
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull final Bundle outState) {
    outState.putString(KEY_PROJECT_PATH, ProjectManager.INSTANCE.getProjectDirPath());
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onBackPressed() {
    if (mBinding.getRoot().isDrawerOpen(GravityCompat.END)) {
      mBinding.getRoot().closeDrawer(GravityCompat.END);
    } else if (mBinding.getRoot().isDrawerOpen(GravityCompat.START)) {
      mBinding.getRoot().closeDrawer(GravityCompat.START);
    } else if (getDaemonStatusFragment().isShowing()) {
      getDaemonStatusFragment().dismiss();
    } else if (mEditorBottomSheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
    } else {
      confirmProjectClose();
    }
  }

  @Override
  protected void onDestroy() {
    isFinishing = true;
    closeProject(false);
    try {
      unregisterReceiver(mLogReceiver);
    } catch (Throwable th) {
      LOG.error("Failed to unregister LogReceiver", th);
    }
    unbindService(mGradleServiceConnection);
    super.onDestroy();
    Lookup.DEFAULT.unregister(BuildService.class);
    mBinding = null;
    mViewModel = null;
  }

  private void preProjectInit() {
    setStatus(getString(R.string.msg_initializing_project));
    mViewModel.isInitializing.setValue(true);
  }

  private void initialSetup() {
    final var openedProject = Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDirPath());
    setLastOpenedProject(openedProject);

    try {
      final var rootProject = ProjectManager.INSTANCE.getRootProject();
      if (rootProject == null) {
        LOG.warn("Project not initialized. Skipping initial setup...");
        return;
      }

      var projectName = rootProject.getName();
      if (projectName.isEmpty()) {
        projectName = new File(ProjectManager.INSTANCE.getProjectDirPath()).getName();
      }

      getSupportActionBar().setSubtitle(projectName);
    } catch (Throwable th) {
      // ignored
    }

    CompletableFuture.runAsync(
        () -> {
          final var resDirs = getResourceDirs();
          resDirs.removeIf(Objects::isNull);
          ValuesTableFactory.setupWithResDirectories(resDirs.toArray(new File[0]));
        });
  }

  private Set<File> getResourceDirs() {
    return ProjectManager.INSTANCE.getApplicationResDirectories();
  }

  @NonNull
  private ArrayList<String> getResourceDirPaths() {
    return getResourceDirs().stream()
        .filter(Objects::nonNull)
        .map(File::getAbsolutePath)
        .collect(Collectors.toCollection(ArrayList::new));
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

  private void toggleProgressBarVisibility(final boolean visible) {
    if (mBinding == null) {
      return;
    }

    mBinding.buildProgressIndicator.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  private void setupViews() {

    mViewModel.progressBarVisible.observe(
        this,
        visible ->
            toggleProgressBarVisibility(
                visible || Boolean.TRUE.equals(mViewModel.isInitializing.getValue())));

    mViewModel.isInitializing.observe(
        this,
        initializing ->
            toggleProgressBarVisibility(
                initializing || Boolean.TRUE.equals(mViewModel.progressBarVisible.getValue())));

    mViewModel.observeFiles(
        this,
        files -> {
          if (mBinding == null) {
            return;
          }

          if (files == null || files.isEmpty()) {
            mBinding.tabs.setVisibility(View.GONE);
            mBinding.viewContainer.setDisplayedChild(1);
          } else {
            mBinding.tabs.setVisibility(View.VISIBLE);
            mBinding.viewContainer.setDisplayedChild(0);
          }
        });

    setupNoEditorView();
    setupBottomSheetPager();
    setupBottomSheet();
    setupBottomSheetTabs();
    setupBottomSheetClearFAB();
    setupBottomSheetShareOutputFAB();

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
  
  private void setupNoEditorView() {
    
    mBinding.noEditorSummary.setMovementMethod(new LinkMovementMethod());
    
    final var filesSpan =
        new ClickableSpan() {
          @Override
          public void onClick(@NonNull final View widget) {
            if (mBinding != null) {
              mBinding.getRoot().openDrawer(GravityCompat.END);
            }
          }
        };

    final var bottomSheetSpan = new ClickableSpan() {
      @Override
      public void onClick(@NonNull final View widget) {
        if (mEditorBottomSheet != null) {
          mEditorBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
      }
    };
    
    final var sb = new SpannableStringBuilder();
    appendClickableSpan(sb, R.string.msg_swipe_for_files, filesSpan);
    appendClickableSpan(sb, R.string.msg_swipe_for_output, bottomSheetSpan);
    mBinding.noEditorSummary.setText(sb);
  }
  
  private void appendClickableSpan(final SpannableStringBuilder sb, @StringRes final int textRes, final ClickableSpan span) {
    final var str = getString(textRes);
    final var split = str.split("@@",3);
    
    if (split.length != 3) {
      // Not a valid format
      sb.append(str);
      sb.append('\n');
      return;
    }
    
    sb.append(split[0]);
    sb.append(split[1], span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    sb.append(split[2]);
    sb.append('\n');
  }
  
  private void setupBottomSheetShareOutputFAB() {
    mBinding.bottomSheet.shareOutputFab.setOnClickListener(
        v -> {
          final var fragment =
              bottomSheetTabAdapter.getFragmentAtIndex(
                  mBinding.bottomSheet.tabs.getSelectedTabPosition());

          if (!(fragment instanceof ShareableOutputFragment)) {
            LOG.error("Unknown fragment:", fragment);
            return;
          }

          final var outputFragment = (ShareableOutputFragment) fragment;

          final var filename = outputFragment.getFilename();
          //noinspection deprecation
          final var progress =
              ProgressDialog.show(EditorActivity.this, null, getString(R.string.please_wait));
          TaskExecutor.executeAsync(
              outputFragment::getContent,
              text -> {
                progress.dismiss();
                shareText(text, filename);
              });
        });
  }

  private void setupBottomSheetClearFAB() {
    TooltipCompat.setTooltipText(
        mBinding.bottomSheet.clearFab, getString(R.string.title_clear_output));
    mBinding.bottomSheet.clearFab.setOnClickListener(
        v -> {
          final var fragment =
              bottomSheetTabAdapter.getFragmentAtIndex(
                  mBinding.bottomSheet.tabs.getSelectedTabPosition());

          if (!(fragment instanceof ShareableOutputFragment)) {
            LOG.error("Unknown fragment:", fragment);
            return;
          }

          ((ShareableOutputFragment) fragment).clearOutput();
        });
  }

  private void setupBottomSheetTabs() {
    mBinding.bottomSheet.tabs.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {

          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            final var fragment = bottomSheetTabAdapter.getFragmentAtIndex(tab.getPosition());
            if (fragment instanceof ShareableOutputFragment) {
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
  }

  private void setupBottomSheet() {
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
  }

  private void setupBottomSheetPager() {
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
  }

  @SuppressWarnings("deprecation")
  private void shareText(String text, String type) {
    if (TextUtils.isEmpty(text)) {
      getApp().toast(getString(R.string.msg_output_text_extraction_failed), Toaster.Type.ERROR);
      return;
    }

    final var pd = ProgressDialog.show(this, null, getString(R.string.please_wait), true, false);
    TaskExecutor.executeAsyncProvideError(
        () -> writeTempFile(text, type),
        (result, error) -> {
          pd.dismiss();
          if (result == null || error != null) {
            LOG.warn("Unable to share output", error);
            return;
          }

          shareFile(result);
        });
  }

  private void shareFile(File file) {
    IntentUtils.shareFile(this, file, "text/plain");
  }

  @NonNull
  private File writeTempFile(String text, String type) {
    // use a common name to avoid multiple files
    final var file = getFilesDir().toPath().resolve(type + ".txt");
    try {
      if (Files.exists(file)) {
        Files.delete(file);
      }

      Files.write(
          file,
          text.getBytes(StandardCharsets.UTF_8),
          StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE);
    } catch (IOException e) {
      LOG.error("Unable to write output to file", e);
    }
    return file.toFile();
  }

  private void notifyFilesUnsaved(List<CodeEditorView> unsavedEditors, Runnable invokeAfter) {

    if (isFinishing) {
      // Do not show unsaved files dialog if the activity is being destroyed
      // TODO Use a service to save files and to avoid file content loss
      for (final CodeEditorView editor : unsavedEditors) {
        editor.markUnmodified();
      }
      invokeAfter.run();
      return;
    }

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

    connectLSPClient();
  }

  private void connectLSPClient() {
    final var client = IDELanguageClientImpl.getInstance();
    final var registry = ILanguageServerRegistry.getDefault();
    registry.connectClient(client);
  }

  private void stopServices() {

    if (IDELanguageClientImpl.isInitialized()) {
      IDELanguageClientImpl.shutdown();
    }

    shutdownLanguageServers();
  }

  private void shutdownLanguageServers() {
    ILanguageServerRegistry.getDefault().destroy();
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
    // This will make sure that file contents are not erased.
    closeAll(
        () -> {
          setLastOpenedProject(NO_OPENED_PROJECT);
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
          rootProject.getSubModules().stream()
              .map(Project::getProjectDir)
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
          for (int i = 0; i < binding.modulesContainer.getChildCount(); i++) {
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
          if (EditorPreferencesKt.getAutoSave()) {
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
