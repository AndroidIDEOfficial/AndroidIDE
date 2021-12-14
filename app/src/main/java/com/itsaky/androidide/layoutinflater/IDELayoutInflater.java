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

package com.itsaky.androidide.layoutinflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.itsaky.layoutinflater.LayoutInflaterConfiguration;
import com.itsaky.layoutinflater.XMLLayoutInflater;

import java.lang.reflect.InvocationTargetException;

/**
 * A layout inflater for inflating views from XML.
 * This implementation handles IDE-specific stuff. Like, setting drag listeners to view groups.
 *
 * @author Akash Yadav
 */
public class IDELayoutInflater extends XMLLayoutInflater {

    public IDELayoutInflater(LayoutInflaterConfiguration config) {
        super(config);
    }

    @NonNull
    @Override
    protected View createAndroidViewForName(String name) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        final var view = super.createAndroidViewForName(name);
        if (view instanceof ViewGroup) {
            final var group = (ViewGroup) view;
            group.setOnDragListener(new WidgetDragListener(group.getContext()));
        }

        return view;
    }
}
