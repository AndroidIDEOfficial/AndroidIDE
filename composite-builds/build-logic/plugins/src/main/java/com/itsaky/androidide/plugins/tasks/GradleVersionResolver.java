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

/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itsaky.androidide.plugins.tasks;

import static org.gradle.api.tasks.wrapper.internal.DefaultWrapperVersionsResources.LATEST;
import static org.gradle.api.tasks.wrapper.internal.DefaultWrapperVersionsResources.NIGHTLY;
import static org.gradle.api.tasks.wrapper.internal.DefaultWrapperVersionsResources.RELEASE_CANDIDATE;
import static org.gradle.api.tasks.wrapper.internal.DefaultWrapperVersionsResources.RELEASE_NIGHTLY;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import org.gradle.api.GradleException;
import org.gradle.api.resources.TextResource;
import org.gradle.api.tasks.wrapper.internal.DefaultWrapperVersionsResources;
import org.gradle.util.GradleVersion;

class GradleVersionResolver {

  private TextResource latest;
  private TextResource releaseCandidate;
  private TextResource nightly;
  private TextResource releaseNightly;

  private GradleVersion gradleVersion;
  private String gradleVersionString = GradleVersion.current().getVersion();

  void setTextResources(TextResource latest, TextResource releaseCandidate, TextResource nightly,
      TextResource releaseNightly) {
    this.latest = latest;
    this.releaseCandidate = releaseCandidate;
    this.nightly = nightly;
    this.releaseNightly = releaseNightly;
  }

  String resolve(String version) {
    if (version == null) {
      return GradleVersion.current().getVersion();
    }
    switch (version) {
      case LATEST:
        return getVersion(latest.asString(), version);
      case NIGHTLY:
        return getVersion(nightly.asString(), version);
      case RELEASE_NIGHTLY:
        return getVersion(releaseNightly.asString(), version);
      case RELEASE_CANDIDATE:
        return getVersion(releaseCandidate.asString(), version);
      default:
        return version;
    }
  }

  static String getVersion(String json, String placeHolder) {
    Type type = new TypeToken<Map<String, String>>() {
    }.getType();
    Map<String, String> map = new Gson().fromJson(json, type);
    String version = map.get("version");
    if (version == null) {
      throw new GradleException(
          "There is currently no version information available for '" + placeHolder + "'.");
    }
    return version;
  }

  static boolean isPlaceHolder(String version) {
    return DefaultWrapperVersionsResources.PLACE_HOLDERS.contains(version);
  }

  GradleVersion getGradleVersion() {
    if (gradleVersion == null) {
      gradleVersion = GradleVersion.version(resolve(gradleVersionString));
    }
    return gradleVersion;
  }

  void setGradleVersionString(String gradleVersionString) {
    if (!isPlaceHolder(gradleVersionString)) {
      this.gradleVersion = GradleVersion.version(gradleVersionString);
    }
    if (this.gradleVersionString != gradleVersionString) {
      this.gradleVersionString = gradleVersionString;
      this.gradleVersion = null;
    }
  }
}