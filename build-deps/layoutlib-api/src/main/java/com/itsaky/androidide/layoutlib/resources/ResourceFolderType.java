/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itsaky.androidide.layoutlib.resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing a type of resource folder.
 */
public enum ResourceFolderType {
    ANIM(ResourceConstants.FD_RES_ANIM),
    ANIMATOR(ResourceConstants.FD_RES_ANIMATOR),
    COLOR(ResourceConstants.FD_RES_COLOR),
    DRAWABLE(ResourceConstants.FD_RES_DRAWABLE),
    FONT(ResourceConstants.FD_RES_FONT),
    INTERPOLATOR(ResourceConstants.FD_RES_INTERPOLATOR),
    LAYOUT(ResourceConstants.FD_RES_LAYOUT),
    MENU(ResourceConstants.FD_RES_MENU),
    MIPMAP(ResourceConstants.FD_RES_MIPMAP),
    NAVIGATION(ResourceConstants.FD_NAVIGATION),
    RAW(ResourceConstants.FD_RES_RAW),
    TRANSITION(ResourceConstants.FD_RES_TRANSITION),
    VALUES(ResourceConstants.FD_RES_VALUES),
    XML(ResourceConstants.FD_RES_XML);

    private final String name;

    private static final Map<String, ResourceFolderType> nameToType;

    static {
        ResourceFolderType[] values = ResourceFolderType.values();
        nameToType = new HashMap<>(2 * values.length);

        for (ResourceFolderType type : values) {
            nameToType.put(type.getName(), type);
        }
    }

    ResourceFolderType(String name) {
        this.name = name;
    }

    /**
     * Returns the folder name for this resource folder type.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Returns the enum by name.
     * @param name The enum string value.
     * @return the enum or null if not found.
     */
    @Nullable
    public static ResourceFolderType getTypeByName(@NonNull String name) {
        assert name.indexOf('-') == -1 : name; // use #getFolderType instead
        return nameToType.get(name);
    }

    /**
     * Returns the {@link ResourceFolderType} from the folder name
     * @param folderName The name of the folder. This must be a valid folder name in the format
     * <code>resType[-resqualifiers[-resqualifiers[...]]</code>
     * @return the <code>ResourceFolderType</code> representing the type of the folder, or
     * <code>null</code> if no matching type was found.
     */
    @Nullable
    public static ResourceFolderType getFolderType(@NonNull String folderName) {
        int index = folderName.indexOf(ResourceConstants.RES_QUALIFIER_SEP);
        if (index != -1) {
            folderName = folderName.substring(0, index);
        }
        return getTypeByName(folderName);
    }
}
