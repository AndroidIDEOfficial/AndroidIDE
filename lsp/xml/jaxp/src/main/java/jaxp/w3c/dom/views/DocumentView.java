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

package jaxp.w3c.dom.views;

/**
 * The <code>DocumentView</code> interface is implemented by
 * <code>Document</code> objects in DOM implementations supporting DOM
 * Views. It provides an attribute to retrieve the default view of a
 * document.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Views-20001113'>Document Object Model (DOM) Level 2 Views Specification</a>.
 * @since DOM Level 2
 */
public interface DocumentView {
    /**
     * The default <code>AbstractView</code> for this <code>Document</code>,
     * or <code>null</code> if none available.
     */
    public AbstractView getDefaultView();

}
