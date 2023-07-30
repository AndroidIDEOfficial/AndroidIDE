package com.itsaky.androidide.logsender;

/**
 * The LogSender interface.
 */
interface ILogSender {

  /**
   * Called by the log receiver to check if the sender is alive.
   */
  void ping();

  /**
   * Asks the sender to start reading the logs.
   */
  void startReader(int port);

  /**
   * Get the process ID.
   */
  int getPid();

  /**
   * Get the package name of the sender.
   */
  String getPackageName();

  /**
   * Get the unique ID for this log sender.
   */
  String getId();

  /**
   * Called by the IDE to notify a client that the log receiver has been disconnected.
   */
  void onDisconnect();
}