package com.lougw.downloader;

interface IDownloader {
    void enqueue(DownloadRequest request);

    void download(DownloadInfo model);

    void pause(DownloadInfo model);

    void pause(DownloadRequest request);

    void resume(DownloadRequest request);

    void delTask(DownloadRequest request);

    void delTaskAndFile(DownloadRequest request);

    DownloadRequest getRequestByGuid(String guid);

    DownloadRequest getRequestDownloadInfo(DownloadInfo info);

    void clear();
}
