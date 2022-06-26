package com.sun.tools.javac.util;

public class PlatformUtils {
    public static boolean isAndroid () {
        try {
            Class.forName("com.itsaky.androidide.utils.Environment");
            return true;
        } catch (ClassNotFoundException eThrowable) {
            System.err.println("This version of javac is made specifically made for running on Android.");
            System.err.println("It may not work properly on other systems.");
            return false;
        }
    }    
}