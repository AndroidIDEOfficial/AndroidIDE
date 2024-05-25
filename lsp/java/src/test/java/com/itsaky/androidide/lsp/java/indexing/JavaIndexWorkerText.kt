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

import android.view.View
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.lsp.java.indexing.models.AnnotationElement
import com.itsaky.androidide.lsp.java.indexing.models.IAnnotationElementValue
import com.itsaky.androidide.lsp.java.indexing.models.IJavaType
import com.itsaky.androidide.lsp.java.indexing.models.JavaAnnotation
import com.itsaky.androidide.lsp.java.indexing.models.JavaClass
import com.itsaky.androidide.lsp.java.indexing.models.JavaEnum
import com.itsaky.androidide.lsp.java.indexing.models.JavaField
import com.itsaky.androidide.lsp.java.indexing.models.JavaInterface
import com.itsaky.androidide.lsp.java.indexing.models.JavaMethod
import com.itsaky.androidide.lsp.java.indexing.models.JavaType
import com.itsaky.androidide.lsp.java.indexing.models.PrimitiveAnnotationElementValue
import io.realm.RealmAny
import openjdk.tools.classfile.AccessFlags
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Modifier

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
class JavaIndexModelBuilderTest {

  @Rule
  @JvmField
  val indexRule = AndroidJarModelingRule()

  private val types: List<IJavaType<*, *>>
    get() = indexRule.types

  @Test
  fun `test JavaClass properties`() {
    val sampleClass = types.firstOrNull { it.fqn == "java/lang/String" }

    assertThat(sampleClass).isNotNull()
    sampleClass?.let {
      assertThat(it.fqn).isEqualTo("java/lang/String")
      assertThat(it.name).isEqualTo("String")
      assertThat(it.packageName).isEqualTo("java/lang")
      assertThat(it.superClassFqn).isEqualTo("java/lang/Object")
      assertThat(it.superInterfacesFqn).containsAtLeast(
        "java/io/Serializable",
        "java/lang/Comparable",
        "java/lang/CharSequence"
      )
    }
  }

  @Test
  fun `test JavaField properties`() {
    val sampleClass = types.firstOrNull { it.fqn == "java/lang/String" }
    val fields = sampleClass?.fields

    assertThat(fields).isNotEmpty()
    fields?.forEach { f ->
      val field = f as JavaField
      assertThat(field.name).isNotEmpty()
      assertThat(field.accessFlags).isGreaterThan(0)
      assertThat(field.type).isNotNull()
    }

    // java.lang.String in Android only has a single public field
    assertThat(fields?.size).isEqualTo(1)

    val field = fields?.firstOrNull() as JavaField?
    assertThat(field).isNotNull()
    assertThat(field?.name).isEqualTo("CASE_INSENSITIVE_ORDER")
    assertThat(field?.type?.name).isEqualTo("java/util/Comparator")
    assertThat(field?.type?.internalForm()).isEqualTo("Ljava/util/Comparator;")
    assertThat(field?.type?.kind).isEqualTo(JavaType.KIND_REF)
    assertThat(Modifier.isPublic(field?.accessFlags ?: 0)).isTrue()
    assertThat(Modifier.isStatic(field?.accessFlags ?: 0)).isTrue()
    assertThat(Modifier.isFinal(field?.accessFlags ?: 0)).isTrue()
  }

