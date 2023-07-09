package com.itsaky.androidide.logsender;

/**
 * The LogSender interface.
 */
interface ILogSender {

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
}