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
package jaxp.sun.org.apache.bcel.internal.util;

import jaxp.sun.org.apache.bcel.internal.generic.*;
import jaxp.sun.org.apache.bcel.internal.classfile.Utility;
import jaxp.sun.org.apache.bcel.internal.Constants;
import jaxp.sun.org.apache.bcel.internal.generic.AllocationInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.ArrayInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.BranchInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.CHECKCAST;
import jaxp.sun.org.apache.bcel.internal.generic.CPInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.CodeExceptionGen;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPushInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.EmptyVisitor;
import jaxp.sun.org.apache.bcel.internal.generic.FieldInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.IINC;
import jaxp.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionConstants;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InvokeInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.LDC;
import jaxp.sun.org.apache.bcel.internal.generic.LDC2_W;
import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
import jaxp.sun.org.apache.bcel.internal.generic.MethodGen;
import jaxp.sun.org.apache.bcel.internal.generic.NEWARRAY;
import jaxp.sun.org.apache.bcel.internal.generic.ObjectType;
import jaxp.sun.org.apache.bcel.internal.generic.RET;
import jaxp.sun.org.apache.bcel.internal.generic.ReturnInstruction;
import jaxp.sun.org.apache.bcel.internal.generic.Select;
import jaxp.sun.org.apache.bcel.internal.generic.Type;

import java.io.PrintWriter;
import java.util.*;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
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

