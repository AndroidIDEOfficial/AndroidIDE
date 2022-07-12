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

package com.itsaky.androidide.lsp.java

import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.api.LSPTest
import com.itsaky.androidide.lsp.java.actions.AddImportTester
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.partial.PartialReparserImplTester
import com.itsaky.androidide.lsp.java.providers.JavaCompletionProviderTester
import com.itsaky.androidide.lsp.java.providers.JavaSelectionProviderTester
import com.itsaky.androidide.projects.ProjectManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** @author Akash Yadav */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.DEFAULT_VALUE_STRING)
open class JavaLSPTest : LSPTest() {

  protected val server by lazy {
    ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
      as JavaLanguageServer
  }

  @Before
  fun setup() {
    log.debug("Initializing project...")
    initProjectIfNeeded()
  }

  override fun registerServer() {
    ILanguageServerRegistry.getDefault().register(JavaLanguageServer())
  }

  override fun getServerId() = JavaLanguageServer.SERVER_ID
  
  protected fun getCompiler() : JavaCompilerService {
    return JavaCompilerProvider.get(ProjectManager.app)
  }
  
  @Test
  override fun test() {
    AddImportTester().test()
    PartialReparserImplTester().test()
    JavaCompletionProviderTester().test()
    JavaSelectionProviderTester().test()
  }
}
