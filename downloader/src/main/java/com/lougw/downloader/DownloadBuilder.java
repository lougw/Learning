package com.lougw.downloader;

public final class DownloadBuilder {
    public int poolSize = 1;
    public String localDir;

    public DownloadBuilder poolSize(int poolSize) {
        this.poolSize = poolSize;
        return this;
    }

    public DownloadBuilder localDir(String localDir) {
        this.localDir = localDir;
        return this;
    }

}
