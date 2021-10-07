/*
 * Copyright (C) 2017, Megatron King
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.megatronking.stringfog.plugin;

import com.github.megatronking.stringfog.plugin.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The white list contains some ignored levels. We defined some popular
 * library domains and classes which must be ignored when executing string fog.
 *
 * @author Megatron King
 * @since 2017/3/7 19:34
 */

final class WhiteLists {

    private static final List<String> CLASS_WHITE_LIST = new ArrayList<>();

    static {
        // default classes short name in white list.
        addWhiteList("BuildConfig");
        addWhiteList("R");
        addWhiteList("R2");
        addWhiteList("StringFog");
    }

    static boolean inWhiteList(String name) {
        return !TextUtils.isEmpty(name) && checkClass(shortClassName(name));
    }

    private WhiteLists() {
    }

    private static void addWhiteList(String name) {
        CLASS_WHITE_LIST.add(name);
    }

    private static boolean checkClass(String name) {
        for (String className : CLASS_WHITE_LIST) {
            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }

    private static String trueClassName(String className) {
        return className.replace('/', '.');
    }

    private static String shortClassName(String className) {
        String[] spiltArrays = trueClassName(className).split("[.]");
        return spiltArrays[spiltArrays.length - 1];
    }

}
