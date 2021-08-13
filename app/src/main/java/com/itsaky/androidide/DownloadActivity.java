package com.itsaky.androidide;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.core.content.ContextCompat;
import androidx.transition.TransitionManager;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityDownloadBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ListDirectoryCallable;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.PreferenceManager;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class DownloadActivity extends StudioActivity {
	
	private ActivityDownloadBinding binding;
	private ProgressSheet progressSheet;
	private File choosenDir = null;
	
	@Override
	protected View bindLayout() {
		binding = ActivityDownloadBinding.inflate(getLayoutInflater());
		return binding.getRoot();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		OnClickListener clickListener = v -> handleClick(v);
		binding.dismiss.setOnClickListener(clickListener);
		binding.choose.setOnClickListener(clickListener);
		binding.install.setOnClickListener(clickListener);
		
		binding.install.setEnabled(false);
    }
	
	private void handleClick(View v) {
		final int id = v.getId();
		if(id == binding.dismiss.getId()) {
			TransitionManager.beginDelayedTransition(binding.getRoot());
			binding.card.setVisibility(View.GONE);
		} else if(id == binding.choose.getId()) {
			try {
                binding.install.setEnabled(false);
                UnicornFilePicker.from(this)
                    .addConfigBuilder()
                    .addItemDivider(false)
                    .selectMultipleFiles(false)
                    .setRootDirectory(FileUtil.getExternalStorageDir())
                    .showHiddenFiles(true)
                    .showOnlyDirectory(true)
                    .theme(R.style.AppTheme_FilePicker)
                    .build()
                    .forResult(abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE);
            } catch (Throwable th){
                getApp().writeException(th);
            }
		} else if(id == binding.install.getId()) {
			if(choosenDir == null) {
				getApp().toast(R.string.msg_pick_files, Toaster.Type.ERROR);
				return;
			}
			
			if(!choosenDir.exists()) {
				getApp().toast(R.string.msg_file_doesnt_exist, Toaster.Type.ERROR);
				return;
			}
			
			installAll();
		}
	}
	
	private final String DONE = "'Installed successfully.'";
	private void installAll() {
		showProgress();
		getApp().getPrefManager().putBoolean(PreferenceManager.KEY_FRAMEWORK_DOWNLOADED, true);
		final ShellServer server = getApp().newShell(__ -> checkInstalled(__));
		final StringBuilder sb = new StringBuilder();
		sb.append("cd $HOME && ");
		sb.append("echo 'Installing...' && ");
		File[] files = choosenDir.listFiles(ARCHIVE_FILTER);
		if(files != null)
			for(File f : files) {
				if(f.getName().endsWith(".tar.xz")) {
					sb.append("$BUSYBOX tar xvJf '" + f.getAbsolutePath() + "' && ");
				} else if(f.getName().endsWith(".zip")) {
					sb.append("$BUSYBOX unzip '" + f.getAbsolutePath() + "' && ");
				}
			}
		sb.append("rm -rf '" + getApp().getToolsDownloadDirectory().getAbsolutePath() + "' && ");
        sb.append("echo 'Cleaning unsupported flags in binaries...' && $BUSYBOX find $JAVA_HOME/bin -type f -exec androidide-cleaner {} \\; && ");
        sb.append("echo " + DONE);
		server.bgAppend(sb.toString());
	}
    
	private void checkInstalled(CharSequence out) {
		if(out != null) {
			final String line = out.toString().trim();
			runOnUiThread(() -> {
				getProgressSheet().setSubMessage(line);
				if(line.contains(DONE)) {
					getApp().getPrefManager().putBoolean(PreferenceManager.KEY_FRAMEWORK_INSTALLED, true);
					showRestartNeeded();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE && resultCode == RESULT_OK) {
			ArrayList<String> files = data.getStringArrayListExtra("filePaths");
			if(files != null) {
				if(files.size() == 1) {
					File choosenDir = new File(files.get(0));
					if(choosenDir.exists() && choosenDir.isDirectory()) {
						this.choosenDir = choosenDir;
						setDone(binding.choose);
						binding.install.setEnabled(true);
						new TaskExecutor().executeAsync(new ListDirectoryCallable(choosenDir), __ -> {
							binding.filesText.setText(getString(R.string.msg_installable_files, __));
						});
					} else {
						getApp().toast(R.string.msg_picked_isnt_dir, Toaster.Type.ERROR);
						choosenDir = null;
					}
				} else {
					getApp().toast(R.string.msg_pick_single_file, Toaster.Type.ERROR);
				}
			}
		}
	}
	
	private void setDone(MaterialButton button) {
		final int dp8 = SizeUtils.dp2px(8);
		final int dp4 = SizeUtils.dp2px(4);
		final int dp24 = SizeUtils.dp2px(24);
		button.setText("");
		button.setIconResource(R.drawable.ic_ok);
		button.setIconTint(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.bg_green)));
		button.setBackgroundColor(android.graphics.Color.TRANSPARENT);
		button.setPaddingRelative(dp8, dp4, dp8, dp4);
		button.setStrokeWidth(0);
		button.setGravity(Gravity.CENTER);
		button.setIconPadding(0);
		button.setInsetTop(0);
		button.setInsetBottom(0);
		button.setPaddingRelative(dp24, dp4, dp24, dp4);
	}
	
	private void showProgress() {
		getProgressSheet().setCancelable(false);
		getProgressSheet().setShowShadow(false);
		getProgressSheet().setSubMessageEnabled(true)
			.setWelcomeTextEnabled(true)
			.show(getSupportFragmentManager(), "progress_sheet");
	}

	private void showRestartNeeded() {
		setDone(binding.install);
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.title_restart);
		builder.setMessage(R.string.msg_restart);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, (p1, p2) -> finishAffinity());
		builder.create().show();
	}
	
	private ProgressSheet getProgressSheet() {
		return progressSheet == null ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait)) : progressSheet;
	}
	
	private final FileFilter ARCHIVE_FILTER = new FileFilter(){

		@Override
		public boolean accept(File p1) {
			return p1.isFile() && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));
		}
	};
}
