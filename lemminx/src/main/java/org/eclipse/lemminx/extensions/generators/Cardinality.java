/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.generators;

/**
 * Cardinality for sequence.
 * 
 */
public class Cardinality {

	private long min;

	private long max;

	public Cardinality() {
		setMin(0L);
		setMax(1L);
	}

	public void setMin(long min) {
		this.min = min;
	}

	public void setMax(long max) {
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public void update(long value) {
		setMin(Math.min(value, getMin()));
		setMax(Math.max(value, getMax()));
	}

	public void set(Long value) {
		setMin(value);
		setMax(value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[");
		result.append(getMin());
		result.append("-");
		result.append(getMax());
		result.append("]");
		return result.toString();
	}

}