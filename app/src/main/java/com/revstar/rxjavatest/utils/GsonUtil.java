package com.revstar.rxjavatest.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;


public class GsonUtil {

    private static Gson gson = null;

    static {
        GsonBuilder builder = new GsonBuilder();
        //builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        builder.registerTypeAdapter(Date.class, new DateSerializer());
        gson = builder.create();
    }

    public static class DateDeserializer implements JsonDeserializer<Date>{
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsLong());
        }

    }

    public static class DateSerializer implements JsonSerializer<Date>{
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static String toJson(Object o){
        return gson.toJson(o);
    }

    public static <T> T asT(Class<T> clazz, String json){
        return gson.fromJson(json, clazz);
    }

}