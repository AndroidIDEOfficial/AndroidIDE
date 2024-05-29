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
package com.itsaky.androidide.lsp.java.utils;

import static com.itsaky.androidide.projects.util.StringSearch.containsClass;
import static com.itsaky.androidide.projects.util.StringSearch.containsInterface;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;
import com.github.javaparser.printer.configuration.PrinterConfiguration;
import com.itsaky.androidide.lsp.java.visitors.PrettyPrintingVisitor;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.type.DeclaredType;
import jdkx.lang.model.type.NoType;
import jdkx.lang.model.type.TypeKind;
import jdkx.lang.model.type.TypeMirror;
import jdkx.lang.model.type.TypeVariable;
import openjdk.source.tree.IdentifierTree;
import openjdk.source.tree.ParameterizedTypeTree;
import openjdk.source.tree.PrimitiveTypeTree;
import openjdk.source.tree.Tree;
import openjdk.source.tree.TypeParameterTree;
import openjdk.source.tree.WildcardTree;
import openjdk.tools.javac.code.BoundKind;
import openjdk.tools.javac.tree.JCTree;

public class TypeUtils {

  // trees
  public static ClassOrInterfaceType toClassOrInterfaceType(Tree tree) {
    ClassOrInterfaceType type = new ClassOrInterfaceType();
    if (tree instanceof IdentifierTree) {
      type.setName(((IdentifierTree) tree).getName().toString());
    }
    if (tree instanceof ParameterizedTypeTree) {
      ParameterizedTypeTree parameterizedTypeTree = (ParameterizedTypeTree) tree;
      Type t = toType(parameterizedTypeTree.getType());

      NodeList<Type> typeArguments = new NodeList<>();
      for (Tree typeArgument : parameterizedTypeTree.getTypeArguments()) {
        Type typ = toType(typeArgument);
        typeArguments.add(typ);
      }
      if (t.isClassOrInterfaceType()) {
        type.setName(t.asClassOrInterfaceType().getName());
      }
      type.setTypeArguments(typeArguments);
    }
    return type;
  }

  public static Type toType(Tree tree) {
    Type type;
    if (tree instanceof PrimitiveTypeTree) {
      type = getPrimitiveType((PrimitiveTypeTree) tree);
    } else if (tree instanceof IdentifierTree) {
      type = toClassOrInterfaceType(tree);
    } else if (tree instanceof WildcardTree) {
      JCTree.JCWildcard wildcardTree = (JCTree.JCWildcard) tree;
      WildcardType wildcardType = new WildcardType();
      Tree bound = wildcardTree.getBound();
      Type boundType = toType(bound);
      if (wildcardTree.kind.kind == BoundKind.EXTENDS) {
        wildcardType.setExtendedType((ReferenceType) boundType);
      } else {
        wildcardType.setSuperType((ReferenceType) boundType);
      }
      type = wildcardType;
    } else if (tree instanceof ParameterizedTypeTree) {
      type = toClassOrInterfaceType(tree);
    } else if (tree instanceof TypeParameterTree) {
      TypeParameter typeParameter = new TypeParameter();
      typeParameter.setName(((TypeParameterTree) tree).getName().toString());
      typeParameter.setTypeBound(
          ((TypeParameterTree) tree)
              .getBounds().stream()
                  .map(TypeUtils::toClassOrInterfaceType)
                  .collect(NodeList.toNodeList()));
      type = typeParameter;
    } else {
      if (tree != null) {
        type = StaticJavaParser.parseType(tree.toString());
      } else {
        type = null;
      }
    }
    return type;
  }

  public static Type getPrimitiveType(PrimitiveTypeTree tree) {
    Type type;
    switch (tree.getPrimitiveTypeKind()) {
      case INT:
        type = PrimitiveType.intType();
        break;
      case BOOLEAN:
        type = PrimitiveType.booleanType();
        break;
      case LONG:
        type = PrimitiveType.longType();
        break;
      case SHORT:
        type = PrimitiveType.shortType();
        break;
      case CHAR:
        type = PrimitiveType.charType();
        break;
      case FLOAT:
        type = PrimitiveType.floatType();
        break;
      case VOID:
        type = new VoidType();
        break;
      default:
        type = new UnknownType();
    }
    return type;
  }

