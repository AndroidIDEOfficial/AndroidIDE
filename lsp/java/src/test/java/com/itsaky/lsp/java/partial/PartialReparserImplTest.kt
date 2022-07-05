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

import com.google.common.truth.Truth.assertThat
import com.itsaky.lsp.java.BaseJavaTest
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.compiler.SourceFileObject
import com.itsaky.lsp.java.models.CompilationRequest
import com.itsaky.lsp.java.models.PartialReparseRequest
import com.itsaky.lsp.java.visitors.PrintingVisitor
import com.itsaky.lsp.models.ChangeType.INSERT
import com.itsaky.lsp.models.DocumentChangeEvent
import com.itsaky.lsp.models.Range
import com.sun.source.tree.ExpressionStatementTree
import com.sun.source.tree.LiteralTree
import com.sun.source.tree.Tree
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit
import com.sun.tools.javac.tree.JCTree.JCMethodDecl
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation
import com.sun.tools.javac.tree.JCTree.JCVariableDecl
import com.sun.tools.javac.tree.TreeScanner
import javax.lang.model.type.ArrayType
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PartialReparserImplTest : BaseJavaTest() {

  val jls = mServer as JavaLanguageServer

  @Test
  fun parseMethod() {
    openFile("PartialReparserTest")
    jls.compiler.compile(file).run { task ->
      AssertingScanner().scan(task.root() as JCCompilationUnit)
    }
  }

  @Test
  fun testSimpleErrorneousStatement() {
    openFile("PartialErrReparserTest")
    jls.compiler
      .compile(
        CompilationRequest(
          listOf(SourceFileObject(file)),
          PartialReparseRequest(172, contents.toString())
        )
      )
      .run { PrintingVisitor().scan(it.root() as JCCompilationUnit) }
    jls.onContentChange(
      DocumentChangeEvent(
        file!!,
        contents!!.insert(192, "trim().").toString(),
        2,
        INSERT,
        "trim().".length,
        Range.NONE
      )
    )
    jls.compiler.compile(
      CompilationRequest(
        listOf(SourceFileObject(file)),
        PartialReparseRequest(179, contents.toString())
      )
    )
  }

  override fun openFile(fileName: String) {
    super.openFile("partial/$fileName")
  }

  class AssertingScanner : TreeScanner() {
    private var methodCount = 0
    override fun visitMethodDef(tree: JCMethodDecl?) {
      assertThat(tree).isNotNull()
      tree!!

      if (tree.name.contentEquals("<init>")) {
        // Javac automatically adds the deafult constructor
        methodCount++
        return super.visitMethodDef(tree)
      }

      assertThat(methodCount).isEqualTo(1)
      methodCount++

      val params = tree.params
      assertThat(params).isNotNull()
      assertThat(params.size).isEqualTo(1)

      val argsParam = params[0]
      assertThat(argsParam.name.toString()).isEqualTo("args")
      assertThat(argsParam.type).isInstanceOf(ArrayType::class.java)

      val argType = argsParam.type as ArrayType
      assertThat(argType.componentType.toString()).isEqualTo("java.lang.String")

      val body = tree.body
      assertThat(body).isNotNull()

      val statements = body.statements
      assertThat(statements).isNotNull()
      assertThat(statements.size).isEqualTo(2)

      val println = statements[0]
      assertThat(println).isNotNull()
      assertThat(println.kind).isEqualTo(Tree.Kind.EXPRESSION_STATEMENT)
      println as ExpressionStatementTree

      val arguments = (println.expression as JCMethodInvocation).arguments
      assertThat(arguments).isNotNull()
      assertThat(arguments.size).isEqualTo(1)

      val arg = arguments[0]
      assertThat(arg).isNotNull()
      assertThat(arg.kind).isEqualTo(Tree.Kind.STRING_LITERAL)
      arg as LiteralTree
      assertThat(arg.value).isEqualTo("Hello World!")

      val varDecl = statements[1]
      assertThat(varDecl).isNotNull()
      assertThat(varDecl.kind).isEqualTo(Tree.Kind.VARIABLE)
      varDecl as JCVariableDecl
      assertThat(varDecl.name.toString()).isEqualTo("klass")

      val type = varDecl.type
      assertThat(type).isNotNull()
      assertThat(type.tsym.qualifiedName.toString()).isEqualTo("java.lang.Class")

      val targs = type.typeArguments
      assertThat(targs).isNotNull()
      assertThat(targs.size).isEqualTo(1)

      val tOne = targs[0]
      assertThat(tOne).isNotNull()
      assertThat(tOne.tsym.qualifiedName.toString()).isEqualTo("java.lang.String")

      super.visitMethodDef(tree)
    }
  }
}
