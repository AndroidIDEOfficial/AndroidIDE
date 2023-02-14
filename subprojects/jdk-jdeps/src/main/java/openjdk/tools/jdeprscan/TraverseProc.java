/*
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
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

package openjdk.tools.jdeprscan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdkx.annotation.processing.AbstractProcessor;
import jdkx.annotation.processing.Messager;
import jdkx.annotation.processing.ProcessingEnvironment;
import jdkx.annotation.processing.RoundEnvironment;
import jdkx.annotation.processing.SupportedAnnotationTypes;
import jdkx.annotation.processing.SupportedSourceVersion;

import jdkx.lang.model.SourceVersion;
import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.ElementKind;
import jdkx.lang.model.element.Modifier;
import jdkx.lang.model.element.ModuleElement;
import jdkx.lang.model.element.PackageElement;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.util.Elements;
import jdkx.tools.Diagnostic;

@SupportedAnnotationTypes("*")
public class TraverseProc extends AbstractProcessor {
    Elements elements;
    Messager messager;
    final List<String> moduleRoots;
    Map<PackageElement, List<TypeElement>> publicTypes;

    TraverseProc(List<String> roots) {
        moduleRoots = roots;
    }

    @Override
    public void init(ProcessingEnvironment pe) {
        super.init(pe);
        elements = pe.getElementUtils();
        messager = pe.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        Set<ModuleElement> modules = new HashSet<>();
        for (String mname : moduleRoots) {
            ModuleElement me = elements.getModuleElement(mname);
            if (me == null) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                                      String.format("module %s not found%n", mname));
            } else {
                modules.addAll(findModules(me));
            }
        }

        Set<PackageElement> packages = findPackages(modules);

        publicTypes = findPublicTypes(packages);

        return true;
    }

    void printPublicTypes() {
        printPublicTypes(publicTypes);
    }

    public Map<PackageElement, List<TypeElement>> getPublicTypes() {
        return publicTypes;
    }

    void printPublicTypes(Map<PackageElement, List<TypeElement>> types) {
        System.out.println("All public types:");
        types.entrySet().stream()
             .sorted(Comparator.comparing(e -> e.getKey().toString()))
             .forEach(e -> {
                 System.out.println("  " + e.getKey());
                 e.getValue().stream()
                         .sorted(Comparator.comparing(TypeElement::toString))
                         .forEach(t -> System.out.println("    " + t));
             });
        System.out.println();
        System.out.flush();
    }

    Set<ModuleElement> findModules(ModuleElement root) {
        return findModules0(root, new HashSet<>(), 0);
    }

    Set<ModuleElement> findModules0(ModuleElement m, Set<ModuleElement> set, int nesting) {
        set.add(m);
        for (ModuleElement.Directive dir : m.getDirectives()) {
            if (dir.getKind() == ModuleElement.DirectiveKind.REQUIRES) {
                ModuleElement.RequiresDirective req = (ModuleElement.RequiresDirective)dir;
                findModules0(req.getDependency(), set, nesting + 1);
            }
        }
        return set;
    }

    Set<PackageElement> findPackages(Collection<ModuleElement> mods) {
        Set<PackageElement> set = new HashSet<>();
        for (ModuleElement m : mods) {
            for (ModuleElement.Directive dir : m.getDirectives()) {
                if (dir.getKind() == ModuleElement.DirectiveKind.EXPORTS) { //XXX
                    ModuleElement.ExportsDirective exp = (ModuleElement.ExportsDirective)dir;
                    if (exp.getTargetModules() == null) {
                        set.add(exp.getPackage());
                    }
                }
            }
        }
        return set;
    }

    Map<PackageElement, List<TypeElement>> findPublicTypes(Collection<PackageElement> pkgs) {
        Map<PackageElement, List<TypeElement>> map = new HashMap<>();
        for (PackageElement pkg : pkgs) {
            List<TypeElement> enclosed = new ArrayList<>();
            for (Element e : pkg.getEnclosedElements()) {
                addPublicTypes(enclosed, e);
            }
            map.put(pkg, enclosed);
        }
        return map;
    }

    void addPublicTypes(List<TypeElement> list, Element e) {
        ElementKind kind = e.getKind();
        if ((kind.isClass() || kind.isInterface())
                && e.getModifiers().contains(Modifier.PUBLIC)) {
            list.add((TypeElement)e);
            for (Element enc : e.getEnclosedElements()) {
                addPublicTypes(list, enc);
            }
        }
    }
}
