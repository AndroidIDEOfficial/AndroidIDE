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
import jdkx.tools.DiagnosticListener
import jdkx.tools.JavaFileManager
import jdkx.tools.JavaFileObject
import openjdk.source.util.JavacTask
import openjdk.source.util.TaskEvent
import openjdk.source.util.TaskEvent.Kind.ANALYZE
import openjdk.source.util.TaskListener
import openjdk.tools.javac.api.JavacTrees
import openjdk.tools.javac.api.MultiTaskListener
import openjdk.tools.javac.code.Types
import openjdk.tools.javac.comp.Annotate
import openjdk.tools.javac.comp.Check
import openjdk.tools.javac.comp.CompileStates
import openjdk.tools.javac.comp.Enter
import openjdk.tools.javac.comp.Modules
import openjdk.tools.javac.file.CacheFSInfo
import openjdk.tools.javac.file.FSInfo
import openjdk.tools.javac.main.Arguments
import openjdk.tools.javac.main.JavaCompiler
import openjdk.tools.javac.model.JavacElements
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit
import openjdk.tools.javac.util.Context
import openjdk.tools.javac.util.DefinedBy
import openjdk.tools.javac.util.DefinedBy.Api.COMPILER_TREE
import openjdk.tools.javac.util.Log
import java.net.URI

/**
 * Reusable [Context] for [ReusableCompiler].
 *
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
  
  /** **FOR INTERNAL USE ONLY!** */
  fun <T> drop(k: Key<T>?) {
    ht.remove(k)
  }
  
  /** **FOR INTERNAL USE ONLY!** */
  fun <T> drop(c: Class<T>?) {
    drop(key(c))
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
}
