/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.itsaky.androidide.javac.wrappers;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.tools.ToolProvider;

public class ServiceLoaderWrapper<T> implements Iterable<T> {
    private final ServiceLoader<T> loader;

    public ServiceLoaderWrapper(ServiceLoader<T> loader) {
        this.loader = loader;
    }

    public static <T> ServiceLoader<T> load(ModuleWrapper.ModuleLayer layer, Class<T> aClass) {
        ModuleWrapper.ensureUses(aClass);
        return ServiceLoader.load(aClass); //XXX
    }

    public static <T> ServiceLoaderWrapper<T> load(Class<T> aClass) {
        ModuleWrapper.ensureUses(aClass);
        return new ServiceLoaderWrapper<T>(ServiceLoader.load(aClass));
    }

    public static <T> ServiceLoaderWrapper<T> load(Class<T> aClass, ClassLoader classLoader) {
        ModuleWrapper.ensureUses(aClass);
        return new ServiceLoaderWrapper<T>(ServiceLoader.load(aClass, classLoader));
    }

    public static <T> ServiceLoader<T> loadWithClassLoader(Class<T> aClass, ClassLoader classLoader) {
        ModuleWrapper.ensureUses(aClass);
        return ServiceLoader.load(aClass, classLoader);
    }

    public static <T> ServiceLoader<T> loadTool(Class<T> toolClass) {
        ModuleWrapper.ensureUses(toolClass);
        ServiceLoader<T> res = ServiceLoader.load(toolClass, ToolProvider.class.getClassLoader());
        if (res.iterator().hasNext()) {
            return res;
        }
        return ServiceLoader.load(toolClass, ClassLoader.getSystemClassLoader());
    }

    public Stream<Provider<T>> stream() {
        return StreamSupport.stream(loader.spliterator(), false)
                             .map(v -> new Provider<T>() {
            @Override
            public Class<? extends T> type() {
                return (Class<T>) v.getClass();
            }

            @Override
            public T get() {
                return v;
            }
                             });
    }

    @Override
    public Iterator<T> iterator() {
        return loader.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        loader.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return loader.spliterator();
    }

    public interface Provider<T> {
        public Class<? extends T> type();
        public T get();
    }

}
