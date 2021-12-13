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

package com.itsaky.androidide.models;

import androidx.annotation.DrawableRes;

import java.util.Objects;

/**
 * Holds data about the widget item in widgets list shown in
 * UI Designer screen.
 *
 * @author Akash Yadav
 */
public class UIWidget implements IconTextListItem {

    private final String name;
    private final int icon;

    private final String clazz;

    public UIWidget(String name, @DrawableRes int icon, Class<?> clazz) {
        this.name = name;
        this.icon = icon;
        this.clazz = clazz.getName();
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIWidget widget = (UIWidget) o;
        return getIcon() == widget.getIcon() && getName().equals(widget.getName()) && clazz.equals(widget.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIcon(), clazz);
    }

    /**
     * Get the class of this widget.
     * @return The class that this widget represents
     * @throws ClassNotFoundException If there is no class with this widget's class name.
     *          However, this will never be thrown.
     */
    public Class<?> asClass () throws ClassNotFoundException {
        return getClass().getClassLoader().loadClass(this.clazz);
    }

    public String getWidgetClassName () {
        return this.clazz;
    }

    @Override
    public String getText() {
        return getName();
    }

    @Override
    public int getIconResource() {
        return getIcon();
    }
}
