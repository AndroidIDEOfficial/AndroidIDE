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

package jaxp.w3c.dom;

/**
 * <code>DOMError</code> is an interface that describes an error.
 * <p>See also the <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407'>Document Object Model (DOM) Level 3 Core Specification</a>.
 * @since DOM Level 3
 */
public interface DOMError {
    // ErrorSeverity
    /**
     * The severity of the error described by the <code>DOMError</code> is
     * warning. A <code>SEVERITY_WARNING</code> will not cause the
     * processing to stop, unless <code>DOMErrorHandler.handleError()</code>
     * returns <code>false</code>.
     */
    public static final short SEVERITY_WARNING          = 1;
    /**
     * The severity of the error described by the <code>DOMError</code> is
     * error. A <code>SEVERITY_ERROR</code> may not cause the processing to
     * stop if the error can be recovered, unless
     * <code>DOMErrorHandler.handleError()</code> returns <code>false</code>.
     */
    public static final short SEVERITY_ERROR            = 2;
    /**
     * The severity of the error described by the <code>DOMError</code> is
     * fatal error. A <code>SEVERITY_FATAL_ERROR</code> will cause the
     * normal processing to stop. The return value of
     * <code>DOMErrorHandler.handleError()</code> is ignored unless the
     * implementation chooses to continue, in which case the behavior
     * becomes undefined.
     */
    public static final short SEVERITY_FATAL_ERROR      = 3;

    /**
     * The severity of the error, either <code>SEVERITY_WARNING</code>,
     * <code>SEVERITY_ERROR</code>, or <code>SEVERITY_FATAL_ERROR</code>.
     */
    public short getSeverity();

    /**
     * An implementation specific string describing the error that occurred.
     */
    public String getMessage();

    /**
     *  A <code>DOMString</code> indicating which related data is expected in
     * <code>relatedData</code>. Users should refer to the specification of
     * the error in order to find its <code>DOMString</code> type and
     * <code>relatedData</code> definitions if any.
     * <p ><b>Note:</b>  As an example,
     * <code>Document.normalizeDocument()</code> does generate warnings when
     * the "split-cdata-sections" parameter is in use. Therefore, the method
     * generates a <code>SEVERITY_WARNING</code> with <code>type</code>
     * <code>"cdata-sections-splitted"</code> and the first
     * <code>CDATASection</code> node in document order resulting from the
     * split is returned by the <code>relatedData</code> attribute.
     */
    public String getType();

    /**
     * The related platform dependent exception if any.
     */
    public Object getRelatedException();

    /**
     *  The related <code>DOMError.type</code> dependent data if any.
     */
    public Object getRelatedData();

    /**
     * The location of the error.
     */
    public DOMLocator getLocation();

}
