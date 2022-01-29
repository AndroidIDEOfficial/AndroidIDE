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
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jaxp.w3c.dom.xpath;

/**
 * A new exception has been created for exceptions specific to these XPath
 * interfaces.
 * <p>See also the <a href='http://www.w3.org/2002/08/WD-DOM-Level-3-XPath-20020820'>Document Object Model (DOM) Level 3 XPath Specification</a>.
 */
public class XPathException extends RuntimeException {
    public XPathException(short code, String message) {
        super(message);
        this.code = code;
    }
    public short   code;
    // XPathExceptionCode
    /**
     * If the expression has a syntax error or otherwise is not a legal
     * expression according to the rules of the specific
     * <code>XPathEvaluator</code> or contains specialized extension
     * functions or variables not supported by this implementation.
     */
    public static final short INVALID_EXPRESSION_ERR    = 1;
    /**
     * If the expression cannot be converted to return the specified type.
     */
    public static final short TYPE_ERR                  = 2;

}