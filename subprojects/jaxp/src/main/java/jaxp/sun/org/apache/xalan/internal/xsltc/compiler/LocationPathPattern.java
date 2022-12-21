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
 * $Id: LocationPathPattern.java,v 1.2.4.1 2005/09/12 10:42:42 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xml.internal.dtm.Axis;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public abstract class LocationPathPattern extends Pattern {
    private Template _template;
    private int _importPrecedence;
    private double _priority = Double.NaN;
    private int _position = 0;

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;               // TODO
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        // TODO: What does it mean to translate a Pattern ?
    }

    public void setTemplate(final Template template) {
        _template = template;
        _priority = template.getPriority();
        _importPrecedence = template.getImportPrecedence();
        _position = template.getPosition();
    }

    public Template getTemplate() {
        return _template;
    }

    public final double getPriority() {
        return Double.isNaN(_priority) ? getDefaultPriority() : _priority;
    }

    public double getDefaultPriority() {
        return 0.5;
    }

    /**
     * This method is used by the Mode class to prioritise patterns and
     * template. This method is called for templates that are in the same
     * mode and that match on the same core pattern. The rules used are:
     *  o) first check precedence - highest precedence wins
     *  o) then check priority - highest priority wins
     *  o) then check the position - the template that occured last wins
     */
    public boolean noSmallerThan(LocationPathPattern other) {
        if (_importPrecedence > other._importPrecedence) {
            return true;
        }
        else if (_importPrecedence == other._importPrecedence) {
            if (_priority > other._priority) {
                return true;
            }
            else if (_priority == other._priority) {
                if (_position > other._position) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract StepPattern getKernelPattern();

    public abstract void reduceKernelPattern();

    public abstract boolean isWildcard();

    public int getAxis() {
        final StepPattern sp = getKernelPattern();
        return (sp != null) ? sp.getAxis() : Axis.CHILD;
    }

    public String toString() {
        return "root()";
    }
}
