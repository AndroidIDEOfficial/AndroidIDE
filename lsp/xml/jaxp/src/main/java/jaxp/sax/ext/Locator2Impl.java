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

// Locator2Impl.java - extended LocatorImpl
// http://www.saxproject.org
// Public Domain: no warranty.
// $Id: Locator2Impl.java,v 1.2 2004/11/03 22:49:08 jsuttor Exp $

package jaxp.sax.ext;

import jaxp.sax.Locator;
import jaxp.sax.helpers.LocatorImpl;


/**
 * SAX2 extension helper for holding additional Entity information,
 * implementing the {@link Locator2} interface.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * <p> This is not part of core-only SAX2 distributions.</p>
 *
 * @since SAX 2.0.2
 * @author David Brownell
 */
public class Locator2Impl extends LocatorImpl implements Locator2
{
    private String      encoding;
    private String      version;


    /**
     * Construct a new, empty Locator2Impl object.
     * This will not normally be useful, since the main purpose
     * of this class is to make a snapshot of an existing Locator.
     */
    public Locator2Impl () { }

    /**
     * Copy an existing Locator or Locator2 object.
     * If the object implements Locator2, values of the
     * <em>encoding</em> and <em>version</em>strings are copied,
     * otherwise they set to <em>null</em>.
     *
     * @param locator The existing Locator object.
     */
    public Locator2Impl (Locator locator)
    {
        super (locator);
        if (locator instanceof Locator2) {
            Locator2 l2 = (Locator2) locator;

            version = l2.getXMLVersion ();
            encoding = l2.getEncoding ();
        }
    }

    ////////////////////////////////////////////////////////////////////
    // Locator2 method implementations
    ////////////////////////////////////////////////////////////////////

    /**
     * Returns the current value of the version property.
     *
     * @see #setXMLVersion
     */
    public String getXMLVersion ()
        { return version; }

    /**
     * Returns the current value of the encoding property.
     *
     * @see #setEncoding
     */
    public String getEncoding ()
        { return encoding; }


    ////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////

    /**
     * Assigns the current value of the version property.
     *
     * @param version the new "version" value
     * @see #getXMLVersion
     */
    public void setXMLVersion (String version)
        { this.version = version; }

    /**
     * Assigns the current value of the encoding property.
     *
     * @param encoding the new "encoding" value
     * @see #getEncoding
     */
    public void setEncoding (String encoding)
        { this.encoding = encoding; }
}
