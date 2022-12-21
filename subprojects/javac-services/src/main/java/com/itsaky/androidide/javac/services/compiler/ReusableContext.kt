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

package com.itsaky.androidide.javac.services.compiler

import com.itsaky.androidide.javac.services.CancelService
import com.itsaky.androidide.javac.services.NBAttr
import com.itsaky.androidide.javac.services.NBClassFinder
import com.itsaky.androidide.javac.services.NBClassReader
import com.itsaky.androidide.javac.services.NBEnter
import com.itsaky.androidide.javac.services.NBJavacTrees
import com.itsaky.androidide.javac.services.NBMemberEnter
import com.itsaky.androidide.javac.services.NBParserFactory
import com.itsaky.androidide.javac.services.NBResolve
import com.itsaky.androidide.javac.services.NBTreeMaker
import com.itsaky.androidide.javac.services.fs.CacheFSInfoSingleton
import com.itsaky.androidide.javac.services.fs.JarPackageProviderImpl
import com.itsaky.androidide.utils.VMUtils
import com.itsaky.androidide.zipfs2.JarPackageProvider
import com.sun.source.util.JavacTask
import com.sun.source.util.TaskEvent
import com.sun.source.util.TaskEvent.Kind.ANALYZE
import com.sun.source.util.TaskListener
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.api.MultiTaskListener
import com.sun.tools.javac.code.Types
import com.sun.tools.javac.comp.Annotate
import com.sun.tools.javac.comp.Check
import com.sun.tools.javac.comp.CompileStates
import com.sun.tools.javac.comp.Enter
import com.sun.tools.javac.comp.Modules
import com.sun.tools.javac.file.CacheFSInfo
import com.sun.tools.javac.file.FSInfo
import com.sun.tools.javac.main.Arguments
import com.sun.tools.javac.main.JavaCompiler
import com.sun.tools.javac.model.JavacElements
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.DefinedBy
import com.sun.tools.javac.util.DefinedBy.Api.COMPILER_TREE
import com.sun.tools.javac.util.Log
import java.io.PrintWriter
import java.net.URI
import javax.tools.DiagnosticListener
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject

/**
 * Reusable [Context] for [ReusableCompiler].
 * @author Akash Yadav
 */
class ReusableContext(cancelService: CancelService) : Context(), TaskListener {

  private val flowCompleted = mutableSetOf<URI>()

  init {
    put(Log.logKey, ReusableLog.factory)
    put(FSInfo::class.java, if (VMUtils.isJvm()) CacheFSInfo() else CacheFSInfoSingleton)
    put(JavaCompiler.compilerKey, ReusableJavaCompiler.factory)
    put(JavacFlowListener.flowListenerKey, JavacFlowListener { this.hasFlowCompleted(it) })
    put(JarPackageProvider::class.java, JarPackageProviderImpl)
    registerNBServices(cancelService)
  }

  @DefinedBy(COMPILER_TREE)
  override fun started(e: TaskEvent) {
    //    log.debug("Started: $e")
    // Do nothing
  }

  @DefinedBy(COMPILER_TREE)
  override fun finished(e: TaskEvent) {
    if (e.kind == ANALYZE) {
      val cu = e.compilationUnit as JCCompilationUnit
      if (cu.sourcefile != null) {
        flowCompleted.add(cu.sourcefile.toUri())
      }
    }
  }

  fun clear() {
    drop(Arguments.argsKey)
    drop(DiagnosticListener::class.java)
    drop(Log.outKey)
    drop(Log.errKey)
    drop(JavaFileManager::class.java)
    drop(JavacTask::class.java)
    drop(JavacTrees::class.java)
    drop(JavacElements::class.java)

    if (ht[Log.logKey] is ReusableLog) {
      // log already init-ed - not first round
      (Log.instance(this) as ReusableLog).clear()
      Enter.instance(this).newRound()
      (JavaCompiler.instance(this) as ReusableJavaCompiler).clear()
      Types.instance(this).newRound()
      Check.instance(this).newRound()
      Modules.instance(this).newRound()
      Annotate.instance(this).newRound()
      CompileStates.instance(this).clear()
      MultiTaskListener.instance(this).clear()
    }
  }

  private fun <T> drop(k: Key<T>?) {
    ht.remove(k)
  }

  private fun <T> drop(c: Class<T>?) {
    ht.remove(key(c))
  }

  private fun hasFlowCompleted(fo: JavaFileObject?): Boolean {
    return if (fo == null) {
      false
    } else {
      try {
        this.flowCompleted.contains(fo.toUri())
      } catch (e: Exception) {
        false
      }
    }
  }

  private fun registerNBServices(cancelService: CancelService) {
    NBAttr.preRegister(this)
    NBParserFactory.preRegister(this)
    NBTreeMaker.preRegister(this)
    NBJavacTrees.preRegister(this)
    NBResolve.preRegister(this)
    NBEnter.preRegister(this)
    NBMemberEnter.preRegister(this, false)
    NBClassFinder.preRegister(this)
    NBClassReader.preRegister(this)
    CancelService.preRegister(this, cancelService)
  }
}
