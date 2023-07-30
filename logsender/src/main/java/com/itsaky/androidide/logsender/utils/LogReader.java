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

import com.itsaky.androidide.logsender.socket.ISocketCommand;
import com.itsaky.androidide.logsender.socket.SenderInfoCommand;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Reads application logs with `logcat`.
 *
 * @author Akash Yadav
 */
public class LogReader extends Thread {

  private final String senderId;
  private final String packageName;
  private final int port;
  private final ProcessBuilder processBuilder;
  private final AtomicBoolean isInterrupted = new AtomicBoolean(false);

  public LogReader(String senderId, String packageName, int port) {
    this(senderId, packageName, port, defaultCmd());
  }

  public LogReader(String senderId, String packageName, int port, String[] cmd) {
    super("AndroidIDE-LogReader");
    this.senderId = senderId;
    this.packageName = packageName;
    this.port = port;

    this.processBuilder = new ProcessBuilder(cmd);
    this.processBuilder.redirectErrorStream(true);
  }

  private static String[] defaultCmd() {
    return new String[]{"logcat", "-v", "threadtime"};
  }

  @Override
  public void run() {
    Logger.info("Starting to read logs...");
    try (final Socket socket = new Socket(InetAddress.getLocalHost(), port)) {
      final Process process = processBuilder.start();

      try (final BufferedReader reader = new BufferedReader(
          new InputStreamReader(process.getInputStream()))) {

        final OutputStream outputStream = socket.getOutputStream();

        // Send the sender info
        writeCommand(new SenderInfoCommand(this.senderId, this.packageName), outputStream);

        String line;
        while (!isInterrupted.get() && (line = reader.readLine()) != null) {
          line += "\n";
          outputStream.write(line.getBytes());
        }

      } catch (IOException ioError) {
        Logger.error("Error reading from the logcat process or writing to the socket", ioError);
      } finally {
        socket.close();
      }
    } catch (IOException ioError) {
      Logger.error("Error creating the socket or starting the process", ioError);
    }
  }

  private void writeCommand(ISocketCommand command, OutputStream outputStream) throws IOException {
    outputStream.write(command.toString().getBytes());
  }

  public void cancel() {
    this.isInterrupted.set(true);
    this.interrupt();
  }
}