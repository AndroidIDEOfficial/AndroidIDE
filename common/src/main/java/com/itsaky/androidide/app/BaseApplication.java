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
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.buildinfo.BuildInfo;
import com.itsaky.androidide.common.R;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.managers.ToolsManager;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.FlashbarUtilsKt;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.VMUtils;
import java.io.File;

public class BaseApplication extends Application {

  public static final String NOTIFICATION_GRADLE_BUILD_SERVICE = "17571";
  public static final String TELEGRAM_GROUP_URL = "https://t.me/androidide_discussions";
  public static final String TELEGRAM_CHANNEL_URL = "https://t.me/AndroidIDEOfficial";
  public static final String SPONSOR_URL = BuildInfo.PROJECT_SITE + "/donate";
  public static final String DOCS_URL = "https://docs.androidide.com";
  public static final String CONTRIBUTOR_GUIDE_URL =
      BuildInfo.REPO_URL + "/blob/dev/CONTRIBUTING.md";
  public static final String EMAIL = "contact@androidide.com";
  private static BaseApplication instance;
  private PreferenceManager mPrefsManager;

  public static BaseApplication getBaseInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    instance = this;
    Environment.init(this);
    super.onCreate();

    mPrefsManager = new PreferenceManager(this);
    JavaCharacter.initMap();

    if (!VMUtils.isJvm()) {
      ToolsManager.init(this, null);
    }
  }

  public void writeException(Throwable th) {
    FileUtil.writeFile(new File(FileUtil.getExternalStorageDir(), "idelog.txt").getAbsolutePath(),
        ThrowableUtils.getFullStackTrace(th));
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

  public void openDonationsPage() {
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
      } else if (th instanceof ActivityNotFoundException) {
        FlashbarUtilsKt.flashError(R.string.msg_app_unavailable_for_intent);
      } else {
        FlashbarUtilsKt.flashError(th.getMessage());
      }
    }
  }
}
