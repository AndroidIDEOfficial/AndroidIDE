package com.itsaky.androidide.services.compiler;

import android.text.TextUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.interfaces.CompileListener;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnostic;
import com.itsaky.androidide.services.compiler.model.CompilerDiagnosticImpl;
import com.itsaky.androidide.services.compiler.request.CompilationRequest;
import com.itsaky.androidide.tools.SourceJavaFileObject;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileManager;
import org.eclipse.jdt.internal.compiler.tool.EclipseFileObject;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

public class JavaCompilerService implements DiagnosticListener<JavaFileObject> {
	
	private AndroidProject mProject;
    
	private EclipseCompiler mCompiler;
    private CompilerWrapper mSingleFileCompiler;
	private EclipseFileManager mFileManager;
    
    private FileSystem sf_nameEnvironment;
    private CompilerOptions sf_options;
    private IProblemFactory sf_problemFactory;
    private ICompilerRequestor sf_compilerRequest;
    private CompilerProgress sf_progress;
    private IErrorHandlingPolicy sf_policy;
    private PrintWriter sf_outWriter;
    
	private List<String> mOptions;
    private List<File> mSources;
    
    private static final Map<File, List<CompilerDiagnostic>> mDiagnostics = new HashMap<>();
	
	private FileFilter JAVA_FILTER = new FileFilter(){

		@Override
		public boolean accept(File p1) {
			return p1.isFile() && p1.getName().endsWith(".java");
		}
	};
	
	public JavaCompilerService(AndroidProject project) {
		this.mProject = project;
		this.createOptions();
	}
    
    private void createOptions() {
        this.mOptions = new ArrayList<>();

        mOptions.add("-1.8");
        mOptions.add("-d");
        mOptions.add(Environment.TMP_DIR.getAbsolutePath());
        mOptions.add("-proc:none");
        mOptions.add("-warn:all");
        mOptions.add("-warn:-nls");
        mOptions.add("-g");
        mOptions.add("-bootclasspath");
        mOptions.add(Environment.BOOTCLASSPATH.getAbsolutePath());
        mOptions.add("-cp");
        mOptions.add(TextUtils.join(File.pathSeparator, mProject.getClassPaths()));
    }
    
    private EclipseCompiler getCompiler() {
        return getCompiler(false);
	}
    
	private EclipseCompiler getCompiler(boolean forceCreate) {
		if(mCompiler == null || forceCreate) {
			this.mCompiler = new EclipseCompiler();
			this.mFileManager = (EclipseFileManager) mCompiler.getStandardFileManager(this, Locale.US, null);
        }
		return mCompiler;
	}
    
    private EclipseFileManager getFileManager() {
        if(mFileManager == null)
            getCompiler(true);
            
        return mFileManager;
    }
    
    private CompilerWrapper getSingleFileCompiler() {
        if(this.sf_nameEnvironment == null)
            this.sf_nameEnvironment = new FileSystem(createClasspaths(mProject.getClassPaths()), null, null);
        
        if(this.sf_compilerRequest == null) 
            this.sf_compilerRequest = new CompilerRequestor(this);
        
        if(this.sf_problemFactory == null)
            this.sf_problemFactory = getProblemFactory();
        
        if(this.sf_options == null) {
            this.sf_options = new CompilerOptions();
            putOptions(sf_options);
        }
        
        if(sf_policy == null)
            this.sf_policy = DefaultErrorHandlingPolicies.exitAfterAllProblems();
            
        if(sf_outWriter == null) 
            sf_outWriter = new PrintWriter(System.out);
        
        if(mSingleFileCompiler == null) {
            mSingleFileCompiler = new CompilerWrapper(
                sf_nameEnvironment,
                sf_policy,
                sf_options,
                sf_compilerRequest,
                sf_problemFactory,
                sf_outWriter,
                sf_progress = new CompilerProgress()
            );
        }
        return mSingleFileCompiler;
    }

