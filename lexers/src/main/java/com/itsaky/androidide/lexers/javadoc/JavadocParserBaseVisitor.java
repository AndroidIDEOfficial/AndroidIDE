/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
// Generated from JavadocParser.g4 by ANTLR 4.9.2
package com.itsaky.androidide.antlr4.javadoc;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**
 * This class provides an empty implementation of {@link JavadocParserVisitor}, which can be
 * extended to create a visitor which only needs to handle a subset of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for operations with no return
 *     type.
 */
public class JavadocParserBaseVisitor<T> extends AbstractParseTreeVisitor<T>
    implements JavadocParserVisitor<T> {
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDocumentation(JavadocParser.DocumentationContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDocumentationContent(JavadocParser.DocumentationContentContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitSkipWhitespace(JavadocParser.SkipWhitespaceContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescription(JavadocParser.DescriptionContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionLine(JavadocParser.DescriptionLineContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionLineStart(JavadocParser.DescriptionLineStartContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionLineNoSpaceNoAt(
      JavadocParser.DescriptionLineNoSpaceNoAtContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionLineElement(JavadocParser.DescriptionLineElementContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionLineText(JavadocParser.DescriptionLineTextContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitDescriptionNewline(JavadocParser.DescriptionNewlineContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitTagSection(JavadocParser.TagSectionContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBlockTag(JavadocParser.BlockTagContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBlockTagName(JavadocParser.BlockTagNameContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBlockTagContent(JavadocParser.BlockTagContentContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBlockTagText(JavadocParser.BlockTagTextContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBlockTagTextElement(JavadocParser.BlockTagTextElementContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitInlineTag(JavadocParser.InlineTagContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitInlineTagName(JavadocParser.InlineTagNameContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitInlineTagContent(JavadocParser.InlineTagContentContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBraceExpression(JavadocParser.BraceExpressionContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBraceContent(JavadocParser.BraceContentContext context) {
    return visitChildren(context);
  }
  /**
   * {@inheritDoc}
   *
   * <p>The default implementation returns the result of calling {@link #visitChildren} on {@code
   * context}.
   */
  @Override
  public T visitBraceText(JavadocParser.BraceTextContext context) {
    return visitChildren(context);
  }
}
