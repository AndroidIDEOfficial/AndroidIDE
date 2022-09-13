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
 * $Id: SlotAllocator.java,v 1.2.4.1 2005/09/05 11:32:51 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;

import jaxp.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import jaxp.sun.org.apache.bcel.internal.generic.Type;

/**
 * @author Jacek Ambroziak
 */
final class SlotAllocator {

    private int   _firstAvailableSlot;
    private int   _size = 8;
    private int   _free = 0;
    private int[] _slotsTaken = new int[_size];

    public void initialize(LocalVariableGen[] vars) {
        final int length = vars.length;
        int slot = 0, size, index;

        for (int i = 0; i < length; i++) {
            size  = vars[i].getType().getSize();
            index = vars[i].getIndex();
            slot  = Math.max(slot, index + size);
        }
        _firstAvailableSlot = slot;
    }

    public int allocateSlot(Type type) {
        final int size = type.getSize();
        final int limit = _free;
        int slot = _firstAvailableSlot, where = 0;

        if (_free + size > _size) {
            final int[] array = new int[_size *= 2];
            for (int j = 0; j < limit; j++)
                array[j] = _slotsTaken[j];
            _slotsTaken = array;
        }

        while (where < limit) {
            if (slot + size <= _slotsTaken[where]) {
                // insert
                for (int j = limit - 1; j >= where; j--)
                    _slotsTaken[j + size] = _slotsTaken[j];
                break;
            }
            else {
                slot = _slotsTaken[where++] + 1;
            }
        }

        for (int j = 0; j < size; j++)
            _slotsTaken[where + j] = slot + j;

        _free += size;
        return slot;
    }

    public void releaseSlot(LocalVariableGen lvg) {
        final int size = lvg.getType().getSize();
        final int slot = lvg.getIndex();
        final int limit = _free;

        for (int i = 0; i < limit; i++) {
            if (_slotsTaken[i] == slot) {
                int j = i + size;
                while (j < limit) {
                    _slotsTaken[i++] = _slotsTaken[j++];
                }
                _free -= size;
                return;
            }
        }
        String state = "Variable slot allocation error"+
                       "(size="+size+", slot="+slot+", limit="+limit+")";
        ErrorMsg err = new ErrorMsg(ErrorMsg.INTERNAL_ERR, state);
        throw new Error(err.toString());
    }
}
