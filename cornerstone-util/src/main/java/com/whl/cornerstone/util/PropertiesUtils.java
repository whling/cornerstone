package com.whl.cornerstone.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by whling on 2018/4/10.
 */
public class PropertiesUtils {

    private static Properties properties = new Properties();

    public static void loadProps(String[] paths) {
        ClassLoader classLoader = getUsefulClassLoader();
        if (paths.length > 0)
            for (String s : paths)
                loadProperties(s, classLoader);
    }

    private static void loadProperties(String resourceName, ClassLoader classLoader) {
        try {
            if (classLoader == null) {
                classLoader = getUsefulClassLoader();
            }

            Enumeration urls = classLoader != null ? classLoader.getResources(resourceName) :
                    ClassLoader.getSystemResources(resourceName);

            SysoutLogUtils.info("loading properties from " + resourceName);
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                try {
                    if ((url.getFile() != null) && (url.getFile().endsWith(".xml")))
                        properties.loadFromXML(is);
                    else
                        properties.load(is);
                } finally {
                    try {
                        is.close();
                    } catch (IOException ioe) {
                        SysoutLogUtils.error("couldnot close stream on file " + url.getFile(), ioe);
                    }
                }
            }
        } catch (IOException e) {
            SysoutLogUtils.error("load properties from " + resourceName + " Exception ", e);
        }
    }

    public static ClassLoader getUsefulClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable localThrowable) {
        }
        if (classLoader == null) {
            classLoader = PropertiesUtils.class.getClassLoader();
            if (classLoader == null)
                try {
                    classLoader = ClassLoader.getSystemClassLoader();
                } catch (Throwable localThrowable1) {
                }
        }
        return classLoader;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static Properties getProperties() {
        return properties;
    }
}
