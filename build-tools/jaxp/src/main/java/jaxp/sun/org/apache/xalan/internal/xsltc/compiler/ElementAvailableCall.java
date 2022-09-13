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
 * $Id: ElementAvailableCall.java,v 1.2.4.1 2005/09/01 14:13:01 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class ElementAvailableCall extends FunctionCall {

    public ElementAvailableCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    /**
     * Force the argument to this function to be a literal string.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (argument() instanceof LiteralExpr) {
            return _type = Type.Boolean;
        }
        ErrorMsg err = new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR,
                                    "element-available", this);
        throw new TypeCheckError(err);
    }

    /**
     * Returns an object representing the compile-time evaluation
     * of an expression. We are only using this for function-available
     * and element-available at this time.
     */
    public Object evaluateAtCompileTime() {
        return getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns the result that this function will return
     */
    public boolean getResult() {
        try {
            final LiteralExpr arg = (LiteralExpr) argument();
            final String qname = arg.getValue();
            final int index = qname.indexOf(':');
            final String localName = (index > 0) ?
                qname.substring(index + 1) : qname;
            return getParser().elementSupported(arg.getNamespace(),
                                                localName);
        }
        catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Calls to 'element-available' are resolved at compile time since
     * the namespaces declared in the stylsheet are not available at run
     * time. Consequently, arguments to this function must be literals.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final boolean result = getResult();
        methodGen.getInstructionList().append(new PUSH(cpg, result));
    }
}
