/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.androidide.shell;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.ILogger;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ShellServer extends Thread {

  private static final ILogger LOG = ILogger.newInstance("ShellServer");
  private final Callback callback;
  private BufferedReader output;
  private Process process;

  public ShellServer(
      Callback callback,
      String command,
      String dirPath,
      Map<String, String> env,
      boolean redirectErrors) {
    this.callback = callback;

    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.directory(new File(dirPath));
    processBuilder.redirectErrorStream(redirectErrors);
    processBuilder.environment().putAll(env);

    try {
      this.process = processBuilder.start();
      this.output = new BufferedReader(new InputStreamReader(process.getInputStream()));
    } catch (Throwable th) {
      if (callback != null) {
        String out = getFullStackTrace(th).concat("\n");
        callback.output(out);
      }
    }
  }

  @NonNull
  private String getFullStackTrace(@NonNull Throwable th) {
    StringWriter sw = new StringWriter();
    th.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  public InputStream getProcessInputStream() {
    return process.getInputStream();
  }

  public OutputStream getProcessOutputStream() {
    return process.getOutputStream();
  }

  public void bgAppend(String str) {
    append(str, false);
  }

  public void append(String str, boolean z) {
    if (z) {
      try {
        if (str.endsWith("\n")) {
          output(str);
        } else {
          output(str.concat("\n"));
        }
      } catch (Throwable th) {
        output(th.toString().concat("\n"));
        return;
      }
    }
    try {
      this.process.getOutputStream().write(str.concat("\n").getBytes());
      this.process.getOutputStream().flush();
    } catch (Throwable e) {
      LOG.error("Unable to write to shell server", e);
    }
  }

  public void output(String str) {
    if (this.callback != null) {
      this.callback.output(str);
    }
  }

  public void append(@NonNull String... strArr) {
    for (String append : strArr) {
      append(append);
    }
  }

  public void append(String str) {
    append(str, true);
  }

  public void exit() {
    append("exit");
  }

  @Override
  public void run() {
    while (true) {
      try {
        String readLine = this.output.readLine();
        if (readLine == null) {
          break;
        }
        output(readLine.concat("\n"));
      } catch (Throwable th) {
        output(th.toString().concat("\n"));
      }
    }
    closeIOQuietly(
        this.output,
        this.process.getInputStream(),
        this.process.getErrorStream(),
        this.process.getOutputStream());
    this.process.destroy();
  }

  private void closeIOQuietly(@NonNull Closeable... toClose) {
    for (Closeable c : toClose) {
      try {
        c.close();
      } catch (IOException e) {
        // ignored
      }
    }
  }

  public interface Callback {
    void output(String charSequence);
  }
}
