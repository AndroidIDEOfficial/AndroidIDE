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

package com.itsaky.androidide.desugaring.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.reflect.jvm.javaMethod

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class ReflectionUtilsTest {

  @Test
  fun `test primitive type descriptions`() {
    assertThat(ReflectionUtils.describe(Methods::primitive.javaMethod!!)).isEqualTo(
      "(BSIJFDCZ)V")
    assertThat(
      ReflectionUtils.describe(Methods::returnsPrimitive.javaMethod!!)).isEqualTo(
      "()B")
  }

  @Test
  fun `test primitive array descriptions`() {
    assertThat(ReflectionUtils.describe(Methods::array.javaMethod!!)).isEqualTo(
      "([B[S[I[J[F[D[C[Z)V")
    assertThat(ReflectionUtils.describe(Methods::nested.javaMethod!!)).isEqualTo(
      "([[B[[S[[I[[J[[F[[D[[C[[Z)V")
  }

  @Test
  fun `test primitive array return type descriptions`() {
    assertThat(
      ReflectionUtils.describe(Methods::returnsArray.javaMethod!!)).isEqualTo(
      "()[B")
    assertThat(
      ReflectionUtils.describe(Methods::returnsNested.javaMethod!!)).isEqualTo(
      "()[[B")
  }

  @Test
  fun `test object type return type descriptions`() {
    assertThat(
      ReflectionUtils.describe(Methods::returnsReference.javaMethod!!)).isEqualTo(
      "()Ljava/lang/Byte;")
  }

  @Test
  fun `test object array return type descriptions`() {
    assertThat(ReflectionUtils.describe(
      Methods::returnsReferenceArray.javaMethod!!)).isEqualTo(
      "()[Ljava/lang/Byte;")
    assertThat(ReflectionUtils.describe(
      Methods::returnsReferenceNested.javaMethod!!)).isEqualTo(
      "()[[Ljava/lang/Byte;")
  }

  @Test
  fun `test object type descriptions`() {
    assertThat(
      ReflectionUtils.describe(Methods::references.javaMethod!!)).isEqualTo(
      "(Ljava/lang/Byte;Ljava/lang/Short;Ljava/lang/Integer;Ljava/lang/Long;Ljava/lang/Float;Ljava/lang/Double;Ljava/lang/Character;Ljava/lang/Boolean;)V")
    assertThat(
      ReflectionUtils.describe(Methods::returnsReference.javaMethod!!)).isEqualTo(
      "()Ljava/lang/Byte;")
  }

  @Test
  fun `test object array descriptions`() {
    assertThat(
      ReflectionUtils.describe(Methods::referenceArrays.javaMethod!!)).isEqualTo(
      "([Ljava/lang/Byte;[Ljava/lang/Short;[Ljava/lang/Integer;[Ljava/lang/Long;[Ljava/lang/Float;[Ljava/lang/Double;[Ljava/lang/Character;[Ljava/lang/Boolean;)V")
    assertThat(
      ReflectionUtils.describe(Methods::referenceNested.javaMethod!!)).isEqualTo(
      "([[Ljava/lang/Byte;[[Ljava/lang/Short;[[Ljava/lang/Integer;[[Ljava/lang/Long;[[Ljava/lang/Float;[[Ljava/lang/Double;[[Ljava/lang/Character;[[Ljava/lang/Boolean;)V")
  }
}