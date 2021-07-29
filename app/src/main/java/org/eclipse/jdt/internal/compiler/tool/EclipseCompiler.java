/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.tool;

import com.itsaky.androidide.services.compiler.CompilerProgress;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import javax.annotation.processing.Processor;
import javax.lang.model.SourceVersion;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * Implementation of a batch compiler that supports the jsr199
 */
public class EclipseCompiler implements JavaCompiler {

	private static Set<SourceVersion> SupportedSourceVersions;
    
	static {
		// Eclipse compiler supports all possible versions from version 0 to
		// version 6
		// we don't care about the order
		EnumSet<SourceVersion> enumSet = EnumSet.range(SourceVersion.RELEASE_0, SourceVersion.RELEASE_6);
		// we don't want anybody to modify this list
		EclipseCompiler.SupportedSourceVersions = Collections.unmodifiableSet(enumSet);
	}

	WeakHashMap<Thread, EclipseCompilerImpl> threadCache;
	public DiagnosticListener<? super JavaFileObject> diagnosticListener;
    
    private CompilerProgress progress;
    
	public EclipseCompiler() {
		this.threadCache = new WeakHashMap<>();
	}
    
    public EclipseCompiler setCompilationCancelled() {
        return setCompilationCancelled(true);
    }
    
    public EclipseCompiler setCompilationCancelled(boolean p0) {
        if(this.progress != null)
            this.progress.setCanceled(p0);
        return this;
    }
    
    public EclipseCompiler newProgress() {
        this.progress = new CompilerProgress();
        return this;
    }
    
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.tools.Tool#getSourceVersions()
	 */
	@Override
	public Set<SourceVersion> getSourceVersions() {
		return EclipseCompiler.SupportedSourceVersions;
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.tools.JavaCompiler#getStandardFileManager(javax.tools.DiagnosticListener,
	 *      java.util.Locale, java.nio.charset.Charset)
	 */
	@Override
	public StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> someDiagnosticListener, Locale locale, Charset charset) {
		this.diagnosticListener = someDiagnosticListener;
		return new EclipseFileManager(locale, charset);
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.tools.JavaCompiler#getTask(java.io.Writer,
	 *      javax.tools.JavaFileManager, javax.tools.DiagnosticListener,
	 *      java.lang.Iterable, java.lang.Iterable, java.lang.Iterable)
	 */
	@Override
	public CompilationTask getTask(Writer out, JavaFileManager fileManager, DiagnosticListener<? super JavaFileObject> someDiagnosticListener, Iterable<String> options, Iterable<String> classes, Iterable<? extends JavaFileObject> compilationUnits) {
		PrintWriter writerOut = null;
		PrintWriter writerErr = null;
		if (out == null) {
			writerOut = new PrintWriter(System.err);
			writerErr = new PrintWriter(System.err);
		} else {
			writerOut = new PrintWriter(out);
			writerErr = new PrintWriter(out);
		}
		final Thread currentThread = Thread.currentThread();
		EclipseCompilerImpl eclipseCompiler = this.threadCache.get(currentThread);
		if (eclipseCompiler == null) {
			eclipseCompiler = new EclipseCompilerImpl(writerOut, writerErr, false);
			this.threadCache.put(currentThread, eclipseCompiler);
		} else {
			eclipseCompiler.initialize(writerOut, writerErr, false, null/*options*/, null/*progress*/);
		}
		final EclipseCompilerImpl eclipseCompiler2 = new EclipseCompilerImpl(writerOut, writerErr, false);
		eclipseCompiler2.compilationUnits = compilationUnits;
		eclipseCompiler2.diagnosticListener = someDiagnosticListener;
        eclipseCompiler2.progress = eclipseCompiler.progress = this.progress;
		if (fileManager != null) {
			eclipseCompiler2.fileManager = fileManager;
		} else {
			eclipseCompiler2.fileManager = this.getStandardFileManager(someDiagnosticListener, null, null);
		}

		eclipseCompiler2.options.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);
		eclipseCompiler2.options.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
		eclipseCompiler2.options.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);

		ArrayList<String> allOptions = new ArrayList<>();
		if (options != null) {
			for (Iterator<String> iterator = options.iterator(); iterator.hasNext(); ) {
				eclipseCompiler2.fileManager.handleOption(iterator.next(), iterator);
			}
			for (String option : options) {
				allOptions.add(option);
			}
		}

		if (compilationUnits != null) {
			for (JavaFileObject javaFileObject : compilationUnits) {
				// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6419926
				// compells us to check that the returned URIs are absolute,
				// which they happen not to be for the default compiler on some
				// unices
				URI uri = javaFileObject.toUri();
				if (!uri.isAbsolute()) {
					uri = URI.create("file://" + uri.toString()); //$NON-NLS-1$
				}
				if (uri.getScheme().equals("file")) { //$NON-NLS-1$
					allOptions.add(new File(uri).getAbsolutePath());
				} else {
					allOptions.add(uri.toString());
				}
			}
		}

		if (classes != null) {
			allOptions.add("-classNames"); //$NON-NLS-1$
			StringBuilder builder = new StringBuilder();
			int i = 0;
			for (String className : classes) {
				if (i != 0) {
					builder.append(',');
				}
				builder.append(className);
				i++;
			}
			allOptions.add(String.valueOf(builder));
		}

		final String[] optionsToProcess = new String[allOptions.size()];
		allOptions.toArray(optionsToProcess);
		try {
			eclipseCompiler2.configure(optionsToProcess);
		} catch (IllegalArgumentException e) {
			throw e;
		}

		if (eclipseCompiler2.fileManager instanceof StandardJavaFileManager) {
			StandardJavaFileManager javaFileManager = (StandardJavaFileManager) eclipseCompiler2.fileManager;
			Iterable<? extends File> location = javaFileManager.getLocation(StandardLocation.CLASS_OUTPUT);
			if (location != null) {
				eclipseCompiler2.setDestinationPath(location.iterator().next().getAbsolutePath());
			}
		}
        
		return new CompilationTask() {
			private boolean hasRun = false;
			@Override
			public Boolean call() {
				// set up compiler with passed options
				if (this.hasRun) {
					throw new IllegalStateException("This task has already been run"); //$NON-NLS-1$
				}
				Boolean value = eclipseCompiler2.call() ? Boolean.TRUE : Boolean.FALSE;
				this.hasRun = true;
				return value;
			}
			@Override
			public void setLocale(Locale locale) {
				eclipseCompiler2.setLocale(locale);
			}
			@Override
			public void setProcessors(Iterable<? extends Processor> processors) {
				ArrayList<Processor> temp = new ArrayList<>();
				for (Processor processor : processors) {
					temp.add(processor);
				}
				Processor[] processors2 = new Processor[temp.size()];
				temp.toArray(processors2);
				eclipseCompiler2.processors = processors2;
			}
		};
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.tools.OptionChecker#isSupportedOption(java.lang.String)
	 */
	@Override
	public int isSupportedOption(String option) {
		return Options.processOptions(option);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.tools.Tool#run(java.io.InputStream, java.io.OutputStream,
	 *      java.io.OutputStream, java.lang.String[])
	 */
	@Override
	public int run(InputStream in, OutputStream out, OutputStream err, String... arguments) {
		boolean succeed = new Main(
				new PrintWriter(new OutputStreamWriter(out != null ? out : System.out)),
				new PrintWriter(new OutputStreamWriter(err != null ? err : System.err)),
				true/* systemExit */,
				null/* options */,
				null/* progress */).compile(arguments);
		return succeed ? 0 : -1;
	}
}
