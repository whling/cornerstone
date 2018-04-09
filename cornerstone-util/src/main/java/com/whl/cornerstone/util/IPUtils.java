package com.whl.cornerstone.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by whling on 2018/4/10.
 */
public class IPUtils {

    private static Pattern ipPattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
    private static final String LOOPBACK_IP = "127.0.0.1";
    public static final String LOCAL_IP = getLocalIP();

    public static String getRealIp()
            throws SocketException
    {
        String localIp = null;
        String netip = null;
        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;
        while ((netInterfaces.hasMoreElements()) && (!finded)) {
            NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
            Enumeration address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = (InetAddress)address.nextElement();
                if ((!ip.isSiteLocalAddress()) && (!ip.isLoopbackAddress()) &&
                        (!ip
                                .getHostAddress().equals("127.0.0.1")) &&
                        (ip
                                .getHostAddress().indexOf(":") == -1)) {
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                }if ((!ip.isSiteLocalAddress()) || (ip.isLoopbackAddress()) ||
                        (ip
                                .getHostAddress().equals("127.0.0.1")) ||
                        (ip
                                .getHostAddress().indexOf(":") != -1)) continue;
                localIp = ip.getHostAddress();
            }

        }

        if (!StringUtils.isBlank(netip)) {
            return netip;
        }
        return localIp;
    }
    public static String getLocalHostIP() {
        List<String> ipList = new ArrayList();
        NetworkInterface ni;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                ni = (NetworkInterface)netInterfaces.nextElement();
                Enumeration nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = (InetAddress)nii.nextElement();
                    String ipValue = ip.getHostAddress();
                    if ((ip.getHostAddress().indexOf(":") == -1) && (!ip.isLoopbackAddress()))
                        ipList.add(ipValue);
                }
            }
        }
        catch (SocketException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        StringBuffer sBuffer = new StringBuffer();
        for (String ip : ipList) {
            sBuffer.append(ip).append(",");
        }

        return sBuffer.toString();
    }

    public static long convertIpToInt(String ip)
    {
        String[] ipArray = StringUtils.split(ip, ".");
        long ipInt = 0L;
        try
        {
            for (int i = 0; i < ipArray.length; i++) {
                if ((ipArray[i] == null) || (ipArray[i].trim().equals(""))) {
                    ipArray[i] = "0";
                }
                if (new Integer(ipArray[i].toString()).intValue() < 0) {
                    Double j = new Double(Math.abs(new Integer(ipArray[i].toString()).intValue()));
                    ipArray[i] = j.toString();
                }
                if (new Integer(ipArray[i].toString()).intValue() > 255) {
                    ipArray[i] = "255";
                }

            }

            ipInt = new Double(ipArray[0]).longValue() * 256L * 256L * 256L + new Double(ipArray[1])
                    .longValue() * 256L * 256L + new Double(ipArray[2])
                    .longValue() * 256L + new Double(ipArray[3]).longValue();
        }
        catch (Exception localException) {
        }
        return ipInt;
    }

    public static String getLocalIP()
    {
        try
        {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            if ((!localHost.isLoopbackAddress()) && (!localHost.equals("127.0.0.1"))) {
                return localIp;
            }
            return getRealIp();
        }
        catch (UnknownHostException localUnknownHostException) {
        }
        catch (SocketException localSocketException) {
        }
        return null;
    }

    public static boolean isIP(String address)
    {
        return ipPattern.matcher(address).matches();
    }
}
