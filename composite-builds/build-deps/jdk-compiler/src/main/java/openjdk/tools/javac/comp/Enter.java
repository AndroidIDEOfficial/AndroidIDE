/*
 * Copyright (c) 1999, 2018, Oracle and/or its affiliates. All rights reserved.
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

package openjdk.tools.javac.comp;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jdkx.lang.model.element.ExecutableElement;
import jdkx.lang.model.element.TypeElement;
import jdkx.lang.model.element.VariableElement;
import jdkx.lang.model.util.ElementScanner14;
import jdkx.tools.JavaFileObject;
import jdkx.tools.JavaFileManager;

import openjdk.tools.javac.api.DuplicateClassChecker;
import openjdk.tools.javac.code.*;
import openjdk.tools.javac.code.Kinds.KindName;
import openjdk.tools.javac.code.Kinds.KindSelector;
import openjdk.tools.javac.code.Scope.*;
import openjdk.tools.javac.code.Symbol.*;
import openjdk.tools.javac.code.Type.*;
import openjdk.tools.javac.main.Option.PkgInfo;
import openjdk.tools.javac.model.LazyTreeLoader;
import openjdk.tools.javac.resources.CompilerProperties.Errors;
import openjdk.tools.javac.resources.CompilerProperties.Warnings;
import openjdk.tools.javac.tree.*;
import openjdk.tools.javac.tree.JCTree.*;
import openjdk.tools.javac.util.*;
import openjdk.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import openjdk.tools.javac.util.List;

import static openjdk.tools.javac.code.Flags.*;
import static openjdk.tools.javac.code.Kinds.Kind.*;

/** This class enters symbols for all encountered definitions into
 *  the symbol table. The pass consists of high-level two phases,
 *  organized as follows:
 *
 *  <p>In the first phase, all class symbols are entered into their
 *  enclosing scope, descending recursively down the tree for classes
 *  which are members of other classes. The class symbols are given a
 *  TypeEnter object as completer.
 *
 *  <p>In the second phase classes are completed using
 *  TypeEnter.complete(). Completion might occur on demand, but
 *  any classes that are not completed that way will be eventually
 *  completed by processing the `uncompleted' queue. Completion
 *  entails determination of a class's parameters, supertype and
 *  interfaces, as well as entering all symbols defined in the
 *  class into its scope, with the exception of class symbols which
 *  have been entered in phase 1.
 *
 *  <p>Whereas the first phase is organized as a sweep through all
 *  compiled syntax trees, the second phase is on-demand. Members of a
 *  class are entered when the contents of a class are first
 *  accessed. This is accomplished by installing completer objects in
 *  class symbols for compiled classes which invoke the type-enter
 *  phase for the corresponding class tree.
 *
 *  <p>Classes migrate from one phase to the next via queues:
 *
 *  <pre>{@literal
 *  class enter -> (Enter.uncompleted)         --> type enter
 *              -> (Todo)                      --> attribute
 *                                              (only for toplevel classes)
 *  }</pre>
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class Enter extends JCTree.Visitor {
    protected static final Context.Key<Enter> enterKey = new Context.Key<>();

    Annotate annotate;
    Log log;
    Symtab syms;
    Check chk;
    TreeMaker make;
    TypeEnter typeEnter;
    Types types;
    Lint lint;
    Names names;
    JavaFileManager fileManager;
    PkgInfo pkginfoOpt;
    TypeEnvs typeEnvs;
    Modules modules;
    JCDiagnostic.Factory diags;

    private final LazyTreeLoader treeLoader;
    private final DuplicateClassChecker duplicateClassChecker;
    private final Source source;

    private final Todo todo;

    public static Enter instance(Context context) {
        Enter instance = context.get(enterKey);
        if (instance == null)
            instance = new Enter(context);
        return instance;
    }

    protected Enter(Context context) {
        context.put(enterKey, this);

        log = Log.instance(context);
        make = TreeMaker.instance(context);
        syms = Symtab.instance(context);
        chk = Check.instance(context);
        typeEnter = TypeEnter.instance(context);
        types = Types.instance(context);
        annotate = Annotate.instance(context);
        lint = Lint.instance(context);
        names = Names.instance(context);
        modules = Modules.instance(context);
        diags = JCDiagnostic.Factory.instance(context);
        treeLoader = LazyTreeLoader.instance(context);
        duplicateClassChecker = context.get(DuplicateClassChecker.class);

        predefClassDef = make.ClassDef(
            make.Modifiers(PUBLIC),
            syms.predefClass.name,
            List.nil(),
            null,
            List.nil(),
            List.nil());
        predefClassDef.sym = syms.predefClass;
        todo = Todo.instance(context);
        fileManager = context.get(JavaFileManager.class);

        Options options = Options.instance(context);
        pkginfoOpt = PkgInfo.get(options);
        typeEnvs = TypeEnvs.instance(context);
        source = Source.instance(context);
    }

    Map<TypeSymbol,Env<AttrContext>> typeEnvsShadow = null;

    private final Map<URI, JCCompilationUnit> compilationUnits =
            new HashMap<URI, JCCompilationUnit> ();

    public JCCompilationUnit getCompilationUnit (JavaFileObject fobj) {
        return this.compilationUnits.get(fobj.toUri());
    }

    /** Accessor for typeEnvs
     */
    public Env<AttrContext> getEnv(TypeSymbol sym) {
        return typeEnvs.get(sym);
    }

    public Iterable<Env<AttrContext>> getEnvs() {
        return typeEnvs.values();
    }

    public Env<AttrContext> getClassEnv(TypeSymbol sym) {
        Env<AttrContext> localEnv = getEnv(sym);
        if (localEnv == null) return null;
        Env<AttrContext> lintEnv = localEnv;
        while (lintEnv.info.lint == null)
            lintEnv = lintEnv.next;
        localEnv.info.lint = lintEnv.info.lint.augment(sym);
        return localEnv;
    }

    /** The queue of all classes that might still need to be completed;
     *  saved and initialized by main().
     */
    ListBuffer<ClassSymbol> uncompleted;

    /** The queue of modules whose imports still need to be checked. */
    ListBuffer<JCCompilationUnit> unfinishedModules = new ListBuffer<>();

    /** A dummy class to serve as enclClass for toplevel environments.
     */
    private JCClassDecl predefClassDef;

