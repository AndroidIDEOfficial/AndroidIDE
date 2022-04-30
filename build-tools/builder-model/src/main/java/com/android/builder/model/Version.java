/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.builder.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * @deprecated use com.android.Version instead
 *     <p>TODO: remove (along with the associated version.properties) once it's no longer used by
 *     the gradle build scan plugin
 */
@Deprecated
public final class Version {
    public static final String ANDROID_GRADLE_PLUGIN_VERSION;
    public static final String ANDROID_TOOLS_BASE_VERSION;
    public static final int BUILDER_MODEL_API_VERSION;
    public static final int BUILDER_NATIVE_MODEL_API_VERSION;

    static {
        Properties properties = new Properties();
        InputStream stream =
                new BufferedInputStream(Version.class.getResourceAsStream("version.properties"));
        try {
            try {
                properties.load(stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        ANDROID_GRADLE_PLUGIN_VERSION = properties.getProperty("buildVersion");
        ANDROID_TOOLS_BASE_VERSION = properties.getProperty("baseVersion");
        BUILDER_MODEL_API_VERSION = Integer.parseInt(properties.getProperty("apiVersion"));
        BUILDER_NATIVE_MODEL_API_VERSION =
                Integer.parseInt(properties.getProperty("nativeApiVersion"));
    }

    private Version() {}
}
