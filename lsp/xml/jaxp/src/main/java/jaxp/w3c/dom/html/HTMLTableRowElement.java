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

package jaxp.w3c.dom.html;

import jaxp.w3c.dom.DOMException;

/**
 *  A row in a table. See the  TR element definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLTableRowElement extends HTMLElement {
    /**
     *  The index of this row, relative to the entire table, starting from 0.
     * This is in document tree order and not display order. The
     * <code>rowIndex</code> does not take into account sections (
     * <code>THEAD</code> , <code>TFOOT</code> , or <code>TBODY</code> )
     * within the table.
     */
    public int getRowIndex();

    /**
     *  The index of this row, relative to the current section (
     * <code>THEAD</code> , <code>TFOOT</code> , or <code>TBODY</code> ),
     * starting from 0.
     */
    public int getSectionRowIndex();

    /**
     *  The collection of cells in this row.
     */
    public HTMLCollection getCells();

    /**
     *  Horizontal alignment of data within cells of this row. See the  align
     * attribute definition in HTML 4.0.
     */
    public String getAlign();
    public void setAlign(String align);

    /**
     *  Background color for rows. See the  bgcolor attribute definition in
     * HTML 4.0. This attribute is deprecated in HTML 4.0.
     */
    public String getBgColor();
    public void setBgColor(String bgColor);

    /**
     *  Alignment character for cells in a column. See the  char attribute
     * definition in HTML 4.0.
     */
    public String getCh();
    public void setCh(String ch);

    /**
     *  Offset of alignment character. See the  charoff attribute definition
     * in HTML 4.0.
     */
    public String getChOff();
    public void setChOff(String chOff);

    /**
     *  Vertical alignment of data within cells of this row. See the  valign
     * attribute definition in HTML 4.0.
     */
    public String getVAlign();
    public void setVAlign(String vAlign);

    /**
     *  Insert an empty <code>TD</code> cell into this row. If
     * <code>index</code> is equal to the number of cells, the new cell is
     * appended
     * @param index  The place to insert the cell, starting from 0.
     * @return  The newly created cell.
     * @exception DOMException
     *    INDEX_SIZE_ERR: Raised if the specified <code>index</code> is
     *   greater than the number of cells or if the index is negative.
     */
    public HTMLElement insertCell(int index)
                                  throws DOMException;

    /**
     *  Delete a cell from the current row.
     * @param index  The index of the cell to delete, starting from 0.
     * @exception DOMException
     *    INDEX_SIZE_ERR: Raised if the specified <code>index</code> is
     *   greater than or equal to the number of cells or if the index is
     *   negative.
     */
    public void deleteCell(int index)
                           throws DOMException;

}
