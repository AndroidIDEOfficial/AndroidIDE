package com.itsaky.androidide.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.util.Pair;
import com.itsaky.androidide.app.StudioApp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AndroidProject implements Parcelable {
    
	private String appName;
	private String packageName;
	private String iconPath;
	private String projectPath;
	private String mainModule;
	private String mainSourcePath;
	
    private List<String> modulePaths = new ArrayList<>();
    private List<String> sourcePaths = new ArrayList<>();
    private List<String> classPaths = new ArrayList<>();

	public AndroidProject() {
		addSource(StudioApp.getInstance().getLogSenderDir().getAbsolutePath());
	}

	protected AndroidProject(Parcel in) {
		appName = in.readString();
		packageName = in.readString();
		iconPath = in.readString();
		projectPath = in.readString();
		mainModule = in.readString();
		mainSourcePath = in.readString();
        in.readList(modulePaths, String.class.getClassLoader());
        in.readList(sourcePaths, String.class.getClassLoader());
        in.readList(classPaths, String.class.getClassLoader());
	}
	
	public File getBuildDir() {
		return new File(getMainModulePath(), "build");
	}

	public AndroidProject setMainModule(String mainModule) {
		this.mainModule = mainModule;
		return this;
	}

	public String getMainModule() {
		return mainModule;
	}
	
	public String getMainModulePath() {
		return new File(getProjectPath(), getMainModule()).getAbsolutePath();
	}

	public AndroidProject setMainSourcePath(String mainModulePath) {
		this.mainSourcePath = mainModulePath;
		return this;
	}

	public String getMainSourcePath() {
		return mainSourcePath;
	}

    public AndroidProject setModulePaths(List<String> modulePaths) {
        this.modulePaths = modulePaths;
        return this;
    }

    public List<String> getModulePaths() {
        if (modulePaths == null) modulePaths = new ArrayList<>();
        return modulePaths;
    }

    public AndroidProject addModule(String path) {
        if (!getModulePaths().contains(path))
            getModulePaths().add(path);
        return this;
    }

    public AndroidProject setSourcePaths(List<String> sourcePaths) {
        this.sourcePaths = sourcePaths;
        return this;
    }

    public List<String> getSourcePaths() {
        if (sourcePaths == null) sourcePaths = new ArrayList<>();
        return sourcePaths;
    }

    public AndroidProject addSource(String path) {
        if (!getSourcePaths().contains(path))
            getSourcePaths().add(path);
        return this;
    }

    public AndroidProject setClassPaths(List<String> classPaths) {
        this.classPaths = classPaths;
        return this;
    }

    public List<String> getClassPaths() {
        if (classPaths == null) classPaths = new ArrayList<>();
        return classPaths;
    }

    public AndroidProject addClasspath(String path) {
        if (!getClassPaths().contains(path))
            getClassPaths().add(path);
        return this;
    }

	public AndroidProject setAppName(String appName) {
		this.appName = appName;
        return this;
	}

	public String getAppName() {
		return appName;
	}

	public AndroidProject setPackageName(String packageName) {
		this.packageName = packageName;
        return this;
	}

	public String getPackageName() {
		return packageName;
	}

	public AndroidProject setIconPath(String iconPath) {
		this.iconPath = iconPath;
        return this;
	}

	public String getIconPath() {
		return iconPath;
	}

	public AndroidProject setProjectPath(String projectPath) {
		this.projectPath = projectPath;
        return this;
	}

	public String getProjectPath() {
		return projectPath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p1, int p2) {
		p1.writeString(appName);
		p1.writeString(packageName);
		p1.writeString(iconPath);
		p1.writeString(projectPath);
		p1.writeString(mainModule);
		p1.writeString(mainSourcePath);
        p1.writeList(modulePaths);
        p1.writeList(sourcePaths);
        p1.writeList(classPaths);
	}

	@Override
	public String toString() {
		return "["
			+ "\nappName=" + getAppName() 
			+ "\npackageName=" + getPackageName()
			+ "\niconPath=" + getIconPath()
			+ "\nprojectPath=" + getProjectPath()
			+ "\nmainModule=" + getMainModule()
			+ "\nmainModulePath=" + getMainModulePath()
			+ "\nmainModuleSourcePath=" + getMainSourcePath()
			+ "\nmodules=" + getModulePaths()
			+ "\nsources=" + getSourcePaths()
			+ "\nclasspaths=" + getClassPaths()
			+ "\n]";
	}

	public static final Creator<AndroidProject> CREATOR = new Creator<AndroidProject>(){

		@Override
		public AndroidProject createFromParcel(Parcel p1) {
			return new AndroidProject(p1);
		}

		@Override
		public AndroidProject[] newArray(int p1) {
			return new AndroidProject[p1];
		}
	};
}
