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

import jaxp.w3c.dom.DOMException;

/**
 *  The <code>MediaList</code> interface provides the abstraction of an
 * ordered collection of media, without defining or constraining how this
 * collection is implemented. An empty list is the same as a list that
 * contains the medium <code>"all"</code>.
 * <p> The items in the <code>MediaList</code> are accessible via an integral
 * index, starting from 0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface MediaList {
    /**
     *  The parsable textual representation of the media list. This is a
     * comma-separated list of media.
     */
    public String getMediaText();
    /**
     *  The parsable textual representation of the media list. This is a
     * comma-separated list of media.
     * @exception DOMException
     *   SYNTAX_ERR: Raised if the specified string value has a syntax error
     *   and is unparsable.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this media list is
     *   readonly.
     */
    public void setMediaText(String mediaText)
                             throws DOMException;

    /**
     *  The number of media in the list. The range of valid media is
     * <code>0</code> to <code>length-1</code> inclusive.
     */
    public int getLength();

    /**
     *  Returns the <code>index</code>th in the list. If <code>index</code> is
     * greater than or equal to the number of media in the list, this
     * returns <code>null</code>.
     * @param index  Index into the collection.
     * @return  The medium at the <code>index</code>th position in the
     *   <code>MediaList</code>, or <code>null</code> if that is not a valid
     *   index.
     */
    public String item(int index);

    /**
     *  Deletes the medium indicated by <code>oldMedium</code> from the list.
     * @param oldMedium The medium to delete in the media list.
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this list is readonly.
     *   <br> NOT_FOUND_ERR: Raised if <code>oldMedium</code> is not in the
     *   list.
     */
    public void deleteMedium(String oldMedium)
                             throws DOMException;

    /**
     *  Adds the medium <code>newMedium</code> to the end of the list. If the
     * <code>newMedium</code> is already used, it is first removed.
     * @param newMedium The new medium to add.
     * @exception DOMException
     *    INVALID_CHARACTER_ERR: If the medium contains characters that are
     *   invalid in the underlying style language.
     *   <br> NO_MODIFICATION_ALLOWED_ERR: Raised if this list is readonly.
     */
    public void appendMedium(String newMedium)
                             throws DOMException;

}
