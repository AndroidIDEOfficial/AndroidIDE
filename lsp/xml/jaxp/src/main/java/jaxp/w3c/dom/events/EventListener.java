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

package jaxp.w3c.dom.events;


/**
 *  The <code>EventListener</code> interface is the primary method for
 * handling events. Users implement the <code>EventListener</code> interface
 * and register their listener on an <code>EventTarget</code> using the
 * <code>AddEventListener</code> method. The users should also remove their
 * <code>EventListener</code> from its <code>EventTarget</code> after they
 * have completed using the listener.
 * <p> When a <code>Node</code> is copied using the <code>cloneNode</code>
 * method the <code>EventListener</code>s attached to the source
 * <code>Node</code> are not attached to the copied <code>Node</code>. If
 * the user wishes the same <code>EventListener</code>s to be added to the
 * newly created copy the user must add them manually.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113'>Document Object Model (DOM) Level 2 Events Specification</a>.
 * @since DOM Level 2
 */
public interface EventListener {
    /**
     *  This method is called whenever an event occurs of the type for which
     * the <code> EventListener</code> interface was registered.
     * @param evt  The <code>Event</code> contains contextual information
     *   about the event. It also contains the <code>stopPropagation</code>
     *   and <code>preventDefault</code> methods which are used in
     *   determining the event's flow and default action.
     */
    public void handleEvent(Event evt);
}