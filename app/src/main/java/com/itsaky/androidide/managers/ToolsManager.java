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
    
    public static final int JLS_VERSION = 25;
    public static final int LOGSENDER_VERSION = 2;
    public static final int CLEANER_VERSION = 1;
    public static final int GRADLE_API_VERSION = 2;
    
    public static final String KEY_JLS_VERSION = "tools_jlsVersion";
    public static final String KEY_LOGSENDER_VERSION = "tools_logsenderVersion";
    public static final String KEY_CLEANER_VERSION = "tools_cleanerVersion";
    public static final String KEY_GRADLE_API_VERSION = "tools_gradleApiVersion";
    
    
    public static void init(StudioApp app, Runnable onFinish) {
        prefs = app.getPrefManager();
        copyBusyboxIfNeeded();
        extractJlsIfNeeded();
        extractLogsenderIfNeeded();
        extractLibHooks();
        
        writeInitScript();
        
        rewriteProjectData();
        
        if(onFinish != null)
            onFinish.run();
    }

    private static void extractLibHooks() {
        if(!Environment.LIBHOOK.exists()) {
            ResourceUtils.copyFileFromAssets("data/libhook.so", Environment.LIBHOOK.getAbsolutePath());
        }
        
        if(!Environment.LIBHOOK2.exists()) {
            ResourceUtils.copyFileFromAssets("data/libhook2.so", Environment.LIBHOOK2.getAbsolutePath());
        }
    }

    private static void rewriteProjectData() {
        FileIOUtils.writeFileFromString(Environment.PROJECT_DATA_FILE, "/**********************/");
    }

    private static void copyBusyboxIfNeeded() {
        File exec = Environment.BUSYBOX;
        if(exec.exists()) return;
        Environment.mkdirIfNotExits(exec.getParentFile());
        ResourceUtils.copyFileFromAssets("data/busybox", exec.getAbsolutePath());
        if(!exec.canExecute()) {
            exec.setExecutable(true);
        }
    }
    
    private static void writeInitScript() {
        FileIOUtils.writeFileFromString(Environment.INIT_SCRIPT, INIT_SCRIPT);
    }
    
    
    private static void extractJlsIfNeeded() {
        final boolean isOld = JLS_VERSION > prefs.getInt(KEY_JLS_VERSION, 0);
        
        if(!Environment.JLS_JAR.exists() || isOld) {
            try {
                extractJls();
            } catch (Throwable e) {}
        }
    }

    private static void extractJls(){
        ResourceUtils.copyFileFromAssets("data/jls.jar", Environment.JLS_JAR.getAbsolutePath());
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
    
    private static final String INIT_SCRIPT = 
    (   "import org.gradle.api.Plugin"
    + "\nimport org.gradle.api.Project"
    + "\nimport groovy.json.*"
    + "\n"
    + "\ngradle.projectsLoaded {"
    + "\n    rootProject.subprojects.forEach { sub ->"
    + "\n        sub.afterEvaluate {"
    + "\n            sub.apply plugin: AndroidIDEPlugin"
    + "\n        }"
    + "\n    }"
    + "\n}"
    + "\n"
    + "\nclass AndroidIDEPlugin implements Plugin<Project> {"
    + "\n    "
    + "\n    @Override"
    + "\n    void apply(Project project) {"
    + "\n        "
    + "\n        def isApp = project.getPluginManager().hasPlugin(\"com.android.application\")"
    + "\n        def isLibrary = project.getPluginManager().hasPlugin(\"com.android.library\")"
    + "\n        "
    + "\n        if(!(isApp || isLibrary)) {"
    + "\n            println \"Project: \" + project.getName() + \" doesn\'t apply Android\'s application or library plugin\""
    + "\n            println \"AndroidIDE\'s plugin will not be applied to this project!\""
    + "\n            return"
    + "\n        }"
    + "\n        "
    + "\n        if(isApp) {"
    + "\n            project.android {"
    + "\n                sourceSets {"
    + "\n                    main.java.srcDirs += \"${System.getenv(\"HOME\")}/logsender\""
    + "\n                }"
    + "\n            }"
    + "\n        }"
    + "\n        "
    + "\n        def variants = null"
    + "\n        if(isApp) {"
    + "\n            variants = project.android.applicationVariants"
    + "\n        } else if (isLibrary) {"
    + "\n            variants = project.android.libraryVariants"
    + "\n        } else {"
    + "\n            println \"AndroidIDE: Cannot find applicationVariants or libraryVariants\""
    + "\n            return"
    + "\n        }"
    + "\n        "
    + "\n        project.tasks.create("
    + "\n            name: \"initializeIDEProject\","
    + "\n            group: \"AndroidIDE\","
    + "\n            description: \"Initializes the project in AndroidIDE. AndroidIDE manages the proper execution of this task. You\'re not supposed to execute this task manually!\""
    + "\n            )"
    + "\n        {"
    + "\n            outputs.upToDateWhen { false }"
    + "\n            "
    + "\n            doLast {"
    + "\n                def root = project.getRootProject()"
    + "\n                "
    + "\n                IDEProject ideProject = new IDEProject()"
    + "\n                ideProject.name = root.getName()"
    + "\n                ideProject.displayName = root.getDisplayName()"
    + "\n                ideProject.description = root.getDescription()"
    + "\n                ideProject.path = root.getPath()"
    + "\n                ideProject.projectDir = root.getProjectDir().getAbsolutePath()"
    + "\n                "
    + "\n                def tasks = root.tasks"
    + "\n                if(tasks != null && tasks.size() > 0) {"
    + "\n                    addTasks(ideProject, root)"
    + "\n                }"
    + "\n    "
    + "\n                root.subprojects.forEach { sub ->"
    + "\n                    addIDEProject(ideProject, sub);"
    + "\n                }"
    + "\n    "
    + "\n                File out = new File(\"@@PROJECT_DATA_FILE@@\")"
    + "\n                if(out.exists()) {"
    + "\n                    out.delete()"
    + "\n                }"
    + "\n                out << new JsonBuilder( ideProject ).toPrettyString()"
    + "\n    "
    + "\n                println \">>> PROJECT INITIALIZED <<<\""
    + "\n            }"
    + "\n        }"
    + "\n        "
    + "\n        project.afterEvaluate {"
    + "\n            project.tasks.getByName(\'compileDebugJavaWithJavac\').finalizedBy(\'initializeIDEProject\')"
    + "\n            project.tasks.getByName(\'compileReleaseJavaWithJavac\').finalizedBy(\'initializeIDEProject\')"
    + "\n        }"
    + "\n    }"
    + "\n    "
    + "\n    def addIDEProject (IDEProject parent, Project sub) {"
    + "\n        if(parent == null || sub == null) {"
    + "\n            return"
    + "\n        }"
    + "\n        "
    + "\n        def isApp = sub.getPluginManager().hasPlugin(\"com.android.application\")"
    + "\n        def isLibrary = sub.getPluginManager().hasPlugin(\"com.android.library\")"
    + "\n            "
    + "\n        if(!(isApp || isLibrary)) {"
    + "\n            return"
    + "\n        }"
    + "\n            "
    + "\n        IDEModule module = new IDEModule()"
    + "\n        if(isApp) {"
    + "\n            module = new IDEAppModule()"
    + "\n        }"
    + "\n        "
    + "\n        def android = sub.android"
    + "\n        def config = android.defaultConfig"
    + "\n        "
    + "\n        module.name = sub.getName()"
    + "\n        module.displayName = sub.getDisplayName()"
    + "\n        module.description = sub.getDescription()"
    + "\n        module.path = sub.getPath()"
    + "\n        module.buildToolsVersion = android.buildToolsVersion"
    + "\n        module.compileSdkVersion = android.compileSdkVersion"
    + "\n        module.minSdk = config.minSdkVersion"
    + "\n        module.targetSdk = config.targetSdkVersion"
    + "\n        module.versionCode = config.versionCode"
    + "\n        module.versionName = config.versionName"
    + "\n        module.projectDir = sub.getProjectDir().getAbsolutePath()"
    + "\n        module.viewBindingEnabled = android.buildFeatures.viewBinding"
    + "\n        "
    + "\n        if(isApp) {"
    + "\n            module.applicationId = config.applicationId"
    + "\n        }"
    + "\n        "
    + "\n        def tasks = sub.tasks"
    + "\n        if(tasks != null && tasks.size() > 0) {"
    + "\n            addTasks(module, sub)"
    + "\n        }"
    + "\n        "
    + "\n        def variants = null"
    + "\n        if(isApp) {"
    + "\n            variants = sub.android.applicationVariants"
    + "\n        } else if (isLibrary) {"
    + "\n            variants = sub.android.libraryVariants"
    + "\n        } else {"
    + "\n            println \"AndroidIDE: Cannot find build variants of project: ${module.name}(${module.path})\""
    + "\n        }"
    + "\n        "
    + "\n        if(variants != null) {"
    + "\n            variants.all { variant ->"
    + "\n                addDependencies(module, variant);"
    + "\n            }"
    + "\n        }"
    + "\n        "
    + "\n        def subs = sub.getSubprojects()"
    + "\n        if(subs != null && subs.size() > 0) {"
    + "\n            subs.forEach { subOfSub -> "
    + "\n                addIDEProject(module, subOfSub)"
    + "\n            }"
    + "\n        }"
    + "\n        parent.modules.add(module)"
    + "\n    }"
    + "\n    "
    + "\n    def addDependencies(def module, def variant) {"
    + "\n        variant.getCompileClasspath().each { dependency ->"
    + "\n            def path = dependency.absolutePath"
    + "\n            if(!module.dependencies.contains(path)) {"
    + "\n                module.dependencies.add(dependency.absolutePath);"
    + "\n            }"
    + "\n        }"
    + "\n    }"
    + "\n    "
    + "\n    def addTasks (IDEProject project, Project gradleProject) {"
    + "\n        if(project == null || gradleProject == null) {"
    + "\n            return"
    + "\n        }"
    + "\n        "
    + "\n        gradleProject.tasks.forEach { gradleTask -> "
    + "\n            IDETask task = new IDETask()"
    + "\n            task.name = gradleTask.getName()"
    + "\n            task.description = gradleTask.getDescription()"
    + "\n            task.group = gradleTask.getGroup()"
    + "\n            task.path = gradleTask.getPath()"
    + "\n            "
    + "\n            project.tasks.add(task)"
    + "\n        }"
    + "\n    }"
    + "\n}"
    + "\n"
    + "\nclass IDEProject {"
    + "\n    def name = \"Not defined\""
    + "\n    def displayName = \"Not defined\""
    + "\n    def description = \"Not defined\""
    + "\n    def path = \"Not defined\""
    + "\n    def projectDir = \"Not defined\""
    + "\n    List<IDEProject> modules = new ArrayList<IDEProject>()"
    + "\n    List<IDETask> tasks = new ArrayList<IDETask>()"
    + "\n    List<String> dependencies = new ArrayList<String>()"
    + "\n}"
    + "\n"
    + "\nclass IDEModule extends IDEProject {"
    + "\n    def buildToolsVersion = 0"
    + "\n    def compileSdkVersion = 0"
    + "\n    def minSdk = 0"
    + "\n    def targetSdk = 0"
    + "\n    def versionCode = 0"
    + "\n    def versionName = \"Not defined\""
    + "\n    def isLibrary = true"
    + "\n    def viewBindingEnabled = false"
    + "\n}"
    + "\n"
    + "\nclass IDEAppModule extends IDEModule {"
    + "\n    def applicationId = \"Not defined\""
    + "\n    "
    + "\n    public IDEAppModule() {"
    + "\n        isLibrary = false"
    + "\n    }"
    + "\n}"
    + "\n"
    + "\nclass IDETask {"
    + "\n    def name = \"Not defined\""
    + "\n    def description = \"Not defined\""
    + "\n    def group = \"Not defined\""
    + "\n    def path = \"Not defined\""
    + "\n}").replace("@@PROJECT_DATA_FILE@@", Environment.PROJECT_DATA_FILE.getAbsolutePath());
    
    private static final Logger LOG = Logger.instance("ToolsManager");
}
