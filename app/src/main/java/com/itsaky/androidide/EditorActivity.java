package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
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
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.EditorPagerAdapter;
import com.itsaky.androidide.adapters.SearchListAdapter;
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityEditorBinding;
import com.itsaky.androidide.databinding.LayoutCreateFileJavaBinding;
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding;
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.fragments.FileTreeFragment;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.fragments.sheets.ProjectInfoSheet;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.interfaces.JLSRequestor;
import com.itsaky.androidide.language.buildout.BuildOutputLanguage;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import com.itsaky.androidide.language.logs.LogLanguageImpl;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.VersionedFileManager;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.models.SheetOption;
import com.itsaky.androidide.models.project.IDEModule;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.receivers.LogReceiver;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.ProjectWriter;
import com.itsaky.androidide.utils.RecursiveFileSearcher;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.utils.TransformUtils;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.views.MaterialBanner;
import com.itsaky.androidide.views.SymbolInputView;
import com.itsaky.lsp.Diagnostic;
import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.DidChangeWatchedFilesParams;
import com.itsaky.lsp.DidCloseTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.DidSaveTextDocumentParams;
import com.itsaky.lsp.FileChangeType;
import com.itsaky.lsp.FileEvent;
import com.itsaky.lsp.JavaColors;
import com.itsaky.lsp.JavaReportProgressParams;
import com.itsaky.lsp.JavaStartProgressParams;
import com.itsaky.lsp.LanguageClient;
import com.itsaky.lsp.Location;
import com.itsaky.lsp.Message;
import com.itsaky.lsp.ParameterInformation;
import com.itsaky.lsp.PublishDiagnosticsParams;
import com.itsaky.lsp.Range;
import com.itsaky.lsp.ReferenceParams;
import com.itsaky.lsp.ShowMessageParams;
import com.itsaky.lsp.SignatureHelp;
import com.itsaky.lsp.SignatureInformation;
import com.itsaky.lsp.TextDocumentIdentifier;
import com.itsaky.lsp.TextDocumentItem;
import com.itsaky.lsp.TextDocumentPositionParams;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Pattern;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import com.itsaky.androidide.models.SaveResult;

