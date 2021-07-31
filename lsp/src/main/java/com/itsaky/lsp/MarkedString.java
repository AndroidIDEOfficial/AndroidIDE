package com.itsaky.lsp;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

@JsonAdapter(MarkedString.Adapter.class)
public class MarkedString {
    public String language, value;

    public MarkedString() {}

    public MarkedString(String value) {
        this.value = value;
    }

    public MarkedString(String language, String value) {
        this.language = language;
        this.value = value;
    }

    public static class Adapter extends TypeAdapter<MarkedString> {
        @Override
        public void write(JsonWriter out, MarkedString markedString) throws IOException {
            if (markedString.language == null) {
                out.value(markedString.value);
            } else {
                out.beginObject();
                out.name("language");
                out.value(markedString.language);
                out.name("value");
                out.value(markedString.value);
                out.endObject();
            }
        }

        @Override
        public MarkedString read(JsonReader reader) throws IOException {
            throw new UnsupportedOperationException("Deserializing MarkedString's is unsupported.");
        }
    }
}
