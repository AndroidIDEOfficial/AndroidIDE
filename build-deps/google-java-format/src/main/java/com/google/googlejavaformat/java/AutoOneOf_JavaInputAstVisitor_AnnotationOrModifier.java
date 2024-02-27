package com.google.googlejavaformat.java;

import com.google.googlejavaformat.Input;
import openjdk.source.tree.AnnotationTree;

import jdkx.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoOneOfProcessor")
final class AutoOneOf_JavaInputAstVisitor_AnnotationOrModifier {
  private
  AutoOneOf_JavaInputAstVisitor_AnnotationOrModifier() {} // There are no instances of this type.

  static JavaInputAstVisitor.AnnotationOrModifier annotation(AnnotationTree annotation) {
    if (annotation == null) {
      throw new NullPointerException();
    }
    return new Impl_annotation(annotation);
  }

  static JavaInputAstVisitor.AnnotationOrModifier modifier(Input.Tok modifier) {
    if (modifier == null) {
      throw new NullPointerException();
    }
    return new Impl_modifier(modifier);
  }

  // Parent class that each implementation will inherit from.
  private abstract static class Parent_ extends JavaInputAstVisitor.AnnotationOrModifier {
    @Override
    AnnotationTree annotation() {
      throw new UnsupportedOperationException(getKind().toString());
    }

    @Override
    Input.Tok modifier() {
      throw new UnsupportedOperationException(getKind().toString());
    }
  }

  // Implementation when the contained property is "annotation".
  private static final class Impl_annotation extends Parent_ {
    private final AnnotationTree annotation;

    Impl_annotation(AnnotationTree annotation) {
      this.annotation = annotation;
    }

    @Override
    public AnnotationTree annotation() {
      return annotation;
    }

    @Override
    public int hashCode() {
      return annotation.hashCode();
    }

    @Override
    public boolean equals(Object x) {
      if (x instanceof JavaInputAstVisitor.AnnotationOrModifier) {
        JavaInputAstVisitor.AnnotationOrModifier that =
            (JavaInputAstVisitor.AnnotationOrModifier) x;
        return this.getKind() == that.getKind() && this.annotation.equals(that.annotation());
      } else {
        return false;
      }
    }

    @Override
    public String toString() {
      return "AnnotationOrModifier{annotation=" + this.annotation + "}";
    }

    @Override
    public JavaInputAstVisitor.AnnotationOrModifier.Kind getKind() {
      return JavaInputAstVisitor.AnnotationOrModifier.Kind.ANNOTATION;
    }
  }

  // Implementation when the contained property is "modifier".
  private static final class Impl_modifier extends Parent_ {
    private final Input.Tok modifier;

    Impl_modifier(Input.Tok modifier) {
      this.modifier = modifier;
    }

    @Override
    public Input.Tok modifier() {
      return modifier;
    }

    @Override
    public int hashCode() {
      return modifier.hashCode();
    }

    @Override
    public boolean equals(Object x) {
      if (x instanceof JavaInputAstVisitor.AnnotationOrModifier) {
        JavaInputAstVisitor.AnnotationOrModifier that =
            (JavaInputAstVisitor.AnnotationOrModifier) x;
        return this.getKind() == that.getKind() && this.modifier.equals(that.modifier());
      } else {
        return false;
      }
    }

    @Override
    public String toString() {
      return "AnnotationOrModifier{modifier=" + this.modifier + "}";
    }

    @Override
    public JavaInputAstVisitor.AnnotationOrModifier.Kind getKind() {
      return JavaInputAstVisitor.AnnotationOrModifier.Kind.MODIFIER;
    }
  }
}
