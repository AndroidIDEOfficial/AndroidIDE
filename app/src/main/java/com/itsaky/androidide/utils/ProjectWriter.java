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
	
	private static final String[] FILE_TO_CHANGE = {
		"build.gradle",
        "settings.gradle",
		"app/src/main/AndroidManifest.xml",
		"app/src/main/res/values/strings.xml",
		"app/src/main/java/$package_name/MainActivity.java"
	};
	
	private static final String JAVA_PATH_REGEX = "/.*/src/.*/java";
	private static final String ANAME = "$app_name",
								PNAME = "$package_name",
								MSDK = "$min_sdk",
								TSDK = "$target_sdk",
								ANDROIDIDE_PLUGIN = "$androidide_gradle_plugin_version";
    
	public static String createMenu() {
		return "<menu"
			+ "\n\txmlns:android=\"http://schemas.android.com/apk/res/android\""
			+ "\n\txmlns:app=\"http://schemas.android.com/apk/res-auto\""
			+ "\n\txmlns:tools=\"http://schemas.android.com/tools\">"
			+ "\n\t"
			+ "\n</menu>";
	}

	public static String createDrawable() {
		return "<shape"
			+ "\n\txmlns:android=\"http://schemas.android.com/apk/res/android\""
			+ "\n\tandroid:shape=\"rectangle\">"
			+ "\n\t\t<solid"
			+ "\n\t\t\tandroid:color=\"@color/colorPrimary\"/>"
			+ "\n\t\t<corners"
			+ "\n\t\t\tandroid:topLeftRadius=\"8dp\""
			+ "\n\t\t\tandroid:topRightRadius=\"8dp\"/>"
			+ "\n</shape>";
	}

	public static String createLayout() {
		return "<androidx.coordinatorlayout.widget.CoordinatorLayout"
			+ "\n	xmlns:android=\"http://schemas.android.com/apk/res/android\""
			+ "\n	xmlns:app=\"http://schemas.android.com/apk/res-auto\""
			+ "\n	android:layout_width=\"match_parent\""
			+ "\n	android:layout_height=\"match_parent\""
			+ "\n	android:animateLayoutChanges=\"true\">"
			+ "\n	"
			+ "\n	<com.google.android.material.appbar.AppBarLayout"
			+ "\n		android:layout_width=\"match_parent\""
			+ "\n		android:layout_height=\"wrap_content\""
			+ "\n		android:elevation=\"8dp\""
			+ "\n		android:id=\"@+id/appbar\""
			+ "\n		app:layout_behavior=\"com.google.android.material.appbar.AppBarLayout$Behavior\">"
			+ "\n"
			+ "\n		<com.google.android.material.appbar.MaterialToolbar"
			+ "\n			android:layout_height=\"wrap_content\""
			+ "\n			android:layout_width=\"match_parent\""
			+ "\n			android:id=\"@+id/toolbar\"/>"
			+ "\n"
			+ "\n	</com.google.android.material.appbar.AppBarLayout>"
			+ "\n	"
			+ "\n	<LinearLayout"
			+ "\n		android:layout_height=\"match_parent\""
			+ "\n		android:layout_width=\"match_parent\""
			+ "\n		app:layout_behavior=\"com.google.android.material.appbar.AppBarLayout$ScrollingViewBehavior\">"
			+ "\n		"
			+ "\n	</LinearLayout>"
			+ "\n    "
			+ "\n</androidx.coordinatorlayout.widget.CoordinatorLayout>";
	}
	
	public static String getPackageName(File parentPath) {
		Matcher pkgMatcher = Pattern.compile(JAVA_PATH_REGEX).matcher(parentPath.getAbsolutePath());
		if(pkgMatcher.find()) {
			int end = pkgMatcher.end();
			if(end <= 0) return "";
			String name = parentPath.getAbsolutePath().substring(pkgMatcher.end());
			if(name.startsWith(File.separator))
				name = name.substring(1);
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
	
	public static void write(ProjectTemplate template, NewProjectDetails details, ProjectWriterCallback listener) throws Exception {
		write(template.getId(), details, listener);
	}
	
	public static void write(int id, NewProjectDetails details, ProjectWriterCallback listener) throws Exception {
		callback = listener;
		notifyBegin();
		final StudioApp instance = StudioApp.getInstance();
		final File temp = instance.getTempProjectDir();
		final File projectDir = new File(instance.getProjectsDir(), details.name);
		if(projectDir.exists()) {
			notifyFailed(instance.getString(R.string.project_exists));
			return;
		}
		if(temp == null) notifyFailed(instance.getString(R.string.cannot_create_temp));
		if(!FileUtils.delete(temp) || !Environment.mkdirIfNotExits(temp).exists()) notifyFailed(instance.getString(R.string.cannot_create_temp));
		notifyTask(instance.getString(R.string.copying_assets));
		projectDir.mkdirs();
		File destZip = new File(Environment.TMP_DIR, "templates/" + id + ".zip");
		Environment.mkdirIfNotExits(destZip.getParentFile());
		if(ResourceUtils.copyFileFromAssets("templates/" + destZip.getName(), destZip.getAbsolutePath())) {
			ZipUtils.unzipFile(destZip, temp);
			notifyTask(instance.getString(R.string.writing_files));
			for(String s : FILE_TO_CHANGE) {
				File file = new File(temp, s);
				if(file.exists() && s.contains(PNAME)) {
					s = s.replace(PNAME, details.packageName.replace(".", "/"));
					File f = new File(temp, s);
					FileUtils.move(file, f);
					
					try {
						File f2 = file.getParentFile();
						while(!f2.getName().contains(PNAME))
							f2 = f2.getParentFile();
						FileUtils.delete(f2);
					} catch (Throwable t) {}
					file = f;
				}
				if(file.exists()) {
					String read = FileIOUtils.readFile2String(file);
					read = read.replace(ANAME, details.name)
							.replace(PNAME, details.packageName)
							.replace(MSDK, String.valueOf(details.minSdk))
							.replace(TSDK, String.valueOf(details.targetSdk))
							.replace(ANDROIDIDE_PLUGIN, "1.0.6");
					if(FileIOUtils.writeFileFromString(file, read, false)) {
						continue;
					} else {
						notifyFailed(instance.getString(R.string.failed_write_file, file.getName()));
					}
				}
			}
			notifyTask(instance.getString(R.string.copying_files));
			if(FileUtils.createOrExistsDir(projectDir)) {
				boolean success = true;
				for(File f : temp.listFiles()) {
					if(success &= FileUtils.copy(f, new File(projectDir, f.getName())))
						continue;
					else {
						notifyFailed(instance.getString(R.string.failed_write_file, f.getName()));
						break;
					}
				}
				if(success)
					notifySuccess(projectDir);
			} else {
				notifyFailed(instance.getString(R.string.failed_create_project_dir));
			}
		} else {
			notifyFailed(instance.getString(R.string.asset_copy_failed));
		}
	}
	
	private static void notifyBegin() {
		ThreadUtils.runOnUiThread(() -> {
			if(callback != null)
				callback.beforeBegin();
		});
	}
	
	private static void notifyTask(String name) {
		ThreadUtils.runOnUiThread(() -> {
			if(callback != null)
				callback.onProcessTask(name);
		});
	}
	
	private static void notifySuccess(File root) {
		ThreadUtils.runOnUiThread(() -> {
			if(callback != null)
				callback.onSuccess(root);
		});
	}
	
	private static void notifyFailed(String reason) {
		ThreadUtils.runOnUiThread(() -> {
			if(callback != null)
				callback.onFailed(reason);
		});
	}
}
