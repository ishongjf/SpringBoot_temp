package com.hongjf.common.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright 2019  hongjf, Inc. All rights reserved.
 *
 * @Author: hongjf
 * @Date: 2020/1/6
 * @Time: 22:32
 * @Description:请求工具类
 */
@Slf4j
public class RequestUtil {

    private static final String SPLIT = ",";
    private static final int IP_LENGTH = 15;
    private static final String LOCAL_HOST = "0:0:0:0:0:0:0:1";
    private static final String LOCAL = "127.0.0.1";
    private static final String UNKNOWN = "unknown";

    public static String getIp(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCAL_HOST.equals(ipAddress) || LOCAL.equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > IP_LENGTH) {
                if (ipAddress.indexOf(SPLIT) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(SPLIT));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 获取浏览器名和版本信息
     *
     * @param request
     * @return
     */
    public static String getBrowserInfo(HttpServletRequest request) {
        String info = null;
        try {
            //获取浏览器信息
            Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
            //获取浏览器版本号
            Version version = browser.getVersion(request.getHeader("User-Agent"));
            info = browser.getName() + " version:" + version.getVersion();
        } catch (Exception e) {
            e.printStackTrace();
            info = "非浏览器请求";
        }
        return info;
    }

    /**
     * 获取app端的请求设备信息
     *
     * @param request
     * @return
     */
    public static String getAppBrowserInfo(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        //客户端类型常量
        String type = "";
        if (agent.contains("iPhone") || agent.contains("iPod") || agent.contains("iPad")) {
            type = "ios";
        } else if (agent.contains("Android") || agent.contains("Linux")) {
            type = "apk";
        } else if (agent.indexOf("micromessenger") > 0) {
            type = "wx";
        } else {
            type = "pc";
        }
        return type;
    }

    /**
     * 获取请求中的数据
     *
     * @param request
     * @return
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * map转xml
     *
     * @param params map参数
     * @return
     */
    public static String toXml(Map<String, String> params) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        Iterator var2 = params.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var2.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (ToolUtil.isNotEmpty(value)) {
                xml.append("<").append(key).append(">");
                xml.append(entry.getValue());
                xml.append("</").append(key).append(">");
            }
        }
        xml.append("</xml>");
        return xml.toString();
    }
}
