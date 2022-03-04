/*
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
 */
package com.itsaky.androidide.project;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.io.File;
import java.io.FileFilter;

public class ProjectResourceTable implements IResourceTable {

    private File resDir;
    private File color;
    private File layout;

    private File[] drawables;
    private File[] mipmaps;

    private static final Logger LOG = Logger.instance("ProjectResourceFinder");

    @Override
    public File findDrawable(@NonNull String name) {
        for (File drawable : drawables) {
            final File file = findFileWithName(drawable.listFiles(), name);
            if (file != null) {
                return file;
            }
        }

        return null;
    }

    @Override
    public File findLayout(@NonNull String name) {
        return findFileWithName(this.layout.listFiles(), name);
    }

    @Override
    public String findString(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);

        if (table == null) {
            return name;
        }

        final var resource = table.findString(name);
        if (resource == null) {
            return name;
        }

        final var value = resource.getValue();
        if (value.startsWith("@string/")) {
            var ref = value.substring("@string/".length());
            if (name.equals(ref)) {
                return name;
            }
        }

        return value;
    }

    @Override
    public String findBoolean(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);

        if (table == null) {
            return name;
        }

        final var resource = table.findBoolean(name);
        if (resource == null) {
            return name;
        }

        final var val = resource.getValue();
        if (val.startsWith("@bool/")) {
            final var ref = val.substring("@bool/".length());
            if (ref.equals(name)) {
                // recursive reference
                return name;
            }
        }

        return val;
    }

    @Override
    public String findInteger(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);

        if (table == null) {
            return name;
        }

        final var resource = table.findInteger(name);
        if (resource == null) {
            return name;
        }

        final var val = resource.getValue();
        if (val.startsWith("@integer/")) {
            final var ref = val.substring("@integer/".length());
            if (ref.equals(name)) {
                // recursive reference
                return name;
            }
        }

        return val;
    }

    @Override
    public String findColor(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);

        if (table == null) {
            return name;
        }

        final var resource = table.findColor(name);
        if (resource == null) {
            return name;
        }

        final var value = resource.getValue();
        if (value.startsWith("@color/")) {
            var ref = value.substring("@color/".length());
            if (name.equals(ref)) {
                return name;
            }
        }

        return value;
    }

    @Override
    public String[] findArray(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);
        if (table == null) {
            return new String[] {name};
        }

        final var resource = table.findArray(name);
        if (resource == null) {
            return new String[] {name};
        }

        return resource.getValues();
    }

    @Override
    public String findDimension(@NonNull String name) {
        final var table = ValuesTableFactory.getTable(resDir);

        if (table == null) {
            return name;
        }

        final var resource = table.findDimension(name);
        if (resource == null) {
            return name;
        }

        final var value = resource.getValue();
        if (value.startsWith("@dimen/")) {
            var ref = value.substring("@dimen/".length());
            if (name.equals(ref)) {
                return name;
            }
        }

        return value;
    }

    @Override
    public void setInflatingFile(@NonNull File file) {
        if (file.getParentFile() == null) {
            throw new IllegalArgumentException("Invalid inflating file");
        }

        setupDirectories(
                file // layout file
                        .getParentFile() // layout dir
                        .getParentFile()); // res dir
    }

    private void setupDirectories(File resDir) {

        if (resDir == null) {
            LOG.error("Null resource directory passed to resource finder. Ignoring.");
            return;
        }

        this.resDir = resDir;
        this.color = new File(resDir, "color");
        this.layout = new File(resDir, "layout");

        final File[] drawables = resDir.listFiles(new NameStartsWith("drawable"));
        this.drawables = drawables == null ? new File[0] : drawables;

        final File[] mipmaps = resDir.listFiles(new NameStartsWith("mipmap"));
        this.mipmaps = mipmaps == null ? new File[0] : mipmaps;
    }

    private File findFileWithName(File[] files, String name) {
        if (files == null || name == null) {
            return null;
        }

        for (File file : files) {
            var simpleName = file.getName();

            if (simpleName.startsWith(name)) {
                return file;
            }
        }

        return null;
    }

    private static class NameStartsWith implements FileFilter {

        private final String name;

        public NameStartsWith(String name) {
            this.name = name;
        }

        @Override
        public boolean accept(@NonNull File file) {
            return file.getName().startsWith(name);
        }
    }
}
