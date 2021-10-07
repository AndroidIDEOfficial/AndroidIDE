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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Children properties of an element declaration:
 * 
 * <ul>
 * <li>sequenced if all children have the same order.</li>
 * <li>cardinalities for each child.</li>
 * </ul>
 *
 */
public class ChildrenProperties {

	/**
	 * Current ordered tags.
	 */
	private List<String> orderedTags;

	/**
	 * True if tags for each child hierarchy have the same order and false
	 * otherwise.
	 */
	private boolean sequenced;

	/**
	 * Map which stores cardinality per tag.
	 */
	private Map<String, Cardinality> cardinalities;

	public ChildrenProperties() {
		this.cardinalities = new LinkedHashMap<>();
		this.sequenced = true;
	}

	public void addChildHierarchy(List<String> tags) {
		boolean first = orderedTags == null;

		// Group by tags (by keeping the order) to count it
		// For the given tags "a", "b", "c", "b", we compute this map:
		// - "a" : 1
		// - "b" : 2
		// - "c": 1
		Map<String, Long> tagsCount = tags.stream().collect( //
				Collectors.groupingBy( //
						Function.identity(), LinkedHashMap::new, Collectors.counting() //
				));
		// Get ordered distinct tags (ex : "a", "b", "c")
		List<String> newOrderedTags = new LinkedList<>(tagsCount.keySet());

		// Update cardinality for existing tags
		for (Map.Entry<String, Cardinality> entry : cardinalities.entrySet()) {
			String tag = entry.getKey();
			Cardinality cardinality = entry.getValue();
			// Get the count of the existing tag from the new tags by removing it
			Long count = tagsCount.remove(tag);
			if (count != null) {
				cardinality.update(count);
			} else {
				// the current tag doesn't exist in the existings tags, set min to 0
				cardinality.setMin(0);
			}
		}

		// Update cardinality for the new tags
		for (Map.Entry<String, Long> entry : tagsCount.entrySet()) {
			String tag = entry.getKey();
			Cardinality cardinality = cardinalities.get(tag);
			if (cardinality == null) {
				cardinality = new Cardinality();
				cardinalities.put(tag, cardinality);
			}
			Long count = tagsCount.get(tag);
			if (first) {
				cardinality.set(count);
			} else {
				cardinality.update(count);
			}
		}

		// Update ordered tags by removing tag which have 0 as min cardinality (optional
		// tag)
		for (Map.Entry<String, Cardinality> entry : cardinalities.entrySet()) {
			String tag = entry.getKey();
			Cardinality cardinality = entry.getValue();
			if (cardinality.getMin() == 0) {
				if (this.orderedTags != null) {
					this.orderedTags.remove(tag);
				}
				newOrderedTags.remove(tag);
			}
		}

		// Compute sequenced
		if (this.sequenced && this.orderedTags != null) {
			this.sequenced = this.orderedTags.equals(newOrderedTags);
		}
		this.orderedTags = newOrderedTags;
	}

	/**
	 * Returns map which stores cardinality per tag.
	 * 
	 * @return map which stores cardinality per tag
	 */
	public Map<String /* tag */ , Cardinality> getCardinalities() {
		return cardinalities;
	}

	/**
	 * Returns true if tags for each child hierarchy have the same order and false
	 * otherwise.
	 * 
	 * @return true if tags for each child hierarchy have the same order and false
	 *         otherwise.
	 */
	public boolean isSequenced() {
		return sequenced;
	}
}
