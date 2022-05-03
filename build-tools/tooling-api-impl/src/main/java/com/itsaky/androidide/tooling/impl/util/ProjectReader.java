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

import static com.itsaky.androidide.tooling.impl.Main.applyCommonArguments;
import static com.itsaky.androidide.utils.ILogger.newInstance;
import static java.util.Collections.emptyList;

import com.android.builder.model.v2.ide.ProjectType;
import com.android.builder.model.v2.models.AndroidProject;
import com.android.builder.model.v2.models.BasicAndroidProject;
import com.android.builder.model.v2.models.ModelBuilderParameter;
import com.android.builder.model.v2.models.ProjectSyncIssues;
import com.android.builder.model.v2.models.VariantDependencies;
import com.android.builder.model.v2.models.Versions;
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.IdeGradleTask;
import com.itsaky.androidide.tooling.api.model.IdeJavaModule;
import com.itsaky.androidide.tooling.api.model.JavaContentRoot;
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleExternalDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleProjectDependency;
import com.itsaky.androidide.tooling.api.model.JavaSourceDirectory;
import com.itsaky.androidide.tooling.api.model.internal.DefaultProjectSyncIssues;
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant;
import com.itsaky.androidide.tooling.api.model.util.AndroidModulePropertyCopier;
import com.itsaky.androidide.tooling.api.model.util.ProjectBuilder;
import com.itsaky.androidide.tooling.impl.progress.LoggingProgressListener;
import com.itsaky.androidide.utils.ILogger;

import org.gradle.tooling.BuildController;
import org.gradle.tooling.ConfigurableLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ModelBuilder;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.UnknownModelException;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.GradleTask;
import org.gradle.tooling.model.idea.IdeaDependency;
import org.gradle.tooling.model.idea.IdeaModule;
import org.gradle.tooling.model.idea.IdeaModuleDependency;
import org.gradle.tooling.model.idea.IdeaProject;
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Akash Yadav
 */
public class ProjectReader {

    private static final ILogger LOG = newInstance("ProjectReader");

    public static IdeGradleProject read(
            ProjectConnection connection, Map<String, DefaultProjectSyncIssues> outIssues) {
        final var watch = new StopWatch("Create basic models");
        LOG.debug("Creating Idea project model...");
        final var builder = connection.model(IdeaProject.class);
        applyCommonArguments(builder);
        final var ideaProject = builder.get();
        watch.lapFromLast("Idea project model created");

        return buildGradleProjectModel(ideaProject, outIssues);
    }

    public static IdeAndroidModule buildAndroidModuleProject(
            GradleProject gradle, AndroidProject android, ProjectType type) {
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
        builder.setModelSyncFiles(emptyList());
        builder.setLintChecksJars(android.getLintChecksJars());
        builder.setProjectType(type);

        final var module = builder.buildAndroidModule();
        addTasks(gradle, module);

        return module;
    }

