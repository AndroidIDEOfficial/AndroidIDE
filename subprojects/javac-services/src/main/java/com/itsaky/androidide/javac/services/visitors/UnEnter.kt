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
package com.itsaky.androidide.javac.services.visitors

import com.blankj.utilcode.util.ReflectUtils
import openjdk.tools.javac.code.Symbol.ModuleSymbol
import openjdk.tools.javac.comp.Enter
import openjdk.tools.javac.tree.JCTree.JCClassDecl
import openjdk.tools.javac.tree.TreeScanner

/**
 * `Enter.unenter` method is not available in JDK 11. This visitor does the same thing as calling
 * `Enter.unenter` in JDK 17.
 *
 * This is never used in the Android Runtime.
 *
 * @author Akash Yadav
 */
class UnEnter(private val enter: Enter, private val msym: ModuleSymbol) : TreeScanner() {
  override fun visitClassDef(tree: JCClassDecl) {
    val csym = tree.sym ?: return

    val etr = ReflectUtils.reflect(enter)
    ReflectUtils.reflect(etr.field("typeEnvs")).method("remove", csym)

    val chk = etr.field("chk")
    ReflectUtils.reflect(chk).method("removeCompiled", csym)
    ReflectUtils.reflect(chk).method("clearLocalClassNameIndexes", csym)

    ReflectUtils.reflect(etr.field("syms")).method("removeClass", msym, csym.flatname)
    super.visitClassDef(tree)
  }
}