/* ************************************************************************
 * environment construction
 *************************************************************************/


    /** Create a fresh environment for class bodies.
     *  This will create a fresh scope for local symbols of a class, referred
     *  to by the environments info.scope field.
     *  This scope will contain
     *    - symbols for this and super
     *    - symbols for any type parameters
     *  In addition, it serves as an anchor for scopes of methods and initializers
     *  which are nested in this scope via Scope.dup().
     *  This scope should not be confused with the members scope of a class.
     *
     *  @param tree     The class definition.
     *  @param env      The environment current outside of the class definition.
     */
    public Env<AttrContext> classEnv(JCClassDecl tree, Env<AttrContext> env) {
        Env<AttrContext> localEnv =
            env.dup(tree, env.info.dup(WriteableScope.create(tree.sym)));
        localEnv.enclClass = tree;
        localEnv.outer = env;
        localEnv.info.isSelfCall = false;
        localEnv.info.lint = null; // leave this to be filled in by Attr,
                                   // when annotations have been processed
        localEnv.info.isAnonymousDiamond = TreeInfo.isDiamond(env.tree);
        return localEnv;
    }

    /** Create a fresh environment for toplevels.
     *  @param tree     The toplevel tree.
     */
    Env<AttrContext> topLevelEnv(JCCompilationUnit tree) {
        Env<AttrContext> localEnv = new Env<>(tree, new AttrContext());
        localEnv.toplevel = tree;
        localEnv.enclClass = predefClassDef;
        tree.toplevelScope = WriteableScope.create(tree.packge);
        tree.namedImportScope = new NamedImportScope(tree.packge);
        tree.starImportScope = new StarImportScope(tree.packge);
        localEnv.info.scope = tree.toplevelScope;
        localEnv.info.lint = lint;
        return localEnv;
    }

    public Env<AttrContext> getTopLevelEnv(JCCompilationUnit tree) {
        Env<AttrContext> localEnv = new Env<>(tree, new AttrContext());
        localEnv.toplevel = tree;
        localEnv.enclClass = predefClassDef;
        localEnv.info.scope = tree.toplevelScope;
        localEnv.info.lint = lint;
        return localEnv;
    }

    /** The scope in which a member definition in environment env is to be entered
     *  This is usually the environment's scope, except for class environments,
     *  where the local scope is for type variables, and the this and super symbol
     *  only, and members go into the class member scope.
     */
    WriteableScope enterScope(Env<AttrContext> env) {
        return (env.tree.hasTag(JCTree.Tag.CLASSDEF))
            ? ((JCClassDecl) env.tree).sym.members_field
            : env.info.scope;
    }

    /** Create a fresh environment for modules.
     *
     *  @param tree     The module definition.
     *  @param env      The environment current outside of the module definition.
     */
    public Env<AttrContext> moduleEnv(JCModuleDecl tree, Env<AttrContext> env) {
        Assert.checkNonNull(tree.sym);
        Env<AttrContext> localEnv =
            env.dup(tree, env.info.dup(WriteableScope.create(tree.sym)));
        localEnv.enclClass = predefClassDef;
        localEnv.outer = env;
        localEnv.info.isSelfCall = false;
        localEnv.info.lint = null; // leave this to be filled in by Attr,
                                   // when annotations have been processed
        return localEnv;
    }

    public void shadowTypeEnvs(boolean b) {
        if (b) {
            assert typeEnvsShadow == null;
            typeEnvsShadow = new HashMap<TypeSymbol,Env<AttrContext>>();
        } else {
            for (Map.Entry<TypeSymbol, Env<AttrContext>> entry : typeEnvsShadow.entrySet()) {
                if (entry.getValue() == null)
                    typeEnvs.remove(entry.getKey());
                else
                    typeEnvs.put(entry.getKey(), entry.getValue());
            }
            typeEnvsShadow = null;
        }
    }

    public boolean isShadowed() {
        return typeEnvsShadow != null;
    }

