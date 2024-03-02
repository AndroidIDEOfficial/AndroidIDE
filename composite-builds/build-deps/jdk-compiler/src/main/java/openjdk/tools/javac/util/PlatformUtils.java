package openjdk.tools.javac.util;

public class PlatformUtils {
    public static boolean isAndroid () {
        try {
            Class.forName("android.content.Context");
            return true;
        } catch (ClassNotFoundException eThrowable) {
            return false;
        }
    }    
}