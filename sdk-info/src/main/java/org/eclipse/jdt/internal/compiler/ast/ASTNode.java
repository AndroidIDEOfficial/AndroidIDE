/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ASTNode {
	// storage for internal flags (32 bits)				BIT USAGE
	public final static int Bit1 = 0x1;					// return type (operator) | name reference kind (name ref) | add assertion (type decl) | useful empty statement (empty statement)
	public final static int Bit2 = 0x2;					// return type (operator) | name reference kind (name ref) | has local type (type, method, field decl) | if type elided (local)
	public final static int Bit3 = 0x4;					// return type (operator) | name reference kind (name ref) | implicit this (this ref) | is argument(local)
	public final static int Bit4 = 0x8;					// return type (operator) | first assignment to local (name ref,local decl) | undocumented empty block (block, type and method decl)
	public final static int Bit5 = 0x10;					// value for return (expression) | has all method bodies (unit) | supertype ref (type ref) | resolved (field decl)| name ref (yield result value)
	public final static int Bit6 = 0x20;					// depth (name ref, msg) | ignore need cast check (cast expression) | error in signature (method declaration/ initializer) | is recovered (annotation reference)
	public final static int Bit7 = 0x40;					// depth (name ref, msg) | need runtime checkcast (cast expression) | label used (labelStatement) | needFreeReturn (AbstractMethodDeclaration)
	public final static int Bit8 = 0x80;					// depth (name ref, msg) | unsafe cast (cast expression) | is default constructor (constructor declaration) | isElseStatementUnreachable (if statement)
	public final static int Bit9 = 0x100;				// depth (name ref, msg) | operator (operator) | is local type (type decl) | isThenStatementUnreachable (if statement) | can be static
	public final static int Bit10= 0x200;				// depth (name ref, msg) | operator (operator) | is anonymous type (type decl) | is implicit constructor (constructor)
	public final static int Bit11 = 0x400;				// depth (name ref, msg) | operator (operator) | is member type (type decl)
	public final static int Bit12 = 0x800;				// depth (name ref, msg) | operator (operator) | has abstract methods (type decl)
	public final static int Bit13 = 0x1000;			// depth (name ref, msg) | operator (operator) | is secondary type (type decl)
	public final static int Bit14 = 0x2000;			// strictly assigned (reference lhs) | operator (operator) | discard enclosing instance (explicit constr call) | hasBeenGenerated (type decl)
	public final static int Bit15 = 0x4000;			// is unnecessary cast (expression) | is varargs (type ref) | isSubRoutineEscaping (try statement) | superAccess (javadoc allocation expression/javadoc message send/javadoc return statement)
	public final static int Bit16 = 0x8000;			// in javadoc comment (name ref, type ref, msg)
	public final static int Bit17 = 0x10000;			// compound assigned (reference lhs) | unchecked (msg, alloc, explicit constr call)
	public final static int Bit18 = 0x20000;			// non null (expression) | onDemand (import reference)
	public final static int Bit19 = 0x40000;			// didResolve (parameterized qualified type ref/parameterized single type ref)  | empty (javadoc return statement) | needReceiverGenericCast (msg/fieldref)
	public final static int Bit20 = 0x80000;			// contains syntax errors (method declaration, type declaration, field declarations, initializer), typeref: <> name ref: lambda capture)
	public final static int Bit21 = 0x100000;			// for all declarations that can contain type references that have type annotations | insideExpressionStatement
	public final static int Bit22 = 0x200000;			// parenthesis count (expression) | used (import reference) shadows outer local (local declarations)
	public final static int Bit23 = 0x400000;			// parenthesis count (expression) | second or later declarator in declaration (local declarations)
	public final static int Bit24 = 0x800000;			// parenthesis count (expression)
	public final static int Bit25 = 0x1000000;		// parenthesis count (expression)
	public final static int Bit26 = 0x2000000;		// parenthesis count (expression)
	public final static int Bit27 = 0x4000000;		// parenthesis count (expression)
	public final static int Bit28 = 0x8000000;		// parenthesis count (expression)
	public final static int Bit29 = 0x10000000;		// parenthesis count (expression)
	public final static int Bit30 = 0x20000000;		// elseif (if statement) | try block exit (try statement) | fall-through (case statement) | ignore no effect assign (expression ref) | needScope (for statement) | isAnySubRoutineEscaping (return statement) | blockExit (synchronized statement)
	public final static int Bit31 = 0x40000000;		// local declaration reachable (local decl) | ignore raw type check (type ref) | discard entire assignment (assignment) | isSynchronized (return statement) | thenExit (if statement)
	public final static int Bit32 = 0x80000000;		// reachable (statement)

	public final static long Bit32L = 0x80000000L;
	public final static long Bit33L = 0x100000000L;
	public final static long Bit34L = 0x200000000L;
	public final static long Bit35L = 0x400000000L;
	public final static long Bit36L = 0x800000000L;
	public final static long Bit37L = 0x1000000000L;
	public final static long Bit38L = 0x2000000000L;
	public final static long Bit39L = 0x4000000000L;
	public final static long Bit40L = 0x8000000000L;
	public final static long Bit41L = 0x10000000000L;
	public final static long Bit42L = 0x20000000000L;
	public final static long Bit43L = 0x40000000000L;
	public final static long Bit44L = 0x80000000000L;
	public final static long Bit45L = 0x100000000000L;
	public final static long Bit46L = 0x200000000000L;
	public final static long Bit47L = 0x400000000000L;
	public final static long Bit48L = 0x800000000000L;
	public final static long Bit49L = 0x1000000000000L;
	public final static long Bit50L = 0x2000000000000L;
	public final static long Bit51L = 0x4000000000000L;
	public final static long Bit52L = 0x8000000000000L;
	public final static long Bit53L = 0x10000000000000L;
	public final static long Bit54L = 0x20000000000000L;
	public final static long Bit55L = 0x40000000000000L;
	public final static long Bit56L = 0x80000000000000L;
	public final static long Bit57L = 0x100000000000000L;
	public final static long Bit58L = 0x200000000000000L;
	public final static long Bit59L = 0x400000000000000L;
	public final static long Bit60L = 0x800000000000000L;
	public final static long Bit61L = 0x1000000000000000L;
	public final static long Bit62L = 0x2000000000000000L;
	public final static long Bit63L = 0x4000000000000000L;
	public final static long Bit64L = 0x8000000000000000L;
}
