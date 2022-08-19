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
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
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

package jaxp.sun.org.apache.xerces.internal.jaxp.validation;

import jaxp.sun.org.apache.xerces.internal.impl.Constants;
import jaxp.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import jaxp.xml.transform.Result;
import jaxp.xml.transform.Source;
import jaxp.xml.transform.Transformer;
import jaxp.xml.transform.TransformerConfigurationException;
import jaxp.xml.transform.TransformerException;
import jaxp.xml.transform.TransformerFactory;
import jaxp.xml.transform.TransformerFactoryConfigurationError;
import jaxp.xml.transform.sax.SAXResult;
import jaxp.xml.transform.sax.SAXTransformerFactory;
import jaxp.xml.transform.sax.TransformerHandler;
import jaxp.xml.transform.stax.StAXResult;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * <p>A validator helper for <code>StAXSource</code>s.</p>
 *
 * @author <a href="mailto:Sunitha.Reddy@Sun.com">Sunitha Reddy</a>
 */
public final class StAXValidatorHelper implements ValidatorHelper {
    private static final String DEFAULT_TRANSFORMER_IMPL = "jaxp.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";

    /** Component manager. **/
    private XMLSchemaValidatorComponentManager fComponentManager;

    private Transformer identityTransformer1 = null;
    private TransformerHandler identityTransformer2 = null;
    private ValidatorHandlerImpl handler = null;

    /** Creates a new instance of StaxValidatorHelper */
    public StAXValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
        fComponentManager = componentManager;
    }

    public void validate(Source source, Result result)
        throws SAXException, IOException {

        if (result == null || result instanceof StAXResult) {

            if( identityTransformer1==null ) {
                try {
                    SAXTransformerFactory tf = fComponentManager.getFeature(Constants.ORACLE_FEATURE_SERVICE_MECHANISM) ?
                                    (SAXTransformerFactory)SAXTransformerFactory.newInstance()
                                    : (SAXTransformerFactory) TransformerFactory.newInstance(DEFAULT_TRANSFORMER_IMPL, StAXValidatorHelper.class.getClassLoader());
                    XMLSecurityManager securityManager = (XMLSecurityManager)fComponentManager.getProperty(Constants.SECURITY_MANAGER);
                    if (securityManager != null) {
                        for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
                            if (securityManager.isSet(limit.ordinal())){
                                tf.setAttribute(limit.apiProperty(),
                                        securityManager.getLimitValueAsString(limit));
                            }
                        }
                        if (securityManager.printEntityCountInfo()) {
                            tf.setAttribute(Constants.JDK_ENTITY_COUNT_INFO, "yes");
                        }
                    }

                    identityTransformer1 = tf.newTransformer();
                    identityTransformer2 = tf.newTransformerHandler();
                } catch (TransformerConfigurationException e) {
                    // this is impossible, but again better safe than sorry
                    throw new TransformerFactoryConfigurationError(e);
                }
            }

            handler = new ValidatorHandlerImpl(fComponentManager);
            if( result!=null ) {
                handler.setContentHandler(identityTransformer2);
                identityTransformer2.setResult(result);
            }

            try {
                identityTransformer1.transform( source, new SAXResult(handler) );
            } catch (TransformerException e) {
                if( e.getException() instanceof SAXException )
                    throw (SAXException)e.getException();
                throw new SAXException(e);
            } finally {
                handler.setContentHandler(null);
            }
            return;
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(fComponentManager.getLocale(),
                "SourceResultMismatch",
                new Object [] {source.getClass().getName(), result.getClass().getName()}));
    }
}
