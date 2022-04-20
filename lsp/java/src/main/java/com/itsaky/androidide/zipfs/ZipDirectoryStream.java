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

import java.io.IOException;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.DirectoryStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author  Xueming Shen, Rajendra Gutupalli, Jaya Hangal
 */

public class ZipDirectoryStream implements DirectoryStream<Path> {

    private final ZipFileSystem zipfs;
    private final byte[] path;
    private final Filter<? super Path> filter;
    private volatile boolean isClosed;
    private volatile Iterator<Path> itr;

    ZipDirectoryStream(ZipPath zipPath,
                       Filter<? super Path> filter)
        throws IOException
    {
        this.zipfs = zipPath.getFileSystem();
        this.path = zipPath.getResolvedPath();
        this.filter = filter;
        // sanity check
        if (!zipfs.isDirectory(path))
            throw new NotDirectoryException(zipPath.toString());
    }

    @Override
    public synchronized Iterator<Path> iterator() {
        if (isClosed)
            throw new ClosedDirectoryStreamException();
        if (itr != null)
            throw new IllegalStateException("Iterator has already been returned");

        try {
            itr = zipfs.iteratorOf(path, filter);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return new Iterator<Path>() {
            private Path next;
            @Override
            public boolean hasNext() {
                if (isClosed)
                    return false;
                return itr.hasNext();
            }

            @Override
            public synchronized Path next() {
                if (isClosed)
                    throw new NoSuchElementException();
                return itr.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public synchronized void close() throws IOException {
        isClosed = true;
    }


}
