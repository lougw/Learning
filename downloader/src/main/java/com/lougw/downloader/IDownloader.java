package com.lougw.downloader;

interface IDownloader {
    void enqueue(DownloadRequest request);

    void downLoad(DownloadInfo model);

    void pause(DownloadInfo model);

    void resume(DownloadRequest request);

    void deleteTask(DownloadRequest request);

    DownloadRequest getRequestDownloadInfo(DownloadInfo info);

    void clear();
}
