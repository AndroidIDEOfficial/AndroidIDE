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
import com.android.builder.model.v2.models.VariantDependencies;
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
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

/**
 * @author Akash Yadav
 */
public class ProjectReader {

    private static final ILogger LOG = newInstance("test");
    
    public static IdeGradleProject read(ProjectConnection connection) {
        final var gradleModel = connection.getModel(GradleProject.class);
        if (gradleModel == null) {
            return null;
        }

        final var project = buildProjectModel(connection, gradleModel);
        LOG.debug(project);
        return project;
    }

    private static IdeGradleProject buildProjectModel(
            ProjectConnection connection, GradleProject gradleModel) {
        try {
            final var modelBuilder = connection.model(AndroidProject.class);
            addProperty(modelBuilder, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);
            addProperty(modelBuilder, AndroidProject.PROPERTY_INVOKED_FROM_IDE, true);
            final var android = modelBuilder.get();

            if (android == null) {
                throw new UnknownModelException(
                        "Project " + gradleModel.getPath() + " is not an Android project.");
            }

            final var module = buildAndroidProjectModel(gradleModel, android);

            try {
                tryReadDependencies(connection, module);
            } catch (Throwable err) {
                LOG.error("Unable to fetch dependencies for project", module.getProjectPath(), err);
            }

            return module;
        } catch (Throwable error) {
            LOG.warn("Project", gradleModel.getPath(), "is most likely not an Android project");
            try {
                return buildGradleProjectModel(gradleModel);
            } catch (Throwable e) {
                LOG.error("Unable to create model for project", e);
                return null;
            }
        }
    }

    private static void tryReadDependencies(
            ProjectConnection connection, IdeAndroidModule android) {
        for (final var variant : android.getVariants()) {
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

            android.getVariantDependencyJars()
                    .put(name, LegacyProjectReader.INSTANCE.findDependencyJars(connection, name));
        }
    }

    private static IdeAndroidModule buildAndroidProjectModel(
            GradleProject gradle, AndroidProject android) {
        LOG.debug("Building android module model for project:", gradle.getPath());
        final var builder = new ProjectBuilder();
        final var copier = AndroidModulePropertyCopier.INSTANCE;
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());
        builder.setProjectType(android.getProjectType());
        builder.setMainSourceSet(copier.copy(android.getMainSourceSet()));
        builder.setBuildTypeSourceSets(copier.copy(android.getBuildTypeSourceSets()));
        builder.setProductFlavorSourceSets(copier.copy(android.getProductFlavorSourceSets()));
        builder.setVariants(copier.copyVariants(android.getVariants()));
        builder.setBootClasspath(android.getBootClasspath());
        builder.setJavaCompileOptions(copier.copy(android.getJavaCompileOptions()));
        builder.setBuildFolder(android.getBuildFolder());
        builder.setResourcePrefix(android.getResourcePrefix());
        builder.setDynamicFeatures(android.getDynamicFeatures());
        builder.setViewBindingOptions(copier.copy(android.getViewBindingOptions()));
        builder.setFlags(copier.copy(android.getFlags()));
        builder.setLintRuleJars(android.getLintRuleJars());

        final var module = builder.buildAndroidModule();
        addSubprojects(gradle, module);
        addTasks(gradle, module);

        return module;
    }

    private static void addProperty(
            ConfigurableLauncher<?> launcher, String property, Object value) {
        launcher.addArguments(String.format("-P%s=%s", property, value));
    }

    private static IdeGradleProject buildGradleProjectModel(GradleProject gradle) {
        final var builder = new ProjectBuilder();
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());

        final var project = builder.buildGradleProject();
        addSubprojects(gradle, project);
        addTasks(gradle, project);

        return project;
    }

    private static void addTasks(GradleProject gradle, IdeGradleProject project) {
        for (final var task : gradle.getTasks()) {
            project.getTasks().add(buildGradleTaskModel(project, task));
        }
    }

    private static void addSubprojects(GradleProject gradle, IdeGradleProject project) {
        for (final var subGradle : gradle.getChildren()) {
            LOG.debug("Subproject of", gradle.getPath(), "->", subGradle.getPath());
            final var connection =
                    GradleConnector.newConnector()
                            .forProjectDirectory(subGradle.getProjectDirectory())
                            .connect();
            final var sub = buildProjectModel(connection, subGradle);
            if (sub != null) {
                LOG.debug(
                        "Is project",
                        sub.getProjectPath(),
                        "an Android module?:",
                        sub instanceof IdeAndroidModule);
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
