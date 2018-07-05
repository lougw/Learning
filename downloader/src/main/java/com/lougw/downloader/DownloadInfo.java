package com.lougw.downloader;

import com.lougw.downloader.utils.DownloadUtils;

/**
 * @Title:下载的内容
 */
public class DownloadInfo {
    public String guid;
    private long srcType;
    public long createTime;
    public long updateTime;
    private String downLoadUrl;
    public String fileName;
    public String remarks;
    private boolean recoveryNetworkAutoDownload;
    private String reservedField01;
    private String reservedField02;
    private String reservedField03;
    private long reservedField04;
    private boolean reservedField05;

    public DownloadInfo(Builder builder) {
        this.guid = builder.guid;
        this.srcType = builder.srcType;
        this.createTime = builder.createTime;
        this.updateTime = builder.updateTime;
        this.downLoadUrl = builder.downLoadUrl;
        this.fileName = builder.fileName;
        this.remarks = builder.remarks;
        this.recoveryNetworkAutoDownload = builder.recoveryNetworkAutoDownload;
        this.reservedField01 = builder.reservedField01;
        this.reservedField02 = builder.reservedField02;
        this.reservedField03 = builder.reservedField03;
        this.reservedField04 = builder.reservedField04;
        this.reservedField05 = builder.reservedField05;

    }

    public static class Builder {
        private String guid;
        private long srcType;
        private String downLoadUrl;
        private String fileName;
        private String remarks;
        public long createTime;
        public long updateTime;
        private boolean recoveryNetworkAutoDownload;
        private String reservedField01;
        private String reservedField02;
        private String reservedField03;
        private long reservedField04;
        private boolean reservedField05;

        public Builder() {
        }

        public Builder Url(String url) {
            this.downLoadUrl = url;
            this.guid = DownloadUtils.getGuid(url);
            return this;
        }

        public Builder Guid(String guid) {
            this.guid = guid;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
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


        public Builder reservedField01(String reservedField01) {
            this.reservedField01 = reservedField01;
            return this;
        }


        public Builder reservedField02(String reservedField02) {
            this.reservedField02 = reservedField02;
            return this;
        }


        public Builder reservedField03(String reservedField03) {
            this.reservedField03 = reservedField03;
            return this;
        }

        public Builder reservedField04(long reservedField04) {
            this.reservedField04 = reservedField04;
            return this;
        }

        public Builder reservedField05(boolean reservedField05) {
            this.reservedField05 = reservedField05;
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

    public String getReservedField01() {
        return reservedField01;
    }

    public void setReservedField01(String reservedField01) {
        this.reservedField01 = reservedField01;
    }

    public String getReservedField02() {
        return reservedField02;
    }

    public void setReservedField02(String reservedField02) {
        this.reservedField02 = reservedField02;
    }

    public String getReservedField03() {
        return reservedField03;
    }

    public void setReservedField03(String reservedField03) {
        this.reservedField03 = reservedField03;
    }

    public long getReservedField04() {
        return reservedField04;
    }

    public void setReservedField04(long reservedField04) {
        this.reservedField04 = reservedField04;
    }

    public boolean isReservedField05() {
        return reservedField05;
    }

    public void setReservedField05(boolean reservedField05) {
        this.reservedField05 = reservedField05;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
