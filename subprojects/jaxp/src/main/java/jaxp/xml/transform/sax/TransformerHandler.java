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
 * Copyright (c) 2000, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jaxp.xml.transform.sax;

import jaxp.xml.transform.Result;
import jaxp.xml.transform.Transformer;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * A TransformerHandler
 * listens for SAX ContentHandler parse events and transforms
 * them to a Result.
 */
public interface TransformerHandler
    extends ContentHandler, LexicalHandler, DTDHandler {

    /**
     * <p>Set  the <code>Result</code> associated with this
     * <code>TransformerHandler</code> to be used for the transformation.</p>
     *
     * @param result A <code>Result</code> instance, should not be
     *   <code>null</code>.
     *
     * @throws IllegalArgumentException if result is invalid for some reason.
     */
    public void setResult(Result result) throws IllegalArgumentException;

    /**
     * Set the base ID (URI or system ID) from where relative
     * URLs will be resolved.
     * @param systemID Base URI for the source tree.
     */
    public void setSystemId(String systemID);

    /**
     * Get the base ID (URI or system ID) from where relative
     * URLs will be resolved.
     * @return The systemID that was set with {@link #setSystemId}.
     */
    public String getSystemId();

    /**
     * <p>Get the <code>Transformer</code> associated with this handler, which
     * is needed in order to set parameters and output properties.</p>
     *
     * @return <code>Transformer</code> associated with this
     *   <code>TransformerHandler</code>.
     */
    public Transformer getTransformer();
}
