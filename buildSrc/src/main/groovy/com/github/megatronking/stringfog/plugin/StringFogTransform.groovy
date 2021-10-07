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

package com.github.megatronking.stringfog.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.api.BaseVariant
import com.github.megatronking.stringfog.plugin.utils.MD5
import com.google.common.collect.ImmutableSet
import com.google.common.io.Files
import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.DomainObjectSet
import org.gradle.api.Project
import org.gradle.api.Task

abstract class StringFogTransform extends Transform {

    public static final String FOG_CLASS_NAME = 'StringFog'
    private static final String TRANSFORM_NAME = 'stringFog'

    protected StringFogClassInjector mInjector
    protected StringFogMappingPrinter mMappingPrinter

    protected String mKey
    protected String mImplementation

    StringFogTransform(Project project, DomainObjectSet<BaseVariant> variants) {
        project.afterEvaluate {
            String key = project.stringfog.key
            String[] fogPackages = project.stringfog.fogPackages
            String implementation = project.stringfog.implementation
            if (key == null || key.length() == 0) {
                throw new IllegalArgumentException("Missing stringfog key config")
            }
            if (implementation == null || implementation.length() == 0) {
                throw new IllegalArgumentException("Missing stringfog implementation config")
            }
            if (project.stringfog.enable) {
                def applicationId = variants.first().applicationId
                def manifestFile = project.file("src/main/AndroidManifest.xml")
                if (manifestFile.exists()) {
                    def parsedManifest = new XmlParser().parse(
                            new InputStreamReader(new FileInputStream(manifestFile), "utf-8"))
                    if (parsedManifest != null) {
                        def packageName = parsedManifest.attribute("package")
                        if (packageName != null) {
                            applicationId = packageName
                        }
                    }
                }
                createFogClass(project, fogPackages, key, implementation, variants, applicationId)
            } else {
                mMappingPrinter = null
                mInjector = null
            }
            mKey = key
            mImplementation = implementation
        }
    }

    void createFogClass(def project, String[] fogPackages, String key, String implementation,
                        DomainObjectSet<BaseVariant> variants, def applicationId) {
        variants.all { variant ->
            def variantName = variant.name.toUpperCase()[0] + variant.name.substring(1, variant.name.length() - 1)
            Task generateTask = project.tasks.findByName(variantName)
            if (generateTask == null) {
                generateTask = project.tasks.create("generate${variantName}StringFog", DefaultTask)

                def stringfogDir = new File(project.buildDir, "generated" +
                        File.separatorChar + "source" + File.separatorChar + "stringfog" + File.separatorChar + variant.name)
                def stringfogFile = new File(stringfogDir, applicationId.replace((char)'.', File.separatorChar) + File.separator + "StringFog.java")
                variant.registerJavaGeneratingTask(generateTask, stringfogDir)

                generateTask.doLast {
                    mMappingPrinter = new StringFogMappingPrinter(
                            new File(project.buildDir, "outputs/mapping/${variant.name.toLowerCase()}/stringfog.txt"))
                    // Create class injector
                    mInjector = new StringFogClassInjector(fogPackages, key, implementation,
                            applicationId + "." + FOG_CLASS_NAME, mMappingPrinter)

                    // Generate StringFog.java
                    StringFogClassGenerator.generate(stringfogFile, applicationId, FOG_CLASS_NAME,
                            key, implementation)
                }
            }
        }
    }

