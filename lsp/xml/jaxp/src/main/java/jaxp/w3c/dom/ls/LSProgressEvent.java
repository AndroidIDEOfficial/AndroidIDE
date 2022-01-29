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

package jaxp.w3c.dom.ls;

import jaxp.w3c.dom.events.Event;

/**
 *  This interface represents a progress event object that notifies the
 * application about progress as a document is parsed. It extends the
 * <code>Event</code> interface defined in [<a href='http://www.w3.org/TR/2003/NOTE-DOM-Level-3-Events-20031107'>DOM Level 3 Events</a>]
 * .
 * <p> The units used for the attributes <code>position</code> and
 * <code>totalSize</code> are not specified and can be implementation and
 * input dependent.
 * <p>See also the <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-LS-20040407'>Document Object Model (DOM) Level 3 Load
and Save Specification</a>.
 */
public interface LSProgressEvent extends Event {
    /**
     * The input source that is being parsed.
     */
    public LSInput getInput();

    /**
     * The current position in the input source, including all external
     * entities and other resources that have been read.
     */
    public int getPosition();

    /**
     * The total size of the document including all external resources, this
     * number might change as a document is being parsed if references to
     * more external resources are seen. A value of <code>0</code> is
     * returned if the total size cannot be determined or estimated.
     */
    public int getTotalSize();

}
