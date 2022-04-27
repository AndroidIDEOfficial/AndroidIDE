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

package com.itsaky.androidide.tooling.api.util;

import com.google.gson.GsonBuilder;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
import com.itsaky.androidide.tooling.api.model.IdeLaunchable;

import org.eclipse.lsp4j.jsonrpc.Launcher;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for launching {@link IToolingApiClient} and {@link IToolingApiServer}.
 *
 * @author Akash Yadav
 */
public class ToolingApiLauncher {
    public static Launcher<IToolingApiServer> createClientLauncher(
            IToolingApiClient client, InputStream in, OutputStream out) {
        return createIOLauncher(client, IToolingApiServer.class, in, out);
    }

    public static <T> Launcher<T> createIOLauncher(
            Object local, Class<T> remote, InputStream in, OutputStream out) {
        return new Launcher.Builder<T>()
                .setInput(in)
                .setOutput(out)
                .setLocalService(local)
                .setRemoteInterface(remote)
                .configureGson(ToolingApiLauncher::configureGson)
                .create();
    }

    public static void configureGson(GsonBuilder builder) {
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(IdeGradleProject.class, "gsonType", true)
                        .registerSubtype(IdeAndroidModule.class, IdeAndroidModule.class.getName())
                        .registerSubtype(IdeGradleProject.class, IdeGradleProject.class.getName()));
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(IdeLaunchable.class, "gsonType", true)
                        .registerSubtype(IdeGradleTask.class, IdeGradleTask.class.getName())
                        .registerSubtype(IdeLaunchable.class, IdeLaunchable.class.getName()));
    }

    public static Launcher<IToolingApiClient> createServerLauncher(
            IToolingApiServer server, InputStream in, OutputStream out) {
        return createIOLauncher(server, IToolingApiClient.class, in, out);
    }
}
