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

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced by {@link
 * JavadocParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for operations with no return
 *     type.
 */
public interface JavadocParserVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link JavadocParser#documentation}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDocumentation(JavadocParser.DocumentationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#documentationContent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDocumentationContent(JavadocParser.DocumentationContentContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#skipWhitespace}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSkipWhitespace(JavadocParser.SkipWhitespaceContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#description}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescription(JavadocParser.DescriptionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionLine}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionLine(JavadocParser.DescriptionLineContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionLineStart}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionLineStart(JavadocParser.DescriptionLineStartContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionLineNoSpaceNoAt}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionLineNoSpaceNoAt(JavadocParser.DescriptionLineNoSpaceNoAtContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionLineElement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionLineElement(JavadocParser.DescriptionLineElementContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionLineText}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionLineText(JavadocParser.DescriptionLineTextContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#descriptionNewline}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDescriptionNewline(JavadocParser.DescriptionNewlineContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#tagSection}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTagSection(JavadocParser.TagSectionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#blockTag}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockTag(JavadocParser.BlockTagContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#blockTagName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockTagName(JavadocParser.BlockTagNameContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#blockTagContent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockTagContent(JavadocParser.BlockTagContentContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#blockTagText}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockTagText(JavadocParser.BlockTagTextContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#blockTagTextElement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockTagTextElement(JavadocParser.BlockTagTextElementContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#inlineTag}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInlineTag(JavadocParser.InlineTagContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#inlineTagName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInlineTagName(JavadocParser.InlineTagNameContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#inlineTagContent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInlineTagContent(JavadocParser.InlineTagContentContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#braceExpression}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBraceExpression(JavadocParser.BraceExpressionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#braceContent}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBraceContent(JavadocParser.BraceContentContext ctx);
  /**
   * Visit a parse tree produced by {@link JavadocParser#braceText}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBraceText(JavadocParser.BraceTextContext ctx);
}
