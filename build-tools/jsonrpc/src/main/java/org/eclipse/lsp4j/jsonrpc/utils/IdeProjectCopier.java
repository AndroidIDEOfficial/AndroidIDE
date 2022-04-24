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

package org.eclipse.lsp4j.jsonrpc.utils;

import com.itsaky.androidide.tooling.api.model.IdeProject;
import com.itsaky.androidide.tooling.api.model.internal.DefaultIdeProject;

/**
 * Copies an {@link IdeProject}.
 *
 * <p><b>Why do we need this class? <br>
 * </b>The actual {@link IdeProject} instance is created in the <b>AndroidIDEToolingApiPlugin</b>
 * class, which is NOT executed in the same JVM instance as the JSONRpc classes. As a result, the
 * object we get from the plugin is a {@link java.lang.reflect.Proxy Proxy}. Gson is not capable of
 * serializing proxy classes. So, before writing messages to the remote endpoint, we first check if
 * the object we are writing is a proxy or not. If it is, then the data being written to
 * the remote endpoint is definitely an {@link IdeProject}. So we use this class to copy the data
 * stored in that proxy object to create a new instance of the project object.
 *
 * <p><b>Why we cannot create a <code>copy()</code> function in {@link IdeProject} class itself?</b><br>
 * I tried to do the same and found that the new project object created is also a proxy.
 *
 * @author Akash Yadav
 */
public class IdeProjectCopier {

    public static IdeProject copy(IdeProject project) {
        final var copy = new DefaultIdeProject();
        copy.setPath(project.getPath());
        copy.setProjectDir(project.getProjectDir());
        copy.setDisplayName(project.getDisplayName());
        return copy;
    }
}
