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

package com.itsaky.androidide.tooling.api.model;

import com.itsaky.androidide.tooling.api.messages.FindProjectParams;

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
    CompletableFuture<File> getBuildDir();

    @NotNull
    @JsonRequest
    CompletableFuture<File> getBuildScript();

    @NotNull
    @JsonRequest
    CompletableFuture<List<IdeGradleTask>> getTasks();

    @NotNull
    @JsonRequest
    CompletableFuture<List<IdeGradleProject>> getModules();

    @NotNull
    @JsonRequest
    CompletableFuture<IdeGradleProject> findByPath(FindProjectParams params);

    @NotNull
    @JsonRequest
    CompletableFuture<List<IdeAndroidModule>> findAndroidModules();

    @NotNull
    @JsonRequest
    CompletableFuture<IdeAndroidModule> findFirstAndroidModule();
}
