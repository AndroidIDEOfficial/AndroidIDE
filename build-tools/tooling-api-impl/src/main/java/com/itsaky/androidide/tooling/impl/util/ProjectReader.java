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

import static com.itsaky.androidide.tooling.impl.Main.finalizeLauncher;
import static com.itsaky.androidide.utils.ILogger.MSG_SEPARATOR;
import static java.util.Collections.emptyList;

import com.android.builder.model.ModelBuilderParameter;
import com.android.builder.model.v2.ide.ProjectType;
import com.android.builder.model.v2.models.AndroidProject;
import com.android.builder.model.v2.models.BasicAndroidProject;
import com.android.builder.model.v2.models.ProjectSyncIssues;
import com.android.builder.model.v2.models.VariantDependencies;
import com.android.builder.model.v2.models.Versions;
import com.itsaky.androidide.builder.model.DefaultLibrary;
import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.tooling.api.model.AndroidModule;
import com.itsaky.androidide.tooling.api.model.GradleArtifact;
import com.itsaky.androidide.tooling.api.model.GradleTask;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.JavaContentRoot;
import com.itsaky.androidide.tooling.api.model.JavaModule;
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleExternalDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleProjectDependency;
import com.itsaky.androidide.tooling.api.model.JavaSourceDirectory;
import com.itsaky.androidide.tooling.api.model.util.AndroidModulePropertyCopier;
import com.itsaky.androidide.tooling.api.model.util.ProjectBuilder;
import com.itsaky.androidide.tooling.impl.Main;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.LogUtils;

