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
package jaxp.sun.org.apache.bcel.internal.classfile;

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
import  java.io.*;

/**
 * This class is derived from <em>Attribute</em> and represents a reference
 * to a <href="http://wwwipd.ira.uka.de/~pizza/gj/">GJ</a> attribute.
 *
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 * @see     Attribute
 */
public final class Signature extends Attribute {
  private int signature_index;

  /**
   * Initialize from another object. Note that both objects use the same
   * references (shallow copy). Use clone() for a physical copy.
   */
  public Signature(Signature c) {
    this(c.getNameIndex(), c.getLength(), c.getSignatureIndex(), c.getConstantPool());
  }

  /**
   * Construct object from file stream.
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param file Input stream
   * @param constant_pool Array of constants
   * @throws IOException
   */
  Signature(int name_index, int length, DataInputStream file,
           ConstantPool constant_pool) throws IOException
  {
    this(name_index, length, file.readUnsignedShort(), constant_pool);
  }

  /**
   * @param name_index Index in constant pool to CONSTANT_Utf8
   * @param length Content length in bytes
   * @param constant_pool Array of constants
   * @param Signature_index Index in constant pool to CONSTANT_Utf8
   */
  public Signature(int name_index, int length, int signature_index,
                  ConstantPool constant_pool)
  {
    super(Constants.ATTR_SIGNATURE, name_index, length, constant_pool);
    this.signature_index = signature_index;
  }

  /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
   public void accept(Visitor v) {
     System.err.println("Visiting non-standard Signature object");
     v.visitSignature(this);
   }

  /**
   * Dump source file attribute to file stream in binary format.
   *
   * @param file Output file stream
   * @throws IOException
   */
  public final void dump(DataOutputStream file) throws IOException
  {
    super.dump(file);
    file.writeShort(signature_index);
  }

  /**
   * @return Index in constant pool of source file name.
   */
  public final int getSignatureIndex() { return signature_index; }

  /**
   * @param Signature_index.
   */
  public final void setSignatureIndex(int signature_index) {
    this.signature_index = signature_index;
  }

  /**
   * @return GJ signature.
   */
  public final String getSignature() {
    ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(signature_index,
                                                             Constants.CONSTANT_Utf8);
    return c.getBytes();
  }

  /**
   * Extends ByteArrayInputStream to make 'unreading' chars possible.
   */
  private static final class MyByteArrayInputStream extends ByteArrayInputStream {
    MyByteArrayInputStream(String data) { super(data.getBytes()); }
    final int  mark()                   { return pos; }
    final String getData()              { return new String(buf); }
    final void reset(int p)             { pos = p; }
    final void unread()                 { if(pos > 0) pos--; }
  }

  private static boolean identStart(int ch) {
    return ch == 'T' || ch == 'L';
  }

  private static boolean identPart(int ch) {
    return ch == '/' || ch == ';';
  }

  private static final void matchIdent(MyByteArrayInputStream in, StringBuffer buf) {
    int ch;

    if((ch = in.read()) == -1)
      throw new RuntimeException("Illegal signature: " + in.getData() +
                                 " no ident, reaching EOF");

    //System.out.println("return from ident:" + (char)ch);

    if(!identStart(ch)) {
      StringBuffer buf2 = new StringBuffer();

      int count = 1;
      while(Character.isJavaIdentifierPart((char)ch)) {
        buf2.append((char)ch);
        count++;
        ch = in.read();
      }

      if(ch == ':') { // Ok, formal parameter
        in.skip("Ljava/lang/Object".length());
        buf.append(buf2);

        ch = in.read();
        in.unread();
        //System.out.println("so far:" + buf2 + ":next:" +(char)ch);
      } else {
        for(int i=0; i < count; i++)
          in.unread();
      }

      return;
    }

    StringBuffer buf2 = new StringBuffer();
    ch = in.read();

    do {
      buf2.append((char)ch);
      ch = in.read();
      //System.out.println("within ident:"+ (char)ch);

    } while((ch != -1) && (Character.isJavaIdentifierPart((char)ch) || (ch == '/')));

    buf.append(buf2.toString().replace('/', '.'));

    //System.out.println("regular return ident:"+ (char)ch + ":" + buf2);

    if(ch != -1)
      in.unread();
  }

  private static final void matchGJIdent(MyByteArrayInputStream in,
                                         StringBuffer buf)
  {
    int ch;

    matchIdent(in, buf);

    ch = in.read();
    if((ch == '<') || ch == '(') { // Parameterized or method
      //System.out.println("Enter <");
      buf.append((char)ch);
      matchGJIdent(in, buf);

      while(((ch = in.read()) != '>') && (ch != ')')) { // List of parameters
        if(ch == -1)
          throw new RuntimeException("Illegal signature: " + in.getData() +
                                     " reaching EOF");

        //System.out.println("Still no >");
        buf.append(", ");
        in.unread();
        matchGJIdent(in, buf); // Recursive call
      }

      //System.out.println("Exit >");

      buf.append((char)ch);
    } else
      in.unread();

    ch = in.read();
    if(identStart(ch)) {
      in.unread();
      matchGJIdent(in, buf);
    } else if(ch == ')') {
      in.unread();
      return;
    } else if(ch != ';')
      throw new RuntimeException("Illegal signature: " + in.getData() + " read " +
                                 (char)ch);
  }

  public static String translate(String s) {
    //System.out.println("Sig:" + s);
    StringBuffer buf = new StringBuffer();

    matchGJIdent(new MyByteArrayInputStream(s), buf);

    return buf.toString();
  }

  public static final boolean isFormalParameterList(String s) {
    return s.startsWith("<") && (s.indexOf(':') > 0);
  }

  public static final boolean isActualParameterList(String s) {
    return s.startsWith("L") && s.endsWith(">;");
  }

  /**
   * @return String representation
   */
  public final String toString() {
    String s = getSignature();

    return "Signature(" + s + ")";
  }

  /**
   * @return deep copy of this attribute
   */
  public Attribute copy(ConstantPool constant_pool) {
    return (Signature)clone();
  }
}
