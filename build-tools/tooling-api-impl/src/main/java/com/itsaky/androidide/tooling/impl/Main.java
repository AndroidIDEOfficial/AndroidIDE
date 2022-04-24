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

import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.utils.ILogger;

import org.eclipse.lsp4j.jsonrpc.Launcher;

import java.util.concurrent.ExecutionException;

public class Main {
    private static final ILogger LOG = newInstance("ToolingApiMain");
    public final Object lock = new Object();

    public static void main(String[] args) {
        LOG.debug("Starting Tooling API server...");
        final var server = new ToolingApiServerImpl();
        final var launcher =
                Launcher.createLauncher(server, IToolingApiClient.class, System.in, System.out);
        final var future = launcher.startListening();
        server.connect(launcher.getRemoteProxy());

        LOG.debug("Server started. Will run until shutdown message is received...");
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("An error occurred while waiting for shutdown message", e);
        }
    }
}
