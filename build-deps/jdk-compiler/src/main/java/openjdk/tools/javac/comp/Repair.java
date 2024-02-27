/*
 * Copyright (c) 1999, 2020, Oracle and/or its affiliates. All rights reserved.
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

import openjdk.source.tree.CaseTree;
import openjdk.source.tree.TreeVisitor;
import openjdk.tools.javac.code.Flags;
import openjdk.tools.javac.code.Kinds;
import openjdk.tools.javac.code.Scope;
import openjdk.tools.javac.code.Source;
import openjdk.tools.javac.code.Symbol;
import openjdk.tools.javac.code.Symbol.ClassSymbol;
import openjdk.tools.javac.code.Symbol.MethodSymbol;
import openjdk.tools.javac.code.Symtab;
import openjdk.tools.javac.code.Type;
import openjdk.tools.javac.code.Type.ClassType;
import openjdk.tools.javac.code.TypeTag;
import openjdk.tools.javac.code.Types;
import openjdk.tools.javac.jvm.PoolWriter;
import openjdk.tools.javac.main.Option;
import openjdk.tools.javac.parser.Tokens;
import openjdk.tools.javac.resources.CompilerProperties;
import openjdk.tools.javac.tree.JCTree;
import openjdk.tools.javac.tree.JCTree.JCAssignOp;
import openjdk.tools.javac.tree.JCTree.JCBinary;
import openjdk.tools.javac.tree.JCTree.JCBlock;
import openjdk.tools.javac.tree.JCTree.JCCase;
import openjdk.tools.javac.tree.JCTree.JCClassDecl;
import openjdk.tools.javac.tree.JCTree.JCErroneous;
import openjdk.tools.javac.tree.JCTree.JCExpression;
import openjdk.tools.javac.tree.JCTree.JCExpressionStatement;
import openjdk.tools.javac.tree.JCTree.JCImport;
import openjdk.tools.javac.tree.JCTree.JCLiteral;
import openjdk.tools.javac.tree.JCTree.JCMethodDecl;
import openjdk.tools.javac.tree.JCTree.JCMethodInvocation;
import openjdk.tools.javac.tree.JCTree.JCNewClass;
import openjdk.tools.javac.tree.JCTree.JCStatement;
import openjdk.tools.javac.tree.JCTree.JCSwitch;
import openjdk.tools.javac.tree.JCTree.JCTypeParameter;
import openjdk.tools.javac.tree.JCTree.JCUnary;
import openjdk.tools.javac.tree.JCTree.JCVariableDecl;
import openjdk.tools.javac.tree.TreeInfo;
import openjdk.tools.javac.tree.TreeMaker;
import openjdk.tools.javac.tree.TreeScanner;
import openjdk.tools.javac.tree.TreeTranslator;
import openjdk.tools.javac.util.Context;
import openjdk.tools.javac.util.JCDiagnostic;
import openjdk.tools.javac.util.JCDiagnostic.DiagnosticPosition;
import openjdk.tools.javac.util.List;
import openjdk.tools.javac.util.Log;
import openjdk.tools.javac.util.MissingPlatformError;
import openjdk.tools.javac.util.Name;
import openjdk.tools.javac.util.Options;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import jdkx.lang.model.element.ElementKind;
import jdkx.tools.JavaFileObject;


/**
 *
 * @author Dusan Balek
 */
public class Repair extends TreeTranslator {

    /** The context key for the Repair phase. */
    protected static final Context.Key<Repair> repairKey = new Context.Key<Repair>();
    private static final String ERR_MESSAGE = "Uncompilable source code";
    private static final Logger LOGGER = Logger.getLogger(Repair.class.getName());

    /** Get the instance for this context. */
    public static Repair instance(Context context) {
        Repair instance = context.get(repairKey);
        if (instance == null) {
            instance = new Repair(context);
        }
        return instance;
    }

    private Symtab syms;
    private Resolve rs;
    private Enter enter;
    private Types types;
    private Log log;
    private TreeMaker make;
    private JCDiagnostic.Factory diags;
    private boolean allowLambda;

