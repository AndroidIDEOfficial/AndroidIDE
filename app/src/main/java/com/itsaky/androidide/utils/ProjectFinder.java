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

import android.text.TextUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.itsaky.androidide.project.AndroidProject;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectFinder {

  public static AndroidProject fromFolder(File file) {
    if (!file.isDirectory() || file.isHidden()) return null;
    File settings = new File(file, "settings.gradle");
    if (!settings.exists()) return null;
    String content = FileUtil.readFile(settings.getAbsolutePath());
    if (TextUtils.isEmpty(content)) return null;

    AndroidProject project = new AndroidProject();
    for (File module : file.listFiles()) {
      if (module.isDirectory() && !module.isHidden()) {
        try {
          proceedModule(file, module, project);
        } catch (JSONException e) {
          continue;
        }
      }
    }

    project.setAppName(file.getName());
    project.setProjectPath(file.getAbsolutePath());
    if (project.getMainModule() == null
        && project.getModulePaths().size() > 0
        && project.getModulePaths().get(0) != null) {
      File f = new File(project.getModulePaths().get(0));
      if (!f.exists() || !f.isDirectory()) return null;
      project.setMainModule(f.getName());
      project.setMainSourcePath(new File(f, "src/main/java").getAbsolutePath());
    }

    return project;
  }

  private static void proceedModule(File file, File module, AndroidProject project)
      throws JSONException {
    final Pattern appPattern =
        Pattern.compile("apply\\s+plugin\\s*:\\s*(['\"])com.android.application(['\"])");
    File projectGradle = new File(file, "build.gradle");
    File moduleGradle = new File(module, "build.gradle");
    File manifest = new File(module, "src/main/AndroidManifest.xml");
    File res = new File(module, "src/main/res");
    if (!module.exists() || !projectGradle.exists() || !moduleGradle.exists() || !manifest.exists())
      return;
    if (!module.isDirectory()
        || !moduleGradle.isFile()
        || !projectGradle.isFile()
        || !manifest.isFile()) return;

    File java = new File(module, "src/main/java");
    if (java.exists() && java.isDirectory())
      project.addSource(java.getAbsolutePath()).addModule(module.getAbsolutePath());

    File libsFolder = new File(module, "libs");
    if (libsFolder.exists() && libsFolder.isDirectory()) {
      File[] libs = libsFolder.listFiles();
      if (libs != null && libs.length > 0) {
        for (File lib : libs)
          if (lib.getName().endsWith(".jar")) project.addClasspath(lib.getAbsolutePath());
      }
    }

    project.addSource(
        new File(module, "build/generated/source/buildConfig/debug").getAbsolutePath());
    project.addSource(
        new File(module, "build/generated/data_binding_base_class_source_out/debug/out")
            .getAbsolutePath());

    JSONObject obj =
        new XmlToJson.Builder(FileUtil.readFile(manifest.getAbsolutePath())).build().toJson();
    if (!obj.has("manifest")) return;
    String iconPath = null;
    if (obj.has("manifest")
        && obj.getJSONObject("manifest").has("application")
        && obj.getJSONObject("manifest").getJSONObject("application").has("android:icon")) {
      String iconRes =
          obj.getJSONObject("manifest").getJSONObject("application").getString("android:icon");
      if (iconRes.startsWith("@drawable")) {
        final String iconName = iconRes.replace("@drawable/", "");
        File[] drawables = listDrawables(res);
        for (File drawable : drawables) {
          File[] icons = listFilesNotEndingWith(drawable, ".xml");
          for (File icon : icons) {
            String nameWithoutExtension =
                icon.getName().substring(0, icon.getName().lastIndexOf("."));
            if (nameWithoutExtension.equals(iconName)) {
              iconPath = icon.getAbsolutePath();
              break;
            }
          }
        }
      } else if (iconRes.startsWith("@mipmap")) {
        final String iconName = iconRes.replace("@mipmap/", "");
        File[] drawables = listMipmaps(res);
        for (File drawable : drawables) {
          File[] icons = listFilesNotEndingWith(drawable, ".xml");
          for (File icon : icons) {
            String nameWithoutExtension =
                icon.getName().substring(0, icon.getName().lastIndexOf("."));
            if (nameWithoutExtension.equals(iconName)) {
              iconPath = icon.getAbsolutePath();
              break;
            }
          }
        }
      }
    }
    final String packageName = obj.getJSONObject("manifest").getString("package");
    final String mainModuleName = module.getName();

    boolean isMainModule = false;
    if (project.getMainModule() == null) {
      String content = FileIOUtils.readFile2String(moduleGradle);
      if (content != null && appPattern.matcher(content).find()) {
        isMainModule = true;
        project.setMainModule(mainModuleName);
        project.setMainSourcePath(new File(module, "src/main/java").getAbsolutePath());
      }
    }

    if (iconPath != null) {
      project.setIconPath(iconPath);
    }

    if (packageName != null && isMainModule) {
      project.setPackageName(packageName);
    }
  }

  private static File[] listDrawables(File file) {
    return listFolderStartingWith(file, "drawable");
  }

  private static File[] listMipmaps(File file) {
    return listFolderStartingWith(file, "mipmap");
  }

  private static File[] listFolderStartingWith(File file, final String prefix) {
    return file.listFiles(
        new FileFilter() {

          @Override
          public boolean accept(File p1) {
            return p1.isDirectory() && p1.getName().startsWith(prefix);
          }
        });
  }

  private static File[] listFilesNotEndingWith(File file, final String suffix) {
    return file.listFiles(
        new FileFilter() {

          @Override
          public boolean accept(File p1) {
            return p1.isFile() && !p1.getName().endsWith(suffix);
          }
        });
  }
}
