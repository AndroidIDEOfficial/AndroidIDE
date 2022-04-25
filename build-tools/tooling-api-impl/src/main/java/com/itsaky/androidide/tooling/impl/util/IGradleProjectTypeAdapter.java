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

package com.itsaky.androidide.tooling.impl.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.itsaky.androidide.tooling.api.model.IGradleProject;

import java.lang.reflect.Type;

/**
 * Gson (de)serializer for {@link IGradleProject}.
 *
 * @author Akash Yadav
 */
public class IGradleProjectTypeAdapter
        implements JsonSerializer<IGradleProject>, JsonDeserializer<IGradleProject> {

    public static final String className = "className";
    public static final String classInst = "classInst";

    @Override
    public JsonElement serialize(
            IGradleProject src, Type typeOfSrc, JsonSerializationContext context) {
        final var obj = new JsonObject();
        obj.addProperty(className, src.getClass().getName());
        obj.add(classInst, context.serialize(obj));
        return obj;
    }

    @Override
    public IGradleProject deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final var obj = json.getAsJsonObject();
        final var name = obj.get(className).getAsString();
        final Class<?> klass;
        try {
            klass = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }

        return context.deserialize(obj.get(classInst), klass);
    }
}
