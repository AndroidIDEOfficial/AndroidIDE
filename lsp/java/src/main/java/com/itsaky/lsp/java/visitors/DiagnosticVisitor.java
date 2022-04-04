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

package com.itsaky.lsp.java.visitors;

import com.itsaky.androidide.utils.Logger;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class DiagnosticVisitor extends TreeScanner<Void, Map<TreePath, String>> {

    // Copied from TreePathScanner
    // We need to be able to call scan(path, _) recursively
    private TreePath path;
    private Map<String, TreePath> declaredExceptions = new HashMap<>();
    private Set<String> observedExceptions = new HashSet<>();

    private final Trees trees;
    private final Map<Element, TreePath> privateDeclarations = new HashMap<>(),
            localVariables = new HashMap<>();
    private final Set<Element> used = new HashSet<>();

    public DiagnosticVisitor(JavacTask task) {
        this.trees = Trees.instance(task);
    }

    private void scanPath(TreePath path) {
        TreePath prev = this.path;
        this.path = path;
        try {
            path.getLeaf().accept(this, null);
        } finally {
            this.path = prev; // So we can call scan(path, _) recursively
        }
    }

    @Override
    public Void scan(Tree tree, Map<TreePath, String> p) {
        if (tree == null) return null;

        TreePath prev = path;
        path = new TreePath(path, tree);

        try {
            return tree.accept(this, p);
        } finally {
            path = prev;
        }
    }

    public Set<Element> notUsed() {
        Set<Element> unused = new HashSet<>();
        unused.addAll(privateDeclarations.keySet());
        unused.addAll(localVariables.keySet());
        unused.removeAll(used);
        // Remove if there are any null elements somehow ended up being added
        // during async work which calls `lint`
        unused.removeIf(Objects::isNull);
        // Remove if <error > field was injected while forming the AST
        unused.removeIf(i -> i.toString().equals("<error>"));
        return unused;
    }

    private void foundPrivateDeclaration() {
        privateDeclarations.put(trees.getElement(path), path);
    }

    private void foundLocalVariable() {
        localVariables.put(trees.getElement(path), path);
    }

    private void foundReference() {
        Element toEl = trees.getElement(path);
        if (toEl == null) {
            return;
        }
        if (toEl.asType().getKind() == TypeKind.ERROR) {
            foundPseudoReference(toEl);
            return;
        }
        sweep(toEl);
    }

    private void foundPseudoReference(Element toEl) {
        Element parent = toEl.getEnclosingElement();
        if (!(parent instanceof TypeElement)) {
            return;
        }

        Name memberName = toEl.getSimpleName();
        TypeElement type = (TypeElement) parent;
        for (Element member : type.getEnclosedElements()) {
            if (member.getSimpleName().contentEquals(memberName)) {
                sweep(member);
            }
        }
    }

    private void sweep(Element toEl) {
        boolean firstUse = used.add(toEl);
        boolean notScanned = firstUse && privateDeclarations.containsKey(toEl);
        if (notScanned) {
            scanPath(privateDeclarations.get(toEl));
        }
    }

    private boolean isReachable(TreePath path) {
        // Check if t is reachable because it's public
        Tree t = path.getLeaf();
        if (t instanceof VariableTree) {
            VariableTree v = (VariableTree) t;
            boolean isPrivate = v.getModifiers().getFlags().contains(Modifier.PRIVATE);
            if (!isPrivate || isLocalVariable(path)) {
                return true;
            }
        }
        if (t instanceof MethodTree) {
            MethodTree m = (MethodTree) t;
            boolean isPrivate = m.getModifiers().getFlags().contains(Modifier.PRIVATE);
            boolean isEmptyConstructor = m.getParameters().isEmpty() && m.getReturnType() == null;
            if (!isPrivate || isEmptyConstructor) {
                return true;
            }
        }
        if (t instanceof ClassTree) {
            ClassTree c = (ClassTree) t;
            boolean isPrivate = c.getModifiers().getFlags().contains(Modifier.PRIVATE);
            if (!isPrivate) {
                return true;
            }
        }
        // Check if t has been referenced by a reachable element
        Element el = trees.getElement(path);
        return used.contains(el);
    }

    private boolean isLocalVariable(TreePath path) {
        Tree.Kind kind = path.getLeaf().getKind();
        if (kind != Tree.Kind.VARIABLE) {
            return false;
        }
        Tree.Kind parent = path.getParentPath().getLeaf().getKind();
        if (parent == Tree.Kind.CLASS || parent == Tree.Kind.INTERFACE) {
            return false;
        }
        if (parent == Tree.Kind.METHOD) {
            MethodTree method = (MethodTree) path.getParentPath().getLeaf();
            return method.getBody() != null;
        }
        return true;
    }

    private Map<String, TreePath> declared(MethodTree t) {
        Map<String, TreePath> names = new HashMap<>();
        for (ExpressionTree e : t.getThrows()) {
            TreePath path = new TreePath(this.path, e);
            Element to = trees.getElement(path);
            if (!(to instanceof TypeElement)) continue;
            TypeElement type = (TypeElement) to;
            String name = type.getQualifiedName().toString();
            names.put(name, path);
        }
        return names;
    }

    @Override
    public Void visitCompilationUnit(CompilationUnitTree t, Map<TreePath, String> notThrown) {
        return super.visitCompilationUnit(t, notThrown);
    }

    @Override
    public Void visitVariable(VariableTree t, Map<TreePath, String> notThrown) {
        if (isLocalVariable(path)) {
            foundLocalVariable();
            super.visitVariable(t, notThrown);
        } else if (isReachable(path)) {
            super.visitVariable(t, notThrown);
        } else {
            foundPrivateDeclaration();
        }
        return null;
    }

    @Override
    public Void visitMethod(MethodTree t, Map<TreePath, String> notThrown) {
        // Create a new method scope
        Map<String, TreePath> pushDeclared = declaredExceptions;
        Set<String> pushObserved = observedExceptions;
        declaredExceptions = declared(t);
        observedExceptions = new HashSet<>();
        // Recursively scan for 'throw' and method calls
        super.visitMethod(t, notThrown);
        // Check for exceptions that were never thrown
        for (String exception : declaredExceptions.keySet()) {
            if (!observedExceptions.contains(exception)) {
                notThrown.put(declaredExceptions.get(exception), exception);
            }
        }
        declaredExceptions = pushDeclared;
        observedExceptions = pushObserved;

        if (!isReachable(path)) {
            foundPrivateDeclaration();
        }
        return null;
    }

    @Override
    public Void visitClass(ClassTree t, Map<TreePath, String> notThrown) {
        if (isReachable(path)) {
            super.visitClass(t, notThrown);
        } else {
            foundPrivateDeclaration();
        }
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierTree t, Map<TreePath, String> notThrown) {
        foundReference();
        return super.visitIdentifier(t, notThrown);
    }

    @Override
    public Void visitMemberSelect(MemberSelectTree t, Map<TreePath, String> notThrown) {
        foundReference();
        return super.visitMemberSelect(t, notThrown);
    }

    @Override
    public Void visitMemberReference(MemberReferenceTree t, Map<TreePath, String> notThrown) {
        foundReference();
        return super.visitMemberReference(t, notThrown);
    }

    @Override
    public Void visitNewClass(NewClassTree t, Map<TreePath, String> notThrown) {
        foundReference();
        return super.visitNewClass(t, notThrown);
    }

    @Override
    public Void visitThrow(ThrowTree t, Map<TreePath, String> notThrown) {
        TreePath path = new TreePath(this.path, t.getExpression());
        TypeMirror type = trees.getTypeMirror(path);
        addThrown(type);
        return super.visitThrow(t, notThrown);
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree t, Map<TreePath, String> notThrown) {
        Element target = trees.getElement(this.path);
        if (target instanceof ExecutableElement) {
            ExecutableElement method = (ExecutableElement) target;
            for (TypeMirror type : method.getThrownTypes()) {
                addThrown(type);
            }
        }
        return super.visitMethodInvocation(t, notThrown);
    }

    private void addThrown(TypeMirror type) {
        if (type instanceof DeclaredType) {
            DeclaredType declared = (DeclaredType) type;
            TypeElement el = (TypeElement) declared.asElement();
            String name = el.getQualifiedName().toString();
            observedExceptions.add(name);
        }
    }

    private static final Logger LOG = Logger.newInstance("main");
}
