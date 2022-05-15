/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.lsp.api

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.utils.Environment
import com.itsaky.lsp.api.FileProvider.Companion.resources
import com.itsaky.lsp.models.InitializeParams
import java.io.File
import org.jetbrains.annotations.Contract

/**
 * Provides instance to the java language server to test classes.
 *
 * @author Akash Yadav
 */
abstract class LanguageServerProvider {

    fun server(): ILanguageServer {

        if (Environment.COMPILER_MODULE == null) {
            val javaHome = System.getProperty("java.home")
            assertThat(javaHome).isNotEmpty()
            Environment.COMPILER_MODULE = File(javaHome)
        }

        initIfNecessary()

        return getServer()
    }

    protected abstract fun getServer(): ILanguageServer

    protected open fun initIfNecessary() {
        if (!getServer().isInitialized) {
            getServer().initialize(createInitParams())
        }
    }

    @Contract(" -> new")
    private fun createInitParams(): InitializeParams {
        return InitializeParams(setOf(resources()))
    }
}
