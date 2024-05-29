package com.itsaky.androidide.logsender;

import com.itsaky.androidide.logsender.ILogSender;

/**
 * The LogReceiver interface.
 */
oneway interface ILogReceiver {

  /**
   * Called by the log sender to check if the receiver is alive.
   */
  void ping();

  /**
   * Called by the client applications to connect to log receiver in AndroidIDE.
   */
  void connect(ILogSender sender);

  /**
   * Called by the client applications to disconnect from the log receiver in AndroidIDE.
   */
  void disconnect(String packageName, String senderId);
}