package com.itsaky.androidide.services.compiler;

import android.text.TextUtils;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.interfaces.CompileListener;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnosticImpl;
import com.itsaky.androidide.utils.Environment;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileManager;
import com.elvishew.xlog.XLog;

public class JavaCompilerService implements DiagnosticListener<JavaFileObject> {
	
	private AndroidProject project;
	
	private CompileListener listener;
	private EclipseCompiler compiler;
	private EclipseFileManager fileManager;
	
	private List<String> options;
	private final Map<File, List<CompilerDiagnostic>> diagnostics = new HashMap<>();
	
	private FileFilter JAVA_FILTER = new FileFilter(){

		@Override
		public boolean accept(File p1) {
			return p1.isFile() && p1.getName().endsWith(".java");
		}
	};
	
	public JavaCompilerService(AndroidProject project) {
		this.project = project;
		
		getCompiler();
	}
	
	public JavaCompilerService setListener(CompileListener listener) {
		this.listener = listener;
		return this;
	}
	
	private EclipseCompiler getCompiler() {
		if(compiler == null) {
			this.compiler = new EclipseCompiler();
			this.fileManager = (EclipseFileManager) compiler.getStandardFileManager(this, Locale.US, null);
			this.options = new ArrayList<>();
			
			options.add("-1.8");
			options.add("-d");
			options.add("none");
			options.add("-proc:none");
			options.add("-warn:all");
			options.add("-g");
			options.add("-bootclasspath");
			options.add(Environment.BOOTCLASSPATH.getAbsolutePath());
			options.add("-cp");
			options.add(TextUtils.join(File.pathSeparator, project.getClassPaths()));
		}
		
		return compiler;
	}
	
	public boolean compileSingle(File file) {
		List<String> ops = new ArrayList<String>(options);
		ops.add("-sourcepath");
		ops.add(TextUtils.join(File.pathSeparator, project.getSourcePaths()));
		
		JavaCompiler.CompilationTask task = getCompiler().getTask(null, fileManager, this, ops, null, fileManager.getJavaFileObjects(file));
		diagnostics.clear();
		boolean success = task.call();
		if(this.listener != null) {
			this.listener.onCompilationResult(success, diagnostics);
		}
		return success;
	}
	
	public boolean compileAll() {
		List<File> files = new ArrayList<File>();
		for(String path : project.getSourcePaths()) {
			if(path == null || path.trim().length() <= 0) continue;
			File p = new File(path);
			if(!p.exists() || !p.isDirectory()) continue;
			files.addAll(FileUtils.listFilesInDirWithFilter(p, JAVA_FILTER, true));
		}
		return compile(files);
	}
	
	public boolean compile(List<File> files) {
		JavaCompiler.CompilationTask task = getCompiler().getTask(null, fileManager, this, options, null, fileManager.getJavaFileObjectsFromFiles(files));
		diagnostics.clear();
		final long s = System.nanoTime();
		boolean success = task.call();
		XLog.i("[JavaCompiler]" 
			+ "\ncompiled: " + files.size() + " files"
			+ "\ntimeElapsed: " + (System.nanoTime() - s) + "ns");
		return success;
	}
	
	public Map<File, List<CompilerDiagnostic>> getDiagnostics() {
		return diagnostics;
	}
	
	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
		CompilerDiagnostic d = CompilerDiagnosticImpl.fromDiagnostic(diagnostic);
		if(diagnostics.containsKey(d.file())) {
			List<CompilerDiagnostic> diags = diagnostics.get(d.file());
			if(diags == null)
				diags = new ArrayList<>();
			diags.add(d);
			diagnostics.put(d.file(), diags);
		} else {
			List<CompilerDiagnostic> diags = new ArrayList<>();
			diags.add(d);
			diagnostics.put(d.file(), diags);
		}
	}
}
