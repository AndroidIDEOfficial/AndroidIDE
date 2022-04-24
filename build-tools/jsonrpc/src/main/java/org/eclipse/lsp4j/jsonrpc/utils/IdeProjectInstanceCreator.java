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

import com.google.gson.InstanceCreator;
import com.itsaky.androidide.tooling.api.model.IdeProject;
import com.itsaky.androidide.tooling.api.model.internal.DefaultIdeProject;

import java.lang.reflect.Type;

/**
 * As {@link IdeProject} is an interface, we need to implement an {@link InstanceCreator}.
 *
 * @author Akash Yadav
 */
public class IdeProjectInstanceCreator implements InstanceCreator<IdeProject> {

    @Override
    public IdeProject createInstance(Type type) {
        return new DefaultIdeProject();
    }
}