    private void putOptions(CompilerOptions options) {
        final long languageLevel = ClassFileConstants.JDK1_8;
        options.sourceLevel = options.targetJDK = options.complianceLevel = languageLevel;
        options.processAnnotations = false;
        Map<String, String> map = options.getMap();
        for(String warn : CompilerOptions.warningTokens) {
            map.put(warn, CompilerOptions.ENABLED);
        }
        map.put(CompilerOptions.OPTION_ReportNonExternalizedStringLiteral, CompilerOptions.DISABLED);
        map.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.DO_NOT_GENERATE);
        map.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.DO_NOT_GENERATE);
        map.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.DO_NOT_GENERATE);
        options.set(map);
    }
    
    private String[] createClasspaths(List<String> classpaths) {
        if (classpaths == null) {
            classpaths = new ArrayList<>();
        }
        
        if(!classpaths.contains(Environment.BOOTCLASSPATH.getAbsolutePath())) {
            classpaths.add(Environment.BOOTCLASSPATH.getAbsolutePath());
        }
        
        String[] result = new String[classpaths.size()];
        return classpaths.toArray(result);
    }
    
    private CompilationRequest createCompilationRequestForAll(CompileListener listener) {
        if(mSources == null || mSources.size() <= 0) {
            loadSources();
        }
        return CompilationRequest.Builder
            .newInstance()
            .withCallback(listener)
            .withFiles(mSources)
            .build();
    }
    
    private void notifyCompiled(CompileListener listener, Map<File, List<CompilerDiagnostic>> diags) {
        ThreadUtils.runOnUiThread(() -> {
            listener.onCompilationResult(true, diags);
        });
    }
    
    protected void cancelCompilation() {
        getCompiler().setCompilationCancelled(true);
        if(sf_progress != null) {
            sf_progress.setCanceled(true);
        }
    }
    
    public List<String> getClasspaths() {
        return mProject.getClassPaths();
    }

    public void loadSources() {
        mSources = new ArrayList<File>();
        for(String path : mProject.getSourcePaths()) {
            if(path == null || path.trim().length() <= 0) continue;
            File p = new File(path);
            if(!p.exists() || !p.isDirectory()) continue;
            mSources.addAll(FileUtils.listFilesInDirWithFilter(p, JAVA_FILTER, true));
        }
    }
    
    public void compileAllAsync(CompileListener listener) {
        new Thread(() -> {
            compile(createCompilationRequestForAll(listener));
        }).start();
    }
    
    public void compileAsync(CompilationRequest request) {
        new Thread(() -> {
            compile(request);
        }).start();
    }
    
    public void compileSingleAsync(SourceJavaFileObject source, CompileListener listener) {
        new Thread(() -> {
            compileSingle(source, listener);
        }).start();
    }
    
    public boolean compile(final CompilationRequest request) {
        this.mDiagnostics.clear();
        final List<JavaFileObject> fileObjects = (ArrayList<JavaFileObject>) getFileManager().getJavaFileObjectsFromFiles(request.files());
        JavaCompiler.CompilationTask task = getCompiler()
                .setCompilationCancelled()
                .newProgress()
                .getTask(null, getFileManager(), this, mOptions, null, fileObjects);
        boolean success = task.call();
        notifyCompiled(request.callback(), mDiagnostics);
		return success;
    }
    
    public void compileSingle(final SourceJavaFileObject source, final CompileListener listener) {
        mDiagnostics.clear();
        cancelCompilation();
        if(mDiagnostics != null) mDiagnostics.clear();
        getSingleFileCompiler()
            .setProgress(this.sf_progress = new CompilerProgress())
            .compile(new CompilationUnit[]{source.asUnit()});
        notifyCompiled(listener, mDiagnostics);
    }
	
	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
		CompilerDiagnostic d = CompilerDiagnosticImpl.fromDiagnostic(diagnostic);
		if(mDiagnostics.containsKey(d.file())) {
			List<CompilerDiagnostic> diags = mDiagnostics.get(d.file());
			if(diags == null)
				diags = new ArrayList<>();
			diags.add(d);
			mDiagnostics.put(d.file(), diags);
		} else {
			List<CompilerDiagnostic> diags = new ArrayList<>();
			diags.add(d);
			mDiagnostics.put(d.file(), diags);
		}
	}
    
    private IProblemFactory getProblemFactory() {
        return new DefaultProblemFactory() {
            @Override
            public CategorizedProblem createProblem(
                final char[] originatingFileName,
                final int problemId,
                final String[] problemArguments,
                final String[] messageArguments,
                final int severity,
                final int startPosition,
                final int endPosition,
                final int lineNumber,
                final int columnNumber) {

                report(new Diagnostic<JavaFileObject>() {
                        @Override
                        public String getCode() {
                            return Integer.toString(problemId);
                        }
                        @Override
                        public long getColumnNumber() {
                            return columnNumber;
                        }
                        @Override
                        public long getEndPosition() {
                            return endPosition;
                        }
                        @Override
                        public Kind getKind() {
                            if ((severity & ProblemSeverities.Error) != 0) {
                                return Diagnostic.Kind.ERROR;
                            }
                            if ((severity & ProblemSeverities.Optional) != 0) {
                                return Diagnostic.Kind.WARNING;
                            }
                            if ((severity & ProblemSeverities.Warning) != 0) {
                                return Diagnostic.Kind.MANDATORY_WARNING;
                            }
                            return Diagnostic.Kind.OTHER;
                        }
                        @Override
                        public long getLineNumber() {
                            return lineNumber;
                        }
                        @Override
                        public String getMessage(Locale locale) {
                            if (locale != null) {
                                setLocale(locale);
                            }
                            return getLocalizedMessage(problemId, problemArguments);
                        }
                        @Override
                        public long getPosition() {
                            return startPosition;
                        }
                        @Override
                        public JavaFileObject getSource() {
                            File f = new File(new String(originatingFileName));
                            if (f.exists()) {
                                return new EclipseFileObject(null, f.toURI(), JavaFileObject.Kind.SOURCE, null);
                            }
                            return null;
                        }
                        @Override
                        public long getStartPosition() {
                            return startPosition;
                        }
                    });
                return super.createProblem(originatingFileName, problemId, problemArguments, messageArguments, severity, startPosition, endPosition, lineNumber, columnNumber);
            }
            @Override
            public CategorizedProblem createProblem(
                final char[] originatingFileName,
                final int problemId,
                final String[] problemArguments,
                final int elaborationID,
                final String[] messageArguments,
                final int severity,
                final int startPosition,
                final int endPosition,
                final int lineNumber,
                final int columnNumber) {

                report(new Diagnostic<JavaFileObject>() {
                        @Override
                        public String getCode() {
                            return Integer.toString(problemId);
                        }
                        @Override
                        public long getColumnNumber() {
                            return columnNumber;
                        }
                        @Override
                        public long getEndPosition() {
                            return endPosition;
                        }
                        @Override
                        public Kind getKind() {
                            if ((severity & ProblemSeverities.Error) != 0) {
                                return Diagnostic.Kind.ERROR;
                            }
                            if ((severity & ProblemSeverities.Info) != 0) {
                                return Diagnostic.Kind.NOTE;
                            }
                            if ((severity & ProblemSeverities.Optional) != 0) {
                                return Diagnostic.Kind.WARNING;
                            }
                            if ((severity & ProblemSeverities.Warning) != 0) {
                                return Diagnostic.Kind.MANDATORY_WARNING;
                            }
                            return Diagnostic.Kind.OTHER;
                        }
                        @Override
                        public long getLineNumber() {
                            return lineNumber;
                        }
                        @Override
                        public String getMessage(Locale locale) {
                            if (locale != null) {
                                setLocale(locale);
                            }
                            return getLocalizedMessage(problemId, problemArguments);
                        }
                        @Override
                        public long getPosition() {
                            return startPosition;
                        }
                        @Override
                        public JavaFileObject getSource() {
                            File f = new File(new String(originatingFileName));
                            if (f.exists()) {
                                return new EclipseFileObject(null, f.toURI(), JavaFileObject.Kind.SOURCE, null);
                            }
                            return null;
                        }
                        @Override
                        public long getStartPosition() {
                            return startPosition;
                        }
                    });
                return super.createProblem(originatingFileName, problemId, problemArguments, elaborationID, messageArguments, severity, startPosition, endPosition, lineNumber, columnNumber);
            }
        };
    }
}
