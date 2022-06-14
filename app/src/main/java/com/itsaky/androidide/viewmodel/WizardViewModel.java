package com.itsaky.androidide.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.CollectionUtils;

import com.itsaky.androidide.R;
import com.itsaky.androidide.models.ProjectTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class WizardViewModel extends AndroidViewModel {

  private MutableLiveData<List<ProjectTemplate>> mProjectTemplatesList =
      new MutableLiveData<>(CollectionUtils.newArrayList());
  private MutableLiveData<Boolean> mLoadingState = new MutableLiveData<>(true);

  public WizardViewModel(@NonNull Application application) {
    super(application);
  }

  public void createTemplatesList() {
    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              List<ProjectTemplate> mTemplates = new ArrayList<>();

              ProjectTemplate
                  empty =
                      new ProjectTemplate()
                          .setId(0)
                          .setSupportJava(true)
                          .setSupportKotlin(true)
                          .setName(
                              getApplication().getApplicationContext(), R.string.template_empty)
                          .setDescription(
                              getApplication().getApplicationContext(),
                              R.string.template_description_empty)
                          .setImageId(R.drawable.template_empty),
                  basic =
                      new ProjectTemplate()
                          .setId(1)
                          .setSupportJava(true)
                          .setSupportKotlin(true)
                          .setName(
                              getApplication().getApplicationContext(), R.string.template_basic)
                          .setDescription(
                              getApplication().getApplicationContext(),
                              R.string.template_description_basic)
                          .setImageId(R.drawable.template_basic),
                  drawer =
                      new ProjectTemplate()
                          .setId(2)
                          .setSupportJava(true)
                          .setSupportKotlin(true)
                          .setName(
                              getApplication().getApplicationContext(),
                              R.string.template_navigation_drawer)
                          .setDescription(
                              getApplication().getApplicationContext(),
                              R.string.template_description_navigation_drawer)
                          .setImageId(R.drawable.template_navigation_drawer),
                  libgdx =
                      new ProjectTemplate()
                          .setId(3)
                          .setSupportJava(true)
                          .setName(
                              getApplication().getApplicationContext(), R.string.template_libgdx)
                          .setDescription(
                              getApplication().getApplicationContext(),
                              R.string.template_description_libgdx)
                          .setImageId(R.drawable.template_libgdx),
                  compose =
                      new ProjectTemplate()
                          .setId(4)
                          .setSupportKotlin(true)
                          .setName(
                              getApplication().getApplicationContext(), R.string.template_compose)
                          .setDescription(
                              getApplication().getApplicationContext(),
                              R.string.template_description_compose)
                          .setImageId(R.drawable.template_kotlin);
                          
              mTemplates.add(empty);
              mTemplates.add(basic);
              mTemplates.add(drawer);
              mTemplates.add(libgdx);
              mTemplates.add(compose);

              mProjectTemplatesList.postValue(mTemplates);
              mLoadingState.postValue(false);
            });
  }

  public LiveData<List<ProjectTemplate>> getProjects() {
    return mProjectTemplatesList;
  }

  public LiveData<Boolean> getLoadingState() {
    return mLoadingState;
  }
}
