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
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */



package com.itsaky.androidide.zipfs;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Formatter;

/**
 *
 * @author  Xueming Shen, Rajendra Gutupalli,Jaya Hangal
 */

public class ZipFileAttributes implements BasicFileAttributes

{
    private final ZipFileSystem.Entry e;

    ZipFileAttributes(ZipFileSystem.Entry e) {
        this.e = e;
    }

    ///////// basic attributes ///////////
    @Override
    public FileTime creationTime() {
        if (e.ctime != -1)
            return FileTime.fromMillis(e.ctime);
        return null;
    }

    @Override
    public boolean isDirectory() {
        return e.isDir();
    }

    @Override
    public boolean isOther() {
        return false;
    }

    @Override
    public boolean isRegularFile() {
        return !e.isDir();
    }

    @Override
    public FileTime lastAccessTime() {
        if (e.atime != -1)
            return FileTime.fromMillis(e.atime);
        return null;
    }

    @Override
    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(e.mtime);
    }

    @Override
    public long size() {
        return e.size;
    }

    @Override
    public boolean isSymbolicLink() {
        return false;
    }

    @Override
    public Object fileKey() {
        return null;
    }

    ///////// zip entry attributes ///////////
    public long compressedSize() {
        return e.csize;
    }

    public long crc() {
        return e.crc;
    }

    public int method() {
        return e.method;
    }

    public byte[] extra() {
        if (e.extra != null)
            return Arrays.copyOf(e.extra, e.extra.length);
        return null;
    }

    public byte[] comment() {
        if (e.comment != null)
            return Arrays.copyOf(e.comment, e.comment.length);
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        Formatter fm = new Formatter(sb);
        if (creationTime() != null)
            fm.format("    creationTime    : %tc%n", creationTime().toMillis());
        else
            fm.format("    creationTime    : null%n");

        if (lastAccessTime() != null)
            fm.format("    lastAccessTime  : %tc%n", lastAccessTime().toMillis());
        else
            fm.format("    lastAccessTime  : null%n");
        fm.format("    lastModifiedTime: %tc%n", lastModifiedTime().toMillis());
        fm.format("    isRegularFile   : %b%n", isRegularFile());
        fm.format("    isDirectory     : %b%n", isDirectory());
        fm.format("    isSymbolicLink  : %b%n", isSymbolicLink());
        fm.format("    isOther         : %b%n", isOther());
        fm.format("    fileKey         : %s%n", fileKey());
        fm.format("    size            : %d%n", size());
        fm.format("    compressedSize  : %d%n", compressedSize());
        fm.format("    crc             : %x%n", crc());
        fm.format("    method          : %d%n", method());
        fm.close();
        return sb.toString();
    }
}
