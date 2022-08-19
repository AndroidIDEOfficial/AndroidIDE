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
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: XSLTCSource.java,v 1.2.4.1 2005/09/06 12:43:28 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.trax;

import jaxp.xml.transform.Source;
import jaxp.xml.transform.stream.StreamSource;

import jaxp.sun.org.apache.xalan.internal.xsltc.DOM;
import jaxp.sun.org.apache.xalan.internal.xsltc.StripFilter;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import jaxp.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import jaxp.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import jaxp.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;

import org.xml.sax.SAXException;

/**
 * @author Morten Jorgensen
 */
public final class XSLTCSource implements Source {

    private String     _systemId = null;
    private Source     _source   = null;
    private ThreadLocal _dom     = new ThreadLocal();

    /**
     * Create a new XSLTC-specific source from a system ID
     */
    public XSLTCSource(String systemId)
    {
        _systemId = systemId;
    }

    /**
     * Create a new XSLTC-specific source from a JAXP Source
     */
    public XSLTCSource(Source source)
    {
        _source = source;
    }

    /**
     * Implements javax.xml.transform.Source.setSystemId()
     * Set the system identifier for this Source.
     * This Source can get its input either directly from a file (in this case
     * it will instanciate and use a JAXP parser) or it can receive it through
     * ContentHandler/LexicalHandler interfaces.
     * @param systemId The system Id for this Source
     */
    public void setSystemId(String systemId) {
        _systemId = systemId;
        if (_source != null) {
            _source.setSystemId(systemId);
        }
    }

    /**
     * Implements javax.xml.transform.Source.getSystemId()
     * Get the system identifier that was set with setSystemId.
     * @return The system identifier that was set with setSystemId,
     *         or null if setSystemId was not called.
     */
    public String getSystemId() {
        if (_source != null) {
            return _source.getSystemId();
        }
        else {
            return(_systemId);
        }
    }

    /**
     * Internal interface which returns a DOM for a given DTMManager and translet.
     */
    protected DOM getDOM(XSLTCDTMManager dtmManager, AbstractTranslet translet)
        throws SAXException
    {
        SAXImpl idom = (SAXImpl)_dom.get();

        if (idom != null) {
            if (dtmManager != null) {
                idom.migrateTo(dtmManager);
            }
        }
        else {
            Source source = _source;
            if (source == null) {
                if (_systemId != null && _systemId.length() > 0) {
                    source = new StreamSource(_systemId);
                }
                else {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.XSLTC_SOURCE_ERR);
                    throw new SAXException(err.toString());
                }
            }

            DOMWSFilter wsfilter = null;
            if (translet != null && translet instanceof StripFilter) {
                wsfilter = new DOMWSFilter(translet);
            }

            boolean hasIdCall = (translet != null) ? translet.hasIdCall() : false;

            if (dtmManager == null) {
                dtmManager = XSLTCDTMManager.newInstance();
            }

            idom = (SAXImpl)dtmManager.getDTM(source, true, wsfilter, false, false, hasIdCall);

            String systemId = getSystemId();
            if (systemId != null) {
                idom.setDocumentURI(systemId);
            }
            _dom.set(idom);
        }
        return idom;
    }

}
