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
package com.itsaky.androidide.handlers;

import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.project.IDEProject;
import com.itsaky.androidide.utils.Logger;

/**
 * A handler is an implementation that handles a specific feature, data or anything else in
 * AndroidIDE
 */
public abstract class IDEHandler {

    protected static final Logger LOG = Logger.instance();

    protected Provider provider;

    public IDEHandler(Provider provider) {
        this.provider = provider;
    }

    protected void throwNPE() {
        throw new NullPointerException();
    }

    protected EditorActivity activity() {
        return provider.provideEditorActivity();
    }

    protected AndroidProject androidProject() {
        return provider.provideAndroidProject();
    }

    protected IDEProject ideProject() {
        return provider.provideIDEProject();
    }

    public abstract void start();

    public abstract void stop();

    /** An interface to communicate between a handler and its client */
    public interface Provider {

        /**
         * Called by handler to get a reference to {@link EditorActivity}
         *
         * @throws NullPointerException is this is required
         */
        EditorActivity provideEditorActivity();

        /**
         * Called by handler to get a reference of the current {@link AndroidProject}
         *
         * @throws NullPointerException is this is required
         */
        AndroidProject provideAndroidProject();

        /**
         * Called by handler to get a reference of the current {@link IDEProject}
         *
         * @throws NullPointerException is this is required
         */
        IDEProject provideIDEProject();
    }
}
