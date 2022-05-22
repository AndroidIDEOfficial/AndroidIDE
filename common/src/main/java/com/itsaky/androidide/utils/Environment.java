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
package com.itsaky.androidide.utils;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.app.BaseApplication;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressLint("SdCardPath")
public final class Environment {

  public static final Map<String, String> IDE_PROPS = new HashMap<>();
  public static final Map<String, String> ENV_VARS = new HashMap<>();
  public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
  public static final String DEFAULT_ROOT = "/data/data/com.itsaky.androidide/files";
  public static final String DEFAULT_HOME = DEFAULT_ROOT + "/framework";
  private static final String DEFAULT_JAVA_HOME = DEFAULT_HOME + "/jdk";
  private static final String DEFAULT_ANDROID_HOME = DEFAULT_HOME + "/android-sdk";
  private static final ILogger LOG = ILogger.newInstance("Environment");
  private static final List<String> blacklist = new ArrayList<>();
  public static File ROOT;
  public static File SYSROOT;
  public static File HOME;
  public static File ANDROIDIDE_HOME;
  public static File JAVA_HOME;
  public static File ANDROID_HOME;
  public static File TMP_DIR;
  public static File BIN_DIR;
  public static File LIB_DIR;
  public static File PROJECTS_DIR;
  public static File IDE_PROPS_FILE;
  public static File LIB_HOOK;
  public static File LIB_HOOK2;
  /**
   * JDK modules used by the java language server for completions. This version of JDK modules
   * contains only the classes included in android.jar
   */
  public static File COMPILER_MODULE;

  public static File TOOLING_API_JAR;

  public static File INIT_SCRIPT;
  public static File PROJECT_DATA_FILE;
  public static File GRADLE_PROPS;
  public static File GRADLE_USER_HOME;
  public static File AAPT2;
  public static File JAVA;
  public static File BUSYBOX;
  public static File SHELL;
  public static File LOGIN_SHELL;
  public static File BOOTCLASSPATH;

