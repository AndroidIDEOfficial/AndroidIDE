package com.itsaky.androidide.managers;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.lingala.zip4j.ZipFile;

public class ToolsManager {
    
    private static PreferenceManager prefs;
    
    public static final int JLS_VERSION = 1;
    public static final int LOGSENDER_VERSION = 1;
    public static final int CLEANER_VERSION = 1;
    public static final int GRADLE_API_VERSION = 2;
    
    public static final String KEY_JLS_VERSION = "tools_jlsVersion";
    public static final String KEY_LOGSENDER_VERSION = "tools_logsenderVersion";
    public static final String KEY_CLEANER_VERSION = "tools_cleanerVersion";
    public static final String KEY_GRADLE_API_VERSION = "tools_gradleApiVersion";
    
    
    public static void init(StudioApp app, Runnable onFinish) {
        prefs = app.getPrefManager();
        extractJlsIfNeeded();
        extractLogsenderIfNeeded();
        extractCleanerIfNeeded();
        extractGradleApiIfNeeded();
        writeInitScript(app);
        
        if(onFinish != null)
            onFinish.run();
    }

    private static void writeInitScript(StudioApp app) {
        FileIOUtils.writeFileFromString(Environment.INIT_SCRIPT, INIT_SCRIPT);
    }
    
    
    private static void extractJlsIfNeeded() {
        boolean gson = false, jls = false, proto = false;
        final List<File> files = FileUtils.listFilesInDir(Environment.JLS_HOME, true);
        for(File f : files) {
            gson = gson == true ? gson : f.getName().equals("gson.jar");
            jls = jls == true ? jls : f.getName().equals("jls.jar");
            proto = proto == true ? proto : f.getName().equals("protobuf.jar");
        }

        final boolean exists = gson && jls && proto;
        final boolean isOld = JLS_VERSION > prefs.getInt(KEY_JLS_VERSION, 0);
        if(!exists || isOld) {
            try {
                extractJls();
            } catch (Throwable e) {}
        }
    }

    private static void extractJls(){
        final File jlsZip = new File(Environment.JLS_HOME, "jls.zip");
        ResourceUtils.copyFileFromAssets("data/jls.zip", jlsZip.getAbsolutePath());
        try {
            ZipFile file = new ZipFile(jlsZip, ConstantsBridge.JLS_ZIP_PASSWORD_HASH.toCharArray());
            file.extractAll(Environment.JLS_HOME.getAbsolutePath());
            FileUtils.delete(jlsZip);
            prefs.putInt(KEY_JLS_VERSION, JLS_VERSION);
        } catch (Throwable th) {}
    }
    
    
    
    private static void extractLogsenderIfNeeded() {
        try {
            final boolean isOld = LOGSENDER_VERSION > prefs.getInt(KEY_LOGSENDER_VERSION, 0);
            if(isOld) {
                final File logsenderZip = new File(Environment.JLS_HOME, "logsender.zip");
                ResourceUtils.copyFileFromAssets("data/logsender.zip", logsenderZip.getAbsolutePath());
                ZipUtils.unzipFile(logsenderZip, Environment.HOME);
                prefs.putInt(KEY_LOGSENDER_VERSION, LOGSENDER_VERSION);
            }
        } catch (IOException e) {}
    }
    
    
    
    private static void extractCleanerIfNeeded() {
        File cleanerZip = new File(Environment.TMP_DIR, "bin.zip");
        File cleaner = new File(Environment.BINDIR, "androidide-cleaner");
        final boolean isOld = CLEANER_VERSION > prefs.getInt(KEY_CLEANER_VERSION, 0);
        if(!cleaner.exists() || isOld) {
            ResourceUtils.copyFileFromAssets("data/bin.zip", cleanerZip.getAbsolutePath());
            try {
                ZipUtils.unzipFile(cleanerZip, Environment.BINDIR);
                prefs.putInt(KEY_CLEANER_VERSION, CLEANER_VERSION);
            } catch (Throwable e) {}
        }

        if(cleaner.exists() && !cleaner.canExecute())
            cleaner.setExecutable(true);
    }
    
    
    
