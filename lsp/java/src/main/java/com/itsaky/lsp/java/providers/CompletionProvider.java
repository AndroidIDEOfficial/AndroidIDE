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

import static com.itsaky.lsp.java.utils.EditHelper.repeatSpaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.AbstractServiceProvider;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.IServerSettings;
import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.java.compiler.CompileTask;
import com.itsaky.lsp.java.compiler.CompilerProvider;
import com.itsaky.lsp.java.compiler.SourceFileObject;
import com.itsaky.lsp.java.compiler.SynchronizedTask;
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.java.rewrite.AddImport;
import com.itsaky.lsp.java.utils.EditHelper;
import com.itsaky.lsp.java.utils.Extractors;
import com.itsaky.lsp.java.utils.ScopeHelper;
import com.itsaky.lsp.java.utils.StringSearch;
import com.itsaky.lsp.java.visitors.FindCompletionsAt;
import com.itsaky.lsp.java.visitors.PruneMethodBodies;
import com.itsaky.lsp.models.Command;
import com.itsaky.lsp.models.CompletionData;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.CompletionItemKind;
import com.itsaky.lsp.models.CompletionParams;
import com.itsaky.lsp.models.CompletionResult;
import com.itsaky.lsp.models.InsertTextFormat;
import com.itsaky.lsp.models.TextEdit;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Scope;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Types;

public class CompletionProvider extends AbstractServiceProvider implements ICompletionProvider {

    public static final int MAX_COMPLETION_ITEMS = 50;
    private static final String[] TOP_LEVEL_KEYWORDS = {
        "package",
        "import",
        "public",
        "private",
        "protected",
        "abstract",
        "class",
        "interface",
        "@interface",
        "extends",
        "implements",
    };
    private static final String[] CLASS_BODY_KEYWORDS = {
        "public",
        "private",
        "protected",
        "static",
        "final",
        "native",
        "synchronized",
        "abstract",
        "default",
        "class",
        "interface",
        "void",
        "boolean",
        "int",
        "long",
        "float",
        "double",
    };
    private static final String[] METHOD_BODY_KEYWORDS = {
        "new",
        "assert",
        "try",
        "catch",
        "finally",
        "throw",
        "return",
        "break",
        "case",
        "continue",
        "default",
        "do",
        "while",
        "for",
        "switch",
        "if",
        "else",
        "instanceof",
        "var",
        "final",
        "class",
        "void",
        "boolean",
        "int",
        "long",
        "float",
        "double",
    };
    private static final Logger LOG = Logger.instance("JavaCompletionProvider");
    private final CompilerProvider compiler;
    private Path completingFile;
    private long cursor;

    public CompletionProvider(CompilerProvider compiler, IServerSettings settings) {
        super();
        super.applySettings(settings);

        this.compiler = compiler;
    }

    @Override
    public boolean canComplete(Path file) {
        return ICompletionProvider.super.canComplete(file)
                && file.toFile().getName().endsWith(".java");
    }

    @NonNull
    @Override
    public CompletionResult complete(@NonNull CompletionParams params) {
        return complete(
                params.getFile(), params.getPosition().getLine(), params.getPosition().getColumn());
    }

    public CompletionResult complete(@NonNull Path file, int line, int column) {
        LOG.info("Complete at " + file.getFileName() + "(" + line + "," + column + ")...");

        // javac expects 1-based line and column indexes
        line++;
        column++;

        Instant started = Instant.now();
        ParseTask task = compiler.parse(file);

        this.completingFile = file;
        this.cursor = task.root.getLineMap().getPosition(line, column);
        StringBuilder contents = new PruneMethodBodies(task.task).scan(task.root, cursor);
        int endOfLine = endOfLine(contents, (int) cursor);
        contents.insert(endOfLine, ';');
        CompletionResult list = compileAndComplete(file, contents.toString(), cursor);
        addTopLevelSnippets(task, list);
        logCompletionTiming(started, list.getItems(), list.isIncomplete());
        return list;
    }

    private int endOfLine(@NonNull CharSequence contents, int cursor) {
        while (cursor < contents.length()) {
            char c = contents.charAt(cursor);
            if (c == '\r' || c == '\n') break;
            cursor++;
        }
        return cursor;
    }

