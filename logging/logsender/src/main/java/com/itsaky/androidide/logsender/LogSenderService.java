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

package com.itsaky.androidide.logsender;

import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.widget.Toast;
import com.itsaky.androidide.logsender.utils.Logger;

/**
 * A {@link Service} which runs in the background and sends logs to AndroidIDE.
 *
 * @author Akash Yadav
 */
public class LogSenderService extends Service {

  private final LogSender logSender = new LogSender();
  private static final int NOTIFICATION_ID = 644;
  private static final String NOTIFICATION_CHANNEL_NAME = "LogSender Service";
  private static final String NOTIFICATION_TITLE = "LogSender Service";
  private static final String NOTIFICATION_TEXT = "Connected to AndroidIDE";
  private static final String NOTIFICATION_CHANNEL_ID = "ide.logsender.service";
  public static final String ACTION_START_SERVICE = "ide.logsender.service.start";
  public static final String ACTION_STOP_SERVICE = "ide.logsender.service.stop";

  @Override
  public void onCreate() {
    Logger.debug("[LogSenderService] onCreate()");
    super.onCreate();
    setupNotificationChannel();
    startForeground(NOTIFICATION_ID, buildNotification());
  }

  @Override
  public IBinder onBind(Intent intent) {
    Logger.debug("Unexpected request to bind.", intent);
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Logger.debug("onStartCommand", intent, flags, startId);

    switch (intent.getAction()) {
      case ACTION_START_SERVICE:
        actionStartService();
        break;
      case ACTION_STOP_SERVICE:
        actionStopService();
        break;
      default:
        Logger.error("Unknown service action:", intent.getAction());
        break;
    }

    return START_NOT_STICKY;
  }

  private void actionStartService() {
    Logger.info("Starting log sender service...");

    boolean result = false;
    try {
      result = logSender.bind(getApplicationContext());
      Logger.debug("Bind to AndroidIDE:", result);
    } catch (Exception err) {
      Logger.error(getString(R.string.msg_bind_service_failed), err);
    }

    if (!result) {
      Toast.makeText(this, getString(R.string.msg_bind_service_failed), Toast.LENGTH_SHORT).show();
      actionStopService();
    }
  }

  private void actionStopService() {
    Logger.info("Stopping log sender service...");
    stopSelf();
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    Logger.debug("[LogSenderService] [onTaskRemoved]", rootIntent);

    if (!logSender.isConnected() && !logSender.isBinding()) {
      Logger.debug("Not bound to AndroidIDE. Ignored.");
      return;
    }

    Logger.warn("Task removed. Destroying log sender...");
    logSender.destroy(getApplicationContext());
    stopSelf();
  }

  @Override
  public void onDestroy() {
    Logger.debug("[LogSenderService] [onDestroy]");
    if (!logSender.isConnected() && !logSender.isBinding()) {
      Logger.debug("Not bound to AndroidIDE. Ignored.");
      return;
    }

    Logger.warn("Service is being destroyed. Destroying log sender...");
    logSender.destroy(getApplicationContext());
    super.onDestroy();
  }

  private void setupNotificationChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return;
    }

    NotificationChannel channel = new NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_LOW
    );

    NotificationManager notificationManager = getSystemService(NotificationManager.class);
    if (notificationManager != null) {
      notificationManager.createNotificationChannel(channel);
    }
  }

  private Notification buildNotification() {
    Resources res = getResources();

    // Set notification priority
    // If holding a wake or wifi lock consider the notification of high priority since it's using power,
    // otherwise use a low priority
    int priority = Notification.PRIORITY_LOW;

    // Build the notification
    final Builder builder = new Builder(this);
    builder.setContentTitle(NOTIFICATION_TITLE);
    builder.setContentText(NOTIFICATION_TEXT);
    builder.setStyle(new BigTextStyle().bigText(NOTIFICATION_TEXT));
    builder.setPriority(priority);

    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      builder.setChannelId(NOTIFICATION_CHANNEL_ID);
    }

    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      builder.setShowWhen(false);
    }

    builder.setSmallIcon(R.drawable.ic_androidide_log);

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      builder.setColor(0xFF607D8B);
    }

    builder.setOngoing(true);

    // Set Exit button action
    Intent exitIntent = new Intent(this, LogSenderService.class).setAction(ACTION_STOP_SERVICE);
    builder.addAction(android.R.drawable.ic_delete,
        res.getString(R.string.notification_action_exit),
        PendingIntent.getService(this, 0, exitIntent, PendingIntent.FLAG_IMMUTABLE));

    return builder.build();
  }
}
