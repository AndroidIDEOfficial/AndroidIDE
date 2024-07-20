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

import com.itsaky.androidide.lsp.java.indexing.classfile.JavaClass
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaField
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaMethod
import com.itsaky.androidide.lsp.java.utils.JavaType
import io.realm.RealmList
import openjdk.tools.classfile.AccessFlags

/**
 * @author Akash Yadav
 */
object ModelBuilderTestUtil {

  fun createTestClass() = JavaClass.newInstance(
    fqn = "com/itsaky/androidide/indexing/TestClass",
    name = "TestClass",
    packageName = "com/itsaky/androidide/indexing",
    accessFlags = AccessFlags.ACC_PUBLIC or AccessFlags.ACC_FINAL,
    superClassFqn = "java/lang/Object",
    superInterfacesFqn = RealmList(),
    fields = RealmList<JavaField>().apply {
      add(JavaField.newField("someString", JavaType.STRING, AccessFlags.ACC_PRIVATE))
    },
    methods = RealmList<JavaMethod>().apply {
      add(
        JavaMethod.newInstance(
          name = "getSomeString",
          paramsTypes = RealmList(),
          returnType = JavaType.STRING,
          accessFlags = AccessFlags.ACC_PUBLIC
        )
      )
    })
}