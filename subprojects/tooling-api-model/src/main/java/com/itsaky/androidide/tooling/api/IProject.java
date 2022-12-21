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

package com.itsaky.androidide.tooling.api;

import com.itsaky.androidide.tooling.api.messages.VariantDataRequest;
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData;
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData;
import com.itsaky.androidide.tooling.api.model.AndroidModule;
import com.itsaky.androidide.tooling.api.model.GradleTask;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;

import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Root Gradle project. The getter methods return completed {@link CompletableFuture}s.
 *
 * @author Akash Yadav
 */
@JsonSegment("project")
public interface IProject {

  String FILE_PATH_NOT_AVAILABLE = "<not_available>";

  @NotNull
  @JsonRequest
  CompletableFuture<Boolean> isProjectInitialized();

  @NotNull
  @JsonRequest
  CompletableFuture<String> getName();

  @NotNull
  @JsonRequest
  CompletableFuture<String> getDescription();

  @NotNull
  @JsonRequest
  CompletableFuture<String> getProjectPath();

  @NotNull
  @JsonRequest
  CompletableFuture<File> getProjectDir();

  @NotNull
  @JsonRequest
  CompletableFuture<Type> getType();

  @NotNull
  @JsonRequest
  CompletableFuture<File> getBuildDir();

  @NotNull
  @JsonRequest
  CompletableFuture<File> getBuildScript();

  @NotNull
  @JsonRequest
  CompletableFuture<List<GradleTask>> getTasks();

  @NotNull
  @JsonRequest
  CompletableFuture<List<SimpleModuleData>> listModules();

  @NotNull
  @JsonRequest
  CompletableFuture<SimpleVariantData> getVariantData(@NotNull VariantDataRequest request);

  @NotNull
  @JsonRequest
  CompletableFuture<IdeGradleProject> findByPath(@NotNull String path);

  @NotNull
  @JsonRequest
  CompletableFuture<AndroidModule> findFirstAndroidModule();

  @NotNull
  @JsonRequest
  CompletableFuture<AndroidModule> findFirstAndroidAppModule();

  @NotNull
  @JsonRequest
  CompletableFuture<List<AndroidModule>> findAndroidModules();

  /** Type of the {@link IProject}. */
  enum Type {

    /** A simple Gradle project. Only root projects are represented by this type. */
    Gradle,

    /**
     * An Android project. Mostly module projects are of this type. But in some cases, this type can
     * also be applied to a root Gradle project.
     */
    Android,

    /**
     * A Java project. Usually, module projects which are not {@link Type#Android} type are of this
     * type.
     */
    Java,

    /** An unknown project type. */
    Unknown
  }
}
