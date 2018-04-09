package com.whl.cornerstone.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by whling on 2018/4/10.
 */
public class AESUtils {
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static String encrypt(String clearPwd)
            throws UnsupportedEncodingException {
        return encrypt(clearPwd, "", "UTF-8");
    }

    public static String encrypt(String clearPwd, String secret) throws UnsupportedEncodingException {
        return encrypt(clearPwd, secret, "UTF-8");
    }

    public static String encrypt(String clearPwd, String secret, String encoding) throws UnsupportedEncodingException {
        byte[] encrypt = encrypt(clearPwd.getBytes(encoding), secret);
        return parseByte2HexStr(encrypt);
    }

    public static String decrypt(String clearPwd) throws UnsupportedEncodingException {
        return decrypt(clearPwd, "", "UTF-8");
    }

    public static String decrypt(String clearPwd, String secret) throws UnsupportedEncodingException {
        return decrypt(clearPwd, secret, "UTF-8");
    }

    public static String decrypt(String clearPwd, String secret, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = parseHexStr2Byte(clearPwd);
        byte[] decrypt = decrypt(bytes, secret);
        return new String(decrypt, encoding);
    }

    private static byte[] encrypt(byte[] content, String password) {
        return aes(content, password, 1);
    }

    private static byte[] decrypt(byte[] content, String password) {
        return aes(content, password, 2);
    }

    private static byte[] aes(byte[] content, String password, int cipherMod) {
        try {
            if ((cipherMod != 1) && (cipherMod != 2)) {
                return null;
            }
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMod, key);
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        if (args.length < 2) {
            System.out.println("Usage: java com.jiupai.cornerstone.ics.util.AESUtils <encrypt|decrypt> <passwd> [secret]");

            return;
        }

        if (args[0].equals("encrypt")) {
            String encrypt = null;
            if (args.length == 3)
                encrypt = encrypt(args[1], args[2]);
            else {
                encrypt = encrypt(args[1]);
            }
            System.out.println("passwd: <" + args[1] + ">");
            System.out.println("encrypt passwd: <" + encrypt + ">");
        } else if (args[0].equals("decrypt")) {
            String decrypt = null;
            if (args.length == 3)
                decrypt = decrypt(args[1], args[2]);
            else {
                decrypt = decrypt(args[1]);
            }

            System.out.println("encrypt passwd: <" + args[1] + ">");
            System.out.println("decrypt passwd: <" + decrypt + ">");
        } else {
            System.out.println("Usage: java com.jiupai.cornerstone.ics.util.AESUtils <encrypt|decrypt> <passwd>");
        }
    }
}
