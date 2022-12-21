package org.eclipse.lsp4j.jsonrpc.json.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.eclipse.lsp4j.jsonrpc.messages.Tuple.Two;

import java.io.IOException;
import java.lang.reflect.Type;

public final class TupleTypeAdapters {

  private TupleTypeAdapters() {}

  public static class TwoTypeAdapter<F extends Object, S extends Object>
      extends TypeAdapter<Two<F, S>> {

    protected final TypeAdapter<F> first;
    protected final TypeAdapter<S> second;

    @SuppressWarnings("unchecked")
    public TwoTypeAdapter(Gson gson, TypeToken<Two<F, S>> typeToken) {
      Type[] elementTypes = TypeUtils.getElementTypes(typeToken, Two.class);
      this.first = gson.getAdapter((TypeToken<F>) TypeToken.get(elementTypes[0]));
      this.second = gson.getAdapter((TypeToken<S>) TypeToken.get(elementTypes[1]));
    }

    @Override
    public void write(final JsonWriter out, final Two<F, S> value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        out.beginArray();
        first.write(out, value.getFirst());
        second.write(out, value.getSecond());
        out.endArray();
      }
    }

    @Override
    public Two<F, S> read(final JsonReader in) throws IOException {
      JsonToken next = in.peek();
      if (next == JsonToken.NULL) {
        in.nextNull();
        return null;
      }
      in.beginArray();
      F f = first.read(in);
      S s = second.read(in);
      Two<F, S> result = new Two<F, S>(f, s);
      in.endArray();
      return result;
    }
  }

  public static class TwoTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
      if (!TypeUtils.isTwoTuple(typeToken.getType())) {
        return null;
      }
      return new TwoTypeAdapter(gson, typeToken);
    }
  }
}
