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

import com.itsaky.androidide.lsp.java.indexing.classfile.AnnotationAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.AnnotationElement
import com.itsaky.androidide.lsp.java.indexing.classfile.ArrayAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.ClassAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.EnumAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaAnnotation
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaClass
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaConstant
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaEnum
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaField
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaInterface
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaMethod
import com.itsaky.androidide.lsp.java.indexing.classfile.JavaType
import com.itsaky.androidide.lsp.java.indexing.classfile.PrimitiveAnnotationElementValue
import io.realm.annotations.RealmModule

/**
 * [RealmModule] for the indexing databased for Java.
 *
 * @author Akash Yadav
 */
@RealmModule(
  library = true, classes = [
    AnnotationElement::class,
    AnnotationAnnotationElementValue::class,
    ArrayAnnotationElementValue::class,
    ClassAnnotationElementValue::class,
    EnumAnnotationElementValue::class,
    PrimitiveAnnotationElementValue::class,
    JavaAnnotation::class,
    JavaClass::class,
    JavaConstant::class,
    JavaEnum::class,
    JavaField::class,
    JavaInterface::class,
    JavaMethod::class,
    JavaType::class,
  ]
)
internal class JavaIndexingRealmModule