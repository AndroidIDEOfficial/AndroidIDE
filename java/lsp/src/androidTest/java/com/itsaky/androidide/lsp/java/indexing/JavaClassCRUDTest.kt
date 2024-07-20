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
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaClass
import com.itsaky.androidide.lsp.java.utils.JavaType
import com.itsaky.androidide.testing.android.rules.RealmDBTestRule
import io.realm.RealmList
import io.realm.RealmObject
import openjdk.tools.classfile.AccessFlags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Akash Yadav
 */
@RunWith(AndroidJUnit4::class)
class JavaClassCRUDTest {

  @Rule
  @JvmField
  val dbTestRule = RealmDBTestRule(JavaIndexingRealmModule())

  @Test
  fun testSimpleJavaClassCRUD() {
    dbTestRule.withDb("java-class-CRUD") {
      val klass = ModelBuilderTestUtil.createTestClass()

      // CREATE
      executeTransaction {
        it.insert(klass)
      }

      var dbKlass: JavaClass? = null
      executeTransaction {
        // READ
        dbKlass = where(JavaClass::class.java)
          .equalTo("fqn", "com/itsaky/androidide/indexing/TestClass")
          .findFirst()

        assertThat(dbKlass).isNotNull()
        assertThat(dbKlass?.fqn).isEqualTo("com/itsaky/androidide/indexing/TestClass")
        assertThat(dbKlass?.name).isEqualTo("TestClass")
        assertThat(dbKlass?.packageName).isEqualTo("com/itsaky/androidide/indexing")
        assertThat(dbKlass?.accessFlags).isEqualTo(AccessFlags.ACC_PUBLIC or AccessFlags.ACC_FINAL)
        assertThat(dbKlass?.superClassFqn).isEqualTo("java/lang/Object")
        assertThat(dbKlass?.superInterfacesFqn).isEqualTo(RealmList<String>())
        assertThat(dbKlass?.fields).isNotEmpty()
        assertThat(dbKlass?.fields).hasSize(1)

        dbKlass?.fields?.get(0).apply {
          assertThat(this).isNotNull()
          assertThat(this?.name).isEqualTo("someString")
          assertThat(this?.type).isEqualTo(JavaType.STRING)
          assertThat(this?.accessFlags).isEqualTo(AccessFlags.ACC_PRIVATE)
        }

        assertThat(dbKlass?.methods).isNotEmpty()
        assertThat(dbKlass?.methods).hasSize(1)

        dbKlass?.methods?.get(0).apply {
          assertThat(this).isNotNull()
          assertThat(this?.name).isEqualTo("getSomeString")
          assertThat(this?.paramsTypes).isEqualTo(RealmList<String>())
          assertThat(this?.returnType).isEqualTo(JavaType.STRING)
          assertThat(this?.accessFlags).isEqualTo(AccessFlags.ACC_PUBLIC)
        }
      }

      assertThat(dbKlass).isNotNull()

      // UPDATE
      executeTransaction {
        dbKlass?.accessFlags = AccessFlags.ACC_PUBLIC or AccessFlags.ACC_ABSTRACT
      }

      // verify UPDATE
      executeTransaction {
        assertThat(
          where(JavaClass::class.java)
            .equalTo("fqn", "com/itsaky/androidide/indexing/TestClass")
            .findFirst()?.accessFlags
        ).isEqualTo(AccessFlags.ACC_PUBLIC or AccessFlags.ACC_ABSTRACT)
      }

      // DELETE
      executeTransaction {
        RealmObject.deleteFromRealm(dbKlass!!)
      }

      // verify DELTE
      executeTransaction {
        assertThat(
          where(JavaClass::class.java)
            .equalTo("fqn", "com/itsaky/androidide/indexing/TestClass")
            .findFirst()
        ).isNull()
      }
    }
  }
}