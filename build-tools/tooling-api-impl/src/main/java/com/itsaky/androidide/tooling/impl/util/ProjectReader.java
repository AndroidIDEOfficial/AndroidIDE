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
import com.android.builder.model.v2.models.ModelBuilderParameter;
import com.android.builder.model.v2.models.ProjectSyncIssues;
import com.android.builder.model.v2.models.VariantDependencies;
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
import com.itsaky.androidide.tooling.api.model.internal.DefaultProjectSyncIssues;
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant;
import com.itsaky.androidide.tooling.api.model.util.AndroidModulePropertyCopier;
import com.itsaky.androidide.tooling.api.model.util.ProjectBuilder;
import com.itsaky.androidide.tooling.impl.progress.LoggingProgressListener;
import com.itsaky.androidide.utils.ILogger;

import org.gradle.tooling.ConfigurableLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.UnknownModelException;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.GradleTask;

import java.util.Map;

/**
 * @author Akash Yadav
 */
public class ProjectReader {

    private static final ILogger LOG = newInstance("test");

    public static IdeGradleProject read(
            ProjectConnection connection, Map<String, DefaultProjectSyncIssues> outIssues) {
        final var gradleModel = connection.getModel(GradleProject.class);
        if (gradleModel == null) {
            return null;
        }

        return buildProjectModel(connection, gradleModel, outIssues);
    }

    private static IdeGradleProject buildProjectModel(
            ProjectConnection connection,
            GradleProject gradleModel,
            Map<String, DefaultProjectSyncIssues> outIssues) {
        try {
            final var modelBuilder = connection.model(AndroidProject.class);
            addProperty(modelBuilder, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);
            addProperty(modelBuilder, AndroidProject.PROPERTY_INVOKED_FROM_IDE, true);
            final var android = modelBuilder.get();

            if (android == null) {
                throw new UnknownModelException(
                        "Project " + gradleModel.getPath() + " is not an Android project.");
            }

            final var module = buildAndroidProjectModel(gradleModel, android, outIssues);
            readDependencies(connection, module);

            final var issues = readSyncIssues(connection);
            outIssues.put(gradleModel.getPath(), issues);

            return module;
        } catch (Throwable error) {
            try {
                LOG.info("Building IdeGradleProject model for project:", gradleModel.getPath());
                return buildGradleProjectModel(gradleModel, outIssues);
            } catch (Throwable e) {
                LOG.error("Unable to create model for project", e);
                return null;
            }
        }
    }

    private static DefaultProjectSyncIssues readSyncIssues(ProjectConnection connection) {
        final var builder = connection.model(ProjectSyncIssues.class);
        addProperty(builder, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);
        addProperty(builder, AndroidProject.PROPERTY_INVOKED_FROM_IDE, true);

        final var issues = builder.get();
        return AndroidModulePropertyCopier.INSTANCE.copy(issues);
    }

    private static void readDependencies(ProjectConnection connection, IdeAndroidModule android) {
        for (final var variant : android.getVariants()) {

            fillVariantDependencies(connection, android, variant);

            // FIXME: Don't know how to fetch dependency jars using v2 model classes.
            //        This should be replaced with something that uses v2 of the model builder API
            android.getVariantDependencyJars()
                    .put(
                            variant.getName(),
                            LegacyProjectReader.INSTANCE.findVariantDependencyJars(
                                    connection, variant.getName()));
        }
    }

    @SuppressWarnings("unused")
    private static void fillVariantDependencies(
            ProjectConnection connection, IdeAndroidModule android, DefaultVariant variant) {
        final var name = variant.getName();
        final var modelFinder =
                connection.action(
                        controller ->
                                controller.findModel(
                                        VariantDependencies.class,
                                        ModelBuilderParameter.class,
                                        parameter -> parameter.setVariantName(name)));
        addProperty(modelFinder, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);
        addProperty(modelFinder, AndroidProject.PROPERTY_INVOKED_FROM_IDE, true);
        modelFinder.addProgressListener(new LoggingProgressListener());
        final var variantDependencies =
                AndroidModulePropertyCopier.INSTANCE.copy(modelFinder.run());
        android.getVariantDependencies().put(name, variantDependencies);
    }

    private static void addProperty(
            ConfigurableLauncher<?> launcher, String property, Object value) {
        launcher.addArguments(String.format("-P%s=%s", property, value));
    }

    private static IdeAndroidModule buildAndroidProjectModel(
            GradleProject gradle,
            AndroidProject android,
            Map<String, DefaultProjectSyncIssues> outIssues) {
        LOG.debug("Building IdeAndroidModule for project:", gradle.getPath());
        final var builder = new ProjectBuilder();
        final var copier = AndroidModulePropertyCopier.INSTANCE;
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());
        builder.setVariants(copier.copyVariants(android.getVariants()));
        builder.setJavaCompileOptions(copier.copy(android.getJavaCompileOptions()));
        builder.setResourcePrefix(android.getResourcePrefix());
        builder.setDynamicFeatures(android.getDynamicFeatures());
        builder.setViewBindingOptions(copier.copy(android.getViewBindingOptions()));
        builder.setFlags(copier.copy(android.getFlags()));
        builder.setModelSyncFiles(copier.copyModelSyncFiles(android.getModelSyncFiles()));
        builder.setLintChecksJars(android.getLintChecksJars());

        final var module = builder.buildAndroidModule();
        addSubprojects(gradle, module, outIssues);
        addTasks(gradle, module);

        return module;
    }

    private static IdeGradleProject buildGradleProjectModel(
            GradleProject gradle, Map<String, DefaultProjectSyncIssues> outIssues) {
        final var builder = new ProjectBuilder();
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());

        final var project = builder.buildGradleProject();
        addSubprojects(gradle, project, outIssues);
        addTasks(gradle, project);

        return project;
    }

    private static void addTasks(GradleProject gradle, IdeGradleProject project) {
        for (final var task : gradle.getTasks()) {
            project.getTasks().add(buildGradleTaskModel(project, task));
        }
    }

    private static void addSubprojects(
            GradleProject gradle,
            IdeGradleProject project,
            Map<String, DefaultProjectSyncIssues> outIssues) {
        for (final var subGradle : gradle.getChildren()) {
            final var connection =
                    GradleConnector.newConnector()
                            .forProjectDirectory(subGradle.getProjectDirectory())
                            .connect();
            final var sub = buildProjectModel(connection, subGradle, outIssues);
            if (sub != null) {
                project.getSubprojects().add(sub);
            }
        }
    }

    private static IdeGradleTask buildGradleTaskModel(IdeGradleProject project, GradleTask task) {
        return new IdeGradleTask(
                task.getName(),
                task.getDescription(),
                task.getGroup(),
                task.getPath(),
                task.getDisplayName(),
                task.isPublic(),
                project.getProjectPath());
    }
}
