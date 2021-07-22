package com.itsaky.androidide.language.java.parser.internal;

import android.os.FileObserver;
import androidx.annotation.Nullable;
import com.itsaky.androidide.language.java.parser.model.PackageDescription;
import com.itsaky.androidide.models.AndroidProject;
import java.io.File;
import java.util.ArrayList;

public class PackageManager {
    private static final String TAG = "AutoCompletePackage";
    private PackageDescription root;
    private FileObserver fileObserver;
	
    public PackageManager() {
        root = PackageDescription.root();
    }
	
    public void init(AndroidProject projectFile, JavaClassManager classReader) {
        ArrayList<IClass> classes = classReader.getAllClasses();
        for (IClass clazz : classes) {
            root.put(clazz.getFullClassName());
        }
		
		final String parentPath = new File(projectFile.getMainModulePath(), "src/main/java").getPath();
        fileObserver = new FileObserver(parentPath) {
            @Override
            public void onEvent(int event, String path) {
                remove(path, parentPath);
                add(path, parentPath);
            }
        };
        fileObserver.startWatching();
    }
	
    private void add(String child, String parent) {
        if (child.startsWith(parent)) {
            child = child.substring(parent.length() + 1);
            child = child.replace(File.separator, ".");
            root.put(child);
        }
    }
	
    @Nullable
    private PackageDescription remove(String child, String parent) {
        if (child.startsWith(parent)) {
            child = child.substring(parent.length() + 1);
            child = child.replace(File.separator, ".");
            return root.remove(child);
        }
        return null;
    }
	
    public void destroy() {
        if (fileObserver != null) fileObserver.stopWatching();
    }
	
    @Nullable
    public PackageDescription trace(String child) {
        return this.root.get(child);
    }
}
