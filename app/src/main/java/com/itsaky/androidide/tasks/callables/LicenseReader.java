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

package com.itsaky.androidide.tasks.callables;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itsaky.androidide.models.License;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;

public class LicenseReader implements Callable<List<License>> {

  private final Context ctx;

  public LicenseReader(Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public List<License> call() throws Exception {
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(ctx.getAssets().open("licenses.json")));
    String line;
    StringBuilder sb = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      sb.append(line);
    }
    TypeToken<List<License>> token = new TypeToken<List<License>>() {};
    return new Gson().fromJson(sb.toString(), token.getType());
  }
}
