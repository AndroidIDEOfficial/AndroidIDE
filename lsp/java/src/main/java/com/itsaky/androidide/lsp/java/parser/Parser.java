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

package com.itsaky.androidide.lsp.java.parser;

import com.itsaky.androidide.lsp.java.compiler.SourceFileManager;
import com.itsaky.androidide.lsp.java.compiler.SourceFileObject;
import com.itsaky.androidide.models.Position;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.utils.ILogger;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.LineMap;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.api.JavacTool;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;

public class Parser {

  private static final JavaCompiler COMPILER = JavacTool.create();
  private static SourceFileManager FILE_MANAGER = SourceFileManager.NO_MODULE;
  private static final ILogger LOG = ILogger.newInstance("JavaParser");
  private static Parser cachedParse;
  private static long cachedModified = -1;
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

  /** Create a task that compiles a single file */
  private static JavacTask singleFileTask(JavaFileObject file) {
    final ModuleProject module = ProjectManager.INSTANCE.findModuleForFile(Paths.get(file.toUri()));
    if (module != null) {
      FILE_MANAGER = SourceFileManager.forModule(module);
    }

    return (JavacTask)
        COMPILER.getTask(
            null,
            FILE_MANAGER,
            Parser::ignoreError,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.singletonList(file));
  }

  private static void ignoreError(javax.tools.Diagnostic<? extends JavaFileObject> __) {
    // Too noisy, this only comes up in parse tasks which tend to be less important
    // LOG.warning(err.getMessage(Locale.getDefault()));
  }

  public static Parser parseFile(Path file) {
    return parseJavaFileObject(new SourceFileObject(file));
  }

  public static Parser parseJavaFileObject(JavaFileObject file) {
    if (needsParse(file)) {
      loadParse(file);
    }
    return cachedParse;
  }

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
}
