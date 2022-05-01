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
package com.itsaky.androidide.projects;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a module (subproject) of a root project.
 *
 * <p>In Android projects, a module can either be an application project, or a library project.
 */
public class IDEModule extends IDEProject {

    /** Build tools version of this project */
    @SerializedName("buildToolsVersion")
    public String buildToolsVersion;

    /**
     * Compile SDK version of this project
     *
     * <p>Representation: 'android-31', 'android-30', 'android-29', etc
     */
    @SerializedName("compileSdkVersion")
    public String compileSdkVersion;

    /** Minimum SDK version of this project */
    @SerializedName("minSdk")
    public SDK minSdk;

    /** Target SDK version of this project */
    @SerializedName("targetSdk")
    public SDK targetSdk;

    /** Version code of this project */
    @SerializedName("versionCode")
    public int versionCode = 0;

    /** Version name of this project */
    @SerializedName("versionName")
    public String versionName = "Not defined";

    /** Is this project an Android library project? */
    @SerializedName("isLibrary")
    public boolean isLibrary = true;

    /** Does this module has viewBinding enabled? */
    @SerializedName("viewBindingEnabled")
    public boolean viewBindingEnabled;

    /** Library projects don't have applicationId, application projects do. */
    @SerializedName("applicationId")
    public String applicationId;
}
