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
 * [JavaJarModelBuilder] builds models for Java classes in a JAR file.
 *
 * @author Akash Yadav
 */
class JavaJarModelBuilder(private val jar: File) {

  // for testing purposes
  // prefer using consumeTypes(Function)
  fun buildTypes(): List<IJavaType<*, *>> {
    val types = mutableListOf<IJavaType<*, *>>()
    consumeTypes { types.add(it) }
    return types
  }

  fun consumeTypes(consumer: (IJavaType<*, *>) -> Unit) {
    ZipFile(jar).use { jar ->
      jar.entries().asSequence().filter { it.name.endsWith(".class") }.forEach { entry ->
        jar.getInputStream(entry).buffered().use { inputStream ->
          val classFile = ClassFile.read(inputStream)!!

          // build model only if the class is not an anonymous or local class
          if (classFile.getAttribute(Attribute.EnclosingMethod) == null) {
            val type = buildType(classFile)
            consumer(type)
          }
        }
      }
    }
  }

  private fun buildType(classFile: ClassFile): IJavaType<*, *> {
    val classInfo = classFile.constant_pool.getClassInfo(classFile.this_class)
    val fqn = classInfo.baseName
    val (pck, name, isInner) = splitFqn(fqn)
    return buildType(fqn, pck, name, isInner, classFile)
  }

  /**
   * Split the given fully qualified name of a class and return the package name and the simple name
   * of the class, along with whether the class is an inner class.
   */
  private fun splitFqn(fqn: String): Triple<String, String, Boolean> {
    val dolIdx = fqn.lastIndexOf('$')

    // start looking for '/' from the index of the last '$'
    // a '/' will always appear before a '$'
    val slaIdx = fqn.lastIndexOf('/', startIndex = if (dolIdx == -1) fqn.lastIndex else dolIdx - 1)

    if (slaIdx == -1 && dolIdx != -1) {
      // fqn == SomeClass$InnerClass (inner class of a class in the root package)
      // pck is empty, but fqn is not the class's simple name
      return Triple("", fqn.substring(dolIdx + 1), true)
    }

    if (slaIdx == -1) {
      // fqn == SomeClass (class in the root package)
      // pck is empty and fqn is class's simple name
      return Triple("", fqn, false)
    }

    if (dolIdx == -1) {
      // fqn == com/example/SomeClass
      return Triple(fqn.substring(0, slaIdx), fqn.substring(slaIdx + 1), false)
    }

    // fqn == com/example/SomeClass$InnerClass
    return Triple(fqn.substring(0, slaIdx), fqn.substring(dolIdx + 1), true)
  }

  private fun buildType(
    fqn: String,
    pck: String,
    name: String,
    isInner: Boolean,
    classFile: ClassFile
  ): IJavaType<*, *> {
    return when {
      classFile.access_flags.`is`(AccessFlags.ACC_ENUM) -> buildEnum(fqn, name, pck, classFile)
      classFile.access_flags.`is`(AccessFlags.ACC_ANNOTATION) -> buildAnnotation(
        fqn,
        name,
        pck,
        classFile
      )

      classFile.access_flags.`is`(AccessFlags.ACC_INTERFACE) -> buildInterface(
        fqn,
        name,
        pck,
        classFile
      )

      else -> buildClass(fqn, name, pck, classFile)
    }.also {
      it.isInner = isInner
    }
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
      classFile.methods.forEach { method ->
        val descriptor = classFile.constant_pool.getUTF8Value(method.descriptor.index)
        val methodName = method.getName(classFile.constant_pool)
        val methodType = DescriptorUtils.returnType(descriptor)
        val paramTypes = RealmList<JavaType>().apply {
          addAll(DescriptorUtils.paramTypes(descriptor))
        }

        add(JavaMethod.newInstance(methodName, paramTypes, methodType, method.access_flags.flags))
      }
    }
  }

  private fun IJavaType<JavaField, *>.setFields(classFile: ClassFile) {
    fields = RealmList<JavaField>().apply {
      classFile.fields.forEach { field ->
        val descriptor = classFile.constant_pool.getUTF8Value(field.descriptor.index)
        val fieldName = field.getName(classFile.constant_pool)
        val fieldType = DescriptorUtils.type(descriptor)

        add(JavaField.newField(fieldName, fieldType, field.access_flags.flags).apply {
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
        })
      }
    }
  }

  private fun JavaAnnotation.setAnnotationElements(classFile: ClassFile) {
    methods = RealmList<AnnotationElement>().apply {
      classFile.methods.forEach { method ->
        val descriptor = classFile.constant_pool.getUTF8Value(method.descriptor.index)
        val methodName = method.getName(classFile.constant_pool)
        val methodType = DescriptorUtils.returnType(descriptor)

        add(this@setAnnotationElements.newAnnotationElement(
          methodName, methodType, method.access_flags.flags
        ).apply {
          val annotationDefault =
            method.attributes.get(Attribute.AnnotationDefault) as? AnnotationDefault_attribute?
          if (annotationDefault != null) {
            val defaultValue = annotationDefault.default_value
            val value = toAnnotationElementValue(defaultValue, classFile)
            this.defaultValue = value.let(RealmAny::valueOf) ?: RealmAny.nullValue()
          }
        })
      }
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
        // annotation value kinds are same as JavaType.KIND_*
        kind = kind,
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
          // even though the name is given as "class_info_index", the constant at that index
          // is actually a CONSTANT_Utf8_info representing a return type descriptor (JLS $4.3.3)
          // this has been clearly mentioned in the Java Language Specification
          ClassAnnotationElementValue.newInstance(
            DescriptorUtils.type(
              classFile.constant_pool.getUTF8Value(
                (defaultValue as Class_element_value).class_info_index
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
          ArrayAnnotationElementValue.newInstance(values = RealmList<RealmAny>().apply {
            (defaultValue as Array_element_value).values.forEach { element ->
              add(toAnnotationElementValue(element, classFile).let { elementValue ->
                RealmAny.valueOf(elementValue)
              })
            }
          })
        }

        else -> throw IllegalArgumentException("Unknown value tag: ${defaultValue.tag}")
      }
    }
    return value
  }

  private fun toPrimitiveConstant(
    constantPool: ConstantPool, kind: Int, char: Char, prim: Primitive_element_value
  ): JavaConstant {
    if (kind == JavaType.KIND_REF && char != 's') {
      throw IllegalArgumentException("Unknown value tag: $char")
    }

    val constant = constantPool.get(prim.const_value_index)
    return toJavaConstant(kind, constant, char)
  }

  private fun toJavaConstant(
    kind: Int, constant: CPInfo, char: Char?
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
}