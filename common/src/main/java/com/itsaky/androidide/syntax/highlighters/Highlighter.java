/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/
package com.itsaky.androidide.syntax.highlighters;

import android.text.SpannableStringBuilder;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;

/**
 * A highlighter is used to highlight a small piece of code
 *
 * <p>Implementations of these class shouldn't be used in CodeEditor For CodeEditor usage, implement
 * an EditorLanguage instead
 */
public interface Highlighter {
  SpannableStringBuilder highlight(SchemeAndroidIDE scheme, String code, String match) throws Exception;
}
