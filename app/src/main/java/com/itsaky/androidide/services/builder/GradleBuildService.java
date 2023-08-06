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

package com.itsaky.androidide.services.builder;

import static com.itsaky.androidide.managers.ToolsManager.getCommonAsset;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.getGradleInstallationDir;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isBuildCacheEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isDebugEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isInfoEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isOfflineEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isScanEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isStacktraceEnabled;
import static com.itsaky.androidide.preferences.internal.BuildPreferencesKt.isWarningModeAllEnabled;
import static com.itsaky.androidide.utils.ILogger.newInstance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.BuildConfig;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.lookup.Lookup;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.builder.BuildService;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.services.ToolingServerNotStartedException;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.tooling.api.ForwardingToolingApiClient;
import com.itsaky.androidide.tooling.api.IProject;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage;
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage;
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult;
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo;
import com.itsaky.androidide.tooling.api.messages.result.BuildResult;
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult;
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult;
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult;
import com.itsaky.androidide.tooling.events.ProgressEvent;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * A foreground service that handles interaction with the Gradle Tooling API.
 *
 * @author Akash Yadav
 */
public class GradleBuildService extends Service implements BuildService, IToolingApiClient, ToolingServerRunner.Observer {

  private static final ILogger LOG = newInstance("GradleBuildService");
  private static final int NOTIFICATION_ID = R.string.app_name;
  private static final ILogger SERVER_System_err = newInstance("ToolingApiErrorStream");
  private final ILogger SERVER_LOGGER = newInstance("ToolingApiServer");
  private GradleServiceBinder mBinder = null;
  private boolean isToolingServerStarted = false;
  private boolean isBuildInProgress = false;
  
  /**
   * We do not provide direct access to GradleBuildService instance to the Tooling API launcher as
   * it may cause memory leaks. Instead, we create another client object which forwards all calls to us.
   * So, when the service is destroyed, we release the reference to the service from this client.
   */
  private ForwardingToolingApiClient _toolingApiClient;
  private ToolingServerRunner toolingServerThread;
  private OutputReaderThread outputReader;
  private NotificationManager notificationManager;
  private IToolingApiServer server;
  @Nullable
  private EventListener eventListener;

  @Override
  public void onCreate() {
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    showNotification(getString(R.string.build_status_idle), false);

    Lookup.getDefault().update(BuildService.KEY_BUILD_SERVICE, this);
  }

  @Override
  public boolean isToolingServerStarted() {
    return isToolingServerStarted && server != null;
  }

  @SuppressWarnings("SameParameterValue")
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
            .setSmallIcon(R.drawable.ic_launcher_notification)
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
    mBinder.release();
    mBinder = null;

    LOG.info("Service is being destroyed.", "Dismissing the shown notification...");
    notificationManager.cancel(NOTIFICATION_ID);

    final var lookup = Lookup.getDefault();
    lookup.unregister(GradleBuildService.KEY_BUILD_SERVICE);
    lookup.unregister(GradleBuildService.KEY_PROJECT_PROXY);

    if (server != null) {
      server.cancelCurrentBuild();
      final var shutdown = server.shutdown();
  
      //noinspection ConstantConditions
      if (shutdown != null) {
        try {
          LOG.info("Shutting down Tooling API server...");
          shutdown.get();
        } catch (Throwable e) {
          LOG.error("Failed to shutdown Tooling API server", e);
        }
      }
    }

    if (toolingServerThread != null) {
      toolingServerThread.release();
      toolingServerThread.interrupt();
      toolingServerThread = null;
    }
    
    if (_toolingApiClient != null) {
      _toolingApiClient.setClient(null);
      _toolingApiClient = null;
    }
    
    if (outputReader != null) {
      outputReader.interrupt();
      outputReader = null;
    }

    isToolingServerStarted = false;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    if (mBinder == null) {
      mBinder = new GradleServiceBinder(this);
    }
    return mBinder;
  }
  
  @Override
  public void onListenerStarted(@NotNull final IToolingApiServer server, @NotNull IProject projectProxy, @NotNull final ProcessStreamsHolder streams) {
    startServerOutputReader(streams.err);
    this.server = server;
    Lookup.getDefault().update(BuildService.KEY_PROJECT_PROXY, projectProxy);
    this.isToolingServerStarted = true;
    ProjectManager.INSTANCE.setupProject(projectProxy, false);
  }
  
  @Override
  public void onServerExited(int exitCode) {
    LOG.warn("Tooling API process terminated with exit code:", exitCode);
    stopForeground(true);
  }
  
  @NonNull
  @Override
  public IToolingApiClient getClient() {
    if (_toolingApiClient == null) {
      _toolingApiClient = new ForwardingToolingApiClient(this);
    }
    return _toolingApiClient;
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
  public void prepareBuild(@NotNull BuildInfo buildInfo) {
    updateNotification(getString(R.string.build_status_in_progress), true);
    if (eventListener != null) {
      eventListener.prepareBuild(buildInfo);
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

  public void setServerListener (@Nullable ToolingServerRunner.OnServerStartListener listener) {
    if (this.toolingServerThread != null) {
      this.toolingServerThread.setListener(listener);
    }
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
    checkServerStarted();
    final var message = new InitializeProjectMessage(rootDir, "", getGradleInstallationDir());
    return performBuildTasks(server.initialize(message));
  }

  @NonNull
  @Override
  public CompletableFuture<TaskExecutionResult> executeTasks(@NonNull String... tasks) {
    checkServerStarted();
    final var message =
        new TaskExecutionMessage(IProject.ROOT_PROJECT_PATH, Arrays.asList(tasks), getGradleInstallationDir());
    return performBuildTasks(server.executeTasks(message));
  }

  @NonNull
  @Override
  public CompletableFuture<TaskExecutionResult> executeProjectTasks(
      @NonNull String projectPath, @NonNull String... tasks) {
    checkServerStarted();
    final var message =
        new TaskExecutionMessage(projectPath, Arrays.asList(tasks), getGradleInstallationDir());
    return performBuildTasks(server.executeTasks(message));
  }

  @NonNull
  @Override
  public CompletableFuture<BuildCancellationRequestResult> cancelCurrentBuild() {
    checkServerStarted();
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

  private void checkServerStarted() throws ToolingServerNotStartedException {
    if (!isToolingServerStarted()) {
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

  public void startToolingServer(@Nullable ToolingServerRunner.OnServerStartListener listener) {
    if (toolingServerThread == null || !toolingServerThread.isAlive()) {
      toolingServerThread = new ToolingServerRunner(listener, this);
      toolingServerThread.start();
      return;
    }

    if (toolingServerThread.isStarted() && listener != null) {
      listener.onServerStarted();
    } else {
      setServerListener(listener);
    }
  }

  public GradleBuildService setEventListener(EventListener eventListener) {
    if (eventListener == null) {
      this.eventListener = null;
      return this;
    }
    
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
      public void prepareBuild(@NonNull BuildInfo buildInfo) {
        ThreadUtils.runOnUiThread(() -> listener.prepareBuild(buildInfo));
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

  private void startServerOutputReader(InputStream err) {
    if (outputReader == null) {
      outputReader = new OutputReaderThread(err, SERVER_System_err::error);
    }
    
    if (!outputReader.isAlive()) {
      outputReader.start();
    }
  }
  
  /** Handles events received from a Gradle build. */
  public interface EventListener {

    /**
     * Called just before a build is started.
     *
     * @param buildInfo The information about the build to be executed.
     * @see IToolingApiClient#prepareBuild(BuildInfo)
     */
    void prepareBuild(@NonNull BuildInfo buildInfo);

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
