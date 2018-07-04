package com.lougw.downloader;

/**
 * 下载状态
 */
public enum DownloadStatus {
    /**
     * 正常状态，未加入队列
     */
    STATUS_NORMAL,
    /**
     * 下载任务处于挂起状态
     */
    STATUS_IDLE,
    /**
     * 下载任务处于开始状态
     */
    STATUS_START,
    /**
     * 下载任务处于下载状态
     */
    STATUS_DOWNLOADING,
    /**
     * 下载任务处于暂停状态
     */
    STATUS_PAUSE,
    /**
     * 下载任务处于结束状态
     */
    STATUS_COMPLETE,
    /**
     * 下载任务时出错 例如：网络错误，io读写错误等
     */
    STATUS_ERROR,
    /**
     * 下载任务被删除
     */
    STATUS_DELETE;
}
