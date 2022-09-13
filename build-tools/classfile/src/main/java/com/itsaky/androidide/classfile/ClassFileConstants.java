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

package com.itsaky.androidide.classfile;

/**
 * Constants in a class file.
 *
 * @author Akash Yadav
 */
public class ClassFileConstants {

  /** Required magic number in the class files. */
  public static final int MAGIC = 0xCAFEBABE;

  /** Declared public; may be accessed from outside its package. */
  public static final int ACC_PUBLIC = 0x0001;

  /**
   * Declared private; accessible only within the defining class and other classes belonging to the
   * same nest
   */
  public static final int ACC_PRIVATE = 0x0002;

  /** Declared protected; may be accessed within subclasses. */
  public static final int ACC_PROTECTED = 0x0004;

  /** Declared static. */
  public static final int ACC_STATIC = 0x0008;
  
  /** Declared final; no subclasses allowed. */
  public static final int ACC_FINAL = 0x0010;
  
  /** Treat superclass methods specially when invoked by the invokespecial instruction. */
  public static final int ACC_SUPER = 0x0020;

  /** Declared volatile; cannot be cached */
  public static final int ACC_VOLATILE = 0x0040;

  /** Declared transient; not written or read by a persistent object manager. */
  public static final int ACC_TRANSIENT = 0x0080;

  /** Is an interface, not a class. */
  public static final int ACC_INTERFACE = 0x0200;

  /** Declared abstract; must not be instantiated. */
  public static final int ACC_ABSTRACT = 0x0400;

  /** Declared synthetic; Not present in the source code. */
  public static final int ACC_SYNTHETIC = 0x1000;

  /** Declared as an annotation type. */
  public static final int ACC_ANNOTATION = 0x2000;

  /** Declared as an enum type. */
  public static final int ACC_ENUM = 0x4000;

  /** Is a module, not a class or interface. */
  public static final int ACC_MODULE = 0x8000;

  public static final String ATTR_CONSTANT_VALUE = "ConstantValue";
  public static final String ATTR_CODE = "Code";
  public static final String ATTR_EXCEPTIONS = "Exceptions";
  public static final String ATTR_SOURCE_FILE = "SourceFile";
  public static final String ATTR_LINE_NUMBER_TABLE = "LineNumberTable";
  public static final String ATTR_LOCAL_VARIABLE_TABLE = "LocalVariableTable";
  public static final String ATTR_INNER_CLASSES = "InnerClasses";
  public static final String ATTR_SYNTHETIC = "Synthetic";
  public static final String ATTR_DEPRECATED = "Deprecated";
  public static final String ATTR_ENCLOSING_METHOD = "EnclosingMethod";
  public static final String ATTR_SIGNATURE = "Signature";
  public static final String ATTR_SOURCE_DEBUG_EXTENSION = "SourceDebugExtension";
  public static final String ATTR_LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable";
  public static final String ATTR_RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
  public static final String ATTR_RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations";
  public static final String ATTR_RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS =
      "RuntimeVisibleParameterAnnotations";
  public static final String ATTR_RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS =
      "RuntimeInvisibleParameterAnnotations";
  public static final String ATTR_ANNOTATION_DEFAULT = "AnnotationDefault";
  public static final String ATTR_STACK_MAP_TABLE = "StackMapTable";
  public static final String ATTR_BOOTSTRAP_METHODS = "BootstrapMethods";
  public static final String ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS =
      "RuntimeVisibleTypeAnnotations";
  public static final String ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS =
      "RuntimeInvisibleTypeAnnotations";
  public static final String ATTR_METHOD_PARAMETERS = "MethodParameters";
  public static final String ATTR_MODULE = "Module";
  public static final String ATTR_MODULE_PACKAGES = "ModulePackages";
  public static final String ATTR_MAIN_CLASS = "ModuleMainClass";
  public static final String ATTR_NEST_HOST = "NestHost";
  public static final String ATTR_NEST_MEMBERS = "NestMembers";
  public static final String ATTR_RECORD = "Record";
  public static final String ATTR_PERMITTED_SUBCLASSES = "PermittedSubclasses";
}
