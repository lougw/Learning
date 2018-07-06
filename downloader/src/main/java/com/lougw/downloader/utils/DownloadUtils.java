package com.lougw.downloader.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.lougw.downloader.DownloadInfo;
import com.lougw.downloader.Downloader;

import java.io.File;

/**
 * Created by gaoweilou on 2016/4/1.
 */
public class DownloadUtils {
    public static final String CACHE_DIRECTORY_ROOT = "Downloader";
    public static final String SUFFIX = ".temp";

    private static final long sMaxDownloadDataDirSize =
            50 * 1024 * 1024;
    public static final long sDownloadDataDirLowSpaceThreshold =
            5 * sMaxDownloadDataDirSize;


    public static boolean isMemoryLow() {
        long bytesAvailable = getAvailableBytesInFileSystemAtGivenRoot(new File(
                getDownLoadFileHome()));
        if (bytesAvailable < sDownloadDataDirLowSpaceThreshold) {
            bytesAvailable = getAvailableBytesInFileSystemAtGivenRoot(new File(
                    getDownLoadFileHome()));
            if (bytesAvailable < sDownloadDataDirLowSpaceThreshold) {
                return true;
            }
        }
        return false;
    }


    private static long getAvailableBytesInFileSystemAtGivenRoot(File root) {
        StatFs stat = new StatFs(root.getPath());
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        long size = stat.getBlockSize() * availableBlocks;
        return size;
    }

    public static String getDownLoadFileHome() {
        return getPath(CACHE_DIRECTORY_ROOT);
    }

    /**
     * @Description:获取目录
     */
    public static String getPath(String path) {
        File file;
        boolean isHaveSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isHaveSDCard) {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + path);
        } else {
            file = new File(Downloader.getInstance().getContext().getFilesDir() + File.separator + path);
        }
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }


    public static String getFileName(String localDir, String fileName) {
        try {
            File file = new File(localDir);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localDir + File.separator + fileName;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    public static String getGuid(String srcUri) {
        return MD5Util.encodeMD5(srcUri);
    }
}
