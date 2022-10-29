package com.itsaky.androidide.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itsaky.androidide.preferences.internal.tabSize
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier.PROTECTED
import javax.lang.model.element.Modifier.PUBLIC

object ClassBuilder {
  @JvmStatic
  fun createClass(packageName: String, className: String): String {
    return toJavaFile(packageName, newClassSpec(className)).toString()
  }

  private fun toJavaFile(
    packageName: String,
    type: TypeSpec,
    block: JavaFile.Builder.() -> Unit = {}
  ): JavaFile {
    return JavaFile.builder(packageName, type)
      .indent(" ".repeat(tabSize))
      .apply { block(this) }
      .build()
  }

  private fun newClassSpec(className: String): TypeSpec {
    return TypeSpec.classBuilder(className).addModifiers(PUBLIC).build()
  }

  @JvmStatic
  fun createInterface(packageName: String, className: String): String {
    return toJavaFile(packageName, newInterfaceSpec(className)).toString()
  }

  private fun newInterfaceSpec(className: String): TypeSpec {
    return TypeSpec.interfaceBuilder(className).addModifiers(PUBLIC).build()
  }

  @JvmStatic
  fun createEnum(packageName: String, className: String): String {
    return toJavaFile(packageName, newEnumSpec(className)).toString()
  }

  private fun newEnumSpec(className: String): TypeSpec {
    return TypeSpec.enumBuilder(className)
      .addModifiers(PUBLIC)
      .addEnumConstant("ENUM_DECLARED")
      .build()
  }

  @JvmStatic
  fun createActivity(packageName: String, className: String): String {
    val onCreate =
      MethodSpec.methodBuilder("onCreate")
        .addAnnotation(Override::class.java)
        .addModifiers(PROTECTED)
        .addParameter(Bundle::class.java, "savedInstanceState")
        .addStatement("super.onCreate(savedInstanceState)")
        .build()
    val activity =
      newClassSpec(className)
        .toBuilder()
        .superclass(AppCompatActivity::class.java)
        .addMethod(onCreate)
    return toJavaFile(packageName, activity.build()) { skipJavaLangImports(true) }.toString()
  }
}
