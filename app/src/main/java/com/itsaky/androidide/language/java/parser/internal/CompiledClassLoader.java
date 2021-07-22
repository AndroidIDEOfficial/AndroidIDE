package com.itsaky.androidide.language.java.parser.internal;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.elvishew.xlog.XLog;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.models.ConstantsBridge;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class CompiledClassLoader {

    @NonNull
    public ArrayList<Class> getCompiledClassesFromProject(@NonNull AndroidProject project) {
        ArrayList<Class> classes = new ArrayList<>();
        try {
			final List<String> dexes = ConstantsBridge.PROJECT_DEXES;
			final PathClassLoader loader = new PathClassLoader(TextUtils.join(File.pathSeparator, dexes), ClassLoader.getSystemClassLoader());
			if (dexes == null || dexes.size() <= 0) return classes;
			for (String dex : dexes) {
				final DexFile d = new DexFile(new File(dex));
				Enumeration<String> entries = d.entries();
				String name = null;
				while (entries.hasMoreElements() && (name = entries.nextElement()) != null) {
					while(name.startsWith(File.separator))
						name = name.substring(1);
					if(name.contains(File.separator))
						name = name.replace(File.separator, ".");
					Class<?> load = tryLoad(loader, name);
					if (load != null)
						classes.add(load);
				}
				d.close();
			}
			
			ConstantsBridge.CLASS_LOAD_SUCCESS = true;
			return classes;
		} catch (Throwable th) {
			ConstantsBridge.CLASS_LOAD_SUCCESS = false;
			return classes;
		}
    }
	
	private Class<?> tryLoad(ClassLoader loader, String name) {
		try {
			return loader.loadClass(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}
