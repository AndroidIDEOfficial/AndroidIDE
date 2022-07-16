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
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions;
import com.itsaky.androidide.builder.model.IJavaCompilerSettings;
import com.itsaky.androidide.tooling.api.IProject;
import com.itsaky.androidide.tooling.api.IToolingApiClient;
import com.itsaky.androidide.tooling.api.IToolingApiServer;
import com.itsaky.androidide.tooling.api.model.AndroidModule;
import com.itsaky.androidide.tooling.api.model.GradleTask;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeLaunchable;
import com.itsaky.androidide.tooling.api.model.JavaModule;
import com.itsaky.androidide.tooling.api.model.JavaModuleCompilerSettings;
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleExternalDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleProjectDependency;
import com.itsaky.androidide.tooling.events.OperationDescriptor;
import com.itsaky.androidide.tooling.events.OperationResult;
import com.itsaky.androidide.tooling.events.ProgressEvent;
import com.itsaky.androidide.tooling.events.StatusEvent;
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationFinishEvent;
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationOperationDescriptor;
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationOperationResult;
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationProgressEvent;
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationStartEvent;
import com.itsaky.androidide.tooling.events.download.FileDownloadFinishEvent;
import com.itsaky.androidide.tooling.events.download.FileDownloadOperationDescriptor;
import com.itsaky.androidide.tooling.events.download.FileDownloadProgressEvent;
import com.itsaky.androidide.tooling.events.download.FileDownloadResult;
import com.itsaky.androidide.tooling.events.download.FileDownloadStartEvent;
import com.itsaky.androidide.tooling.events.internal.DefaultFinishEvent;
import com.itsaky.androidide.tooling.events.internal.DefaultOperationDescriptor;
import com.itsaky.androidide.tooling.events.internal.DefaultOperationResult;
import com.itsaky.androidide.tooling.events.internal.DefaultProgressEvent;
import com.itsaky.androidide.tooling.events.internal.DefaultStartEvent;
import com.itsaky.androidide.tooling.events.task.TaskExecutionResult;
import com.itsaky.androidide.tooling.events.task.TaskFailureResult;
import com.itsaky.androidide.tooling.events.task.TaskFinishEvent;
import com.itsaky.androidide.tooling.events.task.TaskOperationDescriptor;
import com.itsaky.androidide.tooling.events.task.TaskOperationResult;
import com.itsaky.androidide.tooling.events.task.TaskProgressEvent;
import com.itsaky.androidide.tooling.events.task.TaskSkippedResult;
import com.itsaky.androidide.tooling.events.task.TaskStartEvent;
import com.itsaky.androidide.tooling.events.task.TaskSuccessResult;
import com.itsaky.androidide.tooling.events.test.TestFinishEvent;
import com.itsaky.androidide.tooling.events.test.TestOperationDescriptor;
import com.itsaky.androidide.tooling.events.test.TestOperationResult;
import com.itsaky.androidide.tooling.events.test.TestProgressEvent;
import com.itsaky.androidide.tooling.events.test.TestStartEvent;
import com.itsaky.androidide.tooling.events.transform.TransformFinishEvent;
import com.itsaky.androidide.tooling.events.transform.TransformOperationDescriptor;
import com.itsaky.androidide.tooling.events.transform.TransformOperationResult;
import com.itsaky.androidide.tooling.events.transform.TransformProgressEvent;
import com.itsaky.androidide.tooling.events.transform.TransformStartEvent;
import com.itsaky.androidide.tooling.events.work.WorkItemFinishEvent;
import com.itsaky.androidide.tooling.events.work.WorkItemOperationDescriptor;
import com.itsaky.androidide.tooling.events.work.WorkItemOperationResult;
import com.itsaky.androidide.tooling.events.work.WorkItemProgressEvent;
import com.itsaky.androidide.tooling.events.work.WorkItemStartEvent;

import org.eclipse.lsp4j.jsonrpc.Launcher;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Utility class for launching {@link IToolingApiClient} and {@link IToolingApiServer}.
 *
 * @author Akash Yadav
 */
