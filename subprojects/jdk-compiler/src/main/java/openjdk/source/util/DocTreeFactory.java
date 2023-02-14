/*
 * Copyright (c) 2011, 2020, Oracle and/or its affiliates. All rights reserved.
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

package openjdk.source.util;

import java.util.List;

import jdkx.lang.model.element.Name;
import jdkx.tools.Diagnostic;
import jdkx.tools.JavaFileObject;

import openjdk.source.doctree.AttributeTree;
import openjdk.source.doctree.AttributeTree.ValueKind;
import openjdk.source.doctree.AuthorTree;
import openjdk.source.doctree.CommentTree;
import openjdk.source.doctree.DeprecatedTree;
import openjdk.source.doctree.DocCommentTree;
import openjdk.source.doctree.DocRootTree;
import openjdk.source.doctree.DocTree;
import openjdk.source.doctree.DocTypeTree;
import openjdk.source.doctree.EndElementTree;
import openjdk.source.doctree.EntityTree;
import openjdk.source.doctree.ErroneousTree;
import openjdk.source.doctree.HiddenTree;
import openjdk.source.doctree.IdentifierTree;
import openjdk.source.doctree.IndexTree;
import openjdk.source.doctree.InheritDocTree;
import openjdk.source.doctree.LinkTree;
import openjdk.source.doctree.LiteralTree;
import openjdk.source.doctree.ParamTree;
import openjdk.source.doctree.ProvidesTree;
import openjdk.source.doctree.ReferenceTree;
import openjdk.source.doctree.ReturnTree;
import openjdk.source.doctree.SeeTree;
import openjdk.source.doctree.SerialDataTree;
import openjdk.source.doctree.SerialFieldTree;
import openjdk.source.doctree.SerialTree;
import openjdk.source.doctree.SinceTree;
import openjdk.source.doctree.StartElementTree;
import openjdk.source.doctree.SummaryTree;
import openjdk.source.doctree.SystemPropertyTree;
import openjdk.source.doctree.TextTree;
import openjdk.source.doctree.ThrowsTree;
import openjdk.source.doctree.UnknownBlockTagTree;
import openjdk.source.doctree.UnknownInlineTagTree;
import openjdk.source.doctree.UsesTree;
import openjdk.source.doctree.ValueTree;
import openjdk.source.doctree.VersionTree;

/**
 * Factory for creating {@code DocTree} nodes.
 *
 * @implNote The methods in an implementation of this interface may only accept {@code DocTree}
 * nodes that have been created by the same implementation.
 *
 * @since 9
 */
public interface DocTreeFactory {
    /**
     * Creates a new {@code AttributeTree} object, to represent an HTML attribute in an HTML tag.
     * @param name  the name of the attribute
     * @param vkind the kind of the attribute value
     * @param value the value, if any, of the attribute
     * @return an {@code AttributeTree} object
     */
    AttributeTree newAttributeTree(Name name, ValueKind vkind, List<? extends DocTree> value);

    /**
     * Creates a new {@code AuthorTree} object, to represent an {@code @author} tag.
     * @param name the name of the author
     * @return an {@code AuthorTree} object
     */
    AuthorTree newAuthorTree(List<? extends DocTree> name);

    /**
     * Creates a new {@code LiteralTree} object, to represent a {@code {@code }} tag.
     * @param text the content of the tag
     * @return a {@code LiteralTree} object
     */
    LiteralTree newCodeTree(TextTree text);

    /**
     * Creates a new {@code CommentTree}, to represent an HTML comment.
     * @param text the content of the comment
     * @return a {@code CommentTree} object
     */
    CommentTree newCommentTree(String text);

    /**
     * Creates a new {@code DeprecatedTree} object, to represent an {@code @deprecated} tag.
     * @param text the content of the tag
     * @return a {@code DeprecatedTree} object
     */
    DeprecatedTree newDeprecatedTree(List<? extends DocTree> text);

