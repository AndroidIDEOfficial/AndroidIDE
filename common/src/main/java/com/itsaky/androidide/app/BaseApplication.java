/************************************************************************************
 * This file is part of AndroidIDE.
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
package com.itsaky.androidide.app;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.common.R;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.StudioUtils;
import com.itsaky.toaster.Toaster;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.Arrays;

public abstract class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    private StudioUtils mUtils;
    private PreferenceManager mPrefsManager;

    public static final String NOTIFICATION_ID_UPDATE = "17571";
    public static final String NOTIFICATION_ID_DEVS = "17572";
    public static final String TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions";
    public static final String GITHUB_URL = "https://github.com/itsaky/AndroidIDE";
    public static final String WEBSITE = "https://androidide.com";
    public static final String EMAIL = "contact@androidide.com";

    @Override
    public void onCreate() {
        instance = this;
        Environment.init();
        super.onCreate();

        mPrefsManager = new PreferenceManager(this);
        JavaCharacter.initMap();
        ToolsManager.init(this, null);

        createNotificationChannels();
    }

    @SuppressLint("NewApi")
    private void createNotificationChannels() {
        NotificationChannel updateChannel =
                new NotificationChannel(
                        NOTIFICATION_ID_UPDATE,
                        getNotificationChannelNameForId(NOTIFICATION_ID_UPDATE),
                        NotificationManager.IMPORTANCE_HIGH);
        updateChannel.enableLights(true);
        updateChannel.enableVibration(true);
        updateChannel.setLightColor(Color.RED);
        updateChannel.setVibrationPattern(new long[] {10, 50, 10});
        NotificationManagerCompat.from(this).createNotificationChannel(updateChannel);

        NotificationChannel devsChannels =
                new NotificationChannel(
                        NOTIFICATION_ID_DEVS,
                        getNotificationChannelNameForId(NOTIFICATION_ID_DEVS),
                        NotificationManager.IMPORTANCE_MIN);
        devsChannels.enableLights(true);
        devsChannels.enableVibration(true);
        devsChannels.setLightColor(Color.BLUE);
        devsChannels.setVibrationPattern(new long[] {10, 50, 10});
        NotificationManagerCompat.from(this).createNotificationChannel(devsChannels);
    }

    public String getNotificationChannelNameForId(@NonNull String id) {
        if (id.equals(NOTIFICATION_ID_UPDATE)) return getUpdateNotificationChannelName();
        else if (id.equals(NOTIFICATION_ID_DEVS)) return getDevNotificationChannelName();
        else return "AndroidIDE Notifications";
    }

    private String getUpdateNotificationChannelName() {
        return getString(R.string.cms_channel_id_update);
    }

    private String getDevNotificationChannelName() {
        return getString(R.string.cms_channel_id_devs);
    }

    public static BaseApplication getBaseInstance() {
        return instance;
    }

    public ShellServer newShell(ShellServer.Callback callback) {
        return newShell(callback, true);
    }

    public ShellServer newShell(ShellServer.Callback callback, boolean redirectErrors) {
        ShellServer shellServer =
                new ShellServer(
                        callback,
                        "sh",
                        Environment.mkdirIfNotExits(getRootDir()).getAbsolutePath(),
                        Environment.getEnvironment(),
                        redirectErrors);
        shellServer.start();
        return shellServer;
    }

    public void writeException(Throwable th) {
        FileUtil.writeFile(
                new File(FileUtil.getExternalStorageDir(), "idelog.txt").getAbsolutePath(),
                ThrowableUtils.getFullStackTrace(th));
    }

    public File getRootDir() {
        return new File(getIDEDataDir(), "framework");
    }

    @SuppressLint("SdCardPath")
    public File getIDEDataDir() {
        return Environment.mkdirIfNotExits(new File("/data/data/com.itsaky.androidide/files"));
    }

    @NonNull
    @Contract(" -> new")
    public final File getLogSenderDir() {
        return new File(getRootDir(), "logsender");
    }

    public final File getTempProjectDir() {
        return Environment.mkdirIfNotExits(new File(Environment.TMP_DIR, "tempProject"));
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
        return getProjectsDir().listFiles(File::isDirectory);
    }

    public static boolean isAbiSupported() {
        return isAarch64() || isArmv7a();
    }

    public static boolean isAarch64() {
        return Arrays.asList(Build.SUPPORTED_ABIS).contains("arm64-v8a");
    }

    public static boolean isArmv7a() {
        return Arrays.asList(Build.SUPPORTED_ABIS).contains("armeabi-v7a");
    }

    @NonNull
    public static String getArch() {
        if (BaseApplication.isAarch64()) {
            return "arm64-v8a";
        } else if (BaseApplication.isArmv7a()) {
            return "armeabi-v7a";
        }
        throw new UnsupportedOperationException("Device not supported");
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

    public void openTelegramGroup() {
        try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse(BaseApplication.TELEGRAM_GROUP_URL));
            open.setPackage("org.telegram.messenger");
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open);
        } catch (Throwable th) {
            try {
                Intent open = new Intent();
                open.setAction(Intent.ACTION_VIEW);
                open.setData(Uri.parse(BaseApplication.TELEGRAM_GROUP_URL));
                open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(open);
            } catch (Throwable th2) {
                toast(th2.getMessage(), Toaster.Type.ERROR);
            }
        }
    }

    public void openGitHub() {
        try {
            Intent open = new Intent();
            open.setAction(Intent.ACTION_VIEW);
            open.setData(Uri.parse(BaseApplication.GITHUB_URL));
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
            open.setData(Uri.parse(BaseApplication.WEBSITE));
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
