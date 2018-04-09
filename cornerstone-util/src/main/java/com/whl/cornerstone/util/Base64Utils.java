package com.whl.cornerstone.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by whling on 2018/4/10.
 */
public class Base64Utils {
    public static String encode(String in, String charset)
            throws Exception {
        try {
            if (in == null) {
                return null;
            }
            return new BASE64Encoder().encodeBuffer(in.getBytes(charset)).trim();
        } catch (UnsupportedEncodingException e) {
        }
        throw new Exception("Base64加密异常");
    }

    public static String encode(String in) throws Exception {
        return encode(in, "UTF-8");
    }

    public static String decode(String in, String charset)
            throws Exception {
        byte[] dcode = null;
        if (null == in)
            return null;
        try {
            dcode = new BASE64Decoder().decodeBuffer(in);
            return new String(dcode, charset);
        } catch (IOException e) {
        }
        throw new Exception("Base64解密异常");
    }

    public static String decode(String in) throws Exception {
        return decode(in, "UTF-8");
    }
}
