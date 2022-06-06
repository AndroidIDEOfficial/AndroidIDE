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
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityMainBinding;
import com.itsaky.androidide.fragments.MainFragment;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.toaster.Toaster;

import java.io.File;

public class MainActivity extends StudioActivity {
  private ActivityMainBinding binding;

  public void openProject(@NonNull File root) {
    ProjectManager.INSTANCE.setProjectPath(root.getAbsolutePath());
    startActivity(new Intent(this, EditorActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.container, new MainFragment(), MainFragment.TAG)
        .commit();
  }

  @Override
  protected View bindLayout() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    return binding.getRoot();
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

  @Override
  protected void onStorageGranted() {}

  @Override
  protected void onStorageDenied() {
    getApp().toast(R.string.msg_storage_denied, Toaster.Type.ERROR);
    finishAffinity();
  }
}
