/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.services;

import static com.itsaky.androidide.managers.ToolsManager.getCommonAsset;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.getGradleInstallationDir;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isBuildCacheEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isDebugEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isInfoEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isOfflineEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isScanEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isStacktraceEnabled;
import static com.itsaky.androidide.models.prefs.BuildPreferencesKt.isWarningModeAllEnabled;
import static com.itsaky.androidide.utils.ILogger.newInstance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.BuildConfig;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.builder.BuildService;
import com.itsaky.androidide.shell.CommonProcessExecutor;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.tooling.api.IProject;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage;
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage;
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult;
import com.itsaky.androidide.tooling.api.messages.result.BuildResult;
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult;
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult;
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;
import com.itsaky.androidide.tooling.events.ProgressEvent;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.InputStreamLineReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A foreground service that handles interaction with the Gradle Tooling API.
 *
 * @author Akash Yadav
 */
public class GradleBuildService extends Service implements BuildService, IToolingApiClient {

  private static final ILogger LOG = newInstance("GradleBuildService");
  private static final int NOTIFICATION_ID = R.string.app_name;
  private static final ILogger SERVER_System_err = newInstance("ToolingApiErrorStream");
  private final ILogger SERVER_LOGGER = newInstance("ToolingApiServer");
  private final IBinder mBinder = new GradleServiceBinder();
  public IProject projectProxy;
  private boolean isToolingServerStarted = false;
  private boolean isBuildInProgress = false;
  private Thread toolingServerThread;
  private NotificationManager notificationManager;
  private IToolingApiServer server;
  private EventListener eventListener;

  @Override
  public void onCreate() {
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    showNotification(getString(R.string.build_status_idle), false);
  }

  private void showNotification(final String message, final boolean isProgress) {
    LOG.info("Showing notification to user...");
    startForeground(NOTIFICATION_ID, buildNotification(message, isProgress));
  }

  private Notification buildNotification(final String message, final boolean isProgress) {
    final var ticker = getString(R.string.title_gradle_service_notification_ticker);
    final var title = getString(R.string.title_gradle_service_notification);

    final var launch = getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);

    final var intent =
        PendingIntent.getActivity(this, 0, launch, PendingIntent.FLAG_UPDATE_CURRENT);
    final Notification.Builder builder =
        new Notification.Builder(this, BaseApplication.NOTIFICATION_GRADLE_BUILD_SERVICE)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setTicker(ticker)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(intent);
    
    // Checking whether to add a ProgressBar to the notification
    if (isProgress) {
      // Add ProgressBar to Notification
      builder.setProgress(100, 0, true);
    }

