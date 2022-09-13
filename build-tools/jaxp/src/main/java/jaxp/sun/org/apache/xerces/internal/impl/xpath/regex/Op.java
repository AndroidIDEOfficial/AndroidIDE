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
/*
 * Copyright 1999-2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.impl.xpath.regex;

import java.util.Vector;

/**
 * @xerces.internal
 *
 */
class Op {
    static final int DOT = 0;
    static final int CHAR = 1;                  // Single character
    static final int RANGE = 3;                 // [a-zA-Z]
    static final int NRANGE = 4;                // [^a-zA-Z]
    static final int ANCHOR = 5;                // ^ $ ...
    static final int STRING = 6;                // literal String
    static final int CLOSURE = 7;               // X*
    static final int NONGREEDYCLOSURE = 8;      // X*?
    static final int QUESTION = 9;              // X?
    static final int NONGREEDYQUESTION = 10;    // X??
    static final int UNION = 11;                // X|Y
    static final int CAPTURE = 15;              // ( and )
    static final int BACKREFERENCE = 16;        // \1 \2 ...
    static final int LOOKAHEAD = 20;            // (?=...)
    static final int NEGATIVELOOKAHEAD = 21;    // (?!...)
    static final int LOOKBEHIND = 22;           // (?<=...)
    static final int NEGATIVELOOKBEHIND = 23;   // (?<!...)
    static final int INDEPENDENT = 24;          // (?>...)
    static final int MODIFIER = 25;             // (?ims-ims:...)
    static final int CONDITION = 26;            // (?(..)yes|no)

    static int nofinstances = 0;
    static final boolean COUNT = false;

    static Op createDot() {
        if (Op.COUNT)  Op.nofinstances ++;
        return new Op(Op.DOT);
    }
    static CharOp createChar(int data) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new CharOp(Op.CHAR, data);
    }
    static CharOp createAnchor(int data) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new CharOp(Op.ANCHOR, data);
    }
    static CharOp createCapture(int number, Op next) {
        if (Op.COUNT)  Op.nofinstances ++;
        CharOp op = new CharOp(Op.CAPTURE, number);
        op.next = next;
        return op;
    }
    static UnionOp createUnion(int size) {
        if (Op.COUNT)  Op.nofinstances ++;
        //System.err.println("Creates UnionOp");
        return new UnionOp(Op.UNION, size);
    }
    static ChildOp createClosure(int id) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new ModifierOp(Op.CLOSURE, id, -1);
    }
    static ChildOp createNonGreedyClosure() {
        if (Op.COUNT)  Op.nofinstances ++;
        return new ChildOp(Op.NONGREEDYCLOSURE);
    }
    static ChildOp createQuestion(boolean nongreedy) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new ChildOp(nongreedy ? Op.NONGREEDYQUESTION : Op.QUESTION);
    }
    static RangeOp createRange(Token tok) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new RangeOp(Op.RANGE, tok);
    }
    static ChildOp createLook(int type, Op next, Op branch) {
        if (Op.COUNT)  Op.nofinstances ++;
        ChildOp op = new ChildOp(type);
        op.setChild(branch);
        op.next = next;
        return op;
    }
    static CharOp createBackReference(int refno) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new CharOp(Op.BACKREFERENCE, refno);
    }
    static StringOp createString(String literal) {
        if (Op.COUNT)  Op.nofinstances ++;
        return new StringOp(Op.STRING, literal);
    }
    static ChildOp createIndependent(Op next, Op branch) {
        if (Op.COUNT)  Op.nofinstances ++;
        ChildOp op = new ChildOp(Op.INDEPENDENT);
        op.setChild(branch);
        op.next = next;
        return op;
    }
    static ModifierOp createModifier(Op next, Op branch, int add, int mask) {
        if (Op.COUNT)  Op.nofinstances ++;
        ModifierOp op = new ModifierOp(Op.MODIFIER, add, mask);
        op.setChild(branch);
        op.next = next;
        return op;
    }
    static ConditionOp createCondition(Op next, int ref, Op conditionflow, Op yesflow, Op noflow) {
        if (Op.COUNT)  Op.nofinstances ++;
        ConditionOp op = new ConditionOp(Op.CONDITION, ref, conditionflow, yesflow, noflow);
        op.next = next;
        return op;
    }

    int type;
    Op next = null;

    protected Op(int type) {
        this.type = type;
    }

    int size() {                                // for UNION
        return 0;
    }
    Op elementAt(int index) {                   // for UNIoN
        throw new RuntimeException("Internal Error: type="+this.type);
    }
    Op getChild() {                             // for CLOSURE, QUESTION
        throw new RuntimeException("Internal Error: type="+this.type);
    }
                                                // ModifierOp
    int getData() {                             // CharOp  for CHAR, BACKREFERENCE, CAPTURE, ANCHOR,
        throw new RuntimeException("Internal Error: type="+this.type);
    }
    int getData2() {                            // ModifierOp
        throw new RuntimeException("Internal Error: type="+this.type);
    }
    RangeToken getToken() {                     // RANGE, NRANGE
        throw new RuntimeException("Internal Error: type="+this.type);
    }
    String getString() {                        // STRING
        throw new RuntimeException("Internal Error: type="+this.type);
    }

    // ================================================================
    static class CharOp extends Op {
        int charData;
        CharOp(int type, int data) {
            super(type);
            this.charData = data;
        }
        int getData() {
            return this.charData;
        }
    }

    // ================================================================
    static class UnionOp extends Op {
        Vector branches;
        UnionOp(int type, int size) {
            super(type);
            this.branches = new Vector(size);
        }
        void addElement(Op op) {
            this.branches.addElement(op);
        }
        int size() {
            return this.branches.size();
        }
        Op elementAt(int index) {
            return (Op)this.branches.elementAt(index);
        }
    }

    // ================================================================
    static class ChildOp extends Op {
        Op child;
        ChildOp(int type) {
            super(type);
        }
        void setChild(Op child) {
            this.child = child;
        }
        Op getChild() {
            return this.child;
        }
    }
    // ================================================================
    static class ModifierOp extends ChildOp {
        int v1;
        int v2;
        ModifierOp(int type, int v1, int v2) {
            super(type);
            this.v1 = v1;
            this.v2 = v2;
        }
        int getData() {
            return this.v1;
        }
        int getData2() {
            return this.v2;
        }
    }
    // ================================================================
    static class RangeOp extends Op {
        Token tok;
        RangeOp(int type, Token tok) {
            super(type);
            this.tok = tok;
        }
        RangeToken getToken() {
            return (RangeToken)this.tok;
        }
    }
    // ================================================================
    static class StringOp extends Op {
        String string;
        StringOp(int type, String literal) {
            super(type);
            this.string = literal;
        }
        String getString() {
            return this.string;
        }
    }
    // ================================================================
    static class ConditionOp extends Op {
        int refNumber;
        Op condition;
        Op yes;
        Op no;
        ConditionOp(int type, int refno, Op conditionflow, Op yesflow, Op noflow) {
            super(type);
            this.refNumber = refno;
            this.condition = conditionflow;
            this.yes = yesflow;
            this.no = noflow;
        }
    }
}
