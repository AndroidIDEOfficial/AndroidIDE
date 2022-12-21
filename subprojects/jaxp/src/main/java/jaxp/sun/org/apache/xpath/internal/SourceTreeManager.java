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
/*
 * $Id: SourceTreeManager.java,v 1.2.4.1 2005/09/10 18:14:09 jeffsuttor Exp $
 */
package jaxp.sun.org.apache.xpath.internal;

import java.io.IOException;
import java.util.Vector;

import jaxp.xml.parsers.FactoryConfigurationError;
import jaxp.xml.parsers.ParserConfigurationException;
import jaxp.xml.parsers.SAXParser;
import jaxp.xml.parsers.SAXParserFactory;
import jaxp.xml.transform.Source;
import jaxp.xml.transform.SourceLocator;
import jaxp.xml.transform.TransformerException;
import jaxp.xml.transform.URIResolver;
import jaxp.xml.transform.sax.SAXSource;
import jaxp.xml.transform.stream.StreamSource;

import jaxp.sun.org.apache.xml.internal.dtm.DTM;
import jaxp.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import jaxp.sun.org.apache.xml.internal.utils.SystemIDResolver;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class bottlenecks all management of source trees.  The methods
 * in this class should allow easy garbage collection of source
 * trees (not yet!), and should centralize parsing for those source trees.
 */
public class SourceTreeManager
{

  /** Vector of SourceTree objects that this manager manages. */
  private Vector m_sourceTree = new Vector();

  /**
   * Reset the list of SourceTree objects that this manager manages.
   *
   */
  public void reset()
  {
    m_sourceTree = new Vector();
  }

  /** The TrAX URI resolver used to obtain source trees. */
  URIResolver m_uriResolver;

  /**
   * Set an object that will be used to resolve URIs used in
   * document(), etc.
   * @param resolver An object that implements the URIResolver interface,
   * or null.
   */
  public void setURIResolver(URIResolver resolver)
  {
    m_uriResolver = resolver;
  }

  /**
   * Get the object that will be used to resolve URIs used in
   * document(), etc.
   * @return An object that implements the URIResolver interface,
   * or null.
   */
  public URIResolver getURIResolver()
  {
    return m_uriResolver;
  }

  /**
   * Given a document, find the URL associated with that document.
   * @param owner Document that was previously processed by this liaison.
   *
   * @return The base URI of the owner argument.
   */
  public String findURIFromDoc(int owner)
  {
    int n = m_sourceTree.size();

    for (int i = 0; i < n; i++)
    {
      SourceTree sTree = (SourceTree) m_sourceTree.elementAt(i);

      if (owner == sTree.m_root)
        return sTree.m_url;
    }

    return null;
  }

  /**
   * This will be called by the processor when it encounters
   * an xsl:include, xsl:import, or document() function.
   *
   * @param base The base URI that should be used.
   * @param urlString Value from an xsl:import or xsl:include's href attribute,
   * or a URI specified in the document() function.
   *
   * @return a Source that can be used to process the resource.
   *
   * @throws IOException
   * @throws TransformerException
   */
  public Source resolveURI(
          String base, String urlString, SourceLocator locator)
            throws TransformerException, IOException
  {

    Source source = null;

    if (null != m_uriResolver)
    {
      source = m_uriResolver.resolve(urlString, base);
    }

    if (null == source)
    {
      String uri = SystemIDResolver.getAbsoluteURI(urlString, base);

      source = new StreamSource(uri);
    }

    return source;
  }

  /** JJK: Support  <?xalan:doc_cache_off?> kluge in ElemForEach.
   * TODO: This function is highly dangerous. Cache management must be improved.
   *
   * @param n The node to remove.
   */
  public void removeDocumentFromCache(int n)
  {
    if(DTM.NULL ==n)
      return;
    for(int i=m_sourceTree.size()-1;i>=0;--i)
    {
      SourceTree st=(SourceTree)m_sourceTree.elementAt(i);
      if(st!=null && st.m_root==n)
      {
        m_sourceTree.removeElementAt(i);
        return;
      }
    }
  }



  /**
   * Put the source tree root node in the document cache.
   * TODO: This function needs to be a LOT more sophisticated.
   *
   * @param n The node to cache.
   * @param source The Source object to cache.
   */
  public void putDocumentInCache(int n, Source source)
  {

    int cachedNode = getNode(source);

    if (DTM.NULL != cachedNode)
    {
      if (!(cachedNode == n))
        throw new RuntimeException(
          "Programmer's Error!  "
          + "putDocumentInCache found reparse of doc: "
          + source.getSystemId());
      return;
    }
    if (null != source.getSystemId())
    {
      m_sourceTree.addElement(new SourceTree(n, source.getSystemId()));
    }
  }

