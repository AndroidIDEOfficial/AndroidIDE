// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.javadoc;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavadocParser}.
 */
public interface JavadocParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JavadocParser#documentation}.
	 * @param ctx the parse tree
	 */
	void enterDocumentation(JavadocParser.DocumentationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#documentation}.
	 * @param ctx the parse tree
	 */
	void exitDocumentation(JavadocParser.DocumentationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#documentationContent}.
	 * @param ctx the parse tree
	 */
	void enterDocumentationContent(JavadocParser.DocumentationContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#documentationContent}.
	 * @param ctx the parse tree
	 */
	void exitDocumentationContent(JavadocParser.DocumentationContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#skipWhitespace}.
	 * @param ctx the parse tree
	 */
	void enterSkipWhitespace(JavadocParser.SkipWhitespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#skipWhitespace}.
	 * @param ctx the parse tree
	 */
	void exitSkipWhitespace(JavadocParser.SkipWhitespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(JavadocParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(JavadocParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionLine}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionLine(JavadocParser.DescriptionLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionLine}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionLine(JavadocParser.DescriptionLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionLineStart}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionLineStart(JavadocParser.DescriptionLineStartContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionLineStart}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionLineStart(JavadocParser.DescriptionLineStartContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionLineNoSpaceNoAt}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionLineNoSpaceNoAt(JavadocParser.DescriptionLineNoSpaceNoAtContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionLineNoSpaceNoAt}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionLineNoSpaceNoAt(JavadocParser.DescriptionLineNoSpaceNoAtContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionLineElement}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionLineElement(JavadocParser.DescriptionLineElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionLineElement}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionLineElement(JavadocParser.DescriptionLineElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionLineText}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionLineText(JavadocParser.DescriptionLineTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionLineText}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionLineText(JavadocParser.DescriptionLineTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#descriptionNewline}.
	 * @param ctx the parse tree
	 */
	void enterDescriptionNewline(JavadocParser.DescriptionNewlineContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#descriptionNewline}.
	 * @param ctx the parse tree
	 */
	void exitDescriptionNewline(JavadocParser.DescriptionNewlineContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#tagSection}.
	 * @param ctx the parse tree
	 */
	void enterTagSection(JavadocParser.TagSectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#tagSection}.
	 * @param ctx the parse tree
	 */
	void exitTagSection(JavadocParser.TagSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#blockTag}.
	 * @param ctx the parse tree
	 */
	void enterBlockTag(JavadocParser.BlockTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#blockTag}.
	 * @param ctx the parse tree
	 */
	void exitBlockTag(JavadocParser.BlockTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#blockTagName}.
	 * @param ctx the parse tree
	 */
	void enterBlockTagName(JavadocParser.BlockTagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#blockTagName}.
	 * @param ctx the parse tree
	 */
	void exitBlockTagName(JavadocParser.BlockTagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#blockTagContent}.
	 * @param ctx the parse tree
	 */
	void enterBlockTagContent(JavadocParser.BlockTagContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#blockTagContent}.
	 * @param ctx the parse tree
	 */
	void exitBlockTagContent(JavadocParser.BlockTagContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#blockTagText}.
	 * @param ctx the parse tree
	 */
	void enterBlockTagText(JavadocParser.BlockTagTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#blockTagText}.
	 * @param ctx the parse tree
	 */
	void exitBlockTagText(JavadocParser.BlockTagTextContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#blockTagTextElement}.
	 * @param ctx the parse tree
	 */
	void enterBlockTagTextElement(JavadocParser.BlockTagTextElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#blockTagTextElement}.
	 * @param ctx the parse tree
	 */
	void exitBlockTagTextElement(JavadocParser.BlockTagTextElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#inlineTag}.
	 * @param ctx the parse tree
	 */
	void enterInlineTag(JavadocParser.InlineTagContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#inlineTag}.
	 * @param ctx the parse tree
	 */
	void exitInlineTag(JavadocParser.InlineTagContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#inlineTagName}.
	 * @param ctx the parse tree
	 */
	void enterInlineTagName(JavadocParser.InlineTagNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#inlineTagName}.
	 * @param ctx the parse tree
	 */
	void exitInlineTagName(JavadocParser.InlineTagNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#inlineTagContent}.
	 * @param ctx the parse tree
	 */
	void enterInlineTagContent(JavadocParser.InlineTagContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#inlineTagContent}.
	 * @param ctx the parse tree
	 */
	void exitInlineTagContent(JavadocParser.InlineTagContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#braceExpression}.
	 * @param ctx the parse tree
	 */
	void enterBraceExpression(JavadocParser.BraceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#braceExpression}.
	 * @param ctx the parse tree
	 */
	void exitBraceExpression(JavadocParser.BraceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#braceContent}.
	 * @param ctx the parse tree
	 */
	void enterBraceContent(JavadocParser.BraceContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#braceContent}.
	 * @param ctx the parse tree
	 */
	void exitBraceContent(JavadocParser.BraceContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavadocParser#braceText}.
	 * @param ctx the parse tree
	 */
	void enterBraceText(JavadocParser.BraceTextContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavadocParser#braceText}.
	 * @param ctx the parse tree
	 */
	void exitBraceText(JavadocParser.BraceTextContext ctx);
}