package com.itsaky.androidide.fragments.preferences;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutInstallToolsBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.fragments.sheets.TextSheetFragment;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.toaster.Toaster;
import java.io.File;

import static com.itsaky.androidide.managers.PreferenceManager.*;
import com.itsaky.androidide.utils.Logger;

public class BuildPreferences extends BasePreferenceFragment implements Preference.OnPreferenceClickListener {
	
	public static final String KEY_GRADLE_COMMAMDS = "idepref_build_gradleCommands";
	public static final String KEY_GRADLE_CLEAR_CACHE = "idepref_build_gradleClearCache";
	
	private ProgressSheet progressSheet;
	
	@Override
	public void onCreatePreferences(Bundle p1, String p2) {
		super.onCreatePreferences(p1, p2);
		if(getContext() == null) return;
		final PreferenceScreen screen = getPreferenceScreen();
		final PreferenceCategory categoryGradle = new PreferenceCategory(getContext());
		final Preference customCommands = new Preference(getContext());
		final Preference clearCache = new Preference(getContext());
		
		screen.addPreference(categoryGradle);
		
		customCommands.setKey(KEY_GRADLE_COMMAMDS);
		customCommands.setIcon(R.drawable.ic_bash_commands);
		customCommands.setTitle(R.string.idepref_build_customgradlecommands_title);
		customCommands.setSummary(R.string.idepref_build_customgradlecommands_summary);
		
		clearCache.setKey(KEY_GRADLE_CLEAR_CACHE);
		clearCache.setIcon(R.drawable.ic_file_delete);
		clearCache.setTitle(R.string.idepref_build_clearCache_title);
		clearCache.setSummary(R.string.idepref_build_clearCache_summary);
		
		categoryGradle.setTitle(R.string.gradle);
		categoryGradle.addPreference(customCommands);
		categoryGradle.addPreference(clearCache);
        
		setPreferenceScreen(screen);
        
		customCommands.setOnPreferenceClickListener(this);
		clearCache.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference p1) {
		final String key = p1.getKey();
		if(key.equals(KEY_GRADLE_COMMAMDS)) {
			showGradleCommandsDialog();
		} else if(key.equals(KEY_GRADLE_CLEAR_CACHE)) {
			showClearCacheDialog();
		}
		return true;
	}
    
	private void showGradleCommandsDialog() {
		final String[] labels = {
			"--stacktrace",
			"--info",
			"--debug",
			"--scan",
			"--warning-mode all"
		};
		final boolean[] checked = {
			getPrefManager().isStracktraceEnabled(),
			getPrefManager().isGradleInfoEnabled(),
			getPrefManager().isGradleDebugEnabled(),
			getPrefManager().isGradleScanEnabled(),
			getPrefManager().isGradleWarningEnabled()
		};
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.idepref_build_customgradlecommands_title);
		builder.setMultiChoiceItems(labels, checked, (p1, p2, p3) -> {
			boolean isChecked = p3;
			int pos = p2;
			if(pos == 0) {
				getPrefManager().setGradleStacktraceEnabled(isChecked);
			} else if(pos == 1) {
				getPrefManager().setGradleInfoEnabled(isChecked);
			} else if(pos == 2) {
				getPrefManager().setGradleDebugEnabled(isChecked);
			} else if(pos == 3) {
				getPrefManager().setGradleScanEnabled(isChecked);
			} else if(pos == 4) {
				getPrefManager().setGradleWarningEnabled(isChecked);
			}
		});
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setCancelable(false);
		builder.create().show();
	}

	private void showClearCacheDialog() {
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.idepref_build_clearCache_title);
		builder.setMessage(R.string.msg_clear_cache);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.yes, (p1, p2) -> {
			p1.dismiss();
			getProgressSheet().show(getChildFragmentManager(), "progress_sheet");
			new TaskExecutor().executeAsync(() -> deleteCaches(), __ -> {
				getProgressSheet().dismiss();
			});
		});
		builder.setNegativeButton(android.R.string.no, null);
		builder.create().show();
	}
	
	private Object deleteCaches() {
		File file = new File(Environment.GRADLE_USER_HOME, "caches");
		if(file.exists()) {
			FileUtils.delete(file);
		}
		return null;
	}
	
	private ProgressSheet getProgressSheet() {
		return progressSheet == null ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait)) : progressSheet;
	}
}
