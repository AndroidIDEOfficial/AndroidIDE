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

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityMainBinding;
import com.itsaky.androidide.fragments.MainFragment;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.toaster.Toaster;

import java.io.File;

public class MainActivity extends StudioActivity {
  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!checkToolsIsInstalled()) {
      showDialogInstallJdkSdk();
    } else {
      openLastProject();
    }

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new MainFragment(), MainFragment.TAG)
        .commit();
  }

  private void openLastProject() {
    binding
        .getRoot()
        .post(
            () -> {
              PreferenceManager manager = getApp().getPrefManager();
              if (manager.autoOpenProject()
                  && manager.wasProjectOpened()
                  && ConstantsBridge.SPLASH_TO_MAIN) {
                String path = manager.getOpenedProject();
                if (path == null || path.trim().isEmpty()) {
                  getApp().toast(R.string.msg_opened_project_does_not_exist, Toaster.Type.INFO);
                } else {
                  File root = new File(manager.getOpenedProject());
                  if (!root.exists()) {
                    getApp().toast(R.string.msg_opened_project_does_not_exist, Toaster.Type.INFO);
                  } else {
                    if (manager.confirmProjectOpen()) {
                      askProjectOpenPermission(root);
                    } else {
                      openProject(root);
                    }
                  }
                }
              }

              ConstantsBridge.SPLASH_TO_MAIN = false;
            });
  }

  private void askProjectOpenPermission(File root) {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_confirm_open_project);
    builder.setMessage(getString(R.string.msg_confirm_open_project, root.getAbsolutePath()));
    builder.setCancelable(false);
    builder.setPositiveButton(R.string.yes, (d, w) -> openProject(root));
    builder.setNegativeButton(R.string.no, null);
    builder.show();
  }

  public void openProject(@NonNull File root) {
    ProjectManager.INSTANCE.setProjectPath(root.getAbsolutePath());
    startActivity(new Intent(this, EditorActivity.class));
  }

  @Override
  protected void onStorageGranted() {}

  @Override
  protected void onStorageDenied() {
    getApp().toast(R.string.msg_storage_denied, Toaster.Type.ERROR);
    finishAffinity();
  }

  @Override
  protected View bindLayout() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private void showDialogInstallJdkSdk() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
    builder.setTitle(R.string.title_warning);
    TextView view = new TextView(this);
    view.setPadding(10, 10, 10, 10);
    view.setText(
        HtmlCompat.fromHtml(
            getString(R.string.msg_require_install_jdk_and_android_sdk),
            HtmlCompat.FROM_HTML_MODE_COMPACT));
    view.setMovementMethod(LinkMovementMethod.getInstance());
    builder.setView(view);
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.ok, (d, w) -> openTerminal());
    builder.setNegativeButton(android.R.string.cancel, (d, w) -> finishAffinity());
    builder.show();
  }

  private boolean checkToolsIsInstalled() {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists();
  }

  private void openTerminal() {
    startActivity(new Intent(this, TerminalActivity.class));
  }
}
