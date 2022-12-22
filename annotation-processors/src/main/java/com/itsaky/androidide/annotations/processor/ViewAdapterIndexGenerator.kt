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

package com.itsaky.androidide.annotations.processor

import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.WildcardTypeName
import java.io.File
import java.util.Collections
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.Modifier.PUBLIC
import javax.lang.model.element.Modifier.STATIC
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic.Kind.ERROR

/**
 * Generates the ViewAdapterIndex class.
 *
 * @author Akash Yadav
 */
class ViewAdapterIndexGenerator {

  private val indexClassBuilder =
    TypeSpec.classBuilder(INDEX_CLASS_NAME).addModifiers(PUBLIC, FINAL)
  private val indexAddStatements = CodeBlock.builder()

  fun init(env: ProcessingEnvironment) {
    val adapterType =
      ParameterizedTypeName.get(
        ClassName.get(ADAPTER_BASE_CLASS_PCK, ADAPTER_BASE_CLASS_NAME),
        WildcardTypeName.subtypeOf(ClassName.get(env.viewElement()))
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

    addUninstantiableConstructor(indexClassBuilder)

    indexClassBuilder.addMethod(
      MethodSpec.methodBuilder("getAdapter")
        .addAnnotation(ClassName.get("androidx.annotation", "Nullable"))
        .addModifiers(PUBLIC, STATIC)
        .addParameter(String::class.java, "view", FINAL)
        .returns(adapterType)
        .addStatement("var adapter = \$L.get(\$L)", INDEX_MAP_FIELD, "view")
        .beginControlFlow("if (adapter == null)")
        .addStatement("adapter = \$L.get(\$S)", INDEX_MAP_FIELD, env.viewElement().qualifiedName)
        .endControlFlow()
        .addStatement("return adapter")
        .build()
    )
  
    indexClassBuilder.addMethod(
      MethodSpec.methodBuilder("getViewAdapter")
        .addAnnotation(ClassName.get("androidx.annotation", "Nullable"))
        .addModifiers(PUBLIC, STATIC)
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
        .addAnnotation(nonNull)
        .addModifiers(PUBLIC, STATIC)
        .returns(listOfViewAdapters)
        .addParameter(groupParam)
        .addStatement("return \$L.get(group)", INDEX_PROVIDER_MAP_FIELD)
        .build()
    )
  }

  private val listOfString: TypeName =
    ParameterizedTypeName.get(java.util.ArrayList::class.java, String::class.java)

  fun addViewAdapter(processingEnv: ProcessingEnvironment, element: TypeElement) {
    val block = CodeBlock.builder()

    val viewAdapter =
      element.getAnnotation(ViewAdapter::class.java) ?: throw IllegalStateException()
    val includeInDesigner = element.getAnnotation(IncludeInDesigner::class.java)

    val viewName = getViewName(processingEnv, viewAdapter)
    val superclasses = getViewSuperclasses(processingEnv, viewAdapter)

    block.addStatement("final var superclasses = new \$T()", listOfString)
    for (superclass in superclasses) {
      block.addStatement("superclasses.add(\$S)", superclass)
    }

    val adapterType = ClassName.get(element)
    val paramType = getViewTypeName(processingEnv, viewAdapter)

    block.add("\n")
    block.addStatement(
      "final var adapter = new \$T()",
      ParameterizedTypeName.get(adapterType, paramType)
    )
    block.addStatement("adapter.\$L(superclasses)", METHOD_SET_SUPERCLASS_HIERARCHY)
    block.addStatement("adapter.\$L(\$S)", METHOD_SET_MODULE, viewAdapter.moduleNamespace)
    block.addStatement("adapters.put(\$S, adapter)", viewName)

    if (includeInDesigner != null) {
      assertOverridesCreateWidget(processingEnv, element)
      block.addStatement(
        "providers.computeIfAbsent(\$T.\$L, g -> new ArrayList<>()).add(adapter)",
        ClassName.get(IncludeInDesigner.Group::class.java),
        includeInDesigner.group
      )
    }

    indexAddStatements.add("{\n")
    indexAddStatements.indent()
    indexAddStatements.add(block.build())
    indexAddStatements.unindent()
    indexAddStatements.add("}\n")
  }

  private fun assertOverridesCreateWidget(env: ProcessingEnvironment, element: TypeElement) {
    element.enclosedElements.filterIsInstance<ExecutableElement>().find {
      ADAPTER_FUNC_CREATE_WIDGET == it.simpleName.toString()
    }
      ?: env.messager.printMessage(
        ERROR,
        "${element.qualifiedName} annotated with ${IncludeInDesigner::class.java.simpleName} must override $ADAPTER_FUNC_CREATE_WIDGET"
      )
  }

  fun generate(outDir: String) {
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
    val file = JavaFile.builder(INDEX_PACKAGE_NAME, indexClassBuilder.build())
    val generatedDir = File(outDir)
    file.build().writeTo(generatedDir)
  }

  private fun addUninstantiableConstructor(builder: TypeSpec.Builder) {
    builder.addMethod(
      MethodSpec.constructorBuilder()
        .addModifiers(PRIVATE)
        .addStatement(
          "throw new \$T(\$S)",
          UnsupportedOperationException::class.java,
          "This class cannot be instantiated."
        )
        .build()
    )
  }

  private fun getViewTypeName(
    processingEnv: ProcessingEnvironment,
    annotation: ViewAdapter
  ): TypeName {
    return try {
      ClassName.get(annotation.forView.java)
        ?: throw IllegalStateException("Cannot find type of $annotation")
    } catch (err: MirroredTypeException) {
      (processingEnv.typeUtils.asElement(err.typeMirror) as TypeElement).let { ClassName.get(it) }
    }
  }

  private fun getViewName(processingEnv: ProcessingEnvironment, annotation: ViewAdapter): String {
    return try {
      annotation.forView.qualifiedName
        ?: throw IllegalStateException("Cannot find type of $annotation")
    } catch (err: MirroredTypeException) {
      (processingEnv.typeUtils.asElement(err.typeMirror) as TypeElement).qualifiedName.toString()
    }
  }

  private fun getViewSuperclasses(
    processingEnv: ProcessingEnvironment,
    annotation: ViewAdapter
  ): List<String> {
    val result = mutableListOf<String>()
    try {
      val view = annotation.forView
      var superclass = view.java.superclass
      while (superclass != null) {
        result.add(superclass.name)
        superclass = superclass.superclass
      }
    } catch (err: MirroredTypeException) {
      val typeElement = processingEnv.typeUtils.asElement(err.typeMirror) as TypeElement
      result.add(typeElement.qualifiedName.toString())

      var superclass = typeElement.superclass
      while (superclass != null) {
        val superType = processingEnv.typeUtils.asElement(superclass) as? TypeElement?
        superType ?: break
        result.add(superType.qualifiedName.toString())
        superclass = superType.superclass
      }
    }

    return result
  }
}
