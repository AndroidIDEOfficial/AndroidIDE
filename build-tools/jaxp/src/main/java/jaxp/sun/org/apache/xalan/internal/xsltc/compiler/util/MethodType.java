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
 * $Id: MethodType.java,v 1.2.4.1 2005/09/05 11:18:05 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.Vector;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class MethodType extends Type {
    private final Type _resultType;
    private final Vector _argsType;

    public MethodType(Type resultType) {
        _argsType = null;
        _resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1) {
        if (arg1 != Void) {
            _argsType = new Vector();
            _argsType.addElement(arg1);
        }
        else {
            _argsType = null;
        }
        _resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1, Type arg2) {
        _argsType = new Vector(2);
        _argsType.addElement(arg1);
        _argsType.addElement(arg2);
        _resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
        _argsType = new Vector(3);
        _argsType.addElement(arg1);
        _argsType.addElement(arg2);
        _argsType.addElement(arg3);
        _resultType = resultType;
    }

    public MethodType(Type resultType, Vector argsType) {
        _resultType = resultType;
        _argsType = argsType.size() > 0 ? argsType : null;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("method{");
        if (_argsType != null) {
            final int count = _argsType.size();
            for (int i=0; i<count; i++) {
                result.append(_argsType.elementAt(i));
                if (i != (count-1)) result.append(',');
            }
        }
        else {
            result.append("void");
        }
        result.append('}');
        return result.toString();
    }

    public String toSignature() {
        return toSignature("");
    }

    /**
     * Returns the signature of this method that results by adding
     * <code>lastArgSig</code> to the end of the argument list.
     */
    public String toSignature(String lastArgSig) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append('(');
        if (_argsType != null) {
            final int n = _argsType.size();
            for (int i = 0; i < n; i++) {
                buffer.append(((Type)_argsType.elementAt(i)).toSignature());
            }
        }
        return buffer
            .append(lastArgSig)
            .append(')')
            .append(_resultType.toSignature())
            .toString();
    }

    public jaxp.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return null;    // should never be called
    }

    public boolean identicalTo(Type other) {
        boolean result = false;
        if (other instanceof MethodType) {
            final MethodType temp = (MethodType) other;
            if (_resultType.identicalTo(temp._resultType)) {
                final int len = argsCount();
                result = len == temp.argsCount();
                for (int i = 0; i < len && result; i++) {
                    final Type arg1 = (Type)_argsType.elementAt(i);
                    final Type arg2 = (Type)temp._argsType.elementAt(i);
                    result = arg1.identicalTo(arg2);
                }
            }
        }
        return result;
    }

    public int distanceTo(Type other) {
        int result = Integer.MAX_VALUE;
        if (other instanceof MethodType) {
            final MethodType mtype = (MethodType) other;
            if (_argsType != null) {
                final int len = _argsType.size();
                if (len == mtype._argsType.size()) {
                    result = 0;
                    for (int i = 0; i < len; i++) {
                        Type arg1 = (Type) _argsType.elementAt(i);
                        Type arg2 = (Type) mtype._argsType.elementAt(i);
                        final int temp = arg1.distanceTo(arg2);
                        if (temp == Integer.MAX_VALUE) {
                            result = temp;  // return MAX_VALUE
                            break;
                        }
                        else {
                            result += arg1.distanceTo(arg2);
                        }
                    }
                }
            }
            else if (mtype._argsType == null) {
                result = 0;   // both methods have no args
            }
        }
        return result;
    }

    public Type resultType() {
        return _resultType;
    }

    public Vector argsType() {
        return _argsType;
    }

    public int argsCount() {
        return _argsType == null ? 0 : _argsType.size();
    }
}
