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
import com.android.builder.model.v2.ide.GraphItem;
import com.android.builder.model.v2.ide.Library;
import com.android.builder.model.v2.ide.ProjectType;
import com.android.builder.model.v2.models.AndroidProject;
import com.android.builder.model.v2.models.BasicAndroidProject;
import com.android.builder.model.v2.models.ProjectSyncIssues;
import com.android.builder.model.v2.models.VariantDependencies;
import com.android.builder.model.v2.models.Versions;
import com.itsaky.androidide.builder.model.DefaultLibrary;
import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues;
import com.itsaky.androidide.builder.model.IJavaCompilerSettings;
import com.itsaky.androidide.tooling.api.model.AndroidModule;
import com.itsaky.androidide.tooling.api.model.GradleArtifact;
import com.itsaky.androidide.tooling.api.model.GradleTask;
import com.itsaky.androidide.tooling.api.model.IdeGradleProject;
import com.itsaky.androidide.tooling.api.model.JavaContentRoot;
import com.itsaky.androidide.tooling.api.model.JavaModule;
import com.itsaky.androidide.tooling.api.model.JavaModuleCompilerSettings;
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleExternalDependency;
import com.itsaky.androidide.tooling.api.model.JavaModuleProjectDependency;
import com.itsaky.androidide.tooling.api.model.JavaSourceDirectory;
import com.itsaky.androidide.tooling.api.model.util.AndroidModulePropertyCopier;
import com.itsaky.androidide.tooling.api.model.util.ProjectBuilder;
import com.itsaky.androidide.tooling.impl.Main;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.LogUtils;

import org.gradle.api.JavaVersion;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import kotlin.Pair;

/**
 * @author Akash Yadav
 */
public class ProjectReader {

  /** All modules mapped by their names (not paths). */
  private static final Map<String, IdeaModule> allModules = new ConcurrentHashMap<>();

  public static IdeGradleProject read(
      ProjectConnection connection, Map<String, DefaultProjectSyncIssues> outIssues)
      throws ExecutionException, InterruptedException {
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

    final var project = buildActionExecutor.run();

    // Fetch java module dependencies
    final var ideaProject = connection.model(IdeaProject.class).get();
    final var modules = ideaProject.getModules();

    for (var module : modules) {
      allModules.put(module.getName(), module);
    }

    for (final var module : modules) {
      final var moduleProject = project.findByPath(module.getGradleProject().getPath()).get();
      if (!(moduleProject instanceof JavaModule)) {
        continue;
      }

      final var dependencies = ((JavaModule) moduleProject).getJavaDependencies();
      dependencies.clear();
      dependencies.addAll(collectJavaDependencies(module));
    }

    allModules.clear();

    return project;
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
    tryFillNamespaces(android, module);
    addTasks(gradle, module);

    return module;
  }

  private static void tryFillNamespaces(final AndroidProject android, final AndroidModule module) {
    try {
      module.setNamespace(android.getNamespace());
      module.setAndroidTestNamespace(android.getAndroidTestNamespace());
      module.setTestFixturesNamespace(android.getTestFixturesNamespace());
    } catch (Throwable err) {
      log("Unable to get namespaces of module: '" + module.projectPath + "'.", err.getMessage());
    }
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
      final IdeaProject ideaProject,
      IdeaModule ideaModule,
      Map<String, DefaultProjectSyncIssues> outIssues) {
    return buildModuleProject(controller, ideaProject, ideaModule, outIssues, false);
  }

