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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.lemminx.utils.StringUtils;

/**
 * Attribute declaration.
 *
 */
public class AttributeDeclaration {

	public static enum DataType {
		UNKNOWN, DATE, DATE_TIME, DECIMAL, BOOLEAN, INTEGER;
	}

	private final String name;
	private final ElementDeclaration ownerElementDecl;
	private final SortedSet<String> values;
	private int occurrences;
	private boolean unique;
	private boolean allNames;
	private boolean allNMTOKENs;
	private DataType dataType;

	public AttributeDeclaration(String name, ElementDeclaration ownerElementDecl) {
		this.name = name;
		this.ownerElementDecl = ownerElementDecl;
		this.values = new TreeSet<>();
		this.occurrences = 0;
		this.allNames = true;
		this.allNMTOKENs = true;
		this.unique = true;
	}

	/**
	 * Returns the attribute name.
	 * 
	 * @return the attribute name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Increment the DOM attributes occurrence.
	 */
	public void incrementOccurrences() {
		this.occurrences++;
	}

	public void addValue(String value) {
		if (!values.contains(value)) {
			// We haven't seen this attribute value before
			values.add(value);

			// Check if attribute value is a valid name
			if (allNames && !XMLChar.isValidName(value)) {
				allNames = false;
			}
			// Check if attribute value is a valid NMTOKEN
			if (allNMTOKENs && !XMLChar.isValidNmtoken(value)) {
				allNMTOKENs = false;
			}

			// Update the data type (xs:boolean, xs:date, etc)
			if (dataType == null) {
				// data type is not initialized
				dataType = getDataType(value);
			} else {
				DataType newDataType = getDataType(value);
				if (dataType != newDataType) {
					// the attribute value type is different from the previous value type
					dataType = DataType.UNKNOWN;
				}
			}
		} else {
			// We've seen this attribute value before
			this.unique = false;
		}
	}

	/**
	 * Returns the occurrences of DOM attribute.
	 * 
	 * @return the occurrences of DOM attribute.
	 */
	public int getOccurrences() {
		return occurrences;
	}

	/**
	 * Returns true if the attribute is an ID and false otherwise.
	 * 
	 * @param settings the generator settings.
	 * 
	 * @return true if the attribute is an ID and false otherwise.
	 */
	public boolean isID(FileContentGeneratorSettings settings) {
		if (ownerElementDecl.hasAttributeId()) {
			return false;
		}
		// An attribute is an ID if:
		// - all values of the attribute are unique
		// - attribute occurrences > MIN_ID_VALUES settings (10 by default).
		boolean id = isAllNames() && // ID values must be Names
				(isUnique()) && (getOccurrences() >= settings.getMinIdValues());
		if (id) {
			ownerElementDecl.setAttributeID(true);
		}
		return id;
	}

	/**
	 * Returns true if the attribute declaration is required and false otherwise.
	 * 
	 * @return true if the attribute declaration is required and false otherwise.
	 */
	public boolean isRequired() {
		// the attribute is required, if it is present on every instance of the element
		return this.getOccurrences() == ownerElementDecl.getOccurrences();
	}

	public boolean isUnique() {
		return unique;
	}

	public boolean isAllNames() {
		return allNames;
	}

	public boolean isAllNMTOKENs() {
		return allNMTOKENs;
	}

	/**
	 * Returns the all distinct attribute values.
	 * 
	 * @return the all distinct attribute values.
	 */
	public SortedSet<String> getValues() {
		return values;
	}

	/**
	 * Returns true if all values of the attribute are fixed with a given value and
	 * false otherwise.
	 * 
	 * @param settings the generator settings.
	 * 
	 * @return true if all values of the attribute are fixed with a given value and
	 *         false otherwise.
	 */
	public boolean isFixedValue(FileContentGeneratorSettings settings) {
		return isRequired() && getValues().size() == 1 && getOccurrences() >= settings.getMinFixed();
	}

	/**
	 * Returns true if attributes have values enumeration and false otherwise.
	 * 
	 * @param settings the generator settings.
	 * 
	 * @return true if attributes have values enumeration and false otherwise.
	 */
	public boolean isEnums(FileContentGeneratorSettings settings) {
		return isAllNMTOKENs() && // Enumeration values must be NMTOKENs
				(getOccurrences() >= settings.getMinEnumerationInstances())
				&& (getValues().size() <= getOccurrences() / settings.getMinEnumerationRatio())
				&& (getValues().size() <= settings.getMaxEnumerationValues());
	}

	/**
	 * Returns the attribute data type.
	 * 
	 * @return the attribute data type.
	 */
	public DataType getDataType() {
		return dataType != null ? dataType : DataType.UNKNOWN;
	}

	/**
	 * Returns the data type from the given value.
	 * 
	 * @param value the value
	 * 
	 * @return the data type from the given value.
	 */
	private static DataType getDataType(String value) {
		value = value != null ? value.trim() : null;
		if (StringUtils.isEmpty(value)) {
			return DataType.UNKNOWN;
		}
		// Is xs:integer ?
		try {
			Integer.parseInt(value);
			return DataType.INTEGER;
		} catch (Exception e) {
			// Do nothing
		}
		// Is xs:decimal ?
		try {
			Double.parseDouble(value);
			return DataType.DECIMAL;
		} catch (Exception e) {
			// Do nothing
		}
		// Is xs:boolean ?
		if ("true".equals(value) || "1".equals(value) || "false".equals(value) || "0".equals(value)) {
			return DataType.BOOLEAN;
		}
		// Is xs:dateTime ?
		try {
			// Try to parse "2001-10-26T21:32:52+02:00"
			LocalDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			return DataType.DATE_TIME;
		} catch (Exception e) {
			try {
				// Try to parse "2001-10-26T21:32:52"
				LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
				return DataType.DATE_TIME;
			} catch (Exception e1) {
				// Do nothing
			}
		}
		// Is xs:date ?
		try {
			if (LocalDate.parse(value) != null) {
				return DataType.DATE;
			}
		} catch (Exception e) {
			// Do nothing
		}
		return DataType.UNKNOWN;
	}
}
