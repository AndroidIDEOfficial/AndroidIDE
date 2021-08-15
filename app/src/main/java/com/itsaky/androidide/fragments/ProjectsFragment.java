package com.itsaky.androidide.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.FileIOUtils;
import com.itsaky.androidide.EditorActivity;
import com.itsaky.androidide.adapters.ProjectsListAdapter;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutProjectsBinding;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.FileUtil;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;
import com.itsaky.androidide.project.utils.ProjectFinder;

public class ProjectsFragment extends BaseFragment {
	
	private LayoutProjectsBinding binding;
	private ArrayList<AndroidProject> mProjects;
	private boolean isLoading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = LayoutProjectsBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getActivity() == null) return;

		loadProjects();
	}

	public void loadProjects() {
		if (!isLoading && getStudioActivity().isStoragePermissionGranted()) {
			getProgressBar().setVisibility(View.VISIBLE);
			getRecyclerView().setVisibility(View.GONE);
			setLoading(true);
			final ProjectsFragment frag = this;
			new TaskExecutor().executeAsync(new GetProjectsTask(), (result) -> {
				frag.setLoading(false);

				if (frag.getProjectsList() == null || frag.getProjectsList().size() == 0) {
					frag.getProgressBar().setVisibility(View.GONE);
					frag.getRecyclerView().setVisibility(View.GONE);
					frag.getNothingLayout().setVisibility(View.VISIBLE);
				} else {
					frag.getNothingLayout().setVisibility(View.GONE);
					frag.getProgressBar().setVisibility(View.GONE);
					frag.getRecyclerView().setVisibility(View.VISIBLE);
					frag.getRecyclerView().setLayoutManager(new LinearLayoutManager(getActivity()));
					frag.getRecyclerView().setAdapter(new ProjectsListAdapter(mProjects, (project) -> openProject(project)));
				}
			});
		}
	}

	private void openProject(AndroidProject project) {
		startActivity(new Intent(getActivity(), EditorActivity.class)
					  .putExtra("project", project));
	}

	public void getProjects() throws Exception {
		mProjects = new ArrayList<>();
		File[] files = StudioApp.getInstance().listProjects();
		for (File file : files) {
			AndroidProject project = ProjectFinder.fromFolder(file);
            
            if(project == null)
                continue;
            
            mProjects.add(project);
		}
	}
    
	public ProgressBar getProgressBar() {
		return binding.projectsProgressBar;
	}

	public RecyclerView getRecyclerView() {
		return binding.mainProjectsRecyclerView;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public ArrayList<AndroidProject> getProjectsList() {
		return mProjects;
	}

	public View getNothingLayout() {
		return binding.projectsNothingLayout;
	}

	private class GetProjectsTask implements Callable<List<AndroidProject>> {
		@Override
		public List<AndroidProject> call() throws Exception {
			getProjects();
			return null;
		}
	}

}
