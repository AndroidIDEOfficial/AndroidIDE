package com.itsaky.lsp.java.utils;

import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.AbstractTypeVisitor8;

// TODO this should be package-private once completions code is refactored
public class ShortTypePrinter extends AbstractTypeVisitor8<String, Void> {
  // TODO reduce usage of DEFAULT in favor of context-specific "suppress my own package" printer
  public static final ShortTypePrinter DEFAULT = new ShortTypePrinter("");
  public static final ShortTypePrinter NO_PACKAGE = new ShortTypePrinter("*");

  private final String packageContext;

  private ShortTypePrinter(String packageContext) {
    this.packageContext = packageContext;
  }

  public String print(TypeMirror type) {
    return type.accept(new ShortTypePrinter(packageContext), null);
  }

  @Override
  public String visitIntersection(IntersectionType t, Void aVoid) {
    return t.getBounds().stream().map(this::print).collect(Collectors.joining(" & "));
  }

  @Override
  public String visitUnion(UnionType t, Void aVoid) {
    return t.getAlternatives().stream().map(this::print).collect(Collectors.joining(" | "));
  }

  @Override
  public String visitPrimitive(PrimitiveType t, Void aVoid) {
    return t.toString();
  }

  @Override
  public String visitNull(NullType t, Void aVoid) {
    return t.toString();
  }

  @Override
  public String visitArray(ArrayType t, Void aVoid) {
    return print(t.getComponentType()) + "[]";
  }

  @Override
  public String visitDeclared(DeclaredType t, Void aVoid) {
    String result = t.asElement().toString();

    if (!t.getTypeArguments().isEmpty()) {
      String params =
          t.getTypeArguments().stream().map(this::print).collect(Collectors.joining(", "));

      result += "<" + params + ">";
    }

    if (packageContext.equals("*")) return result.substring(result.lastIndexOf('.') + 1);
    else if (result.startsWith("java.lang")) return result.substring("java.lang.".length());
    else if (result.startsWith("java.util")) return result.substring("java.util.".length());
    else if (result.startsWith(packageContext)) return result.substring(packageContext.length());
    else return result;
  }

  @Override
  public String visitError(ErrorType t, Void aVoid) {
    return "_";
  }

  @Override
  public String visitTypeVariable(TypeVariable t, Void aVoid) {
    String result = t.asElement().toString();
    // TypeMirror upper = t.getUpperBound();
    // NOTE this can create infinite recursion
    // if (!upper.toString().equals("java.lang.Object"))
    //     result += " extends " + print(upper);

    return result;
  }

  @Override
  public String visitWildcard(WildcardType t, Void aVoid) {
    String result = "?";

    if (t.getSuperBound() != null) result += " super " + print(t.getSuperBound());

    if (t.getExtendsBound() != null) result += " extends " + print(t.getExtendsBound());

    return result;
  }

  @Override
  public String visitExecutable(ExecutableType t, Void aVoid) {
    return t.toString();
  }

  @Override
  public String visitNoType(NoType t, Void aVoid) {
    return t.toString();
  }

  public static boolean missingParamNames(ExecutableElement e) {
    return e.getParameters().stream()
        .allMatch(p -> p.getSimpleName().toString().matches("arg\\d+"));
  }

  private String printArguments(ExecutableElement e) {
    StringJoiner result = new StringJoiner(", ");
    boolean missingParamNames = missingParamNames(e);
    for (VariableElement p : e.getParameters()) {
      StringBuilder s = new StringBuilder();
      s.append(print(p.asType()));
      if (!missingParamNames) {
        s.append(" ").append(p.getSimpleName());
      }
      result.add(s);
    }
    return result.toString();
  }

  String printMethod(ExecutableElement m) {
    if (m.getSimpleName().contentEquals("<init>")) {
      return m.getEnclosingElement().getSimpleName() + "(" + printArguments(m) + ")";
    } else {
      StringBuilder result = new StringBuilder();
      // static void foo
      if (m.getModifiers().contains(Modifier.STATIC)) result.append("static ");
      result.append(print(m.getReturnType())).append(" ");
      result.append(m.getSimpleName());
      // (int arg, String other)
      result.append("(").append(printArguments(m)).append(")");
      // throws Foo, Bar
      if (!m.getThrownTypes().isEmpty()) {
        result.append(" throws ");
        StringJoiner types = new StringJoiner(", ");
        for (TypeMirror t : m.getThrownTypes()) {
          types.add(print(t));
        }
        result.append(types);
      }
      return result.toString();
    }
  }
}
