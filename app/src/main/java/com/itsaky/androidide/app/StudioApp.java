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

import android.content.Intent;

import com.blankj.utilcode.util.ThrowableUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itsaky.androidide.CrashHandlerActivity;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.project.ProjectResourceTable;
import com.itsaky.androidide.services.MessagingService;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.inflater.ILayoutInflater;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.LayoutInflaterConfiguration;
import com.itsaky.widgets.WidgetInfo;

import java.io.File;
import java.util.Set;

public class StudioApp extends BaseApplication {
    
	private static StudioApp instance;
    private static ApiInfo mApiInfo;
    private static AttrInfo mAttrInfo;
    private static WidgetInfo mWidgetInfo;
    
    private IResourceTable mResFinder;
    private XMLCompletionService mXmlCompletionService;
    private ILayoutInflater mLayoutInflater;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    
	public static final boolean DEBUG = com.itsaky.androidide.BuildConfig.DEBUG;
    
    private static final Logger LOG = Logger.instance("StudioApp");
	
	@Override
	public void onCreate() {
		instance = this;
        this.uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this::handleCrash);
		super.onCreate();
		
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_UPDATE);
		FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.TOPIC_DEV_MSGS);
		
        initializeApiInformation();
	}
    
    private void handleCrash(Thread thread, Throwable th) {
        writeException(th);
        
        try {
            final var intent = new Intent ();
            intent.setAction (CrashHandlerActivity.REPORT_ACTION);
            intent.putExtra (CrashHandlerActivity.TRACE_KEY, ThrowableUtils.getFullStackTrace (th));
            intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity (intent);
    
            if(this.uncaughtExceptionHandler != null) {
                this.uncaughtExceptionHandler.uncaughtException(thread, th);
            }
            System.exit (1);
        } catch (Throwable error) {
            LOG.error ("Unable to show crash handler activity", error);
        }
    }
    
    public void createInflater (LayoutInflaterConfiguration config) {
        this.mLayoutInflater = ILayoutInflater.newInstance(config);
    }

    public LayoutInflaterConfiguration createInflaterConfig(ILayoutInflater.ContextProvider contextProvider, Set<File> resDirs) {
        return new LayoutInflaterConfiguration.Builder ()
                .setAttrInfo(this.attrInfo())
                .setWidgetInfo(this.widgetInfo())
                .setResourceFinder(mResFinder == null ? mResFinder = new ProjectResourceTable () : mResFinder)
                .setResourceDirectories(resDirs)
                .setContextProvider(contextProvider)
                .create();
    }
    
    public ILayoutInflater getLayoutInflater () {
        return mLayoutInflater;
    }

    public IResourceTable getResFinder () {
	    return this.mResFinder;
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
                
                mXmlCompletionService = new XMLCompletionService (mAttrInfo, mWidgetInfo);
            } catch (Throwable th) {
                LOG.error (getString (com.itsaky.androidide.R.string.err_init_sdkinfo), th);
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
        return mApiInfo;
    }
    
    public AttrInfo attrInfo () {
        return mAttrInfo;
    }

    public WidgetInfo widgetInfo () {
        return mWidgetInfo;
    }
	
	public boolean isXmlServiceStarted() {
		return mXmlCompletionService != null;
	}
    
    public void setStopGradleDaemon(boolean startGradleDaemon) {
    }
    
    public static StudioApp getInstance() {
		return instance;
	}
}
