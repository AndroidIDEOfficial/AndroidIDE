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

package com.itsaky.lsp.java.providers;

import androidx.annotation.NonNull;

import com.itsaky.lsp.api.IDefinitionProvider;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SourceFileObject;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.utils.FindHelper;
import com.itsaky.lsp.java.utils.NavigationHelper;
import com.itsaky.lsp.models.DefinitionParams;
import com.itsaky.lsp.models.DefinitionResult;
import com.itsaky.lsp.models.Location;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

public class DefinitionProvider implements IDefinitionProvider {
    private final CompilerProvider compiler;
    private Path file;
    private int line, column;
    
    public static final List<Location> NOT_SUPPORTED = Collections.emptyList ();
    
    public DefinitionProvider(CompilerProvider compiler) {
        this.compiler = compiler;
    }
    
    @NonNull
    @Override
    public DefinitionResult findDefinitions (DefinitionParams params) {
        this.file = params.getFile ();
        this.line = params.getPosition ().getLine ();
        this.column = params.getPosition ().getColumn ();
        return new DefinitionResult (find ());
    }
    
    public List<Location> find() {
        try (SynchronizedTask synchronizedTask = compiler.compile(file)) {
            return synchronizedTask.getWithTask (task -> {
                Element element = NavigationHelper.findElement(task, file, line, column);
                if (element == null) {
                    return NOT_SUPPORTED;
                }
                
                if (element.asType().getKind() == TypeKind.ERROR) {
                    task.close();
                    return findError(element);
                }
                // TODO instead of checking isLocal, just try to resolve the location, fall back to searching
                if (NavigationHelper.isLocal(element)) {
                    return findDefinitions(task, element);
                }
                String className = className(element);
                if (className.isEmpty()) {
                    return NOT_SUPPORTED;
                }
                Optional<JavaFileObject> otherFile = compiler.findAnywhere(className);
                if (!otherFile.isPresent ()) {
                    return Collections.emptyList ();
                }
                
                if (otherFile.get().toUri().equals(file.toUri())) {
                    return findDefinitions(task, element);
                }
                task.close();

                return findRemoteDefinitions(otherFile.get());
            });
        }
    }
    
    private List<Location> findError(Element element) {
        Name name = element.getSimpleName();
        if (name == null) {
            return NOT_SUPPORTED;
        }
        
        Element parent = element.getEnclosingElement();
        if (!(parent instanceof TypeElement)) return NOT_SUPPORTED;
        TypeElement type = (TypeElement) parent;
        String className = type.getQualifiedName().toString();
        String memberName = name.toString();
        return findAllMembers(className, memberName);
    }
    
    private List<Location> findAllMembers(String className, String memberName) {
        Optional<JavaFileObject> otherFile = compiler.findAnywhere(className);
        if (!otherFile.isPresent ()) {
            return Collections.emptyList ();
        }
        
        SourceFileObject fileAsSource = new SourceFileObject (file);
        List<JavaFileObject> sources = Arrays.asList (fileAsSource, otherFile.get());
        if (otherFile.get().toUri ().equals(file.toUri())) {
            sources = Collections.singletonList (fileAsSource);
        }
        
        List<Location> locations = new ArrayList<> ();
        try (SynchronizedTask synchronizedTask = compiler.compile(sources)) {
            synchronizedTask.runWithTask (task -> {
                Trees trees = Trees.instance(task.task);
                Elements elements = task.task.getElements();
                TypeElement parentClass = elements.getTypeElement(className);
                for (Element member : elements.getAllMembers(parentClass)) {
                    if (!member.getSimpleName().contentEquals(memberName)) continue;
                    TreePath path = trees.getPath(member);
                    if (path == null) {
                        continue;
                    }
                    Location location = FindHelper.location(task, path, memberName);
                    locations.add(location);
                }
            });
        }
        return locations;
    }
    
    private String className(Element element) {
        while (element != null) {
            if (element instanceof TypeElement) {
                TypeElement type = (TypeElement) element;
                return type.getQualifiedName().toString();
            }
            element = element.getEnclosingElement();
        }
        return "";
    }
    
    private List<Location> findRemoteDefinitions(JavaFileObject otherFile) {
        try (SynchronizedTask synchronizedTask = compiler.compile(Arrays.asList (new SourceFileObject(file), otherFile))) {
            return synchronizedTask.getWithTask (task -> {
                Element element = NavigationHelper.findElement(task, file, line, column);
                return findDefinitions(task, element);
            });
        }
    }
    
    private List<Location> findDefinitions(CompileTask task, Element element) {
        Trees trees = Trees.instance(task.task);
        TreePath path = trees.getPath(element);
        if (path == null) {
            return Collections.emptyList ();
        }
        
        Name name = element.getSimpleName();
        if (name.contentEquals("<init>")) name = element.getEnclosingElement().getSimpleName();
        return Collections.singletonList (FindHelper.location(task, path, name));
    }
}