  private static IdeGradleProject buildModuleProject(
      BuildController controller,
      final IdeaProject ideaProject,
      IdeaModule ideaModule,
      Map<String, DefaultProjectSyncIssues> outIssues,
      boolean androidOnly) {

    final var gradle = ideaModule.getGradleProject();

    try {
      final var info = createAndroidModelInfo(gradle, controller);
      if (info == null) {
        throw new UnknownModelException(
            "Project " + gradle.getName() + " is not an AndroidProject");
      }

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

        return buildJavaModuleProject(gradle, ideaProject, ideaModule);
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

    log("Fetching Android project model...");
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

    final var dependencies = getAllCompileDependencies(variantDependencies);
    module.setLibraryMap(dependencies.getFirst());
    module.setLibraries(dependencies.getSecond());

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

  private static Pair<Map<String, DefaultLibrary>, Set<String>> getAllCompileDependencies(
      final VariantDependencies variantDependencies) {
    final var seen = new HashMap<String, DefaultLibrary>();
    final var libs = new HashSet<String>();
    final var compileDependencies = variantDependencies.getMainArtifact().getCompileDependencies();
    final var libraries = variantDependencies.getLibraries();
    for (final var dependency : compileDependencies) {
      var library = seen.get(dependency.getKey());
      if (library == null) {
        library = getLibrary(dependency, libraries, seen);
      }
      if (library == null) {
        continue;
      }

      libs.add(library.getKey());
    }
    return new Pair<>(seen, libs);
  }

  private static DefaultLibrary getLibrary(
      final GraphItem item,
      final Map<String, Library> libraries,
      final HashMap<String, DefaultLibrary> seen) {
    final var lib = libraries.get(item.getKey());
    if (lib == null) {
      return null;
    }

    final var library = AndroidModulePropertyCopier.INSTANCE.copy(lib);
    for (final var dependency : item.getDependencies()) {
      final DefaultLibrary dep = Objects.requireNonNull(getLibrary(dependency, libraries, seen));
      library.getDependencies().add(dep.getKey());
    }

    seen.put(item.getKey(), library);
    return library;
  }

  private static JavaModule buildJavaModuleProject(
      GradleProject gradle, final IdeaProject ideaProject, IdeaModule idea) {
    final var builder = new ProjectBuilder();
    builder.setName(gradle.getName());
    builder.setDescription(gradle.getDescription());
    builder.setPath(gradle.getPath());
    builder.setProjectDir(gradle.getProjectDirectory());
    builder.setBuildDir(gradle.getBuildDirectory());
    builder.setBuildScript(gradle.getBuildScript().getSourceFile());
    builder.setContentRoots(collectContentRoots(idea));
    builder.setJavaCompilerSettings(createCompilerSettings(ideaProject, idea));

    final var project = builder.buildJavaModule();
    addTasks(gradle, project);
    return project;
  }

  private static IJavaCompilerSettings createCompilerSettings(
      final IdeaProject ideaProject, final IdeaModule module) {
    final var javaLanguageSettings = module.getJavaLanguageSettings();
    if (javaLanguageSettings == null) {
      return createCompilerSettings(ideaProject);
    }

    JavaVersion languageLevel = javaLanguageSettings.getLanguageLevel();
    JavaVersion targetBytecodeVersion = javaLanguageSettings.getTargetBytecodeVersion();

    if (languageLevel == null || targetBytecodeVersion == null) {
      return createCompilerSettings(ideaProject);
    }

    final var source = languageLevel.toString();
    final var target = targetBytecodeVersion.toString();
    return new JavaModuleCompilerSettings(source, target);
  }

  private static IJavaCompilerSettings createCompilerSettings(final IdeaProject ideaProject) {
    final var settings = ideaProject.getJavaLanguageSettings();
    if (settings == null) {
      return new JavaModuleCompilerSettings();
    }

    final var source = settings.getLanguageLevel();
    final var target = settings.getTargetBytecodeVersion();
    if (source == null || target == null) {
      return new JavaModuleCompilerSettings();
    }

    return new JavaModuleCompilerSettings(source.toString(), target.toString());
  }

  private static void log(Object... messages) {
    System.err.println(generateMessage(messages));
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

  private static List<? extends JavaModuleDependency> collectJavaDependencies(IdeaModule idea) {
    final var list = new ArrayList<JavaModuleDependency>();
    for (IdeaDependency dependency : idea.getDependencies()) {
      // TODO There might be unresolved dependencies here. We need to handle them too.
      if (dependency instanceof IdeaSingleEntryLibraryDependency) {
        final var external = (IdeaSingleEntryLibraryDependency) dependency;
        final var file = external.getFile();
        final var source = external.getSource();
        final var javadoc = external.getJavadoc();
        final GradleArtifact artifact = getGradleArtifact(external);
        list.add(
            new JavaModuleExternalDependency(
                file,
                source,
                javadoc,
                artifact,
                dependency.getScope().getScope(),
                dependency.getExported()));
      } else if (dependency instanceof IdeaModuleDependency) {
        final var project = ((IdeaModuleDependency) dependency);
        final var moduleName = project.getTargetModuleName();
        list.add(
            new JavaModuleProjectDependency(
                moduleName,
                findModulePathByName(moduleName),
                project.getScope().getScope(),
                project.getExported()));
      }
    }
    return list;
  }

  @Nullable
  private static GradleArtifact getGradleArtifact(final IdeaSingleEntryLibraryDependency external) {
    final var moduleVersion = external.getGradleModuleVersion();
    if (moduleVersion == null) {
      return null;
    }

    return new GradleArtifact(
        moduleVersion.getGroup(), moduleVersion.getName(), moduleVersion.getVersion());
  }

  private static String findModulePathByName(final String moduleName) {
    final var module = allModules.get(moduleName);
    if (module != null) {
      return module.getGradleProject().getPath();
    }
    return "";
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
          "No GradleProject model is associated with project path: " + "':'");
    }

    IdeGradleProject project =
        buildModuleProject(controller, ideaProject, rootModule.get(), outIssues, true);
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

      final var sub = buildModuleProject(controller, ideaProject, module, outIssues);
      if (sub != null) {
        project.moduleProjects.add(sub);
      }
    }
  }
}
