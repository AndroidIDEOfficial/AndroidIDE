/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/

package org.eclipse.lemminx.extensions.catalog;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/**
 * Utility functions for working with XML catalog documents
 */
public class CatalogUtils {

	/**
	 * The catalog entries that have a 'uri' attribute
	 */
	private static final Collection<String> HAS_URI_ATTRIBUTE = Arrays.asList("public", "system", "uri", "systemSuffix",
			"uriSuffix");

	/**
	 * The catalog entries that have a 'catalog' attribute
	 */
	private static final Collection<String> HAS_CATALOG_ATTRIBUTE = Arrays.asList("delegatePublic", "delegateSystem",
			"delegateUri", "nextCatalog");

	private static final String XML_BASE_ATTRIBUTE = "xml:base";

	private static final String CATALOG_ENTITY_NAME = "catalog";

	private static final String GROUP_ENTITY_NAME = "group";

	private static final String URI_ATTRIBUTE_NAME = "uri";

	private static final String CATALOG_ATTRIBUTE_NAME = "catalog";

	/**
	 * Returns a list of all the catalog entries in the given document, or an empty
	 * list if the document is not a catalog.
	 *
	 * @return a list of all the catalog entries in the given document, or an empty
	 *         list if the document is not a catalog.
	 */
	public static List<CatalogEntry> getCatalogEntries(DOMDocument document) {
		if (!DOMUtils.isCatalog(document)) {
			return Collections.emptyList();
		}
		for (DOMNode n : document.getChildren()) {
			if (n.isElement() && CATALOG_ENTITY_NAME.equals(n.getNodeName())) {
				return collectCatalogEntries((DOMElement) n);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns the uri attribute node of the given catalog entry or null if there is
	 * no uri attribute
	 *
	 * @param element The catalog entry to get the uri attribute of
	 * @return the uri attribute node of the given catalog entry or null if there is
	 *         no uri attribute
	 */
	public static DOMAttr getCatalogEntryURI(DOMElement element) {
		return element.getAttributeNode(URI_ATTRIBUTE_NAME);
	}

	/**
	 * Returns the catalog attribute node of the given catalog entry or null if
	 * there is no catalog attribute
	 *
	 * @param element The catalog entry to get the catalog attribute of
	 * @return the catalog attribute node of the given catalog entry or null if
	 *         there is no catalog attribute
	 */
	public static DOMAttr getCatalogEntryCatalog(DOMElement element) {
		return element.getAttributeNode(CATALOG_ATTRIBUTE_NAME);
	}

	/**
	 * Returns true if this element is a catalog entry that is required to have the
	 * 'uri' attribute and false otherwise
	 *
	 * @param element The element to check
	 * @return true if this element is an catalog entry that is required to have the
	 *         'uri' attribute and false otherwise
	 */
	private static boolean isCatalogEntryWithURI(DOMElement element) {
		return HAS_URI_ATTRIBUTE.contains(element.getNodeName());
	}

	/**
	 * Returns true if this element is a catalog entry that is required to have the
	 * 'catalog' attribute and false otherwise
	 *
	 * @param element The element to check
	 * @return true if this element is an catalog entry that is required to have the
	 *         'catalog' attribute and false otherwise
	 */
	private static boolean isCatalogEntryWithCatalog(DOMElement element) {
		return HAS_CATALOG_ATTRIBUTE.contains(element.getNodeName());
	}

	/**
	 * Returns true if the given element is a group element and false otherwise
	 *
	 * @param element The element to check
	 * @return true if the given element is a group element and false otherwise
	 */
	private static boolean isGroupCatalogEntry(DOMElement element) {
		return GROUP_ENTITY_NAME.equals(element.getNodeName());
	}

	/**
	 * Get a list of all catalog entry elements that are in the given catalog
	 *
	 * @param catalog the catalog element to collect the entries for
	 * @return A list of all the catalog entities in this catalog
	 */
	private static List<CatalogEntry> collectCatalogEntries(@NonNull DOMElement catalog) {
		List<CatalogEntry> entries = new ArrayList<>();
		String baseURI = catalog.getAttribute(XML_BASE_ATTRIBUTE);
		baseURI = baseURI == null ? "" : baseURI;
		for (DOMNode node : catalog.getChildren()) {
			if (node.isElement()) {
				DOMElement element = (DOMElement) node;
				CatalogEntry catalogEntry = createCatalogEntry(baseURI, element);
				if (catalogEntry != null) {
					entries.add(catalogEntry);
				} else if (isGroupCatalogEntry(element)) {
					entries.addAll(collectGroupEntries(element, baseURI));
				}
			}
		}
		return entries;
	}

	/**
	 * Get a list of all catalog entry elements that are in the given group
	 *
	 * @param group   the group element to collect the entries for
	 * @param baseURI the baseURI of the catalog
	 * @return A list of all the catalog entities in this group
	 */
	private static List<CatalogEntry> collectGroupEntries(DOMElement group, @NonNull String baseURI) {
		List<CatalogEntry> entries = new ArrayList<>();
		String groupSegment = group.getAttribute(XML_BASE_ATTRIBUTE);
		if (groupSegment != null) {
			baseURI = Paths.get(baseURI, groupSegment).toString();
		}
		for (DOMNode node : group.getChildren()) {
			if (node.isElement()) {
				DOMElement element = (DOMElement) node;
				CatalogEntry catalogEntry = createCatalogEntry(baseURI, element);
				if (catalogEntry != null) {
					entries.add(catalogEntry);
				}
			}
		}
		return entries;
	}

	/**
	 * Returns a catalog entry for the given element and null if a catalog entry
	 * can't be made
	 *
	 * @param baseURI the base URI of the catalog entry
	 * @param element the element to turn into a catalog entry
	 * @return a catalog entry for the given element and null if a catalog entry
	 *         can't be made
	 */
	private static CatalogEntry createCatalogEntry(@NonNull String baseURI, DOMElement element) {
		if (isCatalogEntryWithURI(element)) {
			return new URICatalogEntry(baseURI, element);
		} else if (isCatalogEntryWithCatalog(element)) {
			return new CatalogCatalogEntry(baseURI, element);
		}
		return null;
	}

}