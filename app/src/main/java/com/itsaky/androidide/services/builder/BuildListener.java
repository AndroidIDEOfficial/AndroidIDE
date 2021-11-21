package com.itsaky.androidide.services.builder;

import com.itsaky.androidide.project.IDEModule;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.tasks.GradleTask;
import java.util.Optional;

/**
 * A client should implement this interface to get callbacks about a build
 */
public interface BuildListener {
    /**
     * Called to notify the client about the modification of build/generated files
     */
    void onBuildModified();

    /**
     * Called when the project has been loaded and its data has been read
     *
     * @param project The loaded project
     * @param appModule The main application module
     */
    void onProjectLoaded(IDEProject project, Optional<IDEModule> appModule);

    /**
     * Called when a Gradle Daemon is being started
     *
     * @param task Task that is currently running
     */
    void onStartingGradleDaemon(GradleTask task);

    /**
     * Called when a task is started
     *
     * @param task Task that is currently running
     * @param name Name of this task that should be shown to user
     */
    void onRunTask(GradleTask task, String name);

    /**
     * Notifys about a successful build
     *
     * @param task Task that succeeded
     * @param msg Successful message
     */
    void onBuildSuccessful(GradleTask task, String msg);

    /**
     * Notifys about a failed build
     *
     * @param task Task that failed
     * @param msg Failure message
     */
    void onBuildFailed(GradleTask task, String msg);

    /**
     * Called to notify client to save any opened files
     */
    void saveFiles();

    /**
     * Notifys the client to append the build output that should be shown to user
     *
     * @param task Task that is currently running
     * @param text Output to append
     */
    void appendOutput(GradleTask task, CharSequence text);

    /**
     * Called to notify the client to prepare for a build
     */
    void prepareBuild();
}
