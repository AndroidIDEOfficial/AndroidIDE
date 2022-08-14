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

package com.itsaky.androidide.lsp.java.providers;

import static com.itsaky.androidide.progress.ProgressManager.abortIfCancelled;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ReflectUtils;
import com.itsaky.androidide.lsp.api.AbstractServiceProvider;
import com.itsaky.androidide.lsp.api.ICompletionProvider;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.lsp.internal.model.CachedCompletion;
import com.itsaky.androidide.lsp.java.compiler.CompileTask;
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService;
import com.itsaky.androidide.lsp.java.compiler.SourceFileObject;
import com.itsaky.androidide.lsp.java.compiler.SynchronizedTask;
import com.itsaky.androidide.lsp.java.models.CompilationRequest;
import com.itsaky.androidide.lsp.java.models.PartialReparseRequest;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.providers.completion.IJavaCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.IdentifierCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.ImportCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.KeywordCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.MemberReferenceCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.MemberSelectCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.SwitchConstantCompletionProvider;
import com.itsaky.androidide.lsp.java.providers.completion.TopLevelSnippetsProvider;
import com.itsaky.androidide.lsp.java.utils.ASTFixer;
import com.itsaky.androidide.lsp.java.visitors.FindCompletionsAt;
import com.itsaky.androidide.lsp.java.visitors.PruneMethodBodies;
import com.itsaky.androidide.lsp.models.CompletionParams;
import com.itsaky.androidide.lsp.models.CompletionResult;
import com.itsaky.androidide.utils.DocumentUtils;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.StopWatch;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CompletionProvider extends AbstractServiceProvider implements ICompletionProvider {

  public static final int MAX_COMPLETION_ITEMS = CompletionResult.MAX_ITEMS;
  private static final ILogger LOG = ILogger.newInstance("JavaCompletionProvider");
  private final AtomicBoolean completing = new AtomicBoolean(false);
  private JavaCompilerService compiler;
  private CachedCompletion cache;
  private Consumer<CachedCompletion> nextCacheConsumer;

  public CompletionProvider() {
    super();
  }

  public synchronized CompletionProvider reset(
      JavaCompilerService compiler,
      IServerSettings settings,
      CachedCompletion cache,
      Consumer<CachedCompletion> nextCacheConsumer) {
    this.compiler = compiler;
    this.cache = cache;
    this.nextCacheConsumer = nextCacheConsumer;

    super.applySettings(settings);
    return this;
  }
  
  @Override
  public boolean canComplete(Path file) {
    return ICompletionProvider.super.canComplete(file) && DocumentUtils.isJavaFile(file);
  }

  @NonNull
  @Override
  public CompletionResult complete(@NonNull CompletionParams params) {
    if (compiler.getSynchronizedTask().isCompiling()) {
      return CompletionResult.EMPTY;
    }

    completing.set(true);
    try {
      abortIfCancelled();
      abortCompletionIfCancelled();
      return completeInternal(params);
    } finally {
      completing.set(false);
    }
  }

  @NonNull
  private CompletionResult completeInternal(final @NonNull CompletionParams params) {
    Path file = params.getFile();
    int line = params.getPosition().getLine();
    int column = params.getPosition().getColumn();
    LOG.info("Complete at " + file.getFileName() + "(" + line + "," + column + ")...");

    // javac expects 1-based line and column indexes
    line++;
    column++;

    Instant started = Instant.now();

    if (this.cache != null && this.cache.canUseCache(params)) {
      final String prefix = params.requirePrefix();
      final String partial = partialIdentifier(prefix, prefix.length());
      final CompletionResult result = CompletionResult.filter(this.cache.getResult(), partial);

      result.markCached();

      if (!result.isIncomplete() && !result.getItems().isEmpty()) {
        LOG.info("...using cached completion");
        logCompletionDuration(started, result);
        return result;
      } else {
        LOG.info("...cached completions are empty");
      }
    } else {
      LOG.info("...cannot use cached completions");
    }

    abortIfCancelled();
    abortCompletionIfCancelled();
    final StopWatch watch = new StopWatch("Prune method bodies");
    ParseTask task = compiler.parse(file);

    abortIfCancelled();
    abortCompletionIfCancelled();
    long cursor = task.root.getLineMap().getPosition(line, column);
    StringBuilder pruned = new PruneMethodBodies(task.task).scan(task.root, cursor);
    watch.log();
    int endOfLine = endOfLine(pruned, (int) cursor);
    pruned.insert(endOfLine, ';');

    final CharSequence contents;
    if (compiler.compiler.currentContext != null) {
      abortIfCancelled();
      abortCompletionIfCancelled();
      contents = new ASTFixer(compiler.compiler.currentContext).fix(pruned);
    } else {
      contents = pruned.toString();
    }

    final String contentString = contents.toString();
    final PartialReparseRequest partialRequest =
        new PartialReparseRequest(cursor - params.requirePrefix().length(), contentString);
    abortIfCancelled();
    abortCompletionIfCancelled();
    CompletionResult result = compileAndComplete(file, contentString, cursor, partialRequest);
    if (result == null) {
      result = CompletionResult.EMPTY;
    }

    abortIfCancelled();
    abortCompletionIfCancelled();
    new TopLevelSnippetsProvider().complete(task, result);
    logCompletionDuration(started, result);

    abortIfCancelled();
    abortCompletionIfCancelled();
    if (this.nextCacheConsumer != null) {
      this.nextCacheConsumer.accept(CachedCompletion.cache(params, result));
    }

    return result;
  }

  @NonNull
  private String partialIdentifier(String contents, int end) {
    int start = end;
    while (start > 0 && Character.isJavaIdentifierPart(contents.charAt(start - 1))) {
      start--;
    }
    return contents.substring(start, end);
  }

  private void logCompletionDuration(Instant started, @NonNull CompletionResult result) {
    long elapsedMs = Duration.between(started, Instant.now()).toMillis();
    LOG.info(
        String.format(
            Locale.US,
            "Found %d items%s%sin %,d ms",
            result.getItems().size(),
            result.isIncomplete() ? " (incomplete) " : "",
            result.isCached() ? " (cached) " : " ",
            elapsedMs));
  }

  private int endOfLine(@NonNull CharSequence contents, int cursor) {
    while (cursor < contents.length()) {
      char c = contents.charAt(cursor);
      if (c == '\r' || c == '\n') break;
      cursor++;
    }
    return cursor;
  }

  private CompletionResult compileAndComplete(
      Path file, String contents, final long cursor, PartialReparseRequest partialRequest) {
    final Instant started = Instant.now();
    final SourceFileObject source = new SourceFileObject(file, contents, Instant.now());
    final String partial = partialIdentifier(contents, (int) cursor);
    final boolean endsWithParen = endsWithParen(contents, (int) cursor);

    abortIfCancelled();
    abortCompletionIfCancelled();

    final CompilationRequest request =
        new CompilationRequest(Collections.singletonList(source), partialRequest);
    SynchronizedTask synchronizedTask = compiler.compile(request);
    return synchronizedTask.get(
        task -> {
          if (task == null || task.task == null || task.task.getContext() == null) {
            return CompletionResult.EMPTY;
          }
          abortIfCancelled();
          abortCompletionIfCancelled();
          LOG.info("...compiled in " + Duration.between(started, Instant.now()).toMillis() + "ms");
          TreePath path = new FindCompletionsAt(task.task).scan(task.root(), cursor);

          abortIfCancelled();
          abortCompletionIfCancelled();
          String newPartial = partial;
          if (path.getLeaf().getKind() == Tree.Kind.IMPORT) {
            newPartial = qualifiedPartialIdentifier(contents, (int) cursor);
            if (newPartial.endsWith(ASTFixer.IDENT)) {
              newPartial = newPartial.substring(0, newPartial.length() - ASTFixer.IDENT.length());
            }
          }

          return doComplete(file, contents, cursor, newPartial, endsWithParen, task, path);
        });
  }

  @NonNull
  private CompletionResult doComplete(
      final Path file,
      final String contents,
      final long cursor,
      final String partial,
      final boolean endsWithParen,
      final CompileTask task,
      final TreePath path) {
    final Class<? extends IJavaCompletionProvider> klass;
    abortIfCancelled();
    abortCompletionIfCancelled();
    switch (path.getLeaf().getKind()) {
      case IDENTIFIER:
        klass = IdentifierCompletionProvider.class;
        break;
      case MEMBER_SELECT:
        klass = MemberSelectCompletionProvider.class;
        break;
      case MEMBER_REFERENCE:
        klass = MemberReferenceCompletionProvider.class;
        break;
      case SWITCH:
        klass = SwitchConstantCompletionProvider.class;
        break;
      case IMPORT:
        klass = ImportCompletionProvider.class;
        break;
      default:
        klass = KeywordCompletionProvider.class;
        break;
    }

    final IJavaCompletionProvider provider =
        ReflectUtils.reflect(klass).newInstance(file, cursor, compiler, getSettings()).get();

    if (provider instanceof ImportCompletionProvider) {
      ((ImportCompletionProvider) provider)
          .setImportPath(qualifiedPartialIdentifier(contents, (int) cursor));
    }

    abortIfCancelled();
    abortCompletionIfCancelled();
    return provider.complete(task, path, partial, endsWithParen);
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
}
