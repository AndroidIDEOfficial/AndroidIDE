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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.itsaky.androidide.logsender.utils.LogReader;
import com.itsaky.androidide.logsender.utils.Logger;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry point for LogSender.
 *
 * @author Akash Yadav
 */
public final class LogSender extends ILogSender.Stub implements ServiceConnection {

  private final AtomicBoolean isBinding = new AtomicBoolean(false);
  private final AtomicBoolean isConnected = new AtomicBoolean(false);

  private final String senderId;

  private Context context;

  private LogReader reader;

  private ILogReceiver receiver;

  private String packageName;

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
  public static final String PACKAGE_ANDROIDIDE = "com.itsaky.androidide";

  LogSender() {
    this.senderId = UUID.randomUUID().toString();
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
      this.isConnected.set(true);
    } catch (RemoteException e) {
      Logger.error("Failed to connect to log receiver", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    tryDisconnect(true);
    if (this.context != null) {
      tryUnbind(this.context);
      this.context = null;
    }
  }

  @Override
  public void ping() {
    Log.d("LogSender", "ping: Received a ping request");
  }

  @Override
  public void startReader(int port) {
    if (reader != null && reader.isAlive()) {
      Logger.warn("LogReader has already been started");
      return;
    }

    reader = new LogReader(getId(), getPackageName(), port);
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

  @Override
  public String getId() {
    return this.senderId;
  }

  @Override
  public void onDisconnect() {
    tryDisconnect(false);
    tryUnbind(this.context);
    try {
      this.context.stopService(new Intent(this.context, LogSenderService.class));
    } catch (Exception err) {
      Logger.error("Failed to stop LogSenderService", err);
    }
    this.context = null;
  }

  /**
   * @return Whether the log sender is bound to the log receiver service in AndroidIDE.
   */
  boolean isBinding() {
    return isBinding.get();
  }

  /**
   * @return Whether the log sender is connected to the service.
   */
  boolean isConnected() {
    return isConnected.get();
  }

  /**
   * Binds to the log receiver service in AndroidIDE.
   *
   * @param context The context used to bind to the service.
   * @return Whether the binding was successful or not. This returns <code>true</code> if
   * {@link Context#bindService(Intent, ServiceConnection, int)} returns true or if the log sender
   * is already bound to the service. Otherwise <code>false</code> is returned.
   */
  boolean bind(Context context) {
    if (isConnected()) {
      Logger.warn("LogSender is already installed");
      return true;
    }

    if (isBinding()) {
      Logger.warn("LogSender is already being installed");
      return true;
    }

    this.packageName = context.getPackageName();
    if (PACKAGE_ANDROIDIDE.equals(packageName)) {
      return false;
    }

    this.context = context;

    final Intent intent = new Intent(SERVICE_ACTION);
    intent.setPackage(PACKAGE_ANDROIDIDE);
    isBinding.set(context.bindService(intent, this, Context.BIND_IMPORTANT | Context.BIND_AUTO_CREATE));

    if (isBinding()) {
      Logger.info("Binding to log receiver");
    } else {
      Logger.error("Failed to bind to log receiver");
    }

    return isBinding();
  }

  /**
   * Disconnects from the log receiver and unbinds from the log receiver service.
   *
   * @param context The context used to unbind from the service.
   */
  void destroy(Context context) {
    tryDisconnect(true);
    tryUnbind(context);
    this.context = null;
  }

  private void tryDisconnect(boolean notifyRecevier) {
    Logger.info("Disconnecting from log receiver...");
    if (this.reader != null) {
      this.reader.cancel();
    }

    if (notifyRecevier && isReceiverAlive(receiver)) {
      try {
        receiver.disconnect(getPackageName(), getId());
      } catch (Exception err) {
        Logger.error("Failed to disconnect from log receiver service", err);
      }
    }

    this.receiver = null;
    this.reader = null;
    this.isBinding.set(false);
    this.isConnected.set(false);
  }

  private void tryUnbind(Context context) {
    try {
      context.unbindService(this);
    } catch (Exception err) {
      Logger.error("Failed to unbind from the the log receiver service", err);
    }
  }

  private boolean isReceiverAlive(ILogReceiver receiver) {
    if (receiver == null) {
      return false;
    }

    try {
      receiver.ping();
      return true;
    } catch (RemoteException err) {
      return false;
    }
  }
}