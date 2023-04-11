package com.itsaky.androidide.logsender;

import com.itsaky.androidide.logsender.ILogSender;

/**
 * The LogReceiver interface.
 */
oneway interface ILogReceiver {

  /**
   * Called by the client applications to connect to log receiver in AndroidIDE.
   */
  void connect(ILogSender sender);

  /**
   * Called when a new log line is available.
   */
  void onLog(String line);
}