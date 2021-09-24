package com.itsaky.androidide.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.itsaky.androidide.fragments.EditorFragment;
import com.itsaky.androidide.interfaces.JLSRequestor;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.SaveResult;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.Range;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditorPagerAdapter extends FragmentStatePagerAdapter implements EditorFragment.ModificationStateListener {
    
	private ArrayList<File> mOpenedFiles;
	private ArrayList<Fragment> mFragments;
	private AndroidProject project;
    
    private EditorStateListener mEditorStateListener;
    
    private static final Logger LOG = Logger.instance("EditorPagerAdapter");

	public EditorPagerAdapter(FragmentManager manager, AndroidProject project) {
		super(manager);
		this.project = project;
		this.mOpenedFiles = new ArrayList<>();
		this.mFragments = new ArrayList<>();
	}

	public EditorPagerAdapter(FragmentManager manager, AndroidProject project, List<Fragment> fragments, List<File> files) {
		super(manager);
		this.project = project;
		this.mOpenedFiles = new ArrayList<>();
		this.mFragments = new ArrayList<>();

		mOpenedFiles.addAll(files);
		mFragments.addAll(fragments);
	}
    
    public EditorPagerAdapter setEditorStateListener (EditorStateListener listener) {
        this.mEditorStateListener = listener;
        return this;
    }

    public int findIndexOfEditorByFile(File file) {
        for (int i=0;i < mFragments.size();i++) {
            Fragment frag = mFragments.get(i);
            if (!(frag instanceof EditorFragment))
                continue;

            EditorFragment editor = (EditorFragment) frag;
            if (editor.getFile() != null && editor.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                return i;
        }

        return -1;
    }

    public EditorFragment findEditorByFile(File file) {
        int index = findIndexOfEditorByFile(file);
        if (index != -1)
            return getFrag(index);
        return null;
    }

	public int openFile(File file, Range selection, EditorFragment.FileOpenListener listener, JLSRequestor jlsRequestor) {
		int openedFileIndex = -1;
		for (int i=0;i < mOpenedFiles.size();i++) {
			File f = mOpenedFiles.get(i);
			if (f.getAbsolutePath().equals(file.getAbsolutePath())) {
				openedFileIndex = i;
				break;
			}
		}

		if (openedFileIndex == -1) {
			mFragments.add(EditorFragment
                           .newInstance(file, project, selection)
                           .setFileOpenListener(listener)
                           .setJLSRequestor(jlsRequestor)
                           .setModificationStateListener(this));
			mOpenedFiles.add(file);
			notifyDataSetChanged();
			return mFragments.size() - 1;
		} else return openedFileIndex;
	}

	public SaveResult saveAll() {
		SaveResult result = new SaveResult();
		for (int i=0;i < mFragments.size();i++) {
			save(i, result);
		}
		return result;
	}

	public void save(int index, SaveResult result) {
		if (index >= 0 && index < mFragments.size()) {
			EditorFragment frag = getFrag(index);
            
            /**
             * Must be called before frag.save()
             * Otherwise, it'll always return false
             */
            final boolean modified = frag.isModified();
            
			frag.save();
            
            final boolean isGradle = frag.getFile().getName().endsWith(EditorFragment.EXT_GRADLE);
            final boolean isXml = frag.getFile().getName().endsWith(EditorFragment.EXT_XML);
            
            if (!result.gradleSaved) {
                result.gradleSaved = modified && isGradle;
            }

            if (!result.xmlSaved) {
                result.xmlSaved = modified && isXml;
            }
		}
	}

	public EditorFragment getFrag(int index) {
		return (EditorFragment) getItem(index);
	}

	public List<Fragment> getFragments() {
		return mFragments;
	}

    @Override
    public void onModified(EditorFragment editor) {
        if(mEditorStateListener != null)
            mEditorStateListener.editorStateChanged();
    }

    @Override
    public void onSaved(EditorFragment editor) {
        if(mEditorStateListener != null)
            mEditorStateListener.editorStateChanged();
    }
    
	public List<File> getOpenedFiles() {
		return mOpenedFiles;
	}

	@Override
	public Fragment getItem(int p1) {
		return p1 < 0 || p1 >= mFragments.size() ? null : mFragments.get(p1);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return getFrag(position).getTabTitle();
	}
    
    public static interface EditorStateListener {
        void editorStateChanged();
    }
}