/**
 * Factory creates il.append() statements, and sets instruction targets.
 * A helper class for BCELifier.
 *
 * @see BCELifier
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
class BCELFactory extends EmptyVisitor {
  private MethodGen _mg;
  private PrintWriter     _out;
  private ConstantPoolGen _cp;

  BCELFactory(MethodGen mg, PrintWriter out) {
    _mg  = mg;
    _cp  = mg.getConstantPool();
    _out = out;
  }

  private HashMap branch_map = new HashMap(); // Map<Instruction, InstructionHandle>

  public void start() {
    if(!_mg.isAbstract() && !_mg.isNative()) {
      for(InstructionHandle ih = _mg.getInstructionList().getStart();
          ih != null; ih = ih.getNext()) {
        Instruction i = ih.getInstruction();

        if(i instanceof BranchInstruction) {
          branch_map.put(i, ih); // memorize container
        }

        if(ih.hasTargeters()) {
          if(i instanceof BranchInstruction) {
            _out.println("    InstructionHandle ih_" + ih.getPosition() + ";");
          } else {
            _out.print("    InstructionHandle ih_" + ih.getPosition() + " = ");
          }
        } else {
          _out.print("    ");
        }

        if(!visitInstruction(i))
          i.accept(this);
      }

      updateBranchTargets();
      updateExceptionHandlers();
    }
  }

  private boolean visitInstruction(Instruction i) {
    short opcode = i.getOpcode();

    if((InstructionConstants.INSTRUCTIONS[opcode] != null) &&
       !(i instanceof ConstantPushInstruction) &&
       !(i instanceof ReturnInstruction)) { // Handled below
      _out.println("il.append(InstructionConstants." +
                   i.getName().toUpperCase() + ");");
      return true;
    }

    return false;
  }

  public void visitLocalVariableInstruction(LocalVariableInstruction i) {
    short  opcode = i.getOpcode();
    Type type   = i.getType(_cp);

    if(opcode == Constants.IINC) {
      _out.println("il.append(new IINC(" + i.getIndex() + ", " +
                   ((IINC)i).getIncrement() + "));");
    } else {
      String kind   = (opcode < Constants.ISTORE)? "Load" : "Store";
      _out.println("il.append(_factory.create" + kind + "(" +
                   BCELifier.printType(type) + ", " +
                   i.getIndex() + "));");
    }
  }

  public void visitArrayInstruction(ArrayInstruction i) {
    short  opcode = i.getOpcode();
    Type   type   = i.getType(_cp);
    String kind   = (opcode < Constants.IASTORE)? "Load" : "Store";

    _out.println("il.append(_factory.createArray" + kind + "(" +
                 BCELifier.printType(type) + "));");
  }

  public void visitFieldInstruction(FieldInstruction i) {
    short  opcode = i.getOpcode();

    String class_name = i.getClassName(_cp);
    String field_name = i.getFieldName(_cp);
    Type   type       = i.getFieldType(_cp);

    _out.println("il.append(_factory.createFieldAccess(\"" +
                 class_name + "\", \"" + field_name + "\", " +
                 BCELifier.printType(type) + ", " +
                 "Constants." + Constants.OPCODE_NAMES[opcode].toUpperCase() +
                 "));");
  }

  public void visitInvokeInstruction(InvokeInstruction i) {
    short  opcode      = i.getOpcode();
    String class_name  = i.getClassName(_cp);
    String method_name = i.getMethodName(_cp);
    Type   type        = i.getReturnType(_cp);
    Type[] arg_types   = i.getArgumentTypes(_cp);

    _out.println("il.append(_factory.createInvoke(\"" +
                 class_name + "\", \"" + method_name + "\", " +
                 BCELifier.printType(type) + ", " +
                 BCELifier.printArgumentTypes(arg_types) + ", " +
                 "Constants." + Constants.OPCODE_NAMES[opcode].toUpperCase() +
                 "));");
  }

  public void visitAllocationInstruction(AllocationInstruction i) {
    Type type;

    if(i instanceof CPInstruction) {
      type = ((CPInstruction)i).getType(_cp);
    } else {
      type = ((NEWARRAY)i).getType();
    }

    short opcode = ((Instruction)i).getOpcode();
    int   dim    = 1;

    switch(opcode) {
    case Constants.NEW:
      _out.println("il.append(_factory.createNew(\"" +
                   ((ObjectType)type).getClassName() + "\"));");
      break;

    case Constants.MULTIANEWARRAY:
      dim = ((MULTIANEWARRAY)i).getDimensions();

    case Constants.ANEWARRAY:
    case Constants.NEWARRAY:
      _out.println("il.append(_factory.createNewArray(" +
                   BCELifier.printType(type) + ", (short) " + dim + "));");
      break;

    default:
      throw new RuntimeException("Oops: " + opcode);
    }
  }

  private void createConstant(Object value) {
    String embed = value.toString();

    if(value instanceof String)
      embed = '"' + Utility.convertString(value.toString()) + '"';
    else if(value instanceof Character)
      embed = "(char)0x" + Integer.toHexString(((Character)value).charValue());

    _out.println("il.append(new PUSH(_cp, " + embed + "));");
  }

  public void visitLDC(LDC i) {
    createConstant(i.getValue(_cp));
  }

  public void visitLDC2_W(LDC2_W i) {
    createConstant(i.getValue(_cp));
  }

  public void visitConstantPushInstruction(ConstantPushInstruction i) {
    createConstant(i.getValue());
  }

  public void visitINSTANCEOF(INSTANCEOF i) {
    Type type = i.getType(_cp);

    _out.println("il.append(new INSTANCEOF(_cp.addClass(" +
                 BCELifier.printType(type) + ")));");
  }

  public void visitCHECKCAST(CHECKCAST i) {
    Type type = i.getType(_cp);

    _out.println("il.append(_factory.createCheckCast(" +
                 BCELifier.printType(type) + "));");
  }

  public void visitReturnInstruction(ReturnInstruction i) {
    Type type = i.getType(_cp);

    _out.println("il.append(_factory.createReturn(" +
                 BCELifier.printType(type) + "));");
  }

  // Memorize BranchInstructions that need an update
  private ArrayList branches = new ArrayList();

  public void visitBranchInstruction(BranchInstruction bi) {
    BranchHandle bh   = (BranchHandle)branch_map.get(bi);
    int          pos  = bh.getPosition();
    String       name = bi.getName() + "_" + pos;

    if(bi instanceof Select) {
      Select s = (Select)bi;
      branches.add(bi);

      StringBuffer args   = new StringBuffer("new int[] { ");
      int[]        matchs = s.getMatchs();

      for(int i=0; i < matchs.length; i++) {
        args.append(matchs[i]);

        if(i < matchs.length - 1)
          args.append(", ");
      }

      args.append(" }");

      _out.print("    Select " + name + " = new " +
                 bi.getName().toUpperCase() + "(" + args +
                 ", new InstructionHandle[] { ");

      for(int i=0; i < matchs.length; i++) {
        _out.print("null");

        if(i < matchs.length - 1)
          _out.print(", ");
      }

      _out.println(");");
    } else {
      int    t_pos  = bh.getTarget().getPosition();
      String target;

      if(pos > t_pos) {
        target = "ih_" + t_pos;
      } else {
        branches.add(bi);
        target = "null";
      }

      _out.println("    BranchInstruction " + name +
                   " = _factory.createBranchInstruction(" +
                   "Constants." + bi.getName().toUpperCase() + ", " +
                   target + ");");
    }

    if(bh.hasTargeters())
      _out.println("    ih_" + pos + " = il.append(" + name + ");");
    else
      _out.println("    il.append(" + name + ");");
  }

  public void visitRET(RET i) {
    _out.println("il.append(new RET(" + i.getIndex() + ")));");
  }

  private void updateBranchTargets() {
    for(Iterator i = branches.iterator(); i.hasNext(); ) {
      BranchInstruction bi    = (BranchInstruction)i.next();
      BranchHandle      bh    = (BranchHandle)branch_map.get(bi);
      int               pos   = bh.getPosition();
      String            name  = bi.getName() + "_" + pos;
      int               t_pos = bh.getTarget().getPosition();

      _out.println("    " + name + ".setTarget(ih_" + t_pos + ");");

      if(bi instanceof Select) {
        InstructionHandle[] ihs = ((Select)bi).getTargets();

        for(int j = 0; j < ihs.length; j++) {
          t_pos = ihs[j].getPosition();

          _out.println("    " + name + ".setTarget(" + j +
                       ", ih_" + t_pos + ");");
        }
      }
    }
  }

  private void updateExceptionHandlers() {
    CodeExceptionGen[] handlers = _mg.getExceptionHandlers();

    for(int i=0; i < handlers.length; i++) {
      CodeExceptionGen h    = handlers[i];
      String           type = (h.getCatchType() == null)?
        "null" : BCELifier.printType(h.getCatchType());

      _out.println("    method.addExceptionHandler(" +
                   "ih_" + h.getStartPC().getPosition() + ", " +
                   "ih_" + h.getEndPC().getPosition() + ", " +
                   "ih_" + h.getHandlerPC().getPosition() + ", " +
                   type + ");");
    }
  }
}
