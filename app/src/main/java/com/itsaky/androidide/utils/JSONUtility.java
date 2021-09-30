package com.itsaky.androidide.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtility {
    public static final Gson gson = new Gson();
    public static final Gson prettyPrinter = new GsonBuilder().setPrettyPrinting().create();
}
