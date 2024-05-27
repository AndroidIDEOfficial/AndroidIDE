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

package com.itsaky.androidide.lsp.java.utils

import com.itsaky.androidide.lsp.java.indexing.IJavaType
import com.itsaky.androidide.lsp.java.indexing.classfile.AnnotationElement
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaField
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaMethod
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaType
import java.util.Objects

/**
 * @author Akash Yadav
 */
object DescriptorUtils {

  /**
   * Create a method ID for the given method.
   */
  fun createMethodId(
    klass: IJavaType<*, *>,
    method: JavaMethod
  ): Int {
    return Objects.hash(klass.fqn, method.name, method.paramsTypes, method.returnType)
  }

  /**
   * Create a method ID for the given method.
   */
  fun createMethodId(
    klass: IJavaType<*, *>,
    element: AnnotationElement
  ): Int {
    return Objects.hash(klass.fqn, element.name, element.type)
  }

  /**
   * Create a field ID for the given field. The string representation is similar to how fields
   * are represented in Smali.
   *
   * ```
   * Lcom/example/Foo;->bar:I
   * ```
   */
  fun createFieldId(
    klass: IJavaType<*, *>,
    field: JavaField
  ): Int {
    return Objects.hash(klass.fqn, field.name, field.type)
  }

  /**
   * Parse the return type of a method descriptor.
   *
   * @param descriptor The method descriptor.
   */
  fun returnType(descriptor: String): JavaType {
    val idx = descriptor.indexOfLast { it == ')' }
    if (descriptor[0] != '(' || idx == -1) {
      throw IllegalArgumentException("Invalid method descriptor: $descriptor")
    }

    return type(descriptor.substring(idx + 1))
  }

  /**
   * Parse the parameter types of a method descriptor.
   *
   * @param descriptor The method descriptor.
   */
  fun paramTypes(descriptor: String): List<JavaType> {
    val idx = descriptor.indexOfLast { it == ')' }
    if (descriptor[0] != '(' || idx == -1) {
      throw IllegalArgumentException("Invalid method descriptor: $descriptor")
    }

    val types = mutableListOf<JavaType>()
    val params = descriptor.substring(1, idx)
    var i = 0
    while (i < params.length) {
      types.add(
        when (params[i]) {
          'L' -> {
            val end = params.indexOf(';', startIndex = i + 1)
            referenceType(params, fromIndex = i, toIndex = end).also {
              i = end + 1
            }
          }

          '[' -> arrayType(params, fromIndex = i).also {
            // number of '['
            // + length of type (I or java/lang/String)
            // + 2 if kind is ref (because refs have 'L' and ';')
            i += it.arrayDims + it.name!!.length + (if (it.kind == JavaType.KIND_REF) 2 else 0)
          }

          else -> primitive(params[i].toString()).also {
            ++i
          }
        }
      )
    }

    return types
  }

  /**
   * Parse a type descriptor. A type descriptor can be any of :
   * - a primitive type descriptor
   * - a reference type descriptor
   * - an array type descriptor
   *
   * @param descriptor The type descriptor.
   */
  fun type(descriptor: String): JavaType {
    if (descriptor.startsWith("L")) {
      return referenceType(descriptor)
    }

    if (descriptor.startsWith("[")) {
      return arrayType(descriptor)
    }

    if (descriptor.length != 1) {
      throw IllegalArgumentException("Invalid type descriptor: $descriptor")
    }

    return primitive(descriptor)
  }

  /**
   * Parse a primitive type descriptor.
   *
   * @param descriptor The primitive type descriptor.
   */
  fun primitive(descriptor: String): JavaType = primitive(descriptor[0])

  /**
   * Parse a primitive type descriptor.
   *
   * @param char The primitive type.
   */
  fun primitive(char: Char): JavaType {
    val primitive = JavaType.primitiveFor(char)
    return requireNotNull(primitive) {
      "Invalid type descriptor: $char"
    }
  }

  /**
   * Parse a reference type descriptor.
   *
   * Format:
   * ```
   * Lcom/example/Foo;
   * ```
   *
   * @param descriptor The descriptor of a reference type
   * @param fromIndex The index of the first character of the reference type name.
   * @param toIndex The index of the last character of the reference type name (inclusive).
   */
  fun referenceType(
    descriptor: String, fromIndex: Int = 0, toIndex: Int = descriptor.length - 1
  ): JavaType {
    if (descriptor[fromIndex] != 'L' || descriptor[toIndex] != ';') {
      throw IllegalArgumentException("Invalid reference type descriptor: $descriptor, fromIndex: $fromIndex, toIndex: $toIndex")
    }

    return when (descriptor) {
      "Ljava/lang/String;" -> JavaType.STRING
      "Ljava/lang/Object;" -> JavaType.OBJECT
      "Ljava/lang/Class;" -> JavaType.CLASS
      else -> {
        // remove leading 'L' and trailing ';'
        val name = descriptor.substring(fromIndex + 1, toIndex)
        JavaType.newInstance(name = name, kind = JavaType.KIND_REF)
      }
    }
  }


  /**
   * Parse an array type descriptor.
   *
   * Format:
   * ```
   * [I
   * [[I
   * [[Lcom/example/Foo;
   * ```
   *
   * @param descriptor The array type descriptor.
   * @param fromIndex The index of the first character of the array type descriptor.
   */
  fun arrayType(descriptor: String, fromIndex: Int = 0): JavaType {
    if (descriptor[fromIndex] != '[') {
      throw IllegalArgumentException("Invalid array type descriptor: $descriptor, fromIndex: $fromIndex")
    }

    var idx = fromIndex + 1
    var dims = 1
    while (descriptor[idx] == '[') {
      idx++
      dims++
    }

    val type = if (descriptor[idx] == 'L') {
      referenceType(
        descriptor, fromIndex = idx, toIndex = descriptor.indexOf(';', startIndex = idx + 1)
      )
    } else {
      primitive(descriptor[idx])
    }

    return JavaType.newInstance(name = type.name, kind = type.kind, arrayDims = dims)
  }
}