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

package com.itsaky.androidide.lsp.java.indexing

import com.itsaky.androidide.lsp.java.indexing.models.JavaType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
class DescriptorUtilsTest {

  @Test
  fun testReturnType() {
    val descriptor1 = "(I)V"
    val expected1 = JavaType.newInstance(name = "V", kind = JavaType.KIND_VOID)
    assertEquals(expected1, DescriptorUtils.returnType(descriptor1))

    val descriptor2 = "(Ljava/lang/String;)I"
    val expected2 = JavaType.newInstance(name = "I", kind = JavaType.KIND_INT)
    assertEquals(expected2, DescriptorUtils.returnType(descriptor2))

    val descriptor3 = "(I[Ljava/lang/String;)V"
    val expected3 = JavaType.newInstance(name = "V", kind = JavaType.KIND_VOID)
    assertEquals(expected3, DescriptorUtils.returnType(descriptor3))
  }

  @Test(expected = IllegalArgumentException::class)
  fun testReturnTypeInvalidDescriptor() {
    DescriptorUtils.returnType("Ljava/lang/String;I")
  }

  @Test
  fun testParamTypes() {
    val descriptor1 = "(I)V"
    val expected1 = listOf(JavaType.newInstance(name = "I", kind = JavaType.KIND_INT))
    assertEquals(expected1, DescriptorUtils.paramTypes(descriptor1))

    val descriptor2 = "(Ljava/lang/String;I)V"
    val expected2 = listOf(
      JavaType.newInstance(name = "java/lang/String", kind = JavaType.KIND_REF),
      JavaType.newInstance(name = "I", kind = JavaType.KIND_INT)
    )
    assertEquals(expected2, DescriptorUtils.paramTypes(descriptor2))

    val descriptor3 = "([I[Ljava/lang/String;)V"
    val expected3 = listOf(
      JavaType.newInstance(name = "I", kind = JavaType.KIND_INT, arrayDims = 1),
      JavaType.newInstance(name = "java/lang/String", kind = JavaType.KIND_REF, arrayDims = 1)
    )
    assertEquals(expected3, DescriptorUtils.paramTypes(descriptor3))
  }

  @Test(expected = IllegalArgumentException::class)
  fun testParamTypesInvalidDescriptor() {
    DescriptorUtils.paramTypes("Ljava/lang/String;I")
  }

  @Test
  fun testPrimitive() {
    val booleanType = JavaType.newInstance(name = "Z", kind = JavaType.KIND_BOOLEAN)
    assertEquals(booleanType, DescriptorUtils.primitive('Z'))

    val intType = JavaType.newInstance(name = "I", kind = JavaType.KIND_INT)
    assertEquals(intType, DescriptorUtils.primitive('I'))
  }

  @Test(expected = IllegalArgumentException::class)
  fun testPrimitiveInvalidType() {
    DescriptorUtils.primitive('X')
  }

  @Test
  fun testReferenceType() {
    val descriptor = "Ljava/lang/String;"
    val expected = JavaType.newInstance(name = "java/lang/String", kind = JavaType.KIND_REF)
    assertEquals(expected, DescriptorUtils.referenceType(descriptor))
  }

  @Test(expected = IllegalArgumentException::class)
  fun testReferenceTypeInvalidDescriptor() {
    DescriptorUtils.referenceType("Ljava/lang/String")
  }

  @Test
  fun testArrayType() {
    val descriptor1 = "[I"
    val expected1 = JavaType.newInstance(name = "I", kind = JavaType.KIND_INT, arrayDims = 1)
    assertEquals(expected1, DescriptorUtils.arrayType(descriptor1))

    val descriptor2 = "[[Ljava/lang/String;"
    val expected2 =
      JavaType.newInstance(name = "java/lang/String", kind = JavaType.KIND_REF, arrayDims = 2)
    assertEquals(expected2, DescriptorUtils.arrayType(descriptor2))
  }

  @Test(expected = IllegalArgumentException::class)
  fun testArrayTypeInvalidDescriptor() {
    DescriptorUtils.arrayType("Ljava/lang/String;")
  }
}