public class ToolingApiLauncher {

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
            .registerSubtype(AndroidModule.class, AndroidModule.class.getName())
            .registerSubtype(JavaModule.class, JavaModule.class.getName())
            .registerSubtype(IdeGradleProject.class, IdeGradleProject.class.getName()));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(IdeLaunchable.class, "gsonType", true)
            .registerSubtype(GradleTask.class, GradleTask.class.getName())
            .registerSubtype(IdeLaunchable.class, IdeLaunchable.class.getName()));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(JavaModuleDependency.class, "gsonType", true)
            .registerSubtype(
                JavaModuleExternalDependency.class, JavaModuleExternalDependency.class.getName())
            .registerSubtype(
                JavaModuleProjectDependency.class, JavaModuleProjectDependency.class.getName()));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(IJavaCompilerSettings.class)
            .registerSubtype(
                DefaultJavaCompileOptions.class, DefaultJavaCompileOptions.class.getName())
            .registerSubtype(
                JavaModuleCompilerSettings.class, JavaModuleCompilerSettings.class.getName()));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(ProgressEvent.class, "gsonType", true)

            // Project configuration
            .registerSubtype(
                ProjectConfigurationProgressEvent.class,
                ProjectConfigurationProgressEvent.class.getName())
            .registerSubtype(
                ProjectConfigurationStartEvent.class,
                ProjectConfigurationStartEvent.class.getName())
            .registerSubtype(
                ProjectConfigurationFinishEvent.class,
                ProjectConfigurationFinishEvent.class.getName())

            // File download
            .registerSubtype(
                FileDownloadProgressEvent.class, FileDownloadProgressEvent.class.getName())
            .registerSubtype(FileDownloadStartEvent.class, FileDownloadStartEvent.class.getName())
            .registerSubtype(FileDownloadFinishEvent.class, FileDownloadFinishEvent.class.getName())

            // Task execution
            .registerSubtype(TaskProgressEvent.class, TaskProgressEvent.class.getName())
            .registerSubtype(TaskStartEvent.class, TaskStartEvent.class.getName())
            .registerSubtype(TaskFinishEvent.class, TaskFinishEvent.class.getName())

            // Test execution
            .registerSubtype(TestProgressEvent.class, TestProgressEvent.class.getName())
            .registerSubtype(TestStartEvent.class, TestStartEvent.class.getName())
            .registerSubtype(TestFinishEvent.class, TestFinishEvent.class.getName())

            // Transform
            .registerSubtype(TransformProgressEvent.class, TransformProgressEvent.class.getName())
            .registerSubtype(TransformStartEvent.class, TransformStartEvent.class.getName())
            .registerSubtype(TransformFinishEvent.class, TransformFinishEvent.class.getName())

            // Work item
            .registerSubtype(WorkItemProgressEvent.class, WorkItemProgressEvent.class.getName())
            .registerSubtype(WorkItemStartEvent.class, WorkItemStartEvent.class.getName())
            .registerSubtype(WorkItemFinishEvent.class, WorkItemFinishEvent.class.getName())

            // Default implementations
            .registerSubtype(DefaultProgressEvent.class, DefaultProgressEvent.class.getName())
            .registerSubtype(DefaultStartEvent.class, DefaultStartEvent.class.getName())
            .registerSubtype(DefaultFinishEvent.class, DefaultFinishEvent.class.getName())

            // Status event
            .registerSubtype(StatusEvent.class, StatusEvent.class.getName()));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(OperationDescriptor.class, "gsonType", true)
            .registerSubtype(
                ProjectConfigurationOperationDescriptor.class,
                ProjectConfigurationOperationDescriptor.class.getName())
            .registerSubtype(
                FileDownloadOperationDescriptor.class,
                FileDownloadOperationDescriptor.class.getName())
            .registerSubtype(TaskOperationDescriptor.class, TaskOperationDescriptor.class.getName())
            .registerSubtype(TestOperationDescriptor.class, TestOperationDescriptor.class.getName())
            .registerSubtype(
                TransformOperationDescriptor.class, TransformOperationDescriptor.class.getName())
            .registerSubtype(
                WorkItemOperationDescriptor.class, WorkItemOperationDescriptor.class.getName())
            .registerSubtype(
                DefaultOperationDescriptor.class, DefaultOperationDescriptor.class.getName()));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(OperationResult.class, "gsonType", true)
            .registerSubtype(
                ProjectConfigurationOperationResult.class,
                ProjectConfigurationOperationResult.class.getName())
            .registerSubtype(FileDownloadResult.class, FileDownloadResult.class.getName())
            .registerSubtype(TaskOperationResult.class, TaskOperationResult.class.getName())
            .registerSubtype(TestOperationResult.class, TestOperationResult.class.getName())
            .registerSubtype(
                TransformOperationResult.class, TransformOperationResult.class.getName())
            .registerSubtype(WorkItemOperationResult.class, WorkItemOperationResult.class.getName())
            .registerSubtype(DefaultOperationResult.class, DefaultOperationResult.class.getName()));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(TaskOperationResult.class, "gsonType", true)
            .registerSubtype(TaskFailureResult.class, TaskFailureResult.class.getName())
            .registerSubtype(TaskSkippedResult.class, TaskSkippedResult.class.getName())
            .registerSubtype(TaskExecutionResult.class, TaskExecutionResult.class.getName())
            .registerSubtype(TaskSuccessResult.class, TaskSuccessResult.class.getName()));
  }

  public static Launcher<Object> newClientLauncher(
      IToolingApiClient client, InputStream in, OutputStream out) {
    return newIoLauncher(
        new Object[] {client}, new Class[] {IToolingApiServer.class, IProject.class}, in, out);
  }

  public static Launcher<Object> newIoLauncher(
      Object[] locals, Class<?>[] remotes, InputStream in, OutputStream out) {
    return new Launcher.Builder<>()
        .setInput(in)
        .setOutput(out)
        .setExecutorService(Executors.newCachedThreadPool())
        .setLocalServices(Arrays.asList(locals))
        .setRemoteInterfaces(Arrays.asList(remotes))
        .configureGson(ToolingApiLauncher::configureGson)
        .setClassLoader(locals[0].getClass().getClassLoader())
        .create();
  }

  public static Launcher<Object> newServerLauncher(
      IToolingApiServer server, IProject project, InputStream in, OutputStream out) {
    return newIoLauncher(
        new Object[] {server, project}, new Class[] {IToolingApiClient.class}, in, out);
  }
}
