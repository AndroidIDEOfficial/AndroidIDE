/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.buildinfo.BuildInfo;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.FlashbarUtilsKt;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.VMUtils;
import java.io.File;
import java.util.Arrays;
import kotlin.collections.ArraysKt;

public class BaseApplication extends Application {

  public static final String NOTIFICATION_GRADLE_BUILD_SERVICE = "17571";
  public static final String TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions";
  public static final String TELEGRAM_CHANNEL_URL = "https://t.me/AndroidIDEOfficial";
  public static final String SPONSOR_URL = BuildInfo.PROJECT_SITE + "/donate";
  public static final String DOCS_URL = BuildInfo.PROJECT_SITE + "/docs";
  public static final String EMAIL = "contact@androidide.com";
  private static final String AARCH64 = "arm64-v8a";
  private static final String ARM = "armeabi-v7a";
  private static BaseApplication instance;
  private PreferenceManager mPrefsManager;

  public static BaseApplication getBaseInstance() {
    return instance;
  }

  public static boolean isAbiSupported() {
    return Arrays.asList(Build.SUPPORTED_ABIS).contains(getArch());
  }

  public static boolean isAarch64() {
    return ArraysKt.contains(Build.SUPPORTED_64_BIT_ABIS, AARCH64);
  }

  public static boolean isArmv7a() {
    return ArraysKt.contains(Build.SUPPORTED_32_BIT_ABIS, ARM);
  }

  @Nullable
  public static String getArch() {
    if (isAarch64()) {
      return AARCH64;
    } else if (isArmv7a()) {
      return ARM;
    }
    return null;
  }

  @Override
  public void onCreate() {
    instance = this;
    Environment.init();
    super.onCreate();

    mPrefsManager = new PreferenceManager(this);
    JavaCharacter.initMap();

    if (!VMUtils.isJvm()) {
      ToolsManager.init(this, null);
    }

    createNotificationChannels();
  }

  private void createNotificationChannels() {
    NotificationChannel buildNotificationChannel = new NotificationChannel(
        NOTIFICATION_GRADLE_BUILD_SERVICE,
        getString(R.string.title_gradle_service_notification_channel),
        NotificationManager.IMPORTANCE_LOW);
    NotificationManagerCompat.from(this).createNotificationChannel(buildNotificationChannel);
  }

  public void writeException(Throwable th) {
    FileUtil.writeFile(new File(FileUtil.getExternalStorageDir(), "idelog.txt").getAbsolutePath(),
        ThrowableUtils.getFullStackTrace(th));
  }

  public final File getTempProjectDir() {
    return Environment.mkdirIfNotExits(new File(Environment.TMP_DIR, "tempProject"));
  }

  public PreferenceManager getPrefManager() {
    return mPrefsManager;
  }

  public File getProjectsDir() {
    return Environment.PROJECTS_DIR;
  }

  public void openTelegramGroup() {
    openTelegram(BaseApplication.TELEGRAM_GROUP_URL);
  }

  public void openTelegramChannel() {
    openTelegram(BaseApplication.TELEGRAM_CHANNEL_URL);
  }

  public void openGitHub() {
    openUrl(BuildInfo.REPO_URL);
  }

  public void openWebsite() {
    openUrl(BuildInfo.PROJECT_SITE);
  }

  public void openSponsors() {
    openUrl(SPONSOR_URL);
  }

  public void openDocs() {
    openUrl(DOCS_URL);
  }

  public void emailUs() {
    openUrl("mailto:" + EMAIL);
  }

  public void openUrl(String url) {
    openUrl(url, null);
  }

  public void openTelegram(String url) {
    openUrl(url, "org.telegram.messenger");
  }

  public void openUrl(String url, String pkg) {
    try {
      Intent open = new Intent();
      open.setAction(Intent.ACTION_VIEW);
      open.setData(Uri.parse(url));
      open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (pkg != null) {
        open.setPackage(pkg);
      }
      startActivity(open);
    } catch (Throwable th) {
      if (pkg != null) {
        openUrl(url);
      } else {
        FlashbarUtilsKt.flashError(th.getMessage());
      }
    }
  }
}
