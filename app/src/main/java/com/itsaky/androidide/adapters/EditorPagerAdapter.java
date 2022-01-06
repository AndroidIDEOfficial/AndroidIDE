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
import androidx.recyclerview.widget.DiffUtil;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.models.OpenedFile;
import com.itsaky.androidide.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditorPagerAdapter extends FragmentStateAdapter implements EditorFragment.ModificationStateListener {
    
    private final List<OpenedFile> mFiles;
    private final EditorFragment.FileOpenListener mFileOpenListener;
    
    private static final Logger LOG = Logger.instance ("EditorPagerAdapter");
    
    public EditorPagerAdapter (final FragmentActivity activity, EditorFragment.FileOpenListener fileOpenListener) {
        super (activity);
        this.mFileOpenListener = fileOpenListener;
        this.mFiles = new ArrayList<> ();
    }
    
    public void setFiles (List<OpenedFile> files) {
        final var result = DiffUtil.calculateDiff (new DiffUtil.Callback () {
            @Override
            public int getOldListSize () {
                return mFiles.size ();
            }
    
            @Override
            public int getNewListSize () {
                return files.size ();
            }
    
            @Override
            public boolean areItemsTheSame (int oldItemPosition, int newItemPosition) {
                return Objects.equals (mFiles.get (oldItemPosition), files.get (newItemPosition));
            }
    
            @Override
            public boolean areContentsTheSame (int oldItemPosition, int newItemPosition) {
                return areItemsTheSame (oldItemPosition, newItemPosition);
            }
        });
        mFiles.clear ();
        mFiles.addAll (files);
        result.dispatchUpdatesTo (this);
    }
    
    
    public String getEditorTitle (int index) {
        return mFiles.get (index).getFile ().getName ();
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
        return mFiles.get (position)
                .createFragment (this.mFileOpenListener, this);
    }
    
    @Override
    public int getItemCount () {
        return mFiles.size ();
    }
    
    @Override
    public long getItemId (int position) {
        if (mFiles.isEmpty ()) {
            LOG.warn ("File list is empty but getItemId(int) is being called. Returning -1");
            return -1;
        }
    
        final var id = mFiles.get (position).hashCode ();
        LOG.debug ("Item ID for editor fragment at position " + position + " is " + id);
        return id;
    }
    
    @Override
    public boolean containsItem (long itemId) {
        for (final var file : mFiles) {
            if (itemId == file.hashCode ()) {  // see getItemId(int)
                return true;
            }
        }
        
        LOG.warn ("EditorPagerAdapter does not contain any file with item ID : " + itemId);
        return false;
    }
}
