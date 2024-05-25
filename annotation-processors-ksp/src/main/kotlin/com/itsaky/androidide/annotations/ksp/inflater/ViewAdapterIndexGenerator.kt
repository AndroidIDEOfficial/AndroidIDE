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

package com.itsaky.androidide.annotations.ksp.inflater

import androidx.annotation.RequiresApi
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.ksp.getKSAnnotationsByType
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.Collections
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC

/**
 * Generates the ViewAdapterIndex class.
 *
 * @author Akash Yadav
 */
class ViewAdapterIndexGenerator(private val logger: KSPLogger) {

  private val indexClassBuilder =
    TypeSpec.classBuilder(INDEX_CLASS_NAME)
      .addModifiers(PUBLIC, FINAL)
      .addSuperinterface(viewAdapterInterface)
  private val indexAddStatements = CodeBlock.builder()

  fun init() {
    val adapterType =
      ParameterizedTypeName.get(
        ClassName.get(ADAPTER_BASE_CLASS_PCK, ADAPTER_BASE_CLASS_NAME),
        WildcardTypeName.subtypeOf(viewClass)
      )
    val mapOfStringToViewAdapter =
      ParameterizedTypeName.get(
        ClassName.get(Map::class.java),
        ClassName.get(String::class.java),
        adapterType
      )
    indexClassBuilder.addField(
      FieldSpec.builder(mapOfStringToViewAdapter, INDEX_MAP_FIELD, PRIVATE, STATIC, FINAL).build()
    )

    indexAddStatements.addStatement(
      "final var adapters = new \$T()",
      ParameterizedTypeName.get(
        ClassName.get(HashMap::class.java),
        ClassName.get(String::class.java),
        adapterType
      )
    )

    //    addPrivateConstructor(indexClassBuilder)

    val indexImpl = ClassName.get(INDEX_PACKAGE_NAME, INDEX_CLASS_NAME)
    indexClassBuilder.addField(
      FieldSpec.builder(indexImpl, "INSTANCE", PUBLIC, STATIC, FINAL)
        .initializer("new \$T()", indexImpl)
        .build()
    )

    indexClassBuilder.addMethod(
      MethodSpec.methodBuilder("getAdapter")
        .addAnnotation(Override::class.java)
        .addAnnotation(ClassName.get("androidx.annotation", "Nullable"))
        .addModifiers(PUBLIC)
        .addParameter(String::class.java, "view", FINAL)
        .returns(adapterType)
        .addStatement("var adapter = \$L.get(\$L)", INDEX_MAP_FIELD, "view")
        .beginControlFlow("if (adapter == null)")
        .addStatement("adapter = \$L.get(\$S)", INDEX_MAP_FIELD, VIEW_CLASS)
        .endControlFlow()
        .addStatement("return adapter")
        .build()
    )

    indexClassBuilder.addMethod(
      MethodSpec.methodBuilder("getViewAdapter")
        .addAnnotation(Override::class.java)
        .addAnnotation(ClassName.get("androidx.annotation", "Nullable"))
        .addModifiers(PUBLIC)
        .addParameter(String::class.java, "view", FINAL)
        .returns(adapterType)
        .addStatement("return \$L.get(\$L)", INDEX_MAP_FIELD, "view")
        .build()
    )

    val listOfViewAdapters =
      ParameterizedTypeName.get(ClassName.get(java.util.List::class.java), adapterType)
    val mapOfGroupToListOfViewAdapter =
      ParameterizedTypeName.get(
        ClassName.get(Map::class.java),
        ClassName.get(IncludeInDesigner.Group::class.java),
        listOfViewAdapters
      )

    indexClassBuilder.addField(
      FieldSpec.builder(
          mapOfGroupToListOfViewAdapter,
          INDEX_PROVIDER_MAP_FIELD,
          PRIVATE,
          STATIC,
          FINAL
        )
        .build()
    )

    indexAddStatements.addStatement(
      "final var providers = new \$T()",
      ParameterizedTypeName.get(
        ClassName.get(HashMap::class.java),
        ClassName.get(IncludeInDesigner.Group::class.java),
        listOfViewAdapters
      )
    )

    val nonNull = ClassName.get("androidx.annotation", "NonNull")
    val groupParam =
      ParameterSpec.builder(IncludeInDesigner.Group::class.java, "group", FINAL).build()

    indexClassBuilder.addMethod(
      MethodSpec.methodBuilder("getWidgetProviders")
        .addAnnotation(Override::class.java)
        .addAnnotation(nonNull)
        .addModifiers(PUBLIC)
        .returns(listOfViewAdapters)
        .addParameter(groupParam)
        .addStatement("return \$L.get(group)", INDEX_PROVIDER_MAP_FIELD)
        .build()
    )
  }

