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
    public static final File LIBHOOK2;
    
    public static final File INIT_SCRIPT;
    public static final File PROJECT_DATA_FILE;
    public static final File JLS_JAR;
    
	public static final File GRADLE_PROPS;
	public static final File GRADLE_USER_HOME;
    
    public static final File JAVA;
    public static final File BUSYBOX;
    public static final File SHELL;
    public static final File BUSYBOX_SH;
    
    public static File BOOTCLASSPATH;
    
	public static final String SAMPLE_GRADLE_PROP_CONTENTS = "# Specify global Gradle properties in this file\n# These properties will be applicable for every project you build with Gradle.";
    public static final Map<String, String> IDE_PROPS = new HashMap<>();
    public static final Map<String, String> ENV_VARS = new HashMap<>();
    
    public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
    
    private static final String DEFAULT_JAVA_HOME = "/data/data/com.itsaky.androidide/files/framework/jdk";
    private static final String DEFAULT_ANDROID_HOME = "/data/data/com.itsaky.androidide/files/framework/android-sdk";
    
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
        LIBHOOK2 = new File(LIBDIR, "libhook2.so");
        PROJECT_DATA_FILE = new File(TMP_DIR, "ide_project");
        JLS_JAR = new File(JLS_HOME, "jls.jar");
        
        INIT_SCRIPT = new File(mkdirIfNotExits(new File(app.getIDEDataDir(), "init")), "androidide.init.gradle");
        BOOTCLASSPATH = new File("");
        GRADLE_USER_HOME = new File(HOME, ".gradle");
        GRADLE_PROPS = new File(GRADLE_USER_HOME, "gradle.properties");

        IDE_PROPS.putAll(readProperties());
        ANDROID_HOME = new File(readProp("ANDROID_HOME", DEFAULT_ANDROID_HOME));
        JAVA_HOME = new File(readProp("JAVA_HOME", DEFAULT_JAVA_HOME));
        
        JAVA = new File(JAVA_HOME, "bin/java");
        BUSYBOX = new File(BINDIR, "busybox");
        SHELL = new File(BINDIR, "sh.sh");
        BUSYBOX_SH = new File (BINDIR, "sh");
        
        if(!JAVA.canExecute()) {
            JAVA.setExecutable(true);
        }
        
        if(!BUSYBOX.canExecute()) {
            BUSYBOX.setExecutable(true);
        }
        
        if(!SHELL.canExecute()) {
            SHELL.setExecutable(true);
        }
        
        System.setProperty("user.home", HOME.getAbsolutePath());
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
        } catch (Throwable th) {
            // ignored
        }
        return props;
    }
    
	public static void setBootClasspath(File file) {
        BOOTCLASSPATH = new File(file.getAbsolutePath());
    }
    
    public static Map<String, String> getEnvironment(boolean publicUse) {
        
        if(!ENV_VARS.isEmpty()) {
            return ENV_VARS;
        }
        
        ENV_VARS.put("HOME", HOME.getAbsolutePath());
        ENV_VARS.put("ANDROID_HOME", ANDROID_HOME.getAbsolutePath());
        ENV_VARS.put("JAVA_HOME", JAVA_HOME.getAbsolutePath());
        ENV_VARS.put("GRADLE_USER_HOME", GRADLE_USER_HOME.getAbsolutePath());
        ENV_VARS.put("TMPDIR", TMP_DIR.getAbsolutePath());
        ENV_VARS.put("PROJECTS", PROJECTS_DIR.getAbsolutePath());
        ENV_VARS.put("LANG", "en_US.UTF-8");
        ENV_VARS.put("LC_ALL", "en_US.UTF-8");
        
        if(!publicUse) {
            // These environment variables must not be accessed to users
            ENV_VARS.put("JLS_HOME", JLS_HOME.getAbsolutePath());
        }
        
        ENV_VARS.put("SYSROOT", SYSROOT.getAbsolutePath());
        
        ENV_VARS.put("BUSYBOX", BUSYBOX.getAbsolutePath());
        ENV_VARS.put("SHELL", SHELL.getAbsolutePath());
        ENV_VARS.put("CONFIG_SHELL", SHELL.getAbsolutePath());
        ENV_VARS.put("TERM", "screen");
        
        /**
         * If LD_LIBRARY_PATH is set, append $SYSROOT/lib to it,
         * else set it to $SYSROOT/lib
         */
        String ld = System.getenv("LD_LIBRARY_PATH");
        if(ld == null || ld.trim().length() <= 0)
            ld = "";
        else ld += File.pathSeparator;
        ld += LIBDIR.getAbsolutePath();
        ENV_VARS.put("LD_LIBRARY_PATH", ld);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && StudioApp.isAarch64()) {
            ENV_VARS.put("LD_PRELOAD", LIBHOOK.getAbsolutePath());
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
            if (blacklistedVariables().contains(key.trim())) {
                continue;
            } else {
                ENV_VARS.put(key, readProp(key, ""));
            }
        }
        
        return ENV_VARS;
    }

    private static String createPath() {
        String path = "";
        path += String.format("%s/bin", JAVA_HOME.getAbsolutePath());
        path += String.format(":%s/cmdline-tools/latest/bin", ANDROID_HOME.getAbsolutePath());
        path += String.format(":%s/cmake/bin", ANDROID_HOME.getAbsolutePath());
        path += String.format(":%s/bin", SYSROOT.getAbsolutePath());
        path += String.format(":%s", System.getenv("PATH"));
        return path;
    }
    
    public static void addToEnvIfPresent(Map<String, String> environment, String name) {
        String value = System.getenv(name);
        if (value != null) {
            environment.put(name, value);
        }
    }
    
    public static String readProp (String key) {
        return readProp(key, null);
    }
    
    public static String readProp(String key, String defaultValue) {
        String value = IDE_PROPS.getOrDefault(key, defaultValue);
        if(value == null ) {
            return defaultValue;
        }
        if(value.contains("$HOME")) {
            value = value.replace("$HOME", HOME.getAbsolutePath());
        }
        if(value.contains("$SYSROOT")) {
            value = value.replace("$SYSROOT", SYSROOT.getAbsolutePath());
        }
        if(value.contains("$PATH")) {
            value = value.replace("$PATH", createPath());
        }
        return value;
    }
    
	public static String path(File file) {
		return file.getAbsolutePath();
	}

	public static File mkdirIfNotExits(File in) {
		if (!in.exists())
			FileUtils.createOrExistsDir(in);
			
		return in;
	}
    
    private static final List<String> blacklist = new ArrayList<>();
    private static List<String> blacklistedVariables() {
        if(blacklist.isEmpty()) {
            blacklist.add("HOME");
            blacklist.add("SYSROOT");
            blacklist.add("JLS_HOME");
        }
        return blacklist;
    }
}
