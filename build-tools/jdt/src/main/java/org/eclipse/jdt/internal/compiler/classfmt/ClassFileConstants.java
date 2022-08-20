/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jesper S Moller - Contributions for
 *							Bug 405066 - [1.8][compiler][codegen] Implement code generation infrastructure for JSR335
 *							Bug 406982 - [1.8][compiler] Generation of MethodParameters Attribute in classfile
 *     Andy Clement (GoPivotal, Inc) aclement@gopivotal.com - Contributions for
 *                          Bug 405104 - [1.8][compiler][codegen] Implement support for serializeable lambdas
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.classfmt;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;

public final class ClassFileConstants {

  // jdk level used to denote future releases: optional behavior is not enabled for now, but may
  // become so. In order to enable these,
  // search for references to this constant, and change it to one of the official JDT constants
  // above.
  public static long JDK_DEFERRED = Long.MAX_VALUE;
  static int MAJOR_VERSION_1_1 = 45;
  static int MAJOR_VERSION_1_2 = 46;
  static int MAJOR_VERSION_1_3 = 47;
  static int MAJOR_VERSION_1_4 = 48;
  static int MAJOR_VERSION_1_5 = 49;
  static int MAJOR_VERSION_1_6 = 50;
  static int MAJOR_VERSION_1_7 = 51;
  static int MAJOR_VERSION_1_8 = 52;
  static int MAJOR_VERSION_9 = 53;
  static int MAJOR_VERSION_10 = 54;
  static int MAJOR_VERSION_11 = 55;
  static int MAJOR_VERSION_12 = 56;
  static int MAJOR_VERSION_13 = 57;
  static int MAJOR_VERSION_14 = 58;
  static int MAJOR_VERSION_15 = 59;
  static int MAJOR_VERSION_16 = 60;
  static int MAJOR_VERSION_0 = 44;
  static int MAJOR_LATEST_VERSION = MAJOR_VERSION_16;
  static int MINOR_VERSION_0 = 0;
  public static long JDK1_2 = ((long) MAJOR_VERSION_1_2 << 16) + MINOR_VERSION_0;
  public static long JDK1_3 = ((long) MAJOR_VERSION_1_3 << 16) + MINOR_VERSION_0;
  public static long JDK1_4 = ((long) MAJOR_VERSION_1_4 << 16) + MINOR_VERSION_0;
  public static long JDK1_5 = ((long) MAJOR_VERSION_1_5 << 16) + MINOR_VERSION_0;
  public static long JDK1_6 = ((long) MAJOR_VERSION_1_6 << 16) + MINOR_VERSION_0;
  public static long JDK1_7 = ((long) MAJOR_VERSION_1_7 << 16) + MINOR_VERSION_0;
  public static long JDK1_8 = ((long) MAJOR_VERSION_1_8 << 16) + MINOR_VERSION_0;
  public static long JDK9 = ((long) MAJOR_VERSION_9 << 16) + MINOR_VERSION_0;
  public static long JDK10 = ((long) MAJOR_VERSION_10 << 16) + MINOR_VERSION_0;
  public static long JDK11 = ((long) MAJOR_VERSION_11 << 16) + MINOR_VERSION_0;
  public static long JDK12 = ((long) MAJOR_VERSION_12 << 16) + MINOR_VERSION_0;
  public static long JDK13 = ((long) MAJOR_VERSION_13 << 16) + MINOR_VERSION_0;
  public static long JDK14 = ((long) MAJOR_VERSION_14 << 16) + MINOR_VERSION_0;
  public static long JDK15 = ((long) MAJOR_VERSION_15 << 16) + MINOR_VERSION_0;
  public static long JDK16 = ((long) MAJOR_VERSION_16 << 16) + MINOR_VERSION_0;
  static int MINOR_VERSION_1 = 1;
  static int MINOR_VERSION_2 = 2;
  static int MINOR_VERSION_3 = 3;
  // JDK 1.1 -> 9, comparable value allowing to check both major/minor version at once 1.4.1 >
  // 1.4.0
  // 16 unsigned bits for major, then 16 bits for minor
  public static long JDK1_1 = ((long) MAJOR_VERSION_1_1 << 16) + MINOR_VERSION_3; // 1.1. is 45.3
  static int MINOR_VERSION_4 = 4;
  static int MINOR_VERSION_PREVIEW = 0xffff;
  int AccDefault = 0;
  /*
   * Modifiers
   */
  int AccPublic = 0x0001;
  int AccPrivate = 0x0002;
  int AccProtected = 0x0004;
  int AccStatic = 0x0008;
  int AccFinal = 0x0010;
  int AccSynchronized = 0x0020;
  int AccVolatile = 0x0040;
  int AccBridge = 0x0040;
  int AccTransient = 0x0080;
  int AccVarargs = 0x0080;
  int AccNative = 0x0100;
  int AccInterface = 0x0200;
  int AccAbstract = 0x0400;
  int AccStrictfp = 0x0800;
  int AccSynthetic = 0x1000;
  int AccAnnotation = 0x2000;
  int AccEnum = 0x4000;
  int AccModule = 0x8000;
  /**
   * From classfile version 52 (compliance 1.8 up), meaning that a formal parameter is mandated by a
   * language specification, so all compilers for the language must emit it.
   */
  int AccMandated = 0x8000;
  /** Flags in module declaration - since java9 */
  int ACC_OPEN = 0x0020;

