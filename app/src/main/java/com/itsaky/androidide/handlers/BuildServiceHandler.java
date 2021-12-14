/************************************************************************************
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
**************************************************************************************/
package com.itsaky.androidide.handlers;

import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.project.IDEModule;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.services.builder.BuildListener;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.build.ApkGeneratingTask;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BuildServiceHandler extends IDEHandler implements BuildListener {
    
    private IDEService service;
    private boolean shouldInstallApk = false;

    public BuildServiceHandler(Provider provider) {
        super(provider);
    }
    
    public IDEService getService() {
        return service;
    }
    
    @Override
    public void start() {
        if(this.activity() == null) {
            throwNPE();
        }
            
        AndroidProject project = provider.provideAndroidProject();
        if(project == null) {
            throwNPE();
        }
            
        if(project.getProjectPath() == null) {
            throwNPE();
        }
        
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
        appendOutput(task, msg);
        if(task == null || activity() == null) return;
        if(task.canOutput()) {
            activity().setStatus(msg);
        }
            
        // If this task generates APK, ask user if he/she wants to install them
        if (shouldInstallApk && task instanceof ApkGeneratingTask) {
            final IDEProject project = activity().provideIDEProject();
            final Optional<IDEModule> app = project.getModuleByPath(":app"); // TODO Handle multiple application modules
            if(app.isPresent()) {
                final String path = app.get().projectDir;
                if(path != null) {
                    final File buildDir = new File (path, "build");
                    installApks (((ApkGeneratingTask) task).getApks(buildDir));
                }
            }
        }

        activity().getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().invalidateOptionsMenu();
        activity().getBinding().buildProgressIndicator.setVisibility(View.GONE);
    }
    
    @Override
    public void onBuildFailed(GradleTask task, String msg) {
        appendOutput(task, msg);
        if(task == null) return;
        if(task.canOutput()) {
            activity().setStatus(msg);
        }
         
        activity().getApp().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
        activity().invalidateOptionsMenu();
        activity().getBinding().buildProgressIndicator.setVisibility(View.GONE);
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
    public void prepareBuild() {
        boolean isFirstBuild = activity().getApp().getPrefManager().getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true);
        activity().setStatus(activity().getString(isFirstBuild ? R.string.preparing_first : R.string.preparing));
        if(isFirstBuild) {
            activity().showFirstBuildNotice();
        }
        activity().getBinding().buildProgressIndicator.setVisibility(View.VISIBLE);
    }

    public void setShouldInstallApk (boolean install) {
        this.shouldInstallApk = install;
    }

    public void assembleDebug (boolean shouldInstallApk) {
        setShouldInstallApk(shouldInstallApk);
        getService().assembleDebug(shouldInstallApk);
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
    
    private void installApks (final Set<File> apks) {
        if (activity () == null || apks == null || apks.isEmpty()) {
            return;
        }
        
        if(apks.size() == 1) {
            activity().install(apks.iterator().next());
        } else {
            final List<File> files = new ArrayList<File> (apks);
            final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity(), R.style.AppTheme_MaterialAlertDialog);
            builder.setTitle(activity().getString(R.string.title_install_apks));
            builder.setItems(getNames(files), (d, w) -> {
                d.dismiss();
                activity().install(files.get(w));
            });
            builder.show();
        }
    }
    
    private CharSequence[] getNames (final Collection<File> apks) {
        final String[] names = new String[apks.size()];
        
        int i = 0;
        for (File apk : apks) {
            names[i] = apk.getName();
            ++i;
        }
        
        return names;
    }
}
