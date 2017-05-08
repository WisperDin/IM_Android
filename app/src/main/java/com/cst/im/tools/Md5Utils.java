package com.cst.im.tools;

import java.security.MessageDigest;
/**
 * Created by ASUS on 2017/5/7.
 */

public class Md5Utils {
    private static MessageDigest md5 = null;
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * 用于获取一个String的md5值
     * @param str
     * @return
     */
    public static String getMd5(byte[] str) {
        byte[] bs = md5.digest(str);
        StringBuilder sb = new StringBuilder(40);
        for(byte x:bs) {
            if((x & 0xff)>>4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }
}