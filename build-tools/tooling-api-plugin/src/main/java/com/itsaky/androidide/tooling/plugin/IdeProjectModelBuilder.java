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

package com.itsaky.androidide.tooling.plugin;

import com.itsaky.androidide.tooling.api.model.IdeProject;
import com.itsaky.androidide.tooling.api.model.internal.DefaultIdeProject;

import org.gradle.api.Project;
import org.gradle.tooling.provider.model.ToolingModelBuilder;

/**
 * @author Akash Yadav
 */
public class IdeProjectModelBuilder implements ToolingModelBuilder {

    @Override
    public boolean canBuild(String s) {
        return IdeProject.class.getName().equals(s);
    }

    @Override
    public Object buildAll(String s, Project project) {
        final var ideProject = new DefaultIdeProject();
        fillProjectDetails(project, ideProject);
        return ideProject;
    }

    private void fillProjectDetails(Project project, DefaultIdeProject ideProject) {
        ideProject.setPath(project.getPath());
        ideProject.setDisplayName(project.getDisplayName());
        ideProject.setProjectDir(project.getProjectDir());
    }
}
