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

import com.android.builder.model.AndroidArtifact;
import com.android.builder.model.v2.ModelSyncFile;
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags;
import com.android.builder.model.v2.ide.ApiVersion;
import com.android.builder.model.v2.ide.BundleInfo;
import com.android.builder.model.v2.ide.JavaArtifact;
import com.android.builder.model.v2.ide.JavaCompileOptions;
import com.android.builder.model.v2.ide.SourceProvider;
import com.android.builder.model.v2.ide.SourceSetContainer;
import com.android.builder.model.v2.ide.TestInfo;
import com.android.builder.model.v2.ide.TestedTargetVariant;
import com.android.builder.model.v2.ide.Variant;
import com.android.builder.model.v2.ide.ViewBindingOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
import com.itsaky.androidide.tooling.api.model.IdeLaunchable;
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidArtifact;
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidGradlePluginProjectFlags;
import com.itsaky.androidide.tooling.api.model.internal.DefaultApiVersion;
import com.itsaky.androidide.tooling.api.model.internal.DefaultBundleInfo;
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaArtifact;
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaCompileOptions;
import com.itsaky.androidide.tooling.api.model.internal.DefaultModelSyncFile;
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceProvider;
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceSetContainer;
import com.itsaky.androidide.tooling.api.model.internal.DefaultTestInfo;
import com.itsaky.androidide.tooling.api.model.internal.DefaultTestedTargetVariant;
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant;
import com.itsaky.androidide.tooling.api.model.internal.DefaultViewBindingOptions;

import org.eclipse.lsp4j.jsonrpc.Launcher;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.function.Function;

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
