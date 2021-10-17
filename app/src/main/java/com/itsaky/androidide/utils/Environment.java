package com.itsaky.androidide.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.app.StudioApp;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressLint("SdCardPath")
public final class Environment {
	
    public static final File ROOT;
    public static final File SYSROOT;
	public static final File HOME;
	public static final File JAVA_HOME;
	public static final File ANDROID_HOME;
    public static final File JLS_HOME;
	public static final File TMP_DIR;
    public static final File BINDIR;
    public static final File LIBDIR;
    public static final File PROJECTS_DIR;
    public static final File IDEPROPS;
    public static final File LIBHOOK;
    
    public static final File INIT_SCRIPT;
    public static final File PROJECT_DATA_FILE;
    public static final File JLS_JAR;
    
	public static final File GRADLE_HOME;
	public static final File GRADLE_PROPS;
	public static final File GRADLE_USER_HOME;
    
    public static final File GRADLE;
    public static final File JAVA;
    public static final File BUSYBOX;
    public static final File SHELL;
    
    public static File BOOTCLASSPATH;
    
	public static final String SAMPLE_GRADLE_PROP_CONTENTS = "# Specify global Gradle properties in this file\n# These properties will be applicable for every project you build with Gradle.";
	public static List<File> AARS = new ArrayList<>();
    
    public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
    
    private static final String DEFAULT_JAVA_HOME = "/data/data/com.itsaky.androidide/files/framework/jdk";
    private static final String DEFAULT_ANDROID_HOME = "/data/data/com.itsaky.androidide/files/framework/android-sdk";
    private static final String DEFAULT_GRADLE_HOME = "/data/data/com.itsaky.androidide/files/framework/gradle-7.1.1";
    
    private static final Logger LOG = Logger.instance("Environment");
    
	static {
		final StudioApp app = StudioApp.getInstance();
        ROOT = app.getIDEDataDir();
        SYSROOT = mkdirIfNotExits(new File(app.getIDEDataDir(), "sysroot"));
        HOME = mkdirIfNotExits(app.getRootDir());
        JLS_HOME = mkdirIfNotExits(new File(app.getIDEDataDir(), "jls"));
        TMP_DIR = mkdirIfNotExits(new File(SYSROOT, "tmp"));
        BINDIR = mkdirIfNotExits(new File(SYSROOT, "bin"));
        LIBDIR = mkdirIfNotExits(new File(SYSROOT, "lib"));
        PROJECTS_DIR = mkdirIfNotExits(new File(FileUtil.getExternalStorageDir(), PROJECTS_FOLDER));
        
        IDEPROPS = new File(SYSROOT, "etc/ide-environment.properties");
        LIBHOOK = new File(LIBDIR, "libhook.so");
        PROJECT_DATA_FILE = new File(TMP_DIR, "ide_project");
        JLS_JAR = new File(JLS_HOME, "jls.jar");
        
        INIT_SCRIPT = new File(mkdirIfNotExits(new File(app.getIDEDataDir(), "init")), "androidide.init.gradle");
        BOOTCLASSPATH = new File("");
        GRADLE_USER_HOME = new File(HOME, ".gradle");
        GRADLE_PROPS = new File(GRADLE_USER_HOME, "gradle.properties");

        final Map<String, String> props = readProperties();
        ANDROID_HOME = new File(props.getOrDefault("ANDROID_HOME", DEFAULT_ANDROID_HOME));
        JAVA_HOME = new File(props.getOrDefault("JAVA_HOME", DEFAULT_JAVA_HOME));
        GRADLE_HOME = new File(props.getOrDefault("GRADLE_HOME", DEFAULT_GRADLE_HOME));
        
        GRADLE = new File(GRADLE_HOME, "bin/gradle");
        JAVA = new File(JAVA_HOME, "bin/java");
        BUSYBOX = new File(BINDIR, "busybox");
        SHELL = new File(BINDIR, "sh.sh");
        
        if(!GRADLE.canExecute()) {
            GRADLE.setExecutable(true);
        }
        
        if(!JAVA.canExecute()) {
            JAVA.setExecutable(true);
        }
        
        if(!BUSYBOX.canExecute()) {
            BUSYBOX.setExecutable(true);
        }
        
        if(!SHELL.canExecute()) {
            SHELL.setExecutable(true);
        }
        
        System.setProperty("java.home", JAVA_HOME.getAbsolutePath());
	}
    