  @OptIn(KspExperimental::class)
  fun addViewAdapter(sym: KSClassDeclaration): Boolean {
    val block = CodeBlock.builder()

    val viewAdapter =
      sym.getKSAnnotationsByType(ViewAdapter::class).iterator().run {
        val result = next()
        if (hasNext()) {
          logger.error("${ViewAdapter::class.simpleName} can only be applied once.", sym)
          return false
        }
        result
      }

    val includeInDesigner =
      if (sym.isAnnotationPresent(IncludeInDesigner::class)) {
        sym.getAnnotationsByType(IncludeInDesigner::class).iterator().run {
          val result = next()
          if (hasNext()) {
            logger.error("${IncludeInDesigner::class.simpleName} can only be applied once.", sym)
            return false
          }
          result
        }
      } else null
    
    val requiresApi =
      if (sym.isAnnotationPresent(RequiresApi::class))
        sym.getAnnotationsByType(RequiresApi::class).iterator().next()
      else null

    val viewName = getViewName(viewAdapter)
    val moduleNs = getModuleNs(viewAdapter)

    val adapterTypeName = ClassName.get(sym.packageName.asString(), sym.simpleName.asString())
    val viewTypeName =
      ClassName.get(viewName.substringBeforeLast('.'), viewName.substringAfterLast('.'))

    requiresApi?.let { annotation ->
      val api = if (annotation.value == 1) annotation.api else annotation.value
      val androidBuild = ClassName.get("android.os", "Build")
      block.beginControlFlow("if(\$T.VERSION.SDK_INT >= \$L)", androidBuild, api)
    }

    block.addStatement(
      "final var adapter = new \$T()",
      ParameterizedTypeName.get(adapterTypeName, viewTypeName)
    )

    block.addStatement("adapter.\$L(\$S)", METHOD_SET_MODULE, moduleNs)
    block.addStatement("adapters.put(\$S, adapter)", viewName)

    if (includeInDesigner != null) {
      assertOverridesCreateWidget(sym)
      block.addStatement(
        "providers.computeIfAbsent(\$T.\$L, g -> new \$T<>()).add(adapter)",
        ClassName.get(IncludeInDesigner.Group::class.java),
        includeInDesigner.group,
        ClassName.get(java.util.ArrayList::class.java)
      )
    }

    requiresApi?.let { block.endControlFlow() }

    indexAddStatements.add("{\n")
    indexAddStatements.indent()
    indexAddStatements.add(block.build())
    indexAddStatements.unindent()
    indexAddStatements.add("}\n")

    return true
  }

  fun generate(out: OutputStream) {
    val javaCollections = ClassName.get(Collections::class.java)

    indexAddStatements.addStatement(
      "\$L = \$T.unmodifiableMap(adapters)",
      INDEX_MAP_FIELD,
      javaCollections
    )

    indexAddStatements.addStatement(
      "\$L = \$T.unmodifiableMap(providers)",
      INDEX_PROVIDER_MAP_FIELD,
      javaCollections
    )

    indexClassBuilder.addStaticBlock(indexAddStatements.build())

    OutputStreamWriter(out).use {
      val file = JavaFile.builder(INDEX_PACKAGE_NAME, indexClassBuilder.build())
      file.build().writeTo(it)
    }
  }

  private fun assertOverridesCreateWidget(sym: KSClassDeclaration): Boolean {
    return sym.declarations
      .filter { it is KSFunctionDeclaration }
      .find { ADAPTER_FUNC_CREATE_WIDGET == it.simpleName.asString() } != null
  }

  private fun getViewName(viewAdapter: KSAnnotation): String {
    return getViewType(viewAdapter).declaration.qualifiedName!!.asString()
  }

  private fun getViewType(viewAdapter: KSAnnotation) =
    (getAnnotationArg(viewAdapter, "forView") as KSType)

  private fun getModuleNs(viewAdapter: KSAnnotation) =
    (getAnnotationArg(viewAdapter, "moduleNamespace") as String)

  private fun getAnnotationArg(viewAdapter: KSAnnotation, arg: String) =
    viewAdapter.arguments.first { it.name!!.asString() == arg }.value
}
