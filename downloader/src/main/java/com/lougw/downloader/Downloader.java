package com.lougw.downloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.lougw.downloader.db.DownloadColumns;
import com.lougw.downloader.db.DownloadDataBaseIml;
import com.lougw.downloader.utils.DToastUtil;
import com.lougw.downloader.utils.DownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Downloader implements IDownloader {
    private static final String TAG = Downloader.class.getSimpleName();
    private static DownloadThreadPool mDownloadThreadPool;
    private static DownloadDataBaseIml mDownloadIml;
    private static Context mContext;
    private DownloadBuilder mDownloadBuilder;
    private ArrayList<DownloadObserver> mObservers = new ArrayList<>();

    private static class DownloaderHolder {
        private static final Downloader instance = new Downloader();
    }

    private Downloader() {
    }

    public static Downloader getInstance() {
        return DownloaderHolder.instance;
    }

    public void init(Context context, DownloadBuilder builder) {
        this.mContext = context;
        this.mDownloadBuilder = builder;
        mDownloadThreadPool = new DownloadThreadPool(context, builder.poolSize);
        mDownloadIml = new DownloadDataBaseIml(context);
        mDownloadThreadPool.setDownLoadDatabase(mDownloadIml);
        setDownLoadListener(new DownloadCallBack());

    }


    /**
     * 注册观察者
     */
    public void registerObserver(DownloadObserver observer) {
        synchronized (mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    /**
     * 反注册观察者
     */
    public void unRegisterObserver(DownloadObserver observer) {
        synchronized (mObservers) {
            if (mObservers.contains(observer)) {
                mObservers.remove(observer);
            }
        }
    }


    /**
     * 当下载进度发送改变的时候回调
     */
    public void notifyDownloadStateChanged(final DownloadRequest request) {
        synchronized (mObservers) {
            for (DownloadObserver observer : mObservers) {
                observer.onDownloadStateChanged(request);
            }
        }
    }


    public Context getContext() {
        return mContext;
    }


    public void setDownLoadListener(DownloadListener listener) {
        mDownloadIml.setDownloadListener(listener);
    }


    /**
     * @param info 下载的对象
     * @Description:下载任务
     */
    @Override
    public void download(DownloadInfo info) {
        DownloadRequest request = getRequestDownloadInfo(info);
        if (request != null) {
            enqueue(request);
        }
    }

    /**
     * 提交一个任务到线程池的任务队列 如果线程池中有空闲线程它将会立即开始任务
     * DownloadRequest对象 可能被修改. 例如当DownloadRequest开始时
     */
    @SuppressLint("UseSparseArrays")
    @Override
    public void enqueue(DownloadRequest request) {
        if (DownloadStatus.STATUS_NORMAL == request.getDownloadStatus())
            request.setDownloadStatus(DownloadStatus.STATUS_IDLE);
        if (mDownloadThreadPool.enqueue(request)) {
            if (request.getId() == -1) {
                request.setDownloadSize(0);
                long id = mDownloadIml.insert(request);
                request.setId(id);
            } else {
                mDownloadIml.update(request);
            }
        } else {
            if (DownloadUtils.isMemoryLow()) {
                DToastUtil.showMessage(R.string.download_no_space);
            }
        }
    }


    /**
     * @Description:暂停
     */
    @Override
    public void pause(DownloadInfo info) {
        DownloadRequest request = getRequestDownloadInfo(info);
        if (request != null) {
            DownloadStatus status = request.getDownloadStatus();
            if (status.equals(DownloadStatus.STATUS_IDLE)
                    || status.equals(DownloadStatus.STATUS_START)
                    || status.equals(DownloadStatus.STATUS_DOWNLOADING)
                    || status.equals(DownloadStatus.STATUS_PAUSE)) {
                request.setDownloadStatus(DownloadStatus.STATUS_PAUSE);
                mDownloadIml.update(request);
            }
            mDownloadThreadPool.onDequeue(request);
        }
    }

    /**
     * @Description:暂停
     */
    @Override
    public void pause(DownloadRequest request) {
        DownloadStatus status = request.getDownloadStatus();
        if (status.equals(DownloadStatus.STATUS_IDLE)
                || status.equals(DownloadStatus.STATUS_START)
                || status.equals(DownloadStatus.STATUS_DOWNLOADING)
                || status.equals(DownloadStatus.STATUS_PAUSE)) {
            request.setDownloadStatus(DownloadStatus.STATUS_PAUSE);
            mDownloadIml.update(request);
        }
        mDownloadThreadPool.onDequeue(request);
    }

    /**
     * @Description:恢复
     */
    @Override
    public void resume(DownloadRequest request) {
        DownloadStatus status = request.getDownloadStatus();
        if (status.equals(DownloadStatus.STATUS_PAUSE)
                || status.equals(DownloadStatus.STATUS_ERROR)) {
            request.setDownloadStatus(DownloadStatus.STATUS_NORMAL);
            enqueue(request);
        }
    }

    @Override
    public void delTask(DownloadRequest request) {
        pause(request);
        request.setDownloadStatus(DownloadStatus.STATUS_DELETE);
        mDownloadThreadPool.onDequeue(request);
        mDownloadIml.delete(request);
    }

    @Override
    public void delTaskAndFile(DownloadRequest request) {
        pause(request);
        request.setDownloadStatus(DownloadStatus.STATUS_DELETE);
        mDownloadThreadPool.onDequeue(request);
        mDownloadIml.delete(request);
        try {
            File file = new File(request.getDestUri());
            if (null != file && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description:清除所有任务
     */
    @Override
    public void clear() {
        ArrayList<DownloadRequest> requests = query(null, null, null);
        delete(requests);
    }

    public void delete(List<DownloadRequest> list) {
        for (DownloadRequest request : list) {
            delTaskAndFile(request);
        }
    }

    public void deleteRequest(DownloadRequest request) {
        mDownloadThreadPool.onDequeue(request);
    }

    @Override
    public DownloadRequest getRequestDownloadInfo(DownloadInfo info) {
        if (info == null) {
            return null;
        }
        String guid = info.getGuid();
        DownloadRequest request = getRequestByGuid(guid);
        if (request != null) {
            if (request.getDownloadStatus() == DownloadStatus.STATUS_COMPLETE) {
                try {
                    File file = new File(request.getDestUri());
                    if (null == file || !file.exists()) {
                        request.setDownloadStatus(DownloadStatus.STATUS_DELETE);
                        mDownloadThreadPool.onDequeue(request);
                        mDownloadIml.delete(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request.getDownloadStatus() == DownloadStatus.STATUS_ERROR) {
                request.setDownloadStatus(DownloadStatus.STATUS_NORMAL);
            }
        }
        String localDir;
        if (TextUtils.isEmpty(info.getLocalDir())) {
            localDir = mDownloadBuilder.localDir;
        } else {
            localDir = info.getLocalDir();
        }
        String desPath = DownloadUtils.getFileName(localDir, info.getFileName());
        String downLoadUrl = info.getDownLoadUrl();
        if (DownloadUtils.isFileExists(desPath)) {
            request = new DownloadRequest(downLoadUrl, desPath,
                    info);
            request.setDownloadStatus(DownloadStatus.STATUS_COMPLETE);
            return request;
        } else {
            desPath = desPath + DownloadUtils.SUFFIX;
            request = new DownloadRequest(downLoadUrl, desPath,
                    info);
            request.setDownloadStatus(DownloadStatus.STATUS_NORMAL);
            return request;
        }
    }


    public DownloadRequest getRequestByGuid(String guid) {

        DownloadRequest request = mDownloadThreadPool.getRequestByGuid(guid);
        if (request != null) {
            return request;
        }
        String selection = DownloadColumns.GUID + "='" + guid + "'";
        ArrayList<DownloadRequest> requests = query(selection, null, null);
        if (requests != null && requests.size() != 0) {
            return requests.get(0);
        }
        return null;
    }


    /**
     * @Description:查询下载完成的列表
     */
    public ArrayList<DownloadRequest> queryDownLoaded() {
        String selection = DownloadColumns.DOWNLOAD_STATUS + "='"
                + DownloadStatus.STATUS_COMPLETE + "'";
        return query(selection, null, null);
    }

    /**
     * @Description:查询正在下载的列表
     */
    public synchronized ArrayList<DownloadRequest> queryAllDownLoads() {
        return query(null, null, null);
    }


    private ArrayList<DownloadRequest> getDownLoadRequests(Cursor cursor) {
        ArrayList<DownloadRequest> resultList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor
                        .getColumnIndex(DownloadColumns._ID));
                DownloadRequest executingRequest = mDownloadThreadPool
                        .getDownloadRequest(id);
                if (executingRequest != null) {
                    resultList.add(executingRequest);
                } else {
                    DownloadRequest request = new DownloadRequest(cursor);
                    if (request.getDownloadStatus() == DownloadStatus.STATUS_START
                            || request.getDownloadStatus() == DownloadStatus.STATUS_DOWNLOADING
                            || request.getDownloadStatus() == DownloadStatus.STATUS_IDLE) {
                        request.setDownloadStatus(DownloadStatus.STATUS_PAUSE);
                    }
                    resultList.add(request);
                }
            } while (cursor.moveToNext());
        }
        return resultList;
    }


    public synchronized ArrayList<DownloadRequest> query(String selection,
                                                         String[] selectionArgs, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = mDownloadIml.query(null, selection, selectionArgs,
                    orderBy);
            return getDownLoadRequests(cursor);
        } catch (RuntimeException e) {
            return new ArrayList<DownloadRequest>();
        } finally {
            try {
                if (null != cursor) {
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
