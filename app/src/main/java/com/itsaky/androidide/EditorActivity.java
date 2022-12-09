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
import static com.itsaky.androidide.R.drawable;
import static com.itsaky.androidide.R.string;
import static com.itsaky.androidide.preferences.internal.GeneralPreferencesKt.NO_OPENED_PROJECT;
import static com.itsaky.androidide.preferences.internal.GeneralPreferencesKt.setLastOpenedProject;
import static com.itsaky.toaster.ToastUtilsKt.toast;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInstaller;
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
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.itsaky.androidide.actions.ActionData;
import com.itsaky.androidide.actions.ActionItem;
import com.itsaky.androidide.actions.ActionsRegistry;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.app.IDEActivity;
import com.itsaky.androidide.databinding.ActivityEditorBinding;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding;
import com.itsaky.androidide.events.InstallationResultEvent;
import com.itsaky.androidide.fragments.FileTreeFragment;
import com.itsaky.androidide.fragments.SearchResultFragment;
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
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.Project;
import com.itsaky.androidide.projects.builder.BuildService;
import com.itsaky.androidide.services.GradleBuildService;
import com.itsaky.androidide.services.LogReceiver;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.ui.EditorBottomSheet;
import com.itsaky.androidide.ui.MaterialBanner;
import com.itsaky.androidide.ui.editor.CodeEditorView;
import com.itsaky.androidide.ui.editor.IDEEditor;
import com.itsaky.androidide.uidesigner.UIDesignerActivity;
import com.itsaky.androidide.utils.ActionMenuUtils;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.EditorActivityActions;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.InstallationResultHandler;
import com.itsaky.androidide.utils.IntentUtils;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.RecursiveFileSearcher;
import com.itsaky.androidide.utils.ResourceUtilsKt;
import com.itsaky.androidide.utils.SingleSessionCallback;
import com.itsaky.androidide.viewmodel.EditorViewModel;
import com.itsaky.androidide.xml.resources.ResourceTableRegistry;
import com.itsaky.androidide.xml.versions.ApiVersionsRegistry;
import com.itsaky.androidide.xml.widgets.WidgetTableRegistry;
import com.itsaky.toaster.Toaster;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import io.github.rosemoe.sora.event.ContentChangeEvent;
import io.github.rosemoe.sora.event.Unsubscribe;
import kotlin.Unit;

