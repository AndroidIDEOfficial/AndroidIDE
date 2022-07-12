/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

// Forked from JavacTaskImpl
package com.itsaky.androidide.javac.services.compiler;

import com.itsaky.androidide.javac.services.CancelService;
import com.itsaky.androidide.javac.services.NBAttr;
import com.itsaky.androidide.javac.services.NBClassFinder;
import com.itsaky.androidide.javac.services.NBEnter;
import com.itsaky.androidide.javac.services.NBJavaCompiler;
import com.itsaky.androidide.javac.services.NBJavacTrees;
import com.itsaky.androidide.javac.services.NBLog;
import com.itsaky.androidide.javac.services.NBMemberEnter;
import com.itsaky.androidide.javac.services.NBParserFactory;
import com.itsaky.androidide.javac.services.NBResolve;
import com.itsaky.androidide.javac.services.NBTreeMaker;
import com.itsaky.androidide.javac.services.util.JavacTaskUtil;
import com.itsaky.androidide.utils.ILogger;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.api.MultiTaskListener;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Annotate;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.CompileStates;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Modules;
import com.sun.tools.javac.main.Arguments;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.DefinedBy;
import com.sun.tools.javac.util.DefinedBy.Api;
import com.sun.tools.javac.util.Log;

import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * A pool of reusable JavacTasks. When a task is no valid anymore, it is returned to the pool, and
 * its Context may be reused for future processing in some cases. The reuse is achieved by replacing
 * some components (most notably JavaCompiler and Log) with reusable counterparts, and by cleaning
 * up leftovers from previous compilation.
 *
 * <p>For each combination of options, a separate task/context is created and kept, as most option
 * values are cached inside components themselves.
 *
 * <p>When the compilation redefines sensitive classes (e.g. classes in the the java.* packages),
 * the task/context is not reused.
 *
 * <p>When the task is reused, then packages that were already listed won't be listed again.
 *
 * <p>Care must be taken to only return tasks that won't be used by the original caller.
 *
 * <p>Care must also be taken when custom components are installed, as those are not cleaned when
 * the task/context is reused, and subsequent getTask may return a task based on a context with
 * these custom components.
 *
 * <p><b>This is NOT part of any supported API. If you write code that depends on this, you do so at
 * your own risk. This code and its internal interfaces are subject to change or deletion without
 * notice.</b>
 */
public class ReusableCompiler {

  private static final ILogger LOG = ILogger.newInstance("ReusableCompiler");
  private static final CancelService cancelService = new CancelServiceImpl();
  private final JavacTool systemProvider = JavacTool.create();
  private final List<String> currentOptions = new ArrayList<>();
  private boolean checkedOut;

  public ReusableContext currentContext;

  /**
   * Creates a new task as if by {@link javax.tools.JavaCompiler#getTask} and runs the provided
   * worker with it. The task is only valid while the worker is running. The internal structures may
   * be reused from some previous compilation.
   *
   * @param fileManager a file manager; if {@code null} use the compiler's standard filemanager
   * @param diagnosticListener a diagnostic listener; if {@code null} use the compiler's default
   *     method for reporting diagnostics
   * @param options compiler options, {@code null} means no options
   * @param classes names of classes to be processed by annotation processing, {@code null} means no
   *     class names
   * @param compilationUnits the compilation units to compile, {@code null} means no compilation
   *     units
   * @return an object representing the compilation
   * @throws RuntimeException if an unrecoverable error occurred in a user supplied component. The
   *     {@linkplain Throwable#getCause() cause} will be the error in user code.
   * @throws IllegalArgumentException if any of the options are invalid, or if any of the given
   *     compilation units are of other kind than {@linkplain JavaFileObject.Kind#SOURCE source}
   */
  public Borrow getTask(
      JavaFileManager fileManager,
      DiagnosticListener<? super JavaFileObject> diagnosticListener,
      Iterable<String> options,
      Iterable<String> classes,
      Iterable<? extends JavaFileObject> compilationUnits) {

    if (checkedOut) {
      throw new RuntimeException("Compiler is already in-use!");
    }

    checkedOut = true;
    List<String> opts =
        StreamSupport.stream(options.spliterator(), false)
            .collect(Collectors.toCollection(ArrayList::new));
    if (!opts.equals(currentOptions)) {
      currentOptions.clear();
      currentOptions.addAll(opts);
      currentContext = new ReusableContext(cancelService);
    }
    JavacTaskImpl task =
        (JavacTaskImpl)
            systemProvider.getTask(
                null,
                fileManager,
                diagnosticListener,
                opts,
                classes,
                compilationUnits,
                currentContext);

    task.addTaskListener(currentContext);

    return new Borrow(task);
  }

