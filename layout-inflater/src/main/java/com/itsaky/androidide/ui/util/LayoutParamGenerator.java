package com.itsaky.androidide.ui.util;
import android.view.ViewGroup;
import java.lang.reflect.Method;

public class LayoutParamGenerator {
    
    public static ViewGroup.LayoutParams generateParams (ViewGroup parent) {
        try {
            final Class<?> clazz = parent.getClass();
            final Method method = clazz.getDeclaredMethod("generateDefaultLayoutParams");
            method.setAccessible(true);
            return (ViewGroup.LayoutParams) method.invoke(parent);
        } catch (Throwable th) {
            return null;
        }
    }
}
