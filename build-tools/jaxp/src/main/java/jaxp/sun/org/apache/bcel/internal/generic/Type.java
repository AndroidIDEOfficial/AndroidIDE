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

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
package jaxp.sun.org.apache.bcel.internal.generic;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache BCEL" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache BCEL", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import jaxp.sun.org.apache.bcel.internal.Constants;
import jaxp.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import jaxp.sun.org.apache.bcel.internal.classfile.Utility;

import jaxp.sun.org.apache.bcel.internal.classfile.*;
import java.util.ArrayList;

/**
 * Abstract super class for all possible java types, namely basic types
 * such as int, object types like String and array types, e.g. int[]
 *
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public abstract class Type implements java.io.Serializable {
  protected byte   type;
  protected String signature; // signature for the type

  /** Predefined constants
   */
  public static final BasicType     VOID         = new BasicType(Constants.T_VOID);
  public static final BasicType     BOOLEAN      = new BasicType(Constants.T_BOOLEAN);
  public static final BasicType     INT          = new BasicType(Constants.T_INT);
  public static final BasicType     SHORT        = new BasicType(Constants.T_SHORT);
  public static final BasicType     BYTE         = new BasicType(Constants.T_BYTE);
  public static final BasicType     LONG         = new BasicType(Constants.T_LONG);
  public static final BasicType     DOUBLE       = new BasicType(Constants.T_DOUBLE);
  public static final BasicType     FLOAT        = new BasicType(Constants.T_FLOAT);
  public static final BasicType     CHAR         = new BasicType(Constants.T_CHAR);
  public static final ObjectType    OBJECT       = new ObjectType("java.lang.Object");
  public static final ObjectType    STRING       = new ObjectType("java.lang.String");
  public static final ObjectType    STRINGBUFFER = new ObjectType("java.lang.StringBuffer");
  public static final ObjectType    THROWABLE    = new ObjectType("java.lang.Throwable");
  public static final Type[]        NO_ARGS      = new Type[0];
  public static final ReferenceType NULL         = new ReferenceType(){};
  public static final Type          UNKNOWN      = new Type(Constants.T_UNKNOWN,
                                                            "<unknown object>"){};

  protected Type(byte t, String s) {
    type      = t;
    signature = s;
  }

  /**
   * @return signature for given type.
   */
  public String getSignature() { return signature; }

  /**
   * @return type as defined in Constants
   */
  public byte getType() { return type; }

  /**
   * @return stack size of this type (2 for long and double, 0 for void, 1 otherwise)
   */
  public int getSize() {
    switch(type) {
    case Constants.T_DOUBLE:
    case Constants.T_LONG: return 2;
    case Constants.T_VOID: return 0;
    default:     return 1;
    }
  }

  /**
   * @return Type string, e.g. `int[]'
   */
  public String toString() {
    return ((this.equals(Type.NULL) || (type >= Constants.T_UNKNOWN)))? signature :
      Utility.signatureToString(signature, false);
  }

  /**
   * Convert type to Java method signature, e.g. int[] f(java.lang.String x)
   * becomes (Ljava/lang/String;)[I
   *
   * @param return_type what the method returns
   * @param arg_types what are the argument types
   * @return method signature for given type(s).
   */
  public static String getMethodSignature(Type return_type, Type[] arg_types) {
    StringBuffer buf = new StringBuffer("(");
    int length = (arg_types == null)? 0 : arg_types.length;

    for(int i=0; i < length; i++)
      buf.append(arg_types[i].getSignature());

    buf.append(')');
    buf.append(return_type.getSignature());

    return buf.toString();
  }

  private static int consumed_chars=0; // Remember position in string, see getArgumentTypes

  /**
   * Convert signature to a Type object.
   * @param signature signature string such as Ljava/lang/String;
   * @return type object
   */
  public static final Type getType(String signature)
    throws StringIndexOutOfBoundsException
  {
    byte type = Utility.typeOfSignature(signature);

    if(type <= Constants.T_VOID) {
      consumed_chars = 1;
      return BasicType.getType(type);
    } else if(type == Constants.T_ARRAY) {
      int dim=0;
      do { // Count dimensions
        dim++;
      } while(signature.charAt(dim) == '[');

      // Recurse, but just once, if the signature is ok
      Type t = getType(signature.substring(dim));

      consumed_chars += dim; // update counter

      return new ArrayType(t, dim);
    } else { // type == T_REFERENCE
      int index = signature.indexOf(';'); // Look for closing `;'

      if(index < 0)
        throw new ClassFormatException("Invalid signature: " + signature);

      consumed_chars = index + 1; // "Lblabla;" `L' and `;' are removed

      return new ObjectType(signature.substring(1, index).replace('/', '.'));
    }
  }

  /**
   * Convert return value of a method (signature) to a Type object.
   *
   * @param signature signature string such as (Ljava/lang/String;)V
   * @return return type
   */
  public static Type getReturnType(String signature) {
    try {
      // Read return type after `)'
      int index = signature.lastIndexOf(')') + 1;
      return getType(signature.substring(index));
    } catch(StringIndexOutOfBoundsException e) { // Should never occur
      throw new ClassFormatException("Invalid method signature: " + signature);
    }
  }

  /**
   * Convert arguments of a method (signature) to an array of Type objects.
   * @param signature signature string such as (Ljava/lang/String;)V
   * @return array of argument types
   */
  public static Type[] getArgumentTypes(String signature) {
    ArrayList vec = new ArrayList();
    int       index;
    Type[]     types;

    try { // Read all declarations between for `(' and `)'
      if(signature.charAt(0) != '(')
        throw new ClassFormatException("Invalid method signature: " + signature);

      index = 1; // current string position

      while(signature.charAt(index) != ')') {
        vec.add(getType(signature.substring(index)));
        index += consumed_chars; // update position
      }
    } catch(StringIndexOutOfBoundsException e) { // Should never occur
      throw new ClassFormatException("Invalid method signature: " + signature);
    }

    types = new Type[vec.size()];
    vec.toArray(types);
    return types;
  }

  /** Convert runtime java.lang.Class to BCEL Type object.
   * @param cl Java class
   * @return corresponding Type object
   */
  public static Type getType(java.lang.Class cl) {
    if(cl == null) {
      throw new IllegalArgumentException("Class must not be null");
    }

    /* That's an amzingly easy case, because getName() returns
     * the signature. That's what we would have liked anyway.
     */
    if(cl.isArray()) {
      return getType(cl.getName());
    } else if(cl.isPrimitive()) {
      if(cl == Integer.TYPE) {
        return INT;
      } else if(cl == Void.TYPE) {
        return VOID;
      } else if(cl == Double.TYPE) {
        return DOUBLE;
      } else if(cl == Float.TYPE) {
        return FLOAT;
      } else if(cl == Boolean.TYPE) {
        return BOOLEAN;
      } else if(cl == Byte.TYPE) {
        return BYTE;
      } else if(cl == Short.TYPE) {
        return SHORT;
      } else if(cl == Byte.TYPE) {
        return BYTE;
      } else if(cl == Long.TYPE) {
        return LONG;
      } else if(cl == Character.TYPE) {
        return CHAR;
      } else {
        throw new IllegalStateException("Ooops, what primitive type is " + cl);
      }
    } else { // "Real" class
      return new ObjectType(cl.getName());
    }
  }

  public static String getSignature(java.lang.reflect.Method meth) {
    StringBuffer sb = new StringBuffer("(");
    Class[] params = meth.getParameterTypes(); // avoid clone

    for(int j = 0; j < params.length; j++) {
      sb.append(getType(params[j]).getSignature());
    }

    sb.append(")");
    sb.append(getType(meth.getReturnType()).getSignature());
    return sb.toString();
  }
}
