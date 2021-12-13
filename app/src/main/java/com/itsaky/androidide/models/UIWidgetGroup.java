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
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class for holding data about a group that will be displayed
 * in Widgets list in UI Designer screen.
 *
 * A UIWidgetGroup contains multiple {@link UIWidget}.
 *
 * @author Akash Yadav
 */
public class UIWidgetGroup implements IconTextListItem {

    /**
     * Name of this group.
     */
    private final String name;

    /**
     * Children of this group.
     */
    private final List<UIWidget> children;

    /**
     * Is this group currently selected in the group list?
     */
    private boolean selected;

    public UIWidgetGroup(String name) {
        this.name = name;
        this.selected = false;
        this.children = new ArrayList<>();
    }

    /**
     * Get the name of this group.
     * @return The name of this group.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the children of this group.
     * @return The children.
     */
    @NonNull
    public List<UIWidget> getChildren() {
        return children;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    /**
     * Add this widget to this group. Does nothing if the child is already present.
     * @param child The child to add.
     * @return This instance for chained method calls.
     */
    public UIWidgetGroup addChild (UIWidget child) {
        if (children.contains(child)) {
            return  this;
        }
        this.children.add(child);
        return this;
    }

    /**
     * Get the child at the given index.
     * @param index The index of the child.
     * @return The child at the index.
     * @throws IndexOutOfBoundsException If the given index is not valid.
     */
    public UIWidget getChild (int index ){
        return children.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIWidgetGroup group = (UIWidgetGroup) o;
        return isSelected() == group.isSelected() && getName().equals(group.getName()) && getChildren().equals(group.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getChildren(), isSelected());
    }

    @Override
    public String getText() {
        return getName();
    }

    @Override
    public int getIconResource() {
        return -1;
    }
}
