package com.itsaky.androidide.language.java.parser.expression;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFactory {
    public static final Pattern PACKAGE = Pattern.compile("package\\s+[^;]*;");
    public static final Pattern PACKAGE_NAME = Pattern.compile("[A-Za-z_]+(.[A-Za-z][A-Za-z0-9_]*)*");
    public static final Pattern IMPORT = Pattern.compile("(import\\s+)([^;]*)(\\s?);");
    public static final Pattern WORD = Pattern.compile("[^\\s-]+$");

    public static final String IDENTIFIER_STR = "[A-Za-z][A-Za-z0-9]*";
    public static final Pattern IDENTIFIER = Pattern.compile(IDENTIFIER_STR);

    public static final Pattern ANNOTATION = Pattern.compile("@[A-Za-z][A-Za-z0-9]*");
    public static final Pattern BRACKET = Pattern.compile("\\[(.*?)\\]");
    public static final Pattern MODIFIERS = Pattern.compile("\\b(public|protected|private|abstract|static|final|strictfp)\\b");
    public static final Pattern SPLIT_NON_WORD = Pattern.compile("\\W+");
    public static final String SPLIT_NON_WORD_STR = "\\W+";
    public static final String[] PRIMITIVE_TYPE = new String[]{"boolean", "byte", "char", "int",
            "short", "long", "float", "double"};
    public static final String[] KEYWORD_MODIFIERS = new String[]{"public", "private", "protected",
            "static", "final", "synchronized", "volatile", "transient", "native", "strictfp"};
    public static final String[] KEYWORD_TYPE = new String[]{"class", "interface", "enum"};
    public static final String[] KEYWORD;

    public static final String GENERIC_STR = "<[A-Z][a-zA-Z0-9_<>, ]*>";

    public static final Pattern FILE_NAME = Pattern.compile("[A-Za-z][A-Za-z0-9_\\-.]*");

    static {
        KEYWORD = new String[PRIMITIVE_TYPE.length + KEYWORD_MODIFIERS.length + KEYWORD_TYPE.length];
        System.arraycopy(PRIMITIVE_TYPE, 0, KEYWORD, 0, PRIMITIVE_TYPE.length);
        System.arraycopy(KEYWORD_MODIFIERS, 0, KEYWORD, PRIMITIVE_TYPE.length, KEYWORD_MODIFIERS.length);
        System.arraycopy(KEYWORD_TYPE, 0, KEYWORD, KEYWORD_MODIFIERS.length, KEYWORD_TYPE.length);
    }

    public static Pattern makeImportClass(String className) {
        return Pattern.compile("(import\\s+)(.*" + className + ")(\\s?;)");
    }

    @Nullable
    public static String lastMatchStr(CharSequence text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        if (list.size() == 0) return null;
        return list.get(list.size() - 1);
    }

    @Nullable
    public static String match(CharSequence text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) return matcher.group();
        else return null;
    }

    public static ArrayList<String> allMatch(CharSequence text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }


    public static int lastMatch(CharSequence editor, Pattern pattern) {
        int last = -1;
        Matcher matcher = pattern.matcher(editor);
        while (matcher.find()) last = matcher.end();
        return last;
    }

    public static int matchEnd(CharSequence editor, Pattern pattern, int start) {
        Matcher matcher = pattern.matcher(editor);
        if (matcher.find(start)) {
            return matcher.end();
        }
        return -1;
    }

    public static int firstMatch(CharSequence editor, Pattern pattern) {
        Matcher matcher = pattern.matcher(editor);
        if (matcher.find()) {
            return matcher.start();
        }
        return -1;
    }

    public static Pattern makeInstance(String prefix) {
        //ArrayList<String> list = new ArrayList();
        return Pattern.compile("(" + IDENTIFIER_STR + ")" + "(\\s?)" + //type
                "((" + GENERIC_STR + ")?(\\s?)|(\\s+))" + //generic or space
                "(" + prefix + ")(\\s?)([,;=)])"); //name
    }

    public static int lastMatch(String statement, String pattern) {
        return lastMatch(statement, Pattern.compile(pattern));
    }

    public static int lastMatch(String statement, Pattern pattern) {
        return lastMatch(statement, pattern, 0);
    }

    public static int lastMatch(String expr, Pattern pattern, int start) {
        Matcher matcher = pattern.matcher(expr.substring(start));
        int index = -1;
        while (matcher.find()) {
            index = matcher.end();
        }
        return index != 0 ? index + start : index;
    }
}
