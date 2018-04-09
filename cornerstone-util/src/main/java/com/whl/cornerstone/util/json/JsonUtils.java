package com.whl.cornerstone.util.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by whling on 2018/4/10.
 */
public class JsonUtils {

    private static Gson gson = null;

    public static String objectToJsonStr(Object obj) {
        return gson.toJson(obj);
    }

    public static JsonElement toJson(String json) {
        return (JsonElement) gson.fromJson(json, JsonElement.class);
    }

    public static String objectToJsonDateSerializer(Object obj, String dateformat) {
        String jsonStr = null;

        Gson diyGson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class, new JsonSerializer() {

                    @Override
                    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
                        Date src = (Date) o;
                        SimpleDateFormat format = new SimpleDateFormat(dateformat);
                        return new JsonPrimitive(format.format(src));
                    }

                }).setDateFormat(dateformat)
                .create();
        if (diyGson != null) {
            jsonStr = diyGson.toJson(obj);
        }
        return jsonStr;
    }

    public static List<?> jsonToList(String jsonStr) {
        List objList = null;
        if (gson != null) {
            Type type = new TypeToken() {
            }.getType();
            objList = (List) gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    public static List<?> jsonToList(String jsonStr, Type type) {
        List objList = null;
        if (gson != null) {
            objList = (List) gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz) {
        List objList = null;
        if (gson != null) {
            Type jsonListType = new TypeToken() {
            }.getType();
            ArrayList<JsonObject> jsonObjects = (ArrayList) gson.fromJson(jsonStr, jsonListType);
            objList = new ArrayList();
            for (JsonObject jsonObject : jsonObjects) {
                objList.add(gson.fromJson(jsonObject, clazz));
            }
        }

        return objList;
    }

    public static <K, V> Map<K, V> jsonToMap(String json) {
        return (Map) gson.fromJson(json, new TypeToken() {
        }.getType());
    }

    public static <T> T jsonToBean(String jsonStr, Type type) {
        Object t = null;
        if (gson != null) {
            t = gson.fromJson(jsonStr, type);
        }
        return (T) t;
    }

    public static <T> T jsonToBean(byte[] jsonStr, Class<T> cl) {
        Object t = null;
        if (gson != null) {
            t = gson.fromJson(new String(jsonStr), cl);
        }
        return (T) t;
    }

    public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
        Object t = null;
        if (gson != null) {
            t = gson.fromJson(jsonStr, cl);
        }
        return (T) t;
    }

    public static <T> T jsonToBean(byte[] jsonStr, Charset charset, Class<T> cl) {
        Object t = null;
        if (gson != null) {
            t = gson.fromJson(new String(jsonStr, charset), cl);
        }
        return (T) t;
    }

    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> cl, String dateFormat) {
        Object obj = null;

        Gson diyGson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer() {
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
                        String dateStr = json.getAsString();
                        try {
                            return format.parse(dateStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).setDateFormat(dateFormat)
                .create();
        if (diyGson != null) {
            obj = diyGson.fromJson(jsonStr, cl);
        }
        return (T) obj;
    }

    public static <T> T getJsonValue(String jsonStr, String key, Class<T> clazz) {
        Object rulsObj = null;
        Map rulsMap = jsonToMap(jsonStr);
        if ((rulsMap != null) && (rulsMap.size() > 0)) {
            rulsObj = rulsMap.get(key);
        }
        return (T) rulsObj;
    }

    public static void main(String[] args) {
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            String jsonStr = "{\"appid\":\"sysmng\",\"timestamp\":\"2017-03-21 15:38:15.000\",\"localip\":\"100.66.164.139\",\"level\":\"ERROR\",\"logger\":\"servlet.IcsAutoBootServlet\",\"version\":\"1.0\",\"logmsg\":\"boot server:[CDUBPAY1] failure\",\"logtype\":\"2\",\"desc\":\"com.hisun.exception.HiException: 211007-Unexpected exception parsing XML document from class path resource [spring-beans-provider.xml]; nested exception is java.lang.RuntimeException: javassist.NotFoundException: com.jiupai.sysmng.api.TestApi\\n Nested Exception: Unexpected exception parsing XML document from class path resource [spring-beans-provider.xml]; nested exception is java.lang.RuntimeException: javassist.NotFoundException: com.jiupai.sysmng.api.TestApi    at com.hisun.exception.HiException.makeException(HiException.java:66)\"}";

            JsonElement jsonElement = toJson(jsonStr);
            System.out.println(jsonElement);
        }
    }

    static {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();

            gsonBuilder.registerTypeAdapter(Throwable.class, new JsonSerializer() {
                @Override
                public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
                    Throwable throwable = (Throwable) o;

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("cause", new JsonPrimitive(String.valueOf(throwable.getCause())));
                    jsonObject.add("message", new JsonPrimitive(String.valueOf(throwable.getMessage())));
                    return jsonObject;
                }

            });
            gson = gsonBuilder.disableHtmlEscaping().create();
        }
    }
}
