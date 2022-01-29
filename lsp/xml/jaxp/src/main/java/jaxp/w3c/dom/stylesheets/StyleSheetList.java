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

package jaxp.w3c.dom.stylesheets;

/**
 * The <code>StyleSheetList</code> interface provides the abstraction of an
 * ordered collection of style sheets.
 * <p> The items in the <code>StyleSheetList</code> are accessible via an
 * integral index, starting from 0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface StyleSheetList {
    /**
     *  The number of <code>StyleSheets</code> in the list. The range of valid
     * child stylesheet indices is <code>0</code> to <code>length-1</code>
     * inclusive.
     */
    public int getLength();

    /**
     *  Used to retrieve a style sheet by ordinal index. If index is greater
     * than or equal to the number of style sheets in the list, this returns
     * <code>null</code>.
     * @param index Index into the collection
     * @return The style sheet at the <code>index</code> position in the
     *   <code>StyleSheetList</code>, or <code>null</code> if that is not a
     *   valid index.
     */
    public StyleSheet item(int index);

}
