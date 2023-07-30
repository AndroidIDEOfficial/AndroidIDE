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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.itsaky.androidide.logsender.utils.Logger;

/**
 * A {@link Service} which runs in the background and sends logs to AndroidIDE.
 *
 * @author Akash Yadav
 */
public class LogSenderService extends Service {

  private final LogSender logSender = new LogSender();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    boolean result = false;
    try {
      result = logSender.bind(getApplicationContext());
    } catch (Exception err) {
      Logger.error(getString(R.string.msg_bind_service_failed), err);
    }

    if (!result) {
      Toast.makeText(this, getString(R.string.msg_bind_service_failed), Toast.LENGTH_SHORT).show();
      stopSelf();
    }

    return START_NOT_STICKY;
  }

  @Override
  public void onTaskRemoved(Intent rootIntent) {
    if (!logSender.isConnected() && !logSender.isBinding()) {
      return;
    }

    Logger.warn("Task removed. Destroying log sender...");
    logSender.destroy(getApplicationContext());
    stopSelf();
  }

  @Override
  public void onDestroy() {
    if (!logSender.isConnected() && !logSender.isBinding()) {
      return;
    }

    Logger.warn("Service is being destroyed. Destroying log sender...");
    logSender.destroy(getApplicationContext());
    super.onDestroy();
  }
}
