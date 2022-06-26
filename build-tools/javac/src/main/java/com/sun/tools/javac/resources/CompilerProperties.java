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


package com.sun.tools.javac.resources;

import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Flags.Flag;
import com.sun.tools.javac.code.Kinds.Kind;
import com.sun.tools.javac.code.Kinds.KindName;
import com.sun.tools.javac.code.Source;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.jvm.Profile;
import com.sun.tools.javac.jvm.Target;
import com.sun.tools.javac.main.Option;
import com.sun.tools.javac.parser.Tokens.TokenKind;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Name;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import com.sun.tools.javac.util.JCDiagnostic.Error;
import com.sun.tools.javac.util.JCDiagnostic.Warning;
import com.sun.tools.javac.util.JCDiagnostic.Note;
import com.sun.tools.javac.util.JCDiagnostic.Fragment;

public class CompilerProperties {
    public static class Errors {
        /**
         * compiler.err.abstract.cant.be.accessed.directly=\
         *    abstract {0} {1} in {2} cannot be accessed directly
         */
        public static Error AbstractCantBeAccessedDirectly(KindName arg0, Symbol arg1, Symbol arg2) {
            return new Error("compiler", "abstract.cant.be.accessed.directly", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.abstract.cant.be.instantiated=\
         *    {0} is abstract; cannot be instantiated
         */
        public static Error AbstractCantBeInstantiated(Symbol arg0) {
            return new Error("compiler", "abstract.cant.be.instantiated", arg0);
        }
        
        /**
         * compiler.err.abstract.meth.cant.have.body=\
         *    abstract methods cannot have a body
         */
        public static final Error AbstractMethCantHaveBody = new Error("compiler", "abstract.meth.cant.have.body");
        
        /**
         * compiler.err.add.exports.with.release=\
         *    exporting a package from system module {0} is not allowed with --release
         */
        public static Error AddExportsWithRelease(Symbol arg0) {
            return new Error("compiler", "add.exports.with.release", arg0);
        }
        
        /**
         * compiler.err.add.reads.with.release=\
         *    adding read edges for system module {0} is not allowed with --release
         */
        public static Error AddReadsWithRelease(Symbol arg0) {
            return new Error("compiler", "add.reads.with.release", arg0);
        }
        
        /**
         * compiler.err.addmods.all.module.path.invalid=\
         *    --add-modules ALL-MODULE-PATH can only be used when compiling the unnamed module or \
         *    when compiling in the context of an automatic module
         */
        public static final Error AddmodsAllModulePathInvalid = new Error("compiler", "addmods.all.module.path.invalid");
        
        /**
         * compiler.err.already.annotated=\
         *    {0} {1} has already been annotated
         */
        public static Error AlreadyAnnotated(KindName arg0, Symbol arg1) {
            return new Error("compiler", "already.annotated", arg0, arg1);
        }
        
        /**
         * compiler.err.already.defined=\
         *    {0} {1} is already defined in {2} {3}
         */
        public static Error AlreadyDefined(KindName arg0, Symbol arg1, KindName arg2, Symbol arg3) {
            return new Error("compiler", "already.defined", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.already.defined.in.clinit=\
         *    {0} {1} is already defined in {2} of {3} {4}
         */
        public static Error AlreadyDefinedInClinit(KindName arg0, Symbol arg1, KindName arg2, KindName arg3, Symbol arg4) {
            return new Error("compiler", "already.defined.in.clinit", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.already.defined.single.import=\
         *    a type with the same simple name is already defined by the single-type-import of {0}
         */
        public static Error AlreadyDefinedSingleImport(Symbol arg0) {
            return new Error("compiler", "already.defined.single.import", arg0);
        }
        
        /**
         * compiler.err.already.defined.static.single.import=\
         *    a type with the same simple name is already defined by the static single-type-import of {0}
         */
        public static Error AlreadyDefinedStaticSingleImport(Symbol arg0) {
            return new Error("compiler", "already.defined.static.single.import", arg0);
        }
        
        /**
         * compiler.err.already.defined.this.unit=\
         *    {0} is already defined in this compilation unit
         */
        public static Error AlreadyDefinedThisUnit(Symbol arg0) {
            return new Error("compiler", "already.defined.this.unit", arg0);
        }
        
        /**
         * compiler.err.annotation.decl.not.allowed.here=\
         *    annotation type declaration not allowed here
         */
        public static final Error AnnotationDeclNotAllowedHere = new Error("compiler", "annotation.decl.not.allowed.here");
        
        /**
         * compiler.err.annotation.missing.default.value=\
         *    annotation @{0} is missing a default value for the element ''{1}''
         */
        public static Error AnnotationMissingDefaultValue(Type arg0, List<? extends Name> arg1) {
            return new Error("compiler", "annotation.missing.default.value", arg0, arg1);
        }
        
        /**
         * compiler.err.annotation.missing.default.value.1=\
         *    annotation @{0} is missing default values for elements {1}
         */
        public static Error AnnotationMissingDefaultValue1(Type arg0, List<? extends Name> arg1) {
            return new Error("compiler", "annotation.missing.default.value.1", arg0, arg1);
        }
        
        /**
         * compiler.err.annotation.not.valid.for.type=\
         *    annotation not valid for an element of type {0}
         */
        public static Error AnnotationNotValidForType(Type arg0) {
            return new Error("compiler", "annotation.not.valid.for.type", arg0);
        }
        
        /**
         * compiler.err.annotation.type.not.applicable=\
         *    annotation type not applicable to this kind of declaration
         */
        public static final Error AnnotationTypeNotApplicable = new Error("compiler", "annotation.type.not.applicable");
        
        /**
         * compiler.err.annotation.type.not.applicable.to.type=\
         *    annotation @{0} not applicable in this type context
         */
        public static Error AnnotationTypeNotApplicableToType(Type arg0) {
            return new Error("compiler", "annotation.type.not.applicable.to.type", arg0);
        }
        
        /**
         * compiler.err.annotation.value.must.be.annotation=\
         *    annotation value must be an annotation
         */
        public static final Error AnnotationValueMustBeAnnotation = new Error("compiler", "annotation.value.must.be.annotation");
        
        /**
         * compiler.err.annotation.value.must.be.class.literal=\
         *    annotation value must be a class literal
         */
        public static final Error AnnotationValueMustBeClassLiteral = new Error("compiler", "annotation.value.must.be.class.literal");
        
        /**
         * compiler.err.annotation.value.must.be.name.value=\
         *    annotation values must be of the form ''name=value''
         */
        public static final Error AnnotationValueMustBeNameValue = new Error("compiler", "annotation.value.must.be.name.value");
        
        /**
         * compiler.err.annotation.value.not.allowable.type=\
         *    annotation value not of an allowable type
         */
        public static final Error AnnotationValueNotAllowableType = new Error("compiler", "annotation.value.not.allowable.type");
        
        /**
         * compiler.err.anon.class.impl.intf.no.args=\
         *    anonymous class implements interface; cannot have arguments
         */
        public static final Error AnonClassImplIntfNoArgs = new Error("compiler", "anon.class.impl.intf.no.args");
        
        /**
         * compiler.err.anon.class.impl.intf.no.qual.for.new=\
         *    anonymous class implements interface; cannot have qualifier for new
         */
        public static final Error AnonClassImplIntfNoQualForNew = new Error("compiler", "anon.class.impl.intf.no.qual.for.new");
        
        /**
         * compiler.err.anon.class.impl.intf.no.typeargs=\
         *    anonymous class implements interface; cannot have type arguments
         */
        public static final Error AnonClassImplIntfNoTypeargs = new Error("compiler", "anon.class.impl.intf.no.typeargs");
        
        /**
         * compiler.err.anonymous.diamond.method.does.not.override.superclass=\
         *    method does not override or implement a method from a supertype\n\
         *    {0}
         */
        public static Error AnonymousDiamondMethodDoesNotOverrideSuperclass(JCDiagnostic arg0) {
            return new Error("compiler", "anonymous.diamond.method.does.not.override.superclass", arg0);
        }
        
        /**
         * compiler.err.anonymous.diamond.method.does.not.override.superclass=\
         *    method does not override or implement a method from a supertype\n\
         *    {0}
         */
        public static Error AnonymousDiamondMethodDoesNotOverrideSuperclass(Fragment arg0) {
            return new Error("compiler", "anonymous.diamond.method.does.not.override.superclass", arg0);
        }
        
        /**
         * compiler.err.array.and.receiver =\
         *    legacy array notation not allowed on receiver parameter
         */
        public static final Error ArrayAndReceiver = new Error("compiler", "array.and.receiver");
        
        /**
         * compiler.err.array.and.varargs=\
         *    cannot declare both {0} and {1} in {2}
         */
        public static Error ArrayAndVarargs(Symbol arg0, Symbol arg1, Symbol arg2) {
            return new Error("compiler", "array.and.varargs", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.array.dimension.missing=\
         *    array dimension missing
         */
        public static final Error ArrayDimensionMissing = new Error("compiler", "array.dimension.missing");
        
        /**
         * compiler.err.array.req.but.found=\
         *    array required, but {0} found
         */
        public static Error ArrayReqButFound(Type arg0) {
            return new Error("compiler", "array.req.but.found", arg0);
        }
        
        /**
         * compiler.err.assert.as.identifier=\
         *    as of release 1.4, ''assert'' is a keyword, and may not be used as an identifier
         */
        public static final Error AssertAsIdentifier = new Error("compiler", "assert.as.identifier");
        
        /**
         * compiler.err.attribute.value.must.be.constant=\
         *    element value must be a constant expression
         */
        public static final Error AttributeValueMustBeConstant = new Error("compiler", "attribute.value.must.be.constant");
        
        /**
         * compiler.err.bad.functional.intf.anno=\
         *    Unexpected @FunctionalInterface annotation
         */
        public static final Error BadFunctionalIntfAnno = new Error("compiler", "bad.functional.intf.anno");
        
        /**
         * compiler.err.bad.functional.intf.anno.1=\
         *    Unexpected @FunctionalInterface annotation\n\
         *    {0}
         */
        public static Error BadFunctionalIntfAnno1(JCDiagnostic arg0) {
            return new Error("compiler", "bad.functional.intf.anno.1", arg0);
        }
        
        /**
         * compiler.err.bad.functional.intf.anno.1=\
         *    Unexpected @FunctionalInterface annotation\n\
         *    {0}
         */
        public static Error BadFunctionalIntfAnno1(Fragment arg0) {
            return new Error("compiler", "bad.functional.intf.anno.1", arg0);
        }
        
        /**
         * compiler.err.bad.initializer=\
         *    bad initializer for {0}
         */
        public static Error BadInitializer(String arg0) {
            return new Error("compiler", "bad.initializer", arg0);
        }
        
        /**
         * compiler.err.bad.name.for.option=\
         *    bad name in value for {0} option: ''{1}''
         */
        public static Error BadNameForOption(Option arg0, String arg1) {
            return new Error("compiler", "bad.name.for.option", arg0, arg1);
        }
        
        /**
         * compiler.err.bad.value.for.option=\
         *    bad value for {0} option: ''{1}''
         */
        public static Error BadValueForOption(String arg0, String arg1) {
            return new Error("compiler", "bad.value.for.option", arg0, arg1);
        }
        
        /**
         * compiler.err.break.outside.switch.expression=\
         *    attempt to break out of a switch expression
         */
        public static final Error BreakOutsideSwitchExpression = new Error("compiler", "break.outside.switch.expression");
        
        /**
         * compiler.err.break.outside.switch.loop=\
         *    break outside switch or loop
         */
        public static final Error BreakOutsideSwitchLoop = new Error("compiler", "break.outside.switch.loop");
        
        /**
         * compiler.err.call.must.be.first.stmt.in.ctor=\
         *    call to {0} must be first statement in constructor
         */
        public static Error CallMustBeFirstStmtInCtor(Name arg0) {
            return new Error("compiler", "call.must.be.first.stmt.in.ctor", arg0);
        }
        
        /**
         * compiler.err.call.to.super.not.allowed.in.enum.ctor=\
         *    call to super not allowed in enum constructor
         */
        public static Error CallToSuperNotAllowedInEnumCtor(Symbol arg0) {
            return new Error("compiler", "call.to.super.not.allowed.in.enum.ctor", arg0);
        }
        
        /**
         * compiler.err.cannot.create.array.with.diamond=\
         *    cannot create array with ''<>''
         */
        public static final Error CannotCreateArrayWithDiamond = new Error("compiler", "cannot.create.array.with.diamond");
        
        /**
         * compiler.err.cannot.create.array.with.type.arguments=\
         *    cannot create array with type arguments
         */
        public static final Error CannotCreateArrayWithTypeArguments = new Error("compiler", "cannot.create.array.with.type.arguments");
        
        /**
         * compiler.err.cannot.generate.class=\
         *    error while generating class {0}\n\
         *    ({1})
         */
        public static Error CannotGenerateClass(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "cannot.generate.class", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.access=\
         *    cannot access {0}\n\
         *    {1}
         */
        public static Error CantAccess(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.access", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.access=\
         *    cannot access {0}\n\
         *    {1}
         */
        public static Error CantAccess(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "cant.access", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond=\
         *    cannot infer type arguments for {0}
         */
        public static Error CantApplyDiamond(JCDiagnostic arg0, Void arg1) {
            return new Error("compiler", "cant.apply.diamond", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond=\
         *    cannot infer type arguments for {0}
         */
        public static Error CantApplyDiamond(Fragment arg0, Void arg1) {
            return new Error("compiler", "cant.apply.diamond", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(JCDiagnostic arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(JCDiagnostic arg0, Fragment arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(Fragment arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(Fragment arg0, Fragment arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(Type arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Error CantApplyDiamond1(Type arg0, Fragment arg1) {
            return new Error("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types;\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Error CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Error("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.cant.apply.symbols=\
         *    no suitable {0} found for {1}({2})
         */
        public static Error CantApplySymbols(Kind arg0, Name arg1, List<? extends Type> arg2) {
            return new Error("compiler", "cant.apply.symbols", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.cant.assign.val.to.final.var=\
         *    cannot assign a value to final variable {0}
         */
        public static Error CantAssignValToFinalVar(Symbol arg0) {
            return new Error("compiler", "cant.assign.val.to.final.var", arg0);
        }
        
        /**
         * compiler.err.cant.assign.val.to.this=\
         *    cannot assign to ''this''
         */
        public static final Error CantAssignValToThis = new Error("compiler", "cant.assign.val.to.this");
        
        /**
         * compiler.err.cant.deref=\
         *    {0} cannot be dereferenced
         */
        public static Error CantDeref(Type arg0) {
            return new Error("compiler", "cant.deref", arg0);
        }
        
        /**
         * compiler.err.cant.extend.intf.annotation=\
         *    ''extends'' not allowed for @interfaces
         */
        public static final Error CantExtendIntfAnnotation = new Error("compiler", "cant.extend.intf.annotation");
        
        /**
         * compiler.err.cant.infer.local.var.type=\
         *    cannot infer type for local variable {0}\n\
         *    ({1})
         */
        public static Error CantInferLocalVarType(Name arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.infer.local.var.type", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.infer.local.var.type=\
         *    cannot infer type for local variable {0}\n\
         *    ({1})
         */
        public static Error CantInferLocalVarType(Name arg0, Fragment arg1) {
            return new Error("compiler", "cant.infer.local.var.type", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.inherit.diff.arg=\
         *    {0} cannot be inherited with different arguments: <{1}> and <{2}>
         */
        public static Error CantInheritDiffArg(Symbol arg0, String arg1, String arg2) {
            return new Error("compiler", "cant.inherit.diff.arg", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.cant.inherit.from.anon=\
         *    cannot inherit from anonymous class
         */
        public static final Error CantInheritFromAnon = new Error("compiler", "cant.inherit.from.anon");
        
        /**
         * compiler.err.cant.inherit.from.final=\
         *    cannot inherit from final {0}
         */
        public static Error CantInheritFromFinal(Symbol arg0) {
            return new Error("compiler", "cant.inherit.from.final", arg0);
        }
        
        /**
         * compiler.err.cant.inherit.from.sealed=\
         *    class is not allowed to extend sealed class: {0} \
         *    (as it is not listed in its 'permits' clause)
         */
        public static Error CantInheritFromSealed(Symbol arg0) {
            return new Error("compiler", "cant.inherit.from.sealed", arg0);
        }
        
        /**
         * compiler.err.cant.read.file=\
         *    cannot read: {0}
         */
        public static final Error CantReadFile = new Error("compiler", "cant.read.file");
        
        /**
         * compiler.err.cant.ref.before.ctor.called=\
         *    cannot reference {0} before supertype constructor has been called
         */
        public static Error CantRefBeforeCtorCalled(Symbol arg0) {
            return new Error("compiler", "cant.ref.before.ctor.called", arg0);
        }
        
        /**
         * compiler.err.cant.ref.before.ctor.called=\
         *    cannot reference {0} before supertype constructor has been called
         */
        public static Error CantRefBeforeCtorCalled(String arg0) {
            return new Error("compiler", "cant.ref.before.ctor.called", arg0);
        }
        
        /**
         * compiler.err.cant.ref.non.effectively.final.var=\
         *    local variables referenced from {1} must be final or effectively final
         */
        public static Error CantRefNonEffectivelyFinalVar(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "cant.ref.non.effectively.final.var", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.ref.non.effectively.final.var=\
         *    local variables referenced from {1} must be final or effectively final
         */
        public static Error CantRefNonEffectivelyFinalVar(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "cant.ref.non.effectively.final.var", arg0, arg1);
        }
        
        /**
         * compiler.err.cant.resolve=\
         *    cannot find symbol\n\
         *    symbol: {0} {1}
         */
        public static Error CantResolve(KindName arg0, Name arg1, Void arg2, Void arg3) {
            return new Error("compiler", "cant.resolve", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.cant.resolve.args=\
         *    cannot find symbol\n\
         *    symbol: {0} {1}({3})
         */
        public static Error CantResolveArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3) {
            return new Error("compiler", "cant.resolve.args", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.cant.resolve.args.params=\
         *    cannot find symbol\n\
         *    symbol: {0} <{2}>{1}({3})
         */
        public static Error CantResolveArgsParams(KindName arg0, Name arg1, List<? extends Type> arg2, List<? extends Type> arg3) {
            return new Error("compiler", "cant.resolve.args.params", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.cant.resolve.location=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}\n\
         *    location: {4}
         */
        public static Error CantResolveLocation(KindName arg0, Name arg1, Void arg2, Void arg3, JCDiagnostic arg4) {
            return new Error("compiler", "cant.resolve.location", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.resolve.location=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}\n\
         *    location: {4}
         */
        public static Error CantResolveLocation(KindName arg0, Name arg1, Void arg2, Void arg3, Fragment arg4) {
            return new Error("compiler", "cant.resolve.location", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.resolve.location.args=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}({3})\n\
         *    location: {4}
         */
        public static Error CantResolveLocationArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3, JCDiagnostic arg4) {
            return new Error("compiler", "cant.resolve.location.args", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.resolve.location.args=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}({3})\n\
         *    location: {4}
         */
        public static Error CantResolveLocationArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3, Fragment arg4) {
            return new Error("compiler", "cant.resolve.location.args", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.resolve.location.args.params=\
         *    cannot find symbol\n\
         *    symbol:   {0} <{2}>{1}({3})\n\
         *    location: {4}
         */
        @SuppressWarnings("rawtypes")
        public static Error CantResolveLocationArgsParams(KindName arg0, Name arg1, List<? extends Type> arg2, List arg3, JCDiagnostic arg4) {
            return new Error("compiler", "cant.resolve.location.args.params", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.resolve.location.args.params=\
         *    cannot find symbol\n\
         *    symbol:   {0} <{2}>{1}({3})\n\
         *    location: {4}
         */
        @SuppressWarnings("rawtypes")
        public static Error CantResolveLocationArgsParams(KindName arg0, Name arg1, List<? extends Type> arg2, List arg3, Fragment arg4) {
            return new Error("compiler", "cant.resolve.location.args.params", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.cant.select.static.class.from.param.type=\
         *    cannot select a static class from a parameterized type
         */
        public static final Error CantSelectStaticClassFromParamType = new Error("compiler", "cant.select.static.class.from.param.type");
        
        /**
         * compiler.err.cant.type.annotate.scoping=\
         *    scoping construct cannot be annotated with type-use annotations: {0}
         */
        public static Error CantTypeAnnotateScoping(List<? extends Compound> arg0) {
            return new Error("compiler", "cant.type.annotate.scoping", arg0);
        }
        
        /**
         * compiler.err.cant.type.annotate.scoping.1=\
         *    scoping construct cannot be annotated with type-use annotation: {0}
         */
        public static Error CantTypeAnnotateScoping1(Compound arg0) {
            return new Error("compiler", "cant.type.annotate.scoping.1", arg0);
        }
        
        /**
         * compiler.err.catch.without.try=\
         *    ''catch'' without ''try''
         */
        public static final Error CatchWithoutTry = new Error("compiler", "catch.without.try");
        
        /**
         * compiler.err.clash.with.pkg.of.same.name=\
         *    {0} {1} clashes with package of same name
         */
        public static Error ClashWithPkgOfSameName(KindName arg0, Symbol arg1) {
            return new Error("compiler", "clash.with.pkg.of.same.name", arg0, arg1);
        }
        
        /**
         * compiler.err.class.cant.write=\
         *    error while writing {0}: {1}
         */
        public static Error ClassCantWrite(Symbol arg0, String arg1) {
            return new Error("compiler", "class.cant.write", arg0, arg1);
        }
        
        /**
         * compiler.err.class.in.module.cant.extend.sealed.in.diff.module=\
         *    class {0} in module {1} cannot extend a sealed class in a different module
         */
        public static Error ClassInModuleCantExtendSealedInDiffModule(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "class.in.module.cant.extend.sealed.in.diff.module", arg0, arg1);
        }
        
        /**
         * compiler.err.class.in.unnamed.module.cant.extend.sealed.in.diff.package=\
         *    class {0} in unnamed module cannot extend a sealed class in a different package
         */
        public static Error ClassInUnnamedModuleCantExtendSealedInDiffPackage(Symbol arg0) {
            return new Error("compiler", "class.in.unnamed.module.cant.extend.sealed.in.diff.package", arg0);
        }
        
        /**
         * compiler.err.class.not.allowed=\
         *    class, interface or enum declaration not allowed here
         */
        public static final Error ClassNotAllowed = new Error("compiler", "class.not.allowed");
        
        /**
         * compiler.err.class.public.should.be.in.file=\
         *    {0} {1} is public, should be declared in a file named {1}.java
         */
        public static Error ClassPublicShouldBeInFile(KindName arg0, Name arg1) {
            return new Error("compiler", "class.public.should.be.in.file", arg0, arg1);
        }
        
        /**
         * compiler.err.concrete.inheritance.conflict=\
         *    methods {0} from {1} and {2} from {3} are inherited with the same signature
         */
        public static Error ConcreteInheritanceConflict(Symbol arg0, Type arg1, Symbol arg2, Type arg3, Type arg4) {
            return new Error("compiler", "concrete.inheritance.conflict", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.err.conflicting.exports=\
         *    duplicate or conflicting exports: {0}
         */
        public static Error ConflictingExports(Symbol arg0) {
            return new Error("compiler", "conflicting.exports", arg0);
        }
        
        /**
         * compiler.err.conflicting.exports.to.module=\
         *    duplicate or conflicting exports to module: {0}
         */
        public static Error ConflictingExportsToModule(Symbol arg0) {
            return new Error("compiler", "conflicting.exports.to.module", arg0);
        }
        
        /**
         * compiler.err.conflicting.opens=\
         *    duplicate or conflicting opens: {0}
         */
        public static Error ConflictingOpens(Symbol arg0) {
            return new Error("compiler", "conflicting.opens", arg0);
        }
        
        /**
         * compiler.err.conflicting.opens.to.module=\
         *    duplicate or conflicting opens to module: {0}
         */
        public static Error ConflictingOpensToModule(Symbol arg0) {
            return new Error("compiler", "conflicting.opens.to.module", arg0);
        }
        
        /**
         * compiler.err.const.expr.req=\
         *    constant expression required
         */
        public static final Error ConstExprReq = new Error("compiler", "const.expr.req");
        
        /**
         * compiler.err.constant.label.not.compatible=\
         *    constant label of type {0} is not compatible with switch selector type {1}
         */
        public static Error ConstantLabelNotCompatible(Type arg0, Type arg1) {
            return new Error("compiler", "constant.label.not.compatible", arg0, arg1);
        }
        
        /**
         * compiler.err.cont.outside.loop=\
         *    continue outside of loop
         */
        public static final Error ContOutsideLoop = new Error("compiler", "cont.outside.loop");
        
        /**
         * compiler.err.continue.outside.switch.expression=\
         *    attempt to continue out of a switch expression
         */
        public static final Error ContinueOutsideSwitchExpression = new Error("compiler", "continue.outside.switch.expression");
        
        /**
         * compiler.err.cyclic.annotation.element=\
         *    type of element {0} is cyclic
         */
        public static Error CyclicAnnotationElement(Symbol arg0) {
            return new Error("compiler", "cyclic.annotation.element", arg0);
        }
        
        /**
         * compiler.err.cyclic.inheritance=\
         *    cyclic inheritance involving {0}
         */
        public static Error CyclicInheritance(Symbol arg0) {
            return new Error("compiler", "cyclic.inheritance", arg0);
        }
        
        /**
         * compiler.err.cyclic.inheritance=\
         *    cyclic inheritance involving {0}
         */
        public static Error CyclicInheritance(Type arg0) {
            return new Error("compiler", "cyclic.inheritance", arg0);
        }
        
        /**
         * compiler.err.cyclic.requires=\
         *    cyclic dependence involving {0}
         */
        public static Error CyclicRequires(Symbol arg0) {
            return new Error("compiler", "cyclic.requires", arg0);
        }
        
        /**
         * compiler.err.dc.bad.entity=\
         *    bad HTML entity
         */
        public static final Error DcBadEntity = new Error("compiler", "dc.bad.entity");
        
        /**
         * compiler.err.dc.bad.inline.tag=\
         *    incorrect use of inline tag
         */
        public static final Error DcBadInlineTag = new Error("compiler", "dc.bad.inline.tag");
        
        /**
         * compiler.err.dc.gt.expected=\
         *    ''>'' expected
         */
        public static final Error DcGtExpected = new Error("compiler", "dc.gt.expected");
        
        /**
         * compiler.err.dc.identifier.expected=\
         *    identifier expected
         */
        public static final Error DcIdentifierExpected = new Error("compiler", "dc.identifier.expected");
        
        /**
         * compiler.err.dc.malformed.html=\
         *    malformed HTML
         */
        public static final Error DcMalformedHtml = new Error("compiler", "dc.malformed.html");
        
        /**
         * compiler.err.dc.missing.semicolon=\
         *    semicolon missing
         */
        public static final Error DcMissingSemicolon = new Error("compiler", "dc.missing.semicolon");
        
        /**
         * compiler.err.dc.no.content=\
         *    no content
         */
        public static final Error DcNoContent = new Error("compiler", "dc.no.content");
        
        /**
         * compiler.err.dc.no.tag.name=\
         *    no tag name after '@'
         */
        public static final Error DcNoTagName = new Error("compiler", "dc.no.tag.name");
        
        /**
         * compiler.err.dc.ref.bad.parens=\
         *    unexpected text after parenthesis
         */
        public static final Error DcRefBadParens = new Error("compiler", "dc.ref.bad.parens");
        
        /**
         * compiler.err.dc.ref.syntax.error=\
         *    syntax error in reference
         */
        public static final Error DcRefSyntaxError = new Error("compiler", "dc.ref.syntax.error");
        
        /**
         * compiler.err.dc.ref.unexpected.input=\
         *    unexpected text
         */
        public static final Error DcRefUnexpectedInput = new Error("compiler", "dc.ref.unexpected.input");
        
        /**
         * compiler.err.dc.unexpected.content=\
         *    unexpected content
         */
        public static final Error DcUnexpectedContent = new Error("compiler", "dc.unexpected.content");
        
        /**
         * compiler.err.dc.unterminated.inline.tag=\
         *    unterminated inline tag
         */
        public static final Error DcUnterminatedInlineTag = new Error("compiler", "dc.unterminated.inline.tag");
        
        /**
         * compiler.err.dc.unterminated.signature=\
         *    unterminated signature
         */
        public static final Error DcUnterminatedSignature = new Error("compiler", "dc.unterminated.signature");
        
        /**
         * compiler.err.dc.unterminated.string=\
         *    unterminated string
         */
        public static final Error DcUnterminatedString = new Error("compiler", "dc.unterminated.string");
        
        /**
         * compiler.err.default.allowed.in.intf.annotation.member=\
         *    default value only allowed in an annotation type declaration
         */
        public static final Error DefaultAllowedInIntfAnnotationMember = new Error("compiler", "default.allowed.in.intf.annotation.member");
        
        /**
         * compiler.err.default.overrides.object.member=\
         *    default method {0} in {1} {2} overrides a member of java.lang.Object
         */
        public static Error DefaultOverridesObjectMember(Name arg0, KindName arg1, Symbol arg2) {
            return new Error("compiler", "default.overrides.object.member", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.does.not.override.abstract=\
         *    {0} is not abstract and does not override abstract method {1} in {2}
         */
        public static Error DoesNotOverrideAbstract(Symbol arg0, Symbol arg1, Symbol arg2) {
            return new Error("compiler", "does.not.override.abstract", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.doesnt.exist=\
         *    package {0} does not exist
         */
        public static Error DoesntExist(Symbol arg0) {
            return new Error("compiler", "doesnt.exist", arg0);
        }
        
        /**
         * compiler.err.dot.class.expected=\
         *    ''.class'' expected
         */
        public static final Error DotClassExpected = new Error("compiler", "dot.class.expected");
        
        /**
         * compiler.err.duplicate.annotation.invalid.repeated=\
         *    annotation {0} is not a valid repeatable annotation
         */
        public static Error DuplicateAnnotationInvalidRepeated(Type arg0) {
            return new Error("compiler", "duplicate.annotation.invalid.repeated", arg0);
        }
        
        /**
         * compiler.err.duplicate.annotation.member.value=\
         *    duplicate element ''{0}'' in annotation @{1}.
         */
        public static Error DuplicateAnnotationMemberValue(Name arg0, Type arg1) {
            return new Error("compiler", "duplicate.annotation.member.value", arg0, arg1);
        }
        
        /**
         * compiler.err.duplicate.annotation.missing.container=\
         *    {0} is not a repeatable annotation type
         */
        public static Error DuplicateAnnotationMissingContainer(Type arg0) {
            return new Error("compiler", "duplicate.annotation.missing.container", arg0);
        }
        
        /**
         * compiler.err.duplicate.case.label=\
         *    duplicate case label
         */
        public static final Error DuplicateCaseLabel = new Error("compiler", "duplicate.case.label");
        
        /**
         * compiler.err.duplicate.class=\
         *    duplicate class: {0}
         */
        public static Error DuplicateClass(Name arg0) {
            return new Error("compiler", "duplicate.class", arg0);
        }
        
        /**
         * compiler.err.duplicate.default.label=\
         *    duplicate default label
         */
        public static final Error DuplicateDefaultLabel = new Error("compiler", "duplicate.default.label");
        
        /**
         * compiler.err.duplicate.module=\
         *    duplicate module: {0}
         */
        public static Error DuplicateModule(Symbol arg0) {
            return new Error("compiler", "duplicate.module", arg0);
        }
        
        /**
         * compiler.err.duplicate.module.on.path=\
         *    duplicate module on {0}\nmodule in {1}
         */
        public static Error DuplicateModuleOnPath(Fragment arg0, Name arg1) {
            return new Error("compiler", "duplicate.module.on.path", arg0, arg1);
        }
        
        /**
         * compiler.err.duplicate.provides=\
         *    duplicate provides: service {0}, implementation {1}
         */
        public static Error DuplicateProvides(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "duplicate.provides", arg0, arg1);
        }
        
        /**
         * compiler.err.duplicate.requires=\
         *    duplicate requires: {0}
         */
        public static Error DuplicateRequires(Symbol arg0) {
            return new Error("compiler", "duplicate.requires", arg0);
        }
        
        /**
         * compiler.err.duplicate.total.pattern=\
         *    duplicate total pattern
         */
        public static final Error DuplicateTotalPattern = new Error("compiler", "duplicate.total.pattern");
        
        /**
         * compiler.err.duplicate.uses=\
         *    duplicate uses: {0}
         */
        public static Error DuplicateUses(Symbol arg0) {
            return new Error("compiler", "duplicate.uses", arg0);
        }
        
        /**
         * compiler.err.else.without.if=\
         *    ''else'' without ''if''
         */
        public static final Error ElseWithoutIf = new Error("compiler", "else.without.if");
        
        /**
         * compiler.err.empty.A.argument=\
         *    -A requires an argument; use ''-Akey'' or ''-Akey=value''
         */
        public static final Error EmptyAArgument = new Error("compiler", "empty.A.argument");
        
        /**
         * compiler.err.empty.char.lit=\
         *    empty character literal
         */
        public static final Error EmptyCharLit = new Error("compiler", "empty.char.lit");
        
        /**
         * compiler.err.encl.class.required=\
         *    an enclosing instance that contains {0} is required
         */
        public static Error EnclClassRequired(Symbol arg0) {
            return new Error("compiler", "encl.class.required", arg0);
        }
        
        /**
         * compiler.err.enum.annotation.must.be.enum.constant=\
         *    an enum annotation value must be an enum constant
         */
        public static final Error EnumAnnotationMustBeEnumConstant = new Error("compiler", "enum.annotation.must.be.enum.constant");
        
        /**
         * compiler.err.enum.as.identifier=\
         *    as of release 5, ''enum'' is a keyword, and may not be used as an identifier
         */
        public static final Error EnumAsIdentifier = new Error("compiler", "enum.as.identifier");
        
        /**
         * compiler.err.enum.cant.be.instantiated=\
         *    enum types may not be instantiated
         */
        public static final Error EnumCantBeInstantiated = new Error("compiler", "enum.cant.be.instantiated");
        
        /**
         * compiler.err.enum.constant.expected=\
         *    enum constant expected here
         */
        public static final Error EnumConstantExpected = new Error("compiler", "enum.constant.expected");
        
        /**
         * compiler.err.enum.constant.not.expected=\
         *    enum constant not expected here
         */
        public static final Error EnumConstantNotExpected = new Error("compiler", "enum.constant.not.expected");
        
        /**
         * compiler.err.enum.label.must.be.unqualified.enum=\
         *    an enum switch case label must be the unqualified name of an enumeration constant
         */
        public static final Error EnumLabelMustBeUnqualifiedEnum = new Error("compiler", "enum.label.must.be.unqualified.enum");
        
        /**
         * compiler.err.enum.no.finalize=\
         *    enums cannot have finalize methods
         */
        public static final Error EnumNoFinalize = new Error("compiler", "enum.no.finalize");
        
        /**
         * compiler.err.enum.no.subclassing=\
         *    classes cannot directly extend java.lang.Enum
         */
        public static final Error EnumNoSubclassing = new Error("compiler", "enum.no.subclassing");
        
        /**
         * compiler.err.enum.types.not.extensible=\
         *    enum types are not extensible
         */
        public static final Error EnumTypesNotExtensible = new Error("compiler", "enum.types.not.extensible");
        
        /**
         * compiler.err.error=\
         *    error:\u0020
         */
        public static final Error Error = new Error("compiler", "error");
        
        /**
         * compiler.err.error.reading.file=\
         *    error reading {0}; {1}
         */
        public static Error ErrorReadingFile(File arg0, String arg1) {
            return new Error("compiler", "error.reading.file", arg0, arg1);
        }
        
        /**
         * compiler.err.error.reading.file=\
         *    error reading {0}; {1}
         */
        public static Error ErrorReadingFile(JavaFileObject arg0, String arg1) {
            return new Error("compiler", "error.reading.file", arg0, arg1);
        }
        
        /**
         * compiler.err.error.reading.file=\
         *    error reading {0}; {1}
         */
        public static Error ErrorReadingFile(Path arg0, String arg1) {
            return new Error("compiler", "error.reading.file", arg0, arg1);
        }
        
        /**
         * compiler.err.error.writing.file=\
         *    error writing {0}; {1}
         */
        public static Error ErrorWritingFile(String arg0, String arg1) {
            return new Error("compiler", "error.writing.file", arg0, arg1);
        }
        
        /**
         * compiler.err.except.already.caught=\
         *    exception {0} has already been caught
         */
        public static Error ExceptAlreadyCaught(Type arg0) {
            return new Error("compiler", "except.already.caught", arg0);
        }
        
        /**
         * compiler.err.except.never.thrown.in.try=\
         *    exception {0} is never thrown in body of corresponding try statement
         */
        public static Error ExceptNeverThrownInTry(Type arg0) {
            return new Error("compiler", "except.never.thrown.in.try", arg0);
        }
        
        /**
         * compiler.err.expected=\
         *    {0} expected
         */
        public static Error Expected(TokenKind arg0) {
            return new Error("compiler", "expected", arg0);
        }
        
        /**
         * compiler.err.expected.module=\
         *    expected ''module''
         */
        public static final Error ExpectedModule = new Error("compiler", "expected.module");
        
        /**
         * compiler.err.expected.module.or.open=\
         *    ''module'' or ''open'' expected
         */
        public static final Error ExpectedModuleOrOpen = new Error("compiler", "expected.module.or.open");
        
        /**
         * compiler.err.expected.str=\
         *    {0} expected
         */
        public static Error ExpectedStr(String arg0) {
            return new Error("compiler", "expected.str", arg0);
        }
        
        /**
         * compiler.err.expected2=\
         *    {0} or {1} expected
         */
        public static Error Expected2(TokenKind arg0, TokenKind arg1) {
            return new Error("compiler", "expected2", arg0, arg1);
        }
        
        /**
         * compiler.err.expected3=\
         *    {0}, {1}, or {2} expected
         */
        public static Error Expected3(TokenKind arg0, TokenKind arg1, TokenKind arg2) {
            return new Error("compiler", "expected3", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.expected4=\
         *    {0}, {1}, {2}, or {3} expected
         */
        public static Error Expected4(TokenKind arg0, TokenKind arg1, TokenKind arg2, String arg3) {
            return new Error("compiler", "expected4", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.expression.not.allowable.as.annotation.value=\
         *    expression not allowed as annotation value
         */
        public static final Error ExpressionNotAllowableAsAnnotationValue = new Error("compiler", "expression.not.allowable.as.annotation.value");
        
        /**
         * compiler.err.feature.not.supported.in.source=\
         *   {0} is not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Error FeatureNotSupportedInSource(JCDiagnostic arg0, String arg1, String arg2) {
            return new Error("compiler", "feature.not.supported.in.source", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.feature.not.supported.in.source=\
         *   {0} is not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Error FeatureNotSupportedInSource(Fragment arg0, String arg1, String arg2) {
            return new Error("compiler", "feature.not.supported.in.source", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.feature.not.supported.in.source.plural=\
         *   {0} are not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Error FeatureNotSupportedInSourcePlural(JCDiagnostic arg0, String arg1, String arg2) {
            return new Error("compiler", "feature.not.supported.in.source.plural", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.feature.not.supported.in.source.plural=\
         *   {0} are not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Error FeatureNotSupportedInSourcePlural(Fragment arg0, String arg1, String arg2) {
            return new Error("compiler", "feature.not.supported.in.source.plural", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.file.not.directory=\
         *    not a directory: {0}
         */
        public static Error FileNotDirectory(String arg0) {
            return new Error("compiler", "file.not.directory", arg0);
        }
        
        /**
         * compiler.err.file.not.file=\
         *    not a file: {0}
         */
        public static Error FileNotFile(Object arg0) {
            return new Error("compiler", "file.not.file", arg0);
        }
        
        /**
         * compiler.err.file.not.found=\
         *    file not found: {0}
         */
        public static Error FileNotFound(String arg0) {
            return new Error("compiler", "file.not.found", arg0);
        }
        
        /**
         * compiler.err.file.patched.and.msp=\
         *    file accessible from both --patch-module and --module-source-path, \
         *    but belongs to a different module on each path: {0}, {1}
         */
        public static Error FilePatchedAndMsp(Name arg0, Name arg1) {
            return new Error("compiler", "file.patched.and.msp", arg0, arg1);
        }
        
        /**
         * compiler.err.file.sb.on.source.or.patch.path.for.module=\
         *    file should be on source path, or on patch path for module
         */
        public static final Error FileSbOnSourceOrPatchPathForModule = new Error("compiler", "file.sb.on.source.or.patch.path.for.module");
        
        /**
         * compiler.err.final.parameter.may.not.be.assigned=\
         *    final parameter {0} may not be assigned
         */
        public static Error FinalParameterMayNotBeAssigned(Symbol arg0) {
            return new Error("compiler", "final.parameter.may.not.be.assigned", arg0);
        }
        
        /**
         * compiler.err.finally.without.try=\
         *    ''finally'' without ''try''
         */
        public static final Error FinallyWithoutTry = new Error("compiler", "finally.without.try");
        
        /**
         * compiler.err.first.statement.must.be.call.to.another.constructor=\
         *    constructor is not canonical, so its first statement must invoke another constructor of class {0}
         */
        public static Error FirstStatementMustBeCallToAnotherConstructor(Symbol arg0) {
            return new Error("compiler", "first.statement.must.be.call.to.another.constructor", arg0);
        }
        
        /**
         * compiler.err.flows.through.from.pattern=\
         *    illegal fall-through from a pattern
         */
        public static final Error FlowsThroughFromPattern = new Error("compiler", "flows.through.from.pattern");
        
        /**
         * compiler.err.flows.through.to.pattern=\
         *    illegal fall-through to a pattern
         */
        public static final Error FlowsThroughToPattern = new Error("compiler", "flows.through.to.pattern");
        
        /**
         * compiler.err.foreach.not.applicable.to.type=\
         *    for-each not applicable to expression type\n\
         *    required: {1}\n\
         *    found:    {0}
         */
        public static Error ForeachNotApplicableToType(Type arg0, JCDiagnostic arg1) {
            return new Error("compiler", "foreach.not.applicable.to.type", arg0, arg1);
        }
        
        /**
         * compiler.err.foreach.not.applicable.to.type=\
         *    for-each not applicable to expression type\n\
         *    required: {1}\n\
         *    found:    {0}
         */
        public static Error ForeachNotApplicableToType(Type arg0, Fragment arg1) {
            return new Error("compiler", "foreach.not.applicable.to.type", arg0, arg1);
        }
        
        /**
         * compiler.err.fp.number.too.large=\
         *    floating-point number too large
         */
        public static final Error FpNumberTooLarge = new Error("compiler", "fp.number.too.large");
        
        /**
         * compiler.err.fp.number.too.small=\
         *    floating-point number too small
         */
        public static final Error FpNumberTooSmall = new Error("compiler", "fp.number.too.small");
        
        /**
         * compiler.err.generic.array.creation=\
         *    generic array creation
         */
        public static final Error GenericArrayCreation = new Error("compiler", "generic.array.creation");
        
        /**
         * compiler.err.generic.throwable=\
         *    a generic class may not extend java.lang.Throwable
         */
        public static final Error GenericThrowable = new Error("compiler", "generic.throwable");
        
        /**
         * compiler.err.icls.cant.have.static.decl=\
         *    Illegal static declaration in inner class {0}\n\
         *    modifier \''static\'' is only allowed in constant variable declarations
         */
        public static Error IclsCantHaveStaticDecl(Symbol arg0) {
            return new Error("compiler", "icls.cant.have.static.decl", arg0);
        }
        
        /**
         * compiler.err.illegal.argument.for.option=\
         *    illegal argument for {0}: {1}
         */
        public static Error IllegalArgumentForOption(String arg0, String arg1) {
            return new Error("compiler", "illegal.argument.for.option", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.array.creation.both.dimension.and.initialization=\
         *    array creation with both dimension expression and initialization is illegal
         */
        public static final Error IllegalArrayCreationBothDimensionAndInitialization = new Error("compiler", "illegal.array.creation.both.dimension.and.initialization");
        
        /**
         * compiler.err.illegal.char=\
         *    illegal character: ''{0}''
         */
        public static Error IllegalChar(String arg0) {
            return new Error("compiler", "illegal.char", arg0);
        }
        
        /**
         * compiler.err.illegal.char.for.encoding=\
         *    unmappable character (0x{0}) for encoding {1}
         */
        public static Error IllegalCharForEncoding(String arg0, String arg1) {
            return new Error("compiler", "illegal.char.for.encoding", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.combination.of.modifiers=\
         *    illegal combination of modifiers: {0} and {1}
         */
        public static Error IllegalCombinationOfModifiers(Set<? extends Flag> arg0, Set<? extends Flag> arg1) {
            return new Error("compiler", "illegal.combination.of.modifiers", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.default.super.call=\
         *    bad type qualifier {0} in default super call\n\
         *    {1}
         */
        public static Error IllegalDefaultSuperCall(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "illegal.default.super.call", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.default.super.call=\
         *    bad type qualifier {0} in default super call\n\
         *    {1}
         */
        public static Error IllegalDefaultSuperCall(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "illegal.default.super.call", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.default.super.call=\
         *    bad type qualifier {0} in default super call\n\
         *    {1}
         */
        public static Error IllegalDefaultSuperCall(Type arg0, JCDiagnostic arg1) {
            return new Error("compiler", "illegal.default.super.call", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.default.super.call=\
         *    bad type qualifier {0} in default super call\n\
         *    {1}
         */
        public static Error IllegalDefaultSuperCall(Type arg0, Fragment arg1) {
            return new Error("compiler", "illegal.default.super.call", arg0, arg1);
        }
        
        /**
         * compiler.err.illegal.dot=\
         *    illegal ''.''
         */
        public static final Error IllegalDot = new Error("compiler", "illegal.dot");
        
        /**
         * compiler.err.illegal.enum.static.ref=\
         *    illegal reference to static field from initializer
         */
        public static final Error IllegalEnumStaticRef = new Error("compiler", "illegal.enum.static.ref");
        
        /**
         * compiler.err.illegal.esc.char=\
         *    illegal escape character
         */
        public static final Error IllegalEscChar = new Error("compiler", "illegal.esc.char");
        
        /**
         * compiler.err.illegal.forward.ref=\
         *    illegal forward reference
         */
        public static final Error IllegalForwardRef = new Error("compiler", "illegal.forward.ref");
        
        /**
         * compiler.err.illegal.initializer.for.type=\
         *    illegal initializer for {0}
         */
        public static Error IllegalInitializerForType(Type arg0) {
            return new Error("compiler", "illegal.initializer.for.type", arg0);
        }
        
        /**
         * compiler.err.illegal.line.end.in.char.lit=\
         *    illegal line end in character literal
         */
        public static final Error IllegalLineEndInCharLit = new Error("compiler", "illegal.line.end.in.char.lit");
        
        /**
         * compiler.err.illegal.nonascii.digit=\
         *    illegal non-ASCII digit
         */
        public static final Error IllegalNonasciiDigit = new Error("compiler", "illegal.nonascii.digit");
        
        /**
         * compiler.err.illegal.parenthesized.expression=\
         *    illegal parenthesized expression
         */
        public static final Error IllegalParenthesizedExpression = new Error("compiler", "illegal.parenthesized.expression");
        
        /**
         * compiler.err.illegal.qual.not.icls=\
         *    illegal qualifier; {0} is not an inner class
         */
        public static Error IllegalQualNotIcls(Symbol arg0) {
            return new Error("compiler", "illegal.qual.not.icls", arg0);
        }
        
        /**
         * compiler.err.illegal.record.component.name=\
         *    illegal record component name {0}
         */
        public static Error IllegalRecordComponentName(Symbol arg0) {
            return new Error("compiler", "illegal.record.component.name", arg0);
        }
        
        /**
         * compiler.err.illegal.ref.to.restricted.type=\
         *    illegal reference to restricted type ''{0}''
         */
        public static Error IllegalRefToRestrictedType(Name arg0) {
            return new Error("compiler", "illegal.ref.to.restricted.type", arg0);
        }
        
        /**
         * compiler.err.illegal.self.ref=\
         *    self-reference in initializer
         */
        public static final Error IllegalSelfRef = new Error("compiler", "illegal.self.ref");
        
        /**
         * compiler.err.illegal.start.of.expr=\
         *    illegal start of expression
         */
        public static final Error IllegalStartOfExpr = new Error("compiler", "illegal.start.of.expr");
        
        /**
         * compiler.err.illegal.start.of.stmt=\
         *    illegal start of statement
         */
        public static final Error IllegalStartOfStmt = new Error("compiler", "illegal.start.of.stmt");
        
        /**
         * compiler.err.illegal.start.of.type=\
         *    illegal start of type
         */
        public static final Error IllegalStartOfType = new Error("compiler", "illegal.start.of.type");
        
        /**
         * compiler.err.illegal.static.intf.meth.call=\
         *    illegal static interface method call\n\
         *    the receiver expression should be replaced with the type qualifier ''{0}''
         */
        public static Error IllegalStaticIntfMethCall(Type arg0) {
            return new Error("compiler", "illegal.static.intf.meth.call", arg0);
        }
        
        /**
         * compiler.err.illegal.text.block.open=\
         *    illegal text block open delimiter sequence, missing line terminator
         */
        public static final Error IllegalTextBlockOpen = new Error("compiler", "illegal.text.block.open");
        
        /**
         * compiler.err.illegal.underscore=\
         *    illegal underscore
         */
        public static final Error IllegalUnderscore = new Error("compiler", "illegal.underscore");
        
        /**
         * compiler.err.illegal.unicode.esc=\
         *    illegal unicode escape
         */
        public static final Error IllegalUnicodeEsc = new Error("compiler", "illegal.unicode.esc");
        
        /**
         * compiler.err.import.requires.canonical=\
         *    import requires canonical name for {0}
         */
        public static Error ImportRequiresCanonical(Symbol arg0) {
            return new Error("compiler", "import.requires.canonical", arg0);
        }
        
        /**
         * compiler.err.improperly.formed.type.inner.raw.param=\
         *    improperly formed type, type arguments given on a raw type
         */
        public static final Error ImproperlyFormedTypeInnerRawParam = new Error("compiler", "improperly.formed.type.inner.raw.param");
        
        /**
         * compiler.err.improperly.formed.type.param.missing=\
         *    improperly formed type, some parameters are missing
         */
        public static final Error ImproperlyFormedTypeParamMissing = new Error("compiler", "improperly.formed.type.param.missing");
        
        /**
         * compiler.err.incomparable.types=\
         *    incomparable types: {0} and {1}
         */
        public static Error IncomparableTypes(Type arg0, Type arg1) {
            return new Error("compiler", "incomparable.types", arg0, arg1);
        }
        
        /**
         * compiler.err.incompatible.thrown.types.in.mref=\
         *    incompatible thrown types {0} in functional expression
         */
        public static Error IncompatibleThrownTypesInMref(List<? extends Type> arg0) {
            return new Error("compiler", "incompatible.thrown.types.in.mref", arg0);
        }
        
        /**
         * compiler.err.incorrect.constructor.receiver.name=\
         *    the receiver name does not match the enclosing outer class type\n\
         *    required: {0}\n\
         *    found:    {1}
         */
        public static Error IncorrectConstructorReceiverName(Type arg0, Type arg1) {
            return new Error("compiler", "incorrect.constructor.receiver.name", arg0, arg1);
        }
        
        /**
         * compiler.err.incorrect.constructor.receiver.type=\
         *    the receiver type does not match the enclosing outer class type\n\
         *    required: {0}\n\
         *    found:    {1}
         */
        public static Error IncorrectConstructorReceiverType(Type arg0, Type arg1) {
            return new Error("compiler", "incorrect.constructor.receiver.type", arg0, arg1);
        }
        
        /**
         * compiler.err.incorrect.receiver.name=\
         *    the receiver name does not match the enclosing class type\n\
         *    required: {0}\n\
         *    found:    {1}
         */
        public static Error IncorrectReceiverName(Type arg0, Type arg1) {
            return new Error("compiler", "incorrect.receiver.name", arg0, arg1);
        }
        
        /**
         * compiler.err.incorrect.receiver.type=\
         *    the receiver type does not match the enclosing class type\n\
         *    required: {0}\n\
         *    found:    {1}
         */
        public static Error IncorrectReceiverType(Type arg0, Type arg1) {
            return new Error("compiler", "incorrect.receiver.type", arg0, arg1);
        }
        
        /**
         * compiler.err.initializer.must.be.able.to.complete.normally=\
         *    initializer must be able to complete normally
         */
        public static final Error InitializerMustBeAbleToCompleteNormally = new Error("compiler", "initializer.must.be.able.to.complete.normally");
        
        /**
         * compiler.err.initializer.not.allowed=\
         *    initializers not allowed in interfaces
         */
        public static final Error InitializerNotAllowed = new Error("compiler", "initializer.not.allowed");
        
        /**
         * compiler.err.instance.initializer.not.allowed.in.records=\
         *    instance initializers not allowed in records
         */
        public static final Error InstanceInitializerNotAllowedInRecords = new Error("compiler", "instance.initializer.not.allowed.in.records");
        
        /**
         * compiler.err.instanceof.pattern.no.subtype=\
         *    expression type {0} is a subtype of pattern type {1}
         */
        public static Error InstanceofPatternNoSubtype(Type arg0, Type arg1) {
            return new Error("compiler", "instanceof.pattern.no.subtype", arg0, arg1);
        }
        
        /**
         * compiler.err.instanceof.reifiable.not.safe=\
         *    {0} cannot be safely cast to {1}
         */
        public static Error InstanceofReifiableNotSafe(Type arg0, Type arg1) {
            return new Error("compiler", "instanceof.reifiable.not.safe", arg0, arg1);
        }
        
        /**
         * compiler.err.int.number.too.large=\
         *    integer number too large
         */
        public static Error IntNumberTooLarge(String arg0) {
            return new Error("compiler", "int.number.too.large", arg0);
        }
        
        /**
         * compiler.err.intf.annotation.cant.have.type.params=\
         *    annotation type {0} cannot be generic
         */
        public static Error IntfAnnotationCantHaveTypeParams(Symbol arg0) {
            return new Error("compiler", "intf.annotation.cant.have.type.params", arg0);
        }
        
        /**
         * compiler.err.intf.annotation.member.clash=\
         *    annotation type {1} declares an element with the same name as method {0}
         */
        public static Error IntfAnnotationMemberClash(Symbol arg0, Type arg1) {
            return new Error("compiler", "intf.annotation.member.clash", arg0, arg1);
        }
        
        /**
         * compiler.err.intf.annotation.members.cant.have.params=\
         *    elements in annotation type declarations cannot declare formal parameters
         */
        public static final Error IntfAnnotationMembersCantHaveParams = new Error("compiler", "intf.annotation.members.cant.have.params");
        
        /**
         * compiler.err.intf.annotation.members.cant.have.type.params=\
         *    elements in annotation type declarations cannot be generic methods
         */
        public static final Error IntfAnnotationMembersCantHaveTypeParams = new Error("compiler", "intf.annotation.members.cant.have.type.params");
        
        /**
         * compiler.err.intf.expected.here=\
         *    interface expected here
         */
        public static final Error IntfExpectedHere = new Error("compiler", "intf.expected.here");
        
        /**
         * compiler.err.intf.meth.cant.have.body=\
         *    interface abstract methods cannot have body
         */
        public static final Error IntfMethCantHaveBody = new Error("compiler", "intf.meth.cant.have.body");
        
        /**
         * compiler.err.intf.not.allowed.here=\
         *    interface not allowed here
         */
        public static final Error IntfNotAllowedHere = new Error("compiler", "intf.not.allowed.here");
        
        /**
         * compiler.err.invalid.A.key=\
         *    key in annotation processor option ''{0}'' is not a dot-separated sequence of identifiers
         */
        public static Error InvalidAKey(String arg0) {
            return new Error("compiler", "invalid.A.key", arg0);
        }
        
        /**
         * compiler.err.invalid.accessor.method.in.record=\
         *    invalid accessor method in record {0}\n\
         *    ({1})
         */
        public static Error InvalidAccessorMethodInRecord(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "invalid.accessor.method.in.record", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.annotation.member.type=\
         *    invalid type for annotation type element
         */
        public static final Error InvalidAnnotationMemberType = new Error("compiler", "invalid.annotation.member.type");
        
        /**
         * compiler.err.invalid.binary.number=\
         *    binary numbers must contain at least one binary digit
         */
        public static final Error InvalidBinaryNumber = new Error("compiler", "invalid.binary.number");
        
        /**
         * compiler.err.invalid.canonical.constructor.in.record=\
         *    invalid {0} constructor in record {1}\n\
         *    ({2})
         */
        public static Error InvalidCanonicalConstructorInRecord(Fragment arg0, Name arg1, Fragment arg2) {
            return new Error("compiler", "invalid.canonical.constructor.in.record", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.invalid.flag=\
         *    invalid flag: {0}
         */
        public static Error InvalidFlag(String arg0) {
            return new Error("compiler", "invalid.flag", arg0);
        }
        
        /**
         * compiler.err.invalid.hex.number=\
         *    hexadecimal numbers must contain at least one hexadecimal digit
         */
        public static final Error InvalidHexNumber = new Error("compiler", "invalid.hex.number");
        
        /**
         * compiler.err.invalid.lambda.parameter.declaration=\
         *    invalid lambda parameter declaration\n\
         *    ({0})
         */
        public static Error InvalidLambdaParameterDeclaration(Fragment arg0) {
            return new Error("compiler", "invalid.lambda.parameter.declaration", arg0);
        }
        
        /**
         * compiler.err.invalid.meth.decl.ret.type.req=\
         *    invalid method declaration; return type required
         */
        public static final Error InvalidMethDeclRetTypeReq = new Error("compiler", "invalid.meth.decl.ret.type.req");
        
        /**
         * compiler.err.invalid.module.directive=\
         *  module directive keyword or ''}'' expected
         */
        public static final Error InvalidModuleDirective = new Error("compiler", "invalid.module.directive");
        
        /**
         * compiler.err.invalid.module.specifier=\
         *    module specifier not allowed: {0}
         */
        public static Error InvalidModuleSpecifier(String arg0) {
            return new Error("compiler", "invalid.module.specifier", arg0);
        }
        
        /**
         * compiler.err.invalid.mref=\
         *    invalid {0} reference\n\
         *    {1}
         */
        public static Error InvalidMref(KindName arg0, JCDiagnostic arg1) {
            return new Error("compiler", "invalid.mref", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.mref=\
         *    invalid {0} reference\n\
         *    {1}
         */
        public static Error InvalidMref(KindName arg0, Fragment arg1) {
            return new Error("compiler", "invalid.mref", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.path=\
         *    Invalid filename: {0}
         */
        public static Error InvalidPath(String arg0) {
            return new Error("compiler", "invalid.path", arg0);
        }
        
        /**
         * compiler.err.invalid.permits.clause=\
         *    invalid permits clause\n\
         *    ({0})
         */
        public static Error InvalidPermitsClause(Fragment arg0) {
            return new Error("compiler", "invalid.permits.clause", arg0);
        }
        
        /**
         * compiler.err.invalid.profile=\
         *    invalid profile: {0}
         */
        public static Error InvalidProfile(String arg0) {
            return new Error("compiler", "invalid.profile", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation=\
         *    duplicate annotation: {0} is annotated with an invalid @Repeatable annotation
         */
        public static Error InvalidRepeatableAnnotation(Symbol arg0) {
            return new Error("compiler", "invalid.repeatable.annotation", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.elem.nondefault=\
         *    containing annotation type ({0}) does not have a default value for element {1}
         */
        public static Error InvalidRepeatableAnnotationElemNondefault(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.elem.nondefault", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.elem.nondefault=\
         *    containing annotation type ({0}) does not have a default value for element {1}
         */
        public static Error InvalidRepeatableAnnotationElemNondefault(Type arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.elem.nondefault", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.incompatible.target=\
         *    containing annotation type ({0}) is applicable to more targets than repeatable annotation type ({1})
         */
        public static Error InvalidRepeatableAnnotationIncompatibleTarget(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.incompatible.target", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.invalid.value=\
         *    {0} is not a valid @Repeatable: invalid value element
         */
        public static Error InvalidRepeatableAnnotationInvalidValue(Type arg0) {
            return new Error("compiler", "invalid.repeatable.annotation.invalid.value", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.multiple.values=\
         *    {0} is not a valid @Repeatable, {1} element methods named ''value'' declared
         */
        public static Error InvalidRepeatableAnnotationMultipleValues(Type arg0, int arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.multiple.values", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.no.value=\
         *    {0} is not a valid @Repeatable, no value element method declared
         */
        public static Error InvalidRepeatableAnnotationNoValue(Symbol arg0) {
            return new Error("compiler", "invalid.repeatable.annotation.no.value", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.no.value=\
         *    {0} is not a valid @Repeatable, no value element method declared
         */
        public static Error InvalidRepeatableAnnotationNoValue(Type arg0) {
            return new Error("compiler", "invalid.repeatable.annotation.no.value", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.not.applicable=\
         *    container {0} is not applicable to element {1}
         */
        public static Error InvalidRepeatableAnnotationNotApplicable(Type arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.not.applicable", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.not.applicable.in.context=\
         *    container {0} is not applicable in this type context
         */
        public static Error InvalidRepeatableAnnotationNotApplicableInContext(Type arg0) {
            return new Error("compiler", "invalid.repeatable.annotation.not.applicable.in.context", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.not.documented=\
         *    repeatable annotation type ({1}) is @Documented while containing annotation type ({0}) is not
         */
        public static Error InvalidRepeatableAnnotationNotDocumented(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.not.documented", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.not.inherited=\
         *    repeatable annotation type ({1}) is @Inherited while containing annotation type ({0}) is not
         */
        public static Error InvalidRepeatableAnnotationNotInherited(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "invalid.repeatable.annotation.not.inherited", arg0, arg1);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.repeated.and.container.present=\
         *    container {0} must not be present at the same time as the element it contains
         */
        public static Error InvalidRepeatableAnnotationRepeatedAndContainerPresent(Symbol arg0) {
            return new Error("compiler", "invalid.repeatable.annotation.repeated.and.container.present", arg0);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.retention=\
         *    retention of containing annotation type ({0}) is shorter than the retention of repeatable annotation type ({2})
         */
        public static Error InvalidRepeatableAnnotationRetention(Symbol arg0, String arg1, Symbol arg2, String arg3) {
            return new Error("compiler", "invalid.repeatable.annotation.retention", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.value.return=\
         *    containing annotation type ({0}) must declare an element named ''value'' of type {2}
         */
        public static Error InvalidRepeatableAnnotationValueReturn(Symbol arg0, Type arg1, Type arg2) {
            return new Error("compiler", "invalid.repeatable.annotation.value.return", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.invalid.repeatable.annotation.value.return=\
         *    containing annotation type ({0}) must declare an element named ''value'' of type {2}
         */
        public static Error InvalidRepeatableAnnotationValueReturn(Type arg0, Type arg1, Type arg2) {
            return new Error("compiler", "invalid.repeatable.annotation.value.return", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.invalid.source=\
         *    invalid source release: {0}
         */
        public static Error InvalidSource(String arg0) {
            return new Error("compiler", "invalid.source", arg0);
        }
        
        /**
         * compiler.err.invalid.supertype.record=\
         *    classes cannot directly extend {0}
         */
        public static Error InvalidSupertypeRecord(Symbol arg0) {
            return new Error("compiler", "invalid.supertype.record", arg0);
        }
        
        /**
         * compiler.err.invalid.target=\
         *    invalid target release: {0}
         */
        public static Error InvalidTarget(String arg0) {
            return new Error("compiler", "invalid.target", arg0);
        }
        
        /**
         * compiler.err.invalid.yield=\
         *    invalid use of a restricted identifier ''yield''\n\
         *    (to invoke a method called yield, qualify the yield with a receiver or type name)
         */
        public static final Error InvalidYield = new Error("compiler", "invalid.yield");
        
        /**
         * compiler.err.io.exception=\
         *    error reading source file: {0}
         */
        public static final Error IoException = new Error("compiler", "io.exception");
        
        /**
         * compiler.err.is.preview=\
         *    {0} is a preview API and is disabled by default.\n\
         *    (use --enable-preview to enable preview APIs)
         */
        public static Error IsPreview(Symbol arg0) {
            return new Error("compiler", "is.preview", arg0);
        }
        
        /**
         * compiler.err.label.already.in.use=\
         *    label {0} already in use
         */
        public static Error LabelAlreadyInUse(Name arg0) {
            return new Error("compiler", "label.already.in.use", arg0);
        }
        
        /**
         * compiler.err.lambda.body.neither.value.nor.void.compatible=\
         *    lambda body is neither value nor void compatible
         */
        public static final Error LambdaBodyNeitherValueNorVoidCompatible = new Error("compiler", "lambda.body.neither.value.nor.void.compatible");
        
        /**
         * compiler.err.limit.code=\
         *    code too large
         */
        public static final Error LimitCode = new Error("compiler", "limit.code");
        
        /**
         * compiler.err.limit.code.too.large.for.try.stmt=\
         *    code too large for try statement
         */
        public static final Error LimitCodeTooLargeForTryStmt = new Error("compiler", "limit.code.too.large.for.try.stmt");
        
        /**
         * compiler.err.limit.dimensions=\
         *    array type has too many dimensions
         */
        public static final Error LimitDimensions = new Error("compiler", "limit.dimensions");
        
        /**
         * compiler.err.limit.locals=\
         *    too many local variables
         */
        public static final Error LimitLocals = new Error("compiler", "limit.locals");
        
        /**
         * compiler.err.limit.parameters=\
         *    too many parameters
         */
        public static final Error LimitParameters = new Error("compiler", "limit.parameters");
        
        /**
         * compiler.err.limit.pool=\
         *    too many constants
         */
        public static final Error LimitPool = new Error("compiler", "limit.pool");
        
        /**
         * compiler.err.limit.pool.in.class=\
         *    too many constants in class {0}
         */
        public static final Error LimitPoolInClass = new Error("compiler", "limit.pool.in.class");
        
        /**
         * compiler.err.limit.stack=\
         *    code requires too much stack
         */
        public static final Error LimitStack = new Error("compiler", "limit.stack");
        
        /**
         * compiler.err.limit.string=\
         *    constant string too long
         */
        public static final Error LimitString = new Error("compiler", "limit.string");
        
        /**
         * compiler.err.limit.string.overflow=\
         *    UTF8 representation for string \"{0}...\" is too long for the constant pool
         */
        public static Error LimitStringOverflow(String arg0) {
            return new Error("compiler", "limit.string.overflow", arg0);
        }
        
        /**
         * compiler.err.local.classes.cant.extend.sealed=\
         *    {0} classes must not extend sealed classes\
         */
        public static Error LocalClassesCantExtendSealed(Fragment arg0) {
            return new Error("compiler", "local.classes.cant.extend.sealed", arg0);
        }
        
        /**
         * compiler.err.local.enum=\
         *    enum types must not be local
         */
        public static final Error LocalEnum = new Error("compiler", "local.enum");
        
        /**
         * compiler.err.local.var.accessed.from.icls.needs.final=\
         *    local variable {0} is accessed from within inner class; needs to be declared final
         */
        public static Error LocalVarAccessedFromIclsNeedsFinal(Symbol arg0) {
            return new Error("compiler", "local.var.accessed.from.icls.needs.final", arg0);
        }
        
        /**
         * compiler.err.locn.bad.module-info=\
         *    problem reading module-info.class in {0}
         */
        public static Error LocnBadModuleInfo(Path arg0) {
            return new Error("compiler", "locn.bad.module-info", arg0);
        }
        
        /**
         * compiler.err.locn.cant.get.module.name.for.jar=\
         *    cannot determine module name for {0}
         */
        public static Error LocnCantGetModuleNameForJar(Path arg0) {
            return new Error("compiler", "locn.cant.get.module.name.for.jar", arg0);
        }
        
        /**
         * compiler.err.locn.cant.read.directory=\
         *    cannot read directory {0}
         */
        public static Error LocnCantReadDirectory(Path arg0) {
            return new Error("compiler", "locn.cant.read.directory", arg0);
        }
        
        /**
         * compiler.err.locn.cant.read.file=\
         *    cannot read file {0}
         */
        public static Error LocnCantReadFile(Path arg0) {
            return new Error("compiler", "locn.cant.read.file", arg0);
        }
        
        /**
         * compiler.err.locn.invalid.arg.for.xpatch=\
         *    invalid argument for --patch-module option: {0}
         */
        public static Error LocnInvalidArgForXpatch(String arg0) {
            return new Error("compiler", "locn.invalid.arg.for.xpatch", arg0);
        }
        
        /**
         * compiler.err.locn.module-info.not.allowed.on.patch.path=\
         *    module-info.class not allowed on patch path: {0}
         */
        public static Error LocnModuleInfoNotAllowedOnPatchPath(JavaFileObject arg0) {
            return new Error("compiler", "locn.module-info.not.allowed.on.patch.path", arg0);
        }
        
        /**
         * compiler.err.malformed.fp.lit=\
         *    malformed floating-point literal
         */
        public static final Error MalformedFpLit = new Error("compiler", "malformed.fp.lit");
        
        /**
         * compiler.err.match.binding.exists=\
         *    illegal attempt to redefine an existing match binding
         */
        public static final Error MatchBindingExists = new Error("compiler", "match.binding.exists");
        
        /**
         * compiler.err.method.does.not.override.superclass=\
         *    method does not override or implement a method from a supertype
         */
        public static final Error MethodDoesNotOverrideSuperclass = new Error("compiler", "method.does.not.override.superclass");
        
        /**
         * compiler.err.missing.meth.body.or.decl.abstract=\
         *    missing method body, or declare abstract
         */
        public static final Error MissingMethBodyOrDeclAbstract = new Error("compiler", "missing.meth.body.or.decl.abstract");
        
        /**
         * compiler.err.missing.ret.stmt=\
         *    missing return statement
         */
        public static final Error MissingRetStmt = new Error("compiler", "missing.ret.stmt");
        
        /**
         * compiler.err.mod.not.allowed.here=\
         *    modifier {0} not allowed here
         */
        public static Error ModNotAllowedHere(Set<? extends Flag> arg0) {
            return new Error("compiler", "mod.not.allowed.here", arg0);
        }
        
        /**
         * compiler.err.modifier.not.allowed.here=\
         *    modifier {0} not allowed here
         */
        public static Error ModifierNotAllowedHere(Name arg0) {
            return new Error("compiler", "modifier.not.allowed.here", arg0);
        }
        
        /**
         * compiler.err.module.decl.sb.in.module-info.java=\
         *    module declarations should be in a file named module-info.java
         */
        public static final Error ModuleDeclSbInModuleInfoJava = new Error("compiler", "module.decl.sb.in.module-info.java");
        
        /**
         * compiler.err.module.name.mismatch=\
         *    module name {0} does not match expected name {1}
         */
        public static Error ModuleNameMismatch(Name arg0, Name arg1) {
            return new Error("compiler", "module.name.mismatch", arg0, arg1);
        }
        
        /**
         * compiler.err.module.non.zero.opens=\
         *    open module {0} has non-zero opens_count
         */
        public static Error ModuleNonZeroOpens(Name arg0) {
            return new Error("compiler", "module.non.zero.opens", arg0);
        }
        
        /**
         * compiler.err.module.not.found=\
         *    module not found: {0}
         */
        public static Error ModuleNotFound(Symbol arg0) {
            return new Error("compiler", "module.not.found", arg0);
        }
        
        /**
         * compiler.err.module.not.found.in.module.source.path=\
         *    module {0} not found in module source path
         */
        public static Error ModuleNotFoundInModuleSourcePath(String arg0) {
            return new Error("compiler", "module.not.found.in.module.source.path", arg0);
        }
        
        /**
         * compiler.err.module.not.found.on.module.source.path=\
         *    module not found on module source path
         */
        public static final Error ModuleNotFoundOnModuleSourcePath = new Error("compiler", "module.not.found.on.module.source.path");
        
        /**
         * compiler.err.modulesourcepath.must.be.specified.with.dash.m.option=\
         *    module source path must be specified if -m option is used
         */
        public static final Error ModulesourcepathMustBeSpecifiedWithDashMOption = new Error("compiler", "modulesourcepath.must.be.specified.with.dash.m.option");
        
        /**
         * compiler.err.multi-module.outdir.cannot.be.exploded.module=\
         *    in multi-module mode, the output directory cannot be an exploded module: {0}
         */
        public static Error MultiModuleOutdirCannotBeExplodedModule(Path arg0) {
            return new Error("compiler", "multi-module.outdir.cannot.be.exploded.module", arg0);
        }
        
        /**
         * compiler.err.multicatch.parameter.may.not.be.assigned=\
         *    multi-catch parameter {0} may not be assigned
         */
        public static Error MulticatchParameterMayNotBeAssigned(Symbol arg0) {
            return new Error("compiler", "multicatch.parameter.may.not.be.assigned", arg0);
        }
        
        /**
         * compiler.err.multicatch.types.must.be.disjoint=\
         *    Alternatives in a multi-catch statement cannot be related by subclassing\n\
         *    Alternative {0} is a subclass of alternative {1}
         */
        public static Error MulticatchTypesMustBeDisjoint(Type arg0, Type arg1) {
            return new Error("compiler", "multicatch.types.must.be.disjoint", arg0, arg1);
        }
        
        /**
         * compiler.err.multiple.values.for.module.source.path=\
         *    --module-source-path specified more than once with a pattern argument
         */
        public static final Error MultipleValuesForModuleSourcePath = new Error("compiler", "multiple.values.for.module.source.path");
        
        /**
         * compiler.err.name.clash.same.erasure=\
         *    name clash: {0} and {1} have the same erasure
         */
        public static Error NameClashSameErasure(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "name.clash.same.erasure", arg0, arg1);
        }
        
        /**
         * compiler.err.name.clash.same.erasure.no.hide=\
         *    name clash: {0} in {1} and {2} in {3} have the same erasure, yet neither hides the other
         */
        public static Error NameClashSameErasureNoHide(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Error("compiler", "name.clash.same.erasure.no.hide", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.name.clash.same.erasure.no.override=\
         *    name clash: {0}({1}) in {2} and {3}({4}) in {5} have the same erasure, yet neither overrides the other
         */
        public static Error NameClashSameErasureNoOverride(Name arg0, List<? extends Type> arg1, Symbol arg2, Name arg3, List<? extends Type> arg4, Symbol arg5) {
            return new Error("compiler", "name.clash.same.erasure.no.override", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.err.name.clash.same.erasure.no.override.1=\
         *    name clash: {0} {1} has two methods with the same erasure, yet neither overrides the other\n\
         *    first method:  {2}({3}) in {4}\n\
         *    second method: {5}({6}) in {7}
         */
        public static Error NameClashSameErasureNoOverride1(String arg0, Name arg1, Name arg2, List<? extends Type> arg3, Symbol arg4, Name arg5, List<? extends Type> arg6, Symbol arg7) {
            return new Error("compiler", "name.clash.same.erasure.no.override.1", arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        }
        
        /**
         * compiler.err.name.reserved.for.internal.use=\
         *    {0} is reserved for internal use
         */
        public static final Error NameReservedForInternalUse = new Error("compiler", "name.reserved.for.internal.use");
        
        /**
         * compiler.err.native.meth.cant.have.body=\
         *    native methods cannot have a body
         */
        public static final Error NativeMethCantHaveBody = new Error("compiler", "native.meth.cant.have.body");
        
        /**
         * compiler.err.new.not.allowed.in.annotation=\
         *    ''new'' not allowed in an annotation
         */
        public static final Error NewNotAllowedInAnnotation = new Error("compiler", "new.not.allowed.in.annotation");
        
        /**
         * compiler.err.no.annotation.member=\
         *    no annotation member {0} in {1}
         */
        public static Error NoAnnotationMember(Name arg0, Type arg1) {
            return new Error("compiler", "no.annotation.member", arg0, arg1);
        }
        
        /**
         * compiler.err.no.annotations.on.dot.class=\
         *    no annotations are allowed in the type of a class literal
         */
        public static final Error NoAnnotationsOnDotClass = new Error("compiler", "no.annotations.on.dot.class");
        
        /**
         * compiler.err.no.encl.instance.of.type.in.scope=\
         *    no enclosing instance of type {0} is in scope
         */
        public static Error NoEnclInstanceOfTypeInScope(Symbol arg0) {
            return new Error("compiler", "no.encl.instance.of.type.in.scope", arg0);
        }
        
        /**
         * compiler.err.no.intf.expected.here=\
         *    no interface expected here
         */
        public static final Error NoIntfExpectedHere = new Error("compiler", "no.intf.expected.here");
        
        /**
         * compiler.err.no.match.entry=\
         *    {0} has no match in entry in {1}; required {2}
         */
        public static final Error NoMatchEntry = new Error("compiler", "no.match.entry");
        
        /**
         * compiler.err.no.opens.unless.strong=\
         *    ''opens'' only allowed in strong modules
         */
        public static final Error NoOpensUnlessStrong = new Error("compiler", "no.opens.unless.strong");
        
        /**
         * compiler.err.no.output.dir=\
         *    no class output directory specified
         */
        public static final Error NoOutputDir = new Error("compiler", "no.output.dir");
        
        /**
         * compiler.err.no.pkg.in.module-info.java=\
         *    package declarations not allowed in file module-info.java
         */
        public static final Error NoPkgInModuleInfoJava = new Error("compiler", "no.pkg.in.module-info.java");
        
        /**
         * compiler.err.no.source.files=\
         *    no source files
         */
        public static final Error NoSourceFiles = new Error("compiler", "no.source.files");
        
        /**
         * compiler.err.no.source.files.classes=\
         *    no source files or class names
         */
        public static final Error NoSourceFilesClasses = new Error("compiler", "no.source.files.classes");
        
        /**
         * compiler.err.no.superclass=\
         *    {0} has no superclass.
         */
        public static Error NoSuperclass(Type arg0) {
            return new Error("compiler", "no.superclass", arg0);
        }
        
        /**
         * compiler.err.no.switch.expression =\
         *    yield outside of switch expression
         */
        public static final Error NoSwitchExpression = new Error("compiler", "no.switch.expression");
        
        /**
         * compiler.err.no.switch.expression.qualify=\
         *    yield outside of switch expression\n\
         *    (to invoke a method called yield, qualify the yield with a receiver or type name)
         */
        public static final Error NoSwitchExpressionQualify = new Error("compiler", "no.switch.expression.qualify");
        
        /**
         * compiler.err.no.value.for.option=\
         *    no value for {0} option
         */
        public static Error NoValueForOption(String arg0) {
            return new Error("compiler", "no.value.for.option", arg0);
        }
        
        /**
         * compiler.err.no.zipfs.for.archive=\
         *    No file system provider is available to handle this file: {0}
         */
        public static Error NoZipfsForArchive(Path arg0) {
            return new Error("compiler", "no.zipfs.for.archive", arg0);
        }
        
        /**
         * compiler.err.non-static.cant.be.ref=\
         *    non-static {0} {1} cannot be referenced from a static context
         */
        public static Error NonStaticCantBeRef(Kind arg0, Symbol arg1) {
            return new Error("compiler", "non-static.cant.be.ref", arg0, arg1);
        }
        
        /**
         * compiler.err.non.sealed.or.sealed.expected=\
         *    sealed or non-sealed modifiers expected
         */
        public static final Error NonSealedOrSealedExpected = new Error("compiler", "non.sealed.or.sealed.expected");
        
        /**
         * compiler.err.non.sealed.sealed.or.final.expected=\
         *    sealed, non-sealed or final modifiers expected
         */
        public static final Error NonSealedSealedOrFinalExpected = new Error("compiler", "non.sealed.sealed.or.final.expected");
        
        /**
         * compiler.err.non.sealed.with.no.sealed.supertype=\
         *    non-sealed modifier not allowed here\n\
         *    (class {0} does not have any sealed supertypes)
         */
        public static Error NonSealedWithNoSealedSupertype(Symbol arg0) {
            return new Error("compiler", "non.sealed.with.no.sealed.supertype", arg0);
        }
        
        /**
         * compiler.err.not.annotation.type=\
         *    {0} is not an annotation type
         */
        public static Error NotAnnotationType(Type arg0) {
            return new Error("compiler", "not.annotation.type", arg0);
        }
        
        /**
         * compiler.err.not.def.access.class.intf.cant.access=\
         *    {1}.{0} is defined in an inaccessible class or interface
         */
        public static Error NotDefAccessClassIntfCantAccess(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "not.def.access.class.intf.cant.access", arg0, arg1);
        }
        
        /**
         * compiler.err.not.def.access.class.intf.cant.access.reason=\
         *    {1}.{0} in package {2} is not accessible\n\
         *    ({3})
         */
        public static Error NotDefAccessClassIntfCantAccessReason(Symbol arg0, Symbol arg1, Symbol arg2, JCDiagnostic arg3) {
            return new Error("compiler", "not.def.access.class.intf.cant.access.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.not.def.access.class.intf.cant.access.reason=\
         *    {1}.{0} in package {2} is not accessible\n\
         *    ({3})
         */
        public static Error NotDefAccessClassIntfCantAccessReason(Symbol arg0, Symbol arg1, Symbol arg2, Fragment arg3) {
            return new Error("compiler", "not.def.access.class.intf.cant.access.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.not.def.access.package.cant.access=\
         *    {0} is not visible\n\
         *    ({2})
         */
        public static Error NotDefAccessPackageCantAccess(Symbol arg0, Symbol arg1, JCDiagnostic arg2) {
            return new Error("compiler", "not.def.access.package.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.not.def.access.package.cant.access=\
         *    {0} is not visible\n\
         *    ({2})
         */
        public static Error NotDefAccessPackageCantAccess(Symbol arg0, Symbol arg1, Fragment arg2) {
            return new Error("compiler", "not.def.access.package.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.not.def.public=\
         *    {0} is not public in {1}
         */
        public static Error NotDefPublic(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "not.def.public", arg0, arg1);
        }
        
        /**
         * compiler.err.not.def.public.cant.access=\
         *    {0} is not public in {1}; cannot be accessed from outside package
         */
        public static Error NotDefPublicCantAccess(Symbol arg0, Symbol arg1) {
            return new Error("compiler", "not.def.public.cant.access", arg0, arg1);
        }
        
        /**
         * compiler.err.not.encl.class=\
         *    not an enclosing class: {0}
         */
        public static Error NotEnclClass(Symbol arg0) {
            return new Error("compiler", "not.encl.class", arg0);
        }
        
        /**
         * compiler.err.not.exhaustive=\
         *    the switch expression does not cover all possible input values
         */
        public static final Error NotExhaustive = new Error("compiler", "not.exhaustive");
        
        /**
         * compiler.err.not.exhaustive.statement=\
         *    the switch statement does not cover all possible input values
         */
        public static final Error NotExhaustiveStatement = new Error("compiler", "not.exhaustive.statement");
        
        /**
         * compiler.err.not.in.module.on.module.source.path=\
         *    not in a module on the module source path
         */
        public static final Error NotInModuleOnModuleSourcePath = new Error("compiler", "not.in.module.on.module.source.path");
        
        /**
         * compiler.err.not.in.profile=\
         *    {0} is not available in profile ''{1}''
         */
        public static Error NotInProfile(Symbol arg0, Object arg1) {
            return new Error("compiler", "not.in.profile", arg0, arg1);
        }
        
        /**
         * compiler.err.not.loop.label=\
         *    not a loop label: {0}
         */
        public static Error NotLoopLabel(Name arg0) {
            return new Error("compiler", "not.loop.label", arg0);
        }
        
        /**
         * compiler.err.not.stmt=\
         *    not a statement
         */
        public static final Error NotStmt = new Error("compiler", "not.stmt");
        
        /**
         * compiler.err.not.within.bounds=\
         *    type argument {0} is not within bounds of type-variable {1}
         */
        public static Error NotWithinBounds(Type arg0, Type arg1) {
            return new Error("compiler", "not.within.bounds", arg0, arg1);
        }
        
        /**
         * compiler.err.not.within.bounds=\
         *    type argument {0} is not within bounds of type-variable {1}
         */
        public static Error NotWithinBounds(Type arg0, Symbol arg1) {
            return new Error("compiler", "not.within.bounds", arg0, arg1);
        }
        
        /**
         * compiler.err.operator.cant.be.applied=\
         *    bad operand type {1} for unary operator ''{0}''
         */
        public static Error OperatorCantBeApplied(Name arg0, Type arg1) {
            return new Error("compiler", "operator.cant.be.applied", arg0, arg1);
        }
        
        /**
         * compiler.err.operator.cant.be.applied.1=\
         *    bad operand types for binary operator ''{0}''\n\
         *    first type:  {1}\n\
         *    second type: {2}
         */
        public static Error OperatorCantBeApplied1(Name arg0, Type arg1, Type arg2) {
            return new Error("compiler", "operator.cant.be.applied.1", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.option.not.allowed.with.target=\
         *    option {0} not allowed with target {1}
         */
        public static Error OptionNotAllowedWithTarget(Option arg0, Target arg1) {
            return new Error("compiler", "option.not.allowed.with.target", arg0, arg1);
        }
        
        /**
         * compiler.err.option.removed.source=\
         *    Source option {0} is no longer supported. Use {1} or later.
         */
        public static Error OptionRemovedSource(String arg0, String arg1) {
            return new Error("compiler", "option.removed.source", arg0, arg1);
        }
        
        /**
         * compiler.err.option.removed.target=\
         *    Target option {0} is no longer supported. Use {1} or later.
         */
        public static Error OptionRemovedTarget(Target arg0, Target arg1) {
            return new Error("compiler", "option.removed.target", arg0, arg1);
        }
        
        /**
         * compiler.err.option.too.many=\
         *    option {0} can only be specified once
         */
        public static Error OptionTooMany(String arg0) {
            return new Error("compiler", "option.too.many", arg0);
        }
        
        /**
         * compiler.err.orphaned=\
         *    orphaned {0}
         */
        public static Error Orphaned(TokenKind arg0) {
            return new Error("compiler", "orphaned", arg0);
        }
        
        /**
         * compiler.err.output.dir.must.be.specified.with.dash.m.option=\
         *    class output directory must be specified if -m option is used
         */
        public static final Error OutputDirMustBeSpecifiedWithDashMOption = new Error("compiler", "output.dir.must.be.specified.with.dash.m.option");
        
        /**
         * compiler.err.override.incompatible.ret=\
         *    {0}\n\
         *    return type {1} is not compatible with {2}
         */
        public static Error OverrideIncompatibleRet(JCDiagnostic arg0, Type arg1, Type arg2) {
            return new Error("compiler", "override.incompatible.ret", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.override.incompatible.ret=\
         *    {0}\n\
         *    return type {1} is not compatible with {2}
         */
        public static Error OverrideIncompatibleRet(Fragment arg0, Type arg1, Type arg2) {
            return new Error("compiler", "override.incompatible.ret", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.override.meth=\
         *    {0}\n\
         *    overridden method is {1}
         */
        public static Error OverrideMeth(JCDiagnostic arg0, Set<? extends Flag> arg1) {
            return new Error("compiler", "override.meth", arg0, arg1);
        }
        
        /**
         * compiler.err.override.meth=\
         *    {0}\n\
         *    overridden method is {1}
         */
        public static Error OverrideMeth(Fragment arg0, Set<? extends Flag> arg1) {
            return new Error("compiler", "override.meth", arg0, arg1);
        }
        
        /**
         * compiler.err.override.meth.doesnt.throw=\
         *    {0}\n\
         *    overridden method does not throw {1}
         */
        public static Error OverrideMethDoesntThrow(JCDiagnostic arg0, Type arg1) {
            return new Error("compiler", "override.meth.doesnt.throw", arg0, arg1);
        }
        
        /**
         * compiler.err.override.meth.doesnt.throw=\
         *    {0}\n\
         *    overridden method does not throw {1}
         */
        public static Error OverrideMethDoesntThrow(Fragment arg0, Type arg1) {
            return new Error("compiler", "override.meth.doesnt.throw", arg0, arg1);
        }
        
        /**
         * compiler.err.override.static=\
         *    {0}\n\
         *    overriding method is static
         */
        public static Error OverrideStatic(JCDiagnostic arg0) {
            return new Error("compiler", "override.static", arg0);
        }
        
        /**
         * compiler.err.override.static=\
         *    {0}\n\
         *    overriding method is static
         */
        public static Error OverrideStatic(Fragment arg0) {
            return new Error("compiler", "override.static", arg0);
        }
        
        /**
         * compiler.err.override.weaker.access=\
         *    {0}\n\
         *    attempting to assign weaker access privileges; was {1}
         */
        public static Error OverrideWeakerAccess(JCDiagnostic arg0, Set<? extends Flag> arg1) {
            return new Error("compiler", "override.weaker.access", arg0, arg1);
        }
        
        /**
         * compiler.err.override.weaker.access=\
         *    {0}\n\
         *    attempting to assign weaker access privileges; was {1}
         */
        public static Error OverrideWeakerAccess(JCDiagnostic arg0, String arg1) {
            return new Error("compiler", "override.weaker.access", arg0, arg1);
        }
        
        /**
         * compiler.err.override.weaker.access=\
         *    {0}\n\
         *    attempting to assign weaker access privileges; was {1}
         */
        public static Error OverrideWeakerAccess(Fragment arg0, Set<? extends Flag> arg1) {
            return new Error("compiler", "override.weaker.access", arg0, arg1);
        }
        
        /**
         * compiler.err.override.weaker.access=\
         *    {0}\n\
         *    attempting to assign weaker access privileges; was {1}
         */
        public static Error OverrideWeakerAccess(Fragment arg0, String arg1) {
            return new Error("compiler", "override.weaker.access", arg0, arg1);
        }
        
        /**
         * compiler.err.package.clash.from.requires=\
         *    module {0} reads package {1} from both {2} and {3}
         */
        public static Error PackageClashFromRequires(Symbol arg0, Name arg1, Symbol arg2, Symbol arg3) {
            return new Error("compiler", "package.clash.from.requires", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.err.package.clash.from.requires.in.unnamed=\
         *    the unnamed module reads package {0} from both {1} and {2}
         */
        public static Error PackageClashFromRequiresInUnnamed(Name arg0, Symbol arg1, Symbol arg2) {
            return new Error("compiler", "package.clash.from.requires.in.unnamed", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.package.empty.or.not.found=\
         *    package is empty or does not exist: {0}
         */
        public static Error PackageEmptyOrNotFound(Symbol arg0) {
            return new Error("compiler", "package.empty.or.not.found", arg0);
        }
        
        /**
         * compiler.err.package.in.other.module=\
         *    package exists in another module: {0}
         */
        public static Error PackageInOtherModule(Symbol arg0) {
            return new Error("compiler", "package.in.other.module", arg0);
        }
        
        /**
         * compiler.err.package.not.visible=\
         *    package {0} is not visible\n\
         *    ({1})
         */
        public static Error PackageNotVisible(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "package.not.visible", arg0, arg1);
        }
        
        /**
         * compiler.err.package.not.visible=\
         *    package {0} is not visible\n\
         *    ({1})
         */
        public static Error PackageNotVisible(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "package.not.visible", arg0, arg1);
        }
        
        /**
         * compiler.err.pattern.dominated=\
         *    this case label is dominated by a preceding case label
         */
        public static final Error PatternDominated = new Error("compiler", "pattern.dominated");
        
        /**
         * compiler.err.pattern.expected=\
         *    type pattern expected
         */
        public static final Error PatternExpected = new Error("compiler", "pattern.expected");
        
        /**
         * compiler.err.pkg.annotations.sb.in.package-info.java=\
         *    package annotations should be in file package-info.java
         */
        public static final Error PkgAnnotationsSbInPackageInfoJava = new Error("compiler", "pkg.annotations.sb.in.package-info.java");
        
        /**
         * compiler.err.pkg.clashes.with.class.of.same.name=\
         *    package {0} clashes with class of same name
         */
        public static Error PkgClashesWithClassOfSameName(Symbol arg0) {
            return new Error("compiler", "pkg.clashes.with.class.of.same.name", arg0);
        }
        
        /**
         * compiler.err.plugin.not.found=\
         *    plug-in not found: {0}
         */
        public static Error PluginNotFound(String arg0) {
            return new Error("compiler", "plugin.not.found", arg0);
        }
        
        /**
         * compiler.err.premature.eof=\
         *    reached end of file while parsing
         */
        public static final Error PrematureEof = new Error("compiler", "premature.eof");
        
        /**
         * compiler.err.preview.feature.disabled=\
         *   {0} is a preview feature and is disabled by default.\n\
         *   (use --enable-preview to enable {0})
         */
        public static Error PreviewFeatureDisabled(JCDiagnostic arg0) {
            return new Error("compiler", "preview.feature.disabled", arg0);
        }
        
        /**
         * compiler.err.preview.feature.disabled=\
         *   {0} is a preview feature and is disabled by default.\n\
         *   (use --enable-preview to enable {0})
         */
        public static Error PreviewFeatureDisabled(Fragment arg0) {
            return new Error("compiler", "preview.feature.disabled", arg0);
        }
        
        /**
         * compiler.err.preview.feature.disabled.classfile=\
         *   class file for {0} uses preview features of Java SE {1}.\n\
         *   (use --enable-preview to allow loading of class files which contain preview features)
         */
        public static Error PreviewFeatureDisabledClassfile(JavaFileObject arg0, String arg1) {
            return new Error("compiler", "preview.feature.disabled.classfile", arg0, arg1);
        }
        
        /**
         * compiler.err.preview.feature.disabled.plural=\
         *   {0} are a preview feature and are disabled by default.\n\
         *   (use --enable-preview to enable {0})
         */
        public static Error PreviewFeatureDisabledPlural(JCDiagnostic arg0) {
            return new Error("compiler", "preview.feature.disabled.plural", arg0);
        }
        
        /**
         * compiler.err.preview.feature.disabled.plural=\
         *   {0} are a preview feature and are disabled by default.\n\
         *   (use --enable-preview to enable {0})
         */
        public static Error PreviewFeatureDisabledPlural(Fragment arg0) {
            return new Error("compiler", "preview.feature.disabled.plural", arg0);
        }
        
        /**
         * compiler.err.preview.not.latest=\
         *    invalid source release {0} with --enable-preview\n\
         *    (preview language features are only supported for release {1})
         */
        public static Error PreviewNotLatest(String arg0, Source arg1) {
            return new Error("compiler", "preview.not.latest", arg0, arg1);
        }
        
        /**
         * compiler.err.preview.without.source.or.release=\
         *    --enable-preview must be used with either -source or --release
         */
        public static final Error PreviewWithoutSourceOrRelease = new Error("compiler", "preview.without.source.or.release");
        
        /**
         * compiler.err.prob.found.req=\
         *    incompatible types: {0}
         */
        public static Error ProbFoundReq(JCDiagnostic arg0) {
            return new Error("compiler", "prob.found.req", arg0);
        }
        
        /**
         * compiler.err.prob.found.req=\
         *    incompatible types: {0}
         */
        public static Error ProbFoundReq(Fragment arg0) {
            return new Error("compiler", "prob.found.req", arg0);
        }
        
        /**
         * compiler.err.proc.bad.config.file=\
         *    Bad service configuration file, or exception thrown while constructing Processor object: {0}
         */
        public static Error ProcBadConfigFile(String arg0) {
            return new Error("compiler", "proc.bad.config.file", arg0);
        }
        
        /**
         * compiler.err.proc.cant.access=\
         *    cannot access {0}\n\
         *    {1}\n\
         *    Consult the following stack trace for details.\n\
         *    {2}
         */
        public static Error ProcCantAccess(Symbol arg0, JCDiagnostic arg1, String arg2) {
            return new Error("compiler", "proc.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.proc.cant.access=\
         *    cannot access {0}\n\
         *    {1}\n\
         *    Consult the following stack trace for details.\n\
         *    {2}
         */
        public static Error ProcCantAccess(Symbol arg0, Fragment arg1, String arg2) {
            return new Error("compiler", "proc.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.proc.cant.access.1=\
         *    cannot access {0}\n\
         *    {1}
         */
        public static Error ProcCantAccess1(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "proc.cant.access.1", arg0, arg1);
        }
        
        /**
         * compiler.err.proc.cant.access.1=\
         *    cannot access {0}\n\
         *    {1}
         */
        public static Error ProcCantAccess1(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "proc.cant.access.1", arg0, arg1);
        }
        
        /**
         * compiler.err.proc.cant.create.loader=\
         *    Could not create class loader for annotation processors: {0}
         */
        public static final Error ProcCantCreateLoader = new Error("compiler", "proc.cant.create.loader");
        
        /**
         * compiler.err.proc.cant.find.class=\
         *    Could not find class file for ''{0}''.
         */
        public static Error ProcCantFindClass(String arg0) {
            return new Error("compiler", "proc.cant.find.class", arg0);
        }
        
        /**
         * compiler.err.proc.cant.load.class=\
         *    Could not load processor class file due to ''{0}''.
         */
        public static Error ProcCantLoadClass(String arg0) {
            return new Error("compiler", "proc.cant.load.class", arg0);
        }
        
        /**
         * compiler.err.proc.messager=\
         *    {0}
         */
        public static Error ProcMessager(String arg0) {
            return new Error("compiler", "proc.messager", arg0);
        }
        
        /**
         * compiler.err.proc.no.explicit.annotation.processing.requested=\
         *    Class names, ''{0}'', are only accepted if annotation processing is explicitly requested
         */
        public static Error ProcNoExplicitAnnotationProcessingRequested(Collection<? extends String> arg0) {
            return new Error("compiler", "proc.no.explicit.annotation.processing.requested", arg0);
        }
        
        /**
         * compiler.err.proc.no.service=\
         *    A ServiceLoader was not usable and is required for annotation processing.
         */
        public static final Error ProcNoService = new Error("compiler", "proc.no.service");
        
        /**
         * compiler.err.proc.processor.bad.option.name=\
         *    Bad option name ''{0}'' provided by processor ''{1}''
         */
        public static Error ProcProcessorBadOptionName(String arg0, String arg1) {
            return new Error("compiler", "proc.processor.bad.option.name", arg0, arg1);
        }
        
        /**
         * compiler.err.proc.processor.cant.instantiate=\
         *    Could not instantiate an instance of processor ''{0}''
         */
        public static Error ProcProcessorCantInstantiate(String arg0) {
            return new Error("compiler", "proc.processor.cant.instantiate", arg0);
        }
        
        /**
         * compiler.err.proc.processor.not.found=\
         *    Annotation processor ''{0}'' not found
         */
        public static Error ProcProcessorNotFound(String arg0) {
            return new Error("compiler", "proc.processor.not.found", arg0);
        }
        
        /**
         * compiler.err.proc.processor.wrong.type=\
         *    Annotation processor ''{0}'' does not implement javax.annotation.processing.Processor
         */
        public static Error ProcProcessorWrongType(String arg0) {
            return new Error("compiler", "proc.processor.wrong.type", arg0);
        }
        
        /**
         * compiler.err.proc.service.problem=\
         *    Error creating a service loader to load Processors.
         */
        public static final Error ProcServiceProblem = new Error("compiler", "proc.service.problem");
        
        /**
         * compiler.err.processorpath.no.processormodulepath=\
         *    illegal combination of -processorpath and --processor-module-path
         */
        public static final Error ProcessorpathNoProcessormodulepath = new Error("compiler", "processorpath.no.processormodulepath");
        
        /**
         * compiler.err.profile.bootclasspath.conflict=\
         *    profile and bootclasspath options cannot be used together
         */
        public static final Error ProfileBootclasspathConflict = new Error("compiler", "profile.bootclasspath.conflict");
        
        /**
         * compiler.err.qualified.new.of.static.class=\
         *    qualified new of static class
         */
        public static Error QualifiedNewOfStaticClass(Symbol arg0) {
            return new Error("compiler", "qualified.new.of.static.class", arg0);
        }
        
        /**
         * compiler.err.receiver.parameter.not.applicable.constructor.toplevel.class=\
         *    receiver parameter not applicable for constructor of top-level class
         */
        public static final Error ReceiverParameterNotApplicableConstructorToplevelClass = new Error("compiler", "receiver.parameter.not.applicable.constructor.toplevel.class");
        
        /**
         * compiler.err.record.cannot.declare.instance.fields=\
         *    field declaration must be static\n\
         *    (consider replacing field with record component)
         */
        public static final Error RecordCannotDeclareInstanceFields = new Error("compiler", "record.cannot.declare.instance.fields");
        
        /**
         * compiler.err.record.cant.declare.field.modifiers=\
         *    record components cannot have modifiers
         */
        public static final Error RecordCantDeclareFieldModifiers = new Error("compiler", "record.cant.declare.field.modifiers");
        
        /**
         * compiler.err.record.component.and.old.array.syntax=\
         *    legacy array notation not allowed on record components
         */
        public static final Error RecordComponentAndOldArraySyntax = new Error("compiler", "record.component.and.old.array.syntax");
        
        /**
         * compiler.err.record.header.expected=\
         *    record header expected
         */
        public static final Error RecordHeaderExpected = new Error("compiler", "record.header.expected");
        
        /**
         * compiler.err.recursive.ctor.invocation=\
         *    recursive constructor invocation
         */
        public static final Error RecursiveCtorInvocation = new Error("compiler", "recursive.ctor.invocation");
        
        /**
         * compiler.err.ref.ambiguous=\
         *    reference to {0} is ambiguous\n\
         *    both {1} {2} in {3} and {4} {5} in {6} match
         */
        public static Error RefAmbiguous(Name arg0, Kind arg1, Symbol arg2, Symbol arg3, Kind arg4, Symbol arg5, Symbol arg6) {
            return new Error("compiler", "ref.ambiguous", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.err.release.bootclasspath.conflict=\
         *    option {0} cannot be used together with --release
         */
        public static Error ReleaseBootclasspathConflict(Option arg0) {
            return new Error("compiler", "release.bootclasspath.conflict", arg0);
        }
        
        /**
         * compiler.err.repeated.annotation.target=\
         *    repeated annotation target
         */
        public static final Error RepeatedAnnotationTarget = new Error("compiler", "repeated.annotation.target");
        
        /**
         * compiler.err.repeated.interface=\
         *    repeated interface
         */
        public static final Error RepeatedInterface = new Error("compiler", "repeated.interface");
        
        /**
         * compiler.err.repeated.modifier=\
         *    repeated modifier
         */
        public static final Error RepeatedModifier = new Error("compiler", "repeated.modifier");
        
        /**
         * compiler.err.repeated.provides.for.service=\
         *    multiple ''provides'' for service {0}
         */
        public static Error RepeatedProvidesForService(Symbol arg0) {
            return new Error("compiler", "repeated.provides.for.service", arg0);
        }
        
        /**
         * compiler.err.repeated.value.for.module.source.path=\
         *    --module-source-path specified more than once for module {0}
         */
        public static Error RepeatedValueForModuleSourcePath(String arg0) {
            return new Error("compiler", "repeated.value.for.module.source.path", arg0);
        }
        
        /**
         * compiler.err.repeated.value.for.patch.module=\
         *    --patch-module specified more than once for module {0}
         */
        public static Error RepeatedValueForPatchModule(String arg0) {
            return new Error("compiler", "repeated.value.for.patch.module", arg0);
        }
        
        /**
         * compiler.err.report.access=\
         *    {0} has {1} access in {2}
         */
        public static Error ReportAccess(Symbol arg0, Set<? extends Modifier> arg1, Symbol arg2) {
            return new Error("compiler", "report.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.req.arg=\
         *    {0} requires an argument
         */
        public static Error ReqArg(String arg0) {
            return new Error("compiler", "req.arg", arg0);
        }
        
        /**
         * compiler.err.restricted.type.not.allowed=\
         *    ''{0}'' not allowed here\n\
         *    as of release {1}, ''{0}'' is a restricted type name and cannot be used for type declarations
         */
        public static Error RestrictedTypeNotAllowed(Name arg0, Source arg1) {
            return new Error("compiler", "restricted.type.not.allowed", arg0, arg1);
        }
        
        /**
         * compiler.err.restricted.type.not.allowed.array=\
         *    ''{0}'' is not allowed as an element type of an array
         */
        public static Error RestrictedTypeNotAllowedArray(Name arg0) {
            return new Error("compiler", "restricted.type.not.allowed.array", arg0);
        }
        
        /**
         * compiler.err.restricted.type.not.allowed.compound=\
         *    ''{0}'' is not allowed in a compound declaration
         */
        public static Error RestrictedTypeNotAllowedCompound(Name arg0) {
            return new Error("compiler", "restricted.type.not.allowed.compound", arg0);
        }
        
        /**
         * compiler.err.restricted.type.not.allowed.here=\
         *    ''{0}'' is not allowed here
         */
        public static Error RestrictedTypeNotAllowedHere(Name arg0) {
            return new Error("compiler", "restricted.type.not.allowed.here", arg0);
        }
        
        /**
         * compiler.err.ret.outside.meth=\
         *    return outside method
         */
        public static final Error RetOutsideMeth = new Error("compiler", "ret.outside.meth");
        
        /**
         * compiler.err.return.outside.switch.expression=\
         *    attempt to return out of a switch expression
         */
        public static final Error ReturnOutsideSwitchExpression = new Error("compiler", "return.outside.switch.expression");
        
        /**
         * compiler.err.rule.completes.normally=\
         *    switch rule completes without providing a value\n\
         *    (switch rules in switch expressions must either provide a value or throw)
         */
        public static final Error RuleCompletesNormally = new Error("compiler", "rule.completes.normally");
        
        /**
         * compiler.err.same.binary.name=\
         *    classes: {0} and {1} have the same binary name
         */
        public static Error SameBinaryName(Name arg0, Name arg1) {
            return new Error("compiler", "same.binary.name", arg0, arg1);
        }
        
        /**
         * compiler.err.sealed.class.must.have.subclasses=\
         *    sealed class must have subclasses
         */
        public static final Error SealedClassMustHaveSubclasses = new Error("compiler", "sealed.class.must.have.subclasses");
        
        /**
         * compiler.err.sealed.or.non.sealed.local.classes.not.allowed=\
         *    sealed or non-sealed local classes are not allowed
         */
        public static final Error SealedOrNonSealedLocalClassesNotAllowed = new Error("compiler", "sealed.or.non.sealed.local.classes.not.allowed");
        
        /**
         * compiler.err.service.definition.is.enum=\
         *    the service definition is an enum: {0}
         */
        public static Error ServiceDefinitionIsEnum(Symbol arg0) {
            return new Error("compiler", "service.definition.is.enum", arg0);
        }
        
        /**
         * compiler.err.service.implementation.doesnt.have.a.no.args.constructor=\
         *    the service implementation does not have a default constructor: {0}
         */
        public static Error ServiceImplementationDoesntHaveANoArgsConstructor(Symbol arg0) {
            return new Error("compiler", "service.implementation.doesnt.have.a.no.args.constructor", arg0);
        }
        
        /**
         * compiler.err.service.implementation.is.abstract=\
         *    the service implementation is an abstract class: {0}
         */
        public static Error ServiceImplementationIsAbstract(Symbol arg0) {
            return new Error("compiler", "service.implementation.is.abstract", arg0);
        }
        
        /**
         * compiler.err.service.implementation.is.inner=\
         *    the service implementation is an inner class: {0}
         */
        public static Error ServiceImplementationIsInner(Symbol arg0) {
            return new Error("compiler", "service.implementation.is.inner", arg0);
        }
        
        /**
         * compiler.err.service.implementation.must.be.subtype.of.service.interface=\
         *    the service implementation type must be a subtype of the service interface type, or \
         *    have a public static no-args method named "provider" returning the service implementation
         */
        public static final Error ServiceImplementationMustBeSubtypeOfServiceInterface = new Error("compiler", "service.implementation.must.be.subtype.of.service.interface");
        
        /**
         * compiler.err.service.implementation.no.args.constructor.not.public=\
         *    the no arguments constructor of the service implementation is not public: {0}
         */
        public static Error ServiceImplementationNoArgsConstructorNotPublic(Symbol arg0) {
            return new Error("compiler", "service.implementation.no.args.constructor.not.public", arg0);
        }
        
        /**
         * compiler.err.service.implementation.not.in.right.module=\
         *    service implementation must be defined in the same module as the provides directive
         */
        public static Error ServiceImplementationNotInRightModule(Symbol arg0) {
            return new Error("compiler", "service.implementation.not.in.right.module", arg0);
        }
        
        /**
         * compiler.err.service.implementation.provider.return.must.be.subtype.of.service.interface=\
         *    the "provider" method return type must be a subtype of the service interface type
         */
        public static final Error ServiceImplementationProviderReturnMustBeSubtypeOfServiceInterface = new Error("compiler", "service.implementation.provider.return.must.be.subtype.of.service.interface");
        
        /**
         * compiler.err.signature.doesnt.match.intf=\
         *    signature does not match {0}; incompatible interfaces
         */
        public static final Error SignatureDoesntMatchIntf = new Error("compiler", "signature.doesnt.match.intf");
        
        /**
         * compiler.err.signature.doesnt.match.supertype=\
         *    signature does not match {0}; incompatible supertype
         */
        public static final Error SignatureDoesntMatchSupertype = new Error("compiler", "signature.doesnt.match.supertype");
        
        /**
         * compiler.err.source.cant.overwrite.input.file=\
         *    error writing source; cannot overwrite input file {0}
         */
        public static Error SourceCantOverwriteInputFile(JavaFileObject arg0) {
            return new Error("compiler", "source.cant.overwrite.input.file", arg0);
        }
        
        /**
         * compiler.err.sourcepath.modulesourcepath.conflict=\
         *    cannot specify both --source-path and --module-source-path
         */
        public static final Error SourcepathModulesourcepathConflict = new Error("compiler", "sourcepath.modulesourcepath.conflict");
        
        /**
         * compiler.err.stack.sim.error=\
         *    Internal error: stack sim error on {0}
         */
        public static Error StackSimError(Symbol arg0) {
            return new Error("compiler", "stack.sim.error", arg0);
        }
        
        /**
         * compiler.err.static.declaration.not.allowed.in.inner.classes=\
         *    static declarations not allowed in inner classes
         */
        public static final Error StaticDeclarationNotAllowedInInnerClasses = new Error("compiler", "static.declaration.not.allowed.in.inner.classes");
        
        /**
         * compiler.err.static.imp.only.classes.and.interfaces=\
         *    static import only from classes and interfaces
         */
        public static final Error StaticImpOnlyClassesAndInterfaces = new Error("compiler", "static.imp.only.classes.and.interfaces");
        
        /**
         * compiler.err.static.methods.cannot.be.annotated.with.override=\
         *    static methods cannot be annotated with @Override
         */
        public static final Error StaticMethodsCannotBeAnnotatedWithOverride = new Error("compiler", "static.methods.cannot.be.annotated.with.override");
        
        /**
         * compiler.err.string.const.req=\
         *    constant string expression required
         */
        public static final Error StringConstReq = new Error("compiler", "string.const.req");
        
        /**
         * compiler.err.switch.case.unexpected.statement=\
         *    unexpected statement in case, expected is an expression, a block or a throw statement
         */
        public static final Error SwitchCaseUnexpectedStatement = new Error("compiler", "switch.case.unexpected.statement");
        
        /**
         * compiler.err.switch.expression.completes.normally=\
         *    switch expression completes without providing a value\n\
         *    (switch expressions must either provide a value or throw for all possible input values)
         */
        public static final Error SwitchExpressionCompletesNormally = new Error("compiler", "switch.expression.completes.normally");
        
        /**
         * compiler.err.switch.expression.empty=\
         *    switch expression does not have any case clauses
         */
        public static final Error SwitchExpressionEmpty = new Error("compiler", "switch.expression.empty");
        
        /**
         * compiler.err.switch.expression.no.result.expressions=\
         *    switch expression does not have any result expressions
         */
        public static final Error SwitchExpressionNoResultExpressions = new Error("compiler", "switch.expression.no.result.expressions");
        
        /**
         * compiler.err.switch.mixing.case.types=\
         *    different case kinds used in the switch
         */
        public static final Error SwitchMixingCaseTypes = new Error("compiler", "switch.mixing.case.types");
        
        /**
         * compiler.err.this.as.identifier=\
         *    as of release 8, ''this'' is allowed as the parameter name for the receiver type only\n\
         *    which has to be the first parameter, and cannot be a lambda parameter
         */
        public static final Error ThisAsIdentifier = new Error("compiler", "this.as.identifier");
        
        /**
         * compiler.err.throws.not.allowed.in.intf.annotation=\
         *    throws clause not allowed in @interface members
         */
        public static final Error ThrowsNotAllowedInIntfAnnotation = new Error("compiler", "throws.not.allowed.in.intf.annotation");
        
        /**
         * compiler.err.too.many.modules=\
         *    too many module declarations found
         */
        public static final Error TooManyModules = new Error("compiler", "too.many.modules");
        
        /**
         * compiler.err.too.many.patched.modules=\
         *    too many patched modules ({0}), use --module-source-path
         */
        public static Error TooManyPatchedModules(Set<? extends String> arg0) {
            return new Error("compiler", "too.many.patched.modules", arg0);
        }
        
        /**
         * compiler.err.total.pattern.and.default=\
         *    switch has both a total pattern and a default label
         */
        public static final Error TotalPatternAndDefault = new Error("compiler", "total.pattern.and.default");
        
        /**
         * compiler.err.try.resource.may.not.be.assigned=\
         *    auto-closeable resource {0} may not be assigned
         */
        public static Error TryResourceMayNotBeAssigned(Symbol arg0) {
            return new Error("compiler", "try.resource.may.not.be.assigned", arg0);
        }
        
        /**
         * compiler.err.try.with.resources.expr.effectively.final.var=\
         *    variable {0} used as a try-with-resources resource neither final nor effectively final
         */
        public static Error TryWithResourcesExprEffectivelyFinalVar(Symbol arg0) {
            return new Error("compiler", "try.with.resources.expr.effectively.final.var", arg0);
        }
        
        /**
         * compiler.err.try.with.resources.expr.needs.var=\
         *    the try-with-resources resource must either be a variable declaration or an expression denoting \
         *a reference to a final or effectively final variable
         */
        public static final Error TryWithResourcesExprNeedsVar = new Error("compiler", "try.with.resources.expr.needs.var");
        
        /**
         * compiler.err.try.without.catch.finally.or.resource.decls=\
         *    ''try'' without ''catch'', ''finally'' or resource declarations
         */
        public static final Error TryWithoutCatchFinallyOrResourceDecls = new Error("compiler", "try.without.catch.finally.or.resource.decls");
        
        /**
         * compiler.err.two.class.loaders.1=\
         *    javac is split between multiple class loaders: check your configuration
         */
        public static final Error TwoClassLoaders1 = new Error("compiler", "two.class.loaders.1");
        
        /**
         * compiler.err.two.class.loaders.2=\
         *    javac is split between multiple class loaders:\n\
         *    one class comes from file: {0}\n\
         *    while javac comes from {1}
         */
        public static Error TwoClassLoaders2(URL arg0, URL arg1) {
            return new Error("compiler", "two.class.loaders.2", arg0, arg1);
        }
        
        /**
         * compiler.err.type.doesnt.take.params=\
         *    type {0} does not take parameters
         */
        public static Error TypeDoesntTakeParams(Symbol arg0) {
            return new Error("compiler", "type.doesnt.take.params", arg0);
        }
        
        /**
         * compiler.err.type.error=\
         *    type of {0} is erroneous
         */
        public static Error TypeError(Symbol arg0) {
            return new Error("compiler", "type.error", arg0);
        }
        
        /**
         * compiler.err.type.found.req=\
         *    unexpected type\n\
         *    required: {1}\n\
         *    found:    {0}
         */
        public static Error TypeFoundReq(Object arg0, JCDiagnostic arg1) {
            return new Error("compiler", "type.found.req", arg0, arg1);
        }
        
        /**
         * compiler.err.type.found.req=\
         *    unexpected type\n\
         *    required: {1}\n\
         *    found:    {0}
         */
        public static Error TypeFoundReq(Object arg0, Fragment arg1) {
            return new Error("compiler", "type.found.req", arg0, arg1);
        }
        
        /**
         * compiler.err.type.var.cant.be.deref=\
         *    cannot select from a type variable
         */
        public static final Error TypeVarCantBeDeref = new Error("compiler", "type.var.cant.be.deref");
        
        /**
         * compiler.err.type.var.may.not.be.followed.by.other.bounds=\
         *    a type variable may not be followed by other bounds
         */
        public static final Error TypeVarMayNotBeFollowedByOtherBounds = new Error("compiler", "type.var.may.not.be.followed.by.other.bounds");
        
        /**
         * compiler.err.type.var.more.than.once=\
         *    type variable {0} occurs more than once in result type of {1}; cannot be left uninstantiated
         */
        public static final Error TypeVarMoreThanOnce = new Error("compiler", "type.var.more.than.once");
        
        /**
         * compiler.err.type.var.more.than.once.in.result=\
         *    type variable {0} occurs more than once in type of {1}; cannot be left uninstantiated
         */
        public static final Error TypeVarMoreThanOnceInResult = new Error("compiler", "type.var.more.than.once.in.result");
        
        /**
         * compiler.err.types.incompatible=\
         *    types {0} and {1} are incompatible;\n\
         *    {2}
         */
        public static Error TypesIncompatible(Type arg0, Type arg1, Fragment arg2) {
            return new Error("compiler", "types.incompatible", arg0, arg1, arg2);
        }
        
        /**
         * compiler.err.unclosed.char.lit=\
         *    unclosed character literal
         */
        public static final Error UnclosedCharLit = new Error("compiler", "unclosed.char.lit");
        
        /**
         * compiler.err.unclosed.comment=\
         *    unclosed comment
         */
        public static final Error UnclosedComment = new Error("compiler", "unclosed.comment");
        
        /**
         * compiler.err.unclosed.str.lit=\
         *    unclosed string literal
         */
        public static final Error UnclosedStrLit = new Error("compiler", "unclosed.str.lit");
        
        /**
         * compiler.err.unclosed.text.block=\
         *    unclosed text block
         */
        public static final Error UnclosedTextBlock = new Error("compiler", "unclosed.text.block");
        
        /**
         * compiler.err.undef.label=\
         *    undefined label: {0}
         */
        public static Error UndefLabel(Name arg0) {
            return new Error("compiler", "undef.label", arg0);
        }
        
        /**
         * compiler.err.underscore.as.identifier=\
         *    as of release 9, ''_'' is a keyword, and may not be used as an identifier
         */
        public static final Error UnderscoreAsIdentifier = new Error("compiler", "underscore.as.identifier");
        
        /**
         * compiler.err.underscore.as.identifier.in.lambda=\
         *    ''_'' used as an identifier\n\
         *    (use of ''_'' as an identifier is forbidden for lambda parameters)
         */
        public static final Error UnderscoreAsIdentifierInLambda = new Error("compiler", "underscore.as.identifier.in.lambda");
        
        /**
         * compiler.err.unexpected.lambda=\
         *   lambda expression not expected here
         */
        public static final Error UnexpectedLambda = new Error("compiler", "unexpected.lambda");
        
        /**
         * compiler.err.unexpected.mref=\
         *   method reference not expected here
         */
        public static final Error UnexpectedMref = new Error("compiler", "unexpected.mref");
        
        /**
         * compiler.err.unexpected.type=\
         *    unexpected type\n\
         *    required: {0}\n\
         *    found:    {1}
         */
        public static Error UnexpectedType(Set<? extends KindName> arg0, Set<? extends KindName> arg1) {
            return new Error("compiler", "unexpected.type", arg0, arg1);
        }
        
        /**
         * compiler.err.unmatched.quote=\
         *    unmatched quote in environment variable {0}
         */
        public static Error UnmatchedQuote(String arg0) {
            return new Error("compiler", "unmatched.quote", arg0);
        }
        
        /**
         * compiler.err.unnamed.pkg.not.allowed.named.modules=\
         *    unnamed package is not allowed in named modules
         */
        public static final Error UnnamedPkgNotAllowedNamedModules = new Error("compiler", "unnamed.pkg.not.allowed.named.modules");
        
        /**
         * compiler.err.unreachable.stmt=\
         *    unreachable statement
         */
        public static final Error UnreachableStmt = new Error("compiler", "unreachable.stmt");
        
        /**
         * compiler.err.unreported.exception.default.constructor=\
         *    unreported exception {0} in default constructor
         */
        public static Error UnreportedExceptionDefaultConstructor(Type arg0) {
            return new Error("compiler", "unreported.exception.default.constructor", arg0);
        }
        
        /**
         * compiler.err.unreported.exception.implicit.close=\
         *    unreported exception {0}; must be caught or declared to be thrown\n\
         *    exception thrown from implicit call to close() on resource variable ''{1}''
         */
        public static Error UnreportedExceptionImplicitClose(Type arg0, Name arg1) {
            return new Error("compiler", "unreported.exception.implicit.close", arg0, arg1);
        }
        
        /**
         * compiler.err.unreported.exception.need.to.catch.or.throw=\
         *    unreported exception {0}; must be caught or declared to be thrown
         */
        public static Error UnreportedExceptionNeedToCatchOrThrow(Type arg0) {
            return new Error("compiler", "unreported.exception.need.to.catch.or.throw", arg0);
        }
        
        /**
         * compiler.err.unsupported.encoding=\
         *    unsupported encoding: {0}
         */
        public static Error UnsupportedEncoding(String arg0) {
            return new Error("compiler", "unsupported.encoding", arg0);
        }
        
        /**
         * compiler.err.unsupported.release.version=\
         *    release version {0} not supported
         */
        public static Error UnsupportedReleaseVersion(String arg0) {
            return new Error("compiler", "unsupported.release.version", arg0);
        }
        
        /**
         * compiler.err.var.might.already.be.assigned=\
         *    variable {0} might already have been assigned
         */
        public static Error VarMightAlreadyBeAssigned(Symbol arg0) {
            return new Error("compiler", "var.might.already.be.assigned", arg0);
        }
        
        /**
         * compiler.err.var.might.be.assigned.in.loop=\
         *    variable {0} might be assigned in loop
         */
        public static Error VarMightBeAssignedInLoop(Symbol arg0) {
            return new Error("compiler", "var.might.be.assigned.in.loop", arg0);
        }
        
        /**
         * compiler.err.var.might.not.have.been.initialized=\
         *    variable {0} might not have been initialized
         */
        public static Error VarMightNotHaveBeenInitialized(Symbol arg0) {
            return new Error("compiler", "var.might.not.have.been.initialized", arg0);
        }
        
        /**
         * compiler.err.var.not.initialized.in.default.constructor=\
         *    variable {0} not initialized in the default constructor
         */
        public static Error VarNotInitializedInDefaultConstructor(Symbol arg0) {
            return new Error("compiler", "var.not.initialized.in.default.constructor", arg0);
        }
        
        /**
         * compiler.err.varargs.and.old.array.syntax=\
         *    legacy array notation not allowed on variable-arity parameter
         */
        public static final Error VarargsAndOldArraySyntax = new Error("compiler", "varargs.and.old.array.syntax");
        
        /**
         * compiler.err.varargs.and.receiver =\
         *    varargs notation not allowed on receiver parameter
         */
        public static final Error VarargsAndReceiver = new Error("compiler", "varargs.and.receiver");
        
        /**
         * compiler.err.varargs.invalid.trustme.anno=\
         *    Invalid {0} annotation. {1}
         */
        public static Error VarargsInvalidTrustmeAnno(Symbol arg0, JCDiagnostic arg1) {
            return new Error("compiler", "varargs.invalid.trustme.anno", arg0, arg1);
        }
        
        /**
         * compiler.err.varargs.invalid.trustme.anno=\
         *    Invalid {0} annotation. {1}
         */
        public static Error VarargsInvalidTrustmeAnno(Symbol arg0, Fragment arg1) {
            return new Error("compiler", "varargs.invalid.trustme.anno", arg0, arg1);
        }
        
        /**
         * compiler.err.varargs.must.be.last =\
         *    varargs parameter must be the last parameter
         */
        public static final Error VarargsMustBeLast = new Error("compiler", "varargs.must.be.last");
        
        /**
         * compiler.err.variable.not.allowed=\
         *    variable declaration not allowed here
         */
        public static final Error VariableNotAllowed = new Error("compiler", "variable.not.allowed");
        
        /**
         * compiler.err.void.not.allowed.here=\
         *    ''void'' type not allowed here
         */
        public static final Error VoidNotAllowedHere = new Error("compiler", "void.not.allowed.here");
        
        /**
         * compiler.err.warnings.and.werror=\
         *    warnings found and -Werror specified
         */
        public static final Error WarningsAndWerror = new Error("compiler", "warnings.and.werror");
        
        /**
         * compiler.err.wrong.number.type.args=\
         *    wrong number of type arguments; required {0}
         */
        public static Error WrongNumberTypeArgs(String arg0) {
            return new Error("compiler", "wrong.number.type.args", arg0);
        }
        
        /**
         * compiler.err.wrong.receiver =\
         *    wrong receiver parameter name
         */
        public static final Error WrongReceiver = new Error("compiler", "wrong.receiver");
    }
    public static class Warnings {
        /**
         * compiler.warn.access.to.member.from.serializable.element=\
         *    access to member {0} from serializable element can be publicly accessible to untrusted code
         */
        public static Warning AccessToMemberFromSerializableElement(Symbol arg0) {
            return new Warning("compiler", "access.to.member.from.serializable.element", arg0);
        }
        
        /**
         * compiler.warn.access.to.member.from.serializable.lambda=\
         *    access to member {0} from serializable lambda can be publicly accessible to untrusted code
         */
        public static Warning AccessToMemberFromSerializableLambda(Symbol arg0) {
            return new Warning("compiler", "access.to.member.from.serializable.lambda", arg0);
        }
        
        /**
         * compiler.warn.addopens.ignored=\
         *    --add-opens has no effect at compile time
         */
        public static final Warning AddopensIgnored = new Warning("compiler", "addopens.ignored");
        
        /**
         * compiler.warn.annotation.method.not.found=\
         *    Cannot find annotation method ''{1}()'' in type ''{0}''
         */
        public static Warning AnnotationMethodNotFound(Type arg0, Name arg1) {
            return new Warning("compiler", "annotation.method.not.found", arg0, arg1);
        }
        
        /**
         * compiler.warn.annotation.method.not.found.reason=\
         *    Cannot find annotation method ''{1}()'' in type ''{0}'': {2}
         */
        public static Warning AnnotationMethodNotFoundReason(Type arg0, Name arg1, JCDiagnostic arg2) {
            return new Warning("compiler", "annotation.method.not.found.reason", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.annotation.method.not.found.reason=\
         *    Cannot find annotation method ''{1}()'' in type ''{0}'': {2}
         */
        public static Warning AnnotationMethodNotFoundReason(Type arg0, Name arg1, Fragment arg2) {
            return new Warning("compiler", "annotation.method.not.found.reason", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.attempt.to.synchronize.on.instance.of.value.based.class=\
         *    attempt to synchronize on an instance of a value-based class
         */
        public static final Warning AttemptToSynchronizeOnInstanceOfValueBasedClass = new Warning("compiler", "attempt.to.synchronize.on.instance.of.value.based.class");
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Symbol arg0, File arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Symbol arg0, JavaFileObject arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Symbol arg0, Path arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Type arg0, File arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Type arg0, JavaFileObject arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.auxiliary.class.accessed.from.outside.of.its.source.file=\
         *    auxiliary class {0} in {1} should not be accessed from outside its own source file
         */
        public static Warning AuxiliaryClassAccessedFromOutsideOfItsSourceFile(Type arg0, Path arg1) {
            return new Warning("compiler", "auxiliary.class.accessed.from.outside.of.its.source.file", arg0, arg1);
        }
        
        /**
         * compiler.warn.bad.name.for.option=\
         *    bad name in value for {0} option: ''{1}''
         */
        public static Warning BadNameForOption(Option arg0, String arg1) {
            return new Warning("compiler", "bad.name.for.option", arg0, arg1);
        }
        
        /**
         * compiler.warn.big.major.version=\
         *    {0}: major version {1} is newer than {2}, the highest major version supported by this compiler.\n\
         *    It is recommended that the compiler be upgraded.
         */
        public static Warning BigMajorVersion(File arg0, int arg1, int arg2) {
            return new Warning("compiler", "big.major.version", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.big.major.version=\
         *    {0}: major version {1} is newer than {2}, the highest major version supported by this compiler.\n\
         *    It is recommended that the compiler be upgraded.
         */
        public static Warning BigMajorVersion(JavaFileObject arg0, int arg1, int arg2) {
            return new Warning("compiler", "big.major.version", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.big.major.version=\
         *    {0}: major version {1} is newer than {2}, the highest major version supported by this compiler.\n\
         *    It is recommended that the compiler be upgraded.
         */
        public static Warning BigMajorVersion(Path arg0, int arg1, int arg2) {
            return new Warning("compiler", "big.major.version", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.constant.SVUID=\
         *    serialVersionUID must be constant in class {0}
         */
        public static Warning ConstantSVUID(Symbol arg0) {
            return new Warning("compiler", "constant.SVUID", arg0);
        }
        
        /**
         * compiler.warn.declared.using.preview=\
         *    {0} {1} is declared using a preview feature, which may be removed in a future release.
         */
        public static Warning DeclaredUsingPreview(KindName arg0, Symbol arg1) {
            return new Warning("compiler", "declared.using.preview", arg0, arg1);
        }
        
        /**
         * compiler.warn.deprecated.annotation.has.no.effect=\
         *    @Deprecated annotation has no effect on this {0} declaration
         */
        public static Warning DeprecatedAnnotationHasNoEffect(KindName arg0) {
            return new Warning("compiler", "deprecated.annotation.has.no.effect", arg0);
        }
        
        /**
         * compiler.warn.diamond.redundant.args=\
         *    Redundant type arguments in new expression (use diamond operator instead).
         */
        public static final Warning DiamondRedundantArgs = new Warning("compiler", "diamond.redundant.args");
        
        /**
         * compiler.warn.dir.path.element.not.directory=\
         *    bad path element "{0}": not a directory
         */
        public static Warning DirPathElementNotDirectory(File arg0) {
            return new Warning("compiler", "dir.path.element.not.directory", arg0);
        }
        
        /**
         * compiler.warn.dir.path.element.not.directory=\
         *    bad path element "{0}": not a directory
         */
        public static Warning DirPathElementNotDirectory(JavaFileObject arg0) {
            return new Warning("compiler", "dir.path.element.not.directory", arg0);
        }
        
        /**
         * compiler.warn.dir.path.element.not.directory=\
         *    bad path element "{0}": not a directory
         */
        public static Warning DirPathElementNotDirectory(Path arg0) {
            return new Warning("compiler", "dir.path.element.not.directory", arg0);
        }
        
        /**
         * compiler.warn.dir.path.element.not.found=\
         *    bad path element "{0}": no such directory
         */
        public static Warning DirPathElementNotFound(Path arg0) {
            return new Warning("compiler", "dir.path.element.not.found", arg0);
        }
        
        /**
         * compiler.warn.div.zero=\
         *    division by zero
         */
        public static final Warning DivZero = new Warning("compiler", "div.zero");
        
        /**
         * compiler.warn.doclint.not.available=\
         *    No service provider for doclint is available
         */
        public static final Warning DoclintNotAvailable = new Warning("compiler", "doclint.not.available");
        
        /**
         * compiler.warn.empty.if=\
         *    empty statement after if
         */
        public static final Warning EmptyIf = new Warning("compiler", "empty.if");
        
        /**
         * compiler.warn.file.from.future=\
         *    Modification date is in the future for file {0}
         */
        public static Warning FileFromFuture(File arg0) {
            return new Warning("compiler", "file.from.future", arg0);
        }
        
        /**
         * compiler.warn.file.from.future=\
         *    Modification date is in the future for file {0}
         */
        public static Warning FileFromFuture(JavaFileObject arg0) {
            return new Warning("compiler", "file.from.future", arg0);
        }
        
        /**
         * compiler.warn.file.from.future=\
         *    Modification date is in the future for file {0}
         */
        public static Warning FileFromFuture(Path arg0) {
            return new Warning("compiler", "file.from.future", arg0);
        }
        
        /**
         * compiler.warn.finally.cannot.complete=\
         *    finally clause cannot complete normally
         */
        public static final Warning FinallyCannotComplete = new Warning("compiler", "finally.cannot.complete");
        
        /**
         * compiler.warn.forward.ref=\
         *    reference to variable ''{0}'' before it has been initialized
         */
        public static Warning ForwardRef(Symbol arg0) {
            return new Warning("compiler", "forward.ref", arg0);
        }
        
        /**
         * compiler.warn.future.attr=\
         *    {0} attribute introduced in version {1}.{2} class files is ignored in version {3}.{4} class files
         */
        public static Warning FutureAttr(Name arg0, int arg1, int arg2, int arg3, int arg4) {
            return new Warning("compiler", "future.attr", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.warn.has.been.deprecated=\
         *    {0} in {1} has been deprecated
         */
        public static Warning HasBeenDeprecated(Symbol arg0, Symbol arg1) {
            return new Warning("compiler", "has.been.deprecated", arg0, arg1);
        }
        
        /**
         * compiler.warn.has.been.deprecated.for.removal=\
         *    {0} in {1} has been deprecated and marked for removal
         */
        public static Warning HasBeenDeprecatedForRemoval(Symbol arg0, Symbol arg1) {
            return new Warning("compiler", "has.been.deprecated.for.removal", arg0, arg1);
        }
        
        /**
         * compiler.warn.has.been.deprecated.for.removal.module=\
         *    module {0} has been deprecated and marked for removal
         */
        public static Warning HasBeenDeprecatedForRemovalModule(Symbol arg0) {
            return new Warning("compiler", "has.been.deprecated.for.removal.module", arg0);
        }
        
        /**
         * compiler.warn.has.been.deprecated.module=\
         *    module {0} has been deprecated
         */
        public static Warning HasBeenDeprecatedModule(Symbol arg0) {
            return new Warning("compiler", "has.been.deprecated.module", arg0);
        }
        
        /**
         * compiler.warn.illegal.char.for.encoding=\
         *    unmappable character for encoding {0}
         */
        public static final Warning IllegalCharForEncoding = new Warning("compiler", "illegal.char.for.encoding");
        
        /**
         * compiler.warn.illegal.ref.to.restricted.type=\
         *    illegal reference to restricted type ''{0}''
         */
        public static Warning IllegalRefToRestrictedType(Name arg0) {
            return new Warning("compiler", "illegal.ref.to.restricted.type", arg0);
        }
        
        /**
         * compiler.warn.improper.SVUID=\
         *    serialVersionUID must be declared static final in class {0}
         */
        public static Warning ImproperSVUID(Symbol arg0) {
            return new Warning("compiler", "improper.SVUID", arg0);
        }
        
        /**
         * compiler.warn.inconsistent.white.space.indentation=\
         *    inconsistent white space indentation
         */
        public static final Warning InconsistentWhiteSpaceIndentation = new Warning("compiler", "inconsistent.white.space.indentation");
        
        /**
         * compiler.warn.incubating.modules=\
         *    using incubating module(s): {0}
         */
        public static Warning IncubatingModules(String arg0) {
            return new Warning("compiler", "incubating.modules", arg0);
        }
        
        /**
         * compiler.warn.inexact.non-varargs.call=\
         *    non-varargs call of varargs method with inexact argument type for last parameter;\n\
         *    cast to {0} for a varargs call\n\
         *    cast to {1} for a non-varargs call and to suppress this warning
         */
        public static Warning InexactNonVarargsCall(Type arg0, Type arg1) {
            return new Warning("compiler", "inexact.non-varargs.call", arg0, arg1);
        }
        
        /**
         * compiler.warn.invalid.archive.file=\
         *    Unexpected file on path: {0}
         */
        public static Warning InvalidArchiveFile(Path arg0) {
            return new Warning("compiler", "invalid.archive.file", arg0);
        }
        
        /**
         * compiler.warn.invalid.path=\
         *    Invalid filename: {0}
         */
        public static Warning InvalidPath(String arg0) {
            return new Warning("compiler", "invalid.path", arg0);
        }
        
        /**
         * compiler.warn.invalid.yield=\
         *    ''yield'' may become a restricted identifier in a future release\n\
         *    (to invoke a method called yield, qualify the yield with a receiver or type name)
         */
        public static final Warning InvalidYield = new Warning("compiler", "invalid.yield");
        
        /**
         * compiler.warn.is.preview=\
         *    {0} is a preview API and may be removed in a future release.
         */
        public static Warning IsPreview(Symbol arg0) {
            return new Warning("compiler", "is.preview", arg0);
        }
        
        /**
         * compiler.warn.is.preview.reflective=\
         *    {0} is a reflective preview API and may be removed in a future release.
         */
        public static Warning IsPreviewReflective(Symbol arg0) {
            return new Warning("compiler", "is.preview.reflective", arg0);
        }
        
        /**
         * compiler.warn.leaks.not.accessible=\
         *    {0} {1} in module {2} is not accessible to clients that require this module
         */
        public static Warning LeaksNotAccessible(KindName arg0, Symbol arg1, Symbol arg2) {
            return new Warning("compiler", "leaks.not.accessible", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.leaks.not.accessible.not.required.transitive=\
         *    {0} {1} in module {2} is not indirectly exported using 'requires transitive'
         */
        public static Warning LeaksNotAccessibleNotRequiredTransitive(KindName arg0, Symbol arg1, Symbol arg2) {
            return new Warning("compiler", "leaks.not.accessible.not.required.transitive", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.leaks.not.accessible.unexported=\
         *    {0} {1} in module {2} is not exported
         */
        public static Warning LeaksNotAccessibleUnexported(KindName arg0, Symbol arg1, Symbol arg2) {
            return new Warning("compiler", "leaks.not.accessible.unexported", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.leaks.not.accessible.unexported.qualified=\
         *    {0} {1} in module {2} may not be visible to all clients that require this module
         */
        public static Warning LeaksNotAccessibleUnexportedQualified(KindName arg0, Symbol arg1, Symbol arg2) {
            return new Warning("compiler", "leaks.not.accessible.unexported.qualified", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.lintOption=\
         *    [{0}]\u0020
         */
        public static Warning LintOption(Option arg0) {
            return new Warning("compiler", "lintOption", arg0);
        }
        
        /**
         * compiler.warn.local.redundant.type=\
         *    Redundant type for local variable (replace explicit type with ''var'').
         */
        public static final Warning LocalRedundantType = new Warning("compiler", "local.redundant.type");
        
        /**
         * compiler.warn.locn.unknown.file.on.module.path=\
         *    unknown file on module path: {0}
         */
        public static Warning LocnUnknownFileOnModulePath(Path arg0) {
            return new Warning("compiler", "locn.unknown.file.on.module.path", arg0);
        }
        
        /**
         * compiler.warn.long.SVUID=\
         *    serialVersionUID must be of type long in class {0}
         */
        public static Warning LongSVUID(Symbol arg0) {
            return new Warning("compiler", "long.SVUID", arg0);
        }
        
        /**
         * compiler.warn.method.redundant.typeargs=\
         *    Redundant type arguments in method call.
         */
        public static final Warning MethodRedundantTypeargs = new Warning("compiler", "method.redundant.typeargs");
        
        /**
         * compiler.warn.missing-explicit-ctor=\
         *    class {0} in exported package {1} declares no explicit constructors, thereby exposing a default constructor to clients of module {2}
         */
        public static Warning MissingExplicitCtor(Symbol arg0, Symbol arg1, Symbol arg2) {
            return new Warning("compiler", "missing-explicit-ctor", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.missing.SVUID=\
         *    serializable class {0} has no definition of serialVersionUID
         */
        public static Warning MissingSVUID(Symbol arg0) {
            return new Warning("compiler", "missing.SVUID", arg0);
        }
        
        /**
         * compiler.warn.missing.deprecated.annotation=\
         *    deprecated item is not annotated with @Deprecated
         */
        public static final Warning MissingDeprecatedAnnotation = new Warning("compiler", "missing.deprecated.annotation");
        
        /**
         * compiler.warn.module.for.option.not.found=\
         *    module name in {0} option not found: {1}
         */
        public static Warning ModuleForOptionNotFound(Option arg0, Symbol arg1) {
            return new Warning("compiler", "module.for.option.not.found", arg0, arg1);
        }
        
        /**
         * compiler.warn.module.not.found=\
         *    module not found: {0}
         */
        public static Warning ModuleNotFound(Symbol arg0) {
            return new Warning("compiler", "module.not.found", arg0);
        }
        
        /**
         * compiler.warn.option.obsolete.source=\
         *    source value {0} is obsolete and will be removed in a future release
         */
        public static Warning OptionObsoleteSource(String arg0) {
            return new Warning("compiler", "option.obsolete.source", arg0);
        }
        
        /**
         * compiler.warn.option.obsolete.suppression=\
         *    To suppress warnings about obsolete options, use -Xlint:-options.
         */
        public static final Warning OptionObsoleteSuppression = new Warning("compiler", "option.obsolete.suppression");
        
        /**
         * compiler.warn.option.obsolete.target=\
         *    target value {0} is obsolete and will be removed in a future release
         */
        public static Warning OptionObsoleteTarget(Target arg0) {
            return new Warning("compiler", "option.obsolete.target", arg0);
        }
        
        /**
         * compiler.warn.option.parameters.unsupported=\
         *    -parameters is not supported for target value {0}. Use {1} or later.
         */
        public static Warning OptionParametersUnsupported(Target arg0, Target arg1) {
            return new Warning("compiler", "option.parameters.unsupported", arg0, arg1);
        }
        
        /**
         * compiler.warn.outdir.is.in.exploded.module=\
         *    the output directory is within an exploded module: {0}
         */
        public static Warning OutdirIsInExplodedModule(Path arg0) {
            return new Warning("compiler", "outdir.is.in.exploded.module", arg0);
        }
        
        /**
         * compiler.warn.override.bridge=\
         *    {0}; overridden method is a bridge method
         */
        public static Warning OverrideBridge(JCDiagnostic arg0) {
            return new Warning("compiler", "override.bridge", arg0);
        }
        
        /**
         * compiler.warn.override.bridge=\
         *    {0}; overridden method is a bridge method
         */
        public static Warning OverrideBridge(Fragment arg0) {
            return new Warning("compiler", "override.bridge", arg0);
        }
        
        /**
         * compiler.warn.override.equals.but.not.hashcode=\
         *    Class {0} overrides equals, but neither it nor any superclass overrides hashCode method
         */
        public static Warning OverrideEqualsButNotHashcode(Symbol arg0) {
            return new Warning("compiler", "override.equals.but.not.hashcode", arg0);
        }
        
        /**
         * compiler.warn.override.unchecked.ret=\
         *    {0}\n\
         *    return type requires unchecked conversion from {1} to {2}
         */
        public static Warning OverrideUncheckedRet(JCDiagnostic arg0, Type arg1, Type arg2) {
            return new Warning("compiler", "override.unchecked.ret", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.override.unchecked.ret=\
         *    {0}\n\
         *    return type requires unchecked conversion from {1} to {2}
         */
        public static Warning OverrideUncheckedRet(Fragment arg0, Type arg1, Type arg2) {
            return new Warning("compiler", "override.unchecked.ret", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.override.unchecked.thrown=\
         *    {0}\n\
         *    overridden method does not throw {1}
         */
        public static Warning OverrideUncheckedThrown(JCDiagnostic arg0, Type arg1) {
            return new Warning("compiler", "override.unchecked.thrown", arg0, arg1);
        }
        
        /**
         * compiler.warn.override.unchecked.thrown=\
         *    {0}\n\
         *    overridden method does not throw {1}
         */
        public static Warning OverrideUncheckedThrown(Fragment arg0, Type arg1) {
            return new Warning("compiler", "override.unchecked.thrown", arg0, arg1);
        }
        
        /**
         * compiler.warn.override.varargs.extra=\
         *    {0}; overriding method is missing ''...''
         */
        public static Warning OverrideVarargsExtra(JCDiagnostic arg0) {
            return new Warning("compiler", "override.varargs.extra", arg0);
        }
        
        /**
         * compiler.warn.override.varargs.extra=\
         *    {0}; overriding method is missing ''...''
         */
        public static Warning OverrideVarargsExtra(Fragment arg0) {
            return new Warning("compiler", "override.varargs.extra", arg0);
        }
        
        /**
         * compiler.warn.override.varargs.missing=\
         *    {0}; overridden method has no ''...''
         */
        public static Warning OverrideVarargsMissing(JCDiagnostic arg0) {
            return new Warning("compiler", "override.varargs.missing", arg0);
        }
        
        /**
         * compiler.warn.override.varargs.missing=\
         *    {0}; overridden method has no ''...''
         */
        public static Warning OverrideVarargsMissing(Fragment arg0) {
            return new Warning("compiler", "override.varargs.missing", arg0);
        }
        
        /**
         * compiler.warn.package.empty.or.not.found=\
         *    package is empty or does not exist: {0}
         */
        public static Warning PackageEmptyOrNotFound(Symbol arg0) {
            return new Warning("compiler", "package.empty.or.not.found", arg0);
        }
        
        /**
         * compiler.warn.path.element.not.found=\
         *    bad path element "{0}": no such file or directory
         */
        public static Warning PathElementNotFound(Path arg0) {
            return new Warning("compiler", "path.element.not.found", arg0);
        }
        
        /**
         * compiler.warn.pkg-info.already.seen=\
         *    a package-info.java file has already been seen for package {0}
         */
        public static Warning PkgInfoAlreadySeen(Symbol arg0) {
            return new Warning("compiler", "pkg-info.already.seen", arg0);
        }
        
        /**
         * compiler.warn.poor.choice.for.module.name=\
         *    module name component {0} should avoid terminal digits
         */
        public static Warning PoorChoiceForModuleName(Name arg0) {
            return new Warning("compiler", "poor.choice.for.module.name", arg0);
        }
        
        /**
         * compiler.warn.position.overflow=\
         *    Position encoding overflows at line {0}
         */
        public static Warning PositionOverflow(int arg0) {
            return new Warning("compiler", "position.overflow", arg0);
        }
        
        /**
         * compiler.warn.possible.fall-through.into.case=\
         *    possible fall-through into case
         */
        public static final Warning PossibleFallThroughIntoCase = new Warning("compiler", "possible.fall-through.into.case");
        
        /**
         * compiler.warn.potential.lambda.found=\
         *    This anonymous inner class creation can be turned into a lambda expression.
         */
        public static final Warning PotentialLambdaFound = new Warning("compiler", "potential.lambda.found");
        
        /**
         * compiler.warn.potentially.ambiguous.overload=\
         *    {0} in {1} is potentially ambiguous with {2} in {3}
         */
        public static Warning PotentiallyAmbiguousOverload(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Warning("compiler", "potentially.ambiguous.overload", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.warn.preview.feature.use=\
         *   {0} is a preview feature and may be removed in a future release.
         */
        public static Warning PreviewFeatureUse(JCDiagnostic arg0) {
            return new Warning("compiler", "preview.feature.use", arg0);
        }
        
        /**
         * compiler.warn.preview.feature.use=\
         *   {0} is a preview feature and may be removed in a future release.
         */
        public static Warning PreviewFeatureUse(Fragment arg0) {
            return new Warning("compiler", "preview.feature.use", arg0);
        }
        
        /**
         * compiler.warn.preview.feature.use.classfile=\
         *   class file for {0} uses preview features of Java SE {1}.
         */
        public static Warning PreviewFeatureUseClassfile(JavaFileObject arg0, String arg1) {
            return new Warning("compiler", "preview.feature.use.classfile", arg0, arg1);
        }
        
        /**
         * compiler.warn.preview.feature.use.plural=\
         *   {0} are a preview feature and may be removed in a future release.
         */
        public static Warning PreviewFeatureUsePlural(JCDiagnostic arg0) {
            return new Warning("compiler", "preview.feature.use.plural", arg0);
        }
        
        /**
         * compiler.warn.preview.feature.use.plural=\
         *   {0} are a preview feature and may be removed in a future release.
         */
        public static Warning PreviewFeatureUsePlural(Fragment arg0) {
            return new Warning("compiler", "preview.feature.use.plural", arg0);
        }
        
        /**
         * compiler.warn.prob.found.req=\
         *    {0}\n\
         *    required: {2}\n\
         *    found:    {1}
         */
        public static Warning ProbFoundReq(JCDiagnostic arg0, Type arg1, Type arg2) {
            return new Warning("compiler", "prob.found.req", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.prob.found.req=\
         *    {0}\n\
         *    required: {2}\n\
         *    found:    {1}
         */
        public static Warning ProbFoundReq(Fragment arg0, Type arg1, Type arg2) {
            return new Warning("compiler", "prob.found.req", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.proc.annotations.without.processors=\
         *    No processor claimed any of these annotations: {0}
         */
        public static Warning ProcAnnotationsWithoutProcessors(Set<? extends String> arg0) {
            return new Warning("compiler", "proc.annotations.without.processors", arg0);
        }
        
        /**
         * compiler.warn.proc.duplicate.option.name=\
         *    Duplicate supported option ''{0}'' returned by annotation processor ''{1}''
         */
        public static Warning ProcDuplicateOptionName(String arg0, String arg1) {
            return new Warning("compiler", "proc.duplicate.option.name", arg0, arg1);
        }
        
        /**
         * compiler.warn.proc.duplicate.supported.annotation=\
         *    Duplicate supported annotation type ''{0}'' returned by annotation processor ''{1}''
         */
        public static Warning ProcDuplicateSupportedAnnotation(String arg0, String arg1) {
            return new Warning("compiler", "proc.duplicate.supported.annotation", arg0, arg1);
        }
        
        /**
         * compiler.warn.proc.file.create.last.round=\
         *    File for type ''{0}'' created in the last round will not be subject to annotation processing.
         */
        public static Warning ProcFileCreateLastRound(String arg0) {
            return new Warning("compiler", "proc.file.create.last.round", arg0);
        }
        
        /**
         * compiler.warn.proc.file.reopening=\
         *    Attempt to create a file for ''{0}'' multiple times
         */
        public static Warning ProcFileReopening(String arg0) {
            return new Warning("compiler", "proc.file.reopening", arg0);
        }
        
        /**
         * compiler.warn.proc.illegal.file.name=\
         *    Cannot create file for illegal name ''{0}''.
         */
        public static Warning ProcIllegalFileName(String arg0) {
            return new Warning("compiler", "proc.illegal.file.name", arg0);
        }
        
        /**
         * compiler.warn.proc.malformed.supported.string=\
         *    Malformed string ''{0}'' for a supported annotation type returned by processor ''{1}''
         */
        public static Warning ProcMalformedSupportedString(String arg0, String arg1) {
            return new Warning("compiler", "proc.malformed.supported.string", arg0, arg1);
        }
        
        /**
         * compiler.warn.proc.messager=\
         *    {0}
         */
        public static Warning ProcMessager(String arg0) {
            return new Warning("compiler", "proc.messager", arg0);
        }
        
        /**
         * compiler.warn.proc.package.does.not.exist=\
         *    package {0} does not exist
         */
        public static Warning ProcPackageDoesNotExist(String arg0) {
            return new Warning("compiler", "proc.package.does.not.exist", arg0);
        }
        
        /**
         * compiler.warn.proc.proc-only.requested.no.procs=\
         *    Annotation processing without compilation requested but no processors were found.
         */
        public static final Warning ProcProcOnlyRequestedNoProcs = new Warning("compiler", "proc.proc-only.requested.no.procs");
        
        /**
         * compiler.warn.proc.processor.incompatible.source.version=\
         *    Supported source version ''{0}'' from annotation processor ''{1}'' less than -source ''{2}''
         */
        public static Warning ProcProcessorIncompatibleSourceVersion(SourceVersion arg0, String arg1, String arg2) {
            return new Warning("compiler", "proc.processor.incompatible.source.version", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.proc.redundant.types.with.wildcard=\
         *    Annotation processor ''{0}'' redundantly supports both ''*'' and other annotation types
         */
        public static Warning ProcRedundantTypesWithWildcard(String arg0) {
            return new Warning("compiler", "proc.redundant.types.with.wildcard", arg0);
        }
        
        /**
         * compiler.warn.proc.suspicious.class.name=\
         *    Creating file for a type whose name ends in {1}: ''{0}''
         */
        public static Warning ProcSuspiciousClassName(String arg0, String arg1) {
            return new Warning("compiler", "proc.suspicious.class.name", arg0, arg1);
        }
        
        /**
         * compiler.warn.proc.type.already.exists=\
         *    A file for type ''{0}'' already exists on the sourcepath or classpath
         */
        public static Warning ProcTypeAlreadyExists(String arg0) {
            return new Warning("compiler", "proc.type.already.exists", arg0);
        }
        
        /**
         * compiler.warn.proc.type.recreate=\
         *    Attempt to create a file for type ''{0}'' multiple times
         */
        public static Warning ProcTypeRecreate(String arg0) {
            return new Warning("compiler", "proc.type.recreate", arg0);
        }
        
        /**
         * compiler.warn.proc.unclosed.type.files=\
         *    Unclosed files for the types ''{0}''; these types will not undergo annotation processing
         */
        public static Warning ProcUnclosedTypeFiles(Set<? extends String> arg0) {
            return new Warning("compiler", "proc.unclosed.type.files", arg0);
        }
        
        /**
         * compiler.warn.proc.unmatched.processor.options=\
         *    The following options were not recognized by any processor: ''{0}''
         */
        public static Warning ProcUnmatchedProcessorOptions(String arg0) {
            return new Warning("compiler", "proc.unmatched.processor.options", arg0);
        }
        
        /**
         * compiler.warn.proc.use.implicit=\
         *    Implicitly compiled files were not subject to annotation processing.\n\
         *    Use -implicit to specify a policy for implicit compilation.
         */
        public static final Warning ProcUseImplicit = new Warning("compiler", "proc.use.implicit");
        
        /**
         * compiler.warn.proc.use.proc.or.implicit=\
         *    Implicitly compiled files were not subject to annotation processing.\n\
         *    Use -proc:none to disable annotation processing or -implicit to specify a policy for implicit compilation.
         */
        public static final Warning ProcUseProcOrImplicit = new Warning("compiler", "proc.use.proc.or.implicit");
        
        /**
         * compiler.warn.profile.target.conflict=\
         *    profile {0} is not valid for target release {1}
         */
        public static Warning ProfileTargetConflict(Profile arg0, Target arg1) {
            return new Warning("compiler", "profile.target.conflict", arg0, arg1);
        }
        
        /**
         * compiler.warn.raw.class.use=\
         *    found raw type: {0}\n\
         *    missing type arguments for generic class {1}
         */
        public static Warning RawClassUse(Type arg0, Type arg1) {
            return new Warning("compiler", "raw.class.use", arg0, arg1);
        }
        
        /**
         * compiler.warn.redundant.cast=\
         *    redundant cast to {0}
         */
        public static Warning RedundantCast(Type arg0) {
            return new Warning("compiler", "redundant.cast", arg0);
        }
        
        /**
         * compiler.warn.requires.automatic=\
         *    requires directive for an automatic module
         */
        public static final Warning RequiresAutomatic = new Warning("compiler", "requires.automatic");
        
        /**
         * compiler.warn.requires.transitive.automatic=\
         *    requires transitive directive for an automatic module
         */
        public static final Warning RequiresTransitiveAutomatic = new Warning("compiler", "requires.transitive.automatic");
        
        /**
         * compiler.warn.restricted.type.not.allowed=\
         *    as of release {1}, ''{0}'' is a restricted type name and cannot be used for type declarations or as the element type of an array
         */
        public static Warning RestrictedTypeNotAllowed(Name arg0, Source arg1) {
            return new Warning("compiler", "restricted.type.not.allowed", arg0, arg1);
        }
        
        /**
         * compiler.warn.restricted.type.not.allowed.preview=\
         *    ''{0}'' may become a restricted type name in a future release and may be unusable for type declarations or as the element type of an array
         */
        public static Warning RestrictedTypeNotAllowedPreview(Name arg0, Source arg1) {
            return new Warning("compiler", "restricted.type.not.allowed.preview", arg0, arg1);
        }
        
        /**
         * compiler.warn.self.ref=\
         *    self-reference in initializer of variable ''{0}''
         */
        public static Warning SelfRef(Symbol arg0) {
            return new Warning("compiler", "self.ref", arg0);
        }
        
        /**
         * compiler.warn.service.provided.but.not.exported.or.used=\
         *    service interface provided but not exported or used
         */
        public static Warning ServiceProvidedButNotExportedOrUsed(Symbol arg0) {
            return new Warning("compiler", "service.provided.but.not.exported.or.used", arg0);
        }
        
        /**
         * compiler.warn.source.no.bootclasspath=\
         *    bootstrap class path not set in conjunction with -source {0}
         */
        public static Warning SourceNoBootclasspath(String arg0) {
            return new Warning("compiler", "source.no.bootclasspath", arg0);
        }
        
        /**
         * compiler.warn.source.no.system.modules.path=\
         *    system modules path not set in conjunction with -source {0}
         */
        public static Warning SourceNoSystemModulesPath(String arg0) {
            return new Warning("compiler", "source.no.system.modules.path", arg0);
        }
        
        /**
         * compiler.warn.source.target.conflict=\
         *    source release {0} requires target release {1}
         */
        public static Warning SourceTargetConflict(String arg0, Target arg1) {
            return new Warning("compiler", "source.target.conflict", arg0, arg1);
        }
        
        /**
         * compiler.warn.static.not.qualified.by.type=\
         *    static {0} should be qualified by type name, {1}, instead of by an expression
         */
        public static Warning StaticNotQualifiedByType(KindName arg0, Symbol arg1) {
            return new Warning("compiler", "static.not.qualified.by.type", arg0, arg1);
        }
        
        /**
         * compiler.warn.strictfp=\
         *    as of release 17, all floating-point expressions are evaluated strictly and ''strictfp'' is not required
         */
        public static final Warning Strictfp = new Warning("compiler", "strictfp");
        
        /**
         * compiler.warn.sun.proprietary=\
         *    {0} is internal proprietary API and may be removed in a future release
         */
        public static Warning SunProprietary(Symbol arg0) {
            return new Warning("compiler", "sun.proprietary", arg0);
        }
        
        /**
         * compiler.warn.target.default.source.conflict=\
         *    target release {0} conflicts with default source release {1}
         */
        public static Warning TargetDefaultSourceConflict(String arg0, Target arg1) {
            return new Warning("compiler", "target.default.source.conflict", arg0, arg1);
        }
        
        /**
         * compiler.warn.trailing.white.space.will.be.removed=\
         *    trailing white space will be removed
         */
        public static final Warning TrailingWhiteSpaceWillBeRemoved = new Warning("compiler", "trailing.white.space.will.be.removed");
        
        /**
         * compiler.warn.try.explicit.close.call=\
         *    explicit call to close() on an auto-closeable resource
         */
        public static final Warning TryExplicitCloseCall = new Warning("compiler", "try.explicit.close.call");
        
        /**
         * compiler.warn.try.resource.not.referenced=\
         *    auto-closeable resource {0} is never referenced in body of corresponding try statement
         */
        public static Warning TryResourceNotReferenced(Symbol arg0) {
            return new Warning("compiler", "try.resource.not.referenced", arg0);
        }
        
        /**
         * compiler.warn.try.resource.throws.interrupted.exc=\
         *    auto-closeable resource {0} has a member method close() that could throw InterruptedException
         */
        public static Warning TryResourceThrowsInterruptedExc(Type arg0) {
            return new Warning("compiler", "try.resource.throws.interrupted.exc", arg0);
        }
        
        /**
         * compiler.warn.unchecked.assign=\
         *    unchecked assignment: {0} to {1}
         */
        public static final Warning UncheckedAssign = new Warning("compiler", "unchecked.assign");
        
        /**
         * compiler.warn.unchecked.assign.to.var=\
         *    unchecked assignment to variable {0} as member of raw type {1}
         */
        public static Warning UncheckedAssignToVar(Symbol arg0, Type arg1) {
            return new Warning("compiler", "unchecked.assign.to.var", arg0, arg1);
        }
        
        /**
         * compiler.warn.unchecked.call.mbr.of.raw.type=\
         *    unchecked call to {0} as a member of the raw type {1}
         */
        public static Warning UncheckedCallMbrOfRawType(Symbol arg0, Type arg1) {
            return new Warning("compiler", "unchecked.call.mbr.of.raw.type", arg0, arg1);
        }
        
        /**
         * compiler.warn.unchecked.cast.to.type=\
         *    unchecked cast to type {0}
         */
        public static final Warning UncheckedCastToType = new Warning("compiler", "unchecked.cast.to.type");
        
        /**
         * compiler.warn.unchecked.generic.array.creation=\
         *    unchecked generic array creation for varargs parameter of type {0}
         */
        public static Warning UncheckedGenericArrayCreation(Type arg0) {
            return new Warning("compiler", "unchecked.generic.array.creation", arg0);
        }
        
        /**
         * compiler.warn.unchecked.meth.invocation.applied=\
         *    unchecked method invocation: {0} {1} in {4} {5} is applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}
         */
        public static Warning UncheckedMethInvocationApplied(KindName arg0, Name arg1, Object arg2, Object arg3, KindName arg4, Symbol arg5) {
            return new Warning("compiler", "unchecked.meth.invocation.applied", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.warn.unchecked.varargs.non.reifiable.type=\
         *    Possible heap pollution from parameterized vararg type {0}
         */
        public static Warning UncheckedVarargsNonReifiableType(Type arg0) {
            return new Warning("compiler", "unchecked.varargs.non.reifiable.type", arg0);
        }
        
        /**
         * compiler.warn.underscore.as.identifier=\
         *    as of release 9, ''_'' is a keyword, and may not be used as an identifier
         */
        public static final Warning UnderscoreAsIdentifier = new Warning("compiler", "underscore.as.identifier");
        
        /**
         * compiler.warn.unexpected.archive.file=\
         *    Unexpected extension for archive file: {0}
         */
        public static Warning UnexpectedArchiveFile(Path arg0) {
            return new Warning("compiler", "unexpected.archive.file", arg0);
        }
        
        /**
         * compiler.warn.unknown.enum.constant=\
         *    unknown enum constant {1}.{2}
         */
        public static Warning UnknownEnumConstant(JavaFileObject arg0, Symbol arg1, Name arg2) {
            return new Warning("compiler", "unknown.enum.constant", arg0, arg1, arg2);
        }
        
        /**
         * compiler.warn.unknown.enum.constant.reason=\
         *    unknown enum constant {1}.{2}\n\
         *    reason: {3}
         */
        public static Warning UnknownEnumConstantReason(JavaFileObject arg0, Symbol arg1, Name arg2, JCDiagnostic arg3) {
            return new Warning("compiler", "unknown.enum.constant.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.warn.unknown.enum.constant.reason=\
         *    unknown enum constant {1}.{2}\n\
         *    reason: {3}
         */
        public static Warning UnknownEnumConstantReason(JavaFileObject arg0, Symbol arg1, Name arg2, Fragment arg3) {
            return new Warning("compiler", "unknown.enum.constant.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.warn.unreachable.catch=\
         *    unreachable catch clause\n\
         *    thrown type {0} has already been caught
         */
        public static Warning UnreachableCatch(List<? extends Type> arg0) {
            return new Warning("compiler", "unreachable.catch", arg0);
        }
        
        /**
         * compiler.warn.unreachable.catch.1=\
         *    unreachable catch clause\n\
         *    thrown types {0} have already been caught
         */
        public static Warning UnreachableCatch1(List<? extends Type> arg0) {
            return new Warning("compiler", "unreachable.catch.1", arg0);
        }
        
        /**
         * compiler.warn.varargs.redundant.trustme.anno=\
         *    Redundant {0} annotation. {1}
         */
        public static Warning VarargsRedundantTrustmeAnno(Symbol arg0, JCDiagnostic arg1) {
            return new Warning("compiler", "varargs.redundant.trustme.anno", arg0, arg1);
        }
        
        /**
         * compiler.warn.varargs.redundant.trustme.anno=\
         *    Redundant {0} annotation. {1}
         */
        public static Warning VarargsRedundantTrustmeAnno(Symbol arg0, Fragment arg1) {
            return new Warning("compiler", "varargs.redundant.trustme.anno", arg0, arg1);
        }
        
        /**
         * compiler.warn.varargs.unsafe.use.varargs.param=\
         *    Varargs method could cause heap pollution from non-reifiable varargs parameter {0}
         */
        public static Warning VarargsUnsafeUseVarargsParam(Symbol arg0) {
            return new Warning("compiler", "varargs.unsafe.use.varargs.param", arg0);
        }
        
        /**
         * compiler.warn.warning=\
         *    warning:\u0020
         */
        public static final Warning Warning = new Warning("compiler", "warning");
    }
    public static class Notes {
        /**
         * compiler.note.compressed.diags=\
         *    Some messages have been simplified; recompile with -Xdiags:verbose to get full output
         */
        public static final Note CompressedDiags = new Note("compiler", "compressed.diags");
        
        /**
         * compiler.note.deferred.method.inst=\
         *    Deferred instantiation of method {0}\n\
         *    instantiated signature: {1}\n\
         *    target-type: {2}
         */
        public static Note DeferredMethodInst(Symbol arg0, Type arg1, Type arg2) {
            return new Note("compiler", "deferred.method.inst", arg0, arg1, arg2);
        }
        
        /**
         * compiler.note.deprecated.filename=\
         *    {0} uses or overrides a deprecated API.
         */
        public static Note DeprecatedFilename(File arg0) {
            return new Note("compiler", "deprecated.filename", arg0);
        }
        
        /**
         * compiler.note.deprecated.filename=\
         *    {0} uses or overrides a deprecated API.
         */
        public static Note DeprecatedFilename(JavaFileObject arg0) {
            return new Note("compiler", "deprecated.filename", arg0);
        }
        
        /**
         * compiler.note.deprecated.filename=\
         *    {0} uses or overrides a deprecated API.
         */
        public static Note DeprecatedFilename(Path arg0) {
            return new Note("compiler", "deprecated.filename", arg0);
        }
        
        /**
         * compiler.note.deprecated.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API.
         */
        public static Note DeprecatedFilenameAdditional(File arg0) {
            return new Note("compiler", "deprecated.filename.additional", arg0);
        }
        
        /**
         * compiler.note.deprecated.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API.
         */
        public static Note DeprecatedFilenameAdditional(JavaFileObject arg0) {
            return new Note("compiler", "deprecated.filename.additional", arg0);
        }
        
        /**
         * compiler.note.deprecated.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API.
         */
        public static Note DeprecatedFilenameAdditional(Path arg0) {
            return new Note("compiler", "deprecated.filename.additional", arg0);
        }
        
        /**
         * compiler.note.deprecated.plural=\
         *    Some input files use or override a deprecated API.
         */
        public static final Note DeprecatedPlural = new Note("compiler", "deprecated.plural");
        
        /**
         * compiler.note.deprecated.plural.additional=\
         *    Some input files additionally use or override a deprecated API.
         */
        public static final Note DeprecatedPluralAdditional = new Note("compiler", "deprecated.plural.additional");
        
        /**
         * compiler.note.deprecated.recompile=\
         *    Recompile with -Xlint:deprecation for details.
         */
        public static final Note DeprecatedRecompile = new Note("compiler", "deprecated.recompile");
        
        /**
         * compiler.note.lambda.stat=\
         *    Translating lambda expression\n\
         *    alternate metafactory = {0}\n\
         *    synthetic method = {1}
         */
        public static Note LambdaStat(boolean arg0, Symbol arg1) {
            return new Note("compiler", "lambda.stat", arg0, arg1);
        }
        
        /**
         * compiler.note.method.ref.search.results.multi=\
         *    {0} search results for {1}, with most specific {2}\n\
         *    applicable candidates:
         */
        public static Note MethodRefSearchResultsMulti(Fragment arg0, String arg1, int arg2) {
            return new Note("compiler", "method.ref.search.results.multi", arg0, arg1, arg2);
        }
        
        /**
         * compiler.note.mref.stat=\
         *    Translating method reference\n\
         *    alternate metafactory = {0}\n\
         */
        public static Note MrefStat(boolean arg0, Void arg1) {
            return new Note("compiler", "mref.stat", arg0, arg1);
        }
        
        /**
         * compiler.note.mref.stat.1=\
         *    Translating method reference\n\
         *    alternate metafactory = {0}\n\
         *    bridge method = {1}
         */
        public static Note MrefStat1(boolean arg0, Symbol arg1) {
            return new Note("compiler", "mref.stat.1", arg0, arg1);
        }
        
        /**
         * compiler.note.multiple.elements=\
         *    Multiple elements named ''{1}'' in modules ''{2}'' were found by javax.lang.model.util.Elements.{0}.
         */
        public static Note MultipleElements(String arg0, String arg1, String arg2) {
            return new Note("compiler", "multiple.elements", arg0, arg1, arg2);
        }
        
        /**
         * compiler.note.note=\
         *    Note:\u0020
         */
        public static final Note Note = new Note("compiler", "note");
        
        /**
         * compiler.note.preview.filename=\
         *    {0} uses preview features of Java SE {1}.
         */
        public static Note PreviewFilename(File arg0, Source arg1) {
            return new Note("compiler", "preview.filename", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.filename=\
         *    {0} uses preview features of Java SE {1}.
         */
        public static Note PreviewFilename(JavaFileObject arg0, Source arg1) {
            return new Note("compiler", "preview.filename", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.filename=\
         *    {0} uses preview features of Java SE {1}.
         */
        public static Note PreviewFilename(Path arg0, Source arg1) {
            return new Note("compiler", "preview.filename", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.filename.additional=\
         *    {0} has additional uses of preview features of Java SE {1}.
         */
        public static Note PreviewFilenameAdditional(File arg0, Source arg1) {
            return new Note("compiler", "preview.filename.additional", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.filename.additional=\
         *    {0} has additional uses of preview features of Java SE {1}.
         */
        public static Note PreviewFilenameAdditional(JavaFileObject arg0, Source arg1) {
            return new Note("compiler", "preview.filename.additional", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.filename.additional=\
         *    {0} has additional uses of preview features of Java SE {1}.
         */
        public static Note PreviewFilenameAdditional(Path arg0, Source arg1) {
            return new Note("compiler", "preview.filename.additional", arg0, arg1);
        }
        
        /**
         * compiler.note.preview.plural=\
         *    Some input files use preview features of Java SE {0}.
         */
        public static Note PreviewPlural(Source arg0) {
            return new Note("compiler", "preview.plural", arg0);
        }
        
        /**
         * compiler.note.preview.plural.additional=\
         *    Some input files additionally use preview features of Java SE {0}.
         */
        public static Note PreviewPluralAdditional(Source arg0) {
            return new Note("compiler", "preview.plural.additional", arg0);
        }
        
        /**
         * compiler.note.preview.recompile=\
         *    Recompile with -Xlint:preview for details.
         */
        public static final Note PreviewRecompile = new Note("compiler", "preview.recompile");
        
        /**
         * compiler.note.proc.messager=\
         *    {0}
         */
        public static Note ProcMessager(String arg0) {
            return new Note("compiler", "proc.messager", arg0);
        }
        
        /**
         * compiler.note.removal.filename=\
         *    {0} uses or overrides a deprecated API that is marked for removal.
         */
        public static Note RemovalFilename(File arg0) {
            return new Note("compiler", "removal.filename", arg0);
        }
        
        /**
         * compiler.note.removal.filename=\
         *    {0} uses or overrides a deprecated API that is marked for removal.
         */
        public static Note RemovalFilename(JavaFileObject arg0) {
            return new Note("compiler", "removal.filename", arg0);
        }
        
        /**
         * compiler.note.removal.filename=\
         *    {0} uses or overrides a deprecated API that is marked for removal.
         */
        public static Note RemovalFilename(Path arg0) {
            return new Note("compiler", "removal.filename", arg0);
        }
        
        /**
         * compiler.note.removal.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API that is marked for removal.
         */
        public static Note RemovalFilenameAdditional(File arg0) {
            return new Note("compiler", "removal.filename.additional", arg0);
        }
        
        /**
         * compiler.note.removal.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API that is marked for removal.
         */
        public static Note RemovalFilenameAdditional(JavaFileObject arg0) {
            return new Note("compiler", "removal.filename.additional", arg0);
        }
        
        /**
         * compiler.note.removal.filename.additional=\
         *    {0} has additional uses or overrides of a deprecated API that is marked for removal.
         */
        public static Note RemovalFilenameAdditional(Path arg0) {
            return new Note("compiler", "removal.filename.additional", arg0);
        }
        
        /**
         * compiler.note.removal.plural=\
         *    Some input files use or override a deprecated API that is marked for removal.
         */
        public static final Note RemovalPlural = new Note("compiler", "removal.plural");
        
        /**
         * compiler.note.removal.plural.additional=\
         *    Some input files additionally use or override a deprecated API that is marked for removal.
         */
        public static final Note RemovalPluralAdditional = new Note("compiler", "removal.plural.additional");
        
        /**
         * compiler.note.removal.recompile=\
         *    Recompile with -Xlint:removal for details.
         */
        public static final Note RemovalRecompile = new Note("compiler", "removal.recompile");
        
        /**
         * compiler.note.unchecked.filename=\
         *    {0} uses unchecked or unsafe operations.
         */
        public static Note UncheckedFilename(File arg0) {
            return new Note("compiler", "unchecked.filename", arg0);
        }
        
        /**
         * compiler.note.unchecked.filename=\
         *    {0} uses unchecked or unsafe operations.
         */
        public static Note UncheckedFilename(JavaFileObject arg0) {
            return new Note("compiler", "unchecked.filename", arg0);
        }
        
        /**
         * compiler.note.unchecked.filename=\
         *    {0} uses unchecked or unsafe operations.
         */
        public static Note UncheckedFilename(Path arg0) {
            return new Note("compiler", "unchecked.filename", arg0);
        }
        
        /**
         * compiler.note.unchecked.filename.additional=\
         *    {0} has additional unchecked or unsafe operations.
         */
        public static Note UncheckedFilenameAdditional(File arg0) {
            return new Note("compiler", "unchecked.filename.additional", arg0);
        }
        
        /**
         * compiler.note.unchecked.filename.additional=\
         *    {0} has additional unchecked or unsafe operations.
         */
        public static Note UncheckedFilenameAdditional(JavaFileObject arg0) {
            return new Note("compiler", "unchecked.filename.additional", arg0);
        }
        
        /**
         * compiler.note.unchecked.filename.additional=\
         *    {0} has additional unchecked or unsafe operations.
         */
        public static Note UncheckedFilenameAdditional(Path arg0) {
            return new Note("compiler", "unchecked.filename.additional", arg0);
        }
        
        /**
         * compiler.note.unchecked.plural=\
         *    Some input files use unchecked or unsafe operations.
         */
        public static final Note UncheckedPlural = new Note("compiler", "unchecked.plural");
        
        /**
         * compiler.note.unchecked.plural.additional=\
         *    Some input files additionally use unchecked or unsafe operations.
         */
        public static final Note UncheckedPluralAdditional = new Note("compiler", "unchecked.plural.additional");
        
        /**
         * compiler.note.unchecked.recompile=\
         *    Recompile with -Xlint:unchecked for details.
         */
        public static final Note UncheckedRecompile = new Note("compiler", "unchecked.recompile");
        
        /**
         * compiler.note.verbose.l2m.deduplicate=\
         *    deduplicating lambda implementation method {0}
         */
        public static Note VerboseL2mDeduplicate(Symbol arg0) {
            return new Note("compiler", "verbose.l2m.deduplicate", arg0);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, List<? extends Type> arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, List<? extends Type> arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, List<? extends Type> arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, JCDiagnostic arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, JCDiagnostic arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, JCDiagnostic arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, Fragment arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, Fragment arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi=\
         *    resolving method {0} in type {1} to candidate {2}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti(Name arg0, Symbol arg1, int arg2, String arg3, Fragment arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, List<? extends Type> arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, List<? extends Type> arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, List<? extends Type> arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, JCDiagnostic arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, JCDiagnostic arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, JCDiagnostic arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, Fragment arg4, List<? extends Type> arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, Fragment arg4, JCDiagnostic arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.note.verbose.resolve.multi.1=\
         *    erroneous resolution for method {0} in type {1}\n\
         *    phase: {3}\n\
         *    with actuals: {4}\n\
         *    with type-args: {5}\n\
         *    candidates:
         */
        public static Note VerboseResolveMulti1(Name arg0, Symbol arg1, Void arg2, String arg3, Fragment arg4, Fragment arg5) {
            return new Note("compiler", "verbose.resolve.multi.1", arg0, arg1, arg2, arg3, arg4, arg5);
        }
    }
    public static class Fragments {
        /**
         * compiler.misc.accessor.method.cant.throw.exception=\
         *    throws clause not allowed for accessor method
         */
        public static final Fragment AccessorMethodCantThrowException = new Fragment("compiler", "accessor.method.cant.throw.exception");
        
        /**
         * compiler.misc.accessor.method.must.not.be.generic=\
         *    accessor method must not be generic
         */
        public static final Fragment AccessorMethodMustNotBeGeneric = new Fragment("compiler", "accessor.method.must.not.be.generic");
        
        /**
         * compiler.misc.accessor.method.must.not.be.static=\
         *    accessor method must not be static
         */
        public static final Fragment AccessorMethodMustNotBeStatic = new Fragment("compiler", "accessor.method.must.not.be.static");
        
        /**
         * compiler.misc.accessor.return.type.doesnt.match=\
         *    return type of accessor method {0} must match the type of record component {1}
         */
        public static Fragment AccessorReturnTypeDoesntMatch(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "accessor.return.type.doesnt.match", arg0, arg1);
        }
        
        /**
         * compiler.misc.anachronistic.module.info=\
         *    module declaration found in version {0}.{1} classfile
         */
        public static Fragment AnachronisticModuleInfo(String arg0, String arg1) {
            return new Fragment("compiler", "anachronistic.module.info", arg0, arg1);
        }
        
        /**
         * compiler.misc.anonymous=\
         *    anonymous
         */
        public static final Fragment Anonymous = new Fragment("compiler", "anonymous");
        
        /**
         * compiler.misc.anonymous.class=\
         *    <anonymous {0}>
         */
        public static Fragment AnonymousClass(Name arg0) {
            return new Fragment("compiler", "anonymous.class", arg0);
        }
        
        /**
         * compiler.misc.applicable.method.found=\
         */
        public static Fragment ApplicableMethodFound(int arg0, Symbol arg1, Void arg2) {
            return new Fragment("compiler", "applicable.method.found", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.applicable.method.found.1=\
         *    ({2})
         */
        public static Fragment ApplicableMethodFound1(int arg0, Symbol arg1, JCDiagnostic arg2) {
            return new Fragment("compiler", "applicable.method.found.1", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.applicable.method.found.1=\
         *    ({2})
         */
        public static Fragment ApplicableMethodFound1(int arg0, Symbol arg1, Fragment arg2) {
            return new Fragment("compiler", "applicable.method.found.1", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.applicable.method.found.2=\
         */
        public static Fragment ApplicableMethodFound2(int arg0, Fragment arg1, Symbol arg2) {
            return new Fragment("compiler", "applicable.method.found.2", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.applicable.method.found.3=\
         *    ({3})
         */
        public static Fragment ApplicableMethodFound3(int arg0, Fragment arg1, Symbol arg2, JCDiagnostic arg3) {
            return new Fragment("compiler", "applicable.method.found.3", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.applicable.method.found.3=\
         *    ({3})
         */
        public static Fragment ApplicableMethodFound3(int arg0, Fragment arg1, Symbol arg2, Fragment arg3) {
            return new Fragment("compiler", "applicable.method.found.3", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.arg.length.mismatch=\
         *    actual and formal argument lists differ in length
         */
        public static final Fragment ArgLengthMismatch = new Fragment("compiler", "arg.length.mismatch");
        
        /**
         * compiler.misc.bad.class.file=\
         *    class file is invalid for class {0}
         */
        public static Fragment BadClassFile(Name arg0) {
            return new Fragment("compiler", "bad.class.file", arg0);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(File arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(File arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(JavaFileObject arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(JavaFileObject arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(Path arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.file.header=\
         *    bad class file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the classpath.
         */
        public static Fragment BadClassFileHeader(Path arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.class.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.class.signature=\
         *    bad class signature: {0}
         */
        public static final Fragment BadClassSignature = new Fragment("compiler", "bad.class.signature");
        
        /**
         * compiler.misc.bad.const.pool.entry=\
         *    bad constant pool entry in {0}\n\
         *    expected {1} at index {2}
         */
        public static Fragment BadConstPoolEntry(File arg0, String arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.entry", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.entry=\
         *    bad constant pool entry in {0}\n\
         *    expected {1} at index {2}
         */
        public static Fragment BadConstPoolEntry(JavaFileObject arg0, String arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.entry", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.entry=\
         *    bad constant pool entry in {0}\n\
         *    expected {1} at index {2}
         */
        public static Fragment BadConstPoolEntry(Path arg0, String arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.entry", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.index=\
         *    bad constant pool index in {0}\n\
         *    index {1} is not within pool size {2}.
         */
        public static Fragment BadConstPoolIndex(File arg0, int arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.index", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.index=\
         *    bad constant pool index in {0}\n\
         *    index {1} is not within pool size {2}.
         */
        public static Fragment BadConstPoolIndex(JavaFileObject arg0, int arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.index", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.index=\
         *    bad constant pool index in {0}\n\
         *    index {1} is not within pool size {2}.
         */
        public static Fragment BadConstPoolIndex(Path arg0, int arg1, int arg2) {
            return new Fragment("compiler", "bad.const.pool.index", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.const.pool.tag=\
         *    bad constant pool tag: {0}
         */
        public static final Fragment BadConstPoolTag = new Fragment("compiler", "bad.const.pool.tag");
        
        /**
         * compiler.misc.bad.const.pool.tag.at=\
         *    bad constant pool tag: {0} at {1}
         */
        public static final Fragment BadConstPoolTagAt = new Fragment("compiler", "bad.const.pool.tag.at");
        
        /**
         * compiler.misc.bad.constant.range=\
         *    constant value ''{0}'' for {1} is outside the expected range for {2}
         */
        public static Fragment BadConstantRange(String arg0, Symbol arg1, Type arg2) {
            return new Fragment("compiler", "bad.constant.range", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.constant.value=\
         *    bad constant value ''{0}'' for {1}, expected {2}
         */
        public static Fragment BadConstantValue(String arg0, Symbol arg1, String arg2) {
            return new Fragment("compiler", "bad.constant.value", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.bad.constant.value.type=\
         *    variable of type ''{0}'' cannot have a constant value, but has one specified
         */
        public static Fragment BadConstantValueType(Type arg0) {
            return new Fragment("compiler", "bad.constant.value.type", arg0);
        }
        
        /**
         * compiler.misc.bad.enclosing.class=\
         *    bad enclosing class for {0}: {1}
         */
        public static final Fragment BadEnclosingClass = new Fragment("compiler", "bad.enclosing.class");
        
        /**
         * compiler.misc.bad.enclosing.method=\
         *    bad enclosing method attribute for class {0}
         */
        public static Fragment BadEnclosingMethod(Symbol arg0) {
            return new Fragment("compiler", "bad.enclosing.method", arg0);
        }
        
        /**
         * compiler.misc.bad.instance.method.in.unbound.lookup=\
         *    unexpected instance {0} {1} found in unbound lookup
         */
        public static Fragment BadInstanceMethodInUnboundLookup(Kind arg0, Symbol arg1) {
            return new Fragment("compiler", "bad.instance.method.in.unbound.lookup", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.intersection.target.for.functional.expr=\
         *    bad intersection type target for lambda or method reference\n\
         *    {0}
         */
        public static Fragment BadIntersectionTargetForFunctionalExpr(JCDiagnostic arg0) {
            return new Fragment("compiler", "bad.intersection.target.for.functional.expr", arg0);
        }
        
        /**
         * compiler.misc.bad.intersection.target.for.functional.expr=\
         *    bad intersection type target for lambda or method reference\n\
         *    {0}
         */
        public static Fragment BadIntersectionTargetForFunctionalExpr(Fragment arg0) {
            return new Fragment("compiler", "bad.intersection.target.for.functional.expr", arg0);
        }
        
        /**
         * compiler.misc.bad.module-info.name=\
         *    bad class name
         */
        public static final Fragment BadModuleInfoName = new Fragment("compiler", "bad.module-info.name");
        
        /**
         * compiler.misc.bad.requires.flag=\
         *    bad requires flag: {0}
         */
        public static final Fragment BadRequiresFlag = new Fragment("compiler", "bad.requires.flag");
        
        /**
         * compiler.misc.bad.runtime.invisible.param.annotations=\
         *    bad RuntimeInvisibleParameterAnnotations attribute: {0}
         */
        public static final Fragment BadRuntimeInvisibleParamAnnotations = new Fragment("compiler", "bad.runtime.invisible.param.annotations");
        
        /**
         * compiler.misc.bad.signature=\
         *    bad signature: {0}
         */
        public static final Fragment BadSignature = new Fragment("compiler", "bad.signature");
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(File arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(File arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(JavaFileObject arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(JavaFileObject arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(Path arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.source.file.header=\
         *    bad source file: {0}\n\
         *    {1}\n\
         *    Please remove or make sure it appears in the correct subdirectory of the sourcepath.
         */
        public static Fragment BadSourceFileHeader(Path arg0, Fragment arg1) {
            return new Fragment("compiler", "bad.source.file.header", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.static.method.in.bound.lookup=\
         *    unexpected static {0} {1} found in bound lookup
         */
        public static Fragment BadStaticMethodInBoundLookup(Kind arg0, Symbol arg1) {
            return new Fragment("compiler", "bad.static.method.in.bound.lookup", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.static.method.in.unbound.lookup=\
         *    unexpected static {0} {1} found in unbound lookup
         */
        public static Fragment BadStaticMethodInUnboundLookup(Kind arg0, Symbol arg1) {
            return new Fragment("compiler", "bad.static.method.in.unbound.lookup", arg0, arg1);
        }
        
        /**
         * compiler.misc.bad.type.annotation.value=\
         *    bad type annotation target type value: {0}
         */
        public static final Fragment BadTypeAnnotationValue = new Fragment("compiler", "bad.type.annotation.value");
        
        /**
         * compiler.misc.base.membership=\
         *    all your base class are belong to us
         */
        public static final Fragment BaseMembership = new Fragment("compiler", "base.membership");
        
        /**
         * compiler.misc.bound=\
         *    bound
         */
        public static final Fragment Bound = new Fragment("compiler", "bound");
        
        /**
         * compiler.misc.canonical=\
         *    canonical
         */
        public static final Fragment Canonical = new Fragment("compiler", "canonical");
        
        /**
         * compiler.misc.canonical.cant.have.return.statement=\
         *    compact constructor must not have return statements
         */
        public static final Fragment CanonicalCantHaveReturnStatement = new Fragment("compiler", "canonical.cant.have.return.statement");
        
        /**
         * compiler.misc.canonical.must.not.contain.explicit.constructor.invocation=\
         *    canonical constructor must not contain explicit constructor invocation
         */
        public static final Fragment CanonicalMustNotContainExplicitConstructorInvocation = new Fragment("compiler", "canonical.must.not.contain.explicit.constructor.invocation");
        
        /**
         * compiler.misc.canonical.must.not.declare.type.variables=\
         *    canonical constructor must not declare type variables
         */
        public static final Fragment CanonicalMustNotDeclareTypeVariables = new Fragment("compiler", "canonical.must.not.declare.type.variables");
        
        /**
         * compiler.misc.canonical.must.not.have.stronger.access=\
         *    attempting to assign stronger access privileges; was {0}
         */
        public static Fragment CanonicalMustNotHaveStrongerAccess(Set<? extends Flag> arg0) {
            return new Fragment("compiler", "canonical.must.not.have.stronger.access", arg0);
        }
        
        /**
         * compiler.misc.canonical.must.not.have.stronger.access=\
         *    attempting to assign stronger access privileges; was {0}
         */
        public static Fragment CanonicalMustNotHaveStrongerAccess(String arg0) {
            return new Fragment("compiler", "canonical.must.not.have.stronger.access", arg0);
        }
        
        /**
         * compiler.misc.canonical.with.name.mismatch=\
         *    invalid parameter names in canonical constructor
         */
        public static final Fragment CanonicalWithNameMismatch = new Fragment("compiler", "canonical.with.name.mismatch");
        
        /**
         * compiler.misc.cant.access.inner.cls.constr=\
         *    cannot access constructor {0}({1})\n\
         *    an enclosing instance of type {2} is not in scope
         */
        public static Fragment CantAccessInnerClsConstr(Symbol arg0, List<? extends Type> arg1, Type arg2) {
            return new Fragment("compiler", "cant.access.inner.cls.constr", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(JCDiagnostic arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(JCDiagnostic arg0, Fragment arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(Fragment arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(Fragment arg0, Fragment arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(Type arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.diamond.1=\
         *    cannot infer type arguments for {0}\n\
         *    reason: {1}
         */
        public static Fragment CantApplyDiamond1(Type arg0, Fragment arg1) {
            return new Fragment("compiler", "cant.apply.diamond.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, List<? extends Type> arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, JCDiagnostic arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, List<? extends Type> arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, List<? extends Type> arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, JCDiagnostic arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, JCDiagnostic arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, Fragment arg3, Kind arg4, Type arg5, JCDiagnostic arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbol=\
         *    {0} {1} in {4} {5} cannot be applied to given types\n\
         *    required: {2}\n\
         *    found:    {3}\n\
         *    reason: {6}
         */
        public static Fragment CantApplySymbol(Kind arg0, Name arg1, Fragment arg2, Fragment arg3, Kind arg4, Type arg5, Fragment arg6) {
            return new Fragment("compiler", "cant.apply.symbol", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.cant.apply.symbols=\
         *    no suitable {0} found for {1}({2})
         */
        public static Fragment CantApplySymbols(Kind arg0, Name arg1, List<? extends Type> arg2) {
            return new Fragment("compiler", "cant.apply.symbols", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.cant.hide=\
         *    {0} in {1} cannot hide {2} in {3}
         */
        public static Fragment CantHide(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "cant.hide", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.cant.implement=\
         *    {0} in {1} cannot implement {2} in {3}
         */
        public static Fragment CantImplement(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "cant.implement", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.cant.override=\
         *    {0} in {1} cannot override {2} in {3}
         */
        public static Fragment CantOverride(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "cant.override", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.cant.resolve.args=\
         *    cannot find symbol\n\
         *    symbol: {0} {1}({3})
         */
        public static Fragment CantResolveArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3) {
            return new Fragment("compiler", "cant.resolve.args", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.cant.resolve.location.args=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}({3})\n\
         *    location: {4}
         */
        public static Fragment CantResolveLocationArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3, JCDiagnostic arg4) {
            return new Fragment("compiler", "cant.resolve.location.args", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.misc.cant.resolve.location.args=\
         *    cannot find symbol\n\
         *    symbol:   {0} {1}({3})\n\
         *    location: {4}
         */
        public static Fragment CantResolveLocationArgs(KindName arg0, Name arg1, Void arg2, List<? extends Type> arg3, Fragment arg4) {
            return new Fragment("compiler", "cant.resolve.location.args", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.misc.cant.resolve.location.args.params=\
         *    cannot find symbol\n\
         *    symbol:   {0} <{2}>{1}({3})\n\
         *    location: {4}
         */
        @SuppressWarnings("rawtypes")
        public static Fragment CantResolveLocationArgsParams(KindName arg0, Name arg1, List<? extends Type> arg2, List arg3, JCDiagnostic arg4) {
            return new Fragment("compiler", "cant.resolve.location.args.params", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.misc.cant.resolve.location.args.params=\
         *    cannot find symbol\n\
         *    symbol:   {0} <{2}>{1}({3})\n\
         *    location: {4}
         */
        @SuppressWarnings("rawtypes")
        public static Fragment CantResolveLocationArgsParams(KindName arg0, Name arg1, List<? extends Type> arg2, List arg3, Fragment arg4) {
            return new Fragment("compiler", "cant.resolve.location.args.params", arg0, arg1, arg2, arg3, arg4);
        }
        
        /**
         * compiler.misc.cant.resolve.modules=\
         *    cannot resolve modules
         */
        public static final Fragment CantResolveModules = new Fragment("compiler", "cant.resolve.modules");
        
        /**
         * compiler.misc.captured.type=\
         *    CAP#{0}
         */
        public static Fragment CapturedType(int arg0) {
            return new Fragment("compiler", "captured.type", arg0);
        }
        
        /**
         * compiler.misc.clashes.with=\
         *    {0} in {1} clashes with {2} in {3}
         */
        public static Fragment ClashesWith(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "clashes.with", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.class.file.not.found=\
         *    class file for {0} not found
         */
        public static Fragment ClassFileNotFound(Name arg0) {
            return new Fragment("compiler", "class.file.not.found", arg0);
        }
        
        /**
         * compiler.misc.class.file.wrong.class=\
         *    class file contains wrong class: {0}
         */
        public static final Fragment ClassFileWrongClass = new Fragment("compiler", "class.file.wrong.class");
        
        /**
         * compiler.misc.class.is.not.sealed=\
         *    {0} must be sealed
         */
        public static Fragment ClassIsNotSealed(String arg0) {
            return new Fragment("compiler", "class.is.not.sealed", arg0);
        }
        
        /**
         * compiler.misc.compact=\
         *    compact
         */
        public static final Fragment Compact = new Fragment("compiler", "compact");
        
        /**
         * compiler.misc.conditional.target.cant.be.void=\
         *    target-type for conditional expression cannot be void
         */
        public static final Fragment ConditionalTargetCantBeVoid = new Fragment("compiler", "conditional.target.cant.be.void");
        
        /**
         * compiler.misc.count.error=\
         *    {0} error
         */
        public static Fragment CountError(int arg0) {
            return new Fragment("compiler", "count.error", arg0);
        }
        
        /**
         * compiler.misc.count.error.plural=\
         *    {0} errors
         */
        public static Fragment CountErrorPlural(int arg0) {
            return new Fragment("compiler", "count.error.plural", arg0);
        }
        
        /**
         * compiler.misc.count.error.recompile=\
         *    only showing the first {0} errors, of {1} total; use -Xmaxerrs if you would like to see more
         */
        public static Fragment CountErrorRecompile(int arg0, int arg1) {
            return new Fragment("compiler", "count.error.recompile", arg0, arg1);
        }
        
        /**
         * compiler.misc.count.warn=\
         *    {0} warning
         */
        public static Fragment CountWarn(int arg0) {
            return new Fragment("compiler", "count.warn", arg0);
        }
        
        /**
         * compiler.misc.count.warn.plural=\
         *    {0} warnings
         */
        public static Fragment CountWarnPlural(int arg0) {
            return new Fragment("compiler", "count.warn.plural", arg0);
        }
        
        /**
         * compiler.misc.count.warn.recompile=\
         *    only showing the first {0} warnings, of {1} total; use -Xmaxwarns if you would like to see more
         */
        public static Fragment CountWarnRecompile(int arg0, int arg1) {
            return new Fragment("compiler", "count.warn.recompile", arg0, arg1);
        }
        
        /**
         * compiler.misc.descriptor=\
         *    descriptor: {2} {0}({1})
         */
        public static Fragment Descriptor(Name arg0, List<? extends Type> arg1, Type arg2, List<? extends Type> arg3) {
            return new Fragment("compiler", "descriptor", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.descriptor.throws=\
         *    descriptor: {2} {0}({1}) throws {3}
         */
        public static Fragment DescriptorThrows(Name arg0, List<? extends Type> arg1, Type arg2, List<? extends Type> arg3) {
            return new Fragment("compiler", "descriptor.throws", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.diamond=\
         *    {0}<>
         */
        public static Fragment Diamond(Symbol arg0) {
            return new Fragment("compiler", "diamond", arg0);
        }
        
        /**
         * compiler.misc.diamond.and.explicit.params=\
         *    cannot use ''<>'' with explicit type parameters for constructor
         */
        public static Fragment DiamondAndExplicitParams(Type arg0) {
            return new Fragment("compiler", "diamond.and.explicit.params", arg0);
        }
        
        /**
         * compiler.misc.diamond.anonymous.methods.implicitly.override=\
         *    (due to <>, every non-private method declared in this anonymous class must override or implement a method from a supertype)
         */
        public static final Fragment DiamondAnonymousMethodsImplicitlyOverride = new Fragment("compiler", "diamond.anonymous.methods.implicitly.override");
        
        /**
         * compiler.misc.diamond.invalid.arg=\
         *    type argument {0} inferred for {1} is not allowed in this context\n\
         *    inferred argument is not expressible in the Signature attribute
         */
        public static Fragment DiamondInvalidArg(List<? extends Type> arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "diamond.invalid.arg", arg0, arg1);
        }
        
        /**
         * compiler.misc.diamond.invalid.arg=\
         *    type argument {0} inferred for {1} is not allowed in this context\n\
         *    inferred argument is not expressible in the Signature attribute
         */
        public static Fragment DiamondInvalidArg(List<? extends Type> arg0, Fragment arg1) {
            return new Fragment("compiler", "diamond.invalid.arg", arg0, arg1);
        }
        
        /**
         * compiler.misc.diamond.invalid.args=\
         *    type arguments {0} inferred for {1} are not allowed in this context\n\
         *    inferred arguments are not expressible in the Signature attribute
         */
        public static Fragment DiamondInvalidArgs(List<? extends Type> arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "diamond.invalid.args", arg0, arg1);
        }
        
        /**
         * compiler.misc.diamond.invalid.args=\
         *    type arguments {0} inferred for {1} are not allowed in this context\n\
         *    inferred arguments are not expressible in the Signature attribute
         */
        public static Fragment DiamondInvalidArgs(List<? extends Type> arg0, Fragment arg1) {
            return new Fragment("compiler", "diamond.invalid.args", arg0, arg1);
        }
        
        /**
         * compiler.misc.diamond.non.generic=\
         *    cannot use ''<>'' with non-generic class {0}
         */
        public static Fragment DiamondNonGeneric(Type arg0) {
            return new Fragment("compiler", "diamond.non.generic", arg0);
        }
        
        /**
         * compiler.misc.doesnt.extend.sealed=\
         *    subclass {0} must extend sealed class
         */
        public static Fragment DoesntExtendSealed(Type arg0) {
            return new Fragment("compiler", "doesnt.extend.sealed", arg0);
        }
        
        /**
         * compiler.misc.eq.bounds=\
         *        equality constraints: {0}
         */
        public static Fragment EqBounds(List<? extends Type> arg0) {
            return new Fragment("compiler", "eq.bounds", arg0);
        }
        
        /**
         * compiler.misc.exception.message=\
         *    {0}
         */
        public static Fragment ExceptionMessage(String arg0) {
            return new Fragment("compiler", "exception.message", arg0);
        }
        
        /**
         * compiler.misc.explicit.param.do.not.conform.to.bounds=\
         *    explicit type argument {0} does not conform to declared bound(s) {1}
         */
        public static Fragment ExplicitParamDoNotConformToBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "explicit.param.do.not.conform.to.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.fatal.err.cant.close=\
         *    Fatal Error: Cannot close compiler resources
         */
        public static final Fragment FatalErrCantClose = new Fragment("compiler", "fatal.err.cant.close");
        
        /**
         * compiler.misc.fatal.err.cant.locate.ctor=\
         *    Fatal Error: Unable to find constructor for {0}
         */
        public static Fragment FatalErrCantLocateCtor(Type arg0) {
            return new Fragment("compiler", "fatal.err.cant.locate.ctor", arg0);
        }
        
        /**
         * compiler.misc.fatal.err.cant.locate.field=\
         *    Fatal Error: Unable to find field {0}
         */
        public static Fragment FatalErrCantLocateField(Name arg0) {
            return new Fragment("compiler", "fatal.err.cant.locate.field", arg0);
        }
        
        /**
         * compiler.misc.fatal.err.cant.locate.meth=\
         *    Fatal Error: Unable to find method {0}
         */
        public static Fragment FatalErrCantLocateMeth(Name arg0) {
            return new Fragment("compiler", "fatal.err.cant.locate.meth", arg0);
        }
        
        /**
         * compiler.misc.fatal.err.no.java.lang=\
         *    Fatal Error: Unable to find package java.lang in classpath or bootclasspath
         */
        public static final Fragment FatalErrNoJavaLang = new Fragment("compiler", "fatal.err.no.java.lang");
        
        /**
         * compiler.misc.feature.annotations.after.type.params=\
         *    annotations after method type parameters
         */
        public static final Fragment FeatureAnnotationsAfterTypeParams = new Fragment("compiler", "feature.annotations.after.type.params");
        
        /**
         * compiler.misc.feature.case.null=\
         *    null in switch cases
         */
        public static final Fragment FeatureCaseNull = new Fragment("compiler", "feature.case.null");
        
        /**
         * compiler.misc.feature.default.methods=\
         *    default methods
         */
        public static final Fragment FeatureDefaultMethods = new Fragment("compiler", "feature.default.methods");
        
        /**
         * compiler.misc.feature.diamond=\
         *    diamond operator
         */
        public static final Fragment FeatureDiamond = new Fragment("compiler", "feature.diamond");
        
        /**
         * compiler.misc.feature.diamond.and.anon.class=\
         *    ''<>'' with anonymous inner classes
         */
        public static final Fragment FeatureDiamondAndAnonClass = new Fragment("compiler", "feature.diamond.and.anon.class");
        
        /**
         * compiler.misc.feature.intersection.types.in.cast=\
         *    intersection types
         */
        public static final Fragment FeatureIntersectionTypesInCast = new Fragment("compiler", "feature.intersection.types.in.cast");
        
        /**
         * compiler.misc.feature.lambda=\
         *    lambda expressions
         */
        public static final Fragment FeatureLambda = new Fragment("compiler", "feature.lambda");
        
        /**
         * compiler.misc.feature.method.references=\
         *    method references
         */
        public static final Fragment FeatureMethodReferences = new Fragment("compiler", "feature.method.references");
        
        /**
         * compiler.misc.feature.modules=\
         *    modules
         */
        public static final Fragment FeatureModules = new Fragment("compiler", "feature.modules");
        
        /**
         * compiler.misc.feature.multiple.case.labels=\
         *    multiple case labels
         */
        public static final Fragment FeatureMultipleCaseLabels = new Fragment("compiler", "feature.multiple.case.labels");
        
        /**
         * compiler.misc.feature.not.supported.in.source=\
         *   {0} is not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Fragment FeatureNotSupportedInSource(JCDiagnostic arg0, String arg1, String arg2) {
            return new Fragment("compiler", "feature.not.supported.in.source", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.feature.not.supported.in.source=\
         *   {0} is not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Fragment FeatureNotSupportedInSource(Fragment arg0, String arg1, String arg2) {
            return new Fragment("compiler", "feature.not.supported.in.source", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.feature.not.supported.in.source.plural=\
         *   {0} are not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Fragment FeatureNotSupportedInSourcePlural(JCDiagnostic arg0, String arg1, String arg2) {
            return new Fragment("compiler", "feature.not.supported.in.source.plural", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.feature.not.supported.in.source.plural=\
         *   {0} are not supported in -source {1}\n\
         *    (use -source {2} or higher to enable {0})
         */
        public static Fragment FeatureNotSupportedInSourcePlural(Fragment arg0, String arg1, String arg2) {
            return new Fragment("compiler", "feature.not.supported.in.source.plural", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.feature.pattern.matching.instanceof=\
         *    pattern matching in instanceof
         */
        public static final Fragment FeaturePatternMatchingInstanceof = new Fragment("compiler", "feature.pattern.matching.instanceof");
        
        /**
         * compiler.misc.feature.pattern.switch=\
         *    patterns in switch statements
         */
        public static final Fragment FeaturePatternSwitch = new Fragment("compiler", "feature.pattern.switch");
        
        /**
         * compiler.misc.feature.private.intf.methods=\
         *    private interface methods
         */
        public static final Fragment FeaturePrivateIntfMethods = new Fragment("compiler", "feature.private.intf.methods");
        
        /**
         * compiler.misc.feature.records=\
         *    records
         */
        public static final Fragment FeatureRecords = new Fragment("compiler", "feature.records");
        
        /**
         * compiler.misc.feature.reifiable.types.instanceof=\
         *    reifiable types in instanceof
         */
        public static final Fragment FeatureReifiableTypesInstanceof = new Fragment("compiler", "feature.reifiable.types.instanceof");
        
        /**
         * compiler.misc.feature.repeatable.annotations=\
         *    repeated annotations
         */
        public static final Fragment FeatureRepeatableAnnotations = new Fragment("compiler", "feature.repeatable.annotations");
        
        /**
         * compiler.misc.feature.sealed.classes=\
         *    sealed classes
         */
        public static final Fragment FeatureSealedClasses = new Fragment("compiler", "feature.sealed.classes");
        
        /**
         * compiler.misc.feature.static.intf.method.invoke=\
         *    static interface method invocations
         */
        public static final Fragment FeatureStaticIntfMethodInvoke = new Fragment("compiler", "feature.static.intf.method.invoke");
        
        /**
         * compiler.misc.feature.static.intf.methods=\
         *    static interface methods
         */
        public static final Fragment FeatureStaticIntfMethods = new Fragment("compiler", "feature.static.intf.methods");
        
        /**
         * compiler.misc.feature.switch.expressions=\
         *    switch expressions
         */
        public static final Fragment FeatureSwitchExpressions = new Fragment("compiler", "feature.switch.expressions");
        
        /**
         * compiler.misc.feature.switch.rules=\
         *    switch rules
         */
        public static final Fragment FeatureSwitchRules = new Fragment("compiler", "feature.switch.rules");
        
        /**
         * compiler.misc.feature.text.blocks=\
         *    text blocks
         */
        public static final Fragment FeatureTextBlocks = new Fragment("compiler", "feature.text.blocks");
        
        /**
         * compiler.misc.feature.type.annotations=\
         *    type annotations
         */
        public static final Fragment FeatureTypeAnnotations = new Fragment("compiler", "feature.type.annotations");
        
        /**
         * compiler.misc.feature.var.in.try.with.resources=\
         *    variables in try-with-resources
         */
        public static final Fragment FeatureVarInTryWithResources = new Fragment("compiler", "feature.var.in.try.with.resources");
        
        /**
         * compiler.misc.feature.var.syntax.in.implicit.lambda=\
         *    var syntax in implicit lambdas
         */
        public static final Fragment FeatureVarSyntaxInImplicitLambda = new Fragment("compiler", "feature.var.syntax.in.implicit.lambda");
        
        /**
         * compiler.misc.file.does.not.contain.module=\
         *    file does not contain module declaration
         */
        public static final Fragment FileDoesNotContainModule = new Fragment("compiler", "file.does.not.contain.module");
        
        /**
         * compiler.misc.file.does.not.contain.package=\
         *    file does not contain package {0}
         */
        public static Fragment FileDoesNotContainPackage(Symbol arg0) {
            return new Fragment("compiler", "file.does.not.contain.package", arg0);
        }
        
        /**
         * compiler.misc.file.doesnt.contain.class=\
         *    file does not contain class {0}
         */
        public static Fragment FileDoesntContainClass(Name arg0) {
            return new Fragment("compiler", "file.doesnt.contain.class", arg0);
        }
        
        /**
         * compiler.misc.guard=\
         *    a guard
         */
        public static final Fragment Guard = new Fragment("compiler", "guard");
        
        /**
         * compiler.misc.illegal.signature=\
         *    illegal signature attribute for type {1}
         */
        public static Fragment IllegalSignature(Symbol arg0, Type arg1) {
            return new Fragment("compiler", "illegal.signature", arg0, arg1);
        }
        
        /**
         * compiler.misc.illegal.start.of.class.file=\
         *    illegal start of class file
         */
        public static final Fragment IllegalStartOfClassFile = new Fragment("compiler", "illegal.start.of.class.file");
        
        /**
         * compiler.misc.implicit.and.explicit.not.allowed=\
         *    cannot mix implicitly-typed and explicitly-typed parameters
         */
        public static final Fragment ImplicitAndExplicitNotAllowed = new Fragment("compiler", "implicit.and.explicit.not.allowed");
        
        /**
         * compiler.misc.inaccessible.varargs.type=\
         *    formal varargs element type {0} is not accessible from {1} {2}
         */
        public static Fragment InaccessibleVarargsType(Type arg0, Kind arg1, Symbol arg2) {
            return new Fragment("compiler", "inaccessible.varargs.type", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.inapplicable.method=\
         *    {0} {1}.{2} is not applicable\n\
         *    ({3})
         */
        public static Fragment InapplicableMethod(KindName arg0, Symbol arg1, Symbol arg2, JCDiagnostic arg3) {
            return new Fragment("compiler", "inapplicable.method", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.inapplicable.method=\
         *    {0} {1}.{2} is not applicable\n\
         *    ({3})
         */
        public static Fragment InapplicableMethod(KindName arg0, Symbol arg1, Symbol arg2, Fragment arg3) {
            return new Fragment("compiler", "inapplicable.method", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.incompatible.abstract.default=\
         *    {0} {1} inherits abstract and default for {2}({3}) from types {4} and {5}
         */
        public static Fragment IncompatibleAbstractDefault(KindName arg0, Type arg1, Name arg2, List<? extends Type> arg3, Symbol arg4, Symbol arg5) {
            return new Fragment("compiler", "incompatible.abstract.default", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.misc.incompatible.abstracts=\
         *    multiple non-overriding abstract methods found in {0} {1}
         */
        public static Fragment IncompatibleAbstracts(KindName arg0, Symbol arg1) {
            return new Fragment("compiler", "incompatible.abstracts", arg0, arg1);
        }
        
        /**
         * compiler.misc.incompatible.arg.types.in.lambda=\
         *    incompatible parameter types in lambda expression
         */
        public static final Fragment IncompatibleArgTypesInLambda = new Fragment("compiler", "incompatible.arg.types.in.lambda");
        
        /**
         * compiler.misc.incompatible.arg.types.in.mref=\
         *    incompatible parameter types in method reference
         */
        public static final Fragment IncompatibleArgTypesInMref = new Fragment("compiler", "incompatible.arg.types.in.mref");
        
        /**
         * compiler.misc.incompatible.bounds=\
         *    inference variable {0} has incompatible bounds\n\
         *    {1}\n\
         *    {2}
         */
        public static Fragment IncompatibleBounds(Type arg0, Fragment arg1, Fragment arg2) {
            return new Fragment("compiler", "incompatible.bounds", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.incompatible.descs.in.functional.intf=\
         *    incompatible function descriptors found in {0} {1}
         */
        public static Fragment IncompatibleDescsInFunctionalIntf(KindName arg0, Symbol arg1) {
            return new Fragment("compiler", "incompatible.descs.in.functional.intf", arg0, arg1);
        }
        
        /**
         * compiler.misc.incompatible.diff.ret=\
         *    both define {0}({1}), but with unrelated return types
         */
        public static Fragment IncompatibleDiffRet(Name arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "incompatible.diff.ret", arg0, arg1);
        }
        
        /**
         * compiler.misc.incompatible.eq.bounds=\
         *    inference variable {0} has incompatible equality constraints {1}
         */
        public static Fragment IncompatibleEqBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "incompatible.eq.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.incompatible.ret.type.in.lambda=\
         *    bad return type in lambda expression\n\
         *    {0}
         */
        public static Fragment IncompatibleRetTypeInLambda(JCDiagnostic arg0) {
            return new Fragment("compiler", "incompatible.ret.type.in.lambda", arg0);
        }
        
        /**
         * compiler.misc.incompatible.ret.type.in.lambda=\
         *    bad return type in lambda expression\n\
         *    {0}
         */
        public static Fragment IncompatibleRetTypeInLambda(Fragment arg0) {
            return new Fragment("compiler", "incompatible.ret.type.in.lambda", arg0);
        }
        
        /**
         * compiler.misc.incompatible.ret.type.in.mref=\
         *    bad return type in method reference\n\
         *    {0}
         */
        public static Fragment IncompatibleRetTypeInMref(JCDiagnostic arg0) {
            return new Fragment("compiler", "incompatible.ret.type.in.mref", arg0);
        }
        
        /**
         * compiler.misc.incompatible.ret.type.in.mref=\
         *    bad return type in method reference\n\
         *    {0}
         */
        public static Fragment IncompatibleRetTypeInMref(Fragment arg0) {
            return new Fragment("compiler", "incompatible.ret.type.in.mref", arg0);
        }
        
        /**
         * compiler.misc.incompatible.type.in.conditional=\
         *    bad type in conditional expression\n\
         *    {0}
         */
        public static Fragment IncompatibleTypeInConditional(JCDiagnostic arg0) {
            return new Fragment("compiler", "incompatible.type.in.conditional", arg0);
        }
        
        /**
         * compiler.misc.incompatible.type.in.conditional=\
         *    bad type in conditional expression\n\
         *    {0}
         */
        public static Fragment IncompatibleTypeInConditional(Fragment arg0) {
            return new Fragment("compiler", "incompatible.type.in.conditional", arg0);
        }
        
        /**
         * compiler.misc.incompatible.type.in.switch.expression=\
         *    bad type in switch expression\n\
         *    {0}
         */
        public static Fragment IncompatibleTypeInSwitchExpression(JCDiagnostic arg0) {
            return new Fragment("compiler", "incompatible.type.in.switch.expression", arg0);
        }
        
        /**
         * compiler.misc.incompatible.type.in.switch.expression=\
         *    bad type in switch expression\n\
         *    {0}
         */
        public static Fragment IncompatibleTypeInSwitchExpression(Fragment arg0) {
            return new Fragment("compiler", "incompatible.type.in.switch.expression", arg0);
        }
        
        /**
         * compiler.misc.incompatible.unrelated.defaults=\
         *    {0} {1} inherits unrelated defaults for {2}({3}) from types {4} and {5}
         */
        public static Fragment IncompatibleUnrelatedDefaults(KindName arg0, Type arg1, Name arg2, List<? extends Type> arg3, Symbol arg4, Symbol arg5) {
            return new Fragment("compiler", "incompatible.unrelated.defaults", arg0, arg1, arg2, arg3, arg4, arg5);
        }
        
        /**
         * compiler.misc.incompatible.upper.bounds=\
         *    inference variable {0} has incompatible upper bounds {1}
         */
        public static Fragment IncompatibleUpperBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "incompatible.upper.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.inconvertible.types=\
         *    {0} cannot be converted to {1}
         */
        public static Fragment InconvertibleTypes(Type arg0, Type arg1) {
            return new Fragment("compiler", "inconvertible.types", arg0, arg1);
        }
        
        /**
         * compiler.misc.infer.arg.length.mismatch=\
         *    cannot infer type-variable(s) {0}\n\
         *    (actual and formal argument lists differ in length)
         */
        public static Fragment InferArgLengthMismatch(List<? extends Type> arg0) {
            return new Fragment("compiler", "infer.arg.length.mismatch", arg0);
        }
        
        /**
         * compiler.misc.infer.no.conforming.assignment.exists=\
         *    cannot infer type-variable(s) {0}\n\
         *    (argument mismatch; {1})
         */
        public static Fragment InferNoConformingAssignmentExists(List<? extends Type> arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "infer.no.conforming.assignment.exists", arg0, arg1);
        }
        
        /**
         * compiler.misc.infer.no.conforming.assignment.exists=\
         *    cannot infer type-variable(s) {0}\n\
         *    (argument mismatch; {1})
         */
        public static Fragment InferNoConformingAssignmentExists(List<? extends Type> arg0, Fragment arg1) {
            return new Fragment("compiler", "infer.no.conforming.assignment.exists", arg0, arg1);
        }
        
        /**
         * compiler.misc.infer.no.conforming.instance.exists=\
         *    no instance(s) of type variable(s) {0} exist so that {1} conforms to {2}
         */
        public static Fragment InferNoConformingInstanceExists(List<? extends Type> arg0, Type arg1, Type arg2) {
            return new Fragment("compiler", "infer.no.conforming.instance.exists", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.infer.varargs.argument.mismatch=\
         *    cannot infer type-variable(s) {0}\n\
         *    (varargs mismatch; {1})
         */
        public static Fragment InferVarargsArgumentMismatch(List<? extends Type> arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "infer.varargs.argument.mismatch", arg0, arg1);
        }
        
        /**
         * compiler.misc.infer.varargs.argument.mismatch=\
         *    cannot infer type-variable(s) {0}\n\
         *    (varargs mismatch; {1})
         */
        public static Fragment InferVarargsArgumentMismatch(List<? extends Type> arg0, Fragment arg1) {
            return new Fragment("compiler", "infer.varargs.argument.mismatch", arg0, arg1);
        }
        
        /**
         * compiler.misc.inferred.do.not.conform.to.eq.bounds=\
         *    inferred type does not conform to equality constraint(s)\n\
         *    inferred: {0}\n\
         *    equality constraints(s): {1}
         */
        public static Fragment InferredDoNotConformToEqBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "inferred.do.not.conform.to.eq.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.inferred.do.not.conform.to.lower.bounds=\
         *    inferred type does not conform to lower bound(s)\n\
         *    inferred: {0}\n\
         *    lower bound(s): {1}
         */
        public static Fragment InferredDoNotConformToLowerBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "inferred.do.not.conform.to.lower.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.inferred.do.not.conform.to.upper.bounds=\
         *    inferred type does not conform to upper bound(s)\n\
         *    inferred: {0}\n\
         *    upper bound(s): {1}
         */
        public static Fragment InferredDoNotConformToUpperBounds(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "inferred.do.not.conform.to.upper.bounds", arg0, arg1);
        }
        
        /**
         * compiler.misc.inner.cls=\
         *    an inner class
         */
        public static final Fragment InnerCls = new Fragment("compiler", "inner.cls");
        
        /**
         * compiler.misc.intersection.type=\
         *    INT#{0}
         */
        public static Fragment IntersectionType(int arg0) {
            return new Fragment("compiler", "intersection.type", arg0);
        }
        
        /**
         * compiler.misc.invalid.default.interface=\
         *    default method found in version {0}.{1} classfile
         */
        public static Fragment InvalidDefaultInterface(String arg0, String arg1) {
            return new Fragment("compiler", "invalid.default.interface", arg0, arg1);
        }
        
        /**
         * compiler.misc.invalid.generic.lambda.target=\
         *    invalid functional descriptor for lambda expression\n\
         *    method {0} in {1} {2} is generic
         */
        public static Fragment InvalidGenericLambdaTarget(Type arg0, KindName arg1, Symbol arg2) {
            return new Fragment("compiler", "invalid.generic.lambda.target", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.invalid.mref=\
         *    invalid {0} reference\n\
         *    {1}
         */
        public static Fragment InvalidMref(KindName arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "invalid.mref", arg0, arg1);
        }
        
        /**
         * compiler.misc.invalid.mref=\
         *    invalid {0} reference\n\
         *    {1}
         */
        public static Fragment InvalidMref(KindName arg0, Fragment arg1) {
            return new Fragment("compiler", "invalid.mref", arg0, arg1);
        }
        
        /**
         * compiler.misc.invalid.static.interface=\
         *    static method found in version {0}.{1} classfile
         */
        public static Fragment InvalidStaticInterface(String arg0, String arg1) {
            return new Fragment("compiler", "invalid.static.interface", arg0, arg1);
        }
        
        /**
         * compiler.misc.is.a.type.variable=\
         *    must not include type variables: {0}
         */
        public static Fragment IsATypeVariable(Type arg0) {
            return new Fragment("compiler", "is.a.type.variable", arg0);
        }
        
        /**
         * compiler.misc.is.duplicated=\
         *    must not contain duplicates: {0}
         */
        public static Fragment IsDuplicated(Type arg0) {
            return new Fragment("compiler", "is.duplicated", arg0);
        }
        
        /**
         * compiler.misc.kindname.annotation=\
         *    @interface
         */
        public static final Fragment KindnameAnnotation = new Fragment("compiler", "kindname.annotation");
        
        /**
         * compiler.misc.kindname.class=\
         *    class
         */
        public static final Fragment KindnameClass = new Fragment("compiler", "kindname.class");
        
        /**
         * compiler.misc.kindname.constructor=\
         *    constructor
         */
        public static final Fragment KindnameConstructor = new Fragment("compiler", "kindname.constructor");
        
        /**
         * compiler.misc.kindname.enum=\
         *    enum
         */
        public static final Fragment KindnameEnum = new Fragment("compiler", "kindname.enum");
        
        /**
         * compiler.misc.kindname.instance.init=\
         *    instance initializer
         */
        public static final Fragment KindnameInstanceInit = new Fragment("compiler", "kindname.instance.init");
        
        /**
         * compiler.misc.kindname.interface=\
         *    interface
         */
        public static final Fragment KindnameInterface = new Fragment("compiler", "kindname.interface");
        
        /**
         * compiler.misc.kindname.method=\
         *    method
         */
        public static final Fragment KindnameMethod = new Fragment("compiler", "kindname.method");
        
        /**
         * compiler.misc.kindname.module=\
         *    module
         */
        public static final Fragment KindnameModule = new Fragment("compiler", "kindname.module");
        
        /**
         * compiler.misc.kindname.package=\
         *    package
         */
        public static final Fragment KindnamePackage = new Fragment("compiler", "kindname.package");
        
        /**
         * compiler.misc.kindname.record=\
         *    record
         */
        public static final Fragment KindnameRecord = new Fragment("compiler", "kindname.record");
        
        /**
         * compiler.misc.kindname.record.component=\
         *    record component
         */
        public static final Fragment KindnameRecordComponent = new Fragment("compiler", "kindname.record.component");
        
        /**
         * compiler.misc.kindname.static=\
         *    static
         */
        public static final Fragment KindnameStatic = new Fragment("compiler", "kindname.static");
        
        /**
         * compiler.misc.kindname.static.init=\
         *    static initializer
         */
        public static final Fragment KindnameStaticInit = new Fragment("compiler", "kindname.static.init");
        
        /**
         * compiler.misc.kindname.type.variable=\
         *    type variable
         */
        public static final Fragment KindnameTypeVariable = new Fragment("compiler", "kindname.type.variable");
        
        /**
         * compiler.misc.kindname.type.variable.bound=\
         *    bound of type variable
         */
        public static final Fragment KindnameTypeVariableBound = new Fragment("compiler", "kindname.type.variable.bound");
        
        /**
         * compiler.misc.kindname.value=\
         *    value
         */
        public static final Fragment KindnameValue = new Fragment("compiler", "kindname.value");
        
        /**
         * compiler.misc.kindname.variable=\
         *    variable
         */
        public static final Fragment KindnameVariable = new Fragment("compiler", "kindname.variable");
        
        /**
         * compiler.misc.lambda=\
         *    a lambda expression
         */
        public static final Fragment Lambda = new Fragment("compiler", "lambda");
        
        /**
         * compiler.misc.local=\
         *    local
         */
        public static final Fragment Local = new Fragment("compiler", "local");
        
        /**
         * compiler.misc.local.array.missing.target=\
         *    array initializer needs an explicit target-type
         */
        public static final Fragment LocalArrayMissingTarget = new Fragment("compiler", "local.array.missing.target");
        
        /**
         * compiler.misc.local.cant.infer.null=\
         *    variable initializer is ''null''
         */
        public static final Fragment LocalCantInferNull = new Fragment("compiler", "local.cant.infer.null");
        
        /**
         * compiler.misc.local.cant.infer.void=\
         *    variable initializer is ''void''
         */
        public static final Fragment LocalCantInferVoid = new Fragment("compiler", "local.cant.infer.void");
        
        /**
         * compiler.misc.local.lambda.missing.target=\
         *    lambda expression needs an explicit target-type
         */
        public static final Fragment LocalLambdaMissingTarget = new Fragment("compiler", "local.lambda.missing.target");
        
        /**
         * compiler.misc.local.missing.init=\
         *    cannot use ''var'' on variable without initializer
         */
        public static final Fragment LocalMissingInit = new Fragment("compiler", "local.missing.init");
        
        /**
         * compiler.misc.local.mref.missing.target=\
         *    method reference needs an explicit target-type
         */
        public static final Fragment LocalMrefMissingTarget = new Fragment("compiler", "local.mref.missing.target");
        
        /**
         * compiler.misc.local.self.ref=\
         *    cannot use ''var'' on self-referencing variable
         */
        public static final Fragment LocalSelfRef = new Fragment("compiler", "local.self.ref");
        
        /**
         * compiler.misc.location=\
         *    {0} {1}
         */
        public static Fragment Location(KindName arg0, Type arg1, Void arg2) {
            return new Fragment("compiler", "location", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.location=\
         *    {0} {1}
         */
        public static Fragment Location(KindName arg0, Symbol arg1, Void arg2) {
            return new Fragment("compiler", "location", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.location.1=\
         *    {0} {1} of type {2}
         */
        public static Fragment Location1(KindName arg0, Symbol arg1, Type arg2) {
            return new Fragment("compiler", "location.1", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.locn.module_path=\
         *    application module path
         */
        public static final Fragment LocnModule_path = new Fragment("compiler", "locn.module_path");
        
        /**
         * compiler.misc.locn.module_source_path=\
         *    module source path
         */
        public static final Fragment LocnModule_source_path = new Fragment("compiler", "locn.module_source_path");
        
        /**
         * compiler.misc.locn.system_modules=\
         *    system modules
         */
        public static final Fragment LocnSystem_modules = new Fragment("compiler", "locn.system_modules");
        
        /**
         * compiler.misc.locn.upgrade_module_path=\
         *    upgrade module path
         */
        public static final Fragment LocnUpgrade_module_path = new Fragment("compiler", "locn.upgrade_module_path");
        
        /**
         * compiler.misc.lower.bounds=\
         *        lower bounds: {0}
         */
        public static Fragment LowerBounds(List<? extends Type> arg0) {
            return new Fragment("compiler", "lower.bounds", arg0);
        }
        
        /**
         * compiler.misc.malformed.vararg.method=\
         *    class file contains malformed variable arity method: {0}
         */
        public static final Fragment MalformedVarargMethod = new Fragment("compiler", "malformed.vararg.method");
        
        /**
         * compiler.misc.method.descriptor.invalid=\
         *    method descriptor invalid for {0}
         */
        public static Fragment MethodDescriptorInvalid(Name arg0) {
            return new Fragment("compiler", "method.descriptor.invalid", arg0);
        }
        
        /**
         * compiler.misc.method.must.be.public=\
         *    accessor method must be public
         */
        public static final Fragment MethodMustBePublic = new Fragment("compiler", "method.must.be.public");
        
        /**
         * compiler.misc.missing.ret.val=\
         *    missing return value
         */
        public static Fragment MissingRetVal(Type arg0) {
            return new Fragment("compiler", "missing.ret.val", arg0);
        }
        
        /**
         * compiler.misc.module.info.definition.expected=\
         *    module-info definition expected
         */
        public static final Fragment ModuleInfoDefinitionExpected = new Fragment("compiler", "module.info.definition.expected");
        
        /**
         * compiler.misc.module.info.invalid.super.class=\
         *    module-info with invalid super class
         */
        public static final Fragment ModuleInfoInvalidSuperClass = new Fragment("compiler", "module.info.invalid.super.class");
        
        /**
         * compiler.misc.module.name.mismatch=\
         *    module name {0} does not match expected name {1}
         */
        public static Fragment ModuleNameMismatch(Name arg0, Name arg1) {
            return new Fragment("compiler", "module.name.mismatch", arg0, arg1);
        }
        
        /**
         * compiler.misc.module.non.zero.opens=\
         *    open module {0} has non-zero opens_count
         */
        public static Fragment ModuleNonZeroOpens(Name arg0) {
            return new Fragment("compiler", "module.non.zero.opens", arg0);
        }
        
        /**
         * compiler.misc.mref.infer.and.explicit.params=\
         *    cannot use raw constructor reference with explicit type parameters for constructor
         */
        public static final Fragment MrefInferAndExplicitParams = new Fragment("compiler", "mref.infer.and.explicit.params");
        
        /**
         * compiler.misc.must.not.be.same.class=\
         *    illegal self-reference in permits clause
         */
        public static final Fragment MustNotBeSameClass = new Fragment("compiler", "must.not.be.same.class");
        
        /**
         * compiler.misc.must.not.be.supertype=\
         *    illegal reference to supertype {0}
         */
        public static Fragment MustNotBeSupertype(Type arg0) {
            return new Fragment("compiler", "must.not.be.supertype", arg0);
        }
        
        /**
         * compiler.misc.no.abstracts=\
         *    no abstract method found in {0} {1}
         */
        public static Fragment NoAbstracts(KindName arg0, Symbol arg1) {
            return new Fragment("compiler", "no.abstracts", arg0, arg1);
        }
        
        /**
         * compiler.misc.no.args=\
         *    no arguments
         */
        public static final Fragment NoArgs = new Fragment("compiler", "no.args");
        
        /**
         * compiler.misc.no.conforming.assignment.exists=\
         *    argument mismatch; {0}
         */
        public static Fragment NoConformingAssignmentExists(JCDiagnostic arg0) {
            return new Fragment("compiler", "no.conforming.assignment.exists", arg0);
        }
        
        /**
         * compiler.misc.no.conforming.assignment.exists=\
         *    argument mismatch; {0}
         */
        public static Fragment NoConformingAssignmentExists(Fragment arg0) {
            return new Fragment("compiler", "no.conforming.assignment.exists", arg0);
        }
        
        /**
         * compiler.misc.no.suitable.functional.intf.inst=\
         *    cannot infer functional interface descriptor for {0}
         */
        public static Fragment NoSuitableFunctionalIntfInst(Type arg0) {
            return new Fragment("compiler", "no.suitable.functional.intf.inst", arg0);
        }
        
        /**
         * compiler.misc.no.unique.maximal.instance.exists=\
         *    no unique maximal instance exists for type variable {0} with upper bounds {1}
         */
        public static Fragment NoUniqueMaximalInstanceExists(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "no.unique.maximal.instance.exists", arg0, arg1);
        }
        
        /**
         * compiler.misc.no.unique.minimal.instance.exists=\
         *    no unique minimal instance exists for type variable {0} with lower bounds {1}
         */
        public static Fragment NoUniqueMinimalInstanceExists(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "no.unique.minimal.instance.exists", arg0, arg1);
        }
        
        /**
         * compiler.misc.non.static=\
         *    non-static
         */
        public static final Fragment NonStatic = new Fragment("compiler", "non.static");
        
        /**
         * compiler.misc.not.a.functional.intf=\
         *    {0} is not a functional interface
         */
        public static Fragment NotAFunctionalIntf(Symbol arg0) {
            return new Fragment("compiler", "not.a.functional.intf", arg0);
        }
        
        /**
         * compiler.misc.not.a.functional.intf.1=\
         *    {0} is not a functional interface\n\
         *    {1}
         */
        public static Fragment NotAFunctionalIntf1(Symbol arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "not.a.functional.intf.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.a.functional.intf.1=\
         *    {0} is not a functional interface\n\
         *    {1}
         */
        public static Fragment NotAFunctionalIntf1(Symbol arg0, Fragment arg1) {
            return new Fragment("compiler", "not.a.functional.intf.1", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.an.intf.component=\
         *    component type {0} is not an interface
         */
        public static Fragment NotAnIntfComponent(Symbol arg0) {
            return new Fragment("compiler", "not.an.intf.component", arg0);
        }
        
        /**
         * compiler.misc.not.an.intf.component=\
         *    component type {0} is not an interface
         */
        public static Fragment NotAnIntfComponent(Type arg0) {
            return new Fragment("compiler", "not.an.intf.component", arg0);
        }
        
        /**
         * compiler.misc.not.applicable.method.found=\
         *    ({2})
         */
        public static Fragment NotApplicableMethodFound(int arg0, Symbol arg1, JCDiagnostic arg2) {
            return new Fragment("compiler", "not.applicable.method.found", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.applicable.method.found=\
         *    ({2})
         */
        public static Fragment NotApplicableMethodFound(int arg0, Symbol arg1, Fragment arg2) {
            return new Fragment("compiler", "not.applicable.method.found", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.def.access.class.intf.cant.access=\
         *    {1}.{0} is defined in an inaccessible class or interface
         */
        public static Fragment NotDefAccessClassIntfCantAccess(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.class.intf.cant.access", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.class.intf.cant.access.reason=\
         *    {1}.{0} in package {2} is not accessible\n\
         *    ({3})
         */
        public static Fragment NotDefAccessClassIntfCantAccessReason(Symbol arg0, Symbol arg1, Symbol arg2, JCDiagnostic arg3) {
            return new Fragment("compiler", "not.def.access.class.intf.cant.access.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.not.def.access.class.intf.cant.access.reason=\
         *    {1}.{0} in package {2} is not accessible\n\
         *    ({3})
         */
        public static Fragment NotDefAccessClassIntfCantAccessReason(Symbol arg0, Symbol arg1, Symbol arg2, Fragment arg3) {
            return new Fragment("compiler", "not.def.access.class.intf.cant.access.reason", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.not.def.access.does.not.read=\
         *    package {1} is declared in module {2}, but module {0} does not read it
         */
        public static Fragment NotDefAccessDoesNotRead(Symbol arg0, Symbol arg1, Symbol arg2) {
            return new Fragment("compiler", "not.def.access.does.not.read", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.def.access.does.not.read.from.unnamed=\
         *    package {0} is declared in module {1}, which is not in the module graph
         */
        public static Fragment NotDefAccessDoesNotReadFromUnnamed(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.does.not.read.from.unnamed", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.does.not.read.unnamed=\
         *    package {0} is declared in the unnamed module, but module {1} does not read it
         */
        public static Fragment NotDefAccessDoesNotReadUnnamed(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.does.not.read.unnamed", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.not.exported=\
         *    package {0} is declared in module {1}, which does not export it
         */
        public static Fragment NotDefAccessNotExported(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.not.exported", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.not.exported.from.unnamed=\
         *    package {0} is declared in module {1}, which does not export it
         */
        public static Fragment NotDefAccessNotExportedFromUnnamed(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.not.exported.from.unnamed", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.not.exported.to.module=\
         *    package {0} is declared in module {1}, which does not export it to module {2}
         */
        public static Fragment NotDefAccessNotExportedToModule(Symbol arg0, Symbol arg1, Symbol arg2) {
            return new Fragment("compiler", "not.def.access.not.exported.to.module", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.def.access.not.exported.to.module.from.unnamed=\
         *    package {0} is declared in module {1}, which does not export it to the unnamed module
         */
        public static Fragment NotDefAccessNotExportedToModuleFromUnnamed(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.access.not.exported.to.module.from.unnamed", arg0, arg1);
        }
        
        /**
         * compiler.misc.not.def.access.package.cant.access=\
         *    {0} is not visible\n\
         *    ({2})
         */
        public static Fragment NotDefAccessPackageCantAccess(Symbol arg0, Symbol arg1, JCDiagnostic arg2) {
            return new Fragment("compiler", "not.def.access.package.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.def.access.package.cant.access=\
         *    {0} is not visible\n\
         *    ({2})
         */
        public static Fragment NotDefAccessPackageCantAccess(Symbol arg0, Symbol arg1, Fragment arg2) {
            return new Fragment("compiler", "not.def.access.package.cant.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.not.def.public.cant.access=\
         *    {0} is not public in {1}; cannot be accessed from outside package
         */
        public static Fragment NotDefPublicCantAccess(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "not.def.public.cant.access", arg0, arg1);
        }
        
        /**
         * compiler.misc.overridden.default=\
         *    method {0} is overridden in {1}
         */
        public static Fragment OverriddenDefault(Symbol arg0, Type arg1) {
            return new Fragment("compiler", "overridden.default", arg0, arg1);
        }
        
        /**
         * compiler.misc.package.not.visible=\
         *    package {0} is not visible\n\
         *    ({1})
         */
        public static Fragment PackageNotVisible(Symbol arg0, JCDiagnostic arg1) {
            return new Fragment("compiler", "package.not.visible", arg0, arg1);
        }
        
        /**
         * compiler.misc.package.not.visible=\
         *    package {0} is not visible\n\
         *    ({1})
         */
        public static Fragment PackageNotVisible(Symbol arg0, Fragment arg1) {
            return new Fragment("compiler", "package.not.visible", arg0, arg1);
        }
        
        /**
         * compiler.misc.partial.inst.sig=\
         *    partially instantiated to: {0}
         */
        public static Fragment PartialInstSig(Type arg0) {
            return new Fragment("compiler", "partial.inst.sig", arg0);
        }
        
        /**
         * compiler.misc.possible.loss.of.precision=\
         *    possible lossy conversion from {0} to {1}
         */
        public static Fragment PossibleLossOfPrecision(Type arg0, Type arg1) {
            return new Fragment("compiler", "possible.loss.of.precision", arg0, arg1);
        }
        
        /**
         * compiler.misc.prob.found.req=\
         *    incompatible types: {0}
         */
        public static Fragment ProbFoundReq(JCDiagnostic arg0) {
            return new Fragment("compiler", "prob.found.req", arg0);
        }
        
        /**
         * compiler.misc.prob.found.req=\
         *    incompatible types: {0}
         */
        public static Fragment ProbFoundReq(Fragment arg0) {
            return new Fragment("compiler", "prob.found.req", arg0);
        }
        
        /**
         * compiler.misc.redundant.supertype=\
         *    redundant interface {0} is extended by {1}
         */
        public static Fragment RedundantSupertype(Symbol arg0, Type arg1) {
            return new Fragment("compiler", "redundant.supertype", arg0, arg1);
        }
        
        /**
         * compiler.misc.redundant.supertype=\
         *    redundant interface {0} is extended by {1}
         */
        public static Fragment RedundantSupertype(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "redundant.supertype", arg0, arg1);
        }
        
        /**
         * compiler.misc.ref.ambiguous=\
         *    reference to {0} is ambiguous\n\
         *    both {1} {2} in {3} and {4} {5} in {6} match
         */
        public static Fragment RefAmbiguous(Name arg0, Kind arg1, Symbol arg2, Symbol arg3, Kind arg4, Symbol arg5, Symbol arg6) {
            return new Fragment("compiler", "ref.ambiguous", arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        }
        
        /**
         * compiler.misc.report.access=\
         *    {0} has {1} access in {2}
         */
        public static Fragment ReportAccess(Symbol arg0, Set<? extends Modifier> arg1, Symbol arg2) {
            return new Fragment("compiler", "report.access", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.resume.abort=\
         *    R)esume, A)bort>
         */
        public static final Fragment ResumeAbort = new Fragment("compiler", "resume.abort");
        
        /**
         * compiler.misc.source.unavailable=\
         *    (source unavailable)
         */
        public static final Fragment SourceUnavailable = new Fragment("compiler", "source.unavailable");
        
        /**
         * compiler.misc.stat.expr.expected=\
         *    lambda body is not compatible with a void functional interface\n\
         *    (consider using a block lambda body, or use a statement expression instead)
         */
        public static final Fragment StatExprExpected = new Fragment("compiler", "stat.expr.expected");
        
        /**
         * compiler.misc.static=\
         *    static
         */
        public static final Fragment Static = new Fragment("compiler", "static");
        
        /**
         * compiler.misc.static.mref.with.targs=\
         *    parameterized qualifier on static method reference
         */
        public static final Fragment StaticMrefWithTargs = new Fragment("compiler", "static.mref.with.targs");
        
        /**
         * compiler.misc.switch.expression.target.cant.be.void=\
         *    target-type for switch expression cannot be void
         */
        public static final Fragment SwitchExpressionTargetCantBeVoid = new Fragment("compiler", "switch.expression.target.cant.be.void");
        
        /**
         * compiler.misc.synthetic.name.conflict=\
         *    the symbol {0} conflicts with a compiler-synthesized symbol in {1}
         */
        public static Fragment SyntheticNameConflict(Symbol arg0, Symbol arg1) {
            return new Fragment("compiler", "synthetic.name.conflict", arg0, arg1);
        }
        
        /**
         * compiler.misc.throws.clause.not.allowed.for.canonical.constructor=\
         *    throws clause not allowed for {0} constructor
         */
        public static Fragment ThrowsClauseNotAllowedForCanonicalConstructor(Fragment arg0) {
            return new Fragment("compiler", "throws.clause.not.allowed.for.canonical.constructor", arg0);
        }
        
        /**
         * compiler.misc.token.bad-symbol=\
         *    <bad symbol>
         */
        public static final Fragment TokenBadSymbol = new Fragment("compiler", "token.bad-symbol");
        
        /**
         * compiler.misc.token.character=\
         *    <character>
         */
        public static final Fragment TokenCharacter = new Fragment("compiler", "token.character");
        
        /**
         * compiler.misc.token.double=\
         *    <double>
         */
        public static final Fragment TokenDouble = new Fragment("compiler", "token.double");
        
        /**
         * compiler.misc.token.end-of-input=\
         *    <end of input>
         */
        public static final Fragment TokenEndOfInput = new Fragment("compiler", "token.end-of-input");
        
        /**
         * compiler.misc.token.float=\
         *    <float>
         */
        public static final Fragment TokenFloat = new Fragment("compiler", "token.float");
        
        /**
         * compiler.misc.token.identifier=\
         *    <identifier>
         */
        public static final Fragment TokenIdentifier = new Fragment("compiler", "token.identifier");
        
        /**
         * compiler.misc.token.integer=\
         *    <integer>
         */
        public static final Fragment TokenInteger = new Fragment("compiler", "token.integer");
        
        /**
         * compiler.misc.token.long-integer=\
         *    <long integer>
         */
        public static final Fragment TokenLongInteger = new Fragment("compiler", "token.long-integer");
        
        /**
         * compiler.misc.token.string=\
         *    <string>
         */
        public static final Fragment TokenString = new Fragment("compiler", "token.string");
        
        /**
         * compiler.misc.try.not.applicable.to.type=\
         *    try-with-resources not applicable to variable type\n\
         *    ({0})
         */
        public static Fragment TryNotApplicableToType(JCDiagnostic arg0) {
            return new Fragment("compiler", "try.not.applicable.to.type", arg0);
        }
        
        /**
         * compiler.misc.try.not.applicable.to.type=\
         *    try-with-resources not applicable to variable type\n\
         *    ({0})
         */
        public static Fragment TryNotApplicableToType(Fragment arg0) {
            return new Fragment("compiler", "try.not.applicable.to.type", arg0);
        }
        
        /**
         * compiler.misc.type.captureof=\
         *    capture#{0} of {1}
         */
        public static Fragment TypeCaptureof(Name arg0, Type arg1) {
            return new Fragment("compiler", "type.captureof", arg0, arg1);
        }
        
        /**
         * compiler.misc.type.captureof.1=\
         *    capture#{0}
         */
        public static final Fragment TypeCaptureof1 = new Fragment("compiler", "type.captureof.1");
        
        /**
         * compiler.misc.type.must.be.identical.to.corresponding.record.component.type=\
         *    type and arity must match that of the corresponding record component\
         */
        public static final Fragment TypeMustBeIdenticalToCorrespondingRecordComponentType = new Fragment("compiler", "type.must.be.identical.to.corresponding.record.component.type");
        
        /**
         * compiler.misc.type.none=\
         *    <none>
         */
        public static final Fragment TypeNone = new Fragment("compiler", "type.none");
        
        /**
         * compiler.misc.type.null=\
         *    <null>
         */
        public static final Fragment TypeNull = new Fragment("compiler", "type.null");
        
        /**
         * compiler.misc.type.parameter=\
         *    type parameter {0}
         */
        public static Fragment TypeParameter(Type arg0) {
            return new Fragment("compiler", "type.parameter", arg0);
        }
        
        /**
         * compiler.misc.type.req.array.or.iterable=\
         *    array or java.lang.Iterable
         */
        public static final Fragment TypeReqArrayOrIterable = new Fragment("compiler", "type.req.array.or.iterable");
        
        /**
         * compiler.misc.type.req.class=\
         *    class
         */
        public static final Fragment TypeReqClass = new Fragment("compiler", "type.req.class");
        
        /**
         * compiler.misc.type.req.class.array=\
         *    class or array
         */
        public static final Fragment TypeReqClassArray = new Fragment("compiler", "type.req.class.array");
        
        /**
         * compiler.misc.type.req.exact=\
         *    class or interface without bounds
         */
        public static final Fragment TypeReqExact = new Fragment("compiler", "type.req.exact");
        
        /**
         * compiler.misc.type.req.ref=\
         *    reference
         */
        public static final Fragment TypeReqRef = new Fragment("compiler", "type.req.ref");
        
        /**
         * compiler.misc.type.var=\
         *    {0}#{1}
         */
        public static Fragment TypeVar(Name arg0, int arg1) {
            return new Fragment("compiler", "type.var", arg0, arg1);
        }
        
        /**
         * compiler.misc.unable.to.access.file=\
         *    unable to access file: {0}
         */
        public static final Fragment UnableToAccessFile = new Fragment("compiler", "unable.to.access.file");
        
        /**
         * compiler.misc.unbound=\
         *    unbound
         */
        public static final Fragment Unbound = new Fragment("compiler", "unbound");
        
        /**
         * compiler.misc.unchecked.assign=\
         *    unchecked conversion
         */
        public static final Fragment UncheckedAssign = new Fragment("compiler", "unchecked.assign");
        
        /**
         * compiler.misc.unchecked.cast.to.type=\
         *    unchecked cast
         */
        public static final Fragment UncheckedCastToType = new Fragment("compiler", "unchecked.cast.to.type");
        
        /**
         * compiler.misc.unchecked.clash.with=\
         *    {0} in {1} overrides {2} in {3}
         */
        public static Fragment UncheckedClashWith(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "unchecked.clash.with", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.unchecked.implement=\
         *    {0} in {1} implements {2} in {3}
         */
        public static Fragment UncheckedImplement(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "unchecked.implement", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.unchecked.override=\
         *    {0} in {1} overrides {2} in {3}
         */
        public static Fragment UncheckedOverride(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "unchecked.override", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.undecl.type.var=\
         *    undeclared type variable: {0}
         */
        public static final Fragment UndeclTypeVar = new Fragment("compiler", "undecl.type.var");
        
        /**
         * compiler.misc.unexpected.const.pool.tag.at=\
         *    unexpected constant pool tag: {0} at {1}
         */
        public static final Fragment UnexpectedConstPoolTagAt = new Fragment("compiler", "unexpected.const.pool.tag.at");
        
        /**
         * compiler.misc.unexpected.ret.val=\
         *    unexpected return value
         */
        public static final Fragment UnexpectedRetVal = new Fragment("compiler", "unexpected.ret.val");
        
        /**
         * compiler.misc.unicode.str.not.supported=\
         *    unicode string in class file not supported
         */
        public static final Fragment UnicodeStrNotSupported = new Fragment("compiler", "unicode.str.not.supported");
        
        /**
         * compiler.misc.unnamed.module=\
         *    unnamed module
         */
        public static final Fragment UnnamedModule = new Fragment("compiler", "unnamed.module");
        
        /**
         * compiler.misc.unnamed.package=\
         *    unnamed package
         */
        public static final Fragment UnnamedPackage = new Fragment("compiler", "unnamed.package");
        
        /**
         * compiler.misc.upper.bounds=\
         *        lower bounds: {0}
         */
        public static Fragment UpperBounds(List<? extends Type> arg0) {
            return new Fragment("compiler", "upper.bounds", arg0);
        }
        
        /**
         * compiler.misc.user.selected.completion.failure=\
         *    user-selected completion failure by class name
         */
        public static final Fragment UserSelectedCompletionFailure = new Fragment("compiler", "user.selected.completion.failure");
        
        /**
         * compiler.misc.var.and.explicit.not.allowed=\
         *    cannot mix ''var'' and explicitly-typed parameters
         */
        public static final Fragment VarAndExplicitNotAllowed = new Fragment("compiler", "var.and.explicit.not.allowed");
        
        /**
         * compiler.misc.var.and.implicit.not.allowed=\
         *    cannot mix ''var'' and implicitly-typed parameters
         */
        public static final Fragment VarAndImplicitNotAllowed = new Fragment("compiler", "var.and.implicit.not.allowed");
        
        /**
         * compiler.misc.varargs.argument.mismatch=\
         *    varargs mismatch; {0}
         */
        public static Fragment VarargsArgumentMismatch(JCDiagnostic arg0) {
            return new Fragment("compiler", "varargs.argument.mismatch", arg0);
        }
        
        /**
         * compiler.misc.varargs.argument.mismatch=\
         *    varargs mismatch; {0}
         */
        public static Fragment VarargsArgumentMismatch(Fragment arg0) {
            return new Fragment("compiler", "varargs.argument.mismatch", arg0);
        }
        
        /**
         * compiler.misc.varargs.clash.with=\
         *    {0} in {1} overrides {2} in {3}
         */
        public static Fragment VarargsClashWith(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "varargs.clash.with", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.varargs.implement=\
         *    {0} in {1} implements {2} in {3}
         */
        public static Fragment VarargsImplement(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "varargs.implement", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.varargs.override=\
         *    {0} in {1} overrides {2} in {3}
         */
        public static Fragment VarargsOverride(Symbol arg0, Symbol arg1, Symbol arg2, Symbol arg3) {
            return new Fragment("compiler", "varargs.override", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.varargs.trustme.on.non.varargs.accessor=\
         *    Accessor {0} is not a varargs method.
         */
        public static Fragment VarargsTrustmeOnNonVarargsAccessor(Symbol arg0) {
            return new Fragment("compiler", "varargs.trustme.on.non.varargs.accessor", arg0);
        }
        
        /**
         * compiler.misc.varargs.trustme.on.non.varargs.meth=\
         *    Method {0} is not a varargs method.
         */
        public static Fragment VarargsTrustmeOnNonVarargsMeth(Symbol arg0) {
            return new Fragment("compiler", "varargs.trustme.on.non.varargs.meth", arg0);
        }
        
        /**
         * compiler.misc.varargs.trustme.on.reifiable.varargs=\
         *    Varargs element type {0} is reifiable.
         */
        public static Fragment VarargsTrustmeOnReifiableVarargs(Type arg0) {
            return new Fragment("compiler", "varargs.trustme.on.reifiable.varargs", arg0);
        }
        
        /**
         * compiler.misc.varargs.trustme.on.virtual.varargs=\
         *    Instance method {0} is neither final nor private.
         */
        public static Fragment VarargsTrustmeOnVirtualVarargs(Symbol arg0) {
            return new Fragment("compiler", "varargs.trustme.on.virtual.varargs", arg0);
        }
        
        /**
         * compiler.misc.varargs.trustme.on.virtual.varargs.final.only=\
         *    Instance method {0} is not final.
         */
        public static Fragment VarargsTrustmeOnVirtualVarargsFinalOnly(Symbol arg0) {
            return new Fragment("compiler", "varargs.trustme.on.virtual.varargs.final.only", arg0);
        }
        
        /**
         * compiler.misc.verbose.checking.attribution=\
         *    [checking {0}]
         */
        public static Fragment VerboseCheckingAttribution(Symbol arg0) {
            return new Fragment("compiler", "verbose.checking.attribution", arg0);
        }
        
        /**
         * compiler.misc.verbose.classpath=\
         *    [search path for class files: {0}]
         */
        public static Fragment VerboseClasspath(String arg0) {
            return new Fragment("compiler", "verbose.classpath", arg0);
        }
        
        /**
         * compiler.misc.verbose.loading=\
         *    [loading {0}]
         */
        public static Fragment VerboseLoading(String arg0) {
            return new Fragment("compiler", "verbose.loading", arg0);
        }
        
        /**
         * compiler.misc.verbose.parsing.done=\
         *    [parsing completed {0}ms]
         */
        public static Fragment VerboseParsingDone(String arg0) {
            return new Fragment("compiler", "verbose.parsing.done", arg0);
        }
        
        /**
         * compiler.misc.verbose.parsing.started=\
         *    [parsing started {0}]
         */
        public static Fragment VerboseParsingStarted(File arg0) {
            return new Fragment("compiler", "verbose.parsing.started", arg0);
        }
        
        /**
         * compiler.misc.verbose.parsing.started=\
         *    [parsing started {0}]
         */
        public static Fragment VerboseParsingStarted(JavaFileObject arg0) {
            return new Fragment("compiler", "verbose.parsing.started", arg0);
        }
        
        /**
         * compiler.misc.verbose.parsing.started=\
         *    [parsing started {0}]
         */
        public static Fragment VerboseParsingStarted(Path arg0) {
            return new Fragment("compiler", "verbose.parsing.started", arg0);
        }
        
        /**
         * compiler.misc.verbose.sourcepath=\
         *    [search path for source files: {0}]
         */
        public static Fragment VerboseSourcepath(String arg0) {
            return new Fragment("compiler", "verbose.sourcepath", arg0);
        }
        
        /**
         * compiler.misc.verbose.total=\
         *    [total {0}ms]
         */
        public static Fragment VerboseTotal(String arg0) {
            return new Fragment("compiler", "verbose.total", arg0);
        }
        
        /**
         * compiler.misc.verbose.wrote.file=\
         *    [wrote {0}]
         */
        public static Fragment VerboseWroteFile(File arg0) {
            return new Fragment("compiler", "verbose.wrote.file", arg0);
        }
        
        /**
         * compiler.misc.verbose.wrote.file=\
         *    [wrote {0}]
         */
        public static Fragment VerboseWroteFile(JavaFileObject arg0) {
            return new Fragment("compiler", "verbose.wrote.file", arg0);
        }
        
        /**
         * compiler.misc.verbose.wrote.file=\
         *    [wrote {0}]
         */
        public static Fragment VerboseWroteFile(Path arg0) {
            return new Fragment("compiler", "verbose.wrote.file", arg0);
        }
        
        /**
         * compiler.misc.version.not.available=\
         *    (version info not available)
         */
        public static final Fragment VersionNotAvailable = new Fragment("compiler", "version.not.available");
        
        /**
         * compiler.misc.where.captured=\
         *    {0} extends {1} super: {2} from capture of {3}
         */
        public static Fragment WhereCaptured(Type arg0, Type arg1, Type arg2, Type arg3) {
            return new Fragment("compiler", "where.captured", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.where.captured.1=\
         *    {0} extends {1} from capture of {3}
         */
        public static Fragment WhereCaptured1(Type arg0, Type arg1, Void arg2, Type arg3) {
            return new Fragment("compiler", "where.captured.1", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.where.description.captured=\
         *    where {0} is a fresh type-variable:
         */
        public static final Fragment WhereDescriptionCaptured = new Fragment("compiler", "where.description.captured");
        
        /**
         * compiler.misc.where.description.captured.1=\
         *    where {0} are fresh type-variables:
         */
        public static Fragment WhereDescriptionCaptured1(Set<? extends Type> arg0) {
            return new Fragment("compiler", "where.description.captured.1", arg0);
        }
        
        /**
         * compiler.misc.where.description.intersection=\
         *    where {0} is an intersection type:
         */
        public static Fragment WhereDescriptionIntersection(Set<? extends Type> arg0) {
            return new Fragment("compiler", "where.description.intersection", arg0);
        }
        
        /**
         * compiler.misc.where.description.intersection.1=\
         *    where {0} are intersection types:
         */
        public static Fragment WhereDescriptionIntersection1(Set<? extends Type> arg0) {
            return new Fragment("compiler", "where.description.intersection.1", arg0);
        }
        
        /**
         * compiler.misc.where.description.typevar=\
         *    where {0} is a type-variable:
         */
        public static Fragment WhereDescriptionTypevar(Set<? extends Type> arg0) {
            return new Fragment("compiler", "where.description.typevar", arg0);
        }
        
        /**
         * compiler.misc.where.description.typevar.1=\
         *    where {0} are type-variables:
         */
        public static Fragment WhereDescriptionTypevar1(Set<? extends Type> arg0) {
            return new Fragment("compiler", "where.description.typevar.1", arg0);
        }
        
        /**
         * compiler.misc.where.fresh.typevar=\
         *    {0} extends {1}
         */
        public static Fragment WhereFreshTypevar(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "where.fresh.typevar", arg0, arg1);
        }
        
        /**
         * compiler.misc.where.intersection=\
         *    {0} extends {1}
         */
        public static Fragment WhereIntersection(Type arg0, List<? extends Type> arg1) {
            return new Fragment("compiler", "where.intersection", arg0, arg1);
        }
        
        /**
         * compiler.misc.where.typevar=\
         *    {0} extends {1} declared in {2} {3}
         */
        public static Fragment WhereTypevar(Type arg0, List<? extends Type> arg1, Kind arg2, Symbol arg3) {
            return new Fragment("compiler", "where.typevar", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.where.typevar.1=\
         *    {0} declared in {2} {3}
         */
        public static Fragment WhereTypevar1(Type arg0, List<? extends Type> arg1, Kind arg2, Symbol arg3) {
            return new Fragment("compiler", "where.typevar.1", arg0, arg1, arg2, arg3);
        }
        
        /**
         * compiler.misc.wrong.number.type.args=\
         *    wrong number of type arguments; required {0}
         */
        public static Fragment WrongNumberTypeArgs(String arg0) {
            return new Fragment("compiler", "wrong.number.type.args", arg0);
        }
        
        /**
         * compiler.misc.wrong.version=\
         *    class file has wrong version {0}.{1}, should be {2}.{3}
         */
        public static final Fragment WrongVersion = new Fragment("compiler", "wrong.version");
        
        /**
         * compiler.misc.x.print.processor.info=\
         *    Processor {0} matches {1} and returns {2}.
         */
        public static Fragment XPrintProcessorInfo(String arg0, String arg1, boolean arg2) {
            return new Fragment("compiler", "x.print.processor.info", arg0, arg1, arg2);
        }
        
        /**
         * compiler.misc.x.print.rounds=\
         *    Round {0}:\n\tinput files: {1}\n\tannotations: {2}\n\tlast round: {3}
         */
        public static Fragment XPrintRounds(int arg0, String arg1, Set<? extends Symbol> arg2, boolean arg3) {
            return new Fragment("compiler", "x.print.rounds", arg0, arg1, arg2, arg3);
        }
    }
}