public class EditorActivity extends StudioActivity implements FileTreeFragment.FileActionListener,
														IDEService.BuildListener,
														TabLayout.OnTabSelectedListener,
														NavigationView.OnNavigationItemSelectedListener,
                                                        DiagnosticClickListener, 
                                                        EditorFragment.FileOpenListener,
                                                        LanguageClient,
                                                        JLSRequestor {
    
    private ActivityEditorBinding mBinding;
	private EditorPagerAdapter mPagerAdapter;
	private FileTreeFragment mFileTreeFragment;
	private EditorFragment mCurrentFragment;
	public static File mCurrentFile;
	private TreeNode mLastHolded;
	private CodeEditor buildView;
	private CodeEditor logView;
    private RecyclerView diagnosticList;
    private RecyclerView searchResultList;
    private SymbolInputView symbolInput;
	
    private static AndroidProject mProject;
    private IDEProject mIDEProject;
    
	private LogLanguageImpl mLogLanguageImpl;
	
	private QuickAction mTabCloseAction;
	private TextSheetFragment mDaemonStatusFragment;
	private OptionsListFragment mFileOptionsFragment;
    private ProgressSheet mSearchingProgress;
    private ProjectInfoSheet mProjectInfoSheet;
    private AlertDialog mFindInProjectDialog;
    
    private final Map<File, List<Diagnostic>> diagnostics = new HashMap<>();
	private final Stack<Message> pendingMessages = new Stack<>();
    
	private final String RES_PATH_REGEX = "/.*/src/.*/res";
	private final String LAYOUTRES_PATH_REGEX = "/.*/src/.*/res/layout";
	private final String MENURES_PATH_REGEX = "/.*/src/.*/res/menu";
	private final String DRAWABLERES_PATH_REGEX = "/.*/src/.*/res/drawable";
	private final String JAVA_PATH_REGEX = "/.*/src/.*/java";
	
    private static final Gson gson = new Gson();
	private static final String TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment";
    public static final String TAG = "EditorActivity";
	
	private static final int ACTIONID_CLOSE = 100;
	private static final int ACTIONID_OTHERS = 101;
	private static final int ACTIONID_ALL = 102;
    
    public static final String EXTRA_PROJECT = "project";
    
	private LogReceiver mLogReceiver = new LogReceiver().setLogListener(line -> appendApkLog(line));
	
    /**
     * MenuItem(s) that are related to the build process
     *
     * These items will be disabled once the build process starts and will be enabled after the execution
     */
	private final int[] BUILD_IDS = {
		R.id.menuEditor_quickRun,
		R.id.menuEditor_runDebug,
		R.id.menuEditor_runRelease,
		R.id.menuEditor_runBuild,
		R.id.menuEditor_runBundle,
		R.id.menuEditor_runClean,
		R.id.menuEditor_runCleanBuild,
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
		
		getProjectFromIntent();
		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), this.mProject);
		mFileTreeFragment = FileTreeFragment.newInstance(this.mProject).setFileActionListener(this);
		mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.editorDrawerLayout, mBinding.editorToolbar, R.string.app_name, R.string.app_name);
		mBinding.editorDrawerLayout.setDrawerListener(toggle);
		mBinding.startNav.setNavigationItemSelectedListener(this);
		toggle.syncState();
		loadFragment(mFileTreeFragment);
		
		createQuickActions();
        
        symbolInput = new SymbolInputView(this);
        mBinding.inputContainer.addView(symbolInput, 0, new ViewGroup.LayoutParams(-1, -2));
        
		mBinding.editorViewPager.setOffscreenPageLimit(9);
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.setOnTabSelectedListener(this);
		mBinding.fabView.setOnClickListener(v -> showViewOptions());
		
		getApp().checkAndUpdateGradle();
		getApp().startBuildService(mProject);
		getApp().getBuildService().setListener(this);
        startServices();
        invalidateOptionsMenu();
        
		KeyboardUtils.registerSoftInputChangedListener(this, __ -> onSoftInputChanged());
		registerLogReceiver();
		setupContainers();
        setupSignatureText();
    }

    private void getProjectFromIntent() {
        this.mProject = getIntent().getParcelableExtra(EXTRA_PROJECT);
        getApp().getPrefManager().setOpenedProject(this.mProject.getProjectPath());
    }

    private void setupSignatureText() {
        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(0xff212121);
        gd.setStroke(1, 0xffffffff);
        gd.setCornerRadius(8);
        mBinding.symbolText.setBackgroundDrawable(gd);
        mBinding.symbolText.setVisibility(View.GONE);
    }

	private void setupContainers() {
        removeFromParent(getBuildView());
        removeFromParent(getLogView());
        removeFromParent(getDiagnosticsList());
        removeFromParent(getSearchResultList());
		
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
        
        mBinding.searchResultsListContainer.addView(getSearchResultList(), new ViewGroup.LayoutParams(-1, -1));
        mBinding.searchResultsContainer.setVisibility(View.GONE);
        mBinding.searchResultsToolbar.setNavigationOnClickListener(v -> hideSearchResults());
		mBinding.searchResultsToolbar.setOnClickListener(v -> hideSearchResults());
		
		mBinding.viewOptionsCard.setVisibility(View.GONE);
		mBinding.transformScrim.setVisibility(View.GONE);
		
		mBinding.transformScrim.setOnClickListener(v -> hideViewOptions());
		mBinding.viewBuildOut.setOnClickListener(v -> showBuildResult());
        mBinding.viewDiags.setOnClickListener(v -> showDiagnostics());
        mBinding.viewSearchResults.setOnClickListener(v -> showSearchResults());
		mBinding.viewLogs.setOnClickListener(v -> showLogResult());
		mBinding.viewFiles.setOnClickListener(v -> showFiles());
		mBinding.viewDaemonStatus.setOnClickListener(v -> showDaemonStatus());
        
        handleDiagnosticsResultVisibility(true);
        handleSearchResultVisibility(true);
	}
    
    private void startServices() {
        getBuildService().assembleDebug(false);
    }
    
    private void removeFromParent(View v) {
        if(v.getParent() != null && v.getParent() instanceof ViewGroup) {
            ((ViewGroup) v.getParent()).removeView(v);
		}
    }
	
	private void onSoftInputChanged() {
		invalidateOptionsMenu();
		if(KeyboardUtils.isSoftInputVisible(this)) {
			TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.TOP));
			symbolInput.setVisibility(View.VISIBLE);
			mBinding.fabView.hide();
		} else {
			TransitionManager.beginDelayedTransition(mBinding.getRoot(), new Slide(Gravity.BOTTOM));
			symbolInput.setVisibility(View.GONE);
			mBinding.fabView.show();
		}
	}

	private void refreshSymbolInput(EditorFragment frag) {
		symbolInput.bindEditor(frag.getEditor());
		symbolInput.setSymbols(Symbols.forFile(frag.getFile()));
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
    
    private void closeProject(boolean manualFinish) {
        if (getBuildService() != null) {
            getBuildService().setListener(null);
            getBuildService().exit();
        }
        
        getApp().getPrefManager().setOpenedProject(PreferenceManager.NO_OPENED_PROJECT);
        
        if(getApp().getJavaLanguageServer() != null)
            getApp().getJavaLanguageServer().exit();
            
        getApp().stopAllDaemons();
        
        if(manualFinish)
            finish();
    }
    
    private void confirmProjectClose() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
        builder.setTitle(R.string.title_confirm_project_close);
        builder.setMessage(R.string.msg_confirm_project_close);
        builder.setNegativeButton(android.R.string.no, null);
        builder.setPositiveButton(android.R.string.yes, (d, w) -> {
            d.dismiss();
            closeProject(true);
        });
        builder.show();
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
		} else if(mBinding.searchResultsContainer.getVisibility() == View.VISIBLE) {
            hideSearchResults();
		} else if(mFileOptionsFragment != null && mFileOptionsFragment.isShowing()) {
			mFileOptionsFragment.dismiss();
		} else {
            confirmProjectClose();
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
        closeProject(false);
        try {
            unregisterReceiver(mLogReceiver);
		} catch (Throwable th) {}
		super.onDestroy();
	}

	@Override
	@SuppressLint("AlwaysShowAction")
	public boolean onPrepareOptionsMenu(Menu menu) {
		for (int id : BUILD_IDS) {
			MenuItem item = menu.findItem(id);
			if (item != null) {
                boolean enabled = getBuildService() != null && !getBuildService().isBuilding();
				item.setEnabled(enabled);
                item.getIcon().setAlpha(enabled ? 255 : 76);
			}
		}
        
		MenuItem run1 = menu.findItem(R.id.menuEditor_quickRun);
		MenuItem run2 = menu.findItem(R.id.menuEditor_run);
		MenuItem undo = menu.findItem(R.id.menuEditor_undo);
		MenuItem redo = menu.findItem(R.id.menuEditor_redo);
        MenuItem save = menu.findItem(R.id.menuEditor_save);
        MenuItem def =  menu.findItem(R.id.menuEditor_gotoDefinition);
        MenuItem ref =  menu.findItem(R.id.menuEditor_findReferences);
        MenuItem comment = menu.findItem(R.id.menuEditor_commentLine);
        MenuItem uncomment = menu.findItem(R.id.menuEditor_uncommentLine);
        MenuItem findFile = menu.findItem(R.id.menuEditor_findFile);
        
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
        
        final boolean enabled1 = mCurrentFile != null;
        final boolean enabled2 = enabled1 && mCurrentFile.getName().endsWith(".java");
        final int alpha1 = enabled1 ? 255 : 76;
        final int alpha2 = enabled2 ? 255 : 76;
        
        undo.setEnabled(enabled1);
        redo.setEnabled(enabled1);
        save.setEnabled(enabled1);
        comment.setEnabled(enabled1);
        uncomment.setEnabled(enabled1);
        findFile.setEnabled(enabled1);
        def.setEnabled(enabled2);
        ref.setEnabled(enabled2);
        
        undo.getIcon().setAlpha(alpha1);
        redo.getIcon().setAlpha(alpha1);
        save.getIcon().setAlpha(alpha1);
        comment.getIcon().setAlpha(alpha1);
        uncomment.getIcon().setAlpha(alpha1);
        findFile.getIcon().setAlpha(alpha1);
        def.getIcon().setAlpha(alpha2);
        ref.getIcon().setAlpha(alpha2);
        
		return true;
	}

    @SuppressLint("RestrictedApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        if(menu instanceof MenuBuilder){
            MenuBuilder builder = (MenuBuilder) menu;
            builder.setOptionalIconsVisible(true);
        }
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
            /**
             * 1. Notify that all files are saved
             * 2. If there were any XML files modified, call ':app:processDebugResources' task
             *
             * This will make sure that we generate view bindings and R.jar at proper time
             * This will further result in updated code completion
             */
			saveAll(true, true);
		} else if (id == R.id.menuEditor_undo && this.mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
			this.mCurrentFragment.undo();
		} else if (id == R.id.menuEditor_redo && this.mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
			this.mCurrentFragment.redo();
		} else if(id == R.id.menuEditor_gotoDefinition && mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
            this.mCurrentFragment.findDefinition();
        } else if(id == R.id.menuEditor_findReferences && mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
            this.mCurrentFragment.findReferences();
        } else if(id == R.id.menuEditor_commentLine && mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
            this.mCurrentFragment.commentLine();
        } else if(id == R.id.menuEditor_uncommentLine && mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
            this.mCurrentFragment.uncommentLine();
        } else if(id == R.id.menuEditor_findFile && mCurrentFragment != null && this.mCurrentFragment.isVisible()) {
            this.mCurrentFragment.beginSearch();
        } else if(id == R.id.menuEditor_findProject) {
            AlertDialog d = getFindInProjectDialog();
            if(d != null) d.show();
        } else if(id == R.id.menuEditor_projectInfo) {
            if(!getProjectInfoSheet().isShowing()) {
                getProjectInfoSheet().show(getSupportFragmentManager(), "project_info_sheet");
            }
        }
		invalidateOptionsMenu();
		return true;
	}
    
    private AlertDialog getFindInProjectDialog() {
        return mFindInProjectDialog == null ? createFindInProjectDialog() : mFindInProjectDialog;
    }
    
    private AlertDialog createFindInProjectDialog() {
        if(mProject == null 
        || mProject.getModulePaths() == null
        || mProject.getModulePaths().size() <= 0) {
            getApp().toast(R.string.msg_no_modules, Toaster.Type.ERROR);
            return null;
        }
        final List<String> modules = mProject.getModulePaths();
        final List<File> srcDirs = new ArrayList<>();
        final LayoutSearchProjectBinding binding = LayoutSearchProjectBinding.inflate(getLayoutInflater());
        binding.modulesContainer.removeAllViews();
        for(int i=0;i<modules.size();i++) {
            final File file = new File(modules.get(i));
            final File src = new File(file, "src");
            if(!file.exists() || !file.isDirectory() || !src.exists() || !src.isDirectory()) continue;
            
            CheckBox check = new CheckBox(this);
            check.setText(file.getName());
            check.setChecked(true);
            
            LinearLayout.MarginLayoutParams params = new LinearLayout.MarginLayoutParams(-2, -2);
            params.bottomMargin = SizeUtils.dp2px(4);
            binding.modulesContainer.addView(check, params);
            
            srcDirs.add(src);
        }
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
        builder.setTitle(R.string.menu_find_project);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.menu_find, (dialog, which) -> {
            final String text = binding.input.getEditText().getText().toString().trim();
            if(text == null || text.isEmpty()) {
                getApp().toast(R.string.msg_empty_search_query, Toaster.Type.ERROR);
                return;
            }
            
            final List<File> searchDirs = new ArrayList<>();
            for(int i=0;i<binding.modulesContainer.getChildCount();i++) {
                CheckBox check = (CheckBox) binding.modulesContainer.getChildAt(i);
                if(check.isChecked())
                    searchDirs.add(srcDirs.get(i));
            }
            
            final String extensions = binding.filter.getEditText().getText().toString().trim();
            final List<String> exts = new ArrayList<>();
            if(extensions != null && !extensions.isEmpty()) {
                if(extensions.contains("|")) {
                    for(String str : extensions.split(Pattern.quote("|"))) {
                        if(str == null || str.trim().isEmpty())
                            continue;

                        exts.add(str);
                    }
                } else 
                    exts.add(extensions);
            }
            
            if(searchDirs == null || searchDirs.isEmpty()) {
                getApp().toast(R.string.msg_select_search_modules, Toaster.Type.ERROR);
            } else {
                dialog.dismiss();
                getProgressSheet(R.string.msg_searching_project).show(getSupportFragmentManager(), "search_in_project_progress");
                RecursiveFileSearcher.searchRecursiveAsync(text, exts, searchDirs, result -> handleSearchResults(result));
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (__, ___) -> __.dismiss());
        mFindInProjectDialog = builder.create();
        return mFindInProjectDialog;
    }
    
    private void handleSearchResults(Map<File, List<SearchResult>> results) {
        getSearchResultList().setAdapter(new SearchListAdapter(results, file -> {
            openFile(file);
            hideSearchResults();
        }, match -> {
            openFileAndSelect(match.file, match);
            hideSearchResults();
        }));
        mBinding.transformScrim.setVisibility(View.VISIBLE);
        mBinding.fabView.setVisibility(View.GONE);
        showSearchResults();
        
        if(mSearchingProgress != null && mSearchingProgress.isShowing())
            mSearchingProgress.dismiss();
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
    public void prepare() {
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
    public void onRunTask(GradleTask task, String taskName) {
        if(task != null && task.canOutput()) {
            setStatus(getBuildService().typeString(task.getTaskID()) + " " + taskName);
        }
    }

    @Override
    public void onBuildSuccessful(GradleTask task, String msg) {
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
        
        invalidateOptionsMenu();
    }

    @Override
    public void onBuildFailed(GradleTask task, String msg) {
        if(task == null) return;
        if(task.canOutput()) {
            setStatus(msg);
        }

        showBuildResult();

        getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        
        invalidateOptionsMenu();
    }

    @Override
    public void onGetDependencies(List<String> dependencies) {
        setStatus(getString(R.string.msg_starting_completion));
        mProject.setClassPaths(dependencies);
        createServices();
    }

    @Override
    public void onGetDependenciesFailed() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
        builder.setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, (p1, p2) -> {
            p1.dismiss();
            if(getBuildService() != null)
                getBuildService().initProject();
        })
        .setTitle(R.string.failed)
            .setMessage(R.string.msg_first_prepare_failed)
            .create().show();
    }

    @Override
    public void onProjectLoaded(IDEProject project) {
        project.iconPath = mProject.getIconPath();
        mIDEProject = project;
        createProjectInfoSheet(); // Recreate sheet, even if already created. Just to update its contents
        Optional<IDEModule> appModule = project.getModuleByPath(":app");
        if(appModule.isPresent()) {
            IDEModule app = appModule.get();
            setStatus(getString(R.string.msg_starting_completion));
            mProject.setClassPaths(app.dependencies);
            
            File androidJar = new File(Environment.ANDROID_HOME, String.format("platforms/%s/android.jar", app.compileSdkVersion));
            if(androidJar.exists()) {
                Environment.setBootClasspath(androidJar);
                mProject.addClasspath(androidJar.getAbsolutePath());
                createServices();
            } else setStatus("android.jar not found!");
        } else setStatus("Cannot get :app module...");
    }

    @Override
    public void saveFiles() {
        saveAll(false, false);
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
        try {
            EditorFragment frag = mPagerAdapter.getFrag(mBinding.tabs.getSelectedTabPosition());
            if(frag != null && frag.getEditor() != null) {
                frag.getEditor().hideAutoCompleteWindow();
                frag.getEditor().hideDiagnosticWindow();
            }
        } catch (Throwable e) {}
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
    
    private void showSearchResults() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewSearchResults, mBinding.searchResultsContainer));
        mBinding.searchResultsContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    private void hideSearchResults() {
        showToolbarAndTabs();
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.searchResultsContainer, mBinding.viewSearchResults));
        mBinding.searchResultsContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
	}
    
    private void handleDiagnosticsResultVisibility(boolean errorVisible) {
        mBinding.diagEmptyView.setVisibility(errorVisible ? View.VISIBLE : View.GONE);
        getDiagnosticsList().setVisibility(errorVisible ? View.GONE : View.VISIBLE);
    }

    private void handleSearchResultVisibility(boolean errorVisible) {
        mBinding.searchEmptyView.setVisibility(errorVisible ? View.VISIBLE : View.GONE);
        getSearchResultList().setVisibility(errorVisible ? View.GONE : View.VISIBLE);
    }
	
	private MaterialContainerTransform createContainerTransformFor(View start, View end) {
		return createContainerTransformFor(start, end, mBinding.realContainer);
	}
	
	private MaterialContainerTransform createContainerTransformFor(View start, View end, View drawingView) {
		return TransformUtils.createContainerTransformFor(start, end, drawingView);
	}

	private void install(File apk) {
		if(apk.exists()) {
			Intent i = IntentUtils.getInstallAppIntent(apk);
			if(i != null) {
				startActivity(i);
			}
		}
	}

	private void createServices() {
		new TaskExecutor().executeAsync(() -> {
			getApp().createCompletionService(mProject, EditorActivity.this);
			return null;
		}, __ -> {
			setStatus(getString(getApp().areCompletorsStarted() ? R.string.msg_service_started : R.string.msg_starting_completion_failed));
		});
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
    public void onDiagnosticClick(File file, Diagnostic diagnostic) {
        openFileAndSelect(file, diagnostic.range);
        hideDiagnostics();
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
        boolean error = params == null || params.diagnostics == null || params.diagnostics.size() <= 0;
        handleDiagnosticsResultVisibility(error);
        
        if(error) return;
        
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
        if(editor != null)
            editor.setJavaColors(colors);
    }

    @Override
    public void signatureHelp(SignatureHelp signature, File file) {
        if(signature == null || signature.signatures == null) {
            mBinding.symbolText.setVisibility(View.GONE);
            return;
        }
        SignatureInformation info = signature.signatures.get(signature.activeSignature);
        mBinding.symbolText.setText(formatSignature(info, signature.activeParameter));
        final EditorFragment frag = mPagerAdapter.findEditorByFile(file);
        if(frag != null) {
            final CodeEditor editor = frag.getEditor();
            final float[] cursor = editor.getCursorPosition();
            
            float x = editor.updateCursorAnchor() - (mBinding.symbolText.getWidth() / 2);
            float y = mBinding.editorAppBarLayout.getHeight() + (cursor[0] - editor.getRowHeight() - editor.getOffsetY() - mBinding.symbolText.getHeight());
            mBinding.symbolText.setX(x);
            mBinding.symbolText.setY(y);
            mBinding.symbolText.setVisibility(View.VISIBLE);
            
              //////////////////////////////////////////////
             //// Position the signature text properly ////
            //////////////////////////////////////////////
            Rect r = new Rect();
            int width = mBinding.symbolText.getWidth();
            x = mBinding.symbolText.getX();
            mBinding.symbolText.getWindowVisibleDisplayFrame(r);
            if(r.width() != width) {
                if(x < r.left) {
                    x = SizeUtils.dp2px(8); // an offset of 8dp from the left edge of screen
                    mBinding.symbolText.setX(x);
                }
                
                if(x + width > r.right) {
                    x = r.right - SizeUtils.dp2px(8) - width; // position to the right but leaving 8dp space from the right edge of screen
                    mBinding.symbolText.setX(x); 
                }
            }
        }
    }

    private CharSequence formatSignature(SignatureInformation signature, int paramIndex) {
        String name = signature.label;
        name = name.substring(0, name.indexOf("("));
        
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(name, new ForegroundColorSpan(0xffffffff), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("(", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
         
        List<ParameterInformation> params = signature.parameters;
        for(int i=0;i<params.size();i++) {
            int color = i == paramIndex ? 0xffff6060 : 0xffffffff;
            final ParameterInformation info = params.get(i);
            if(i == params.size() - 1) {
                sb.append(info.label, new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                sb.append(info.label, new ForegroundColorSpan(color), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(",", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append(" ");
            }
        }
        sb.append(")", new ForegroundColorSpan(0xff4fc3f7), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public void hideSignature() {
        mBinding.symbolText.setVisibility(View.GONE);
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

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.didChangeWatchedFiles(params);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }

    @Override
    public void signatureHelp(TextDocumentPositionParams params, File file) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server != null)
            server.signatureHelp(params, file);
        else addPendingMessage(createMessage(JavaLanguageServer.Method.DID_SAVE, gson.toJson(params)));
    }

    @Override
    public void findDefinition(TextDocumentPositionParams params) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server == null) return;
        server.findDefinition(params);
        getProgressSheet(R.string.msg_finding_definition).show(getSupportFragmentManager(), "definition_progress");
    }

    @Override
    public void gotoDefinition(List<Location> locations) {
        if(mSearchingProgress != null && mSearchingProgress.isShowing())
            mSearchingProgress.dismiss();
        if(locations == null || locations.size() <= 0) {
            getApp().toast(R.string.msg_no_definition, Toaster.Type.ERROR);
            return;
        }
        Location loc = locations.get(0);
        final File file = new File(loc.uri);
        final Range range = loc.range;
        try {
            if(mPagerAdapter == null) return;
            if(mPagerAdapter.getCount() <= 0) {
                openFile(file, range);
                return;
            }
            EditorFragment frag = mPagerAdapter.getFrag(mBinding.tabs.getSelectedTabPosition());
            if(frag != null
            && frag.getFile() != null
            && frag.getEditor() != null
            && frag.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                if(range.start.equals(range.end)) {
                    frag.getEditor().setSelection(range.start.line, range.start.column);
                } else {
                    frag.getEditor().setSelectionRegion(range.start.line, range.start.column, range.end.line, range.end.column);
                }
            } else {
                openFileAndSelect(file, range);
            }
        } catch (Throwable th) {
            Logger.instance().error(ThrowableUtils.getFullStackTrace(th));
        }
    }

    private void openFileAndSelect(File file, Range range) {
        openFile(file, range);
        EditorFragment opened = mPagerAdapter.findEditorByFile(file);
        if (opened != null && opened.getEditor() != null) {
            CodeEditor editor = opened.getEditor();
            editor.post(() -> {
                if (range.start.equals(range.end)) {
                    editor.setSelection(range.start.line, range.start.column);
                } else {
                    editor.setSelectionRegion(range.start.line, range.start.column, range.end.line, range.end.column);
                }
            });
        }
    }

    @Override
    public void findReferences(ReferenceParams params) {
        final JavaLanguageServer server = getApp().getJavaLanguageServer();
        if(server == null) return;
        server.findReferences(params);
        getProgressSheet(R.string.msg_finding_references).show(getSupportFragmentManager(), "references_progress");
    }

    @Override
    public void references(List<Location> references) {
        if(mSearchingProgress != null && mSearchingProgress.isShowing())
            mSearchingProgress.dismiss();
            
        boolean error = references == null || references.size() <= 0;
        handleSearchResultVisibility(error);
        
        
        if(error) {
            getApp().toast(R.string.msg_no_references, Toaster.Type.INFO);
            getSearchResultList().setAdapter(new SearchListAdapter(null, null, null));
            return;
        }
        
        final Map<File, List<SearchResult>> results = new HashMap<>();
        for(int i=0;i<references.size();i++) {
            try {
                final Location loc = references.get(i);
                if(loc == null || loc.uri == null || loc.range == null) continue;
                final File file = new File(loc.uri);
                if(!file.exists() || !file.isFile()) continue;
                EditorFragment frag = mPagerAdapter.findEditorByFile(file);
                Content content;
                if(frag != null && frag.getEditor() != null)
                    content = frag.getEditor().getText();
                else content = new Content(null, FileIOUtils.readFile2String(file));
                final List<SearchResult> matches = results.containsKey(file) ? results.get(file) : new ArrayList<>();
                matches.add(
                    new SearchResult(
                        loc.range,
                        file,
                        content.getLineString(loc.range.start.line),
                        content.subContent(
                            loc.range.start.line,
                            loc.range.start.column,
                            loc.range.end.line,
                            loc.range.end.column
                        ).toString()
                    )
                );
                results.put(file, matches);
            } catch (Throwable th) {
                Logger.instance().error(ThrowableUtils.getFullStackTrace(th));
            }
        }
        
        handleSearchResults(results);
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
    
    private void notifyFileCreated(File file) {
        if(!file.getName().endsWith(".java")) return;
        DidChangeWatchedFilesParams p = new DidChangeWatchedFilesParams();
        p.changes.add(new FileEvent(file.toURI(), FileChangeType.Created));
        didChangeWatchedFiles(p);
        
        openFile(file);
    }
    
    private void notifyFileDeleted(File file) {
        if(!file.getName().endsWith(".java")) return;
        DidChangeWatchedFilesParams p = new DidChangeWatchedFilesParams();
        p.changes.add(new FileEvent(file.toURI(), FileChangeType.Deleted));
        didChangeWatchedFiles(p);
    }
	
	/*****************************
	 **** Tab Related ************
	 *****************************/
	 
	@Override
	public void onTabSelected(TabLayout.Tab p1) {
		EditorFragment current = mPagerAdapter.getFrag(p1.getPosition());
		if(current != null && current.getFile() != null) {
			this.mCurrentFragment = current;
			this.mCurrentFile = current.getFile();
			refreshSymbolInput(current);
		}
        
        invalidateOptionsMenu();
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
		} else if(id == R.id.editornav_close_project) {
            confirmProjectClose();
        }
        mBinding.getRoot().closeDrawer(GravityCompat.START);
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
	// -------------------------------
    
	
	public void loadFragment(Fragment fragment) {
		super.loadFragment(fragment, mBinding.editorFrameLayout.getId());
	}
    
    private ProjectInfoSheet getProjectInfoSheet() {
        return mProjectInfoSheet == null ? createProjectInfoSheet() : mProjectInfoSheet;
    }
    
    private ProjectInfoSheet createProjectInfoSheet() {
        mProjectInfoSheet = new ProjectInfoSheet();
        mProjectInfoSheet.setProject(mIDEProject);
        return mProjectInfoSheet;
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
    
    private ProgressSheet getProgressSheet(int msg) {
        if(mSearchingProgress != null && mSearchingProgress.isShowing()) {
            mSearchingProgress.dismiss();
        }
        mSearchingProgress = new ProgressSheet();
        mSearchingProgress.setCancelable(false);
        mSearchingProgress.setWelcomeTextEnabled(false);
        mSearchingProgress.setMessage(getString(msg));
        mSearchingProgress.setSubMessageEnabled(false);
        return mSearchingProgress;
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
		buildView.setEditorLanguage(new BuildOutputLanguage());
		buildView.setOverScrollEnabled(false);
		buildView.setTextActionMode(CodeEditor.TextActionMode.ACTION_MODE);
		buildView.setWordwrap(false);
		buildView.setUndoEnabled(false);
		buildView.setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
		buildView.setTypefaceText(TypefaceUtils.jetbrainsMono());
		buildView.setTextSize(12);
        buildView.setColorScheme(new SchemeAndroidIDE());
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
        buildView.setColorScheme(new SchemeAndroidIDE());
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
    
    private RecyclerView getSearchResultList() {
        return searchResultList == null ? createSearchResultList() : searchResultList;
    }
    
    private RecyclerView createSearchResultList() {
        searchResultList = new RecyclerView(this);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        return searchResultList;
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
		openFile(file, null);
	}
    
    public EditorFragment openFile(File file, Range selection) {
        if(selection == null) selection = Range.ofZero();
        int i = mPagerAdapter.openFile(file, selection, this, this);
        if(i >= 0 && !mBinding.tabs.getTabAt(i).isSelected())
            mBinding.tabs.getTabAt(i).select();
        mBinding.editorDrawerLayout.closeDrawer(GravityCompat.END);
        invalidateOptionsMenu();
        try {
            return mPagerAdapter.getFrag(i);
        } catch (Throwable th) {
            return null;
        }
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
        return saveAll(true);
    }
    
    private boolean saveAll(boolean notify) {
        return saveAll(notify, false);
    }
    
	private boolean saveAll(boolean notify, boolean canProcessResources) {
		SaveResult result = mPagerAdapter.saveAll();
        if(notify) {
            getApp().toast(R.string.all_saved, Toaster.Type.SUCCESS);
        }
		if(result.gradleSaved){
            notifySyncNeeded();
        }
        LOG.info(
            "saveAll()",
            "notify:" + notify,
            "processResources:" + canProcessResources,
            "xmlSaved:" + result.xmlSaved,
            "gradleSaved:" + result.gradleSaved);
        if(result.xmlSaved && canProcessResources && getBuildService() != null) {
            getBuildService().processDebugResources();
        }
		return result.gradleSaved;
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
                    notifyFileCreated(file);
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
                notifyFileDeleted(f);
				if(mLastHolded != null) {
					TreeNode parent = mLastHolded.getParent();
					parent.deleteChild(mLastHolded);
					mFileTreeFragment.expandNode(parent);
				} else {
					mFileTreeFragment.listProjectFiles();
				}
                EditorFragment frag = mPagerAdapter.findEditorByFile(f);
                if(frag != null) {
                    closeFile(mPagerAdapter.getFragments().indexOf(frag));
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
		final EditorFragment frag = mPagerAdapter.getFrag(mBinding.tabs.getSelectedTabPosition());
		final File file = mPagerAdapter.getOpenedFiles().get(mBinding.tabs.getSelectedTabPosition());
        try { frag.save(); } catch (Throwable th) { }
		mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), mProject);
		mBinding.editorViewPager.setAdapter(mPagerAdapter);
		mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
		mBinding.tabs.addOnTabSelectedListener(this);
        openFile(file);
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
        
        if(mPagerAdapter.getCount() <= 0) {
            mCurrentFragment = null;
            mCurrentFile = null;
        }
        
        invalidateOptionsMenu();
	}
    
    private static final Logger LOG = Logger.instance("EditorActivity");
}
