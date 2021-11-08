package com.itsaky.androidide.ui.parser;

import androidx.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceTableFactory {
    
    @Nullable
    public static ResourceTable newFrameworkResourceTable () {
        final ResourceTable table = new ResourceTable ("android");
        final Class<?> androidR = android.R.class;
        try {
            processResources(table, androidR);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
        return table;
    }

    private static void processResources(ResourceTable table, Class<?> rClass) throws IllegalArgumentException, IllegalAccessException {
        addRClassValues(table, rClass);
        addMissingStyleableAttributes(table, rClass);
    }
    
    private static void addRClassValues(ResourceTable resourceTable, Class<?> rClass) throws IllegalArgumentException, IllegalAccessException {
        for (Class<?> innerClass : rClass.getClasses()) {
            String resourceType = innerClass.getSimpleName();
            if (!resourceType.equals("styleable")) {
                for (Field field : innerClass.getDeclaredFields()) {
                    if (field.getType().equals(Integer.TYPE) && Modifier.isStatic(field.getModifiers())) {
                        int id = field.getInt(null);
                        String resourceName = field.getName();
                        if (id == 0) {
                            continue;
                        }
                        resourceTable.addResource(id, resourceType, resourceName);
                    }
                }
            }
        }
    }
    
    private static void addMissingStyleableAttributes (ResourceTable table, Class<?> rClass) throws IllegalArgumentException, IllegalAccessException {
        final Map<String, List<Integer>> styleableMap = new HashMap<>();
        for (Class<?> innerClass : rClass.getClasses()) {
            if ("styleable".equals(innerClass.getSimpleName())) {
                for (Field attr : innerClass.getDeclaredFields()) {
                    final Class<?> attrClassType = attr.getType();
                    if (attrClassType == int[].class) { // <declare-styleable> declaration
                        addStyleableGroup (styleableMap, attr);
                    } else if (attrClassType == int.class) { // Member of <declare-styleable>
                        addStyleableMember (table, styleableMap, attr);
                    }
                }
                break;
            }
        }
    }

    private static void addStyleableGroup(Map<String, List<Integer>> styleableMap, Field attr) throws IllegalArgumentException, IllegalAccessException {
        final String name = attr.getName();
        List<Integer> members = styleableMap.get(name);
        if (members == null) {
            members = new ArrayList<>();
        }
        final int[] mm = (int[]) attr.get(null);
        if (mm == null || mm.length <= 0) {
            return;
        }
        
        for (int i=0;i<mm.length;i++) {
            members.add(mm[i]);
        }
        
        styleableMap.put(name, members);
    }

    private static void addStyleableMember(ResourceTable table, Map<String, List<Integer>> styleableMap, Field attr) throws IllegalArgumentException, IllegalAccessException {
        final String name = attr.getName();
        final int separator = findGroupAndAttrNameSeparator (styleableMap, name); // If this is a <declare-styleable> member, then the name will definitely contain am underscore
        final String groupName = name.substring(0, separator);
        final String attrName = name.substring(separator + 1);
        
        final List<Integer> members = styleableMap.get(groupName);
        if (members == null) {
            throw new IllegalStateException ("No styleable declared for " + groupName + "\nstyleableMap = " + styleableMap); // No entry for this name
        }
        
        final int index = (int) attr.get(null);
        if (index < 0 || index >= members.size()) {
            throw new IndexOutOfBoundsException ("Index " + index + " is out of bounds (size=" + members.size() + "). name=" + name + ", grpName=" + groupName + ", attrName=" + attrName);
        }
        
        table.addResource(members.get(index), "attr", attrName);
    }

    private static int findGroupAndAttrNameSeparator(Map<String, List<Integer>> styleableMap, String name) {
        int index = name.length() - 1;
        
         while (index > 0) {
             // LinearLayout_Layout_layout_gravity
             int i = name.lastIndexOf("_", index);
             String group = name.substring(0, i);
             List<Integer> mm = styleableMap.get(group);
             if (mm != null) {
                 return i;
             } else {
                 index = i - 1;
             }
         }
        
        return -1;
    }
    
    private static void addMissingStyleableAttributes2(ResourceTable resourceTable, Class<?> rClass) throws IllegalArgumentException, IllegalAccessException {
        for (Class<?> innerClass : rClass.getClasses()) {
            if (innerClass.getSimpleName().equals("styleable")) {
                String styleableName = null;
                int[] styleableArray = null;
                for (Field field : innerClass.getDeclaredFields()) {
                    if (field.getType().equals(int[].class) && Modifier.isStatic(field.getModifiers())) {
                        styleableName = field.getName();
                        styleableArray = (int[]) (field.get(null));
                    } else if (field.getType().equals(Integer.TYPE) && Modifier.isStatic(field.getModifiers())) {
                        String attributeName = field.getName().substring(styleableName.length() + 1);
                        int styleableIndex = field.getInt(null);
                        int attributeResId = styleableArray[styleableIndex];
                        resourceTable.addResource(attributeResId, "attr", attributeName);
                    }
                }
            }
        }
    }
}
