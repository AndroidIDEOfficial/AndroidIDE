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
 * $Id: FunctionAvailableCall.java,v 1.2.4.1 2005/09/01 15:30:25 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import jaxp.sun.org.apache.bcel.internal.generic.PUSH;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import jaxp.sun.org.apache.xalan.internal.utils.ObjectFactory;

/**
 * @author G. Todd Miller
 * @author Santiago Pericas-Geertsen
 */
final class FunctionAvailableCall extends FunctionCall {

    private Expression _arg;
    private String     _nameOfFunct = null;
    private String     _namespaceOfFunct = null;
    private boolean    _isFunctionAvailable = false;

    /**
     * Constructs a FunctionAvailableCall FunctionCall. Takes the
     * function name qname, for example, 'function-available', and
     * a list of arguments where the arguments must be instances of
     * LiteralExpression.
     */
    public FunctionAvailableCall(QName fname, Vector arguments) {
        super(fname, arguments);
        _arg = (Expression)arguments.elementAt(0);
        _type = null;

        if (_arg instanceof LiteralExpr) {
            LiteralExpr arg = (LiteralExpr) _arg;
            _namespaceOfFunct = arg.getNamespace();
            _nameOfFunct = arg.getValue();

            if (!isInternalNamespace()) {
              _isFunctionAvailable = hasMethods();
            }
        }
    }

    /**
     * Argument of function-available call must be literal, typecheck
     * returns the type of function-available to be boolean.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (_type != null) {
           return _type;
        }
        if (_arg instanceof LiteralExpr) {
            return _type = Type.Boolean;
        }
        ErrorMsg err = new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR,
                        "function-available", this);
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
     * for external java functions only: reports on whether or not
     * the specified method is found in the specifed class.
     */
    private boolean hasMethods() {
        LiteralExpr arg = (LiteralExpr)_arg;

        // Get the class name from the namespace uri
        String className = getClassNameFromUri(_namespaceOfFunct);

        // Get the method name from the argument to function-available
        String methodName = null;
        int colonIndex = _nameOfFunct.indexOf(":");
        if (colonIndex > 0) {
          String functionName = _nameOfFunct.substring(colonIndex+1);
          int lastDotIndex = functionName.lastIndexOf('.');
          if (lastDotIndex > 0) {
            methodName = functionName.substring(lastDotIndex+1);
            if (className != null && !className.equals(""))
              className = className + "." + functionName.substring(0, lastDotIndex);
            else
              className = functionName.substring(0, lastDotIndex);
          }
          else
            methodName = functionName;
        }
        else
          methodName = _nameOfFunct;

        if (className == null || methodName == null) {
            return false;
        }

        // Replace the '-' characters in the method name
        if (methodName.indexOf('-') > 0)
          methodName = replaceDash(methodName);

        try {
            final Class clazz = ObjectFactory.findProviderClass(className, true);

            if (clazz == null) {
                return false;
            }

            final Method[] methods = clazz.getMethods();

            for (int i = 0; i < methods.length; i++) {
                final int mods = methods[i].getModifiers();

                if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                        && methods[i].getName().equals(methodName))
                {
                    return true;
                }
            }
        }
        catch (ClassNotFoundException e) {
          return false;
        }
        return false;
    }

    /**
     * Reports on whether the function specified in the argument to
     * xslt function 'function-available' was found.
     */
    public boolean getResult() {
        if (_nameOfFunct == null) {
            return false;
        }

        if (isInternalNamespace()) {
            final Parser parser = getParser();
            _isFunctionAvailable =
                parser.functionSupported(Util.getLocalName(_nameOfFunct));
        }
        return _isFunctionAvailable;
    }

    /**
     * Return true if the namespace uri is null or it is the XSLTC translet uri.
     */
    private boolean isInternalNamespace() {
        return (_namespaceOfFunct == null ||
            _namespaceOfFunct.equals(EMPTYSTRING) ||
            _namespaceOfFunct.equals(TRANSLET_URI));
    }

    /**
     * Calls to 'function-available' are resolved at compile time since
     * the namespaces declared in the stylsheet are not available at run
     * time. Consequently, arguments to this function must be literals.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        methodGen.getInstructionList().append(new PUSH(cpg, getResult()));
    }

}
