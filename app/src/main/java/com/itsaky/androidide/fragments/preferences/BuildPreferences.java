package com.itsaky.androidide.fragments.preferences;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutDialogTextInputBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.util.List;
import java.util.Set;

import static com.itsaky.androidide.utils.PreferenceManager.*;

public class BuildPreferences extends BasePreferenceFragment implements Preference.OnPreferenceClickListener {
	
	public static final String KEY_GRADLE_INSTALL = "idepref_build_gradleInstall";
	public static final String KEY_GRADLE_COMMAMDS = "idepref_build_gradleCommands";
	public static final String KEY_GRADLE_CLEAR_CACHE = "idepref_build_gradleClearCache";
	public static final String KEY_CHOOSE_GRADLE = "idepref_build_chooseGradle";
	
	private ProgressSheet progressSheet;
	
	@Override
	public void onCreatePreferences(Bundle p1, String p2) {
		super.onCreatePreferences(p1, p2);
		if(getContext() == null) return;
		final PreferenceScreen screen = getPreferenceScreen();
		final PreferenceCategory categoryGradle = new PreferenceCategory(getContext());
		final Preference customGradle = new Preference(getContext());
		final Preference chooseGradle = new Preference(getContext());
		final Preference customCommands = new Preference(getContext());
		final Preference clearCache = new Preference(getContext());
		
		screen.addPreference(categoryGradle);
		
		customGradle.setKey(KEY_GRADLE_INSTALL);
		customGradle.setIcon(R.drawable.ic_language_gradle);
		customGradle.setTitle(R.string.idepref_build_installgradle_title);
		customGradle.setSummary(R.string.idepref_build_installgradle_summary);
		
		chooseGradle.setKey(KEY_CHOOSE_GRADLE);
		chooseGradle.setIcon(R.drawable.ic_file_rename);
		chooseGradle.setTitle(R.string.idepref_build_choosegradle_title);
		chooseGradle.setSummary(R.string.idepref_build_choosegradle_summary);
		
		customCommands.setKey(KEY_GRADLE_COMMAMDS);
		customCommands.setIcon(R.drawable.ic_bash_commands);
		customCommands.setTitle(R.string.idepref_build_customgradlecommands_title);
		customCommands.setSummary(R.string.idepref_build_customgradlecommands_summary);
		
		clearCache.setKey(KEY_GRADLE_CLEAR_CACHE);
		clearCache.setIcon(R.drawable.ic_file_delete);
		clearCache.setTitle(R.string.idepref_build_clearCache_title);
		clearCache.setSummary(R.string.idepref_build_clearCache_summary);
		
		categoryGradle.setTitle(R.string.gradle);
//		categoryGradle.addPreference(customGradle);
//		categoryGradle.addPreference(chooseGradle);
		categoryGradle.addPreference(customCommands);
		categoryGradle.addPreference(clearCache);
		
		setPreferenceScreen(screen);
		
		customGradle.setOnPreferenceClickListener(this);
		chooseGradle.setOnPreferenceClickListener(this);
		customCommands.setOnPreferenceClickListener(this);
		clearCache.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference p1) {
		final String key = p1.getKey();
		if(key.equals(KEY_GRADLE_INSTALL)) {
			showInstallGradleDialog();
		} else if(key.equals(KEY_GRADLE_COMMAMDS)) {
			showGradleCommandsDialog();
		} else if(key.equals(KEY_GRADLE_CLEAR_CACHE)) {
			showClearCacheDialog();
		} else if(key.equals(KEY_CHOOSE_GRADLE)) {
			showChooseGradle();
		}
		return true;
	}

	private void showChooseGradle() {
		final Set<String> installed = getPrefManager().getGradleFolderNames();
		final String[] labels = installed.toArray(new String[installed.size()]);
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
		builder.setTitle(R.string.idepref_build_choosegradle_title);
		builder.setSingleChoiceItems(labels, 0, (p1, p2) -> {
			DialogInterface iface = p1;
			int pos = p2;
		});
		builder.create().show();
	}

	private void showInstallGradleDialog() {
		final LayoutDialogTextInputBinding binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(getContext()));
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
		binding.name.setHint(R.string.zip_file_path);
		builder.setTitle(R.string.idepref_build_installgradle_title);
		builder.setMessage(R.string.msg_custom_gradle);
		builder.setView(binding.getRoot());
		builder.setPositiveButton(android.R.string.ok, (p1, p2) -> {
			final String path = binding.name.getEditText().getText().toString();
			if(path.trim().length() <= 0) {
				StudioApp.getInstance().toast(R.string.invalid_zip, Toaster.Type.ERROR);
				return;
			}
			File file = new File(path.trim());
			if(!(file.exists() && file.isFile() && file.getName().endsWith(".zip"))) {
				StudioApp.getInstance().toast(R.string.msg_file_should_be_zip, Toaster.Type.ERROR);
				return;
			}
			p1.dismiss();
			getProgressSheet().setCancelable(false);
			getProgressSheet().setShowShadow(false);
			getProgressSheet().setSubMessageEnabled(false);
			getProgressSheet().setWelcomeTextEnabled(false);
			getProgressSheet().show(getChildFragmentManager(), "custom_gradle_progress");
			new TaskExecutor().executeAsync(() -> {
				try {
					List<File> extracted = ZipUtils.unzipFile(file, Environment.HOME);
					if(extracted != null && extracted.size() > 0) {
						File newestFolder = null;
						for(File f : extracted) {
							if((newestFolder = f.getParentFile()).getAbsolutePath().equals(Environment.HOME.getAbsolutePath())) {
								break;
							}
						}
						if(newestFolder != null) {
							getPrefManager().setCurentGradleFolderName(newestFolder.getName());
							Environment.GRADLE_FOLDER_NAME = newestFolder.getName();
							Environment.updateGradleDir();
							StudioApp.getInstance().toast(R.string.msg_gradle_installed, Toaster.Type.SUCCESS);
							getProgressSheet().dismiss();
						}
					} else {
						StudioApp.getInstance().toast(R.string.failed_unzip, Toaster.Type.ERROR);
					}
				} catch (Throwable th) {
					StudioApp.getInstance().toast(R.string.failed_unzip, Toaster.Type.ERROR);
				}
				return null;
			}, __ -> {});
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.create().show();
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
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
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
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
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
		File file = new File(Environment.HOME, ".gradle/caches");
		if(file.exists()) {
			FileUtils.delete(file);
		}
		return null;
	}
	
	private ProgressSheet getProgressSheet() {
		return progressSheet == null ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait)) : progressSheet;
	}
}
