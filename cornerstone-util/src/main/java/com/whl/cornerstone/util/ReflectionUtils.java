package com.whl.cornerstone.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by whling on 2018/4/10.
 */
public class ReflectionUtils {

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        for (Class clazz = object.getClass(); clazz != Object.class; ) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Throwable localThrowable) {
            }
            clazz = clazz
                    .getSuperclass();
        }

        return null;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        try {
            if (null != method) {
                method.setAccessible(true);

                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        Class clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Throwable localThrowable) {
            }
        }

        return null;
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);

        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);

        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception ex) {
            SysoutLogUtils.error(ex.getMessage(), ex);
        }

        return null;
    }

    public static void writeField(String fieldName, Object obj, Object value) {
        try {
            Class tClass = obj.getClass();
            Field field = tClass.getDeclaredField(fieldName);
            if (field != null) {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);

                Method method = pd.getWriteMethod();
                method.invoke(obj, new Object[]{value});
                SysoutLogUtils.debug("field:" + field.getName() + "---getValue:" + value);
            }
        } catch (Exception ex) {
            SysoutLogUtils.error("set field[" + fieldName + "] error", ex);
        }
    }

    public static void writeFieldWithSet(String fieldName, Object obj, Object value) {
        try {
            Class tClass = obj.getClass();
            Field field = tClass.getDeclaredField(fieldName);
            if (field != null) {
                Method method = tClass.getDeclaredMethod("set" + uppercaseFirstCharacter(fieldName), new Class[]{String.class});

                if (method != null) {
                    method.invoke(obj, new Object[]{value});
                    SysoutLogUtils.debug("field:" + field.getName() + "---getValue:" + value);
                } else {
                    writeField(fieldName, obj, value);
                }
            }
        } catch (Exception ex) {
            SysoutLogUtils.error("set field[" + fieldName + "] error", ex);
        }
    }

    public static String uppercaseFirstCharacter(String name) {
        char[] cs = name.toCharArray();
        if ((cs[0] >= 'a') && (cs[0] <= 'z')) {
            int tmp23_22 = 0;
            char[] tmp23_21 = cs;
            tmp23_21[tmp23_22] = (char) (tmp23_21[tmp23_22] - ' ');
        }
        return String.valueOf(cs);
    }
}
