/*******************************************************************************
* Copyright (c) 2019-2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.settings;

import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;

/**
 * SharedSettings
 */
public class SharedSettings {

	private final XMLCompletionSettings completionSettings;
	private final XMLFoldingSettings foldingSettings;
	private final XMLFormattingOptions formattingSettings;
	private final XMLValidationSettings validationSettings;
	private final XMLSymbolSettings symbolSettings;
	private final XMLCodeLensSettings codeLensSettings;
	private final XMLHoverSettings hoverSettings;
	private final XMLPreferences preferences;
	private final XMLWorkspaceSettings workspaceSettings;
	private boolean actionableNotificationSupport;
	private boolean openSettingsCommandSupport;
	private boolean bindingWizardSupport;

	public SharedSettings() {
		this.completionSettings = new XMLCompletionSettings();
		this.foldingSettings = new XMLFoldingSettings();
		this.formattingSettings = new XMLFormattingOptions(true);
		this.validationSettings = new XMLValidationSettings();
		this.symbolSettings = new XMLSymbolSettings();
		this.codeLensSettings = new XMLCodeLensSettings();
		this.hoverSettings = new XMLHoverSettings();
		this.preferences = new XMLPreferences();
		this.workspaceSettings = new XMLWorkspaceSettings();
		this.actionableNotificationSupport = false;
		this.openSettingsCommandSupport = false;

	}

	public SharedSettings(SharedSettings newSettings) {
		this();
		this.completionSettings.merge(newSettings.getCompletionSettings());
		this.formattingSettings.merge(newSettings.getFormattingSettings());
		this.validationSettings.merge(newSettings.getValidationSettings());
		this.symbolSettings.merge(newSettings.getSymbolSettings());
		this.codeLensSettings.merge(newSettings.getCodeLensSettings());
		this.preferences.merge(newSettings.getPreferences());
		this.actionableNotificationSupport = newSettings.isActionableNotificationSupport();
		this.openSettingsCommandSupport = newSettings.isOpenSettingsCommandSupport();
		this.bindingWizardSupport = newSettings.isBindingWizardSupport();
	}

	public XMLCompletionSettings getCompletionSettings() {
		return completionSettings;
	}

	public XMLFoldingSettings getFoldingSettings() {
		return foldingSettings;
	}

	public XMLFormattingOptions getFormattingSettings() {
		return formattingSettings;
	}

	public XMLValidationSettings getValidationSettings() {
		return validationSettings;
	}

	public XMLSymbolSettings getSymbolSettings() {
		return symbolSettings;
	}

	public XMLCodeLensSettings getCodeLensSettings() {
		return codeLensSettings;
	}

	public XMLHoverSettings getHoverSettings() {
		return hoverSettings;
	}

	public XMLPreferences getPreferences() {
		return preferences;
	}

	public XMLWorkspaceSettings getWorkspaceSettings() {
		return workspaceSettings;
	}

	/**
	 * Returns true if the client supports actionable notifications and false
	 * otherwise
	 *
	 * See {@link org.eclipse.lemminx.customservice.ActionableNotification} and
	 * {@link org.eclipse.lemminx.customservice.XMLLanguageClientAPI}
	 *
	 * @return true if the client supports actionable notifications and false
	 *         otherwise
	 */
	public boolean isActionableNotificationSupport() {
		return actionableNotificationSupport;
	}

	/**
	 * Sets the actionableNotificationSupport boolean
	 *
	 * @param actionableNotificationSupport
	 */
	public void setActionableNotificationSupport(boolean actionableNotificationSupport) {
		this.actionableNotificationSupport = actionableNotificationSupport;
	}

	/**
	 * Returns true if the client supports the open settings command and false
	 * otherwise
	 *
	 * See {@link org.eclipse.lemminx.client.ClientCommands#OPEN_SETTINGS}
	 *
	 * @return true if the client supports the open settings command and false
	 *         otherwise
	 */
	public boolean isOpenSettingsCommandSupport() {
		return openSettingsCommandSupport;
	}

	/**
	 * Sets the openSettingsCommandSupport boolean
	 *
	 * @param openSettingsCommandSupport
	 */
	public void setOpenSettingsCommandSupport(boolean openSettingsCommandSupport) {
		this.openSettingsCommandSupport = openSettingsCommandSupport;
	}

	/**
	 * Returns true if the client supports the `xml.open.binding.wizard` command using dropdown and false otherwise
	 *
	 * @return bindingWizardSupport
	 */
	public boolean isBindingWizardSupport() {
		return this.bindingWizardSupport;
	}

	/**
	 * Sets the bindingWizardSupport boolean
	 *
	 * @param bindingWizardSupport
	 */
	public void setBindingWizardSupport(boolean bindingWizardSupport) {
		this.bindingWizardSupport = bindingWizardSupport;
	}

}