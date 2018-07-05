package com.lougw.downloader;

public final class DownloadBuilder {
    public int poolSize = 1;

    public DownloadBuilder poolSize(int poolSize) {
        this.poolSize = poolSize;
        return this;
    }

}