    private static void extractGradleApiIfNeeded() {
        final File libs = Environment.GRADLE_API_LIBS_DIR;
        final File gradleZip = new File(Environment.TMP_DIR, "gradle-api.zip");
        final boolean isOld = GRADLE_API_VERSION > prefs.getInt(KEY_GRADLE_API_VERSION, 0);
        if(!libs.exists() || isOld) {
            try {
                ResourceUtils.copyFileFromAssets("data/gradle-api.zip", gradleZip.getAbsolutePath());
                ZipFile file = new ZipFile(gradleZip, ConstantsBridge.GRADLE_API_ZIP_PASSWORD_HASH.toCharArray());
                file.extractAll(Environment.GRADLE_API_DIR.getAbsolutePath());
                FileUtils.delete(gradleZip);
                prefs.putInt(KEY_GRADLE_API_VERSION, GRADLE_API_VERSION);
            } catch (Throwable th) { }
        }
    }
    
    private static final String INIT_SCRIPT = 
        "/**"
    + "\n * This is the init script that AndroidIDE uses to configure builds."
    + "\n * "
    + "\n * DO NOT MODIFY"
    + "\n * Any modification will be overwritten!"
    + "\n *"
    + "\n * If you\'re here, most probably you\'re trying to look into how AndroidIDE works"
    + "\n * or maybe you\'re trying to modify AndroidIDE"
    + "\n * Only thing I can say, is, FUCK YOU!"
    + "\n */"
    + "\n"
    + "\nimport org.gradle.api.Plugin"
    + "\nimport org.gradle.api.Project"
    + "\n"
    + "\ninitscript {"
    + "\n    repositories {"
    + "\n        maven { url \'https://maven.aliyun.com/repository/public/\' } "
    + "\n        maven { url \'https://maven.aliyun.com/repository/google/\' }"
    + "\n        maven { url \'https://maven.aliyun.com/repository/gradle-plugin/\' }"
    + "\n        maven { url \'https://clojars.org/repo/\'}"
    + "\n        maven { url \'https://jitpack.io\'}"
    + "\n        maven { url \'https://maven.google.com\' }"
    + "\n        google()"
    + "\n        mavenCentral()"
    + "\n        mavenLocal()"
    + "\n        jcenter()"
    + "\n    }"
    + "\n    "
    + "\n    dependencies {"
    + "\n        classpath \"com.android.tools.build:gradle:7.0.0\""
    + "\n    }"
    + "\n}"
    + "\n"
    + "\ngradle.projectsLoaded {"
    + "\n    rootProject.subprojects.forEach { sub ->"
    + "\n        sub.afterEvaluate {"
    + "\n            if(sub.getPluginManager().hasPlugin(\"com.android.application\")) {"
    + "\n                sub.getPluginManager().apply(AndroidIDEPlugin.class)"
    + "\n            }"
    + "\n        }"
    + "\n    }"
    + "\n}"
    + "\n"
    + "\nclass AndroidIDEPlugin implements Plugin<Project> {"
    + "\n  "
    + "\n  def AAPT2_VERSION = \"4.0.1-6197926\""
    + "\n  "
    + "\n  @Override"
    + "\n  void apply(Project project) {"
    + "\n      "
    + "\n      project.android {"
    + "\n          sourceSets {"
    + "\n              main.java.srcDirs += \"${System.getenv(\"HOME\")}/logsender\""
    + "\n          }"
    + "\n      }"
    + "\n      "
    + "\n      project.configurations.matching { it.name == \'_internal_aapt2_binary\' }.all { config ->"
    + "\n          config.resolutionStrategy.eachDependency { details ->"
    + "\n              details.useVersion(\"$AAPT2_VERSION\")"
    + "\n          }"
    + "\n      }"
    + "\n      "
    + "\n      project.android.applicationVariants.all { variant ->"
    + "\n          project.tasks.create(name: \"printDependencies${variant.name.capitalize()}\", group: \"Android IDE\", description: \"[AndroidIDE] List all dependencies in project including those in subprojects for ${variant.name.capitalize()} build.\") {"
    + "\n              doLast {"
    + "\n                  showDependencies(project, variant)"
    + "\n              }"
    + "\n          }"
    + "\n      }"
    + "\n  }"
    + "\n}"
    + "\n"
    + "\ndef showDependencies(def project, def variant) {"
    + "\n  println \">> ${variant.name.capitalize()} dependencies <<\""
    + "\n  variant.getCompileClasspath().each { dep ->"
    + "\n      println dep.absolutePath"
    + "\n  }"
    + "\n    println \"${System.getenv(\'ANDROID_HOME\')}/platforms/${project.android.compileSdkVersion}/android.jar\""
    + "\n}";
    
    private static final Logger LOG = Logger.instance("ToolsManager");
}
