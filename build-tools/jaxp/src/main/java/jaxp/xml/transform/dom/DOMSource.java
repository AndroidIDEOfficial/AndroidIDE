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
 * Copyright (c) 2000, 2005, Oracle and/or its affiliates. All rights reserved.
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

package jaxp.xml.transform.dom;

import jaxp.xml.parsers.DocumentBuilder;
import jaxp.xml.parsers.DocumentBuilderFactory;
import jaxp.xml.transform.Source;

import jaxp.xml.transform.Transformer;
import jaxp.xml.transform.TransformerFactory;
import org.w3c.dom.Node;

/**
 * <p>Acts as a holder for a transformation Source tree in the
 * form of a Document Object Model (DOM) tree.</p>
 *
 * <p>Note that XSLT requires namespace support. Attempting to transform a DOM
 * that was not contructed with a namespace-aware parser may result in errors.
 * Parsers can be made namespace aware by calling
 * {@link DocumentBuilderFactory#setNamespaceAware(boolean awareness)}.</p>
 *
 * @author <a href="Jeff.Suttor@Sun.com">Jeff Suttor</a>
 * @see <a href="http://www.w3.org/TR/DOM-Level-2">Document Object Model (DOM) Level 2 Specification</a>
 */
public class DOMSource implements Source {

    /**
     * <p><code>Node</code> to serve as DOM source.</p>
     */
    private Node node;

    /**
     * <p>The base ID (URL or system ID) from where URLs
     * will be resolved.</p>
     */
    private String systemID;

    /** If {@link TransformerFactory#getFeature}
     * returns true when passed this value as an argument,
     * the Transformer supports Source input of this type.
     */
    public static final String FEATURE =
        "http://javax.xml.transform.dom.DOMSource/feature";

    /**
     * <p>Zero-argument default constructor.  If this constructor is used, and
     * no DOM source is set using {@link #setNode(Node node)} , then the
     * <code>Transformer</code> will
     * create an empty source {@link org.w3c.dom.Document} using
     * {@link DocumentBuilder#newDocument()}.</p>
     *
     * @see Transformer#transform(Source xmlSource, Result outputTarget)
     */
    public DOMSource() { }

    /**
     * Create a new input source with a DOM node.  The operation
     * will be applied to the subtree rooted at this node.  In XSLT,
     * a "/" pattern still means the root of the tree (not the subtree),
     * and the evaluation of global variables and parameters is done
     * from the root node also.
     *
     * @param n The DOM node that will contain the Source tree.
     */
    public DOMSource(Node n) {
        setNode(n);
    }

    /**
     * Create a new input source with a DOM node, and with the
     * system ID also passed in as the base URI.
     *
     * @param node The DOM node that will contain the Source tree.
     * @param systemID Specifies the base URI associated with node.
     */
    public DOMSource(Node node, String systemID) {
        setNode(node);
        setSystemId(systemID);
    }

    /**
     * Set the node that will represents a Source DOM tree.
     *
     * @param node The node that is to be transformed.
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Get the node that represents a Source DOM tree.
     *
     * @return The node that is to be transformed.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Set the base ID (URL or system ID) from where URLs
     * will be resolved.
     *
     * @param systemID Base URL for this DOM tree.
     */
    public void setSystemId(String systemID) {
        this.systemID = systemID;
    }

    /**
     * Get the base ID (URL or system ID) from where URLs
     * will be resolved.
     *
     * @return Base URL for this DOM tree.
     */
    public String getSystemId() {
        return this.systemID;
    }
}
