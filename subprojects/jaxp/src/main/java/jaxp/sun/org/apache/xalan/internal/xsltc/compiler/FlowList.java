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
 * $Id: FlowList.java,v 1.2.4.1 2005/09/01 15:21:43 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import java.util.Iterator;
import java.util.Vector;

import jaxp.sun.org.apache.bcel.internal.generic.BranchHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionHandle;
import jaxp.sun.org.apache.bcel.internal.generic.InstructionList;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class FlowList {
    private Vector _elements;

    public FlowList() {
        _elements = null;
    }

    public FlowList(InstructionHandle bh) {
        _elements = new Vector();
        _elements.addElement(bh);
    }

    public FlowList(FlowList list) {
        _elements = list._elements;
    }

    public FlowList add(InstructionHandle bh) {
        if (_elements == null) {
            _elements = new Vector();
        }
        _elements.addElement(bh);
        return this;
    }

    public FlowList append(FlowList right) {
        if (_elements == null) {
            _elements = right._elements;
        }
        else {
            final Vector temp = right._elements;
            if (temp != null) {
                final int n = temp.size();
                for (int i = 0; i < n; i++) {
                    _elements.addElement(temp.elementAt(i));
                }
            }
        }
        return this;
    }

    /**
     * Back patch a flow list. All instruction handles must be branch handles.
     */
    public void backPatch(InstructionHandle target) {
        if (_elements != null) {
            final int n = _elements.size();
            for (int i = 0; i < n; i++) {
                BranchHandle bh = (BranchHandle)_elements.elementAt(i);
                bh.setTarget(target);
            }
            _elements.clear();          // avoid backpatching more than once
        }
    }

    /**
     * Redirect the handles from oldList to newList. "This" flow list
     * is assumed to be relative to oldList.
     */
    public FlowList copyAndRedirect(InstructionList oldList,
                                    InstructionList newList)
    {
        final FlowList result = new FlowList();
        if (_elements == null) {
            return result;
        }

        final int n = _elements.size();
        final Iterator oldIter = oldList.iterator();
        final Iterator newIter = newList.iterator();

        while (oldIter.hasNext()) {
            final InstructionHandle oldIh = (InstructionHandle) oldIter.next();
            final InstructionHandle newIh = (InstructionHandle) newIter.next();

            for (int i = 0; i < n; i++) {
                if (_elements.elementAt(i) == oldIh) {
                    result.add(newIh);
                }
            }
        }
        return result;
    }
}
