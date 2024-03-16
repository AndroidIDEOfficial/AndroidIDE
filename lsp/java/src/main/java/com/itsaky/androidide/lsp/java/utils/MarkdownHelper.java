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

package com.itsaky.androidide.lsp.java.utils;

import com.itsaky.androidide.lsp.models.MarkupContent;
import com.itsaky.androidide.lsp.models.MarkupKind;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import openjdk.source.doctree.DocCommentTree;
import openjdk.source.doctree.DocTree;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MarkdownHelper {

  private static final Pattern HTML_TAG = Pattern.compile("<(\\w+)[^>]*>");
  private static final Logger LOG = Logger.getLogger("main");

  public static MarkupContent asMarkupContent(DocCommentTree comment) {
    String markdown = asMarkdown(comment);
    MarkupContent content = new MarkupContent();
    content.setKind(MarkupKind.MARKDOWN);
    content.setValue(markdown);
    return content;
  }

  public static String asMarkdown(DocCommentTree comment) {
    List<? extends DocTree> lines = comment.getFirstSentence();
    return asMarkdown(lines);
  }

  /** If `commentText` looks like HTML, convert it to markdown */
  public static String asMarkdown(String commentText) {
    if (isHtml(commentText)) {
      commentText = htmlToMarkdown(commentText);
    }
    commentText = replaceTags(commentText);
    return commentText;
  }

  private static String asMarkdown(List<? extends DocTree> lines) {
    StringJoiner join = new StringJoiner("\n");
    for (DocTree l : lines) join.add(l.toString());
    String html = join.toString();
    return asMarkdown(html);
  }

  private static Document parse(String html) {
    try {
      String xml = "<wrapper>" + html + "</wrapper>";
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.parse(new InputSource(new StringReader(xml)));
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void replaceNodes(Document doc, String tagName, Function<String, String> replace) {
    NodeList nodes = doc.getElementsByTagName(tagName);
    while (nodes.getLength() > 0) {
      Node node = nodes.item(0);
      Node parent = node.getParentNode();
      String text = replace.apply(node.getTextContent().trim());
      Node replacement = doc.createTextNode(text);
      parent.replaceChild(replacement, node);
      nodes = doc.getElementsByTagName(tagName);
    }
  }

  private static String print(Document doc) {
    try {
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      String wrapped = writer.getBuffer().toString();
      return wrapped.substring("<wrapper>".length(), wrapped.length() - "</wrapper>".length());
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private static void check(CharBuffer in, char expected) {
    char head = in.get();
    if (head != expected) {
      throw new RuntimeException(String.format("want `%s` got `%s`", expected, head));
    }
  }

  private static boolean empty(CharBuffer in) {
    return in.position() == in.limit();
  }

  private static char peek(CharBuffer in) {
    return in.get(in.position());
  }

  private static String parseTag(CharBuffer in) {
    check(in, '@');
    StringBuilder tag = new StringBuilder();
    while (!empty(in) && Character.isAlphabetic(peek(in))) {
      tag.append(in.get());
    }
    return tag.toString();
  }

  private static void parseBlock(CharBuffer in, StringBuilder out) {
    check(in, '{');
    if (peek(in) == '@') {
      String tag = parseTag(in);
      if (peek(in) == ' ') in.get();
      switch (tag) {
        case "code":
        case "link":
        case "linkplain":
          out.append("`");
          parseInner(in, out);
          out.append("`");
          break;
        case "literal":
          parseInner(in, out);
          break;
        default:
          LOG.warning(String.format("Unknown tag `@%s`", tag));
          parseInner(in, out);
      }
    } else {
      parseInner(in, out);
    }
    check(in, '}');
  }

  private static void parseInner(CharBuffer in, StringBuilder out) {
    while (!empty(in)) {
      switch (peek(in)) {
        case '{':
          parseBlock(in, out);
          break;
        case '}':
          return;
        default:
          out.append(in.get());
      }
    }
  }

  private static void parse(CharBuffer in, StringBuilder out) {
    while (!empty(in)) {
      parseInner(in, out);
    }
  }

  private static String replaceTags(String in) {
    StringBuilder out = new StringBuilder();
    parse(CharBuffer.wrap(in), out);
    return out.toString();
  }

  private static String htmlToMarkdown(String html) {
    html = replaceTags(html);

    Document doc = parse(html);

    replaceNodes(doc, "i", contents -> String.format("*%s*", contents));
    replaceNodes(doc, "b", contents -> String.format("**%s**", contents));
    replaceNodes(doc, "pre", contents -> String.format("`%s`", contents));
    replaceNodes(doc, "code", contents -> String.format("`%s`", contents));
    replaceNodes(doc, "a", contents -> contents);

    return print(doc);
  }

  private static boolean isHtml(String text) {
    Matcher tags = HTML_TAG.matcher(text);
    while (tags.find()) {
      String tag = tags.group(1);
      String close = String.format("</%s>", tag);
      int findClose = text.indexOf(close, tags.end());
      if (findClose != -1) return true;
    }
    return false;
  }
}
