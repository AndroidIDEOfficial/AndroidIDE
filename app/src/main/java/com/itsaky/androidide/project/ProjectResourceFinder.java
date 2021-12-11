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
package com.itsaky.androidide.project;

import com.itsaky.layoutinflater.IResourceFinder;
import java.io.File;
import java.io.FileFilter;

public class ProjectResourceFinder implements IResourceFinder {
    
    private File resDir;
    private File color;
    private File layout;
    private File values;
    
    private File[] drawables;
    private File[] mipmaps;
    
    @Override
    public File inflateDrawable(String name) {
        for (File drawable : drawables) {
            final File file = findFileWithName(drawable.listFiles(), name);
            if (file != null) {
                return file;
            }
        }
        return null;
    }

    @Override
    public File inflateLayout(String name) {
        return findFileWithName(this.layout.listFiles(), name);
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
    public String[] findArray(String name) {
        return null;
    }

    @Override
    public String findDimension(String name) {
        return null;
    }

    @Override
    public void setInflatingFile(File file) {
        setupDirectories(file.getParentFile().getParentFile());
    }
    
    private void setupDirectories (File resDir) {
        this.resDir = resDir;
        this.color = new File (resDir, "color");
        this.layout = new File (resDir, "layout");
        this.values = new File (resDir, "values");
        
        final File[] drawables = resDir.listFiles(new NameStartsWith ("drawable"));
        this.drawables = drawables == null ? new File [0] : drawables;
        
        final File[] mipmaps = resDir.listFiles(new NameStartsWith ("mipmap"));
        this.mipmaps = mipmaps == null ? new File [0] : mipmaps;
    }
    
    private File findFileWithName (File[] files, String name) {
        for (File file : files) {
            String simpleName = file.getName();
            if (simpleName.contains(".")) {
                simpleName = simpleName.substring(simpleName.lastIndexOf(".") + 1);
            }

            if (simpleName.equals(name)) {
                return file;
            }
        }
        
        return null;
    }
    
    private class NameStartsWith implements FileFilter {
        
        private final String name;
        
        public NameStartsWith(String name) {
            this.name = name;
        }
        
        @Override
        public boolean accept(File file) {
            return file.getName().startsWith(name);
        }
    };
}
