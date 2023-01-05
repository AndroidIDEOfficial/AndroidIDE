// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.xml;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XMLParser}.
 */
public interface XMLParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XMLParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(XMLParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(XMLParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#prolog}.
	 * @param ctx the parse tree
	 */
	void enterProlog(XMLParser.PrologContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#prolog}.
	 * @param ctx the parse tree
	 */
	void exitProlog(XMLParser.PrologContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(XMLParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(XMLParser.ContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(XMLParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(XMLParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#nonEmptyClosingElement}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyClosingElement(XMLParser.NonEmptyClosingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#nonEmptyClosingElement}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyClosingElement(XMLParser.NonEmptyClosingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#emptyClosingElement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyClosingElement(XMLParser.EmptyClosingElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#emptyClosingElement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyClosingElement(XMLParser.EmptyClosingElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#reference}.
	 * @param ctx the parse tree
	 */
	void enterReference(XMLParser.ReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#reference}.
	 * @param ctx the parse tree
	 */
	void exitReference(XMLParser.ReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(XMLParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(XMLParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#prefix}.
	 * @param ctx the parse tree
	 */
	void enterPrefix(XMLParser.PrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#prefix}.
	 * @param ctx the parse tree
	 */
	void exitPrefix(XMLParser.PrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#chardata}.
	 * @param ctx the parse tree
	 */
	void enterChardata(XMLParser.ChardataContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#chardata}.
	 * @param ctx the parse tree
	 */
	void exitChardata(XMLParser.ChardataContext ctx);
	/**
	 * Enter a parse tree produced by {@link XMLParser#misc}.
	 * @param ctx the parse tree
	 */
	void enterMisc(XMLParser.MiscContext ctx);
	/**
	 * Exit a parse tree produced by {@link XMLParser#misc}.
	 * @param ctx the parse tree
	 */
	void exitMisc(XMLParser.MiscContext ctx);
}