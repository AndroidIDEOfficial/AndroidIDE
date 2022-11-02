/*
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
 */
package com.itsaky.androidide.app;

import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.BuildConfig;
import com.itsaky.androidide.CrashHandlerActivity;
import com.itsaky.androidide.events.AppEventsIndex;
import com.itsaky.androidide.events.LspApiEventsIndex;
import com.itsaky.androidide.events.LspJavaEventsIndex;
import com.itsaky.androidide.events.ProjectsApiEventsIndex;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.toaster.ToastUtilsKt;
import com.itsaky.toaster.Toaster;

import org.greenrobot.eventbus.EventBus;

public class IDEApplication extends BaseApplication {

  private static final ILogger LOG = ILogger.newInstance("IDEApplication");
  private static IDEApplication instance;
  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  public static IDEApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    instance = this;
    this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this::handleCrash);
    super.onCreate();

    EventBus.builder()
        .addIndex(new AppEventsIndex())
        .addIndex(new ProjectsApiEventsIndex())
        .addIndex(new LspApiEventsIndex())
        .addIndex(new LspJavaEventsIndex())
        .installDefaultEventBus();
  }

  private void handleCrash(Thread thread, Throwable th) {
    writeException(th);

    try {
      final var intent = new Intent();
      intent.setAction(CrashHandlerActivity.REPORT_ACTION);
      intent.putExtra(CrashHandlerActivity.TRACE_KEY, ThrowableUtils.getFullStackTrace(th));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);

      if (this.uncaughtExceptionHandler != null) {
        this.uncaughtExceptionHandler.uncaughtException(thread, th);
      }
      System.exit(1);
    } catch (Throwable error) {
      LOG.error("Unable to show crash handler activity", error);
    }
  }

  public void showChangelog() {
    final var intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(
        Uri.parse(
            BaseApplication.GITHUB_URL.concat("/releases/tag/v").concat(BuildConfig.VERSION_NAME)));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      startActivity(intent);
    } catch (Throwable th) {
      LOG.error("Unable to start activity to show changelog", th);
      ToastUtilsKt.toast("Unable to start activity", Toaster.Type.ERROR);
    }
  }
}
