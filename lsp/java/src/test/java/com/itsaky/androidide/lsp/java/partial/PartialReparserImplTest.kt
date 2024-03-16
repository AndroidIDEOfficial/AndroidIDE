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

package com.itsaky.androidide.lsp.java.partial

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.eventbus.events.editor.ChangeType.INSERT
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.lsp.java.JavaLSPTest
import com.itsaky.androidide.lsp.java.compiler.SourceFileObject
import com.itsaky.androidide.lsp.java.models.CompilationRequest
import com.itsaky.androidide.lsp.java.models.PartialReparseRequest
import com.itsaky.androidide.lsp.java.visitors.PrintingVisitor
import com.itsaky.androidide.models.Range
import jdkx.lang.model.type.ArrayType
import openjdk.source.tree.ExpressionStatementTree
import openjdk.source.tree.LiteralTree
import openjdk.source.tree.Tree
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit
import openjdk.tools.javac.tree.JCTree.JCMethodDecl
import openjdk.tools.javac.tree.JCTree.JCMethodInvocation
import openjdk.tools.javac.tree.JCTree.JCVariableDecl
import openjdk.tools.javac.tree.TreeScanner
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
@Ignore("Partial reparser is currently unused")
class PartialReparserImplTest {

  @Before
  fun setup() {
    JavaLSPTest.setup()
  }

  @Test
  fun parseMethod() {
    JavaLSPTest.apply {
      openFile("partial/PartialReparserTest")
      getCompiler().compile(file).run { task ->
        AssertingScanner().scan(task.root() as JCCompilationUnit)
      }
    }
  }

  @Test
  fun testSimpleErrorneousStatement() {
    JavaLSPTest.apply {
      openFile("partial/PartialErrReparserTest")
      getCompiler()
        .compile(
          CompilationRequest(
            listOf(SourceFileObject(file)),
            PartialReparseRequest(172, contents.toString())
          )
        )
        .run { PrintingVisitor().scan(it.root() as JCCompilationUnit) }
      val changedText = contents!!.insert(192, "trim().").toString()
      dispatchEvent(
        DocumentChangeEvent(
          file!!,
          changedText,
          changedText,
          2,
          INSERT,
          "trim().".length,
          Range.NONE
        )
      )
      getCompiler()
        .compile(
          CompilationRequest(
            listOf(SourceFileObject(file)),
            PartialReparseRequest(179, contents.toString())
          )
        )
    }
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