    private Env<AttrContext> attrEnv;
    private boolean hasError;
    private JCDiagnostic err;
    private JCTree classLevelErrTree;
    private String classLevelErrMessage;
    private String errMessage;
    private JCBlock staticInit;
    private List<JCTree> parents;
    private Set<ClassSymbol> repairedClasses = new HashSet<ClassSymbol>();
    private boolean isErrClass;
    private boolean insideErrEnum;
    private Name fixedTopLevelName;
    private Symbol runtimeExceptionDefaultConstructor = null;
    private Symbol runtimeExceptionConstructor = null;
    private Context context;

    private Repair(Context context) {
        context.put(repairKey, this);
        this.context = context;
        syms = Symtab.instance(context);
        rs = Resolve.instance(context);
        enter = Enter.instance(context);
        types = Types.instance(context);
        log = Log.instance(context);
        diags = JCDiagnostic.Factory.instance(context);
        Source source = Source.instance(context);
        allowLambda = Source.Feature.LAMBDA.allowedInSource(source);
    }

    @Override
    public <T extends JCTree> T translate(T tree) {
        if (tree == null)
            return null;
        if (tree.type != null && tree.type.constValue() instanceof String && ((String)tree.type.constValue()).length() >= PoolWriter.MAX_STRING_LENGTH) {
            log.error(tree.pos(), CompilerProperties.Errors.LimitString); //NOI18N
        }
        parents = parents.prepend(tree);
        try {
            if (hasError)
                return super.translate(tree);
            if ((err = log.getErrDiag(tree)) != null)
                hasError = true;
            tree = super.translate(tree);
        } finally {
            parents = parents.tail;
        }
        if (tree.type != null && tree.type.isErroneous() || tree.type == syms.unknownType) {
            JCTree parent = parents.head;
            if (parent == null || !parent.hasTag(JCTree.Tag.CLASSDEF)) {
                hasError = true;
                if (err == null && errMessage == null)
                    errMessage = "Erroneous tree type: " + tree.type;
            }
        }
        if (!(hasError && tree instanceof JCStatement))
            return tree;
        if (tree.hasTag(JCTree.Tag.CASE))
            return tree;
        if (tree.hasTag(JCTree.Tag.CLASSDEF)) {
            JCTree parent = parents.head;
            if (parent == null || (!parent.hasTag(JCTree.Tag.BLOCK) && !parent.hasTag(JCTree.Tag.CASE))) {
                return tree;
            }
        }
        if (tree.hasTag(JCTree.Tag.VARDEF)) {
            JCTree parent = parents.head;
            if (parent == null) {
                return tree;
            }
            // special case: in switch-case, generate throw and terminate the case only if the variable is not used
            // in subsequent switch cases. Otherwise return the tree unchanged with error flags, the whole switch
            // statement will be aborted and replaced by throw.
            if (parent.hasTag(JCTree.Tag.CASE)) {
                if (!parents.tail.isEmpty()) {
                    JCTree t = parents.tail.head;
                    if (t.hasTag(JCTree.Tag.SWITCH) && varUsedInOtherCaseBranch((JCVariableDecl)tree, (JCSwitch)t, (JCCase)parent)) {
                        // assume the whole switch will be replaced by throw statement.
                        return tree;
                    }
                }
            } else if (!parent.hasTag(JCTree.Tag.BLOCK)) {
                return tree;
            }
        }
        String msg = err != null ? err.getMessage(null) : errMessage;
        hasError = false;
        err = null;
        errMessage = null;
        if (tree.hasTag(JCTree.Tag.BLOCK)) {
            ((JCBlock)tree).stats = List.of(generateErrStat(tree.pos(), msg));
            return tree;
        }
        return (T)generateErrStat(tree.pos(), msg);
    }

