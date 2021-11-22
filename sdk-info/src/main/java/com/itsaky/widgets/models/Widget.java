/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
package com.itsaky.widgets.models;

public class Widget implements Comparable {
    
    public String name;
    public String simpleName;
    public boolean isViewGroup;

    public Widget(String name, String simpleName, boolean isViewGroup) {
        this.name = name;
        this.simpleName = simpleName;
        this.isViewGroup = isViewGroup;
    }

    @Override
    public int compareTo(Object p1) {
        if(p1 instanceof Widget) {
            Widget that = (Widget) p1;
            return this.simpleName.compareTo(that.simpleName);
        }
        return -1;
    }
    
}