/* ************************************************************************
 * Visitor methods for phase 1: class enter
 *************************************************************************/

    /** Visitor argument: the current environment.
     */
    protected Env<AttrContext> env;

    /** Visitor result: the computed type.
     */
    Type result;

    /** Visitor method: enter all classes in given tree, catching any
     *  completion failure exceptions. Return the tree's type.
     *
     *  @param tree    The tree to be visited.
     *  @param env     The environment visitor argument.
     */
    Type classEnter(JCTree tree, Env<AttrContext> env) {
        Env<AttrContext> prevEnv = this.env;
        try {
            this.env = env;
            annotate.blockAnnotations();
            tree.accept(this);
            return result;
        }  catch (CompletionFailure ex) {
            return chk.completionError(tree.pos(), ex);
        } finally {
            annotate.unblockAnnotations();
            this.env = prevEnv;
        }
    }

    /** Visitor method: enter classes of a list of trees, returning a list of types.
     */
    <T extends JCTree> List<Type> classEnter(List<T> trees, Env<AttrContext> env) {
        ListBuffer<Type> ts = new ListBuffer<>();
        for (List<T> l = trees; l.nonEmpty(); l = l.tail) {
            Type t = classEnter(l.head, env);
            if (t != null)
                ts.append(t);
        }
        return ts.toList();
    }

    @Override
    public void visitTopLevel(JCCompilationUnit tree) {
//        Assert.checkNonNull(tree.modle, tree.sourcefile.toString());

        JavaFileObject prev = log.useSource(tree.sourcefile);
        boolean addEnv = false;
        boolean isPkgInfo = tree.sourcefile.isNameCompatible("package-info",
                                                             JavaFileObject.Kind.SOURCE);
        if (TreeInfo.isModuleInfo(tree)) {
            JCPackageDecl pd = tree.getPackage();
            if (pd != null) {
                log.error(pd.pos(), Errors.NoPkgInModuleInfoJava);
            }
            tree.packge = syms.rootPackage;
            Env<AttrContext> topEnv = topLevelEnv(tree);
            if (tree.modle != syms.noModule) {
                classEnter(tree.defs, topEnv);
                tree.modle.usesProvidesCompleter = modules.getUsesProvidesCompleter();
            }
        } else {
            JCPackageDecl pd = tree.getPackage();
            if (pd != null) {
                tree.packge = pd.packge = syms.enterPackage(tree.modle, TreeInfo.fullName(pd.pid));

                PackageAttributer.attrib(pd.pid, tree.packge);

                setPackageSymbols.scan(pd);

                if (   pd.annotations.nonEmpty()
                    || pkginfoOpt == PkgInfo.ALWAYS
                    || tree.docComments != null) {
                    if (isPkgInfo) {
                        addEnv = true;
                    } else if (pd.annotations.nonEmpty()) {
                        log.error(pd.annotations.head.pos(),
                                  Errors.PkgAnnotationsSbInPackageInfoJava);
                    }
                }
            } else {
                tree.packge = tree.modle.unnamedPackage;
            }

            Map<Name, PackageSymbol> visiblePackages = tree.modle.visiblePackages;
            Optional<ModuleSymbol> dependencyWithPackage =
                syms.listPackageModules(tree.packge.fullname)
                    .stream()
                    .filter(m -> m != tree.modle)
                    .filter(cand -> visiblePackages != null && visiblePackages.get(tree.packge.fullname) == syms.getPackage(cand, tree.packge.fullname))
                    .findAny();

            if (dependencyWithPackage.isPresent()) {
                log.error(pd, Errors.PackageInOtherModule(dependencyWithPackage.get()));
            }

            tree.packge.complete(); // Find all classes in package.

            Env<AttrContext> topEnv = topLevelEnv(tree);
            Env<AttrContext> packageEnv = isPkgInfo ? topEnv.dup(pd) : null;

            // Save environment of package-info.java file.
            if (isPkgInfo) {
                Env<AttrContext> env0 = typeEnvs.get(tree.packge);
                if (env0 != null) {
                    JCCompilationUnit tree0 = env0.toplevel;
                    if (!fileManager.isSameFile(tree.sourcefile, tree0.sourcefile)) {
                        log.warning(pd != null ? pd.pid.pos() : null,
                                    Warnings.PkgInfoAlreadySeen(tree.packge));
                    }
                }
                typeEnvs.put(tree.packge, packageEnv);

                for (Symbol q = tree.packge; q != null && q.kind == PCK; q = q.owner)
                    q.flags_field |= EXISTS;

                Name name = names.package_info;
                ClassSymbol c = syms.enterClass(tree.modle, name, tree.packge);
                c.flatname = names.fromString(tree.packge + "." + name);
                c.sourcefile = tree.sourcefile;
                c.completer = Completer.NULL_COMPLETER;
                c.members_field = WriteableScope.create(c);
                tree.packge.package_info = c;
                tree.packge.sourcefile = tree.sourcefile;
            }
            compilationUnits.put(tree.sourcefile.toUri(), tree);
            classEnter(tree.defs, topEnv);
            if (addEnv) {
                if ((tree.packge.flags_field & APT_CLEANED) != 0)
                    todo.remove(tree.packge);
                todo.append(packageEnv);
            }
        }
        log.useSource(prev);
        result = null;
    }
        //where:
        //set package Symbols to the package expression:
        private final TreeScanner setPackageSymbols = new TreeScanner() {
            Symbol currentPackage;

            @Override
            public void visitIdent(JCIdent tree) {
                tree.sym = currentPackage;
                tree.type = currentPackage.type;
            }

            @Override
            public void visitSelect(JCFieldAccess tree) {
                tree.sym = currentPackage;
                tree.type = currentPackage.type;
                currentPackage = currentPackage.owner;
                super.visitSelect(tree);
            }

            @Override
            public void visitPackageDef(JCPackageDecl tree) {
                currentPackage = tree.packge;
                scan(tree.pid);
            }
        };

    private static class PackageAttributer extends TreeScanner {

        private Symbol pkg;

        public static void attrib(JCExpression pid, Symbol pkg) {
            PackageAttributer pa = new PackageAttributer();
            pa.pkg = pkg;
            pa.scan(pid);
        }

        @Override
        public void visitIdent(JCIdent that) {
            that.sym = pkg;
        }

        @Override
        public void visitSelect(JCFieldAccess that) {
            that.sym = pkg;
            pkg = pkg.owner;
            super.visitSelect(that);
        }
    }


    @Override
    public void visitClassDef(JCClassDecl tree) {
        Symbol owner = env.info.scope.owner;
        WriteableScope enclScope = enterScope(env);
        ClassSymbol c = null;
        boolean doEnterClass = true;
        boolean reattr=false, noctx=false;
        if (owner.kind == PCK) {
            // We are seeing a toplevel class.
            PackageSymbol packge = (PackageSymbol)owner;
            for (Symbol q = packge; q != null && q.kind == PCK; q = q.owner)
                q.flags_field |= EXISTS;
            c = syms.enterClass(env.toplevel.modle, tree.name, packge);
            packge.members().enterIfAbsent(c);
            if ((tree.mods.flags & PUBLIC) != 0 && !classNameMatchesFileName(c, env)) {
                KindName topElement = KindName.CLASS;
                if ((tree.mods.flags & ENUM) != 0) {
                    topElement = KindName.ENUM;
                } else if ((tree.mods.flags & INTERFACE) != 0) {
                    topElement = KindName.INTERFACE;
                }
                log.error(tree.pos(),
                          Errors.ClassPublicShouldBeInFile(topElement, tree.name));
            }
        } else {
            if ((enclScope.owner.flags_field & FROMCLASS) != 0) {
                for (Symbol sym : enclScope.getSymbolsByName(tree.name)) {
                    if (sym.kind == TYP) {
                        c = (ClassSymbol)sym;
                        break;
                    }
                }
                if (c != null) {
                    if (chk.getCompiled(c) != null) {
                        c = null;
                    } else {
                        reattr = true;
                        if (owner.kind == TYP) {
                            if ((owner.flags_field & INTERFACE) != 0) {
                                tree.mods.flags |= PUBLIC | STATIC;
                            }
                        }
                        doEnterClass = false;
                    }
                } else if ((enclScope.owner.flags_field & APT_CLEANED) == 0) {
                    ClassSymbol cs = enclScope.owner.outermostClass();
                    treeLoader.couplingError(cs, tree);
                    doEnterClass = false;
                }
            }
            if (c == null) {
                if (!tree.name.isEmpty() &&
                        !chk.checkUniqueClassName(tree.pos(), tree.name, enclScope)) {
                    result = types.createErrorType(tree.name, owner, Type.noType);
                    tree.sym = (ClassSymbol)result.tsym;
                    Env<AttrContext> localEnv = classEnv(tree, env);
                    typeEnvs.put(tree.sym, localEnv);
                    tree.sym.completer = typeEnter;
                    ((ClassType)result).typarams_field = classEnter(tree.typarams, localEnv);
                    if (!tree.sym.isDirectlyOrIndirectlyLocal()&& uncompleted != null) uncompleted.append(tree.sym);
                    tree.type = tree.sym.type;
                    return;
                }
                if (owner.kind == TYP || owner.kind == ERR) {
                    // We are seeing a member class.
                    c = syms.enterClass(env.toplevel.modle, tree.name, (TypeSymbol)owner);
                    if (c.owner != owner) {
                        if (c.name != tree.name) {
                            log.error(tree.pos(), Errors.SameBinaryName(c.name, tree.name));
                            result = types.createErrorType(tree.name, (TypeSymbol)owner, Type.noType);
                            tree.sym = (ClassSymbol)result.tsym;
                            return;
                        }
                        //anonymous class loaded from a classfile may be recreated from source (see below)
                        //if this class is a member of such an anonymous class, fix the owner:
                        Assert.check(owner.owner.kind != TYP && owner.owner.kind != ERR, owner::toString);
                        Symbol own = c.owner;
                        Assert.check(c.owner.kind == TYP || c.owner.kind == ERR, own::toString);
                        ClassSymbol cowner = (ClassSymbol) c.owner;
                        if (cowner.members_field != null) {
                            cowner.members_field.remove(c);
                        }
                        c.owner = owner;
                    }
                    if ((owner.flags_field & INTERFACE) != 0) {
                        tree.mods.flags |= PUBLIC | STATIC;
                    }
                    Symbol q = owner;
                    while(q != null && q.kind.matches(KindSelector.TYP)) {
                        q = q.owner;
                    }
                    if (q != null && q.kind != PCK && chk.getCompiled(c) != null) {
                        reattr = true;
                    }
                } else {
                    // We are seeing a local class.
                    if (getIndex(tree) == -1) {
                        c = syms.defineClass(tree.name, owner);
                        c.flatname = chk.localClassName(c);
                        noctx = true;
                    }
                    else {
                        Name flatname = chk.localClassName(owner.enclClass(), tree.name, getIndex(tree));
                        if ((c=chk.getCompiled(env.toplevel.modle, flatname)) != null) {
                            reattr = true;
                        }
                        else {
                            c = syms.enterClass(env.toplevel.modle, flatname, tree.name, owner);
                            if (c.completer.isTerminal())
                                reattr = true;
                        }
                    }
                    if (!c.name.isEmpty())
                        chk.checkTransparentClass(tree.pos(), c, env.info.scope);
                }
            }
        }
        tree.sym = c;

        if (c.kind == ERR && c.type.isErroneous()) {
            c.flags_field &= ~FROMCLASS;
            c.kind = TYP;
            c.type = new ClassType(Type.noType, List.<Type>nil(), c);
        } else if (reattr && c.completer.isTerminal()) {
            new ElementScanner14<Void, Void>() {
                @Override
                public Void visitType(TypeElement te, Void p) {
                    if (te instanceof ClassSymbol && ((ClassSymbol) te).completer.isTerminal()) {
                        ((ClassSymbol) te).flags_field |= FROMCLASS;
                        for (Symbol sym : ((ClassSymbol) te).members().getSymbols()) {
                            try {
                                if (sym != null && sym.owner == te)
                                    scan(sym);
                            } catch (CompletionFailure cf) {}
                        }
                    }
                    return null;
                }
                @Override
                public Void visitExecutable(ExecutableElement ee, Void p) {
                    if (ee instanceof MethodSymbol)
                        ((MethodSymbol) ee).flags_field |= FROMCLASS;
                    return null;
                }
                @Override
                public Void visitVariable(VariableElement ve, Void p) {
                    if (ve instanceof VarSymbol)
                        ((VarSymbol) ve).flags_field |= FROMCLASS;
                    return null;
                }
            }.scan(c);
        }

        // Enter class into `compiled' table and enclosing scope.
        if (!reattr && !noctx && (chk.getCompiled(c) != null
                || (!c.isDirectlyOrIndirectlyLocal() && duplicateClassChecker != null && duplicateClassChecker.check(c.fullname, env.toplevel.getSourceFile())))) {
            duplicateClass(tree.pos(), c);
            result = types.createErrorType(tree.name, owner, Type.noType);
            tree.sym = c = (ClassSymbol)result.tsym;
        } else {
            chk.putCompiled(c);
        }
        if (doEnterClass) {
            enclScope.enter(c);
        }

        if (typeEnvsShadow != null) {
            Env<AttrContext> localEnv = typeEnvs.get(c);
            typeEnvsShadow.put(c, localEnv);
        }
        // Set up an environment for class block and store in `typeEnvs'
        // table, to be retrieved later in memberEnter and attribution.
        Env<AttrContext> localEnv = classEnv(tree, env);
        typeEnvs.put(c, localEnv);

        // Fill out class fields.
        boolean notYetCompleted = !c.completer.isTerminal();
        c.completer = Completer.NULL_COMPLETER; // do not allow the initial completer linger on.
        c.sourcefile = env.toplevel.sourcefile;
        if (notYetCompleted || (c.flags_field & FROMCLASS) == 0 && (enclScope.owner.flags_field & FROMCLASS) == 0) {
            c.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, c, tree);
            c.members_field = WriteableScope.create(c);

            ClassType ct = (ClassType)c.type;
            if (owner.kind != PCK && (c.flags_field & STATIC) == 0) {
                // We are seeing a local or inner class.
                // Set outer_field of this class to closest enclosing class
                // which contains this class in a non-static context
                // (its "enclosing instance class"), provided such a class exists.
                Symbol owner1 = owner;
                while (owner1.kind.matches(KindSelector.VAL_MTH) &&
                       (owner1.flags_field & STATIC) == 0) {
                    owner1 = owner1.owner;
                }
                if (owner1.kind == TYP) {
                    ct.setEnclosingType(owner1.type);
                }
            }
            // Enter type parameters.
            ct.typarams_field = classEnter(tree.typarams, localEnv);
            ct.allparams_field = null;
        } else {
            c.flags_field = chk.checkFlags(tree.pos(), tree.mods.flags, c, tree) | (c.flags_field & (FROMCLASS | APT_CLEANED));
            ClassType ct = (ClassType)c.type;
            if (owner.kind != PCK && (c.flags_field & STATIC) == 0) {
                // We are seeing a local or inner class.
                // Set outer_field of this class to closest enclosing class
                // which contains this class in a non-static context
                // (its "enclosing instance class"), provided such a class exists.
                Symbol owner1 = owner;
                while (owner1.kind.matches(KindSelector.VAL_MTH) &&
                        (owner1.flags_field & STATIC) == 0) {
                    owner1 = owner1.owner;
                }
                if (owner1.kind == TYP) {
                    ct.setEnclosingType(owner1.type);
                }
            }
            boolean wasNull = false;
            if (ct.typarams_field != null) {
                for (List<Type> l = ct.typarams_field; l.nonEmpty(); l = l.tail)
                    localEnv.info.scope.enter(l.head.tsym);
            } else {
                wasNull = true;
            }
            List<Type> classEnter = classEnter(tree.typarams, localEnv);
            if (wasNull) {
                if (!classEnter.isEmpty()) {
                    //the symbol from class does not have any type parameters,
                    //but the symbol in the source code does:
                    ClassSymbol cs = env.info.scope.owner.outermostClass();
                    treeLoader.couplingError(cs, tree);
                } else {
                    ct.typarams_field = List.nil();
                    ct.allparams_field = null;
                }
            }
            if (c.members_field == null) {
                c.members_field = WriteableScope.create(c);
                c.flags_field &= ~FROMCLASS;
            }
            if (c.owner.kind.matches(KindSelector.VAL_MTH)) {
                // local or anonymous class
                for (Symbol ctor : c.members_field.getSymbolsByName(names.init)) {
                    c.members_field.remove(ctor);
                }
            }
       }

        // install further completer for this type.
        c.completer = typeEnter;

        // Add non-local class to uncompleted, to make sure it will be
        // completed later.
        if (!c.isDirectlyOrIndirectlyLocal() && uncompleted != null) uncompleted.append(c);
