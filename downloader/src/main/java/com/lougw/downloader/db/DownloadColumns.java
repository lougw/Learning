package com.lougw.downloader.db;

import android.provider.BaseColumns;

/**
 * 数据库列信息
 */
public final class DownloadColumns implements BaseColumns {

    /**
     * 全局唯一标识
     */
    public static final String GUID = "guid";
    /**
     * 下载类型
     */
    public static final String SRC_TYPE = "src_type";
    /**
     * 下载地址
     */
    public static final String SRC_URI = "src_url";
    /**
     * 文件下载后保存的路径
     */
    public static final String DEST_URI = "dest_url";
    /**
     * 文件名称
     */
    public static final String FILE_NAME = "file_name";
    /**
     * 下载文件路径
     */
    public static final String LOCAL_DIR = "local_dir";
    /**
     * 文件总大小
     */
    public static final String TOTAL_SIZE = "total_size";
    /**
     * 已经下载的文件长度 当调用pause方法后会记录当前下载的长度
     */
    public static final String DOWNLOAD_SIZE = "download_size";
    /**
     * 下载状态. 例如： IDLE, START,STATUS_DOWNLOADING, PAUSE, COMPLETE, ERROR.
     */
    public static final String DOWNLOAD_STATUS = "status";
    /**
     * 下载时间
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 下载完成的时间戳，也可以用来记录下载开始的时间戳，可选
     */
    public static final String UPDATE_TIME = "update_time";
    /**
     * 备注
     */
    public static final String REMARKS = "remarks";
    /**
     * 断网wifi恢复后是否会自动下载
     */
    public static final String RECOVERY_NETWORK_AUTO_DOWNLOAD = "recovery_network_auto_download";
    /**
     * 异常编码
     */
    public static final String ERROR_CODE = "error_code";
    /**
     * 总重试次数
     */
    public static final String RETRY_TOTAL = "retry_total";
    /**
     * 重试了多少次
     */
    public static final String RETRY_COUNT = "retry_count";

}
