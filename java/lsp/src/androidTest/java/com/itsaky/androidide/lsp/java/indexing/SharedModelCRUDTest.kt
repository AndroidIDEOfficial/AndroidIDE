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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.indexing.core.platform.ApiInfo
import com.itsaky.androidide.lsp.java.indexing.classfile.ArrayAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.ClassAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.EnumAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.IAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaConstant
import com.itsaky.androidide.lsp.java.utils.JavaType
import com.itsaky.androidide.lsp.java.indexing.classfile.PrimitiveAnnotationElementValue
import com.itsaky.androidide.testing.android.rules.RealmDBTestRule
import io.realm.RealmAny
import io.realm.RealmList
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class SharedModelCRUDTest {

  @Rule
  @JvmField
  val dbTestRule = RealmDBTestRule(JavaIndexingRealmModule())

  @Test
  fun testApiInfoCreate() {
    dbTestRule.withDb("api-info") {
      val apiInfo = com.itsaky.androidide.indexing.core.platform.ApiInfo.newInstance(since = 1, deprecatedIn = 21, removedIn = 23)
      assertInsertUnique(apiInfo)

      val apiInfo2 = apiInfo.clone().update(removedIn = 26)
      assertInsertion(allowUpdate = true, apiInfo, apiInfo2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(apiInfo, apiInfo2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testApiInfoDuplicationError() {
    dbTestRule.withDb("api-info-duplication-error") {
      val apiInfo = com.itsaky.androidide.indexing.core.platform.ApiInfo.newInstance(since = 1, deprecatedIn = 21, removedIn = 23)
      assertInsertSingle(apiInfo)
      assertInsertSingle(apiInfo)
    }
  }

  @Test
  fun testConstantCreate() {
    dbTestRule.withDb("java-constant-create") {
      val constant =
        JavaConstant.newInstance(kind = JavaType.KIND_INT, value = RealmAny.valueOf(1.toInt()))
      val constant2 =
        JavaConstant.newInstance(kind = JavaType.KIND_INT, value = RealmAny.valueOf(2.toInt()))

      assertInsertUnique(constant)

      assertInsertion(allowUpdate = true, constant, constant2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(constant, constant2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testConstantDuplicationError() {
    dbTestRule.withDb("java-constant-duplication-error") {
      val constant =
        JavaConstant.newInstance(kind = JavaType.KIND_INT, value = RealmAny.valueOf(1.toInt()))
      assertInsertSingle(constant)
      assertInsertSingle(constant)
    }
  }

  @Test
  fun testJavaTypeCreate() {
    dbTestRule.withDb("java-type-create") {
      assertInsertUnique(JavaType.INT)

      assertInsertion(JavaType.STRING, JavaType.OBJECT) { models ->
        assertThat(models).hasSize(3)
        assertThat(models).containsExactly(JavaType.INT, JavaType.STRING, JavaType.OBJECT)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testJavaTypeDuplicationError() {
    dbTestRule.withDb("java-type-duplication-error") {
      assertInsertSingle(JavaType.OBJECT)
      assertInsertSingle(JavaType.OBJECT)
    }
  }

  @Test
  fun testPrimitiveAnnotationValueCreate() {
    dbTestRule.withDb("annotation-value-primitive") {
      val prim1 = PrimitiveAnnotationElementValue.newInstance(
        IAnnotationElementValue.KIND_INT,
        JavaConstant.newInstance(JavaType.KIND_INT, RealmAny.valueOf(1.toInt()))
      )
      val prim2 = PrimitiveAnnotationElementValue.newInstance(
        IAnnotationElementValue.KIND_STRING,
        JavaConstant.newInstance(JavaType.KIND_REF, RealmAny.valueOf("Something something"))
      )

      assertInsertUnique(prim1)

      assertInsertion(allowUpdate = true, prim1, prim2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(prim1, prim2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testPrimitiveAnnotationValueDuplicationError() {
    dbTestRule.withDb("annotation-value-primitive-duplication") {
      val prim1 = PrimitiveAnnotationElementValue.newInstance(
        IAnnotationElementValue.KIND_INT,
        JavaConstant.newInstance(JavaType.KIND_INT, RealmAny.valueOf(1.toInt()))
      )
      assertInsertSingle(prim1)
      assertInsertSingle(prim1)
    }
  }

  @Test
  fun testArrayAnnotationValueCreate() {
    dbTestRule.withDb("annotation-value-array") {
      val obj1 = ArrayAnnotationElementValue.newInstance(
        RealmList()
      )
      val obj2 = ArrayAnnotationElementValue.newInstance(
        RealmList<RealmAny>().apply {
          add(
            RealmAny.valueOf(
              PrimitiveAnnotationElementValue.newInstance(
                IAnnotationElementValue.KIND_STRING,
                JavaConstant.newInstance(JavaType.KIND_REF, RealmAny.valueOf("Something something"))
              )
            )
          )

          RealmAny.valueOf(
            PrimitiveAnnotationElementValue.newInstance(
              IAnnotationElementValue.KIND_INT,
              JavaConstant.newInstance(JavaType.KIND_INT, RealmAny.valueOf(123.toInt()))
            )
          )
        }
      )

      assertInsertUnique(obj1)

      assertInsertion(allowUpdate = true, obj1, obj2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(obj1, obj2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testArrayAnnotationValueDuplicationError() {
    dbTestRule.withDb("annotation-value-array-duplication") {
      val obj1 = ArrayAnnotationElementValue.newInstance(
        RealmList<RealmAny>().apply {
          add(
            RealmAny.valueOf(
              PrimitiveAnnotationElementValue.newInstance(
                IAnnotationElementValue.KIND_STRING,
                JavaConstant.newInstance(JavaType.KIND_REF, RealmAny.valueOf("Something something"))
              )
            )
          )

          RealmAny.valueOf(
            PrimitiveAnnotationElementValue.newInstance(
              IAnnotationElementValue.KIND_INT,
              JavaConstant.newInstance(JavaType.KIND_INT, RealmAny.valueOf(123.toInt()))
            )
          )
        }
      )
      assertInsertSingle(obj1)
      assertInsertSingle(obj1)
    }
  }

  @Test
  fun testClassAnnotationValueCreate() {
    dbTestRule.withDb("annotation-value-class") {
      val obj1 = ClassAnnotationElementValue.newInstance(
        JavaType.newInstance("com/example/SomeType", JavaType.KIND_REF)
      )
      val obj2 = ClassAnnotationElementValue.newInstance(
        JavaType.newInstance("com/example/SomeType", JavaType.KIND_REF, arrayDims = 5)
      )

      assertInsertUnique(obj1)

      assertInsertion(allowUpdate = true, obj1, obj2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(obj1, obj2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testClassAnnotationValueDuplicationError() {
    dbTestRule.withDb("annotation-value-class-duplication") {
      val obj1 = ClassAnnotationElementValue.newInstance(
        JavaType.newInstance("com/example/SomeType", JavaType.KIND_REF)
      )
      assertInsertSingle(obj1)
      assertInsertSingle(obj1)
    }
  }

  @Test
  fun testEnumAnnotationValueCreate() {
    dbTestRule.withDb("annotation-value-enum") {
      val obj1 = EnumAnnotationElementValue.newInstance(
        "SomeEnum_ENTRY",
        JavaType.newInstance("com/example/SomeEnum", JavaType.KIND_REF),
      )
      val obj2 = EnumAnnotationElementValue.newInstance(
        "SomeEnum_ENTRY_2",
        JavaType.newInstance("com/example/SomeEnum", JavaType.KIND_REF),
      )

      assertInsertUnique(obj1)

      assertInsertion(allowUpdate = true, obj1, obj2) { models ->
        assertThat(models).hasSize(2)
        assertThat(models).containsExactly(obj1, obj2)
      }
    }
  }

  @Test(expected = RealmPrimaryKeyConstraintException::class)
  fun testEnumAnnotationValueDuplicationError() {
    dbTestRule.withDb("annotation-value-enum-duplication") {
      val obj1 = EnumAnnotationElementValue.newInstance(
        "SomeEnum_ENTRY",
        JavaType.newInstance("com/example/SomeEnum", JavaType.KIND_REF),
      )
      assertInsertSingle(obj1)
      assertInsertSingle(obj1)
    }
  }
}