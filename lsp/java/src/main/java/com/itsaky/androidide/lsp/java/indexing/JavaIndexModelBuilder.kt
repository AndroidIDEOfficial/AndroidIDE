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

import com.itsaky.androidide.lsp.java.indexing.models.AnnotationAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.AnnotationElement
import com.itsaky.androidide.lsp.java.indexing.models.AnnotationElement.Companion.newAnnotationElement
import com.itsaky.androidide.lsp.java.indexing.models.ArrayAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.ClassAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.EnumAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.IAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.IJavaType
import com.itsaky.androidide.lsp.java.indexing.models.JavaAnnotation
import com.itsaky.androidide.lsp.java.indexing.models.JavaClass
import com.itsaky.androidide.lsp.java.indexing.models.JavaConstant
import com.itsaky.androidide.lsp.java.indexing.models.JavaEnum
import com.itsaky.androidide.lsp.java.indexing.models.JavaField
import com.itsaky.androidide.lsp.java.indexing.models.JavaInterface
import com.itsaky.androidide.lsp.java.indexing.models.JavaMethod
import com.itsaky.androidide.lsp.java.indexing.models.JavaType
import com.itsaky.androidide.lsp.java.indexing.models.PrimitiveAnnotationElementValue
import io.realm.RealmAny
import io.realm.RealmDictionary
import io.realm.RealmList
import openjdk.tools.classfile.AccessFlags
import openjdk.tools.classfile.Annotation.Annotation_element_value
import openjdk.tools.classfile.Annotation.Array_element_value
import openjdk.tools.classfile.Annotation.Class_element_value
import openjdk.tools.classfile.Annotation.Enum_element_value
import openjdk.tools.classfile.Annotation.Primitive_element_value
import openjdk.tools.classfile.Annotation.element_value
import openjdk.tools.classfile.AnnotationDefault_attribute
import openjdk.tools.classfile.Attribute
import openjdk.tools.classfile.ClassFile
import openjdk.tools.classfile.ConstantPool
import openjdk.tools.classfile.ConstantPool.CONSTANT_Double_info
import openjdk.tools.classfile.ConstantPool.CONSTANT_Float_info
import openjdk.tools.classfile.ConstantPool.CONSTANT_Integer_info
import openjdk.tools.classfile.ConstantPool.CONSTANT_Long_info
import openjdk.tools.classfile.ConstantPool.CONSTANT_String_info
import openjdk.tools.classfile.ConstantPool.CONSTANT_Utf8_info
import openjdk.tools.classfile.ConstantPool.CPInfo
import openjdk.tools.classfile.ConstantValue_attribute
import java.io.File
import java.util.zip.ZipFile

/**
 * [JavaIndexModelBuilder] builds models for Java classes in a JAR file.
 *
 * @author Akash Yadav
 */
