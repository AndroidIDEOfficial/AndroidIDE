/*
 * This file is part of AndroidIDE.
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

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.core.content.ContextCompat;
import androidx.transition.TransitionManager;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityDownloadBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.shell.IProcessExecutor;
import com.itsaky.androidide.shell.ProcessExecutorFactory;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ListDirectoryCallable;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.InputStreamLineReader;
import com.itsaky.toaster.Toaster;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;

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
    final OnClickListener clickListener = this::handleClick;
    binding.dismiss.setOnClickListener(clickListener);
    binding.choose.setOnClickListener(clickListener);
    binding.install.setOnClickListener(clickListener);

    binding.install.setEnabled(false);
  }

  private void handleClick(View v) {
    final int id = v.getId();
    if (id == binding.dismiss.getId()) {
      TransitionManager.beginDelayedTransition(binding.getRoot());
      binding.card.setVisibility(View.GONE);
    } else if (id == binding.choose.getId()) {
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
      } catch (Throwable th) {
        LOG.error(getString(R.string.err_cannot_start_file_picker), th);
      }
    } else if (id == binding.install.getId()) {
      if (choosenDir == null) {
        getApp().toast(R.string.msg_pick_files, Toaster.Type.ERROR);
        return;
      }

      if (!choosenDir.exists()) {
        getApp().toast(R.string.msg_file_doesnt_exist, Toaster.Type.ERROR);
        return;
      }

      installAll();
    }
  }

  private void installAll() {
    showProgress();
    getApp().getPrefManager().putBoolean(PreferenceManager.KEY_FRAMEWORK_DOWNLOADED, true);

    try {
      final File script = createExtractScript();
      final ProcessStreamsHolder holder = new ProcessStreamsHolder();
      final IProcessExecutor executor = ProcessExecutorFactory.commonExecutor();

      executor.execAsync(
          holder,
          this::onInstallProcessExit,
          true,
          Environment.BUSYBOX.getAbsolutePath(),
          "sh", // We use busybox's sh, because Environment.SHELL is not installed yet...
          script.getAbsolutePath());

      this.output = new StringBuilder();
      final InputStreamLineReader reader =
          new InputStreamLineReader(holder.in, this::onInstallationOutput);
      new Thread(reader).start();

    } catch (DownloadActivity.InstallationException e) {
      LOG.error(getString(R.string.err_installation), e);
      onInstallationFailed(e.exitCode);
    } catch (IOException e) {
      LOG.error(getString(R.string.err_installation), e);
      onInstallationFailed(5); // Exit code 5 : I/O Error
    }
  }

  private void onInstallationOutput(final String line) {
    ThreadUtils.runOnUiThread(() -> this.appendOut(line));
  }

  private void onInstallProcessExit(final int code) {
    ThreadUtils.runOnUiThread(
        () -> {
          if (code == 0) { // 0 = normal execution
            getApp().getPrefManager().putBoolean(PreferenceManager.KEY_FRAMEWORK_INSTALLED, true);
            showRestartNeeded();
            if (getProgressSheet().isShowing()) {
              getProgressSheet().dismiss();
            }
          } else {
            onInstallationFailed(code);
          }
        });
  }

  private void onInstallationFailed(int code) {
    // TODO Can this be improved by adding some animations?

    if (getProgressSheet().isShowing()) {
      getProgressSheet().dismiss();
    }

    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_installation_failed);
    builder.setMessage(getString(R.string.msg_installation_failed, code, this.output.toString()));
    builder.setPositiveButton(
        android.R.string.ok,
        (d, w) -> {
          d.dismiss();
          finishAffinity();
        });
    builder.setNegativeButton(android.R.string.cancel, null);
    builder.show();
  }

  private File createExtractScript() throws DownloadActivity.InstallationException {
    final StringBuilder sb = new StringBuilder();
    sb.append("cd");
    joiner(sb);
    sb.append("echo 'Installing...'");
    joiner(sb);

    File[] files = choosenDir.listFiles(ARCHIVE_FILTER);

    if (files == null || files.length <= 0) {
      throw new InstallationException(2);
    }

    for (File f : files) {
      if (f.getName().endsWith(".tar.xz")) {
        if (f.getName().startsWith("androidide-sysroot")) {
          sb.append("cd $SYSROOT/..");
          joiner(sb);
        }
        sb.append("$BUSYBOX tar xvJf '").append(f.getAbsolutePath()).append("'");
        joiner(sb);
        sb.append("cd $HOME");
        joiner(sb);
      } else if (f.getName().endsWith(".zip")) {
        sb.append("$BUSYBOX unzip '").append(f.getAbsolutePath()).append("'");
        joiner(sb);
      }
    }

    sb.append("echo 'Cleaning unsupported flags in binaries...'");
    joiner(sb);
    String DONE = "DONE";
    sb.append("echo ").append(DONE);

    final File script = new File(Environment.TMP_DIR, "extract_tools.sh");
    if (!FileIOUtils.writeFileFromString(script, sb.toString())) {
      throw new InstallationException(2);
    }

    return script;
  }

  private void joiner(StringBuilder sb) {
    sb.append(" && ");
  }

  private StringBuilder output = new StringBuilder();

  private void appendOut(String line) {
    LOG.debug("Installation:", line);
    output.append(line.trim());
    output.append("\n");

    getProgressSheet().setSubMessage(line);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE
        && resultCode == RESULT_OK) {
      ArrayList<String> files = data.getStringArrayListExtra("filePaths");
      if (files != null) {
        if (files.size() == 1) {
          File choosenDir = new File(files.get(0));
          if (choosenDir.exists() && choosenDir.isDirectory()) {
            this.choosenDir = choosenDir;
            setDone(binding.choose);
            binding.install.setEnabled(true);
            new TaskExecutor()
                .executeAsync(
                    new ListDirectoryCallable(choosenDir),
                    __ -> binding.filesText.setText(getString(R.string.msg_installable_files, __)));
          } else {
            getApp().toast(R.string.msg_picked_isnt_dir, Toaster.Type.ERROR);
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
    getProgressSheet()
        .setSubMessageEnabled(true)
        .setWelcomeTextEnabled(true)
        .show(getSupportFragmentManager(), "progress_sheet");
  }

  private void showRestartNeeded() {
    setDone(binding.install);
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_restart);
    builder.setMessage(R.string.msg_restart);
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.ok, (p1, p2) -> finishAffinity());
    builder.create().show();
  }

  private ProgressSheet getProgressSheet() {
    return progressSheet == null
        ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait))
        : progressSheet;
  }

  private final FileFilter ARCHIVE_FILTER =
      p1 -> p1.isFile() && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));

  private static class InstallationException extends Exception {
    private final int exitCode;

    public InstallationException(int exitCode) {
      this.exitCode = exitCode;
    }
  }
}
