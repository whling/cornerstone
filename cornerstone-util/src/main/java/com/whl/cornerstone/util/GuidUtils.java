package com.whl.cornerstone.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by whling on 2018/4/10.
 */
public class GuidUtils {
    private static AtomicInteger inc = new AtomicInteger(1);
    private static String ipHex;
    private static String ipNum;

    public static String getNextUid(String seed)
    {
        int curr = inc.getAndIncrement();
        if (curr >= 99999) {
            inc.getAndSet(curr % 99999 + 1);
        }
        String postfix = seed == null ? "" : seed;
        String currentTmls = String.valueOf(System.currentTimeMillis());
        return StringUtils.leftPad(String.valueOf(curr), 5, '0') + currentTmls + ipHex + postfix;
    }

    public static String getNextNumUid()
    {
        int curr = inc.getAndIncrement();
        if (curr >= 999999999) {
            inc.getAndSet(curr % 999999999 + 1);
        }
        String currentTmls = StringUtils.rightPad(String.valueOf(System.currentTimeMillis()), 14, '0');

        return currentTmls + StringUtils.leftPad(String.valueOf(curr), 9, '0') + ipNum;
    }

    public static String getNextUid()
    {
        return getNextUid("");
    }

    static
    {
        String ip = "";
        try {
            ip = IPUtils.getRealIp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ipHex = "";
        ipNum = "";
        String[] split = ip.split("\\.");
        for (String string : split) {
            ipHex += Integer.toHexString(Integer.valueOf(string).intValue());
            ipNum += Integer.toBinaryString(Integer.valueOf(string).intValue());
        }

        ipNum = StringUtils.leftPad(String.valueOf(Long.parseLong(ipNum, 2)), 8, '0');
    }
}
