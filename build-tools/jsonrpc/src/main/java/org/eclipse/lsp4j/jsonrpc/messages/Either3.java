/******************************************************************************
 * Copyright (c) 2017 TypeFox and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc.messages;

import java.util.function.Function;

import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/**
 * Union type for three types.
 */
public class Either3<T1, T2, T3> extends Either<T1, Either<T2, T3>> {
	
	public static <T1, T2, T3> Either3<T1, T2, T3> forFirst(@NonNull T1 first) {
		return new Either3<T1, T2, T3>(first, null);
	}

	public static <T1, T2, T3> Either3<T1, T2, T3> forSecond(@NonNull T2 second) {
		return new Either3<T1, T2, T3>(null, new Either<T2, T3>(second, null));
	}
	
	public static <T1, T2, T3> Either3<T1, T2, T3> forThird(@NonNull T3 third) {
		return new Either3<T1, T2, T3>(null, new Either<T2, T3>(null, third));
	}
	
	public static <T1, T2, T3> Either3<T1, T2, T3> forLeft3(@NonNull T1 first) {
		return new Either3<T1, T2, T3>(first, null);
	}
	
	public static <T1, T2, T3> Either3<T1, T2, T3> forRight3(@NonNull Either<T2, T3> right) {
		return new Either3<T1, T2, T3>(null, right);
	}

	protected Either3(T1 left, Either<T2, T3> right) {
		super(left, right);
	}
	
	public T1 getFirst() {
		return getLeft();
	}
	
	public T2 getSecond() {
		Either<T2, T3> right = getRight();
		if (right == null)
			return null;
		else
			return right.getLeft();
	}
	
	public T3 getThird() {
		Either<T2, T3> right = getRight();
		if (right == null)
			return null;
		else
			return right.getRight();
	}
	
	@Override
	public Object get() {
		if (isRight())
			return getRight().get();
		return super.get();
	}
	
	public boolean isFirst() {
		return isLeft();
	}
	
	public boolean isSecond() {
		return isRight() && getRight().isLeft();
	}
	
	public boolean isThird() {
		return isRight() && getRight().isRight();
	}

	public <T> T map(
			@NonNull Function<? super T1, ? extends T> mapFirst,
			@NonNull Function<? super T2, ? extends T> mapSecond,
			@NonNull Function<? super T3, ? extends T> mapThird) {
		if (isFirst()) {
			return mapFirst.apply(getFirst());
		}
		if (isSecond()) {
			return mapSecond.apply(getSecond());
		}
		if (isThird()) {
			return mapThird.apply(getThird());
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("Either3 [").append(System.lineSeparator());
		builder.append("  first = ").append(getFirst()).append(System.lineSeparator());
		builder.append("  second = ").append(getSecond()).append(System.lineSeparator());
		builder.append("  third = ").append(getThird()).append(System.lineSeparator());
		return builder.append("]").toString();
	}

}
