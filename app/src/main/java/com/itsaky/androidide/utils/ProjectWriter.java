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

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.models.ProjectTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import kotlin.text.StringsKt;

public class ProjectWriter {

  private static final String XML_TEMPLATE_PATH = "templates/xml";
  private static final String SOURCE_PATH_REGEX = "/.*/src/.*/java|kt";
  private static final String FILE_EXT_REGEX = ".*/java|kt|gradle|xml|txt|cpp";
  private static final String APP_NAME = "$app_name",
      PACKAGE_NAME = "$package_name",
      MIN_SDK = "$min_sdk",
      TARGET_SDK = "$target_sdk",
      PACKAGE_NAME_CPP = "{$package_name_cpp}",
      CPP_FLAGS = "$cpp_flags",
      LIBRARY_NAME = "$library_name";
  private static ProjectWriterCallback callback;

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

  @NonNull
  public static String createLayoutName(String name) {
    final var nameWithoutExtension = StringsKt.substringBeforeLast(name, '.', name);
    var baseName = nameWithoutExtension;
    if (baseName.endsWith("Activity")) {
      baseName = StringsKt.substringBeforeLast(baseName, "Activity", baseName);
      baseName = "activity" + baseName;
    } else if (baseName.endsWith("Fragment")) {
      baseName = StringsKt.substringBeforeLast(baseName, "Fragment", baseName);
      baseName = "fragment" + baseName;
    } else {
      baseName = "layout" + baseName;
    }

    final var sb = new StringBuilder();
    var hasUpper = false;
    for (int i = 0; i < baseName.length(); i++) {
      final char c = baseName.charAt(i);
      if (isUpperCase(c)) {
        hasUpper = true;
        sb.append("_");
        sb.append(toLowerCase(c));
        continue;
      }

      sb.append(c);
    }

    if (!hasUpper) {
      sb.delete(0, sb.length());
      sb.append("layout_");
      sb.append(nameWithoutExtension);
    }

    sb.append(".xml");

    return sb.toString();
  }

  public static String getPackageName(File parentPath) {
    Matcher pkgMatcher = Pattern.compile(SOURCE_PATH_REGEX).matcher(parentPath.getAbsolutePath());
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
    final File tempDir = instance.getTempProjectDir();
    final File projectDir = new File(details.savePath);
    if (projectDir.exists()) {
      notifyFailed(instance.getString(R.string.project_exists));
      return;
    }
    if (tempDir == null) {
      notifyFailed(instance.getString(R.string.cannot_create_temp));
    }
    if (!FileUtils.delete(tempDir) || !Environment.mkdirIfNotExits(tempDir).exists()) {
      notifyFailed(instance.getString(R.string.cannot_create_temp));
    }
    notifyTask(instance.getString(R.string.copying_assets));
    projectDir.mkdirs();
    File destZip = new File(Environment.TMP_DIR, "templates/" + id + ".zip");
    Environment.mkdirIfNotExits(destZip.getParentFile());
    if (ResourceUtils.copyFileFromAssets(
        "templates/" + destZip.getName(), destZip.getAbsolutePath())) {
      unzipTemplate(destZip, tempDir, details);
      notifyTask(instance.getString(R.string.writing_files));
      notifyTask(instance.getString(R.string.copying_files));
      if (FileUtils.createOrExistsDir(projectDir)) {
        boolean success = true;
        final var files = tempDir == null ? null : tempDir.listFiles();
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

  private static void unzipTemplate(File zipFile, File location, NewProjectDetails details)
      throws IOException {
    final StudioApp instance = StudioApp.getInstance();
    int size;
    final int BUFFER_SIZE = 2048;
    byte[] buffer = new byte[BUFFER_SIZE];

    File f = location;
    if (!f.isDirectory()) {
      f.mkdirs();
    }
    try (ZipInputStream zin =
        new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE))) {
      ZipEntry ze;
      while ((ze = zin.getNextEntry()) != null) {
        final var prefix = details.language.toLowerCase(Locale.ROOT);
        if (ze.getName().startsWith(prefix) && !ze.isDirectory()) {
          String name = ze.getName().replaceFirst(prefix, "");
          if (name.contains(PACKAGE_NAME)) {
            name = name.replace(PACKAGE_NAME, details.packageName.replace(".", "/"));
          }
          String path = location.getAbsolutePath() + "/" + name;
          File unzipFile = new File(path);

          if (ze.isDirectory()) {
            if (!unzipFile.isDirectory()) {
              unzipFile.mkdirs();
            }
          } else {
            // check for and create parent directories if they don't exist
            File parentDir = unzipFile.getParentFile();
            if (null != parentDir) {
              if (!parentDir.isDirectory()) {
                parentDir.mkdirs();
              }
            }

            // unzip the file
            FileOutputStream out = new FileOutputStream(unzipFile, false);
            BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
            try {
              while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1) {
                fout.write(buffer, 0, size);
              }
              zin.closeEntry();
            } finally {
              fout.flush();
              fout.close();
            }

            // replace temp variables to real
            Matcher extMatcher = Pattern.compile(FILE_EXT_REGEX).matcher(name);
            if (extMatcher.find()) {
              String fileContent = FileIOUtils.readFile2String(unzipFile);

              fileContent = customReplaceAll(fileContent, APP_NAME, details.name);
              fileContent =
                  customReplaceAll(
                      fileContent,
                      PACKAGE_NAME_CPP,
                      String.valueOf(details.packageName.replace("_", "_1").replace('.', '_')));
              fileContent = customReplaceAll(fileContent, PACKAGE_NAME, details.packageName);
              fileContent = customReplaceAll(fileContent, MIN_SDK, String.valueOf(details.minSdk));
              fileContent =
                  customReplaceAll(fileContent, TARGET_SDK, String.valueOf(details.targetSdk));
              fileContent = customReplaceAll(fileContent, CPP_FLAGS, details.cppFlags);
              fileContent =
                  customReplaceAll(
                      fileContent,
                      LIBRARY_NAME,
                      details.packageName.substring(details.packageName.lastIndexOf('.') + 1));
              if (!FileIOUtils.writeFileFromString(unzipFile, fileContent, false)) {
                notifyFailed(instance.getString(R.string.failed_write_file, unzipFile.getName()));
              }
            }
          }
        }
      }
    }
  }

  public static String customReplaceAll(String str, String oldStr, String newStr) {

    if ("".equals(str) || "".equals(oldStr) || oldStr.equals(newStr)) {
      return str;
    }
    if (newStr == null) {
      newStr = "";
    }
    final int strLength = str.length();
    final int oldStrLength = oldStr.length();
    StringBuilder builder = new StringBuilder(str);

    for (int i = 0; i < strLength; i++) {
      int index = builder.indexOf(oldStr, i);

      if (index == -1) {
        if (i == 0) {
          return str;
        }
        return builder.toString();
      }
      builder = builder.replace(index, index + oldStrLength, newStr);
    }
    return builder.toString();
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
