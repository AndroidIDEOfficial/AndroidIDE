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
import org.json.JSONException;
import org.json.JSONObject;
import com.blankj.utilcode.util.FileUtils;
import java.util.stream.Collectors;

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
			if (!file.isDirectory() || file.isHidden()) continue;
			File settings = new File(file, "settings.gradle");
			if (!settings.exists()) continue;
			String content = FileUtil.readFile(settings.getAbsolutePath());
			if (TextUtils.isEmpty(content)) continue;
            boolean foundIcon = false;
            AndroidProject project = new AndroidProject();
            for (File module : file.listFiles()) {
                String[] result;
                if (module.isDirectory()
                    && !module.isHidden()
                    && (result = proceedModule(file, module, project)) != null
                    && result.length == 2
                    && result[0] != null
                    && result[1] != null
                    && !foundIcon) {
                    foundIcon = true;
                    String packageName = result[0];
                    String icon = result[1];
                    project.setPackageName(packageName)
						.setIconPath(icon)
						.setMainModule(module.getName())
						.setMainSourcePath(new File(module, "src/main/java").getAbsolutePath());
                }
            }

            project.setAppName(file.getName());
            project.setProjectPath(file.getAbsolutePath());
            mProjects.add(project);
		}
	}

	private String[] proceedModule(File file, File module, AndroidProject project) throws JSONException {
		File projectGradle = new File(file, "build.gradle");
		File moduleGradle = new File(module, "build.gradle");
		File manifest = new File(module, "src/main/AndroidManifest.xml");
		File res = new File(module, "src/main/res");
		if (!module.exists() || !projectGradle.exists() || !moduleGradle.exists() || !manifest.exists()) return null;
		if (!module.isDirectory() || !moduleGradle.isFile() || !projectGradle.isFile() || !manifest.isFile()) return null;

        File java = new File(module, "src/main/java");
        if (java.exists() && java.isDirectory())
            project.addSource(java.getAbsolutePath())
				.addModule(module.getAbsolutePath());

        File libsFolder = new File(module, "libs");
        if (libsFolder.exists() && libsFolder.isDirectory()) {
            File[] libs = libsFolder.listFiles();
            if (libs != null && libs.length > 0) {
                for (File lib : libs)
                    if (lib.getName().endsWith(".jar"))
                        project.addClasspath(lib.getAbsolutePath());
            }
        }
		
		project.addSource(new File(module, "build/generated/source/buildConfig/debug").getAbsolutePath());
		project.addSource(new File(module, "build/generated/data_binding_base_class_source_out/debug/out").getAbsolutePath());

        JSONObject obj = new XmlToJson.Builder(FileUtil.readFile(manifest.getAbsolutePath())).build().toJson();
		if (!obj.has("manifest")) return null;
        String iconPath = null;
        if (obj.has("manifest") && obj.getJSONObject("manifest").has("application") && obj.getJSONObject("manifest").getJSONObject("application").has("android:icon")) {
            String iconRes = obj.getJSONObject("manifest").getJSONObject("application").getString("android:icon");  
            if (iconRes.startsWith("@drawable")) {
                final String iconName = iconRes.replace("@drawable/", "");
                File[] drawables = listDrawables(res);
                for (File drawable : drawables) {
                    File[] icons = listFilesNotEndingWith(drawable, ".xml");
                    for (File icon : icons) {
                        String nameWithoutExtension = icon.getName().substring(0, icon.getName().lastIndexOf("."));
                        if (nameWithoutExtension.equals(iconName)) {
                            iconPath = icon.getAbsolutePath();
                            break;
                        }
                    }
                }
            } else if (iconRes.startsWith("@mipmap")) {
                final String iconName = iconRes.replace("@mipmap/", "");
                File[] drawables = listMipmaps(res);
                for (File drawable : drawables) {
                    File[] icons = listFilesNotEndingWith(drawable, ".xml");
                    for (File icon : icons) {
                        String nameWithoutExtension = icon.getName().substring(0, icon.getName().lastIndexOf("."));
                        if (nameWithoutExtension.equals(iconName)) {
                            iconPath = icon.getAbsolutePath();
                            break;
                        }
                    }
                }
            }
        }
		final String packageName = obj.getJSONObject("manifest").getString("package");
        if (iconPath == null) return null;
		return new String[]{ packageName, iconPath };
	}

	private File[] listDrawables(File file) {
		return listFolderStartingWith(file, "drawable");
	}

	private File[] listMipmaps(File file) {
		return listFolderStartingWith(file, "mipmap");
	}

	private File[] listFolderStartingWith(File file, final String prefix) {
		return file.listFiles(new FileFilter(){

				@Override
				public boolean accept(File p1) {
					return p1.isDirectory() && p1.getName().startsWith(prefix);
				}
			});
	}

	private File[] listFilesNotEndingWith(File file, final String suffix) {
		return file.listFiles(new FileFilter(){

				@Override
				public boolean accept(File p1) {
					return p1.isFile() && !p1.getName().endsWith(suffix);
				}
			});
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
