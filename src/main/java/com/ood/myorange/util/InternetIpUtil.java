package com.ood.myorange.util;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 * Created by Linkun.
 */
public class InternetIpUtil {
    public static String INTRANET_IP = getInternalIp();
    public static String INTERNET_IP = getInternetIp();

    private InternetIpUtil(){}

    /**
     * get internal IP
     * @return ip
     */
    private static String getInternalIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Get internet Ip
     * @return 外网IP
     */
    private static String getInternetIp(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}