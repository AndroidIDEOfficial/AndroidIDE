package com.itsaky.androidide.services;

import androidx.core.util.Pair;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.interfaces.DexResultListener;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jar2DexService implements ShellServer.Callback {
	
	private final List<String> mClasspaths;
	private final List<String> commands;
	private final List<String> dexNames;
	private final List<File> dexOuts;
	private final ShellServer mServer;
	private final File baseBuildDir;
	private final DexResultListener listener;
	private final StringBuilder outBuilder;
	
	private int currentDexingIndex = 0;
	
	private static final String DONE_MESSAGE = "DEXED_SUCCESSFULLY";
	private static final String FAILED_MESSAGE = "Compilation failed";
	private static final String D8_COMMAND = "java -jar %s --debug --lib '%s' --min-api 21 --output '%s' %s && echo 'DEXED_SUCCESSFULLY'";
	
	public Jar2DexService(File buildDir, List<String> classpaths, DexResultListener listener) {
		this.baseBuildDir = buildDir;
		this.mClasspaths = classpaths;
		this.listener = listener;
		this.mServer = StudioApp.getInstance().newShell(this);
		this.outBuilder = new StringBuilder();
		this.commands = new ArrayList<>();
		this.dexNames = new ArrayList<>();
		this.dexOuts = new ArrayList<>();
		this.currentDexingIndex = 0;
	}
	
	public void startDexing() {
		commands.clear();
		dexOuts.clear();
		dexNames.clear();
		final File d8 = StudioApp.getInstance().getD8Jar();
		final File androidJar = Environment.BOOTCLASSPATH;
		final File androidDexDir = Environment.mkdirIfNotExits(new File(androidJar.getParentFile(), "dex"));
		final File baseOut = baseBuildDir;
		if(d8 == null || !d8.exists()) {
			listener.onDexFailed("d8.jar: File not found!");
			return;
		}
		
		if(androidJar == null || !androidJar.exists()) {
			listener.onDexFailed("android.jar: File not found!");
			return;
		}
		
		if(baseOut == null || !baseOut.exists()) {
			listener.onDexFailed("Dex out directory: Directory not found!");
			return;
		}
		
		final String d8Path = wrapPathIfNeeded(d8);
		final String androidJarPath = wrapPathIfNeeded(androidJar);
		for(String classpath : mClasspaths) {
			final File cp = new File(classpath);
			final String name = !cp.getName().contains(".jar") ? cp.getName() : cp.getName().substring(0, cp.getName().lastIndexOf(".jar"));
			if(!cp.exists()) continue;
			File out = Environment.mkdirIfNotExits(new File(baseOut, name));
			dexOuts.add(out);
			if(out.exists() && containsDexes(out.listFiles())) {
				continue;
			}
			classpath = wrapPathIfNeeded(classpath);
			final String outPath = wrapPathIfNeeded(out);
			final String FINAL_D8_COMMAND = String.format(D8_COMMAND, d8Path, androidJarPath, outPath, classpath);
			commands.add(FINAL_D8_COMMAND);
			dexNames.add(name);
		}
		
		if(androidDexDir.exists()) {
			if(!containsDexes(androidDexDir.listFiles())) {
				addAndroidJarToDex(d8Path, androidJarPath, androidDexDir);
			}
		} else {
			addAndroidJarToDex(d8Path, androidJarPath, androidDexDir);
		}
		
		if(commands.size() <= 0) {
			notifyDexed();
		} else dexNext();
	}
	
	private boolean containsDexes(File[] dir) {
		boolean found = false;
		for(File f : dir) {
			if(f.isFile() && f.getName().endsWith(".dex"))
				return true;
			else if(f.isDirectory()) {
				// can't directly return 'found'
				// it will prevent this recuraion from searching other directories
				found = containsDexes(f.listFiles());
				if(found) {
					return true;
				}
			}
		}
		return found;
	}

	private void addAndroidJarToDex(String d8Path, String androidJarPath, File androidDexDir) {
		dexOuts.add(androidDexDir);
		commands.add(0, String.format(D8_COMMAND, d8Path, androidJarPath, !androidDexDir.getAbsolutePath().contains(" ") ? androidDexDir.getAbsolutePath() : "'" + androidDexDir.getAbsolutePath() + "'", androidJarPath));
		dexNames.add(0, "android.jar");
	}
	
	private String wrapPathIfNeeded(File src) {
		return wrapPathIfNeeded(src.getAbsolutePath());
	}
	
	private String wrapPathIfNeeded(String src) {
		return src.contains(" ") ? "'" + src + "'" : src;
	}
	
	private void dexNext() {
		mServer.bgAppend(commands.get(currentDexingIndex));
		if(listener != null) {
			listener.onDex(dexNames.get(currentDexingIndex));
		}
	}
	
	private boolean canDex() {
		return currentDexingIndex < commands.size();
	}
	
	private void notifyDexed() {
		List<String> dexes = new ArrayList<>();
		for(File f : dexOuts) {
			List<File> dxs = FileUtils.listFilesInDirWithFilter(f, DEX_FILTER, true);
			if(dxs != null && dxs.size() > 0) {
				dexes.addAll(dxs.stream().map(d -> d.getAbsolutePath()).collect(Collectors.toList()));
			}
		}
		
		if(listener != null)
			listener.onDexSuccess(dexes);
	}
	
	@Override
	public void output(CharSequence charSequence) {
		if(charSequence == null || listener == null) return;
		final String line = charSequence.toString().trim();
		final String toAppend = line + "\n";
		outBuilder.append(toAppend);
		if(line.contains(DONE_MESSAGE)) {
			currentDexingIndex++;
			if(canDex())
				dexNext();
			else {
				notifyDexed();
			}
		} else if(line.contains(FAILED_MESSAGE)) {
			listener.onDexFailed(outBuilder.toString());
		}
	}
	
	private final FileFilter DEX_FILTER = new FileFilter(){

		@Override
		public boolean accept(File p1) {
			return p1.isFile() && p1.getName().endsWith(".dex");
		}
	};
}
