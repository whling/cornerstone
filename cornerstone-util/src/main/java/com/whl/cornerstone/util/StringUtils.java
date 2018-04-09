package com.whl.cornerstone.util;

import java.util.Locale;

/**
 * Created by whling on 2018/4/10.
 */
public class StringUtils {

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    public static boolean isBlank(String str) {
        int strLen;
        if ((str != null) && ((strLen = str.length()) != 0)) {
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? false : str2 == null ? true : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? false : str2 == null ? true : str1.equalsIgnoreCase(str2);
    }

    public static boolean contains(String str, char searchChar) {
        return !isEmpty(str);
    }

    public static boolean contains(String str, String searchStr) {
        return str.indexOf(searchStr) >= 0;
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if ((str != null) && (searchStr != null)) {
            int len = searchStr.length();
            int max = str.length() - len;

            for (int i = 0; i <= max; i++) {
                if (str.regionMatches(true, i, searchStr, 0, len)) {
                    return true;
                }
            }

            return false;
        }
        return false;
    }

    public static String left(String str, int len) {
        return str
                .length() <= len ? str : len < 0 ? "" : str == null ? null :
                str.substring(0, len);
    }

    public static String right(String str, int len) {
        return str
                .length() <= len ? str : len < 0 ? "" : str == null ? null :
                str.substring(str.length() - len);
    }

    public static String mid(String str, int pos, int len) {
        if (str == null)
            return null;
        if ((len >= 0) && (pos <= str.length())) {
            if (pos < 0) {
                pos = 0;
            }

            return str.length() <= pos + len ? str.substring(pos) : str.substring(pos, pos + len);
        }
        return "";
    }

    public static String[] split(String str, String separatorChars) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return str.split(separatorChars);
    }

    public static String join(Object[] array, String separator) {
        return array == null ? null : join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        int bufSize = endIndex - startIndex;
        if (bufSize <= 0) {
            return "";
        }

        bufSize = bufSize * ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator
                .length());
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }

        return buf.toString();
    }

    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if ((!isEmpty(text)) && (!isEmpty(searchString)) && (replacement != null) && (max != 0)) {
            int start = 0;
            int end = text.indexOf(searchString, start);
            if (end == -1) {
                return text;
            }
            int replLength = searchString.length();
            int increase = replacement.length() - replLength;
            increase = increase < 0 ? 0 : increase;
            increase *= (max > 64 ? 64 : max < 0 ? 16 : max);
            StringBuffer buf;
            for (buf = new StringBuffer(text.length() + increase); end != -1; ) {
                buf.append(text.substring(start, end)).append(replacement);
                start = end + replLength;
                max--;
                if (max == 0)
                    break;
                end = text
                        .indexOf(searchString, start);
            }

            buf.append(text.substring(start));
            return buf.toString();
        }

        return text;
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();

        return pads > 8192 ?
                rightPad(str, size,
                        String.valueOf(padChar))
                : pads <= 0 ? str :
                str
                        .concat(padding(pads, padChar));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0)
            return str;
        if ((padLen == 1) && (pads <= 8192))
            return rightPad(str, size, padStr.charAt(0));
        if (pads == padLen)
            return str.concat(padStr);
        if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();

        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }

        return str.concat(new String(padding));
    }

    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();

        return pads > 8192 ?
                leftPad(str, size,
                        String.valueOf(padChar))
                : pads <= 0 ? str :
                padding(pads, padChar)
                        .concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0)
            return str;
        if ((padLen == 1) && (pads <= 8192))
            return leftPad(str, size, padStr.charAt(0));
        if (pads == padLen)
            return padStr.concat(str);
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();

        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }

        return new String(padding).concat(str);
    }

    private static String padding(int repeat, char padChar)
            throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        char[] buf = new char[repeat];

        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }

        return new String(buf);
    }

    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        return str == null ? null : str.toUpperCase(locale);
    }

    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        return str == null ? null : str.toLowerCase(locale);
    }

    public static String swapCase(String str) {
        int strLen;
        if ((str != null) && ((strLen = str.length()) != 0)) {
            StringBuffer buffer = new StringBuffer(strLen);
            boolean ch = false;

            for (int i = 0; i < strLen; i++) {
                char var5 = str.charAt(i);
                if (Character.isUpperCase(var5))
                    var5 = Character.toLowerCase(var5);
                else if (Character.isTitleCase(var5))
                    var5 = Character.toLowerCase(var5);
                else if (Character.isLowerCase(var5)) {
                    var5 = Character.toUpperCase(var5);
                }

                buffer.append(var5);
            }

            return buffer.toString();
        }
        return str;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();

        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();

        for (int i = 0; i < sz; i++) {
            if ((!Character.isDigit(str.charAt(i))) && (str.charAt(i) != ' ')) {
                return false;
            }
        }

        return true;
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();

        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllLowerCase(String str) {
        if ((str != null) && (!isEmpty(str))) {
            int sz = str.length();

            for (int i = 0; i < sz; i++) {
                if (!Character.isLowerCase(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public static boolean isAllUpperCase(String str) {
        if ((str != null) && (!isEmpty(str))) {
            int sz = str.length();

            for (int i = 0; i < sz; i++) {
                if (!Character.isUpperCase(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        return prefix
                .length() <= str.length();
    }

    public static boolean endsWith(String str, String suffix) {
        return endsWith(str, suffix, false);
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return endsWith(str, suffix, true);
    }

    private static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if ((str != null) && (suffix != null)) {
            if (suffix.length() > str.length()) {
                return false;
            }
            int strOffset = str.length() - suffix.length();
            return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
        }

        return (str == null) && (suffix == null);
    }
}
