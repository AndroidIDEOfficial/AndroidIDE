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

package com.itsaky.androidide.desugaring.dsl

import com.itsaky.androidide.desugaring.internal.parsing.InsnLexer
import com.itsaky.androidide.desugaring.internal.parsing.InsnParser
import com.itsaky.androidide.desugaring.utils.ReflectionUtils
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import java.io.File
import java.lang.reflect.Method
import java.util.TreeSet
import javax.inject.Inject

/**
 * Defines replacements for desugaring.
 *
 * @author Akash Yadav
 */
abstract class DesugarReplacementsContainer @Inject constructor(
  private val objects: ObjectFactory
) {

  internal val includePackages = TreeSet<String>()

  internal val instructions =
    mutableMapOf<ReplaceMethodInsnKey, ReplaceMethodInsn>()

  companion object {

    private val PACKAGE_NAME_REGEX =
      Regex("""^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*${'$'}""")
  }

  /**
   * Adds the given packages to the list of packages that will be scanned for
   * the desugaring process. By default, the list of packages is empty. An empty
   * list will include all packages.
   */
  fun includePackage(vararg packages: String) {
    for (pck in packages) {
      if (!PACKAGE_NAME_REGEX.matches(pck)) {
        throw IllegalArgumentException("Invalid package name: $pck")
      }

      includePackages.add(pck)
    }
  }

  /**
   * Removes the given packages from the list of included packages.
   */
  fun removePackage(vararg packages: String) {
    includePackages.removeAll(packages.toSet())
  }

  /**
   * Adds an instruction to replace the given method.
   */
  fun replaceMethod(configure: Action<ReplaceMethodInsn>) {
    val instruction = objects.newInstance(ReplaceMethodInsn::class.java)
    configure.execute(instruction)
    addReplaceInsns(instruction)
  }

  /**
   * Replace usage of [sourceMethod] with the [targetMethod].
   */
  @JvmOverloads
  fun replaceMethod(
    sourceMethod: Method,
    targetMethod: Method,
    configure: Action<ReplaceMethodInsn> = Action {}
  ) {
    val instruction = ReplaceMethodInsn.forMethods(sourceMethod, targetMethod).build()
    configure.execute(instruction)
    if (instruction.requireOpcode == MethodOpcode.INVOKEVIRTUAL
      && instruction.toOpcode == MethodOpcode.INVOKESTATIC
    ) {
      ReflectionUtils.validateVirtualToStaticReplacement(sourceMethod, targetMethod)
    }
    addReplaceInsns(instruction)
  }

  /**
   * Load instructions from the given file.
   */
  fun loadFromFile(file: File) {
    val lexer = InsnLexer(file.readText())
    val parser = InsnParser(lexer)
    val insns = parser.parse()
    addReplaceInsns(insns)
  }

  private fun addReplaceInsns(vararg insns: ReplaceMethodInsn
  ) {
    addReplaceInsns(insns.asIterable())
  }

  private fun addReplaceInsns(insns: Iterable<ReplaceMethodInsn>
  ) {
    for (insn in insns) {
      val className = insn.fromClass.replace('/', '.')
      val methodName = insn.methodName
      val methodDescriptor = insn.methodDescriptor

      insn.requireOpcode ?: run {
        insn.requireOpcode = MethodOpcode.ANY
      }

      val key = ReplaceMethodInsnKey(className, methodName, methodDescriptor)
      this.instructions[key] = insn
    }
  }
}