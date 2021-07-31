package com.itsaky.androidide.app;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.MultiDexApplication;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.interfaces.LanguageServerListener;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.java.provider.JavaCompletionProvider;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.services.IDEService;
import com.itsaky.androidide.services.MessagingService;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.PreferenceManager;
import com.itsaky.androidide.utils.StudioUtils;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.language.java.server.JavaLanguageServer;
import android.os.Handler;
import com.itsaky.lsp.InitializeParams;
import com.itsaky.lsp.DidChangeConfigurationParams;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.itsaky.lsp.LanguageClient;

public class StudioApp extends MultiDexApplication
{
	private static StudioApp instance;
	private StudioUtils mUtils;
	private IDEService buildService;
    private JavaLanguageServer languageServer;
    private XMLCompletionService mXmlCompletionService;
	private PreferenceManager mPrefsManager;
	private NotificationManager mNotificationManager;
	
    private boolean stopGradleDaemon = true;
	private String sArch = "";
    
    private List<LanguageServerListener> serveralisteners = new ArrayList<>();
    
	public static final boolean DEBUG = com.itsaky.androidide.BuildConfig.DEBUG;
	
	public static final long BUSYBOX_VERSION = 1;
    public static final String TAG = "StudioApp";
	public static final String ASSETS_DATA_DIR = "data/";
	public static final String PROJECTS_FOLDER = "AndroidIDEProjects";
	
	public static final String NOTIFICATION_ID_UPDATE = "17571";
	public static final String NOTIFICATION_ID_DEVS = "17572";
	
	public static final String TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions";
	public static final String SUGGESTIONS_URL = "https://github.com/itsaky/AndroidIDE";

	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
	
