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
package com.itsaky.androidide.managers;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ZipUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;

public class ToolsManager {
    
    private static PreferenceManager prefs;
    
    public static final int LOG_SENDER_VERSION = 2;
    
    public static final String KEY_LOG_SENDER_VERSION = "tools_logsenderVersion";
    
    public static String ARCH_SPECIFIC_ASSET_DATA_DIR = "data/" + BaseApplication.getArch();
    public static String COMMON_ASSET_DATA_DIR = "data/common";
    
    public static void init(@NonNull BaseApplication app, Runnable onFinish) {
        prefs = app.getPrefManager();
        
        copyBusyboxIfNeeded();
        extractLogsenderIfNeeded();
        extractLibHooks();
        writeInitScript();
        rewriteProjectData();
        
        if(onFinish != null) {
            onFinish.run();
        }
    }

    public static void extractLibHooks() {
        if(!Environment.LIB_HOOK.exists()) {
            ResourceUtils.copyFileFromAssets(getArchSpecificAsset("libhook.so"), Environment.LIB_HOOK.getAbsolutePath());
        }
        
        if(!Environment.LIB_HOOK2.exists()) {
            ResourceUtils.copyFileFromAssets(getArchSpecificAsset("libhook2.so"), Environment.LIB_HOOK2.getAbsolutePath());
        }
    }

    private static void rewriteProjectData() {
        FileIOUtils.writeFileFromString(Environment.PROJECT_DATA_FILE, "/**********************/");
    }

    private static void copyBusyboxIfNeeded() {
        File exec = Environment.BUSYBOX;
        if(exec.exists()) return;
        Environment.mkdirIfNotExits(exec.getParentFile());
        ResourceUtils.copyFileFromAssets(getArchSpecificAsset("busybox"), exec.getAbsolutePath());
        if(!exec.canExecute()) {
            if (!exec.setExecutable(true)) {
                LOG.error ("Cannot set busybox executable permissions.");
            }
        }
    }
    
    private static void writeInitScript() {
        FileIOUtils.writeFileFromString(Environment.INIT_SCRIPT, readInitScript());
    }

    @NonNull
    private static String readInitScript() {
        return ResourceUtils.readAssets2String(getCommonAsset("androidide.init.gradle"));
    }
    
    private static void extractLogsenderIfNeeded() {
        try {
            final boolean isOld = LOG_SENDER_VERSION > prefs.getInt(KEY_LOG_SENDER_VERSION, 0);
            if(isOld) {
                final File logsenderZip = new File(Environment.JLS_HOME, "logsender.zip");
                ResourceUtils.copyFileFromAssets(getCommonAsset("logsender.zip"), logsenderZip.getAbsolutePath());
                ZipUtils.unzipFile(logsenderZip, Environment.HOME);
                prefs.putInt(KEY_LOG_SENDER_VERSION, LOG_SENDER_VERSION);
            }
        } catch (IOException e) {
            LOG.error("Error extracting log sender", e);
        }
    }
    
    @NonNull
    @Contract(pure = true)
    public static String getArchSpecificAsset (String name) {
        return ARCH_SPECIFIC_ASSET_DATA_DIR + "/" + name;
    }
    
    @NonNull
    @Contract(pure = true)
    public static String getCommonAsset (String name) {
        return COMMON_ASSET_DATA_DIR + "/" + name;
    }
    
    private static final Logger LOG = Logger.instance("ToolsManager");
}