    private CompletionResult compileAndComplete(Path file, String contents, long cursor) {
        Instant started = Instant.now();
        SourceFileObject source = new SourceFileObject(file, contents, Instant.now());
        String partial = partialIdentifier(contents, (int) cursor);
        boolean endsWithParen = endsWithParen(contents, (int) cursor);
        SynchronizedTask synchronizedTask = compiler.compile(Collections.singletonList(source));
        return synchronizedTask.getWithTask(
                task -> {
                    LOG.info(
                            "...compiled in "
                                    + Duration.between(started, Instant.now()).toMillis()
                                    + "ms");
                    TreePath path = new FindCompletionsAt(task.task).scan(task.root(), cursor);
                    switch (path.getLeaf().getKind()) {
                        case IDENTIFIER:
                            return completeIdentifier(task, path, partial, endsWithParen);
                        case MEMBER_SELECT:
                            return completeMemberSelect(task, path, partial, endsWithParen);
                        case MEMBER_REFERENCE:
                            return completeMemberReference(task, path, partial);
                        case SWITCH:
                            return completeSwitchConstant(task, path, partial, endsWithParen);
                        case IMPORT:
                            return completeImport(
                                    qualifiedPartialIdentifier(contents, (int) cursor));
                        default:
                            CompletionResult list = new CompletionResult();
                            addKeywords(path, partial, list);
                            return list;
                    }
                });
    }

    @SuppressWarnings("Since15")
    private void addTopLevelSnippets(@NonNull ParseTask task, CompletionResult list) {
        Path file = Paths.get(task.root.getSourceFile().toUri());
        if (!hasTypeDeclaration(task.root)) {
            list.getItems().add(classSnippet(file));
            if (task.root.getPackage() == null) {
                list.getItems().add(packageSnippet(file));
            }
        }
    }