    private boolean varUsedInOtherCaseBranch(final JCTree.JCVariableDecl varDecl, JCSwitch sw, JCCase defCase) {
        List<JCCase> cases = sw.getCases();
        int index = cases.indexOf(defCase);
        if (index == -1) {
            // not sure, eliminate whole switch
            return true;
        }
        class SwitchVariableFinder extends TreeScanner {
            private boolean used;

            @Override
            public void visitIdent(JCTree.JCIdent tree) {
                used |= tree.sym == varDecl.sym;
                super.visitIdent(tree);
            }
        }
        SwitchVariableFinder finder = new SwitchVariableFinder();
        for (int i = index + 1; i < cases.size(); i++) {
            JCTree testCase = cases.get(i);
            finder.scan(testCase);
            if (finder.used) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visitImport(JCImport tree) {
        super.visitImport(tree);
        if (hasError && err != null) {
            classLevelErrTree = err.getTree();
            classLevelErrMessage = err.getMessage(null);
        }
    }

    @Override
    public void visitTypeParameter(JCTypeParameter tree) {
        super.visitTypeParameter(tree);
        if (tree.type != null && tree.type.hasTag(TypeTag.TYPEVAR)) {
            Type.TypeVar tv = (Type.TypeVar)tree.type;
            if (tv.getUpperBound() != null && tv.getUpperBound().isErroneous() || tv.getUpperBound() == syms.unknownType) {
                if (err == null && errMessage == null)
                    errMessage = "Erroneous type var bound: " + tv.getUpperBound();
                tv.setUpperBound(syms.objectType);
                hasError = true;
            }
        }
    }

    @Override
    public void visitClassDef(JCClassDecl tree) {
        translateClass(tree.sym);
        result = tree;
    }

    @Override
    public void visitVarDef(JCVariableDecl tree) {
        super.visitVarDef(tree);
        if (hasError) {
            JCTree parent = parents != null ? parents.tail.head : null;
            if (parent != null && parent.hasTag(JCTree.Tag.CLASSDEF)) {
                tree.init = err != null ? generateErrExpr(err.getTree(), err.getMessage(null)) : generateErrExpr(tree.init, errMessage);
                hasError = false;
                err = null;
                errMessage = null;
                if (tree.sym != null)
                    tree.sym.setData(null);
            }
        } else if (tree.sym == null) {
            JCTree parent = parents != null ? parents.tail.head : null;
            if (parent != null && !parent.hasTag(JCTree.Tag.CLASSDEF)) {
                hasError = true;
                if (err == null && errMessage == null)
                    errMessage = "Null tree sym: " + tree;
           }
        }
    }

    @Override
    public void visitMethodDef(JCMethodDecl tree) {
        boolean hadDuplicateSymError = hasError && err != null && "compiler.err.already.defined".equals(err.getCode()); //NOI18N
        tree.mods = translate(tree.mods);
        tree.restype = translate(tree.restype);
        tree.typarams = translateTypeParams(tree.typarams);
        tree.params = translateVarDefs(tree.params);
        tree.thrown = translate(tree.thrown);
        tree.defaultValue = translate(tree.defaultValue);
        tree.body = translate(tree.body);
        result = tree;
        if (isErrClass && tree.body != null) {
            JCMethodInvocation app = TreeInfo.firstConstructorCall(tree);
            Name meth = app != null ? TreeInfo.name(app.meth) : null;
            if (meth != null && (meth == meth.table.names._this || meth == meth.table.names._super))
                tree.body.stats.tail = List.<JCStatement>nil();
            else
                tree.body.stats = List.<JCStatement>nil();
        }
        if (hasError && !hadDuplicateSymError) {
            if (tree.body != null) {
                if (tree.sym != null)
                    tree.sym.flags_field &= ~(Flags.ABSTRACT | Flags.NATIVE);
                tree.body.stats = List.of(generateErrStat(tree.pos(), err != null ? err.getMessage(null) : errMessage));
            } else if (tree.sym == null || (tree.sym.flags_field & Flags.ABSTRACT) == 0) {
                tree.body = make.Block(0, List.<JCStatement>nil());
                tree.body.stats = List.of(generateErrStat(tree.pos(), err != null ? err.getMessage(null) : errMessage));
            }
            if (tree.sym != null)
                tree.sym.defaultValue = null;
            tree.defaultValue = null;
            hasError = false;
            err = null;
            errMessage = null;
        }
    }

    @Override
    public void visitBlock(JCBlock tree) {
        if (tree.isStatic() && staticInit == null)
            staticInit = tree;
        List<JCStatement> last = null;
        for (List<JCStatement> l = tree.stats; l.nonEmpty(); l = l.tail) {
            l.head = translate(l.head);
            if (last == null && l.head.hasTag(JCTree.Tag.THROW))
                last = l;
        }
        if (last != null)
            last.tail = List.nil();
        result = tree;
    }

    @Override
    public void visitApply(JCMethodInvocation tree) {
        Symbol meth = TreeInfo.symbol(tree.meth);
        if (meth == null) {
            LOGGER.warning("Repair.visitApply tree [" + tree + "] has null symbol."); //NOI18N
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null tree sym: " + tree.meth;
        } else if (meth.type == null) {
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null sym type: " + meth;
        } else if (meth.type.isErroneous() || meth.type == syms.unknownType) {
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Erroneous sym type: " + meth.type;
        }
        super.visitApply(tree);
    }

    @Override
    public void visitNewClass(JCNewClass tree) {
        Symbol ctor = tree.constructor;
        if (ctor == null) {
            LOGGER.warning("Repair.visitNewClass tree [" + tree + "] has null constructor symbol."); //NOI18N
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null tree ctor";
        } else if (tree.constructorType == null) {
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null ctor sym type: " + ctor;
        } else if (tree.constructorType.isErroneous() || tree.constructorType == syms.unknownType) {
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Erroneous ctor sym type: " + tree.constructorType;
        }
        super.visitNewClass(tree);
    }

    @Override
    public void visitUnary(JCUnary tree) {
        Symbol operator = tree.operator;
        if (operator == null) {
            LOGGER.warning("Repair.visitUnary tree [" + tree + "] has null operator symbol."); //NOI18N
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null operator: " + tree;
        }
        super.visitUnary(tree);
    }

    @Override
    public void visitBinary(JCBinary tree) {
        Symbol operator = tree.operator;
        if (operator == null) {
            LOGGER.warning("Repair.visitBinary tree [" + tree + "] has null operator symbol."); //NOI18N
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null operator: " + tree;
        }
        super.visitBinary(tree);
    }

    @Override
    public void visitAssignop(JCAssignOp tree) {
        Symbol operator = tree.operator;
        if (operator == null) {
            LOGGER.warning("Repair.visitAssignop tree [" + tree + "] has null operator symbol."); //NOI18N
            hasError = true;
            if (err == null && errMessage == null)
                errMessage = "Null operator: " + tree;
        }
        super.visitAssignop(tree);
    }

    @Override
    public void visitCase(JCCase tree) {
        for (List<JCExpression> l = tree.pats; l.nonEmpty(); l = l.tail) {
            l.head = translate(l.head);
            if (!hasError && l.head != null && (l.head.type == null
                    || (l.head.type.tsym.flags() & Flags.ENUM) == 0
                    && (l.head.type.constValue() == null && !isSourceVersionSupportSwitchPattern()))) {
                LOGGER.warning("Repair.visitCase tree [" + tree + "] has wrong expression type [" + l.head.type + "]."); //NOI18N
                hasError = true;
                if (err == null && errMessage == null)
                    errMessage = "Wrong expression type: " + tree;
            }
        }
        tree.pats = translate(tree.pats);
        List<JCStatement> last = null;
        for (List<JCStatement> l = tree.stats; l.nonEmpty(); l = l.tail) {
            l.head = translate(l.head);
            if (last == null && l.head.hasTag(JCTree.Tag.THROW))
                last = l;
        }
        if (last != null)
            last.tail = List.nil();
        result = tree;
    }

    @Override
    public void visitLambda(JCTree.JCLambda tree) {
        if (!allowLambda) {
            hasError = true;
        }
        super.visitLambda(tree);
    }

    @Override
    public void visitReference(JCTree.JCMemberReference tree) {
        if (!allowLambda) {
            hasError = true;
        }
        super.visitReference(tree);
    }

    @Override
    public void visitErroneous(JCErroneous tree) {
        hasError = true;
        result = tree;
    }

    private JCStatement generateErrStat(DiagnosticPosition pos, String msg) {
        return generateErrStat(make, pos, msg);
    }

    private boolean isSourceVersionSupportSwitchPattern(){
        Options options = Options.instance(context);
        String sourceVersion = options.get(Option.SOURCE);
        return sourceVersion.compareTo(Source.JDK17.name) >= 0;
    }
    JCStatement generateErrStat(TreeMaker make, DiagnosticPosition pos, String msg) {
        make.at(pos);
        ClassType ctype = (ClassType)syms.runtimeExceptionType;
        if (runtimeExceptionConstructor != null && runtimeExceptionConstructor.kind == Kinds.Kind.MTH) {
            JCLiteral literal = make.Literal(msg != null ? ERR_MESSAGE + " - " + msg : ERR_MESSAGE); //NOI18N
            JCNewClass tree = make.NewClass(null, null, make.QualIdent(ctype.tsym), List.<JCExpression>of(literal), null);
            tree.type = ctype;
            tree.constructor = runtimeExceptionConstructor;
            return make.Throw(tree);
        }
        if (runtimeExceptionDefaultConstructor != null && runtimeExceptionDefaultConstructor.kind == Kinds.Kind.MTH) {
            JCNewClass tree = make.NewClass(null, null, make.QualIdent(ctype.tsym), List.<JCExpression>nil(), null);
            tree.type = ctype;
            tree.constructor = runtimeExceptionDefaultConstructor;
            return make.Throw(tree);
        }
        throw new MissingPlatformError (diags.fragment("fatal.err.cant.locate.ctor", ctype)); //NOI18N
    }

    private JCExpression generateErrExpr(DiagnosticPosition pos, String msg) {
        return generateErrExpr(make, pos, msg);
    }

    JCExpression generateErrExpr(TreeMaker make, DiagnosticPosition pos, String msg) {
        make.at(pos);
        JCExpression expr = make.Erroneous(List.<JCStatement>of(generateErrStat(make, pos, msg)));
        expr.type = syms.errType;
        return expr;
    }

    private JCBlock generateErrStaticInit(DiagnosticPosition pos, String msg) {
        make.at(pos);
        return make.Block(Flags.STATIC, List.<JCStatement>of(generateErrStat(pos, msg)));
    }

    private JCMethodDecl generateErrMethod(MethodSymbol sym) {
        make.at(null);
        return make.MethodDef(sym, make.Block(0, List.<JCStatement>of(generateErrStat(null, null))));
    }

    private JCExpressionStatement generateFakeConstructorCall(MethodSymbol ctor) {
        make.at(null);
        JCTree.JCIdent ident = make.Ident(syms.objectType.tsym.name.table.names._this);
        ident.sym = ctor;
        ident.type = ctor.type;
        List<JCExpression> args = List.nil();
        for (Symbol.VarSymbol param : ((MethodSymbol)ctor).params().reverse()) {
            args = args.prepend(make.Ident(param));
        }
        JCMethodInvocation meth = make.Apply(List.<JCExpression>nil(), ident, args);
        meth.type = ctor.type.getReturnType();
        return make.Exec(meth);
    }

    private void translateClass(ClassSymbol c) {
        if (c == null)
            return;
        Type st = types.supertype(c.type);
        if (st != null && st.hasTag(TypeTag.CLASS))
            translateClass((ClassSymbol)st.tsym);
        LOGGER.finest("Repair.translateClass: " + c); //NOI18N
        if (repairedClasses.contains(c)) {
            LOGGER.finest("Repair.translateClass: Should be already done"); //NOI18N
            return;
        }
        Env<AttrContext> myEnv = enter.typeEnvs.get(c);
        if (myEnv == null) {
            LOGGER.finest("Repair.translateClass: Context not found"); //NOI18N
            return;
        }
        LOGGER.finest("Repair.translateClass: Repairing " + c); //NOI18N
        repairedClasses.add(c);
        Env<AttrContext> oldEnv = attrEnv;
        try {
            attrEnv = myEnv;
            TreeMaker oldMake = make;
            make = make.forToplevel(attrEnv.toplevel);
            boolean oldHasError = hasError;
            boolean oldIsErrClass = isErrClass;
            boolean oldInsideErrEnum = insideErrEnum;
            JCDiagnostic oldErr = err;
            JCTree oldClassLevelErrTree = classLevelErrTree;
            String oldClassLevelErrMessage = classLevelErrMessage;
            JCBlock oldStaticinit = staticInit;
            try {
                for (JCImport imp : attrEnv.toplevel.getImports()) {
                    translate(imp);
                    if (classLevelErrTree != null)
                        break;
                }
                hasError = false;
                isErrClass |= c.type.isErroneous() || c.type == syms.unknownType;
                err = null;
                staticInit = null;
                JCClassDecl tree = (JCClassDecl)attrEnv.tree;
                final Symbol enclosingElement = c.getEnclosingElement();
                if (c.name == c.name.table.names.error &&
                    enclosingElement.getKind() == ElementKind.PACKAGE) {
                    final JavaFileObject source = c.sourcefile;
                    final String path = source.toUri().getPath();
                    int start = path.lastIndexOf('/');
                    int end = path.lastIndexOf('.');
                    if (end > start) {
                        fixedTopLevelName = c.name.table.fromString(path.substring(start+1, end));
                        c.name = fixedTopLevelName;
                    }
                    c.fullname = Symbol.TypeSymbol.formFullName(c.name, enclosingElement);
                    c.flatname = c.fullname;
                    tree.name = c.name;
                    isErrClass = true;
                    hasError = true;
                    err = diags.error(
                            null,
                            log.currentSource(),
                            tree,
                            "expected",         //NOI18N
                            Tokens.TokenKind.IDENTIFIER);
                } else if (fixedTopLevelName != null) {
                    c.fullname = Symbol.TypeSymbol.formFullName(c.name, enclosingElement);
                    c.flatname = Symbol.TypeSymbol.formFlatName(c.name, enclosingElement);
                }
                tree.mods = translate(tree.mods);
                tree.typarams = translateTypeParams(tree.typarams);
                tree.extending = translate(tree.extending);
                tree.implementing = translate(tree.implementing);
                if (!hasError && (err = log.getErrDiag(tree)) != null) {
                    hasError = true;
                    isErrClass = true;
                }
                if ((isErrClass || insideErrEnum)
                        && ((c.flags_field & Flags.ENUM) != 0 || (tree.mods.flags & Flags.ENUM) != 0)) {
                    insideErrEnum = true;
                    hasError = true;
                    isErrClass = true;
                    classLevelErrTree = tree;
                }
                if (hasError && err != null) {
                    isErrClass = true;
                    classLevelErrTree = err.getTree();
                    classLevelErrMessage = err.getMessage(null);
                } else if ((c.type.isErroneous() || c.type == syms.unknownType) && oldHasError && oldErr != null) {
                    classLevelErrTree = oldErr.getTree();
                    classLevelErrMessage = oldErr.getMessage(null);
                }
                if (tree.defs != null) {
                    HashSet<MethodSymbol> nonAbstractMethods = new HashSet<MethodSymbol>();
                    for (Symbol sym : tree.sym.members_field.getSymbols()) {
                        if (sym.kind == Kinds.Kind.MTH && (sym.flags_field & Flags.ABSTRACT) == 0 && sym.name != sym.name.table.names.clinit)
                            nonAbstractMethods.add((MethodSymbol)sym);
                    }
                    List<JCTree> last = null;
                    for (List<JCTree> l = tree.defs; l != null && l.nonEmpty(); l = l.tail) {
                        if (l.head.hasTag(JCTree.Tag.METHODDEF))
                            nonAbstractMethods.remove(((JCMethodDecl)l.head).sym);
                        hasError = false;
                        err = null;
                        if (l.head.hasTag(JCTree.Tag.CLASSDEF) && ((JCClassDecl)l.head).name == c.name.table.names.error) {
                            tree.sym.members_field.remove(((JCClassDecl)l.head).sym);
                            if (last != null)
                                last.tail = l.tail;
                            else
                                tree.defs = l.tail;
                        } else {
                            l.head = translate(l.head);
                            if ((l.head.hasTag(JCTree.Tag.METHODDEF) && ((JCMethodDecl)l.head).sym == null)
                                    || (l.head.hasTag(JCTree.Tag.VARDEF) && ((JCVariableDecl)l.head).sym == null)) {
                                hasError = true;
                            } else if (c.type != syms.objectType && l.head.hasTag(JCTree.Tag.METHODDEF)
                                    && ((JCMethodDecl)l.head).body != null
                                    && ((JCMethodDecl)l.head).name == ((JCMethodDecl)l.head).name.table.names.init
                                    && TreeInfo.firstConstructorCall(l.head) == null) {
                                ((JCMethodDecl)l.head).body.stats = ((JCMethodDecl)l.head).body.stats.append(generateFakeConstructorCall(((JCMethodDecl)l.head).sym));
                            }
                            if (hasError) {
                                if (l.head.hasTag(JCTree.Tag.CLASSDEF) && tree.sym.members_field.includes(((JCClassDecl)l.head).sym)) {
                                    last = l;
                                } else {
                                    if (last != null)
                                        last.tail = l.tail;
                                    else
                                        tree.defs = l.tail;
                                }
                                if (classLevelErrTree == null) {
                                    if (err != null) {
                                        classLevelErrTree = err.getTree();
                                        classLevelErrMessage = err.getMessage(null);
                                    } else {
                                        classLevelErrTree = l.head;
                                    }
                                }
                            } else {
                                last = l;
                            }
                        }
                    }
                    if (classLevelErrTree != null) {
                        if (staticInit != null)
                            staticInit.stats = List.of(generateErrStat(classLevelErrTree, classLevelErrMessage));
                        else
                            tree.defs = tree.defs.prepend(generateErrStaticInit(classLevelErrTree, classLevelErrMessage));
                    }
                    for (MethodSymbol symbol : nonAbstractMethods) {
                        if ((symbol.flags() & Flags.BRIDGE) != 0) {
                            continue;
                        }
                        if ((symbol.owner.flags() & Flags.ENUM) != 0) {
                            if ((symbol.name == symbol.name.table.names.values
                                    && symbol.type.asMethodType().argtypes.isEmpty())
                                    || (symbol.name == symbol.name.table.names.valueOf
                                    && types.isSameType(symbol.type.asMethodType().argtypes.head, enter.syms.stringType)
                                    && symbol.type.asMethodType().argtypes.tail.isEmpty())) {
                                continue;
                            }
                        }
                        tree.defs = tree.defs.prepend(generateErrMethod(symbol));
                    }
                }
            } finally {
                staticInit = oldStaticinit;
                classLevelErrTree = oldClassLevelErrTree;
                classLevelErrMessage = oldClassLevelErrMessage;
                err = oldErr;
                isErrClass = oldIsErrClass;
                insideErrEnum = oldInsideErrEnum;
                hasError = oldHasError;
                make = oldMake;
            }
        } finally {
            attrEnv = oldEnv;
        }
    }

    public JCTree translateTopLevelClass(Env<AttrContext> env, JCTree tree, TreeMaker localMake) {
        try {
            attrEnv = env;
            make = localMake;
            hasError = false;
            insideErrEnum = false;
            parents = List.nil();
            if (runtimeExceptionConstructor == null) {
                runtimeExceptionConstructor = rs.resolveConstructor(tree, attrEnv, syms.runtimeExceptionType, List.of(syms.stringType), null);
            }
            if (runtimeExceptionDefaultConstructor == null) {
                runtimeExceptionDefaultConstructor = rs.resolveConstructor(tree, attrEnv, syms.runtimeExceptionType, List.<Type>nil(), null);
            }
            return translate(tree);
        } finally {
            attrEnv = null;
            make = null;
            fixedTopLevelName = null;
        }
    }

    public void flush() {
        repairedClasses.clear();
    }
}