class JavaIndexModelBuilder(
  private val jar: File
) {

  // for testing purposes
  // prefer using consumeTypes(Function)
  fun buildTypes(): List<IJavaType<*, *>> {
    val types = mutableListOf<IJavaType<*, *>>()
    consumeTypes { types.add(it) }
    return types
  }

  fun consumeTypes(consumer: (IJavaType<*, *>) -> Unit) {
    ZipFile(jar).use { jar ->
      for (entry in jar.entries()) {
        if (!entry.name.endsWith(".class")) {
          continue
        }

        jar.getInputStream(entry).use { inputStream ->
          val classFile = ClassFile.read(inputStream)!!
          val type = buildType(classFile)
          consumer(type)
        }
      }
    }
  }

  private fun buildType(classFile: ClassFile): IJavaType<*, *> {
    val classInfo = classFile.constant_pool.getClassInfo(classFile.this_class)
    val fqn = classInfo.baseName
    val name = simpleName(fqn)
    val pck = fqn.substring(0, fqn.length - name.length - 1)
    return buildType(fqn, name, pck, classFile)
  }

  private fun buildType(
    fqn: String, name: String, pck: String, classFile: ClassFile
  ): IJavaType<*, *> {
    if (classFile.access_flags.`is`(AccessFlags.ACC_ENUM)) {
      return buildEnum(fqn, name, pck, classFile)
    }

    if (classFile.access_flags.`is`(AccessFlags.ACC_ANNOTATION)) {
      return buildAnnotation(fqn, name, pck, classFile)
    }

    if (classFile.access_flags.`is`(AccessFlags.ACC_INTERFACE)) {
      return buildInterface(fqn, name, pck, classFile)
    }

    return buildClass(fqn, name, pck, classFile)
  }

  private fun buildClass(fqn: String, name: String, pck: String, classFile: ClassFile): JavaClass {
    return JavaClass.newInstance(fqn, name, pck, classFile.access_flags.flags).apply {
      setSuperClass(classFile)
      setSuperInterfaces(classFile)
      setFields(classFile)
      setMethods(classFile)
    }
  }

  private fun buildInterface(
    fqn: String, name: String, pck: String, classFile: ClassFile
  ): JavaInterface {
    return JavaInterface.newInstance(fqn, name, pck, classFile.access_flags.flags).apply {
      setSuperClass(classFile)
      setSuperInterfaces(classFile)
      setFields(classFile)
      setMethods(classFile)
    }
  }

  private fun buildAnnotation(
    fqn: String, name: String, pck: String, classFile: ClassFile
  ): JavaAnnotation {
    return JavaAnnotation.newInstance(fqn, name, pck, classFile.access_flags.flags).apply {
      setSuperClass(classFile)
      setSuperInterfaces(classFile)
      setFields(classFile)
      setAnnotationElements(classFile)
    }
  }

  private fun buildEnum(fqn: String, name: String, pck: String, classFile: ClassFile): JavaEnum {
    return JavaEnum.newInstance(fqn, name, pck, classFile.access_flags.flags).apply {
      setSuperClass(classFile)
      setSuperInterfaces(classFile)
      setFields(classFile)
      setMethods(classFile)
    }
  }

  private fun IJavaType<*, *>.setSuperInterfaces(classFile: ClassFile) {
    val interfaces = RealmList<String>()
    for (iface in classFile.interfaces) {
      val ifaceInfo = classFile.constant_pool.getClassInfo(iface)
      interfaces.add(ifaceInfo.baseName!!)
    }

    superInterfacesFqn = interfaces
  }

  private fun IJavaType<*, *>.setSuperClass(classFile: ClassFile) {
    if (classFile.super_class == 0) {
      // From https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html#jvms-4.5
      // If the value of the super_class field is 0, then this class represents the java/lang/Object class itself
      // and hence, doesn't have a super class.
      return
    }

    val superclassInfo = classFile.constant_pool.getClassInfo(classFile.super_class)
    superClassFqn = superclassInfo.baseName!!
  }

  private fun IJavaType<*, JavaMethod>.setMethods(classFile: ClassFile) {
    methods = RealmList<JavaMethod>().apply {
      addAll(classFile.methods.map { method ->
        val descriptor = classFile.constant_pool.getUTF8Value(method.descriptor.index)
        val methodName = method.getName(classFile.constant_pool)
        val methodType = DescriptorUtils.returnType(descriptor)
        val paramTypes = RealmList<JavaType>().apply {
          addAll(DescriptorUtils.paramTypes(descriptor))
        }

        JavaMethod.newInstance(methodName, paramTypes, methodType, method.access_flags.flags)
      })
    }
  }

  private fun IJavaType<JavaField, *>.setFields(classFile: ClassFile) {
    fields = RealmList<JavaField>().apply {
      addAll(classFile.fields.map { field ->
        val descriptor = classFile.constant_pool.getUTF8Value(field.descriptor.index)
        val fieldName = field.getName(classFile.constant_pool)
        val fieldType = DescriptorUtils.type(descriptor)
        JavaField.newField(fieldName, fieldType, field.access_flags.flags).apply {
          field.attributes.get(Attribute.ConstantValue)?.also { constAttr ->
            val constantValue = constAttr as ConstantValue_attribute
            val constant = classFile.constant_pool.get(constantValue.constantvalue_index)
            val isStringType = fieldType.isStringType()
            val javaConstant = if (fieldType.isPrimitive() || isStringType) {
              toJavaConstant(fieldType.kind, constant, if (isStringType) 's' else null)
            } else {
              throw IllegalArgumentException("Invalid field type for constant value: $fieldType")
            }

            this.constantValue = javaConstant
          }
        }
      })
    }
  }

  private fun JavaAnnotation.setAnnotationElements(classFile: ClassFile) {
    methods = RealmList<AnnotationElement>().apply {
      addAll(classFile.methods.map { method ->
        val descriptor = classFile.constant_pool.getUTF8Value(method.descriptor.index)
        val methodName = method.getName(classFile.constant_pool)
        val methodType = DescriptorUtils.returnType(descriptor)

        this@setAnnotationElements.newAnnotationElement(
          methodName, methodType, method.access_flags.flags
        ).apply {
          val annotationDefault =
            method.attributes.get(Attribute.AnnotationDefault) as? AnnotationDefault_attribute?
          if (annotationDefault != null) {
            val defaultValue = annotationDefault.default_value
            val value = toAnnotationElementValue(defaultValue, classFile)
            this.defaultValue = value.let(RealmAny::valueOf) ?: RealmAny.nullValue()
          }
        }
      })
    }
  }

  private fun toAnnotationElementValue(
    defaultValue: element_value, classFile: ClassFile
  ): IAnnotationElementValue {
    val char = defaultValue.tag.toChar()
    var kind = JavaType.kindForType(char)
    if (char == 's') {
      kind = JavaType.KIND_REF
    }

    var value: IAnnotationElementValue? = when (kind) {
      JavaType.KIND_BOOLEAN,
      JavaType.KIND_BYTE,
      JavaType.KIND_CHAR,
      JavaType.KIND_SHORT,
      JavaType.KIND_INT,
      JavaType.KIND_LONG,
      JavaType.KIND_FLOAT,
      JavaType.KIND_DOUBLE -> PrimitiveAnnotationElementValue.newInstance(
        kind = when (kind) {
          JavaType.KIND_BOOLEAN -> IAnnotationElementValue.KIND_BOOLEAN
          JavaType.KIND_BYTE -> IAnnotationElementValue.KIND_BYTE
          JavaType.KIND_CHAR -> IAnnotationElementValue.KIND_CHAR
          JavaType.KIND_SHORT -> IAnnotationElementValue.KIND_SHORT
          JavaType.KIND_INT -> IAnnotationElementValue.KIND_INT
          JavaType.KIND_LONG -> IAnnotationElementValue.KIND_LONG
          JavaType.KIND_FLOAT -> IAnnotationElementValue.KIND_FLOAT
          JavaType.KIND_DOUBLE -> IAnnotationElementValue.KIND_DOUBLE
          else -> throw IllegalArgumentException("Unknown value tag: $char")
        },

        value = toPrimitiveConstant(
          classFile.constant_pool, kind, char, defaultValue as Primitive_element_value
        )
      )

      else -> null
    }

    if (value == null) {
      value = when (char) {
        's' -> PrimitiveAnnotationElementValue.newInstance(
          kind = IAnnotationElementValue.KIND_STRING,
          value = toPrimitiveConstant(
            classFile.constant_pool,
            JavaType.KIND_REF,
            char,
            defaultValue as Primitive_element_value
          )
        )

        'e' -> {
          val enum = defaultValue as Enum_element_value
          EnumAnnotationElementValue.newInstance(
            name = classFile.constant_pool.getUTF8Value(enum.const_name_index),
            type = DescriptorUtils.type(classFile.constant_pool.getUTF8Value(enum.type_name_index))
          )
        }

        'c' -> {
          val clazz = defaultValue as Class_element_value

          // even though the name is given as "class_info_index", the constant at that index
          // is actually a CONSTANT_Utf8_info representing a return type descriptor (JLS $4.3.3)
          // this has been clearly mentioned in the Java Language Specification
          ClassAnnotationElementValue.newInstance(
            DescriptorUtils.type(
              classFile.constant_pool.getUTF8Value(
                clazz.class_info_index
              )
            )
          )
        }

        '@' -> {
          val annotation = (defaultValue as Annotation_element_value).annotation_value
          AnnotationAnnotationElementValue.newInstance(type = DescriptorUtils.type(
            classFile.constant_pool.getUTF8Value(
              annotation.type_index
            )
          ),
            values = RealmDictionary<RealmAny>().apply {
              for (pair in annotation.element_value_pairs) {
                this[classFile.constant_pool.getUTF8Value(pair.element_name_index)] =
                  toAnnotationElementValue(pair.value, classFile).let { elementValue ->
                    RealmAny.valueOf(elementValue)
                  }
              }
            })
        }

        '[' -> {
          val arr = defaultValue as Array_element_value
          ArrayAnnotationElementValue.newInstance(values = RealmList<RealmAny>().apply {
            addAll(arr.values.map { element ->
              toAnnotationElementValue(element, classFile).let { elementValue ->
                RealmAny.valueOf(elementValue)
              }
            })
          })
        }

        else -> throw IllegalArgumentException("Unknown value tag: ${defaultValue.tag}")
      }
    }
    return value
  }

  private fun toPrimitiveConstant(
    constantPool: ConstantPool, kind: Short, char: Char, prim: Primitive_element_value
  ): JavaConstant {
    if (kind == JavaType.KIND_REF && char != 's') {
      throw IllegalArgumentException("Unknown value tag: $char")
    }

    val constant = constantPool.get(prim.const_value_index)
    return toJavaConstant(kind, constant, char)
  }

  private fun toJavaConstant(
    kind: Short, constant: CPInfo, char: Char?
  ): JavaConstant {
    val realmAny = when (kind) {
      JavaType.KIND_BOOLEAN,
      JavaType.KIND_BYTE,
      JavaType.KIND_CHAR,
      JavaType.KIND_SHORT,
      JavaType.KIND_INT -> (constant as CONSTANT_Integer_info).value.let {
        RealmAny.valueOf(it)
      }

      JavaType.KIND_LONG -> (constant as CONSTANT_Long_info).value.let { RealmAny.valueOf(it) }
      JavaType.KIND_FLOAT -> (constant as CONSTANT_Float_info).value.let { RealmAny.valueOf(it) }
      JavaType.KIND_DOUBLE -> (constant as CONSTANT_Double_info).value.let { RealmAny.valueOf(it) }
      else -> if (char == 's') {
        if (constant is CONSTANT_String_info) {
          constant.string
        } else {
          (constant as CONSTANT_Utf8_info).value
        }.let { RealmAny.valueOf(it) }
      } else {
        throw IllegalArgumentException("Unknown value tag: $char")
      }
    }

    return JavaConstant.newInstance(
      kind = kind,
      value = realmAny
    )
  }

  private fun simpleName(fqn: String): String {
    var idx = fqn.lastIndexOf('$')
    idx = if (idx == -1) fqn.lastIndexOf('/') else idx
    return fqn.substring(idx + 1)
  }
}