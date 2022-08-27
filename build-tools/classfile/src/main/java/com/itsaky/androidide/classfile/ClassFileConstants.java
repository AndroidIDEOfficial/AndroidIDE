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

  /** Declared final; no subclasses allowed. */
  public static final int ACC_FINAL = 0x0010;

  /** Treat superclass methods specially when invoked by the invokespecial instruction. */
  public static final int ACC_SUPER = 0x0020;

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
}
