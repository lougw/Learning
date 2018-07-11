package com.lougw.downloader;

import com.lougw.downloader.utils.DownloadUtils;

/**
 * @Title:下载的内容
 */
public class DownloadInfo {
    private String guid;
    private long srcType;
    private long createTime;
    private long updateTime;
    private String downLoadUrl;
    private String fileName;
    private String localDir;
    private String remarks;
    private int errorCode;
    private int retryTotal;
    private int retryCount;
    private boolean recoveryNetworkAutoDownload;


    public DownloadInfo(Builder builder) {
        this.guid = builder.guid;
        this.srcType = builder.srcType;
        this.createTime = builder.createTime;
        this.updateTime = builder.updateTime;
        this.downLoadUrl = builder.downLoadUrl;
        this.fileName = builder.fileName;
        this.localDir = builder.localDir;
        this.remarks = builder.remarks;
        this.errorCode = builder.errorCode;
        this.retryTotal = builder.retryTotal;
        this.retryCount = builder.retryCount;
        this.recoveryNetworkAutoDownload = builder.recoveryNetworkAutoDownload;


    }

    public static class Builder {
        private String guid;
        private long srcType;
        private String downLoadUrl;
        private String fileName;
        private String localDir;
        private String remarks;
        private long createTime;
        private int errorCode;
        private int retryTotal;
        private int retryCount;
        private long updateTime;
        private boolean recoveryNetworkAutoDownload;


        public Builder(String url, String fileName) {
            this.downLoadUrl = url;
            this.guid = DownloadUtils.getGuid(url);
            this.fileName = fileName;
        }

        public Builder Guid(String guid) {
            this.guid = guid;
            return this;
        }

        public Builder localDir(String localDir) {
            this.localDir = localDir;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder srcType(long srcType) {
            this.srcType = srcType;
            return this;
        }


        public Builder createTime(long createTime) {
            this.createTime = createTime;
            return this;
        }


        public Builder updateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }


        public Builder recoveryNetworkAutoDownload(boolean recoveryNetworkAutoDownload) {
            this.recoveryNetworkAutoDownload = recoveryNetworkAutoDownload;
            return this;
        }


        public Builder errorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder retryTotal(int retryTotal) {
            this.retryTotal = retryTotal;
            return this;
        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getSrcType() {
        return srcType;
    }

    public void setSrcType(long srcType) {
        this.srcType = srcType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isRecoveryNetworkAutoDownload() {
        return recoveryNetworkAutoDownload;
    }

    public void setRecoveryNetworkAutoDownload(boolean recoveryNetworkAutoDownload) {
        this.recoveryNetworkAutoDownload = recoveryNetworkAutoDownload;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getRetryTotal() {
        return retryTotal;
    }

    public void setRetryTotal(int retryTotal) {
        this.retryTotal = retryTotal;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
