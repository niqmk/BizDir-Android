package id.bizdir.util;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Hendry on 02/10/2015.
 */
public class IntTypeAdapter extends TypeAdapter<Number> {

    @Override
    public void write(JsonWriter out, Number value)
            throws IOException {
        out.value(value);
    }

    @Override
    public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return 0;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return 0;
            }
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            return 0;
            //throw new JsonSyntaxException(e);
        }
    }
}