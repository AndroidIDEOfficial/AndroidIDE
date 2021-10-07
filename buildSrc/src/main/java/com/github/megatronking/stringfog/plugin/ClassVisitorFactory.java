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


import com.github.megatronking.stringfog.IStringFog;
import com.github.megatronking.stringfog.plugin.utils.Log;
import com.github.megatronking.stringfog.plugin.utils.TextUtils;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * A factory creates {@link ClassVisitor}.
 *
 * @author Megatron King
 * @since 2017/3/7 19:56
 */

public final class ClassVisitorFactory {

    private ClassVisitorFactory() {
    }

    public static ClassVisitor create(IStringFog stringFogImpl, StringFogMappingPrinter mappingPrinter,
                                      String[] fogPackages, String key, String fogClassName,
                                      String className, ClassWriter cw) {
        if (WhiteLists.inWhiteList(className) || !isInFogPackages(fogPackages, className)) {
            Log.v("StringFog ignore: " + className);
            return createEmpty(cw);
        }
        Log.v("StringFog execute: " + className);
        return new StringFogClassVisitor(stringFogImpl, mappingPrinter, fogClassName, key, cw);
    }

    private static ClassVisitor createEmpty(ClassWriter cw) {
        return new ClassVisitor(Opcodes.ASM5, cw) {
        };
    }

    private static boolean isInFogPackages(String[] fogPackages, String className) {
        if (TextUtils.isEmpty(className)) {
            return false;
        }
        if (fogPackages == null || fogPackages.length == 0) {
            // default we fog all packages.
            return true;
        }
        for (String fogPackage : fogPackages) {
            if (className.replace('/', '.').startsWith(fogPackage + ".")) {
                return true;
            }
        }
        return false;
    }

}