    /**
     * Creates a new {@code DocCommentTree} object, to represent a complete doc comment.
     * @param fullBody the entire body of the doc comment
     * @param tags the block tags in the doc comment
     * @return a {@code DocCommentTree} object
     */
    DocCommentTree newDocCommentTree(List<? extends DocTree> fullBody, List<? extends DocTree> tags);

    /**
     * Creates a new {@code DocCommentTree} object, to represent the entire doc comment.
     * @param fullBody the entire body of the doc comment
     * @param tags the block tags in the doc comment
     * @param preamble the meta content of an html file including the body tag
     * @param postamble the meta content of an html including the closing body tag
     * @return a {@code DocCommentTree} object
     * @since 10
     */
    DocCommentTree newDocCommentTree(List<? extends DocTree> fullBody,
                                     List<? extends DocTree> tags,
                                     List<? extends DocTree> preamble,
                                     List<? extends DocTree> postamble);
    /**
     * Creates a new {@code DocRootTree} object, to represent an {@code {@docRoot}} tag.
     * @return a {@code DocRootTree} object
     */
    DocRootTree newDocRootTree();

    /**
     * Creates a new {@code DocTypeTree}, to represent a {@code DOCTYPE} HTML declaration.
     * @param text the content of the declaration
     * @return a {@code DocTypeTree} object
     * @since 10
     */
    DocTypeTree newDocTypeTree(String text);

    /**
     * Creates a new {@code EndElement} object, to represent the end of an HTML element.
     * @param name the name of the HTML element
     * @return an {@code EndElementTree} object
     */
    EndElementTree newEndElementTree(Name name);

    /**
     * Creates a new {@code EntityTree} object, to represent an HTML entity.
     * @param name the name of the entity, representing the characters between '&amp;' and ';'
     * in the representation of the entity in an HTML document
     * @return an {@code EntityTree} object
     */
    EntityTree newEntityTree(Name name);

    /**
     * Creates a new {@code ErroneousTree} object, to represent some unparseable input.
     * @param text the unparseable text
     * @param diag a diagnostic associated with the unparseable text, or {@code null}
     * @return an {@code ErroneousTree} object
     */
    ErroneousTree newErroneousTree(String text, Diagnostic<JavaFileObject> diag);

    /**
     * Creates a new {@code ThrowsTree} object, to represent an {@code @exception} tag.
     * @param name the name of the exception
     * @param description a description of why the exception might be thrown
     * @return an {@code ThrowsTree} object
     */
    ThrowsTree newExceptionTree(ReferenceTree name, List<? extends DocTree> description);

    /**
     * Creates a new {@code HiddenTree} object, to represent an {@code @hidden} tag.
     * @param text the content of the tag
     * @return a {@code HiddenTree} object
     */
    HiddenTree newHiddenTree(List<? extends DocTree> text);

    /**
     * Creates a new {@code IdentifierTree} object, to represent an identifier, such as in a
     * {@code @param} tag.
     * @param name the name of the identifier
     * @return an {@code IdentifierTree} object
     */
    IdentifierTree newIdentifierTree(Name name);

    /**
     * Creates a new {@code IndexTree} object, to represent an {@code {@index }} tag.
     * @param term the search term
     * @param description an optional description of the search term
     * @return an {@code IndexTree} object
     */
    IndexTree newIndexTree(DocTree term, List<? extends DocTree> description);

    /**
     * Creates a new {@code InheritDocTree} object, to represent an {@code {@inheritDoc}} tag.
     * @return an {@code InheritDocTree} object
     */
    InheritDocTree newInheritDocTree();

    /**
     * Creates a new {@code LinkTree} object, to represent a {@code {@link }} tag.
     * @param ref the API element being referenced
     * @param label an optional label for the link
     * @return a {@code LinkTree} object
     */
    LinkTree newLinkTree(ReferenceTree ref, List<? extends DocTree> label);

