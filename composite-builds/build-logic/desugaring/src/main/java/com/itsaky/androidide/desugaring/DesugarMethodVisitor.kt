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

package com.itsaky.androidide.desugaring

import com.android.build.api.instrumentation.ClassContext
import com.itsaky.androidide.desugaring.dsl.MethodOpcode
import com.itsaky.androidide.desugaring.dsl.ReplaceMethodInsn
import com.itsaky.androidide.desugaring.dsl.ReplaceMethodInsnKey
import org.objectweb.asm.MethodVisitor
import org.slf4j.LoggerFactory

class DesugarMethodVisitor @JvmOverloads constructor(
  private val params: DesugarParams, private val classContext: ClassContext,
  api: Int, methodVisitor: MethodVisitor? = null
) : MethodVisitor(api, methodVisitor) {

  companion object {

    private val log = LoggerFactory.getLogger(DesugarMethodVisitor::class.java)
  }

  override fun visitMethodInsn(opcode: Int, owner: String?, name: String?,
    descriptor: String?, isInterface: Boolean
  ) {
    val replacement =
      findReplacementForClsOrSupercls(opcode, owner, name, descriptor)
    if (replacement == null) {
      super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
      return
    }

    val requireOpcode = replacement.requireOpcode ?: MethodOpcode.ANY
    val methodDesc = "${requireOpcode.insnName} ${owner}->${name}${descriptor}"

    val toClass =
      replacement.toClass.requireValue("toClass", methodDesc).replace('.', '/')

    val toMethod = replacement.toMethod.requireValue("toMethod", methodDesc)
    val toDescriptor =
      replacement.toMethodDescriptor.requireValue("toMethodDescriptor",
        methodDesc)
    val toOpcode = replacement.toOpcode.requireValue("toOpcode", methodDesc)

    log.debug(
      "Replacing $methodDesc with ${toOpcode.insnName} ${toClass}->${toMethod}${toDescriptor}")

    super.visitMethodInsn(toOpcode.opcode, toClass, toMethod, toDescriptor,
      false)
  }

  private fun findReplacementForClsOrSupercls(opcode: Int, owner: String?,
    method: String?,
    descriptor: String?
  ): ReplaceMethodInsn? {
    if (owner == null || method == null || descriptor == null) {
      return null
    }

    // case 1: check if we have a replacement for this class
    val thisReplacement = findReplacement(opcode, owner, method, descriptor)
    if (thisReplacement != null) {
      return thisReplacement
    }

    val classData =
      classContext.loadClassData(owner.replace('/', '.')) ?: return null

    // case 2: check if we have a replacement for a superclass
    for (superclass in classData.superClasses) {
      val replacement =
        findReplacementForClsOrSupercls(opcode, superclass, method, descriptor)
      if (replacement != null) {
        return replacement
      }
    }

    // case 3: check if we have a replacement for an super-interface
    for (iface in classData.interfaces) {
      val replacement =
        findReplacementForClsOrSupercls(opcode, iface, method, descriptor)
      if (replacement != null) {
        return replacement
      }
    }

    // case 4: no replacement is available
    return null
  }

  private fun findReplacement(opcode: Int, owner: String?, method: String?,
    descriptor: String?
  ): ReplaceMethodInsn? {
    val replacements = params.replacements.get()
    if (owner == null || method == null || descriptor == null) {
      return null
    }

    val key = ReplaceMethodInsnKey(owner.replace('/', '.'), method, descriptor)
    val replaceMethodInsn = replacements[key] ?: return null

    return replaceMethodInsn.takeIf {
      val methodOpcode = it.requireOpcode ?: MethodOpcode.ANY
      val methodDescriptor = it.methodDescriptor

      (methodOpcode == MethodOpcode.ANY || methodOpcode.opcode == opcode) && methodDescriptor == descriptor
    }
  }

  private fun <T : Any?> T.requireValue(propName: String, methodDesc: String
  ): T {
    return this?.let {
      if (it is String && it.isBlank()) {
        null
      } else {
        it
      }
    } ?: throw IllegalArgumentException(
      "No value specified for '${propName}' (for method ${methodDesc}).")
  }
}