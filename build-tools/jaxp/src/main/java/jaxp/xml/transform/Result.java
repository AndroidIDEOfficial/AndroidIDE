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

package jaxp.xml.transform;

/**
 * <p>An object that implements this interface contains the information
 * needed to build a transformation result tree.</p>
 *
 * @author <a href="Jeff.Suttor@Sun.com">Jeff Suttor</a>
 */
public interface Result {

    /**
     * The name of the processing instruction that is sent if the
     * result tree disables output escaping.
     *
     * <p>Normally, result tree serialization escapes & and < (and
     * possibly other characters) when outputting text nodes.
     * This ensures that the output is well-formed XML. However,
     * it is sometimes convenient to be able to produce output that is
     * almost, but not quite well-formed XML; for example,
     * the output may include ill-formed sections that will
     * be transformed into well-formed XML by a subsequent non-XML aware
     * process. If a processing instruction is sent with this name,
     * serialization should be output without any escaping. </p>
     *
     * <p>Result DOM trees may also have PI_DISABLE_OUTPUT_ESCAPING and
     * PI_ENABLE_OUTPUT_ESCAPING inserted into the tree.</p>
     *
     * @see <a href="http://www.w3.org/TR/xslt#disable-output-escaping">disable-output-escaping in XSLT Specification</a>
     */
    public static final String PI_DISABLE_OUTPUT_ESCAPING =
        "javax.xml.transform.disable-output-escaping";

    /**
     * The name of the processing instruction that is sent
     * if the result tree enables output escaping at some point after having
     * received a PI_DISABLE_OUTPUT_ESCAPING processing instruction.
     *
     * @see <a href="http://www.w3.org/TR/xslt#disable-output-escaping">disable-output-escaping in XSLT Specification</a>
     */
    public static final String PI_ENABLE_OUTPUT_ESCAPING =
        "javax.xml.transform.enable-output-escaping";

    /**
     * Set the system identifier for this Result.
     *
     * <p>If the Result is not to be written to a file, the system identifier is optional.
     * The application may still want to provide one, however, for use in error messages
     * and warnings, or to resolve relative output identifiers.</p>
     *
     * @param systemId The system identifier as a URI string.
     */
    public void setSystemId(String systemId);

    /**
     * Get the system identifier that was set with setSystemId.
     *
     * @return The system identifier that was set with setSystemId,
     * or null if setSystemId was not called.
     */
    public String getSystemId();
}