  public static void init() {
    final BaseApplication app = BaseApplication.getBaseInstance();
    ROOT = app.getIDEDataDir();
    SYSROOT = mkdirIfNotExits(new File(app.getIDEDataDir(), "sysroot"));
    HOME = mkdirIfNotExits(app.getRootDir());
    ANDROIDIDE_HOME = mkdirIfNotExits(new File(HOME, ".androidide"));
    TMP_DIR = mkdirIfNotExits(new File(SYSROOT, "tmp"));
    BIN_DIR = mkdirIfNotExits(new File(SYSROOT, "bin"));
    LIB_DIR = mkdirIfNotExits(new File(SYSROOT, "lib"));
    PROJECTS_DIR = mkdirIfNotExits(new File(FileUtil.getExternalStorageDir(), PROJECTS_FOLDER));
    COMPILER_MODULE = mkdirIfNotExits(new File(ANDROIDIDE_HOME, "compiler-module"));
    TOOLING_API_JAR =
        new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "tooling-api")), "tooling-api-all.jar");
    AAPT2 = new File(ANDROIDIDE_HOME, "aapt2");

    IDE_PROPS_FILE = new File(SYSROOT, "etc/ide-environment.properties");
    LIB_HOOK = new File(LIB_DIR, "libhook.so");
    LIB_HOOK2 = new File(LIB_DIR, "libhook2.so");
    PROJECT_DATA_FILE = new File(TMP_DIR, "ide_project");

    INIT_SCRIPT = new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "init")), "init.gradle");
    BOOTCLASSPATH = new File("");
    GRADLE_USER_HOME = new File(HOME, ".gradle");
    GRADLE_PROPS = new File(GRADLE_USER_HOME, "gradle.properties");

    IDE_PROPS.putAll(readProperties());
    ANDROID_HOME = new File(readProp("ANDROID_HOME", DEFAULT_ANDROID_HOME));
    JAVA_HOME = new File(readProp("JAVA_HOME", DEFAULT_JAVA_HOME));

    JAVA = new File(JAVA_HOME, "bin/java");
    BUSYBOX = new File(BIN_DIR, "busybox");
    SHELL = new File(BIN_DIR, "bash");
    LOGIN_SHELL = new File(BIN_DIR, "login");

    setExecutable(JAVA);
    setExecutable(BUSYBOX);
    setExecutable(SHELL);

    System.setProperty("user.home", HOME.getAbsolutePath());
    System.setProperty("java.home", JAVA_HOME.getAbsolutePath());
  }

  public static File mkdirIfNotExits(File in) {
    if (in != null && !in.exists()) {
      FileUtils.createOrExistsDir(in);
    }

    return in;
  }

  @NonNull
  private static Map<String, String> readProperties() {
    final Map<String, String> props = new HashMap<>();
    if (IDE_PROPS_FILE == null || !IDE_PROPS_FILE.exists()) {
      return props;
    }
    try {
      Properties p = new Properties();
      p.load(new StringReader(FileIOUtils.readFile2String(IDE_PROPS_FILE)));
      for (@SuppressWarnings("rawtypes") Map.Entry entry : p.entrySet()) {
        props.put(entry.getKey() + "", entry.getValue() + "");
      }
    } catch (Throwable th) {
      LOG.error("Unable to read properties file", th);
    }
    return props;
  }

  public static String readProp(String key, String defaultValue) {
    String value = IDE_PROPS.getOrDefault(key, defaultValue);
    if (value == null) {
      return defaultValue;
    }
    if (value.contains("$HOME")) {
      value = value.replace("$HOME", HOME.getAbsolutePath());
    }
    if (value.contains("$SYSROOT")) {
      value = value.replace("$SYSROOT", SYSROOT.getAbsolutePath());
    }
    if (value.contains("$PATH")) {
      value = value.replace("$PATH", createPath());
    }
    return value;
  }

  public static void setExecutable(@NonNull final File file) {
    if (!file.setExecutable(true)) {
      LOG.error("Unable to set executable permissions to file", file);
    }
  }

  @NonNull
  private static String createPath() {
    String path = "";
    path += String.format("%s/bin", JAVA_HOME.getAbsolutePath());
    path += String.format(":%s/cmdline-tools/latest/bin", ANDROID_HOME.getAbsolutePath());
    path += String.format(":%s/cmake/bin", ANDROID_HOME.getAbsolutePath());
    path += String.format(":%s/bin", SYSROOT.getAbsolutePath());
    path += String.format(":%s", System.getenv("PATH"));
    return path;
  }

  public static void setBootClasspath(@NonNull File file) {
    BOOTCLASSPATH = new File(file.getAbsolutePath());
  }

  public static Map<String, String> getEnvironment() {

    if (!ENV_VARS.isEmpty()) {
      return ENV_VARS;
    }

    ENV_VARS.put("HOME", HOME.getAbsolutePath());
    ENV_VARS.put("ANDROID_HOME", ANDROID_HOME.getAbsolutePath());
    ENV_VARS.put("ANDROID_SDK_ROOT", ANDROID_HOME.getAbsolutePath());
    ENV_VARS.put("ANDROID_USER_HOME", HOME.getAbsolutePath() + "/.android");
    ENV_VARS.put("JAVA_HOME", JAVA_HOME.getAbsolutePath());
    ENV_VARS.put("GRADLE_USER_HOME", GRADLE_USER_HOME.getAbsolutePath());
    ENV_VARS.put("TMPDIR", TMP_DIR.getAbsolutePath());
    ENV_VARS.put("PROJECTS", PROJECTS_DIR.getAbsolutePath());
    ENV_VARS.put("LANG", "en_US.UTF-8");
    ENV_VARS.put("LC_ALL", "en_US.UTF-8");

    ENV_VARS.put("SYSROOT", SYSROOT.getAbsolutePath());

    ENV_VARS.put("BUSYBOX", BUSYBOX.getAbsolutePath());
    ENV_VARS.put("SHELL", SHELL.getAbsolutePath());
    ENV_VARS.put("CONFIG_SHELL", SHELL.getAbsolutePath());
    ENV_VARS.put("TERM", "screen");

    // If LD_LIBRARY_PATH is set, append $SYSROOT/lib to it,
    // else set it to $SYSROOT/lib
    String ld = System.getenv("LD_LIBRARY_PATH");
    if (ld == null || ld.trim().length() <= 0) {
      ld = "";
    } else {
      ld += File.pathSeparator;
    }
    ld += LIB_DIR.getAbsolutePath();
    ENV_VARS.put("LD_LIBRARY_PATH", ld);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
        && BaseApplication.isAarch64()
        && LIB_HOOK.exists()) {
      // Required for JDK 11
      ENV_VARS.put("LD_PRELOAD", LIB_HOOK.getAbsolutePath());
    }

    addToEnvIfPresent(ENV_VARS, "ANDROID_ART_ROOT");
    addToEnvIfPresent(ENV_VARS, "DEX2OATBOOTCLASSPATH");
    addToEnvIfPresent(ENV_VARS, "ANDROID_I18N_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_RUNTIME_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_TZDATA_ROOT");
    addToEnvIfPresent(ENV_VARS, "ANDROID_DATA");
    addToEnvIfPresent(ENV_VARS, "ANDROID_ROOT");

    String path = createPath();

    ENV_VARS.put("PATH", path);

    for (String key : IDE_PROPS.keySet()) {
      if (!blacklistedVariables().contains(key.trim())) {
        ENV_VARS.put(key, readProp(key, ""));
      }
    }

    return ENV_VARS;
  }

  public static void addToEnvIfPresent(Map<String, String> environment, String name) {
    String value = System.getenv(name);
    if (value != null) {
      environment.put(name, value);
    }
  }

  private static List<String> blacklistedVariables() {
    if (blacklist.isEmpty()) {
      blacklist.add("HOME");
      blacklist.add("SYSROOT");
      blacklist.add("JLS_HOME");
    }
    return blacklist;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public static boolean isCompilerModuleInstalled() {
    final var modules = new File(COMPILER_MODULE, "lib/modules");
    final var release = new File(COMPILER_MODULE, "release");
    return modules.exists() && modules.isFile() && release.exists() && release.isFile();
  }
}
