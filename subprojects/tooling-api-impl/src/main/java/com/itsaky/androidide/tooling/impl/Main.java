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

import com.itsaky.androidide.logging.JvmStdErrAppender;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;
import com.itsaky.androidide.tooling.impl.internal.ProjectImpl;
import com.itsaky.androidide.tooling.impl.progress.ForwardingProgressListener;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.gradle.tooling.ConfigurableLauncher;
import org.gradle.tooling.events.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);
  public static IToolingApiClient client;
  public static Future<Void> future;

  public static void main(String[] args) {

    // disable the JVM std.err appender
    System.setProperty(JvmStdErrAppender.PROP_JVM_STDERR_APPENDER_ENABLED, "false");

    LOG.debug("Starting Tooling API server...");
    final var project = new ProjectImpl();
    final var server = new ToolingApiServerImpl(project);
    final var launcher =
        ToolingApiLauncher.newServerLauncher(server, project, System.in, System.out);
    Main.future = launcher.startListening();
    Main.client = (IToolingApiClient) launcher.getRemoteProxy();
    server.connect(client);

    LOG.debug("Server started. Will run until shutdown message is received...");
    LOG.debug("Running on Java version: {}", System.getProperty("java.version", "<unknown>"));

    try {
      Main.future.get();
    } catch (CancellationException cancellationException) {
      // ignored
    } catch (InterruptedException | ExecutionException e) {
      LOG.error("An error occurred while waiting for shutdown message", e);
      if (e instanceof InterruptedException) {
        // set the interrupt flag
        Thread.currentThread().interrupt();
      }

    } finally {

      // Cleanup should be performed in ToolingApiServerImpl.shutdown()
      // this is to make sure that the daemons are stopped in case the client doesn't call shutdown()
      try {
        if (server.isInitialized() || server.isConnected()) {
          LOG.warn("Connection to tooling server closed without shutting it down!");
          server.shutdown().get();
        }
      } catch (InterruptedException | ExecutionException e) {
        LOG.error("An error occurred while shutting down tooling API server", e);
      } finally {
        Main.future = null;
        Main.client = null;

        LOG.info("Tooling API server shutdown complete");
      }
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

  @SuppressWarnings("NewApi")
  public static void finalizeLauncher(ConfigurableLauncher<?> launcher) {
    final var out = new LoggingOutputStream();
    launcher.setStandardError(out);
    launcher.setStandardOutput(out);
    launcher.setStandardInput(new ByteArrayInputStream("NoOp".getBytes(StandardCharsets.UTF_8)));
    launcher.addProgressListener(new ForwardingProgressListener(), progressUpdateTypes());

    if (client != null) {
      try {
        final var args = client.getBuildArguments().get();
        args.removeIf(Objects::isNull);
        args.removeIf(String::isBlank);

        LOG.debug("Arguments from tooling client: {}", args);
        launcher.addArguments(args);
      } catch (Throwable e) {
        LOG.error("Unable to get build arguments from tooling client", e);
      }
    }
  }

  public static Set<OperationType> progressUpdateTypes() {
    final Set<OperationType> types = new HashSet<>();

    // AndroidIDE currently does not handle any other type of events
    types.add(OperationType.TASK);
    types.add(OperationType.PROJECT_CONFIGURATION);

    return types;
  }
}
