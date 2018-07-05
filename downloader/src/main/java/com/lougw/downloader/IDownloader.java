package com.lougw.downloader;

interface IDownloader {
    void enqueue(DownloadRequest request);

    void downLoad(BaseModel model);

    void pause(BaseModel model);

    void resume(DownloadRequest request);

    void deleteTask(DownloadRequest request);

    void clear();
}