    private static Map<String, String> readProperties() {
        final Map<String, String> props = new HashMap<>();
        if(IDEPROPS == null || !IDEPROPS.exists()) return props;
        try {
            Properties p = new Properties();
            p.load(new StringReader(FileIOUtils.readFile2String(IDEPROPS)));
            for(Map.Entry entry : p.entrySet()) {
                props.put(entry.getKey() + "", entry.getValue() + "");
            }
            return props;
        } catch (Throwable th) {
            LOG.error("Error reading properties", th);
            return props;
        }
    }
	
	public static File getGradleDir() {
		return GRADLE_HOME;
	}

	public static void setBootClasspath(File file) {
        BOOTCLASSPATH = new File(file.getAbsolutePath());
    }
    
    public static Map<String, String> getEnvironment(boolean publicUse) {
        final Map<String, String> map = new HashMap<>();
        map.put("HOME", HOME.getAbsolutePath());
        map.put("GRADLE_USER_HOME", GRADLE_USER_HOME.getAbsolutePath());
        map.put("GRADLE_HOME", GRADLE_HOME.getAbsolutePath());
        map.put("TMPDIR", TMP_DIR.getAbsolutePath());
        map.put("PROJECT_DIR", PROJECTS_DIR.getAbsolutePath());
        map.put("LANG", "en_US.UTF-8");
        map.put("LC_ALL", "en_US.UTF-8");
        
        if(!publicUse) {
            // These environment variables must not be accessed to users
            map.put("JLS_HOME", JLS_HOME.getAbsolutePath());
        }
        
        map.put("SYSROOT", SYSROOT.getAbsolutePath());
        
        map.put("BUSYBOX", BUSYBOX.getAbsolutePath());
        map.put("SHELL", SHELL.getAbsolutePath());
        map.put("CONFIG_SHELL", SHELL.getAbsolutePath());
        map.put("TERM", "screen");
        
        map.put("ANDROID_HOME", ANDROID_HOME.getAbsolutePath());
        map.put("GRADLE_HOME", GRADLE_HOME.getAbsolutePath());
        map.put("JAVA_HOME", JAVA_HOME.getAbsolutePath());
        
        /**
         * If LD_LIBRARY_PATH is set, prepend $SYSROOT/lib to it,
         * else set it to $SYSROOT/lib
         */
        String ld = System.getenv("LD_LIBRARY_PATH");
        if(ld == null || ld.trim().length() <= 0)
            ld = "";
        else ld = LIBDIR.getAbsolutePath() + File.pathSeparator + ld;
        map.put("LD_LIBRARY_PATH", ld);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            map.put("LD_PRELOAD", LIBHOOK.getAbsolutePath());
        }
        
        addToEnvIfPresent(map, "ANDROID_ART_ROOT");
        addToEnvIfPresent(map, "DEX2OATBOOTCLASSPATH");
        addToEnvIfPresent(map, "ANDROID_I18N_ROOT");
        addToEnvIfPresent(map, "ANDROID_RUNTIME_ROOT");
        addToEnvIfPresent(map, "ANDROID_TZDATA_ROOT");
        
        String path = "";
        path += String.format("%s/bin", JAVA_HOME.getAbsolutePath());
        path += String.format(":%s/bin", GRADLE_HOME.getAbsolutePath());
        path += String.format(":%s/cmdline-tools/latest/bin", ANDROID_HOME.getAbsolutePath());
        path += String.format(":%s/cmake/bin", ANDROID_HOME.getAbsolutePath());
        path += String.format(":%s/bin", SYSROOT.getAbsolutePath());
        path += String.format(":%s", System.getenv("PATH"));
        
        map.put("PATH", path);
        
        return map;
    }
    
    public static void addToEnvIfPresent(Map<String, String> environment, String name) {
        String value = System.getenv(name);
        if (value != null) {
            environment.put(name, value);
        }
    }
    
	public static String path(File file) {
		return file.getAbsolutePath();
	}

	public static File mkdirIfNotExits(File in) {
		if (!in.exists())
			FileUtils.createOrExistsDir(in);
			
		return in;
	}
}
