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
import android.content.Context;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@SuppressLint("SdCardPath")
public final class Environment {

  public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
  private static final Logger LOG = LoggerFactory.getLogger(Environment.class);
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
  public static File REALM_DB_DIR;

  /**
   * Used by Java LSP until the project is initialized.
   */
  public static File ANDROID_JAR;

  public static File TOOLING_API_JAR;

  public static File INIT_SCRIPT;
  public static File GRADLE_USER_HOME;
  public static File AAPT2;
  public static File JAVA;
  public static File BASH_SHELL;
  public static File LOGIN_SHELL;

  public static void init(Context context) {
    ROOT = context.getFilesDir();
    PREFIX = mkdirIfNotExits(new File(ROOT, "usr"));
    HOME = mkdirIfNotExits(new File(ROOT, "home"));
    ANDROIDIDE_HOME = mkdirIfNotExits(new File(HOME, ".androidide"));
    TMP_DIR = mkdirIfNotExits(new File(PREFIX, "tmp"));
    BIN_DIR = mkdirIfNotExits(new File(PREFIX, "bin"));
    LIB_DIR = mkdirIfNotExits(new File(PREFIX, "lib"));
    PROJECTS_DIR = mkdirIfNotExits(new File(FileUtil.getExternalStorageDir(), PROJECTS_FOLDER));
    ANDROID_JAR = mkdirIfNotExits(new File(ANDROIDIDE_HOME, "android.jar"));
    TOOLING_API_JAR = new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "tooling-api")),
      "tooling-api-all.jar");
    AAPT2 = new File(ANDROIDIDE_HOME, "aapt2");
    ANDROIDIDE_UI = mkdirIfNotExits(new File(ANDROIDIDE_HOME, "ui"));
    REALM_DB_DIR = mkdirIfNotExits(new File(ROOT, "realm-dbs"));

    INIT_SCRIPT = new File(mkdirIfNotExits(new File(ANDROIDIDE_HOME, "init")), "init.gradle");
    GRADLE_USER_HOME = new File(HOME, ".gradle");

    ANDROID_HOME = new File(HOME, "android-sdk");
    JAVA_HOME = new File(PREFIX, "opt/openjdk");

    JAVA = new File(JAVA_HOME, "bin/java");
    BASH_SHELL = new File(BIN_DIR, "bash");
    LOGIN_SHELL = new File(BIN_DIR, "login");

    setExecutable(JAVA);
    setExecutable(BASH_SHELL);

    System.setProperty("user.home", HOME.getAbsolutePath());
  }

  public static File mkdirIfNotExits(File in) {
    if (in != null && !in.exists()) {
      FileUtils.createOrExistsDir(in);
    }

    return in;
  }

  public static void setExecutable(@NonNull final File file) {
    if (!file.setExecutable(true)) {
      LOG.error("Unable to set executable permissions to file: {}", file);
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
    }
  }

  public static File getProjectCacheDir(File projectDir) {
    return new File(projectDir, ".androidide");
  }

  @NonNull
  public static File createTempFile() {
    var file = newTempFile();
    while (file.exists()) {
      file = newTempFile();
    }

    return file;
  }

  @NonNull
  private static File newTempFile() {
    return new File(TMP_DIR, "temp_" + UUID.randomUUID().toString().replace('-', 'X'));
  }
}