    private static void addTasks(GradleProject gradle, IdeGradleProject project) {
        for (final var task : gradle.getTasks()) {
            project.getTasks().add(buildGradleTaskModel(project, task));
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

    private static IdeGradleProject buildModuleProject(
            ProjectConnection connection,
            IdeaModule ideaModule,
            Map<String, DefaultProjectSyncIssues> outIssues) {

        final var gradle = ideaModule.getGradleProject();

        try {
            LOG.debug("Trying to create model for Android project...");
            final var executor =
                    connection.action(controller -> createAndroidModelInfo(gradle, controller));

            applyCommonArguments(executor);
            applyAndroidModelBuilderProps(executor);

            final var info = executor.run();
            if (info == null) {
                LOG.debug(
                        "ModelInfoContainer is null. Project",
                        gradle.getName(),
                        "is definitely not an Android project.");
                throw new UnknownModelException(
                        "Project " + gradle.getName() + " is not an AndroidProject");
            }

            LOG.debug("ModelInfoContainer created for project:", gradle.getName(), info.getSyncIssues ());
            outIssues.put(gradle.getPath(), info.getSyncIssues());
            return info.getProject();
        } catch (Throwable error) {
            try {
                LOG.info("Building IdeGradleProject model for project:", gradle.getPath());
                if (!(error instanceof UnknownModelException)) {
                    // If the error is something else than UnknownModelException, we should log it
                    LOG.error(error);
                }

                return buildJavaModuleProject(gradle, ideaModule);
            } catch (Throwable e) {
                LOG.error("Unable to create model for project", e);
                return null;
            }
        }
    }

    private static ModelInfoContainer createAndroidModelInfo(
            GradleProject gradle, BuildController controller) {
        final var watch = new StopWatch("GetAndroidModelV2Action");
        final var versions = controller.findModel(gradle, Versions.class);
        if (versions == null) {
            LOG.warn("Project", gradle.getName(), "is not an AndroidProject");
            return null;
        }

        final var basicAndroid = controller.findModel(gradle, BasicAndroidProject.class);
        final var android = controller.findModel(gradle, AndroidProject.class);
        final var module =
                ProjectReader.buildAndroidModuleProject(
                        gradle, android, basicAndroid.getProjectType());
        module.setBoothclasspaths(basicAndroid.getBootClasspath());
        module.setMainSourceSet(
                basicAndroid.getMainSourceSet() == null
                        ? null
                        : AndroidModulePropertyCopier.INSTANCE.copy(
                                basicAndroid.getMainSourceSet()));

        for (var variant : android.getVariants()) {
            final var variantDependencies =
                    controller.findModel(
                            gradle,
                            VariantDependencies.class,
                            ModelBuilderParameter.class,
                            it -> it.setVariantName(variant.getName()));

            module.getVariantDependencies()
                    .put(
                            variant.getName(),
                            AndroidModulePropertyCopier.INSTANCE.copy(variantDependencies));
        }

        final var issues = controller.findModel(gradle, ProjectSyncIssues.class);
        final var syncIssues =
                issues == null
                        ? new DefaultProjectSyncIssues(emptyList())
                        : AndroidModulePropertyCopier.INSTANCE.copy(issues);
        watch.log();

        return new ModelInfoContainer(module, syncIssues);
    }

    private static IdeJavaModule buildJavaModuleProject(GradleProject gradle, IdeaModule idea) {

        final var builder = new ProjectBuilder();
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());
        builder.setContentRoots(collectContentRoots(idea));
        builder.setJavaDependencies(collectJavaDependencies(idea));

        final var project = builder.buildJavaModule();
        addTasks(gradle, project);
        return project;
    }

    private static List<? extends JavaModuleDependency> collectJavaDependencies(IdeaModule idea) {
        final var list = new ArrayList<JavaModuleDependency>();
        for (IdeaDependency dependency : idea.getDependencies()) {
            // TODO There might be unresolved dependencies here. We need to handle them too.
            if (dependency instanceof IdeaSingleEntryLibraryDependency) {
                final var external = (IdeaSingleEntryLibraryDependency) dependency;
                // There might be multiple entries of same dependency, but with different scope
                // So we only add dependencies with 'RUNTIME' scope
                if (external.getScope().getScope().equals("RUNTIME")) {
                    list.add(
                            new JavaModuleExternalDependency(
                                    external.getFile(),
                                    external.getSource(),
                                    external.getJavadoc()));
                }
            } else if (dependency instanceof IdeaModuleDependency) {
                final var project = ((IdeaModuleDependency) dependency);
                list.add(new JavaModuleProjectDependency(project.getTargetModuleName()));
            }
        }
        return list;
    }

    private static List<JavaContentRoot> collectContentRoots(IdeaModule module) {
        final var list = new ArrayList<JavaContentRoot>();
        for (var contentRoot : module.getContentRoots()) {
            final var thisRoot = new JavaContentRoot();
            for (var sourceDir : contentRoot.getSourceDirectories()) {
                thisRoot.getSourceDirectories()
                        .add(
                                new JavaSourceDirectory(
                                        sourceDir.getDirectory(), sourceDir.isGenerated()));
            }

            for (var testDir : contentRoot.getTestDirectories()) {
                thisRoot.getTestDirectories()
                        .add(
                                new JavaSourceDirectory(
                                        testDir.getDirectory(), testDir.isGenerated()));
            }

            list.add(thisRoot);
        }
        return list;
    }

    private static DefaultProjectSyncIssues readSyncIssues(ProjectConnection connection) {
        final var issues = androidModel(connection, ProjectSyncIssues.class);
        return AndroidModulePropertyCopier.INSTANCE.copy(issues);
    }

    private static <T> T androidModel(ProjectConnection conn, Class<T> tClass) {
        return androidModelBuilder(conn, tClass).get();
    }

    private static <T> ModelBuilder<T> androidModelBuilder(
            ProjectConnection conn, Class<T> tClass) {
        final var builder = conn.model(tClass);
        applyAndroidModelBuilderProps(builder);
        return builder;
    }

    private static <T extends ConfigurableLauncher<T>> void applyAndroidModelBuilderProps(
            ConfigurableLauncher<T> launcher) {
        addProperty(launcher, AndroidProject.PROPERTY_BUILD_MODEL_ONLY, true);
        addProperty(launcher, AndroidProject.PROPERTY_INVOKED_FROM_IDE, true);
    }

    private static void addProperty(
            ConfigurableLauncher<?> launcher, String property, Object value) {
        launcher.addArguments(String.format("-P%s=%s", property, value));
    }

    private static void readDependencies(ProjectConnection connection, IdeAndroidModule android) {
        for (final var variant : android.getVariants()) {
            fillVariantDependencies(connection, android, variant);
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
        applyCommonArguments(modelFinder);
        modelFinder.addProgressListener(new LoggingProgressListener());
        final var variantDependencies =
                AndroidModulePropertyCopier.INSTANCE.copy(modelFinder.run());
        android.getVariantDependencies().put(name, variantDependencies);
    }

    private static IdeGradleProject buildGradleProjectModel(
            IdeaProject ideaProject, Map<String, DefaultProjectSyncIssues> outIssues) {
        final var hasGradle =
                ideaProject.getModules().stream()
                        .map(IdeaModule::getGradleProject)
                        .filter(it -> it.getPath().equals(":"))
                        .findAny();

        if (hasGradle.isEmpty()) {
            throw new IllegalArgumentException(
                    "No GradleProject model is associated with project path: " + ":");
        }

        final var gradle = hasGradle.get();
        final var builder = new ProjectBuilder();
        builder.setName(gradle.getName());
        builder.setDescription(gradle.getDescription());
        builder.setPath(gradle.getPath());
        builder.setProjectDir(gradle.getProjectDirectory());
        builder.setBuildDir(gradle.getBuildDirectory());
        builder.setBuildScript(gradle.getBuildScript().getSourceFile());

        final var project = builder.buildGradleProject();
        addModules(ideaProject, project, outIssues);
        addTasks(gradle, project);

        return project;
    }

    private static void addModules(
            IdeaProject ideaProject,
            IdeGradleProject project,
            Map<String, DefaultProjectSyncIssues> outIssues) {
        for (final var module : ideaProject.getModules()) {
            final var gradle = module.getGradleProject();
            if (gradle.getPath().equals(":")) {
                // Do not try to add the root project again
                continue;
            }

            final var connection =
                    GradleConnector.newConnector()
                            .forProjectDirectory(gradle.getProjectDirectory())
                            .connect();
            final var sub = buildModuleProject(connection, module, outIssues);
            if (sub != null) {
                project.getModules().add(sub);
            }
        }
    }
}
