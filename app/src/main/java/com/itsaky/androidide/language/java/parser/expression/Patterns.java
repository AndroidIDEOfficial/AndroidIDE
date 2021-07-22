package com.itsaky.androidide.language.java.parser.expression;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * Created by Duy on 11-Feb-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Patterns {

    public static final Pattern JAVA_COMMENTS = compile("(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)");
    /**
     * match string java
     * include
     * <p>
     * 'string'
     * <p>
     * And can not find close quote
     * <p>
     * 'sadhasdhasdhashdhas ds asda sd
     */
    public static final Pattern STRINGS = Pattern.compile(
            "((\")(.*?)(\"))" +//'string'
                    "|((\")(.+))", Pattern.DOTALL); // no end string 'asdasdasd


    public static final Pattern PRIMITIVE_TYPES = compile("boolean|byte|char|int|short|long|float|double");
    public static final Pattern KEYWORD_TYPES = compile("class|interface|enum");
    public static final Pattern KEYWORDS_MODS = compile("public|private|protected|static|final|" +
            "synchronized|volatile|transient|native|strictfp|abstract");
    public static final Pattern KEYWORDS = compile(PRIMITIVE_TYPES.pattern()
            + KEYWORDS_MODS.pattern() + "|"
            + KEYWORD_TYPES.pattern() + "|" +
            "super|this|void|assert|break|case|catch|" +
            "const|continue|default|do|else|extends|finally|for|goto|if|implements|import|instanceof|" +
            "interface|new|package|return|switch|throw|throws|try|while|true|false|null");

    public static final Pattern RE_BRACKETS = compile("(\\s*\\[\\s*\\])");

    public static final Pattern IDENTIFIER = compile("[a-zA-Z_][a-zA-Z0-9_]*");
    //case:  JCTree.JCClassDecl
    //case: java.util.ArrayList)
    public static final Pattern RE_QUALID = compile(IDENTIFIER + "(\\s*\\.\\s*" + IDENTIFIER + ")?");
    public static final Pattern RE_REFERENCE_TYPE = compile(RE_QUALID + RE_BRACKETS.pattern() + "\\*");
    public static final Pattern RE_TYPE = RE_REFERENCE_TYPE;

    public static final Pattern TYPE_DECLARE_MODIFIERS = compile("public|protected|private|abstract|static|final|strictfp");
    public static final Pattern TYPE_DECLARE_HEAD = compile("class|inteface|enum");

    //case: public class A
    //case: public class Lamborghini extends Car
    public static final Pattern RE_TYPE_DECL = compile(
            "((public|protected|private|abstract|static|final|strictfp)\\s+)*" +
                    "(class|inteface|enum)\\s+" +
                    "[a-zA-Z_][a-zA-Z0-9_]*" +
                    "(\\s+(extends|implements)(\\s+)([a-zA-Z_][a-zA-Z0-9_]*))?");

    public static final Pattern RE_ARRAY_TYPE = compile("^\\s*(" + RE_QUALID + ")(" + RE_BRACKETS + ")+\\s*");
    public static final Pattern RE_SELECT_OR_ACCESS = compile("^\\s*(" + IDENTIFIER + ")\\s*(\\[.*])?\\s*$");
    public static final Pattern RE_ARRAY_ACCESS = compile("^\\s*(" + IDENTIFIER + ")" + "\\s*(\\[.*])+\\s*$");
    public static final Pattern RE_CASTING = compile("\\s*\\((" + RE_QUALID + "\\s*)\\)\\s*" + IDENTIFIER);
    public static final Pattern RE_KEYWORDS = KEYWORDS;

    public static final Pattern PACKAGE_NAME = compile("([A-Za-z0-9_]([.][a-zA-Z0-9_])?)*");
}
