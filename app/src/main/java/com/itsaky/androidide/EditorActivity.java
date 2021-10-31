package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
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
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.adapters.EditorPagerAdapter;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityEditorBinding;
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding;
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.fragments.FileTreeFragment;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.fragments.sheets.ProjectInfoSheet;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.handlers.BuildServiceHandler;
import com.itsaky.androidide.handlers.FileOptionsHandler;
import com.itsaky.androidide.handlers.IDEHandler;
import com.itsaky.androidide.interfaces.DiagnosticClickListener;
import com.itsaky.androidide.interfaces.EditorActivityProvider;
import com.itsaky.androidide.language.logs.LogLanguageImpl;
import com.itsaky.androidide.lsp.LSP;
import com.itsaky.androidide.lsp.LSPProvider;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.DiagnosticGroup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.models.SaveResult;
import com.itsaky.androidide.models.SearchResult;
import com.itsaky.androidide.models.SheetOption;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.receivers.LogReceiver;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.RecursiveFileSearcher;
import com.itsaky.androidide.utils.Symbols;
import com.itsaky.androidide.utils.TransformUtils;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.views.MaterialBanner;
import com.itsaky.androidide.views.SymbolInputView;
import com.itsaky.lsp.services.IDELanguageServer;
import com.itsaky.toaster.Toaster;
import com.unnamed.b.atv.model.TreeNode;
import io.github.rosemoe.editor.langs.EmptyLanguage;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.Position;

