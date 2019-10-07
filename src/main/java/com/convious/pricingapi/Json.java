package com.convious.pricingapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

class LocalDateAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
        jsonWriter.value(localDate.toString());
    }

    @Override
    public LocalDate read(final JsonReader jsonReader) throws IOException {
        return LocalDate.parse(jsonReader.nextString());
    }
}

class BigDecimalAdapter extends TypeAdapter<BigDecimal> {
    @Override
    public void write(final JsonWriter jsonWriter, final BigDecimal value) throws IOException {
        jsonWriter.value(value.toString());
    }

    @Override
    public BigDecimal read(final JsonReader jsonReader) throws IOException {
        return new BigDecimal(jsonReader.nextString());
    }
}

public class Json {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter().nullSafe())
            .create();

    public static String serialize(Object value) {
        return gson.toJson(value);
    }

    public static <T> T deserialize(String value, Class<T> cls) {
        return gson.fromJson(value, cls);
    }
}
