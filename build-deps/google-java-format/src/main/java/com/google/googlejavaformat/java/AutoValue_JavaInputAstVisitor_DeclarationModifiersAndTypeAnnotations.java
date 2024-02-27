package com.google.googlejavaformat.java;

import com.google.common.collect.ImmutableList;
import openjdk.source.tree.AnnotationTree;

import jdkx.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_JavaInputAstVisitor_DeclarationModifiersAndTypeAnnotations
    extends JavaInputAstVisitor.DeclarationModifiersAndTypeAnnotations {

  private final ImmutableList<JavaInputAstVisitor.AnnotationOrModifier> declarationModifiers;

  private final ImmutableList<AnnotationTree> typeAnnotations;

  AutoValue_JavaInputAstVisitor_DeclarationModifiersAndTypeAnnotations(
      ImmutableList<JavaInputAstVisitor.AnnotationOrModifier> declarationModifiers,
      ImmutableList<AnnotationTree> typeAnnotations) {
    if (declarationModifiers == null) {
      throw new NullPointerException("Null declarationModifiers");
    }
    this.declarationModifiers = declarationModifiers;
    if (typeAnnotations == null) {
      throw new NullPointerException("Null typeAnnotations");
    }
    this.typeAnnotations = typeAnnotations;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= declarationModifiers.hashCode();
    h$ *= 1000003;
    h$ ^= typeAnnotations.hashCode();
    return h$;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof JavaInputAstVisitor.DeclarationModifiersAndTypeAnnotations) {
      JavaInputAstVisitor.DeclarationModifiersAndTypeAnnotations that =
          (JavaInputAstVisitor.DeclarationModifiersAndTypeAnnotations) o;
      return this.declarationModifiers.equals(that.declarationModifiers())
          && this.typeAnnotations.equals(that.typeAnnotations());
    }
    return false;
  }

  @Override
  public String toString() {
    return "DeclarationModifiersAndTypeAnnotations{"
        + "declarationModifiers="
        + declarationModifiers
        + ", "
        + "typeAnnotations="
        + typeAnnotations
        + "}";
  }

  @Override
  ImmutableList<AnnotationTree> typeAnnotations() {
    return typeAnnotations;
  }

  @Override
  ImmutableList<JavaInputAstVisitor.AnnotationOrModifier> declarationModifiers() {
    return declarationModifiers;
  }
}
