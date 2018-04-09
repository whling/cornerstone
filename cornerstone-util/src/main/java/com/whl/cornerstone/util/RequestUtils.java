package com.whl.cornerstone.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by whling on 2018/4/10.
 */
public class RequestUtils {

    public static String getIpAddressFromRequest(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        } else {
            String[] ips = ip.split(",");
            for (int i = 0; i < ips.length; i++) {
                ip = ips[i];
                if ((!ip.startsWith("10.")) && (!ip.startsWith("192.168")) && (!ip.startsWith("172.16.")) &&
                        (!ip
                                .startsWith("19.2.168")) &&
                        (!ip.equalsIgnoreCase("unknown"))) {
                    return ip.trim();
                }
                ip = null;
            }

        }

        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))
            ip = request.getHeader("WL-Proxy-Client-IP");
        else {
            return ip.trim();
        }

        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }

        return ip.trim();
    }
}
