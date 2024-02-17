/*
 * Copyright (c) 2015, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.itsaky.androidide.build.propparser.gen;

import static java.util.stream.Collectors.toList;

import com.itsaky.androidide.build.propparser.parser.Message;
import com.itsaky.androidide.build.propparser.parser.MessageFile;
import com.itsaky.androidide.build.propparser.parser.MessageInfo;
import com.itsaky.androidide.build.propparser.parser.MessageLine;
import com.itsaky.androidide.build.propparser.parser.MessageType;
import com.itsaky.androidide.build.propparser.parser.MessageType.CompoundType;
import com.itsaky.androidide.build.propparser.parser.MessageType.CustomType;
import com.itsaky.androidide.build.propparser.parser.MessageType.SimpleType;
import com.itsaky.androidide.build.propparser.parser.MessageType.UnionType;
import com.itsaky.androidide.build.propparser.parser.MessageType.Visitor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ClassGenerator {

  private static final String TEMPLATE_PROPS = "#\n" +
      "# Copyright (c) 2015, 2019, Oracle and/or its affiliates. All rights reserved.\n" +
      "# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.\n" +
      "#\n" +
      "# This code is free software; you can redistribute it and/or modify it\n" +
      "# under the terms of the GNU General Public License version 2 only, as\n" +
      "# published by the Free Software Foundation.  Oracle designates this\n" +
      "# particular file as subject to the \"Classpath\" exception as provided\n" +
      "# by Oracle in the LICENSE file that accompanied this code.\n" +
      "#\n" +
      "# This code is distributed in the hope that it will be useful, but WITHOUT\n" +
      "# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or\n" +
      "# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License\n" +
      "# version 2 for more details (a copy is included in the LICENSE file that\n" +
      "# accompanied this code).\n" +
      "#\n" +
      "# You should have received a copy of the GNU General Public License version\n" +
      "# 2 along with this work; if not, write to the Free Software Foundation,\n" +
      "# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.\n" +
      "#\n" +
      "# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA\n" +
      "# or visit www.oracle.com if you need additional information or have any\n" +
      "# questions.\n" +
      "#\n" +
      "\n" +
      "toplevel.decl=\\\n" +
      "    package {0};\\n\\\n" +
      "    \\n\\\n" +
      "    {1}\\n\\\n" +
      "    import openjdk.tools.javac.util.JCDiagnostic.Error;\\n\\\n" +
      "    import openjdk.tools.javac.util.JCDiagnostic.Warning;\\n\\\n" +
      "    import openjdk.tools.javac.util.JCDiagnostic.Note;\\n\\\n" +
      "    import openjdk.tools.javac.util.JCDiagnostic.Fragment;\\n\\\n" +
      "    \\n\\\n" +
      "    public class {2} '{'\\n\\\n" +
      "    {3}\\n\\\n" +
      "    '}'\\n\n" +
      "\n" +
      "import.decl=\\\n" +
      "    import {0};\n" +
      "\n" +
      "nested.decl =\\\n" +
      "    public static class {0} '{'\\n\\\n" +
      "    {1}\\n\\\n" +
      "    '}'\n" +
      "\n" +
      "factory.decl.method=\\\n" +
      "    /**\\n\\\n" +
      "    ' '* {5}\\n\\\n" +
      "    ' '*/\\n\\\n" +
      "    {0}public static {1} {2}({3}) '{'\\n\\\n" +
      "    {4}\\n\\\n" +
      "    '}'\n" +
      "\n" +
      "factory.decl.method.arg=\\\n" +
      "    arg{0}\n" +
      "\n" +
      "factory.decl.method.body=\\\n" +
      "    return new {0}({1}, {2}, {3});\n" +
      "\n" +
      "factory.decl.field=\\\n" +
      "    /**\\n\\\n" +
      "    ' '* {4}\\n\\\n" +
      "    ' '*/\\n\\\n" +
      "    public static final {0} {1} = new {0}({2}, {3});\n" +
      "\n" +
      "wildcards.extends=\\\n" +
      "    {0}<? extends {1}>\n" +
      "\n" +
      "suppress.warnings=\\\n" +
      "  @SuppressWarnings(\"rawtypes\")\\n";

  /**
   * Empty string - used to generate indentation padding.
   */
  private final static String INDENT_STRING = "                                                                   ";

  /**
   * Default indentation step.
   */
  private final static int INDENT_WIDTH = 4;

  /**
   * File-backed property file containing basic code stubs.
   */
  static Properties stubs;

  static {
    //init properties from file
    stubs = new Properties();
    try (final ByteArrayInputStream in = new ByteArrayInputStream(TEMPLATE_PROPS.getBytes())) {
      stubs.load(in);
    } catch (IOException ex) {
      throw new AssertionError(ex);
    }
  }

  //where
  Visitor<String, Void> stringVisitor = new Visitor<String, Void>() {
    @Override
    public String visitCustomType(CustomType t, Void aVoid) {
      String customType = t.typeString;
      return customType.substring(customType.lastIndexOf('.') + 1);
    }

    @Override
    public String visitSimpleType(SimpleType t, Void aVoid) {
      return t.clazz;
    }

    @Override
    public String visitCompoundType(CompoundType t, Void aVoid) {
      return StubKind.WILDCARDS_EXTENDS.format(t.kind.clazz.clazz,
          t.elemtype.accept(this, null));
    }

    @Override
    public String visitUnionType(UnionType t, Void aVoid) {
      throw new AssertionError("Union types should have been denormalized!");
    }
  };
  //where
  Visitor<Void, Set<String>> importVisitor = new Visitor<Void, Set<String>>() {
    @Override
    public Void visitCustomType(CustomType t, Set<String> imports) {
      imports.add(t.typeString);
      return null;
    }

    @Override
    public Void visitSimpleType(SimpleType t, Set<String> imports) {
      if (t.qualifier != null) {
        imports.add(t.qualifier + "." + t.clazz);
      }
      return null;
    }

    @Override
    public Void visitCompoundType(CompoundType t, Set<String> imports) {
      visitSimpleType(t.kind.clazz, imports);
      t.elemtype.accept(this, imports);
      return null;
    }

    @Override
    public Void visitUnionType(UnionType t, Set<String> imports) {
      Stream.of(t.choices).forEach(c -> c.accept(this, imports));
      return null;
    }
  };
  //where
  Visitor<List<MessageType>, Void> normalizeVisitor = new Visitor<List<MessageType>, Void>() {
    @Override
    public List<MessageType> visitCustomType(CustomType t, Void aVoid) {
      return Collections.singletonList(t);
    }

    @Override
    public List<MessageType> visitSimpleType(SimpleType t, Void aVoid) {
      return Collections.singletonList(t);
    }

    @Override
    public List<MessageType> visitCompoundType(CompoundType t, Void aVoid) {
      return t.elemtype.accept(this, null).stream()
          .map(nt -> new CompoundType(t.kind, nt))
          .collect(Collectors.toList());
    }

    @Override
    public List<MessageType> visitUnionType(UnionType t, Void aVoid) {
      return Stream.of(t.choices)
          .flatMap(t2 -> t2.accept(this, null).stream())
          .collect(Collectors.toList());
    }
  };

  /**
   * Form the name of the toplevel factory class.
   */
  public static String toplevelName(File file) {
    return Stream.of(file.getName().split("\\."))
        .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
        .collect(Collectors.joining(""));
  }

  /**
   * Main entry-point: generate a Java enum-like set of nested factory classes into given output
   * folder. The factories are populated as mandated by the comments in the input resource file.
   */
  public void generateFactory(MessageFile messageFile, File outDir) {
    Map<FactoryKind, List<Map.Entry<String, Message>>> groupedEntries =
        messageFile.messages.entrySet().stream()
            .collect(
                Collectors.groupingBy(
                    e -> FactoryKind.parseFrom(e.getKey().split("\\.")[1]),
                    TreeMap::new,
                    toList()));
    //generate nested classes
    List<String> nestedDecls = new ArrayList<>();
    Set<String> importedTypes = new TreeSet<>();
    for (Map.Entry<FactoryKind, List<Map.Entry<String, Message>>> entry : groupedEntries.entrySet()) {
        if (entry.getKey() == FactoryKind.OTHER) {
            continue;
        }
      //emit members
      String members = entry.getValue().stream()
          .flatMap(e -> generateFactoryMethodsAndFields(e.getKey(), e.getValue()).stream())
          .collect(Collectors.joining("\n\n"));
      //emit nested class
      String factoryDecl =
          StubKind.FACTORY_CLASS.format(entry.getKey().factoryClazz, indent(members, 1));
      nestedDecls.add(indent(factoryDecl, 1));
      //add imports
      entry.getValue().stream().forEach(e ->
          importedTypes.addAll(importedTypes(e.getValue().getMessageInfo().getTypes())));
    }
    StringBuilder clazzSb = new StringBuilder();
    clazzSb.append(licenseHeader());
    clazzSb.append("\n");
    clazzSb.append(StubKind.TOPLEVEL.format(
        packageName(messageFile.file),
        String.join("\n", generateImports(importedTypes)),
        toplevelName(messageFile.file),
        String.join("\n", nestedDecls)));
    try (FileWriter fw = new FileWriter(
        new File(outDir, toplevelName(messageFile.file) + ".java"))) {
      fw.append(clazzSb);
    } catch (Throwable ex) {
      throw new AssertionError(ex);
    }
  }

  /**
   * Indent a string to a given level.
   */
  String indent(String s, int level) {
    return Stream.of(s.split("\n"))
        .map(sub -> INDENT_STRING.substring(0, level * INDENT_WIDTH) + sub)
        .collect(Collectors.joining("\n"));
  }

  /**
   * license header.
   */
  String licenseHeader() {
    return """
        /*
         * Copyright (c) 1999, 2020, Oracle and/or its affiliates. All rights reserved.
         * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
         *
         * This code is free software; you can redistribute it and/or modify it
         * under the terms of the GNU General Public License version 2 only, as
         * published by the Free Software Foundation.  Oracle designates this
         * particular file as subject to the "Classpath" exception as provided
         * by Oracle in the LICENSE file that accompanied this code.
         *
         * This code is distributed in the hope that it will be useful, but WITHOUT
         * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
         * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
         * version 2 for more details (a copy is included in the LICENSE file that
         * accompanied this code).
         *
         * You should have received a copy of the GNU General Public License version
         * 2 along with this work; if not, write to the Free Software Foundation,
         * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
         *
         * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
         * or visit www.oracle.com if you need additional information or have any
         * questions.
         */
          
        """;
  }

  /**
   * Retrieve package part of given file object.
   */
  String packageName(File file) {
    String path = file.getAbsolutePath();
    int begin = path.indexOf("src/main/resources".replace('/', File.separatorChar))
        + "src/main/resources".length();
    String packagePath = path.substring(begin + 1, path.lastIndexOf(File.separatorChar));
    return packagePath.replace(File.separatorChar, '.');
  }

  /**
   * Generate a list of import declarations given a set of imported types.
   */
  List<String> generateImports(Set<String> importedTypes) {
    List<String> importDecls = new ArrayList<>();
    for (String it : importedTypes) {
      importDecls.add(StubKind.IMPORT.format(it));
    }
    return importDecls;
  }

  /**
   * Generate a list of factory methods/fields to be added to a given factory nested class.
   */
  List<String> generateFactoryMethodsAndFields(String key, Message msg) {
    MessageInfo msgInfo = msg.getMessageInfo();
    List<MessageLine> lines = msg.getLines(false);
    String javadoc = lines.stream()
        .filter(ml -> !ml.isInfo() && !ml.isEmptyOrComment())
        .map(ml -> ml.text)
        .collect(Collectors.joining("\n *"));
    String[] keyParts = key.split("\\.");
    FactoryKind k = FactoryKind.parseFrom(keyParts[1]);
    String factoryName = factoryName(key);
    if (msgInfo.getTypes().isEmpty()) {
      //generate field
      String factoryField = StubKind.FACTORY_FIELD.format(k.keyClazz, factoryName,
          "\"" + keyParts[0] + "\"",
          "\"" + Stream.of(keyParts).skip(2).collect(Collectors.joining(".")) + "\"",
          javadoc);
      return Collections.singletonList(factoryField);
    } else {
      //generate method
      List<String> factoryMethods = new ArrayList<>();
      for (List<MessageType> msgTypes : normalizeTypes(0, msgInfo.getTypes())) {
        List<String> types = generateTypes(msgTypes);
        List<String> argNames = argNames(types.size());
        String suppressionString = needsSuppressWarnings(msgTypes) ?
            StubKind.SUPPRESS_WARNINGS.format() : "";
        String factoryMethod = StubKind.FACTORY_METHOD_DECL.format(suppressionString, k.keyClazz,
            factoryName, argDecls(types, argNames).stream().collect(Collectors.joining(", ")),
            indent(StubKind.FACTORY_METHOD_BODY.format(k.keyClazz,
                "\"" + keyParts[0] + "\"",
                "\"" + Stream.of(keyParts).skip(2).collect(Collectors.joining(".")) + "\"",
                argNames.stream().collect(Collectors.joining(", "))), 1),
            javadoc);
        factoryMethods.add(factoryMethod);
      }
      return factoryMethods;
    }
  }

  /**
   * Form the name of a factory method/field given a resource key.
   */
  String factoryName(String key) {
    return Stream.of(key.split("[\\.-]"))
        .skip(2)
        .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
        .collect(Collectors.joining(""));
  }

  /**
   * Generate a formal parameter list given a list of types and names.
   */
  List<String> argDecls(List<String> types, List<String> args) {
    List<String> argNames = new ArrayList<>();
    for (int i = 0; i < types.size(); i++) {
      argNames.add(types.get(i) + " " + args.get(i));
    }
    return argNames;
  }

  /**
   * Generate a list of formal parameter names given a size.
   */
  List<String> argNames(int size) {
    List<String> argNames = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      argNames.add(StubKind.FACTORY_METHOD_ARG.format(i));
    }
    return argNames;
  }

  /**
   * Convert a (normalized) parsed type into a string-based representation of some Java type.
   */
  List<String> generateTypes(List<MessageType> msgTypes) {
    return msgTypes.stream().map(t -> t.accept(stringVisitor, null)).collect(Collectors.toList());
  }

  /**
   * See if any of the parsed types in the given list needs warning suppression.
   */
  boolean needsSuppressWarnings(List<MessageType> msgTypes) {
    return msgTypes.stream().anyMatch(t -> t.accept(suppressWarningsVisitor, null));
  }

  /**
   * Retrieve a list of types that need to be imported, so that the factory body can refer to the
   * types in the given list using simple names.
   */
  Set<String> importedTypes(List<MessageType> msgTypes) {
    Set<String> imports = new TreeSet<>();
    msgTypes.forEach(t -> t.accept(importVisitor, imports));
    return imports;
  }  //where
  Visitor<Boolean, Void> suppressWarningsVisitor = new Visitor<Boolean, Void>() {
    @Override
    public Boolean visitCustomType(CustomType t, Void aVoid) {
      //play safe
      return true;
    }

    @Override
    public Boolean visitSimpleType(SimpleType t, Void aVoid) {
      switch (t) {
        case LIST:
        case SET:
          return true;
        default:
          return false;
      }
    }

    @Override
    public Boolean visitCompoundType(CompoundType t, Void aVoid) {
      return t.elemtype.accept(this, null);
    }

    @Override
    public Boolean visitUnionType(UnionType t, Void aVoid) {
      return needsSuppressWarnings(Arrays.asList(t.choices));
    }
  };

  /**
   * Normalize parsed types in a comment line. If one or more types in the line contains
   * alternatives, this routine generate a list of 'overloaded' normalized signatures.
   */
  List<List<MessageType>> normalizeTypes(int idx, List<MessageType> msgTypes) {
      if (msgTypes.size() == idx) {
          return Collections.singletonList(Collections.emptyList());
      }
    MessageType head = msgTypes.get(idx);
    List<List<MessageType>> buf = new ArrayList<>();
    for (MessageType alternative : head.accept(normalizeVisitor, null)) {
      for (List<MessageType> rest : normalizeTypes(idx + 1, msgTypes)) {
        List<MessageType> temp = new ArrayList<>(rest);
        temp.add(0, alternative);
        buf.add(temp);
      }
    }
    return buf;
  }

  /**
   * Supported stubs in the property file.
   */
  enum StubKind {
    TOPLEVEL("toplevel.decl"),
    FACTORY_CLASS("nested.decl"),
    IMPORT("import.decl"),
    FACTORY_METHOD_DECL("factory.decl.method"),
    FACTORY_METHOD_ARG("factory.decl.method.arg"),
    FACTORY_METHOD_BODY("factory.decl.method.body"),
    FACTORY_FIELD("factory.decl.field"),
    WILDCARDS_EXTENDS("wildcards.extends"),
    SUPPRESS_WARNINGS("suppress.warnings");

    /**
     * stub key (as it appears in the property file)
     */
    String key;

    StubKind(String key) {
      this.key = key;
    }

    /**
     * Subst a list of arguments into a given stub.
     */
    String format(Object... args) {
      return MessageFormat.format((String) stubs.get(key), args);
    }
  }

  /**
   * Nested factory class kind. There are multiple sub-factories, one for each kind of commonly used
   * diagnostics (i.e. error, warnings, note, fragment). An additional category is defined for those
   * resource keys whose prefix doesn't match any predefined category.
   */
  enum FactoryKind {
    ERR("err", "Error", "Errors"),
    WARN("warn", "Warning", "Warnings"),
    NOTE("note", "Note", "Notes"),
    MISC("misc", "Fragment", "Fragments"),
    OTHER(null, null, null);

    /**
     * The prefix for this factory kind (i.e. 'err').
     */
    String prefix;

    /**
     * The type of the factory method/fields in this class.
     */
    String keyClazz;

    /**
     * The class name to be used for this factory.
     */
    String factoryClazz;

    FactoryKind(String prefix, String keyClazz, String factoryClazz) {
      this.prefix = prefix;
      this.keyClazz = keyClazz;
      this.factoryClazz = factoryClazz;
    }

    /**
     * Utility method for parsing a factory kind from a resource key prefix.
     */
    static FactoryKind parseFrom(String prefix) {
      for (FactoryKind k : FactoryKind.values()) {
        if (k.prefix == null || k.prefix.equals(prefix)) {
          return k;
        }
      }
      return null;
    }
  }


}
