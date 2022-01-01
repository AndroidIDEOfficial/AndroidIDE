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

package com.itsaky.androidide.ui;

import com.itsaky.androidide.models.UIWidget;
import com.itsaky.inflater.IView;

/**
 * Store data about a draggable widget. Used in UI Designer.
 *
 * @author Akash Yadav
 */
public class WidgetDragData {

    /**
     * Set this to true if the view that is being dragged was already inflated in the layout.
     * If this is set to {@code true}, {@link #alreadyInflatedView} must NOT be {@code null}.
     *
     * If this is {@code false}, then {@link #newDragData} must be not be null.
     */
    public boolean isAlreadyInflated;

    /**
     * The view that was already inflated and is being relocated. This must not be null if {@link #isAlreadyInflated}
     * is {@code true}.
     */
    public IView alreadyInflatedView;

    /**
     * The UIWidget that is being dragged.
     */
    public UIWidget newDragData;

    private WidgetDragData () {}

    public WidgetDragData(boolean isAlreadyInflated, IView alreadyInflatedView, UIWidget newDragData) {
        this.isAlreadyInflated = isAlreadyInflated;
        this.alreadyInflatedView = alreadyInflatedView;
        this.newDragData = newDragData;
    }
}