public class EditorActivity extends IDEActivity
    implements TabLayout.OnTabSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        DiagnosticClickListener,
        EditorActivityProvider {

  public static final String KEY_BOTTOM_SHEET_SHOWN = "editor_bottomSheetShown";
  private static final float EDITOR_CONTAINER_SCALE_FACTOR = 0.87f;
  private static final String KEY_PROJECT_PATH = "saved_projectPath";
  private static final ILogger LOG = ILogger.newInstance("EditorActivity");
  private final EditorEventListener mBuildEventListener = new EditorEventListener();
  private final EditorActivityLifecyclerObserver mLifecycleObserver =
      new EditorActivityLifecyclerObserver();

  private ActivityEditorBinding mBinding;
  private LayoutDiagnosticInfoBinding mDiagnosticInfoBinding;
  private final LogReceiver mLogReceiver = new LogReceiver().setLogListener(this::appendApkLog);
  private FileTreeFragment mFileTreeFragment;
  private TextSheetFragment mDaemonStatusFragment;
  private ProgressSheet mSearchingProgress;
  private AlertDialog mFindInProjectDialog;
  private BottomSheetBehavior<? extends View> mEditorBottomSheet;
  private EditorViewModel mViewModel;
  private final ServiceConnection mGradleServiceConnection =
      new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
          final var buildService = ((GradleBuildService.GradleServiceBinder) service).getService();
          LOG.info("Gradle build service has been started...");
          Lookup.DEFAULT.update(BuildService.KEY_BUILD_SERVICE, buildService);
          buildService
              .setEventListener(mBuildEventListener)
              .startToolingServer(() -> initializeProject());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
          LOG.info("Disconnected from Gradle build service...");
        }
      };

  @Subscribe(threadMode = ThreadMode.MAIN)
  @SuppressWarnings("unused")
  public void onInstallationResult(InstallationResultEvent event) {
    final var intent = event.getIntent();
    if (mBinding == null || isFinishing) {
      return;
    }
    final var packageName = InstallationResultHandler.onResult(this, intent);
    if (packageName != null) {
      Snackbar.make(
              mBinding.realContainer, string.msg_action_open_application, Snackbar.LENGTH_LONG)
          .setAction(
              string.yes,
              v -> {
                final var manager = getPackageManager();
                final var launchIntent = manager.getLaunchIntentForPackage(packageName);
                if (launchIntent != null) {
                  startActivity(launchIntent);
                }
              })
          .show();
    }
  }

  private final OnBackPressedCallback onBackPressedCallback =
      new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
          if (mBinding.getRoot().isDrawerOpen(GravityCompat.END)) {
            mBinding.getRoot().closeDrawer(GravityCompat.END);
          } else if (mBinding.getRoot().isDrawerOpen(GravityCompat.START)) {
            mBinding.getRoot().closeDrawer(GravityCompat.START);
          } else if (getDaemonStatusFragment().isShowing()) {
            getDaemonStatusFragment().dismiss();
          } else if (mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            mEditorBottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
          } else {
            confirmProjectClose();
          }
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

    ActionsRegistry.getInstance().fillMenu(data, ActionItem.Location.EDITOR_TOOLBAR, menu);
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
    if (mBinding != null) {
      return (CodeEditorView) mBinding.editorContainer.getChildAt(index);
    }
    return null;
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
    if (mBinding != null) {
      mBinding.bottomSheet.appendApkLog(line);
    }
  }

  public void showDaemonStatus() {
    ShellServer shell = getApp().newShell(t -> getDaemonStatusFragment().append(t));
    shell.bgAppend(String.format("echo '%s'", getString(string.msg_getting_daemom_status)));
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
                .setTitleText(string.gradle_daemon_status)
        : mDaemonStatusFragment;
  }

  public void showSearchResults() {
    if (mEditorBottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
      mEditorBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    final int index =
        mBinding
            .bottomSheet
            .getPagerAdapter()
            .findIndexOfFragmentByClass(SearchResultFragment.class);
    if (index >= 0 && index < mBinding.bottomSheet.binding.tabs.getTabCount()) {
      final var tab = mBinding.bottomSheet.binding.tabs.getTabAt(index);
      if (tab != null) {
        tab.select();
      }
    }
  }

  public void handleDiagnosticsResultVisibility(boolean errorVisible) {
    if (mBinding != null) {
      mBinding.bottomSheet.handleDiagnosticsResultVisibility(errorVisible);
    }
  }

  public void handleSearchResultVisibility(boolean errorVisible) {
    if (mBinding != null) {
      mBinding.bottomSheet.handleSearchResultVisibility(errorVisible);
    }
  }

  public void showFirstBuildNotice() {
    DialogUtils.newMaterialDialogBuilder(this)
        .setPositiveButton(android.R.string.ok, null)
        .setTitle(string.title_first_build)
        .setMessage(string.msg_first_build)
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
    if (mBinding == null) {
      return null;
    }

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

    if (mBinding == null) {
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
    if (mBinding == null) {
      return -1;
    }
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
      invalidateOptionsMenu();
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

  public FileTreeFragment getFileTreeFragment() {
    if (mFileTreeFragment == null) {
      mFileTreeFragment =
          (FileTreeFragment) getSupportFragmentManager().findFragmentByTag(FileTreeFragment.TAG);
    }
    return mFileTreeFragment;
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
    invalidateOptionsMenu();
  }

  @Override
  public void onTabUnselected(@NonNull TabLayout.Tab tab) {}

  @Override
  public void onTabReselected(@NonNull TabLayout.Tab tab) {
    ActionMenuUtils.createMenu(this, tab.view, ActionItem.Location.EDITOR_FILE_TABS, true).show();
  }

  private void refreshSymbolInput(@NonNull CodeEditorView editor) {
    mBinding.bottomSheet.refreshSymbolInput(editor);
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem p1) {
    final int id = p1.getItemId();
    if (id == R.id.editornav_discuss) {
      getApp().openTelegramGroup();
    } else if (id == R.id.editornav_channel) {
      getApp().openTelegramChannel();
    } else if (id == R.id.editornav_suggest) {
      getApp().openGitHub();
    } else if (id == R.id.editornav_needHelp) {
      showNeedHelpDialog();
    } else if (id == R.id.editornav_settings) {
      startActivity(new Intent(this, PreferencesActivity.class));
    } else if (id == R.id.editornav_share) {
      startActivity(getShareTextIntent(getString(string.msg_share_app)));
    } else if (id == R.id.editornav_close_project) {
      confirmProjectClose();
    } else if (id == R.id.editornav_terminal) {
      openTerminal();
    }
    mBinding.getRoot().closeDrawer(GravityCompat.START);
    return false;
  }

  public ProgressSheet getProgressSheet(int msg) {
    if (mSearchingProgress != null && mSearchingProgress.isShowing()) {
      mSearchingProgress.dismiss();
    }
    mSearchingProgress = new ProgressSheet();
    mSearchingProgress.setCancelable(false);
    mSearchingProgress.setMessage(getString(msg));
    mSearchingProgress.setSubMessageEnabled(false);
    return mSearchingProgress;
  }

  public void setDiagnosticsAdapter(@NonNull final DiagnosticsAdapter adapter) {
    if (mBinding != null) {
      mBinding.bottomSheet.setDiagnosticsAdapter(adapter);
    }
  }

  public void setSearchResultAdapter(@NonNull final SearchListAdapter adapter) {
    if (mBinding != null) {
      mBinding.bottomSheet.setSearchResultAdapter(adapter);
    }
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
        editor.getEditor().notifyClose();
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
          editor.getEditor().notifyClose();
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

  public void previewLayout(File file) {
    final var intent = new Intent(this, UIDesignerActivity.class);
    intent.putExtra(UIDesignerActivity.EXTRA_FILE, file.getAbsolutePath());
    startActivity(intent);
  }

  public boolean saveAll(boolean notify) {
    return saveAll(notify, false);
  }

  public boolean saveAll(boolean notify, boolean canProcessResources) {
    SaveResult result = saveAllResult();

    if (notify) {
      toast(string.all_saved, Toaster.Type.SUCCESS);
    }

    if (result.gradleSaved) {
      notifySyncNeeded();
    }

    if (canProcessResources) {
      ProjectManager.INSTANCE.generateSources();
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

      final boolean isGradle =
          frag.getFile().getName().endsWith(".gradle")
              || frag.getFile().getName().endsWith(".gradle.kts");
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
    final var buildService = Lookup.DEFAULT.lookup(BuildService.KEY_BUILD_SERVICE);
    if (buildService != null && !buildService.isBuildInProgress()) {
      getSyncBanner()
          .setNegative(android.R.string.cancel, null)
          .setPositive(android.R.string.ok, v -> initializeProject())
          .show();
    }
  }

  public MaterialBanner getSyncBanner() {
    return mBinding
        .syncBanner
        .setContentTextColor(ResourceUtilsKt.resolveAttr(this, R.attr.colorOnPrimaryContainer))
        .setBannerBackgroundColor(ResourceUtilsKt.resolveAttr(this, R.attr.colorPrimaryContainer))
        .setButtonTextColor(ResourceUtilsKt.resolveAttr(this, R.attr.colorOnPrimaryContainer))
        .setIcon(drawable.ic_sync)
        .setContentText(string.msg_sync_needed);
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

    final var buildService = Lookup.DEFAULT.lookup(BuildService.KEY_BUILD_SERVICE);
    if (buildService == null) {
      LOG.error("No build service found. Cannot initialize project.");
      return;
    }

    final var future = buildService.initializeProject(projectDir.getAbsolutePath());
    future.whenCompleteAsync(
        (result, error) -> {
          if (result == null || error != null) {
            LOG.error("An error occurred initializing the project with Tooling API", error);
            setStatus(getString(string.msg_project_initialization_failed));
            return;
          }

          onProjectInitialized();
        });
  }

  public void setStatus(final CharSequence text) {
    setStatus(text, Gravity.CENTER);
  }

  public void setStatus(final CharSequence text, @GravityInt int gravity) {
    if (mBinding != null) {
      mBinding.bottomSheet.setStatus(text, gravity);
    }
  }

  public void appendBuildOut(final String str) {
    if (mBinding != null) {
      mBinding.bottomSheet.appendBuildOut(str);
    }
  }

  public AlertDialog getFindInProjectDialog() {
    return mFindInProjectDialog == null ? createFindInProjectDialog() : mFindInProjectDialog;
  }

  protected void onProjectInitialized() {
    ProjectManager.INSTANCE.setupProject();
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
    setStatus(getString(string.msg_project_initialized));
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

    getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    getLifecycle().addObserver(mLifecycleObserver);

    setSupportActionBar(mBinding.editorToolbar);

    mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
    mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true);

    setupDrawerToggle();
    mBinding.tabs.addOnTabSelectedListener(this);

    setupViews();

    mBuildEventListener.setActivity(this);

    startServices();

    KeyboardUtils.registerSoftInputChangedListener(this, __ -> onSoftInputChanged());
    registerLogReceiver();
    setupContainers();
    setupDiagnosticInfo();

    EditorActivityActions.register(this);
  }

  @Override
  @NonNull
  protected View bindLayout() {
    mBinding = ActivityEditorBinding.inflate(getLayoutInflater());
    mDiagnosticInfoBinding = mBinding.diagnosticInfo;
    return mBinding.getRoot();
  }

  @Override
  protected void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    final var frag = getFileTreeFragment();
    if (frag != null) {
      frag.saveTreeState();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Actions are cleared when the activity is paused to avoid holding references to the activity
    // So, when resumed, they should be registered and inflated again.
    EditorActivityActions.register(this);
    invalidateOptionsMenu();

    try {
      final var frag = getFileTreeFragment();
      if (frag != null) {
        frag.listProjectFiles();
      }
    } catch (Throwable th) {
      LOG.error("Failed to update files list", th);
      toast(string.msg_failed_list_files, Toaster.Type.ERROR);
    }
  }

  @Override
  protected void onSaveInstanceState(@NonNull final Bundle outState) {
    outState.putString(KEY_PROJECT_PATH, ProjectManager.INSTANCE.getProjectDirPath());
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    isFinishing = true;
    closeProject(false);
    try {
      unregisterReceiver(mLogReceiver);
    } catch (Throwable th) {
      LOG.error("Failed to release resources", th);
    }
    unbindService(mGradleServiceConnection);
    super.onDestroy();
    Lookup.DEFAULT.unregisterAll();
    ApiVersionsRegistry.getInstance().clear();
    ResourceTableRegistry.getInstance().clear();
    WidgetTableRegistry.getInstance().clear();
    mBinding = null;
    mViewModel = null;
  }

  public PackageInstaller.SessionCallback installationSessionCallback() {
    return new SingleSessionCallback() {

      @Override
      public void onCreated(final int sessionId) {
        LOG.debug("on session created:", sessionId);
        if (mBinding != null) {
          mBinding.bottomSheet.setActionText(getString(string.msg_installing_apk));
          mBinding.bottomSheet.setActionProgress(0);
          mBinding.bottomSheet.showChild(EditorBottomSheet.CHILD_ACTION);
        }
      }

      @Override
      public void onProgressChanged(final int sessionId, final float progress) {
        if (mBinding != null) {
          mBinding.bottomSheet.setActionProgress((int) (progress * 100f));
        }
      }

      @Override
      public void onFinished(final int sessionId, final boolean success) {
        if (mBinding != null) {
          mBinding.bottomSheet.showChild(EditorBottomSheet.CHILD_HEADER);
          mBinding.bottomSheet.setActionProgress(0);
          if (!success) {
            Snackbar.make(
                    mBinding.realContainer, string.title_installation_failed, Snackbar.LENGTH_LONG)
                .show();
          }
        }
      }
    };
  }

  private void preProjectInit() {
    setStatus(getString(string.msg_initializing_project));
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
  }

  private void setupDrawerToggle() {
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            mBinding.editorDrawerLayout,
            mBinding.editorToolbar,
            string.app_name,
            string.app_name);
    mBinding.editorDrawerLayout.addDrawerListener(toggle);
    mBinding.startNav.setNavigationItemSelectedListener(this);
    toggle.syncState();

    mBinding.editorDrawerLayout.setChildId(mBinding.realContainer.getId());
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
    setupBottomSheet();

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

    final var bottomSheetSpan =
        new ClickableSpan() {
          @Override
          public void onClick(@NonNull final View widget) {
            if (mEditorBottomSheet != null) {
              mEditorBottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
          }
        };

    final var sb = new SpannableStringBuilder();
    appendClickableSpan(sb, string.msg_swipe_for_files, filesSpan);
    appendClickableSpan(sb, string.msg_swipe_for_output, bottomSheetSpan);
    mBinding.noEditorSummary.setText(sb);
  }

  private void appendClickableSpan(
      final SpannableStringBuilder sb, @StringRes final int textRes, final ClickableSpan span) {
    final var str = getString(textRes);
    final var split = str.split("@@", 3);

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

  private void setupBottomSheet() {
    mEditorBottomSheet = BottomSheetBehavior.from(mBinding.bottomSheet);
    mEditorBottomSheet.addBottomSheetCallback(
        new BottomSheetBehavior.BottomSheetCallback() {
          @Override
          public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
              final var editor = getCurrentEditor();
              if (editor != null && editor.getEditor() != null) {
                editor.getEditor().ensureWindowsDismissed();
              }
            }
          }

          @Override
          public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            mBinding.bottomSheet.onSlide(slideOffset);
            final var editorScale = 1 - (slideOffset * (1 - EDITOR_CONTAINER_SCALE_FACTOR));
            mBinding.viewContainer.setScaleX(editorScale);
            mBinding.viewContainer.setScaleY(editorScale);
          }
        });

    final var observer =
        new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            mBinding.viewContainer.setPivotY(0f);
            mBinding.viewContainer.setPivotX(mBinding.viewContainer.getWidth() / 2f);
            mBinding.viewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        };
    mBinding.viewContainer.getViewTreeObserver().addOnGlobalLayoutListener(observer);
    mBinding.bottomSheet.setOffsetAnchor(mBinding.editorToolbar);
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
            getString(string.title_files_unsaved), // title
            getString(string.msg_files_unsaved, TextUtils.join("\n", mapped)), // message
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
    try {
      if (IDELanguageClientImpl.isInitialized()) {
        IDELanguageClientImpl.shutdown();
      }
      shutdownLanguageServers();
    } catch (Throwable err) {
      LOG.error("Unable to stop editor services. Please report this issue.", err);
    }
  }

  private void shutdownLanguageServers() {
    ILanguageServerRegistry.getDefault().destroy();
  }

  private void onSoftInputChanged() {
    invalidateOptionsMenu();
    if (mBinding != null) {
      mBinding.bottomSheet.onSoftInputChanged();
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
    builder.setTitle(string.title_confirm_project_close);
    builder.setMessage(string.msg_confirm_project_close);
    builder.setNegativeButton(string.no, null);
    builder.setPositiveButton(
        string.yes,
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
      toast(getString(string.msg_project_not_initialized), Toaster.Type.ERROR);
      return null;
    }

    List<File> moduleDirs;
    try {
      moduleDirs =
          rootProject.getSubModules().stream()
              .map(Project::getProjectDir)
              .collect(Collectors.toList());
    } catch (Throwable e) {
      toast(getString(string.msg_no_modules), Toaster.Type.ERROR);
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
    builder.setTitle(string.menu_find_project);
    builder.setView(binding.getRoot());
    builder.setCancelable(false);
    builder.setPositiveButton(
        string.menu_find,
        (dialog, which) -> {
          final String text =
              Objects.requireNonNull(binding.input.getEditText()).getText().toString().trim();
          if (text.isEmpty()) {
            toast(string.msg_empty_search_query, Toaster.Type.ERROR);
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
            toast(string.msg_select_search_modules, Toaster.Type.ERROR);
          } else {
            dialog.dismiss();
            getProgressSheet(string.msg_searching_project)
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
    builder.setTitle(string.need_help);
    builder.setMessage(string.msg_need_help);
    builder.setPositiveButton(android.R.string.ok, null);
    builder.create().show();
  }
}
