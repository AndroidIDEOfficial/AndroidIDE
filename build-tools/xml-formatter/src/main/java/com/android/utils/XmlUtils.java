/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.utils;

import static com.android.SdkConstants.AMP_ENTITY;
import static com.android.SdkConstants.ANDROID_NS_NAME;
import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.APOS_ENTITY;
import static com.android.SdkConstants.APP_PREFIX;
import static com.android.SdkConstants.GT_ENTITY;
import static com.android.SdkConstants.LT_ENTITY;
import static com.android.SdkConstants.QUOT_ENTITY;
import static com.android.SdkConstants.XMLNS;
import static com.android.SdkConstants.XMLNS_PREFIX;
import static com.android.SdkConstants.XMLNS_URI;
import static com.google.common.base.Charsets.UTF_16BE;
import static com.google.common.base.Charsets.UTF_16LE;
import static com.google.common.base.Charsets.UTF_8;

import com.android.SdkConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.common.io.Files;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/** XML Utilities */
public class XmlUtils {
    public static final String XML_COMMENT_BEGIN = "<!--"; //$NON-NLS-1$
    public static final String XML_COMMENT_END = "-->";    //$NON-NLS-1$
    public static final String XML_PROLOG =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";  //$NON-NLS-1$

    /**
     * Separator for xml namespace and localname
     */
    public static final char NS_SEPARATOR = ':';                  //$NON-NLS-1$

    /**
     * Returns the namespace prefix matching the requested namespace URI.
     * If no such declaration is found, returns the default "android" prefix for
     * the Android URI, and "app" for other URI's. By default the app namespace
     * will be created. If this is not desirable, call
     * {@link #lookupNamespacePrefix(Node, String, boolean)} instead.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found,
     *              e.g. {@link SdkConstants#ANDROID_URI}
     * @return The first prefix declared or the default "android" prefix
     *              (or "app" for non-Android URIs)
     */
    @NotNull
    public static String lookupNamespacePrefix(@NotNull Node node, @NotNull String nsUri) {
        String defaultPrefix = ANDROID_URI.equals(nsUri) ? ANDROID_NS_NAME : APP_PREFIX;
        return lookupNamespacePrefix(node, nsUri, defaultPrefix, true /*create*/);
    }

    /**
     * Returns the namespace prefix matching the requested namespace URI. If no
     * such declaration is found, returns the default "android" prefix for the
     * Android URI, and "app" for other URI's.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found, e.g.
     *            {@link SdkConstants#ANDROID_URI}
     * @param create whether the namespace declaration should be created, if
     *            necessary
     * @return The first prefix declared or the default "android" prefix (or
     *         "app" for non-Android URIs)
     */
    @NotNull
    public static String lookupNamespacePrefix(@NotNull Node node, @NotNull String nsUri,
            boolean create) {
        String defaultPrefix = ANDROID_URI.equals(nsUri) ? ANDROID_NS_NAME : APP_PREFIX;
        return lookupNamespacePrefix(node, nsUri, defaultPrefix, create);
    }

    /**
     * Returns the namespace prefix matching the requested namespace URI. If no
     * such declaration is found, returns the default "android" prefix.
     *
     * @param node The current node. Must not be null.
     * @param nsUri The namespace URI of which the prefix is to be found, e.g.
     *            {@link SdkConstants#ANDROID_URI}
     * @param defaultPrefix The default prefix (root) to use if the namespace is
     *            not found. If null, do not create a new namespace if this URI
     *            is not defined for the document.
     * @param create whether the namespace declaration should be created, if
     *            necessary
     * @return The first prefix declared or the provided prefix (possibly with a
     *            number appended to avoid conflicts with existing prefixes.
     */
    public static String lookupNamespacePrefix(
            @Nullable Node node, @Nullable String nsUri, @Nullable String defaultPrefix,
            boolean create) {
        // Note: Node.lookupPrefix is not implemented in wst/xml/core NodeImpl.java
        // The following code emulates this simple call:
        //   String prefix = node.lookupPrefix(NS_RESOURCES);

        // if the requested URI is null, it denotes an attribute with no namespace.
        if (nsUri == null) {
            return null;
        }

        // per XML specification, the "xmlns" URI is reserved
        if (XMLNS_URI.equals(nsUri)) {
            return XMLNS;
        }

        HashSet<String> visited = new HashSet<String>();
        Document doc = node == null ? null : node.getOwnerDocument();

        // Ask the document about it. This method may not be implemented by the Document.
        String nsPrefix = null;
        try {
            nsPrefix = doc != null ? doc.lookupPrefix(nsUri) : null;
            if (nsPrefix != null) {
                return nsPrefix;
            }
        } catch (Throwable t) {
            // ignore
        }

        // If that failed, try to look it up manually.
        // This also gathers prefixed in use in the case we want to generate a new one below.
        for (; node != null && node.getNodeType() == Node.ELEMENT_NODE;
               node = node.getParentNode()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int n = attrs.getLength() - 1; n >= 0; --n) {
                Node attr = attrs.item(n);
                if (XMLNS.equals(attr.getPrefix())) {
                    String uri = attr.getNodeValue();
                    nsPrefix = attr.getLocalName();
                    // Is this the URI we are looking for? If yes, we found its prefix.
                    if (nsUri.equals(uri)) {
                        return nsPrefix;
                    }
                    visited.add(nsPrefix);
                }
            }
        }