  public static Type toType(TypeMirror typeMirror) {
    if (typeMirror.getKind() == TypeKind.ARRAY) {
      return toArrayType((jdkx.lang.model.type.ArrayType) typeMirror);
    }
    if (typeMirror.getKind().isPrimitive()) {
      return toPrimitiveType((jdkx.lang.model.type.PrimitiveType) typeMirror);
    }
    if (typeMirror instanceof jdkx.lang.model.type.IntersectionType) {
      return toIntersectionType((jdkx.lang.model.type.IntersectionType) typeMirror);
    }
    if (typeMirror instanceof jdkx.lang.model.type.WildcardType) {
      return toWildcardType((jdkx.lang.model.type.WildcardType) typeMirror);
    }
    if (typeMirror instanceof jdkx.lang.model.type.DeclaredType) {
      return toClassOrInterfaceType((DeclaredType) typeMirror);
    }
    if (typeMirror instanceof jdkx.lang.model.type.TypeVariable) {
      return toType(((TypeVariable) typeMirror));
    }
    if (typeMirror instanceof NoType) {
      return new VoidType();
    }
    return null;
  }

  // type mirrors

  public static IntersectionType toIntersectionType(jdkx.lang.model.type.IntersectionType type) {
    NodeList<ReferenceType> collect =
        type.getBounds().stream()
            .map(TypeUtils::toType)
            .map(it -> ((ReferenceType) it))
            .collect(NodeList.toNodeList());
    return new IntersectionType(collect);
  }

  public static Type toType(TypeVariable typeVariable) {
    TypeParameter typeParameter = new TypeParameter();
    TypeMirror upperBound = typeVariable.getUpperBound();

    if (!typeVariable.equals(upperBound)) {
      Type type = toType(upperBound);
      if (type != null) {
        if (type.isIntersectionType()) {
          typeParameter.setTypeBound(
              type.asIntersectionType().getElements().stream()
                  .filter(Type::isClassOrInterfaceType)
                  .map(Type::asClassOrInterfaceType)
                  .collect(NodeList.toNodeList()));
        } else if (type.isClassOrInterfaceType()) {
          typeParameter.setTypeBound(NodeList.nodeList(type.asClassOrInterfaceType()));
        }
      }
    }
    typeParameter.setName(typeVariable.toString());
    return typeParameter;
  }

  public static WildcardType toWildcardType(jdkx.lang.model.type.WildcardType type) {
    WildcardType wildcardType = new WildcardType();
    if (type.getSuperBound() != null) {
      Type result = toType(type.getSuperBound());
      if (result instanceof ReferenceType) {
        wildcardType.setSuperType((ReferenceType) result);
      } else if (result instanceof WildcardType) {
        wildcardType = result.asWildcardType();
      }
    }

    if (type.getExtendsBound() != null) {
      wildcardType.setExtendedType((ReferenceType) toType(type.getExtendsBound()));
    }
    return wildcardType;
  }

  public static PrimitiveType toPrimitiveType(jdkx.lang.model.type.PrimitiveType type) {
    PrimitiveType.Primitive primitive = PrimitiveType.Primitive.valueOf(type.getKind().name());
    return new PrimitiveType(primitive);
  }

  public static ArrayType toArrayType(jdkx.lang.model.type.ArrayType type) {
    Type componentType = toType(type.getComponentType());
    return new ArrayType(componentType);
  }

  public static ClassOrInterfaceType toClassOrInterfaceType(DeclaredType type) {
    ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType();
    if (!type.getTypeArguments().isEmpty()) {
      classOrInterfaceType.setTypeArguments(
          type.getTypeArguments().stream()
              .map(TypeUtils::toType)
              .filter(Objects::nonNull)
              .collect(NodeList.toNodeList()));
    }
    if (!type.asElement().toString().isEmpty()) {
      classOrInterfaceType.setName(type.asElement().toString());
    }
    return classOrInterfaceType;
  }

  public static String getName(Type type, Predicate<String> needFqnDelegate) {
    PrinterConfiguration configuration = new DefaultPrinterConfiguration();
    PrettyPrintingVisitor visitor =
        new PrettyPrintingVisitor(configuration) {
          @Override
          public void visit(SimpleName n, Void arg) {
            printOrphanCommentsBeforeThisChildNode(n);
            printComment(n.getComment(), arg);

            String identifier = n.getIdentifier();
            if (needFqnDelegate.test(identifier)) {
              printer.print(identifier);
            } else {
              printer.print(JavaParserUtils.getSimpleName(identifier));
            }
          }
        };
    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(t -> visitor, configuration);
    return prettyPrinter.print(type);
  }

  public static String getSimpleName(Type type) {
    PrinterConfiguration configuration = new DefaultPrinterConfiguration();
    PrettyPrintingVisitor visitor = new PrettyPrintingVisitor(configuration);
    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(t -> visitor, configuration);
    return prettyPrinter.print(type);
  }

  public static boolean containsType(Path file, TypeElement el) {
    switch (el.getKind()) {
      case INTERFACE:
        return containsInterface(file, el.getSimpleName().toString());
      case CLASS:
        return containsClass(file, el.getSimpleName().toString());
      default:
        throw new RuntimeException("Don't know what to do with " + el.getKind());
    }
  }
}
