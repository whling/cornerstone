package com.whl.cornerstone.util;

import java.io.File;

/**
 * Created by whling on 2018/4/10.
 */
public class SystemUtils {

    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    private static final String USER_HOME_KEY = "user.home";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String JAVA_HOME_KEY = "java.home";
    public static final String OS_NAME = getSystemProperty("os.name");
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String USER_DIR = getSystemProperty("user.dir");
    public static final String USER_HOME = getSystemProperty("user.home");
    public static final String USER_NAME = getSystemProperty("user.name");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();
    public static final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();
    public static final int JAVA_VERSION_INT = getJavaVersionAsInt();
    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
    public static final boolean IS_OS_AIX = getOSMatches("AIX");
    public static final boolean IS_OS_HP_UX = getOSMatches("HP-UX");
    public static final boolean IS_OS_IRIX = getOSMatches("Irix");
    public static final boolean IS_OS_LINUX = (getOSMatches("Linux")) ||
            (getOSMatches("LINUX"));

    public static final boolean IS_OS_MAC = getOSMatches("Mac");
    public static final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");
    public static final boolean IS_OS_OS2 = getOSMatches("OS/2");
    public static final boolean IS_OS_SOLARIS = getOSMatches("Solaris");
    public static final boolean IS_OS_SUN_OS = getOSMatches("SunOS");

    public static final boolean IS_OS_UNIX = (IS_OS_AIX) || (IS_OS_HP_UX) || (IS_OS_IRIX) || (IS_OS_LINUX) || (IS_OS_MAC_OSX) || (IS_OS_SOLARIS) || (IS_OS_SUN_OS);

    public static final boolean IS_OS_WINDOWS = getOSMatches("Windows");
    public static final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");
    public static final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows", "5.1");
    public static final boolean IS_OS_WINDOWS_VISTA = getOSMatches("Windows", "6.0");
    public static final boolean IS_OS_WINDOWS_7 = getOSMatches("Windows", "6.1");

    @Deprecated
    public static float getJavaVersion() {
        return JAVA_VERSION_FLOAT;
    }

    private static float getJavaVersionAsFloat() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0.0F;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception var2) {
        }
        return 0.0F;
    }

    private static int getJavaVersionAsInt() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 1);
        str = str + JAVA_VERSION_TRIMMED.substring(2, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5)
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        else {
            str = str + "0";
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception var2) {
        }
        return 0;
    }

    private static String getJavaVersionTrimmed() {
        if (JAVA_VERSION != null) {
            for (int i = 0; i < JAVA_VERSION.length(); i++) {
                char ch = JAVA_VERSION.charAt(i);
                if ((ch >= '0') && (ch <= '9')) {
                    return JAVA_VERSION.substring(i);
                }
            }
        }

        return null;
    }

    private static boolean getJavaVersionMatches(String versionPrefix) {
        return JAVA_VERSION_TRIMMED == null ? false : JAVA_VERSION_TRIMMED
                .startsWith(versionPrefix);
    }

    private static boolean getOSMatches(String osNamePrefix) {
        return OS_NAME == null ? false : OS_NAME.startsWith(osNamePrefix);
    }

    private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
        return (OS_NAME
                .startsWith(osNamePrefix)) &&
                (OS_VERSION.startsWith(osVersionPrefix));
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException var2) {
            System.err
                    .println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to null.");
        }

        return null;
    }

    public static boolean isJavaVersionAtLeast(float requiredVersion) {
        return JAVA_VERSION_FLOAT >= requiredVersion;
    }

    public static boolean isJavaVersionAtLeast(int requiredVersion) {
        return JAVA_VERSION_INT >= requiredVersion;
    }

    public static File getJavaHome() {
        return new File(System.getProperty("java.home"));
    }

    public static File getUserDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME));
    }
}
