package com.whl.cornerstone.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by whling on 2018/4/10.
 */
public class ClassUtils {
    public static List<Class> getAllClassByInterface(Class c) {
        List returnClassList = new ArrayList();

        if (c.isInterface()) {
            String packageName = c.getPackage().getName();
            try {
                List allClass = getClasses(packageName);

                for (int i = 0; i < allClass.size(); i++) {
                    if ((!c.isAssignableFrom((Class) allClass.get(i))) ||
                            (c.equals(allClass.get(i)))) continue;
                    returnClassList.add(allClass.get(i));
                }
            } catch (Exception localException) {
            }
        }

        return returnClassList;
    }

    private static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();
        String newPath;
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            newPath = resource.getFile().replace("%20", " ");
            dirs.add(new File(newPath));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClass(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert (!file.getName().contains("."));
                classes.addAll(findClass(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file
                        .getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static List<Class> getAllClassByAnnotation(Class annotationClass) {
        List returnClassList = new ArrayList();

        if (annotationClass.isAnnotation()) {
            String packageName = annotationClass.getPackage().getName();
            try {
                List allClass = getClasses(packageName);

                for (int i = 0; i < allClass.size(); i++) {
                    if (((Class) allClass.get(i)).isAnnotationPresent(annotationClass))
                        returnClassList.add(allClass.get(i));
                }
            } catch (Exception localException) {
            }
        }
        return returnClassList;
    }
}
