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

package com.itsaky.androidide.lsp.java.indexing.models

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
    ApiInfo::class,
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