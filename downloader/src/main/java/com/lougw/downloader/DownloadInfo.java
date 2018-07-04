package com.lougw.downloader;

import com.lougw.downloader.utils.MD5Util;

public class DownloadInfo extends BaseModel {

    public String Url;

    public String Guid;

    public String FileName;

    public long SrcType;

    public long CreateTime;

    public long UpdateTime;

    public String Remarks;

    public DownloadInfo(Builder builder) {
        Url = builder.Url;
        Guid = builder.Guid;
        FileName = builder.FileName;
    }

    public static class Builder {
        private String Url;

        private String Guid;

        private String FileName;

        private long SrcType;

        private long CreateTime;

        private long UpdateTime;

        private String Remarks;

        public Builder() {
        }

        public Builder Url(String Url) {
            this.Url = Url;
            Guid = MD5Util.encodeMD5(Url);
            return this;
        }

        public Builder FileName(String fileName) {
            this.FileName = fileName;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }

    @Override
    public String getDownLoadUrl() {
        return Url;
    }

    @Override
    public String getGuid() {
        return Guid;
    }

    @Override
    public String getFileName() {
        return FileName;
    }

    @Override
    public long getSrcType() {
        return 0;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public long getUpdateTime() {
        return 0;
    }

    @Override
    public String getRemarks() {
        return Remarks;
    }

    @Override
    public boolean isMonetCanBeDownloaded() {
        return false;
    }

    @Override
    public boolean isRecoveryNetworkAutoDownload() {
        return false;
    }


}
