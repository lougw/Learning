package com.lougw.downloader;

public interface DownloadObserver {
    public void onDownloadStateChanged(DownloadRequest request);
}
