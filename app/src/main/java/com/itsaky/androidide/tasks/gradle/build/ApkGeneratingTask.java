/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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

package com.itsaky.androidide.tasks.gradle.build;

import static com.itsaky.androidide.models.ApkMetadata.*;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.ApkMetadata;
import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.utils.JSONUtility;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ApkGeneratingTask extends BaseGradleTask {

    /**
     * Get the directories where APKs are generated. These directories must contain an
     * output-metadata.json file
     */
    protected abstract Set<File> provideApkDirectories(File buildDir);

    protected Set<File> directories(File buildDir) {
        if (buildDir == null || !buildDir.exists() || !buildDir.isDirectory()) {
            return Collections.emptySet();
        }

        return provideApkDirectories(buildDir);
    }

    /** Get generated APKs */
    public Set<File> getApks(File buildDir) {
        final Set<File> result = new HashSet<>();
        final Set<File> dirs = directories(buildDir);
        if (dirs == null || dirs.isEmpty()) {
            return result;
        }

        for (File dir : dirs) {
            final File jsonData = new File(dir, "output-metadata.json");
            if (!jsonData.exists() || !FileUtils.isUtf8(jsonData)) {
                continue;
            }

            final String contents = FileIOUtils.readFile2String(jsonData);

            try {
                final ApkMetadata metadata = JSONUtility.gson.fromJson(contents, ApkMetadata.class);
                if (!isValid(metadata)) {
                    continue;
                }

                element_finder:
                for (Element element : metadata.getElements()) {
                    if (element == null || element.getOutputFile() == null) {
                        continue element_finder;
                    }

                    if (element.getOutputFile().endsWith(".apk")) {
                        final File apk = new File(dir, element.getOutputFile());
                        if (apk.exists() && apk.isFile()) {
                            result.add(apk);
                        }
                    }
                }

            } catch (Throwable th) {
                LOG.error(
                        StudioApp.getInstance()
                                .getString(
                                        com.itsaky.androidide.R.string
                                                .err_cannot_parse_apk_metadata),
                        th);
            }
        }
        return result;
    }

    private boolean isValid(ApkMetadata metadata) {

        // Null checks
        if (metadata == null
                || metadata.getArtifactType() == null
                || metadata.getArtifactType().getType() == null
                || metadata.getElements() == null) {
            return false;
        }

        final ArtifactType type = metadata.getArtifactType();
        final List<Element> elements = metadata.getElements();

        if (!type.getType().equals(ArtifactType.TYPE_APK)) {
            return false;
        }

        if (elements.isEmpty()) {
            return false;
        }

        boolean atLeastOneApk = false;
        for (Element element : elements) {
            if (element == null
                    || element.getOutputFile() == null
                    || !element.getOutputFile().endsWith(".apk")) {
                continue;
            }

            if (element.getOutputFile().endsWith(".apk")) {
                atLeastOneApk = true;
                break;
            }
        }

        if (atLeastOneApk) {
            return true;
        }

        return false;
    }

    private static final Logger LOG = Logger.newInstance("ApkGeneratingTask");
}
