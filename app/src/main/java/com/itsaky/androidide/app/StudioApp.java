package com.itsaky.androidide.app;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.MultiDexApplication;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.java.manager.JavaCharacter;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.services.MessagingService;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.ui.inflater.ILayoutInflater;
import com.itsaky.androidide.ui.inflater.LayoutInflaterConfiguration;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.StudioUtils;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.toaster.Toaster;
import com.itsaky.widgets.WidgetInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class StudioApp extends MultiDexApplication {
    
	private static StudioApp instance;
    
	private StudioUtils mUtils;
	private PreferenceManager mPrefsManager;
	private NotificationManager mNotificationManager;
	
    private boolean stopGradleDaemon = true;
    
	private static String sArch = "";
    
    private static ApiInfo mApiInfo;
    private static AttrInfo mAttrInfo;
    private static WidgetInfo mWidgetInfo;
    
    private XMLCompletionService mXmlCompletionService;
    private static ILayoutInflater mLayoutInflater;
    
	public static final boolean DEBUG = com.itsaky.androidide.BuildConfig.DEBUG;
	
	public static final long BUSYBOX_VERSION = 1;
    public static final String TAG = "StudioApp";
	public static final String ASSETS_DATA_DIR = "data/";
    
	public static final String NOTIFICATION_ID_UPDATE = "17571";
	public static final String NOTIFICATION_ID_DEVS = "17572";
	
	public static final String TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions";
	public static final String SUGGESTIONS_URL = "https://github.com/itsaky/AndroidIDE";
    public static final String WEBSITE = "https://androidide.com";
    public static final String EMAIL = "contact@androidide.com";
    
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    
    private static final Logger LOG = Logger.instance("StudioApp");
	
	@Override
	public void onCreate() {
		this.instance = this;
		this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        
		Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> handleCrash(thread, ex));
        
		super.onCreate();
        
		mPrefsManager = new PreferenceManager(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        JavaCharacter.initMap();
        ToolsManager.init(this, null);
        
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createNotificationChannels();
		}
        
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_UPDATE);
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_DEV_MSGS);
        
        initializeApiInformation();
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
    
    public void createInflater (LayoutInflaterConfiguration config) {
        this.mLayoutInflater = ILayoutInflater.newInstance(config);
    }
    
    public ILayoutInflater getLayoutInflater () {
        return mLayoutInflater;
    }
    
    /**
     * Reads API version information from api-versions.xml
     */
    public void initializeApiInformation() {
        new Thread(() -> {
            try {
                mApiInfo = new ApiInfo (StudioApp.this);
                mAttrInfo = new AttrInfo (StudioApp.this);
                mWidgetInfo = new WidgetInfo (StudioApp.this);
            } catch (Throwable th) {
                ThreadUtils.runOnUiThread(() -> {
                    toast(R.string.msg_sdk_info_load_failed, Toaster.Type.ERROR);
                });
            }
        }, "SDK Information Loader").start();
    }
    
    public void stopAllDaemons() {
        newShell(null).bgAppend("gradle --stop");
    }
	
	public XMLCompletionService getXmlCompletionService() {
		return mXmlCompletionService;
	}
    
    public ApiInfo apiInfo() {
        return this.mApiInfo;
    }
    
    public AttrInfo attrInfo () {
        return this.mAttrInfo;
    }

    public WidgetInfo widgetInfo () {
        return this.mWidgetInfo;
    }
	
	public boolean isXmlServiceStarted() {
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
        return newShell(callback, true);
    }
    
    public ShellServer newShell(ShellServer.Callback callback, boolean redirectErrors) {
        ShellServer shellServer = new ShellServer(callback, "sh", Environment.mkdirIfNotExits(getRootDir()).getAbsolutePath(), redirectErrors);
        shellServer.start();
        return shellServer;
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
		if(ResourceUtils.copyFileFromAssets(ToolsManager.getCommonAsset("d8.jar"), f.getAbsolutePath())) {
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
	
    @SuppressLint("SdCardPath")
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
		return Environment.mkdirIfNotExits(new File(Environment.TMP_DIR, "tempProject"));
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
		return Environment.PROJECTS_DIR;
	}

	public File[] listProjects() {
		return getProjectsDir().listFiles(p1 -> p1.isDirectory());
	}
	
	public static boolean is64Bit() {
		return isAarch64();
	}
    
    public static boolean is32Bit() {
        return isArmv7a();
	}
    
    public static boolean isAbiSupported () {
        return isAarch64() || isArmv7a();
    }
    
    public static boolean isAarch64 () {
        return Arrays.stream(android.os.Build.SUPPORTED_ABIS).anyMatch("arm64-v8a"::equals);
    }
    
    public static boolean isArmv7a () {
        return Arrays.stream(android.os.Build.SUPPORTED_ABIS).anyMatch("armeabi-v7a"::equals);
    }

	public static String getArch() {
        if (StudioApp.isAarch64()) {
            return "arm64-v8a";
        } else if (StudioApp.isArmv7a()) {
            return "armeabi-v7a";
        }
        throw new UnsupportedOperationException ("Device not supported");
    }
    
	private void handleCrash(Thread thread, Throwable th) {
		writeException(th);
        
		if(this.uncaughtExceptionHandler != null) {
			this.uncaughtExceptionHandler.uncaughtException(thread, th);
		}
        
        // TODO Show exception in another activity
		try {
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Throwable ignored) {
            // ignored
        }
	}
	
	public String getAssetsDataFile() {
        return ASSETS_DATA_DIR;
    }
	
	public String getAssetsDataFile(String str) {
        return new StringBuffer().append(getAssetsDataFile()).append(str).toString();
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
		try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse(StudioApp.TELEGRAM_GROUP_URL));
            open.setPackage("org.telegram.messenger");
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open);
        } catch (Throwable th) {
            try {
                Intent open = new Intent();
                open.setAction(Intent.ACTION_VIEW);
                open.setData(Uri.parse(StudioApp.TELEGRAM_GROUP_URL));
                open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(open);
            } catch (Throwable th2) {
                toast(th2.getMessage(), Toaster.Type.ERROR);
            }
        }
	}
	
	public void openIssueTracker() {
		try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse(StudioApp.SUGGESTIONS_URL));
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open); 
        } catch (Throwable th) {
            toast(th.getMessage(), Toaster.Type.ERROR);
        }
	}
    
    public void openWebsite() {
        try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse(StudioApp.WEBSITE));
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open);
        } catch (Throwable th) {
            toast(th.getMessage(), Toaster.Type.ERROR);
        }
	}
    
    public void emailUs() {
        try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse("mailto:" + EMAIL));
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open);
        } catch (Throwable th) {
            toast(th.getMessage(), Toaster.Type.ERROR);
        }
    }
}