//      System.err.println("entering " + c.fullname + " in " + c.owner);//DEBUG

        // Recursively enter all member classes.
        classEnter(tree.defs, localEnv);

        result = tree.type = c.type;
    }
    //where
        /** Does class have the same name as the file it appears in?
         */
        private static boolean classNameMatchesFileName(ClassSymbol c,
                                                        Env<AttrContext> env) {
            return env.toplevel.sourcefile.isNameCompatible(c.name.toString(),
                                                            JavaFileObject.Kind.SOURCE);
        }

    /** Complain about a duplicate class. */
    protected void duplicateClass(DiagnosticPosition pos, ClassSymbol c) {
        log.error(pos, Errors.DuplicateClass(c.fullname));
    }

    protected int getIndex(JCClassDecl clazz) {
        return -1;
    }

    /** Class enter visitor method for type parameters.
     *  Enter a symbol for type parameter in local scope, after checking that it
     *  is unique.
     */
    @Override
    public void visitTypeParameter(JCTypeParameter tree) {
        result = null;
        if ((env.info.scope.owner.flags_field & FROMCLASS) != 0) {
            for (Symbol sym : env.info.scope.getSymbolsByName(tree.name)) {
                if (sym.kind == TYP) {
                    result = sym.type;
                    tree.type = result;
                    break;
                }
            }
            if (result != null)
                return;
            if ((env.info.scope.owner.flags_field & APT_CLEANED) == 0) {
                ClassSymbol cs = env.info.scope.owner.outermostClass();
                treeLoader.couplingError(cs, tree);
            }
        }
        TypeVar a = (tree.type != null)
        ? (TypeVar)tree.type
                : new TypeVar(tree.name, env.info.scope.owner, syms.botType);
        tree.type = a;
        if (chk.checkUnique(tree.pos(), a.tsym, env.info.scope)) {
            env.info.scope.enter(a.tsym);
        }
        result = a;
    }

    @Override
    public void visitModuleDef(JCModuleDecl tree) {
        Env<AttrContext> moduleEnv = moduleEnv(tree, env);
        typeEnvs.put(tree.sym, moduleEnv);
        if (modules.isInModuleGraph(tree.sym)) {
            todo.append(moduleEnv);
        }
    }

    /** Default class enter visitor method: do nothing.
     */
    @Override
    public void visitTree(JCTree tree) {
        result = null;
    }

    /** Main method: enter all classes in a list of toplevel trees.
     *  @param trees      The list of trees to be processed.
     */
    public void main(List<JCCompilationUnit> trees) {
        complete(trees, null);
    }

    /** Main method: enter classes from the list of toplevel trees, possibly
     *  skipping TypeEnter for all but 'c' by placing them on the uncompleted
     *  list.
     *  @param trees      The list of trees to be processed.
     *  @param c          The class symbol to be processed or null to process all.
     */
    public void complete(List<JCCompilationUnit> trees, ClassSymbol c) {
        annotate.blockAnnotations();
        ListBuffer<ClassSymbol> prevUncompleted = uncompleted;
        if (typeEnter.completionEnabled) uncompleted = new ListBuffer<>();

        try {
            // enter all classes, and construct uncompleted list
            classEnter(trees, null);

            // complete all uncompleted classes in memberEnter
            if (typeEnter.completionEnabled) {
                while (uncompleted.nonEmpty()) {
                    ClassSymbol clazz = uncompleted.next();
                    if (c == null || c == clazz || prevUncompleted == null)
                        clazz.complete();
                    else
                        // defer
                        prevUncompleted.append(clazz);
                }

                if (!modules.modulesInitialized()) {
                    for (JCCompilationUnit cut : trees) {
                        if (TreeInfo.isModuleInfo(cut)) {
                            unfinishedModules.append(cut);
                        } else {
                            typeEnter.ensureImportsChecked(List.of(cut));
                        }
                    }
                } else {
                    typeEnter.ensureImportsChecked(unfinishedModules.toList());
                    unfinishedModules.clear();
                    typeEnter.ensureImportsChecked(trees);
                }
            }
        } finally {
            uncompleted = prevUncompleted;
            annotate.unblockAnnotations();
        }
    }

    public void newRound() {
        typeEnvs.clear();
    }
    
    public void unenter(JCCompilationUnit topLevel, JCTree tree) {
        new UnenterScanner(topLevel.modle).scan(tree);
    }
        class UnenterScanner extends TreeScanner {
            private final ModuleSymbol msym;

            public UnenterScanner(ModuleSymbol msym) {
                this.msym = msym;
            }

            @Override
            public void visitClassDef(JCClassDecl tree) {
                ClassSymbol csym = tree.sym;
                //if something went wrong during method applicability check
                //it is possible that nested expressions inside argument expression
                //are left unchecked - in such cases there's nothing to clean up.
                if (csym == null) return;
                typeEnvs.remove(csym);
                chk.removeCompiled(csym);
                chk.clearLocalClassNameIndexes(csym);
                syms.removeClass(msym, csym.flatname);
                super.visitClassDef(tree);
            }
        }
}
