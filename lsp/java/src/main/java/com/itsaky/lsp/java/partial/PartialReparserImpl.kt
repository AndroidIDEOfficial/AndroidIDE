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

package com.itsaky.lsp.java.partial

import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.java.compiler.JavacFlowListener
import com.itsaky.lsp.java.visitors.FindAnonymousVisitor
import com.itsaky.lsp.java.visitors.TranslateMethodPositionsVisitor
import com.itsaky.lsp.java.visitors.UnEnter
import com.sun.source.tree.BlockTree
import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.Tree.Kind.BLOCK
import com.sun.source.tree.Tree.Kind.METHOD
import com.sun.source.util.TreePath
import com.sun.tools.javac.api.JavacScope
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.code.Flags
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Symtab
import com.sun.tools.javac.comp.Attr
import com.sun.tools.javac.comp.AttrContext
import com.sun.tools.javac.comp.Enter
import com.sun.tools.javac.comp.Env
import com.sun.tools.javac.comp.Flow
import com.sun.tools.javac.comp.TypeEnter
import com.sun.tools.javac.parser.JavacParser
import com.sun.tools.javac.parser.LazyDocCommentTable
import com.sun.tools.javac.parser.ScannerFactory
import com.sun.tools.javac.tree.EndPosTable
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.JCTree.JCBlock
import com.sun.tools.javac.tree.JCTree.JCClassDecl
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit
import com.sun.tools.javac.tree.JCTree.JCMethodDecl
import com.sun.tools.javac.tree.TreeInfo
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.List
import com.sun.tools.javac.util.Log
import com.sun.tools.javac.util.Names
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.nio.CharBuffer
import java.util.*
import org.netbeans.lib.nbjavac.services.CancelService
import org.netbeans.lib.nbjavac.services.NBLog
import org.netbeans.lib.nbjavac.services.NBParserFactory

/**
 * Partial reparser implementation.
 *
 * @author Akash Yadav
 */
class PartialReparserImpl : PartialReparser {

  private val log = ILogger.newInstance(javaClass.simpleName)
  private var unenter: Method?
  private var lazyDocCommentsTable: Field?
  private var parserDocComments: Field?
  private var lineMapBuild: Method?
  private var allowPartialReparse: Boolean
  private var useUnEnterScanner: Boolean

  init {
    try {
      // These methods can be easily accessed without reflection in Android Runtime
      // But not in JVM (for tests)
      try {
        this.unenter =
          Enter::class
            .java
            .getDeclaredMethod("unenter", JCCompilationUnit::class.java, JCTree::class.java)
        this.unenter!!.isAccessible = true
        this.useUnEnterScanner = false
      } catch (methodNotFound: NoSuchMethodException) {
        this.unenter = null
        this.useUnEnterScanner = true
      }
      this.lazyDocCommentsTable = LazyDocCommentTable::class.java.getDeclaredField("table")
      this.lazyDocCommentsTable!!.isAccessible = true
      this.parserDocComments = JavacParser::class.java.getDeclaredField("docComments")
      this.parserDocComments!!.isAccessible = true
      this.lineMapBuild =
        Class.forName("com.sun.tools.javac.util.Position\$LineMapImpl")
          .getDeclaredMethod("build", CharArray::class.java, Int::class.java)
      this.lineMapBuild!!.isAccessible = true
      this.allowPartialReparse = true
    } catch (err: Throwable) {
      log.error(err)
      this.unenter = null
      this.lazyDocCommentsTable = null
      this.parserDocComments = null
      this.lineMapBuild = null
      this.allowPartialReparse = false
      this.useUnEnterScanner = false
    }
  }

