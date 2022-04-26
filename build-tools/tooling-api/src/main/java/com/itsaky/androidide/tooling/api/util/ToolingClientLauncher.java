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
import com.itsaky.androidide.tooling.api.model.IAndroidModule;
import com.itsaky.androidide.tooling.api.model.IGradleProject;
import com.itsaky.androidide.tooling.api.model.ILaunchable;
import com.itsaky.androidide.tooling.api.model.ITask;
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidModule;
import com.itsaky.androidide.tooling.api.model.internal.DefaultGradleProject;
import com.itsaky.androidide.tooling.api.model.internal.DefaultGradleTask;
import com.itsaky.androidide.tooling.api.model.internal.DefaultLaunchable;
import com.itsaky.androidide.tooling.api.model.internal.DefaultTask;
import com.itsaky.androidide.tooling.api.model.internal.util.ProjectBuilder;

import org.eclipse.lsp4j.jsonrpc.Launcher;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Utility class for launching {@link com.itsaky.androidide.tooling.api.IToolingApiClient}.
 *
 * @author Akash Yadav
 */
public class ToolingClientLauncher {
    public static Launcher<IToolingApiServer> createLauncher(
            IToolingApiClient client, InputStream in, OutputStream out) {
        return new Launcher.Builder<IToolingApiServer>()
                .setInput(in)
                .setOutput(out)
                .setLocalService(client)
                .setRemoteInterface(IToolingApiServer.class)
                .configureGson(ToolingClientLauncher::configureGson)
                .create();
    }

    public static void configureGson(GsonBuilder builder) {
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(IGradleProject.class)
                        .registerSubtype(DefaultGradleProject.class)
                        .registerSubtype(DefaultAndroidModule.class));

        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ILaunchable.class)
                        .registerSubtype(DefaultTask.class)
                        .registerSubtype(DefaultGradleTask.class));
    }
}