    @Override
    String getName() {
        return TRANSFORM_NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return ImmutableSet.of(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return null
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def dirInputs = new HashSet<>()
        def jarInputs = new HashSet<>()

        if (!transformInvocation.isIncremental()) {
            transformInvocation.getOutputProvider().deleteAll()
        }

        // Collecting inputs.
        transformInvocation.inputs.each { input ->
            input.directoryInputs.each { dirInput ->
                dirInputs.add(dirInput)
            }
            input.jarInputs.each { jarInput ->
                jarInputs.add(jarInput)
            }
        }

        if (mMappingPrinter != null) {
            mMappingPrinter.startMappingOutput()
            mMappingPrinter.ouputInfo(mKey, mImplementation)
        }

        if (!dirInputs.isEmpty() || !jarInputs.isEmpty()) {
            File dirOutput = transformInvocation.outputProvider.getContentLocation(
                    "classes", getOutputTypes(), getScopes(), Format.DIRECTORY)
            dirOutput.mkdirs()
            if (!dirInputs.isEmpty()) {
                dirInputs.each { dirInput ->
                    if (transformInvocation.incremental) {
                        dirInput.changedFiles.each { entry ->
                            File fileInput = entry.getKey()
                            File fileOutput = new File(fileInput.getAbsolutePath().replace(
                                    dirInput.file.getAbsolutePath(), dirOutput.getAbsolutePath()))
                            fileOutput.parentFile.mkdirs()
                            Status fileStatus = entry.getValue()
                            switch(fileStatus) {
                                case Status.ADDED:
                                case Status.CHANGED:
                                    if (fileInput.isDirectory()) {
                                        return // continue.
                                    }
                                    if (mInjector != null && fileInput.getName().endsWith('.class')) {
                                        mInjector.doFog2Class(fileInput, fileOutput)
                                    } else {
                                        Files.copy(fileInput, fileOutput)
                                    }
                                    break
                                case Status.REMOVED:
                                    if (fileOutput.exists()) {
                                        if (fileOutput.isDirectory()) {
                                            fileOutput.deleteDir()
                                        } else {
                                            fileOutput.delete()
                                        }
                                    }
                                    break
                            }
                        }
                    } else {
                        dirInput.file.traverse(type: FileType.FILES) { fileInput ->
                            File fileOutput = new File(fileInput.getAbsolutePath().replace(dirInput.file.getAbsolutePath(), dirOutput.getAbsolutePath()))
                            fileOutput.parentFile.mkdirs()
                            if (mInjector != null && fileInput.getName().endsWith('.class')) {
                                mInjector.doFog2Class(fileInput, fileOutput)
                            } else {
                                Files.copy(fileInput, fileOutput)
                            }
                        }
                    }
                }
            }

            if (!jarInputs.isEmpty()) {
                jarInputs.each { jarInput ->
                    File jarInputFile = jarInput.file
                    File jarOutputFile = transformInvocation.outputProvider.getContentLocation(
                            getUniqueHashName(jarInputFile), getOutputTypes(), getScopes(), Format.JAR
                    )

                    jarOutputFile.parentFile.mkdirs()

                    switch (jarInput.status) {
                        case Status.NOTCHANGED:
                            if (transformInvocation.incremental) {
                                break
                            }
                        case Status.ADDED:
                        case Status.CHANGED:
                            if (mInjector != null) {
                                mInjector.doFog2Jar(jarInputFile, jarOutputFile)
                            } else {
                                Files.copy(jarInputFile, jarOutputFile)
                            }
                            break
                        case Status.REMOVED:
                            if (jarOutputFile.exists()) {
                                jarOutputFile.delete()
                            }
                            break
                    }
                }
            }
        }

        if (mMappingPrinter != null) {
            mMappingPrinter.endMappingOutput()
        }
    }

    String getUniqueHashName(File fileInput) {
        final String fileInputName = fileInput.getName()
        if (fileInput.isDirectory()) {
            return fileInputName
        }
        final String parentDirPath = fileInput.getParentFile().getAbsolutePath()
        final String pathMD5 = MD5.getMessageDigest(parentDirPath.getBytes())
        final int extSepPos = fileInputName.lastIndexOf('.')
        final String fileInputNamePrefix =
                (extSepPos >= 0 ? fileInputName.substring(0, extSepPos) : fileInputName)
        return fileInputNamePrefix + '_' + pathMD5
    }

}
