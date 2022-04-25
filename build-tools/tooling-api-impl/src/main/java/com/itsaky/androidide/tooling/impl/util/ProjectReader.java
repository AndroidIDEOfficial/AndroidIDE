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

package com.itsaky.androidide.tooling.impl.util;

import static com.itsaky.androidide.utils.ILogger.newInstance;

import com.android.builder.model.v2.models.AndroidProject;
import com.itsaky.androidide.tooling.api.model.IAndroidProject;
import com.itsaky.androidide.tooling.api.model.internal.DefaultIdeProject;
import com.itsaky.androidide.utils.ILogger;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;

/**
 * @author Akash Yadav
 */
public class ProjectReader {

    private static final ILogger LOG = newInstance("test");

    public static IAndroidProject read(ProjectConnection connection) {
        final var watch = new StopWatch("Read eclipse project from connection");
        final var gradle = connection.getModel(GradleProject.class);
        
        LOG.debug(System.getenv());
        LOG.debug(System.getProperties());
        for (var sub : gradle.getChildren()) {
            var subConnection =
                    GradleConnector.newConnector()
                            .forProjectDirectory(sub.getProjectDirectory())
                            .connect();
            final var modelBuilder =
                    subConnection
                            .model(AndroidProject.class)
                            .withArguments("-Pandroid.injected.build.model.v2=true");
            final var project = modelBuilder.get();
            LOG.debug(project);
        }

        watch.log();

        //        return read(project);
        return null;
    }

    public static IAndroidProject read(AndroidProject android) {
        final var project = new DefaultIdeProject();
        LOG.debug(android);
        return project;
    }
}
