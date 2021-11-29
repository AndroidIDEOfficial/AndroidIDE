/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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


package com.itsaky.androidide.project;

import com.itsaky.androidide.ui.inflater.IResourceFinder;
import java.io.File;

// TODO Implement finder
public class ProjectResourceFinder implements IResourceFinder {
    
    @Override
    public File findDrawable(String name) {
        return null;
    }

    @Override
    public File findLayout(String name) {
        return null;
    }

    @Override
    public String findString(String name) {
        return null;
    }

    @Override
    public String findColor(String name) {
        return null;
    }

    @Override
    public Object[] findArray(String name) {
        return null;
    }

    @Override
    public String findDimension(String name) {
        return null;
    }
}
