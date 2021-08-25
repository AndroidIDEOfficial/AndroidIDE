package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.gradle.Artifact;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public final class Environment {
	
	public static File HOME;
	public static File JAVA_HOME;
	public static File ANDROID_HOME;
    public static File JLS_HOME;
	public static File TMP_DIR;
    public static File USRDIR;
	public static File LIBDIR;
    public static File BINDIR;
    
	public static File GRADLE_DIR;
	public static File GRADLE_PROPS;
	public static File GRADLE_USER_HOME;
	public static File GRADLE_PLUGIN;
    public static File BOOTCLASSPATH;
	public static File CACHES_DIR;
	public static File AAPT2_DIR;
    
    public static File INIT_SCRIPT;
    public static File LIBHOOKSO;
    
    public static File GRADLE_API_DIR;
    public static File GRADLE_API_LIBS_DIR;
	
	public static String ANDROID_SDK_FOLDER_NAME = "android-sdk";
	public static String GRADLE_FOLDER_NAME = null;
    public static final String JDK_FOLDER_NAME = "jdk";
	public static final String JDK_FOLDER_NAME_OLD = "openjdk-11.0.1";
	public static final String DEFAULT_GRADLE_FOLDER = "gradle-7.1.1";
	public static final String SAMPLE_GRADLE_PROP_CONTENTS = "# Specify global Gradle properties in this file\n# These properties will be applicable for every project you build with Gradle.";
	
	public static List<File> AARS = new ArrayList<>();

    private static File getJavaHome() {
        File home = new File(HOME, JDK_FOLDER_NAME_OLD);
        if(home.exists() && home.isDirectory()) {
            FileUtils.rename(home, JDK_FOLDER_NAME);
        }
        return new File(HOME, JDK_FOLDER_NAME);
    }
	
	static {
		final StudioApp app = StudioApp.getInstance();
		final File files21 = new File(HOME, ".gradle/caches/modules-2/files-2.1");
		GRADLE_FOLDER_NAME = app.getPrefManager().getCurrentGradleFolderName();
		final File gradleDir = new File(HOME, "gradle-7.1.1");
		if(gradleDir != null && gradleDir.exists()) {
			GRADLE_FOLDER_NAME =  !FileUtils.rename(gradleDir, "gradle") ? GRADLE_FOLDER_NAME : "gradle";
			app.getPrefManager().setCurentGradleFolderName(GRADLE_FOLDER_NAME);
		}
        
		HOME = app.getRootDir();
		JAVA_HOME = getJavaHome();
		ANDROID_HOME = new File(HOME, ANDROID_SDK_FOLDER_NAME);
        JLS_HOME = mkdirIfNotExits(new File(HOME.getParentFile(), "jls"));
		TMP_DIR = mkdirIfNotExits(new File(HOME, "tmp"));
		GRADLE_DIR = new File(HOME, GRADLE_FOLDER_NAME);
        USRDIR = mkdirIfNotExits(new File(HOME.getParentFile(), "usr"));
        LIBDIR = mkdirIfNotExits(new File(USRDIR, "lib"));
        BINDIR = mkdirIfNotExits(new File(USRDIR, "bin"));
        
        GRADLE_API_DIR = mkdirIfNotExits(new File(HOME.getParentFile(), "gradle-api"));
        GRADLE_API_LIBS_DIR = new File(GRADLE_API_DIR, "libs");
        
        INIT_SCRIPT = new File(mkdirIfNotExits(new File(HOME.getParentFile(), "init")), "androidide.init.gradle");
        LIBHOOKSO = new File(LIBDIR, "libhook.so");
		CACHES_DIR = new File(HOME, "ide-caches");
        BOOTCLASSPATH = new File("");
		AAPT2_DIR = new File(files21, "com.android.tools.build/aapt2");
		GRADLE_PLUGIN = new File(files21, "com.itsaky.androidide/gradle");
		GRADLE_USER_HOME = new File(HOME, ".gradle");
		GRADLE_PROPS = new File(GRADLE_USER_HOME, "gradle.properties");
	}
	
	public static File getGradleDir() {
		String name = DEFAULT_GRADLE_FOLDER;
		if(GRADLE_FOLDER_NAME != null)
			name = GRADLE_FOLDER_NAME;
		return new File(HOME, name);
	}

	public static void setBootClasspath(File file) {
        BOOTCLASSPATH = new File(file.getAbsolutePath());
    }

	public static String path(File file) {
		return file.getAbsolutePath();
	}

	public static File destinationForArtifact(Artifact artifact) {
		return mkdirIfNotExits(new File(CACHES_DIR, String.format("%s/%s/%s", artifact.group, artifact.name, artifact.version)));
	}

	public static File mkdirIfNotExits(File in) {
		if (!in.exists())
			FileUtils.createOrExistsDir(in);
			
		return in;
	}

	public static File cachedFolderForArtifact(Artifact artifact) {
		return new File(HOME, String.format(".gradle/caches/modules-2/files-2.1/%s/%s/%s", artifact.group, artifact.name, artifact.version));
	}
	
	public static List<File> findJarForArtifact(final Artifact artifact) {
		List<File> result = new ArrayList<>();
		if(artifact != null && artifact.isValid()) {
			File folder = cachedFolderForArtifact(artifact);
			for(File f : folder.listFiles(p1 -> p1.isDirectory())) {
				File[] jarsOrAars = f.listFiles(p1 -> p1.isFile() && (p1.getName().endsWith(".jar") || p1.getName().endsWith(".aar")));
				if(jarsOrAars != null
				&& jarsOrAars.length > 0) {
					for(File jarOrAar : jarsOrAars) {
						if(jarOrAar.getName().endsWith(".jar")) {
							result.add(jarOrAar);
						} else if(jarOrAar.getName().endsWith(".aar")) {
							result.addAll(extractAar(artifact, jarOrAar));
						}
					}
				}
			}
		}
		return result;
	}

	private static List<File> extractAar(final Artifact artifact, File aar) {
		try {
			final File extractedFolder = destinationForArtifact(artifact);
			ZipUtils.unzipFile(aar, mkdirIfNotExits(extractedFolder));
			AARS.add(extractedFolder);
			return FileUtils.listFilesInDirWithFilter(extractedFolder, (p1 -> p1.isFile() && p1.getName().endsWith(".jar")), true);
		} catch (Throwable e) {
			return null;
		}
	}
	
	public static class Executables {
		public static File GRADLE;
		public static File JAVA;
		
		static {
			GRADLE = new File(GRADLE_DIR, "bin/gradle");
			JAVA = new File(JAVA_HOME, "bin/java");
		}
	}
}
