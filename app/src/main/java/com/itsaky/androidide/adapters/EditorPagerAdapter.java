/*
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
 */

package com.itsaky.androidide.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.models.SaveResult;
import com.itsaky.androidide.utils.Logger;

import org.eclipse.lsp4j.Range;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditorPagerAdapter extends FragmentStateAdapter implements EditorFragment.ModificationStateListener {
    
    private final AndroidProject project;
    private final List<OpenedFile> mFiles;
    
    private static final Logger LOG = Logger.instance ("EditorPagerAdapter");
    
    public EditorPagerAdapter (FragmentActivity activity, AndroidProject project) {
        super (activity);
        this.project = project;
        this.mFiles = new ArrayList<> ();
    }
    
    public int findIndexOfEditorByFile (File file) {
        if (file == null) {
            return -1;
        }
        
        for (int i = 0; i < this.mFiles.size (); i++) {
            final var opened = mFiles.get (i);
            if (opened.file.equals (file)) {
                return i;
            }
        }
        
        return -1;
    }
    
    public EditorFragment findEditorByFile (File file) {
        int index = findIndexOfEditorByFile (file);
		if (index != -1) {
			return getFrag (index);
		}
        return null;
    }
    
    public int openFile (File file, Range selection, EditorFragment.FileOpenListener listener) {
        int openedFileIndex = -1;
        for (int i = 0; i < mFiles.size (); i++) {
            final var f = mFiles.get (i);
            if (f.file.getAbsolutePath ().equals (file.getAbsolutePath ())) {
                openedFileIndex = i;
                break;
            }
        }
	
		if (openedFileIndex == -1) {
			final var position = mFiles.size ();
			final var fragment = EditorFragment
					.newInstance (file, project, selection)
					.setFileOpenListener (listener)
					.setModificationStateListener (this);
			
			mFiles.add (new OpenedFile (file, fragment));
			notifyDataSetChanged ();
			return position;
		} else {
			return openedFileIndex;
		}
    }
    
    /**
     * Close the editor that have opened the given file.
     * Does nothing if the file is not opened.
     *
     * @param file The file to close.
     * @return The index of the editor that was closed. -1 if no files were closed.
     */
    public int closeFile (File file) {
    	final var index = findIndexOfEditorByFile (file);
    	if (index == -1) {
    	    LOG.error ("File is not opened. File: " + file);
    		return -1;
		}
    	
    	closeFileAt (index);
    	return index;
	}
    
    /**
     * Close the file at the given index. Does nothing if the index is invalid.
     * @param index The index of file to close.
     */
	public void closeFileAt (int index) {
		if (index < 0 || index >= getItemCount ()) {
			LOG.error ("Invalid file index. Cannot close any files");
			return;
		}
		
		// Save the file before closing
		var opened = mFiles.get (index);
		opened.fragment.save ();
		opened.fragment.getEditor ().close (); // Send 'textDocument/didClose' to language servers.
        opened = null;
        
        mFiles.remove (index);
        notifyDataSetChanged ();
	}
    
    /**
     * Close all the opened files.
     * @see #closeFileAt(int)
     */
	public void closeAllFiles () {
	    // Close all files one by one
        // This will make sure that we save all the files and send 'textDocument/didClose' to language servers.
        for (int i = 0; i < getItemCount (); i++) {
            closeFileAt (i);
        }
    }
    
    /**
     * Close all the files except the file at the given index.
     * @param index The index of the file that should not be closed.
     */
    public void closeOthers (int index) {
        for (int i = 0; i < getItemCount (); i++) {
            if (i != index) {
                closeFileAt (i);
            }
        }
    }
	
	public SaveResult saveAll () {
        SaveResult result = new SaveResult ();
        for (int i = 0; i < mFiles.size (); i++) {
            save (i, result);
        }
        
        return result;
    }
    
    public void save (int index, SaveResult result) {
        if (index >= 0 && index < mFiles.size ()) {
            EditorFragment frag = getFrag (index);
            if (frag == null || frag.getFile () == null) {
                return;
            }
            // Must be called before frag.save()
            // Otherwise, it'll always return false
            final boolean modified = frag.isModified ();
            
            frag.save ();
            
            final boolean isGradle = frag.getFile ().getName ().endsWith (EditorFragment.EXT_GRADLE);
            final boolean isXml = frag.getFile ().getName ().endsWith (EditorFragment.EXT_XML);
            
            if (!result.gradleSaved) {
                result.gradleSaved = modified && isGradle;
            }
            
            if (!result.xmlSaved) {
                result.xmlSaved = modified && isXml;
            }
        }
    }
    
    public EditorFragment getFrag (int index) {
        return mFiles.get (index).fragment;
    }
    
    public String getEditorTitle (int index) {
        return mFiles.get (index).file.getName ();
    }
    
    public List<OpenedFile> getFiles () {
        return mFiles;
    }
    
    public OpenedFile getOpenedFile (int index) {
        return mFiles.get (index);
    }
    
    @Override
    public void onModified (EditorFragment editor) {
    }
    
    @Override
    public void onSaved (EditorFragment editor) {
    }
    
    @NonNull
    @Override
    public Fragment createFragment (int position) {
        return getFrag (position);
    }
    
    @Override
    public int getItemCount () {
        return mFiles.size ();
    }
    
    @Override
    public long getItemId (int position) {
        return mFiles.get (position).hashCode ();
    }
    
    @Override
    public boolean containsItem (long itemId) {
        for (final var file : mFiles) {
            if (itemId == file.hashCode ()) {  // see getItemId(int)
                return true;
            }
        }
        return false;
    }
    
    public static class OpenedFile {
        
        public final File file;
        public final EditorFragment fragment;
        
        private OpenedFile (File file, EditorFragment fragment) {
            this.file = file;
            this.fragment = fragment;
        }
    
        @Override
        public boolean equals (Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass () != o.getClass ()) {
                return false;
            }
            OpenedFile that = (OpenedFile) o;
            return Objects.equals (file, that.file) && Objects.equals (fragment, that.fragment);
        }
    
        @Override
        public int hashCode () {
            return Objects.hash (file, fragment);
        }
    }
}