        // Failed the find a prefix. Generate a new sensible default prefix, unless
        // defaultPrefix was null in which case the caller does not want the document
        // modified.
        if (defaultPrefix == null) {
            return null;
        }

        //
        // We need to make sure the prefix is not one that was declared in the scope
        // visited above. Pick a unique prefix from the provided default prefix.
        String prefix = defaultPrefix;
        String base = prefix;
        for (int i = 1; visited.contains(prefix); i++) {
            prefix = base + Integer.toString(i);
        }
        // Also create & define this prefix/URI in the XML document as an attribute in the
        // first element of the document.
        if (doc != null) {
            node = doc.getFirstChild();
            while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
                node = node.getNextSibling();
            }
            if (node != null && create) {
                // This doesn't work:
                //Attr attr = doc.createAttributeNS(XMLNS_URI, prefix);
                //attr.setPrefix(XMLNS);
                //
                // Xerces throws
                //org.w3c.dom.DOMException: NAMESPACE_ERR: An attempt is made to create or
                // change an object in a way which is incorrect with regard to namespaces.
                //
                // Instead pass in the concatenated prefix. (This is covered by
                // the UiElementNodeTest#testCreateNameSpace() test.)
                Attr attr = doc.createAttributeNS(XMLNS_URI, XMLNS_PREFIX + prefix);
                attr.setValue(nsUri);
                node.getAttributes().setNamedItemNS(attr);
            }
        }

        return prefix;
    }

    /**
     * Converts the given attribute value to an XML-attribute-safe value, meaning that
     * single and double quotes are replaced with their corresponding XML entities.
     *
     * @param attrValue the value to be escaped
     * @return the escaped value
     */
    @NotNull
    public static String toXmlAttributeValue(@NotNull String attrValue) {
        for (int i = 0, n = attrValue.length(); i < n; i++) {
            char c = attrValue.charAt(i);
            if (c == '"' || c == '\'' || c == '<' || c == '&') {
                StringBuilder sb = new StringBuilder(2 * attrValue.length());
                appendXmlAttributeValue(sb, attrValue);
                return sb.toString();
            }
        }

        return attrValue;
    }

    /**
     * Converts the given XML-attribute-safe value to a java string
     *
     * @param escapedAttrValue the escaped value
     * @return the unescaped value
     */
    @NotNull
    public static String fromXmlAttributeValue(@NotNull String escapedAttrValue) {
        String workingString = escapedAttrValue.replace(QUOT_ENTITY, "\"");
        workingString = workingString.replace(LT_ENTITY, "<");
        workingString = workingString.replace(APOS_ENTITY, "'");
        workingString = workingString.replace(AMP_ENTITY, "&");
        workingString = workingString.replace(GT_ENTITY, ">");

        return workingString;
    }

    /**
     * Converts the given attribute value to an XML-text-safe value, meaning that
     * less than and ampersand characters are escaped.
     *
     * @param textValue the text value to be escaped
     * @return the escaped value
     */
    @NotNull
    public static String toXmlTextValue(@NotNull String textValue) {
        for (int i = 0, n = textValue.length(); i < n; i++) {
            char c = textValue.charAt(i);
            if (c == '<' || c == '&') {
                StringBuilder sb = new StringBuilder(2 * textValue.length());
                appendXmlTextValue(sb, textValue);
                return sb.toString();
            }
        }

        return textValue;
    }

    /**
     * Appends text to the given {@link StringBuilder} and escapes it as required for a
     * DOM attribute node.
     *
     * @param sb the string builder
     * @param attrValue the attribute value to be appended and escaped
     */
    public static void appendXmlAttributeValue(@NotNull StringBuilder sb,
            @NotNull String attrValue) {
        int n = attrValue.length();
        // &, ", ' and < are illegal in attributes; see http://www.w3.org/TR/REC-xml/#NT-AttValue
        // (' legal in a " string and " is legal in a ' string but here we'll stay on the safe
        // side)
        for (int i = 0; i < n; i++) {
            char c = attrValue.charAt(i);
            if (c == '"') {
                sb.append(QUOT_ENTITY);
            } else if (c == '<') {
                sb.append(LT_ENTITY);
            } else if (c == '\'') {
                sb.append(APOS_ENTITY);
            } else if (c == '&') {
                sb.append(AMP_ENTITY);
            } else {
                sb.append(c);
            }
        }
    }

    /**
     * Appends text to the given {@link StringBuilder} and escapes it as required for a
     * DOM text node.
     *
     * @param sb the string builder
     * @param textValue the text value to be appended and escaped
     */
    public static void appendXmlTextValue(@NotNull StringBuilder sb, @NotNull String textValue) {
        for (int i = 0, n = textValue.length(); i < n; i++) {
            char c = textValue.charAt(i);
            if (c == '<') {
                sb.append(LT_ENTITY);
            } else if (c == '&') {
                sb.append(AMP_ENTITY);
            } else {
                sb.append(c);
            }
        }
    }

    /**
     * Returns true if the given node has one or more element children
     *
     * @param node the node to test for element children
     * @return true if the node has one or more element children
     */
    public static boolean hasElementChildren(@NotNull Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0, n = children.getLength(); i < n; i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a character reader for the given file, which must be a UTF encoded file.
     * <p>
     * The reader does not need to be closed by the caller (because the file is read in
     * full in one shot and the resulting array is then wrapped in a byte array input stream,
     * which does not need to be closed.)
     */
    public static Reader getUtfReader(@NotNull File file) throws IOException {
        byte[] bytes = Files.toByteArray(file);
        int length = bytes.length;
        if (length == 0) {
            return new StringReader("");
        }

        switch (bytes[0]) {
            case (byte)0xEF: {
                if (length >= 3
                        && bytes[1] == (byte)0xBB
                        && bytes[2] == (byte)0xBF) {
                    // UTF-8 BOM: EF BB BF: Skip it
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 3, length - 3),
                            UTF_8);
                }
                break;
            }
            case (byte)0xFE: {
                if (length >= 2
                        && bytes[1] == (byte)0xFF) {
                    // UTF-16 Big Endian BOM: FE FF
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 2, length - 2),
                            UTF_16BE);
                }
                break;
            }
            case (byte)0xFF: {
                if (length >= 2
                        && bytes[1] == (byte)0xFE) {
                    if (length >= 4
                            && bytes[2] == (byte)0x00
                            && bytes[3] == (byte)0x00) {
                        // UTF-32 Little Endian BOM: FF FE 00 00
                        return new InputStreamReader(new ByteArrayInputStream(bytes, 4,
                                length - 4), "UTF-32LE");
                    }

                    // UTF-16 Little Endian BOM: FF FE
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 2, length - 2),
                            UTF_16LE);
                }
                break;
            }
            case (byte)0x00: {
                if (length >= 4
                        && bytes[0] == (byte)0x00
                        && bytes[1] == (byte)0x00
                        && bytes[2] == (byte)0xFE
                        && bytes[3] == (byte)0xFF) {
                    // UTF-32 Big Endian BOM: 00 00 FE FF
                    return new InputStreamReader(new ByteArrayInputStream(bytes, 4, length - 4),
                            "UTF-32BE");
                }
                break;
            }
        }

        // No byte order mark: Assume UTF-8 (where the BOM is optional).
        return new InputStreamReader(new ByteArrayInputStream(bytes), UTF_8);
    }

    /**
     * Parses the given XML string as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware.
     *
     * @param xml            the XML content to be parsed (must be well formed)
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document
     */
    @NotNull
    public static Document parseDocument(@NotNull String xml, boolean namespaceAware)
            throws ParserConfigurationException, IOException, SAXException {
        xml = stripBom(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        InputSource is = new InputSource(new StringReader(xml));
        factory.setNamespaceAware(namespaceAware);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }

    /**
     * Parses the given UTF file as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware.
     *
     * @param file           the UTF encoded file to parse
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document
     */
    @NotNull
    public static Document parseUtfXmlFile(@NotNull File file, boolean namespaceAware)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Reader reader = getUtfReader(file);
        try {
            InputSource is = new InputSource(reader);
            factory.setNamespaceAware(namespaceAware);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(is);
        } finally {
            reader.close();
        }
    }

    /** Strips out a leading UTF byte order mark, if present */
    @NotNull
    public static String stripBom(@NotNull String xml) {
        if (!xml.isEmpty() && xml.charAt(0) == '\uFEFF') {
            return xml.substring(1);
        }
        return xml;
    }

    /**
     * Parses the given XML string as a DOM document, using the JDK parser. The parser does not
     * validate, and is optionally namespace aware. Any parsing errors are silently ignored.
     *
     * @param xml            the XML content to be parsed (must be well formed)
     * @param namespaceAware whether the parser is namespace aware
     * @return the DOM document, or null
     */
    @Nullable
    public static Document parseDocumentSilently(@NotNull String xml, boolean namespaceAware) {
        try {
            return parseDocument(xml, namespaceAware);
        } catch (Exception e) {
            // pass
            // This method is deliberately silent; will return null
        }

        return null;
    }

    /**
     * Dump an XML tree to string. This does not perform any pretty printing.
     * To perform pretty printing, use {@code XmlPrettyPrinter.prettyPrint(node)} in
     * {@code sdk-common}.
     */
    public static String toXml(Node node) {
        StringBuilder sb = new StringBuilder(1000);
        append(sb, node, 0);
        return sb.toString();
    }

    /** Dump node to string without indentation adjustments */
    private static void append(
            @NotNull StringBuilder sb,
            @NotNull Node node,
            int indent) {
        short nodeType = node.getNodeType();
        switch (nodeType) {
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE: {
                sb.append(XML_PROLOG);
                NodeList children = node.getChildNodes();
                for (int i = 0, n = children.getLength(); i < n; i++) {
                    append(sb, children.item(i), indent);
                }
                break;
            }
            case Node.COMMENT_NODE:
                sb.append(XML_COMMENT_BEGIN);
                sb.append(node.getNodeValue());
                sb.append(XML_COMMENT_END);
                break;
            case Node.TEXT_NODE: {
                sb.append(toXmlTextValue(node.getNodeValue()));
                break;
            }
            case Node.CDATA_SECTION_NODE: {
                sb.append("<![CDATA["); //$NON-NLS-1$
                sb.append(node.getNodeValue());
                sb.append("]]>");       //$NON-NLS-1$
                break;
            }
            case Node.ELEMENT_NODE: {
                sb.append('<');
                Element element = (Element) node;
                sb.append(element.getTagName());

                NamedNodeMap attributes = element.getAttributes();
                NodeList children = element.getChildNodes();
                int childCount = children.getLength();
                int attributeCount = attributes.getLength();

                if (attributeCount > 0) {
                    for (int i = 0; i < attributeCount; i++) {
                        Node attribute = attributes.item(i);
                        sb.append(' ');
                        sb.append(attribute.getNodeName());
                        sb.append('=').append('"');
                        sb.append(toXmlAttributeValue(attribute.getNodeValue()));
                        sb.append('"');
                    }
                }

                if (childCount == 0) {
                    sb.append('/');
                }
                sb.append('>');
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        Node child = children.item(i);
                        append(sb, child, indent + 1);
                    }
                    sb.append('<').append('/');
                    sb.append(element.getTagName());
                    sb.append('>');
                }
                break;
            }

            default:
                throw new UnsupportedOperationException(
                        "Unsupported node type " + nodeType + ": not yet implemented");
        }
    }

    /**
     * Format the given floating value into an XML string, omitting decimals if
     * 0
     *
     * @param value the value to be formatted
     * @return the corresponding XML string for the value
     */
    public static String formatFloatAttribute(double value) {
        if (value != (int) value) {
            // Run String.format without a locale, because we don't want locale-specific
            // conversions here like separating the decimal part with a comma instead of a dot!
            return String.format((Locale) null, "%.2f", value); //$NON-NLS-1$
        } else {
            return Integer.toString((int) value);
        }
    }
}
