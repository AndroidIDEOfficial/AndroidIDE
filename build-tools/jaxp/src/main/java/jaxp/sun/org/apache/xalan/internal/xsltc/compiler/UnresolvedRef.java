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
 * $Id: UnresolvedRef.java,v 1.5 2005/09/28 13:48:17 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/**
 * @author Morten Jorgensen
 */
final class UnresolvedRef extends VariableRefBase {

    private QName _variableName = null;
    private VariableRefBase _ref = null;

    public UnresolvedRef(QName name) {
        super();
        _variableName = name;
    }

    public QName getName() {
        return(_variableName);
    }

    private ErrorMsg reportError() {
        ErrorMsg err = new ErrorMsg(ErrorMsg.VARIABLE_UNDEF_ERR,
                                    _variableName, this);
        getParser().reportError(ERROR, err);
        return(err);
    }

    private VariableRefBase resolve(Parser parser, SymbolTable stable) {
        // At this point the AST is already built and we should be able to
        // find any declared global variable or parameter
        VariableBase ref = parser.lookupVariable(_variableName);
        if (ref == null) {
            ref = (VariableBase)stable.lookupName(_variableName);
        }
        if (ref == null) {
            reportError();
            return null;
        }

        // If in a top-level element, create dependency to the referenced var
        _variable = ref;
        addParentDependency();

        if (ref instanceof Variable) {
            return new VariableRef((Variable) ref);
        }
        else if (ref instanceof Param) {
            return new ParameterRef((Param)ref);
        }
        return null;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (_ref != null) {
            final String name = _variableName.toString();
            ErrorMsg err = new ErrorMsg(ErrorMsg.CIRCULAR_VARIABLE_ERR,
                                        name, this);
        }
        if ((_ref = resolve(getParser(), stable)) != null) {
            return (_type = _ref.typeCheck(stable));
        }
        throw new TypeCheckError(reportError());
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (_ref != null)
            _ref.translate(classGen, methodGen);
        else
            reportError();
    }

    public String toString() {
        return "unresolved-ref()";
    }

}
