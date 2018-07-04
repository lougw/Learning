package com.lougw.downloader;

/**
 * 下载状态的监听器 <strong>注意：该接口方法有可能在一个新线程中被调用</strong>
 */
public interface DownloadListener {
    /**
     * 进入下载
     *
     * @param request DownloadRequest对象
     */
    void onEnqueue(DownloadRequest request);

    /**
     * 当一个在等待中的任务开始下载的时候被调用 注意：不能直接在该方法中更新UI
     *
     * @param request DownloadRequest对象
     */
    void onStart(DownloadRequest request);

    /**
     * 当一个任务下载的时候被调用 注意：不能直接在该方法中更新UI
     *
     * @param request DownloadRequest对象
     */
    void onDownloading(DownloadRequest request);

    /**
     * 当下载任务出错失败时被调用 注意：不能直接在该方法中更新UI
     *
     * @param request DownloadRequest对象
     */
    void onError(DownloadRequest request);

    /**
     * 当下载任务完成时被调用 注意：不能直接在该方法中更新UI
     *
     * @param request DownloadRequest对象
     */
    void onComplete(DownloadRequest request);

    /**
     * 当下载任务被暂停时被调用 注意：不能直接在该方法中更新UI
     *
     * @param request 对象
     */
    void onPause(DownloadRequest request);

    /**
     * 退出下载
     */
    void onDequeue(DownloadRequest request);

    /**
     * 当下载进度改变是被调用
     *
     * @param request DownloadRequest对象
     */
    void onProgress(DownloadRequest request);
}