  int ACC_TRANSITIVE = 0x0020;
  int ACC_STATIC_PHASE = 0x0040;
  int ACC_SYNTHETIC = 0x1000;
  /** Other VM flags. */
  int AccSuper = 0x0020;
  /**
   * Extra flags for types and members attributes (not from the JVMS, should have been defined in
   * ExtraCompilerModifiers).
   */
  int AccAnnotationDefault =
      ASTNode.Bit18; // indicate presence of an attribute  "DefaultValue" (annotation method)

  int AccDeprecated = ASTNode.Bit21; // indicate presence of an attribute "Deprecated"
  int Utf8Tag = 1;
  int IntegerTag = 3;
  int FloatTag = 4;
  int LongTag = 5;
  int DoubleTag = 6;
  int ClassTag = 7;
  int StringTag = 8;
  int FieldRefTag = 9;
  int MethodRefTag = 10;
  int InterfaceMethodRefTag = 11;
  int NameAndTypeTag = 12;
  int MethodHandleTag = 15;
  int MethodTypeTag = 16;
  int DynamicTag = 17;
  int InvokeDynamicTag = 18;
  int ModuleTag = 19;
  int PackageTag = 20;
  int ConstantMethodRefFixedSize = 5;
  int ConstantClassFixedSize = 3;
  int ConstantDoubleFixedSize = 9;
  int ConstantFieldRefFixedSize = 5;
  int ConstantFloatFixedSize = 5;
  int ConstantIntegerFixedSize = 5;
  int ConstantInterfaceMethodRefFixedSize = 5;
  int ConstantLongFixedSize = 9;
  int ConstantStringFixedSize = 3;
  int ConstantUtf8FixedSize = 3;
  int ConstantNameAndTypeFixedSize = 5;
  int ConstantMethodHandleFixedSize = 4;
  int ConstantMethodTypeFixedSize = 3;
  int ConstantDynamicFixedSize = 5;
  int ConstantInvokeDynamicFixedSize = 5;
  int ConstantModuleFixedSize = 3;
  int ConstantPackageFixedSize = 3;
  // JVMS 4.4.8
  int MethodHandleRefKindGetField = 1;
  int MethodHandleRefKindGetStatic = 2;
  int MethodHandleRefKindPutField = 3;
  int MethodHandleRefKindPutStatic = 4;
  int MethodHandleRefKindInvokeVirtual = 5;
  int MethodHandleRefKindInvokeStatic = 6;
  int MethodHandleRefKindInvokeSpecial = 7;
  int MethodHandleRefKindNewInvokeSpecial = 8;
  int MethodHandleRefKindInvokeInterface = 9;
  /*
   * cldc1.1 is 45.3, but we modify it to be different from JDK1_1.
   * In the code gen, we will generate the same target value as JDK1_1
   */
  long CLDC_1_1 = ((long) MAJOR_VERSION_1_1 << 16) + MINOR_VERSION_4;
  int INT_ARRAY = 10;
  int BYTE_ARRAY = 8;
  int BOOLEAN_ARRAY = 4;
  int SHORT_ARRAY = 9;
  int CHAR_ARRAY = 5;
  int LONG_ARRAY = 11;
  int FLOAT_ARRAY = 6;
  int DOUBLE_ARRAY = 7;
  // Debug attributes
  int ATTR_SOURCE = 0x1; // SourceFileAttribute
  int ATTR_LINES = 0x2; // LineNumberAttribute
  int ATTR_VARS = 0x4; // LocalVariableTableAttribute
  int ATTR_STACK_MAP_TABLE = 0x8; // Stack map table attribute
  int ATTR_STACK_MAP = 0x10; // Stack map attribute: cldc
  int ATTR_TYPE_ANNOTATION = 0x20; // type annotation attribute (jsr 308)
  int ATTR_METHOD_PARAMETERS = 0x40; // method parameters attribute (jep 118)
  // See java.lang.invoke.LambdaMetafactory constants - option bitflags when calling
  // altMetaFactory()
  int FLAG_SERIALIZABLE = 0x01;
  int FLAG_MARKERS = 0x02;
  int FLAG_BRIDGES = 0x04;

  public static long getLatestJDKLevel() {
    return ((long) MAJOR_LATEST_VERSION << 16) + MINOR_VERSION_0;
  }

  /**
   * As we move away from declaring every compliance level explicitly (such as JDK11, JDK12 etc.),
   * this method can be used to compute the compliance level on the fly for a given Java major
   * version.
   *
   * @param major Java major version
   * @return the compliance level for the given Java version
   */
  public static long getComplianceLevelForJavaVersion(int major) {
    switch (major) {
      case 45: // MAJOR_VERSION_1_1
        return ((long) MAJOR_VERSION_1_1 << 16) + MINOR_VERSION_3;
      default:
        major = Math.min(major, MAJOR_LATEST_VERSION);
        return ((long) major << 16) + MINOR_VERSION_0;
    }
  }
}
