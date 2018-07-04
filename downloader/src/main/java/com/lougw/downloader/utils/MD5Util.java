package com.lougw.downloader.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * MD5 工具类
 *
 * @author Bruce.lu
 */

public class MD5Util {

    public static String encodeMD5ByPackage(Context ctx, String packageName) {
        PackageManager mPackMgr = ctx.getPackageManager();
        try {
            ApplicationInfo ai = mPackMgr.getApplicationInfo(packageName, 0);
            File file = new File(ai.publicSourceDir);
            return encodeMD5(file);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> encodeMD5ByDir(File file) {
        Map<String, String> encodeMD5 = null;
        if (file.exists() && file.isDirectory()) {
            encodeMD5 = new HashMap<String, String>();
            File[] childFiles = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".apk") || filename.endsWith(".APK");
                }
            });
            for (File childFile : childFiles) {
                if (childFile.isFile()) encodeMD5.put(childFile.getName(), encodeMD5(file));
            }
        }
        return encodeMD5;
    }

    /**
     * 对文件做摘要
     */
    public static String encodeMD5(File file) {
        if (!file.exists()) return null;

        InputStream in = null;
        DigestInputStream dis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            in = new FileInputStream(file);
            dis = new DigestInputStream(in, md);

            byte[] buf = new byte[1024 * 5];
            while (dis.read(buf, 0, 1024 * 5) != -1) ;

            byte[] md5 = md.digest();
            return toHexString(md5);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (dis != null) dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 对字节数组做MD5摘要
     */
    public static String encodeMD5(byte[] inData) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(inData);
            byte[] md5 = md.digest();
            return toHexString(md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

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
