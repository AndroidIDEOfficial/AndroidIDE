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

package com.itsaky.androidide.plugins.tasks.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import org.gradle.api.GradleException;
import org.gradle.api.NonNullApi;
import org.gradle.api.UncheckedIOException;
import org.gradle.api.internal.plugins.StartScriptGenerator;
import org.gradle.api.tasks.wrapper.Wrapper;
import org.gradle.api.tasks.wrapper.Wrapper.PathBase;
import org.gradle.internal.util.PropertiesUtils;
import org.gradle.util.GradleVersion;
import org.gradle.util.internal.DistributionLocator;
import org.gradle.util.internal.GFileUtils;
import org.gradle.wrapper.GradleWrapperMain;
import org.gradle.wrapper.WrapperExecutor;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Properties;

import static java.util.Collections.singletonList;

@NonNullApi
public class WrapperGenerator {

  public static File getPropertiesFile(File jarFileDestination) {
    return new File(jarFileDestination.getParentFile(), jarFileDestination.getName().replaceAll("\\.jar$", ".properties"));
  }

  public static File getBatchScript(File scriptFile) {
    return new File(scriptFile.getParentFile(), scriptFile.getName().replaceFirst("(\\.[^\\.]+)?$", ".bat"));
  }

  public static String getDistributionUrl(GradleVersion gradleVersion, Wrapper.DistributionType distributionType) {
    String distType = distributionType.name().toLowerCase(Locale.ENGLISH);
    return new DistributionLocator().getDistributionFor(gradleVersion, distType).toASCIIString();
  }

  public static void generate(
      PathBase archiveBase, String archivePath,
      PathBase distributionBase, String distributionPath,
      @Nullable String distributionSha256Sum,
      File wrapperPropertiesOutputFile,
      File wrapperJarOutputFile, String jarFileRelativePath,
      File unixScript, File batchScript,
      @Nullable String distributionUrl,
      boolean validateDistributionUrl,
      @Nullable Integer networkTimeout
  ) {
    writeProperties(wrapperPropertiesOutputFile, distributionUrl, distributionSha256Sum, distributionBase, distributionPath, archiveBase, archivePath, networkTimeout, validateDistributionUrl);
    writeWrapperJar(wrapperJarOutputFile);
    writeScripts(jarFileRelativePath, unixScript, batchScript);
  }

  private static void writeProperties(
      File propertiesFileDestination,
      @Nullable String distributionUrl,
      @Nullable String distributionSha256Sum,
      PathBase distributionBase,
      String distributionPath,
      PathBase archiveBase,
      String archivePath,
      @Nullable Integer networkTimeout,
      boolean validateDistributionUrl
  ) {
    Properties wrapperProperties = new Properties();
    wrapperProperties.put(WrapperExecutor.DISTRIBUTION_URL_PROPERTY, distributionUrl);
    if (distributionSha256Sum != null) {
      wrapperProperties.put(WrapperExecutor.DISTRIBUTION_SHA_256_SUM, distributionSha256Sum);
    }
    wrapperProperties.put(WrapperExecutor.DISTRIBUTION_BASE_PROPERTY, distributionBase.toString());
    wrapperProperties.put(WrapperExecutor.DISTRIBUTION_PATH_PROPERTY, distributionPath);
    wrapperProperties.put(WrapperExecutor.ZIP_STORE_BASE_PROPERTY, archiveBase.toString());
    wrapperProperties.put(WrapperExecutor.ZIP_STORE_PATH_PROPERTY, archivePath);
    if (networkTimeout != null) {
      wrapperProperties.put(WrapperExecutor.NETWORK_TIMEOUT_PROPERTY, String.valueOf(networkTimeout));
    }
    wrapperProperties.put(WrapperExecutor.VALIDATE_DISTRIBUTION_URL, String.valueOf(validateDistributionUrl));

    GFileUtils.parentMkdirs(propertiesFileDestination);
    try {
      PropertiesUtils.store(wrapperProperties, propertiesFileDestination);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static void writeWrapperJar(File destination) {
    URL jarFileSource = Wrapper.class.getResource("/gradle-wrapper.jar");
    if (jarFileSource == null) {
      throw new GradleException("Cannot locate wrapper JAR resource.");
    }
    try (InputStream in = jarFileSource.openStream(); OutputStream out = Files.newOutputStream(destination.toPath())) {
      ByteStreams.copy(in, out);
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to write wrapper JAR to " + destination, e);
    }
  }

  private static void writeScripts(String jarFileRelativePath, File unixScript, File batchScript) {
    StartScriptGenerator generator = new StartScriptGenerator();
    generator.setApplicationName("Gradle");
    generator.setMainClassName(GradleWrapperMain.class.getName());
    generator.setClasspath(singletonList(jarFileRelativePath));
    generator.setOptsEnvironmentVar("GRADLE_OPTS");
    generator.setExitEnvironmentVar("GRADLE_EXIT_CONSOLE");
    generator.setAppNameSystemProperty("org.gradle.appname");
    generator.setScriptRelPath(unixScript.getName());
    generator.setDefaultJvmOpts(ImmutableList.of("-Xmx64m", "-Xms64m"));
    generator.generateUnixScript(unixScript);
    generator.generateWindowsScript(batchScript);
  }

}