    return builder.build();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    // No point in restarting the service if it really gets killed.
    return START_NOT_STICKY;
  }

  @Override
  public void onDestroy() {
    LOG.info("Service is being destroyed.", "Dismissing the shown notification...");
    notificationManager.cancel(NOTIFICATION_ID);

    if (server != null) {
      server.cancelCurrentBuild();
      server.shutdown();
    }

    if (toolingServerThread != null) {
      toolingServerThread.interrupt();
    }

    isToolingServerStarted = false;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override
  public void logMessage(@NonNull LogLine line) {
    SERVER_LOGGER.log(line.priority, line.formattedTagAndMessage());
  }

  @Override
  public void logOutput(@NonNull String line) {
    if (eventListener != null) {
      eventListener.onOutput(line);
    }
  }

  @Override
  public void prepareBuild() {
    updateNotification(getString(R.string.build_status_in_progress), true);
    if (eventListener != null) {
      eventListener.prepareBuild();
    }
  }

  @Override
  public void onBuildSuccessful(@NonNull BuildResult result) {
    updateNotification(getString(R.string.build_status_sucess), false);
    if (eventListener != null) {
      eventListener.onBuildSuccessful(result.getTasks());
    }
  }

  @Override
  public void onBuildFailed(@NonNull BuildResult result) {
    updateNotification(getString(R.string.build_status_failed), false);
    if (eventListener != null) {
      eventListener.onBuildFailed(result.getTasks());
    }
  }

  @Override
  public void onProgressEvent(@NonNull ProgressEvent event) {
    if (eventListener != null) {
      eventListener.onProgressEvent(event);
    }
  }

  @NonNull
  @Override
  public CompletableFuture<List<String>> getBuildArguments() {
    final var extraArgs = new ArrayList<String>();

    extraArgs.add("--init-script");
    extraArgs.add(Environment.INIT_SCRIPT.getAbsolutePath());

    // Override AAPT2 binary
    // The one downloaded from Maven is not built for Android
    extraArgs.add("-Pandroid.aapt2FromMavenOverride=" + Environment.AAPT2.getAbsolutePath());

    if (isStacktraceEnabled()) {
      extraArgs.add("--stacktrace");
    }
    if (isInfoEnabled()) {
      extraArgs.add("--info");
    }
    if (isDebugEnabled()) {
      extraArgs.add("--debug");
    }
    if (isScanEnabled()) {
      extraArgs.add("--scan");
    }
    if (isWarningModeAllEnabled()) {
      extraArgs.add("--warning-mode");
      extraArgs.add("all");
    }
    if (isBuildCacheEnabled()) {
      extraArgs.add("--build-cache");
    }
    if (isOfflineEnabled()) {
      extraArgs.add("--offline");
    }

    return CompletableFuture.completedFuture(extraArgs);
  }

  @NonNull
  @Override
  public CompletableFuture<GradleWrapperCheckResult> checkGradleWrapperAvailability() {
    return isGradleWrapperAvailable()
        ? CompletableFuture.completedFuture(new GradleWrapperCheckResult(true))
        : installWrapper();
  }

  public boolean isGradleWrapperAvailable() {
    final var projectDir = ProjectManager.INSTANCE.getProjectDirPath();
    if (TextUtils.isEmpty(projectDir)) {
      return false;
    }

    final var projectRoot = Objects.requireNonNull(ProjectManager.INSTANCE.getProjectDir());
    if (!projectRoot.exists()) {
      return false;
    }

    final File gradlew = new File(projectRoot, "gradlew");
    final File gradleWrapperJar = new File(projectRoot, "gradle/wrapper/gradle-wrapper.jar");
    final File gradleWrapperProps =
        new File(projectRoot, "gradle/wrapper/gradle-wrapper.properties");

    return gradlew.exists() && gradleWrapperJar.exists() && gradleWrapperProps.exists();
  }

  private CompletableFuture<GradleWrapperCheckResult> installWrapper() {
    if (eventListener != null) {
      eventListener.onOutput("-------------------- NOTE --------------------");
      eventListener.onOutput(getString(R.string.msg_installing_gradlew));
      eventListener.onOutput("----------------------------------------------");
    }

    return CompletableFuture.supplyAsync(this::doInstallWrapper);
  }

  @NonNull
  private GradleWrapperCheckResult doInstallWrapper() {
    final var extracted = new File(Environment.TMP_DIR, "gradle-wrapper.zip");
    if (!ResourceUtils.copyFileFromAssets(
        getCommonAsset("gradle-wrapper.zip"), extracted.getAbsolutePath())) {
      LOG.error("Unable to extract gradle-plugin.zip from IDE resources.");
      return new GradleWrapperCheckResult(false);
    }

    try {
      final var projectDir = ProjectManager.INSTANCE.getProjectDir();
      final var files = ZipUtils.unzipFile(extracted, projectDir);
      if (files != null && !files.isEmpty()) {
        return new GradleWrapperCheckResult(true);
      }
    } catch (IOException e) {
      LOG.error("An error occurred while extracting Gradle wrapper", e);
    }

    return new GradleWrapperCheckResult(false);
  }

  @SuppressWarnings("ConstantConditions")
  protected void updateNotification(final String message, final boolean isProgress) {
    ThreadUtils.runOnUiThread(() -> doUpdateNotification(message, isProgress));
  }

  protected void doUpdateNotification(final String message, final boolean isProgress) {
    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
        .notify(NOTIFICATION_ID, buildNotification(message, isProgress));
  }

  @Override
  public boolean isBuildInProgress() {
    return isBuildInProgress;
  }

  @NonNull
  @Override
  public CompletableFuture<InitializeResult> initializeProject(@NonNull String rootDir) {
    final var message = new InitializeProjectMessage(rootDir, getGradleInstallationDir());
    return performBuildTasks(server.initialize(message));
  }

  @NonNull
  @Override
  public CompletableFuture<TaskExecutionResult> executeTasks(@NonNull String... tasks) {
    final var message =
        new TaskExecutionMessage(":", Arrays.asList(tasks), getGradleInstallationDir());
    return performBuildTasks(server.executeTasks(message));
  }

  @NonNull
  @Override
  public CompletableFuture<TaskExecutionResult> executeProjectTasks(
      @NonNull String projectPath, @NonNull String... tasks) {
    final var message =
        new TaskExecutionMessage(projectPath, Arrays.asList(tasks), getGradleInstallationDir());
    return performBuildTasks(server.executeTasks(message));
  }

  @NonNull
  @Override
  public CompletableFuture<BuildCancellationRequestResult> cancelCurrentBuild() {
    return server.cancelCurrentBuild();
  }

  protected <T> CompletableFuture<T> performBuildTasks(CompletableFuture<T> future) {
    return CompletableFuture.runAsync(this::onPrepareBuildRequest)
        .handle(
            (__, err) -> {
              try {
                return future.get();
              } catch (Throwable e) {
                throw new CompletionException(e);
              }
            })
        .handle(this::markBuildAsFinished);
  }

  protected void onPrepareBuildRequest() {
    checkServerStarted();
    ensureTmpdir();

    if (isBuildInProgress) {
      logBuildInProgress();
      throw new BuildInProgressException();
    }

    isBuildInProgress = true;
  }

  private void checkServerStarted() {
    if (!isToolingServerStarted) {
      throw new ToolingServerNotStartedException();
    }
  }

  private void ensureTmpdir() {
    Environment.mkdirIfNotExits(Environment.TMP_DIR);
  }

  private void logBuildInProgress() {
    LOG.warn("A build is already in progress!");
  }

  protected <T> T markBuildAsFinished(final T result, final Throwable throwable) {
    isBuildInProgress = false;
    return result;
  }

  public void startToolingServer(@Nullable OnServerStartListener listener) {
    if (toolingServerThread == null || !toolingServerThread.isAlive()) {
      toolingServerThread = new Thread(new ToolingServerRunner(listener));
      toolingServerThread.start();
    }
  }

  public GradleBuildService setEventListener(EventListener eventListener) {
    this.eventListener = wrap(eventListener);
    return this;
  }

  private EventListener wrap(EventListener listener) {
    if (listener == null) {
      return null;
    }

    return new EventListener() {

      @SuppressWarnings("ConstantConditions")
      @Override
      public void prepareBuild() {
        ThreadUtils.runOnUiThread(listener::prepareBuild);
      }

      @SuppressWarnings("ConstantConditions")
      @Override
      public void onBuildSuccessful(@NonNull List<String> tasks) {
        ThreadUtils.runOnUiThread(() -> listener.onBuildSuccessful(tasks));
      }

      @SuppressWarnings("ConstantConditions")
      @Override
      public void onProgressEvent(@NonNull ProgressEvent event) {
        ThreadUtils.runOnUiThread(() -> listener.onProgressEvent(event));
      }

      @SuppressWarnings("ConstantConditions")
      @Override
      public void onBuildFailed(@NonNull List<String> tasks) {
        ThreadUtils.runOnUiThread(() -> listener.onBuildFailed(tasks));
      }

      @SuppressWarnings("ConstantConditions")
      @Override
      public void onOutput(String line) {
        ThreadUtils.runOnUiThread(() -> listener.onOutput(line));
      }
    };
  }

  protected void onServerExited(int exitCode) {
    LOG.warn("Tooling API process terminated with exit code:", exitCode);
    stopForeground(true);
  }

  private void startServerOutputReader(InputStream err) {
    new Thread(new InputStreamLineReader(err, this::onServerOutput)).start();
  }

  protected void onServerOutput(String line) {
    SERVER_System_err.error(line);
  }

  public class GradleServiceBinder extends Binder {
    public GradleBuildService getService() {
      return GradleBuildService.this;
    }
  }

  private class ToolingServerRunner implements Runnable {

    @Nullable private final OnServerStartListener listener;

    public ToolingServerRunner(@Nullable OnServerStartListener listener) {
      this.listener = listener;
    }

    @Override
    public void run() {
      try {
        LOG.info("Starting tooling API server...");
        final var serverStreams = new ProcessStreamsHolder();
        final var executor = new CommonProcessExecutor();
        executor.execAsync(
            serverStreams,
            GradleBuildService.this::onServerExited,
            false,

            // The 'java' binary executable
            Environment.JAVA.getAbsolutePath(),

            // Allow reflective access to private members of classes in the following
            // packages:
            // - java.lang
            // - java.io
            // - java.util
            //
            // If any of the model classes in 'tooling-api-model' module send/receive
            // objects from the JDK, their package name must be declared here with
            // '--add-opens' to prevent InaccessibleObjectException.
            // For example, some of the model classes has members of type java.io.File.
            // When sending/receiving these type of objects using LSP4J, members of
            // these objects are reflectively accessed by Gson. If we do no specify
            // '--add-opens' for 'java.io' (for java.io.File) package, JVM will throw an
            // InaccessibleObjectException.
            "--add-opens",
            "java.base/java.lang=ALL-UNNAMED",
            "--add-opens",
            "java.base/java.util=ALL-UNNAMED",
            "--add-opens",
            "java.base/java.io=ALL-UNNAMED",

            // The JAR file to run
            "-jar",
            Environment.TOOLING_API_JAR.getAbsolutePath());

        final var launcher =
            ToolingApiLauncher.newClientLauncher(
                GradleBuildService.this, serverStreams.in, serverStreams.out);
        final var future = launcher.startListening();

        GradleBuildService.this.startServerOutputReader(serverStreams.err);
        GradleBuildService.this.server = (IToolingApiServer) launcher.getRemoteProxy();
        GradleBuildService.this.projectProxy = (IProject) launcher.getRemoteProxy();
        GradleBuildService.this.isToolingServerStarted = true;

        ProjectManager.INSTANCE.setupProject(GradleBuildService.this.projectProxy);

        if (listener != null) {
          listener.onServerStarted();
        }

        // Wait(block) until the process terminates
        try {
          future.get();
        } catch (Throwable err) {
          if (!(err instanceof CancellationException) && !(err instanceof InterruptedException)) {
            LOG.error("An error occurred while waiting for tooling API server to terminate", err);
          }
        }

      } catch (Throwable e) {
        LOG.error("Unable to start tooling API server", e);
      }
    }
  }

  /** Callback to listen for Tooling API server start event. */
  public interface OnServerStartListener {

    /** Called when the tooling API server has been successfully started. */
    void onServerStarted();
  }

  /** Handles events received from a Gradle build. */
  public interface EventListener {

    /**
     * Called just before a build is started.
     *
     * @see IToolingApiClient#prepareBuild()
     */
    void prepareBuild();

    /**
     * Called when a build is successful.
     *
     * @param tasks The tasks that were run.
     * @see IToolingApiClient#onBuildSuccessful(BuildResult)
     */
    void onBuildSuccessful(@NonNull List<String> tasks);

    /**
     * Called when a progress event is received from the Tooling API server.
     *
     * @param event The event model describing the event.
     */
    void onProgressEvent(@NonNull ProgressEvent event);

    /**
     * Called when a build fails.
     *
     * @param tasks The tasks that were run.
     * @see IToolingApiClient#onBuildFailed(BuildResult)
     */
    void onBuildFailed(@NonNull List<String> tasks);

    /**
     * Called when the output line is received.
     *
     * @param line The line of the build output.
     */
    void onOutput(String line);
  }
}
