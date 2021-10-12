package com.itsaky.androidide.handlers;

import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.project.IDEModule;
import com.itsaky.androidide.models.project.IDEProject;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuildServiceHandler extends IDEHandler implements IDEService.BuildListener {
    
    private IDEService service;
    
    public BuildServiceHandler(Provider provider) {
        super(provider);
    }
    
    public IDEService getService() {
        return service;
    }
    
    @Override
    public void start() {
        if(this.activity() == null)
            throwNPE();
            
        AndroidProject project = provider.provideAndroidProject();
        if(project == null)
            throwNPE();
            
        if(project.getProjectPath() == null)
            throwNPE();
        
        this.service = new IDEService(new File(project.getProjectPath()));
        this.service.setListener(this);
    }

    @Override
    public void stop() {
        this.service.stopAllDaemons();
        this.service.exit();
    }
    
    @Override
    public void onBuildModified() {
        // Does nothing currently
    }
    
    @Override
    public void onProjectLoaded(IDEProject project, Optional<IDEModule> appModule) {
        project.iconPath = provider.provideAndroidProject().getIconPath();
        activity().setIDEProject(project);
        activity().createProjectInfoSheet(); // Recreate sheet, even if already created. Just to update its contents
        if(appModule.isPresent()) {
            IDEModule app = appModule.get();
            activity().setStatus(activity().getString(R.string.msg_starting_completion));

            final List<String> paths = new ArrayList<String>(app.dependencies);
            paths.stream().filter(path -> isClasspathValid(path)).collect(Collectors.toList());

            provider.provideAndroidProject().setClassPaths(paths);

            File androidJar = new File(Environment.ANDROID_HOME, String.format("platforms/%s/android.jar", app.compileSdkVersion));
            if(androidJar.exists()) {
                Environment.setBootClasspath(androidJar);
                provider.provideAndroidProject().addClasspath(androidJar.getAbsolutePath());
                activity().createServices();
            } else activity().setStatus("android.jar not found!");
        } else activity().setStatus("Cannot get :app module...");
    }
    
    @Override
    public void onStartingGradleDaemon(GradleTask task) {
        activity().setStatus(activity().getString(R.string.msg_starting_daemon));
        activity().getApp().setStopGradleDaemon(false);
    }
    
    @Override
    public void onRunTask(GradleTask task, String name) {
        if(task != null && task.canOutput()) {
            activity().setStatus(service.typeString(task.getTaskID()) + " " + name);
        }
    }
    
    @Override
    public void onBuildSuccessful(GradleTask task, String msg) {
        if(task == null) return;
        if(task.canOutput())
            activity().setStatus(msg);

        if(task.getType() == GradleTask.Type.BUILD) {
            if(task.getTaskID() == IDEService.TASK_ASSEMBLE_DEBUG) {
                if(task.buildsApk()) {
                    activity().install(task.getApk(new File(provider.provideAndroidProject().getMainModulePath(), "build").getAbsolutePath(), provider.provideAndroidProject().getMainModule()));
                }
            }
        }

        activity().getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().invalidateOptionsMenu();
    }
    
    @Override
    public void onBuildFailed(GradleTask task, String msg) {
        if(task == null) return;
        if(task.canOutput()) {
            activity().setStatus(msg);
        }
        
        activity().getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().invalidateOptionsMenu();
    }
    
    @Override
    public void saveFiles() {
        activity().saveAll(false, false);
    }
    
    @Override
    public void appendOutput(GradleTask task, CharSequence text) {
        if(text == null)
            return;
        if(task != null && !task.canOutput()) {
            return;
        }
        
        activity().appendBuildOut(text.toString());
    }
    
    @Override
    public void prepare() {
        boolean isFirstBuild = activity().getApp().getPrefManager().getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true);
        activity().setStatus(activity().getString(isFirstBuild ? R.string.preparing_first : R.string.preparing));
        if(isFirstBuild) {
            activity().showFirstBuildNotice();
        }
    }
    
    private boolean isClasspathValid(String path) {
        if(path == null || path.trim().length() <= 0 || path.trim().equals("/")) return false;
        File file = new File(path);
        return file.isFile()
            && file.getName().endsWith(".jar")
            /**
             * Release R.jar shouldn't be included in classpath
             */
            && !file.getAbsolutePath().endsWith("/release/R.jar");
    }
}
