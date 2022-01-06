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

import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.project.AndroidProject;

import org.eclipse.lsp4j.Range;

import java.io.File;

/**
 * Represents a file that has been opened in the editor,
 *
 * @author Akash Yadav
 */
public class OpenedFile {
    
    private final File file;
    private final Range selection;
    
    public OpenedFile (File file, Range selection) {
        this.file = file;
        this.selection = selection;
    }
    
    public File getFile () {
        return file;
    }
    
    public Range getSelection () {
        return selection;
    }
    
    public int hashCode () {
        return this.file.getAbsolutePath ().hashCode ();
    }
    
    public EditorFragment createFragment (final EditorFragment.FileOpenListener fileOpenListener,
                                          EditorFragment.ModificationStateListener modificationStateListener) {
        return EditorFragment.newInstance (this.file, this.selection)
                .setModificationStateListener (modificationStateListener)
                .setFileOpenListener (fileOpenListener);
    }
}
