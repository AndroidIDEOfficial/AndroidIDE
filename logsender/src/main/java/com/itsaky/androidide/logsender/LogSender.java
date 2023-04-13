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

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import com.itsaky.androidide.logsender.utils.LogReader;
import com.itsaky.androidide.logsender.utils.Logger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry point for LogSender.
 *
 * @author Akash Yadav
 */
public class LogSender extends ILogSender.Stub implements ServiceConnection {

  private final AtomicBoolean installed = new AtomicBoolean(false);

  private LogReader reader;

  private ILogReceiver receiver;

  private String packageName;

  public static final LogSender INSTANCE = new LogSender();

  /**
   * Action for the LogSender service.
   */
  public static final String SERVICE_ACTION = "com.itsaky.androidide.LOG_SERVICE_ACTION";

  /**
   * Constant used to indicate that the package name of the application cannot be determined.
   */
  public static final String PACKAGE_UNKNOWN = "<unknown-package-name>";

  /**
   * AndroidIDE's package name.
   */
  private static final String PACKAGE_ANDROIDIDE = "com.itsaky.androidide";

  private LogSender() {
  }

  public static void install(Application app) {
    INSTANCE.doInstall(app);
  }

  private void doInstall(Application app) {
    if (installed.get()) {
      Logger.warn("LogSender is already installed");
      return;
    }

    this.packageName = app.getPackageName();
    if (PACKAGE_ANDROIDIDE.equals(packageName)) {
      return;
    }

    final Intent intent = new Intent(SERVICE_ACTION);
    intent.setPackage(PACKAGE_ANDROIDIDE);
    if (app.bindService(intent, this, 0)) {
      Logger.info("Binding to log receiver");
      installed.set(true);
    } else {
      Logger.error("Failed to bind to log receiver");
    }
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    this.receiver = ILogReceiver.Stub.asInterface(service);
    if (this.receiver == null) {
      throw new IllegalStateException("Failed to get log receiver instance");
    }

    try {
      this.receiver.connect(this);
      Logger.info("Connecting to log receiver");
    } catch (RemoteException e) {
      Logger.error("Failed to connect to log receiver", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    Logger.info("Disconnected from log receiver");
    if (this.reader != null) {
      this.reader.interrupt();
    }
    this.receiver = null;
    this.reader = null;
    this.installed.set(false);
  }

  @Override
  public void startReader() {
    if (reader != null && reader.isAlive()) {
      Logger.warn("LogReader has already been started");
      return;
    }

    reader = new LogReader(this::doLog);
    reader.start();
  }

  @Override
  public int getPid() {
    return Process.myPid();
  }

  @Override
  public String getPackageName() {
    if (this.packageName == null) {
      return PACKAGE_UNKNOWN;
    }

    return this.packageName;
  }

  private void doLog(String line) {
    try {
      if (line != null && receiver != null) {
        receiver.onLog(line);
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}