  /**
   * Given a Source object, find the node associated with it.
   *
   * @param source The Source object to act as the key.
   *
   * @return The node that is associated with the Source, or null if not found.
   */
  public int getNode(Source source)
  {

//    if (source instanceof DOMSource)
//      return ((DOMSource) source).getNode();

    // TODO: Not sure if the BaseID is really the same thing as the ID.
    String url = source.getSystemId();

    if (null == url)
      return DTM.NULL;

    int n = m_sourceTree.size();

    // System.out.println("getNode: "+n);
    for (int i = 0; i < n; i++)
    {
      SourceTree sTree = (SourceTree) m_sourceTree.elementAt(i);

      // System.out.println("getNode -         url: "+url);
      // System.out.println("getNode - sTree.m_url: "+sTree.m_url);
      if (url.equals(sTree.m_url))
        return sTree.m_root;
    }

    // System.out.println("getNode - returning: "+node);
    return DTM.NULL;
  }

  /**
   * Get the source tree from the a base URL and a URL string.
   *
   * @param base The base URI to use if the urlString is relative.
   * @param urlString An absolute or relative URL string.
   * @param locator The location of the caller, for diagnostic purposes.
   *
   * @return should be a non-null reference to the node identified by the
   * base and urlString.
   *
   * @throws TransformerException If the URL can not resolve to a node.
   */
  public int getSourceTree(
          String base, String urlString, SourceLocator locator, XPathContext xctxt)
            throws TransformerException
  {

    // System.out.println("getSourceTree");
    try
    {
      Source source = this.resolveURI(base, urlString, locator);

      // System.out.println("getSourceTree - base: "+base+", urlString: "+urlString+", source: "+source.getSystemId());
      return getSourceTree(source, locator, xctxt);
    }
    catch (IOException ioe)
    {
      throw new TransformerException(ioe.getMessage(), locator, ioe);
    }

    /* catch (TransformerException te)
     {
       throw new TransformerException(te.getMessage(), locator, te);
     }*/
  }

  /**
   * Get the source tree from the input source.
   *
   * @param source The Source object that should identify the desired node.
   * @param locator The location of the caller, for diagnostic purposes.
   *
   * @return non-null reference to a node.
   *
   * @throws TransformerException if the Source argument can't be resolved to
   *         a node.
   */
  public int getSourceTree(Source source, SourceLocator locator, XPathContext xctxt)
          throws TransformerException
  {

    int n = getNode(source);

    if (DTM.NULL != n)
      return n;

    n = parseToNode(source, locator, xctxt);

    if (DTM.NULL != n)
      putDocumentInCache(n, source);

    return n;
  }

  /**
   * Try to create a DOM source tree from the input source.
   *
   * @param source The Source object that identifies the source node.
   * @param locator The location of the caller, for diagnostic purposes.
   *
   * @return non-null reference to node identified by the source argument.
   *
   * @throws TransformerException if the source argument can not be resolved
   *         to a source node.
   */
  public int parseToNode(Source source, SourceLocator locator, XPathContext xctxt)
          throws TransformerException
  {

    try
    {
      Object xowner = xctxt.getOwnerObject();
      DTM dtm;
      if(null != xowner && xowner instanceof DTMWSFilter)
      {
        dtm = xctxt.getDTM(source, false,
                          (DTMWSFilter)xowner, false, true);
      }
      else
      {
        dtm = xctxt.getDTM(source, false, null, false, true);
      }
      return dtm.getDocument();
    }
    catch (Exception e)
    {
      //e.printStackTrace();
      throw new TransformerException(e.getMessage(), locator, e);
    }

  }

  /**
   * This method returns the SAX2 parser to use with the InputSource
   * obtained from this URI.
   * It may return null if any SAX2-conformant XML parser can be used,
   * or if getInputSource() will also return null. The parser must
   * be free for use (i.e.
   * not currently in use for another parse().
   *
   * @param inputSource The value returned from the URIResolver.
   * @return a SAX2 XMLReader to use to resolve the inputSource argument.
   * @param locator The location of the original caller, for diagnostic purposes.
   *
   * @throws TransformerException if the reader can not be created.
   */
  public static XMLReader getXMLReader(Source inputSource, SourceLocator locator)
          throws TransformerException
  {

    try
    {
      XMLReader reader = (inputSource instanceof SAXSource)
                         ? ((SAXSource) inputSource).getXMLReader() : null;

      if (null == reader)
      {
        try {
          SAXParserFactory factory=
              SAXParserFactory.newInstance();
          factory.setNamespaceAware( true );
          SAXParser jaxpParser=
              factory.newSAXParser();
          reader=jaxpParser.getXMLReader();

        } catch( ParserConfigurationException ex ) {
          throw new org.xml.sax.SAXException( ex );
        } catch( FactoryConfigurationError ex1 ) {
            throw new org.xml.sax.SAXException( ex1.toString() );
        } catch( NoSuchMethodError ex2 ) {
        }
        catch (AbstractMethodError ame){}
        if(null == reader)
          reader = XMLReaderFactory.createXMLReader();
      }

      try
      {
        reader.setFeature("http://xml.org/sax/features/namespace-prefixes",
                          true);
      }
      catch (org.xml.sax.SAXException se)
      {

        // What can we do?
        // TODO: User diagnostics.
      }

      return reader;
    }
    catch (org.xml.sax.SAXException se)
    {
      throw new TransformerException(se.getMessage(), locator, se);
    }
  }
}
