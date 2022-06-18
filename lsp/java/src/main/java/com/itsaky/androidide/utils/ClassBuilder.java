package com.itsaky.androidide.utils;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class ClassBuilder {

  public static String createClass(String packageName, String className) {
    return toJavaFile(packageName, newClassSpec(className)).toString();
  }

  public static String createInterface(String packageName, String className) {
    return toJavaFile(packageName, newInterfaceSpec(className)).toString();
  }

  public static String createEnum(String packageName, String className) {
    return toJavaFile(packageName, newEnumSpec(className)).toString();
  }

  public static String createActivity(String packageName, String className) {
    MethodSpec onCreate =
        MethodSpec.methodBuilder("onCreate")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .addParameter(android.os.Bundle.class, "savedInstanceState")
            .addStatement("super.onCreate(savedInstanceState)")
            .build();

    TypeSpec.Builder activity =
        newClassSpec(className).toBuilder()
            .superclass(androidx.appcompat.app.AppCompatActivity.class)
            .addMethod(onCreate);

    return JavaFile.builder(packageName, activity.build()).build().toString();
  }

  // TODO: Allow user to choose number of spaces to indent
  // Most probably, get this from preferences
  public static JavaFile toJavaFile(String packageName, TypeSpec type) {
    return JavaFile.builder(packageName, type)
        .indent("    ") // Indent 4 spaces
        .build();
  }

  public static TypeSpec newClassSpec(String className) {
    return TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).build();
  }

  public static TypeSpec newInterfaceSpec(String className) {
    return TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC).build();
  }

  public static TypeSpec newEnumSpec(String className) {
    return TypeSpec.enumBuilder(className)
        .addModifiers(Modifier.PUBLIC)
        .addEnumConstant("ENUM_DECLARED")
        .build();
  }
}
