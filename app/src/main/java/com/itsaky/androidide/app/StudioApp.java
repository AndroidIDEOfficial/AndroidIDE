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

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.CrashHandlerActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.projects.ProjectResourceTable;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.inflater.ILayoutInflater;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.LayoutInflaterConfiguration;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.java.JavaLanguageServer;
import com.itsaky.lsp.xml.XMLLanguageServer;
import com.itsaky.sdk.SDKInfo;
import com.itsaky.widgets.WidgetInfo;

import java.io.File;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class StudioApp extends BaseApplication {

  private static final ILogger LOG = ILogger.newInstance("StudioApp");
  private static StudioApp instance;
  private static SDKInfo sdkInfo;
  private final ILanguageServer mJavaLanguageServer = new JavaLanguageServer();
  private final ILanguageServer mXMLLanguageServer = new XMLLanguageServer();
  private IResourceTable mResTable;
  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  public static StudioApp getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    instance = this;
    this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this::handleCrash);
    super.onCreate();

    getApiInformation();
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

  /** Reads API version information from api-versions.xml */
  public CompletableFuture<SDKInfo> getApiInformation() {
    return CompletableFuture.supplyAsync(
        () -> {
          if (sdkInfo == null) {
            try {
              sdkInfo = new SDKInfo(StudioApp.this);
              ((XMLLanguageServer) mXMLLanguageServer).setupSDK(sdkInfo);
            } catch (Throwable th) {
              LOG.error(getString(R.string.err_init_sdkinfo), th);
            }
          }

          return sdkInfo;
        });
  }

  @NonNull
  public ILanguageServer getJavaLanguageServer() {
    return mJavaLanguageServer;
  }

  @NonNull
  public ILanguageServer getXMLLanguageServer() {
    return mXMLLanguageServer;
  }

  public CompletableFuture<LayoutInflaterConfiguration> createInflaterConfig(
      ILayoutInflater.ContextProvider contextProvider, Set<File> resDirs) {

    return CompletableFuture.supplyAsync(
        () -> {
          try {
            final var sdkInfo = getApiInformation().get();
            return new LayoutInflaterConfiguration.Builder()
                .setAttrInfo(sdkInfo.getAttrInfo())
                .setWidgetInfo(sdkInfo.getWidgetInfo())
                .setResourceFinder(getResourceTable())
                .setResourceDirectories(resDirs)
                .setContextProvider(contextProvider)
                .create();
          } catch (Throwable e) {
            throw new CompletionException(e);
          }
        });
  }

  public IResourceTable getResourceTable() {
    return mResTable == null ? mResTable = new ProjectResourceTable() : mResTable;
  }

  public ApiInfo apiInfo() {
    return sdkInfo.getApiInfo();
  }

  public AttrInfo attrInfo() {
    return sdkInfo.getAttrInfo();
  }

  public WidgetInfo widgetInfo() {
    return sdkInfo.getWidgetInfo();
  }
}
