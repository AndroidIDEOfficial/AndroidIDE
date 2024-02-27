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
 * Copyright 1999-2004 The Apache Software Foundation.
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

// $Id: JAXPExtensionsProvider.java,v 1.1.2.1 2005/08/01 01:30:17 jeffsuttor Exp $

package jaxp.sun.org.apache.xpath.internal.jaxp;

import jaxp.xml.xpath.XPathFunctionResolver;
import jaxp.xml.xpath.XPathFunction;
import jaxp.xml.xpath.XPathFunctionException;

import jaxp.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import jaxp.sun.org.apache.xpath.internal.ExtensionsProvider;
import jaxp.sun.org.apache.xpath.internal.objects.XObject;
import jaxp.sun.org.apache.xpath.internal.objects.XNodeSet;
import jaxp.sun.org.apache.xpath.internal.res.XPATHErrorResources;
import jaxp.sun.org.apache.xalan.internal.res.XSLMessages;
import jaxp.sun.org.apache.xalan.internal.utils.FeatureManager;

import jaxp.sun.org.apache.xpath.internal.functions.FuncExtFunction;

import java.util.Vector;
import java.util.ArrayList;
import jaxp.xml.namespace.QName;
import jaxp.xml.transform.TransformerException;

/**
 *
 * @author Ramesh Mandava ( ramesh.mandava@sun.com )
 */
public class JAXPExtensionsProvider implements ExtensionsProvider {

    private final XPathFunctionResolver resolver;
    private boolean extensionInvocationDisabled = false;

    public JAXPExtensionsProvider(XPathFunctionResolver resolver) {
        this.resolver = resolver;
        this.extensionInvocationDisabled = false;
    }

    public JAXPExtensionsProvider(XPathFunctionResolver resolver,
        boolean featureSecureProcessing, FeatureManager featureManager ) {
        this.resolver = resolver;
        if (featureSecureProcessing &&
                !featureManager.isFeatureEnabled(FeatureManager.Feature.ORACLE_ENABLE_EXTENSION_FUNCTION)) {
            this.extensionInvocationDisabled = true;
        }
    }

    /**
     * Is the extension function available?
     */

    public boolean functionAvailable(String ns, String funcName)
          throws TransformerException {
      try {
        if ( funcName == null ) {
            String fmsg = XSLMessages.createXPATHMessage(
                XPATHErrorResources.ER_ARG_CANNOT_BE_NULL,
                new Object[] {"Function Name"} );
            throw new NullPointerException ( fmsg );
        }
        //Find the XPathFunction corresponding to namespace and funcName
        QName myQName = new QName( ns, funcName );
        XPathFunction xpathFunction =
            resolver.resolveFunction ( myQName, 0 );
        if (  xpathFunction == null ) {
            return false;
        }
        return true;
      } catch ( Exception e ) {
        return false;
      }


    }


    /**
     * Is the extension element available?
     */
    public boolean elementAvailable(String ns, String elemName)
          throws TransformerException {
        return false;
    }

    /**
     * Execute the extension function.
     */
    public Object extFunction(String ns, String funcName, Vector argVec,
        Object methodKey) throws TransformerException {
        try {

            if ( funcName == null ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_ARG_CANNOT_BE_NULL,
                    new Object[] {"Function Name"} );
                throw new NullPointerException ( fmsg );
            }
            //Find the XPathFunction corresponding to namespace and funcName
            QName myQName = new QName( ns, funcName );

            // JAXP 1.3 spec says When XMLConstants.FEATURE_SECURE_PROCESSING
            // feature is set then invocation of extension functions need to
            // throw XPathFunctionException
            if ( extensionInvocationDisabled ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED,
                    new Object[] { myQName.toString() } );
                throw new XPathFunctionException ( fmsg );
            }

            // Assuming user is passing all the needed parameters ( including
            // default values )
            int arity = argVec.size();

            XPathFunction xpathFunction =
                resolver.resolveFunction ( myQName, arity );

            // not using methodKey
            ArrayList argList = new ArrayList( arity);
            for ( int i=0; i<arity; i++ ) {
                Object argument = argVec.elementAt( i );
                // XNodeSet object() returns NodeVector and not NodeList
                // Explicitly getting NodeList by using nodelist()
                if ( argument instanceof XNodeSet) {
                    argList.add ( i, ((XNodeSet)argument).nodelist() );
                } else if ( argument instanceof XObject) {
                    Object passedArgument = ((XObject)argument).object();
                    argList.add ( i, passedArgument );
                } else {
                    argList.add ( i, argument );
                }
            }

            return ( xpathFunction.evaluate ( argList ));
        } catch ( XPathFunctionException xfe ) {
            // If we get XPathFunctionException then we want to terminate
            // further execution by throwing WrappedRuntimeException
            throw new WrappedRuntimeException( xfe );
        } catch ( Exception e ) {
            throw new TransformerException( e );
        }

    }

    /**
     * Execute the extension function.
     */
    public Object extFunction(FuncExtFunction extFunction,
                              Vector argVec)
        throws TransformerException {
        try {
            String namespace = extFunction.getNamespace();
            String functionName = extFunction.getFunctionName();
            int arity = extFunction.getArgCount();
            QName myQName =
                new QName( namespace, functionName );

            // JAXP 1.3 spec says  When XMLConstants.FEATURE_SECURE_PROCESSING
            // feature is set then invocation of extension functions need to
            // throw XPathFunctionException
            if ( extensionInvocationDisabled ) {
                String fmsg = XSLMessages.createXPATHMessage(
                    XPATHErrorResources.ER_EXTENSION_FUNCTION_CANNOT_BE_INVOKED,                    new Object[] { myQName.toString() } );
                throw new XPathFunctionException ( fmsg );
            }

            XPathFunction xpathFunction =
                resolver.resolveFunction( myQName, arity );

            ArrayList argList = new ArrayList( arity);
            for ( int i=0; i<arity; i++ ) {
                Object argument = argVec.elementAt( i );
                // XNodeSet object() returns NodeVector and not NodeList
                // Explicitly getting NodeList by using nodelist()
                if ( argument instanceof XNodeSet ) {
                    argList.add ( i, ((XNodeSet)argument).nodelist() );
                } else if ( argument instanceof XObject ) {
                    Object passedArgument = ((XObject)argument).object();
                    argList.add ( i, passedArgument );
                } else {
                    argList.add ( i, argument );
                }
            }

            return ( xpathFunction.evaluate ( argList ));

        } catch ( XPathFunctionException xfe ) {
            // If we get XPathFunctionException then we want to terminate
            // further execution by throwing WrappedRuntimeException
            throw new WrappedRuntimeException( xfe );
        } catch ( Exception e ) {
            throw new TransformerException( e );
        }
    }

}
