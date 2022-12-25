/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.managers;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class ToolsManager {

  public static final int LOG_SENDER_VERSION = 2;
  public static final String KEY_LOG_SENDER_VERSION = "tools_logsenderVersion";
  private static final ILogger LOG = ILogger.newInstance("ToolsManager");
  public static String ARCH_SPECIFIC_ASSET_DATA_DIR = "data/" + BaseApplication.getArch();
  public static String COMMON_ASSET_DATA_DIR = "data/common";
  private static PreferenceManager prefs;

  public static void init(@NonNull BaseApplication app, Runnable onFinish) {

    if (BaseApplication.getArch() == null) {
      LOG.error("Device not supported");
      return;
    }

    prefs = app.getPrefManager();

    CompletableFuture.runAsync(
            () -> {
              writeNoMediaFile();
              copyBusyboxIfNeeded();
              extractLogsenderIfNeeded();
              extractAapt2();
              extractGradlePlugin();
              extractToolingApi();
              extractAndroidJar();
              extractColorScheme(app);
              writeInitScript();

              deleteIdeenv();
            })
        .whenComplete(
            (__, error) -> {
              if (error != null) {
                LOG.error("Error extracting tools", error);
              }

              if (onFinish != null) {
                onFinish.run();
              }
            });
  }

  private static void extractColorScheme(final BaseApplication app) {
    final var defPath = "editor/schemes";
    final var dir = new File(Environment.ANDROIDIDE_UI, defPath);
    try {
      for (final String asset : app.getAssets().list(defPath)) {

        final var prop = new File(dir, asset + "/" + "scheme.prop");
        if (prop.exists()
            && !shouldExtractScheme(app, new File(dir, asset), defPath + "/" + asset)) {
          continue;
        }

        final File schemeDir = new File(dir, asset);
        if (schemeDir.exists()) {
          schemeDir.delete();
        }

        ResourceUtils.copyFileFromAssets(defPath + "/" + asset, schemeDir.getAbsolutePath());
      }
    } catch (IOException e) {
      LOG.error("Failed to extract color schemes", e);
    }
  }

  private static boolean shouldExtractScheme(
      final BaseApplication app, final File dir, final String path) throws IOException {

    final var schemePropFile = new File(dir, "scheme.prop");
    if (!schemePropFile.exists()) {
      return true;
    }

    final var files = app.getAssets().list(path);
    if (Arrays.stream(files).noneMatch("scheme.prop"::equals)) {
      // no scheme.prop file
      return true;
    }

    try {
      final var props = new Properties();
      props.load(new InputStreamReader(app.getAssets().open(path + "/scheme.prop")));

      final var version = Integer.parseInt(props.getProperty("scheme.version", "0"));
      if (version == 0) {
        return true;
      }

      props.clear();

      props.load(new FileReader(schemePropFile));
      final var fileVersion = Integer.parseInt(props.getProperty("scheme.version", "0"));
      if (fileVersion < 0) {
        return true;
      }

      return version > fileVersion;
    } catch (Throwable err) {
      LOG.error("Failed to read color scheme version for scheme '" + path + "'", err);
      return false;
    }
  }

  private static void writeNoMediaFile() {
    final var noMedia = new File(BaseApplication.getBaseInstance().getProjectsDir(), ".nomedia");
    if (!noMedia.exists()) {
      try {
        if (!noMedia.createNewFile()) {
          LOG.error("Failed to create .nomedia file in projects directory");
        }
      } catch (IOException e) {
        LOG.error("Failed to create .nomedia file in projects directory");
      }
    }
  }

  private static void extractAndroidJar() {
    if (!Environment.ANDROID_JAR.exists()) {
      ResourceUtils.copyFileFromAssets(
          getCommonAsset("android.jar"), Environment.ANDROID_JAR.getAbsolutePath());
    }
  }

  private static void deleteIdeenv() {
    final var file = new File(Environment.BIN_DIR, "ideenv");
    if (file.exists() && !file.delete()) {
      LOG.warn("Unable to delete", file);
    }
  }

  private static void copyBusyboxIfNeeded() {
    File exec = Environment.BUSYBOX;
    if (exec.exists()) return;
    Environment.mkdirIfNotExits(exec.getParentFile());
    ResourceUtils.copyFileFromAssets(getArchSpecificAsset("busybox"), exec.getAbsolutePath());
    if (!exec.canExecute()) {
      if (!exec.setExecutable(true)) {
        LOG.error("Cannot set busybox executable permissions.");
      }
    }
  }

  @NonNull
  @Contract(pure = true)
  public static String getArchSpecificAsset(String name) {
    return ARCH_SPECIFIC_ASSET_DATA_DIR + "/" + name;
  }

  private static void extractLogsenderIfNeeded() {
    try {
      final boolean isOld = LOG_SENDER_VERSION > prefs.getInt(KEY_LOG_SENDER_VERSION, 0);
      if (isOld) {
        final File logsenderZip = new File(Environment.TMP_DIR, "logsender.zip");
        ResourceUtils.copyFileFromAssets(
            getCommonAsset("logsender.zip"), logsenderZip.getAbsolutePath());
        ZipUtils.unzipFile(logsenderZip, Environment.HOME);
        prefs.putInt(KEY_LOG_SENDER_VERSION, LOG_SENDER_VERSION);
      }
    } catch (IOException e) {
      LOG.error("Error extracting log sender", e);
    }
  }

  @NonNull
  @Contract(pure = true)
  public static String getCommonAsset(String name) {
    return COMMON_ASSET_DATA_DIR + "/" + name;
  }

  private static void extractAapt2() {
    if (!Environment.AAPT2.exists()) {
      ResourceUtils.copyFileFromAssets(
          getArchSpecificAsset("aapt2"), Environment.AAPT2.getAbsolutePath());
    }

    if (!Environment.AAPT2.canExecute() && !Environment.AAPT2.setExecutable(true)) {
      LOG.error("Cannot set executable permissions to AAPT2 binary");
    }
  }

  private static void extractGradlePlugin() {
    final var repoDir = new File(Environment.ANDROIDIDE_HOME, "repo");
    FileUtils.createOrExistsDir(repoDir);

    final var zip = new File(Environment.TMP_DIR, "gradle-plugin.zip");
    if (zip.exists()) {
      FileUtils.delete(zip);
    }

    ResourceUtils.copyFileFromAssets(getCommonAsset("gradle-plugin.zip"), zip.getAbsolutePath());
    try {
      ZipUtils.unzipFile(zip, repoDir);
    } catch (Throwable e) {
      LOG.error("Unable to extract gradle plugin zip file");
    }
  }

  private static void extractToolingApi() {
    if (Environment.TOOLING_API_JAR.exists()) {
      FileUtils.delete(Environment.TOOLING_API_JAR);
    }

    ResourceUtils.copyFileFromAssets(
        getCommonAsset("tooling-api-all.jar"), Environment.TOOLING_API_JAR.getAbsolutePath());
  }

  private static void writeInitScript() {
    if (Environment.INIT_SCRIPT.exists()) {
      FileUtils.delete(Environment.INIT_SCRIPT);
    }
    FileIOUtils.writeFileFromString(Environment.INIT_SCRIPT, readInitScript());
  }

  @NonNull
  private static String readInitScript() {
    return ResourceUtils.readAssets2String(getCommonAsset("androidide.init.gradle"));
  }

  public static void extractLibHooks() {
    if (!Environment.LIB_HOOK.exists()) {
      ResourceUtils.copyFileFromAssets(
          getArchSpecificAsset("libhook.so"), Environment.LIB_HOOK.getAbsolutePath());
    }
  }
}
