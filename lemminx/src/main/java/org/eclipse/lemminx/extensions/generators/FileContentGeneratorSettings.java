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
 * Base class for generator settings.
 *
 */
public class FileContentGeneratorSettings {

	private static final int DEFAULT_MIN_ENUMERATION_INSTANCES = 10;

	private static final int DEFAULT_MIN_ENUMERATION_RATIO = 3;

	private static final int DEFAULT_MAX_ENUMERATION_VALUES = 20;

	private static final int DEFAULT_MIN_FIXED = 5;

	private static final int DEFAULT_MIN_ID_VALUES = 10;

	private static final int DEFAULT_MAX_ID_VALUES = 100000;

	private int minEnumerationInstances;

	private int minEnumerationRatio;

	private int maxEnumerationValues;

	private int minFixed;

	private int minIdValues;

	private int maxIdValues;

	public FileContentGeneratorSettings() {
		// enumeration
		setMinEnumerationInstances(DEFAULT_MIN_ENUMERATION_INSTANCES);
		setMinEnumerationRatio(DEFAULT_MIN_ENUMERATION_RATIO);
		setMaxEnumerationValues(DEFAULT_MAX_ENUMERATION_VALUES);
		// fixed
		setMinFixed(DEFAULT_MIN_FIXED);
		// ID
		setMinIdValues(DEFAULT_MIN_ID_VALUES);
		setMaxIdValues(DEFAULT_MAX_ID_VALUES);
	}

	// --------------- Attribute enumerations settings

	/**
	 * Returns the minimum number of appearances of an attribute for it to be
	 * considered a candidate for an enumeration type
	 * 
	 * @return the minimum number of appearances of an attribute for it to be
	 *         considered a candidate for an enumeration type
	 */
	public int getMinEnumerationInstances() {
		return minEnumerationInstances;
	}

	/**
	 * Set the minimum number of appearances of an attribute for it to be considered
	 * a candidate for an enumeration type
	 * 
	 * @param minEnumerationInstances the minimum number of appearances of an
	 *                                attribute for it to be considered a candidate
	 *                                for an enumeration type
	 */
	public void setMinEnumerationInstances(int minEnumerationInstances) {
		this.minEnumerationInstances = minEnumerationInstances;
	}

	/**
	 * Returns the maximum number of distinct attribute values to be included in an
	 * enumeration.
	 * 
	 * @return the maximum number of distinct attribute values to be included in an
	 *         enumeration.
	 */
	public int getMaxEnumerationValues() {
		return maxEnumerationValues;
	}

	/**
	 * Set the maximum number of distinct attribute values to be included in an
	 * enumeration.
	 * 
	 * @param maxEnumerationValues the maximum number of distinct attribute values
	 *                             to be included in an enumeration.
	 */
	public void setMaxEnumerationValues(int maxEnumerationValues) {
		this.maxEnumerationValues = maxEnumerationValues;
	}

	/**
	 * Returns the ratio for an attribute will be regarded as an enumeration
	 * attribute only if the number of instances divided by the number of distinct
	 * values is >= this ratio
	 * 
	 * @return the ratio for an attribute will be regarded as an enumeration
	 *         attribute only if the number of instances divided by the number of
	 *         distinct values is >= this ratio
	 */
	public int getMinEnumerationRatio() {
		return minEnumerationRatio;
	}

	/**
	 * Set the ratio for an attribute will be regarded as an enumeration attribute
	 * only if the number of instances divided by the number of distinct values is
	 * >= this ratio
	 * 
	 * @param minEnumerationRatio the ratio for an attribute will be regarded as an
	 *                            enumeration attribute only if the number of
	 *                            instances divided by the number of distinct values
	 *                            is >= this ratio
	 */
	public void setMinEnumerationRatio(int minEnumerationRatio) {
		this.minEnumerationRatio = minEnumerationRatio;
	}

	// --------------- Attribute fixed settings

	/**
	 * Returns the minimum number of attributes that must appear, with the same
	 * value each time, for the value to be regarded as FIXED.
	 * 
	 * @return the minimum number of attributes that must appear, with the same
	 *         value each time, for the value to be regarded as FIXED.
	 */
	public int getMinFixed() {
		return minFixed;
	}

	/**
	 * Set the minimum number of attributes that must appear, with the same value
	 * each time, for the value to be regarded as FIXED.
	 * 
	 * @param minFixed the minimum number of attributes that must appear, with the
	 *                 same value each time, for the value to be regarded as FIXED.
	 */
	public void setMinFixed(int minFixed) {
		this.minFixed = minFixed;
	}

	// --------------- Attribute ID settings

	/**
	 * Returns the minumum number of attribute values that must appear for the
	 * attribute to be regarded as an ID value.
	 * 
	 * @return the minumum number of attribute values that must appear for the
	 *         attribute to be regarded as an ID value.
	 */
	public int getMinIdValues() {
		return minIdValues;
	}

	/**
	 * Set the minumum number of attribute values that must appear for the attribute
	 * to be regarded as an ID value.
	 * 
	 * @param minIdValues the minumum number of attribute values that must appear
	 *                    for the attribute to be regarded as an ID value.
	 */
	public void setMinIdValues(int minIdValues) {
		this.minIdValues = minIdValues;
	}

	/**
	 * Returns the maximum number of attribute values to be saved while checking for
	 * uniqueness.
	 * 
	 * @return the maximum number of attribute values to be saved while checking for
	 *         uniqueness.
	 */
	public int getMaxIdValues() {
		return maxIdValues;
	}

	/**
	 * Set the maximum number of attribute values to be saved while checking for
	 * uniqueness.
	 * 
	 * @param maxIdValues the maximum number of attribute values to be saved while
	 *                    checking for uniqueness.
	 */
	public void setMaxIdValues(int maxIdValues) {
		this.maxIdValues = maxIdValues;
	}

}
