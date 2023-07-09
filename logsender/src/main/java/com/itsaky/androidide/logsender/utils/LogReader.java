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

package com.itsaky.androidide.logsender.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Reads application logs with `logcat`.
 *
 * @author Akash Yadav
 */
public class LogReader extends Thread {

  private final int port;
  private final ProcessBuilder processBuilder;

  public LogReader(int port) {
    this(port, defaultCmd());
  }

  public LogReader(int port, String[] cmd) {
    super("AndroidIDE-LogReader");
    this.port = port;

    this.processBuilder = new ProcessBuilder(cmd);
    this.processBuilder.redirectErrorStream(true);
  }

  public static String[] defaultCmd() {
    return new String[]{"logcat", "-v", "threadtime"};
  }

  @Override
  public void run() {
    Logger.info("Starting to read logs...");
    try (final Socket socket = new Socket(InetAddress.getLocalHost(), port)) {
      final Process process = processBuilder.start();
      try (final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          line += "\n";
          socket.getOutputStream().write(line.getBytes());
        }
      }
    } catch (Throwable err) {
      Logger.error("An error occured while reading logs", err);
    }
  }
}