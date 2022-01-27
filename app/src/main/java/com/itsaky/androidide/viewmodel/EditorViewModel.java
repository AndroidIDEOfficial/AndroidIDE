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

package com.itsaky.androidide.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.itsaky.androidide.project.AndroidProject;
import com.itsaky.androidide.project.IDEProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ViewModel for data used in {@link com.itsaky.androidide.EditorActivity}
 */
public class EditorViewModel extends ViewModel {
    
    private final MutableLiveData <List<File>> mFiles = new MutableLiveData<> (new ArrayList<> ());
    private final MutableLiveData <AndroidProject> mProject = new MutableLiveData<> (null);
    private final MutableLiveData <IDEProject> mIDEProject = new MutableLiveData<> (null);
    
    /**
     * Holds information about the currently selected editor fragment.
     * First value in the pair is the index of the editor opened.
     * Second value is the file that is opened.
     */
    private final MutableLiveData <Pair<Integer, File>> mCurrentFile = new MutableLiveData<> (null);
    
    /**
     * Get the current {@link AndroidProject}.
     * @return The android project.
     */
    @Nullable
    public AndroidProject getAndroidProject () {
        return mProject.getValue ();
    }
    
    /**
     * Set the android project currently opened in the editor activity.
     * @param project The project that is opened.
     */
    public void setAndroidProject (final AndroidProject project) {
        this.mProject.setValue (project);
    }
    
    /**
     * Get the {@link IDEProject} opened in the editor activity.
     * @return The ide project.
     */
    @Nullable
    public IDEProject getIDEProject () {
        return this.mIDEProject.getValue ();
    }
    
    /**
     * Set the IDEProject that is currently opened in the editor activity.
     * @param project The project that is opened.
     */
    public void setIDEProject (final IDEProject project) {
        this.mIDEProject.setValue (project);
    }
    
    /**
     * Add the given file to the list of opened files.
     * @param file The file that has been opened.
     */
    public void addFile (final File file) {
        final var files = mFiles.getValue ();
        Objects.requireNonNull (files).add (file);
        mFiles.setValue (files);
    }
    
    /**
     * Remove the file at the given index from the list of opened files.
     * @param index The index of the closed file.
     */
    public void removeFile (int index) {
        final var files = mFiles.getValue ();
        Objects.requireNonNull (files).remove (index);
        mFiles.setValue (files);
    }
    
    public void removeAllFiles () {
        mFiles.setValue (new ArrayList<> ());
        setCurrentFile (-1, null);
    }
    
    /**
     * Get the opened file at the given index.
     * @param index The index of the file.
     * @return The file at the given index.
     */
    public File getOpenedFile (final int index) {
        return Objects.requireNonNull (mFiles.getValue ()).get (index);
    }
    
    /**
     * Get the number of files opened.
     * @return The number of files opened.
     */
    public int getOpenedFileCount () {
        return Objects.requireNonNull (mFiles.getValue ()).size ();
    }
    
    /**
     * Get the list of currently opened files.
     * @return The list of opened files.
     */
    @NonNull
    public List<File> getOpenedFiles () {
        return mFiles.getValue () == null ? new ArrayList<> () : mFiles.getValue ();
    }
    
    /**
     * Add an observer to the list of opened files.
     * @param lifecycleOwner The lifecycle owner.
     * @param observer The observer.
     */
    public void observeFiles (LifecycleOwner lifecycleOwner, Observer<List<File>> observer) {
        this.mFiles.observe (lifecycleOwner, observer);
    }
    
    public int getCurrentFileIndex () {
        if (mCurrentFile.getValue () == null) {
            return -1;
        }
        
        return mCurrentFile.getValue ().first;
    }
    
    public File getCurrentFile () {
        if (mCurrentFile.getValue () == null) {
            return null;
        }
        
        return mCurrentFile.getValue ().second;
    }
    
    public void setCurrentFile (final int index, @Nullable final File file) {
        mCurrentFile.setValue (Pair.create (index, file));
    }
}
