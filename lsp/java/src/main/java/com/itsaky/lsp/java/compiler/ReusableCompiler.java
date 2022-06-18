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
package com.itsaky.lsp.java.compiler;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.utils.TestUtils;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.api.MultiTaskListener;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.comp.Annotate;
import com.sun.tools.javac.comp.Check;
import com.sun.tools.javac.comp.CompileStates;
import com.sun.tools.javac.comp.Enter;
import com.sun.tools.javac.comp.Modules;
import com.sun.tools.javac.main.Arguments;
import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.model.LazyTreeLoader;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.DefinedBy;
import com.sun.tools.javac.util.DefinedBy.Api;
import com.sun.tools.javac.util.Log;

import org.netbeans.lib.nbjavac.services.NBAttr;
import org.netbeans.lib.nbjavac.services.NBClassFinder;
import org.netbeans.lib.nbjavac.services.NBEnter;
import org.netbeans.lib.nbjavac.services.NBJavaCompiler;
import org.netbeans.lib.nbjavac.services.NBJavacTrees;
import org.netbeans.lib.nbjavac.services.NBLog;
import org.netbeans.lib.nbjavac.services.NBMemberEnter;
import org.netbeans.lib.nbjavac.services.NBParserFactory;
import org.netbeans.lib.nbjavac.services.NBResolve;
import org.netbeans.lib.nbjavac.services.NBTreeMaker;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
  private static final JavacTool systemProvider = JavacTool.create();

  private final List<String> currentOptions = new ArrayList<>();
  private ReusableContext currentContext;
  private boolean checkedOut;

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
  Borrow getTask(
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
      final ArrayList<String> newOpts = new ArrayList<>(currentOptions);
      newOpts.removeAll(opts);
      LOG.debug("New compiler options:", newOpts);

      currentOptions.clear();
      currentOptions.addAll(opts);
      currentContext = new ReusableContext(new ArrayList<>(opts));
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

  private static class IDELazyTreeLoader extends LazyTreeLoader {
    @Override
    public boolean loadTreeFor(Symbol.ClassSymbol clazz, boolean persist) {
      LOG.debug("loadTreeFor:", clazz, persist);
      return true;
    }

    @Override
    public boolean loadParamNames(Symbol.ClassSymbol clazz) {
      LOG.debug("loadParamNames:", clazz);
      return true;
    }

    @Override
    public void couplingError(Symbol.ClassSymbol clazz, Tree t) {
      LOG.debug("Coupling error:", clazz, t);
    }

    @Override
    public void updateContext(Context context) {
      LOG.debug("updateContext:", context);
    }
  }

  static class ReusableContext extends Context implements TaskListener {

    List<String> arguments;

    ReusableContext(List<String> arguments) {
      super();
      this.arguments = arguments;
      put(Log.logKey, ReusableLog.factory);
      put(JavaCompiler.compilerKey, ReusableJavaCompiler.factory);
      registerNBServices();
    }

    private void registerNBServices() {
      NBAttr.preRegister(this);
      NBParserFactory.preRegister(this);
      NBTreeMaker.preRegister(this);
      NBJavacTrees.preRegister(this);
      NBResolve.preRegister(this);
      NBEnter.preRegister(this);
      NBMemberEnter.preRegister(this, false);
      NBClassFinder.preRegister(this);

      if (!TestUtils.isTestEnvironment()) {
        put(LazyTreeLoader.lazyTreeLoaderKey, new IDELazyTreeLoader());
      }
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public void started(TaskEvent e) {
      // do nothing
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public void finished(TaskEvent e) {
      // do nothing
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
    static class ReusableJavaCompiler extends NBJavaCompiler {

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

  class Borrow implements AutoCloseable {

    final JavacTaskImpl task;
    boolean closed;

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
      try {
        Method method = JavacTaskImpl.class.getDeclaredMethod("cleanup");
        method.setAccessible(true);
        method.invoke(task);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Unable to call cleanup() on JavacTaskImpl", e);
      }

      checkedOut = false;
      closed = true;
    }
  }
}
