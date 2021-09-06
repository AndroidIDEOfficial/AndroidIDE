package com.itsaky.androidide.utils;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.app.StudioApp;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    public static final File IDEPROPS;
    
    public static final File INIT_SCRIPT;
    public static final File PROJECT_DATA_FILE;
    
	public static final File GRADLE_HOME;
	public static final File GRADLE_PROPS;
	public static final File GRADLE_USER_HOME;
    
    public static final File GRADLE;
    public static final File JAVA;
    public static final File BUSYBOX;
    public static final File BASH;
    public static final File BASHRC;
    
    public static File BOOTCLASSPATH;
    
	public static final String SAMPLE_GRADLE_PROP_CONTENTS = "# Specify global Gradle properties in this file\n# These properties will be applicable for every project you build with Gradle.";
	public static List<File> AARS = new ArrayList<>();
    
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
        
        IDEPROPS = new File(SYSROOT, "etc/ide-environment.properties");
        PROJECT_DATA_FILE = new File(TMP_DIR, "ide_project");

        INIT_SCRIPT = new File(mkdirIfNotExits(new File(app.getIDEDataDir(), "init")), "androidide.init.gradle");
        BOOTCLASSPATH = new File("");
        GRADLE_USER_HOME = new File(HOME, ".gradle");
        GRADLE_PROPS = new File(GRADLE_USER_HOME, "gradle.properties");

        final Map<String, String> props = readProperties();
        ANDROID_HOME = new File(props.getOrDefault("ANDROID_HOME", DEFAULT_ANDROID_HOME));
        JAVA_HOME = new File(props.getOrDefault("JAVA_HOME", DEFAULT_JAVA_HOME));
        GRADLE_HOME = new File(props.getOrDefault("GRADLE_HOME", DEFAULT_GRADLE_HOME));

        BASH = new File(BINDIR, "bash");
        GRADLE = new File(GRADLE_HOME, "bin/gradle");
        JAVA = new File(JAVA_HOME, "bin/java");
        BUSYBOX = new File(BINDIR, "busybox");
        BASHRC = new File(HOME, ".bashrc");
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
            LOG.e("Error reading properties", th);
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
        map.put("TMPDIR", TMP_DIR.getAbsolutePath());
        map.put("ANDROID_DATA", "/data");
        map.put("ANDROID_ROOT", "/system");
        map.put("LANG", "en_US.UTF-8");
        
        if(!publicUse) {
            // These environment variables must not be provided to users
            map.put("JLS_HOME", JLS_HOME.getAbsolutePath());
        }
        
        map.put("SYSROOT", SYSROOT.getAbsolutePath());
        map.put("PREFIX", SYSROOT.getAbsolutePath());
        
        map.put("BUSYBOX", BUSYBOX.getAbsolutePath());
        map.put("SHELL", BASH.getAbsolutePath());
        map.put("TERM", "xterm-256color");
        map.put("COLORTERM", "truecolor");
        
        map.put("ANDROID_HOME", ANDROID_HOME.getAbsolutePath());
        map.put("GRADLE_INSTALLATION_DIR", GRADLE_HOME.getAbsolutePath());
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
