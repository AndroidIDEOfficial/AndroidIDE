/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.utils.platform;

/**
 * JVM memory information
 *
 */
public class Memory {

	private final long free;

	private final long total;

	private final long max;

	Memory() {
		super();
		this.free = Runtime.getRuntime().freeMemory();
		this.total = Runtime.getRuntime().totalMemory();
		this.max = Runtime.getRuntime().maxMemory();
	}

	public long getFree() {
		return free;
	}

	public long getTotal() {
		return total;
	}

	public long getMax() {
		return max;
	}

}