	@Override
	public void onCreate() {
		this.instance = this;
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> handleCrash(thread, ex));
        initLogger();
		super.onCreate();
		mPrefsManager = new PreferenceManager(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        JavaCharacter.initMap();
        
		newShell(line -> handleLog(line)).bgAppend("logcat -v threadtime");
        setupLibsIfNeeded();
        extractJlsIfNeeded();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createNotificationChannels();
		}
		
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_UPDATE);
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_DEV_MSGS);
	}
	
	private void handleLog(CharSequence seq) {
		if(seq == null)
			return;
		String line = seq.toString();
		line = line.endsWith("\n") ? line : line + "\n";
		
		File log = new File(FileUtil.getExternalStorageDir(), "ide_xlog/idelog.txt");
		try {
			FileOutputStream os = new FileOutputStream(log, true);
			os.write(line.getBytes());
			os.close();
		} catch (IOException e) {}
	}
	
	@SuppressLint("NewApi")
	private void createNotificationChannels() {
		NotificationChannel updateChannel = new NotificationChannel(NOTIFICATION_ID_UPDATE, getNotificationChannelNameForId(NOTIFICATION_ID_UPDATE), NotificationManager.IMPORTANCE_HIGH);
		updateChannel.enableLights(true);
		updateChannel.enableVibration(true);
		updateChannel.setLightColor(Color.RED);
		updateChannel.setVibrationPattern(new long[]{10, 50, 10});
		NotificationManagerCompat.from(this).createNotificationChannel(updateChannel);
		
		NotificationChannel devsChannels = new NotificationChannel(NOTIFICATION_ID_DEVS, getNotificationChannelNameForId(NOTIFICATION_ID_DEVS), NotificationManager.IMPORTANCE_MIN);
		devsChannels.enableLights(true);
		devsChannels.enableVibration(true);
		devsChannels.setLightColor(Color.BLUE);
		devsChannels.setVibrationPattern(new long[]{10, 50, 10});
		NotificationManagerCompat.from(this).createNotificationChannel(devsChannels);
	}
	
	public String getNotificationChannelNameForId(String id) {
		if(id.equals(NOTIFICATION_ID_UPDATE))
			return getUpdateNotificationChannelName();
		else if(id.equals(NOTIFICATION_ID_DEVS))
			return getDevNotificationChannelName();
		else return "AndroidIDE Notifications";
	}
	
	private String getUpdateNotificationChannelName() {
		return getString(R.string.cms_channel_id_update);
	}
	
	private String getDevNotificationChannelName() {
		return getString(R.string.cms_channel_id_devs);
	}
	
	private void initLogger() {
		
	}
    
    private void extractJlsIfNeeded() {
        boolean gson = false, jls = false, proto = false;
        final List<File> files = FileUtils.listFilesInDir(Environment.JLS_HOME, true);
        for(File f : files) {
            gson = gson == true ? gson : f.getName().equals("gson.jar");
            jls = jls == true ? jls : f.getName().equals("jls.jar");
            proto = proto == true ? proto : f.getName().equals("protobuf.jar");
        }
        
        if(!false/* !(gson && jls && proto) */) {
            try {
                extractJls();
            } catch (Throwable e) {
                Logger.instance("StudioApp").e(ThrowableUtils.getFullStackTrace(e));
            }
        }
    }

    private void extractJls() throws IOException {
        final File jlsZip = new File(Environment.JLS_HOME, "jls.zip");
        ResourceUtils.copyFileFromAssets("data/jls.zip", jlsZip.getAbsolutePath());
        ZipUtils.unzipFile(jlsZip, Environment.JLS_HOME);
        FileUtils.delete(jlsZip);
    }
    
    private boolean setupLibsIfNeeded() {
        File lib = Environment.LIBDIR;
        if(lib != null && lib.exists() && lib.isDirectory()) {
            boolean libhook, librt, libgcc, libgcc_real, libpthread, libz, libcpp_shared;
            libhook = librt = libgcc = libgcc_real = libpthread = libz = libcpp_shared = false;
            File[] list = lib.listFiles();
            if(list != null) {
               for(File f : list) {
                   libhook = libhook == false ? f.getName().equals("libhook.so") : libhook;
                   librt = librt == false ? f.getName().equals("librt.so") : librt;
                   libpthread = libpthread == false ? f.getName().equals("libpthread.so") : libpthread;
                   libgcc = libgcc == false ? f.getName().equals("libgcc.so") : libgcc;
                   libgcc_real = libgcc_real = false ? f.getName().equals("libgcc_real.so") : libgcc_real;
                   libz = libz == false ? f.getName().equals("libz.so.1") : libz;
                   libcpp_shared = libcpp_shared == false ? f.getName().equals("libc++_shared.so") : libcpp_shared;
               }
            } else return extractLibs(lib);
            return !(libhook && librt && libgcc && libgcc_real && libpthread && libz && libcpp_shared) ? extractLibs(lib) : true;
        } else return extractLibs(lib);
    }

    private boolean extractLibs(File lib) {
        if(lib.exists()) 
            FileUtils.delete(lib.getParentFile());
        return lib.mkdirs() && ResourceUtils.copyFileFromAssets(getAssetsDataFile("lib"), lib.getAbsolutePath());
    }
    
	private File getBusybox() {
        File parentFile = getRootDir().getParentFile();
        File busyboxDir = new File(parentFile, "bin/busybox");
        if (busyboxDir.exists() && busyboxDir.isDirectory())
		    FileUtil.deleteFile(busyboxDir.getAbsolutePath());
        
        if ((!busyboxDir.exists() || SPStaticUtils.getLong("BUSYBOX_VERSION", 0) < BUSYBOX_VERSION) && Build.SUPPORTED_64_BIT_ABIS.length > 0) {
            String arch = getArch(Build.SUPPORTED_64_BIT_ABIS[0]);
            
            if (arch == null) {
                return null;
            } else if (ResourceUtils.copyFileFromAssets(getAssetsDataFile(new StringBuffer().append("busybox/").append(arch).toString()), parentFile.getAbsolutePath())) {
                SPStaticUtils.put("busyboxArch", arch);
                busyboxDir.setExecutable(true);
                SPStaticUtils.put("BUSYBOX_VERSION", BUSYBOX_VERSION);
                return busyboxDir;
            } else return null;
        }
        if (!busyboxDir.canExecute())
		    busyboxDir.setExecutable(true);
            
        return busyboxDir;
    }
	
	public void onGradleUpdated() {
		IDEService service = getBuildService();
		if(service != null) {
			service.onGradleUpdated();
		}
	}
	
	public void startBuildService() {
		if(!IDEService.isRunning()) {
			startService(new Intent(this, IDEService.class));
		}
	}
	
	public void storeBuildServiceInstance(IDEService.BuildListener listener) {
		buildService = IDEService.getInstance();
		buildService.setListener(listener);
	}
	
	public IDEService getBuildService() {
		return buildService;
	}
	
    public void createCompletionService(AndroidProject project, LanguageClient client) {
        this.languageServer = new JavaLanguageServer(project, client);
        this.mXmlCompletionService = new XMLCompletionService();
        
        this.languageServer.startServer();
    }
    
    public JavaLanguageServer getJavaLanguageServer() {
        return languageServer;
    }
	
	public XMLCompletionService getXmlCompletionService() {
		return mXmlCompletionService;
	}
	
	public boolean areCompletorsStarted() {
		return mXmlCompletionService != null
			&& mXmlCompletionService.isInitiated();
	}
    
    public void setStopGradleDaemon(boolean startGradleDaemon) {
        this.stopGradleDaemon = startGradleDaemon;
    }

    public boolean isStopGradleDaemon() {
        return stopGradleDaemon;
    }
	
	public static StudioApp getInstance() {
		return instance;
	}
    
    public ShellServer newShell(ShellServer.Callback callback) {
        ShellServer shellServer = new ShellServer(callback, "sh", Environment.mkdirIfNotExits(getRootDir()).getAbsolutePath());
        if (getBusybox() != null) shellServer.append(String.format("export BUSYBOX=%s\n", getBusybox().getAbsolutePath()), false);
        shellServer.start();
        return shellServer;
    }
    
	public void addServerListener(LanguageServerListener listener) {
        if(serveralisteners == null) serveralisteners = new ArrayList<>();
        if(!serveralisteners.contains(listener))
            serveralisteners.add(listener);
    }
    
    public void removeServerListener(LanguageServerListener listener) {
        if(serveralisteners == null) serveralisteners = new ArrayList<>();
        if(serveralisteners.contains(listener))
            serveralisteners.remove(listener);
	}


	public void writeException(Throwable th) {
		FileUtil.writeFile(new File(FileUtil.getExternalStorageDir(), "idelog.txt").getAbsolutePath(), ThrowableUtils.getFullStackTrace(th));
	}
	
	public File getIndexedDir() {
		return Environment.mkdirIfNotExits(new File(getRootDir(), "indexed"));
	}
	
	public File getD8Jar() {
		File f = new File(getRootDir(), "d8.jar");
		if(f.exists() && f.isFile())
			return f;
		if(ResourceUtils.copyFileFromAssets("data/d8.jar", f.getAbsolutePath())) {
			return f;
		}
		
		return null;
	}
	
	public File getRootDir() {
		return new File(getIDEDataDir(), "framework");
	}
    
	public File getRootDirOld() {
		return new File(getFilesDir(), "framework");
	}
	
	public File getIDEDataDir() {
		return Environment.mkdirIfNotExits(new File("/data/data/com.itsaky.androidide/files"));
	}
	
	public final File getLogSenderDir() {
		return new File(getRootDir(), "logsender");
	}
	
	public final File getToolsDownloadDirectory() {
		return new File(getRootDir(), "downloads");
	}
	
	public final File getTempProjectDir() {
		return Environment.mkdirIfNotExits(new File(getRootDir(), "tempProject"));
	}
	
	public final File getTempClassesDir() {
		return Environment.mkdirIfNotExits(new File(FileUtil.getExternalStorageDir() /*getTempProjectDir() */, "classes"));
	}
	
	public final String getDownloadRequestUrl() {
		return new String(EncodeUtils.base64Decode("aHR0cHM6Ly9hbmRyb2lkaWRlLmNvbS9idWlsZC90b29scy9kb3dubG9hZC8="));
	}
	
	public boolean isFrameworkDownloaded() {
		return getToolsDownloadDirectory().exists() && mPrefsManager.isFrameworkDownloaded();
	}

	public boolean isFrameworkInstalled() {
		return getRootDir().exists() && mPrefsManager.isFrameworkInstalled();
	}
	
	public PreferenceManager getPrefManager() {
		return mPrefsManager;
	}

	public File getProjectsDir() {
		return Environment.mkdirIfNotExits(new File(FileUtil.getExternalStorageDir(), PROJECTS_FOLDER));
	}

	public File[] listProjects() {
		return getProjectsDir().listFiles(p1 -> p1.isDirectory());
	}
	
	public boolean is64Bit() {
		if(Build.VERSION.SDK_INT >= 23) {
			return android.os.Process.is64Bit();
		} else {
			String[] abis = Build.SUPPORTED_64_BIT_ABIS;
			return abis != null && abis.length > 0;
		}
	}

	public String getArch() {
        if (sArch == null) {
            sArch = getArch(System.getProperty("os.arch"));
        }
        return sArch;
    }
	
	private void handleCrash(Thread thread, Throwable th) {
		if(DEBUG)
			writeException(th);
		if(this.uncaughtExceptionHandler != null) {
			this.uncaughtExceptionHandler.uncaughtException(thread, th);
		}
		try {
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Throwable ignored) {}
	}

    private String getArch(String str) {
        if (str.length() > 0) {
            switch (str.toLowerCase(Locale.US).charAt(0)) {
                case 'a':
                    return str.equals("amd64") ? "x86_64" : str.contains("64") ? "arm64" : "arm";
                case 'i':
                case 'x':
                    return str.contains("64") ? "x86_64" : "x86";
                case 'm':
                    return str.contains("64") ? "mips64" : "mips";
            }
        }
        return "unknown";
    }
	
	public String getAssetsDataFile() {
        return ASSETS_DATA_DIR;
    }
	
	public String getAssetsDataFile(String str) {
        return new StringBuffer().append(getAssetsDataFile()).append(str).toString();
    }
	
	public void checkAndUpdateGradle() {
		File gradle = new File(Environment.GRADLE_DIR, "bin/gradle");
		if(gradle.exists()) {
			String[] txt = FileUtil.readFile(gradle.getAbsolutePath()).split("\n");
			if(txt != null && txt.length > 1 && txt[0].contains("#!/usr/bin/env sh")) {
				txt[0] = "";
				FileUtil.writeFile(gradle.getAbsolutePath(), TextUtils.join("\n", txt));
				if(!gradle.canExecute())
					gradle.setExecutable(true);
			}
		}
	}
	
	public StudioUtils getUtils() {
		return mUtils == null ? mUtils = new StudioUtils(this) : mUtils;
	}
	
	public void toast(String msg, Toaster.Type type) {
		getUtils().toast(msg, type);
	}
	
	public void toast(int msg, Toaster.Type type) {
		getUtils().toast(msg, type);
	}
	
	public void toastLong(String msg, Toaster.Type type) {
		getUtils().toastLong(msg, type);
	}
	
	public void toastLong(int msg, Toaster.Type type) {
		getUtils().toastLong(msg, type);
	}
    
    public static File getCompilerOut() {
        return new File(FileUtil.getExternalStorageDir(), "ide_xlog/compiler_out.txt");
    }
    
    public static File getCompilerError() {
        return new File(FileUtil.getExternalStorageDir(), "ide_xlog/compiler_err.txt");
    }
	
	public void openTelegramGroup() {
		Intent open = new Intent();
		open.setAction(Intent.ACTION_VIEW);
		open.setData(Uri.parse(StudioApp.TELEGRAM_GROUP_URL));
		open.setPackage("org.telegram.messenger");
		open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(open);
	}
	
	public void openIssueTracker() {
		Intent open = new Intent();
		open.setAction(Intent.ACTION_VIEW);
		open.setData(Uri.parse(StudioApp.SUGGESTIONS_URL));
		open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(open);
	}
}
