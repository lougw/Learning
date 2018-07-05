package com.lougw.downloader;

/**
 * 线程池配置类
 */
public class Config {
    public static int POOL_SIZE = 1;
    public static int DOWNLOAD_THREADS = 1;


    public static final String ACTION = "com.lougw.app.ACTION_DOWNLOAD";
    public static final String DOWNLOAD_REQUEST = "DOWNLOAD_REQUEST";
    public static final String ACTION_MEAN = "action_mean";
    public static final String ON_ENQUEUE = "onEnqueue";
    public static final String ON_START = "onStart";
    public static final String ON_COMPLETE = "onComplete";
    public static final String ON_ERROR = "onError";
    public static final String ON_PAUSED = "onPaused";
    public static final String ON_DEQUEUE = "onDequeue";
    public static final String ON_PROGRESS = "onProgress";


    public static void setPoolSize(int poolSize) {
        POOL_SIZE = poolSize;
    }

    public static void setDownloadThreads(int threadNum) {
        DOWNLOAD_THREADS = threadNum;
    }

    public static int getPoolSize() {
        return POOL_SIZE;
    }

    public static int getDownloadThreads() {
        return DOWNLOAD_THREADS;
    }
}
