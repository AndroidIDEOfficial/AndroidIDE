/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.itsaky.androidide.zipfs2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author itsaky
 */
public class Collections2 {
    
    public static <T> List<T> listOf(T... values) {
        final List<T> result = new ArrayList<>();
        if (values == null) {
            return result;
        }
        result.addAll(Arrays.asList(values));
        return result;
    }
    
    public static <T> Set<T> setOf(T... values) {
        final Set<T> result = new HashSet<>();
        if (values == null) {
            return result;
        }
        result.addAll(Arrays.asList(values));
        return result;
    }
}