public class EditorActivity extends StudioActivity implements FileTreeFragment.FileActionListener,
														TabLayout.OnTabSelectedListener,
														NavigationView.OnNavigationItemSelectedListener,
                                                        DiagnosticClickListener, 
                                                        EditorFragment.FileOpenListener,
                                                        EditorPagerAdapter.EditorStateListener,
                                                        IDEHandler.Provider,
                                                        EditorActivityProvider {
    
    private ActivityEditorBinding mBinding;
    private LayoutDiagnosticInfoBinding mDiagnosticInfoBinding;
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
    
    private BuildServiceHandler mBuildServiceHandler;
    private FileOptionsHandler mFileOptionsHandler;
    
	private LogLanguageImpl mLogLanguageImpl;
	
	private QuickAction mTabCloseAction;
	private TextSheetFragment mDaemonStatusFragment;
	private OptionsListFragment mFileOptionsFragment;
    private ProgressSheet mSearchingProgress;
    private ProjectInfoSheet mProjectInfoSheet;
    private AlertDialog mFindInProjectDialog;
    
	private static final String TAG_FILE_OPTIONS_FRAGMENT = "file_options_fragment";
    public static final String TAG = "EditorActivity";
    
    private static final org.eclipse.lsp4j.Range Range_ofZero = new org.eclipse.lsp4j.Range(new Position(0, 0), new Position(0, 0));
	
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
        mDiagnosticInfoBinding = mBinding.diagnosticInfo;
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
        
        symbolInput = new SymbolInputView(this);
        mBinding.inputContainer.addView(symbolInput, 1, new ViewGroup.LayoutParams(-1, -2));
        
        mBinding.editorViewPager.setOffscreenPageLimit(9);
        mBinding.editorViewPager.setAdapter(mPagerAdapter);
        mBinding.tabs.setupWithViewPager(mBinding.editorViewPager);
        mBinding.tabs.setOnTabSelectedListener(this);
		mBinding.fabView.setOnClickListener(v -> showViewOptions());
        
		createQuickActions();
        
        mBuildServiceHandler = new BuildServiceHandler(this);
        mFileOptionsHandler = new FileOptionsHandler(this);
        
        startServices();
        
		KeyboardUtils.registerSoftInputChangedListener(this, __ -> onSoftInputChanged());
		registerLogReceiver();
		setupContainers();
        setupSignatureText();
        setupDiagnosticInfo();
        
        startLanguageServers();
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

    @Override
    public EditorActivity provide() {
        return this;
    }
    
    @Override
    public EditorActivity provideEditorActivity() {
        return this;
    }

    @Override
    public AndroidProject provideAndroidProject() {
        return mProject;
    }

    @Override
    public IDEProject provideIDEProject() {
        return mIDEProject;
    }
    
    @Override
    public void editorStateChanged() {
        for(int i=0;i<mBinding.tabs.getTabCount();i++) {
            try {
                mBinding.tabs.getTabAt(i).setText(mPagerAdapter.getPageTitle(i));
            } catch (Throwable th) {}
        }
    }
    
    public void handleSearchResults(Map<File, List<SearchResult>> results) {
        getSearchResultList().setAdapter(new com.itsaky.androidide.adapters.SearchListAdapter(results, file -> {
            openFile(file);
            hideSearchResults();
            hideViewOptions();
        }, match -> {
            openFileAndSelect(match.file, match);
            hideSearchResults();
            hideViewOptions();
        }));
        mBinding.transformScrim.setVisibility(View.VISIBLE);
        mBinding.fabView.setVisibility(View.GONE);
        showSearchResults();

        if(mSearchingProgress != null && mSearchingProgress.isShowing())
            mSearchingProgress.dismiss();
    }

    public void appendApkLog(LogLine line) {
        getLogLanguage().addLine(line);
        appendLogOut(line.toString());
    }

    public void setIDEProject (IDEProject project) {
        this.mIDEProject = project;
    }

    public void appendBuildOut(final String str) {
        runOnUiThread(() -> {
            String strFinal = str.endsWith("\n") ? str : str.concat("\n");
            getBuildView(true).getText().append(strFinal);
        });
    }

    public void appendLogOut(final String str) {
        runOnUiThread(() -> {
            String strFinal = str.endsWith("\n") ? str : str.concat("\n");
            getLogView(true).getText().append(strFinal);
        });
    }

    public void showFiles() {
        if(mBinding.viewOptionsCard.getVisibility() == View.VISIBLE) {
            hideViewOptions();
        }

        mBinding.getRoot().openDrawer(GravityCompat.END, true);
    }

    public void showDaemonStatus() {
        if(mBinding.viewOptionsCard.getVisibility() == View.VISIBLE) {
            hideViewOptions();
        }
        ShellServer shell = getApp().newShell(t -> getDaemonStatusFragment().append(t.toString()));
        shell.bgAppend(String.format("echo '%s'", getString(R.string.msg_getting_daemom_status)));
        shell.bgAppend(String.format("cd '%s' && sh gradlew --status", mProject.getProjectPath()));
        if(!getDaemonStatusFragment().isShowing())
            getDaemonStatusFragment().show(getSupportFragmentManager(), "daemon_status");
    }

    public void hideToolbarAndTabs() {
        mBinding.editorAppBarLayout.setVisibility(View.INVISIBLE);
    }

    public void showToolbarAndTabs() {
        mBinding.editorAppBarLayout.setVisibility(View.VISIBLE);
    }

    public void showViewOptions() {
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

    public void hideViewOptions() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewOptionsCard, mBinding.fabView));
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        mBinding.transformScrim.setVisibility(View.GONE);
        mBinding.fabView.setVisibility(View.VISIBLE);
    }

    public void showBuildResult() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewBuildOut, mBinding.buildContainer));
        mBinding.buildContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    public void hideBuildResult() {
        showToolbarAndTabs();
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.buildContainer, mBinding.viewBuildOut));
        mBinding.buildContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
    }

    public void showLogResult() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewLogs, mBinding.logContainer));
        mBinding.logContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    public void hideLogResult() {
        showToolbarAndTabs();

        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.logContainer, mBinding.viewLogs));
        mBinding.logContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
    }

    public void showDiagnostics() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewDiags, mBinding.diagContainer));
        mBinding.diagContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    public void hideDiagnostics() {
        showToolbarAndTabs();
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.diagContainer, mBinding.viewDiags));
        mBinding.diagContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
    }

    public void showSearchResults() {
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.viewSearchResults, mBinding.searchResultsContainer));
        mBinding.searchResultsContainer.setVisibility(View.VISIBLE);
        mBinding.viewOptionsCard.setVisibility(View.GONE);
        hideToolbarAndTabs();
    }

    public void hideSearchResults() {
        showToolbarAndTabs();
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), createContainerTransformFor(mBinding.searchResultsContainer, mBinding.viewSearchResults));
        mBinding.searchResultsContainer.setVisibility(View.GONE);
        mBinding.viewOptionsCard.setVisibility(View.VISIBLE);
    }

    public void handleDiagnosticsResultVisibility(boolean errorVisible) {
        mBinding.diagEmptyView.setVisibility(errorVisible ? View.VISIBLE : View.GONE);
        getDiagnosticsList().setVisibility(errorVisible ? View.GONE : View.VISIBLE);
    }

    public void handleSearchResultVisibility(boolean errorVisible) {
        mBinding.searchEmptyView.setVisibility(errorVisible ? View.VISIBLE : View.GONE);
        getSearchResultList().setVisibility(errorVisible ? View.GONE : View.VISIBLE);
    }
    
    public void setStatus(final CharSequence text) {
        try {
            runOnUiThread(() -> mBinding.editorStatusText.setText(text));
        } catch (Throwable th) {}
    }

    public void showFirstBuildNotice() {
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
            hideViewOptions();
        }
    }

    @Override
    public void onDiagnosticClick(File file, Diagnostic diagnostic) {
        openFileAndSelect(file, diagnostic.getRange());
        hideDiagnostics();
        hideViewOptions();
    }

    public EditorPagerAdapter getPagerAdapter(){
        return mPagerAdapter;
    }

    public ActivityEditorBinding getBinding() {
        return mBinding;
    }
    
    public LayoutDiagnosticInfoBinding getDiagnosticBinding() {
        return mDiagnosticInfoBinding;
    }

    /**
     * Positions the view to the provided coordinates and within screen
     *
     * @param view View to position
     * @param initialX Initial X coordinate of view
     * @param initialY Initial Y coordinate of view
     */
    public void positionViewWithinScreen(final View view, final float initialX, final float initialY) {
        view.setX(initialX);
        view.setY(initialY);

        final Rect r = new Rect();
        final int width = view.getWidth();
        view.getWindowVisibleDisplayFrame(r);
        if (r.width() != width) {

            /**
             * Will be true when the view is going out of screen to left 
             */
            if (initialX < r.left) {
                view.setX(SizeUtils.dp2px(8)); // an offset of 8dp from the left edge of screen
            }

            /**
             * Will be true when the view is going out of screen to right
             */
            if (initialX + width > r.right) {
                view.setX(r.right - SizeUtils.dp2px(8) - width);  // position to the right but leaving 8dp space from the right edge of screen
            }
        }
    }

    public void openFileAndSelect(File file, org.eclipse.lsp4j.Range range) {
        openFile(file, range);
        EditorFragment opened = mPagerAdapter.findEditorByFile(file);
        if (opened != null && opened.getEditor() != null) {
            CodeEditor editor = opened.getEditor();
            editor.post(() -> {
                if (LSPUtils.isEqual(range.getStart(), range.getEnd())) {
                    editor.setSelection(range.getStart().getLine(), range.getEnd().getCharacter());
                } else {
                    editor.setSelectionRegion(range.getStart().getLine(), range.getStart().getCharacter(), range.getEnd().getLine(), range.getEnd().getCharacter());
                }
            });
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        EditorFragment current = mPagerAdapter.getFrag(tab.getPosition());
        if(current != null && current.getFile() != null) {
            this.mCurrentFragment = current;
            this.mCurrentFile = current.getFile();
            refreshSymbolInput(current);
        }
        
        invalidateOptionsMenu();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        EditorFragment frag = mPagerAdapter.getFrag(tab.getPosition());
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
        } else if (id == R.id.editornav_terminal) {
            openTerminal();
        }
        mBinding.getRoot().closeDrawer(GravityCompat.START);
        return false;
	}
    
    public void loadFragment(Fragment fragment) {
        super.loadFragment(fragment, mBinding.editorFrameLayout.getId());
    }

    public ProjectInfoSheet getProjectInfoSheet() {
        return mProjectInfoSheet == null ? createProjectInfoSheet() : mProjectInfoSheet;
    }

    public ProjectInfoSheet createProjectInfoSheet() {
        mProjectInfoSheet = new ProjectInfoSheet();
        mProjectInfoSheet.setProject(mIDEProject);
        return mProjectInfoSheet;
    }
    
    public FileTreeFragment getFileTreeFragment() {
        return mFileTreeFragment;
    }
    
    public TreeNode getLastHoldTreeNode() {
        return mLastHolded;
    }
    
    public OptionsListFragment getFileOptionsFragment() {
        return mFileOptionsFragment;
    }
    
    public OptionsListFragment getFileOptionsFragment(File file) {
        mFileOptionsFragment = new OptionsListFragment();
        mFileOptionsFragment.addOption(new SheetOption(0, R.drawable.ic_file_copy_path, R.string.copy_path, file));
        mFileOptionsFragment.addOption(new SheetOption(1, R.drawable.ic_file_rename, R.string.rename_file, file));
        mFileOptionsFragment.addOption(new SheetOption(2, R.drawable.ic_file_delete, R.string.delete_file, file));
        if(file.isDirectory()) {
            mFileOptionsFragment.addOption(new SheetOption(3, R.drawable.ic_new_file, R.string.new_file, file));
            mFileOptionsFragment.addOption(new SheetOption(4, R.drawable.ic_new_folder, R.string.new_folder, file));
        }
        mFileOptionsFragment.setOnOptionsClickListener(mFileOptionsHandler);
        return mFileOptionsFragment;
    }

    public ProgressSheet getProgressSheet() {
        return mSearchingProgress;
    }

    public ProgressSheet getProgressSheet(int msg) {
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

    public MaterialBanner getSyncBanner() {
        return mBinding.syncBanner.setContentTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
            .setBannerBackgroundColor(ContextCompat.getColor(this, R.color.primaryLightColor))
            .setButtonTextColor(ContextCompat.getColor(this, R.color.secondaryColor))
            .setIcon(R.drawable.ic_sync)
            .setContentText(R.string.msg_sync_needed);
    }

    public TextSheetFragment getDaemonStatusFragment() {
        return mDaemonStatusFragment == null ? mDaemonStatusFragment = new TextSheetFragment().setTextSelectable(true).setTitleText(R.string.gradle_daemon_status) : mDaemonStatusFragment;
    }

    public CodeEditor getBuildView() {
        return getBuildView(false);
    }

    public CodeEditor getBuildView(boolean setup) {
        CodeEditor edit = buildView == null ? createBuildView() : buildView;
        if(setup && edit.getParent() == null) {
            setupContainers();
        }
        return edit;
    }

    public CodeEditor createBuildView() {
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
        buildView.setColorScheme(new SchemeAndroidIDE());
        return buildView;
    }

    public CodeEditor getLogView() {
        return getLogView(false);
    }

    public CodeEditor getLogView(boolean setup) {
        CodeEditor edit = logView == null ? createLogView() : logView;
        if(setup && edit.getParent() == null) {
            setupContainers();
        }
        return edit;
    }

    public CodeEditor createLogView() {
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

    public RecyclerView getDiagnosticsList() {
        return diagnosticList == null ? createDiagnosticsList() : diagnosticList;
    }

    public RecyclerView createDiagnosticsList() {
        diagnosticList = new RecyclerView(this);
        diagnosticList.setLayoutManager(new LinearLayoutManager(this));
        diagnosticList.setAdapter(new DiagnosticsAdapter(new ArrayList<DiagnosticGroup>(), this));
        return diagnosticList;
    }

    public RecyclerView getSearchResultList() {
        return searchResultList == null ? createSearchResultList() : searchResultList;
    }

    public RecyclerView createSearchResultList() {
        searchResultList = new RecyclerView(this);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        return searchResultList;
    }

    public LogLanguageImpl getLogLanguage() {
        return mLogLanguageImpl == null ? mLogLanguageImpl = new LogLanguageImpl() : mLogLanguageImpl;
    }

    @Override
    public EditorFragment openFile(File file) {
        return openFile(file, null);
    }

    public EditorFragment openFile(File file, org.eclipse.lsp4j.Range selection) {
        if(selection == null) selection = Range_ofZero;
        int i = mPagerAdapter.openFile(file, selection, this);
        if(i >= 0 && !mBinding.tabs.getTabAt(i).isSelected())
            mBinding.tabs.getTabAt(i).select();
        
        if(mBinding.editorDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mBinding.editorDrawerLayout.closeDrawer(GravityCompat.END);
        }
        
        invalidateOptionsMenu();
        try {
            return mPagerAdapter.getFrag(i);
        } catch (Throwable th) {
            return null;
        }
    }

    @Override
    public void onOpenSuccessful(File file, String text) {
        // textDocument/didOpen is now handled by CodeEditor
    }

    @Override
    public void showFileOptions(File thisFile, TreeNode node) {
        mLastHolded = node;
        getFileOptionsFragment(thisFile).show(getSupportFragmentManager(), TAG_FILE_OPTIONS_FRAGMENT);
    }

    public boolean saveAll() {
        return saveAll(true);
    }

    public boolean saveAll(boolean notify) {
        return saveAll(notify, false);
    }

    public boolean saveAll(boolean notify, boolean canProcessResources) {
        SaveResult result = mPagerAdapter.saveAll();
        if(notify) {
            getApp().toast(R.string.all_saved, Toaster.Type.SUCCESS);
        }
        if(result.gradleSaved){
            notifySyncNeeded();
        }
        if(result.xmlSaved && canProcessResources && getBuildService() != null) {
            getBuildService().updateResourceClasses();
        }
        return result.gradleSaved;
	}
    
    public void install(File apk) {
        if(apk.exists()) {
            Intent i = IntentUtils.getInstallAppIntent(apk);
            if(i != null) {
                startActivity(i);
            }
        }
    }

    public void createServices() {
        new TaskExecutor().executeAsync(() -> {
            IDELanguageServer javaServer = LSPProvider.getServerForLanguage(LSPProvider.LANGUAGE_JAVA);
            if(javaServer == null) return null;

            List<String> cps = mProject.getClassPaths();
            JsonObject settings = new JsonObject();
            JsonObject java = new JsonObject();
            JsonArray classPath = new JsonArray();

            for(int i=0;i<cps.size();i++) {
                classPath.add(cps.get(i));
            }

            java.add("classPath", classPath);
            settings.add("java", java);

            DidChangeConfigurationParams params = new DidChangeConfigurationParams();
            params.setSettings(settings);

            javaServer.getWorkspaceService().didChangeConfiguration(params);

            return null;
        }, __ -> {
            setStatus(getString(getApp().areCompletorsStarted() ? R.string.msg_service_started : R.string.msg_starting_completion_failed));
        });
	}
    
    public void closeFile(int index) {
        closeFile(index, true);
    }

    public void closeFile(int index, boolean selectOther) {
        mBinding.tabs.removeOnTabSelectedListener(this);

        int pos = index;
        final List<Fragment> frags = mPagerAdapter.getFragments();
        final List<File> files = mPagerAdapter.getOpenedFiles();
        
        final EditorFragment editorFragment = mPagerAdapter.getFrag(index);
        
        frags.remove(index);
        files.remove(index);
        
        if(editorFragment != null && editorFragment.getEditor() != null) {
            editorFragment.getEditor().close();
        }

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

        if(mPagerAdapter.getCount() <= 0) {
            mCurrentFragment = null;
            mCurrentFile = null;
        }

        invalidateOptionsMenu();
	}
    
       /////////////////////////////////////////////////
      ////////////// PRIVATE APIS /////////////////////
     /////////////////////////////////////////////////
     
     private void openTerminal () {
         final Intent intent = new Intent(this, TerminalActivity.class);
         intent.putExtra(TerminalActivity.KEY_WORKING_DIRECTORY, mProject.getProjectPath());
         startActivity(intent);
     }
     
    private void startLanguageServers() {
        LSP.setActivityProvider(this);
        LSP.Java.start(() -> {
            Optional<InitializeResult> result = LSP.Java.init(mProject.getProjectPath());
            LSP.Java.initialized();
        });
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
        removeFromParent(getBuildView());
        removeFromParent(getLogView());
        removeFromParent(getDiagnosticsList());
        removeFromParent(getSearchResultList());

        mBinding.buildOutcontainer.addView(getBuildView(), new ViewGroup.LayoutParams(-1, -1));
        mBinding.buildContainer.setVisibility(View.GONE);
        mBinding.buildOutToolbar.setNavigationOnClickListener(v -> hideBuildResult());
        mBinding.buildOutToolbar.setOnClickListener(v -> hideBuildResult());
        
        mBinding.buildOutToolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.menu_build_output, mBinding.buildOutToolbar.getMenu());
        mBinding.buildOutToolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.buildOut_clear) {
                getBuildView().setText("");
                setStatus("");
                return true;
            }
            return false;
        });

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
    
    private MaterialContainerTransform createContainerTransformFor(View start, View end) {
        return createContainerTransformFor(start, end, mBinding.realContainer);
    }

    private MaterialContainerTransform createContainerTransformFor(View start, View end, View drawingView) {
        return TransformUtils.createContainerTransformFor(start, end, drawingView);
    }
    
    private void startServices() {
        // Actually, we don't need to start FileOptionsHandler
        // Because it would work anyway
        // But it's a good practice to call the start() method
        mFileOptionsHandler.start();
        
        mBuildServiceHandler.start();
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

        LSP.shutdownAll();
        
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

	private void registerLogReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(LogReceiver.APPEND_LOG);
		registerReceiver(mLogReceiver, filter);
	}
    
    private IDEService getBuildService() {
        return mBuildServiceHandler.getService();
    }
    
	private void showNeedHelpDialog() {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.need_help);
		builder.setMessage(R.string.msg_need_help);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();
	}
    
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
    
    private static final Logger LOG = Logger.instance("EditorActivity");
}
