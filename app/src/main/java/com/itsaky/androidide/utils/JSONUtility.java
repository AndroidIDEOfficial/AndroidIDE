/*
 * This file is part of AndroidIDE.
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
 */
package com.itsaky.androidide.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher;

public class JSONUtility {
  public static final Gson gson = new Gson();
  public static final Gson toolingGson;
  public static final Gson prettyPrinter = new GsonBuilder().setPrettyPrinting().create();

  static {
    final var builder = new GsonBuilder();
    ToolingApiLauncher.configureGson(builder);
    toolingGson = builder.create();
  }
}
