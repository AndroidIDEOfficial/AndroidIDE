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

import com.google.firebase.messaging.FirebaseMessaging;
import com.itsaky.androidide.language.xml.completion.XMLCompletionService;
import com.itsaky.androidide.project.ProjectResourceFinder;
import com.itsaky.androidide.services.MessagingService;
import com.itsaky.inflater.ILayoutInflater;
import com.itsaky.inflater.IResourceFinder;
import com.itsaky.inflater.LayoutInflaterConfiguration;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.apiinfo.ApiInfo;
import com.itsaky.attrinfo.AttrInfo;
import com.itsaky.widgets.WidgetInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class StudioApp extends BaseApplication {
    
	private static StudioApp instance;
    private boolean stopGradleDaemon = true;
    private static ApiInfo mApiInfo;
    private static AttrInfo mAttrInfo;
    private static WidgetInfo mWidgetInfo;

    private IResourceFinder mResFinder;
    
    private XMLCompletionService mXmlCompletionService;
    private ILayoutInflater mLayoutInflater;
    
	public static final boolean DEBUG = com.itsaky.androidide.BuildConfig.DEBUG;
    
    private static final Logger LOG = Logger.instance("StudioApp");
	
	@Override
	public void onCreate() {
		this.instance = this;
		super.onCreate();
        
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
		} catch (IOException e) {
		    // ignored
        }
	}
    
    public void createInflater (LayoutInflaterConfiguration config) {
        this.mLayoutInflater = ILayoutInflater.newInstance(config);
    }

    public LayoutInflaterConfiguration createInflaterConfig(ILayoutInflater.ContextProvider contextProvider, Set<File> resDirs) {
        return new LayoutInflaterConfiguration.Builder ()
                .setAttrInfo(this.attrInfo())
                .setWidgetInfo(this.widgetInfo())
                .setResourceFinder(mResFinder == null ? mResFinder = new ProjectResourceFinder() : mResFinder)
                .setResourceDirectories(resDirs)
                .setContextProvider(contextProvider)
                .create();
    }
    
    public ILayoutInflater getLayoutInflater () {
        return mLayoutInflater;
    }

    public IResourceFinder getResFinder () {
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
        return this.mApiInfo;
    }
    
    public AttrInfo attrInfo () {
        return this.mAttrInfo;
    }

    public WidgetInfo widgetInfo () {
        return this.mWidgetInfo;
    }
	
	public boolean isXmlServiceStarted() {
		return mXmlCompletionService != null;
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
}
