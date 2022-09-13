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
 * $Id: Import.java,v 1.8 2007/04/09 21:30:40 joehw Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

import jaxp.sun.org.apache.xalan.internal.XalanConstants;
import jaxp.sun.org.apache.xalan.internal.utils.SecuritySupport;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import jaxp.sun.org.apache.xml.internal.utils.SystemIDResolver;

import java.util.Enumeration;
import jaxp.xml.XMLConstants;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author Jacek Ambroziak
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author Gunnlaugur Briem <gthb@dimon.is>
 */
final class Import extends TopLevelElement {

    private Stylesheet _imported = null;

    public Stylesheet getImportedStylesheet() {
        return _imported;
    }

    public void parseContents(final Parser parser) {
        final XSLTC xsltc = parser.getXSLTC();
        final Stylesheet context = parser.getCurrentStylesheet();

        try {
            String docToLoad = getAttribute("href");
            if (context.checkForLoop(docToLoad)) {
                final ErrorMsg msg = new ErrorMsg(ErrorMsg.CIRCULAR_INCLUDE_ERR,
                                                  docToLoad, this);
                parser.reportError(FATAL, msg);
                return;
            }

            InputSource input = null;
            XMLReader reader = null;
            String currLoadedDoc = context.getSystemId();
            SourceLoader loader = context.getSourceLoader();

            // Use SourceLoader if available
            if (loader != null) {
                input = loader.loadSource(docToLoad, currLoadedDoc, xsltc);
                if (input != null) {
                    docToLoad = input.getSystemId();
                    reader = xsltc.getXMLReader();
                } else if (parser.errorsFound()) {
                    return;
                }
            }

            // No SourceLoader or not resolved by SourceLoader
            if (input == null) {
                docToLoad = SystemIDResolver.getAbsoluteURI(docToLoad, currLoadedDoc);
                String accessError = SecuritySupport.checkAccess(docToLoad,
                        (String)xsltc.getProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET),
                        XalanConstants.ACCESS_EXTERNAL_ALL);

                if (accessError != null) {
                    final ErrorMsg msg = new ErrorMsg(ErrorMsg.ACCESSING_XSLT_TARGET_ERR,
                                        SecuritySupport.sanitizePath(docToLoad), accessError,
                                        this);
                    parser.reportError(FATAL, msg);
                    return;
                }
                input = new InputSource(docToLoad);
            }

            // Return if we could not resolve the URL
            if (input == null) {
                final ErrorMsg msg =
                    new ErrorMsg(ErrorMsg.FILE_NOT_FOUND_ERR, docToLoad, this);
                parser.reportError(FATAL, msg);
                return;
            }

            final SyntaxTreeNode root;
            if (reader != null) {
                root = parser.parse(reader,input);
            }
            else {
                root = parser.parse(input);
            }

            if (root == null) return;
            _imported = parser.makeStylesheet(root);
            if (_imported == null) return;

            _imported.setSourceLoader(loader);
            _imported.setSystemId(docToLoad);
            _imported.setParentStylesheet(context);
            _imported.setImportingStylesheet(context);
        _imported.setTemplateInlining(context.getTemplateInlining());

            // precedence for the including stylesheet
            final int currPrecedence = parser.getCurrentImportPrecedence();
            final int nextPrecedence = parser.getNextImportPrecedence();
            _imported.setImportPrecedence(currPrecedence);
            context.setImportPrecedence(nextPrecedence);
            parser.setCurrentStylesheet(_imported);
            _imported.parseContents(parser);

            final Enumeration elements = _imported.elements();
            final Stylesheet topStylesheet = parser.getTopLevelStylesheet();
            while (elements.hasMoreElements()) {
                final Object element = elements.nextElement();
                if (element instanceof TopLevelElement) {
                    if (element instanceof Variable) {
                        topStylesheet.addVariable((Variable) element);
                    }
                    else if (element instanceof Param) {
                        topStylesheet.addParam((Param) element);
                    }
                    else {
                        topStylesheet.addElement((TopLevelElement) element);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            parser.setCurrentStylesheet(context);
        }
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        // do nothing
    }
}
