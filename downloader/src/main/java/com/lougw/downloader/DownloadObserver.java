package com.lougw.downloader;

public interface DownloadObserver {
    void onDownloadStateChanged(DownloadRequest request);
}
