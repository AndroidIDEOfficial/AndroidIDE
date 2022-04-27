/******************************************************************************
 * Copyright (c) 2016, 2020 TypeFox and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ******************************************************************************/
package org.eclipse.lsp4j.jsonrpc.services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a notification method on an interface or class.
 * <p>
 * A notification method must be of type <code>void</code> and have zero or one
 * argument.
 * <p>
 * According to jsonrpc an argument must be an 'object' (a java bean, not e,g.
 * String).
 * <p>
 * The name of the jsonrpc notification will be the optional segment, followed
 * by the name of the Java method that is annotated with JsonNotification. The
 * name of the jsonrpc notification can be customized by using the
 * {@link #value()} field of this annotation. To specify the whole name,
 * including the segment, in the value, set {@link #useSegment()} to false.
 *
 * @see JsonSegment
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonNotification {
	/**
	 * The name of the the jsonrpc request method. If empty, uses the name of the
	 * annotated method.
	 */
	String value() default "";

	/**
	 * When using segments, useSegment will be true to prepend the segment name to
	 * the name of the request.
	 *
	 * @see JsonSegment
	 */
	boolean useSegment() default true;
}
