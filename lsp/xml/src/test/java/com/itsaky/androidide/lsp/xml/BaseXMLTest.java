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

package com.itsaky.androidide.lsp.xml;

import androidx.annotation.NonNull;

import com.itsaky.androidide.projects.api.AndroidModule;
import com.itsaky.androidide.lsp.api.CursorDependentTest;
import com.itsaky.lsp.api.ILanguageServer;

import org.mockito.Mockito;

/**
 * @author Akash Yadav
 */
public class BaseXMLTest extends CursorDependentTest {

  protected final ILanguageServer mServer = XmlLanguageServerProvider.INSTANCE.server();

  protected AndroidModule mockModuleProject() {
    final var mocked = Mockito.mock(AndroidModule.class);
    Mockito.when(mocked.getPackageName()).thenReturn("com.itsaky.androidide.test");
    return mocked;
  }

  @NonNull
  @Override
  protected ILanguageServer getServer() {
    return mServer;
  }
}
