package com.whl.cornerstone.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by whling on 2018/4/10.
 */
public class MapUtils {

    public static boolean isEmpty(Map map)
    {
        return (map == null) || (map.isEmpty());
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static Map synchronizedMap(Map map) {
        return Collections.synchronizedMap(map);
    }

    public static Properties toProperties(Map map) {
        Properties properties = new Properties();
        if (map != null) {
            Iterator iter = map.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                properties.put(key, value);
            }
        }

        return properties;
    }

    public static Map toMap(ResourceBundle resourceBundle) {
        Enumeration enumeration = resourceBundle.getKeys();
        HashMap map = new HashMap();

        while (enumeration.hasMoreElements()) {
            String key = (String)enumeration.nextElement();
            Object value = resourceBundle.getObject(key);
            map.put(key, value);
        }

        return map;
    }

    public static String getString(Map map, Object key) {
        if (map != null) {
            Object value = map.get(key);
            if (value != null) {
                return value.toString();
            }
        }

        return null;
    }

    public static Boolean getBoolean(Map map, Object key) {
        if (map != null) {
            Object value = map.get(key);
            if (value != null) {
                if ((value instanceof Boolean)) {
                    return (Boolean)value;
                }

                if ((value instanceof String)) {
                    return new Boolean((String)value);
                }

                if ((value instanceof Number)) {
                    Number n = (Number)value;
                    return n.intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }

        return null;
    }

    public static Number getNumber(Map map, Object key) {
        if (map != null) {
            Object value = map.get(key);
            if (value != null) {
                if ((value instanceof Number)) {
                    return (Number)value;
                }

                if ((value instanceof String)) {
                    try {
                        String e = (String)value;
                        return NumberFormat.getInstance().parse(e);
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
        }

        return null;
    }

    public static Byte getByte(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Byte) ? (Byte)value : value == null ? null :
                new Byte(value
                        .byteValue());
    }

    public static Short getShort(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Short) ? (Short)value : value == null ? null :
                new Short(value
                        .shortValue());
    }

    public static Integer getInteger(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Integer) ? (Integer)value : value == null ? null :
                new Integer(value
                        .intValue());
    }

    public static Long getLong(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Long) ? (Long)value : value == null ? null :
                new Long(value
                        .longValue());
    }

    public static Float getFloat(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Float) ? (Float)value : value == null ? null :
                new Float(value
                        .floatValue());
    }

    public static Double getDouble(Map map, Object key) {
        Number value = getNumber(map, key);

        return (value instanceof Double) ? (Double)value : value == null ? null :
                new Double(value
                        .doubleValue());
    }

    public static Map getMap(Map map, Object key) {
        if (map != null) {
            Object value = map.get(key);
            if ((value != null) && ((value instanceof Map))) {
                return (Map)value;
            }
        }

        return null;
    }

    public static Object getObject(Map map, Object key, Object defaultValue) {
        if (map != null) {
            Object value = map.get(key);
            if (value != null) {
                return value;
            }
        }

        return defaultValue;
    }

    public static String getString(Map map, Object key, String defaultValue) {
        String value = getString(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Boolean getBoolean(Map map, Object key, Boolean defaultValue) {
        Boolean value = getBoolean(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Number getNumber(Map map, Object key, Number defaultValue) {
        Number value = getNumber(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Byte getByte(Map map, Object key, Byte defaultValue) {
        Byte value = getByte(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Short getShort(Map map, Object key, Short defaultValue) {
        Short value = getShort(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Integer getInteger(Map map, Object key, Integer defaultValue) {
        Integer value = getInteger(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Long getLong(Map map, Object key, Long defaultValue) {
        Long value = getLong(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Float getFloat(Map map, Object key, Float defaultValue) {
        Float value = getFloat(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Double getDouble(Map map, Object key, Double defaultValue) {
        Double value = getDouble(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static Map getMap(Map map, Object key, Map defaultValue) {
        Map value = getMap(map, key);
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static boolean getBooleanValue(Map map, Object key) {
        Boolean booleanObject = getBoolean(map, key);
        return booleanObject == null ? false : booleanObject.booleanValue();
    }

    public static byte getByteValue(Map map, Object key) {
        Byte byteObject = getByte(map, key);
        return byteObject == null ? 0 : byteObject.byteValue();
    }

    public static short getShortValue(Map map, Object key) {
        Short shortObject = getShort(map, key);
        return shortObject == null ? 0 : shortObject.shortValue();
    }

    public static int getIntValue(Map map, Object key) {
        Integer integerObject = getInteger(map, key);
        return integerObject == null ? 0 : integerObject.intValue();
    }

    public static long getLongValue(Map map, Object key) {
        Long longObject = getLong(map, key);
        return longObject == null ? 0L : longObject.longValue();
    }

    public static float getFloatValue(Map map, Object key) {
        Float floatObject = getFloat(map, key);
        return floatObject == null ? 0.0F : floatObject.floatValue();
    }

    public static double getDoubleValue(Map map, Object key) {
        Double doubleObject = getDouble(map, key);
        return doubleObject == null ? 0.0D : doubleObject.doubleValue();
    }

    public static boolean getBooleanValue(Map map, Object key, boolean defaultValue) {
        Boolean booleanObject = getBoolean(map, key);
        return booleanObject == null ? defaultValue : booleanObject.booleanValue();
    }

    public static byte getByteValue(Map map, Object key, byte defaultValue) {
        Byte byteObject = getByte(map, key);
        return byteObject == null ? defaultValue : byteObject.byteValue();
    }

    public static short getShortValue(Map map, Object key, short defaultValue) {
        Short shortObject = getShort(map, key);
        return shortObject == null ? defaultValue : shortObject.shortValue();
    }

    public static int getIntValue(Map map, Object key, int defaultValue) {
        Integer integerObject = getInteger(map, key);
        return integerObject == null ? defaultValue : integerObject.intValue();
    }

    public static long getLongValue(Map map, Object key, long defaultValue) {
        Long longObject = getLong(map, key);
        return longObject == null ? defaultValue : longObject.longValue();
    }

    public static float getFloatValue(Map map, Object key, float defaultValue) {
        Float floatObject = getFloat(map, key);
        return floatObject == null ? defaultValue : floatObject.floatValue();
    }

    public static double getDoubleValue(Map map, Object key, double defaultValue) {
        Double doubleObject = getDouble(map, key);
        return doubleObject == null ? defaultValue : doubleObject.doubleValue();
    }
}