    /**
     * Creates a new {@code LinkTree} object, to represent a {@code {@linkplain }} tag.
     * @param ref the API element being referenced
     * @param label an optional label for the link
     * @return a {@code LinkTree} object
     */
    LinkTree newLinkPlainTree(ReferenceTree ref, List<? extends DocTree> label);

    /**
     * Creates a new {@code LiteralTree} object, to represent a {@code {@literal }} tag.
     * @param text the content of the tag
     * @return a {@code LiteralTree} object
     */
    LiteralTree newLiteralTree(TextTree text);

    /**
     * Creates a new {@code ParamTree} object, to represent a {@code @param} tag.
     * @param isTypeParameter {@code true} if this is a type parameter, and {@code false} otherwise
     * @param name the parameter being described
     * @param description the description of the parameter
     * @return a {@code ParamTree} object
     */
    ParamTree newParamTree(boolean isTypeParameter, IdentifierTree name, List<? extends DocTree> description);

    /**
     * Creates a new {@code ProvidesTree} object, to represent a {@code @provides} tag.
     * @param name the name of the service type
     * @param description a description of the service being provided
     * @return a {@code ProvidesTree} object
     */
    ProvidesTree newProvidesTree(ReferenceTree name, List<? extends DocTree> description);

    /**
     * Creates a new {@code ReferenceTree} object, to represent a reference to an API element.
     *
     * @param signature the doc comment signature of the reference
     * @return a {@code ReferenceTree} object
     */
    ReferenceTree newReferenceTree(String signature);

    /**
     * Creates a new {@code ReturnTree} object, to represent a {@code @return} tag.
     * @param description the description of the return value of a method
     * @return a {@code ReturnTree} object
     */
    ReturnTree newReturnTree(List<? extends DocTree> description);

    /**
     * Creates a new {@code ReturnTree} object, to represent a {@code @return} tag
     * or {@code {@return}} tag.
     *
     * @param isInline    {@code true} if this instance is as an inline tag,
     *                    and {@code false} otherwise
     * @param description the description of the return value of a method
     *
     * @return a {@code ReturnTree} object
     * @throws UnsupportedOperationException if inline {@code {@return}} tags are
     *                                       not supported
     *
     * @implSpec This implementation throws {@code UnsupportedOperationException} if
     * {@code isInline} is {@code true}, and calls {@link #newReturnTree(List)} otherwise.
     *
     * @since 16
     */
    default ReturnTree newReturnTree(boolean isInline, List<? extends DocTree> description) {
        if (isInline) {
            throw new UnsupportedOperationException();
        }
        return newReturnTree(description);
    }

    /**
     * Creates a new {@code SeeTree} object, to represent a {@code @see} tag.
     * @param reference the reference
     * @return a {@code SeeTree} object
     */
    SeeTree newSeeTree(List<? extends DocTree> reference);

    /**
     * Creates a new {@code SerialTree} object, to represent a {@code @serial} tag.
     * @param description the description for the tag
     * @return a {@code SerialTree} object
     */
    SerialTree newSerialTree(List<? extends DocTree> description);

    /**
     * Creates a new {@code SerialDataTree} object, to represent a {@code @serialData} tag.
     * @param description the description for the tag
     * @return a {@code SerialDataTree} object
     */
    SerialDataTree newSerialDataTree(List<? extends DocTree> description);

    /**
     * Creates a new {@code SerialFieldTree} object, to represent a {@code @serialField} tag.
     * @param name the name of the field
     * @param type the type of the field
     * @param description the description of the field
     * @return a {@code SerialFieldTree} object
     */
    SerialFieldTree newSerialFieldTree(IdentifierTree name, ReferenceTree type, List<? extends DocTree> description);

    /**
     * Creates a new {@code SinceTree} object, to represent a {@code @since} tag.
     * @param text the content of the tag
     * @return a {@code SinceTree} object
     */
    SinceTree newSinceTree(List<? extends DocTree> text);

