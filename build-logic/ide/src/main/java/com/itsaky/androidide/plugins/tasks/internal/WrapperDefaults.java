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

package com.itsaky.androidide.plugins.tasks.internal;

import org.gradle.api.NonNullApi;
import org.gradle.api.tasks.wrapper.Wrapper;
import org.gradle.wrapper.Install;

@NonNullApi
public class WrapperDefaults {
  public static final String SCRIPT_PATH = "gradlew";
  public static final String JAR_FILE_PATH = "gradle/wrapper/gradle-wrapper.jar";
  public static final Wrapper.DistributionType DISTRIBUTION_TYPE = Wrapper.DistributionType.BIN;

  public static final String DISTRIBUTION_PATH = Install.DEFAULT_DISTRIBUTION_PATH;
  public static final Wrapper.PathBase DISTRIBUTION_BASE = Wrapper.PathBase.GRADLE_USER_HOME;
  public static final String ARCHIVE_PATH = DISTRIBUTION_PATH;
  public static final Wrapper.PathBase ARCHIVE_BASE = DISTRIBUTION_BASE;

  public static final int NETWORK_TIMEOUT = 10000;
  public static final boolean VALIDATE_DISTRIBUTION_URL = true;
}