  @Test
  fun `test JavaMethod properties`() {
    val sampleClass = types.firstOrNull { it.fqn == "java/lang/String" }
    val methods = sampleClass?.methods

    assertThat(methods).isNotEmpty()
    methods?.forEach { mth ->
      val method = mth as JavaMethod
      assertThat(method.name).isNotEmpty()
      assertThat(method.accessFlags).isGreaterThan(0)
      assertThat(method.paramsTypes).isNotNull()
      assertThat(method.returnType).isNotNull()
    }

    assertThat(methods?.size).isAtLeast(1)

    val method = (methods as List<JavaMethod>?)?.firstOrNull { it.name == "compareTo" }
    assertThat(method).isNotNull()
    assertThat(method?.name).isEqualTo("compareTo")
    assertThat(method?.paramsTypes?.size).isEqualTo(1)
    assertThat(method?.returnType?.name).isEqualTo("I")
    assertThat(Modifier.isPublic(method?.accessFlags ?: 0)).isTrue()
    assertThat(Modifier.isStatic(method?.accessFlags ?: 0)).isFalse()
    assertThat(Modifier.isFinal(method?.accessFlags ?: 0)).isFalse()

    val param = method?.paramsTypes?.firstOrNull()
    assertThat(param).isNotNull()
    assertThat(param?.name).isEqualTo("java/lang/String")
    assertThat(param?.internalForm()).isEqualTo("Ljava/lang/String;")
    assertThat(param?.kind).isEqualTo(JavaType.KIND_REF)
    assertThat(param?.arrayDims).isEqualTo(0)
  }

  @Test
  fun `test JavaType properties`() {
    val sampleClass = types.firstOrNull { it.fqn == "java/lang/String" }
    val fields = sampleClass?.fields
    val methods = sampleClass?.methods

    fields?.forEach { f ->
      val field = f as JavaField
      assertThat(field.type?.name).isNotEmpty()
      assertThat(field.type?.kind).isNotEqualTo(JavaType.KIND_UNKNOWN)
    }

    methods?.forEach { mth ->
      val method = mth as JavaMethod
      method.paramsTypes?.forEach { paramType ->
        assertThat(paramType.name).isNotEmpty()
        assertThat(paramType.kind).isNotEqualTo(JavaType.KIND_UNKNOWN)
      }
      assertThat(method.returnType?.name).isNotEmpty()
      assertThat(method.returnType?.kind).isNotEqualTo(JavaType.KIND_UNKNOWN)
    }
  }

  @Test
  fun `test integer constant field values`() {
    val viewClass: IJavaType<JavaField, JavaMethod> =
      types.first { it.fqn == "android/view/View" } as IJavaType<JavaField, JavaMethod>
    assertThat(viewClass.fields).isNotEmpty()

    val fields = viewClass.fields!!
    val visible = fields.firstOrNull { it.name == "VISIBLE" }
    assertThat(visible).isNotNull()
    assertThat(visible?.type).isEqualTo(JavaType.INT)

    val constantValue = visible?.constantValue
    assertThat(constantValue).isNotNull()
    assertThat(constantValue?.kind).isEqualTo(JavaType.KIND_INT)
    assertThat(constantValue?.value?.type).isEqualTo(RealmAny.Type.INTEGER)
    assertThat(constantValue?.asInt()).isNotNull()
    assertThat(constantValue?.asInt()).isEqualTo(View.VISIBLE)
  }

