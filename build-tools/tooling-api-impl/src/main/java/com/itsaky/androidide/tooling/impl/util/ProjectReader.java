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

package com.itsaky.androidide.tooling.impl.util;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import com.android.builder.model.v2.models.AndroidProject;
import com.google.gson.GsonBuilder;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
import com.itsaky.androidide.tooling.api.model.util.ProjectBuilder;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;
import com.itsaky.androidide.utils.ILogger;

import org.gradle.tooling.ConfigurableLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.GradleTask;

/**
 * @author Akash Yadav
 */
public class ProjectReader {

    private static final ILogger LOG = newInstance("test");

    public static IdeGradleProject read(ProjectConnection connection) {
        final var watch = new StopWatch("Read project from connection");
        final var gradle = connection.getModel(GradleProject.class);
        if (gradle == null) {
            LOG.error("Cannot build model for", GradleProject.class);
            return null;
        } else {
            watch.lap("Built " + GradleProject.class.getName() + " model");
        }

        final var root = buildFromModel(gradle, false, true);

        for (var sub : gradle.getChildren()) {
            LOG.debug("Creating project connection for sub-project:", sub.getPath());
            final var subConnection =
                    GradleConnector.newConnector()
                            .forProjectDirectory(sub.getProjectDirectory())
                            .connect();
            watch.lapFromLast("Project connection for sub project created");

            final var modelBuilder = subConnection.model(AndroidProject.class);
            addProperty(modelBuilder, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);

            final AndroidProject android;
            try {
                android = modelBuilder.get();
            } catch (Throwable e) {
                LOG.error("Unable to get AndroidProject model", e.getMessage());
                continue;
            }

            watch.lapFromLast("Built " + AndroidProject.class.getName() + " model");
            LOG.debug("AndroidProject model for", sub.getPath(), "is", android);
        }

        final var gsonBuilder = new GsonBuilder();
        ToolingApiLauncher.configureGson(gsonBuilder);
        LOG.debug(
                "Built",
                IdeGradleProject.class.getName(),
                "model:",
                gsonBuilder.create().toJson(root));

        watch.log();
        return root;
    }

    private static void addProperty(
            ConfigurableLauncher<?> launcher, String property, Object value) {
        launcher.addArguments(String.format("-P%s=%s", property, value));
    }

    private static IdeGradleProject buildFromModel(
            GradleProject gradle, boolean fillSubprojects, boolean fillTasks) {
        final var builder = new ProjectBuilder();
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());

        final var project = builder.buildGradleProject();

        if (fillSubprojects) {
            for (final var subGradle : gradle.getChildren()) {
                project.getSubprojects().add(buildFromModel(subGradle, true, fillTasks));
            }
        }

        if (fillTasks) {
            for (final var task : gradle.getTasks()) {
                project.getTasks().add(buildFromModel(project, task));
            }
        }

        return project;
    }

    private static IdeGradleTask buildFromModel(IdeGradleProject project, GradleTask task) {
        return new IdeGradleTask (
                task.getName(),
                task.getDescription(),
                task.getGroup(),
                task.getPath(),
                task.getDisplayName(),
                task.isPublic(),
                project.getProjectPath());
    }
}