  public static class ReusableContext extends Context implements TaskListener {

    private final Set<URI> flowCompleted = new HashSet<>();

    ReusableContext(final CancelService cancelService) {
      super();
      put(Log.logKey, ReusableLog.factory);
      put(JavaCompiler.compilerKey, ReusableJavaCompiler.factory);
      put(JavacFlowListener.flowListenerKey, this::hasFlowCompleted);
      registerNBServices(cancelService);
    }

    public boolean hasFlowCompleted(final JavaFileObject fo) {
      if (fo == null) {
        return false;
      } else {
        try {
          return this.flowCompleted.contains(fo.toUri());
        } catch (Exception e) {
          return false;
        }
      }
    }

    private void registerNBServices(final CancelService cancelService) {
      NBAttr.preRegister(this);
      NBParserFactory.preRegister(this);
      NBTreeMaker.preRegister(this);
      NBJavacTrees.preRegister(this);
      NBResolve.preRegister(this);
      NBEnter.preRegister(this);
      NBMemberEnter.preRegister(this, false);
      NBClassFinder.preRegister(this);
      CancelService.preRegister(this, cancelService);
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public void started(TaskEvent e) {
      // do nothing
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public void finished(TaskEvent e) {
      if (e.getKind() == TaskEvent.Kind.ANALYZE) {
        JCTree.JCCompilationUnit cu = (JCTree.JCCompilationUnit) e.getCompilationUnit();
        if (cu != null && cu.sourcefile != null) {
          flowCompleted.add(cu.sourcefile.toUri());
        }
      }
    }

    void clear() {
      drop(Arguments.argsKey);
      drop(DiagnosticListener.class);
      drop(Log.outKey);
      drop(Log.errKey);
      drop(JavaFileManager.class);
      drop(JavacTask.class);
      drop(JavacTrees.class);
      drop(JavacElements.class);

      if (ht.get(Log.logKey) instanceof ReusableLog) {
        // log already init-ed - not first round
        ((ReusableLog) Log.instance(this)).clear();
        Enter.instance(this).newRound();
        ((ReusableJavaCompiler) ReusableJavaCompiler.instance(this)).clear();
        Types.instance(this).newRound();
        Check.instance(this).newRound();
        Modules.instance(this).newRound();
        Annotate.instance(this).newRound();
        CompileStates.instance(this).clear();
        MultiTaskListener.instance(this).clear();
      }
    }

    <T> void drop(Key<T> k) {
      ht.remove(k);
    }

    <T> void drop(Class<T> c) {
      ht.remove(key(c));
    }

    /**
     * Reusable JavaCompiler; exposes a method to clean up the component from leftovers associated
     * with previous compilations.
     */
    public static class ReusableJavaCompiler extends NBJavaCompiler {

      static final Factory<JavaCompiler> factory = ReusableJavaCompiler::new;

      ReusableJavaCompiler(Context context) {
        super(context);
      }

      @Override
      protected void checkReusable() {
        // do nothing - it's ok to reuse the compiler
      }

      @Override
      public void close() {
        // do nothing
      }

      void clear() {
        newRound();
      }
    }

    /**
     * Reusable Log; exposes a method to clean up the component from leftovers associated with
     * previous compilations.
     */
    static class ReusableLog extends NBLog {

      static final Factory<Log> factory = ReusableLog::new;

      Context context;

      ReusableLog(Context context) {
        super(context, new PrintWriter(System.err));
        this.context = context;
      }

      void clear() {
        recorded.clear();
        sourceMap.clear();
        nerrors = 0;
        nwarnings = 0;
        // Set a fake listener that will lazily lookup the context for the 'real' listener.
        // Since
        // this field is never updated when a new task is created, we cannot simply reset
        // the field
        // or keep old value. This is a hack to workaround the limitations in the current
        // infrastructure.
        diagListener =
            new DiagnosticListener<JavaFileObject>() {
              DiagnosticListener<JavaFileObject> cachedListener;

              @Override
              @DefinedBy(Api.COMPILER)
              @SuppressWarnings("unchecked")
              public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
                if (cachedListener == null) {
                  cachedListener = context.get(DiagnosticListener.class);
                }
                cachedListener.report(diagnostic);
              }
            };
      }
    }
  }

  public class Borrow implements AutoCloseable {

    public final JavacTaskImpl task;
    public boolean closed;

    Borrow(JavacTaskImpl task) {
      this.task = task;
    }

    @Override
    public void close() {
      if (closed) {
        return;
      }
      // not returning the context to the pool if task crashes with an exception
      // the task/context may be in a broken state
      currentContext.clear();
      JavacTaskUtil.cleanup(task);

      checkedOut = false;
      closed = true;
    }
  }
}
