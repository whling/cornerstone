package com.whl.cornerstone.util;

import java.io.*;

/**
 * Created by whling on 2018/4/10.
 */
public class SerializeUtils {

    public static <T extends Serializable> byte[] serialize(T object)
            throws IOException {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T deserialize(byte[] bytes, Class<?> clazz)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        T localObject1 = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            if (null == obj) {
                localObject1 = null;
                return localObject1;
            }
            if (clazz.isAssignableFrom(obj.getClass())) {
                localObject1 = (T) obj;
                return localObject1;
            }
            return localObject1;
        } finally {
            if (null != ois) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ois = null;
            }
            if (null != bais) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bais = null;
            }
        }
    }
}