    /**
     * Creates a new {@code StartElementTree} object, to represent the start of an HTML element.
     * @param name the name of the HTML element
     * @param attrs the attributes
     * @param selfClosing {@code true} if the start element is marked as self-closing; otherwise {@code false}
     * @return a {@code StartElementTree} object
     */
    StartElementTree newStartElementTree(Name name, List<? extends DocTree> attrs, boolean selfClosing);

    /**
     * Creates a new {@code SummaryTree} object, to represent a {@code {@summary }} tag.
     *
     * @implSpec This implementation throws {@code UnsupportedOperationException}.
     *
     * @param summary the content of the tag
     * @return a {@code SummaryTree} object
     * @since 10
     */
    default SummaryTree newSummaryTree(List<? extends DocTree> summary) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Creates a new {@code SystemPropertyTree} object, to represent a {@code {@systemProperty }} tag.
     *
     * @param propertyName the system property name
     * @return a {@code SystemPropertyTree} object
     * @since 12
     */
    SystemPropertyTree newSystemPropertyTree(Name propertyName);

    /**
     * Creates a new {@code TextTree} object, to represent some plain text.
     * @param text the text
     * @return a {@code TextTree} object
     */
    TextTree newTextTree(String text);

    /**
     * Creates a new {@code ThrowsTree} object, to represent a {@code @throws} tag.
     * @param name the name of the exception
     * @param description a description of why the exception might be thrown
     * @return a {@code ThrowsTree} object
     */
    ThrowsTree newThrowsTree(ReferenceTree name, List<? extends DocTree> description);

    /**
     * Creates a new {@code UnknownBlockTagTree} object, to represent an unrecognized block tag.
     * @param name the name of the block tag
     * @param content the content
     * @return an {@code UnknownBlockTagTree} object
     */
    UnknownBlockTagTree newUnknownBlockTagTree(Name name, List<? extends DocTree> content);

    /**
     * Creates a new {@code UnknownInlineTagTree} object, to represent an unrecognized inline tag.
     * @param name the name of the inline tag
     * @param content the content
     * @return an {@code UnknownInlineTagTree} object
     */
    UnknownInlineTagTree newUnknownInlineTagTree(Name name, List<? extends DocTree> content);

    /**
     * Creates a new {@code UsesTree} object, to represent a {@code @uses} tag.
     * @param name the name of the service type
     * @param description a description of how the service will be used
     * @return a {@code UsesTree} object
     */
    UsesTree newUsesTree(ReferenceTree name, List<? extends DocTree> description);

    /**
     * Creates a new {@code ValueTree} object, to represent a {@code {@value }} tag.
     * @param ref a reference to the value
     * @return a {@code ValueTree} object
     */
    ValueTree newValueTree(ReferenceTree ref);

    /**
     * Creates a new {@code VersionTree} object, to represent a {@code {@version }} tag.
     * @param text the content of the tag
     * @return a {@code VersionTree} object
     */
    VersionTree newVersionTree(List<? extends DocTree> text);

    /**
     * Sets the position to be recorded in subsequent tree nodes created by this factory.
     * The position should be a character offset relative to the beginning of the source file
     * or {@link jdkx.tools.Diagnostic#NOPOS NOPOS}.
     * @param pos the position
     * @return this object, to facilitate method chaining
     */
    DocTreeFactory at(int pos);

    /**
     * Gets the first sentence contained in a list of content.
     * The determination of the first sentence is implementation specific, and may
     * involve the use of a locale-specific {@link java.text.BreakIterator BreakIterator}
     * and other heuristics.
     * The resulting list may share a common set of initial items with the input list.
     * @param list the list
     * @return a list containing the first sentence of the list
     */
    List<DocTree> getFirstSentence(List<? extends DocTree> list);

}
