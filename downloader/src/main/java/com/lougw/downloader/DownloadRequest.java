package com.lougw.downloader;

import android.content.ContentValues;
import android.database.Cursor;

import com.lougw.downloader.db.DownloadColumns;

import java.io.Serializable;

/**
 * 下载任务的模型，开始下载，准备下载，暂停下载和取消下载需提交一个DownloadRequest的实例
 * 可以通过下载地址和文件的保存路径构造一个下载任务模型，也可以通过数据库的实体对象来创建
 */
public class DownloadRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    // 如果 mId == -1 表示该下载任务是一个新建的下载任务并且需要被插入到数据库中以作为下载记录
    private long mId = -1;
    private String mGuid;
    private String mSrcUri;
    private String mDestUri;
    private long mTotalSize = 0;
    private long mDownloadSize = 0;
    private long mSpeed = 0;
    private DownloadStatus mDownloadStatus = DownloadStatus.STATUS_IDLE;
    private DownloadInfo downloadInfo;


    /**
     * 通过下载地址和文件的保存路径构造一个下载任务模型
     *
     * @param srcUri  下载地址
     * @param destUrl 存储路径
     */
    public DownloadRequest(String srcUri, String destUrl, DownloadInfo info) {
        mSrcUri = srcUri;
        mDestUri = destUrl;
        downloadInfo = info;
        mGuid = info.getGuid();
    }

    /**
     * 通过数据库实体构造一个下载任务模型
     *
     * @param cursor
     */
    public DownloadRequest(Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(DownloadColumns._ID));
        mGuid = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.GUID));
        long mSrcType = cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.SRC_TYPE));
        mSrcUri = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.SRC_URI));
        mDestUri = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.DEST_URI));
        mTotalSize = cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.TOTAL_SIZE));
        mDownloadSize = cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.DOWNLOAD_SIZE));
        mDownloadStatus = DownloadStatus.valueOf(cursor.getString(cursor
                .getColumnIndex(DownloadColumns.DOWNLOAD_STATUS)));
        long createTime = cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.CREATE_TIME)) == 0 ? System.currentTimeMillis() : cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.CREATE_TIME));
        long updateTime = cursor.getLong(cursor
                .getColumnIndex(DownloadColumns.UPDATE_TIME));
        String fileName = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.FILE_NAME));
        String localDir = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.LOCAL_DIR));
        String remarks = cursor.getString(cursor
                .getColumnIndex(DownloadColumns.REMARKS));
        boolean recoveryNetworkAutoDownload = cursor.getInt(cursor
                .getColumnIndex(DownloadColumns.RECOVERY_NETWORK_AUTO_DOWNLOAD)) == 0 ? false
                : true;
        int errorCode = cursor.getInt(cursor
                .getColumnIndex(DownloadColumns.ERROR_CODE));
        int retryTotal = cursor.getInt(cursor
                .getColumnIndex(DownloadColumns.RETRY_TOTAL));
        int retryCount = cursor.getInt(cursor
                .getColumnIndex(DownloadColumns.RETRY_COUNT));
        downloadInfo = new DownloadInfo.Builder(mSrcUri, fileName).localDir(localDir).Guid(mGuid).srcType(mSrcType).createTime(createTime).updateTime(updateTime).remarks(remarks).
                recoveryNetworkAutoDownload(recoveryNetworkAutoDownload).errorCode(errorCode).retryCount(retryCount).retryTotal(retryTotal).build();
    }

    /**
     * 通过DownloadRequest创建一个数据ContentValues对象 用来增删改查操作
     *
     * @return ContentValues
     */
    synchronized public ContentValues toContentValues() {
        ContentValues value = new ContentValues();
        if (mId != -1) {
            value.put(DownloadColumns._ID, mId);
        }
        value.put(DownloadColumns.GUID, mGuid);
        value.put(DownloadColumns.SRC_URI, mSrcUri);
        value.put(DownloadColumns.DEST_URI, mDestUri);
        value.put(DownloadColumns.TOTAL_SIZE, mTotalSize);
        value.put(DownloadColumns.DOWNLOAD_SIZE, mDownloadSize);
        value.put(DownloadColumns.DOWNLOAD_STATUS, mDownloadStatus.toString());
        value.put(DownloadColumns.UPDATE_TIME, System.currentTimeMillis());
        value.put(DownloadColumns.CREATE_TIME, downloadInfo.getCreateTime());
        value.put(DownloadColumns.REMARKS, downloadInfo.getRemarks());
        value.put(DownloadColumns.FILE_NAME, downloadInfo.getFileName());
        value.put(DownloadColumns.LOCAL_DIR, downloadInfo.getLocalDir());
        value.put(DownloadColumns.RECOVERY_NETWORK_AUTO_DOWNLOAD, downloadInfo.isRecoveryNetworkAutoDownload());
        value.put(DownloadColumns.ERROR_CODE, downloadInfo.getErrorCode());
        value.put(DownloadColumns.RETRY_COUNT, downloadInfo.getRetryCount());
        value.put(DownloadColumns.RETRY_TOTAL, downloadInfo.getRetryTotal());
        return value;
    }

    /**
     * 获取数据库主键
     *
     * @return 数据库主键
     */
    synchronized public long getId() {
        return mId;
    }

    /**
     * 设置数据库主键值
     *
     * @param id 主键
     * @return 主键值
     */
    synchronized long setId(long id) {
        return mId = id;
    }

    synchronized public String getSrcUri() {
        return mSrcUri;
    }

    synchronized String setSrcUri(String srcUri) {
        return mSrcUri = srcUri;
    }

    synchronized public String getDestUri() {
        return mDestUri;
    }

    synchronized String setDestUri(String destUri) {
        return mDestUri = destUri;
    }

    synchronized public long getTotalSize() {
        return mTotalSize;
    }

    synchronized public long setTotalSize(long size) {
        return mTotalSize = size;
    }

    synchronized public long getDownloadSize() {
        return mDownloadSize;
    }

    public int getProgress() {
        if (mTotalSize == 0) {
            return 0;
        }
        return (int) (mDownloadSize * 100 / mTotalSize);
    }

    synchronized long setDownloadSize(long size) {
        return mDownloadSize = size;
    }

    synchronized public DownloadStatus getDownloadStatus() {
        return mDownloadStatus;
    }

    synchronized public void setDownloadStatus(DownloadStatus status) {
        mDownloadStatus = status;
    }


    synchronized public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }


    synchronized public long getSpeed() {
        return mSpeed;
    }

    synchronized public void setSpeed(long mSpeed) {
        this.mSpeed = mSpeed;
    }


    @Override
    public boolean equals(Object o) {
        if (mGuid != null && null != o && o instanceof DownloadRequest) {
            if (mGuid.equals(
                    ((DownloadRequest) o).getGuid())) {
                return true;
            }
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[mId=").append(mId).append(", mSrcUri=").append(mSrcUri)
                .append(", mDestUri=").append(mDestUri).append(", mTotalSize=")
                .append(mTotalSize).append(", mDownloadSize=")
                .append(mDownloadSize).append(", mDownloadStatus=")
                .append(mDownloadStatus).append(",iteminfo").append("]");
        return sb.toString();
    }

    public String getGuid() {
        return mGuid;
    }

    public void setGuid(String mGuid) {
        this.mGuid = mGuid;
    }
}
