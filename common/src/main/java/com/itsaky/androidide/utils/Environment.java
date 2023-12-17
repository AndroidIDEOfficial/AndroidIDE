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
import androidx.annotation.NonNull;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
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
  public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
  public static final String DEFAULT_ROOT = "/data/data/com.itsaky.androidide/files";
  public static final String DEFAULT_HOME = DEFAULT_ROOT + "/home";
  private static final String DEFAULT_ANDROID_HOME = DEFAULT_HOME + "/android-sdk";
  public static final String DEFAULT_PREFIX = DEFAULT_ROOT + "/usr";
  private static final String DEFAULT_JAVA_HOME = DEFAULT_PREFIX + "/opt/openjdk";
  private static final String ANDROIDIDE_PROJECT_CACHE_DIR = ".androidide";
  private static final ILogger LOG = ILogger.newInstance("Environment");
  private static final List<String> blacklist = new ArrayList<>();
  public static File ROOT;
  public static File PREFIX;
  public static File HOME;
  public static File ANDROIDIDE_HOME;
  public static File ANDROIDIDE_UI;
  public static File JAVA_HOME;
  public static File ANDROID_HOME;
  public static File TMP_DIR;
  public static File BIN_DIR;
  public static File LIB_DIR;
  public static File PROJECTS_DIR;
  public static File IDE_PROPS_FILE;
  public static File LIB_HOOK;

  /**
   * Used by Java LSP until the project is initialized.
   */
  public static File ANDROID_JAR;

  public static File TOOLING_API_JAR;

  public static File INIT_SCRIPT;
  public static File PROJECT_DATA_FILE;
  public static File GRADLE_USER_HOME;
  public static File AAPT2;
  public static File JAVA;
  public static File SHELL;
  public static File LOGIN_SHELL;

  public static void init() {
    ROOT = mkdirIfNotExits(new File(DEFAULT_ROOT));
    PREFIX = mkdirIfNotExits(new File(ROOT, "usr"));
    HOME = mkdirIfNotExits(new File(ROOT, "home"));
    ANDROIDIDE_HOME = mkdirIfNotExits(new File(HOME, ".androidide"));
    TMP_DIR = mkdirIfNotExits(new File(PREFIX, "tmp"));
    BIN_DIR = mkdirIfNotExits(new File(PREFIX, "bin"));
    LIB_DIR = mkdirIfNotExits(new File(PREFIX, "lib"));
    PROJECTS_DIR = mkdirIfNotExits(new File(FileUtil.getExternalStorageDir(), PROJECTS_FOLDER));
    ANDROID_JAR = mkdirIfNotExits(new File(ANDROIDIDE_HOME, "android.jar"));
    TOOLING_API_JAR =
        new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "tooling-api")), "tooling-api-all.jar");
    AAPT2 = new File(ANDROIDIDE_HOME, "aapt2");
    ANDROIDIDE_UI = mkdirIfNotExits(new File(ANDROIDIDE_HOME, "ui"));

    IDE_PROPS_FILE = new File(PREFIX, "etc/ide-environment.properties");
    LIB_HOOK = new File(LIB_DIR, "libhook.so");
    PROJECT_DATA_FILE = new File(TMP_DIR, "ide_project");

    INIT_SCRIPT = new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "init")), "init.gradle");
    GRADLE_USER_HOME = new File(HOME, ".gradle");

    IDE_PROPS.putAll(readProperties());
    ANDROID_HOME = new File(readProp("ANDROID_HOME", DEFAULT_ANDROID_HOME));
    JAVA_HOME = new File(readProp("JAVA_HOME", DEFAULT_JAVA_HOME));

    // If JDK 17 is not installed, check for any JDK in home directory
    if (!JAVA_HOME.exists()) {
      final var java_home = new File(HOME, "jdk");
      if (java_home.exists()) {
        JAVA_HOME = java_home;
      }
    }

    JAVA = new File(JAVA_HOME, "bin/java");
    SHELL = new File(BIN_DIR, "bash");
    LOGIN_SHELL = new File(BIN_DIR, "login");

    setExecutable(JAVA);
    setExecutable(SHELL);

    System.setProperty("user.home", HOME.getAbsolutePath());
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
        props.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
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
      value = value.replace("$SYSROOT", PREFIX.getAbsolutePath());
    }
    if (value.contains("$PATH")) {
      value = value.replace("$PATH", createPath());
    }
    return value;
  }

  @NonNull
  public static String createPath() {
    String path = BIN_DIR.getAbsolutePath();
    path += String.format(":%s/cmdline-tools/latest/bin", ANDROID_HOME.getAbsolutePath());
    return path;
  }

  public static void setExecutable(@NonNull final File file) {
    if (!file.setExecutable(true)) {
      LOG.error("Unable to set executable permissions to file", file);
    }
  }

  public static void setProjectDir(@NonNull File file) {
    PROJECTS_DIR = new File(file.getAbsolutePath());
  }

  public static void putEnvironment(Map<String, String> env, boolean forFailsafe) {

    env.put("HOME", HOME.getAbsolutePath());
    env.put("ANDROID_HOME", ANDROID_HOME.getAbsolutePath());
    env.put("ANDROID_SDK_ROOT", ANDROID_HOME.getAbsolutePath());
    env.put("ANDROID_USER_HOME", HOME.getAbsolutePath() + "/.android");
    env.put("JAVA_HOME", JAVA_HOME.getAbsolutePath());
    env.put("GRADLE_USER_HOME", GRADLE_USER_HOME.getAbsolutePath());
    env.put("SYSROOT", PREFIX.getAbsolutePath());
    env.put("PROJECTS", PROJECTS_DIR.getAbsolutePath());

    // add user envs for non-failsafe sessions
    if (!forFailsafe) {
      // No mirror select
      env.put("TERMUX_PKG_NO_MIRROR_SELECT", "true");

      for (String key : IDE_PROPS.keySet()) {
        if (!blacklistedVariables().contains(key.trim())) {
          env.put(key, readProp(key, ""));
        }
      }
    }


  }

  private static List<String> blacklistedVariables() {
    if (blacklist.isEmpty()) {
      blacklist.add("HOME");
      blacklist.add("SYSROOT");
    }
    return blacklist;
  }

  public static File getProjectCacheDir(File projectDir) {
    return new File(projectDir, ANDROIDIDE_PROJECT_CACHE_DIR);
  }
}
