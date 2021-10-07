/**
 *  Copyright (c) 2019 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.contentmodel.settings;

import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.PublishDiagnosticsCapabilities;

/**
 * XMLValidationSettings
 */
public class XMLValidationSettings {

	private Boolean enabled;

	private XMLNamespacesSettings namespaces;

	private XMLSchemaSettings schema;

	private boolean disallowDocTypeDecl;

	private boolean resolveExternalEntities;

	/**
	 * This severity preference to mark the root element of XML document which is
	 * not bound to a XML Schema/DTD.
	 *
	 * Values are {ignore, hint, info, warning, error}
	 */
	private String noGrammar;

	private PublishDiagnosticsCapabilities publishDiagnostics;

	public XMLValidationSettings() {
		// set defaults
		setEnabled(true);
		setDisallowDocTypeDecl(false);
		setResolveExternalEntities(false);
		setNamespaces(new XMLNamespacesSettings());
		setSchema(new XMLSchemaSettings());
	}

	/**
	 * Returns true if the validation is enabled and false otherwise.
	 *
	 * @return true if the validation is enabled and false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set true if the validation is enabled and false otherwise.
	 *
	 * @param enabled true if the validation is enabled and false otherwise.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Returns the XML Namespaces validation settings.
	 *
	 * @return the XML Namespaces validation settings.
	 */
	public XMLNamespacesSettings getNamespaces() {
		return namespaces;
	}

	/**
	 * Set the XML Namespaces validation settings.
	 *
	 * @param namespaces the XML Namespaces validation settings.
	 */
	public void setNamespaces(XMLNamespacesSettings namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * Returns the XML Schema validation settings.
	 *
	 * @return the XML Schema validation settings.
	 */
	public XMLSchemaSettings getSchema() {
		return schema;
	}

	/**
	 * Set the XML Schema validation settings.
	 *
	 * @param schema the XML Schema validation settings.
	 */
	public void setSchema(XMLSchemaSettings schema) {
		this.schema = schema;
	}

	public void setNoGrammar(String noGrammar) {
		this.noGrammar = noGrammar;
	}

	public String getNoGrammar() {
		return noGrammar;
	}

	/**
	 * Returns true if a fatal error is thrown if the incoming document contains a
	 * DOCTYPE declaration and false otherwise.
	 *
	 * @return true if a fatal error is thrown if the incoming document contains a
	 *         DOCTYPE declaration and false otherwise.
	 */
	public boolean isDisallowDocTypeDecl() {
		return disallowDocTypeDecl;
	}

	/**
	 * Set true if a fatal error is thrown if the incoming document contains a
	 * DOCTYPE declaration and false otherwise.
	 *
	 * @param disallowDocTypeDecl disallow DOCTYPE declaration.
	 */
	public void setDisallowDocTypeDecl(boolean disallowDocTypeDecl) {
		this.disallowDocTypeDecl = disallowDocTypeDecl;
	}

	/**
	 * Returns true if external entities must be resolved and false otherwise.
	 *
	 * @return true if external entities must be resolved and false otherwise.
	 */
	public boolean isResolveExternalEntities() {
		return resolveExternalEntities;
	}

	/**
	 * Set true if external entities must be resolved and false otherwise.
	 *
	 * @param resolveExternalEntities resolve extrenal entities
	 */
	public void setResolveExternalEntities(boolean resolveExternalEntities) {
		this.resolveExternalEntities = resolveExternalEntities;
	}

	/**
	 * Returns the <code>noGrammar</code> severity according the given settings and
	 * {@link DiagnosticSeverity#Hint} otherwise.
	 *
	 * @param validationSettings the validation settings
	 * @return the <code>noGrammar</code> severity according the given settings and
	 *         {@link DiagnosticSeverity#Hint} otherwise.
	 */
	public static DiagnosticSeverity getNoGrammarSeverity(XMLValidationSettings validationSettings) {
		DiagnosticSeverity defaultSeverity = DiagnosticSeverity.Hint;
		if (validationSettings == null) {
			return defaultSeverity;
		}
		String noGrammar = validationSettings.getNoGrammar();
		if ("ignore".equalsIgnoreCase(noGrammar)) {
			// Ignore "noGrammar", return null.
			return null;
		} else if ("info".equalsIgnoreCase(noGrammar)) {
			return DiagnosticSeverity.Information;
		} else if ("warning".equalsIgnoreCase(noGrammar)) {
			return DiagnosticSeverity.Warning;
		} else if ("error".equalsIgnoreCase(noGrammar)) {
			return DiagnosticSeverity.Error;
		}
		return defaultSeverity;
	}

	public XMLValidationSettings merge(XMLValidationSettings settings) {
		if (settings != null) {
			this.namespaces = settings.namespaces;
			this.schema = settings.schema;
			this.enabled = settings.enabled;
			this.noGrammar = settings.noGrammar;
			this.disallowDocTypeDecl = settings.disallowDocTypeDecl;
			this.resolveExternalEntities = settings.resolveExternalEntities;
		}
		return this;
	}

	public void setCapabilities(PublishDiagnosticsCapabilities publishDiagnostics) {
		this.publishDiagnostics = publishDiagnostics;
	}

	public boolean isRelatedInformation() {
		return publishDiagnostics != null && publishDiagnostics.getRelatedInformation() != null
				&& publishDiagnostics.getRelatedInformation();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (disallowDocTypeDecl ? 1231 : 1237);
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((namespaces == null) ? 0 : namespaces.hashCode());
		result = prime * result + ((noGrammar == null) ? 0 : noGrammar.hashCode());
		result = prime * result + (resolveExternalEntities ? 1231 : 1237);
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		XMLValidationSettings other = (XMLValidationSettings) obj;
		if (disallowDocTypeDecl != other.disallowDocTypeDecl) {
			return false;
		}
		if (enabled == null) {
			if (other.enabled != null) {
				return false;
			}
		} else if (!enabled.equals(other.enabled)) {
			return false;
		}
		if (namespaces == null) {
			if (other.namespaces != null) {
				return false;
			}
		} else if (!namespaces.equals(other.namespaces)) {
			return false;
		}
		if (noGrammar == null) {
			if (other.noGrammar != null) {
				return false;
			}
		} else if (!noGrammar.equals(other.noGrammar)) {
			return false;
		}
		if (resolveExternalEntities != other.resolveExternalEntities) {
			return false;
		}
		if (schema == null) {
			if (other.schema != null) {
				return false;
			}
		} else if (!schema.equals(other.schema)) {
			return false;
		}
		return true;
	}

}