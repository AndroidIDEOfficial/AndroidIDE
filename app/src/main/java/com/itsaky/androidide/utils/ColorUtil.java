package com.itsaky.androidide.utils;

import android.graphics.Color;
import android.util.Log;

import androidx.core.graphics.ColorUtils;

import com.itsaky.androidide.app.StudioApp;

import java.lang.reflect.Field;

public class ColorUtil {

    public static boolean isDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.25;
    }

    public static Object getAndroidColor(String clazz, String colorName) {
        Field declaratedField;
        try {
            declaratedField = Class.forName(clazz).getField(colorName);
            declaratedField.setAccessible(true);
            return declaratedField.get(null);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getColor(String colorValue) {
        /*
        if (colorValue.startsWith("@color/")) {
            final var resTable = StudioApp.getInstance().getResourceTable();
            String color = resTable.findColor(colorValue);
            return getColor(color);
        }
        */
        if (colorValue.startsWith("@android:color/")) {
            Object color = getAndroidColor("android.R$color", colorValue.substring(15));
            if (color != null) {
                return StudioApp.getInstance().getColor((Integer) color);
            }
        }
        if (colorValue.startsWith("#")) {
            return Color.parseColor(colorValue);
        }
        throw new NumberFormatException();
    }
    
    public static boolean isColorValue(String value){
        if(value.startsWith("#") | value.startsWith("@android:color")){
            return true;
        }
        return false;
        }
}
