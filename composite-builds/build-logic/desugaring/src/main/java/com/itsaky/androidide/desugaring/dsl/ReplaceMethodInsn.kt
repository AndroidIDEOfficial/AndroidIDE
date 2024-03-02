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

import com.itsaky.androidide.desugaring.internal.dsl.DefaultReplaceMethodInsn
import com.itsaky.androidide.desugaring.utils.ReflectionUtils
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Instruction for replacing usage of a given method.
 *
 * @author Akash Yadav
 */
interface ReplaceMethodInsn {

  /**
   * The owner class name for the method to be replaced. The class name must be
   * in the form of a fully qualified name or in the binary name format.
   */
  var fromClass: String

  /**
   * The name of the method to be replaced.
   */
  var methodName: String

  /**
   * The descriptor of the method to be replaced. This is the method signature
   * as it appears in the bytecode.
   */
  var methodDescriptor: String

  /**
   * The opcode for the method to be replaced. If this is specified, then the
   * opcode for the invoked method will be checked against this and the invocation
   * will only be replaced of the opcode matches.
   *
   * This is optional. By default, the invocation will always be replaced.
   */
  var requireOpcode: MethodOpcode?

  /**
   * The owner class name for the method which will replace the [methodName].
   * The class name must be in the form of a fully qualified name or in the
   * binary name format.
   */
  var toClass: String

  /**
   * The name of the method in [toClass] which will replace the [methodName].
   */
  var toMethod: String

  /**
   * The descriptor of the method in [toClass] which will replace the [methodName].
   */
  var toMethodDescriptor: String

  /**
   * The opcode for invoking [toMethod] in [toClass].
   */
  var toOpcode: MethodOpcode

  class Builder {

    @JvmField
    var fromClass: String = ""

    @JvmField
    var methodName: String = ""

    @JvmField
    var methodDescriptor: String = ""

    @JvmField
    var requireOpcode: MethodOpcode? = null

    @JvmField
    var toClass: String = ""

    @JvmField
    var toMethod: String = ""

    @JvmField
    var toMethodDescriptor: String = ""

    @JvmField
    var toOpcode: MethodOpcode = MethodOpcode.ANY

    fun fromMethod(method: Method) = apply {
      fromClass(method.declaringClass)
      methodName(method.name)
      methodDescriptor(ReflectionUtils.describe(method))

      if (Modifier.isStatic(method.modifiers)) {
        requireOpcode(MethodOpcode.INVOKESTATIC)
      } else {
        requireOpcode(MethodOpcode.INVOKEVIRTUAL)
      }
    }

    fun fromClass(fromClass: String) = apply {
      this.fromClass = fromClass
    }

    fun fromClass(klass: Class<*>): Builder {
      if (klass.isArray || klass.isPrimitive) {
        throw UnsupportedOperationException(
          "Array and primitive types are not supported for desugaring")
      }

      return fromClass(klass.name)
    }

    fun methodName(methodName: String) = apply {
      this.methodName = methodName
    }

    fun methodDescriptor(methodDescriptor: String) = apply {
      this.methodDescriptor = methodDescriptor
    }

    fun requireOpcode(requireOpcode: MethodOpcode) = apply {
      this.requireOpcode = requireOpcode
    }

    fun toClass(toClass: String) = apply {
      this.toClass = toClass
    }

    fun toClass(klass: Class<*>): Builder {
      if (klass.isArray || klass.isPrimitive) {
        throw UnsupportedOperationException(
          "Array and primitive types are not supported for desugaring")
      }

      return toClass(klass.name)
    }

    fun toMethod(toMethod: String) = apply {
      this.toMethod = toMethod
    }

    fun toMethodDescriptor(toMethodDescriptor: String) = apply {
      this.toMethodDescriptor = toMethodDescriptor
    }

    fun toMethod(method: Method) = apply {
      toClass(method.declaringClass)
      toMethod(method.name)
      toMethodDescriptor(ReflectionUtils.describe(method))

      if (Modifier.isStatic(method.modifiers)) {
        toOpcode(MethodOpcode.INVOKESTATIC)
      } else {
        toOpcode(MethodOpcode.INVOKEVIRTUAL)
      }
    }

    fun toOpcode(toOpcode: MethodOpcode) = apply {
      this.toOpcode = toOpcode
    }

    fun build(): DefaultReplaceMethodInsn {
      require(fromClass.isNotBlank()) { "fromClass cannot be blank." }
      require(methodName.isNotBlank()) { "methodName cannot be blank." }
      require(
        methodDescriptor.isNotBlank()) { "methodDescriptor cannot be blank." }
      require(toClass.isNotBlank()) { "toClass cannot be blank." }
      require(toMethod.isNotBlank()) { "toMethod cannot be blank." }
      require(
        toMethodDescriptor.isNotBlank()) { "toMethodDescriptor cannot be blank." }
      require(toOpcode != MethodOpcode.ANY) { "toOpcode cannot be ANY." }

      return DefaultReplaceMethodInsn(fromClass, methodName, methodDescriptor,
        requireOpcode, toClass, toMethod, toMethodDescriptor, toOpcode)
    }
  }

  companion object {

    @JvmStatic
    fun builder(): Builder = Builder()

    /**
     * Creates a [Builder] for the given source and target method.
     */
    @JvmStatic
    fun forMethods(fromMethod: Method, toMethod: Method
    ): Builder {
      return builder().fromMethod(fromMethod).toMethod(toMethod)
    }
  }
}