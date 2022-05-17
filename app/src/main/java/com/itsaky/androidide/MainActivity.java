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
import android.os.Environment;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.transition.TransitionManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.MaterialContainerTransform;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityMainBinding;
import com.itsaky.androidide.databinding.LayoutCreateProjectBinding;
import com.itsaky.androidide.databinding.LayoutCreateProjectContentBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ProjectCreatorCallable;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.TransformUtils;
import com.itsaky.toaster.Toaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;

public class MainActivity extends StudioActivity
    implements View.OnClickListener, ProjectWriterCallback {

  private ActivityMainBinding binding;
  private LayoutCreateProjectBinding createBinding;
  private LayoutCreateProjectContentBinding createLayoutBinding;

  private ProgressSheet mProgressSheet;

  private ArrayList<ProjectTemplate> mTemplates = new ArrayList<>();

  private int currentTemplateIndex = 0;

  @Override
  public void onClick(View p1) {
    if (p1.getId() == binding.createProject.getId()) {
      showCreateProject();
    } else if (p1.getId() == createBinding.createprojectClose.getId()) {
      hideCreateProject();
    } else if (p1.getId() == createLayoutBinding.nextCard.getId()) {
      currentTemplateIndex++;
      if (currentTemplateIndex >= mTemplates.size()) {
        currentTemplateIndex = 0;
      }
      showTemplate(currentTemplateIndex);
    } else if (p1.getId() == createLayoutBinding.previousCard.getId()) {
      currentTemplateIndex--;
      if (currentTemplateIndex < 0) {
        currentTemplateIndex = mTemplates.size() - 1;
      }
      showTemplate(currentTemplateIndex);
    } else if (p1.getId() == createLayoutBinding.createprojectCreate.getId()) {
      createNewProject();
    } else if (p1.getId() == binding.gotoPreferences.getId()) {
      gotoSettings();
    } else if (p1.getId() == binding.openProject.getId()) {
      pickProject();
    } else if (p1.getId() == binding.openTerminal.getId()) {
      startActivity(new Intent(this, TerminalActivity.class));
    }
  }

  private void showCreateProject() {
    MaterialContainerTransform transform =
        TransformUtils.createContainerTransformFor(
            binding.createProject, binding.createNewCard, binding.realContainer);
    TransitionManager.beginDelayedTransition(binding.getRoot(), transform);
    binding.createNewCard.setVisibility(View.VISIBLE);
  }

  private void hideCreateProject() {
    MaterialContainerTransform transform =
        TransformUtils.createContainerTransformFor(
            binding.createNewCard, binding.createProject, binding.realContainer);
    TransitionManager.beginDelayedTransition(binding.getRoot(), transform);
    binding.createNewCard.setVisibility(View.GONE);
  }

  private void showTemplate(int index) {
    if (index < 0 || index >= mTemplates.size()) {
      return;
    }
    final ProjectTemplate template = mTemplates.get(index);
    createLayoutBinding.createprojectTemplateLabel.setText(template.getName());
    createLayoutBinding.createprojectTemplateDescription.setText(template.getDescription());
    createLayoutBinding.createprojectTemplateImage.setImageResource(template.getImageId());
  }

  private void createNewProject() {
    final String appName =
        Objects.requireNonNull(createLayoutBinding.createprojectTextAppName.getEditText())
            .getText()
            .toString()
            .trim();
    final String packageName =
        Objects.requireNonNull(createLayoutBinding.createprojectTextPackageName.getEditText())
            .getText()
            .toString()
            .trim();
    final int minSdk = getMinSdk();
    final int targetSdk = getTargetSdk();

    if (!isValid(appName, packageName, minSdk, targetSdk)) {
      getApp().toast(R.string.invalid_values, Toaster.Type.ERROR);
      return;
    }

    createProject(appName, packageName, minSdk, targetSdk);
  }

  private void gotoSettings() {
    startActivity(new Intent(this, PreferencesActivity.class));
  }

  private void pickProject() {
    UnicornFilePicker.from(this)
        .addConfigBuilder()
        .addItemDivider(false)
        .selectMultipleFiles(false)
        .setRootDirectory(Environment.getExternalStorageDirectory().getAbsolutePath())
        .showHiddenFiles(true)
        .showOnlyDirectory(true)
        .theme(R.style.AppTheme_FilePicker)
        .build()
        .forResult(abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE);
  }

  private int getMinSdk() {
    try {
      return Integer.parseInt(
          Objects.requireNonNull(createLayoutBinding.createprojectTextMinSdk.getEditText())
              .getText()
              .toString());
    } catch (Exception e) {
      getApp().toast(e.getMessage(), Toaster.Type.ERROR);
    }
    return -1;
  }

  private int getTargetSdk() {
    try {
      return Integer.parseInt(
          Objects.requireNonNull(createLayoutBinding.createprojectTextTargetSdk.getEditText())
              .getText()
              .toString());
    } catch (Exception e) {
      getApp().toast(e.getMessage(), Toaster.Type.ERROR);
    }
    return -1;
  }

  private boolean isValid(String appName, String packageName, int minSdk, int targetSdk) {
    return appName != null
        && appName.length() > 0
        && packageName != null
        && packageName.length() > 0
        && minSdk > 1
        && minSdk < 99
        && targetSdk > 1
        && targetSdk < 99;
  }

  private void createProject(String appName, String packageName, int minSdk, int targetSdk) {
    new TaskExecutor()
        .executeAsync(
            new ProjectCreatorCallable(
                mTemplates.get(currentTemplateIndex),
                new NewProjectDetails(appName, packageName, minSdk, targetSdk),
                this),
            r -> {});
  }

  @Override
  public void beforeBegin() {
    if (mProgressSheet == null) {
      createProgressSheet();
    }

    setMessage(R.string.msg_begin_project_write);
  }

  @Override
  public void onProcessTask(String taskName) {
    setMessage(taskName);
  }

  @Override
  public void onSuccess(File root) {
    if (mProgressSheet == null) {
      createProgressSheet();
    }

    if (mProgressSheet.isShowing()) {
      mProgressSheet.dismiss();
    }
    getApp().toast(R.string.project_created_successfully, Toaster.Type.SUCCESS);

    openProject(root);
  }

  @Override
  public void onFailed(String reason) {
    if (mProgressSheet == null) {
      createProgressSheet();
    }

    if (mProgressSheet.isShowing()) {
      mProgressSheet.dismiss();
    }
    getApp().toast(reason, Toaster.Type.ERROR);
  }

  private void openProject(@NonNull File root) {
    ProjectManager.INSTANCE.setProjectPath(root.getAbsolutePath());
    startActivity(new Intent(this, EditorActivity.class));
  }

  private void createProgressSheet() {
    mProgressSheet = new ProgressSheet();
    mProgressSheet.setShowShadow(false);
  }

  private void setMessage(int msg) {
    setMessage(getString(msg));
  }

  private void setMessage(String msg) {
    mProgressSheet.setMessage(msg);
  }

  @Override
  public void onBackPressed() {
    if (binding.createNewCard.getVisibility() == View.VISIBLE) {
      hideCreateProject();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding.createProject.setOnClickListener(this);
    binding.openProject.setOnClickListener(this);
    binding.openTerminal.setOnClickListener(this);
    createBinding.createprojectClose.setOnClickListener(this);
    createLayoutBinding.createprojectCreate.setOnClickListener(this);
    createLayoutBinding.previousCard.setOnClickListener(this);
    createLayoutBinding.nextCard.setOnClickListener(this);
    binding.createNewCard.setVisibility(View.GONE);

    currentTemplateIndex = 0;
    createTemplates();
    createProgressSheet();
    showTemplate(currentTemplateIndex);

    binding.gotoPreferences.setOnClickListener(this);

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
    createBinding = binding.createLayout;
    createLayoutBinding = createBinding.createProjectContent;
    return binding.getRoot();
  }

  /** Must be called in/after onViewCreated */
  private void createTemplates() {
    mTemplates = new ArrayList<>();

    ProjectTemplate
        empty =
            new ProjectTemplate()
                .setId(0)
                .setName(this, R.string.template_empty)
                .setDescription(this, R.string.template_description_empty)
                .setImageId(R.drawable.template_empty),
        basic =
            new ProjectTemplate()
                .setId(1)
                .setName(this, R.string.template_basic)
                .setDescription(this, R.string.template_description_basic)
                .setImageId(R.drawable.template_basic),
        drawer =
            new ProjectTemplate()
                .setId(2)
                .setName(this, R.string.template_navigation_drawer)
                .setDescription(this, R.string.template_description_navigation_drawer)
                .setImageId(R.drawable.template_navigation_drawer),
        kotlinBasic =
            new ProjectTemplate()
                .setId(3)
                .setName(this, R.string.template_kotlin_basic)
                .setDescription(this, R.string.template_description_kotlin_basic)
                .setImageId(R.drawable.template_kotlin),
        libgdx =
            new ProjectTemplate()
                .setId(4)
                .setName(this, R.string.template_libgdx)
                .setDescription(this, R.string.template_description_libgdx)
                .setImageId(R.drawable.template_libgdx);
    mTemplates.add(empty);
    mTemplates.add(basic);
    mTemplates.add(drawer);
    mTemplates.add(kotlinBasic);
    mTemplates.add(libgdx);
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
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == abhishekti7.unicorn.filepicker.utils.Constants.REQ_UNICORN_FILE
        && resultCode == RESULT_OK) {
      ArrayList<String> files = data.getStringArrayListExtra("filePaths");
      if (files != null) {
        if (files.size() == 1) {
          File choseDir = new File(files.get(0));
          if (choseDir.exists() && choseDir.isDirectory()) {
            openProject(choseDir);
          } else {
            getApp().toast(R.string.msg_picked_isnt_dir, Toaster.Type.ERROR);
          }
        } else {
          getApp().toast(R.string.msg_pick_single_file, Toaster.Type.ERROR);
        }
      }
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (mProgressSheet != null && mProgressSheet.isShowing()) {
      mProgressSheet.dismiss();
    }
  }
}
