package com.itsaky.androidide.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.models.AndroidProject;
import io.github.rosemoe.editor.widget.CodeEditor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.itsaky.androidide.interfaces.JLSRequestor;

public class EditorPagerAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<File> mOpenedFiles;
	private ArrayList<Fragment> mFragments;
	private AndroidProject project;
	
	public EditorPagerAdapter(FragmentManager manager, AndroidProject project)
	{
		super(manager);
		this.project = project;
		this.mOpenedFiles = new ArrayList<>();
		this.mFragments = new ArrayList<>();
	}
	
	public EditorPagerAdapter(FragmentManager manager, AndroidProject project, List<Fragment> fragments, List<File> files)
	{
		super(manager);
		this.project = project;
		this.mOpenedFiles = new ArrayList<>();
		this.mFragments = new ArrayList<>();
		
		mOpenedFiles.addAll(files);
		mFragments.addAll(fragments);
	}
    
    public EditorFragment findEditorByFile(File file) {
        for(int i=0;i<mFragments.size();i++) {
            Fragment frag = mFragments.get(i);
            if(!(frag instanceof EditorFragment))
                continue;
                
            EditorFragment editor = (EditorFragment) frag;
            if(editor.getFile() != null && editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                return editor;
        }
        
        return null;
    }
	
	public int openFile(File file, EditorFragment.FileOpenListener listener, CodeEditor.CursorChangeListener cursorListener, JLSRequestor jlsRequestor)
	{
		int openedFileIndex = -1;
		for(int i=0;i<mOpenedFiles.size();i++)
		{
			File f = mOpenedFiles.get(i);
			if(f.getAbsolutePath().equals(file.getAbsolutePath()))
			{
				openedFileIndex = i;
				break;
			}
		}
		
		if(openedFileIndex == -1)
		{
			mFragments.add(EditorFragment
                   .newInstance(file, project)
                   .setFileOpenListener(listener)
                   .setCursorChangeListener(cursorListener)
                   .setJLSRequestor(jlsRequestor));
			mOpenedFiles.add(file);
			notifyDataSetChanged();
			return mFragments.size() - 1;
		} else return openedFileIndex;
	}
	
	public boolean saveAll() {
		boolean hasGradle = false;
		for(int i=0;i<mFragments.size();i++) {
			hasGradle = hasGradle == false && save(i);
		}
		return hasGradle;
	}
	
	public boolean save(int index) {
		if(index >= 0 && index < mFragments.size()) {
			boolean hasGradle = false;
			EditorFragment frag = getFrag(index);
			hasGradle = frag.isModified() && frag.getFile().getName().endsWith(EditorFragment.EXT_GRADLE);
			frag.save();
			return hasGradle;
		}
		return false;
	}
	
	public EditorFragment getFrag(int index) {
		return (EditorFragment) getItem(index);
	}
	
	public List<Fragment> getFragments() {
		return mFragments;
	}
	
	public List<File> getOpenedFiles() {
		return mOpenedFiles;
	}
	
	@Override
	public Fragment getItem(int p1)
	{
		return p1 < 0 || p1 >= mFragments.size() ? null : mFragments.get(p1);
	}
	
	@Override
	public int getCount()
	{
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return mOpenedFiles.get(position).getName();
	}
}
