package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.TransitionManager;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.EditorPagerAdapter;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityEditorBinding;
import com.itsaky.androidide.databinding.LayoutCreateFileJavaBinding;
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.fragments.FileTreeFragment;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.interfaces.JLSRequestor;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.androidide.language.logs.LogLanguageImpl;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.models.SheetOption;
import com.itsaky.androidide.receivers.LogReceiver;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.PreferenceManager;
import com.itsaky.androidide.utils.ProjectWriter;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.utils.VersionedFileManager;
import com.itsaky.androidide.views.MaterialBanner;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.DidCloseTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.JavaReportProgressParams;
import com.itsaky.lsp.JavaStartProgressParams;
import com.itsaky.lsp.LanguageClient;
import com.itsaky.lsp.Message;
import com.itsaky.lsp.PublishDiagnosticsParams;
import com.itsaky.lsp.ShowMessageParams;
import com.itsaky.lsp.TextDocumentItem;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import com.itsaky.lsp.DidSaveTextDocumentParams;
import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.TextDocumentIdentifier;
import com.itsaky.lsp.JavaColors;

public class EditorActivity extends StudioActivity implements FileTreeFragment.FileActionListener,
														IDEService.BuildListener,
														TabLayout.OnTabSelectedListener,
														NavigationView.OnNavigationItemSelectedListener,
                                                        DiagnosticClickListener, 
                                                        EditorFragment.FileOpenListener,
                                                        LanguageClient, CodeEditor.CursorChangeListener,
                                                        JLSRequestor {
    
    private ActivityEditorBinding mBinding;
	private EditorPagerAdapter mPagerAdapter;
	private FileTreeFragment mFileTreeFragment;
	private EditorFragment mCurrentFragment;
	private static AndroidProject mProject;
	public static File mCurrentFile;
	private TreeNode mLastHolded;
	private CodeEditor buildView;
	private CodeEditor logView;
    private RecyclerView diagnosticList;
	
	private LogLanguageImpl mLogLanguageImpl;
	
	private QuickAction mTabCloseAction;
	private TextSheetFragment mDaemonStatusFragment;
	private OptionsListFragment mFileOptionsFragment;
	
	private boolean recreateCompletionServices = false;
    
    private final Map<File, List<Diagnostic>> diagnostics = new HashMap<>();
	private final Stack<Message> pendingMessages = new Stack<>();
    
	private final String RES_PATH_REGEX = "/.*/src/.*/res";
	private final String LAYOUTRES_PATH_REGEX = "/.*/src/.*/res/layout";
	private final String MENURES_PATH_REGEX = "/.*/src/.*/res/menu";
	private final String DRAWABLERES_PATH_REGEX = "/.*/src/.*/res/drawable";
	private final String JAVA_PATH_REGEX = "/.*/src/.*/java";
	
    private static final Gson gson = new Gson();
	private final IDEService.BuildListener mBuildListener = this;
	private static final String TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment";
    public static final String TAG = "EditorActivity";
	
	private static final int ACTIONID_CLOSE = 100;
	private static final int ACTIONID_OTHERS = 101;
	private static final int ACTIONID_ALL = 102;
    
	private LogReceiver mLogReceiver = new LogReceiver().setLogListener(line -> appendApkLog(line));
	private BroadcastReceiver mServiceStartReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context p1, Intent p2) {
			unregisterReceiver(this);
			getApp().storeBuildServiceInstance(mBuildListener);
			if (getApp().isStopGradleDaemon() && getBuildService() != null) getBuildService().stopAllDaemons();
			getBuildService().setModule(mProject.getMainModulePath());
			preBuild();
			invalidateOptionsMenu();
		}
	};
	
	private final int[] BUILD_IDS = {
		R.id.menuEditor_quickRun,
		R.id.menuEditor_runDebug,
		R.id.menuEditor_runRelease,
		R.id.menuEditor_runBuild,
		R.id.menuEditor_runBundle,
		R.id.menuEditor_runClean,
		R.id.menuEditor_runCleanBuild,
		R.id.menuEditor_runRecreate,
		R.id.menuEditor_lint,
		R.id.menuEditor_lintDebug,
		R.id.menuEditor_lintRelease
	};
	

	@Override
	protected View bindLayout() {
		mBinding = ActivityEditorBinding.inflate(getLayoutInflater());
		return mBinding.getRoot();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setSupportActionBar(mBinding.editorToolbar);
		
		this.mProject = getIntent().getParcelableExtra("project");
		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), this.mProject);
		mFileTreeFragment = FileTreeFragment.newInstance(this.mProject).setFileActionListener(this);
		mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.editorDrawerLayout, mBinding.editorToolbar, R.string.app_name, R.string.app_name);
		mBinding.editorDrawerLayout.setDrawerListener(toggle);
		mBinding.startNav.setNavigationItemSelectedListener(this);
		toggle.syncState();
		loadFragment(mFileTreeFragment);
		
		createQuickActions();
		
		mBinding.editorViewPager.setOffscreenPageLimit(9);
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.setOnTabSelectedListener(this);
		mBinding.fabView.setOnClickListener(v -> showViewOptions());
		
		getApp().checkAndUpdateGradle();
		if (getBuildService() == null) {
			registerServiceReceiver();
			getApp().startBuildService();
		} else {
			if (getApp().isStopGradleDaemon() && getBuildService() != null) getBuildService().stopAllDaemons();
			getBuildService().setModule(mProject.getMainModulePath());
			getBuildService().setListener(this);
			preBuild();
			invalidateOptionsMenu();
		}
		
		KeyboardUtils.registerSoftInputChangedListener(this, __ -> onSoftInputChanged());
		registerLogReceiver();
		setupContainers();
    }

	private void setupContainers() {
		if(getBuildView().getParent() != null && getBuildView().getParent() instanceof ViewGroup) {
			((ViewGroup) getBuildView().getParent()).removeView(getBuildView());
		}
		
		if(getLogView().getParent() != null && getLogView().getParent() instanceof ViewGroup) {
			((ViewGroup) getLogView().getParent()).removeView(getLogView());
		}
        
        if(getDiagnosticsList().getParent() != null && getDiagnosticsList().getParent() instanceof ViewGroup) {
            ((ViewGroup) getDiagnosticsList().getParent()).removeView(getDiagnosticsList());
		}
		
		mBinding.buildOutcontainer.addView(getBuildView(), new ViewGroup.LayoutParams(-1, -1));
		mBinding.buildContainer.setVisibility(View.GONE);
		mBinding.buildOutToolbar.setNavigationOnClickListener(v -> hideBuildResult());
		mBinding.buildOutToolbar.setOnClickListener(v -> hideBuildResult());
		
		mBinding.logOutcontainer.addView(getLogView(), new ViewGroup.LayoutParams(-1, -1));
		mBinding.logContainer.setVisibility(View.GONE);
		mBinding.logOutToolbar.setNavigationOnClickListener(v -> hideLogResult());
		mBinding.logOutToolbar.setOnClickListener(v -> hideLogResult());
        
        mBinding.diagListContainer.addView(getDiagnosticsList(), new ViewGroup.LayoutParams(-1, -1));
        mBinding.diagContainer.setVisibility(View.GONE);
        mBinding.diagToolbar.setNavigationOnClickListener(v -> hideDiagnostics());
		mBinding.diagToolbar.setOnClickListener(v -> hideDiagnostics());
		
		mBinding.viewOptionsCard.setVisibility(View.GONE);
		mBinding.transformScrim.setVisibility(View.GONE);
		
		mBinding.transformScrim.setOnClickListener(v -> hideViewOptions());
		mBinding.viewBuildOut.setOnClickListener(v -> showBuildResult());
        mBinding.viewDiags.setOnClickListener(v -> showDiagnostics());
		mBinding.viewLogs.setOnClickListener(v -> showLogResult());
		mBinding.viewFiles.setOnClickListener(v -> showFiles());
		mBinding.viewDaemonStatus.setOnClickListener(v -> showDaemonStatus());
	}
	
	private void onSoftInputChanged() {
		invalidateOptionsMenu();
		if(KeyboardUtils.isSoftInputVisible(this)) {
			TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.TOP));
			mBinding.symbolInput.setVisibility(View.VISIBLE);
			mBinding.fabView.hide();
		} else {
			TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.BOTTOM));
			mBinding.symbolInput.setVisibility(View.GONE);
			mBinding.fabView.show();
		}
	}

	private void refreshSymbolInput(EditorFragment frag) {
		mBinding.symbolInput.bindEditor(frag.binding.editorCodeEditor);
		mBinding.symbolInput.setSymbols(Symbols.forFile(frag.getFile()));
	}
	
	private void notifySyncNeeded() {
		if(getBuildService() != null && !getBuildService().isBuilding()) {
			getSyncBanner()
				.setNegative(android.R.string.cancel, null)
				.setPositive(android.R.string.ok, v -> {
					getBuildService().assembleDebug(false);
				})
				.show();
		}
	}

	@Override
	public void onBackPressed() {
		if(mBinding.getRoot().isDrawerOpen(GravityCompat.END)) {
			mBinding.getRoot().closeDrawer(GravityCompat.END);
		} else if(mBinding.getRoot().isDrawerOpen(GravityCompat.START)) {
			mBinding.getRoot().closeDrawer(GravityCompat.START);
		} else if(getDaemonStatusFragment().isShowing()) {
			getDaemonStatusFragment().dismiss();
		} else if(mBinding.buildContainer.getVisibility() == View.VISIBLE) {
            hideBuildResult();
        } else if(mBinding.logContainer.getVisibility() == View.VISIBLE) {
            hideLogResult();
        } else if(mBinding.viewOptionsCard.getVisibility() == View.VISIBLE) {
            hideViewOptions();
		} else if(mBinding.diagContainer.getVisibility() == View.VISIBLE) {
            hideDiagnostics();
		} else if(mFileOptionsFragment != null && mFileOptionsFragment.isShowing()) {
			mFileOptionsFragment.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			mFileTreeFragment.listProjectFiles(); 
		 } catch (Throwable th) {
			 getApp().toast(R.string.msg_failed_list_files, Toaster.Type.ERROR);
		 }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getBuildService() != null) {
			getBuildService().setListener(null);
		}
		try {
			unregisterReceiver(mServiceStartReceiver);
			unregisterReceiver(mLogReceiver);
		} catch (Throwable th) {}
	}

	@Override
	@SuppressLint("AlwaysShowAction")
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int id : BUILD_IDS) {
			MenuItem item = menu.findItem(id);
			if (item != null) {
				item.setEnabled(getBuildService() != null && !getBuildService().isBuilding());
			}
		}
		MenuItem run1 = menu.findItem(R.id.menuEditor_quickRun);
		MenuItem run2 = menu.findItem(R.id.menuEditor_run);
		MenuItem undo = menu.findItem(R.id.menuEditor_undo);
		MenuItem redo = menu.findItem(R.id.menuEditor_redo);
		if(KeyboardUtils.isSoftInputVisible(this)) {
			run1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			run2.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			undo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			redo.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else {
			run1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			run2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			undo.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			redo.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_editor, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (getBuildService() == null) return false;
		int id = item.getItemId();
		if (id == R.id.menuEditor_runDebug || id == R.id.menuEditor_quickRun) {
			getBuildService().assembleDebug(true);
		} else if (id == R.id.menuEditor_runRelease) {
			getBuildService().assembleRelease();
		} else if (id == R.id.menuEditor_runClean) {
			getBuildService().clean();
		} else if (id == R.id.menuEditor_runCleanBuild) {
			getBuildService().cleanAndRebuild();
		} else if (id == R.id.menuEditor_runStopDaemons) {
			getBuildService().stopAllDaemons();
		} else if (id == R.id.menuEditor_runBuild) {
			getBuildService().build();
		} else if (id == R.id.menuEditor_runBundle) {
			getBuildService().bundle();
		} else if (id == R.id.menuEditor_lint) {
			getBuildService().lint();
		} else if (id == R.id.menuEditor_lintDebug) {
			getBuildService().lintDebug();
		} else if (id == R.id.menuEditor_lintRelease) {
			getBuildService().lintRelease();
		} else if (id == R.id.menuEditor_save) {
			saveAll();
		} else if (id == R.id.menuEditor_undo && this.mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
			this.mCurrentFragment.undo();
		} else if (id == R.id.menuEditor_redo && this.mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
			this.mCurrentFragment.redo();
		} else if(id == R.id.menuEditor_runRecreate) {
			getBuildService().recreateServices();
		}
		invalidateOptionsMenu();
		return true;
	}
	
	/************************************
	 *   Build Related                  *
	 ************************************/
	 
	private void appendApkLog(LogLine line) {
		getLogLanguage().addLine(line);
		appendLogOut(line.toString());
	}
	
	@Override
	public void appendOutput(GradleTask task, CharSequence text) {
		if(text == null)
			return;
		if(task != null && !task.canOutput()) {
			return;
		}
		appendBuildOut(text.toString());
	}

	@Override
	public void showPreparing() {
		boolean isFirstBuild = getApp().getPrefManager().getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true);
		setStatus(getString(isFirstBuild ? R.string.preparing_first : R.string.preparing));
		if(isFirstBuild) {
			showFirstBuildNotice();
		}
	}

	@Override
	public void onStartingGradleDaemon(GradleTask task) {
		setStatus(getString(R.string.msg_starting_daemon));
		getApp().setStopGradleDaemon(false);
	}

	@Override
	public void onRunTask(String taskName, GradleTask task) {
		if(task != null && task.canOutput()) {
			setStatus(getBuildService().typeString(task.getTaskID()) + " " + taskName);
		}
	}

	@Override
	public void onBuildSuccessful(String msg, GradleTask task) {
		if(task == null) return;
		if(task.canOutput())
			setStatus(msg);
			
		if(task.getType() == GradleTask.Type.BUILD) {
			if(task.getTaskID() == IDEService.TASK_ASSEMBLE_DEBUG) {
				if(task.buildsApk()) {
					install(task.getApk(new File(mProject.getMainModulePath(), "build").getAbsolutePath(), mProject.getMainModule()));
				}
			}
		}
		
		getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
		recreateCompletionServices = false;
		invalidateOptionsMenu();
	}

	@Override
	public void onBuildFailed(String msg, GradleTask task) {
		if(task == null) return;
		if(task.canOutput()) {
			setStatus(msg);
		}

		showBuildResult();
		
		getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
		recreateCompletionServices = false;
		invalidateOptionsMenu();
	}

	@Override
	public void onGetDependencies(List<String> dependencies) {
		setStatus(getString(R.string.msg_starting_completion));
		mProject.setClassPaths(dependencies);
        createCompletionServices();
	}

	@Override
	public void onGetDependenciesFailed() {
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, (p1, p2) -> {
				p1.dismiss();
				if(getBuildService() != null)
					getBuildService().showDependencies();
			})
			.setTitle(R.string.failed)
			.setMessage(R.string.msg_first_prepare_failed)
			.create().show();
	}
	
	@Override
	public void saveFiles() {
		saveAll();
	}
    
    // Can be called from a different different, better to run on UI thread
	private void appendBuildOut(final String str) {
		runOnUiThread(() -> {
            String strFinal = str.endsWith("\n") ? str : str.concat("\n");
            getBuildView(true).getText().append(strFinal);
        });
	}
	
    // Can be called from a different different, better to run on UI thread
	private void appendLogOut(final String str) {
		runOnUiThread(() -> {
            String strFinal = str.endsWith("\n") ? str : str.concat("\n");
            getLogView(true).getText().append(strFinal);
        });
	}
	
	private void showFiles() {
		if(mBinding.viewOptionsCard.getVisibility() == View.VISIBLE) {
			hideViewOptions();
		}
		
		mBinding.getRoot().openDrawer(GravityCompat.END, true);
	}
	
	private void showDaemonStatus() {
		if(mBinding.viewOptionsCard.getVisibility() == View.VISIBLE) {
			hideViewOptions();
		}
		ShellServer shell = getApp().newShell(t -> getDaemonStatusFragment().append(t.toString()));
		shell.bgAppend(String.format("echo '%s'", getString(R.string.msg_getting_daemom_status)));
		shell.bgAppend("gradle --status");
		if(!getDaemonStatusFragment().isShowing())
			getDaemonStatusFragment().show(getSupportFragmentManager(), "daemon_status");
	}
	
	private void hideToolbarAndTabs() {
		mBinding.editorAppBarLayout.setVisibility(View.INVISIBLE);
	}
	
	private void showToolbarAndTabs() {
		mBinding.editorAppBarLayout.setVisibility(View.VISIBLE);
	}
	
	private void showViewOptions() {
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.fabView, mBinding.viewOptionsCard));
		mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
		mBinding.transformScrim.setVisibility(View.VISIBLE);
		mBinding.fabView.setVisibility(View.GONE);
	}
	
	private void hideViewOptions() {
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewOptionsCard, mBinding.fabView));
		mBinding.viewOptionsCard.setVisibility(View.GONE);
		mBinding.transformScrim.setVisibility(View.GONE);
		mBinding.fabView.setVisibility(View.VISIBLE);
	}
	
	private void showBuildResult() {
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewBuildOut, mBinding.buildContainer));
		mBinding.buildContainer.setVisibility(View.VISIBLE);
		mBinding.viewOptionsCard.setVisibility(View.GONE);
		hideToolbarAndTabs();
	}
	
	private void hideBuildResult() {
		showToolbarAndTabs();
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.buildContainer, mBinding.viewBuildOut));
		mBinding.buildContainer.setVisibility(View.GONE);
		mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
	}
	
	private void showLogResult() {
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewLogs, mBinding.logContainer));
		mBinding.logContainer.setVisibility(View.VISIBLE);
		mBinding.viewOptionsCard.setVisibility(View.GONE);
		hideToolbarAndTabs();
	}

	private void hideLogResult() {
		showToolbarAndTabs();
		
		TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.logContainer, mBinding.viewLogs));
		mBinding.logContainer.setVisibility(View.GONE);
		mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
	}
    
    private void showDiagnostics() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewDiags, mBinding.diagContainer));
        mBinding.diagContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    private void hideDiagnostics() {
        showToolbarAndTabs();
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.diagContainer, mBinding.viewDiags));
        mBinding.diagContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
	}
	
	private MaterialContainerTransform createContainerTransformFor(View start, View end) {
		return createContainerTransformFor(start, end, mBinding.realContainer);
	}
	
	private MaterialContainerTransform createContainerTransformFor(View start, View end, View drawingView) {
		MaterialContainerTransform transform = new MaterialContainerTransform();
		transform.setStartView(start);
		transform.setEndView(end);
		transform.addTarget(end);
		transform.setDrawingViewId(drawingView.getId());
		transform.setAllContainerColors(ContextCompat.getColor(this, R.color.primaryDarkColor));
		transform.setElevationShadowEnabled(true);
		transform.setPathMotion(new MaterialArcMotion());
		transform.setScrimColor(Color.TRANSPARENT);
		return transform;
	}

	private void preBuild() {
		getBuildService().showDependencies();
	}

	private void install(File apk) {
		if(apk.exists()) {
			Intent i = IntentUtils.getInstallAppIntent(apk);
			if(i != null) {
				startActivity(i);
			}
		}
	}

	private void createCompletionServices() {
		new TaskExecutor().executeAsync(() -> {
			getApp().createCompletionService(mProject, EditorActivity.this);
			return null;
		}, __ -> {
			setStatus(getString(getApp().areCompletorsStarted() ? R.string.msg_service_started : R.string.msg_starting_completion_failed));
		});
	}
	
	private void registerServiceReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(IDEService.ACTION_CREATED);
		registerReceiver(mServiceStartReceiver, filter);
	}

	private void registerLogReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LogReceiver.APPEND_LOG);
		registerReceiver(mLogReceiver, filter);
	}

    private void setStatus(final CharSequence text) {
        try {
			runOnUiThread(() -> mBinding.editorStatusText.setText(text));
        } catch (Throwable th) {
		}
    }
	
	private void showFirstBuildNotice() {
		new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog)
			.setPositiveButton(android.R.string.ok, null)
			.setTitle(R.string.title_first_build)
			.setMessage(R.string.msg_first_build)
			.setCancelable(false)
			.create().show();
	}

    @Override
    public void onGroupClick(DiagnosticGroup group) {
        if(group != null
            && group.file != null
            && group.file.exists()
            && FileUtils.isUtf8(group.file))
        {
            openFile(group.file);
        }
    }

    @Override
    public void onDiagnosticClick(Diagnostic diagnostic) {
    }
    
    @Override
    public void javaProgressStart(JavaStartProgressParams params) {
    }

    @Override
    public void javaProgressReport(JavaReportProgressParams params) {
    }

    @Override
    public void javaProgressEnd() {
    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams params) {
        if(params == null) return;
        File file = new File(params.uri);
        if(!(file.exists() && file.isFile())) return;
        diagnostics.put(file, params.diagnostics);
        getDiagnosticsList().setAdapter(new DiagnosticsAdapter(mapAsGroup(diagnostics), this));
        
        EditorFragment editor = null;
        if(mPagerAdapter != null && (editor = mPagerAdapter.findEditorByFile(new File(params.uri))) != null) {
            editor.setDiagnostics(params.diagnostics);
        }
    }

    @Override
    public void javaColors(JavaColors colors) {
        final File file = new File(colors.uri);
        final EditorFragment editor = mPagerAdapter.findEditorByFile(file);
        editor.setJavaColors(colors);
    }

    @Override
    public void showMessage(ShowMessageParams params) {
    }

    @Override
    public void registerCapability(String method, JsonElement options) {
    }

    @Override
    public void customNotification(String method, JsonElement params) {
    }
    
    @Override
    public void onServerStarted(int currentId) {
        new Thread(() -> {
            final JavaLanguageServer server = getApp().getJavaLanguageServer();
            while(server != null && !pendingMessages.isEmpty()) {
                Message msg = pendingMessages.pop();
                server.send(msg);
            }
        }).start();
    }

    @Override
    public void onCursorPositionChange(int leftLine, int leftColumn, int rightLine, int rightColumn) {
        if(leftLine == rightLine && leftColumn == rightColumn) {
            mBinding.editorToolbar.setSubtitle(String.format("[%d, %d]", leftLine, leftColumn));
        } else {
            mBinding.editorToolbar.setSubtitle(String.format("[%d, %d] | [%d, %d]", leftLine, leftColumn, rightLine, rightColumn));
        }
    }
    
    @Override
    public void didOpen(DidOpenTextDocumentParams p) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.didOpen(p);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_OPEN, gson.toJson(p)));
    }
    
    @Override
    public void didClose(DidCloseTextDocumentParams p) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.didClose(p);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_CLOSE, gson.toJson(p)));
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.didChange(params);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_CHANGE, gson.toJson(params)));
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.didSave(params);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }
    
    private void addPendingMessage(Message msg) {
        pendingMessages.push(msg);
    }
    
    private Message createMessage(String method, String data) {
        Message msg = new Message();
        msg.jsonrpc = "2.0";
        msg.method = method;
        msg.params = new JsonParser().parse(data);
        return msg;
    }
	
	/*****************************
	 **** Tab Related ************
	 *****************************/
	 
	@Override
	public void onTabSelected(TabLayout.Tab p1) {
		EditorFragment current = mPagerAdapter.getFrag(p1.getPosition());
		if(current != null && current.getFile() != null) {
			this.mCurrentFragment = current;
			mCurrentFile = current.getFile();
			refreshSymbolInput(current);
		}
	}

	@Override
	public void onTabUnselected(TabLayout.Tab p1) {
		EditorFragment frag = mPagerAdapter.getFrag(p1.getPosition());
		if(frag == null) return;
		boolean isGradle = frag.isModified() && frag.getFile().getName().endsWith(EditorFragment.EXT_GRADLE);
		frag.save();
		if(isGradle) {
			notifySyncNeeded();
		}
	}

	@Override
	public void onTabReselected(TabLayout.Tab p1) {
		mTabCloseAction.show(mBinding.tabs);
	}
	
	// ************************
	// **** Navigation View
	// ************************
	
	@Override
	public boolean onNavigationItemSelected(MenuItem p1) {
		final int id = p1.getItemId();
		if(id == R.id.editornav_discuss) {
			getApp().openTelegramGroup();
		} else if(id == R.id.editornav_suggest) {
			getApp().openIssueTracker();
		} else if(id == R.id.editornav_needHelp) {
			showNeedHelpDialog();
		} else if(id == R.id.editornav_settings) {
			startActivity(new Intent(this, PreferencesActivity.class));
		} else if(id == R.id.editornav_share) {
			startActivity(IntentUtils.getShareTextIntent(getString(R.string.msg_share_app)));
		} 
		return false;
	}

	private void showNeedHelpDialog() {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.need_help);
		builder.setMessage(R.string.msg_need_help);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();
	}
	
	// -----------------------------
	// ---- Fragment Related
	// -----------------------------
	
	public void loadFragment(Fragment fragment) {
		super.loadFragment(fragment, mBinding.editorFrameLayout.getId());
	}
	
	private OptionsListFragment getFileOptionsFragment(File file) {
		mFileOptionsFragment = new OptionsListFragment();
		mFileOptionsFragment.addOption(new SheetOption(0, R.drawable.ic_file_copy_path, R.string.copy_path, file));
		mFileOptionsFragment.addOption(new SheetOption(1, R.drawable.ic_file_rename, R.string.rename_file, file));
		mFileOptionsFragment.addOption(new SheetOption(2, R.drawable.ic_file_delete, R.string.delete_file, file));
		if(file.isDirectory()) {
			mFileOptionsFragment.addOption(new SheetOption(3, R.drawable.ic_new_file, R.string.new_file, file));
			mFileOptionsFragment.addOption(new SheetOption(4, R.drawable.ic_new_folder, R.string.new_folder, file));
		}
		mFileOptionsFragment.setOnOptionsClickListener(o -> handleOptionClick(o));
		return mFileOptionsFragment;
	}

	private MaterialBanner getSyncBanner() {
		return mBinding.syncBanner.setContentTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
			.setBannerBackgroundColor(ContextCompat.getColor(this, R.color.primaryLightColor))
			.setButtonTextColor(ContextCompat.getColor(this, R.color.secondaryColor))
			.setIcon(R.drawable.ic_sync)
			.setContentText(R.string.msg_sync_needed);
	}
	
	private TextSheetFragment getDaemonStatusFragment() {
		return mDaemonStatusFragment == null ? mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true).setTitleText(R.string.gradle_daemon_status) : mDaemonStatusFragment;
	}
	
	private CodeEditor getBuildView() {
		return getBuildView(false);
	}
	
	private CodeEditor getBuildView(boolean setup) {
		CodeEditor edit = buildView == null ? createBuildView() : buildView;
		if(setup && edit.getParent() == null) {
			setupContainers();
		}
		return edit;
	}

	private CodeEditor createBuildView() {
		buildView = new CodeEditor(this);
		buildView.setEditable(false);
		buildView.setDividerWidth(0);
		buildView.setEditorLanguage(new EmptyLanguage());
		buildView.setOverScrollEnabled(false);
		buildView.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		buildView.setWordwrap(false);
		buildView.setUndoEnabled(false);
		buildView.setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
		buildView.setTypefaceText(TypefaceUtils.jetbrainsMono());
		buildView.setTextSize(12);
		return buildView;
	}
	
	private CodeEditor getLogView() {
		return getLogView(false);
	}

	private CodeEditor getLogView(boolean setup) {
		CodeEditor edit = logView == null ? createLogView() : logView;
		if(setup && edit.getParent() == null) {
			setupContainers();
		}
		return edit;
	}

	private CodeEditor createLogView() {
		logView = new CodeEditor(this);
		logView.setEditable(false);
		logView.setDividerWidth(0);
		logView.setEditorLanguage(getLogLanguage());
		logView.setOverScrollEnabled(false);
		logView.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		logView.setWordwrap(false);
		logView.setUndoEnabled(false);
		logView.setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
		logView.setTypefaceText(TypefaceUtils.jetbrainsMono());
		logView.setTextSize(12);
		return logView;
	}
    
    private RecyclerView getDiagnosticsList() {
        return diagnosticList == null ? createDiagnosticsList() : diagnosticList;
    }
    
    private RecyclerView createDiagnosticsList() {
        diagnosticList = new RecyclerView(this);
        diagnosticList.setLayoutManager(new LinearLayoutManager(this));
        diagnosticList.setAdapter(new DiagnosticsAdapter(mapAsGroup(this.diagnostics), this));
        return diagnosticList;
    }
    
    private List<DiagnosticGroup> mapAsGroup(Map<File, List<Diagnostic>> diags) {
        List<DiagnosticGroup> groups = new ArrayList<>();
        if(diags == null || diags.size() <= 0)
            return groups;
        for(File file : diags.keySet()) {
            List<Diagnostic> fileDiags = diags.get(file);
            if(fileDiags == null || fileDiags.size() <= 0)
                continue;
            DiagnosticGroup group = new DiagnosticGroup(R.drawable.ic_language_java, file, fileDiags);
            groups.add(group);
        }
        return groups;
    }
    
	private LogLanguageImpl getLogLanguage() {
		return mLogLanguageImpl == null ? mLogLanguageImpl = new LogLanguageImpl() : mLogLanguageImpl;
	}
	
	// -----------------------------
	// ---- File Related
	// -----------------------------
	
	@Override
	public void openFile(File file) {
		int i = mPagerAdapter.openFile(file, this, this, this);
		if(i >= 0 && !mBinding.tabs.getTabAt(i).isSelected())
			mBinding.tabs.getTabAt(i).select();
		mBinding.editorDrawerLayout.closeDrawer(GravityCompat.END);
	}

    @Override
    public void onOpenSuccessful(File file, String text) {
        if(file.getName().endsWith(".java")) {
            TextDocumentItem doc = new TextDocumentItem();
            doc.languageId = "java";
            doc.uri = file.toURI();
            doc.text = text;
            doc.version = VersionedFileManager.fileOpened(file);
            DidOpenTextDocumentParams p = new DidOpenTextDocumentParams();
            p.textDocument = doc;
            didOpen(p);
        }
    }

	@Override
	public void showFileOptions(File thisFile, TreeNode node) {
		mLastHolded = node;
		getFileOptionsFragment(thisFile).show(getSupportFragmentManager(), TAG_FILE_OPTIONS_FRAGMENT);
	}
	
	private boolean saveAll() {
		boolean isGradleSaved = mPagerAdapter.saveAll();
		getApp().toast(R.string.all_saved, Toaster.Type.SUCCESS);
		if(isGradleSaved) notifySyncNeeded();
		return isGradleSaved;
	}
	
	private void handleOptionClick(SheetOption o) {
		if(o.extra != null && o.extra instanceof File) {
			final File f = (File) o.extra;
			switch(o.id) {
				case 0 :
					ClipboardUtils.copyText("[AndroidIDE] Copied File Path", f.getAbsolutePath());
					getApp().toast(R.string.copied, Toaster.Type.SUCCESS);
					break;
				case 1 :
					renameFile(f);
					break;
				case 2 :
					delete(f);
					break;
				case 3 :
					createNewFile(f);
					break;
				case 4 :
					createNewFolder(f);
					break;
			}
		}
	}
	
	private void createNewFile(File f) {
		createNewFile(f, false);
	}
	
	private void createNewFile(final File f, boolean forceUnknownType) {
		if(forceUnknownType) {
			createNewEmptyFile(f);
		} else {
			final boolean isJava = Pattern.compile(Pattern.quote(mProject.getProjectPath()) + JAVA_PATH_REGEX).matcher(f.getAbsolutePath()).find();
			final boolean isRes = Pattern.compile(Pattern.quote(mProject.getProjectPath()) + RES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
			final boolean isLayoutRes = Pattern.compile(Pattern.quote(mProject.getProjectPath()) + LAYOUTRES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
			final boolean isMenuRes = Pattern.compile(Pattern.quote(mProject.getProjectPath()) + MENURES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
			final boolean isDrawableRes = Pattern.compile(Pattern.quote(mProject.getProjectPath()) + DRAWABLERES_PATH_REGEX).matcher(f.getAbsolutePath()).find();
			if(isJava) {
				createJavaClass(f);
			} else if(isLayoutRes && f.getName().equals("layout")) {
				createLayoutRes(f);
			} else if(isMenuRes && f.getName().equals("menu")) {
				createMenuRes(f);
			} else if(isDrawableRes && f.getName().equals("drawable")) {
				createDrawableRes(f);
			} else if(isRes && f.getName().equals("res")) {
				createNewResource(f);
			} else {
				createNewEmptyFile(f);
			}
		}
	}

	private void createJavaClass(final File f) {
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		final LayoutCreateFileJavaBinding binding = LayoutCreateFileJavaBinding.inflate(getLayoutInflater());
		builder.setView(binding.getRoot());
		builder.setTitle(R.string.new_java_class);
		builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
			p1.dismiss();
			final String name = binding.name.getEditText().getText().toString().trim();
			final String pkgName = ProjectWriter.getPackageName(f);
			if(pkgName == null || pkgName.trim().length() <= 0) {
				getApp().toast(R.string.msg_get_package_failed, Toaster.Type.ERROR);
				return;
			} else {
				final int id = binding.typeGroup.getCheckedButtonId();
				if(id == binding.typeClass.getId()) {
					createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaClass(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
				} else if(id == binding.typeInterface.getId()) {
					createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaInterface(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
				} else if(id == binding.typeEnum.getId()) {
					createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createJavaEnum(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
				} else if(id == binding.typeActivity.getId()) {
					createFile(f, name.endsWith(".java") ? name : name.concat(".java"), ProjectWriter.createActivity(pkgName, !name.contains(".") ? name : name.substring(0, name.lastIndexOf("."))));
				} else {
					createFile(f, name, "");
				}
				
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setCancelable(false);
		builder.create().show();
	}

	private void createLayoutRes(File f) {
		createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createLayout(), ".xml");
	}

	private void createMenuRes(File f) {
		createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createMenu(), ".xml");
	}

	private void createDrawableRes(File f) {
		createNewFileWithContent(Environment.mkdirIfNotExits(f), ProjectWriter.createDrawable(), ".xml");
	}

	private void createNewResource(File f) {
		final String[] labels = {
			getString(R.string.restype_drawable),
			getString(R.string.restype_layout),
			getString(R.string.restype_menu),
			getString(R.string.restype_other)
		};
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.new_xml_resource);
		builder.setItems(labels, (p1, p2) -> {
			final int pos = p2;
			if(pos == 0) {
				createDrawableRes(new File(f, "drawable"));
			} else if(pos == 1) {
				createLayoutRes(new File(f, "layout"));
			} else if(pos == 2) {
				createMenuRes(new File(f, "menu"));
			} else if(pos == 3) {
				createNewFile(f, true);
			}
		});
		builder.create().show();
	}
	
	private void createNewEmptyFile(File f) {
		createNewFileWithContent(f, "");
	}
	
	private void createNewFileWithContent(File f, String content) {
		createNewFileWithContent(f, content, null);
	}
	
	private void createNewFileWithContent(File folder, String content, String extension) {
		final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(getLayoutInflater());
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		
		binding.name.getEditText().setHint(R.string.file_name);

		builder.setTitle(R.string.new_file);
		builder.setMessage(getString(R.string.msg_can_contain_slashes).concat("\n\n").concat(getString(R.string.msg_newfile_dest, folder.getAbsolutePath())));
		builder.setView(binding.getRoot());
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
			p1.dismiss();
			String name = binding.name.getEditText().getText().toString().trim();
			if(extension != null && extension.trim().length() > 0) {
				name = name.endsWith(extension) ? name : name.concat(extension);
			}
			createFile(folder, name, content);
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}
	
	private void createFile(File f, String name, String content) {
		if(name.length() > 0 && name.length() <= 40 && !name.startsWith("/")) {
			final File file = new File(f, name);
			if(file.exists()) {
				getApp().toast(R.string.msg_file_exists, Toaster.Type.ERROR);
			} else {
				if(FileIOUtils.writeFileFromString(file, content)) {
					getApp().toast(R.string.msg_file_created, Toaster.Type.SUCCESS);
					if(mLastHolded != null) {
						TreeNode node = new TreeNode(file);
						node.setViewHolder(new FileTreeViewHolder(this));
						mLastHolded.addChild(node);
						mFileTreeFragment.expandNode(mLastHolded);
					} else {
						mFileTreeFragment.listProjectFiles();
					}
				} else {
					getApp().toast(R.string.msg_file_creation_failed, Toaster.Type.ERROR);
				}
			}
		} else {
			getApp().toast(R.string.msg_invalid_name, Toaster.Type.ERROR);
		}
	}

	private void createNewFolder(File f) {
		final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(getLayoutInflater());
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		
		binding.name.getEditText().setHint(R.string.folder_name);

		builder.setTitle(R.string.new_folder);
		builder.setMessage(R.string.msg_can_contain_slashes);
		builder.setView(binding.getRoot());
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.text_create, (p1, p2) -> {
			p1.dismiss();
			final String name = binding.name.getEditText().getText().toString().trim();
			if(name.length() > 0 && name.length() <= 40 && !name.startsWith("/")) {
				final File file = new File(f, name);
				if(file.exists()) {
					getApp().toast(R.string.msg_folder_exists, Toaster.Type.ERROR);
				} else {
					if(file.mkdirs()) {
						getApp().toast(R.string.msg_folder_created, Toaster.Type.SUCCESS);
						if(mLastHolded != null) {
							TreeNode node = new TreeNode(file);
							node.setViewHolder(new FileTreeViewHolder(EditorActivity.this));
							mLastHolded.addChild(node);
							mFileTreeFragment.expandNode(mLastHolded);
						} else {
							mFileTreeFragment.listProjectFiles();
						}
					} else {
						getApp().toast(R.string.msg_folder_creation_failed, Toaster.Type.ERROR);
					}
				}
			} else {
				getApp().toast(R.string.msg_invalid_name, Toaster.Type.ERROR);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
	}

	private void delete(final File f) {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder
			.setNegativeButton(android.R.string.no, null)
			.setPositiveButton(android.R.string.yes, (p1, p2) -> {
			p1.dismiss();
			final boolean deleted = FileUtils.delete(f);
			getApp().toast(deleted ? R.string.deleted : R.string.delete_failed, deleted ? Toaster.Type.SUCCESS : Toaster.Type.ERROR);
			if(deleted) {
				if(mLastHolded != null) {
					TreeNode parent = mLastHolded.getParent();
					parent.deleteChild(mLastHolded);
					mFileTreeFragment.expandNode(parent);
				} else {
					mFileTreeFragment.listProjectFiles();
				}
			}
		})
		.setTitle(R.string.title_confirm_delete)
			.setMessage(getString(R.string.msg_confirm_delete, String.format("%s [%s]", f.getName(), f.getAbsolutePath())))
			.setCancelable(false)
			.create().show();
	}

	private void renameFile(File f) {
		final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(this));
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);

		binding.name.getEditText().setHint(getString(R.string.new_name));
		binding.name.getEditText().setText(f.getName());

		builder.setTitle(R.string.rename_file);
		builder.setMessage(R.string.msg_rename_file);
		builder.setView(binding.getRoot());
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.setPositiveButton(R.string.rename_file, (p1, p2) -> {
			p1.dismiss();
			String name = binding.name.getEditText().getText().toString().trim();
			boolean renamed = name != null && name.length() > 0 && name.length() <= 40 && FileUtils.rename(f, name);
			getApp().toast(renamed ? R.string.renamed : R.string.rename_failed, renamed ? Toaster.Type.SUCCESS : Toaster.Type.ERROR);
			if(renamed) {
				if(mLastHolded != null) {
					TreeNode parent = mLastHolded.getParent();
					parent.deleteChild(mLastHolded);
					TreeNode node = new TreeNode(new File(f.getParentFile(), name));
					node.setViewHolder(new FileTreeViewHolder(EditorActivity.this));
					parent.addChild(node);
					mFileTreeFragment.expandNode(parent);
				} else {
					mFileTreeFragment.listProjectFiles();
				}
			}
		});
		builder.create().show();
	}
	
	// -----------------------------
	// ---- Quick Actions
	// -----------------------------
	
	private void createQuickActions() {
		ActionItem closeThis = new ActionItem(ACTIONID_CLOSE, getString(R.string.action_closeThis), R.drawable.ic_close_this);
		ActionItem closeOthers = new ActionItem(ACTIONID_OTHERS, getString(R.string.action_closeOthers), R.drawable.ic_close_others);
		ActionItem closeAll = new ActionItem(ACTIONID_ALL, getString(R.string.action_closeAll), R.drawable.ic_close_all);
		mTabCloseAction = new QuickAction(this, QuickAction.HORIZONTAL);
		mTabCloseAction.addActionItem(closeThis, closeOthers, closeAll);
		mTabCloseAction.setColorRes(R.color.tabAction_background);
		mTabCloseAction.setTextColorRes(R.color.tabAction_text);

		mTabCloseAction.setOnActionItemClickListener((__) -> {
			ActionItem item = __;
			final int id = item.getActionId();
			saveAll();
			if(id == ACTIONID_CLOSE) {
				closeFile(mBinding.tabs.getSelectedTabPosition());
			}

			if(id == ACTIONID_OTHERS) {
				closeOthers();
				closeOthers();
			}

			if(id == ACTIONID_ALL) {
				closeAll();
			}
		});
	}

	private void closeAll() {
		mBinding.tabs.removeOnTabSelectedListener(this);

		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), mProject);
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.addOnTabSelectedListener(this);
	}

	private void closeOthers() {
		final Fragment frag = mPagerAdapter.getFragments().get(mBinding.tabs.getSelectedTabPosition());
		final File file = mPagerAdapter.getOpenedFiles().get(mBinding.tabs.getSelectedTabPosition());
		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), mProject, Arrays.asList(frag), Arrays.asList(file));
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.addOnTabSelectedListener(this);
	}
    
	private void closeFile(int index) {
		closeFile(index, true);
	}

	private void closeFile(int index, boolean selectOther) {
		mBinding.tabs.removeOnTabSelectedListener(this);
        
		int pos = index;
		final List<Fragment> frags = mPagerAdapter.getFragments();
		final List<File> files = mPagerAdapter.getOpenedFiles();
        
        final File removed = files.get(index);
        
		frags.remove(index);
		files.remove(index);

		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), mProject, frags, files);
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.addOnTabSelectedListener(this);

		if(selectOther) {
			if(pos >= 0 && pos < frags.size()) {
				mBinding.editorViewPager.setCurrentItem(pos, false);
			} else {
				int i = pos - 1;
				if(i >= 0 && i < frags.size())
					pos = i;
				else {
					i = pos + 1;
					if(i >= 0 && i < frags.size())
						pos = i;
				}
				mBinding.editorViewPager.setCurrentItem(pos, false);
			}
		}
        
        TextDocumentIdentifier id = new TextDocumentIdentifier();
        id.uri = removed.toURI();
        DidCloseTextDocumentParams p = new DidCloseTextDocumentParams();
        p.textDocument = id;
        didClose(p);
	}
}
