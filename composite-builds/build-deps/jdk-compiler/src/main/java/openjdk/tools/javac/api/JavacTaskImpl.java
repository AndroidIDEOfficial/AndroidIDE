/*
 * Copyright (c) 2005, 2021, Oracle and/or its affiliates. All rights reserved.
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

package openjdk.tools.javac.api;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import jdkx.annotation.processing.Processor;
import jdkx.lang.model.element.Element;
import jdkx.lang.model.element.TypeElement;
import jdkx.tools.*;

import openjdk.source.tree.*;
import openjdk.source.util.SourcePositions;
import openjdk.tools.javac.code.*;
import openjdk.tools.javac.code.DeferredCompletionFailureHandler.Handler;
import openjdk.tools.javac.code.Symbol.ClassSymbol;
import openjdk.tools.javac.comp.*;
import openjdk.tools.javac.file.BaseFileManager;
import openjdk.tools.javac.main.*;
import openjdk.tools.javac.main.JavaCompiler;
import openjdk.tools.javac.parser.JavacParser;
import openjdk.tools.javac.parser.Parser;
import openjdk.tools.javac.parser.ParserFactory;
import openjdk.tools.javac.processing.AnnotationProcessingError;
import openjdk.tools.javac.tree.*;
import openjdk.tools.javac.tree.JCTree.JCBlock;
import openjdk.tools.javac.tree.JCTree.JCClassDecl;
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit;
import openjdk.tools.javac.tree.JCTree.JCExpression;
import openjdk.tools.javac.tree.JCTree.JCModuleDecl;
import openjdk.tools.javac.tree.JCTree.JCStatement;
import openjdk.tools.javac.tree.JCTree.Tag;
import openjdk.tools.javac.util.*;
import openjdk.tools.javac.util.DefinedBy.Api;
import openjdk.tools.javac.util.List;
import openjdk.tools.javac.util.Log.PrefixKind;
import openjdk.tools.javac.util.Log.WriterKind;

/**
 * Provides access to functionality specific to the JDK Java Compiler, javac.
 *
 * <p>
 * <b>This is NOT part of any supported API.
 * If you write code that depends on this, you do so at your own
 * risk. This code and its internal interfaces are subject to change
 * or deletion without notice.</b>
 * </p>
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 */
public class JavacTaskImpl extends BasicJavacTask {
    private final Arguments args;
    private final ClientCodeWrapper ccw;
    private JavaCompiler compiler;
    private JavaFileManager fileManager;
    private DeferredCompletionFailureHandler dcfh;
    private Locale locale;
    private Map<JavaFileObject, JCCompilationUnit> notYetEntered;
    private ListBuffer<Env<AttrContext>> genList;
    private final AtomicBoolean used = new AtomicBoolean();
    private Iterable<? extends Processor> processors;
    private ListBuffer<String> addModules = new ListBuffer<>();

    protected JavacTaskImpl(Context context) {
        super(context, true);
        args = Arguments.instance(context);
        ccw = ClientCodeWrapper.instance(context);
        fileManager = context.get(JavaFileManager.class);
        dcfh = DeferredCompletionFailureHandler.instance(context);
        dcfh.setHandler(dcfh.userCodeHandler);
    }

    @Override
    @DefinedBy(Api.COMPILER)
    public Boolean call() {
        return doCall().isOK();
    }

