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
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: Whitespace.java,v 1.5 2005/09/28 13:48:18 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.StringTokenizer;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ALOAD;
import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
import jaxp.sun.org.apache.bcel.internal.generic.ILOAD;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import jaxp.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/**
 * @author Morten Jorgensen
 */
final class Whitespace extends TopLevelElement {
    // Three possible actions for the translet:
    public static final int USE_PREDICATE  = 0;
    public static final int STRIP_SPACE    = 1;
    public static final int PRESERVE_SPACE = 2;

    // The 3 different categories of strip/preserve rules (order important)
    public static final int RULE_NONE      = 0;
    public static final int RULE_ELEMENT   = 1; // priority 0
    public static final int RULE_NAMESPACE = 2; // priority -1/4
    public static final int RULE_ALL       = 3; // priority -1/2

    private String _elementList;
    private int    _action;
    private int    _importPrecedence;

    /**
     * Auxillary class for encapsulating a single strip/preserve rule
     */
    private final static class WhitespaceRule {
        private final int _action;
        private String _namespace; // Should be replaced by NS type (int)
        private String _element;   // Should be replaced by node type (int)
        private int    _type;
        private int    _priority;

        /**
         * Strip/preserve rule constructor
         */
        public WhitespaceRule(int action, String element, int precedence) {
            // Determine the action (strip or preserve) for this rule
            _action = action;

            // Get the namespace and element name for this rule
            final int colon = element.lastIndexOf(':');
            if (colon >= 0) {
                _namespace = element.substring(0,colon);
                _element = element.substring(colon+1,element.length());
            }
            else {
                _namespace = EMPTYSTRING;
                _element = element;
            }

            // Determine the initial priority for this rule
            _priority = precedence << 2;

            // Get the strip/preserve type; either "NS:EL", "NS:*" or "*"
            if (_element.equals("*")) {
                if (_namespace == EMPTYSTRING) {
                    _type = RULE_ALL;       // Strip/preserve _all_ elements
                    _priority += 2;         // Lowest priority
                }
                else {
                    _type = RULE_NAMESPACE; // Strip/reserve elements within NS
                    _priority += 1;         // Medium priority
                }
            }
            else {
                _type = RULE_ELEMENT;       // Strip/preserve single element
            }
        }

        /**
         * For sorting rules depending on priority
         */
        public int compareTo(WhitespaceRule other) {
            return _priority < other._priority
                ? -1
                : _priority > other._priority ? 1 : 0;
        }

        public int getAction() { return _action; }
        public int getStrength() { return _type; }
        public int getPriority() { return _priority; }
        public String getElement() { return _element; }
        public String getNamespace() { return _namespace; }
    }

