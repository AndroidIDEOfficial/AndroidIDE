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

package com.itsaky.androidide.tooling.api.util;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

/**
 * {@link TypeAdapter} for {@link File}.
 *
 * @see <a href="https://openjdk.org/jeps/403">JEP 403: Strongly Encapsulate JDK internals</a>
 *
 * @author Akash Yadav
 */
public class FileTypeAdapter extends TypeAdapter<File> {
  
  @Override
  public void write(final JsonWriter out, final File value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    
    out.value(value.getPath());
  }
  
  @Override
  public File read(final JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    
    return new File(in.nextString());
  }
}