  override fun reparseMethod(
    ci: CompilationInfo,
    methodPath: TreePath,
    newBody: String,
    fileContents: CharSequence
  ): Boolean {

    if (!allowPartialReparse) {
      log.debug("Partial reparse is disabled")
      return false
    }

    val cu = ci.cu
    val fo = cu.sourceFile
    val task = ci.task
    val method = methodPath.leaf as JCMethodDecl
    if (methodPath.leaf.kind != METHOD) {
      log.warn("Provided TreePath does not correspond to a MethodTree")
      return false
    }

    val methodScope = ci.trees.getScope(TreePath(methodPath, method.body))
    val jt = JavacTrees.instance(task)
    val origStartPos = jt.sourcePositions.getStartPosition(cu, method.body).toInt()
    val origEndPos = jt.sourcePositions.getEndPosition(cu, method.body).toInt()
    if (origStartPos < 0) {
      log.warn("Javac returned start position $origStartPos < 0")
      return false
    }

    if (origStartPos > origEndPos) {
      log.warn("Java returned start position: $origStartPos > end position: $origEndPos")
      return false
    }

    val fav = FindAnonymousVisitor()
    fav.scan(method.body, null)
    if (fav.hasLocalClass) {
      log.debug("Skipped reparse method. Old local classes.", fo)
      return false
    }

    val noInner = fav.noInner
    val context = task.context

    try {
      val l = NBLog.instance(context)
      l.startPartialReparse(fo)
      val prevLogged = l.useSource(fo)
      val block: JCBlock?

      try {
        val dl = ci.diagnosticListener as DiagnosticListenerImpl
        dl.startPartialReparse(origStartPos, origEndPos)
        val docComments = HashMap<JCTree, Any>()
        log.debug("Reparse method...")
        block = reparseMethodBody(context, cu, method, newBody, docComments)
        val endPosTable = (cu as JCCompilationUnit).endPositions
        if (block == null) {
          log.debug("Skipped reparse method. Invalid position, newBody: $newBody")
          return false
        }

        val sourcePositions = jt.sourcePositions
        val newEndPos = sourcePositions.getEndPosition(cu, block).toInt()
        if (newEndPos != origStartPos + newBody.length) {
          log.warn("Invalid position in reparsed method")
          return false
        }

        fav.reset()
        fav.scan(block, null)

        val newNoInner = fav.noInner
        if (fav.hasLocalClass || noInner != newNoInner) {
          log.debug("Skipped method reparse (new local class): $fo")
          return false
        }

        val docCommentsTable = lazyDocCommentsTable!!.get(cu.docComments) as MutableMap<JCTree, Any>
        docCommentsTable.keys.removeAll(fav.docOwners)
        docCommentsTable.putAll(docComments)
        val delta = newEndPos - origEndPos
        val tpv = TranslateMethodPositionsVisitor(method, endPosTable, delta)
        tpv.scan(cu, null)
        doUnenter(context, cu, method)
        method.body = block

        log.debug("ReAttr method...")
        reAttrMethodBody(context, methodScope, method, block)

        if (!dl.hasPartialReparseErrors()) {
          val fl = JavacFlowListener.instance(context)
          if (fl != null && fl.hasFlowCompleted(cu.sourceFile)) {
            log.debug("Reflow method...")
            val tp = TreePath.getPath(cu, method)
            val t = tp.parentPath.leaf as ClassTree
            reflowMethodBody(context, t, method)
          }
        }

        val arr = CharArray(fileContents.length)
        for (index in fileContents.indices) {
          arr[index] = fileContents[index]
        }

        lineMapBuild!!.invoke(cu.getLineMap(), arr, arr.size)
        dl.endPartialReparse(delta)
      } finally {
        l.endPartialReparse(cu.sourceFile)
        l.useSource(prevLogged)
      }
    } catch (err: Throwable) {
      return false
    }

    return true
  }

  private fun doUnenter(context: Context?, cu: CompilationUnitTree, method: JCMethodDecl) {
    if (cu !is JCCompilationUnit) {
      throw IllegalStateException()
    }

    val enter = Enter.instance(context)
    if (this.unenter != null) {
      this.unenter!!.invoke(enter, cu, method.body)
    } else if (useUnEnterScanner) {
      UnEnter(enter, cu.modle).scan(method)
    }
  }

  private fun reparseMethodBody(
    context: Context,
    cu: CompilationUnitTree,
    method: JCMethodDecl,
    newBody: String,
    docComments: MutableMap<JCTree, Any>,
  ): JCBlock? {
    val startPos = method.body.pos
    val body = CharArray(startPos + newBody.length + 1)
    Arrays.fill(body, ' ')
    for (index in newBody.indices) {
      body[startPos + index] = newBody[index]
    }
    body[startPos + newBody.length] = '\u0000'
    val buf = CharBuffer.wrap(body, 0, body.size - 1)
    val parser = newParser(context, buf, method.body.pos, (cu as JCCompilationUnit).endPositions)
    val statement = parser.parseStatement()
    if (statement.kind == BLOCK) {
      docComments.putAll(
        lazyDocCommentsTable!!.get(parserDocComments!!.get(parser)) as MutableMap<JCTree, Any>
      )
      return statement as JCBlock
    }
    
    log.warn("JavacParser parsed invalid statment. Block statement was expected...")
    return null
  }

