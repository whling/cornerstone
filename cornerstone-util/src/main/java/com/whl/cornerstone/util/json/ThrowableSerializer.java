package com.whl.cornerstone.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by whling on 2018/4/10.
 */
public class ThrowableSerializer implements JsonSerializer<Throwable> {

    public JsonElement serialize(Throwable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("cause", new JsonPrimitive(String.valueOf(src.getCause())));
        jsonObject.add("message", new JsonPrimitive(src.getMessage()));
        return jsonObject;
    }
}
