package com.github.megatronking.stringfog.plugin.utils;

public class Log {

    private static boolean isDebug;

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void v(String msg) {
        if (isDebug) {
            System.out.println(msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            System.err.println(msg);
        }
    }

}