  @Test
  fun `test object type`() {
    val obj = types.firstOrNull { it.fqn == "java/lang/Object" }
    assertThat(obj).isNotNull()
    assertThat(obj?.name).isEqualTo("Object")
    assertThat(obj?.superClassFqn).isNull()
    assertThat(obj?.superInterfacesFqn).isEmpty()
    assertThat(obj?.fields).isEmpty()
    assertThat(obj?.methods).isNotEmpty()
    assertThat(obj?.isAnnotation).isFalse()
    assertThat(obj?.isInterface).isFalse()
    assertThat(obj?.isEnum).isFalse()
    assertThat(obj?.isClass).isTrue()
    assertThat(obj).isInstanceOf(JavaClass::class.java)
    assertThat(obj?.packageName).isEqualTo("java/lang")

    val access = AccessFlags(obj!!.accessFlags)
    assertThat(access.`is`(AccessFlags.ACC_PUBLIC)).isTrue()
    assertThat(access.`is`(AccessFlags.ACC_STATIC)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_INTERFACE)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ANNOTATION)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ENUM)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ABSTRACT)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_FINAL)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_NATIVE)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_NATIVE)).isFalse()
  }

  @Test
  fun `test class simple name`() {
    val obj = types.firstOrNull { it.fqn == "android/view/View" }
    assertThat(obj).isNotNull()
    assertThat(obj?.name).isEqualTo("View")
    assertThat(obj?.packageName).isEqualTo("android/view")
    assertThat(obj?.superClassFqn).isEqualTo("java/lang/Object")
    assertThat(obj?.superInterfacesFqn).isNotEmpty()
    assertThat(obj?.fields).isNotEmpty()
    assertThat(obj?.methods).isNotEmpty()
    assertThat(obj?.isAnnotation).isFalse()
    assertThat(obj?.isInterface).isFalse()
    assertThat(obj?.isEnum).isFalse()
    assertThat(obj?.isClass).isTrue()
    assertThat(obj).isInstanceOf(JavaClass::class.java)

    val access = AccessFlags(obj!!.accessFlags)
    assertThat(access.`is`(AccessFlags.ACC_PUBLIC)).isTrue()
    assertThat(access.`is`(AccessFlags.ACC_STATIC)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_INTERFACE)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ANNOTATION)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ENUM)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_ABSTRACT)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_FINAL)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_NATIVE)).isFalse()
    assertThat(access.`is`(AccessFlags.ACC_NATIVE)).isFalse()
  }

  @Test
  fun `test class superclass name`() {
    types.firstOrNull { it.fqn == "android/view/View" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.superClassFqn).isEqualTo("java/lang/Object")
    }
  }

  @Test
  fun `test class super interface names`() {
    types.firstOrNull { it.fqn == "android/view/View" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.superInterfacesFqn).containsExactly(
        "android/graphics/drawable/Drawable\$Callback",
        "android/view/KeyEvent\$Callback",
        "android/view/accessibility/AccessibilityEventSource"
      )
    }
  }

  @Test
  fun `test interface super interface names`() {
    types.firstOrNull { it.fqn == "java/io/Closeable" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.superInterfacesFqn).containsExactly("java/lang/AutoCloseable")
    }
  }

  @Test
  fun `test class fields`() {
    types.firstOrNull { it.fqn == "android/view/View" }.also { view ->
      assertThat(view).isNotNull()
      assertThat(view?.fields?.map { field -> (field as JavaField).name }).containsAtLeast(
        "VISIBLE",
        "INVISIBLE",
        "GONE",
        "FOCUS_DOWN",
        "FOCUS_UP",
        "FOCUS_LEFT",
        "FOCUS_RIGHT",
        "FOCUS_BACKWARD",
        "FOCUS_FORWARD",
      )
    }

    types.firstOrNull { it.fqn == "android/view/ViewGroup\$LayoutParams" }.also { view ->
      assertThat(view).isNotNull()
      assertThat(view?.fields?.map { field -> (field as JavaField).name }).containsAtLeast(
        "WRAP_CONTENT",
        "MATCH_PARENT",
        "FILL_PARENT",
      )
    }
  }

  @Test
  fun `test interface fields`() {
    types.firstOrNull { it.fqn == "android/provider/CalendarContract\$CalendarSyncColumns" }
      .also { view ->
        assertThat(view).isNotNull()
        assertThat(view?.fields?.map { field -> (field as JavaField).name }).containsAtLeast(
          "CAL_SYNC1",
          "CAL_SYNC2",
          "CAL_SYNC3",
          "CAL_SYNC4",
          "CAL_SYNC5",
          "CAL_SYNC6",
          "CAL_SYNC7",
          "CAL_SYNC8",
          "CAL_SYNC9",
          "CAL_SYNC10",
        )
      }
  }

  @Test
  fun `test mandatory enum fields`() {
    types.filterIsInstance<JavaEnum>().forEach { enum ->
      assertThat(enum.fields?.map { it.name }).contains("\$VALUES")
    }
  }

  @Test
  fun `test enum fields`() {
    types.firstOrNull { it.fqn == "android/net/wifi/SupplicantState" }.also { enum ->
      assertThat(enum?.fields?.map { (it as JavaField).name })
        .containsAtLeast(
          "DISCONNECTED",
          "INTERFACE_DISABLED",
          "INACTIVE",
          "SCANNING",
          "AUTHENTICATING",
          "ASSOCIATING",
          "ASSOCIATED",
          "FOUR_WAY_HANDSHAKE",
          "COMPLETED",
          "GROUP_HANDSHAKE",
          "DORMANT",
          "UNINITIALIZED",
          "INVALID"
        )
    }
  }

  @Test
  fun `test annotation fields`() {
    // android.jar doesn't seem to contain annotation classes with some fields
    // TODO: add test for verifying how annotation fields are handled
  }

  @Test
  fun `test class methods`() {
    types.firstOrNull { it.fqn == "android/view/View" }.also { view ->
      assertThat(view).isNotNull()
      assertThat(view?.methods?.map { field -> (field as JavaMethod).signature() }).containsAtLeast(
        "setId(I)V",
        "getId()I",
        "setOnClickListener(Landroid/view/View\$OnClickListener;)V",
        "hasOnClickListeners()Z",
        "hasOnLongClickListeners()Z",
        "setAccessibilityDelegate(Landroid/view/View\$AccessibilityDelegate;)V",
        "getAccessibilityDelegate()Landroid/view/View\$AccessibilityDelegate;",
        "setAccessibilityLiveRegion(I)V",
        "getAccessibilityLiveRegion()I",
        "setAccessibilityHeading(Z)V",
        "isAccessibilityHeading()Z",
        "setPadding(IIII)V",
        "setPaddingRelative(IIII)V",
      )
    }
  }

  @Test
  fun `test interface methods`() {
    types.firstOrNull { it.fqn == "android/view/View\$OnClickListener" }.also { view ->
      assertThat(view).isNotNull()
      assertThat(view?.methods?.map { field -> (field as JavaMethod).signature() })
        .contains("onClick(Landroid/view/View;)V")
    }
    types.firstOrNull { it.fqn == "android/view/View\$OnLongClickListener" }.also { view ->
      assertThat(view).isNotNull()
      assertThat(view?.methods?.map { field -> (field as JavaMethod).signature() })
        .containsAtLeast(
          "onLongClick(Landroid/view/View;)Z",
          "onLongClickUseDefaultHapticFeedback(Landroid/view/View;)Z"
        )
    }
  }

  @Test
  fun `test mandatory enum methods`() {
    types.filterIsInstance<JavaEnum>().forEach { enum ->
      assertThat(enum.methods?.map { it.signature() }).containsAtLeast(
        "values()[L${enum.fqn};",
        "\$values()[L${enum.fqn};",
        "valueOf(Ljava/lang/String;)L${enum.fqn};",
        "<clinit>()V"
      )
    }
  }

  @Test
  fun `test enum methods`() {
    types.firstOrNull { it.fqn == "android/icu/text/DisplayContext" }.also { enum ->
      assertThat(enum?.methods?.map { (it as JavaMethod).signature() }).contains(
        "type()Landroid/icu/text/DisplayContext\$Type;"
      )
    }
  }

  @Test
  fun `test annotation methods`() {
    types.firstOrNull { it.fqn == "android/annotation/TargetApi" }.also { annotation ->
      assertThat(annotation?.methods?.map { (it as AnnotationElement).signature() }).containsExactly(
        "value()I"
      )
    }
  }

  @Test
  fun `test annotation methods with default values`() {
    types.firstOrNull { it.fqn == "java/lang/Deprecated" }.also { annotation ->
      assertThat(annotation?.methods).isNotEmpty()

      val since =
        annotation?.methods?.find { (it as AnnotationElement).name == "since" } as AnnotationElement
      assertThat(since.type).isEqualTo(JavaType.STRING)

      var defValue = since.defaultValue
      assertThat(defValue).isNotNull()
      assertThat(defValue?.valueClass).isEqualTo(PrimitiveAnnotationElementValue::class.java)

      var defVal = defValue?.asRealmModel(PrimitiveAnnotationElementValue::class.java)
      assertThat(defVal).isNotNull()
      assertThat(defVal?.kind).isEqualTo(IAnnotationElementValue.KIND_STRING)
      assertThat(defVal?.value).isNotNull()
      assertThat(defVal?.value?.kind).isEqualTo(JavaType.KIND_REF)
      assertThat(defVal?.value?.asString()).isEqualTo("")
      assertThat(defVal?.value?.asBoolean()).isNull()
      assertThat(defVal?.value?.asByte()).isNull()
      assertThat(defVal?.value?.asShort()).isNull()
      assertThat(defVal?.value?.asChar()).isNull()
      assertThat(defVal?.value?.asInt()).isNull()
      assertThat(defVal?.value?.asLong()).isNull()
      assertThat(defVal?.value?.asFloat()).isNull()
      assertThat(defVal?.value?.asDouble()).isNull()

      val forRemoval =
        annotation.methods?.find { (it as AnnotationElement).name == "forRemoval" } as AnnotationElement
      assertThat(forRemoval.type).isEqualTo(JavaType.BOOLEAN)

      defValue = forRemoval.defaultValue
      assertThat(defValue).isNotNull()
      assertThat(defValue?.valueClass).isEqualTo(PrimitiveAnnotationElementValue::class.java)

      defVal = defValue?.asRealmModel(PrimitiveAnnotationElementValue::class.java)
      assertThat(defVal).isNotNull()
      assertThat(defVal?.kind).isEqualTo(IAnnotationElementValue.KIND_BOOLEAN)
      assertThat(defVal?.value).isNotNull()
      assertThat(defVal?.value?.kind).isEqualTo(JavaType.KIND_BOOLEAN)
      assertThat(defVal?.value?.asBoolean()).isEqualTo(false)
      assertThat(defVal?.value?.asByte()).isNull()
      assertThat(defVal?.value?.asShort()).isNull()
      assertThat(defVal?.value?.asChar()).isNull()
      assertThat(defVal?.value?.asInt()).isNull()
      assertThat(defVal?.value?.asLong()).isNull()
      assertThat(defVal?.value?.asFloat()).isNull()
      assertThat(defVal?.value?.asDouble()).isNull()
      assertThat(defVal?.value?.asString()).isNull()
    }
  }

  @Test
  fun `test annotation methods with array value`() {
    types.firstOrNull { it.fqn == "java/lang/SuppressWarnings" }.also { annotation ->
      assertThat(annotation?.methods).isNotEmpty()

      val value =
        annotation?.methods?.find { (it as AnnotationElement).name == "value" } as AnnotationElement
      assertThat(value.type?.kind).isEqualTo(JavaType.KIND_REF)
      assertThat(value.type?.name).isEqualTo("java/lang/String")
      assertThat(value.type?.arrayDims).isEqualTo(1)

      val defValue = value.defaultValue
      assertThat(defValue).isNull()
    }
  }

  @Test
  fun `test class type classification`() {
    types.firstOrNull { it.fqn == "android/view/View" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.isClass).isTrue()
      assertThat(it?.isInterface).isFalse()
      assertThat(it?.isAnnotation).isFalse()
      assertThat(it?.isEnum).isFalse()
      assertThat(it).isInstanceOf(JavaClass::class.java)
    }
  }

  @Test
  fun `test interface type classification`() {
    types.firstOrNull { it.fqn == "android/view/View\$OnClickListener" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.isClass).isFalse()
      assertThat(it?.isInterface).isTrue()
      assertThat(it?.isAnnotation).isFalse()
      assertThat(it?.isEnum).isFalse()
      assertThat(it).isInstanceOf(JavaInterface::class.java)
    }
  }

  @Test
  fun `test annotation type classification`() {
    types.firstOrNull { it.fqn == "java/lang/Deprecated" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.isClass).isFalse()
      assertThat(it?.isInterface).isTrue()
      assertThat(it?.isAnnotation).isTrue()
      assertThat(it?.isEnum).isFalse()
      assertThat(it).isInstanceOf(JavaAnnotation::class.java)
    }
  }

  @Test
  fun `test enum type classification`() {
    types.firstOrNull { it.fqn == "android/icu/text/DisplayContext" }.also {
      assertThat(it).isNotNull()
      assertThat(it?.isClass).isFalse()
      assertThat(it?.isInterface).isFalse()
      assertThat(it?.isAnnotation).isFalse()
      assertThat(it?.isEnum).isTrue()
      assertThat(it).isInstanceOf(JavaEnum::class.java)
    }
  }
}
