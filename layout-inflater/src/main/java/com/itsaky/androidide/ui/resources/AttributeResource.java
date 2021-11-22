/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.ui.resources;

import androidx.annotation.NonNull;
import java.util.regex.Pattern;

public class AttributeResource {
    public static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";
    public static final String ANDROID_RES_NS_PREFIX = "http://schemas.android.com/apk/res/";
    public static final String RES_AUTO_NS_URI = "http://schemas.android.com/apk/res-auto";

    public static final String NULL_VALUE = "@null";
    public static final String EMPTY_VALUE = "@empty";
    public static final Pattern IS_RESOURCE_REFERENCE = Pattern.compile("^\\s*@");

    public final @NonNull ResName resName;
    public final @NonNull String value;
    public final @NonNull String trimmedValue;
    public final @NonNull String contextPackageName;
    private final Integer referenceResId;

    public AttributeResource(@NonNull ResName resName, @NonNull String value, @NonNull String contextPackageName) {
        this(resName, value, contextPackageName, null);
    }

    public AttributeResource(@NonNull ResName resName, @NonNull String value, @NonNull String contextPackageName, Integer referenceResId) {
        this.referenceResId = referenceResId;
        if (!resName.type.equals("attr")) throw new IllegalStateException("\"" + resName.getFullyQualifiedName() + "\" unexpected");

        this.resName = resName;
        this.value = value;
        this.trimmedValue = value.trim();
        this.contextPackageName = contextPackageName;
    }

    public boolean isResourceReference() {
        return isResourceReference(trimmedValue);
    }

    public @NonNull ResName getResourceReference() {
        if (!isResourceReference()) throw new RuntimeException("not a resource reference: " + this);
        return ResName.qualifyResName(deref(trimmedValue).replace("+", ""), contextPackageName, "style");
    }

    public boolean isStyleReference() {
        return isStyleReference(trimmedValue);
    }

    public ResName getStyleReference() {
        if (!isStyleReference()) throw new RuntimeException("not a style reference: " + this);
        return ResName.qualifyResName(value.substring(1), contextPackageName, "attr");
    }

    public boolean isNull() {
        return NULL_VALUE.equals(trimmedValue);
    }

    public boolean isEmpty() {
        return EMPTY_VALUE.equals(trimmedValue);
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "name='" + resName + '\'' +
            ", value='" + value + '\'' +
            ", contextPackageName='" + contextPackageName + '\'' +
            '}';
    }

    public static boolean isResourceReference(String value) {
        return IS_RESOURCE_REFERENCE.matcher(value).find() && !isNull(value);
    }

    public static @NonNull ResName getResourceReference(String value, String defPackage, String defType) {
        if (!isResourceReference(value)) throw new IllegalArgumentException("not a resource reference: " + value);
        return ResName.qualifyResName(deref(value).replace("+", ""), defPackage, defType);
    }

    private static @NonNull String deref(@NonNull String value) {
        return value.substring(value.indexOf('@') + 1);
    }

    public static boolean isStyleReference(String value) {
        return value.startsWith("?");
    }

    public static ResName getStyleReference(String value, String defPackage, String defType) {
        if (!isStyleReference(value)) throw new IllegalArgumentException("not a style reference: " + value);
        return ResName.qualifyResName(value.substring(1), defPackage, defType);
    }

    public static boolean isNull(String value) {
        return NULL_VALUE.equals(value);
    }

    public static boolean isEmpty(String value) {
        return EMPTY_VALUE.equals(value);
    }

    public Integer getReferenceResId() {
        return referenceResId;
    }
}
