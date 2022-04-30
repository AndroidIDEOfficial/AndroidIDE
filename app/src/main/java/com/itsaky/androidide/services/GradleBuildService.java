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

import static com.itsaky.androidide.utils.ILogger.newInstance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.builder.BuildService;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.shell.CommonProcessExecutor;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage;
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage;
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult;
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.InputStreamLineReader;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    private boolean isToolingServerStarted = false;
    private Thread toolingServerThread;
    private NotificationManager notificationManager;
    private IToolingApiServer server;

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        showNotification();
    }

    private void showNotification() {
        final var ticker = getString(R.string.title_gradle_service_notification_ticker);
        final var title = getString(R.string.title_gradle_service_notification);
        final var message = getString(R.string.msg_gradle_service_notification);
        final var intent =
                PendingIntent.getActivity(this, 0, new Intent(this, EditorActivity.class), 0);
        final var notification =
                new Notification.Builder(this, BaseApplication.NOTIFICATION_GRADLE_BUILD_SERVICE)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setTicker(ticker)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(intent)
                        .build();

        LOG.info("Showing notification to user...");
        startForeground(NOTIFICATION_ID, notification);
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
        SERVER_LOGGER.log(ILogger.priority(line.priorityChar), line.formattedTagAndMessage());
    }

    @NonNull
    @Override
    public CompletableFuture<InitializeResult> initializeProject(@NonNull String rootDir) {
        checkServerStarted();
        ensureTmpdir();
        return server.initialize(new InitializeProjectMessage(rootDir));
    }

    @NonNull
    @Override
    public CompletableFuture<TaskExecutionResult> executeTasks(@NonNull String... tasks) {
        checkServerStarted();
        ensureTmpdir();
        return server.executeTasks(new TaskExecutionMessage(":", Arrays.asList(tasks)));
    }

    @NonNull
    @Override
    public CompletableFuture<TaskExecutionResult> executeProjectTasks(
            @NonNull String projectPath, @NonNull String... tasks) {
        checkServerStarted();
        ensureTmpdir();
        return server.executeTasks(new TaskExecutionMessage(projectPath, Arrays.asList(tasks)));
    }

    private void checkServerStarted() {
        if (!isToolingServerStarted) {
            throw new IllegalStateException("Tooling API server has not been started");
        }
    }

    private void ensureTmpdir() {
        Environment.mkdirIfNotExits(Environment.TMP_DIR);
    }

    public void startToolingServer(@Nullable OnServerStartListener listener) {
        if (toolingServerThread != null && toolingServerThread.isAlive()) {
            throw new IllegalStateException("Tooling API server is already started!");
        }

        toolingServerThread = new Thread(new ToolingServerRunner(listener));
        toolingServerThread.start();
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
                        Environment.JAVA.getAbsolutePath(),
                        "-jar",
                        Environment.TOOLING_API_JAR.getAbsolutePath());

                final var launcher =
                        ToolingApiLauncher.createClientLauncher(
                                GradleBuildService.this, serverStreams.in, serverStreams.out);
                final var future = launcher.startListening();

                GradleBuildService.this.startServerOutputReader(serverStreams.err);
                GradleBuildService.this.server = launcher.getRemoteProxy();
                GradleBuildService.this.isToolingServerStarted = true;

                if (listener != null) {
                    listener.onServerStarted();
                }

                // Wait(block) until the process terminates
                try {
                    future.get();
                } catch (ExecutionException | InterruptedException e) {
                    LOG.error(
                            "An error occurred while waiting for tooling API server to terminate");
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
}