    private boolean hasTypeDeclaration(@NonNull CompilationUnitTree root) {
        for (Tree tree : root.getTypeDecls()) {
            if (tree.getKind() != Tree.Kind.ERRONEOUS) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    private CompletionItem packageSnippet(Path file) {
        String name = FileStore.suggestedPackageName(file);
        return snippetItem("package " + name, "package " + name + ";\n\n");
    }

    @NonNull
    private CompletionItem classSnippet(@NonNull Path file) {
        String name = file.getFileName().toString();
        name = name.substring(0, name.length() - ".java".length());
        return snippetItem("class " + name, "class " + name + " {\n    $0\n}");
    }

    @NonNull
    private String partialIdentifier(String contents, int end) {
        int start = end;
        while (start > 0 && Character.isJavaIdentifierPart(contents.charAt(start - 1))) {
            start--;
        }
        return contents.substring(start, end);
    }

    private boolean endsWithParen(@NonNull String contents, int cursor) {
        for (int i = cursor; i < contents.length(); i++) {
            if (!Character.isJavaIdentifierPart(contents.charAt(i))) {
                return contents.charAt(i) == '(';
            }
        }
        return false;
    }

    @NonNull
    private String qualifiedPartialIdentifier(String contents, int end) {
        int start = end;
        while (start > 0 && isQualifiedIdentifierChar(contents.charAt(start - 1))) {
            start--;
        }
        return contents.substring(start, end);
    }

    private boolean isQualifiedIdentifierChar(char c) {
        return c == '.' || Character.isJavaIdentifierPart(c);
    }

    @NonNull
    private CompletionResult completeIdentifier(
            CompileTask task, TreePath path, String partial, boolean endsWithParen) {
        LOG.info("...complete identifiers");
        CompletionResult list = new CompletionResult();
        list.setItems(completeUsingScope(task, path, partial, endsWithParen));
        addStaticImports(task, path.getCompilationUnit(), partial, endsWithParen, list);
        if (!list.isIncomplete()) {
            final boolean allLower = getSettings().shouldMatchAllLowerCase();
            if (allLower || (partial.length() > 0 && Character.isUpperCase(partial.charAt(0)))) {
                addClassNames(path.getCompilationUnit(), partial, list, allLower);
            }
        }
        addKeywords(path, partial, list);
        return list;
    }

    private void addKeywords(TreePath path, String partial, CompletionResult list) {
        Tree level = findKeywordLevel(path);
        String[] keywords = {};
        if (level instanceof CompilationUnitTree) {
            keywords = TOP_LEVEL_KEYWORDS;
        } else if (level instanceof ClassTree) {
            keywords = CLASS_BODY_KEYWORDS;
        } else if (level instanceof MethodTree) {
            keywords = METHOD_BODY_KEYWORDS;
        }
        for (String k : keywords) {
            if (StringSearch.matchesPartialName(
                    k, partial, getSettings().shouldMatchAllLowerCase())) {
                list.getItems().add(keyword(k));
            }
        }
    }

    private Tree findKeywordLevel(TreePath path) {
        while (path != null) {
            if (path.getLeaf() instanceof CompilationUnitTree
                    || path.getLeaf() instanceof ClassTree
                    || path.getLeaf() instanceof MethodTree) {
                return path.getLeaf();
            }
            path = path.getParentPath();
        }
        throw new RuntimeException("empty path");
    }

    @NonNull
    private List<CompletionItem> completeUsingScope(
            @NonNull CompileTask task, TreePath path, String partial, boolean endsWithParen) {
        Trees trees = Trees.instance(task.task);
        List<CompletionItem> list = new ArrayList<>();
        Scope scope = trees.getScope(path);
        Predicate<CharSequence> filter =
                name -> StringSearch.matchesPartialName(name, partial, true);
        for (Element member : ScopeHelper.scopeMembers(task, scope, filter)) {
            if (member.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) member;
                TreePath parentPath = path.getParentPath() /*method*/.getParentPath() /*class*/;
                list.add(overrideIfPossible(task, parentPath, method, endsWithParen));
            } else {
                list.add(item(task, member));
            }
        }

        LOG.info("...found " + list.size() + " scope members");
        return list;
    }

    private void addStaticImports(
            @NonNull CompileTask task,
            CompilationUnitTree root,
            String partial,
            boolean endsWithParen,
            @NonNull CompletionResult list) {
        Trees trees = Trees.instance(task.task);
        Map<String, List<ExecutableElement>> methods = new HashMap<>();
        int previousSize = list.getItems().size();
        outer:
        for (ImportTree i : root.getImports()) {
            if (!i.isStatic()) continue;
            MemberSelectTree id = (MemberSelectTree) i.getQualifiedIdentifier();
            if (!importMatchesPartial(id.getIdentifier(), partial)) continue;
            TreePath path = trees.getPath(root, id.getExpression());
            TypeElement type = (TypeElement) trees.getElement(path);
            for (Element member : type.getEnclosedElements()) {
                if (!member.getModifiers().contains(Modifier.STATIC)) continue;
                if (!memberMatchesImport(id.getIdentifier(), member)) continue;
                if (!StringSearch.matchesPartialName(
                        member.getSimpleName(), partial, getSettings().shouldMatchAllLowerCase()))
                    continue;
                if (member.getKind() == ElementKind.METHOD) {
                    putMethod((ExecutableElement) member, methods);
                } else {
                    list.getItems().add(item(task, member));
                }
                if (list.getItems().size() + methods.size() > MAX_COMPLETION_ITEMS) {
                    list.setIncomplete(true);
                    break outer;
                }
            }
        }
        for (List<ExecutableElement> overloads : methods.values()) {
            list.getItems().add(method(task, overloads, !endsWithParen));
        }
        LOG.info("...found " + (list.getItems().size() - previousSize) + " static imports");
    }

    private boolean importMatchesPartial(@NonNull Name staticImport, String partial) {
        return staticImport.contentEquals("*")
                || StringSearch.matchesPartialName(
                        staticImport, partial, getSettings().shouldMatchAllLowerCase());
    }

    private boolean memberMatchesImport(@NonNull Name staticImport, Element member) {
        return staticImport.contentEquals("*")
                || staticImport.contentEquals(member.getSimpleName());
    }

    private void addClassNames(
            @NonNull CompilationUnitTree root,
            String partial,
            @NonNull CompletionResult list,
            boolean allLower) {
        String packageName = Objects.toString(root.getPackageName(), "");
        Set<String> uniques = new HashSet<>();
        int previousSize = list.getItems().size();

        final Path file = Paths.get(root.getSourceFile().toUri());
        final Set<String> imports =
                root.getImports().stream()
                        .map(ImportTree::getQualifiedIdentifier)
                        .map(Tree::toString)
                        .collect(Collectors.toSet());

        for (String className : compiler.packagePrivateTopLevelTypes(packageName)) {
            if (!StringSearch.matchesPartialName(className, partial, allLower)) continue;
            list.getItems().add(classItem(imports, file, className));
            uniques.add(className);
        }

        for (String className : compiler.publicTopLevelTypes()) {
            if (!StringSearch.matchesPartialName(simpleName(className), partial, allLower))
                continue;
            if (uniques.contains(className)) continue;
            if (list.getItems().size() > MAX_COMPLETION_ITEMS) {
                list.setIncomplete(true);
                break;
            }
            list.getItems().add(classItem(imports, file, className));
            uniques.add(className);
        }

        for (Tree t : root.getTypeDecls()) {
            if (!(t instanceof ClassTree)) {
                continue;
            }

            final ClassTree c = (ClassTree) t;
            if (!StringSearch.matchesPartialName(
                    c.getSimpleName() == null ? "" : c.getSimpleName(), partial, allLower)) {
                continue;
            }

            final String name = packageName + "." + c.getSimpleName();
            list.getItems().add(classItem(name));
            if (list.getItems().size() > MAX_COMPLETION_ITEMS) {
                list.setIncomplete(true);
                break;
            }
        }

        LOG.info("...found " + (list.getItems().size() - previousSize) + " class names");
    }

    private CompletionResult completeMemberSelect(
            @NonNull CompileTask task,
            @NonNull TreePath path,
            String partial,
            boolean endsWithParen) {
        Trees trees = Trees.instance(task.task);
        MemberSelectTree select = (MemberSelectTree) path.getLeaf();
        LOG.info("...complete members of " + select.getExpression());
        path = new TreePath(path, select.getExpression());
        boolean isStatic = trees.getElement(path) instanceof TypeElement;
        Scope scope = trees.getScope(path);
        TypeMirror type = trees.getTypeMirror(path);
        if (type instanceof ArrayType) {
            return completeArrayMemberSelect(isStatic);
        } else if (type instanceof TypeVariable) {
            return completeTypeVariableMemberSelect(
                    task, scope, (TypeVariable) type, isStatic, partial, endsWithParen);
        } else if (type instanceof DeclaredType) {
            return completeDeclaredTypeMemberSelect(
                    task, scope, (DeclaredType) type, isStatic, partial, endsWithParen);
        } else {
            return EMPTY;
        }
    }

    private CompletionResult completeArrayMemberSelect(boolean isStatic) {
        if (isStatic) {
            return EMPTY;
        } else {
            CompletionResult list = new CompletionResult();
            list.getItems().add(keyword("length"));
            return list;
        }
    }

    private CompletionResult completeTypeVariableMemberSelect(
            CompileTask task,
            Scope scope,
            @NonNull TypeVariable type,
            boolean isStatic,
            String partial,
            boolean endsWithParen) {
        if (type.getUpperBound() instanceof DeclaredType) {
            return completeDeclaredTypeMemberSelect(
                    task,
                    scope,
                    (DeclaredType) type.getUpperBound(),
                    isStatic,
                    partial,
                    endsWithParen);
        } else if (type.getUpperBound() instanceof TypeVariable) {
            return completeTypeVariableMemberSelect(
                    task,
                    scope,
                    (TypeVariable) type.getUpperBound(),
                    isStatic,
                    partial,
                    endsWithParen);
        } else {
            return EMPTY;
        }
    }

    @NonNull
    private CompletionResult completeDeclaredTypeMemberSelect(
            @NonNull CompileTask task,
            Scope scope,
            @NonNull DeclaredType type,
            boolean isStatic,
            String partial,
            boolean endsWithParen) {
        Trees trees = Trees.instance(task.task);
        TypeElement typeElement = (TypeElement) type.asElement();
        List<CompletionItem> list = new ArrayList<>();
        Map<String, List<ExecutableElement>> methods = new HashMap<>();
        for (Element member : task.task.getElements().getAllMembers(typeElement)) {
            if (member.getKind() == ElementKind.CONSTRUCTOR) continue;
            if (!StringSearch.matchesPartialName(
                    member.getSimpleName(), partial, getSettings().shouldMatchAllLowerCase()))
                continue;
            if (!trees.isAccessible(scope, member, type)) continue;
            if (isStatic != member.getModifiers().contains(Modifier.STATIC)) continue;
            if (member.getKind() == ElementKind.METHOD) {
                putMethod((ExecutableElement) member, methods);
            } else {
                list.add(item(task, member));
            }
        }
        for (List<ExecutableElement> overloads : methods.values()) {
            list.add(method(task, overloads, !endsWithParen));
        }
        if (isStatic) {
            list.add(keyword("class"));
        }
        if (isStatic && isEnclosingClass(type, scope)) {
            list.add(keyword("this"));
            list.add(keyword("super"));
        }
        return new CompletionResult(false, list);
    }

    private boolean isEnclosingClass(DeclaredType type, Scope start) {
        for (Scope s : ScopeHelper.fastScopes(start)) {
            // If we reach a static method, stop looking
            ExecutableElement method = s.getEnclosingMethod();
            if (method != null && method.getModifiers().contains(Modifier.STATIC)) {
                return false;
            }
            // If we find the enclosing class
            TypeElement thisElement = s.getEnclosingClass();
            if (thisElement != null && thisElement.asType().equals(type)) {
                return true;
            }
            // If the enclosing class is static, stop looking
            if (thisElement != null && thisElement.getModifiers().contains(Modifier.STATIC)) {
                return false;
            }
        }
        return false;
    }

    private CompletionResult completeMemberReference(
            @NonNull CompileTask task, @NonNull TreePath path, String partial) {
        Trees trees = Trees.instance(task.task);
        MemberReferenceTree select = (MemberReferenceTree) path.getLeaf();
        LOG.info("...complete methods of " + select.getQualifierExpression());
        path = new TreePath(path, select.getQualifierExpression());
        Element element = trees.getElement(path);
        boolean isStatic = element instanceof TypeElement;
        Scope scope = trees.getScope(path);
        TypeMirror type = trees.getTypeMirror(path);
        if (type instanceof ArrayType) {
            return completeArrayMemberReference(isStatic);
        } else if (type instanceof TypeVariable) {
            return completeTypeVariableMemberReference(
                    task, scope, (TypeVariable) type, isStatic, partial);
        } else if (type instanceof DeclaredType) {
            return completeDeclaredTypeMemberReference(
                    task, scope, (DeclaredType) type, isStatic, partial);
        } else {
            return EMPTY;
        }
    }

    private CompletionResult completeArrayMemberReference(boolean isStatic) {
        if (isStatic) {
            CompletionResult list = new CompletionResult();
            list.getItems().add(keyword("new"));
            return list;
        } else {
            return EMPTY;
        }
    }

    private CompletionResult completeTypeVariableMemberReference(
            CompileTask task,
            Scope scope,
            @NonNull TypeVariable type,
            boolean isStatic,
            String partial) {
        if (type.getUpperBound() instanceof DeclaredType) {
            return completeDeclaredTypeMemberReference(
                    task, scope, (DeclaredType) type.getUpperBound(), isStatic, partial);
        } else if (type.getUpperBound() instanceof TypeVariable) {
            return completeTypeVariableMemberReference(
                    task, scope, (TypeVariable) type.getUpperBound(), isStatic, partial);
        } else {
            return EMPTY;
        }
    }

    @NonNull
    private CompletionResult completeDeclaredTypeMemberReference(
            @NonNull CompileTask task,
            Scope scope,
            @NonNull DeclaredType type,
            boolean isStatic,
            String partial) {
        Trees trees = Trees.instance(task.task);
        TypeElement typeElement = (TypeElement) type.asElement();
        List<CompletionItem> list = new ArrayList<>();
        Map<String, List<ExecutableElement>> methods = new HashMap<>();
        for (Element member : task.task.getElements().getAllMembers(typeElement)) {
            if (!StringSearch.matchesPartialName(
                    member.getSimpleName(), partial, getSettings().shouldMatchAllLowerCase()))
                continue;
            if (member.getKind() != ElementKind.METHOD) continue;
            if (!trees.isAccessible(scope, member, type)) continue;
            if (!isStatic && member.getModifiers().contains(Modifier.STATIC)) continue;
            if (member.getKind() == ElementKind.METHOD) {
                putMethod((ExecutableElement) member, methods);
            } else {
                list.add(item(task, member));
            }
        }
        for (List<ExecutableElement> overloads : methods.values()) {
            list.add(method(task, overloads, false));
        }
        if (isStatic) {
            list.add(keyword("new"));
        }
        return new CompletionResult(false, list);
    }

    private void putMethod(
            @NonNull ExecutableElement method,
            @NonNull Map<String, List<ExecutableElement>> methods) {
        String name = method.getSimpleName().toString();

        if (!methods.containsKey(name)) {
            methods.put(name, new ArrayList<>());
        }

        Objects.requireNonNull(methods.get(name)).add(method);
    }

    private CompletionResult completeSwitchConstant(
            @NonNull CompileTask task,
            @NonNull TreePath path,
            String partial,
            boolean endsWithParen) {
        SwitchTree switchTree = (SwitchTree) path.getLeaf();
        path = new TreePath(path, switchTree.getExpression());
        TypeMirror type = Trees.instance(task.task).getTypeMirror(path);

        if (type.getKind().isPrimitive() || !(type instanceof DeclaredType)) {
            // primitive types do not have any members
            return completeIdentifier(task, path, partial, endsWithParen);
        }

        DeclaredType declared = (DeclaredType) type;
        TypeElement element = (TypeElement) declared.asElement();

        if (element.getKind() != ElementKind.ENUM) {
            // If the switch's expression is not an enum type
            // we will not get any constants to complete
            // In this case, we fall back to completing identifiers
            // At this point, we are sure that the case expression will definitely be an identifier
            // tree
            // see visitCase (CaseTree, Long) in FindCompletionsAt.java
            return completeIdentifier(task, path, partial, endsWithParen);
        }

        LOG.info("...complete constants of type " + type);
        List<CompletionItem> list = new ArrayList<>();
        for (Element member : task.task.getElements().getAllMembers(element)) {
            if (member.getKind() != ElementKind.ENUM_CONSTANT) continue;
            if (!StringSearch.matchesPartialName(
                    member.getSimpleName(), partial, getSettings().shouldMatchAllLowerCase()))
                continue;
            list.add(item(task, member));
        }

        return new CompletionResult(false, list);
    }

    private CompletionResult completeImport(String path) {
        LOG.info("...complete import");
        Set<String> names = new HashSet<>();
        CompletionResult list = new CompletionResult();
        for (String className : compiler.publicTopLevelTypes()) {
            if (className.startsWith(path)) {
                int start = path.lastIndexOf('.');
                int end = className.indexOf('.', path.length());
                if (end == -1) end = className.length();
                String segment = className.substring(start + 1, end);
                if (names.contains(segment)) continue;
                names.add(segment);
                boolean isClass = end == path.length();
                if (isClass) {
                    list.getItems().add(classItem(className));
                } else {
                    list.getItems().add(packageItem(segment));
                }
                if (list.getItems().size() > MAX_COMPLETION_ITEMS) {
                    list.setIncomplete(true);
                    return list;
                }
            }
        }
        return list;
    }

    private CompletionItem packageItem(String name) {
        CompletionItem i = new CompletionItem();
        i.setLabel(name);
        i.setKind(CompletionItemKind.MODULE);
        return i;
    }

    private CompletionItem classItem(String className) {
        return classItem(Collections.emptySet(), null, className);
    }

    private CompletionItem classItem(Set<String> imports, Path file, String className) {
        CompletionItem i = new CompletionItem();
        i.setLabel(simpleName(className).toString());
        i.setKind(CompletionItemKind.CLASS);
        i.setDetail(className);

        CompletionData data = new CompletionData();
        data.setClassName(className);
        i.setData(data);
        i.setAdditionalTextEdits(checkForImports(imports, file, className));
        return i;
    }

    private List<TextEdit> checkForImports(
            @NonNull Set<String> fileImports, Path path, String className) {
        final String pkgName = Extractors.packageName(className);
        final String star = pkgName + ".*";
        if ("java.lang".equals(pkgName)
                || fileImports.contains(className)
                || fileImports.contains(star)
                || path == null) {
            return Collections.emptyList();
        }

        AddImport addImport = new AddImport(path, className);
        return Arrays.asList(Objects.requireNonNull(addImport.rewrite(compiler).get(path)));
    }

    private CompletionItem snippetItem(String label, String snippet) {
        CompletionItem i = new CompletionItem();
        i.setLabel(label);
        i.setKind(CompletionItemKind.SNIPPET);
        i.setInsertText(snippet);
        i.setInsertTextFormat(InsertTextFormat.SNIPPET);
        i.setSortText(String.format(Locale.getDefault(), "%02d%s", Priority.SNIPPET, i.getLabel()));
        return i;
    }

    @NonNull
    private CompletionItem item(CompileTask task, @NonNull Element element) {
        if (element.getKind() == ElementKind.METHOD) throw new RuntimeException("method");
        CompletionItem i = new CompletionItem();
        i.setLabel(element.getSimpleName().toString());
        i.setKind(kind(element));
        i.setDetail(element.toString());
        i.setData(data(task, element, 1));
        return i;
    }

    @NonNull
    private CompletionItem method(
            CompileTask task, @NonNull List<ExecutableElement> overloads, boolean addParens) {
        ExecutableElement first = overloads.get(0);
        CompletionItem i = new CompletionItem();
        i.setLabel(first.getSimpleName().toString());
        i.setKind(CompletionItemKind.METHOD);
        i.setDetail(first.getReturnType() + " " + first);
        CompletionData data = data(task, first, overloads.size());
        i.setData(data);
        if (addParens) {
            if (overloads.size() == 1 && first.getParameters().isEmpty()) {
                i.setInsertText(first.getSimpleName() + "()$0");
            } else {
                i.setInsertText(first.getSimpleName() + "($0)");
                i.setCommand(
                        new Command(
                                "Trigger Parameter Hints", "editor.action.triggerParameterHints"));
            }
            i.setInsertTextFormat(InsertTextFormat.SNIPPET); // Snippet
        }
        return i;
    }

    /**
     * Override the given method if it is overridable.
     *
     * @param task The compilation task.
     * @param parentPath The tree path of the parent class.
     * @param method The method to override if possible.
     * @param endsWithParen Does the statement at cursor ends with a parenthesis?
     * @return The completion item.
     */
    @NonNull
    private CompletionItem overrideIfPossible(
            @NonNull CompileTask task,
            TreePath parentPath,
            @NonNull ExecutableElement method,
            boolean endsWithParen) {

        if (parentPath.getLeaf().getKind() != Tree.Kind.CLASS) {
            // Can only override if the cursor is directly in a class declaration
            return method(task, Collections.singletonList(method), !endsWithParen);
        }

        final Types types = task.task.getTypes();
        final Element parentElement = Trees.instance(task.task).getElement(parentPath);

        if (parentElement == null) {
            // Can't get further information for overriding this method
            return method(task, Collections.singletonList(method), !endsWithParen);
        }

        final DeclaredType type = (DeclaredType) parentElement.asType();
        final Element enclosing = method.getEnclosingElement();

        boolean isFinalClass = enclosing.getModifiers().contains(Modifier.FINAL);
        boolean isNotOverridable =
                method.getModifiers().contains(Modifier.STATIC)
                        || method.getModifiers().contains(Modifier.FINAL)
                        || method.getModifiers().contains(Modifier.PRIVATE);
        if (isFinalClass
                || isNotOverridable
                || !types.isAssignable(type, enclosing.asType())
                || !(parentPath.getLeaf() instanceof ClassTree)) {
            // Override is not possible
            return method(task, Collections.singletonList(method), !endsWithParen);
        }

        // Print the method details and the annotations
        final int indent = EditHelper.indent(FileStore.contents(completingFile), (int) this.cursor);
        final MethodSpec.Builder builder = MethodSpec.overriding(method, type, types);
        final List<? extends AnnotationMirror> mirrors = method.getAnnotationMirrors();
        if (mirrors != null && !mirrors.isEmpty()) {
            for (final AnnotationMirror mirror : mirrors) {
                builder.addAnnotation(AnnotationSpec.get(mirror));
            }
        }

        boolean addComment = true;
        // Add super call if the method is not abstract
        if (!method.getModifiers().contains(Modifier.ABSTRACT)) {
            if (method.getReturnType() instanceof NoType) {
                builder.addStatement(createSuperCall(builder));
            } else {
                addComment = false;
                builder.addComment("TODO: Implement this method");
                builder.addStatement("return " + createSuperCall(builder));
            }
        }

        if (addComment) {
            builder.addComment("TODO: Implement this method");
        }

        final MethodSpec built = builder.build();
        String insertText = built.toString();
        insertText = insertText.replace("\n", "\n" + repeatSpaces(indent));

        // TODO Auto-import required classes instead of specifying qualified names.
        CompletionItem item = new CompletionItem();
        item.setLabel(built.name);
        item.setKind(CompletionItemKind.METHOD);
        item.setDetail(method.getReturnType() + " " + method);
        item.setInsertText(insertText);
        item.setInsertTextFormat(InsertTextFormat.SNIPPET);
        item.setData(data(task, method, 1));
        return item;
    }

    /**
     * Create a superclass method invocation statement.
     *
     * @param builder The method builder.
     * @return The super invocation statement string without ending ';'.
     */
    private String createSuperCall(MethodSpec.Builder builder) {
        final StringBuilder sb = new StringBuilder();
        sb.append("super.");
        sb.append(builder.name);
        sb.append("(");
        for (int i = 0; i < builder.parameters.size(); i++) {
            sb.append(builder.parameters.get(i).name);
            if (i != builder.parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return null;
    }

    @Nullable
    private CompletionData data(CompileTask task, Element element, int overloads) {
        CompletionData data = new CompletionData();
        if (element instanceof TypeElement) {
            TypeElement type = (TypeElement) element;
            data.setClassName(type.getQualifiedName().toString());
        } else if (element.getKind() == ElementKind.FIELD) {
            VariableElement field = (VariableElement) element;
            TypeElement type = (TypeElement) field.getEnclosingElement();
            data.setClassName(type.getQualifiedName().toString());
            data.setMemberName(field.getSimpleName().toString());
        } else if (element instanceof ExecutableElement) {
            Types types = task.task.getTypes();
            ExecutableElement method = (ExecutableElement) element;
            TypeElement type = (TypeElement) method.getEnclosingElement();
            data.setClassName(type.getQualifiedName().toString());
            data.setMemberName(method.getSimpleName().toString());
            data.setErasedParameterTypes(new String[method.getParameters().size()]);
            for (int i = 0; i < data.getErasedParameterTypes().length; i++) {
                TypeMirror p = method.getParameters().get(i).asType();
                data.getErasedParameterTypes()[i] = types.erasure(p).toString();
            }
            data.setPlusOverloads(overloads - 1);
        } else {
            return null;
        }
        return data;
    }

    @NonNull
    private CompletionItemKind kind(@NonNull Element e) {
        switch (e.getKind()) {
            case ANNOTATION_TYPE:
                return CompletionItemKind.ANNOTATION_TYPE;
            case CLASS:
                return CompletionItemKind.CLASS;
            case CONSTRUCTOR:
                return CompletionItemKind.CONSTRUCTOR;
            case ENUM:
                return CompletionItemKind.ENUM;
            case ENUM_CONSTANT:
                return CompletionItemKind.ENUM_MEMBER;
            case EXCEPTION_PARAMETER:
            case PARAMETER:
                return CompletionItemKind.PROPERTY;
            case FIELD:
                return CompletionItemKind.FIELD;
            case STATIC_INIT:
            case INSTANCE_INIT:
                return CompletionItemKind.FUNCTION;
            case INTERFACE:
                return CompletionItemKind.INTERFACE;
            case LOCAL_VARIABLE:
            case RESOURCE_VARIABLE:
                return CompletionItemKind.VARIABLE;
            case METHOD:
                return CompletionItemKind.METHOD;
            case PACKAGE:
                return CompletionItemKind.MODULE;
            case TYPE_PARAMETER:
                return CompletionItemKind.TYPE_PARAMETER;
            case OTHER:
            default:
                return CompletionItemKind.NONE;
        }
    }

    @NonNull
    private CompletionItem keyword(String keyword) {
        CompletionItem i = new CompletionItem();
        i.setLabel(keyword);
        i.setKind(CompletionItemKind.KEYWORD);
        i.setDetail("keyword");
        i.setSortText(String.format(Locale.getDefault(), "%02d%s", Priority.KEYWORD, i.getLabel()));
        return i;
    }

    private void logCompletionTiming(Instant started, List<?> list, boolean isIncomplete) {
        long elapsedMs = Duration.between(started, Instant.now()).toMillis();
        if (isIncomplete) {
            LOG.info(
                    String.format(
                            Locale.getDefault(),
                            "Found %d items (incomplete) in %,d ms",
                            list.size(),
                            elapsedMs));
        } else {
            LOG.info(
                    String.format(
                            Locale.getDefault(),
                            "...found %d items in %,d ms",
                            list.size(),
                            elapsedMs));
        }
    }

    @NonNull
    private CharSequence simpleName(@NonNull String className) {
        int dot = className.lastIndexOf('.');
        if (dot == -1) return className;
        return className.subSequence(dot + 1, className.length());
    }

    @SuppressWarnings("unused")
    private static class Priority {
        static int iota = 0;
        static final int SNIPPET = iota;
        static final int LOCAL = iota++;
        static final int FIELD = iota++;
        static final int INHERITED_FIELD = iota++;
        static final int METHOD = iota++;
        static final int INHERITED_METHOD = iota++;
        static final int OBJECT_METHOD = iota++;
        static final int INNER_CLASS = iota++;
        static final int INHERITED_INNER_CLASS = iota++;
        static final int IMPORTED_CLASS = iota++;
        static final int NOT_IMPORTED_CLASS = iota++;
        static final int KEYWORD = iota++;
        static final int PACKAGE_MEMBER = iota++;
        static final int CASE_LABEL = iota++;
    }
}
