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

package com.itsaky.androidide.tooling.impl;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.tooling.api.IProject;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;
import com.itsaky.androidide.tooling.impl.model.InternalForwardingProject;
import com.itsaky.androidide.tooling.impl.progress.ForwardingProgressListener;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.JvmLogger;

import org.gradle.tooling.ConfigurableLauncher;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
  public static final String SUPPORTED_AGP_VERSION = "@AGP_VERSION@";
  private static final ILogger LOG = newInstance("ToolingApiMain");
  public static IToolingApiClient client;
  public static Future<Void> future;

  static {
    JvmLogger.interceptor = Main::onLog;
  }

  public static void main(String[] args) {
    LOG.debug("Starting Tooling API server...");
    final var project = new InternalForwardingProject(null, IProject.FILE_PATH_NOT_AVAILABLE);
    final var server = new ToolingApiServerImpl(project);
    final var launcher =
        ToolingApiLauncher.newServerLauncher(server, project, System.in, System.out);
    Main.future = launcher.startListening();
    Main.client = (IToolingApiClient) launcher.getRemoteProxy();
    server.connect(client);

    LOG.debug("Server started. Will run until shutdown message is received...");
    try {
      Main.future.get();
    } catch (InterruptedException | ExecutionException e) {
      LOG.error("An error occurred while waiting for shutdown message", e);
    }
  }

  public static void checkGradleWrapper() {
    if (client != null) {
      LOG.info("Checking gradle wrapper availability...");
      try {
        if (!client.checkGradleWrapperAvailability().get().isAvailable()) {
          LOG.warn(
              "Gradle wrapper is not available."
                  + " Client might have failed to ensure availability."
                  + " Build might fail.");
        } else {
          LOG.info("Gradle wrapper is available");
        }
      } catch (Throwable e) {
        LOG.warn("Unable to get Gradle wrapper availability from client", e);
      }
    }
  }

  public static void finalizeLauncher(ConfigurableLauncher<?> launcher) {
    final var out = new LoggingOutputStream();
    launcher.setStandardError(out);
    launcher.setStandardOutput(out);
    launcher.setStandardInput(new ByteArrayInputStream("NoOp".getBytes(StandardCharsets.UTF_8)));

    launcher.addProgressListener(new ForwardingProgressListener());

    if (client != null) {
      try {
        final var args = client.getBuildArguments().get();
        args.removeIf(Objects::isNull);
        args.removeIf(String::isBlank);

        LOG.debug("Arguments from tooling client:", args);
        launcher.addArguments(args);
      } catch (Throwable e) {
        LOG.error("Unable to get build arguments from tooling client", e);
      }
    }
  }

  private static void onLog(LogLine line) {
    if (client != null) {
      client.logMessage(line);
    }
  }
}