    /* Internal version of call exposing Main.Result. */
    public Main.Result doCall() {
        try {
            Pair<Main.Result, Throwable> result = invocationHelper(() -> {
                prepareCompiler(false);
                if (compiler.errorCount() > 0)
                    return Main.Result.ERROR;
                compiler.compile(args.getFileObjects(), args.getClassNames(), processors, addModules);
                return (compiler.errorCount() > 0) ? Main.Result.ERROR : Main.Result.OK; // FIXME?
            });
            if (result.snd == null) {
                return result.fst;
            } else {
                return (result.snd instanceof FatalError) ? Main.Result.SYSERR : Main.Result.ABNORMAL;
            }
        } finally {
            try {
                cleanup();
            } catch (ClientCodeException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    @Override
    @DefinedBy(Api.COMPILER)
    public void addModules(Iterable<String> moduleNames) {
        Objects.requireNonNull(moduleNames);
        // not mt-safe
        if (used.get())
            throw new IllegalStateException();
        for (String m : moduleNames) {
            Objects.requireNonNull(m);
            addModules.add(m);
        }
    }

    @Override
    @DefinedBy(Api.COMPILER)
    public void setProcessors(Iterable<? extends Processor> processors) {
        Objects.requireNonNull(processors);
        // not mt-safe
        if (used.get())
            throw new IllegalStateException();
        this.processors = processors;
    }

    public Iterable<? extends Processor> getProcessors() {
        return processors;
    }

    @Override
    @DefinedBy(Api.COMPILER)
    public void setLocale(Locale locale) {
        if (used.get())
            throw new IllegalStateException();
        this.locale = locale;
    }

    private <T> Pair<T, Throwable> invocationHelper(Callable<T> c) {
        Handler prevDeferredHandler = dcfh.setHandler(dcfh.javacCodeHandler);
        try {
            return new Pair<>(c.call(), null);
        } catch (FatalError ex) {
            Log log = Log.instance(context);
            Options options = Options.instance(context);
            log.printRawLines(ex.getMessage());
            if (ex.getCause() != null && options.isSet("dev")) {
                ex.getCause().printStackTrace(log.getWriter(WriterKind.NOTICE));
            }
            return new Pair<>(null, ex);
        } catch (AnnotationProcessingError | ClientCodeException e) {
            // AnnotationProcessingError is thrown from JavacProcessingEnvironment,
            // to forward errors thrown from an annotation processor
            // ClientCodeException is thrown from ClientCodeWrapper,
            // to forward errors thrown from user-supplied code for Compiler API
            // as specified by jdkx.tools.JavaCompiler#getTask
            // and jdkx.tools.JavaCompiler.CompilationTask#call
            throw new RuntimeException(e.getCause());
        } catch (PropagatedException e) {
            throw e.getCause();
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception | Error ex) {
            // Nasty. If we've already reported an error, compensate
            // for buggy compiler error recovery by swallowing thrown
            // exceptions.
            if (compiler == null || compiler.errorCount() == 0
                    || Options.instance(context).isSet("dev")) {
                Log log = Log.instance(context);
                log.printLines(PrefixKind.JAVAC, "msg.bug", JavaCompiler.version());
                ex.printStackTrace(log.getWriter(WriterKind.NOTICE));
            }
            return new Pair<>(null, ex);
        } finally {
            dcfh.setHandler(prevDeferredHandler);
        }
    }

    private void prepareCompiler(boolean forParse) {
        if (used.getAndSet(true)) {
            if (compiler == null)
                throw new PropagatedException(new IllegalStateException());
        } else {
            args.validate();

            // initialize compiler's default locale
            context.put(Locale.class, locale);

            // hack
            JavacMessages messages = context.get(JavacMessages.messagesKey);
            if (messages != null && !messages.getCurrentLocale().equals(locale))
                messages.setCurrentLocale(locale);

            initPlugins(args.getPluginOpts());
            initDocLint(args.getDocLintOpts());

            // init JavaCompiler and queues
            compiler = JavaCompiler.instance(context);
            compiler.keepComments = true;
            compiler.genEndPos = true;
            notYetEntered = new HashMap<>();
            if (forParse) {
                compiler.initProcessAnnotations(processors, args.getFileObjects(), args.getClassNames());
                compiler.initNotYetEntered(notYetEntered);
                for (JavaFileObject file : args.getFileObjects())
                    notYetEntered.put(file, null);
                genList = new ListBuffer<>();
            }
        }
    }

    <T> String toString(Iterable<T> items, String sep) {
        String currSep = "";
        StringBuilder sb = new StringBuilder();
        for (T item : items) {
            sb.append(currSep);
            sb.append(item.toString());
            currSep = sep;
        }
        return sb.toString();
    }

    public void cleanup() {
        if (compiler != null)
            compiler.close();
        if (fileManager instanceof BaseFileManager && ((BaseFileManager) fileManager).autoClose) {
            try {
                fileManager.close();
            } catch (IOException ignore) {
            }
        }
        compiler = null;
        context = null;
        notYetEntered = null;
    }

    public Iterable<? extends CompilationUnitTree> parse(JavaFileObject... files) throws IOException {
        prepareCompiler(true);
        java.util.List<CompilationUnitTree> trees = new java.util.LinkedList<>();
        Set<JavaFileObject> fos = new LinkedHashSet<>();

        for (JavaFileObject file : files) {
            CompilationUnitTree tree = getTreeForFile(file);
            if (tree != null) {
                trees.add(tree);
            } else {
                fos.add(file = ccw.wrap(file));
                if (notYetEntered != null) {
                    assert !notYetEntered.containsKey(file) || notYetEntered.get(file) == null;
                    notYetEntered.put(file, null);
                }
            }
        }
        if (!fos.isEmpty()) {
            args.initFileObjects(fos);
            Iterable<? extends CompilationUnitTree> newTrees = this.parse();
            for (CompilationUnitTree newTree : newTrees) {
                trees.add(newTree);
            }
        }
        return trees;
    }

    private CompilationUnitTree getTreeForFile(final JavaFileObject file) {
        assert file != null;
        Enter enter = Enter.instance(context);
        CompilationUnitTree tree = enter.getCompilationUnit(file);
        if (tree == null && notYetEntered != null) {
            tree = (CompilationUnitTree) notYetEntered.get(file);
        }
        return tree;
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public Iterable<? extends CompilationUnitTree> parse() {
        Pair<Iterable<? extends CompilationUnitTree>, Throwable> result = invocationHelper(this::parseInternal);
        if (result.snd == null) {
            return result.fst;
        }
        throw new IllegalStateException(result.snd);
    }

    private Iterable<? extends CompilationUnitTree> parseInternal() {
        try {
            prepareCompiler(true);
            List<JCCompilationUnit> units = compiler.parseFiles(args.getFileObjects());
            for (JCCompilationUnit unit : units) {
                JavaFileObject file = unit.getSourceFile();
                if (notYetEntered.containsKey(file))
                    notYetEntered.put(file, unit);
            }
            return units;
        } finally {
            parsed = true;
            if (compiler != null && compiler.log != null)
                compiler.log.flush();

            if (notYetEntered != null) {
                for (JavaFileObject file : args.getFileObjects()) {
                    if (notYetEntered.get(file) == null)
                        notYetEntered.remove(file);
                }
            }
        }
    }

    private boolean parsed = false;

    /**
     * Translate all the abstract syntax trees to elements.
     *
     * @return a list of elements corresponding to the top level
     *         classes in the abstract syntax trees
     */
    public Iterable<? extends Element> enter() {
        return enter(null);
    }

    /**
     * Translate the given abstract syntax trees to elements.
     *
     * @param trees a list of abstract syntax trees.
     * @return a list of elements corresponding to the top level
     *         classes in the abstract syntax trees
     */
    public Iterable<? extends Element> enter(Iterable<? extends CompilationUnitTree> trees) {
        if (trees == null && notYetEntered != null && notYetEntered.isEmpty())
            return List.nil();

        boolean wasInitialized = compiler != null;

        prepareCompiler(true);

        ListBuffer<JCCompilationUnit> roots = null;

        if (trees == null) {
            // If there are still files which were specified to be compiled
            // (i.e. in fileObjects) but which have not yet been entered,
            // then we make sure they have been parsed and add them to the
            // list to be entered.
            if (notYetEntered.size() > 0) {
                if (!parsed)
                    parseInternal(); // TODO would be nice to specify files needed to be parsed
                for (JavaFileObject file : args.getFileObjects()) {
                    JCCompilationUnit unit = notYetEntered.remove(file);
                    if (unit != null) {
                        if (roots == null)
                            roots = new ListBuffer<>();
                        roots.append(unit);
                    }
                }
            }
        } else {
            for (CompilationUnitTree cu : trees) {
                if (cu instanceof JCCompilationUnit) {
                    if (roots == null)
                        roots = new ListBuffer<>();
                    roots.append((JCCompilationUnit) cu);
                    notYetEntered.remove(cu.getSourceFile());
                } else
                    throw new IllegalArgumentException(cu.toString());
            }
        }

        if (roots == null) {
            if (trees == null && !wasInitialized) {
                compiler.initModules(List.nil());
            }
            return List.nil();
        }

        Annotate annotate = Annotate.instance(context);

        if (compiler.isEnterDone()) {
            annotate.blockAnnotations();
            compiler.resetEnterDone();
        }

        List<JCCompilationUnit> units = compiler.initModules(roots.toList());

        if (!compiler.skipAnnotationProcessing && compiler.processAnnotations
                && compiler.deferredDiagnosticHandler == null)
            compiler.deferredDiagnosticHandler = new Log.DeferredDiagnosticHandler(compiler.log);

        try {
            units = compiler.enterTrees(units);

            if (!compiler.skipAnnotationProcessing) {
                compiler.skipAnnotationProcessing = true;
                try {
                    compiler.processAnnotations(units);
                } finally {
                    compiler.skipAnnotationProcessing = false;
                }
            }

            ListBuffer<Element> elements = new ListBuffer<>();
            for (JCCompilationUnit unit : units) {
                boolean isPkgInfo = unit.sourcefile.isNameCompatible("package-info",
                        JavaFileObject.Kind.SOURCE);
                if (isPkgInfo) {
                    if (unit.packge != null)
                        elements.append(unit.packge);
                } else {
                    for (JCTree node : unit.defs) {
                        if (node.hasTag(JCTree.Tag.CLASSDEF)) {
                            JCClassDecl cdef = (JCClassDecl) node;
                            if (cdef.sym != null) // maybe null if errors in anno processing
                                elements.append(cdef.sym);
                        } else if (node.hasTag(JCTree.Tag.MODULEDEF)) {
                            JCModuleDecl mdef = (JCModuleDecl) node;
                            if (mdef.sym != null)
                                elements.append(mdef.sym);
                        }
                    }
                }
            }
            return elements.toList();
        } finally {
            compiler.log.flush();
        }
    }

    public Iterable<? extends Element> enterTrees(final Iterable<? extends CompilationUnitTree> trees)
            throws IOException {
        final java.util.List<CompilationUnitTree> toEnter = new java.util.ArrayList();
        final java.util.List<Element> res = new java.util.ArrayList();
        for (CompilationUnitTree tree : trees) {
            final java.util.Collection<Element> te = this.getEnteredElements(tree);
            if (te.isEmpty()) {
                toEnter.add(tree);
            } else {
                res.addAll(te);
            }
        }
        if (!toEnter.isEmpty()) {
            final Iterable<? extends Element> classes = this.enter(toEnter);
            for (Element te : classes) {
                res.add(te);
            }
        }
        return res;
    }

    private java.util.Collection<Element> getEnteredElements(final CompilationUnitTree tree) {
        assert tree instanceof JCCompilationUnit;
        final java.util.List<Element> res = new java.util.ArrayList<>();
        if (((JCCompilationUnit) tree).packge != null) {
            for (JCTree t : ((JCCompilationUnit) tree).defs) {
                if (t.hasTag(JCTree.Tag.CLASSDEF)) {
                    ClassSymbol sym = ((JCClassDecl) t).sym;
                    if (sym != null)
                        res.add(sym);
                }
            }
        }
        return res;
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public Iterable<? extends Element> analyze() {
        Pair<Iterable<? extends Element>, Throwable> result = invocationHelper(() -> analyze(null));
        if (result.snd == null) {
            return result.fst;
        }
        // throw new IllegalStateException(result.snd);
        return List.nil();
    }

    /**
     * Complete all analysis on the given classes.
     * This can be used to ensure that all compile time errors are reported.
     * The classes must have previously been returned from {@link #enter}.
     * If null is specified, all outstanding classes will be analyzed.
     *
     * @param classes a list of class elements
     * @return the elements that were analyzed
     */
    // This implementation requires that we open up privileges on JavaCompiler.
    // An alternative implementation would be to move this code to JavaCompiler and
    // wrap it here
    public Iterable<? extends Element> analyze(Iterable<? extends Element> classes) {
        if (classes == null)
            enter(null); // ensure all classes have been entered

        final ListBuffer<Element> results = new ListBuffer<>();
        try {
            if (classes == null) {
                handleFlowResults(compiler.flow(compiler.attribute(compiler.todo)), results);
            } else {
                Filter f = new Filter() {
                    @Override
                    public void process(Env<AttrContext> env) {
                        handleFlowResults(compiler.flow(compiler.attribute(env)), results);
                    }
                };
                f.run(compiler.todo, classes);
            }
            if (!compiler.skipAnnotationProcessing && compiler.deferredDiagnosticHandler != null
                    && compiler.toProcessAnnotations.nonEmpty()) {
                compiler.skipAnnotationProcessing = true;
                try {
                    compiler.processAnnotations(List.<JCCompilationUnit>nil());
                } finally {
                    compiler.skipAnnotationProcessing = false;
                }
            }
        } finally {
            compiler.log.flush();
        }
        return results;
    }

    // where
    private void handleFlowResults(Queue<Env<AttrContext>> queue, ListBuffer<Element> elems) {
        for (Env<AttrContext> env : queue) {
            switch (env.tree.getTag()) {
                case CLASSDEF:
                    JCClassDecl cdef = (JCClassDecl) env.tree;
                    if (cdef.sym != null)
                        elems.append(cdef.sym);
                    break;
                case MODULEDEF:
                    JCModuleDecl mod = (JCModuleDecl) env.tree;
                    if (mod.sym != null)
                        elems.append(mod.sym);
                    break;
                case PACKAGEDEF:
                    JCCompilationUnit unit = env.toplevel;
                    if (unit.packge != null)
                        elems.append(unit.packge);
                    break;
            }
        }
        genList.addAll(queue);
    }

    @Override
    @DefinedBy(Api.COMPILER_TREE)
    public Iterable<? extends JavaFileObject> generate() {
        Pair<Iterable<? extends JavaFileObject>, Throwable> result = invocationHelper(() -> generate(null));
        if (result.snd == null) {
            return result.fst;
        }
        throw new IllegalStateException(result.snd);
    }

    /**
     * Generate code corresponding to the given classes.
     * The classes must have previously been returned from {@link #enter}.
     * If there are classes outstanding to be analyzed, that will be done before
     * any classes are generated.
     * If null is specified, code will be generated for all outstanding classes.
     *
     * @param classes a list of class elements
     * @return the files that were generated
     */
    public Iterable<? extends JavaFileObject> generate(Iterable<? extends Element> classes) {
        final ListBuffer<JavaFileObject> results = new ListBuffer<>();
        try {
            analyze(classes); // ensure all classes have been parsed, entered, and analyzed

            if (classes == null) {
                compiler.generate(compiler.desugar(genList), results);
                genList.clear();
            } else {
                Filter f = new Filter() {
                    @Override
                    public void process(Env<AttrContext> env) {
                        compiler.generate(compiler.desugar(ListBuffer.of(env)), results);
                    }
                };
                f.run(genList, classes);
            }
            if (genList.isEmpty()) {
                compiler.reportDeferredDiagnostics();
                compiler.log.flush();
                compiler.repair.flush();
            }
        } finally {
            if (compiler != null)
                compiler.log.flush();
        }
        return results;
    }

    public void generateTypeElements(Iterable<? extends TypeElement> classes) throws IOException {
        assert classes != null;
        try {
            analyze(classes);
            Filter f = new Filter() {
                public void process(Env<AttrContext> env) {
                    compiler.generate(compiler.desugar(ListBuffer.of(env)));
                }
            };
            f.run(genList, classes);
        } finally {
            compiler.log.flush();
        }
    }

    public void finish() {
        if (notYetEntered != null && !notYetEntered.isEmpty()) {
            this.notYetEntered.clear();
        }
        if (this.compiler != null && this.compiler.todo != null && !this.compiler.todo.isEmpty()) {
            this.compiler.todo.clear();
        }
        if (this.genList != null && !this.genList.isEmpty()) {
            this.genList.clear();
        }
        cleanup();
    }

    public Iterable<? extends Tree> pathFor(CompilationUnitTree unit, Tree node) {
        return TreeInfo.pathFor((JCTree) node, (JCTree.JCCompilationUnit) unit).reverse();
    }

    public void ensureEntered() {
        args.allowEmpty();
        enter(null);
    }

    abstract class Filter {
        void run(Queue<Env<AttrContext>> list, Iterable<? extends Element> elements) {
            Set<Element> set = new HashSet<>();
            for (Element item : elements) {
                set.add(item);
            }

            ListBuffer<Env<AttrContext>> defer = new ListBuffer<>();
            while (list.peek() != null) {
                Env<AttrContext> env = list.remove();
                Symbol test = null;

                if (env.tree.hasTag(Tag.MODULEDEF)) {
                    test = ((JCModuleDecl) env.tree).sym;
                } else if (env.tree.hasTag(Tag.PACKAGEDEF)) {
                    test = env.toplevel.packge;
                } else {
                    ClassSymbol csym = env.enclClass.sym;
                    if (csym != null)
                        test = csym.outermostClass();
                }
                if (test != null && set.contains(test))
                    process(env);
                else
                    defer = defer.append(env);
            }

            list.addAll(defer);
        }

        abstract void process(Env<AttrContext> env);
    }

    /**
     * For internal use only. This method will be
     * removed without warning.
     * 
     * @param expr  the type expression to be analyzed
     * @param scope the scope in which to analyze the type expression
     * @return the type
     * @throws IllegalArgumentException if the type expression of null or empty
     */
    public Type parseType(String expr, TypeElement scope) {
        if (expr == null || expr.equals(""))
            throw new IllegalArgumentException();
        compiler = JavaCompiler.instance(context);
        JavaFileObject prev = compiler.log.useSource(null);
        ParserFactory parserFactory = ParserFactory.instance(context);
        Attr attr = Attr.instance(context);
        Log.DiagnosticHandler discardHandler = new Log.DiscardDiagnosticHandler(compiler.log);
        try {
            CharSequence buf = '\u0000' == expr.charAt(expr.length() - 1) ? expr
                    : CharBuffer.wrap((expr + "\u0000").toCharArray(), 0, expr.length());
            Parser parser = parserFactory.newParser(buf, false, false, false);
            JCTree tree = parser.parseType();
            return attr.attribType(tree, (Symbol.TypeSymbol) scope);
        } finally {
            compiler.log.popDiagnosticHandler(discardHandler);
            compiler.log.useSource(prev);
        }
    }

    public Tree parseType(String expr) {
        if (expr == null || expr.equals(""))
            throw new IllegalArgumentException();
        compiler = JavaCompiler.instance(context);
        JavaFileObject prev = compiler.log.useSource(null);
        ParserFactory parserFactory = ParserFactory.instance(context);
        Log.DiagnosticHandler discardHandler = new Log.DiscardDiagnosticHandler(compiler.log);
        try {
            CharSequence buf = '\u0000' == expr.charAt(expr.length() - 1) ? expr
                    : CharBuffer.wrap((expr + "\u0000").toCharArray(), 0, expr.length());
            Parser parser = parserFactory.newParser(buf, false, false, false);
            return parser.parseType();
        } finally {
            compiler.log.popDiagnosticHandler(discardHandler);
            compiler.log.useSource(prev);
        }
    }

    public JCStatement parseStatement(CharSequence stmt, SourcePositions[] pos,
            DiagnosticListener<? super JavaFileObject> errors) {
        return (JCStatement) doParse(ParseKind.STATEMENT, stmt, pos, errors);
    }

    private JCTree doParse(ParseKind kind, CharSequence source, SourcePositions[] pos,
            final DiagnosticListener<? super JavaFileObject> errors) {
        if (source == null || source.length() == 0 || (pos != null && pos.length != 1))
            throw new IllegalArgumentException();
        compiler = JavaCompiler.instance(context);
        JavaFileObject prev = compiler.log.useSource(null);
        Log.DiagnosticHandler discardHandler = new Log.DiscardDiagnosticHandler(compiler.log) {
            @Override
            public void report(JCDiagnostic diag) {
                errors.report(diag);
            }
        };
        ParserFactory parserFactory = ParserFactory.instance(context);
        try {
            CharSequence buf = '\u0000' == source.charAt(source.length() - 1) ? source
                    : CharBuffer.wrap((source + "\u0000").toCharArray(), 0, source.length());
            Parser parser = parserFactory.newParser(buf, false, true, false);
            if (parser instanceof JavacParser) {
                if (pos != null)
                    pos[0] = new ParserSourcePositions((JavacParser) parser);
                switch (kind) {
                    case STATEMENT:
                        return parser.parseStatement();
                    case EXPRESSION:
                        return parser.parseExpression();
                    case VARIABLE_INIT:
                        return ((JavacParser) parser).variableInitializer();
                    case STATIC_BLOCK:
                        List<JCTree> trees = ((JavacParser) parser).classOrInterfaceOrRecordBodyDeclaration(null, false,
                                ((JavacParser) parser).isRecordStart());
                        return trees.head != null && trees.head.hasTag(JCTree.Tag.BLOCK) ? (JCBlock) trees.head : null;
                    default:
                        throw new UnsupportedOperationException(kind.name());
                }

            }
            return null;
        } finally {
            compiler.log.popDiagnosticHandler(discardHandler);
            compiler.log.useSource(prev);
        }
    }

    public JCExpression parseExpression(CharSequence expr, SourcePositions[] pos,
            DiagnosticListener<? super JavaFileObject> errors) {
        return (JCExpression) doParse(ParseKind.EXPRESSION, expr, pos, errors);
    }

    public JCExpression parseVariableInitializer(CharSequence init, SourcePositions[] pos,
            DiagnosticListener<? super JavaFileObject> errors) {
        return (JCExpression) doParse(ParseKind.VARIABLE_INIT, init, pos, errors);
    }

    public JCBlock parseStaticBlock(CharSequence block, SourcePositions[] pos,
            DiagnosticListener<? super JavaFileObject> errors) {
        return (JCBlock) doParse(ParseKind.STATIC_BLOCK, block, pos, errors);
    }

    private enum ParseKind {
        STATEMENT, EXPRESSION, VARIABLE_INIT, STATIC_BLOCK;
    }

    @Deprecated
    public JCStatement parseStatement(CharSequence stmt, SourcePositions[] pos) {
        return parseStatement(stmt, pos, new DiscardDiagnosticListener());
    }

    @Deprecated
    public JCExpression parseExpression(CharSequence expr, SourcePositions[] pos) {
        return parseExpression(expr, pos, new DiscardDiagnosticListener());
    }

    @Deprecated
    public JCExpression parseVariableInitializer(CharSequence init, SourcePositions[] pos) {
        return parseVariableInitializer(init, pos, new DiscardDiagnosticListener());
    }

    @Deprecated
    public JCBlock parseStaticBlock(CharSequence block, SourcePositions[] pos) {
        return parseStaticBlock(block, pos, new DiscardDiagnosticListener());
    }

    private static final class DiscardDiagnosticListener implements DiagnosticListener<JavaFileObject> {
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        }
    }

    public Type attributeTree(JCTree tree, Env<AttrContext> env) {
        Log log = Log.instance(context);
        Attr attr = Attr.instance(context);
        JavaFileObject prev = log.useSource(null);
        Log.DiagnosticHandler discardHandler = new Log.DiscardDiagnosticHandler(log);
        Log.DeferredDiagnosticHandler deferredHandler = compiler.deferredDiagnosticHandler;
        compiler.deferredDiagnosticHandler = null;
        Enter enter = Enter.instance(context);
        enter.shadowTypeEnvs(true);
        ArgumentAttr argumentAttr = ArgumentAttr.instance(context);
        ArgumentAttr.LocalCacheContext cacheContext = argumentAttr.withLocalCacheContext();
        try {
            Type type = tree instanceof JCExpression
                    ? attr.attribExpr(tree, env, Type.noType)
                    : attr.attribStat(tree, env);
            if (!compiler.skipAnnotationProcessing && compiler.deferredDiagnosticHandler != null
                    && compiler.toProcessAnnotations.nonEmpty()) {
                compiler.skipAnnotationProcessing = true;
                try {
                    compiler.processAnnotations(List.<JCCompilationUnit>nil());
                } finally {
                    compiler.skipAnnotationProcessing = false;
                }
            }
            return type;
        } finally {
            cacheContext.leave();
            enter.shadowTypeEnvs(false);
            compiler.deferredDiagnosticHandler = deferredHandler;
            log.popDiagnosticHandler(discardHandler);
            log.useSource(prev);
        }
    }

    public JavacScope attributeTreeTo(JCTree tree, Env<AttrContext> env, JCTree to) {
        Log log = Log.instance(context);
        Attr attr = Attr.instance(context);
        JavaFileObject prev = log.useSource(null);
        Log.DiagnosticHandler discardHandler = new Log.DiscardDiagnosticHandler(log);
        Log.DeferredDiagnosticHandler deferredHandler = compiler.deferredDiagnosticHandler;
        compiler.deferredDiagnosticHandler = null;
        Enter enter = Enter.instance(context);
        enter.shadowTypeEnvs(true);
        ArgumentAttr argumentAttr = ArgumentAttr.instance(context);
        ArgumentAttr.LocalCacheContext cacheContext = argumentAttr.withLocalCacheContext();
        try {
            Env<AttrContext> ret = tree instanceof JCExpression ? attr.attribExprToTree(tree, env, to)
                    : attr.attribStatToTree(tree, env, to);
            if (!compiler.skipAnnotationProcessing && compiler.deferredDiagnosticHandler != null
                    && compiler.toProcessAnnotations.nonEmpty()) {
                compiler.skipAnnotationProcessing = true;
                try {
                    compiler.processAnnotations(List.<JCCompilationUnit>nil());
                } finally {
                    compiler.skipAnnotationProcessing = false;
                }
            }
            return new JavacScope(ret);
        } finally {
            cacheContext.leave();
            enter.shadowTypeEnvs(false);
            compiler.deferredDiagnosticHandler = deferredHandler;
            log.popDiagnosticHandler(discardHandler);
            log.useSource(prev);
        }
    }

    private class ParserSourcePositions implements SourcePositions {

        private JavacParser parser;

        private ParserSourcePositions(JavacParser parser) {
            this.parser = parser;
        }

        public long getStartPosition(CompilationUnitTree file, Tree tree) {
            return parser.getStartPos((JCTree) tree);
        }

        public long getEndPosition(CompilationUnitTree file, Tree tree) {
            return parser.getEndPos((JCTree) tree);
        }
    }

    // Debug methods
    public String dumpTodo() {
        StringBuilder res = new StringBuilder();
        if (compiler != null && compiler.todo != null) {
            for (Env<AttrContext> env : compiler.todo) {
                res.append(((JCClassDecl) env.tree).sym.toString()).append(" from: ")
                        .append(env.toplevel.sourcefile.toUri());
            }
        }
        return res.toString();
    }

    public java.util.List<Env<AttrContext>> getTodo() {
        if (compiler != null && compiler.todo != null) {
            return new java.util.ArrayList<Env<AttrContext>>(compiler.todo);
        }
        return java.util.Collections.<Env<AttrContext>>emptyList();
    }

}
