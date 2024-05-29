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

package com.itsaky.androidide.lsp.models;

import java.util.Comparator;

/**
 * @author Akash Yadav
 */
class CompletionItemComparator {
  static int compare(CompletionItem one, CompletionItem two) {
    return Comparator.comparing(item -> ((CompletionItem) item).getMatchLevel().ordinal())
        .thenComparing(item -> ((CompletionItem) item).getIdeSortText())
        .thenComparing(item -> ((CompletionItem) item).getIdeLabel())
        .compare(one, two);
  }
}
