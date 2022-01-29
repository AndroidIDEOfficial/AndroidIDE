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
 *  Event operations may throw an <code>EventException</code> as specified in
 * their method descriptions.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Events-20001113'>Document Object Model (DOM) Level 2 Events Specification</a>.
 * @since DOM Level 2
 */
public class EventException extends RuntimeException {
    public EventException(short code, String message) {
        super(message);
        this.code = code;
    }
    public short   code;
    // EventExceptionCode
    /**
     *  If the <code>Event</code>'s type was not specified by initializing the
     * event before the method was called. Specification of the Event's type
     * as <code>null</code> or an empty string will also trigger this
     * exception.
     */
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;
}