import org.gradle.tooling.BuildController;
import org.gradle.tooling.ConfigurableLauncher;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.UnknownModelException;
import org.gradle.tooling.model.GradleProject;
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

  public static IdeGradleProject read(
      ProjectConnection connection, Map<String, DefaultProjectSyncIssues> outIssues) {
    final var buildActionExecutor =
        connection.action(
            controller -> {
              log("Creating Idea project model...");
              final var ideaProject = controller.findModel(IdeaProject.class);
              log("IdeaProject model created...");
              return buildGradleProjectModel(ideaProject, controller, outIssues);
            });
    finalizeLauncher(buildActionExecutor);
    applyAndroidModelBuilderProps(buildActionExecutor);

    final var logger = ILogger.newInstance("ProjectReader");
    logger.warn("Starting build. See build output for more details...");

    if (Main.client != null) {
      Main.client.logOutput("Starting build...");
    }

    return buildActionExecutor.run();
  }

  public static AndroidModule buildAndroidModuleProject(
      GradleProject gradle, AndroidProject android, ProjectType type) {
    log("Building AndroidModule for project: " + gradle.getName());
    final var builder = new ProjectBuilder();
    final var copier = AndroidModulePropertyCopier.INSTANCE;
    builder.setName(gradle.getName());
    builder.setDescription(gradle.getDescription());
    builder.setPath(gradle.getPath());
    builder.setProjectDir(gradle.getProjectDirectory());
    builder.setBuildDir(gradle.getBuildDirectory());
    builder.setBuildScript(gradle.getBuildScript().getSourceFile());
    builder.setSimpleVariants(copier.asSimpleVariants(android.getVariants()));
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
      project.tasks.add(buildGradleTaskModel(project, task));
    }
  }

  private static GradleTask buildGradleTaskModel(
      IdeGradleProject project, org.gradle.tooling.model.GradleTask task) {
    return new GradleTask(
        task.getName(),
        task.getDescription(),
        task.getGroup(),
        task.getPath(),
        task.getDisplayName(),
        task.isPublic(),
        project.projectPath);
  }

  private static IdeGradleProject buildModuleProject(
      BuildController controller,
      IdeaModule ideaModule,
      Map<String, DefaultProjectSyncIssues> outIssues) {
    return buildModuleProject(controller, ideaModule, outIssues, false);
  }

  private static IdeGradleProject buildModuleProject(
      BuildController controller,
      IdeaModule ideaModule,
      Map<String, DefaultProjectSyncIssues> outIssues,
      boolean androidOnly) {

    final var gradle = ideaModule.getGradleProject();

    try {
      log("");
      log("Trying to create model for Android project...");
      final var info = createAndroidModelInfo(gradle, controller);
      if (info == null) {
        throw new UnknownModelException(
            "Project " + gradle.getName() + " is not an AndroidProject");
      }

      log("ModelInfoContainer created for project:", gradle.getName(), info.getSyncIssues());
      outIssues.put(gradle.getPath(), info.getSyncIssues());
      return info.getProject();
    } catch (Throwable error) {

      if (androidOnly) {
        log(error.getMessage());
        return null;
      }

      try {
        log("Building IdeGradleProject model for project: " + gradle.getPath());
        if (!(error instanceof UnknownModelException)) {
          // If the error is something else than UnknownModelException, we should log it
          error.printStackTrace();
        }

        return buildJavaModuleProject(gradle, ideaModule);
      } catch (Throwable e) {
        log("Unable to create model for project");
        e.printStackTrace();
        return null;
      }
    }
  }

  private static ModelInfoContainer createAndroidModelInfo(
      GradleProject gradle, BuildController controller) {
    final var start = System.currentTimeMillis();
    final var versions = controller.findModel(gradle, Versions.class);
    if (versions == null) {
      return null;
    }

    if (versions.getAgp().compareTo(Main.MIN_SUPPORTED_AGP_VERSION) < 0) {
      throw new UnsupportedOperationException(
          "Android Gradle Plugin version "
              + versions.getAgp()
              + " is not supported by AndroidIDE. "
              + "Please update your project to use at least v"
              + Main.MIN_SUPPORTED_AGP_VERSION
              + " of Android Gradle Plugin to build this project.");
    }

    final var basicAndroid = controller.findModel(gradle, BasicAndroidProject.class);

    log("Fetching project model...");
    final var android = controller.findModel(gradle, AndroidProject.class);
    final var module = buildAndroidModuleProject(gradle, android, basicAndroid.getProjectType());
    module.setBootClassPaths(basicAndroid.getBootClasspath());
    module.setMainSourceSet(
        basicAndroid.getMainSourceSet() == null
            ? null
            : AndroidModulePropertyCopier.INSTANCE.copy(basicAndroid.getMainSourceSet()));

    log("Fetching 'debug' dependencies");
    final var variantDependencies =
        controller.findModel(
            gradle,
            VariantDependencies.class,
            ModelBuilderParameter.class,
            it -> it.setVariantName("debug"));

    final var libraries = new ArrayList<DefaultLibrary>();
    variantDependencies
        .getLibraries()
        .forEach((s, library) -> libraries.add(AndroidModulePropertyCopier.INSTANCE.copy(library)));

    module.setLibraries(libraries);

    log("Fetching project sync issues...");
    final var issues = controller.findModel(gradle, ProjectSyncIssues.class);
    final var syncIssues =
        issues == null
            ? new DefaultProjectSyncIssues(emptyList())
            : AndroidModulePropertyCopier.INSTANCE.copy(issues);

    log(
        "Android project model for project '" + gradle.getName() + "' created in",
        System.currentTimeMillis() - start,
        "ms");
    return new ModelInfoContainer(module, syncIssues);
  }

  private static JavaModule buildJavaModuleProject(GradleProject gradle, IdeaModule idea) {
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
      log("Java dependency: ", dependency, dependency.getClass().getName());
      // TODO There might be unresolved dependencies here. We need to handle them too.
      if (dependency instanceof IdeaSingleEntryLibraryDependency) {
        final var external = (IdeaSingleEntryLibraryDependency) dependency;
        // There might be multiple entries of same dependency, but with different scope
        // So we only add dependencies with 'RUNTIME' scope
        if (external.getScope().getScope().equals("RUNTIME")) {
          final var moduleVersion = external.getGradleModuleVersion();
          list.add(
              new JavaModuleExternalDependency(
                  external.getFile(),
                  external.getSource(),
                  external.getJavadoc(),
                  new GradleArtifact(
                      moduleVersion.getGroup(),
                      moduleVersion.getName(),
                      moduleVersion.getVersion()),
                  dependency.getScope().getScope(),
                  dependency.getExported()));
        }
      } else if (dependency instanceof IdeaModuleDependency) {
        final var project = ((IdeaModuleDependency) dependency);
        list.add(
            new JavaModuleProjectDependency(
                project.getTargetModuleName(),
                dependency.getScope().getScope(),
                dependency.getExported()));
      }
    }
    return list;
  }

  private static List<JavaContentRoot> collectContentRoots(IdeaModule module) {
    final var list = new ArrayList<JavaContentRoot>();
    for (var contentRoot : module.getContentRoots()) {
      final var thisRoot = new JavaContentRoot();
      for (var sourceDir : contentRoot.getSourceDirectories()) {
        thisRoot
            .getSourceDirectories()
            .add(new JavaSourceDirectory(sourceDir.getDirectory(), sourceDir.isGenerated()));
      }

      for (var testDir : contentRoot.getTestDirectories()) {
        thisRoot
            .getTestDirectories()
            .add(new JavaSourceDirectory(testDir.getDirectory(), testDir.isGenerated()));
      }

      list.add(thisRoot);
    }
    return list;
  }

  private static <T extends ConfigurableLauncher<T>> void applyAndroidModelBuilderProps(
      ConfigurableLauncher<T> launcher) {
    addProperty(launcher, AndroidModule.PROPERTY_BUILD_MODEL_ONLY, true);
    addProperty(launcher, AndroidModule.PROPERTY_INVOKED_FROM_IDE, true);
  }

  private static void addProperty(ConfigurableLauncher<?> launcher, String property, Object value) {
    launcher.addArguments(String.format("-P%s=%s", property, value));
  }

  private static IdeGradleProject buildGradleProjectModel(
      IdeaProject ideaProject,
      BuildController controller,
      Map<String, DefaultProjectSyncIssues> outIssues) {
    final var rootModule =
        ideaProject.getModules().stream()
            .filter(it -> it.getGradleProject().getPath().equals(":"))
            .findAny();

    if (rootModule.isEmpty()) {
      throw new IllegalArgumentException(
          "No GradleProject model is associated with project path: " + ":");
    }

    IdeGradleProject project = buildModuleProject(controller, rootModule.get(), outIssues, true);
    final var gradle = rootModule.get().getGradleProject();
    if (project == null) {
      final var builder = new ProjectBuilder();
      builder.setName(gradle.getName());
      builder.setDescription(gradle.getDescription());
      builder.setPath(gradle.getPath());
      builder.setProjectDir(gradle.getProjectDirectory());
      builder.setBuildDir(gradle.getBuildDirectory());
      builder.setBuildScript(gradle.getBuildScript().getSourceFile());

      project = builder.buildGradleProject();

      // If the root project is an Android project
      // Then the tasks have already been added into the project
      // So we only add them here if we create a Gradle project model
      addTasks(gradle, project);
    } else {
      log("Root project is an Android project...");
    }

    addModules(ideaProject, project, controller, outIssues);

    return project;
  }

  private static void addModules(
      IdeaProject ideaProject,
      IdeGradleProject project,
      BuildController controller,
      Map<String, DefaultProjectSyncIssues> outIssues) {
    for (final var module : ideaProject.getModules()) {
      final var gradle = module.getGradleProject();
      if (gradle.getPath().equals(":")) {
        // Do not try to add the root project again
        continue;
      }

      final var sub = buildModuleProject(controller, module, outIssues);
      if (sub != null) {
        project.moduleProjects.add(sub);
      }
    }
  }

  private static void log(Object... messages) {
    final var line =
        new LogLine(ILogger.Priority.DEBUG, "BuildActionExecutor", generateMessage(messages));
    System.err.println(line.toSimpleString());
  }

  private static String generateMessage(Object... messages) {
    StringBuilder sb = new StringBuilder();
    if (messages == null) {
      return "null";
    }

    for (Object msg : messages) {
      sb.append(msg instanceof Throwable ? "\n" : MSG_SEPARATOR);
      sb.append(msg instanceof Throwable ? LogUtils.getFullStackTrace(((Throwable) msg)) : msg);
      sb.append(msg instanceof Throwable ? "\n" : MSG_SEPARATOR);
    }

    return sb.toString();
  }
}
