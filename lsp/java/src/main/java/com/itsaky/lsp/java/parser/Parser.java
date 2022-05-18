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

package com.itsaky.lsp.java.parser;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.java.compiler.SourceFileManager;
import com.itsaky.lsp.java.compiler.SourceFileObject;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.util.StringUtils;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

public class Parser {

  private static final JavaCompiler COMPILER = JavacTool.create();
  private static final SourceFileManager FILE_MANAGER = new SourceFileManager();

  /** Create a task that compiles a single file */
  private static JavacTask singleFileTask(JavaFileObject file) {
    return (JavacTask)
        COMPILER.getTask(
            null,
            FILE_MANAGER,
            Parser::ignoreError,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.singletonList(file));
  }

  public final JavaFileObject file;
  public final String contents;
  public final JavacTask task;
  public final CompilationUnitTree root;
  public final Trees trees;

  private Parser(JavaFileObject file) {
    this.file = file;
    try {
      this.contents = file.getCharContent(false).toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.task = singleFileTask(file);
    try {
      this.root = task.parse().iterator().next();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.trees = Trees.instance(task);
  }

  public static Parser parseFile(Path file) {
    return parseJavaFileObject(new SourceFileObject(file));
  }

  private static Parser cachedParse;
  private static long cachedModified = -1;

  private static boolean needsParse(JavaFileObject file) {
    if (cachedParse == null) {
      return true;
    }
    if (!cachedParse.file.equals(file)) {
      return true;
    }

    return file.getLastModified() > cachedModified;
  }

  private static void loadParse(JavaFileObject file) {
    cachedParse = new Parser(file);
    cachedModified = file.getLastModified();
  }

  public static Parser parseJavaFileObject(JavaFileObject file) {
    if (needsParse(file)) {
      loadParse(file);
    } else {
      LOG.info("...using cached parse");
    }
    return cachedParse;
  }

  public Set<Name> packagePrivateClasses() {
    Set<Name> result = new HashSet<>();
    for (Tree t : root.getTypeDecls()) {
      if (t instanceof ClassTree) {
        ClassTree c = (ClassTree) t;
        boolean isPublic = c.getModifiers().getFlags().contains(Modifier.PUBLIC);
        if (!isPublic) {
          result.add(c.getSimpleName());
        }
      }
    }
    return result;
  }

  public static Range range(JavacTask task, CharSequence contents, TreePath path) {
    // Find start position
    Trees trees = Trees.instance(task);
    SourcePositions pos = trees.getSourcePositions();
    CompilationUnitTree root = path.getCompilationUnit();
    LineMap lines = root.getLineMap();
    int start = (int) pos.getStartPosition(root, path.getLeaf());
    int end = (int) pos.getEndPosition(root, path.getLeaf());

    // If start is -1, give up
    if (start == -1) {
      LOG.warn(String.format("Couldn't locate `%s`", path.getLeaf()));
      return Range.NONE;
    }
    // If end is bad, guess based on start
    if (end == -1) {
      end = start + path.getLeaf().toString().length();
    }

    if (path.getLeaf() instanceof ClassTree) {
      ClassTree cls = (ClassTree) path.getLeaf();

      // If class has annotations, skip over them
      if (!cls.getModifiers().getAnnotations().isEmpty()) {
        start = (int) pos.getEndPosition(root, cls.getModifiers());
      }

      // Find position of class name
      String name = cls.getSimpleName().toString();
      start = indexOf(contents, name, start);
      if (start == -1) {
        LOG.warn(String.format("Couldn't find identifier `%s` in `%s`", name, path.getLeaf()));
        return Range.NONE;
      }
      end = start + name.length();
    }
    if (path.getLeaf() instanceof MethodTree) {
      MethodTree method = (MethodTree) path.getLeaf();

      // If method has annotations, skip over them
      if (!method.getModifiers().getAnnotations().isEmpty()) {
        start = (int) pos.getEndPosition(root, method.getModifiers());
      }

      // Find position of method name
      String name = method.getName().toString();
      if (name.equals("<init>")) {
        name = className(path);
      }
      start = indexOf(contents, name, start);
      if (start == -1) {
        LOG.warn(String.format("Couldn't find identifier `%s` in `%s`", name, path.getLeaf()));
        return Range.NONE;
      }
      end = start + name.length();
    }
    if (path.getLeaf() instanceof VariableTree) {
      VariableTree field = (VariableTree) path.getLeaf();

      // If field has annotations, skip over them
      if (!field.getModifiers().getAnnotations().isEmpty()) {
        start = (int) pos.getEndPosition(root, field.getModifiers());
      }

      // Find position of method name
      String name = field.getName().toString();
      start = indexOf(contents, name, start);
      if (start == -1) {
        LOG.warn(String.format("Couldn't find identifier `%s` in `%s`", name, path.getLeaf()));
        return Range.NONE;
      }
      end = start + name.length();
    }
    if (path.getLeaf() instanceof MemberSelectTree) {
      MemberSelectTree member = (MemberSelectTree) path.getLeaf();
      String name = member.getIdentifier().toString();
      start = indexOf(contents, name, start);
      if (start == -1) {
        LOG.warn(String.format("Couldn't find identifier `%s` in `%s`", name, path.getLeaf()));
        return Range.NONE;
      }
      end = start + name.length();
    }
    int startLine = (int) lines.getLineNumber(start);
    int startCol = (int) lines.getColumnNumber(start);
    int endLine = (int) lines.getLineNumber(end);
    int endCol = (int) lines.getColumnNumber(end);

    return new Range(
        new Position(startLine - 1, startCol - 1), new Position(endLine - 1, endCol - 1));
  }

  private static int indexOf(CharSequence contents, String name, int start) {
    Matcher matcher = Pattern.compile("\\b" + name + "\\b").matcher(contents);
    if (matcher.find(start)) {
      return matcher.start();
    }
    return -1;
  }

  private static void ignoreError(javax.tools.Diagnostic<? extends JavaFileObject> __) {
    // Too noisy, this only comes up in parse tasks which tend to be less important
    // LOG.warning(err.getMessage(Locale.getDefault()));
  }

  static String describeTree(Tree leaf) {
    if (leaf instanceof MethodTree) {
      MethodTree method = (MethodTree) leaf;
      StringJoiner params = new StringJoiner(", ");
      for (VariableTree p : method.getParameters()) {
        params.add(p.getType() + " " + p.getName());
      }
      return method.getName() + "(" + params + ")";
    }
    if (leaf instanceof ClassTree) {
      ClassTree cls = (ClassTree) leaf;
      return "class " + cls.getSimpleName();
    }
    if (leaf instanceof BlockTree) {
      BlockTree block = (BlockTree) leaf;
      return String.format(Locale.ROOT, "{ ...%d lines... }", block.getStatements().size());
    }
    return leaf.toString();
  }

  public List<String> accessibleClasses(String partialName, String fromPackage) {
    String toPackage = Objects.toString(root.getPackageName(), "");
    boolean samePackage = fromPackage.equals(toPackage) || toPackage.isEmpty();
    List<String> result = new ArrayList<String>();
    for (Tree t : root.getTypeDecls()) {
      if (!(t instanceof ClassTree)) {
        continue;
      }
      ClassTree cls = (ClassTree) t;
      // If class is not accessible, skip it
      boolean isPublic = cls.getModifiers().getFlags().contains(Modifier.PUBLIC);
      if (!samePackage && !isPublic) {
        continue;
      }
      // If class doesn't match partialName, skip it
      String name = cls.getSimpleName().toString();
      if (!StringUtils.matchesPartialName(name, partialName)) {
        continue;
      }
      if (root.getPackageName() != null) {
        name = root.getPackageName() + "." + name;
      }
      result.add(name);
    }
    return result;
  }

  private static String prune(
      CompilationUnitTree root,
      SourcePositions pos,
      StringBuilder buffer,
      long[] offsets,
      boolean eraseAfterCursor) {
    class Scan extends TreeScanner<Void, Void> {
      boolean erasedAfterCursor = !eraseAfterCursor;

      boolean containsCursor(Tree node) {
        long start = pos.getStartPosition(root, node);
        long end = pos.getEndPosition(root, node);
        for (long cursor : offsets) {
          if (start <= cursor && cursor <= end) {
            return true;
          }
        }
        return false;
      }

      boolean anyContainsCursor(Collection<? extends Tree> nodes) {
        for (Tree n : nodes) {
          if (containsCursor(n)) {
            return true;
          }
        }
        return false;
      }

      long lastCursorIn(Tree node) {
        long start = pos.getStartPosition(root, node);
        long end = pos.getEndPosition(root, node);
        long last = -1;
        for (long cursor : offsets) {
          if (start <= cursor && cursor <= end) {
            last = cursor;
          }
        }
        if (last == -1) {
          throw new RuntimeException(
              String.format(
                  "No cursor in %s is between %d and %d", Arrays.toString(offsets), start, end));
        }
        return last;
      }

      @Override
      public Void visitImport(ImportTree node, Void __) {
        // Erase 'static' keyword so autocomplete works better
        if (containsCursor(node) && node.isStatic()) {
          int start = (int) pos.getStartPosition(root, node);
          start = buffer.indexOf("static", start);
          int end = start + "static".length();
          erase(buffer, start, end);
        }
        return super.visitImport(node, null);
      }

      @Override
      public Void visitSwitch(SwitchTree node, Void __) {
        if (containsCursor(node)) {
          // Prevent the enclosing block from erasing the closing } of the switch
          erasedAfterCursor = true;
        }
        return super.visitSwitch(node, null);
      }

      @Override
      public Void visitBlock(BlockTree node, Void __) {
        if (containsCursor(node)) {
          super.visitBlock(node, null);
          // When we find the deepest block that includes the cursor
          if (!erasedAfterCursor) {
            long start = lastCursorIn(node);
            long end = pos.getEndPosition(root, node);
            if (end >= buffer.length()) {
              end = buffer.length() - 1;
            }
            // Find the next line
            while (start < end && buffer.charAt((int) start) != '\n') start++;
            // Find the end of the block
            while (end > start && buffer.charAt((int) end) != '}') end--;
            // Erase from next line to end of block
            erase(buffer, start, end - 1);
            erasedAfterCursor = true;
          }
        } else if (!node.getStatements().isEmpty()) {
          StatementTree first = node.getStatements().get(0);
          StatementTree last = node.getStatements().get(node.getStatements().size() - 1);
          long start = pos.getStartPosition(root, first);
          long end = pos.getEndPosition(root, last);
          if (end >= buffer.length()) {
            end = buffer.length() - 1;
          }
          erase(buffer, start, end);
        }
        return null;
      }

      @Override
      public Void visitErroneous(ErroneousTree node, Void nothing) {
        return super.scan(node.getErrorTrees(), nothing);
      }
    }

    new Scan().scan(root, null);

    return buffer.toString();
  }

  private static void erase(StringBuilder buffer, long start, long end) {
    for (int i = (int) start; i < end; i++) {
      switch (buffer.charAt(i)) {
        case '\r':
        case '\n':
          break;
        default:
          buffer.setCharAt(i, ' ');
      }
    }
  }

  String prune(long cursor) {
    SourcePositions pos = Trees.instance(task).getSourcePositions();
    StringBuilder buffer = new StringBuilder(contents);
    long[] cursors = {cursor};
    return prune(root, pos, buffer, cursors, true);
  }

  static Optional<Path> declaringFile(Element e) {
    // Find top-level type surrounding `to`
    LOG.info(String.format("...looking up declaring file of `%s`...", e));
    Optional<TypeElement> top = topLevelDeclaration(e);
    if (!top.isPresent()) {
      LOG.warn("...no top-level type!");
      return Optional.empty();
    }

    // Find file by looking at package and class name
    LOG.info(String.format("...top-level type is %s", top.get()));
    Optional<Path> file = FileStore.findDeclaringFile(top.get());
    if (!file.isPresent()) {
      LOG.info("...couldn't find declaring file for type");
      return Optional.empty();
    }
    return file;
  }

  private static Optional<TypeElement> topLevelDeclaration(Element e) {
    if (e == null) {
      return Optional.empty();
    }
    Element parent = e;
    TypeElement result = null;
    while (parent.getEnclosingElement() != null) {
      if (parent instanceof TypeElement) {
        result = (TypeElement) parent;
      }
      parent = parent.getEnclosingElement();
    }
    return Optional.ofNullable(result);
  }

  static String className(TreePath t) {
    while (t != null) {
      if (t.getLeaf() instanceof ClassTree) {
        ClassTree cls = (ClassTree) t.getLeaf();
        return cls.getSimpleName().toString();
      }
      t = t.getParentPath();
    }
    return "";
  }

  private static final ILogger LOG = ILogger.newInstance("JavaParser");
}
