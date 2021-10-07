/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * List class which checks the limit when item is added.
 *
 * @param <T>
 */
public class LimitList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;

	public static class ResultLimitExceededException extends RuntimeException {

		private static final long serialVersionUID = 1L;

	}

	private transient boolean resultLimitExceeded;

	private final AtomicLong limit;

	public LimitList(AtomicLong limit) {
		this.limit = limit;
	}

	@Override
	public void add(int index, T element) {
		checkLimit();
		super.add(index, element);
	}

	@Override
	public boolean add(T e) {
		checkLimit();
		return super.add(e);
	}

	/**
	 * Decrements <code>limit</code>.
	 * 
	 * Throws a <code>ResultLimitExceededException</code> if <code>limit</code>
	 * becomes negative.
	 * 
	 * @param limit the limit to decrement
	 */
	private void checkLimit() {
		if (limit == null) {
			return;
		}
		long result = limit.decrementAndGet();
		if (result < 0) {
			limit.set(0);
			throw new ResultLimitExceededException();
		}
	}

	protected AtomicLong getLimit() {
		return limit;
	}

	/**
	 * Returns true if the symbols limit has been exceeded while computing symbols
	 * information, false otherwise
	 * 
	 * @return true if the symbols limit has been exceeded while computing symbols
	 *         information, false otherwise
	 */
	public boolean isResultLimitExceeded() {
		return resultLimitExceeded;
	}

	/**
	 * Sets the resultLimitExceeded boolean
	 * 
	 * @param resultLimitExceeded
	 */
	void setResultLimitExceeded(boolean resultLimitExceeded) {
		this.resultLimitExceeded = resultLimitExceeded;
	}
}
