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
import com.github.megatronking.stringfog.StringFogWrapper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class StringFogClassInjector {

    private String[] mFogPackages;
    private String mFogClassName;
    private String mKey;
    private IStringFog mStringFogImpl;
    private StringFogMappingPrinter mMappingPrinter;

    public StringFogClassInjector(String[] fogPackages, String key, String implementation,
                                  String fogClassName, StringFogMappingPrinter mappingPrinter) {
        this.mFogPackages = fogPackages;
        this.mKey = key;
        this.mStringFogImpl = new StringFogWrapper(implementation);
        this.mFogClassName = fogClassName;
        this.mMappingPrinter = mappingPrinter;
    }

    public void doFog2Class(File fileIn, File fileOut) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(fileIn));
            os = new BufferedOutputStream(new FileOutputStream(fileOut));
            processClass(is, os);
        } finally {
            closeQuietly(os);
            closeQuietly(is);
        }
    }

    public void doFog2Jar(File jarIn, File jarOut) throws IOException {
        try {
            processJar(jarIn, jarOut, Charset.forName("UTF-8"), Charset.forName("UTF-8"));
        } catch (IllegalArgumentException e) {
            if ("MALFORMED".equals(e.getMessage())) {
                processJar(jarIn, jarOut, Charset.forName("GBK"), Charset.forName("UTF-8"));
            } else {
                throw e;
            }
        }
    }

    @SuppressWarnings("NewApi")
    private void processJar(File jarIn, File jarOut, Charset charsetIn, Charset charsetOut) throws IOException {
        boolean shouldExclude = shouldExcludeJar(jarIn, charsetIn);
        ZipInputStream zis = null;
        ZipOutputStream zos = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(jarIn)), charsetIn);
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(jarOut)), charsetOut);
            ZipEntry entryIn;
            Map<String, Integer> processedEntryNamesMap = new HashMap<>();
            while ((entryIn = zis.getNextEntry()) != null) {
                final String entryName = entryIn.getName();
                if (!processedEntryNamesMap.containsKey(entryName)) {
                    ZipEntry entryOut = new ZipEntry(entryIn);
                    // Set compress method to default, fixed #12
                    if (entryOut.getMethod() != ZipEntry.DEFLATED) {
                        entryOut.setMethod(ZipEntry.DEFLATED);
                    }
                    entryOut.setCompressedSize(-1);
                    zos.putNextEntry(entryOut);
                    if (!entryIn.isDirectory()) {
                        if (entryName.endsWith(".class") && !shouldExclude) {
                            processClass(zis, zos);
                        } else {
                            copy(zis, zos);
                        }
                    }
                    zos.closeEntry();
                    processedEntryNamesMap.put(entryName, 1);
                }
            }
        } finally {
            closeQuietly(zos);
            closeQuietly(zis);
        }
    }

    private void processClass(InputStream classIn, OutputStream classOut) throws IOException {
        ClassReader cr = new ClassReader(classIn);
        // skip module-info class, fixed #38
        if ("module-info".equals(cr.getClassName())) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = classIn.read(buffer)) >= 0) {
                classOut.write(buffer, 0, read);
            }
        } else {
            ClassWriter cw = new ClassWriter(0);
            ClassVisitor cv = ClassVisitorFactory.create(mStringFogImpl, mMappingPrinter, mFogPackages,
                    mKey, mFogClassName, cr.getClassName() , cw);
            cr.accept(cv, 0);
            classOut.write(cw.toByteArray());
            classOut.flush();
        }
    }

    private boolean shouldExcludeJar(File jarIn, Charset charsetIn) throws IOException {
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(jarIn)), charsetIn);
            ZipEntry entryIn;
            while ((entryIn = zis.getNextEntry()) != null) {
                final String entryName = entryIn.getName();
                if (entryName != null && entryName.contains("StringFog")) {
                    return true;
                }
            }
        } finally {
            closeQuietly(zis);
        }
        return false;
    }

    private void closeQuietly(Closeable target) {
        if (target != null) {
            try {
                target.close();
            } catch (Exception e) {
                // Ignored.
            }
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int c;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
    }

}
