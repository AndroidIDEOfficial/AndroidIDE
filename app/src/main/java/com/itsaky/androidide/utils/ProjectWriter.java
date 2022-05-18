/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
 **************************************************************************************/

package com.itsaky.androidide.utils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.models.ProjectTemplate;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectWriter {

  private static ProjectWriterCallback callback;

  private static final String XML_TEMPLATE_PATH = "templates/xml";

  private static final String[] FILE_TO_CHANGE = {
    "build.gradle",
    "settings.gradle",
    "app/src/main/AndroidManifest.xml",
    "app/src/main/res/values/strings.xml",
    "app/src/main/java/$package_name/MainActivity.java",
    "app/src/main/java/$package_name/MainActivity.kt",
    "app/build.gradle",
    "app/src/main/java/$package_name/AndroidLauncher.java",
    "app/res/values/strings.xml",
    "core/build.gradle",
    "core/src/main/java/$package_name/GameSuperClass.java",
    "app/AndroidManifest.xml"
  };

  private static final String JAVA_PATH_REGEX = "/.*/src/.*/java|kt";
  private static final String APP_NAME = "$app_name",
      PACKAGE_NAME = "$package_name",
      MIN_SDK = "$min_sdk",
      TARGET_SDK = "$target_sdk";

  @NonNull
  public static String createMenu() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/menu.xml");
  }

  @NonNull
  public static String createDrawable() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/drawable.xml");
  }

  @NonNull
  public static String createLayout() {
    return ResourceUtils.readAssets2String(XML_TEMPLATE_PATH + "/layout.xml");
  }

  public static String getPackageName(File parentPath) {
    Matcher pkgMatcher = Pattern.compile(JAVA_PATH_REGEX).matcher(parentPath.getAbsolutePath());
    if (pkgMatcher.find()) {
      int end = pkgMatcher.end();
      if (end <= 0) return "";
      String name = parentPath.getAbsolutePath().substring(pkgMatcher.end());
      if (name.startsWith(File.separator)) name = name.substring(1);
      return name.replace(File.separator, ".");
    }
    return null;
  }

  public static String createJavaClass(String packageName, String className) {
    return ClassBuilder.createClass(packageName, className);
  }

  public static String createJavaInterface(String packageName, String className) {
    return ClassBuilder.createInterface(packageName, className);
  }

  public static String createJavaEnum(String packageName, String className) {
    return ClassBuilder.createEnum(packageName, className);
  }

  public static String createActivity(String packageName, String className) {
    return ClassBuilder.createActivity(packageName, className);
  }

  public static void write(
      ProjectTemplate template, NewProjectDetails details, ProjectWriterCallback listener)
      throws Exception {
    write(template.getId(), details, listener);
  }

  public static void write(int id, NewProjectDetails details, ProjectWriterCallback listener)
      throws Exception {
    callback = listener;
    notifyBegin();
    final StudioApp instance = StudioApp.getInstance();
    final File temp = instance.getTempProjectDir();
    final File projectDir = new File(instance.getProjectsDir(), details.name);
    if (projectDir.exists()) {
      notifyFailed(instance.getString(R.string.project_exists));
      return;
    }
    if (temp == null) notifyFailed(instance.getString(R.string.cannot_create_temp));
    if (!FileUtils.delete(temp) || !Environment.mkdirIfNotExits(temp).exists())
      notifyFailed(instance.getString(R.string.cannot_create_temp));
    notifyTask(instance.getString(R.string.copying_assets));
    projectDir.mkdirs();
    File destZip = new File(Environment.TMP_DIR, "templates/" + id + ".zip");
    Environment.mkdirIfNotExits(destZip.getParentFile());
    if (ResourceUtils.copyFileFromAssets(
        "templates/" + destZip.getName(), destZip.getAbsolutePath())) {
      ZipUtils.unzipFile(destZip, temp);
      notifyTask(instance.getString(R.string.writing_files));
      for (String s : FILE_TO_CHANGE) {
        File file = new File(temp, s);
        if (file.exists() && s.contains(PACKAGE_NAME)) {
          s = s.replace(PACKAGE_NAME, details.packageName.replace(".", "/"));
          File f = new File(temp, s);
          FileUtils.move(file, f);

          try {
            File f2 = file.getParentFile();
            while (f2 != null && !f2.getName().contains(PACKAGE_NAME)) {
              f2 = f2.getParentFile();
            }

            FileUtils.delete(f2);
          } catch (Throwable t) {
            // ignored
          }
          file = f;
        }
        if (file.exists()) {
          String read = FileIOUtils.readFile2String(file);
          read =
              read.replace(APP_NAME, details.name)
                  .replace(PACKAGE_NAME, details.packageName)
                  .replace(MIN_SDK, String.valueOf(details.minSdk))
                  .replace(TARGET_SDK, String.valueOf(details.targetSdk));
          if (!FileIOUtils.writeFileFromString(file, read, false)) {
            notifyFailed(instance.getString(R.string.failed_write_file, file.getName()));
          }
        }
      }
      notifyTask(instance.getString(R.string.copying_files));
      if (FileUtils.createOrExistsDir(projectDir)) {
        boolean success = true;
        final var files = temp == null ? null : temp.listFiles();
        if (files == null) {
          success = false;
        } else {
          for (File f : files) {
            if (!(success &= FileUtils.copy(f, new File(projectDir, f.getName())))) {
              notifyFailed(instance.getString(R.string.failed_write_file, f.getName()));
              break;
            }
          }
        }

        if (success) {
          notifySuccess(projectDir);
        }
      } else {
        notifyFailed(instance.getString(R.string.failed_create_project_dir));
      }
    } else {
      notifyFailed(instance.getString(R.string.asset_copy_failed));
    }
  }

  private static void notifyBegin() {
    ThreadUtils.runOnUiThread(
        () -> {
          if (callback != null) callback.beforeBegin();
        });
  }

  private static void notifyTask(String name) {
    ThreadUtils.runOnUiThread(
        () -> {
          if (callback != null) callback.onProcessTask(name);
        });
  }

  private static void notifySuccess(File root) {
    ThreadUtils.runOnUiThread(
        () -> {
          if (callback != null) callback.onSuccess(root);
        });
  }

  private static void notifyFailed(String reason) {
    ThreadUtils.runOnUiThread(
        () -> {
          if (callback != null) callback.onFailed(reason);
        });
  }
}
