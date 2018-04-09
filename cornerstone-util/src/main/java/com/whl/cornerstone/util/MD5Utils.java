package com.whl.cornerstone.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by whling on 2018/4/9.
 */
public class MD5Utils {
    public MD5Utils() {
    }

    public static final String MD5(String string) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] e = string.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(e);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static final String MD5(String string, Charset charset) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] e = string.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(e);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    public static final String MD5(String string, String salt) {
        byte[] saltBefore = new byte[64];
        byte[] saltAfter = new byte[64];

        byte[] saltBytes;
        byte[] strBytes;
        try {
            saltBytes = salt.getBytes("UTF-8");
            strBytes = string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var9) {
            saltBytes = salt.getBytes();
            strBytes = string.getBytes();
        }
        byte b1 = 54;
        byte b2 = 92;

        Arrays.fill(saltBefore, saltBytes.length, 64, b1);
        Arrays.fill(saltAfter, saltBytes.length, 64, b2);

        for (int digest = 0; digest < saltBytes.length; ++digest) {
            saltBefore[digest] = (byte) (saltBytes[digest] ^ 54);
            saltAfter[digest] = (byte) (saltBytes[digest] ^ 92);
        }

        MessageDigest var10 = null;

        try {
            var10 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var8) {
            return null;
        }

        var10.update(saltBefore);
        var10.update(strBytes);
        byte[] resultBytes = var10.digest();
        var10.reset();
        var10.update(saltAfter);
        var10.update(resultBytes, 0, 16);
        resultBytes = var10.digest();
        return toHex(resultBytes);
    }

    public static void main(String[] args) {
        System.out.println(MD5("test", "111"));
    }

    private static String toHex(byte[] var0) {
        if (var0 == null) {
            return null;
        } else {
            StringBuffer var1 = new StringBuffer(var0.length * 2);

            for (int var2 = 0; var2 < var0.length; ++var2) {
                int var3 = var0[var2] & 255;
                if (var3 < 16) {
                    var1.append("0");
                }

                var1.append(Integer.toString(var3, 16));
            }

            return var1.toString();
        }
    }
}
