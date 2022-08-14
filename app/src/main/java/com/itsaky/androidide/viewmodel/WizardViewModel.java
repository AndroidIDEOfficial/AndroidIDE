package com.itsaky.androidide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.CollectionUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ProjectCreatorCallable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class WizardViewModel extends AndroidViewModel {

  private final MutableLiveData<List<ProjectTemplate>> mProjectTemplatesList =
      new MutableLiveData<>(CollectionUtils.newArrayList());
  private final MutableLiveData<Boolean> mLoadingState = new MutableLiveData<>(true);
  private final MutableLiveData<String> mErrorMessageState = new MutableLiveData<>();
  private final MutableLiveData<String> mMessageState = new MutableLiveData<>();
  private final MutableLiveData<File> mCreatedProject = new MutableLiveData<>(null);

  public WizardViewModel(@NonNull Application application) {
    super(application);
  }

  public void createTemplatesList() {
    TaskExecutor.executeAsync(this::createTemplates);
  }

  private Void createTemplates() {
    List<ProjectTemplate> mTemplates = new ArrayList<>();

    ProjectTemplate
        empty =
            new ProjectTemplate()
                .setId(0)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(getApplication().getApplicationContext(), R.string.template_empty)
                .setDescription(
                    getApplication().getApplicationContext(), R.string.template_description_empty)
                .setImageId(R.drawable.template_empty_activity),
        basic =
            new ProjectTemplate()
                .setId(1)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(getApplication().getApplicationContext(), R.string.template_basic)
                .setDescription(
                    getApplication().getApplicationContext(), R.string.template_description_basic)
                .setImageId(R.drawable.template_basic_activity),
        drawer =
            new ProjectTemplate()
                .setId(2)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(
                    getApplication().getApplicationContext(), R.string.template_navigation_drawer)
                .setDescription(
                    getApplication().getApplicationContext(),
                    R.string.template_description_navigation_drawer)
                .setImageId(R.drawable.template_blank_activity_drawer),
        bottomTabs =
            new ProjectTemplate()
                .setId(3)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(
                    getApplication().getApplicationContext(), R.string.template_navigation_tabs)
                .setImageId(R.drawable.template_bottom_navigation_activity),
        tabs =
            new ProjectTemplate()
                .setId(4)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(getApplication().getApplicationContext(), R.string.template_tabs)
                .setImageId(R.drawable.template_blank_activity_tabs),
        fragmentViewModel =
            new ProjectTemplate()
                .setId(5)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setName(
                    getApplication().getApplicationContext(),
                    R.string.template_fragment_and_viewmodel)
                .setImageId(R.drawable.template_empty_activity),
        cppWizard =
            new ProjectTemplate()
                .setId(6)
                .setSupportJava(true)
                .setSupportKotlin(true)
                .setIsCpp(true)
                .setName(getApplication().getApplicationContext(), R.string.template_cpp)
                .setImageId(R.drawable.template_cpp_configure),
        compose =
            new ProjectTemplate()
                .setId(7)
                .setSupportKotlin(true)
                .setName(getApplication().getApplicationContext(), R.string.template_compose)
                .setDescription(
                    getApplication().getApplicationContext(), R.string.template_description_compose)
                .setImageId(R.drawable.template_compose_empty_activity),
        libgdx =
            new ProjectTemplate()
                .setId(8)
                .setSupportJava(true)
                .setName(getApplication().getApplicationContext(), R.string.template_libgdx)
                .setDescription(
                    getApplication().getApplicationContext(), R.string.template_description_libgdx)
                .setImageId(R.drawable.template_game_activity);

    mTemplates.add(empty);
    mTemplates.add(basic);
    mTemplates.add(drawer);
    mTemplates.add(bottomTabs);
    mTemplates.add(tabs);
    mTemplates.add(fragmentViewModel);
    mTemplates.add(cppWizard);
    mTemplates.add(compose);
    mTemplates.add(libgdx);

    mProjectTemplatesList.postValue(mTemplates);
    mLoadingState.postValue(false);

    return null;
  }

  public LiveData<List<ProjectTemplate>> getProjects() {
    return mProjectTemplatesList;
  }

  public LiveData<Boolean> getLoadingState() {
    return mLoadingState;
  }

  public LiveData<String> getErrorState() {
    return mErrorMessageState;
  }

  public LiveData<File> getFileCreatedState() {
    return mCreatedProject;
  }

  public LiveData<String> getStatusMessage() {
    return mMessageState;
  }

  public void createProject(
      ProjectTemplate currentTemplate,
      String appName,
      String packageName,
      int minSdk,
      int targetSdk,
      String language,
      String cppFlags,
      String savePath) {
    final var details =
        new NewProjectDetails(
            appName, packageName, minSdk, targetSdk, language, cppFlags, savePath);
    final var callable = new ProjectCreatorCallable(currentTemplate, details, createCallback());
    TaskExecutor.executeAsync(callable);
  }

  private ProjectWriterCallback createCallback() {
    return new ProjectWriterCallback() {

      @Override
      public void beforeBegin() {
        mMessageState.setValue(getApplication().getString(R.string.msg_begin_project_write));
      }

      @Override
      public void onProcessTask(String taskName) {
        mMessageState.setValue(taskName);
      }

      @Override
      public void onSuccess(File rootDir) {
        mCreatedProject.setValue(rootDir);
      }

      @Override
      public void onFailed(String reason) {
        mErrorMessageState.setValue(reason);
      }
    };
  }
}
