package com.whl.cornerstone.log.constants;

import com.whl.cornerstone.util.IPUtils;
import com.whl.cornerstone.util.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by whling on 2018/4/10.
 */
public class AppIdLoader {

    private static String PROJECT_NAME = IPUtils.LOCAL_IP;

    static {
        String appId = System.getProperty("appid");
        if (StringUtils.isNotBlank(appId)) {
            PROJECT_NAME = appId;
        } else {
            try {
                ClassLoader classLoader = LogConstants.class.getClassLoader();
                String path = classLoader.getResource(".").getPath();
                int target = path.lastIndexOf("WEB-INF");
                if (target == -1) {
                    target = path.lastIndexOf("target");
                }
                if (target == -1) {
                    Class<?> classLoaderClass = classLoader.getClass();
                    Field jarPath = null;
                    while (classLoaderClass != null) {
                        try {
                            jarPath = classLoaderClass.getDeclaredField("files");
                        } catch (Exception e) {
                            classLoaderClass = classLoaderClass.getSuperclass();
                        }
                    }
                    if (jarPath != null) {
                        jarPath.setAccessible(true);
                        File[] files = (File[]) jarPath.get(classLoader);
                        jarPath.setAccessible(false);
                        for (File file : files) {
                            path = file.getAbsolutePath();
                            target = path.lastIndexOf("WEB-INF");
                            if (target != -1) {
                                break;
                            }
                        }
                    }
                }
                if (target != -1) {
                    String currModulePath = path.substring(0, target - 1);
                    int lastIndexOfSlash = currModulePath.lastIndexOf("/");


                    PROJECT_NAME = currModulePath.substring(lastIndexOfSlash + 1, currModulePath.length()).split("-")[0];
                }
            } catch (Exception e) {
                LoggerFactory.getLogger(AppIdLoader.class).error("通过路径获取appId失败！", e);
            }
        }
    }

    public static String getProjectName() {
        return PROJECT_NAME;
    }
}