    /**
     * Parse the attributes of the xsl:strip/preserve-space element.
     * The element should have not contents (ignored if any).
     */
    public void parseContents(Parser parser) {
        // Determine if this is an xsl:strip- or preserve-space element
        _action = _qname.getLocalPart().endsWith("strip-space")
            ? STRIP_SPACE : PRESERVE_SPACE;

        // Determine the import precedence
        _importPrecedence = parser.getCurrentImportPrecedence();

        // Get the list of elements to strip/preserve
        _elementList = getAttribute("elements");
        if (_elementList == null || _elementList.length() == 0) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "elements");
            return;
        }

        final SymbolTable stable = parser.getSymbolTable();
        StringTokenizer list = new StringTokenizer(_elementList);
        StringBuffer elements = new StringBuffer(EMPTYSTRING);

        while (list.hasMoreElements()) {
            String token = list.nextToken();
            String prefix;
            String namespace;
            int col = token.indexOf(':');

            if (col != -1) {
                namespace = lookupNamespace(token.substring(0,col));
                if (namespace != null) {
                    elements.append(namespace).append(':').append(token.substring(col + 1));
                } else {
                    elements.append(token);
                }
            } else {
                elements.append(token);
            }

            if (list.hasMoreElements())
                elements.append(" ");
        }
        _elementList = elements.toString();
    }


    /**
     * De-tokenize the elements listed in the 'elements' attribute and
     * instanciate a set of strip/preserve rules.
     */
    public Vector getRules() {
        final Vector rules = new Vector();
        // Go through each element and instanciate strip/preserve-object
        final StringTokenizer list = new StringTokenizer(_elementList);
        while (list.hasMoreElements()) {
            rules.add(new WhitespaceRule(_action,
                                         list.nextToken(),
                                         _importPrecedence));
        }
        return rules;
    }


    /**
     * Scans through the rules vector and looks for a rule of higher
     * priority that contradicts the current rule.
     */
    private static WhitespaceRule findContradictingRule(Vector rules,
                                                        WhitespaceRule rule) {
        for (int i = 0; i < rules.size(); i++) {
            // Get the next rule in the prioritized list
            WhitespaceRule currentRule = (WhitespaceRule)rules.elementAt(i);
            // We only consider rules with higher priority
            if (currentRule == rule) {
                return null;
            }

            /*
             * See if there is a contradicting rule with higher priority.
             * If the rules has the same action then this rule is redundant,
             * if they have different action then this rule will never win.
             */
            switch (currentRule.getStrength()) {
            case RULE_ALL:
                return currentRule;

            case RULE_ELEMENT:
                if (!rule.getElement().equals(currentRule.getElement())) {
                    break;
                }
                // intentional fall-through
            case RULE_NAMESPACE:
                if (rule.getNamespace().equals(currentRule.getNamespace())) {
                    return currentRule;
                }
                break;
            }
        }
        return null;
    }


    /**
     * Orders a set or rules by priority, removes redundant rules and rules
     * that are shadowed by stronger, contradicting rules.
     */
    private static int prioritizeRules(Vector rules) {
        WhitespaceRule currentRule;
        int defaultAction = PRESERVE_SPACE;

        // Sort all rules with regard to priority
        quicksort(rules, 0, rules.size()-1);

        // Check if there are any "xsl:strip-space" elements at all.
        // If there are no xsl:strip elements we can ignore all xsl:preserve
        // elements and signal that all whitespaces should be preserved
        boolean strip = false;
        for (int i = 0; i < rules.size(); i++) {
            currentRule = (WhitespaceRule)rules.elementAt(i);
            if (currentRule.getAction() == STRIP_SPACE) {
                strip = true;
            }
        }
        // Return with default action: PRESERVE_SPACE
        if (!strip) {
            rules.removeAllElements();
            return PRESERVE_SPACE;
        }

        // Remove all rules that are contradicted by rules with higher priority
        for (int idx = 0; idx < rules.size(); ) {
            currentRule = (WhitespaceRule)rules.elementAt(idx);

            // Remove this single rule if it has no purpose
            if (findContradictingRule(rules,currentRule) != null) {
                rules.remove(idx);
            }
            else {
                // Remove all following rules if this one overrides all
                if (currentRule.getStrength() == RULE_ALL) {
                    defaultAction = currentRule.getAction();
                    for (int i = idx; i < rules.size(); i++) {
                        rules.removeElementAt(i);
                    }
                }
                // Skip to next rule (there might not be any)...
                idx++;
            }
        }

        // The rules vector could be empty if first rule has strength RULE_ALL
        if (rules.size() == 0) {
            return defaultAction;
        }

        // Now work backwards and strip away all rules that have the same
        // action as the default rule (no reason the check them at the end).
        do {
            currentRule = (WhitespaceRule)rules.lastElement();
            if (currentRule.getAction() == defaultAction) {
                rules.removeElementAt(rules.size() - 1);
            }
            else {
                break;
            }
        } while (rules.size() > 0);

        // Signal that whitespace detection predicate must be used.
        return defaultAction;
    }

    public static void compileStripSpace(BranchHandle strip[],
                                         int sCount,
                                         InstructionList il) {
        final InstructionHandle target = il.append(ICONST_1);
        il.append(IRETURN);
        for (int i = 0; i < sCount; i++) {
            strip[i].setTarget(target);
        }
    }

    public static void compilePreserveSpace(BranchHandle preserve[],
                                            int pCount,
                                            InstructionList il) {
        final InstructionHandle target = il.append(ICONST_0);
        il.append(IRETURN);
        for (int i = 0; i < pCount; i++) {
            preserve[i].setTarget(target);
        }
    }

    /*
    private static void compileDebug(ClassGenerator classGen,
                                     InstructionList il) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final int prt = cpg.addMethodref("java/lang/System/out",
                                         "println",
                                         "(Ljava/lang/String;)V");
        il.append(DUP);
        il.append(new INVOKESTATIC(prt));
    }
    */

    /**
     * Compiles the predicate method
     */
    private static void compilePredicate(Vector rules,
                                         int defaultAction,
                                         ClassGenerator classGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = new InstructionList();
        final XSLTC xsltc = classGen.getParser().getXSLTC();

        // private boolean Translet.stripSpace(int type) - cannot be static
        final MethodGenerator stripSpace =
            new MethodGenerator(ACC_PUBLIC | ACC_FINAL ,
                        jaxp.sun.org.apache.bcel.internal.generic.Type.BOOLEAN,
                        new jaxp.sun.org.apache.bcel.internal.generic.Type[] {
                            Util.getJCRefType(DOM_INTF_SIG),
                            jaxp.sun.org.apache.bcel.internal.generic.Type.INT,
                            jaxp.sun.org.apache.bcel.internal.generic.Type.INT
                        },
                        new String[] { "dom","node","type" },
                        "stripSpace",classGen.getClassName(),il,cpg);

        classGen.addInterface("jaxp.sun/org/apache/xalan/internal/xsltc/StripFilter");

        final int paramDom = stripSpace.getLocalIndex("dom");
        final int paramCurrent = stripSpace.getLocalIndex("node");
        final int paramType = stripSpace.getLocalIndex("type");

        BranchHandle strip[] = new BranchHandle[rules.size()];
        BranchHandle preserve[] = new BranchHandle[rules.size()];
        int sCount = 0;
        int pCount = 0;

        // Traverse all strip/preserve rules
        for (int i = 0; i<rules.size(); i++) {
            // Get the next rule in the prioritised list
            WhitespaceRule rule = (WhitespaceRule)rules.elementAt(i);

            // Returns the namespace for a node in the DOM
            final int gns = cpg.addInterfaceMethodref(DOM_INTF,
                                                      "getNamespaceName",
                                                      "(I)Ljava/lang/String;");

            final int strcmp = cpg.addMethodref("java/lang/String",
                                                "compareTo",
                                                "(Ljava/lang/String;)I");

            // Handle elements="ns:*" type rule
            if (rule.getStrength() == RULE_NAMESPACE) {
                il.append(new ALOAD(paramDom));
                il.append(new ILOAD(paramCurrent));
                il.append(new INVOKEINTERFACE(gns,2));
                il.append(new PUSH(cpg, rule.getNamespace()));
                il.append(new INVOKEVIRTUAL(strcmp));
                il.append(ICONST_0);

                if (rule.getAction() == STRIP_SPACE) {
                    strip[sCount++] = il.append(new IF_ICMPEQ(null));
                }
                else {
                    preserve[pCount++] = il.append(new IF_ICMPEQ(null));
                }
            }
            // Handle elements="ns:el" type rule
            else if (rule.getStrength() == RULE_ELEMENT) {
                // Create the QName for the element
                final Parser parser = classGen.getParser();
                QName qname;
                if (rule.getNamespace() != EMPTYSTRING)
                    qname = parser.getQName(rule.getNamespace(), null,
                                            rule.getElement());
                else
                    qname = parser.getQName(rule.getElement());

                // Register the element.
                final int elementType = xsltc.registerElement(qname);
                il.append(new ILOAD(paramType));
                il.append(new PUSH(cpg, elementType));

                // Compare current node type with wanted element type
                if (rule.getAction() == STRIP_SPACE)
                    strip[sCount++] = il.append(new IF_ICMPEQ(null));
                else
                    preserve[pCount++] = il.append(new IF_ICMPEQ(null));
            }
        }

        if (defaultAction == STRIP_SPACE) {
            compileStripSpace(strip, sCount, il);
            compilePreserveSpace(preserve, pCount, il);
        }
        else {
            compilePreserveSpace(preserve, pCount, il);
            compileStripSpace(strip, sCount, il);
        }

        classGen.addMethod(stripSpace);
    }

    /**
     * Compiles the predicate method
     */
    private static void compileDefault(int defaultAction,
                                       ClassGenerator classGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = new InstructionList();
        final XSLTC xsltc = classGen.getParser().getXSLTC();

        // private boolean Translet.stripSpace(int type) - cannot be static
        final MethodGenerator stripSpace =
            new MethodGenerator(ACC_PUBLIC | ACC_FINAL ,
                        jaxp.sun.org.apache.bcel.internal.generic.Type.BOOLEAN,
                        new jaxp.sun.org.apache.bcel.internal.generic.Type[] {
                            Util.getJCRefType(DOM_INTF_SIG),
                            jaxp.sun.org.apache.bcel.internal.generic.Type.INT,
                            jaxp.sun.org.apache.bcel.internal.generic.Type.INT
                        },
                        new String[] { "dom","node","type" },
                        "stripSpace",classGen.getClassName(),il,cpg);

        classGen.addInterface("jaxp.sun/org/apache/xalan/internal/xsltc/StripFilter");

        if (defaultAction == STRIP_SPACE)
            il.append(ICONST_1);
        else
            il.append(ICONST_0);
        il.append(IRETURN);

        classGen.addMethod(stripSpace);
    }


    /**
     * Takes a vector of WhitespaceRule objects and generates a predicate
     * method. This method returns the translets default action for handling
     * whitespace text-nodes:
     *    - USE_PREDICATE  (run the method generated by this method)
     *    - STRIP_SPACE    (always strip whitespace text-nodes)
     *    - PRESERVE_SPACE (always preserve whitespace text-nodes)
     */
    public static int translateRules(Vector rules,
                                     ClassGenerator classGen) {
        // Get the core rules in prioritized order
        final int defaultAction = prioritizeRules(rules);
        // The rules vector may be empty after prioritising
        if (rules.size() == 0) {
            compileDefault(defaultAction,classGen);
            return defaultAction;
        }
        // Now - create a predicate method and sequence through rules...
        compilePredicate(rules, defaultAction, classGen);
        // Return with the translets required action (
        return USE_PREDICATE;
    }

    /**
     * Sorts a range of rules with regard to PRIORITY only
     */
    private static void quicksort(Vector rules, int p, int r) {
        while (p < r) {
            final int q = partition(rules, p, r);
            quicksort(rules, p, q);
            p = q + 1;
        }
    }

    /**
     * Used with quicksort method above
     */
    private static int partition(Vector rules, int p, int r) {
        final WhitespaceRule x = (WhitespaceRule)rules.elementAt((p+r) >>> 1);
        int i = p - 1, j = r + 1;
        while (true) {
            while (x.compareTo((WhitespaceRule)rules.elementAt(--j)) < 0) {
            }
            while (x.compareTo((WhitespaceRule)rules.elementAt(++i)) > 0) {
            }
            if (i < j) {
                final WhitespaceRule tmp = (WhitespaceRule)rules.elementAt(i);
                rules.setElementAt(rules.elementAt(j), i);
                rules.setElementAt(tmp, j);
            }
            else {
                return j;
            }
        }
    }

    /**
     * Type-check contents/attributes - nothing to do...
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void; // We don't return anything.
    }

    /**
     * This method should not produce any code
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }
}