  private fun newParser(
    context: Context,
    buf: CharBuffer?,
    pos: Int,
    endPositions: EndPosTable?,
  ): JavacParser {
    val factory = NBParserFactory.instance(context) as NBParserFactory
    val scannerFactory = ScannerFactory.instance(context)
    val cancelService = CancelService.instance(context)
    val lexer = scannerFactory.newScanner(buf, true)
    if (endPositions is NBParserFactory.NBJavacParser.EndPosTableImpl) {
      endPositions.resetErrorEndPos()
    }

    return object :
      NBParserFactory.NBJavacParser(factory, lexer, true, false, true, false, cancelService) {
      override fun newEndPosTable(keepEndPositions: Boolean): AbstractEndPosTable {
        return object : AbstractEndPosTable(this) {
          override fun storeEnd(tree: JCTree?, endpos: Int) {
            (endPositions as EndPosTableImpl).storeEnd(tree, endpos)
          }

          override fun <T : JCTree?> to(t: T): T {
            storeEnd(t, token.endPos)
            return t
          }

          override fun <T : JCTree?> toP(t: T): T {
            storeEnd(t, S.prevToken().endPos)
            return t
          }

          override fun getEndPos(tree: JCTree?): Int {
            return endPositions!!.getEndPos(tree)
          }

          override fun replaceTree(oldtree: JCTree?, newtree: JCTree?): Int {
            return endPositions!!.replaceTree(oldtree, newtree)
          }

          override fun setErrorEndPos(errPos: Int) {
            super.setErrorEndPos(errPos)
            (endPositions as EndPosTableImpl).setErrorEndPos(errPos)
          }
        }
      }
    }
  }

  private fun reAttrMethodBody(
    context: Context,
    scope: JavacScope,
    tree: JCMethodDecl,
    block: JCBlock,
  ): JCBlock {
    val attr = Attr.instance(context)
    val names: Names = Names.instance(context)
    val syms: Symtab = Symtab.instance(context)
    val typeEnter: TypeEnter = TypeEnter.instance(context)
    val log: Log = Log.instance(context)
    val make: TreeMaker = TreeMaker.instance(context)
    val env: Env<AttrContext> = scope.env // this is a copy anyway...
    val owner: Symbol.ClassSymbol = env.enclClass.sym

    if (tree.name === names.init && !owner.type.isErroneous && owner.type !== syms.objectType) {
      val body = tree.body
      if (body.stats.isEmpty() || !TreeInfo.isSelfCall(body.stats.head)) {
        body.stats =
          body.stats.prepend(
            make.at(body.pos).Exec(make.Apply(List.nil(), make.Ident(names._super), List.nil()))
          )
      } else if (
        (env.enclClass.sym.flags() and Flags.ENUM.toLong()) != 0L &&
          (tree.mods.flags and Flags.GENERATEDCONSTR) == 0L &&
          TreeInfo.isSuperCall(body.stats.head)
      ) {
        // enum constructors are not allowed to call super
        // directly, so make sure there aren't any super calls
        // in enum constructorObjectCheckers, except in the compiler
        // generated one.
        log.error(
          tree.body.stats.head.pos(),
          com.sun.tools.javac.util.JCDiagnostic.Error(
            "compiler",
            "call.to.super.not.allowed.in.enum.ctor",
            env.enclClass.sym
          )
        )
      }
    }
    attr.attribStat(block, env)
    return block
  }

  private fun reflowMethodBody(
    context: Context,
    ownerClass: ClassTree,
    decl: JCMethodDecl
  ): BlockTree {
    val flow = Flow.instance(context)
    val maker = TreeMaker.instance(context)
    val enter = Enter.instance(context)
    flow.analyzeTree(enter.getEnv((ownerClass as JCClassDecl).sym), maker)
    return decl.body
  }
}