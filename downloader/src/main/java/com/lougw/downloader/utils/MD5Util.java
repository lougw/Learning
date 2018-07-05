package com.lougw.downloader.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 *
 * @author Bruce.lu
 */

public class MD5Util {


    /**
     * 对字符串做MD5摘要
     *
     * @param inStr
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encodeMD5(String inStr) {
        try {
            byte[] data = inStr.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] md5 = md.digest();

            return toHexString(md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toHexString(byte[] in) {
        StringBuilder sb = new StringBuilder();
        for (byte anIn : in) {
            sb.append(Integer.toHexString((0x000000ff & anIn) | 0xffffff00).substring(6));
        }
        return sb.toString();
    }

}
